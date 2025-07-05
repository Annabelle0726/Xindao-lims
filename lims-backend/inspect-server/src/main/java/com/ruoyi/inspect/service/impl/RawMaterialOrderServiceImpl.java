package com.ruoyi.inspect.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.basic.dto.*;
import com.ruoyi.basic.pojo.IfsInventoryQuantity;
import com.ruoyi.common.constant.InsOrderTypeConstants;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.config.WechatProperty;
import com.ruoyi.common.utils.LimsDateUtil;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.WxCpUtils;
import com.ruoyi.inspect.dto.CopperInsOrderDto;
import com.ruoyi.inspect.dto.RawMaterialStandardTreeDto;
import com.ruoyi.basic.mapper.IfsInventoryQuantityMapper;
import com.ruoyi.basic.mapper.StandardTreeMapper;
import com.ruoyi.inspect.dto.SampleProductDto;
import com.ruoyi.inspect.mapper.InsOrderMapper;
import com.ruoyi.inspect.mapper.InsProductMapper;
import com.ruoyi.inspect.mapper.InsSampleMapper;
import com.ruoyi.inspect.pojo.InsOrder;
import com.ruoyi.inspect.pojo.InsReport;
import com.ruoyi.inspect.service.InsOrderService;
import com.ruoyi.inspect.service.InsReportService;
import com.ruoyi.inspect.service.RawMaterialOrderService;
import com.ruoyi.common.numgen.NumberGenerator;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.performance.mapper.AuxiliaryOutputWorkingHoursMapper;
import com.ruoyi.performance.pojo.AuxiliaryOutputWorkingHours;
import com.ruoyi.system.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author zhuo
 * @Date 2024/7/31
 */
@Service
@AllArgsConstructor
public class RawMaterialOrderServiceImpl implements RawMaterialOrderService {

    private StandardTreeMapper standardTreeMapper;
    private IfsInventoryQuantityMapper ifsInventoryQuantityMapper;
    private UserMapper userMapper;
    private InsOrderService insOrderService;
    private InsOrderMapper insOrderMapper;
    private InsSampleMapper insSampleMapper;
    private final NumberGenerator<InsOrder> numberGenerator;
    private InsReportService insReportService;
    private WechatProperty wechatProperty;
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private InsProductMapper insProductMapper;
    private AuxiliaryOutputWorkingHoursMapper auxiliaryOutputWorkingHoursMapper;


    @Override
    public Result selectStandardTreeListByPartNo(String partNo) {
        List<FactoryDto> factoryDtos = standardTreeMapper.selectStandardTreeListByPartNo(partNo);
        if (CollectionUtil.isEmpty(factoryDtos)) {
            return Result.success(null, "零件号为" + partNo + "的原材料没有对应的标准库配置");
        }
        RawMaterialStandardTreeDto rawMaterialStandardTreeDto = new RawMaterialStandardTreeDto();
        for (FactoryDto factoryDto : factoryDtos) {
            for (LaboratoryDto laboratoryDto : factoryDto.getChildren()) {
                for (SampleTypeDto sampleTypeDto : laboratoryDto.getChildren()) {
                    if (sampleTypeDto.getChildren().size() == 0) {
                        sampleTypeDto.setChildren(standardTreeMapper.getStandardTree3(sampleTypeDto.getValue()));
                    }
                    // 判断绑定的是否是当前零件号
                    if (sampleTypeDto.getPartNo() != null && sampleTypeDto.getPartNo().equals(partNo)) {
                        // 添加对象
                        rawMaterialStandardTreeDto.setTreeName(factoryDto.getValue() + " - "
                                + laboratoryDto.getValue() + " - "
                                + sampleTypeDto.getValue());
                        rawMaterialStandardTreeDto.setCode(sampleTypeDto.getCode());
                        rawMaterialStandardTreeDto.setLabel(sampleTypeDto.getLabel());
                        rawMaterialStandardTreeDto.setValue(sampleTypeDto.getValue());
                        rawMaterialStandardTreeDto.setChildren1(sampleTypeDto.getChildren());
                    } else {
                        for (SampleDto sampleDto : sampleTypeDto.getChildren()) {
                            if (sampleDto.getPartNo() != null && sampleDto.getPartNo().equals(partNo)) {
                                // 添加对象
                                rawMaterialStandardTreeDto.setTreeName(factoryDto.getValue() + " - "
                                        + laboratoryDto.getValue() + " - "
                                        + sampleTypeDto.getValue() + " - "
                                        + sampleDto.getValue());
                                rawMaterialStandardTreeDto.setCode(sampleDto.getCode());
                                rawMaterialStandardTreeDto.setLabel(sampleDto.getLabel());
                                rawMaterialStandardTreeDto.setValue(sampleDto.getValue());
                                rawMaterialStandardTreeDto.setChildren2(sampleDto.getChildren());
                            }
                        }
                    }
                }
            }
        }
        return Result.success(rawMaterialStandardTreeDto);
    }

