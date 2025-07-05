package com.ruoyi.inspect.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.data.*;
import com.deepoove.poi.data.style.BorderStyle;
import com.deepoove.poi.data.style.Style;
import com.deepoove.poi.data.style.TableStyle;
import com.deepoove.poi.util.TableTools;
import com.deepoove.poi.util.UnitUtils;
import com.ruoyi.basic.mapper.IfsInventoryQuantityMapper;
import com.ruoyi.basic.pojo.IfsInventoryQuantity;
import com.ruoyi.basic.pojo.StandardTemplate;
import com.ruoyi.basic.service.StandardTemplateService;
import com.ruoyi.common.constant.DictDataConstants;
import com.ruoyi.common.constant.InsOrderTypeConstants;
import com.ruoyi.common.constant.MenuJumpPathConstants;
import com.ruoyi.common.core.domain.entity.Custom;
import com.ruoyi.common.core.domain.entity.InformationNotification;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.*;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.inspect.dto.*;
import com.ruoyi.inspect.mapper.*;
import com.ruoyi.inspect.pojo.*;
import com.ruoyi.inspect.service.*;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import com.ruoyi.inspect.vo.InsOrderPlanTaskSwitchVo;
import com.ruoyi.inspect.vo.InsOrderPlanVO;
import com.ruoyi.performance.mapper.AuxiliaryOutputWorkingHoursMapper;
import com.ruoyi.performance.mapper.PerformanceShiftMapper;
import com.ruoyi.performance.mapper.ShiftTimeMapper;
import com.ruoyi.performance.pojo.AuxiliaryOutputWorkingHours;
import com.ruoyi.performance.pojo.AuxiliaryOutputWorkingHoursTemporary;
import com.ruoyi.performance.service.AuxiliaryOutputWorkingHoursService;
import com.ruoyi.performance.service.AuxiliaryOutputWorkingHoursTemporaryService;
import com.ruoyi.system.mapper.CustomMapper;
import com.ruoyi.system.mapper.UserMapper;
import com.ruoyi.system.service.ISysDictTypeService;
import com.ruoyi.system.service.InformationNotificationService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 检验任务-业务实现层
 */
@Service
public class InsOrderPlanServiceImpl extends ServiceImpl<InsOrderMapper, InsOrder> implements InsOrderPlanService {

    @Resource
    private InsSampleMapper insSampleMapper;
    @Resource
    private InsSampleUserMapper insSampleUserMapper;
    @Resource
    private InsOrderMapper insOrderMapper;
    @Resource
    private InsOrderService insOrderService;
    @Resource
    private StandardTemplateService standardTemplateService;
    @Resource
    private InsOrderStateMapper insOrderStateMapper;
    @Resource
    private InsProductMapper insProductMapper;

    @Value("${wordUrl}")
    private String wordUrl;
    @Resource
    private InsReportMapper insReportMapper;
    @Resource
    private InsProductResultMapper insProductResultMapper;
    @Resource
    private InsProductUserMapper insProductUserMapper;
    @Resource
    private AuxiliaryOutputWorkingHoursService auxiliaryOutputWorkingHoursService;
    @Resource
    private AuxiliaryOutputWorkingHoursMapper auxiliaryOutputWorkingHoursMapper;
    @Resource
    private InformationNotificationService informationNotificationService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private CustomMapper customMapper;
    @Value("${file.path}")
    private String imgUrl;
    @Resource
    private InsOrderFileMapper insOrderFileMapper;
    @Resource
    private IfsInventoryQuantityMapper ifsInventoryQuantityMapper;
    @Resource
    private InsReportService insReportService;
    @Resource
    private InsUnqualifiedRetestProductMapper insUnqualifiedRetestProductMapper;
    @Resource
    private InsUnqualifiedRetestProductService insUnqualifiedRetestProductService;
    @Resource
    private InsUnqualifiedRetestResultMapper insUnqualifiedRetestResultMapper;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private InsOrderFactoryVerifyMapper insOrderFactoryVerifyMapper;
    @Resource
    private InsOrderFactoryVerifyItemService insOrderFactoryVerifyItemService;

    @Resource
    private SpotCheckQuarterItemMapper spotCheckQuarterItemMapper;
    @Resource
    private InsOrderStandardTemplateService insOrderStandardTemplateService;
    @Resource
    private InsOrderDeviceRecordMapper insOrderDeviceRecordMapper;
    @Resource
    private InsOrderDeviceRecordService insOrderDeviceRecordService;
    @Resource
    private ISysDictTypeService iSysDictTypeService;
    @Resource
    private InsOrderRatesService insOrderRatesService;
    @Resource
    private InsProductDeviationWarningService insProductDeviationWarningService;
    @Resource
    private InsProductDeviationWarningDetailService insProductDeviationWarningDetailService;


    @Override
    public IPage<InsOrderPlanVO> selectInsOrderPlanList(Page page, InsOrderPlanDTO insOrderPlanDTO) {
        // todo: 仅看自己或者实验室
        //获取当前人所属实验室id

        String laboratory = null;

        String userName = null;
        Integer userId = null;
        if (ObjectUtil.isNotEmpty(insOrderPlanDTO.getUserId())) {
            userId = SecurityUtils.getUserId().intValue();
            userName = userMapper.selectById(userId).getName();
            insOrderPlanDTO.setUserId(null);
        }
        Integer isCheck = insOrderPlanDTO.getIsCheck();
        insOrderPlanDTO.setIsCheck(null);
        String sonLaboratory = insOrderPlanDTO.getSonLaboratory();//试验室
        IPage<InsOrderPlanVO> insOrderPage = insSampleMapper.findInsSampleAndOrder(page,
                QueryWrappers.queryWrappers(insOrderPlanDTO),
                userName,
                userId,
                sonLaboratory,
                laboratory,
                isCheck);
        return insOrderPage;
    }

    @Override
    public IPage<InsOrderPlanTaskSwitchVo> inspectionOrderDetailsTaskSwitching(Page page, InsOrderPlanDTO insOrderPlanDTO) {

        Integer userId = SecurityUtils.getUserId().intValue();
        User user = userMapper.selectById(userId);//当前登录的人
        // todo: 仅看自己或者实验室
        //获取当前人所属实验室id
        String departLimsId = user.getDepartLimsId();
        String laboratory = null;

        if (ObjectUtil.isNotEmpty(insOrderPlanDTO.getUserId())) {
            insOrderPlanDTO.setUserId(userId.longValue());
        }
        String sonLaboratory = insOrderPlanDTO.getSonLaboratory();//试验室
        IPage<InsOrderPlanTaskSwitchVo> insOrderPage = insSampleMapper.inspectionOrderDetailsTaskSwitching(page, QueryWrappers.queryWrappers(insOrderPlanDTO), userId, sonLaboratory, laboratory);
        return insOrderPage;
    }

    //认领任务
    @Override
    public boolean claimInsOrderPlan(InsOrderPlanDTO entity) {
        if (Objects.isNull(entity)) {
            return false;
        }
        Integer userId = SecurityUtils.getUserId().intValue();
        InsSampleUser insSampleUser = new InsSampleUser(entity.getInsSampleId().intValue(), userId, 0, entity.getSonLaboratory());
        return insSampleUserMapper.insert(insSampleUser) > 0;
    }

    @Override
    public Map<String, Object> doInsOrder(Integer id, String laboratory) {
        InsOrder insOrder = new InsOrder();
        insOrder.setId(id);
        InsOrder order = insOrderMapper.selectById(id);
        if (BeanUtil.isEmpty(order.getInsTime())) {
            insOrder.setInsTime(LocalDateTime.now());
            insOrderMapper.updateById(insOrder);
            insOrderStateMapper.update(null, Wrappers.<InsOrderState>lambdaUpdate().eq(InsOrderState::getInsOrderId, id).eq(InsOrderState::getLaboratory, laboratory).set(InsOrderState::getInsTime, LocalDateTime.now()).set(InsOrderState::getInsState, 1));
        }
        Map<String, Object> map = insOrderService.getInsOrderAndSample(id, laboratory);
        List<SampleProductDto> list = JSON.parseArray(JSON.toJSONString(map.get("sampleProduct")), SampleProductDto.class);
        map.put("sampleProduct", list);

        // 查询厂家密度
        String supplierDensity = "";
        if (CollectionUtils.isNotEmpty(list)) {
            SampleProductDto sampleProductDto = list.get(0);

            // 判断有没有绑定型号
            String modelValue = insSampleMapper.selectSupplierDensityModel(sampleProductDto.getSample(),
                    order.getProduction(),
                    sampleProductDto.getModel());
            if (StringUtils.isNotBlank(modelValue)) {
                supplierDensity = modelValue;
            } else {
                supplierDensity = insSampleMapper.selectSupplierDensity(sampleProductDto.getSample(),
                        order.getProduction());
            }
        }
        map.put("supplierDensity", supplierDensity);
        return map;
    }

    @Override
    public List<InsProduct> getInsProduct(InsOrderPlanProductDto dto) {
        List<InsProduct> insProducts = new ArrayList<>();
        switch (dto.getType()) {
            case 0:
            case 4:
                //委托
                insProducts = insSampleMapper.getInsProduct1(dto.getId(), dto.getLaboratory(), dto.getCableTag(), dto.getRepetitionTag());
                break;
            case 5:
                //原材料下单
                insProducts = insSampleMapper.getInsProduct6(dto.getId(), dto.getLaboratory(), dto.getRawMaterialTag());
                break;
        }
        if (BeanUtil.isEmpty(insProducts)) {
            return null;
        }
        // 查询订单Id
        InsOrder order = insOrderMapper.selectFirstSubmit(dto.getId());
        getTemplateThing(order, insProducts);
        return insProducts;
    }

