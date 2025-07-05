package com.ruoyi.inspect.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.basic.mapper.IfsInventoryQuantityMapper;
import com.ruoyi.basic.mapper.StandardProductListMapper;
import com.ruoyi.basic.mapper.StructureItemParameterMapper;
import com.ruoyi.basic.pojo.IfsInventoryQuantity;
import com.ruoyi.basic.pojo.StandardProductList;
import com.ruoyi.basic.pojo.StructureItemParameter;
import com.ruoyi.basic.pojo.StructureTestObject;
import com.ruoyi.common.constant.InsOrderTypeConstants;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.numgen.NumberGenerator;
import com.ruoyi.common.utils.*;
import com.ruoyi.common.utils.api.IfsApiUtils;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.inspect.dto.*;
import com.ruoyi.inspect.mapper.*;
import com.ruoyi.inspect.pojo.*;
import com.ruoyi.inspect.service.InsOrderService;
import com.ruoyi.inspect.service.InsOrderStateService;
import com.ruoyi.inspect.service.InsProductService;
import com.ruoyi.inspect.service.InsSampleService;
import com.ruoyi.inspect.vo.InsOrderPrintingVo;
import com.ruoyi.system.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author gaoaoy
 * @description 针对表【ins_order(检验下单)】的数据库操作Service实现
 * @createDate 2024-03-12 16:17:55
 */