    @Override
    public IPage<IfsInventoryQuantity> getWarehouseSubmit(IPage<IfsInventoryQuantity> page, IfsInventoryQuantity ifsInventoryQuantity) {
        return standardTreeMapper.selectIfsPage(page, QueryWrappers.queryWrappers(ifsInventoryQuantity));
    }

    @Override
    public IPage<IfsInventoryQuantityDto> getIfsByStateOne(IPage<IfsInventoryQuantityDto> page, IfsInventoryQuantityDto ifsInventoryQuantityDto) {
        return standardTreeMapper.getIfsByStateOne(page, QueryWrappers.queryWrappers(ifsInventoryQuantityDto));
    }


    /**
     * 报检
     * @param ids
     * @return
     */
    @Override
    public int inspectionReport(List<Integer> ids) {
        Integer userId = SecurityUtils.getUserId().intValue();
        ifsInventoryQuantityMapper.update(null, Wrappers.<IfsInventoryQuantity>lambdaUpdate()
                .in(IfsInventoryQuantity::getId, ids)
                .set(IfsInventoryQuantity::getDeclareUser, userMapper.selectById(userId).getName())
                .set(IfsInventoryQuantity::getDeclareUserId, userId)
                .set(IfsInventoryQuantity::getIsInspect, 1)
                .set(IfsInventoryQuantity::getDeclareDate, LocalDateTime.now())
        );
        threadPoolTaskExecutor.execute(() -> {
            List<IfsInventoryQuantity> quantityList = ifsInventoryQuantityMapper.selectList(Wrappers.<IfsInventoryQuantity>lambdaQuery()
                    .in(IfsInventoryQuantity::getId, ids));
            // 企业微信通知
            String message = "";
            message += "新增报检通知";
            for (IfsInventoryQuantity inventoryQuantity : quantityList) {
                message += "\n批次号: " + inventoryQuantity.getUpdateBatchNo();
                message += "\n零件描述: " + inventoryQuantity.getPartDesc();
                message += "\n抵达数量: " + inventoryQuantity.getQtyArrived().stripTrailingZeros().toPlainString() + inventoryQuantity.getBuyUnitMeas();

                // 判断有没有到20吨. 或者能否免检
                int result = notificationRawOrder(inventoryQuantity.getId());
                switch (result) {
                    case 1:
                        message += "\n当前样品已检验过, 可以免检";
                        break;
                    case 2:
                        message += "\n当前样品已超过20吨";
                        break;
                }
                message += "\n";
            }
            WxCpUtils.informWebHook(wechatProperty.getExaminingUrl(), message);
        });
        return 1;
    }

    /**
     * 撤销报检
     * @param id
     * @return
     */
    @Override
    public int revokeInspectionReport(Integer id) {
        return ifsInventoryQuantityMapper.update(null, Wrappers.<IfsInventoryQuantity>lambdaUpdate()
                .eq(IfsInventoryQuantity::getId, id)
                .set(IfsInventoryQuantity::getIsInspect, 0)
        );
    }

    /**
     * 打印标签查询
     * @param ids
     * @return
     */
    @Override
    public List<IfsInventoryQuantityDto> printLabel(List<Integer> ids) {
        return ifsInventoryQuantityMapper.printLabel(ids);
    }