    @Override
    public List<String> checkSubmitPlan(Integer orderId, String laboratory) {
        List<String> collect = new ArrayList<>();
        List<InsSample> insSamples = insSampleMapper.selectList(Wrappers.<InsSample>lambdaQuery().eq(InsSample::getInsOrderId, orderId).select(InsSample::getId));
        List<Integer> ids = insSamples.stream().map(a -> a.getId()).collect(Collectors.toList());
        List<InsProduct> insProducts = insProductMapper.selectList(Wrappers.<InsProduct>lambdaQuery()
                .in(InsProduct::getInsSampleId, ids)
                .eq(InsProduct::getSonLaboratory, laboratory)
                .eq(InsProduct::getState, 1)
                .eq(InsProduct::getInsResult, 0));

        // 过滤判断是有复测的值是合格的
        List<InsProduct> productList = insProducts.stream().filter(insProduct -> {
            // 查询不合格复测
            Long count = insUnqualifiedRetestProductMapper.selectCount(Wrappers.<InsUnqualifiedRetestProduct>lambdaQuery()
                    .eq(InsUnqualifiedRetestProduct::getInsProductId, insProduct.getId())
                    .ne(InsUnqualifiedRetestProduct::getInsResult, 0));
            if (count != 2) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        if (productList.size() > 0) {
            collect = productList.stream().map(insProduct -> {
                return insProduct.getInspectionItem() + "-" + insProduct.getInspectionItemSubclass();
            }).collect(Collectors.toList());
        }
        return collect;
    }

    @Override
    public IPage<InsOrderFile> getFileList(Page page, InsOrderFile insOrderFile) {
        Integer insOrderId = insOrderFile.getInsOrderId();
        insOrderFile.setInsOrderId(null);
        IPage<InsOrderFile> insOrderFileIPage = insOrderFileMapper.getFileList(page, QueryWrappers.queryWrappers(insOrderFile), insOrderId);
        return insOrderFileIPage;
    }

    @Override
    public int uploadFile(Integer orderId, MultipartFile file) {
        String urlString;
        String pathName;
        String path;
        String filename = file.getOriginalFilename();
        String contentType = file.getContentType();
        InsOrderFile insOrderFile = new InsOrderFile();
        insOrderFile.setInsOrderId(orderId);
        insOrderFile.setFileName(filename);
        if (contentType != null && contentType.startsWith("image/")) {
            // 是图片
            path = imgUrl;
            insOrderFile.setType(1);
        } else {
            // 是文件
            path = wordUrl;
            insOrderFile.setType(2);
        }
        try {
            File realpath = new File(path);
            if (!realpath.exists()) {
                realpath.mkdirs();
            }
            pathName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss")) + "_" + file.getOriginalFilename();
            urlString = realpath + "/" + pathName;
            file.transferTo(new File(urlString));
            insOrderFile.setFileUrl(pathName);
            return insOrderFileMapper.insert(insOrderFile);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("附件上传错误");
            return 0;
        }
    }


    @Override
    public List<String> upPlanUser2(Integer orderId) {
        List<Integer> sampleId = insSampleMapper.selectList(Wrappers.<InsSample>lambdaQuery().eq(InsSample::getInsOrderId, orderId)).stream().map(InsSample::getId).collect(Collectors.toList());
        List<String> sonLaboratory = insProductMapper.selectList(Wrappers.<InsProduct>lambdaQuery().eq(InsProduct::getState, 1).in(InsProduct::getInsSampleId, sampleId)).stream().map(InsProduct::getSonLaboratory).distinct().collect(Collectors.toList());
        return sonLaboratory;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveInsContext(SaveInsContextDto saveInsContextDto) {
        Map<String, Object> insContext = JSON.parseObject(saveInsContextDto.getParam(), Map.class);
        Integer userId = SecurityUtils.getUserId().intValue();
        InsSample insSample = insSampleMapper.selectById(saveInsContextDto.getSampleId());
        insContext.forEach((k, v) -> {
            JSONObject jo = JSON.parseObject(JSON.toJSONString(v));
            InsProduct insProduct = new InsProduct();
            insProduct.setId(Integer.parseInt(k));
            InsProduct product = insProductMapper.selectById(insProduct.getId());
            if (saveInsContextDto.getCurrentTable().equals(product.getTemplateId()) && saveInsContextDto.getSampleId().equals(product.getInsSampleId())) {
                List<InsProductResult> oldResults = insProductResultMapper.selectList(Wrappers.<InsProductResult>lambdaQuery()
                        .eq(InsProductResult::getInsProductId, insProduct.getId()));
                InsProductResult newResult = new InsProductResult();
                if (CollectionUtils.isNotEmpty(oldResults)) {
                    BeanUtil.copyProperties(oldResults.get(0), newResult);
                }
                newResult.setInsProductId(Integer.parseInt(k));
                /*校验一下result表*/
                if (oldResults.size() > 1) {
                    for (int i = 1; i < oldResults.size(); i++) {
                        insProductResultMapper.deleteById(oldResults.get(i));
                    }
                }
                //检验值
                if (jo.get("insValue") != null) {
                    JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(jo.get("insValue")));
                    List<Map<String, Object>> iv = new ArrayList<>();
                    for (Object o : jsonArray) {
                        JSONObject insValue = JSON.parseObject(JSON.toJSONString(o));
                        Map<String, Object> map = new HashMap<>();
                        map.put("v", JSON.parseObject(JSON.toJSONString(insValue.get("v"))).get("v"));
                        map.put("r", JSON.toJSONString(insValue.get("r")));
                        map.put("c", JSON.toJSONString(insValue.get("c")));
                        map.put("w", insValue.get("w"));
                        try {
                            if ((insValue.get("u") == null || insValue.get("u").equals("")) && StrUtil.isNotEmpty(JSON.parseObject(JSON.toJSONString(insValue.get("v"))).get("v").toString())) {
                                map.put("u", userId + "");
                            } else {
                                map.put("u", insValue.get("u"));
                            }
                            iv.add(map);
                        } catch (Exception e) {
                        }
                    }
                    newResult.setInsValue(JSON.toJSONString(iv));
                }
                //计算值
                if (jo.get("comValue") != null && !Objects.equals(jo.get("comValue"), "")) {
                    JSONArray jsonArray2 = JSON.parseArray(JSON.toJSONString(jo.get("comValue")));
                    List<Map<String, Object>> cv = new ArrayList<>();
                    for (Object o : jsonArray2) {
                        JSONObject comValue = JSON.parseObject(JSON.toJSONString(o));
                        Map<String, Object> map = new HashMap<>();
                        map.put("r", JSON.toJSONString(comValue.get("r")));
                        map.put("c", JSON.toJSONString(comValue.get("c")));
                        map.put("v", JSON.parseObject(JSON.toJSONString(comValue.get("v"))).get("v"));
                        cv.add(map);
                    }
                    newResult.setComValue(JSON.toJSONString(cv));
                }
                //最终值
                try {
                    JSONObject resValue = JSON.parseObject(JSON.toJSONString(jo.get("resValue")));
                    if (resValue.get("v") != null) {
                        Object o = JSON.parseObject(JSON.toJSONString(resValue.get("v"))).get("v");
                        insProduct.setLastValue(o.equals("") ? null : (o.toString()));
                    }
                } catch (Exception e) {
                    insProduct.setLastValue("");//''
                }
                //设备编号
                if (jo.get("equipValue") != null) {
                    JSONArray jsonArray2 = JSON.parseArray(JSON.toJSONString(jo.get("equipValue")));
                    List<Map<String, Object>> ev = new ArrayList<>();
                    for (Object o : jsonArray2) {
                        JSONObject equipValue = JSON.parseObject(JSON.toJSONString(o));
                        Map<String, Object> map = new HashMap<>();
                        map.put("v", JSON.parseObject(JSON.toJSONString(equipValue.get("v"))).get("v"));
                        ev.add(map);
                    }
                    newResult.setEquipValue(JSON.toJSONString(ev));
                }
                //设备名称
                if (jo.get("equipName") != null) {
                    JSONArray jsonArray2 = JSON.parseArray(JSON.toJSONString(jo.get("equipName")));
                    List<Map<String, Object>> ev = new ArrayList<>();
                    for (Object o : jsonArray2) {
                        JSONObject equipValue = JSON.parseObject(JSON.toJSONString(o));
                        Map<String, Object> map = new HashMap<>();
                        map.put("v", JSON.parseObject(JSON.toJSONString(equipValue.get("v"))).get("v"));
                        ev.add(map);
                    }
                    newResult.setEquipName(JSON.toJSONString(ev));
                }
                //结论
                try {
                    JSONObject insResult = JSON.parseObject(JSON.toJSONString(jo.get("insResult")));
                    String ir = JSON.parseObject(JSON.toJSONString(insResult.get("v"))).get("v") + "";
                    insProduct.setInsResult(Integer.parseInt(ir));
                } catch (Exception e) {
                    insProduct.setInsResult(2);//待定
                }
                if (Objects.isNull(newResult.getId())) {
                    newResult.setCreateUser(userId);
                    newResult.setUpdateUser(userId);
                    insProductResultMapper.insert(newResult);
                } else {
                    newResult.setUpdateUser(userId);
                    newResult.setUpdateTime(LocalDateTime.now());
                    insProductResultMapper.updateById(newResult);
                }

                insProduct.setUpdateUser(userId);
                insProductMapper.updateById(insProduct);
                insProductUserMapper.insert(new InsProductUser(null, userId, LocalDateTime.now(), insProduct.getId()));

                insSample.setInsState(1);
                Long l = insProductMapper.selectCount(Wrappers.<InsProduct>lambdaQuery()
                        .eq(InsProduct::getInsSampleId, insSample.getId()));
                Long l1 = insProductMapper.selectCount(Wrappers.<InsProduct>lambdaQuery()
                        .eq(InsProduct::getInsSampleId, insSample.getId())
                        .and(wrapper -> wrapper
                                .isNotNull(InsProduct::getInsResult)
                                .or()
                                .ne(InsProduct::getInsResult, 2)
                        ));
                if (Objects.equals(l, l1)) {
                    insSample.setInsState(2);
                }
                insSampleMapper.updateById(insSample);
                /*校验一下result表*/
                List<InsProductResult> insProductResults = insProductResultMapper.selectList(Wrappers.<InsProductResult>lambdaQuery()
                        .eq(InsProductResult::getInsProductId, product.getId()));
                if (insProductResults.size() > 1) {
                    for (int i = 1; i < insProductResults.size(); i++) {
                        insProductResultMapper.deleteById(insProductResults.get(i));
                    }
                }

                // 添加工时
                // 判断是否只是参与计算值, 参与计算值实际没有填写
                InsProduct finalInsProduct = insProductMapper.selectById(product.getId());
                InsOrder insOrder = insOrderMapper.selectById(insSample.getInsOrderId());
                if (StringUtils.isNotBlank(newResult.getInsValue()) && !newResult.getInsValue().equals("[]")) {
                    threadPoolTaskExecutor.execute(() -> {
                        this.addAuxiliary(userId, finalInsProduct, insOrder);
                    });
                } else {
                    // 判断是否是没有检验值的内容
                    if (saveInsContextDto.getIsNoTestValue() != null && saveInsContextDto.getIsNoTestValue() == 1) {
                        threadPoolTaskExecutor.execute(() -> {
                            this.addAuxiliary(userId, finalInsProduct, insOrder);
                        });
                    }
                }

            }
        });
        String sampleIdStr = insContext.keySet().stream().findFirst().orElse(null);
        if (sampleIdStr != null) {
            int count = insProductMapper.selectInsProductCountByOrderId(saveInsContextDto.getOrderId());
            if (count == 0) {
                insOrderStateMapper.update(new InsOrderState(), Wrappers.<InsOrderState>lambdaUpdate()
                        .eq(InsOrderState::getInsOrderId, saveInsContextDto.getOrderId())
                        .eq(InsOrderState::getLaboratory, saveInsContextDto.getSonLaboratory())
                        .set(InsOrderState::getInsState, 2));
            }
        }


        // 添加设备记录
        threadPoolTaskExecutor.execute(() -> {
            // 添加设备使用记录
            addDeviceRecord(insSample, userId);
        });
    }

    private synchronized void addDeviceRecord(InsSample insSample, Integer userId) {
        InsOrder order = insOrderMapper.selectById(insSample.getInsOrderId());
        User user = userMapper.selectById(userId);
        // 查询设备使用记录查询该订单的使用记录
        List<InsOrderDeviceRecordDto> deviceRecordDtoList = insOrderDeviceRecordMapper.selectDeviceNumber(insSample.getInsOrderId());
        Set<String> recordCodeset = deviceRecordDtoList.stream().map(InsOrderDeviceRecordDto::getManagementNumber).collect(Collectors.toSet());

        // 获取订单设备编号
        List<InsProductResult> resultList = insProductResultMapper.selectResultByOrderId(insSample.getInsOrderId());
        Set<String> deviceCodeSet = new HashSet<>();
        for (InsProductResult result : resultList) {
            // 添加设备编号
            List<JSONObject> jsonObjects = JSON.parseArray(result.getEquipValue(), JSONObject.class);
            for (JSONObject jsonObject : jsonObjects) {
                if (!"".equals(jsonObject.get("v") + "")) {
                    List<String> v = StrUtil.split(jsonObject.get("v") + "", "，");
                    deviceCodeSet.addAll(v);
                }
            }
        }
        // 1.判断是否有没有添加的使用记录
        Set<String> orderDeviceNumbers = getDeviceDifference(deviceCodeSet, recordCodeset);
        // 添加使用记录, 根据编号查询设备id
        if (CollectionUtils.isNotEmpty(orderDeviceNumbers)) {
            List<Integer> orderDeviceIds = insOrderDeviceRecordMapper.selectDeviceIdsByNumbers(orderDeviceNumbers);
            List<InsOrderDeviceRecord> collect = orderDeviceIds.stream().map(deviceId -> {
                InsOrderDeviceRecord insOrderDeviceRecord = new InsOrderDeviceRecord();
                insOrderDeviceRecord.setInsOrderId(insSample.getInsOrderId());
                insOrderDeviceRecord.setDeviceId(deviceId);
                insOrderDeviceRecord.setSampleCode(order.getEntrustCode());
                insOrderDeviceRecord.setUseBefore(1);
                insOrderDeviceRecord.setUseAfter(1);
                insOrderDeviceRecord.setUsePerson(user.getName());
                insOrderDeviceRecord.setUsePersonId(user.getId());
                return insOrderDeviceRecord;
            }).collect(Collectors.toList());

            insOrderDeviceRecordService.saveBatch(collect);

        }

        // 2.判断是否取消了设备使用
        Set<String> repoprNumbers = getDeviceDifference(recordCodeset, deviceCodeSet);
        if (CollectionUtils.isNotEmpty(repoprNumbers)) {
            List<Integer> reportDeviceIds = insOrderDeviceRecordMapper.selectDeviceIdsByNumbers(repoprNumbers);
            insOrderDeviceRecordMapper.delete(Wrappers.<InsOrderDeviceRecord>lambdaQuery()
                    .in(InsOrderDeviceRecord::getDeviceId, reportDeviceIds)
                    .eq(InsOrderDeviceRecord::getInsOrderId, insSample.getInsOrderId()));
        }
    }

    private static Set<String> getDeviceDifference(Set<String> number1, Set<String> number2) {
        return number1.stream().filter(s1 -> number2.stream().noneMatch(s2 -> s1.equals(s2)))
                .collect(Collectors.toSet());
    }


    //交接
    @Override
    public int upPlanUser(Integer userId, Integer orderId, String sonLaboratory) {
        InsSampleUser insSampleUser = new InsSampleUser();
        insSampleUser.setUserId(userId);
        insSampleUser.setInsSampleId(orderId);
        insSampleUser.setState(0);
        insSampleUser.setSonLaboratory(sonLaboratory);
        return insSampleUserMapper.insert(insSampleUser);
    }

    /**
     * 查询模板内容
     * @param order
     * @param insProducts
     */
    private void getTemplateThing(InsOrder order, List<InsProduct> insProducts) {
        Set<Integer> set = new HashSet<>();
        // 查询订单状态判断是否是查历史模板
        if (order.getIsFirstSubmit() != null && order.getIsFirstSubmit().equals(1)) {
            InsOrderState insOrderState = insOrderStateMapper.selectOne(Wrappers.<InsOrderState>lambdaQuery()
                    .eq(InsOrderState::getInsOrderId, order.getId())
                    .last("limit 1"));
            if (insOrderState != null && (!insOrderState.getInsState().equals(3) || !insOrderState.getInsState().equals(5))) {
                for (InsProduct product : insProducts) {
                    if (product.getTemplateId() == null) {
                        product.setTemplate(new ArrayList<>());
                        continue;
                    }
                    String thing = null;
                    if (product.getTemplateId() != null && set.add(product.getTemplateId())) {
                        // 查询历史模板
                        InsOrderStandardTemplate one = insOrderStandardTemplateService.getOne(Wrappers.<InsOrderStandardTemplate>lambdaQuery()
                                .eq(InsOrderStandardTemplate::getTemplateId, product.getTemplateId())
                                .eq(InsOrderStandardTemplate::getInsOrderId, order.getId()));
                        thing = one.getThing();
                        if (StrUtil.isNotEmpty(thing)) {
                            thing = GZipUtil.uncompress(thing);
                            JSONObject sheet = JSON.parseObject(thing).getJSONArray("data").getJSONObject(0);
                            JSONObject config = sheet.getJSONObject("config");
                            List<JSONObject> cellData = JSON.parseArray(JSON.toJSONString(sheet.get("celldata")), JSONObject.class);
                            Map<String, Object> style = new HashMap<>();
                            style.put("rowlen", config.get("rowlen"));
                            style.put("columnlen", config.get("columnlen"));
                            product.setTemplate(cellData);
                            product.setStyle(style);
                            product.setTemplateName(one.getName());
                        }
                    }
                }
            }
        }
        for (InsProduct product : insProducts) {
            if (product.getTemplateId() == null) {
                product.setTemplate(new ArrayList<>());
                continue;
            }
            String thing = null;
            if (product.getTemplateId() != null && set.add(product.getTemplateId())) {
                thing = standardTemplateService.getStandTempThingById(product.getTemplateId());
            }
            if (StrUtil.isNotEmpty(thing)) {
                JSONObject sheet = JSON.parseObject(thing).getJSONArray("data").getJSONObject(0);
                JSONObject config = sheet.getJSONObject("config");
                List<JSONObject> cellData = JSON.parseArray(JSON.toJSONString(sheet.get("celldata")), JSONObject.class);
                Map<String, Object> style = new HashMap<>();
                style.put("rowlen", config.get("rowlen"));
                style.put("columnlen", config.get("columnlen"));
                product.setTemplate(cellData);
                product.setStyle(style);
                product.setTemplateName(standardTemplateService.getStandTempNameById(product.getTemplateId()));
            }
        }
    }

    /**
     * todo: 原始记录模板清除没有使用的检验项(暂时有bug无法使用)
     * @param sheet
     * @param itemNameList
     */
    private static void eliminateItem(JSONObject sheet, List<String> itemNameList) {
        // 获取到 检验项分类+检验项+检验子项的拼接,如果模板里的信息跟接口返回的检验项信息能够匹配则展示出来

        // 循环行数判断是否
        JSONArray dataListJSONArray = sheet.getJSONArray("data");

        // 添加坐标map
        Map<String, String> coordinatesMap = new HashMap<>();

        // 需要移除的索引
        List<Integer> deleteIndex = new ArrayList<>();
        // 循环列
        for (int r = 0; r < dataListJSONArray.size(); r++) {
            JSONArray dataList = dataListJSONArray.getJSONArray(r);
            // 循环行
            String itemName = "";
            // 判断是否显示
            boolean isShow = false;

            for (int c = 0; c < dataList.size(); c++) {
                // 查询批注
                JSONObject jsonObject = dataList.getJSONObject(c);
                try {
                    if (jsonObject.getJSONObject("ps").getString("value").equals("检验项分类")) {
                        String value = jsonObject.getString("v").trim();
                        itemName += value;
                        // 添加坐标
                        String coordinates = coordinatesJoint(r, c);
                        coordinatesMap.put(coordinates, value);
                        isShow = true;
                    } else if (jsonObject.getJSONObject("ps").getString("value").equals("检验项")) {
                        String value = jsonObject.getString("v").trim();
                        itemName += value;
                        // 添加坐标
                        String coordinates = coordinatesJoint(r, c);
                        coordinatesMap.put(coordinates, value);
                        isShow = true;
                    } else if (jsonObject.getJSONObject("ps").getString("value").equals("检验子项")) {
                        String value = jsonObject.getString("v").trim();
                        itemName += value;
                        // 添加坐标
                        String coordinates = coordinatesJoint(r, c);
                        coordinatesMap.put(coordinates, value);
                        isShow = true;
                    }
                } catch (Exception e) {
                    // 判断是否有mc合并单元格
                    if (jsonObject != null && jsonObject.getJSONObject("mc") != null) {
                        // 查询坐标进行添加
                        String value = coordinatesMap.get(coordinatesJoint(jsonObject.getJSONObject("mc").getInteger("r"), jsonObject.getJSONObject("mc").getInteger("c")));
                        if (StringUtils.isNotBlank(value) && !itemName.contains(value)) {
                            itemName += value;
                        }
                    }
                }
            }
            // 判断该订单是否有改检验项, 没有剔除
            if (isShow) {
                if (!itemNameList.contains(itemName)) {
                    dataListJSONArray.remove(r);
                    r--;
                }
            }
        }
    }


    /**
     * 坐标拼接
     * @param r 横坐标
     * @param c 纵坐标
     * @return
     */
    private static String coordinatesJoint(int r, int c) {
        String coordinates = "";
        coordinates = "r:" + r + ",c:" + c;
        return coordinates;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int submitPlan(Integer orderId, String laboratory, Integer verifyUser, String entrustCode) {
        InsOrder order = insOrderMapper.selectById(orderId);
        // 1. 判断是否有重复编号, 有重复编号做提醒
        Long codeCount = insOrderMapper.selectCount(Wrappers.<InsOrder>lambdaQuery()
                .ne(InsOrder::getState, -1)
                .ne(InsOrder::getIfsInventoryId, order.getIfsInventoryId())
                .eq(InsOrder::getEntrustCode, order.getEntrustCode()));
        if (codeCount > 0) {
            throw new ErrorException("当前编号有重复, 请先去修改重复编号");
        }

        // 2. 判断该订单是否是第一次生产(后续报告生成只取第一次提交时间)
        if (!(order.getIsFirstSubmit() != null && order.getIsFirstSubmit().equals(1))) {
            insOrderMapper.update(null, Wrappers.<InsOrder>lambdaUpdate()
                    .eq(InsOrder::getId, orderId)
                    .set(InsOrder::getIsFirstSubmit, 1)
                    .set(InsOrder::getFirstSubmitDate, LocalDateTime.now()));
        }

        // 3. 判断是否有未检项
        List<InsSample> insSamples = insSampleMapper.selectList(Wrappers.<InsSample>lambdaQuery()
                .eq(InsSample::getInsOrderId, orderId).select(InsSample::getId));
        List<Integer> InsSampleIds = insSamples.stream().map(InsSample::getId).collect(Collectors.toList());
        List<InsProduct> insProducts = insProductMapper.selectList(Wrappers.<InsProduct>lambdaQuery()
                .in(InsProduct::getInsSampleId, InsSampleIds)
                .eq(InsProduct::getSonLaboratory, laboratory)
                .eq(InsProduct::getState, 1)
                .and(wrapper -> wrapper
                        .isNull(InsProduct::getInsResult)
                        .or()
                        .eq(InsProduct::getInsResult, 2)
                )
                .ne(InsProduct::getIsBinding, 1));
        insProducts.addAll(insProductMapper.selectFiberInsProduct(InsSampleIds, laboratory));
        if (insProducts.size() > 0) {
            String str = "";
            int count = 0;
            for (InsProduct product : insProducts) {
                count++;
                str +=  (count != 0 ? "\n" : "") + count + "：" +
                        product.getInspectionItemClass() + " " +
                        product.getInspectionItem() + " " +
                        product.getInspectionItemSubclass();
            }
            if (ObjectUtils.isNotEmpty(str)) {
                throw new ErrorException("存在待检验的项目：" + str);
            }
        }

        // 4.修改检测结果
        insOrderStateMapper.update(null, Wrappers.<InsOrderState>lambdaUpdate().eq(InsOrderState::getInsOrderId, orderId)
                .eq(InsOrderState::getLaboratory, laboratory)
                .set(InsOrderState::getInsTime, LocalDateTime.now())
                .set(InsOrderState::getInsState, 3)
                .set(InsOrderState::getVerifyUser, verifyUser));


        // 5.发送消息通知给复核人
        // 查询当前人信息
        Integer userId = SecurityUtils.getUserId().intValue();
        String userName = insProductMapper.selectUserById(userId).get("name");
        // 查询发送人信息
        String sendUserAccount = insProductMapper.selectUserById(verifyUser).get("account");
        InformationNotification info = new InformationNotification();
        info.setCreateUser(userName);
        info.setMessageType("2");
        info.setTheme("复核通知");
        info.setContent("您有一条检验任务待复核消息, 编号:" + order.getEntrustCode());
        info.setSenderId(userId);
        info.setConsigneeId(verifyUser);
        info.setViewStatus(false);
        info.setJumpPath(MenuJumpPathConstants.INSPECTION_REVIEW);
        informationNotificationService.addInformationNotification(info);

        // 6.复核人--新增检验单相关负责人
        InsSampleUser insSampleUser = new InsSampleUser();
        insSampleUser.setUserId(verifyUser);
        insSampleUser.setInsSampleId(orderId);
        insSampleUser.setState(1);
        insSampleUser.setSonLaboratory(laboratory);
        insSampleUserMapper.insert(insSampleUser);

        // 7.校验一下result表(避免出现多个检验项结果)
        threadPoolTaskExecutor.execute(() -> {
            List<Integer> ips = insProducts.stream().map(InsProduct::getId).distinct().collect(Collectors.toList());
            for (Integer ip : ips) {
                List<InsProductResult> insProductResults = insProductResultMapper.selectList(Wrappers.<InsProductResult>lambdaQuery()
                        .eq(InsProductResult::getInsProductId, ip));
                if (insProductResults.size() > 1) {
                    for (int i = 1; i < insProductResults.size(); i++) {
                        insProductResultMapper.deleteById(insProductResults.get(i));
                    }
                }
            }
        });

        // 8.提交生成报告
        this.generateReport(orderId);

        // 9.添加临时pdf生成地址
        InsReport report = insReportMapper.selectOne(Wrappers.<InsReport>lambdaQuery()
                .eq(InsReport::getInsOrderId, orderId));
        String tempUrlPdf = this.wordToPdfTemp(report.getUrl().replace("/word", wordUrl));
        report.setTempUrlPdf("/word/" + tempUrlPdf);
        insReportMapper.updateById(report);

        // 10.原始记录模板复制(添加备份, 避免修改原始模板影响到已经完成的单子)
        this.templateCopy(orderId, InsSampleIds);

        // 11.成品抽样添加合格状态
        // 判断是否有抽样信息
        if (order.getQuarterItemId() != null) {
            // 判断是否有不合格
            this.addProductSpotCheck(insSamples, order);
        }


        // 12.添加订单费用统计信息
        List<InsProduct> productList = insProductMapper.selectProductByOrderId(orderId);
        // 删除原本费用信息
        insOrderRatesService.remove(Wrappers.<InsOrderRates>lambdaQuery()
                .eq(InsOrderRates::getInsOrderId, orderId));
        List<InsOrderRates> orderRatesList = productList.stream().map(insProduct -> {
            InsOrderRates insOrderRates = new InsOrderRates();
            insOrderRates.setInsOrderId(orderId);
            insOrderRates.setInsSampleId(insProduct.getInsSampleId());
            insOrderRates.setInsProductId(insProduct.getId());
            insOrderRates.setSampleCode(insProduct.getSampleCode());
            insOrderRates.setEntrustCode(order.getEntrustCode());
            insOrderRates.setInspectionItemClass(insProduct.getInspectionItemClass());
            insOrderRates.setInspectionItem(insProduct.getInspectionItem());
            insOrderRates.setInspectionItemSubclass(insProduct.getInspectionItemSubclass());
            insOrderRates.setCableTag(insProduct.getCableTag());
            insOrderRates.setRates(insProduct.getRates());
            insOrderRates.setManHourGroup(insProduct.getManHourGroup());
            return insOrderRates;
        }).collect(Collectors.toList());
        insOrderRatesService.saveBatch(orderRatesList);


        // 13.发送企业微信通知
        // 查询原材料
        IfsInventoryQuantity ifsInventoryQuantity = ifsInventoryQuantityMapper.selectById(order.getIfsInventoryId());
        // 查询样品信息
        InsSample insSample = insSampleMapper.selectOne(Wrappers.<InsSample>lambdaQuery()
                .eq(InsSample::getInsOrderId, orderId)
                .last("limit 1"));
        threadPoolTaskExecutor.execute(() -> {
            String message = "";
            message += "检验任务复核通知";
            message += "\n提交人: " + userName;
            message += "\n委托编号: " + order.getEntrustCode();
            message += "\n样品名称: " + insSample.getModel();
            message += "\n规格型号: " + order.getPartDetail();
            if (ifsInventoryQuantity != null) {
                message += "\n批次号: " + ifsInventoryQuantity.getUpdateBatchNo();
            }
            //发送企业微信消息通知  提交复核
            try {
                WxCpUtils.inform(sendUserAccount, message, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // 14.ifs移库(原材料需要进行移库操作) --> 最后执行,因为失败无法回滚
        if (ifsInventoryQuantity != null) {
            // 登记检验结果
            // 判断是否有不合格, 有不合格不能移库
            // todo: ifs移库
            insReportService.isRawMaterial(order);

            // 15 判断当前样品是否为原材料, 原材料需要进行数据分析, 判断之前10条数据同一个供应商, 同一个检验项的偏差是否超过10%
            // 查询ifs信息获取获取前10个供应商一样的, 检验项一样信息
            threadPoolTaskExecutor.execute(() -> {
                // 添加分析数据
                addAnalysis(productList, ifsInventoryQuantity, order, sendUserAccount);
            });


        } else {
            // 修改成品状态
            // 判断是否有不合格
            Long unqualifiedCount = insReportService.getUnqualifiedCount(order);
            if (unqualifiedCount.equals(0L)) {
                insOrderMapper.update(null, Wrappers.<InsOrder>lambdaUpdate()
                        .eq(InsOrder::getId, order.getId())
                        .set(InsOrder::getInsResult, 1));
            } else {
                insOrderMapper.update(null, Wrappers.<InsOrder>lambdaUpdate()
                        .eq(InsOrder::getId, order.getId())
                        .set(InsOrder::getInsResult, 0));
            }
        }

        return 1;
    }

    /**
     * *****添加分析数据******
     * @param productList
     * @param ifsInventoryQuantity
     * @param order
     */
    private void addAnalysis(List<InsProduct> productList, IfsInventoryQuantity ifsInventoryQuantity, InsOrder order, String sendUserAccount) {
        for (InsProduct insProduct : productList) {
            // 判断是否是数值类型
            if (insProduct.getInspectionValueType().equals("1") && insProduct.getInsResult().equals(1)) {
                List<InsProductDeviationWarningDetail> insProductAnalysisDtoList = insProductMapper.selectAnalysis(insProduct, ifsInventoryQuantity.getSupplierName());

                if (insProductAnalysisDtoList.size() < 10) {
                    continue;
                }

                // 判断当前检测项是否偏差超过10%
                List<String> laseValueList = insProductAnalysisDtoList.stream().map(InsProductDeviationWarningDetail::getTestValue)
                        .collect(Collectors.toList());

                double deviation = isDeviationOverTenPercent(laseValueList, insProduct.getLastValue());
                // 判断偏差是否大于10
                if (deviation > 10) {
                    // 判断之前是否添加过, 添加过不需要添加
                    long count = insProductDeviationWarningService.count(Wrappers.<InsProductDeviationWarning>lambdaQuery()
                            .eq(InsProductDeviationWarning::getInsProductId, insProduct.getId()));
                    if (count == 0L) {
                        // 发送通知, 并且添加数据
                        // 添加主表信息
                        InsProductDeviationWarning deviationWarning = new InsProductDeviationWarning();
                        deviationWarning.setInsOrderId(order.getId());
                        deviationWarning.setInsSampleId(insProduct.getInsSampleId());
                        deviationWarning.setInsProductId(insProduct.getId());
                        deviationWarning.setEntrustCode(order.getEntrustCode());
                        deviationWarning.setSampleCode(insProduct.getSampleCode());
                        deviationWarning.setSupplierName(ifsInventoryQuantity.getSupplierName());
                        deviationWarning.setDeviationValue(Double.toString(deviation));
                        deviationWarning.setDetectionTime(insProduct.getUpdateTime());
                        insProductDeviationWarningService.save(deviationWarning);

                        // 添加详情数据
                        InsProductDeviationWarningDetail deviationWarningDetail = new InsProductDeviationWarningDetail();
                        deviationWarningDetail.setInsOrderId(order.getId());
                        deviationWarningDetail.setInsSampleId(insProduct.getInsSampleId());
                        deviationWarningDetail.setInsProductId(insProduct.getId());
                        deviationWarningDetail.setEntrustCode(order.getEntrustCode());
                        deviationWarningDetail.setSampleCode(insProduct.getSampleCode());
                        deviationWarningDetail.setSupplierName(ifsInventoryQuantity.getSupplierName());
                        deviationWarningDetail.setTestValue(insProduct.getLastValue());
                        deviationWarningDetail.setDetectionTime(insProduct.getCreateTime());
                        deviationWarningDetail.setIsIssue(1);

                        insProductAnalysisDtoList.add(deviationWarningDetail);

                        // 添加id
                        for (InsProductDeviationWarningDetail warningDetail : insProductAnalysisDtoList) {
                            warningDetail.setDeviationWarningId(deviationWarning.getDeviationWarningId());
                        }

                        insProductDeviationWarningDetailService.saveBatch(insProductAnalysisDtoList);

                        //发送企业微信消息通知  检验项预警预警通知
                        try {
                            String message = "";
                            message += "检验项预警预警通知";
                            message += "\n委托编号: " + order.getEntrustCode();
                            message += "\n样品名称: " + order.getSample();
                            message += "\n规格型号: " + order.getPartDetail();
                            message += "\n批次号: " + ifsInventoryQuantity.getUpdateBatchNo();
                            message += "\n供应商名称: " + ifsInventoryQuantity.getSupplierName();
                            message += "\n检验项: " + insProduct.getInspectionItem() + insProduct.getInspectionItemSubclass();
                            message += "\n偏差超过了 10%";
                            // 发送给提交人
//                            WxCpUtils.inform(sendUserAccount, message, null);
//
//                            // todo: 发送给检测中心主任(固定死)
//                            WxCpUtils.inform("ZT-004704", message, null);
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error("偏差预警企业微信通知报错");
                        }
                    }
                }
            }
        }
    }

    /**
     * *****计算偏差****
     * @param data
     * @param targetStr
     * @return
     */
    public static double isDeviationOverTenPercent(List<String> data, String targetStr) {
        if (data.isEmpty()) {
            return 0;
        }
        List<Double> doubleData = data.stream()
                .map(Double::parseDouble)
                .collect(Collectors.toList());
        double sum = doubleData.stream().mapToDouble(Double::doubleValue).sum();
        double average = sum / doubleData.size();

        double target = Double.parseDouble(targetStr);
        double deviationPercent = Math.abs(target - average) / average * 100;

        // 保留两位小数
        DecimalFormat df = new DecimalFormat("#.00");
        String formatted = df.format(deviationPercent);
        return Double.parseDouble(formatted);
    }

    /**
     * ******原始记录模板复制*****
     * @param orderId
     * @param ids
     */
    private void templateCopy(Integer orderId, List<Integer> ids) {
        // 删除原本模板
        insOrderStandardTemplateService.remove(Wrappers.<InsOrderStandardTemplate>lambdaQuery()
                .eq(InsOrderStandardTemplate::getInsOrderId, orderId));
        // 复制模板
        Set<Integer> set = new HashSet<>();
        List<InsProduct> insProductList = insProductMapper.selectList(Wrappers.<InsProduct>lambdaQuery()
                .in(InsProduct::getInsSampleId, ids)
                .select(InsProduct::getTemplateId));

        for (InsProduct product : insProductList) {
            // 查询模板id
            if (product.getTemplateId() != null && set.add(product.getTemplateId())) {
                StandardTemplate standardTemplate = standardTemplateService.getById(product.getTemplateId());
                if (standardTemplate != null) {
                    InsOrderStandardTemplate insOrderStandardTemplate = new InsOrderStandardTemplate();
                    insOrderStandardTemplate.setInsOrderId(orderId);
                    insOrderStandardTemplate.setTemplateId(standardTemplate.getId());
                    insOrderStandardTemplate.setNumber(standardTemplate.getNumber());
                    insOrderStandardTemplate.setName(standardTemplate.getName());
                    insOrderStandardTemplate.setThing(standardTemplate.getThing());
                    insOrderStandardTemplateService.save(insOrderStandardTemplate);
                }
            }
        }
    }


    /**
     * 检验任务复核
     * @param orderId
     * @param laboratory
     * @param type
     * @param tell
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int rawMaterialVerifyPlan(Integer orderId, String laboratory, Integer type, String tell, Integer userId) {
        Integer num = (type == 1 ? 5 : 4);
        LocalDateTime now = LocalDateTime.now();
        insOrderStateMapper.update(null, Wrappers.<InsOrderState>lambdaUpdate()
                .eq(InsOrderState::getInsOrderId, orderId)
                .eq(InsOrderState::getLaboratory, laboratory)
                .set(InsOrderState::getInsTime, now)
                .set(InsOrderState::getInsState, num)
                .set(InsOrderState::getVerifyTell, tell)
                .set(InsOrderState::getVerifyUser, SecurityUtils.getUserId().intValue()));
        Long count = insOrderStateMapper.selectCount(Wrappers.<InsOrderState>lambdaQuery()
                .eq(InsOrderState::getInsOrderId, orderId)
                .ne(InsOrderState::getInsState, 5));
        InsReport report = insReportMapper.selectOne(Wrappers.<InsReport>lambdaQuery()
                .eq(InsReport::getInsOrderId, orderId));
        // 查询订单
        InsOrder order = insOrderMapper.selectById(orderId);
        if (count == 0 && num == 5) {
            // 修改报告状态
            insReportMapper.update(null, Wrappers.<InsReport>lambdaUpdate()
                    .eq(InsReport::getInsOrderId, orderId)
                    .set(InsReport::getIsPass, 1));

            // 修改订单状态
            insOrderMapper.update(null, Wrappers.<InsOrder>lambdaUpdate()
                    .eq(InsOrder::getId, orderId)
                    .set(InsOrder::getInsState, 5));

            //生成原材料进货验证原始记录到附件里
            if (order.getTypeSource() != null && order.getTypeSource().equals(1)) {
                this.reportFactoryVerify(orderId, userId, report.getWriteUserId());
            }

            // 查询检验任务的检验任务
            // 报告盖上批准人
            insReportService.writeReport(report.getId(), userId, report.getWriteUserId());

            // 检验人
            String userName = insProductMapper.selectUserById(report.getWriteUserId()).get("name");

            // 复核人
            Integer checkUserId = SecurityUtils.getUserId().intValue();
            String chenkUserName = insProductMapper.selectUserById(checkUserId).get("name");

            // 审核人
            String sendUserAccount = insProductMapper.selectUserById(userId).get("account");

            // 发送企业微信通知(通知审核人审批)
            threadPoolTaskExecutor.execute(() -> {

                InsSample insSample = insSampleMapper.selectOne(Wrappers.<InsSample>lambdaQuery()
                        .eq(InsSample::getInsOrderId, orderId)
                        .last("limit 1"));
                // 查询原材料
                IfsInventoryQuantity ifsInventoryQuantity = ifsInventoryQuantityMapper.selectById(order.getIfsInventoryId());

                String message = "";
                message += "报告编制审批通知";
                message += "\n检验人: " + userName;
                message += "\n复核人: " + chenkUserName;
                message += "\n委托编号: " + order.getEntrustCode();
                message += "\n样品名称: " + insSample.getModel();
                message += "\n规格型号: " + order.getPartDetail();
                if (ifsInventoryQuantity != null) {
                    message += "\n生产厂家: " + ifsInventoryQuantity.getSupplierName();
                    message += "\n批次号: " + ifsInventoryQuantity.getUpdateBatchNo();
                }
                //发送企业微信消息通知  提交复核
                try {
                    WxCpUtils.inform(sendUserAccount, message, null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });


        } else {
            //复核不通过将把复核的负责人去掉
            Integer id = insSampleUserMapper.selectOne(Wrappers.<InsSampleUser>lambdaQuery().eq(InsSampleUser::getInsSampleId, orderId).orderByDesc(InsSampleUser::getId).last("limit 1")).getId();
            insSampleUserMapper.deleteById(id);

            // 当前审核人
            Integer currentUserId = SecurityUtils.getUserId().intValue();
            String userName = insProductMapper.selectUserById(currentUserId).get("name");

            // 发送消息的人
            // 查询发送人信息
            String sendUserAccount = insProductMapper.selectUserById(report.getWriteUserId()).get("account");

            // 发送企业微信通知(检验任务退回)
            threadPoolTaskExecutor.execute(() -> {
                // 查询订单
                InsSample insSample = insSampleMapper.selectOne(Wrappers.<InsSample>lambdaQuery()
                        .eq(InsSample::getInsOrderId, orderId)
                        .last("limit 1"));
                // 查询原材料
                IfsInventoryQuantity ifsInventoryQuantity = ifsInventoryQuantityMapper.selectById(order.getIfsInventoryId());

                String message = "";
                message += "检验任务复核退回通知";
                message += "\n复核人: " + userName;
                message += "\n委托编号: " + order.getEntrustCode();
                message += "\n样品名称: " + insSample.getModel();
                message += "\n规格型号: " + order.getPartDetail();
                if (ifsInventoryQuantity != null) {
                    message += "\n批次号: " + ifsInventoryQuantity.getUpdateBatchNo();
                }
                message += "\n退回原因: " + tell;
                //发送企业微信消息通知  提交复核
                try {
                    WxCpUtils.inform(sendUserAccount, message, null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return 1;
    }

    /**
     * 生成报告
     * @param orderId
     */
    private void generateReport(Integer orderId) {
        /*样品下的项目只要有一个项目不合格则检验结果为0,否则为1*/
        //这里的insSamples是订单下的所有样品包括("/")
        List<InsSample> insSamples = insSampleMapper.selectList(Wrappers.<InsSample>lambdaQuery().eq(InsSample::getInsOrderId, orderId));
        for (InsSample insSample : insSamples) {
            List<InsProduct> insProducts = insProductMapper.selectList(Wrappers.<InsProduct>lambdaQuery()
                    .eq(InsProduct::getInsSampleId, insSample.getId())
                    .eq(InsProduct::getState, 1)
                    .ne(InsProduct::getIsBinding, 1));
            List<Integer> results = insProducts.stream().map(InsProduct::getInsResult).filter(str -> str != null).collect(Collectors.toList());
            if (results.contains(0)) {
                insSample.setInsResult(0);
            } else {
                insSample.setInsResult(1);
            }
            insSampleMapper.updateById(insSample);
        }
        InsOrder insOrder = insOrderMapper.selectById(orderId);
        // 抽检变成委托检验
        if (insOrder.getOrderType().equals(InsOrderTypeConstants.SPOT_CHECK)) {
            insOrder.setOrderType(InsOrderTypeConstants.CUSTOMER_ORDERED);
        }

        // 判断是大报告还是小报告  进厂小报告, 其他都是大报告
        if (insOrder.getOrderType().equals(InsOrderTypeConstants.ENTER_THE_FACTORY)) {
            // 生成小报告
            addSmallReport(orderId, insOrder, insSamples);
        } else {
            //生成大报告
            addBitReport(orderId, insOrder);
        }

    }


    /**
     * 电缆配置, 查看配置标识
     * @param id
     * @param laboratory
     * @return
     */
    @Override
    public List<Map<String, Object>> getCableTag(Integer id, String laboratory) {
        return insSampleMapper.getCableTag(id, laboratory);
    }

    /**
     * 原材料查看标识
     * @param id
     * @param laboratory
     * @return
     */
    @Override
    public List<Map<String, Object>> getRawMaterialTag(Integer id, String laboratory) {
        return insSampleMapper.getRawMaterialTag(id, laboratory);
    }

    /**
     * 查看重复标识
     * @param id
     * @param laboratory
     * @return
     */
    @Override
    public List<Map<String, Object>> getRepetitionTag(Integer id, String laboratory, String cableTag) {
        return insSampleMapper.getRepetitionTag(id, laboratory, cableTag);
    }

    /**
     * 新增不合格复测内容
     * @return
     */
    @Override
    public boolean addDisqualificationRetest(List<InsProduct> insProductsList) {
        List<Integer> ids = insProductsList.stream().map(InsProduct::getId).collect(Collectors.toList());

        // 判断之前是否添加过
        Long count = insUnqualifiedRetestProductMapper.selectCount(Wrappers.<InsUnqualifiedRetestProduct>lambdaQuery()
                .in(InsUnqualifiedRetestProduct::getInsProductId, ids));
        if (count > 0) {
            throw new ErrorException("有检验项检添加过复测信息");
        }

        // 查询不合格内容
        List<InsUnqualifiedRetestProduct> list = insUnqualifiedRetestProductMapper.selectRetestProduct(ids);
        // 循环判断是否是绑定值, 绑定值修改检验要求
        for (InsUnqualifiedRetestProduct insUnqualifiedRetestProduct : list) {
            for (InsProduct insProduct : insProductsList) {
                if (insProduct.getIsBinding().equals(1)) {
                    insUnqualifiedRetestProduct.setIsBinding(1);
                    insUnqualifiedRetestProduct.setAsk(null);
                    insUnqualifiedRetestProduct.setTell(null);
                    insUnqualifiedRetestProduct.setPrice(null);
                    insUnqualifiedRetestProduct.setManHour(null);
                    insUnqualifiedRetestProduct.setSection(null);
                }
            }
        }

        // 新增不合格内容
        insUnqualifiedRetestProductService.saveBatch(list);
        list.forEach(insUnqualifiedRetestProduct -> {
            insUnqualifiedRetestProduct.setRetestTag("2");
            insUnqualifiedRetestProduct.setId(null);
        });
        insUnqualifiedRetestProductService.saveBatch(list);
        return true;
    }

    @Override
    public List<InsProduct> getInsProductUnqualifiedRetest(InsOrderPlanProductDto dto) {

        List<InsProduct> insProducts = new ArrayList<>();
        switch (dto.getType()) {
            case 0:
            case 4:
                //委托
                insProducts = insUnqualifiedRetestProductMapper.getInsProductUnqualifiedRetest1(dto.getId(), dto.getLaboratory(), dto.getCableTag(), dto.getRepetitionTag(), dto.getRetestTag());
                break;
            case 5:
                //原材料下单
                insProducts = insUnqualifiedRetestProductMapper.getInsProductUnqualifiedRetest6(dto.getId(), dto.getLaboratory(), dto.getRawMaterialTag(), dto.getRetestTag());
                break;
        }
        if (BeanUtil.isEmpty(insProducts)) {
            return null;
        }
        InsOrder order = insOrderMapper.selectFirstSubmit(dto.getId());
        getTemplateThing(order, Collections.unmodifiableList(insProducts));
        return insProducts;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUnqualifiedContext(Map<String, Object> insContext, Integer currentTable, Integer currentSampleId, Integer orderId, String sonLaboratory) {
        Integer userId = SecurityUtils.getUserId().intValue();
        insContext.forEach((k, v) -> {
            JSONObject jo = JSON.parseObject(JSON.toJSONString(v));
            InsUnqualifiedRetestProduct insProduct = new InsUnqualifiedRetestProduct();
            insProduct.setId(Integer.parseInt(k));
            InsUnqualifiedRetestProduct product = insUnqualifiedRetestProductMapper.selectById(insProduct.getId());
            if (currentTable.equals(product.getTemplateId()) && currentSampleId.equals(product.getInsSampleId())) {
                List<InsUnqualifiedRetestResult> oldResults = insUnqualifiedRetestResultMapper.selectList(Wrappers.<InsUnqualifiedRetestResult>lambdaQuery()
                        .eq(InsUnqualifiedRetestResult::getRetestProductId, insProduct.getId()));
                InsUnqualifiedRetestResult newResult = new InsUnqualifiedRetestResult();
                if (CollectionUtils.isNotEmpty(oldResults)) {
                    BeanUtil.copyProperties(oldResults.get(0), newResult);
                }
                newResult.setRetestProductId(Integer.parseInt(k));
                /*校验一下result表*/
                if (oldResults.size() > 1) {
                    for (int i = 1; i < oldResults.size(); i++) {
                        insUnqualifiedRetestResultMapper.deleteById(oldResults.get(i));
                    }
                }
                //检验值
                if (jo.get("insValue") != null) {
                    JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(jo.get("insValue")));
                    List<Map<String, Object>> iv = new ArrayList<>();
                    for (Object o : jsonArray) {
                        JSONObject insValue = JSON.parseObject(JSON.toJSONString(o));
                        Map<String, Object> map = new HashMap<>();
                        map.put("v", JSON.parseObject(JSON.toJSONString(insValue.get("v"))).get("v"));
                        map.put("r", JSON.toJSONString(insValue.get("r")));
                        map.put("c", JSON.toJSONString(insValue.get("c")));
                        map.put("w", insValue.get("w"));
                        try {
                            if ((insValue.get("u") == null || insValue.get("u").equals("")) && StrUtil.isNotEmpty(JSON.parseObject(JSON.toJSONString(insValue.get("v"))).get("v").toString())) {
                                map.put("u", userId + "");
                            } else {
                                map.put("u", insValue.get("u"));
                            }
                            iv.add(map);
                        } catch (Exception e) {
                        }
                    }
                    newResult.setInsValue(JSON.toJSONString(iv));
                }
                //计算值
                if (jo.get("comValue") != null && !Objects.equals(jo.get("comValue"), "")) {
                    JSONArray jsonArray2 = JSON.parseArray(JSON.toJSONString(jo.get("comValue")));
                    List<Map<String, Object>> cv = new ArrayList<>();
                    for (Object o : jsonArray2) {
                        JSONObject comValue = JSON.parseObject(JSON.toJSONString(o));
                        Map<String, Object> map = new HashMap<>();
                        map.put("r", JSON.toJSONString(comValue.get("r")));
                        map.put("c", JSON.toJSONString(comValue.get("c")));
                        map.put("v", JSON.parseObject(JSON.toJSONString(comValue.get("v"))).get("v"));
                        cv.add(map);
                    }
                    newResult.setComValue(JSON.toJSONString(cv));
                }
                //最终值
                try {
                    JSONObject resValue = JSON.parseObject(JSON.toJSONString(jo.get("resValue")));
                    if (resValue.get("v") != null) {
                        Object o = JSON.parseObject(JSON.toJSONString(resValue.get("v"))).get("v");
                        insProduct.setLastValue(o.equals("") ? null : (o.toString()));
                    }
                } catch (Exception e) {
                    insProduct.setLastValue("");//''
                }
                //设备编号
                if (jo.get("equipValue") != null) {
                    JSONArray jsonArray2 = JSON.parseArray(JSON.toJSONString(jo.get("equipValue")));
                    List<Map<String, Object>> ev = new ArrayList<>();
                    for (Object o : jsonArray2) {
                        JSONObject equipValue = JSON.parseObject(JSON.toJSONString(o));
                        Map<String, Object> map = new HashMap<>();
                        map.put("v", JSON.parseObject(JSON.toJSONString(equipValue.get("v"))).get("v"));
                        ev.add(map);
                    }
                    newResult.setEquipValue(JSON.toJSONString(ev));
                }
                //设备名称
                if (jo.get("equipName") != null) {
                    JSONArray jsonArray2 = JSON.parseArray(JSON.toJSONString(jo.get("equipName")));
                    List<Map<String, Object>> ev = new ArrayList<>();
                    for (Object o : jsonArray2) {
                        JSONObject equipValue = JSON.parseObject(JSON.toJSONString(o));
                        Map<String, Object> map = new HashMap<>();
                        map.put("v", JSON.parseObject(JSON.toJSONString(equipValue.get("v"))).get("v"));
                        ev.add(map);
                    }
                    newResult.setEquipName(JSON.toJSONString(ev));
                }
                //结论
                try {
                    JSONObject insResult = JSON.parseObject(JSON.toJSONString(jo.get("insResult")));
                    String ir = JSON.parseObject(JSON.toJSONString(insResult.get("v"))).get("v") + "";
                    insProduct.setInsResult(Integer.parseInt(ir));
                } catch (Exception e) {
                    insProduct.setInsResult(2);//待定
                }
                if (Objects.isNull(newResult.getId())) {
                    newResult.setCreateUser(userId);
                    newResult.setUpdateUser(userId);
                    insUnqualifiedRetestResultMapper.insert(newResult);
                } else {
                    newResult.setUpdateUser(userId);
                    newResult.setUpdateTime(LocalDateTime.now());
                    insUnqualifiedRetestResultMapper.updateById(newResult);
                }

                insProduct.setUpdateUser(userId);
                insUnqualifiedRetestProductMapper.updateById(insProduct);
                insProductUserMapper.insert(new InsProductUser(null, userId, LocalDateTime.now(), insProduct.getId()));


                /*校验一下result表*/
                List<InsUnqualifiedRetestResult> insProductResults = insUnqualifiedRetestResultMapper.selectList(Wrappers.<InsUnqualifiedRetestResult>lambdaQuery()
                        .eq(InsUnqualifiedRetestResult::getRetestProductId, product.getId()));
                if (insProductResults.size() > 1) {
                    for (int i = 1; i < insProductResults.size(); i++) {
                        insUnqualifiedRetestResultMapper.deleteById(insProductResults.get(i));
                    }
                }

            }
        });

    }

    /**
     * 查询进货原始记录
     * @param insOrderId
     * @return
     */
    @Override
    public InsOrderFactoryVerify getFactoryVerify(Integer insOrderId) {
        InsOrderFactoryVerify insOrderFactoryVerify = insOrderFactoryVerifyMapper.selectOne(Wrappers.<InsOrderFactoryVerify>lambdaQuery()
                .eq(InsOrderFactoryVerify::getInsOrderId, insOrderId)
                .last("limit 1"));
        // 判断查询查询是否为空
        if (insOrderFactoryVerify == null) {
            insOrderFactoryVerify = new InsOrderFactoryVerify();
            // 查询下单
            InsOrder order = insOrderMapper.selectById(insOrderId);
            // 查询检验项目表
            List<InsSample> insSamples = insSampleMapper.selectList(Wrappers.<InsSample>lambdaQuery()
                    .eq(InsSample::getInsOrderId, insOrderId));

            //查询规格信号
            List<String> insSamplemodel = insSamples.stream().map(insSample -> insSample.getModel()).collect(Collectors.toList());
            String model = CollUtil.join(insSamplemodel, ",");

            // 查询原材料信息
            IfsInventoryQuantity ifsInventoryQuantity = ifsInventoryQuantityMapper.selectById(order.getIfsInventoryId());
            if (ifsInventoryQuantity == null) {
                throw new ErrorException("缺少原材料信息");
            }

            // 检验编号
            insOrderFactoryVerify.setEntrustCode(order.getEntrustCode());
            // 材料厂家
            insOrderFactoryVerify.setSupplierName(ifsInventoryQuantity.getSupplierName());
            // 到货日期
            insOrderFactoryVerify.setDeclareDate(ifsInventoryQuantity.getDeclareDate());
            // 材料名称
            insOrderFactoryVerify.setSample(order.getSample());
            // 规格型号
            insOrderFactoryVerify.setModel(model);
            // 材料批号
            insOrderFactoryVerify.setUpdateBatchNo(ifsInventoryQuantity.getUpdateBatchNo());

            insOrderFactoryVerify.setInsOrderId(insOrderId);

            return insOrderFactoryVerify;
        }

        // 查询验证项目
        List<InsOrderFactoryVerifyItem> verifyItems = insOrderFactoryVerifyItemService.list(Wrappers.<InsOrderFactoryVerifyItem>lambdaQuery()
                .eq(InsOrderFactoryVerifyItem::getFactoryVerifyId, insOrderFactoryVerify.getFactoryVerifyId())
                .orderByAsc(InsOrderFactoryVerifyItem::getSort));
        insOrderFactoryVerify.setFactoryVerifyItemList(verifyItems);
        return insOrderFactoryVerify;
    }

    /**
     * 保存原材料进货验证
     * @param factoryVerify
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addFactoryVerify(InsOrderFactoryVerify factoryVerify) {
        if (factoryVerify.getInsOrderId() == null) {
            throw new ErrorException("缺少订单Id");
        }

        // 判断是新增还是修改
        if (factoryVerify.getFactoryVerifyId() == null) {
            // 判断之前是否添加过
            Long count = insOrderFactoryVerifyMapper.selectCount(Wrappers.<InsOrderFactoryVerify>lambdaQuery()
                    .eq(InsOrderFactoryVerify::getInsOrderId, factoryVerify.getInsOrderId()));
            if (count > 0) {
                throw new ErrorException("原材料进货验证记录重复添加");
            }

            // 新增
            insOrderFactoryVerifyMapper.insert(factoryVerify);

            for (InsOrderFactoryVerifyItem item : factoryVerify.getFactoryVerifyItemList()) {
                item.setFactoryVerifyId(factoryVerify.getFactoryVerifyId());
            }

            // 批量添加
            insOrderFactoryVerifyItemService.saveBatch(factoryVerify.getFactoryVerifyItemList());

        } else {
            // 编辑
            insOrderFactoryVerifyMapper.updateById(factoryVerify);

            // 删除原本验证项目
            insOrderFactoryVerifyItemService.remove(Wrappers.<InsOrderFactoryVerifyItem>lambdaQuery()
                    .eq(InsOrderFactoryVerifyItem::getFactoryVerifyId, factoryVerify.getFactoryVerifyId()));

            for (InsOrderFactoryVerifyItem item : factoryVerify.getFactoryVerifyItemList()) {
                item.setFactoryVerifyId(factoryVerify.getFactoryVerifyId());
            }

            insOrderFactoryVerifyItemService.saveBatch(factoryVerify.getFactoryVerifyItemList());
        }

        return true;
    }

    /*****************************************************  小报告    ***************************************************************************/

    /**
     * 小报告生成
     * @param orderId
     * @param insOrder
     * @param insSamples
     */
    private void addSmallReport(Integer orderId, InsOrder insOrder, List<InsSample> insSamples) {
        InsReport insReport = new InsReport();
        AtomicReference<String> resultCh = new AtomicReference<>("");
        insReport.setCode(insOrder.getEntrustCode());
        insReport.setInsOrderId(orderId);
        InsSample insSample = insSamples.get(0);
        // 填充基础数据
        EnterFactoryReport enterFactoryReport = new EnterFactoryReport();
        enterFactoryReport.setCode(insOrder.getEntrustCode());
        enterFactoryReport.setSample(insOrder.getSample());
        // 获取ifs数据
        IfsInventoryQuantity ifsInventoryQuantity = ifsInventoryQuantityMapper.selectById(insOrder.getIfsInventoryId());
        enterFactoryReport.setQtyArrived(ifsInventoryQuantity.getQtyArrived() == null ? "" :
                ifsInventoryQuantity.getQtyArrived().stripTrailingZeros().toPlainString() + ifsInventoryQuantity.getBuyUnitMeas());
        enterFactoryReport.setQuantity(insOrder.getTestQuantity());
        enterFactoryReport.setPartDesc(insOrder.getPartDetail());
        enterFactoryReport.setSupplierName("**********");
        enterFactoryReport.setLotBatchNo(ifsInventoryQuantity.getUpdateBatchNo());

        // 检测依据
        Set<String> standardMethod = new HashSet<>();
        StringBuilder standardMethod2 = new StringBuilder();
        standardMethod.add(baseMapper.getStandardMethodCode(insSample.getStandardMethodListId()));
        for (String s : standardMethod) {
            standardMethod2.append("、").append(s);
        }
        standardMethod2.replace(0, 1, "");

        // 样品类型
        String orderType = iSysDictTypeService.selectLabelByDict(DictDataConstants.CHECK_TYPE, insOrder.getOrderType());

        List<RowRenderData> rows = new ArrayList<>();
        List<TextRenderData> text = new ArrayList<>();
        RowRenderData rowRenderData = null;

        // 查询检验内容
        List<SampleProductExportDto> sampleProductDto2s = insOrderMapper.selectSampleBySampleId(insSamples.stream()
                .map(InsSample::getId).collect(Collectors.toList()));

        // 格式化修改检验项
        this.formatProducts(sampleProductDto2s);

        List<SampleProductExportDto> sampleList = new ArrayList<>();
        Integer max = insSamples.stream().mapToInt(InsSample::getQuantity).sum();

        // 转成Mpa进行排序
        Map<String, List<SampleProductExportDto>> sortedMap = sampleProductDto2s.stream()
                .filter(sampleProductDto2 -> StringUtils.isNotBlank(sampleProductDto2.getInspectionItem()))
                .collect(Collectors.groupingBy(SampleProductExportDto::getInspectionItem));
        List<Map.Entry<String, List<SampleProductExportDto>>> entries = new ArrayList<>(sortedMap.entrySet());
        entries.sort((o1, o2) -> (o1.getValue().get(0).getSort() == null ? 0 : o1.getValue().get(0).getSort())
                - (o2.getValue().get(0).getSort() == null ? 0 : o2.getValue().get(0).getSort()));
        // 创建一个 LinkedHashMap 来保持插入顺序
        Map<String, List<SampleProductExportDto>> item = new LinkedHashMap<>();
        for (Map.Entry<String, List<SampleProductExportDto>> entry : entries) {
            item.put(entry.getKey(), entry.getValue());
        }

        AtomicInteger finalIndex = new AtomicInteger(1);
        item.forEach((s, sampleProductDtoInside) -> {
            // 添加检验项
            SampleProductExportDto dto2 = new SampleProductExportDto();
            // 判断是否有英文名称
            SampleProductExportDto temp = sampleProductDtoInside.get(0);

            // 判断是否有检验子项,没有就把检验参数填充
            dto2.setIndex(Integer.toString(finalIndex.get()));
            if (StringUtils.isBlank(temp.getInspectionItemSubclass())) {
                // 处理集合
                Map<String, SampleProductExportDto> map = new LinkedHashMap<>();
                for (SampleProductExportDto productDto2 : sampleProductDtoInside) {
                    String productName = productDto2.getInspectionItemClass() + productDto2.getInspectionItem();
                    if (map.containsKey(productName)) {
                        // 如果名称已经存在，添加 lastValue 值到 lastValueList 列表
                        map.get(productName)
                                .getLastValueList()
                                .add(productDto2.getLastValue());
                        map.get(productName)
                                .getInsResultList()
                                .add(productDto2.getInsResult());
                    } else {
                        // 如果名称不存在，直接放入 map
                        productDto2.setLastValueList(new ArrayList<>()); // 检验内容
                        productDto2.getLastValueList().add(productDto2.getLastValue());
                        productDto2.setInsResultList(new ArrayList<>()); // 结果
                        productDto2.getInsResultList().add(productDto2.getInsResult());

                        map.put(productName, productDto2);
                    }
                }
                List<SampleProductExportDto> result = new ArrayList<>(map.values());

                //添加检验子项
                SampleProductExportDto temp2 = result.get(0);
                if (StringUtils.isNotBlank(temp2.getRadius())) {
                    s = s + "(" + temp2.getRadius() + ")";
                }
                // 判断有没有条件字段有需要拼接到名称后面
                temp2.setInspectionName(s);
                temp2.setIndex(Integer.toString(finalIndex.get()));
                sampleList.add(temp2);
            } else {
                dto2.setInspectionName(s);
                // 填充到要求
                if (StringUtils.isNotBlank(temp.getRadius())) {
                    dto2.setTell("(" + temp.getRadius() + ")");
                }
                sampleList.add(dto2);

                // 处理集合
                Map<String, SampleProductExportDto> map = new LinkedHashMap<>();
                for (SampleProductExportDto productDto2 : sampleProductDtoInside) {
                    String productName = productDto2.getInspectionItemClass() + productDto2.getInspectionItem() + productDto2.getInspectionItemSubclass();
                    if (map.containsKey(productName)) {
                        // 如果名称已经存在，添加 lastValue 值到 lastValueList 列表
                        map.get(productName)
                                .getLastValueList()
                                .add(productDto2.getLastValue());
                        map.get(productName)
                                .getInsResultList()
                                .add(productDto2.getInsResult());
                    } else {
                        // 如果名称不存在，直接放入 map
                        productDto2.setLastValueList(new ArrayList<>()); // 检验内容
                        productDto2.getLastValueList().add(productDto2.getLastValue());
                        productDto2.setInsResultList(new ArrayList<>()); // 结果
                        productDto2.getInsResultList().add(productDto2.getInsResult());

                        map.put(productName, productDto2);
                    }
                }
                List<SampleProductExportDto> result = new ArrayList<>(map.values());
                //添加检验子项
                for (SampleProductExportDto productDto2 : result) {
                    productDto2.setInspectionName("--" + productDto2.getInspectionItemSubclass());
                    sampleList.add(productDto2);
                }
            }
            finalIndex.incrementAndGet();
        });

        // 添加小报告表头
        text = addSmallHead(text, max, rows);


        // 中间检测值添加
        for (int i = 0; i < sampleList.size(); i++) {
            SampleProductExportDto sample = sampleList.get(i);

            // 序号
            TextRenderData middleRenderData1 = new TextRenderData();
            middleRenderData1.setText(sample.getIndex());
            Style middleStyle1 = new Style();
            middleStyle1.setFontFamily("宋体");
            middleStyle1.setColor("000000");
            middleRenderData1.setStyle(middleStyle1);
            text.add(middleRenderData1);

            // 检验项目
            TextRenderData middleRenderData2 = new TextRenderData();
            middleRenderData2.setText(sample.getInspectionName());
            Style middleStyle2 = new Style();
            middleStyle2.setFontFamily("宋体");
            middleStyle2.setColor("000000");
            middleRenderData2.setStyle(middleStyle2);
            text.add(middleRenderData2);

            // 单位
            TextRenderData middleRenderData4 = new TextRenderData();
            middleRenderData4.setText(sample.getUnit());
            Style middleStyle4 = new Style();
            middleStyle4.setFontFamily("宋体");
            middleStyle4.setColor("000000");
            middleRenderData4.setStyle(middleStyle4);
            text.add(middleRenderData4);

            //标准要求
            TextRenderData middleRenderData5 = new TextRenderData();
            middleRenderData5.setText(sample.getTell());
            Style middleStyle5 = new Style();
            middleStyle5.setFontFamily("宋体");
            middleStyle5.setColor("000000");
            middleRenderData5.setStyle(middleStyle5);
            text.add(middleRenderData5);

            // 检验结果
            boolean isMore = false;
            if (CollectionUtils.isNotEmpty(sample.getLastValueList())
                    && sample.getLastValueList().size() == max) {
                // 判断是否是外观, 外观全部合格的话需要合并
                if (sample.getInspectionItem().contains("外观")) {
                    // 判定结果
                    boolean falg = false;
                    if (CollectionUtils.isNotEmpty(sample.getLastValueList())) {
                        // 判断是否有一个错误
                        if (sample.getInsResultList().stream().anyMatch(s -> s.equals(2))) {
                            falg = true;
                        } else {
                            boolean error = sample.getInsResultList().stream().anyMatch(s -> s.equals(0));
                            if (!error) {
                                falg = true;
                            }
                        }
                    } else {
                        if (!sample.getInsResult().equals(2)) {
                            falg = true;
                        }
                    }
                    if (falg) {
                        for (int j = 0; j < max; j++) {
                            TextRenderData middleRenderData6 = new TextRenderData();
                            middleRenderData6.setText((StringUtils.isNotEmpty(sample.getLastValue()) ?
                                    sample.getLastValue() : "")
                                    + "∑" + (50 + i));
                            Style middleStyle6 = new Style();
                            middleStyle6.setFontFamily("宋体");
                            middleStyle6.setColor("000000");
                            middleRenderData6.setStyle(middleStyle6);
                            text.add(middleRenderData6);
                        }
                    } else {
                        for (String s : sample.getLastValueList()) {
                            TextRenderData middleRenderData6 = new TextRenderData();
                            middleRenderData6.setText(s);
                            Style middleStyle6 = new Style();
                            middleStyle6.setFontFamily("宋体");
                            middleStyle6.setColor("000000");
                            middleRenderData6.setStyle(middleStyle6);
                            text.add(middleRenderData6);
                        }
                    }
                } else {
                    for (String s : sample.getLastValueList()) {
                        TextRenderData middleRenderData6 = new TextRenderData();
                        middleRenderData6.setText(s);
                        Style middleStyle6 = new Style();
                        middleStyle6.setFontFamily("宋体");
                        middleStyle6.setColor("000000");
                        middleRenderData6.setStyle(middleStyle6);
                        text.add(middleRenderData6);
                    }
                }
            } else {
                for (int j = 0; j < max; j++) {
                    TextRenderData middleRenderData6 = new TextRenderData();
                    middleRenderData6.setText((StringUtils.isNotEmpty(sample.getLastValue()) ?
                            sample.getLastValue() : "")
                            + "∑" + (7 + i));
                    Style middleStyle6 = new Style();
                    middleStyle6.setFontFamily("宋体");
                    middleStyle6.setColor("000000");
                    middleRenderData6.setStyle(middleStyle6);
                    text.add(middleRenderData6);
                }
            }


            // 判定结果
            String result = "";
            if (sample.getInsResult() != null) {
                switch (sample.getInsResult()) {
                    case 1:
                        result = "√";
                        break;
                    case 2:
                        result = "×";
                        break;
                    case 3:
                        result = "-";
                        break;
                }
            }
            if (CollectionUtils.isNotEmpty(sample.getLastValueList())) {
                // 判断是否有一个错误
                if (sample.getInsResultList().stream().anyMatch(s -> s.equals(3))) {
                    result = "-";
                } else {
                    boolean error = sample.getInsResultList().stream().anyMatch(s -> s.equals(0));
                    if (error) {
                        List<String> collect = new ArrayList<>();
                        int index = 1;
                        for (Integer count : sample.getInsResultList()) {
                            String type;
                            if (count.equals(0)) {
                                // 添加不合格描述
                                String item1 = (max == 1 ? "" : "第" + Integer.toString(index) + "次")
                                        + sample.getInspectionItem()
                                        + (StringUtils.isBlank(sample.getInspectionItemSubclass()) ? "" : "" + sample.getInspectionItemSubclass());
                                if (resultCh.get().equals("")) {
                                    resultCh.set(resultCh.get() + item1);
                                } else {
                                    resultCh.set(resultCh.get() + "、" + item1);
                                }
                                type = "×";
                            } else {
                                type = "√";
                            }
                            collect.add(type);
                            index++;
                        }
                        result = CollUtil.join(collect, " ");
                    } else {
                        result = "√";
                    }
                }
            }

            TextRenderData middleRenderData7 = new TextRenderData();
            middleRenderData7.setText(result);
            Style middleStyle7 = new Style();
            middleStyle7.setFontFamily("宋体");
            middleStyle7.setColor("000000");
            middleRenderData7.setStyle(middleStyle7);
            text.add(middleRenderData7);

            TextRenderData[] text2 = text.toArray(new TextRenderData[0]);
            rowRenderData = Rows.of(text2).center().rowAtleastHeight(1).create();
            // 0.8厘米
            double centimeters = 0.8;
            int twips = UnitUtils.cm2Twips(centimeters);
            rowRenderData.getRowStyle().setHeight(twips);
            rows.add(rowRenderData);
            text = new ArrayList<>();
        }

        TableRenderData tableRenderData = new TableRenderData();
        tableRenderData.setRows(rows);

        //设置样式
        TableStyle tableStyle = new TableStyle();
        for (int i = 1; i <= max; i++) {
            // 根据减压那结果个数修改长度
            switch (i) {
                case 1:
                    tableStyle.setColWidths(new int[]{650, 2900, 850, 2300, 2100, 1200});
                    break;
                case 2:
                    tableStyle.setColWidths(new int[]{650, 2700, 850, 2200, 1200, 1200, 1200});
                    break;
                case 3:
                    tableStyle.setColWidths(new int[]{650, 2700, 850, 1600, 1000, 1000, 1000, 1200});
                    break;
                case 4:
                    tableStyle.setColWidths(new int[]{650, 2400, 850, 1500, 850, 850, 850, 850, 1200});
                    break;
                case 5:
                    tableStyle.setColWidths(new int[]{650, 2200, 850, 1350, 750, 750, 750, 750, 750, 1200});
                    break;
            }
        }
        tableStyle.setWidth("10000");
        tableStyle.setAlign(TableRowAlign.CENTER);
        BorderStyle borderStyle = new BorderStyle();
        borderStyle.setColor("000000");
        borderStyle.setType(XWPFTable.XWPFBorderType.SINGLE);
        borderStyle.setSize(4);
        tableStyle.setLeftBorder(borderStyle);
        tableStyle.setRightBorder(borderStyle);
        tableStyle.setInsideHBorder(borderStyle); // 设置水平内边框
        tableStyle.setInsideVBorder(borderStyle);
        tableRenderData.setTableStyle(tableStyle);

        if (!resultCh.get().equals("")) {
            resultCh.set("经检验，" + resultCh.get() + "所检项目不合格，其余所检项目均合格。");
        } else {
            resultCh.set("经检验此批" + enterFactoryReport.getSample() + "各项目均符合检验规范要求。");
        }

        ConfigureBuilder builder = Configure.builder();
        builder.useSpringEL(true);

        InputStream inputStream = this.getClass().getResourceAsStream("/static/small-report-template.docx");
        XWPFTemplate template = XWPFTemplate.compile(inputStream, builder.build()).render(
                new HashMap<String, Object>() {{
                    put("report", enterFactoryReport);
                    put("standardMethod", standardMethod2.toString().equals("null") ? "" : standardMethod2);
                    put("orderType", orderType);
                    put("table", tableRenderData);
                    put("resultCh", resultCh);
                    put("writeUrl", null);
                    put("examineUrl", null);
                    put("ratifyUrl", null);
                    put("writeDateUrl", null);
                    put("examineDateUrl", null);
                    put("ratifyDateUrl", null);
                    put("seal1", null);
                }});
        try {
            // 修改换行和合并问题
            updaeMerge(template.getXWPFDocument(), true);
            String name = insReport.getCode().replace("/", "") + "-J.docx";
            template.writeAndClose(Files.newOutputStream(Paths.get(wordUrl + "/" + name)));
            insReport.setUrl("/word/" + name);
            insReport.setIsPass(0);
            insReport.setWriteUserId(SecurityUtils.getUserId().intValue());//提交人
            insReport.setWriteTime(LocalDateTime.now());//提交时间
            // 查询报告, 判断之前是否添加过, 添加过删除
            insReportMapper.delete(Wrappers.<InsReport>lambdaQuery()
                    .eq(InsReport::getInsOrderId, insOrder.getId()));
            insReportMapper.insert(insReport);
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 添加小宝表头
     * @param text
     * @param max
     * @param rows
     * @return
     */
    private static List<TextRenderData> addSmallHead(List<TextRenderData> text, Integer max, List<RowRenderData> rows) {
        RowRenderData rowRenderData;
//        // 头
        TextRenderData headRenderData1 = new TextRenderData();
        headRenderData1.setText("序号");
        Style headStyle1 = new Style();
        headStyle1.setFontFamily("宋体");
        headStyle1.setColor("000000");
        headRenderData1.setStyle(headStyle1);
        text.add(headRenderData1);

        TextRenderData headRenderData2 = new TextRenderData();
        headRenderData2.setText("检验项目");
        Style headStyle2 = new Style();
        headStyle2.setFontFamily("宋体");
        headStyle2.setColor("000000");
        headRenderData2.setStyle(headStyle2);
        text.add(headRenderData2);

        TextRenderData headRenderData4 = new TextRenderData();
        headRenderData4.setText("单位");
        Style headStyle4 = new Style();
        headStyle4.setFontFamily("宋体");
        headStyle4.setColor("000000");
        headRenderData4.setStyle(headStyle4);
        text.add(headRenderData4);

        TextRenderData headRenderData5 = new TextRenderData();
        headRenderData5.setText("标准要求");
        Style headStyle5 = new Style();
        headStyle5.setFontFamily("宋体");
        headStyle5.setColor("000000");
        headRenderData5.setStyle(headStyle5);
        text.add(headRenderData5);

        for (int i = 0; i < max; i++) {
            TextRenderData headRenderData6 = new TextRenderData();
            headRenderData6.setText("检验结果∑6");
            Style headStyle6 = new Style();
            headStyle6.setFontFamily("宋体");
            headStyle6.setColor("000000");
            headRenderData6.setStyle(headStyle6);
            text.add(headRenderData6);
        }
        TextRenderData headRenderData7 = new TextRenderData();
        headRenderData7.setText("单项判断");
        Style headStyle7 = new Style();
        headStyle7.setFontFamily("宋体");
        headStyle7.setColor("000000");
        headRenderData7.setStyle(headStyle7);
        text.add(headRenderData7);

        TextRenderData[] text3 = text.toArray(new TextRenderData[0]);
        rowRenderData = Rows.of(text3).center().rowAtleastHeight(1).create();
        rows.add(rowRenderData);
        text = new ArrayList<>();

        // 第二行(可能没有)
        if (max > 1) {
            TextRenderData tagRenderData1 = new TextRenderData();
            tagRenderData1.setText("");
            Style tagStyle1 = new Style();
            tagStyle1.setFontFamily("宋体");
            tagStyle1.setColor("000000");
            tagRenderData1.setStyle(tagStyle1);
            text.add(tagRenderData1);

            TextRenderData tagRenderData2 = new TextRenderData();
            tagRenderData2.setText("标识");
            Style tagStyle2 = new Style();
            tagStyle2.setFontFamily("宋体");
            tagStyle2.setColor("000000");
            tagRenderData2.setStyle(tagStyle2);
            text.add(tagRenderData2);

            TextRenderData tagRenderData4 = new TextRenderData();
            tagRenderData4.setText("");
            Style tagStyle4 = new Style();
            tagStyle4.setFontFamily("宋体");
            tagStyle4.setColor("000000");
            tagRenderData4.setStyle(tagStyle4);
            text.add(tagRenderData4);

            TextRenderData tagRenderData5 = new TextRenderData();
            tagRenderData5.setText("");
            Style tagStyle5 = new Style();
            tagStyle5.setFontFamily("宋体");
            tagStyle5.setColor("000000");
            tagRenderData5.setStyle(tagStyle5);
            text.add(tagRenderData5);

            for (int i = 0; i < max; i++) {
                TextRenderData tagRenderData6 = new TextRenderData();
                tagRenderData6.setText("标识" + (i + 1));
                Style tagStyle6 = new Style();
                tagStyle6.setFontFamily("宋体");
                tagStyle6.setColor("000000");
                tagRenderData6.setStyle(tagStyle6);
                text.add(tagRenderData6);
            }
            TextRenderData tagRenderData7 = new TextRenderData();
            tagRenderData7.setText("");
            Style tagStyle7 = new Style();
            tagStyle7.setFontFamily("宋体");
            tagStyle7.setColor("000000");
            tagRenderData7.setStyle(tagStyle7);
            text.add(tagRenderData7);

            TextRenderData[] text4 = text.toArray(new TextRenderData[0]);
            rowRenderData = Rows.of(text4).center().rowAtleastHeight(1).create();
            rows.add(rowRenderData);
            text = new ArrayList<>();


        }

        return text;
    }

    /*****************************************************  大报告   ***************************************************************************/

    /**
     * 创建大报告
     * @param orderId
     * @param insOrder
     * @param
     */
    private void addBitReport(Integer orderId, InsOrder insOrder) {
        //samples是不包括带有"/"的样品
        List<SampleProductDto> samples = insSampleMapper.selectSampleProductListByOrderId(orderId);
        InsReport insReport = new InsReport();
        insReport.setCode(insOrder.getEntrustCode());
        insReport.setInsOrderId(orderId);
        List<Map<String, Object>> tables = new ArrayList<>();
        Set<String> standardMethod = new HashSet<>();
        Set<String> deviceSet = new HashSet<>();
        Set<String> models = new HashSet<>();
        // 查询检验项数量
        AtomicInteger productSize = new AtomicInteger();
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        List<String> resultChList = new ArrayList<>();
        List<String> resultEnList = new ArrayList<>();
        // 判断是否是只有一个样品
        boolean isOneSample = samples.size() == 1 ? true : false;
        /*基础报告(根据绘制的原始记录模版形成)*/
        samples.forEach(a -> {
            models.add(a.getModel());
            String standardMethodCode = baseMapper.getStandardMethodCode(a.getStandardMethodListId());
            if (StrUtil.isNotBlank(a.getSpecialStandardMethod())) {
                standardMethodCode = standardMethodCode + "+" + a.getSpecialStandardMethod();
            }
            standardMethod.add(standardMethodCode);
            for (InsProduct b : a.getInsProduct()) {
                if (b.getInsProductResult() != null) {
                    List<JSONObject> jsonObjects = JSON.parseArray(b.getInsProductResult().getEquipValue(), JSONObject.class);
                    for (JSONObject jsonObject : jsonObjects) {
                        if (!"".equals(jsonObject.get("v") + "")) {
                            List<String> v = StrUtil.split(jsonObject.get("v") + "", "，");
                            deviceSet.addAll(v);
                        }
                    }
                }
            }
            // 收样日期
            List<RowRenderData> rows = new ArrayList<>();
            List<TextRenderData> text = new ArrayList<>();
            RowRenderData rowRenderData = null;

            InsSample insSamples = null;
            int max = 1;
            List<String> cableTags = new ArrayList<>();
            // 判断是否为原材料
            if (insOrder.getTypeSource() != null && insOrder.getTypeSource().equals(1)) {

                InsOrder one = insOrderService.getOne(Wrappers.<InsOrder>lambdaQuery()
                        .eq(InsOrder::getIfsInventoryId, insOrder.getIfsInventoryId())
                        .eq(InsOrder::getOrderType, InsOrderTypeConstants.ENTER_THE_FACTORY)
                        .ne(InsOrder::getState, -1)
                        .last("limit 1"));
                // 判断之前是否有进厂检验
                if (one != null) {
                    insSamples = insSampleMapper.selectOne(Wrappers.<InsSample>lambdaQuery()
                            .eq(InsSample::getInsOrderId, one.getId())
                            .last("limit 1"));
                }

                max = a.getQuantity();
                // 季度检验需要进厂检验的设备
                // 查询是否有进厂检验
                InsOrder order = insOrderMapper.selectOne(Wrappers.<InsOrder>lambdaQuery()
                        .eq(InsOrder::getIfsInventoryId, insOrder.getIfsInventoryId())
                        .ne(InsOrder::getState, -1)
                        .eq(InsOrder::getOrderType, InsOrderTypeConstants.ENTER_THE_FACTORY)
                        .ne(InsOrder::getId, insOrder.getId())
                        .last("limit 1"));
                if (order != null) {
                    List<SampleProductDto> sampleProductDtos = insSampleMapper.selectSampleProductListByOrderId(order.getId());
                    for (InsProduct b : sampleProductDtos.get(0).getInsProduct()) {
                        if (b.getInsProductResult() != null) {
                            List<JSONObject> jsonObjects = JSON.parseArray(b.getInsProductResult().getEquipValue(), JSONObject.class);
                            for (JSONObject jsonObject : jsonObjects) {
                                if (!"".equals(jsonObject.get("v") + "")) {
                                    List<String> v = StrUtil.split(jsonObject.get("v") + "", "，");
                                    deviceSet.addAll(v);
                                }
                            }
                        }
                    }
                }
                insOrder.setSampleView(insOrder.getSample());
                String sampleViewEn = insSampleMapper.getSampleEn(insOrder.getSample());
                if (StringUtils.isBlank(sampleViewEn)) {
                    sampleViewEn = insSampleMapper.getSampleEnByObject(insOrder.getSample());
                }
                insOrder.setSampleViewEn(sampleViewEn);
            } else {
                // 获得批量检验的总数
                max = insOrderMapper.selectSampleMax(a.getId());
                cableTags = insOrderMapper.selectSampleCableTag(a.getId());
            }

            // 表头添加
            addHead(a, text, rowRenderData, rows, max, cableTags);
            text = new ArrayList<>();

            // 添加检测值
            productSize.set(addTestValue(a, text, rowRenderData, rows, max, resultChList, resultEnList, insSamples, cableTags, isOneSample));
            text = new ArrayList<>();

            // 结尾添加
            addEnding(text, rowRenderData, rows, max);
            text = new ArrayList<>();

            List<TableRenderData> tables1 = new ArrayList<>();
            TableRenderData tableRenderData = new TableRenderData();
            tableRenderData.setRows(new ArrayList<>());
            double totalHeight = 0.0; // 用于跟踪当前表格的总行高
            double heightThreshold = 9300.0; // 阈值，
            List<RowRenderData> firstTwoRows = new ArrayList<>(); // 保存前两行以便复制到新表格
            List<RowRenderData> endTwoRows = new ArrayList<>(); // 保存最后一行以便复制到新表格
            // 保存前两行以便复制到新表格
            firstTwoRows.add(rows.get(0));
            firstTwoRows.add(rows.get(1));
            // 保存最后一行
            endTwoRows.add(rows.get(rows.size() - 1));
            for (RowRenderData row : rows) {
                double rowHeight = row.getRowStyle().getHeight(); // 获取当前行的行高
//                // 判断字体内容是否有多的, 多的行高乘倍数
                RowRenderData lastRaw = rows.get(rows.size() - 1);
                // 排除最后一行
                if (rows.get(0) != row && rows.get(1) != row && lastRaw != row) {
                    // 调整高度
                    rowHeight = adjustRowHeight(row, rowHeight, max);
                }
                totalHeight += rowHeight; // 更新总行高
                if (totalHeight >= heightThreshold) {
                    // 创建新表格并复制前两行
                    TableRenderData newTableRenderData = new TableRenderData();
                    newTableRenderData.setRows(new ArrayList<>(firstTwoRows));
                    //设置样式
                    TableStyle tableStyle = new TableStyle();
                    for (int i = 1; i <= max; i++) {
                        // 根据减压那结果个数修改长度
                        switch (i) {
                            case 1:
                                tableStyle.setColWidths(new int[]{650, 2000, 2000, 850, 2200, 2100, 1200});
                                break;
                            case 2:
                                tableStyle.setColWidths(new int[]{650, 1400, 1400, 850, 2100, 1200, 1200, 1200});
                                break;
                            case 3:
                                tableStyle.setColWidths(new int[]{650, 1400, 1400, 850, 1500, 1000, 1000, 1000, 1200});
                                break;
                            case 4:
                                tableStyle.setColWidths(new int[]{650, 1200, 1200, 850, 1400, 850, 850, 850, 850, 1200});
                                break;
                            case 5:
                                tableStyle.setColWidths(new int[]{650, 1100, 1100, 850, 1350, 750, 750, 750, 750, 750, 1200});
                                break;
                        }
                    }
                    tableStyle.setWidth("10000");
                    tableStyle.setAlign(TableRowAlign.CENTER);
                    BorderStyle borderStyle = new BorderStyle();
                    borderStyle.setColor("000000");
                    borderStyle.setType(XWPFTable.XWPFBorderType.THICK);
                    borderStyle.setSize(14);
                    tableStyle.setLeftBorder(borderStyle);
                    tableStyle.setTopBorder(borderStyle);
                    tableStyle.setRightBorder(borderStyle);
                    tableStyle.setBottomBorder(borderStyle);
                    tableRenderData.setTableStyle(tableStyle);
                    newTableRenderData.setTableStyle(tableStyle);

                    // 添加最后一行
                    tableRenderData.addRow(rows.get(rows.size() - 1));

                    tables1.add(tableRenderData);
                    tableRenderData = newTableRenderData;
                    totalHeight = rowHeight;
                }
                tableRenderData.getRows().add(row);
            }
            if (!tableRenderData.getRows().isEmpty() && tableRenderData.getRows().size() != 3) {
                //设置样式
                TableStyle tableStyle = new TableStyle();
                for (int i = 1; i <= max; i++) {
                    // 根据减压那结果个数修改长度
                    switch (i) {
                        case 1:
                            tableStyle.setColWidths(new int[]{650, 2000, 2000, 850, 2200, 2100, 1200});
                            break;
                        case 2:
                            tableStyle.setColWidths(new int[]{650, 1400, 1400, 850, 2100, 1200, 1200, 1200});
                            break;
                        case 3:
                            tableStyle.setColWidths(new int[]{650, 1400, 1400, 850, 1500, 1000, 1000, 1000, 1200});
                            break;
                        case 4:
                            tableStyle.setColWidths(new int[]{650, 1200, 1200, 850, 1400, 850, 850, 850, 850, 1200});
                            break;
                        case 5:
                            tableStyle.setColWidths(new int[]{650, 1100, 1100, 850, 1350, 750, 750, 750, 750, 750, 1200});
                            break;
                    }
                }
                tableStyle.setWidth("10000");
                tableStyle.setAlign(TableRowAlign.CENTER);
                BorderStyle borderStyle = new BorderStyle();
                borderStyle.setColor("000000");
                borderStyle.setType(XWPFTable.XWPFBorderType.THICK);
                borderStyle.setSize(14);
                tableStyle.setLeftBorder(borderStyle);
                tableStyle.setTopBorder(borderStyle);
                tableStyle.setRightBorder(borderStyle);
                tableStyle.setBottomBorder(borderStyle);
                tableRenderData.setTableStyle(tableStyle);
                tables1.add(tableRenderData);
            }
            tables1.forEach(table -> {
                Map<String, Object> tableMap = new HashMap<>();
                tableMap.put("table", table);
                tableMap.put("report", insReport);
                tables.add(tableMap);
            });

        });

        StringBuilder standardMethod2 = new StringBuilder();
        for (String s : standardMethod) {
            standardMethod2.append("、").append(s);
        }
        standardMethod2.replace(0, 1, "");
        tables.forEach(table -> {
            table.put("tableSize", tables.size() + 1);
        });
        // 设备信息
        List<Map<String, String>> deviceList = null;
        if (CollectionUtils.isNotEmpty(deviceSet)) {
            deviceList = insOrderMapper.selectDeviceList(deviceSet);
        }
        if (CollectionUtils.isNotEmpty(deviceList)) {
            int count = 1;
            for (Map<String, String> stringMap : deviceList) {
                stringMap.put("index", String.valueOf(count));
                count++;
            }
        }

        Map<String, String> codeStr = new HashMap<>();
        codeStr.put("报告编号", insReport.getCode());
        codeStr.put("样品名称", insOrder.getSample());
        codeStr.put("规格型号", samples.get(0).getModel());
        codeStr.put("发放日期", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        String modelStr = CollUtil.join(models, "\n");

        // 检测类型
        String orderType = null;
        orderType = iSysDictTypeService.selectLabelByDict(DictDataConstants.CHECK_TYPE, insOrder.getOrderType());
        // 判断第一个字典是否为空
        if (StringUtils.isBlank(orderType)) {
            orderType = iSysDictTypeService.selectLabelByDict(DictDataConstants.CHECK_TYPE1, insOrder.getOrderType());
        }

        // 来样方式
        String formType = iSysDictTypeService.selectLabelByDict(DictDataConstants.FORM_TYPE, insOrder.getFormType());

        // 样品状态
        String sampleStatus = iSysDictTypeService.selectLabelByDict(DictDataConstants.SAMPLE_STATUS_LIST, insOrder.getSampleStatus());
        ;

        // 公司信息
        Custom custom = customMapper.selectById(insOrder.getCompanyId());

        // 查询判断是否有不判定项目,和全都是判定项
        int judgeType = insProductMapper.selectNoJudge(insOrder.getId());
        String resultCh = "";
        String resultEn = "";
        for (String reference : resultChList) {
            resultCh = resultCh + reference;
        }
        for (String reference : resultEnList) {
            resultEn = resultEn + reference;
        }
        switch (judgeType) {
            // 没有不判定项目
            case 1:
                if (StringUtils.isNotBlank(resultCh)) {
                    resultCh = "依据委托要求，" + resultCh + "所检项目不合格，其余所检项目均合格。";
                    resultEn = "According to commissioned requirements," + resultEn + " these inspected items do not meet the requirements, all other inspected items meet the requirements.";
                } else {
                    resultCh = "依据委托要求，所检项目均符合要求。";
                    resultEn = "According to commissioned requirements, all the tested items meet the requirements.";
                }
                break;
            // 存在不判定项目
            case 2:
                if (StringUtils.isNotBlank(resultCh)) {
                    resultCh = "依据委托要求，" + resultCh + "所检项目不合格，其余所检项目除不判定外均符合要求。";
                    resultEn = "According to commissioned requirements," + resultEn + " these inspected items do not meet the requirements, all the inspected items meet the requirements except that they are not judged.";
                } else {
                    resultCh = "依据委托要求，所检项目除不判定外均符合要求。";
                    resultEn = "According to commissioned requirements, all the tested items meet the requirements.";
                }
                break;
            // 都是不判定项目
            case 3:
                resultCh = "依据委托要求，所检项目均不判定。";
                resultEn = "According to commissioned requirements, the items examined are not judged.";
                break;
        }

        /*获取附件图片类型*/
        List<Map<String, Object>> images = new ArrayList<>();
        List<InsOrderFile> insOrderFiles = insOrderFileMapper.selectList(Wrappers.<InsOrderFile>lambdaQuery().eq(InsOrderFile::getType, 1).eq(InsOrderFile::getInsOrderId, orderId));
        if (CollectionUtils.isNotEmpty(insOrderFiles)) {
            insOrderFiles.forEach(insOrderFile -> {
                Map<String, Object> image = new HashMap<>();
                PictureRenderData pictureRenderData = Pictures.ofLocal(imgUrl + "/" + insOrderFile.getFileUrl()).sizeInCm(17, 20).create();
                image.put("url", pictureRenderData);
                image.put("report", insReport);
                images.add(image);
            });
        }
        //委托人和电话字段判断
        if (ObjectUtils.isEmpty(insOrder.getPrepareUser())) {
            insOrder.setPrepareUser("/");
        }
        if (ObjectUtils.isEmpty(insOrder.getPhone())) {
            insOrder.setPhone("/");
        }

        // 抽样日期
        // 成品是抽样时间
        LocalDateTime sendTime = insOrder.getSendTime();
        if (insOrder.getTypeSource() != null && insOrder.getTypeSource().equals(1)) {
            // 原材料收样日期是报检日期
            IfsInventoryQuantity ifsInventoryQuantity = ifsInventoryQuantityMapper.selectById(insOrder.getIfsInventoryId());
            sendTime = ifsInventoryQuantity.getDeclareDate();
        }

        // 检验时间  抽样时间-提交时间
        LocalDateTime now = LocalDateTime.now();
        // 提交时间
        LocalDateTime submitTime = LocalDateTime.now();
        // 判断是否有提交时间
        if (insOrder.getFirstSubmitDate() != null) {
            submitTime = insOrder.getFirstSubmitDate();
        }

        String insTime = insOrder.getSendTime().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")) + "-"
                + submitTime.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));

        String insTimeEn = monthNames[insOrder.getSendTime().getMonthValue() - 1] + " " + insOrder.getSendTime().format(DateTimeFormatter.ofPattern("dd, yyyy")) + "-"
                + monthNames[submitTime.getMonthValue() - 1] + " " + submitTime.format(DateTimeFormatter.ofPattern("dd, yyyy"));

        //检验项目的环境
        String environment = "";
        environment = (ObjectUtils.isNotEmpty(insOrder.getTemperature()) ? insOrder.getTemperature() + "℃ " : "") + (ObjectUtils.isNotEmpty(insOrder.getHumidity()) ? insOrder.getHumidity() + "%" : "");
        String finalEnvironment = environment;
        LocalDateTime finalSendTime = sendTime;
        String finalResultCh = resultCh;
        String finalResultEn = resultEn;
        String finalOrderType = orderType;
        List<Map<String, String>> finalDeviceList = deviceList;
        String finalModelStr = modelStr;

        InputStream inputStream = this.getClass().getResourceAsStream("/static/report-template.docx");
        Configure configure = Configure.builder()
                .bind("deviceList", new HackLoopTableRenderPolicy())
                .useSpringEL(true)
                .build();

        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("order", insOrder);
                    put("report", insReport);
                    put("environment", finalEnvironment);
                    put("custom", custom);
                    put("sampleSize", insOrder.getTestQuantity());
                    put("tables", tables);
                    put("tableSize", tables.size() + 1);
                    put("standardMethod", (standardMethod2.toString().equals("null") ? "" : standardMethod2));
                    put("deviceList", finalDeviceList);
                    put("twoCode", null);
                    put("models", finalModelStr);
                    put("productSize", productSize.get());
                    put("createTime", now.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")));
                    put("createTimeEn", monthNames[now.getMonthValue() - 1] + " " + now.format(DateTimeFormatter.ofPattern("dd, yyyy")));
                    put("insTime", insTime);
                    put("insTimeEn", insTimeEn);
                    put("writeUrl", null);
                    put("insUrl", null);
                    put("images", images);
                    put("examineUrl", null);
                    put("ratifyUrl", null);
                    put("orderType", finalOrderType);
                    put("getTime", finalSendTime.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")));
                    put("getTimeEn", monthNames[finalSendTime.getMonthValue() - 1] + " " + finalSendTime.format(DateTimeFormatter.ofPattern("dd, yyyy")));
                    put("seal1", null);
                    put("seal2", null);
                    put("formTypeCh", formType);
                    put("formTypeEn", insOrder.getFormType());
                    put("resultCh", finalResultCh);
                    put("resultEn", finalResultEn);
                    put("sampleStatus", sampleStatus);
                }});
        try {
            // 修改换行和合并问题
            updaeMerge(template.getXWPFDocument(), false);
            String name = insReport.getCode().replace("/", "") + ".docx";
            template.writeAndClose(Files.newOutputStream(Paths.get(wordUrl + "/" + name)));
            insReport.setUrl("/word/" + name);
            insReport.setWriteUserId(SecurityUtils.getUserId().intValue());//提交人
            insReport.setWriteTime(LocalDateTime.now());//提交时间
            // 查询报告, 判断之前是否添加过, 添加过删除
            insReportMapper.delete(Wrappers.<InsReport>lambdaQuery()
                    .eq(InsReport::getInsOrderId, insOrder.getId()));
            insReportMapper.insert(insReport);
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 调整高度
     * @param row
     * @param rowHeight
     * @return
     */
    private static double adjustRowHeight(RowRenderData row, double rowHeight, int max) {
        // 根据检验项目名称进行高度调整
        TextRenderData renderData = (TextRenderData) row.getCells().get(1).getParagraphs().get(0).getContents().get(0);
        String dataText = renderData.getText();
        TextRenderData valueData = (TextRenderData) row.getCells().get(5).getParagraphs().get(0).getContents().get(0);
        String valueText = valueData.getText();
        // 获取检测内容判断是否超出
        if (StringUtils.isNotBlank(dataText) && dataText.contains("@")) {
            double number = 1;
            double chinaLength = 0;
            double englishLength = 0;
            double valueLength = 0;
            // 根据线芯个数判断距离
            switch (max) {
                case 1:
                    chinaLength = 15;
                    englishLength = 30;
                    valueLength = 8;
                    break;
                case 2:
                    chinaLength = 13;
                    englishLength = 25;
                    valueLength = 6;
                    break;
                case 3:
                    chinaLength = 13;
                    englishLength = 25;
                    valueLength = 4;
                    break;
                case 4:
                    chinaLength = 13;
                    englishLength = 23;
                    valueLength = 3;
                    break;
                case 5:
                    chinaLength = 12;
                    englishLength = 22;
                    valueLength = 2;
                    break;
            }
            // 根据@符号截取中英文
            String[] split = dataText.split("@");
            // 文字倍数
            double chinaMultiple = (Math.ceil(split[0].length() / chinaLength)) - 1;

            // 英文倍数
            double englishMultiple = (Math.ceil(split[1].length() / englishLength)) - 1;
            double multiple = number + chinaMultiple * 0.5 + englishMultiple * 0.5;

            if (StringUtils.isNotBlank(valueText)) {
                double valueMultiple = (Math.ceil(valueText.length() / valueLength)) - 1;
                if (multiple < number + valueMultiple * 0.4) {
                    multiple = number + valueMultiple * 0.4;
                }
            }
            rowHeight = rowHeight * multiple;
        }
        return rowHeight;
    }

    /**
     * 合并单元格
     */
    private void updaeMerge(XWPFDocument document, boolean isSmall) {
        // 处理合并单元格的问题
        // 获取文档中的所有表格
        List<XWPFTable> xwpfTables = document.getTables();
        // 遍历表格
        for (int i = 1; i < xwpfTables.size(); i++) {
            // 创建一个HashSet来存储唯一的字符串（这里基于"∑"分割后的第二部分）
            Set<String> set1 = new HashSet<>();
            // 创建一个HashMap来存储每个唯一字符串及其对应的单元格位置信息
            Map<String, Map<String, Integer>> maps = new HashMap<>();
            // 遍历当前表格的所有行
            for (int j = 0; j < xwpfTables.get(i).getRows().size(); j++) {
                // 遍历当前行的所有单元格
                for (int k = 0; k < xwpfTables.get(i).getRows().get(j).getTableCells().size(); k++) {
                    // 检查单元格文本中是否包含"∑"
                    if (xwpfTables.get(i).getRows().get(j).getTableCells().get(k).getText().indexOf("∑") > -1) {
                        String[] split = xwpfTables.get(i).getRows().get(j).getTableCells().get(k).getText().split("∑");
                        // 如果分割后的第二部分是新的（即之前未出现过），则添加到set1并创建位置信息map
                        if (set1.add(split[1])) {
                            Map<String, Integer> map = new HashMap<>();
                            // 存储起始行、起始列、结束行（当前行）、结束列（当前列）
                            map.put("sr", j);
                            map.put("sc", k);
                            map.put("er", j + 0);
                            map.put("ec", k + 0);
                            maps.put(split[1], map);
                        } else {
                            // 如果已存在，则更新结束行或结束列
                            Map<String, Integer> map1 = maps.get(split[1]);
                            if (j == map1.get("sr")) {
                                map1.put("ec", map1.get("ec") + 1);
                            } else if (k == map1.get("sc")) {
                                map1.put("er", map1.get("er") + 1);
                            }
                        }
                        // 判断小高报告还是大报告
                        if (isSmall) {
                            // 获取单元格
                            XWPFTableCell cell = xwpfTables.get(i).getRows().get(j).getTableCells().get(k);
                            XWPFParagraph paragraph = cell.getParagraphArray(0);
                            String originalText = paragraph.getText();
                            String newText = originalText.split("∑")[0];
                            List<XWPFRun> runs = paragraph.getRuns();
                            for (XWPFRun run : runs) {
                                run.setText("", 0); // 清空 run 中的文本
                            }
                            if (!runs.isEmpty()) {
                                XWPFRun run = runs.get(0);
                                run.setText(newText);

                                // 复制样式
                                run.setFontFamily(paragraph.getRuns().get(0).getFontFamily());
                                run.setFontSize(paragraph.getRuns().get(0).getFontSize());
                                run.setBold(paragraph.getRuns().get(0).isBold());
                                run.setItalic(paragraph.getRuns().get(0).isItalic());
                                run.setUnderline(paragraph.getRuns().get(0).getUnderline());
                                run.setColor(paragraph.getRuns().get(0).getColor());
                            }

                            cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
                            paragraph.setAlignment(ParagraphAlignment.CENTER);
                        } else {
                            // 移除包含"∑"的段落，并重新设置单元格文本和样式
                            String str = xwpfTables.get(i).getRows().get(j).getTableCells().get(k).getText().split("∑")[0];
                            xwpfTables.get(i).getRows().get(j).getTableCells().get(k).removeParagraph(0);
                            xwpfTables.get(i).getRows().get(j).getTableCells().get(k).setText(str);
                            xwpfTables.get(i).getRows().get(j).getTableCells().get(k).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
                            xwpfTables.get(i).getRows().get(j).getTableCells().get(k).getParagraphArray(0).setAlignment(ParagraphAlignment.CENTER);
                        }
                    }
                }
            }

            // 单元格排序, 避免格式错乱
            List<Map.Entry<String, Map<String, Integer>>> entries = new ArrayList<>(maps.entrySet());
            entries.sort((o1, o2) -> o1.getValue().get("sc") - o2.getValue().get("sc"));

            // 按照顺序添加进集合
            List<String> list = new ArrayList<>();
            for (Map.Entry<String, Map<String, Integer>> entry : entries) {
                list.add(entry.getKey());
            }

            for (int a = list.size() - 1; a >= 0; a--) {
                Map<String, Integer> v = maps.get(list.get(a));
                for (int j = 0; j < v.get("er") - v.get("sr") + 1; j++) {
                    if (v.get("ec") > v.get("sc")) {
                        try {
                            TableTools.mergeCellsHorizonal(xwpfTables.get(i), v.get("sr") + j, v.get("sc"), v.get("ec"));
                        } catch (Exception e) {
                        }
                    }
                }
                if (v.get("er") > v.get("sr")) {
                    try {
                        TableTools.mergeCellsVertically(xwpfTables.get(i), v.get("sc"), v.get("sr"), v.get("er"));
                    } catch (Exception e) {
                    }
                }
            }
        }

        //处理中英文换行的问题

        List<XWPFTable> xwpfTables1 = document.getTables();
        for (int i = 1; i < xwpfTables1.size(); i++) {
            for (int j = 0; j < xwpfTables1.get(i).getRows().size(); j++) {
                for (int k = 0; k < xwpfTables1.get(i).getRows().get(j).getTableCells().size(); k++) {
                    if (xwpfTables1.get(i).getRows().get(j).getTableCells().get(k).getText().contains("@")) {
                        if (isSmall) {
                            // 获取原有段落的第一个 XWPFRun
                            String text = xwpfTables1.get(i).getRows().get(j).getTableCells().get(k).getText();
                            XWPFParagraph oldParagraph = xwpfTables1.get(i).getRows().get(j).getTableCells().get(k).getParagraphs().get(0);
                            XWPFRun oldRun = oldParagraph.getRuns().get(0);
                            // 保存原有样式
                            String fontFamily = oldRun.getFontFamily();
                            int fontSize = oldRun.getFontSize();
                            boolean isBold = oldRun.isBold();
                            boolean isItalic = oldRun.isItalic();
                            boolean isUnderline = oldRun.getUnderline() != UnderlinePatterns.NONE;
                            // 删除原有段落
                            xwpfTables1.get(i).getRows().get(j).getTableCells().get(k).removeParagraph(0);
                            // 添加新段落
                            XWPFParagraph newParagraph = xwpfTables1.get(i).getRows().get(j).getTableCells().get(k).addParagraph();
                            XWPFRun newRun = newParagraph.createRun();
                            // 应用保存的样式
                            newRun.setFontFamily(fontFamily);
                            newRun.setFontSize(fontSize);
                            newRun.setBold(isBold);
                            newRun.setItalic(isItalic);
                            if (isUnderline) {
                                newRun.setUnderline(UnderlinePatterns.SINGLE);
                            }
                            // 设置新文本
                            String[] split = text.split("@");
                            newRun.setText(split[0]);
                            if (split.length > 1) {
                                newRun.addBreak();
                                newRun.setText(split[1]);
                            }
                            // 设置段落对齐方式
                            newParagraph.setAlignment(ParagraphAlignment.CENTER);
                        } else {
                            String text = xwpfTables1.get(i).getRows().get(j).getTableCells().get(k).getText();
                            String[] split = text.split("@");
                            xwpfTables1.get(i).getRows().get(j).getTableCells().get(k).removeParagraph(0);
                            XWPFParagraph xwpfParagraph = xwpfTables1.get(i).getRows().get(j).getTableCells().get(k).addParagraph();
                            XWPFRun run = xwpfParagraph.createRun();
                            run.setText(split[0]);
                            if (ObjectUtils.isNotNull(split[1])) {
                                run.addBreak();
                                run.setText(split[1]);
                            }
                            xwpfParagraph.setAlignment(ParagraphAlignment.CENTER);
                        }
                    }
                }
            }
        }
    }


    /**
     * 添加报告表头
     * @param sample
     * @param text
     * @param rowRenderData
     * @param rows
     * @param max
     * @param cableTags 线芯颜色
     */
    private static void addHead(SampleProductDto sample, List<TextRenderData> text, RowRenderData rowRenderData, List<RowRenderData> rows, int max, List<String> cableTags) {
        // 第一行
        TextRenderData firstRenderData1 = new TextRenderData();
        firstRenderData1.setText("样品编号@Sample No.∑1");
        Style firstStyle1 = new Style();
        firstStyle1.setFontFamily("宋体");
        firstStyle1.setColor("000000");
        firstRenderData1.setStyle(firstStyle1);
        text.add(firstRenderData1);

        TextRenderData firstRenderData2 = new TextRenderData();
        firstRenderData2.setText("样品编号@Sample No.∑1");
        Style firstStyle2 = new Style();
        firstStyle2.setFontFamily("宋体");
        firstStyle2.setColor("000000");
        firstRenderData2.setStyle(firstStyle2);
        text.add(firstRenderData2);

        TextRenderData firstRenderData3 = new TextRenderData();
        firstRenderData3.setText(sample.getSampleCode() + "∑2");
        Style firstStyle3 = new Style();
        firstStyle3.setFontFamily("宋体");
        firstStyle3.setColor("000000");
        firstRenderData3.setStyle(firstStyle3);
        text.add(firstRenderData3);

        TextRenderData firstRenderData4 = new TextRenderData();
        firstRenderData4.setText(sample.getSampleCode() + "∑2");
        Style firstStyle4 = new Style();
        firstStyle4.setFontFamily("宋体");
        firstStyle4.setColor("000000");
        firstRenderData4.setStyle(firstStyle4);
        text.add(firstRenderData4);


        // 判断循环条件

        if (max <= 2) {
            for (int i = 0; i < max; i++) {
                TextRenderData firstRenderData5 = new TextRenderData();
                firstRenderData5.setText("规格型号@Type∑3");
                Style firstStyle5 = new Style();
                firstStyle5.setFontFamily("宋体");
                firstStyle5.setColor("000000");
                firstRenderData4.setStyle(firstStyle5);
                text.add(firstRenderData5);
            }
            for (int i = 0; i < 2; i++) {
                TextRenderData firstRenderData6 = new TextRenderData();
                firstRenderData6.setText(sample.getModel() + "∑4");
                Style firstStyle6 = new Style();
                firstStyle6.setFontFamily("宋体");
                firstStyle6.setColor("000000");
                firstRenderData6.setStyle(firstStyle6);
                text.add(firstRenderData6);
            }
        } else {
            for (int i = 0; i < 2; i++) {
                TextRenderData firstRenderData5 = new TextRenderData();
                firstRenderData5.setText("规格型号@Type∑3");
                Style firstStyle5 = new Style();
                firstStyle5.setFontFamily("宋体");
                firstStyle5.setColor("000000");
                firstRenderData4.setStyle(firstStyle5);
                text.add(firstRenderData5);
            }
            for (int i = 0; i < max; i++) {
                TextRenderData firstRenderData6 = new TextRenderData();
                firstRenderData6.setText(sample.getModel() + "∑4");
                Style firstStyle6 = new Style();
                firstStyle6.setFontFamily("宋体");
                firstStyle6.setColor("000000");
                firstRenderData6.setStyle(firstStyle6);
                text.add(firstRenderData6);
            }

        }
        TextRenderData[] text2 = text.toArray(new TextRenderData[0]);
        rowRenderData = Rows.of(text2).center().rowAtleastHeight(1).create();
        rows.add(rowRenderData);
        text = new ArrayList<>();

        // 第二行
        TextRenderData headRenderData1 = new TextRenderData();
        headRenderData1.setText("序号@NO.");
        Style headStyle1 = new Style();
        headStyle1.setFontFamily("宋体");
        headStyle1.setColor("000000");
        headRenderData1.setStyle(headStyle1);
        text.add(headRenderData1);

        TextRenderData headRenderData2 = new TextRenderData();
        headRenderData2.setText("检验项目@Test item∑5");
        Style headStyle2 = new Style();
        headStyle2.setFontFamily("宋体");
        headStyle2.setColor("000000");
        headRenderData2.setStyle(headStyle2);
        text.add(headRenderData2);

        TextRenderData headRenderData3 = new TextRenderData();
        headRenderData3.setText("检验项目@Test item∑5");
        Style headStyle3 = new Style();
        headStyle3.setFontFamily("宋体");
        headStyle3.setColor("000000");
        headRenderData3.setStyle(headStyle3);
        text.add(headRenderData3);

        TextRenderData headRenderData4 = new TextRenderData();
        headRenderData4.setText("单位@Unit");
        Style headStyle4 = new Style();
        headStyle4.setFontFamily("宋体");
        headStyle4.setColor("000000");
        headRenderData4.setStyle(headStyle4);
        text.add(headRenderData4);

        TextRenderData headRenderData5 = new TextRenderData();
        headRenderData5.setText("标准要求@Requirement");
        Style headStyle5 = new Style();
        headStyle5.setFontFamily("宋体");
        headStyle5.setColor("000000");
        headRenderData5.setStyle(headStyle5);
        text.add(headRenderData5);

        for (int i = 0; i < max; i++) {
            TextRenderData headRenderData6 = new TextRenderData();
            headRenderData6.setText("检验结果@Test result∑6");
            Style headStyle6 = new Style();
            headStyle6.setFontFamily("宋体");
            headStyle6.setColor("000000");
            headRenderData6.setStyle(headStyle6);
            text.add(headRenderData6);
        }
        TextRenderData headRenderData7 = new TextRenderData();
        headRenderData7.setText("结论@Conclusion");
        Style headStyle7 = new Style();
        headStyle7.setFontFamily("宋体");
        headStyle7.setColor("000000");
        headRenderData7.setStyle(headStyle7);
        text.add(headRenderData7);

        TextRenderData[] text3 = text.toArray(new TextRenderData[0]);
        rowRenderData = Rows.of(text3).center().rowAtleastHeight(1).create();
        rows.add(rowRenderData);
        text = new ArrayList<>();

        // 线芯颜色
        if (CollectionUtils.isNotEmpty(cableTags)) {
            TextRenderData cableRenderData1 = new TextRenderData();
            cableRenderData1.setText("");
            Style cableStyle1 = new Style();
            cableStyle1.setFontFamily("宋体");
            cableStyle1.setColor("000000");
            cableRenderData1.setStyle(cableStyle1);
            text.add(cableRenderData1);

            TextRenderData cableRenderData2 = new TextRenderData();
            cableRenderData2.setText("绝缘线芯颜色和标志@Insulation core color and mark∑200");
            Style cableStyle2 = new Style();
            cableStyle2.setFontFamily("宋体");
            cableStyle2.setColor("000000");
            cableRenderData2.setStyle(cableStyle2);
            text.add(cableRenderData2);

            TextRenderData cableRenderData3 = new TextRenderData();
            cableRenderData3.setText("绝缘线芯颜色和标志@Insulation core color and mark∑200");
            Style cableStyle3 = new Style();
            cableStyle3.setFontFamily("宋体");
            cableStyle3.setColor("000000");
            cableRenderData3.setStyle(cableStyle3);
            text.add(cableRenderData3);

            TextRenderData cableRenderData4 = new TextRenderData();
            cableRenderData4.setText("");
            Style cableStyle4 = new Style();
            cableStyle4.setFontFamily("宋体");
            cableStyle4.setColor("000000");
            cableRenderData4.setStyle(cableStyle4);
            text.add(cableRenderData4);

            TextRenderData cableRenderData5 = new TextRenderData();
            cableRenderData5.setText("");
            Style cableStyle5 = new Style();
            cableStyle5.setFontFamily("宋体");
            cableStyle5.setColor("000000");
            cableRenderData5.setStyle(cableStyle5);
            text.add(cableRenderData5);

            for (String cableTag : cableTags) {
                TextRenderData cableRenderData6 = new TextRenderData();
                cableRenderData6.setText(cableTag);
                Style cableStyle6 = new Style();
                cableStyle6.setFontFamily("宋体");
                cableStyle6.setColor("000000");
                cableRenderData6.setStyle(cableStyle6);
                text.add(cableRenderData6);
            }
            TextRenderData cableRenderData7 = new TextRenderData();
            cableRenderData7.setText("-");
            Style cableStyle7 = new Style();
            cableStyle7.setFontFamily("宋体");
            cableStyle7.setColor("000000");
            cableRenderData7.setStyle(cableStyle7);
            text.add(cableRenderData7);

            TextRenderData[] text4 = text.toArray(new TextRenderData[0]);
            rowRenderData = Rows.of(text4).center().rowAtleastHeight(1).create();
            rows.add(rowRenderData);
            text = new ArrayList<>();
        }


    }


    /**
     * 添加检测值
     * @param a   当前样品
     * @param text
     * @param rowRenderData
     * @param rows
     * @param max  检验数量
     * @param resultChList  不符合信息中文
     * @param resultEhList  不符合信息英文
     * @param insSamples
     * @param cableTags 线芯颜色
     * @param isOneSample 判断是否是只有一个样品
     */
    private int addTestValue(SampleProductDto a,
                             List<TextRenderData> text,
                             RowRenderData rowRenderData,
                             List<RowRenderData> rows,
                             int max,
                             List<String> resultChList,
                             List<String> resultEhList,
                             InsSample insSamples, List<String> cableTags,
                             boolean isOneSample) {
        // 检验项总数
        AtomicInteger productSize = new AtomicInteger(1);
        // 不合格信息
        AtomicReference<String> resultCh = new AtomicReference<>("");
        AtomicReference<String> resultEn = new AtomicReference<>("");
        // 查询检验结果
        List<Integer> ids = new ArrayList<>();
        ids.add(a.getId());
        List<SampleProductExportDto> sampleProductDto2s = insOrderMapper.selectSampleBySampleId(ids);

        // 判断是否是季度检验, 且有没有进厂检验, 有进厂检验进厂检验的信息赋值到季度上
        if (insSamples != null) {
            // 查询检验内容
            List<SampleProductExportDto> enterDto = insOrderMapper.selectSampleBySampleIdOne(insSamples.getId());
            // 排除掉进厂报告重复的
            enterDto = enterDto.stream().filter(sampleProductDto2 -> {
                // 判断检验项检验子项是否一样, 一样就直接返回false排除
                return !sampleProductDto2s.stream().anyMatch(dto -> {
                    String quarter = sampleProductDto2.getInspectionItem() + sampleProductDto2.getInspectionItemSubclass();
                    String enter = dto.getInspectionItem() + dto.getInspectionItemSubclass();
                    return quarter.equals(enter);
                });
            }).collect(Collectors.toList());
            sampleProductDto2s.addAll(enterDto);
        }

        // 格式化修改检验项
        this.formatProducts(sampleProductDto2s);

        List<SampleProductExportDto> sampleList = new ArrayList<>();

        // 获取检验项分类的
        Map<String, List<SampleProductExportDto>> itemClassMap = sampleProductDto2s.stream().filter(sampleProductDto2 -> StringUtils.isNotBlank(sampleProductDto2.getInspectionItemClass()))
                .collect(Collectors.groupingBy(SampleProductExportDto::getInspectionItemClass));

        List<Map.Entry<String, List<SampleProductExportDto>>> itemClassEntries = new ArrayList<>(itemClassMap.entrySet());
        itemClassEntries.sort((o1, o2) -> {
            // 再比较 sort
            Integer sort1 = o1.getValue().get(0).getSort();
            Integer sort2 = o2.getValue().get(0).getSort();
            sort1 = sort1 == null ? 0 : sort1;
            sort2 = sort2 == null ? 0 : sort2;
            return sort1.compareTo(sort2);
        });
        // 创建一个 LinkedHashMap 来保持插入顺序
        Map<String, List<SampleProductExportDto>> itemClassList = new LinkedHashMap<>();
        for (Map.Entry<String, List<SampleProductExportDto>> entry : itemClassEntries) {
            itemClassList.put(entry.getKey(), entry.getValue());
        }

        if (CollectionUtils.isNotEmpty(itemClassList)) {
            AtomicInteger finalIndex = new AtomicInteger(1);

            // 获取检验项分类相同的
            itemClassList.forEach((s, sampleClass) -> {
                // 添加检验分类
                SampleProductExportDto classDto = new SampleProductExportDto();
                // 判断是否有英文名称
                SampleProductExportDto temp = sampleClass.get(0);
                if (StringUtils.isNotBlank(temp.getInspectionItemClassEN())) {
                    classDto.setInspectionName(temp.getInspectionItemClass() + "@" + temp.getInspectionItemClassEN());
                } else {
                    classDto.setInspectionName(temp.getInspectionItemClass());
                }
                classDto.setIndex(Integer.toString(finalIndex.get()));
                sampleList.add(classDto);

                // 添加检验项
                Map<String, List<SampleProductExportDto>> sortedMap = sampleClass.stream()
                        .filter(sampleProductDto2 -> StringUtils.isNotBlank(sampleProductDto2.getInspectionItem()))
                        .collect(Collectors.groupingBy(SampleProductExportDto::getInspectionItemRadius));

                List<Map.Entry<String, List<SampleProductExportDto>>> entries = new ArrayList<>(sortedMap.entrySet());
                entries.sort((o1, o2) -> {
                    // 再比较 sort
                    Integer sort1 = o1.getValue().get(0).getSort();
                    Integer sort2 = o2.getValue().get(0).getSort();
                    sort1 = sort1 == null ? 0 : sort1;
                    sort2 = sort2 == null ? 0 : sort2;
                    return sort1.compareTo(sort2);
                });
                // 创建一个 LinkedHashMap 来保持插入顺序
                Map<String, List<SampleProductExportDto>> item = new LinkedHashMap<>();
                for (Map.Entry<String, List<SampleProductExportDto>> entry : entries) {
                    item.put(entry.getKey(), entry.getValue());
                }

                AtomicInteger belowIndex = new AtomicInteger(1);
                item.forEach((s2, sampleProductDtoInside) -> {
                    // 添加检验项
                    SampleProductExportDto dto2 = new SampleProductExportDto();
                    // 判断是否有英文名称
                    SampleProductExportDto temp2 = sampleProductDtoInside.get(0);
                    if (StringUtils.isNotBlank(temp2.getInspectionItemEn())) {
                        dto2.setInspectionName(temp2.getInspectionItem() + "@" + temp2.getInspectionItemEn());
                    } else {
                        dto2.setInspectionName(temp2.getInspectionItem());
                    }
                    // 判断有没有条件字段填充到要求
                    if (StringUtils.isNotBlank(temp2.getRadius())) {
                        dto2.setTell("(" + temp2.getRadius() + ")");
                    }
                    // 判断是否有检验子项,没有就把检验参数填充
                    dto2.setIndex(finalIndex.get() + "." + belowIndex.get());
                    if (StringUtils.isBlank(temp2.getInspectionItemSubclass())) {
                        // 处理集合
                        Map<String, SampleProductExportDto> map = new LinkedHashMap<>();
                        for (SampleProductExportDto productDto2 : sampleProductDtoInside) {
                            String productName = productDto2.getInspectionItemClass() + productDto2.getInspectionItem();
                            if (map.containsKey(productName)) {
                                // 如果名称已经存在，添加 lastValue 值到 lastValueList 列表
                                map.get(productName)
                                        .getLastValueList()
                                        .add(productDto2.getLastValue());
                                map.get(productName)
                                        .getInsResultList()
                                        .add(productDto2.getInsResult());
                                // 添加颜色对应的值和结果
                                if (StringUtils.isNotBlank(productDto2.getCableTag())) {
                                    map.get(productName)
                                            .getCableTagValueMap()
                                            .put(productDto2.getCableTag(), productDto2.getLastValue());
                                    map.get(productName)
                                            .getCableTagResultMap()
                                            .put(productDto2.getCableTag(), productDto2.getInsResult());
                                }
                                // 添加标准要求来确定唯一性
                                map.get(productName).getTellSet().add(productDto2.getTell());
                            } else {
                                // 如果名称不存在，直接放入 map
                                productDto2.setLastValueList(new ArrayList<>()); // 检验内容
                                productDto2.getLastValueList().add(productDto2.getLastValue());
                                productDto2.setInsResultList(new ArrayList<>()); // 结果
                                productDto2.getInsResultList().add(productDto2.getInsResult());
                                productDto2.setTellSet(new LinkedHashSet<>());
                                productDto2.getTellSet().add(productDto2.getTell());// 标准要求

                                // 判断是否有线芯颜色, 添加颜色对应的值和结果
                                productDto2.setCableTagValueMap(new TreeMap<>());
                                productDto2.setCableTagResultMap(new TreeMap<>());
                                if (StringUtils.isNotBlank(productDto2.getCableTag())) {
                                    productDto2.getCableTagValueMap().put(productDto2.getCableTag(), productDto2.getLastValue());
                                    productDto2.getCableTagResultMap().put(productDto2.getCableTag(), productDto2.getInsResult());
                                }

                                map.put(productName, productDto2);
                            }
                        }
                        List<SampleProductExportDto> result = new ArrayList<>(map.values());


                        //添加检验项
                        SampleProductExportDto temp3 = result.get(0);
                        String itemName = temp3.getInspectionItem();
                        if (StringUtils.isNotBlank(temp3.getRadius())) {
                            itemName = itemName + "(" + temp3.getRadius() + ")";
                        }
                        // 判断有没有条件字段有需要拼接到名称后面
                        if (StringUtils.isNotBlank(temp3.getInspectionItemEn())) {
                            temp3.setInspectionName(itemName + "@" + temp3.getInspectionItemEn());
                        } else {
                            temp3.setInspectionName(itemName);
                        }
                        // 判断有没有条件字段有需要拼接到名称后面
                        // 判断有没有条件字段填充到要求
                        if (StringUtils.isNotBlank(temp3.getRadius())) {
                            temp3.setTell("(" + temp3.getRadius() + ")");
                        }
                        temp3.setIndex(finalIndex.get() + "." + belowIndex.get());
                        sampleList.add(temp3);
                    } else {
                        sampleList.add(dto2);
                        // 现在需要根据子项去重累加检验结果
                        // 处理集合
                        Map<String, SampleProductExportDto> map = new LinkedHashMap<>();
                        for (SampleProductExportDto productDto2 : sampleProductDtoInside) {
                            String productName = productDto2.getInspectionItemClass() + productDto2.getInspectionItem() + productDto2.getInspectionItemSubclass();
                            if (map.containsKey(productName)) {
                                // 如果名称已经存在，添加 lastValue 值到 lastValueList 列表
                                map.get(productName)
                                        .getLastValueList()
                                        .add(productDto2.getLastValue());
                                map.get(productName)
                                        .getInsResultList()
                                        .add(productDto2.getInsResult());
                                // 添加颜色对应的值和结果
                                if (StringUtils.isNotBlank(productDto2.getCableTag())) {
                                    map.get(productName)
                                            .getCableTagValueMap()
                                            .put(productDto2.getCableTag(), productDto2.getLastValue());
                                    map.get(productName)
                                            .getCableTagResultMap()
                                            .put(productDto2.getCableTag(), productDto2.getInsResult());
                                }
                                // 添加标准要求来确定唯一性
                                map.get(productName).getTellSet().add(productDto2.getTell());
                            } else {
                                // 如果名称不存在，直接放入 map
                                productDto2.setLastValueList(new ArrayList<>()); // 检验内容
                                productDto2.getLastValueList().add(productDto2.getLastValue());
                                productDto2.setInsResultList(new ArrayList<>()); // 结果
                                productDto2.getInsResultList().add(productDto2.getInsResult());
                                productDto2.setTellSet(new LinkedHashSet<>());
                                productDto2.getTellSet().add(productDto2.getTell());// 标准要求

                                // 判断是否有线芯颜色, 添加颜色对应的值和结果
                                productDto2.setCableTagValueMap(new TreeMap<>());
                                productDto2.setCableTagResultMap(new TreeMap<>());
                                if (StringUtils.isNotBlank(productDto2.getCableTag())) {
                                    productDto2.getCableTagValueMap().put(productDto2.getCableTag(), productDto2.getLastValue());
                                    productDto2.getCableTagResultMap().put(productDto2.getCableTag(), productDto2.getInsResult());
                                }

                                map.put(productName, productDto2);
                            }
                        }
                        List<SampleProductExportDto> result = new ArrayList<>(map.values());

                        //添加检验子项
                        for (SampleProductExportDto productDto2 : result) {
                            if (StringUtils.isNotBlank(productDto2.getInspectionItemSubclassEn())) {
                                productDto2.setInspectionName("--" + productDto2.getInspectionItemSubclass() + "@"
                                        + "--" + productDto2.getInspectionItemSubclassEn());
                            } else {
                                productDto2.setInspectionName("--" + productDto2.getInspectionItemSubclass());
                            }
                            sampleList.add(productDto2);
                        }
                    }
                    belowIndex.incrementAndGet();
                    productSize.incrementAndGet();
                });
                finalIndex.incrementAndGet();
            });
        } else {
            // 转成Mpa进行排序
            Map<String, List<SampleProductExportDto>> sortedMap = sampleProductDto2s.stream()
                    .filter(sampleProductDto2 -> StringUtils.isNotBlank(sampleProductDto2.getInspectionItem()))
                    .collect(Collectors.groupingBy(SampleProductExportDto::getInspectionItemRadius));
            List<Map.Entry<String, List<SampleProductExportDto>>> entries = new ArrayList<>(sortedMap.entrySet());
            entries.sort((o1, o2) -> {
                Integer sort1 = o1.getValue().get(0).getSort();
                Integer sort2 = o2.getValue().get(0).getSort();
                sort1 = sort1 == null ? 0 : sort1;
                sort2 = sort2 == null ? 0 : sort2;
                return sort1.compareTo(sort2);
            });
            // 创建一个 LinkedHashMap 来保持插入顺序
            Map<String, List<SampleProductExportDto>> item = new LinkedHashMap<>();
            for (Map.Entry<String, List<SampleProductExportDto>> entry : entries) {
                item.put(entry.getKey(), entry.getValue());
            }

            AtomicInteger finalIndex = new AtomicInteger(1);
            item.forEach((s, sampleProductDtoInside) -> {
                // 添加检验项
                SampleProductExportDto dto2 = new SampleProductExportDto();
                // 判断是否有英文名称
                SampleProductExportDto temp = sampleProductDtoInside.get(0);

                // 判断是否有检验子项,没有就把检验参数填充
                dto2.setIndex(Integer.toString(finalIndex.get()));
                if (StringUtils.isBlank(temp.getInspectionItemSubclass())) {
                    // 处理集合
                    Map<String, SampleProductExportDto> map = new LinkedHashMap<>();
                    for (SampleProductExportDto productDto2 : sampleProductDtoInside) {
                        String productName = productDto2.getInspectionItemClass() + productDto2.getInspectionItem();
                        if (map.containsKey(productName)) {
                            // 如果名称已经存在，添加 lastValue 值到 lastValueList 列表
                            map.get(productName)
                                    .getLastValueList()
                                    .add(productDto2.getLastValue());
                            map.get(productName)
                                    .getInsResultList()
                                    .add(productDto2.getInsResult());
                            // 添加颜色对应的值和结果
                            if (StringUtils.isNotBlank(productDto2.getCableTag())) {
                                map.get(productName)
                                        .getCableTagValueMap()
                                        .put(productDto2.getCableTag(), productDto2.getLastValue());
                                map.get(productName)
                                        .getCableTagResultMap()
                                        .put(productDto2.getCableTag(), productDto2.getInsResult());
                            }
                            // 添加标准要求来确定唯一性
                            map.get(productName).getTellSet().add(productDto2.getTell());
                        } else {
                            // 如果名称不存在，直接放入 map
                            productDto2.setLastValueList(new ArrayList<>()); // 检验内容
                            productDto2.getLastValueList().add(productDto2.getLastValue());
                            productDto2.setInsResultList(new ArrayList<>()); // 结果
                            productDto2.getInsResultList().add(productDto2.getInsResult());
                            productDto2.setTellSet(new LinkedHashSet<>());
                            productDto2.getTellSet().add(productDto2.getTell());// 标准要求

                            // 判断是否有线芯颜色, 添加颜色对应的值和结果
                            productDto2.setCableTagValueMap(new TreeMap<>());
                            productDto2.setCableTagResultMap(new TreeMap<>());
                            if (StringUtils.isNotBlank(productDto2.getCableTag())) {
                                productDto2.getCableTagValueMap().put(productDto2.getCableTag(), productDto2.getLastValue());
                                productDto2.getCableTagResultMap().put(productDto2.getCableTag(), productDto2.getInsResult());
                            }

                            map.put(productName, productDto2);
                        }
                    }
                    List<SampleProductExportDto> result = new ArrayList<>(map.values());

                    //添加检验子项
                    SampleProductExportDto temp2 = result.get(0);
                    String itemName = temp2.getInspectionItem();
                    if (StringUtils.isNotBlank(temp2.getRadius())) {
                        itemName = itemName + "(" + temp2.getRadius() + ")";
                    }
                    // 判断有没有条件字段有需要拼接到名称后面
                    if (StringUtils.isNotBlank(temp2.getInspectionItemEn())) {
                        temp2.setInspectionName(itemName + "@" + temp2.getInspectionItemEn());
                    } else {
                        temp2.setInspectionName(itemName);
                    }
                    // 判断有没有条件字段有需要拼接到名称后面
                    temp2.setIndex(Integer.toString(finalIndex.get()));
                    sampleList.add(temp2);
                } else {
                    if (StringUtils.isNotBlank(temp.getInspectionItemEn())) {
                        dto2.setInspectionName(temp.getInspectionItem() + "@" + temp.getInspectionItemEn());
                    } else {
                        dto2.setInspectionName(temp.getInspectionItem());
                    }
                    // 填充到要求
                    if (StringUtils.isNotBlank(temp.getRadius())) {
                        dto2.setTell("(" + temp.getRadius() + ")");
                    }
                    sampleList.add(dto2);

                    // 现在需要根据子项去重累加检验结果
                    // 处理集合
                    Map<String, SampleProductExportDto> map = new LinkedHashMap<>();
                    for (SampleProductExportDto productDto2 : sampleProductDtoInside) {
                        // 名字取三层唯一
                        String productName = productDto2.getInspectionItemClass() + productDto2.getInspectionItem() + productDto2.getInspectionItemSubclass();
                        if (map.containsKey(productName)) {
                            // 如果名称已经存在，添加 lastValue 值到 lastValueList 列表
                            map.get(productName)
                                    .getLastValueList()
                                    .add(productDto2.getLastValue());
                            map.get(productName)
                                    .getInsResultList()
                                    .add(productDto2.getInsResult());
                            // 添加颜色对应的值和结果
                            if (StringUtils.isNotBlank(productDto2.getCableTag())) {
                                map.get(productName)
                                        .getCableTagValueMap()
                                        .put(productDto2.getCableTag(), productDto2.getLastValue());
                                map.get(productName)
                                        .getCableTagResultMap()
                                        .put(productDto2.getCableTag(), productDto2.getInsResult());
                            }
                            // 添加标准要求来确定唯一性
                            map.get(productName).getTellSet().add(productDto2.getTell());
                        } else {
                            // 如果名称不存在，直接放入 map
                            productDto2.setLastValueList(new ArrayList<>()); // 检验内容
                            productDto2.getLastValueList().add(productDto2.getLastValue());
                            productDto2.setInsResultList(new ArrayList<>()); // 结果
                            productDto2.getInsResultList().add(productDto2.getInsResult());
                            productDto2.setTellSet(new LinkedHashSet<>());
                            productDto2.getTellSet().add(productDto2.getTell());// 标准要求
                            productDto2.setCableTagValueMap(new TreeMap<>());
                            // 判断是否有线芯颜色, 添加颜色对应的值和结果
                            productDto2.setCableTagValueMap(new TreeMap<>());
                            productDto2.setCableTagResultMap(new TreeMap<>());
                            if (StringUtils.isNotBlank(productDto2.getCableTag())) {
                                productDto2.getCableTagValueMap().put(productDto2.getCableTag(), productDto2.getLastValue());
                                productDto2.getCableTagResultMap().put(productDto2.getCableTag(), productDto2.getInsResult());
                            }
                            map.put(productName, productDto2);
                        }
                    }
                    List<SampleProductExportDto> result = new ArrayList<>(map.values());

                    //添加检验子项
                    for (SampleProductExportDto productDto2 : result) {
                        if (StringUtils.isNotBlank(productDto2.getInspectionItemSubclassEn())) {
                            productDto2.setInspectionName("--" + productDto2.getInspectionItemSubclass() + "@"
                                    + "--" + productDto2.getInspectionItemSubclassEn());
                        } else {
                            productDto2.setInspectionName("--" + productDto2.getInspectionItemSubclass());
                        }
                        sampleList.add(productDto2);
                    }
                }
                finalIndex.incrementAndGet();
            });
            productSize.set(finalIndex.get());
        }

        if (CollectionUtils.isEmpty(sampleList)) {
            return productSize.get() - 1;
        }

        /* 检验项map, 如果不合格判断是否有大类和检验项, 有大类不需要重叠, 如(绝缘机械物理性能老化前断裂伸长率、绝缘机械物理性能老化前抗张强度)
         *  需要修改成绝缘机械物理性能老化前断裂伸长率、抗张强度
         * */
        // 检验项分类      检验项  检验子项
        Map<String, LinkedHashMap<String, List<String>>> errorClassItemMapCn = new LinkedHashMap<>();
        Map<String, LinkedHashMap<String, List<String>>> errorClassItemMapEn = new LinkedHashMap<>();

        // 中间检测值添加
        for (int i = 0; i < sampleList.size(); i++) {
            SampleProductExportDto sample = sampleList.get(i);

            String fixedValue = "(100℃，168h)";
            // 判断是否是  拉伸强度变化率(100℃，168h)  和  断裂拉伸应变变化率(100℃，168h) 需要去除条件
            if (StrUtil.isNotBlank(sample.getInspectionName())
                    && sample.getInspectionName().contains("断裂拉伸应变变化率(100℃，168h)")) {
                sample.setInspectionName(sample.getInspectionName().replace(fixedValue, ""));
            }
            if (StrUtil.isNotBlank(sample.getInspectionName())
                    && sample.getInspectionName().contains("拉伸强度变化率(100℃，168h)")) {
                sample.setInspectionName(sample.getInspectionName().replace(fixedValue, ""));
            }
            if (StrUtil.isNotBlank(sample.getInspectionName())
                    && sample.getInspectionName().contains("老化后机械性能(100℃，168h)")) {
                sample.setInspectionName(sample.getInspectionName().replace(fixedValue, ""));
            }

            // 判断是否是  耐矿物油Ⅰ（150℃ 72h)  和  耐矿物油Ⅰ（150℃ 144h) 需要去除条件
            if (StrUtil.isNotBlank(sample.getInspectionName())
                    && sample.getInspectionName().contains("耐矿物油Ⅰ（150℃ 72h)")) {
                sample.setInspectionName(sample.getInspectionName().replace("（150℃ 72h)", ""));
            }
            if (StrUtil.isNotBlank(sample.getInspectionName())
                    && sample.getInspectionName().contains("耐矿物油Ⅰ（150℃ 144h)")) {
                sample.setInspectionName(sample.getInspectionName().replace("（150℃ 144h)", ""));
            }

            // 序号
            TextRenderData headRenderData1 = new TextRenderData();
            headRenderData1.setText(sample.getIndex());
            Style headStyle1 = new Style();
            headStyle1.setFontFamily("宋体");
            headStyle1.setColor("000000");
            headRenderData1.setStyle(headStyle1);
            text.add(headRenderData1);

            // 检验项目
            TextRenderData headRenderData2 = new TextRenderData();
            headRenderData2.setText(sample.getInspectionName() + "∑" + (i + 999));
            Style headStyle2 = new Style();
            headStyle2.setFontFamily("宋体");
            headStyle2.setColor("000000");
            headRenderData2.setStyle(headStyle2);
            text.add(headRenderData2);

            // 检验项目
            TextRenderData headRenderData3 = new TextRenderData();
            headRenderData3.setText(sample.getInspectionName() + "∑" + (i + 999));
            Style headStyle3 = new Style();
            headStyle3.setFontFamily("宋体");
            headStyle3.setColor("000000");
            headRenderData3.setStyle(headStyle3);
            text.add(headRenderData3);

            // 单位
            TextRenderData headRenderData4 = new TextRenderData();
            headRenderData4.setText(sample.getUnit());
            Style headStyle4 = new Style();
            headStyle4.setFontFamily("宋体");
            headStyle4.setColor("000000");
            headRenderData4.setStyle(headStyle4);
            text.add(headRenderData4);

            //标准要求
            String tell = sample.getTell();
            if (CollectionUtils.isNotEmpty(sample.getTellSet())) {
                tell = CollUtil.join(sample.getTellSet(), "/");
            }
            TextRenderData headRenderData5 = new TextRenderData();
            headRenderData5.setText(tell);
            Style headStyle5 = new Style();
            headStyle5.setFontFamily("宋体");
            headStyle5.setColor("000000");
            headRenderData5.setStyle(headStyle5);
            text.add(headRenderData5);

            // 判断是否有电缆标识map
            if (CollectionUtils.isNotEmpty(sample.getCableTagValueMap())) {
                sample.setLastValueList(new ArrayList<>(sample.getCableTagValueMap().values()));
            }
            if (CollectionUtils.isNotEmpty(sample.getCableTagResultMap())) {
                sample.setInsResultList(new ArrayList<>(sample.getCableTagResultMap().values()));
            }

            // 检验结果
            // 判断是否是多次检验
            if (CollectionUtils.isNotEmpty(sample.getLastValueList())
                    && sample.getLastValueList().size() == max) {

                for (String s : sample.getLastValueList()) {
                    TextRenderData headRenderData6 = new TextRenderData();
                    headRenderData6.setText(s);
                    Style headStyle6 = new Style();
                    headStyle6.setFontFamily("宋体");
                    headStyle6.setColor("000000");
                    headRenderData6.setStyle(headStyle6);
                    text.add(headRenderData6);
                }
            } else {
                for (int j = 0; j < max; j++) {
                    TextRenderData headRenderData6 = new TextRenderData();
                    headRenderData6.setText((StringUtils.isNotEmpty(sample.getLastValue()) ?
                            sample.getLastValue() : "")
                            + "∑" + (7 + i));
                    Style headStyle6 = new Style();
                    headStyle6.setFontFamily("宋体");
                    headStyle6.setColor("000000");
                    headRenderData6.setStyle(headStyle6);
                    text.add(headRenderData6);
                }
            }
            // 判定结果
            String result = "";
            if (sample.getInsResult() != null) {
                switch (sample.getInsResult()) {
                    case 1:
                        result = "√";
                        break;
                    case 2:
                        result = "×";
                        break;
                    case 3:
                        result = "-";
                        break;
                }
            }
            if (CollectionUtils.isNotEmpty(sample.getLastValueList())) {
                // 判断是否有一个错误
                if (sample.getInsResultList().stream().anyMatch(s -> s.equals(3))) {
                    result = "-";
                } else {
                    boolean error = sample.getInsResultList().stream().anyMatch(s -> s.equals(0));
                    if (error) {
                        List<String> collect = new ArrayList<>();
                        int index = 0;
                        for (Integer count : sample.getInsResultList()) {
                            String type;
                            if (count.equals(0)) {
                                String itemCh = "";
                                String itemEn = "";
                                // 添加不合格描述
                                // 判断长度是否为1
                                if (sample.getLastValueList().size() == 1) {
                                    this.fillReportErrorResult(errorClassItemMapCn, errorClassItemMapEn, sample, itemCh, itemEn);
                                } else if (CollectionUtils.isNotEmpty(cableTags)) {
                                    // 判断是否有电缆配置, 不是的话可能为原材料
                                    // 添加不合格描述
                                    itemCh = (max == 1 ? "" : cableTags.get(index));
                                    itemEn = (max == 1 ? "" : "The " + Integer.toString(index + 1) + " time ");
                                    this.fillReportErrorResult(errorClassItemMapCn, errorClassItemMapEn, sample, itemCh, itemEn);
                                } else {
                                    // 添加不合格描述
                                    itemCh = (max == 1 ? "" : "第" + Integer.toString(index + 1) + "次");
                                    itemEn = (max == 1 ? "" : "The " + Integer.toString(index + 1) + " time ");
                                    this.fillReportErrorResult(errorClassItemMapCn, errorClassItemMapEn, sample, itemCh, itemEn);
                                }
                                type = "×";
                            } else {
                                type = "√";
                            }
                            collect.add(type);
                            index++;
                        }
                        result = CollUtil.join(collect, " ");
                        ;
                    } else {
                        result = "√";
                    }
                }
            }

            TextRenderData headRenderData7 = new TextRenderData();
            headRenderData7.setText(result);
            Style headStyle7 = new Style();
            headStyle7.setFontFamily("宋体");
            headStyle7.setColor("000000");
            headRenderData7.setStyle(headStyle7);
            text.add(headRenderData7);

            TextRenderData[] text2 = text.toArray(new TextRenderData[0]);
            rowRenderData = Rows.of(text2).center().rowAtleastHeight(1).create();
            rows.add(rowRenderData);
            text = new ArrayList<>();

        }
        if (CollectionUtils.isNotEmpty(errorClassItemMapCn)) {
            // 循环检验项分类
            errorClassItemMapCn.forEach((errorClass, errorItemMap) -> {
                if (StringUtils.isNotBlank(resultCh.get())) {
                    resultCh.set(resultCh.get() + "，" + errorClass);
                } else {
                    resultCh.set(errorClass);
                }
                // 循环检验项
                AtomicBoolean flag = new AtomicBoolean(false);
                errorItemMap.forEach((errorItem, errorSubClassList) -> {
                    if (flag.get()) {
                        resultCh.set(resultCh.get() + "，" + errorItem);
                    } else {
                        resultCh.set(resultCh.get() + errorItem);
                    }
                    flag.set(true);
                    // 循环检验项分类
                    if (CollectionUtils.isNotEmpty(errorSubClassList)) {
                        for (int i = 0; i < errorSubClassList.size(); i++) {
                            // 判断是否是第二个, 第二个添加顿号
                            if (i > 0) {
                                resultCh.set(resultCh.get() + "、" + errorSubClassList.get(i));
                            } else {
                                resultCh.set(resultCh.get() + errorSubClassList.get(i));
                            }
                        }
                    }
                });
            });
            // 循环检验项分类
            errorClassItemMapEn.forEach((errorClass, errorItemMap) -> {
                if (StringUtils.isNotBlank(resultEn.get())) {
                    resultEn.set(resultEn.get() + "，" + errorClass);
                } else {
                    resultEn.set(errorClass);
                }
                // 循环检验项
                errorItemMap.forEach((errorItem, errorSubClassList) -> {
                    resultEn.set(resultEn.get() + errorItem);
                    // 循环检验项分类
                    if (CollectionUtils.isNotEmpty(errorSubClassList)) {
                        for (int i = 0; i < errorSubClassList.size(); i++) {
                            // 判断是否是第二个, 第二个添加顿号
                            if (i > 0) {
                                resultEn.set(resultEn.get() + "、" + errorSubClassList.get(i));
                            } else {
                                resultEn.set(resultEn.get() + errorSubClassList.get(i));
                            }
                        }
                    }
                });
            });
        }

        if (StringUtils.isNotBlank(resultCh.get())) {
            String resultChString = resultCh.get();
            String resultEnString = resultEn.get();
            // 判断样品是否有多个, 有多个需要拼接样品编号
            if (!isOneSample) {
                resultChString = a.getSampleCode() + resultChString;
                resultEnString = a.getSampleCode() + resultEnString;
            }
            resultChList.add(resultChString);
            resultEhList.add(resultEnString);
        }


        return productSize.get() - 1;
    }

    /**
     * 添加报告结论中英文
     * @param sample
     * @param itemCh
     * @param itemEn
     */
    private void fillReportErrorResult(Map<String, LinkedHashMap<String, List<String>>> errorClassItemMapCn,
                                       Map<String, LinkedHashMap<String, List<String>>> errorClassItemMapEn,
                                       SampleProductExportDto sample,
                                       String itemCh,
                                       String itemEn) {
        // 判断是否有检验项分类map
        String classTiemName = itemCh + (StringUtils.isBlank(sample.getInspectionItemClass()) ? "" : sample.getInspectionItemClass());
        String classTiemNameEn = itemEn + (StringUtils.isBlank(sample.getInspectionItemClassEN()) ? "" : sample.getInspectionItemClassEN());
        String classTiemNameItemEN = StringUtils.isBlank(sample.getInspectionItemEn()) ? "" : sample.getInspectionItemEn();

        if (errorClassItemMapCn.containsKey(classTiemName)) {
            // 判断是否有检验项map
            if (errorClassItemMapCn.get(classTiemName).containsKey(sample.getInspectionItem())) {
                // 添加检验项分类
                if (StringUtils.isNotBlank(sample.getInspectionItemSubclass())) {
                    errorClassItemMapCn.get(classTiemName).get(sample.getInspectionItem()).add(sample.getInspectionItemSubclass());
                }
                if (StringUtils.isNotBlank(sample.getInspectionItemSubclassEn())) {
                    errorClassItemMapEn.get(classTiemNameEn).get(classTiemNameItemEN).add(sample.getInspectionItemSubclassEn());
                }
            } else {
                // 添加检验项map
                List<String> errorItemSubClassList = new ArrayList<>();
                if (StringUtils.isNotBlank(sample.getInspectionItemSubclass())) {
                    errorItemSubClassList.add(sample.getInspectionItemSubclass());
                }
                List<String> errorItemSubClassListEn = new ArrayList<>();
                if (StringUtils.isNotBlank(sample.getInspectionItemSubclassEn())) {
                    errorItemSubClassListEn.add(sample.getInspectionItemSubclassEn());
                }
                errorClassItemMapCn.get(classTiemName).put(sample.getInspectionItem(), errorItemSubClassList);
                errorClassItemMapEn.get(classTiemNameEn).put(classTiemNameItemEN, errorItemSubClassListEn);
            }
        } else {
            // map添加检验项分类
            List<String> errorItemSubClassList = new ArrayList<>();
            if (StringUtils.isNotBlank(sample.getInspectionItemSubclass())) {
                errorItemSubClassList.add(sample.getInspectionItemSubclass());
            }
            List<String> errorItemSubClassListEn = new ArrayList<>();
            if (StringUtils.isNotBlank(sample.getInspectionItemSubclassEn())) {
                errorItemSubClassListEn.add(sample.getInspectionItemSubclassEn());
            }
            errorClassItemMapCn.put(classTiemName, new LinkedHashMap<String, List<String>>() {{
                put(sample.getInspectionItem(), errorItemSubClassList);
            }});
            errorClassItemMapEn.put(classTiemNameEn, new LinkedHashMap<String, List<String>>() {{
                put(classTiemNameItemEN, errorItemSubClassListEn);
            }});
        }
    }


    /**
     * 格式化修改检验项
     * @param sampleProductDto2s
     */
    private void formatProducts(List<SampleProductExportDto> sampleProductDto2s) {
        sampleProductDto2s.forEach(sampleProductDto2 -> {
            // 清除全部结果的前后空格, 换行符
            if (StrUtil.isNotBlank(sampleProductDto2.getLastValue())) {
                sampleProductDto2.setLastValue(sampleProductDto2.getLastValue().trim());
            }

            if (sampleProductDto2.getInsResult().equals(0)) {
                // 查询不合格复测
                Long count = insUnqualifiedRetestProductMapper.selectCount(Wrappers.<InsUnqualifiedRetestProduct>lambdaQuery()
                        .eq(InsUnqualifiedRetestProduct::getInsProductId, sampleProductDto2.getInsProductId())
                        .ne(InsUnqualifiedRetestProduct::getInsResult, 0));
                if (count == 2) {
                    // 复测内容都合格就取第一个结果
                    InsUnqualifiedRetestProduct retestProduct = insUnqualifiedRetestProductMapper.selectOne(Wrappers.<InsUnqualifiedRetestProduct>lambdaQuery()
                            .eq(InsUnqualifiedRetestProduct::getInsProductId, sampleProductDto2.getInsProductId())
                            .last("limit 1"));
                    sampleProductDto2.setLastValue(retestProduct.getLastValue());
                    sampleProductDto2.setInsResult(retestProduct.getInsResult());
                }
            }
            // 判断是否是数字类型
            if (sampleProductDto2.getInspectionValueType().equals("1")) {
                // 把检验内容如果正硫化点和焦烧时间把  .  切割成  :
                String lastValue = sampleProductDto2.getLastValue();
                if (sampleProductDto2.getInspectionItem().contains("正硫化点") || sampleProductDto2.getInspectionItem().contains("焦烧时间")) {
                    sampleProductDto2.setLastValue(sampleProductDto2.getLastValue().replace('.', ':'));
                } else {
                    // 判断当前结果是否是科学计数法
                    boolean scientificNotation = isScientificNotation(lastValue);
                    if (scientificNotation) {
                        // 修改要求描述
                        sampleProductDto2.setTell(convertToScientificNotation(sampleProductDto2.getTell()));

                        // 修改要求值
                        sampleProductDto2.setLastValue(toScientificNotationWithTimes(lastValue));
                    } else {
                        // 排除氧指数
                        if (sampleProductDto2.getInspectionItem().contains("氧指数")) {

                            // 特殊处理判断检验项是否等于外护套电导率, 是的话只需要保留一位小数
                        } else if (StringUtils.isNotBlank(sampleProductDto2.getInspectionItemSubclass())
                                && sampleProductDto2.getInspectionItemSubclass().contains("外护套电导率")) {
                            BigDecimal bd = new BigDecimal(lastValue);
                            bd = bd.setScale(1, RoundingMode.HALF_UP);
                            sampleProductDto2.setLastValue(bd.stripTrailingZeros().toPlainString());
                        } else {
                            // 修改小数点位数根据要求值来
                            sampleProductDto2.setLastValue(modifyNumber(sampleProductDto2.getAsk(), lastValue));
                        }
                    }
                }
            }
            // 判断是否是  拉伸强度变化率(100℃，168h)  和  断裂拉伸应变变化率(100℃，168h) 需要去除条件
            if (StrUtil.isNotBlank(sampleProductDto2.getInspectionItemSubclass())
                    && sampleProductDto2.getInspectionItemSubclass().equals("拉伸强度变化率(100℃，168h)")) {
                sampleProductDto2.setInspectionItem("老化后机械性能(100℃，168h)");
            }
            if (StrUtil.isNotBlank(sampleProductDto2.getInspectionItemSubclass())
                    && sampleProductDto2.getInspectionItemSubclass().equals("断裂拉伸应变变化率(100℃，168h)")) {
                sampleProductDto2.setInspectionItem("老化后机械性能(100℃，168h)");
            }
        });
    }

    /**
     * 添加结尾
     * @param text
     * @param rowRenderData
     * @param rows
     */
    private static void addEnding(List<TextRenderData> text, RowRenderData rowRenderData, List<RowRenderData> rows, int max) {
        for (int i = 0; i < 2; i++) {
            TextRenderData endingRenderData = new TextRenderData();
            endingRenderData.setText("备注∑1999");
            Style endingStyle = new Style();
            endingStyle.setFontFamily("宋体");
            endingStyle.setColor("000000");
            endingRenderData.setStyle(endingStyle);
            text.add(endingRenderData);
        }

        for (int i = 0; i < 5 + max - 1; i++) {
            TextRenderData endingRenderData = new TextRenderData();
            endingRenderData.setText("“√”表示项目合格,“×”表示项目不合格,“-”表示不要判定，“/”表示没有标准要求。@“√” indicates test ltem is qualified,“×” indicates test ltem is unqualified,“-” indicates test ltem judgment is not required,“/” indicates test ltem is no test requirement.∑2000");
            Style endingStyle = new Style();
            endingStyle.setFontFamily("宋体");
            endingStyle.setColor("000000");
            endingRenderData.setStyle(endingStyle);
            text.add(endingRenderData);
        }
        TextRenderData[] text3 = text.toArray(new TextRenderData[0]);
        rowRenderData = Rows.of(text3).center().rowAtleastHeight(1).create();
        rows.add(rowRenderData);
    }

    /**
     * 判断当前内容是否是科学计数法
     * @param str
     * @return
     */
    public static boolean isScientificNotation(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        // 正则表达式匹配科学计数法，包括正数和负数
        String regex = "^[+|-]?\\d*[.]?\\d*e[+|-]?\\d+$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(str).matches();
    }


    /**
     * 修改要求描述的科学计数法
     * @param input
     */
    public static String convertToScientificNotation(String input) {
        // 保存原始输入
        String originalInput = input;

        // 去掉非科学计数法的字符
        String cleanedInput = input.replaceAll("[^0-9eE+-.]", "");

        // 判断要求描述是否是科学计数法
        boolean scientificNotation = isScientificNotation(cleanedInput);
        if (!scientificNotation) {
            return input;
        }

        // 找出被去掉的字符
        String removedChars = originalInput.replace(cleanedInput, "");

        try {
            String scientificNotationWithTimes = toScientificNotationWithTimes(cleanedInput);
            return removedChars + scientificNotationWithTimes;
        } catch (Exception e) {
            return input;
        }

    }

    /**
     * 展示成科学计数法
     * @param number
     * @return
     */
    public static String toScientificNotationWithTimes(String number) {
        Double value = Double.valueOf(number);

        // 提取小数部分和指数部分
        String scientificFormat = String.format("%.1e", value);
        String[] parts = scientificFormat.split("e");

        // 处理数字部分和指数部分
        String mantissa = parts[0]; // 小数部分
        int exponent = Integer.parseInt(parts[1]); // 指数部分

        String power = "";
        switch (exponent) {
            case 2:
                power = "²";
                break;
            case 3:
                power = "³";
                break;
            case 4:
                power = "⁴";
                break;
            case 5:
                power = "⁵";
                break;
            case 6:
                power = "⁶";
                break;
            case 7:
                power = "⁷";
                break;
            case 8:
                power = "⁸";
                break;
            case 9:
                power = "⁹";
                break;
            case 10:
                power = "¹⁰";
                break;
            case 11:
                power = "¹¹";
                break;
            case 12:
                power = "¹²";
                break;
            case 13:
                power = "¹³";
                break;
            case 14:
                power = "¹⁴";
                break;
            case 15:
                power = "¹⁵";
                break;
            case 16:
                power = "¹⁶";
                break;
            case 17:
                power = "¹⁷";
                break;
            case 18:
                power = "¹⁸";
                break;
            case 19:
                power = "¹⁹";
                break;
            case 20:
                power = "²⁰";
                break;

        }

        // 构建最终的科学计数法格式字符串
        return String.format("%s×10%s", mantissa, power);
    }


    // 判断是否是数字
    public static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(str); // 尝试将字符串解析为双精度浮点数
            return true;
        } catch (NumberFormatException e) {
            return false; // 解析失败则返回false
        }
    }

    /**
     * 根据要求描述保留结果小数点位数
     * @param reference
     * @param value
     * @return
     */
    public static String modifyNumber(String reference, String value) {
        if (reference.equals("/") || reference.equals("-") || reference.equals("—")) {
            // 不判定默认保留一位小数 > 默认小数位
//            BigDecimal bd = new BigDecimal(value);
//            bd = bd.setScale(1, RoundingMode.HALF_UP);
            return value;
        }

        // 判断是否是数字
        if (!isNumeric(value)) {
            try {
                // 可能是≤95.1A这样的, 需要截取保留位数
                // 使用正则表达式提取数字部分
                String numberPart = value.replaceAll("[^0-9.]", "");
                // 将数字部分替换成占位符
                String withPlaceholder = value.replaceAll(numberPart, "**");
                // 提取小数点位数
                int decimalPlaces = getDecimalPlaces(reference);
                // 处理第二个字符串
                BigDecimal number = new BigDecimal(numberPart);
                number = number.setScale(decimalPlaces, RoundingMode.HALF_UP);
                // 等于0需要进一步处理
                if (number.compareTo(BigDecimal.ZERO) == 0) {
                    return formatNumber(value, decimalPlaces);
                }
                // 将占位符替换成指定数字
                String finalResult = withPlaceholder.replace("**", number.toString());
                return finalResult;
            } catch (Exception e) {
                return value;
            }
        }

        // 提取小数点位数
        int decimalPlaces = getDecimalPlaces(reference);

        // 处理第二个字符串
        BigDecimal number = new BigDecimal(value);
        number = number.setScale(decimalPlaces, RoundingMode.HALF_UP);

        // 等于0需要进一步处理
        if (number.compareTo(BigDecimal.ZERO) == 0) {
            return formatNumber(value, decimalPlaces);
        }

        return number.toString();
    }


    private static int getDecimalPlaces(String str) {
        // 查找小数点位置
        int decimalPointIndex = str.lastIndexOf('.');

        if (decimalPointIndex == -1) {
            // 如果没有小数点，返回0位小数
            return 0;
        }

        // 计算小数点后的位数
        return str.length() - decimalPointIndex - 1;
    }

    /**
     * 保留位数, 如果等于0 返回找到的一个非0位数
     * @param number 当前数字
     * @param scale  原本保留的位数
     * @return
     */
    public static String formatNumber(String number, int scale) {
        // 将输入的字符串转换为 BigDecimal
        BigDecimal bd = new BigDecimal(number);

        // 保留指定的小数位数，并使用四舍五入模式
        bd = bd.setScale(scale, RoundingMode.HALF_UP);

        // 检查保留的小数是否等于 0
        if (bd.stripTrailingZeros().scale() <= 0) {
            // 需要找到第一个不为 0 的小数位
            String decimalPart = number.substring(number.indexOf('.') + 1);
            int firstNonZeroIndex = findFirstNonZeroIndex(decimalPart);

            if (firstNonZeroIndex != -1) {
                // 计算需要保留的小数位数
                int newScale = firstNonZeroIndex + 1;
                bd = new BigDecimal(number).setScale(newScale, RoundingMode.HALF_UP);
            }
        }

        return bd.toString();
    }

    private static int findFirstNonZeroIndex(String decimalPart) {
        for (int i = 0; i < decimalPart.length(); i++) {
            if (decimalPart.charAt(i) != '0') {
                return i;
            }
        }
        return -1; // 如果没有找到非零的数字
    }

    /**
     * *****保存元此阿里进货验证原始记录*****
     * @param insOrderId 订单Id
     * @param examineUserId  复核人Id
     * @param writeUserId  检验员Id
     */
    private void reportFactoryVerify(Integer insOrderId, Integer examineUserId, Integer writeUserId) {
        // 查询进货验证原始记录
        InsOrderFactoryVerify factoryVerify = insOrderFactoryVerifyMapper.selectOne(Wrappers.<InsOrderFactoryVerify>lambdaQuery()
                .eq(InsOrderFactoryVerify::getInsOrderId, insOrderId)
                .last("limit 1"));

        // 没有就结束
        if (factoryVerify == null) {
            return;
        }

        //1.材料名称
        factoryVerify.setBasicName(this.formatBasic(factoryVerify.getBasicName()));
        //2.规格型号
        factoryVerify.setBasicModel(this.formatBasic(factoryVerify.getBasicModel()));
        //3.材料批号
        factoryVerify.setBasicBatchNo(this.formatBasic(factoryVerify.getBasicBatchNo()));
        //4.执行标准
        factoryVerify.setBasicStandard(this.formatBasic(factoryVerify.getBasicStandard()));
        //5.生产日期
        factoryVerify.setBasicDate(this.formatBasic(factoryVerify.getBasicDate()));
        //6.供货数量
        factoryVerify.setBasicNumber(this.formatBasic(factoryVerify.getBasicNumber()));
        //7.材料名称
        factoryVerify.setBasicColor(this.formatBasic(factoryVerify.getBasicColor()));

        //8.其他
        // 内容
        if (factoryVerify.getBasicOtherValue() == null) {
            factoryVerify.setBasicOtherValue("");
        }
        // 值
        if (factoryVerify.getBasicOther() == null) {
            factoryVerify.setBasicOther("");
        }
        switch (factoryVerify.getBasicOther()) {
            case "1":
                factoryVerify.setBasicOther("☑符合  □不符合");
                break;
            case "2":
                factoryVerify.setBasicOther("□符合  ☑不符合");
                break;
            default:
                factoryVerify.setBasicOther("□符合  □不符合");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String declareDate = factoryVerify.getDeclareDate().format(formatter);

        // 修改验证项目
        List<InsOrderFactoryVerifyItem> verifyItems = insOrderFactoryVerifyItemService.list(Wrappers.<InsOrderFactoryVerifyItem>lambdaQuery()
                .eq(InsOrderFactoryVerifyItem::getFactoryVerifyId, factoryVerify.getFactoryVerifyId())
                .orderByAsc(InsOrderFactoryVerifyItem::getSort));

        for (InsOrderFactoryVerifyItem verifyItem : verifyItems) {
            if (verifyItem.getResult() == null) {
                verifyItem.setResult("");
            }
            switch (verifyItem.getResult()) {
                case "1":
                    verifyItem.setResult("☑符合    □不符合    □不适用");
                    break;
                case "2":
                    verifyItem.setResult("□符合    ☑不符合    □不适用");
                    break;
                case "3":
                    verifyItem.setResult("□符合    □不符合    ☑不适用");
                    break;
                default:
                    verifyItem.setResult("□符合    □不符合    □不适用");
            }
        }

        //获取提交人的签名地址
        String writeUrl;
        try {
            writeUrl = userMapper.selectById(writeUserId).getSignatureUrl();
        } catch (Exception e) {
            throw new ErrorException("找不到编制人的签名");
        }
        if (ObjectUtils.isEmpty(writeUrl) || writeUrl.equals("")) {
            throw new ErrorException("找不到检验人的签名");
        }

        //获取复核人的签名地址
        String examineUrl;
        try {
            examineUrl = userMapper.selectById(examineUserId).getSignatureUrl();
        } catch (Exception e) {
            throw new ErrorException("找不到复核人的签名");
        }
        if (ObjectUtils.isEmpty(examineUrl) || examineUrl.equals("")) {
            throw new ErrorException("找不到复核人的签名");
        }


        InputStream inputStream = this.getClass().getResourceAsStream("/static/factory_verify.docx");
        ConfigureBuilder builder = Configure.builder();
        builder.useSpringEL(true);

        XWPFTemplate template = XWPFTemplate.compile(inputStream, builder.build()).render(
                new HashMap<String, Object>() {{
                    put("report", factoryVerify);
                    put("declareDate", declareDate);
                    put("factoryVerifyItemList", verifyItems);
                    put("writeUrl", Pictures.ofLocal(imgUrl + "/" + writeUrl).create());
                    put("examineUrl", Pictures.ofLocal(imgUrl + "/" + examineUrl).create());
                    put("writeDateUrl", Pictures.ofStream(DateImageUtil.createDateImage(null)).create());
                    put("examineDateUrl", Pictures.ofStream(DateImageUtil.createDateImage(null)).create());
                }});

        MultipartFile multipartFile = xwpfDocumentToMockMultipartFile(template.getXWPFDocument());

        // 保存到附件里面
        uploadFile(insOrderId, multipartFile);
        try {
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * XWPFDocument 转 MultipartFile(MockMultipartFile)
     *
     * @param document 文档对象
     * @return
     */
    public static MultipartFile xwpfDocumentToMockMultipartFile(XWPFDocument document) {
        String fileName = "原材料进货验证记录.docx";
        try {
            String contentType = "text/plain";
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            document.write(bos);
            //XWPFDocument 转 byte[]
            byte[] barray = bos.toByteArray();
            //byte[] 转 InputStream
            InputStream is = new ByteArrayInputStream(barray);
            //InputStream 转 MultipartFile
            MultipartFile multipartFile = new MockMultipartFile(fileName, fileName, contentType, is);
            return multipartFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * ***格式化进厂验证内容****
     * @param basicType
     * @return
     */
    private String formatBasic(String basicType) {
        if (basicType == null) {
            basicType = "";
        }

        //格式化字段
        String basic = "";
        switch (basicType) {
            case "1":
                basic = "☑符合  □不符合  □不适用";
                break;
            case "2":
                basic = "□符合  ☑不符合  □不适用";
                break;
            case "3":
                basic = "□符合  □不符合  ☑不适用";
                break;
            default:
                basic = "□符合  □不符合  □不适用";
        }

        return basic;
    }

    /**
     * ***word转换pdf***
     * @param path
     * @return
     */
    @Override
    public String wordToPdfTemp(String path) {
        try {
            return wordToPdf(path, path.replace(".docx", "-临时.pdf"));
        } catch (Exception e) {
            throw new ErrorException("转换失败");
        }
    }

    private String wordToPdf(String wordPath, String pdfPath) {
        FileOutputStream os = null;
        try {
            //凭证 不然切换后有水印
            InputStream is = new ClassPathResource("/lib/license.xml").getInputStream();
            License license = new License();
            license.setLicense(is);
            if (!license.getIsLicensed()) {
                return null;
            }
            //生成一个空的PDF文件
            File file;
            //判断是否是进厂报告
            file = new File(pdfPath);
            os = new FileOutputStream(file);
            //要转换的word文件
            com.aspose.words.Document doc = new com.aspose.words.Document(wordPath);
            doc.save(os, SaveFormat.PDF);
            String name = file.getName();
            return file.getName();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * *****修改成品抽样状态******
     * @param insSamples
     * @param order
     */
    private void addProductSpotCheck(List<InsSample> insSamples, InsOrder order) {
        Long unqualifiedCount = 0L;
        if (CollectionUtils.isNotEmpty(insSamples)) {
            unqualifiedCount = insProductMapper.selectCount(Wrappers.<InsProduct>lambdaQuery()
                    .in(InsProduct::getInsSampleId, insSamples.stream().map(InsSample::getId).collect(Collectors.toList()))
                    .eq(InsProduct::getInsResult, 0));

            // 判断如果有不合格的检验项, 判断有没有检验项复测, 复核合格也算合格通过
            if (!unqualifiedCount.equals(0L)) {
                List<InsProduct> productList = insProductMapper.selectList(Wrappers.<InsProduct>lambdaQuery()
                        .in(InsProduct::getInsSampleId, insSamples.stream().map(InsSample::getId).collect(Collectors.toList()))
                        .eq(InsProduct::getInsResult, 0));

                boolean flag = true;
                for (InsProduct insProduct : productList) {
                    Long unqualifiedProductCount = insUnqualifiedRetestProductMapper.selectCount(Wrappers.<InsUnqualifiedRetestProduct>lambdaQuery()
                            .eq(InsUnqualifiedRetestProduct::getInsProductId, insProduct.getId())
                            .ne(InsUnqualifiedRetestProduct::getInsResult, 0));
                    if (unqualifiedProductCount != 2) {
                        flag = false;
                    }
                }
                if (flag) {
                    unqualifiedCount = 0L;
                }
            }
        }
        spotCheckQuarterItemMapper.update(null, Wrappers.<SpotCheckQuarterItem>lambdaUpdate()
                .eq(SpotCheckQuarterItem::getQuarterItemId, order.getQuarterItemId())
                .set(SpotCheckQuarterItem::getResult, unqualifiedCount.equals(0L) ? "合格" : "不合格"));
    }

    /**
     * 添加工时
     * @param userId
     * @param insProduct
     * @param insOrder
     */
    private synchronized void addAuxiliary(Integer userId, InsProduct insProduct, InsOrder insOrder) {
        if (insProduct.getIsBinding().equals(1)) {
            return;
        }

        //校验如果这个人这个检测项目已经添加过了
        List<AuxiliaryOutputWorkingHours> count2s = auxiliaryOutputWorkingHoursMapper.selectList(Wrappers.<AuxiliaryOutputWorkingHours>lambdaQuery()
                .eq(AuxiliaryOutputWorkingHours::getCheck, userId)
                .eq(AuxiliaryOutputWorkingHours::getInsProductId, insProduct.getId()));
        if (CollectionUtils.isNotEmpty(count2s)) {
            for (AuxiliaryOutputWorkingHours auxiliaryOutputWorkingHours : count2s) {
                auxiliaryOutputWorkingHours.setCheck(userId);//检测人
            }
            auxiliaryOutputWorkingHoursService.updateBatchById(count2s);
        } else {
            if (ObjectUtils.isNotEmpty(insProduct.getManHour()) && StringUtils.isNotBlank(insProduct.getLastValue())) {
                AuxiliaryOutputWorkingHours auxiliaryOutputWorkingHours = new AuxiliaryOutputWorkingHours();
                auxiliaryOutputWorkingHours.setInspectionItemClass(insProduct.getInspectionItemClass());//检测项分类
                auxiliaryOutputWorkingHours.setInspectionItem(insProduct.getInspectionItem());//检测父项
                auxiliaryOutputWorkingHours.setInspectionItemSubclass(insProduct.getInspectionItemSubclass());//检测子项
                auxiliaryOutputWorkingHours.setSample(insSampleMapper.selectById(insProduct.getInsSampleId()).getSampleCode());//样品编号
                auxiliaryOutputWorkingHours.setOrderId(insOrder.getId());//订单id
                auxiliaryOutputWorkingHours.setOrderNo(insOrder.getEntrustCode());//非加班委托单号
                auxiliaryOutputWorkingHours.setWorkTime(insProduct.getManHour());//非加班工时
                auxiliaryOutputWorkingHours.setAmount(1);//非加班数量
                auxiliaryOutputWorkingHours.setOutputWorkTime((ObjectUtils.isNotEmpty(auxiliaryOutputWorkingHours.getOvertimeWorkTime()) ? auxiliaryOutputWorkingHours.getOvertimeWorkTime() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(auxiliaryOutputWorkingHours.getWorkTime()) ? auxiliaryOutputWorkingHours.getWorkTime() : BigDecimal.ZERO));//产量工时
                auxiliaryOutputWorkingHours.setManHourGroup(insProduct.getManHourGroup());//工时分组
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                auxiliaryOutputWorkingHours.setDateTime(LocalDateTime.now().toLocalDate().atStartOfDay().format(formatters));//日期
                LocalDateTime localDateTime = LocalDateTime.now();
                DateTime parse = DateUtil.parse(localDateTime.format(formatter));
                auxiliaryOutputWorkingHours.setWeekDay(getWeek(localDateTime.format(formatters)));//星期
                auxiliaryOutputWorkingHours.setWeek(String.valueOf(DateUtil.weekOfYear(DateUtil.offsetDay(parse, 1))));//周次
                auxiliaryOutputWorkingHours.setCheck(userId);//检测人
                auxiliaryOutputWorkingHours.setPrice(insProduct.getPrice());//单价
                auxiliaryOutputWorkingHours.setSampleId(insProduct.getInsSampleId());//样品id
                auxiliaryOutputWorkingHours.setInsProductId(insProduct.getId());//检验项id

                auxiliaryOutputWorkingHoursMapper.insert(auxiliaryOutputWorkingHours);

            }
        }
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