@Service
@AllArgsConstructor
public class InsOrderServiceImpl extends ServiceImpl<InsOrderMapper, InsOrder>
        implements InsOrderService {

    private InsOrderMapper insOrderMapper;
    private InsSampleService insSampleService;
    private InsSampleMapper insSampleMapper;
    private InsProductService insProductService;
    private InsProductMapper insProductMapper;
    private InsSampleUserMapper insSampleUserMapper;
    private InsOrderStateService insOrderStateService;
    private UserMapper userMapper;
    private IfsInventoryQuantityMapper ifsInventoryQuantityMapper;
    private final NumberGenerator<InsOrder> numberGenerator;
    private InsReportMapper insReportMapper;
    private InsUnqualifiedRetestProductMapper insUnqualifiedRetestProductMapper;
    private IfsApiUtils ifsApiUtils;
    private SpotCheckQuarterItemMapper spotCheckQuarterItemMapper;
    private StandardProductListMapper standardProductListMapper;
    private StructureItemParameterMapper structureItemParameterMapper;



    //获取检验下单数据
    @Override
    public IPage<SampleOrderDto> selectInsOrderParameter(IPage<InsOrder> page, SampleOrderDto sampleOrderDto) {
        String laboratory = null;
        // 判断是否是全部
        String isOrderAll = null;
        if (sampleOrderDto.getState() != null && sampleOrderDto.getState() == -2) {
            isOrderAll = "1";
            sampleOrderDto.setState(null);
        }
        return insOrderMapper.selectInsOrderPage(page, QueryWrappers.queryWrappers(sampleOrderDto), laboratory, isOrderAll);
    }


    /**
     * 分配检验人
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int upInsOrder(Integer orderId, Integer sampleId, String appointed, Integer userId, String sonLaboratory) {
        InsOrder insOrder = new InsOrder();
        insOrder.setId(orderId);
        insOrder.setAppointed(StringUtils.isNotEmpty(appointed) ? LocalDate.parse(appointed) : null);
        insOrder.setSendTime(LocalDateTime.now());
        insOrderMapper.updateById(insOrder);
        List<InsSample> insSamples = insSampleMapper.selectList(Wrappers.<InsSample>lambdaQuery().eq(InsSample::getInsOrderId, orderId));
        List<Integer> ids = insSamples.stream().map(InsSample::getId).collect(Collectors.toList());
        List<InsProduct> insProducts = insProductMapper.selectList(Wrappers.<InsProduct>lambdaQuery()
                .in(InsProduct::getInsSampleId, ids)
                .eq(InsProduct::getState, 1)
                .select(InsProduct::getSonLaboratory).groupBy(InsProduct::getSonLaboratory));

        // 批量添加检验任务状态表
        List<InsOrderState> insOrderStateList = insProducts.stream().map(insProduct -> {
            InsOrderState insOrderState = new InsOrderState();
            insOrderState.setInsOrderId(orderId);
            try {
                insOrderState.setLaboratory(insProduct.getSonLaboratory());
            } catch (NullPointerException e) {
                throw new ErrorException("该检验单有未维护实验室的检验项目");
            }
            insOrderState.setInsState(0);
            return insOrderState;
        }).collect(Collectors.toList());
        insOrderStateService.saveBatch(insOrderStateList);

        if (userId != null) {
            InsSampleUser insSampleUser = new InsSampleUser();
            insSampleUser.setState(0);
            insSampleUser.setUserId(userId);
            insSampleUser.setInsSampleId(orderId);
            insSampleUser.setSonLaboratory(sonLaboratory);
            insSampleUserMapper.insert(insSampleUser);
        }

        // 判断订单有没有绑定抽样计划
        InsOrder order = insOrderMapper.selectById(orderId);
        if (order.getQuarterItemId() != null) {
           // 需要添加下发时间到抽样时间, 取样人员就是检测人
            SpotCheckQuarterItem spotCheckQuarterItem = spotCheckQuarterItemMapper.selectById(order.getQuarterItemId());
            spotCheckQuarterItem.setSpotCheckTime(order.getSendTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            User user = userMapper.selectById(userId);
            spotCheckQuarterItem.setSamplingUser(user.getName());
            spotCheckQuarterItemMapper.updateById(spotCheckQuarterItem);
        }

        return 1;
    }


    public static String getWeek(String dayStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(dayStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            return getWeekDay(dayOfWeek);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getWeekDay(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                return "周一";
            case Calendar.TUESDAY:
                return "周二";
            case Calendar.WEDNESDAY:
                return "周三";
            case Calendar.THURSDAY:
                return "周四";
            case Calendar.FRIDAY:
                return "周五";
            case Calendar.SATURDAY:
                return "周六";
            case Calendar.SUNDAY:
                return "周日";
            default:
                return "未知";
        }
    }


    // 如果你使用 Java 8+，也可以用 Stream API 简化代码：
    public List<StandardProductList> findMissingItemsWithStream(List<StandardProductList> standardList,
                                                                List<InsProduct> insList) {
        Set<Integer> insIds = insList.stream()
                .map(InsProduct::getStructureItemParameterId)
                .collect(Collectors.toSet());

        return standardList.stream()
                .filter(product -> !insIds.contains(product.getStructureItemParameterId()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addInsOrder(List<SampleProductDto> list, InsOrder insOrder) {
        // todo: 下单判断抽样计划的唯一性
        if (insOrder.getQuarterItemId() != null) {
            Long quarterItemCount = insOrderMapper.selectCount(Wrappers.<InsOrder>lambdaQuery()
                    .eq(InsOrder::getQuarterItemId, insOrder.getQuarterItemId())
                    .notIn(InsOrder::getState, -1 ,2 ,3));
            if (quarterItemCount > 0) {
                throw new ErrorException("该抽样计划已被绑定过");
            }
        }
        insOrder.setState(0);

        LocalDate appointed = insOrder.getAppointed();

        insOrderMapper.insert(insOrder); // 主表

        list.forEach(a -> {
            a.setId(null);
            a.setInsOrderId(insOrder.getId());
            insSampleMapper.insert(a);
            if (ObjectUtil.isNotEmpty(a.getInsProduct())) {
                // 判断是否填写待检项数量
                if (a.getQuantity() != null) {
                    List<InsProduct> ip2 = new ArrayList<>();
                    for (Integer i = 1; i <= a.getQuantity(); i++) {
                        // 重新拷贝 创建新对象
                        AtomicInteger atomicInteger = new AtomicInteger(i);
                        List<InsProduct> insProducts = a.getInsProduct().stream().map(insProduct -> {
                            insProduct.setRawMaterialTag(String.valueOf(atomicInteger.get()));
                            InsProduct product = new InsProduct();
                            BeanUtils.copyProperties(insProduct, product);
                            return product;
                        }).collect(Collectors.toList());

                        ip2.addAll(insProducts);
                    }
                    addInsProductMethod(a.getId(), ip2);
                } else {
                    addInsProductMethod(a.getId(), a.getInsProduct());
                }
            }
            // 判断是否有电缆配置
            if (ObjectUtil.isNotEmpty(a.getInsulating())) {
                // 判断是否有辅助线芯
                if (ObjectUtil.isNotEmpty(a.getAuxiliaryWireCore())) {
                    if (a.getAuxiliaryWireCore().getInsProduct().stream().filter(insProduct -> insProduct.getState() == 1).count() !=
                            a.getInsulating().getInsProduct().stream().filter(insProduct -> insProduct.getState() == 1).count()) {
                        throw new ErrorException("电缆配置辅助线芯检验项数量不统一, 请检查");
                    }
                }
                List<InsProduct> ip2 = new ArrayList<>();
                for (String s : a.getInsulating().getNum()) {
                    // 重新拷贝 创建新对象
                    List<InsProduct> insProducts = a.getInsulating().getInsProduct().stream().map(insProduct -> {
                        insProduct.setCableTag(s);
                        InsProduct product = new InsProduct();
                        BeanUtils.copyProperties(insProduct, product);
                        return product;
                    }).collect(Collectors.toList());
                    ip2.addAll(insProducts);
                }
                for (InsProduct product : ip2) {
                    product.setStandardMethodListId(a.getInsulating().getStandardMethodListId());
                }
                addInsProductMethod(a.getId(), ip2);
            }
            // 判断是否有辅助线芯
            if (ObjectUtil.isNotEmpty(a.getAuxiliaryWireCore())) {
                List<InsProduct> ip2 = new ArrayList<>();
                for (String s : a.getAuxiliaryWireCore().getNum()) {
                    // 重新拷贝 创建新对象
                    List<InsProduct> insProducts = a.getAuxiliaryWireCore().getInsProduct().stream().map(insProduct -> {
                        insProduct.setCableTag(s);
                        InsProduct product = new InsProduct();
                        BeanUtils.copyProperties(insProduct, product);
                        return product;
                    }).collect(Collectors.toList());
                    ip2.addAll(insProducts);
                }
                for (InsProduct product : ip2) {
                    product.setStandardMethodListId(a.getAuxiliaryWireCore().getStandardMethodListId());
                }
                addInsProductMethod(a.getId(), ip2);
            }
            // 子样品配置
            if (ObjectUtil.isNotEmpty(a.getChildSampleList())) {
                for (SampleProductDto b : a.getChildSampleList()) {
                    for (int i = 0; i < b.getNum(); i++) {
                        b.setId(null);
                        b.setInsOrderId(insOrder.getId());
                        b.setParentId(a.getId());
                        insSampleMapper.insert(b);
                        if (ObjectUtil.isNotEmpty(b.getInsProduct())) {
                            addInsProductMethod(b.getId(), b.getInsProduct());
                        }
                    }
                }
            }
        });
        //是否为原材料下单
        if (insOrder.getTypeSource() != null && insOrder.getTypeSource().equals(1)) {
            // 原材料下单: 委托人就是报检人, 生产单位就是供应商单位
            IfsInventoryQuantity ifsInventoryQuantity = ifsInventoryQuantityMapper.selectById(insOrder.getIfsInventoryId());
            Integer declareUserId = ifsInventoryQuantity.getDeclareUserId();
            User user = userMapper.selectById(declareUserId);
            if (user == null) {
               throw new ErrorException("缺少报检人信息");
            }
            // 供应商名称
            insOrder.setProduction(ifsInventoryQuantity.getSupplierName());
            insOrder.setProductionEn("");
            // 委托人名称
            insOrder.setPrepareUserId(user.getId());
            insOrder.setPrepareUser(user.getName());
            insOrder.setPrepareUserEn(user.getNameEn());
            insOrder.setPhone(user.getPhone());
            insOrder.setState(1);
            Long count1 = insOrderMapper.selectCount(Wrappers.<InsOrder>lambdaQuery()
                    .eq(InsOrder::getIfsInventoryId, insOrder.getIfsInventoryId())
                    .ne(InsOrder::getState, -1)
                    .eq(InsOrder::getOrderType, InsOrderTypeConstants.ENTER_THE_FACTORY)
                    .ne(InsOrder::getId, insOrder.getId()));
            // 判断之前是否有进厂检验
            if (count1 == 0) {
                ifsInventoryQuantityMapper.update(null, new LambdaUpdateWrapper<IfsInventoryQuantity>().set(IfsInventoryQuantity::getState, 1)
                        .eq(IfsInventoryQuantity::getId, insOrder.getIfsInventoryId()));
            }

            // 判断结束状态修改合格状态
            if (ifsInventoryQuantity.getIsFinish().equals(0)) {
                ifsInventoryQuantityMapper.update(null, Wrappers.<IfsInventoryQuantity>lambdaUpdate()
                        .eq(IfsInventoryQuantity::getId, insOrder.getIfsInventoryId())
                        .set(IfsInventoryQuantity::getInspectStatus, 0));
            }

            // 审核检验单
            upInsOrderOfState(insOrder);

            // 分配检验人
            upInsOrder(insOrder.getId(), null, appointed != null ? appointed.toString() : null, SecurityUtils.getUserId().intValue(), "原材料");

            // 根据零件号判断是否是辅材
            boolean isRaw = false;
            StructureTestObject productObject = insOrderMapper.selectProductByPartNo(ifsInventoryQuantity.getPartNo());
            // 查询产品
            if (productObject != null && StrUtil.isNotBlank(productObject.getObjectType()) && productObject.getObjectType().equals("1")) {
                isRaw = true;
            } else {
            // 查询对象
                StructureTestObject testObject = insOrderMapper.selectByPartNo(ifsInventoryQuantity.getPartNo());
                if (testObject != null && StrUtil.isNotBlank(testObject.getObjectType()) && testObject.getObjectType().equals("1")) {
                    isRaw = true;
                }
            }

            if (isRaw) {
                // 获取当前季度的开始时间和结束时间
                LocalDateTime now = LocalDateTime.now();
                // 获取当前月份
                int month = now.getMonthValue();
                // 确定当前季度的开始月份
                int startMonth = (month - 1) / 3 * 3 + 1;
                // 构造季度的开始时间
                LocalDateTime startOfQuarter = LocalDateTime.of(now.getYear(), Month.of(startMonth), 1, 0, 0);
                // 计算下一个季度的开始时间
                LocalDateTime startOfNextQuarter = startOfQuarter.plusMonths(3);
                // 计算当前季度的结束时间
                LocalDateTime endOfQuarter = startOfNextQuarter.minusSeconds(1);

                // 根据下单的规格型号判断是否为季度首次出现
                Integer count = ifsInventoryQuantityMapper.selectIsFirst(insOrder.getPartDetail(),
                        ifsInventoryQuantity.getSupplierName(),
                        startOfNextQuarter,
                        endOfQuarter);

                if(count == 0) {
                    ifsInventoryQuantity.setIsFirst(1);
                    ifsInventoryQuantityMapper.update(null, Wrappers.<IfsInventoryQuantity>lambdaUpdate()
                            .eq(IfsInventoryQuantity::getId, insOrder.getIfsInventoryId())
                            .set(IfsInventoryQuantity::getIsFirst, 1));
                }
            }
        }
        return insOrder.getId();
    }

    /**
     * 添加检验项
     * @param sampleId
     * @param productList
     */
    private void addInsProductMethod(Integer sampleId, List<InsProduct> productList) {
        for (InsProduct product : productList) {
            if (product.getState() == 1) {
                product.setId(null);
                product.setCreateTime(null);
                product.setCreateUser(null);
                product.setUpdateTime(null);
                product.setUpdateUser(null);
                product.setInsSampleId(sampleId);
                if (product.getInspectionItemSubclass() == null) {
                    product.setInspectionItemSubclass("");
                }
                if (StringUtils.isBlank(product.getAsk()) || StringUtils.isBlank(product.getTell())) {
                    throw new ErrorException("有检验项的要求值或要求描述为空, 请先去标准库配置要求值或要求描述");
                }
                if (StringUtils.isBlank(product.getSonLaboratory())) {
                    throw new ErrorException("有检验项的的子实验室为绑定, 请先绑定子实验室");
                }
                insProductMapper.insert(product);
            }
        }
    }


    @Override
    public Map<String, Object> getInsOrder(Integer id) {
        Map<String, Object> map = new HashMap<>();
        InsOrder insOrder = insOrderMapper.selectById(id);
        List<SampleProductDto> list;
        // 判断是否是进厂报告免检
        if (insOrder.getIsExemption().equals(1)) {
            list = insSampleMapper.selectExemptionByOrderId(id);
        } else {
            list = insSampleMapper.selectSampleProductListByOrderId2(id);
        }
        Map<String, Object> map1 = insSampleMapper.selectInsOrder(id);
        map.put("insOrder", insOrder);
        map.put("sampleProduct", list);
        map.put("insOrderTemplate", map1);
        return map;
    }

    /**
     * 审核检验单
     * @param insOrder
     * @return
     */
    @Override
    public int upInsOrderOfState(InsOrder insOrder) {
        insOrder.setExamineTime(LocalDateTime.now());
        if (insOrder.getState() == 1) {
            //审核通过才会生成委托编号
            // todo: 检验类型编号
            InsOrder order = this.getById(insOrder.getId());
            String code = "";
            switch (order.getOrderType()) {
                case InsOrderTypeConstants.SPOT_CHECK:
                    code = "C";
                    break;
                case InsOrderTypeConstants.CUSTOMER_ORDERED:
                    code = "W";
                    break;
                case InsOrderTypeConstants.ENTER_THE_FACTORY:
                case InsOrderTypeConstants.QUARTERLY_TEST:
                    code = "Y";
                    break;
            }
            // 生成编号
            String no = numberGenerator.generateNumberWithPrefix(3,
                    "JCZX/ZB-" + code + LimsDateUtil.resetDate(LocalDateTime.now()),
                    InsOrder::getEntrustCode);
            // 判断是否是季度检验, 是季度检验取消原材料季度检验下单
            if (InsOrderTypeConstants.QUARTERLY_TEST.equals(order.getOrderType())) {
                ifsInventoryQuantityMapper.update(null,  Wrappers.<IfsInventoryQuantity>lambdaUpdate()
                        .eq(IfsInventoryQuantity::getId, order.getIfsInventoryId())
                        .set(IfsInventoryQuantity::getIsQuarter, 0));

                // 查询是否有过进厂检验, 有获取里面的编号
                InsOrder order1 = insOrderMapper.selectOne(Wrappers.<InsOrder>lambdaQuery()
                        .eq(InsOrder::getIfsInventoryId, order.getIfsInventoryId())
                        .ne(InsOrder::getState, -1)
                        .eq(InsOrder::getOrderType, InsOrderTypeConstants.ENTER_THE_FACTORY));
                if (order1 != null) {
                  no = order1.getEntrustCode();
                }
            }

            int count = 1;
            // 查询样品表
            List<InsSample> insSamples = insSampleMapper.selectList(Wrappers.<InsSample>lambdaQuery()
                    .eq(InsSample::getInsOrderId, insOrder.getId()));
            for (InsSample insSample : insSamples) {
                if (StringUtils.isBlank(insSample.getSampleCode())) {
                    // 如果只有一个样品就不需要拼接数字
                    if (insSamples.size() != 1) {
                        insSample.setSampleCode(no + "-" + count);
                        count++;
                    } else {
                        insSample.setSampleCode(no);
                    }
                }
            }
            insSampleService.updateBatchById(insSamples);

            insOrder.setEntrustCode(no);
        }
        return insOrderMapper.updateById(insOrder);
    }

    @Override
    public Map<String, Object> getInsOrderAndSample(Integer id, String laboratory) {
        Map<String, Object> map = new HashMap<>();
        InsOrder insOrder = insOrderMapper.selectById(id);
        List<SampleProductDto> list = insSampleMapper.getInsOrderAndSample(id, laboratory);
        map.put("insOrder", insOrder);
        map.put("sampleProduct", list);
        //查询所有记录模版去重
        List<Map<Integer, Object>> list2 = insOrderMapper.selectReportModelByOrderId(id, laboratory);
        map.put("reportModel", list2);
        return map;
    }

    @Override
    public IPage<SampleProductDto2> selectSampleAndProductByOrderId(IPage<SampleProductDto2> page, SampleProductDto2 sampleProductDto) {
        IPage<SampleProductDto2> productDto2IPage = insOrderMapper.selectSampleAndProductByOrderId(page,
                QueryWrappers.queryWrappers(sampleProductDto)
                        .orderByAsc("sample_code")
                        .orderByAsc("cable_tag")
                        .orderByAsc("sort"),
                sampleProductDto.getId());
        return productDto2IPage;
    }


    @Override
    public int updateStatus(Integer id) {
        return insOrderMapper.updateStatus(id);
    }


    /**
     * 获取ifs库存信息
     * @param
     * @return
     */
    @Override
    public void getIfsOrder() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("LOCATION_NO","1302");
        map.put("STATE_DB","To be Inspected");
        List<Map<String, Object>> inventory = ifsApiUtils.getInventory(JSONUtil.toJsonStr(map));
        if(inventory.size() == 0) {
            return;
        }
        // 进行保存
        for (Map<String, Object> map1 : inventory) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 时间格式化
            IfsInventoryQuantity ifsInventoryQuantity = new IfsInventoryQuantity();
            ifsInventoryQuantity.setContract(map1.get("CONTRACT") == null ? "" : map1.get("CONTRACT").toString()); // 域
            ifsInventoryQuantity.setPartNo(map1.get("PART_NO") == null ? "" : map1.get("PART_NO").toString() ); // 零件号
            ifsInventoryQuantity.setPartDesc(map1.get("PART_DESC") == null ? "" : map1.get("PART_DESC").toString()); // 零件描述
            ifsInventoryQuantity.setOrderNo(map1.get("ORDER_NO") == null ? "" : map1.get("ORDER_NO").toString()); // 订单号
            ifsInventoryQuantity.setLineNo(map1.get("LINE_NO") == null ? "" : map1.get("LINE_NO").toString()); // 行号
            ifsInventoryQuantity.setReleaseNo(map1.get("RELEASE_NO") == null ? "" : map1.get("RELEASE_NO").toString()); // 下达号
            ifsInventoryQuantity.setReceiptNo(Integer.parseInt(map1.get("RECEIPT_NO") == null ? "" : map1.get("RECEIPT_NO").toString())); // 接收号
            ifsInventoryQuantity.setStatus(map1.get("STATE") == null ? "" : map1.get("STATE").toString()); // 状态描述
            ifsInventoryQuantity.setStatusDb(map1.get("STATE_DB") == null ? "" : map1.get("STATE_DB").toString()); // 状态
            if(map1.get("QTY_ARRIVED") != null) {
                ifsInventoryQuantity.setQtyArrived(new BigDecimal(map1.get("QTY_ARRIVED").toString())); // 抵达的采购数量
            }
            if(map1.get("QTY_INSPECTED") != null) {
                ifsInventoryQuantity.setQtyInspected(new BigDecimal(map1.get("QTY_INSPECTED").toString())); // 已检验的购买数量
            }
            if(map1.get("QTY_TO_INSPECT") != null) {
                ifsInventoryQuantity.setQtyToInspect(new BigDecimal(map1.get("QTY_TO_INSPECT").toString())); // 要检验的采购数量
            }
            if(map1.get("INV_QTY_IN_STORE") != null) {
                ifsInventoryQuantity.setInvQtyInStore(new BigDecimal(map1.get("INV_QTY_IN_STORE").toString())); // 抵达的库存数量
            }
            if(map1.get("PUR_QTY_IN_STORE") != null) {
                ifsInventoryQuantity.setPurQtyInStore(new BigDecimal( map1.get("PUR_QTY_IN_STORE").toString())); // 抵达的采购数量

            }
            ifsInventoryQuantity.setSupplierId(map1.get("SUPPLIER_ID") == null ? "" : map1.get("SUPPLIER_ID").toString()); // 供应商ID
            ifsInventoryQuantity.setSupplierName(map1.get("SUPPLIER_NAME") == null ? "" : map1.get("SUPPLIER_NAME").toString()); // 供应商名称
            ifsInventoryQuantity.setConfigurationId(map1.get("CONFIGURATION_ID") == null ? "" : map1.get("CONFIGURATION_ID").toString()); // 配置标识
            ifsInventoryQuantity.setLotBatchNo(map1.get("LOT_BATCH_NO") == null ? "" : map1.get("LOT_BATCH_NO").toString()); // 批次号
            ifsInventoryQuantity.setUpdateBatchNo(map1.get("LOT_BATCH_NO") == null ? "" : map1.get("LOT_BATCH_NO").toString()); // 批次号
            ifsInventoryQuantity.setWaivDevRejNo(map1.get("WAIV_DEV_REJ_NO") == null ? "" : map1.get("WAIV_DEV_REJ_NO").toString()); // WDR号
            ifsInventoryQuantity.setActivitySeq(map1.get("ACTIVITY_SEQ") == null ? null : Integer.parseInt(map1.get("ACTIVITY_SEQ").toString())); // 活动序列
            ifsInventoryQuantity.setSerialNo(map1.get("SERIAL_NO") == null ? "" : map1.get("SERIAL_NO").toString()); // 序列号
            ifsInventoryQuantity.setLocationNo(map1.get("LOCATION_NO") == null ? "" : map1.get("LOCATION_NO").toString()); // 库位号
            ifsInventoryQuantity.setEngChgLevel(map1.get("ENG_CHG_LEVEL") == null ? "" : map1.get("ENG_CHG_LEVEL").toString()); // 版本号
            ifsInventoryQuantity.setReceiver(map1.get("RECEIVER") == null ? "" : map1.get("RECEIVER").toString()); // 接收人
            ifsInventoryQuantity.setReceiverName(map1.get("RECEIVER_NAME") == null ? "" : map1.get("RECEIVER_NAME").toString()); // 接收人名称
            ifsInventoryQuantity.setBuyerCode(map1.get("BUYER_CODE") == null ? "" : map1.get("BUYER_CODE").toString()); // 采购员
            ifsInventoryQuantity.setBuyerName(map1.get("BUYER_NAME") == null ? "" : map1.get("BUYER_NAME").toString()); // 采购员名称

            if(map1.get("ARRIVE_DATE") != null) {
                ifsInventoryQuantity.setArriveDate(LocalDateTime.parse(map1.get("ARRIVE_DATE").toString(),dateTimeFormatter)); // 实际到货日期
            }
            if(map1.get("DELIVERY_DATE") != null) {
                ifsInventoryQuantity.setDeliveryDate(LocalDateTime.parse(map1.get("DELIVERY_DATE").toString(),dateTimeFormatter)); // 实际交货日期
            }
            if(map1.get("PRODUCT_DATE") != null) {
                ifsInventoryQuantity.setProductDate(LocalDateTime.parse(map1.get("PRODUCT_DATE").toString(),dateTimeFormatter)); // 生产日期

            }
            if(map1.get("INVALID_DATE") != null) {
                ifsInventoryQuantity.setInvalidDate(LocalDateTime.parse(map1.get("INVALID_DATE").toString(),dateTimeFormatter)); // 失效日期
            }
            if(map1.get("APPROVED_DATE") != null) {
                ifsInventoryQuantity.setApprovedDate(LocalDateTime.parse(map1.get("APPROVED_DATE").toString(),dateTimeFormatter)); // 审批日期
            }
            ifsInventoryQuantity.setReqCeater(map1.get("REQ_CEATER") == null ? "" : map1.get("REQ_CEATER").toString()); // 采购申请创建人
            ifsInventoryQuantity.setReqCeaterName(map1.get("REQ_CEATER_NAME") == null ? "" : map1.get("REQ_CEATER_NAME").toString()); // 采购申请创建人名称
            ifsInventoryQuantity.setLineRemarks(map1.get("LINE_REMARKS") == null ? "" : map1.get("LINE_REMARKS").toString()); // 采购订单行备注
            ifsInventoryQuantity.setBuyUnitMeas(map1.get("BUY_UNIT_MEAS") == null ? "" : map1.get("BUY_UNIT_MEAS").toString()); // 采购单位
            ifsInventoryQuantity.setReceiverDate(LocalDateTime.now()); // 接收日期
            ifsInventoryQuantity.setIsSource(1);
            ifsInventoryQuantity.setState(0);

            Long count = ifsInventoryQuantityMapper.selectCount(new LambdaQueryWrapper<IfsInventoryQuantity>()
                    .eq(IfsInventoryQuantity::getOrderNo, ifsInventoryQuantity.getOrderNo())
                    .eq(IfsInventoryQuantity::getLineNo, ifsInventoryQuantity.getLineNo())
                    .eq(IfsInventoryQuantity::getReleaseNo, ifsInventoryQuantity.getReleaseNo())
                    .eq(IfsInventoryQuantity::getReceiptNo, ifsInventoryQuantity.getReceiptNo())
                    .eq(IfsInventoryQuantity::getLocationNo, ifsInventoryQuantity.getLocationNo())
                    .eq(IfsInventoryQuantity::getLotBatchNo, ifsInventoryQuantity.getLotBatchNo())
                    .eq(IfsInventoryQuantity::getSerialNo, ifsInventoryQuantity.getSerialNo())
                    .eq(IfsInventoryQuantity::getEngChgLevel, ifsInventoryQuantity.getEngChgLevel())
                    .eq(IfsInventoryQuantity::getWaivDevRejNo, ifsInventoryQuantity.getWaivDevRejNo())
                    .eq(IfsInventoryQuantity::getActivitySeq, ifsInventoryQuantity.getActivitySeq())
            );
            if(count == 0) {
                ifsInventoryQuantity.setIsFirst(0);
                // 查询产业链检测数据
                String industryChainAttrFields = IndustryChainUtils.getIndustryChainAttrFields(ifsInventoryQuantity.getOrderNo(),
                        ifsInventoryQuantity.getLineNo(),
                        ifsInventoryQuantity.getReleaseNo());
                ifsInventoryQuantity.setIndustryChain(industryChainAttrFields);

                ifsInventoryQuantityMapper.insert(ifsInventoryQuantity);
            }
        }
    }

    /**
     * id是原材料的id
     *
     * 修改订单单号
     * @param insOrder
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEntrustCode(InsOrder insOrder) {
        // 判断当前订单是否生成了报告, 生成了报告不能修改单号
        List<InsOrder> insOrders = insOrderMapper.selectList(Wrappers.<InsOrder>lambdaQuery()
                .eq(InsOrder::getIfsInventoryId, insOrder.getId())
                .ne(InsOrder::getState, -1));
        List<Integer> insOrderIds = insOrders.stream().map(InsOrder::getId).collect(Collectors.toList());
        Long count = insReportMapper.selectCount(Wrappers.<InsReport>lambdaQuery()
                .in(InsReport::getInsOrderId, insOrderIds));
        if (count > 0 ) {
            throw new ErrorException("当前订单已经生成了报告不能修改编号");
        }


        Long count1 = insOrderMapper.selectCount(Wrappers.<InsOrder>lambdaQuery()
                .eq(InsOrder::getEntrustCode, insOrder.getEntrustCode())
                .ne(InsOrder::getIfsInventoryId, insOrder.getId())
                .ne(InsOrder::getInsState, -1));
        if (count1 > 0) {
            throw new ErrorException("该编号重复");
        }

        //修改报告的编号
        insOrderMapper.update(null, Wrappers.<InsOrder>lambdaUpdate()
                .eq(InsOrder::getIfsInventoryId, insOrder.getId())
                .set(InsOrder::getEntrustCode, insOrder.getEntrustCode()));

        // 修改样品的编号
        // 查询所有的样品
        List<InsSample> insSamples = insSampleMapper.selectList(Wrappers.<InsSample>lambdaQuery()
                .in(InsSample::getInsOrderId, insOrderIds));
        for (InsSample insSample : insSamples) {
            String sampleCode = insSample.getSampleCode();
            String updateCode = insOrder.getEntrustCode();
            // 查找最后一个'-'的位置
            int lastDashIndex = sampleCode.lastIndexOf('-');
            if (lastDashIndex != -1) {
                int secondLastDashIndex = sampleCode.lastIndexOf('-', lastDashIndex - 1);
                // 处理最后一个'-'前的部分
                if (secondLastDashIndex != -1) {
                    // 处理最后一个'-'及之后的部分
                    String afterLastDash = sampleCode.substring(lastDashIndex);

                    updateCode = updateCode + afterLastDash;
                }
            }
            insSampleMapper.update(null, Wrappers.<InsSample>lambdaUpdate()
                    .eq(InsSample::getId, insSample.getId())
                    .set(InsSample::getSampleCode, updateCode));

        }
    }

    @Override
    public List<InsUnqualifiedRetestProduct> getRetestResult(Integer insProductId) {
        return insUnqualifiedRetestProductMapper.selectList(Wrappers.<InsUnqualifiedRetestProduct>lambdaQuery()
                .eq(InsUnqualifiedRetestProduct::getInsProductId, insProductId));
    }

    /**
     * 修改采购订单接收状态, 避免回滚
     * @param id
     */
    @Transactional
    public void updateIfsInventoryQuantity(Integer id) {
        ifsInventoryQuantityMapper.update(null, Wrappers.<IfsInventoryQuantity>lambdaUpdate()
                .set(IfsInventoryQuantity::getIsRegister, 1)
                .eq(IfsInventoryQuantity::getId, id));
    }

    /**
     * 新增铜单丝下单
     * @param list
     * @param insOrder
     * @return
     */
    @Override
    public int addRawCopperOrder(List<SampleProductDto> list, CopperInsOrderDto insOrder) {
        insOrder.setState(1);
        insOrder.setTypeSource(1);

        LocalDate appointed = insOrder.getAppointed();

        insOrderMapper.insert(insOrder); // 主表

        list.forEach(a -> {
            a.setId(null);
            a.setInsOrderId(insOrder.getId());
            insSampleMapper.insert(a);
            if (ObjectUtil.isNotEmpty(a.getInsProduct())) {
                // 判断是否填写待检项数量
                if (a.getQuantity() != null) {
                    List<InsProduct> ip2 = new ArrayList<>();
                    for (Integer i = 1; i <= a.getQuantity(); i++) {
                        // 重新拷贝 创建新对象
                        AtomicInteger atomicInteger = new AtomicInteger(i);
                        List<InsProduct> insProducts = a.getInsProduct().stream().map(insProduct -> {
                            insProduct.setRawMaterialTag(String.valueOf(atomicInteger.get()));
                            InsProduct product = new InsProduct();
                            BeanUtils.copyProperties(insProduct, product);
                            return product;
                        }).collect(Collectors.toList());

                        ip2.addAll(insProducts);
                    }
                    addInsProductMethod(a.getId(), ip2);
                } else {
                    addInsProductMethod(a.getId(), a.getInsProduct());
                }
            }
            if (ObjectUtil.isNotEmpty(a.getChildSampleList())) {
                for (SampleProductDto b : a.getChildSampleList()) {
                    for (int i = 0; i < b.getNum(); i++) {
                        b.setId(null);
                        b.setInsOrderId(insOrder.getId());
                        b.setParentId(a.getId());
                        insSampleMapper.insert(b);
                        if (ObjectUtil.isNotEmpty(b.getInsProduct())) {
                            addInsProductMethod(b.getId(), b.getInsProduct());
                        }
                    }
                }
            }
        });

        // 添加原材料信息
        IfsInventoryQuantity ifsInventoryQuantity = new IfsInventoryQuantity();
        // 基本信息
        ifsInventoryQuantity.setIsSource(0);
        ifsInventoryQuantity.setIsInspect(1);
        ifsInventoryQuantity.setState(1);
        ifsInventoryQuantity.setIsFinish(0);
        ifsInventoryQuantity.setIsCopper(1);
        ifsInventoryQuantity.setIsQuarter(0);
        ifsInventoryQuantity.setInspectStatus(0);

        ifsInventoryQuantity.setQtyArrived(insOrder.getQtyArrived());
        ifsInventoryQuantity.setBuyUnitMeas(insOrder.getBuyUnitMeas());
        ifsInventoryQuantity.setSupplierName(insOrder.getSupplierName());
        ifsInventoryQuantity.setUpdateBatchNo(insOrder.getUpdateBatchNo());
        ifsInventoryQuantity.setDeclareDate(insOrder.getDeclareDate());

        ifsInventoryQuantityMapper.insert(ifsInventoryQuantity);


        insOrder.setIfsInventoryId(ifsInventoryQuantity.getId());
        insOrder.setState(1);


        upInsOrderOfState(insOrder);
        upInsOrder(insOrder.getId(), null, appointed != null ? appointed.toString() : null, SecurityUtils.getUserId().intValue(), "原材料");

        return insOrder.getId();
    }

    /**
     * 修改委托下单编号
     * @param insOrder
     */
    @Override
    public void updateOrderEntrustCode(InsOrder insOrder) {
        // 判断当前订单是否生成了报告, 生成了报告不能修改单号
        Long count = insReportMapper.selectCount(Wrappers.<InsReport>lambdaQuery()
                .eq(InsReport::getInsOrderId, insOrder.getId()));
        if (count > 0 ) {
            throw new ErrorException("当前订单已经生成了报告不能修改编号");
        }

        Long count1 = insOrderMapper.selectCount(Wrappers.<InsOrder>lambdaQuery()
                .eq(InsOrder::getEntrustCode, insOrder.getEntrustCode()));
        if (count1 > 0) {
            throw new ErrorException("该编号重复");
        }

        //修改报告的编号
        insOrderMapper.update(null, Wrappers.<InsOrder>lambdaUpdate()
                .eq(InsOrder::getId, insOrder.getId())
                .set(InsOrder::getEntrustCode, insOrder.getEntrustCode()));

        // 修改样品的编号
        // 查询所有的样品
        List<InsSample> insSamples = insSampleMapper.selectList(Wrappers.<InsSample>lambdaQuery()
                .eq(InsSample::getInsOrderId, insOrder.getId()));
        for (InsSample insSample : insSamples) {
            String sampleCode = insSample.getSampleCode();
            String updateCode = insOrder.getEntrustCode();
            // 查找最后一个'-'的位置
            int lastDashIndex = sampleCode.lastIndexOf('-');
            if (lastDashIndex != -1) {
                int secondLastDashIndex = sampleCode.lastIndexOf('-', lastDashIndex - 1);
                // 处理最后一个'-'前的部分
                if (secondLastDashIndex != -1) {
                    // 处理最后一个'-'及之后的部分
                    String afterLastDash = sampleCode.substring(lastDashIndex);

                    updateCode = updateCode + afterLastDash;
                }
            }
            insSampleMapper.update(null, Wrappers.<InsSample>lambdaUpdate()
                    .eq(InsSample::getId, insSample.getId())
                    .set(InsSample::getSampleCode, updateCode));

        }
    }

    /**
     * 修改检验下单内容
     * @param insOrderUpdateDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateInsOrder(InsOrderUpdateDto insOrderUpdateDto) {
        // 修改订单
        insOrderUpdateDto.getInsOrder().setState(0);
        insOrderUpdateDto.getInsOrder().setTell("");
        insOrderMapper.updateById(insOrderUpdateDto.getInsOrder());

        // 修改检验项
        for (SampleProductDto sampleProductDto : insOrderUpdateDto.getSampleProduct()) {
            insSampleService.update(Wrappers.<InsSample>lambdaUpdate()
                    .eq(InsSample::getId, sampleProductDto.getId())
                    .set(InsSample::getSpecialStandardMethod, sampleProductDto.getSpecialStandardMethod()));

            insProductService.updateBatchById(sampleProductDto.getInsProduct());
        }

        return true;
    }

    /**
     * 成品标签打印
     * @param ids
     * @return
     */
    @Override
    public List<InsOrderPrintingVo> labelOrderPrinting(List<Integer> ids) {
        return insOrderMapper.labelOrderPrinting(ids);
    }

    /**
     * 根据样品id查询检验项树
     * @param insSampleId
     * @return
     */
    @Override
    public List<StandardProductList> getProductTreeBySampleId(Integer insSampleId) {
        // 查询第一个检验项获取检验项树
        InsProduct insProduct = insProductMapper.selectOne(Wrappers.<InsProduct>lambdaQuery()
                .eq(InsProduct::getInsSampleId, insSampleId)
                .last("limit 1"));
        String tree = insProduct.getFactory() + " - " +
                insProduct.getLaboratory() + " - " +
                insProduct.getSampleType() + " - " +
                insProduct.getSample() + " - " +
                insProduct.getModel();
        // 查询标准树
        List<StandardProductList> standardProductLists = standardProductListMapper.selectList(Wrappers.<StandardProductList>lambdaQuery()
                .eq(StandardProductList::getStandardMethodListId, insProduct.getStandardMethodListId())
                .eq(StandardProductList::getTree, tree)
                .orderByAsc(StandardProductList::getSort));
        for (StandardProductList standardProductList : standardProductLists) {
            standardProductList.setId(null);
        }

        return standardProductLists;
    }

    /**
     * 添加遗漏的检验项
     * @param omitOrderProductDto
     * @return
     */
    @Override
    public boolean addOmitOrderProduct(OmitOrderProductDto omitOrderProductDto) {
        if (omitOrderProductDto.getInsSampleId() == null) {
            throw new ErrorException("缺少样品Id");
        }
        for (InsProduct product : omitOrderProductDto.getInsProductBindingList()) {
            if (product.getState() == 1) {
                product.setId(null);
                product.setCreateTime(null);
                product.setCreateUser(null);
                product.setUpdateTime(null);
                product.setUpdateUser(null);
                product.setSection(null);
                product.setInsSampleId(omitOrderProductDto.getInsSampleId());
                if (StringUtils.isBlank(product.getCableTag())) {
                    product.setCableTag(null);
                }
                if (product.getInspectionItemSubclass() == null) {
                    product.setInspectionItemSubclass("");
                }
                if (StringUtils.isBlank(product.getAsk()) || StringUtils.isBlank(product.getTell())) {
                    throw new ErrorException("有检验项的要求值或要求描述为空, 请填写要求值或要求描述");
                }
                insProductMapper.insert(product);
            }
        }

        return true;
    }

    /**
     * 成品检验单导出
     * @param sampleOrderDto
     * @param response
     */
    @Override
    public void rawAllInsOrderExport(SampleOrderDto sampleOrderDto, HttpServletResponse response) {
        List<SampleOrderDto> sampleOrderDtoList = new ArrayList<>();
        if (StringUtils.isNotBlank(sampleOrderDto.getIds())) {
            List<String> orderIds = StrUtil.split(sampleOrderDto.getIds(), ",");
            sampleOrderDtoList = insOrderMapper.getInsOrderExportByIds(orderIds);
        } else {
            String laboratory = null;
            // 判断是否是全部
            String isOrderAll = null;
            if (sampleOrderDto.getState() != null && sampleOrderDto.getState() == -2) {
                isOrderAll = "1";
                sampleOrderDto.setState(null);
            }
            sampleOrderDto.setIds(null);
            sampleOrderDtoList = insOrderMapper.rawAllInsOrderExport(QueryWrappers.queryWrappers(sampleOrderDto), laboratory, isOrderAll);
        }

        // 判断是否是不合格, 不合格查询不合格项
        for (SampleOrderDto orderDto : sampleOrderDtoList) {
            if (orderDto.getInsResult() != null && orderDto.getInsResult() == 0){
                // 查询不合格项
                List<String> unqualifiedList = insProductMapper.selectUnqualifiedList(orderDto.getId());
                orderDto.setUnqualifiedItem(CollUtil.join(unqualifiedList, ","));
            }
            orderDto.setCreateTimeString(orderDto.getCreateTime() == null ? "" : orderDto.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        }

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        try {
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("委托检测信息导出", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            //新建ExcelWriter
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build();
            //获取sheet0对象
            WriteSheet mainSheet = EasyExcel.writerSheet(0, "委托检测信息导出").head(SampleOrderDto.class).build();

            //向sheet0写入数据 传入空list这样只导出表头
            excelWriter.write(sampleOrderDtoList, mainSheet);
            //关闭流
            excelWriter.finish();
        } catch (IOException e) {
            throw new RuntimeException("导出失败");
        }
    }

    /**
     * 修改样品型号
     * @param insSample
     */
    @Override
    public void updateSampleModel(InsSample insSample) {
        // 判断当前订单是否生成了报告, 生成了报告不能修改单号
        Long count = insReportMapper.selectCount(Wrappers.<InsReport>lambdaQuery()
                .eq(InsReport::getInsOrderId, insSample.getInsOrderId()));
        if (count > 0 ) {
            throw new ErrorException("当前订单已经生成了报告不能修改编号");
        }

        insSampleService.update(Wrappers.<InsSample>lambdaUpdate()
                .eq(InsSample::getId, insSample.getId())
                .set(InsSample::getModel, insSample.getModel()));
    }

    /**
     * 查询当前时间是否没有该检测项的抽样计划
     * @param sampleList
     * @param insOrder
     * @return
     */
    @Override
    public Result judgeNotSpotCheckOrder(List<SampleProductDto> sampleList, InsOrder insOrder) {
        // todo: 下单判断抽样计划的唯一性
        if (insOrder.getQuarterItemId() != null) {
            Long quarterItemCount = insOrderMapper.selectCount(Wrappers.<InsOrder>lambdaQuery()
                    .eq(InsOrder::getQuarterItemId, insOrder.getQuarterItemId())
                    .notIn(InsOrder::getState, -1 ,2 ,3));
            if (quarterItemCount > 0) {
                throw new ErrorException("该抽样计划已被绑定过");
            }
        }

        // 判断是否是抽样检测
        if (insOrder.getOrderType().equals(InsOrderTypeConstants.SPOT_CHECK)) {
            Set<String> monthMessageSet = new HashSet<>();
            Set<String> quarterMessageSet = new HashSet<>();
            Set<String> yearMessageSet = new HashSet<>();

            String formatTime = "yyyy-MM-dd HH:mm:ss";
            // 查询当前所有样品的检验项
            for (SampleProductDto sampleProductDto : sampleList) {
                List<StandardProductList> standardProductList = getProductTreeBySampleId(sampleProductDto.getId());

                Set<Integer> insIds = sampleProductDto.getInsProduct().stream()
                        .map(InsProduct::getStructureItemParameterId)
                        .collect(Collectors.toSet());

                List<StandardProductList> productLists = standardProductList.stream()
                        .filter(product -> !insIds.contains(product.getStructureItemParameterId()))
                        .collect(Collectors.toList());

                // 查询检验项基础表, 查询是否有月度, 季度, 年度的检验项
                List<StructureItemParameter> itemParameterList = structureItemParameterMapper.selectList(Wrappers.<StructureItemParameter>lambdaQuery()
                        .in(StructureItemParameter::getId, productLists.stream().map(StandardProductList::getStructureItemParameterId).collect(Collectors.toList())));

                for (StructureItemParameter item : itemParameterList) {
                    if (StringUtils.isNotBlank(item.getSpotCheckType())) {
                        switch (item.getSpotCheckType()) {
                            case "1": // 月度
                                // 查询当月是否有该检测项的抽样计划
                                // 当月开始时间
                                DateTime monthStart = DateUtil.beginOfMonth(DateUtil.date());
                                String monthStartTime = monthStart.toString(formatTime);
                                // 当月结束时间
                                DateTime monthEnd = DateUtil.endOfMonth(DateUtil.date());
                                String monthEndTime = monthEnd.toString(formatTime);
                                Integer count = insOrderMapper.selectNotSpotCheckOrder(item.getId(), monthStartTime, monthEndTime);
                                if (count == 0) {
                                    String monthMessage = item.getInspectionItemClass() +
                                            item.getInspectionItem() +
                                            item.getInspectionItemSubclass();
                                    monthMessageSet.add(monthMessage);
                                }
                                break;
                            case "2": // 季度
                                DateTime quarterStart = DateUtil.beginOfQuarter(DateUtil.date());
                                String quarterStartTime = quarterStart.toString(formatTime);
                                // 当月结束时间
                                DateTime quarterEnd = DateUtil.endOfQuarter(DateUtil.date());
                                String quarterEndTime = quarterEnd.toString(formatTime);
                                Integer quarterCount = insOrderMapper.selectNotSpotCheckOrder(item.getId(), quarterStartTime, quarterEndTime);
                                if (quarterCount == 0) {
                                    String quarterMessage = item.getInspectionItemClass() +
                                            item.getInspectionItem() +
                                            item.getInspectionItemSubclass();
                                    quarterMessageSet.add(quarterMessage);
                                }

                                break;
                            case "3": // 年度
                                DateTime yearStart = DateUtil.beginOfQuarter(DateUtil.date());
                                String yearStartTime = yearStart.toString(formatTime);
                                // 当月结束时间
                                DateTime yearEnd = DateUtil.endOfQuarter(DateUtil.date());
                                String yearEndTime = yearEnd.toString(formatTime);
                                Integer yearCount = insOrderMapper.selectNotSpotCheckOrder(item.getId(), yearStartTime, yearEndTime);
                                if (yearCount == 0) {
                                    String yearMessage = item.getInspectionItemClass() +
                                            item.getInspectionItem() +
                                            item.getInspectionItemSubclass();
                                    yearMessageSet.add(yearMessage);
                                }

                                break;
                        }
                    }
                }
            }

            String message = "";
            if (CollectionUtils.isNotEmpty(monthMessageSet)) {
                message += StrUtil.format("<p>检验项<span style=\"color: red\">{}</span>当月还未进行抽样检测, 请查看是否需要添加当前检验项</p>", CollUtil.join(monthMessageSet, ", "));
            }

            if (CollectionUtils.isNotEmpty(quarterMessageSet)) {
                message += StrUtil.format("<p>检验项<span style=\"color: red\">{}</span>当前季度还未进行抽样检测, 请看是否需要添加当前检验项</p>", CollUtil.join(quarterMessageSet, ", "));
            }

            if (CollectionUtils.isNotEmpty(yearMessageSet)) {
                message += StrUtil.format("<p>检验项<span style=\"color: red\">{}</span>今年还未进行抽样检测, 请看是否需要添加当前检验项</p>", CollUtil.join(yearMessageSet, ", "));
            }

            if (StringUtils.isNotBlank(message)) {
                return Result.success(false, message);
            }
        }
        return Result.success(true);

    }

}