    /**
     * 报检
     * @param ifsInventoryQuantity
     * @return
     */
    @Override
    public int inspectionReportOne(IfsInventoryQuantity ifsInventoryQuantity) {
        Integer userId = SecurityUtils.getUserId().intValue();
        ifsInventoryQuantityMapper.update(null, Wrappers.<IfsInventoryQuantity>lambdaUpdate()
                .eq(IfsInventoryQuantity::getId, ifsInventoryQuantity.getId())
                .set(IfsInventoryQuantity::getDeclareUser, userMapper.selectById(userId).getName())
                .set(IfsInventoryQuantity::getDeclareUserId, userId)
                .set(IfsInventoryQuantity::getIsInspect, 1)
                .set(IfsInventoryQuantity::getDeclareDate, LocalDateTime.now())
                .set(IfsInventoryQuantity::getUpdateBatchNo, ifsInventoryQuantity.getUpdateBatchNo())
        );

        threadPoolTaskExecutor.execute(() -> {
            IfsInventoryQuantity inventoryQuantity = ifsInventoryQuantityMapper.selectById(ifsInventoryQuantity.getId());
            // 企业微信通知
            String message = "";
            message += "新增报检通知";
            message += "\n批次号: " + inventoryQuantity.getUpdateBatchNo();
            message += "\n零件描述: " + inventoryQuantity.getPartDesc();
            message += "\n抵达数量: " + inventoryQuantity.getQtyArrived().stripTrailingZeros().toPlainString() + inventoryQuantity.getBuyUnitMeas();
            WxCpUtils.informWebHook(wechatProperty.getExaminingUrl(), message);
        });
        return 1;
    }

    /**
     * 获取铜产业链检测数据
     * @param id
     * @return
     */
    @Override
    public String getIndustryChain(Integer id) {
        return ifsInventoryQuantityMapper.selectById(id).getIndustryChain();
    }

    /**
     * 原材料撤销接口
     * @param ifsInventoryId
     * @return
     */
    @Override
    public boolean repealRawOrder(Integer ifsInventoryId) {
        // 查询判断是否是铜单丝
        IfsInventoryQuantity ifsInventoryQuantity = ifsInventoryQuantityMapper.selectById(ifsInventoryId);
        if (ifsInventoryQuantity.getIsCopper() != null && ifsInventoryQuantity.getIsCopper().equals(1)) {
            // 铜单丝直接删除就行
            ifsInventoryQuantityMapper.deleteById(ifsInventoryId);
        } else {
            ifsInventoryQuantityMapper.update(null, new LambdaUpdateWrapper<IfsInventoryQuantity>()
                    .set(IfsInventoryQuantity::getState, 0)
                    .set(IfsInventoryQuantity::getIsQuarter, 1)
                    .eq(IfsInventoryQuantity::getId, ifsInventoryId));
        }

        insOrderService.update(Wrappers.<InsOrder>lambdaUpdate()
                .eq(InsOrder::getIfsInventoryId, ifsInventoryId)
                .set(InsOrder::getState, -1)
                .set(InsOrder::getEntrustCode, ""));// 撤销
        return true;
    }

    /**
     * 原材料免检下单
     * @param list
     * @param insOrder
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addExemptionOrder(List<SampleProductDto> list, InsOrder insOrder) {
        if (!insOrder.getOrderType().equals(InsOrderTypeConstants.ENTER_THE_FACTORY)) {
            throw new ErrorException("只有进厂检验才能免检");
        }
        insOrder.setSendTime(LocalDateTime.now());
        // 修改订单表直接为已检验
        insOrder.setState(4);
        insOrder.setTypeSource(1);

        String code = "Y";
        // 生成编号
        String no = numberGenerator.generateNumberWithPrefix(3,
                "JCZX/ZB-" + code + LimsDateUtil.resetDate(LocalDateTime.now()),
                InsOrder::getEntrustCode);

        insOrderMapper.insert(insOrder); // 主表

        AtomicInteger count = new AtomicInteger();
        list.forEach(a -> {
            count.getAndIncrement();
            a.setId(null);
            a.setInsOrderId(insOrder.getId());
            if (StrUtil.isEmpty(a.getSampleCode())) {
                // 如果只有一个样品就不需要拼接数字
                if (list.size() != 1) {
                    a.setSampleCode(no + "-" + count.get());
                } else {
                    a.setSampleCode(no);
                }
            }
            insSampleMapper.insert(a);

            if (!CollectionUtil.isEmpty(a.getInsProduct())) {
                throw new ErrorException("免检不需要填写检验项");
            }
        });

        // 原材料下单: 委托人就是报检人, 生产单位就是供应商单位
        IfsInventoryQuantity ifsInventoryQuantity = ifsInventoryQuantityMapper.selectById(insOrder.getIfsInventoryId());
        Integer declareUserId = ifsInventoryQuantity.getDeclareUserId();
        User user = userMapper.selectById(declareUserId);
        // 供应商名称
        insOrder.setProduction(ifsInventoryQuantity.getSupplierName());
        // 委托人名称
        insOrder.setPrepareUser(user.getName());
        insOrder.setPhone(user.getPhone());

        // 修改原材料数据直接为已检验
        ifsInventoryQuantityMapper.update(null, new LambdaUpdateWrapper<IfsInventoryQuantity>().set(IfsInventoryQuantity::getState, 2)
                .eq(IfsInventoryQuantity::getId, insOrder.getIfsInventoryId()));

        insOrder.setExamineTime(LocalDateTime.now());

        insOrder.setEntrustCode(no);
        insOrder.setIsExemption(1);

        insOrderMapper.updateById(insOrder);

        // 添加工时
        addAuxiliary(insOrder, ifsInventoryQuantity);

        // todo: ifs直接移库
        insReportService.isRawMaterial(insOrder);

        return insOrder.getId();
    }


    /**
     * 查询待下单
     * @param page
     * @param ifsInventoryQuantity
     * @return
     */
    @Override
    public IPage<IfsInventoryQuantityCheckDto> selectIfsInventoryQuantity(Page<IfsInventoryQuantityCheckDto> page, IfsInventoryQuantityCheckDto ifsInventoryQuantity) {
        return standardTreeMapper.selectIfsInventoryQuantity(page, QueryWrappers.queryWrappers(ifsInventoryQuantity));
    }

    /**
     * 查询已检验
     * @param page
     * @param ifsInventoryQuantityDto
     * @return
     */
    @Override
    public IPage<IfsInventoryQuantitySupplierDto> getIfsByOver(Page<IfsInventoryQuantitySupplierDto> page, IfsInventoryQuantitySupplierDto ifsInventoryQuantityDto) {
        // todo: 只看我
        String beginDeclareDate = ifsInventoryQuantityDto.getBeginDeclareDate();
        String endDeclareDate = ifsInventoryQuantityDto.getEndDeclareDate();
        ifsInventoryQuantityDto.setBeginDeclareDate(null);
        ifsInventoryQuantityDto.setEndDeclareDate(null);

        IPage<IfsInventoryQuantitySupplierDto> ifsByOver = standardTreeMapper.getIfsByOver(page, QueryWrappers.queryWrappers(ifsInventoryQuantityDto), beginDeclareDate, endDeclareDate);
        return ifsByOver;
    }

    /**
     * 删除原材料报检信息
     * @param id
     * @return
     */
    @Override
    public int delIfsInventory(Integer id) {
        return ifsInventoryQuantityMapper.deleteById(id);
    }

    /**
     * 原材料放行免检
     * @param ifsInventoryId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rawOrderRelease(Integer ifsInventoryId, String partDetail) {
        // 修改原材料数据直接为已检验
        ifsInventoryQuantityMapper.update(null, new LambdaUpdateWrapper<IfsInventoryQuantity>()
                .set(IfsInventoryQuantity::getState, 2)
                .set(IfsInventoryQuantity::getIsQuarter, 0)
                .eq(IfsInventoryQuantity::getId, ifsInventoryId));

        // 查询原材料信息
        IfsInventoryQuantity ifsInventoryQuantity = ifsInventoryQuantityMapper.selectById(ifsInventoryId);
        // 委托人就是报检人, 生产单位就是供应商单位
        Integer declareUserId = ifsInventoryQuantity.getDeclareUserId();
        User user = userMapper.selectById(declareUserId);

        InsOrder insOrder = new InsOrder();
        insOrder.setState(4);
        insOrder.setTypeSource(1);
        String code = "Y";
        // 生成编号
        String no = numberGenerator.generateNumberWithPrefix(3,
                "JCZX/ZB-" + code + LimsDateUtil.resetDate(LocalDateTime.now()),
                InsOrder::getEntrustCode);
        insOrder.setExamineTime(LocalDateTime.now());
        insOrder.setEntrustCode(no);
        insOrder.setIsExemption(1);
        // 供应商名称
        insOrder.setProduction(ifsInventoryQuantity.getSupplierName());
        // 委托人名称
        insOrder.setPrepareUser(user.getName());
        insOrder.setPhone(user.getPhone());
        insOrder.setIfsInventoryId(ifsInventoryId);
        insOrder.setPartDetail(partDetail);
        insOrder.setSendTime(LocalDateTime.now());
        insOrder.setSample(ifsInventoryQuantity.getPartDesc());
        // 进厂检验
        insOrder.setOrderType(InsOrderTypeConstants.ENTER_THE_FACTORY);

        insOrderMapper.insert(insOrder);

        // 添加工时
        addAuxiliary(insOrder, ifsInventoryQuantity);

        // todo: ifs直接移库
        insReportService.isRawMaterial(insOrder);
        return true;
    }

    /**
     * 0, 无提示, 1提示  当前批次的样品已检验过, 可以免检, 2 提示 当前批次的样品已超20吨, 需要多级多次检验
     *
     * 原材料下单通知免检或者多次检验
     * @param ifsInventoryId
     * @return
     */
    @Override
    public int notificationRawOrder(Integer ifsInventoryId) {
        IfsInventoryQuantity ifsInventory = ifsInventoryQuantityMapper.selectById(ifsInventoryId);
        // 查询当前批次, 供应商, 零件号的原材料是否超过了20吨, 超过了20吨需要进行多次检验提醒
        List<IfsInventoryQuantity> quantityList = ifsInventoryQuantityMapper.selectList(Wrappers.<IfsInventoryQuantity>lambdaQuery()
                .eq(IfsInventoryQuantity::getPartNo, ifsInventory.getPartNo())
                .eq(IfsInventoryQuantity::getUpdateBatchNo, ifsInventory.getUpdateBatchNo())
                .eq(IfsInventoryQuantity::getSupplierId, ifsInventory.getSupplierId())
                .eq(IfsInventoryQuantity::getSupplierName, ifsInventory.getSupplierName()));

        // 判断是否大于20吨
        BigDecimal bigDecimal = new BigDecimal("20000");
        BigDecimal count = BigDecimal.ZERO;
        for (IfsInventoryQuantity inventoryQuantity : quantityList) {
            // 判断单位是kg或者t
            if (inventoryQuantity.getBuyUnitMeas().equalsIgnoreCase("t")) {
                count = count.add(inventoryQuantity.getQtyArrived().multiply(new BigDecimal("1000")));
            } else if (inventoryQuantity.getBuyUnitMeas().equalsIgnoreCase("kg")) {
                count = count.add(inventoryQuantity.getQtyArrived());
            }
        }

        if (count.compareTo(bigDecimal) >= 0) {
            return 2;
        } else {
            // 判断之前是否出过报告, 出过报告可以免检
            int reportCount = ifsInventoryQuantityMapper.selectReportCountById(ifsInventoryId);
            if (reportCount > 0) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * 报检可以新增报检信息
     * @param ifsInventoryQuantity
     */
    @Override
    public void addIfsInventoryQuantity(IfsInventoryQuantity ifsInventoryQuantity) {
        ifsInventoryQuantity.setIsSource(0);
        ifsInventoryQuantity.setState(0);
        ifsInventoryQuantity.setIsFinish(0);
        ifsInventoryQuantityMapper.insert(ifsInventoryQuantity);
    }




    /**
     * 铜单丝下单免检
     * @param list
     * @param insOrder
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addRawCopperOrderExemptionOrder(List<SampleProductDto> list, CopperInsOrderDto insOrder) {
        if (!insOrder.getOrderType().equals(InsOrderTypeConstants.ENTER_THE_FACTORY)) {
            throw new ErrorException("只有进厂检验才能免检");
        }
        insOrder.setSendTime(LocalDateTime.now());
        // 修改订单表直接为已检验
        insOrder.setState(4);
        insOrder.setTypeSource(1);

        String code = "Y";
        // 生成编号
        String no = numberGenerator.generateNumberWithPrefix(3,
                "JCZX/ZB-" + code + LimsDateUtil.resetDate(LocalDateTime.now()),
                InsOrder::getEntrustCode);

        insOrderMapper.insert(insOrder); // 主表

        AtomicInteger count = new AtomicInteger();
        list.forEach(a -> {
            count.getAndIncrement();
            a.setId(null);
            a.setInsOrderId(insOrder.getId());
            if (StrUtil.isEmpty(a.getSampleCode())) {
                // 如果只有一个样品就不需要拼接数字
                if (list.size() != 1) {
                    a.setSampleCode(no + "-" + count.get());
                } else {
                    a.setSampleCode(no);
                }
            }
            insSampleMapper.insert(a);

            if (!CollectionUtil.isEmpty(a.getInsProduct())) {
                throw new ErrorException("免检不需要填写检验项");
            }
        });
        // 添加原材料信息
        IfsInventoryQuantity ifsInventoryQuantity = new IfsInventoryQuantity();
        // 基本信息
        ifsInventoryQuantity.setIsSource(0);
        ifsInventoryQuantity.setState(2);
        ifsInventoryQuantity.setIsInspect(1);
        ifsInventoryQuantity.setIsFinish(1);
        ifsInventoryQuantity.setIsCopper(1);
        ifsInventoryQuantity.setInspectStatus(1);
        ifsInventoryQuantity.setIsQuarter(0);

        ifsInventoryQuantity.setQtyArrived(insOrder.getQtyArrived());
        ifsInventoryQuantity.setBuyUnitMeas(insOrder.getBuyUnitMeas());
        ifsInventoryQuantity.setSupplierName(insOrder.getSupplierName());
        ifsInventoryQuantity.setUpdateBatchNo(insOrder.getUpdateBatchNo());
        ifsInventoryQuantity.setDeclareDate(insOrder.getDeclareDate());

        ifsInventoryQuantityMapper.insert(ifsInventoryQuantity);

        insOrder.setIfsInventoryId(ifsInventoryQuantity.getId());
        insOrder.setExamineTime(LocalDateTime.now());

        insOrder.setEntrustCode(no);
        insOrder.setIsExemption(1);

        insOrderMapper.updateById(insOrder);

        // 添加工时
        addAuxiliary(insOrder, ifsInventoryQuantity);

        return insOrder.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean concessionRelease(Integer ifsInventoryId) {
        // 查询原材料信息
        IfsInventoryQuantity ifsInventoryQuantity = ifsInventoryQuantityMapper.selectById(ifsInventoryId);
        if (!ifsInventoryQuantity.getInspectStatus().equals(2)) {
            throw new ErrorException("不合格的原材料才能让步放行");
        }

        // todo:需要判断oa流程是否是让步放行
        String toLocation = insReportService.moveRawMaterial(ifsInventoryQuantity);

        ifsInventoryQuantityMapper.update(null, new LambdaUpdateWrapper<IfsInventoryQuantity>()
                .set(IfsInventoryQuantity::getInspectStatus, 4)
                .set(IfsInventoryQuantity::getToLocation, toLocation)
                .eq(IfsInventoryQuantity::getId, ifsInventoryId));

        return true;
    }

    /**
     * 原材料进厂撤销下单
     * @param enterOrderId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean repealEnterRawOrder(Integer enterOrderId) {
        // 查询订单
        InsOrder order = insOrderMapper.selectById(enterOrderId);

        // 查询判断是否是铜单丝
        IfsInventoryQuantity ifsInventoryQuantity = ifsInventoryQuantityMapper.selectById(order.getIfsInventoryId());
        if (ifsInventoryQuantity.getIsCopper() != null && ifsInventoryQuantity.getIsCopper().equals(1)) {
            // 铜单丝直接删除就行
            ifsInventoryQuantityMapper.deleteById(order.getIfsInventoryId());
        } else {
            ifsInventoryQuantityMapper.update(null, new LambdaUpdateWrapper<IfsInventoryQuantity>()
                    .set(IfsInventoryQuantity::getState, 0)
                    .set(IfsInventoryQuantity::getIsQuarter, 1)
                    .eq(IfsInventoryQuantity::getId, order.getIfsInventoryId()));
        }
        insOrderService.update(Wrappers.<InsOrder>lambdaUpdate()
                .eq(InsOrder::getId, enterOrderId)
                .set(InsOrder::getState, -1)
                .set(InsOrder::getEntrustCode, ""));// 撤销

        // 清除之前的工时
        auxiliaryOutputWorkingHoursMapper.delete(Wrappers.<AuxiliaryOutputWorkingHours>lambdaQuery()
                .eq(AuxiliaryOutputWorkingHours::getOrderId, enterOrderId));

        // 清除之前报告
        insReportService.remove(Wrappers.<InsReport>lambdaQuery()
                .eq(InsReport::getInsOrderId, enterOrderId));

        return true;
    }

    /**
     * 原材料季度撤销下单
     * @param quarterOrderId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean repealQuarterRawOrder(Integer quarterOrderId) {
        // 查询订单
        InsOrder order = insOrderMapper.selectById(quarterOrderId);

        Long count = insOrderMapper.selectCount(Wrappers.<InsOrder>lambdaQuery()
                .eq(InsOrder::getIfsInventoryId, order.getIfsInventoryId())
                .ne(InsOrder::getState, -1)
                .eq(InsOrder::getOrderType, InsOrderTypeConstants.ENTER_THE_FACTORY)
                .ne(InsOrder::getId, order.getId()));
        // 判断之前是否有进厂检验, 没有需要修改原材料信息
        if (count == 0) {
            ifsInventoryQuantityMapper.update(null, new LambdaUpdateWrapper<IfsInventoryQuantity>()
                    .set(IfsInventoryQuantity::getState, 0)
                    .set(IfsInventoryQuantity::getIsQuarter, 1)
                    .eq(IfsInventoryQuantity::getId, order.getIfsInventoryId()));
        } else {
            ifsInventoryQuantityMapper.update(null, new LambdaUpdateWrapper<IfsInventoryQuantity>()
                    .set(IfsInventoryQuantity::getIsQuarter, 1)
                    .eq(IfsInventoryQuantity::getId, order.getIfsInventoryId()));
        }

        insOrderService.update(Wrappers.<InsOrder>lambdaUpdate()
                .eq(InsOrder::getId, quarterOrderId)
                .set(InsOrder::getState, -1)
                .set(InsOrder::getEntrustCode, ""));// 撤销
        auxiliaryOutputWorkingHoursMapper.delete(Wrappers.<AuxiliaryOutputWorkingHours>lambdaQuery()
                .eq(AuxiliaryOutputWorkingHours::getOrderId, quarterOrderId));
        // 清除之前报告
        insReportService.remove(Wrappers.<InsReport>lambdaQuery()
                .eq(InsReport::getInsOrderId, quarterOrderId));
        return true;
    }

    /**
     * 原材料报检全部信息导出
     * @param ifsInventoryQuantityDto
     * @param response
     */
    @Override
    public void rawAllExport(IfsInventoryQuantitySupplierDto ifsInventoryQuantityDto, HttpServletResponse response) throws UnsupportedEncodingException {
        // 判断是否是根据选择的导出
        List<IfsInventoryQuantitySupplierDto> ifsByOverList = new ArrayList<>();
        if (StringUtils.isNotBlank(ifsInventoryQuantityDto.getIds())) {
            List<String> ifsIds = StrUtil.split(ifsInventoryQuantityDto.getIds(), ",");
            ifsByOverList = standardTreeMapper.getIfsByIds(ifsIds);
        } else {
            String beginDeclareDate = ifsInventoryQuantityDto.getBeginDeclareDate();
            String endDeclareDate = ifsInventoryQuantityDto.getEndDeclareDate();
            ifsInventoryQuantityDto.setBeginDeclareDate(null);
            ifsInventoryQuantityDto.setEndDeclareDate(null);
            ifsInventoryQuantityDto.setIds(null);
            ifsByOverList = standardTreeMapper.getIfsByOverList(QueryWrappers.queryWrappers(ifsInventoryQuantityDto), beginDeclareDate, endDeclareDate);
        }

        for (IfsInventoryQuantitySupplierDto dto : ifsByOverList) {
            dto.setSendTimeString(dto.getSendTime() == null ? "" : dto.getSendTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            dto.setReceiverDateString(dto.getReceiverDate() == null ? "" : dto.getReceiverDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            dto.setDeclareDateString(dto.getDeclareDate() == null ? "" : dto.getDeclareDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            switch (dto.getInspectStatus()) {
                case 1:
                    dto.setInspectStatusString("合格");
                    break;
                case 2:
                    dto.setInspectStatusString("不合格");
                    // 查询不合格项
                    List<String> unqualifiedList = insProductMapper.selectUnqualifiedList(dto.getEnterOrderId() == null ? dto.getQuarterOrderId() : dto.getEnterOrderId());
                    dto.setUnqualifiedItem(CollUtil.join(unqualifiedList, ","));
                    break;
                case 3:
                    dto.setInspectStatusString("未下单");
                    break;
                case 4:
                    dto.setInspectStatusString("让步放行");
                    break;
                case 0:
                    dto.setInspectStatusString("检验中");
                    break;

            }
        }


        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("原材料检测信息导出", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        try {
            //新建ExcelWriter
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build();
            //获取sheet0对象
            WriteSheet mainSheet = EasyExcel.writerSheet(0, "原材料检测信息导出").head(IfsInventoryQuantitySupplierDto.class).build();

            //向sheet0写入数据 传入空list这样只导出表头
            excelWriter.write(ifsByOverList, mainSheet);
            //关闭流
            excelWriter.finish();
        } catch (IOException e) {
            throw new RuntimeException("导出失败");
        }
    }

    /**
     * 原材料查询可以季度检验的内容
     * @param page
     * @param ifsInventoryQuantityDto
     * @return
     */
    @Override
    public IPage<IfsInventoryQuantitySupplierDto> getIfsByQuarter(Page page, IfsInventoryQuantitySupplierDto ifsInventoryQuantityDto) {

        // todo: 只看我
        String beginDeclareDate = ifsInventoryQuantityDto.getBeginDeclareDate();
        String endDeclareDate = ifsInventoryQuantityDto.getEndDeclareDate();
        ifsInventoryQuantityDto.setBeginDeclareDate(null);
        ifsInventoryQuantityDto.setEndDeclareDate(null);

        return standardTreeMapper.getIfsByQuarter(page, QueryWrappers.queryWrappers(ifsInventoryQuantityDto), beginDeclareDate, endDeclareDate);
    }

    /**
     * 提前入库
     * @param ifsInventoryId
     * @return
     */
    @Override
    public boolean advancedGodown(Integer ifsInventoryId) {
        // 查询原材料信息
        IfsInventoryQuantity ifsInventoryQuantity = ifsInventoryQuantityMapper.selectById(ifsInventoryId);
        if (!ifsInventoryQuantity.getInspectStatus().equals(0)
                && !ifsInventoryQuantity.getInspectStatus().equals(3)) {
            throw new ErrorException("未检测完成数据才能提前入库");
        }

        // todo:需要判断oa流程是否是让步放行
        String toLocation = insReportService.moveRawMaterial(ifsInventoryQuantity);

        ifsInventoryQuantityMapper.update(null, new LambdaUpdateWrapper<IfsInventoryQuantity>()
                .set(IfsInventoryQuantity::getInspectStatus, 1)
                .set(IfsInventoryQuantity::getIsFinish, 1)
                .set(IfsInventoryQuantity::getToLocation, toLocation)
                .eq(IfsInventoryQuantity::getId, ifsInventoryId));

        return true;
    }


    /**
     * 添加工时
     * @param insOrder
     * @param ifsInventoryQuantity
     */
    private void addAuxiliary(InsOrder insOrder, IfsInventoryQuantity ifsInventoryQuantity) {
        AuxiliaryOutputWorkingHours auxiliaryOutputWorkingHours = new AuxiliaryOutputWorkingHours();
        auxiliaryOutputWorkingHours.setInspectionItemClass(ifsInventoryQuantity.getUpdateBatchNo() + "免检");//检测项分类
        auxiliaryOutputWorkingHours.setSample(insOrder.getEntrustCode());//样品编号
        auxiliaryOutputWorkingHours.setOrderId(insOrder.getId());//订单id
        auxiliaryOutputWorkingHours.setOrderNo(insOrder.getEntrustCode());//非加班委托单号

        // 免检默认2
        auxiliaryOutputWorkingHours.setWorkTime(new BigDecimal("2"));//非加班工时
        auxiliaryOutputWorkingHours.setAmount(1);//非加班数量
        auxiliaryOutputWorkingHours.setOutputWorkTime((ObjectUtils.isNotEmpty(auxiliaryOutputWorkingHours.getOvertimeWorkTime()) ? auxiliaryOutputWorkingHours.getOvertimeWorkTime() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(auxiliaryOutputWorkingHours.getWorkTime()) ? auxiliaryOutputWorkingHours.getWorkTime() : BigDecimal.ZERO));//产量工时
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        auxiliaryOutputWorkingHours.setDateTime(LocalDateTime.now().toLocalDate().atStartOfDay().format(formatters));//日期
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTime parse = DateUtil.parse(localDateTime.format(formatter));
        auxiliaryOutputWorkingHours.setWeekDay(getWeek(localDateTime.format(formatters)));//星期
        auxiliaryOutputWorkingHours.setWeek(String.valueOf(DateUtil.weekOfYear(DateUtil.offsetDay(parse, 1))));//周次
        auxiliaryOutputWorkingHours.setCheck(SecurityUtils.getUserId().intValue());//检测人
        auxiliaryOutputWorkingHours.setPrice(new BigDecimal("1"));//单价

        auxiliaryOutputWorkingHoursMapper.insert(auxiliaryOutputWorkingHours);
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
}
