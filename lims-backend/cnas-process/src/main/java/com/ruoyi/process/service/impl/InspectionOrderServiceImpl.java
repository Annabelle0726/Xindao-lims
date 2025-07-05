package com.ruoyi.process.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.inspect.dto.InsOrderDeviceRecordDto;
import com.ruoyi.inspect.mapper.InsProductResultMapper;
import com.ruoyi.inspect.mapper.InsSampleMapper;
import com.ruoyi.inspect.pojo.*;
import com.ruoyi.inspect.service.InsOrderService;
import com.ruoyi.inspect.service.InsReportService;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import com.ruoyi.process.dto.InspectionOrderDto;
import com.ruoyi.process.dto.InspectionOrderExportDto;
import com.ruoyi.process.mapper.InspectionOrderMapper;
import com.ruoyi.process.mapper.ProcessOrderDeviceMapper;
import com.ruoyi.process.mapper.ProcessSampleMapper;
import com.ruoyi.process.pojo.*;
import com.ruoyi.process.service.*;
import com.ruoyi.system.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 检验委托单 服务实现类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-09
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class InspectionOrderServiceImpl extends ServiceImpl<InspectionOrderMapper, InspectionOrder> implements InspectionOrderService {
    @Resource
    private InspectionOrderDetailService inspectionOrderDetailService;
    @Resource
    private InsOrderService insOrderService;
    @Resource
    private InsSampleMapper insSampleMapper;
    @Resource
    private InsReportService insReportService;
    @Value("${wordUrl}")
    private String wordUrl;
    @Resource
    private ProcessReportService processReportService;
    @Resource
    private ProcessOrderDeviceMapper processOrderDeviceMapper;
    @Resource
    private InsProductResultMapper insProductResultMapper;
    @Resource
    private ProcessOrderDeviceService processOrderDeviceService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private ProcessSampleService processSampleService;
    @Resource
    private ProcessSampleMapper processSampleMapper;
    @Resource
    private ProcessDealService processDealService;


    /**
     * 检验委托单分页查询
     * @param page
     * @param InspectionOrder
     * @return
     */
    @Override
    public IPage<InspectionOrderDto> pageInspectionOrder(Page page, InspectionOrder InspectionOrder) {
        return baseMapper.pageInspectionOrder(page, QueryWrappers.queryWrappers(InspectionOrder));
    }

    /**
     * 检验委托单新增
     * @param inspectionOrder
     * @return
     */
    @Override
    public boolean addInspectionOrder(InspectionOrderDto inspectionOrder) {
        if (inspectionOrder.getInsOrderId() == null) {
            throw new ErrorException("缺少订单id");
        }

        // 复制报告
        // 查询订单报告
        InsReport insReport = insReportService.getOne(Wrappers.<InsReport>lambdaQuery()
                .eq(InsReport::getInsOrderId, inspectionOrder.getInsOrderId()));

        String path = wordUrl + insReport.getUrl().replaceFirst("/word", "");

        String fileName =LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss")) + "_" + "委托单报告.docx";

        // 源文件路径
        Path sourcePath = Paths.get(path);
        // 目标文件路径
        Path targetPath = Paths.get(wordUrl + "/" + fileName);

        try {
            // 复制文件，如果目标文件已存在，则覆盖
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            // 添加文件地址
            inspectionOrder.setFileUrl("/word/" + fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }

        baseMapper.insert(inspectionOrder);
        // 新增详情
        if (CollectionUtils.isNotEmpty(inspectionOrder.getOrderDetailList())) {
            for (InspectionOrderDetail InspectionOrderDetail : inspectionOrder.getOrderDetailList()) {
                InspectionOrderDetail.setInspectionOrderId(inspectionOrder.getInspectionOrderId());
            }
            inspectionOrderDetailService.saveBatch(inspectionOrder.getOrderDetailList());
        }
        /*新增7.8报告结果*/
        ProcessReport processReport = new ProcessReport();
        processReport.setInspectionOrderId(inspectionOrder.getInsOrderId());
        processReport.setInsReportCode(inspectionOrder.getEntrustCode());//报告编号=委托编号
        //页数
        try {
            com.aspose.words.Document doc = new com.aspose.words.Document(path);
            processReport.setPages(doc.getPageCount()+"");
        } catch (Exception e) {
        }
        processReport.setNumber("1");//发送份数默认1
        processReport.setSend(inspectionOrder.getCommissionUnit());//发往何处=委托单位
        processReport.setMethod(inspectionOrder.getSend()==1?"自取":"其他");//发送方式
        processReport.setSendTime(insReport.getRatifyTime().toLocalDate());//发送日期
        processReport.setSendUser(64);//发送人固定
        processReport.setSignatory(inspectionOrder.getCommissionUser());//签收人=委托人
        processReportService.save(processReport);
        /*新增7.1委托单对应的设备使用记录*/
        addDeviceRecord(inspectionOrder,insReport.getWriteUserId());
        /*新增7.4样品接收*/
        List<InsSample> insSamples = insSampleMapper.selectList(Wrappers.<InsSample>lambdaQuery()
                .eq(InsSample::getInsOrderId, inspectionOrder.getInsOrderId()));
        if (insSamples.size()>0){
            List<ProcessSample> processSamples = new ArrayList<>();
            for (InsSample insSample : insSamples) {
                ProcessSample processSample = new ProcessSample();
                processSample.setReceiveDate(inspectionOrder.getSampleData());//收样日期=领样日期
                processSample.setSampleCode(insSample.getSampleCode());//样品编号
                processSample.setSampleName(insSample.getSample());//样品名称
                processSample.setNum(1);//样品数量=1
                processSample.setSampleSupplier(inspectionOrder.getCommissionUnit());//来样单位
                LocalDate plusMonths = inspectionOrder.getSampleData() == null ? null : inspectionOrder.getSampleData().plusMonths(1);
                processSample.setLeaveDate(plusMonths);//留样日期=收样日期往后延一个月
                processSample.setSampleState(inspectionOrder.getSampleStatus());//样品状态
                processSample.setDealTime(plusMonths);//退样日期=留样日期
                processSample.setInspectionOrderId(inspectionOrder.getInspectionOrderId());//委托单id
                processSamples.add(processSample);
            }
            processSampleService.saveBatch(processSamples);
        }
        return true;
    }

    private synchronized void addDeviceRecord(InspectionOrder inspectionOrder,Integer userId) {
        // 查询设备使用记录查询该订单的使用记录
        Set<String> recordCodeset = processOrderDeviceMapper.selectDeviceNumber(inspectionOrder.getInspectionOrderId());
        // 获取订单设备编号
        List<InsProductResult> resultList = insProductResultMapper.selectResultByOrderId(inspectionOrder.getInsOrderId());
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
            List<Integer> orderDeviceIds = processOrderDeviceMapper.selectDeviceIdsByNumbers(orderDeviceNumbers);
            List<ProcessOrderDevice> collect = orderDeviceIds.stream().map(deviceId -> {
                ProcessOrderDevice processOrderDevice = new ProcessOrderDevice();
                processOrderDevice.setInspectionOrderId(inspectionOrder.getInspectionOrderId());
                processOrderDevice.setDeviceId(deviceId);
                processOrderDevice.setSampleCode(inspectionOrder.getEntrustCode());
                processOrderDevice.setUseBefore(1);
                processOrderDevice.setUseAfter(1);
                processOrderDevice.setUsePerson(userMapper.selectById(userId).getName());//使用人
                processOrderDevice.setUsePersonId(userId);//使用人id
                return processOrderDevice;
            }).collect(Collectors.toList());
            processOrderDeviceService.saveBatch(collect);
        }
    }

    private static Set<String> getDeviceDifference(Set<String> number1, Set<String> number2) {
        return number1.stream().filter(s1 -> number2.stream().noneMatch(s2 -> s1.equals(s2)))
                .collect(Collectors.toSet());
    }

    /**
     * 检验委托单修改
     * @param InspectionOrder
     * @return
     */
    @Override
    public boolean updateInspectionOrder(InspectionOrderDto InspectionOrder) {
        baseMapper.updateById(InspectionOrder);

        // 删除之前的详情
        inspectionOrderDetailService.remove(Wrappers.<InspectionOrderDetail>lambdaQuery()
                .eq(InspectionOrderDetail::getInspectionOrderId, InspectionOrder.getInspectionOrderId()));

        // 新增详情
        for (InspectionOrderDetail InspectionOrderDetail : InspectionOrder.getOrderDetailList()) {
            InspectionOrderDetail.setInspectionOrderId(InspectionOrder.getInspectionOrderId());
        }
        inspectionOrderDetailService.saveBatch(InspectionOrder.getOrderDetailList());

        //修改7.9报告结果
        ProcessReport processReport = processReportService.getOne(Wrappers.<ProcessReport>lambdaQuery().eq(ProcessReport::getInspectionOrderId, InspectionOrder.getInspectionOrderId()));
        processReport.setInsReportCode(InspectionOrder.getEntrustCode());//报告编号=委托编号
        processReportService.updateById(processReport);

        return true;
    }

    /**
     * 检验委托单删除
     * @param inspectionOrderId
     * @return
     */
    @Override
    public boolean delInspectionOrder(Integer inspectionOrderId) {
        inspectionOrderDetailService.remove(Wrappers.<InspectionOrderDetail>lambdaQuery()
                .eq(InspectionOrderDetail::getInspectionOrderId, inspectionOrderId));
        baseMapper.deleteById(inspectionOrderId);
        //删除7.8报告
        processReportService.remove(Wrappers.<ProcessReport>lambdaQuery()
                .eq(ProcessReport::getInspectionOrderId, inspectionOrderId));
        //删除对应的设备使用记录
        processOrderDeviceService.remove(Wrappers.<ProcessOrderDevice>lambdaQuery()
                .eq(ProcessOrderDevice::getInspectionOrderId, inspectionOrderId));
        //删除对应的7.4的样品申请
        List<ProcessSample> processSamples = processSampleMapper.selectList(Wrappers.<ProcessSample>lambdaQuery()
                .eq(ProcessSample::getInspectionOrderId, inspectionOrderId));
        if (processSamples.size()>0){
            List<Integer> list = processSamples.stream().map(ProcessSample::getId).collect(Collectors.toList());
            processDealService.remove(Wrappers.<ProcessDeal>lambdaQuery()
                    .in(ProcessDeal::getProcessSampleId,list));
        }
        //删除对应7.4的样品接收
        processSampleService.remove(Wrappers.<ProcessSample>lambdaQuery()
                .eq(ProcessSample::getInspectionOrderId,inspectionOrderId));
        return true;
    }

    /**
     * 检验委托单查看详情
     * @param inspectionOrderId
     * @return
     */
    @Override
    public InspectionOrderDto getInspectionOrderOne(Integer inspectionOrderId) {
        InspectionOrder inspectionOrder = baseMapper.selectById(inspectionOrderId);
        InspectionOrderDto inspectionOrderDto = new InspectionOrderDto();
        BeanUtils.copyProperties(inspectionOrder, inspectionOrderDto);

        // 查询详细信息
        inspectionOrderDto.setOrderDetailList(inspectionOrderDetailService.list(Wrappers.<InspectionOrderDetail>lambdaQuery()
                .eq(InspectionOrderDetail::getInspectionOrderId, inspectionOrderId)));
        return inspectionOrderDto;
    }

    /**
     * 根据成品订单id查询委托单详情
     * @param insOrderId
     * @return
     */
    @Override
    public InspectionOrderDto getInspectionOrderByInsOderId(Integer insOrderId) {
        InsOrder order = insOrderService.getById(insOrderId);
        InspectionOrderDto inspectionOrderDto = new InspectionOrderDto();
        inspectionOrderDto.setInsOrderId(order.getId());//成品订单id
        inspectionOrderDto.setEntrustCode(order.getEntrustCode());//委托编号
        inspectionOrderDto.setSampleName(order.getSample());//试样名称
        inspectionOrderDto.setProduction(order.getProduction());//生产单位
        inspectionOrderDto.setCommissionUnit(order.getCompany());//委托单位
        inspectionOrderDto.setCommissionUser(order.getPrepareUser());//委托人
        inspectionOrderDto.setSampleStatus("完好");//样品状态
        inspectionOrderDto.setIsLeave(order.getIsLeave());//是否留样
        inspectionOrderDto.setProcessing(order.getProcessing());//样品处理方式
        inspectionOrderDto.setAppointed(order.getAppointed());//约定时间
        inspectionOrderDto.setSend(order.getSend());//报告发送方式
        inspectionOrderDto.setCommissionPhone(order.getPhone());//报告发送方式

        return inspectionOrderDto;
    }


    /**
     * 委托单查询成品订单
     * @return
     */
    @Override
    public IPage<InsOrder> getInsOrderOnInspection(Page page, InsOrder insOrder) {
        return baseMapper.getInsOrderOnInspection(page, QueryWrappers.queryWrappers(insOrder));
    }

    /**
     * 委托单成品报告上传
     * @param file
     * @param inspectionOrderId
     * @return
     */
    @Override
    public boolean uploadInspectionOrderFile(MultipartFile file, Integer inspectionOrderId) {
        String urlString;
        String pathName;
        try {
            String path = wordUrl;
            File realpath = new File(path);
            if (!realpath.exists()) {
                realpath.mkdirs();
            }
            pathName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss")) + "_" + file.getOriginalFilename();
            urlString = realpath + "/" + pathName;
            file.transferTo(new File(urlString));

            baseMapper.update(null, Wrappers.<InspectionOrder>lambdaUpdate()
                    .eq(InspectionOrder::getInspectionOrderId, inspectionOrderId)
                    .set(InspectionOrder::getFileUrl, "/word/" + pathName));



        } catch (Exception e) {
            throw new ErrorException("文件上传失败");
        }
        return true;
    }

    @Override
    public void exportInspectionOrder(Integer inspectionOrderId, HttpServletResponse response) {
        InspectionOrder inspectionOrder = baseMapper.selectById(inspectionOrderId);
        InspectionOrderExportDto inspectionOrderExportDto = extracted(inspectionOrder);
        List<InspectionOrderDetail> list = inspectionOrderDetailService.list(Wrappers.<InspectionOrderDetail>lambdaQuery()
                .eq(InspectionOrderDetail::getInspectionOrderId, inspectionOrderId));
        int index = 1;
        for (InspectionOrderDetail inspectionOrderDetail : list) {
            inspectionOrderDetail.setIndex(index);
            index++;
        }

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/inspection-order.docx");
        Configure configure = Configure.builder()
                .bind("inspectionOrderDetailList", new HackLoopTableRenderPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("inspectionOrder", inspectionOrderExportDto);
                    put("inspectionOrderDetailList", list);
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    inspectionOrderExportDto.getSampleName(), "UTF-8");
            response.setHeader("Content-disposition",
                    "attachment;filename=" + fileName + ".docx");
            OutputStream os = response.getOutputStream();
            template.write(os);
            os.flush();
            os.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("导出失败");
        }
    }

    private InspectionOrderExportDto extracted(InspectionOrder inspectionOrder) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        InspectionOrderExportDto inspectionOrderExportDto = new InspectionOrderExportDto();
        BeanUtils.copyProperties(inspectionOrder, inspectionOrderExportDto);

        if (inspectionOrder.getCommissionDate() != null) {
            inspectionOrderExportDto.setCommissionDateString(inspectionOrder.getCommissionDate().format(formatter));
        }
        if (inspectionOrder.getReceiptData() != null) {
            inspectionOrderExportDto.setReceiptDataString(inspectionOrder.getReceiptData().format(formatter));
        }
        if (inspectionOrder.getSampleData() != null) {
            inspectionOrderExportDto.setSampleDataString(inspectionOrder.getSampleData().format(formatter));
        }
        if (inspectionOrder.getAppointed() != null) {
            inspectionOrderExportDto.setAppointedString(inspectionOrder.getAppointed().format(formatter));
        }

        inspectionOrderExportDto.setIsLeave1(inspectionOrder.getIsLeave() != null && inspectionOrder.getIsLeave() == 1? "☑" : "□");
        inspectionOrderExportDto.setIsLeave2(inspectionOrder.getIsLeave() != null && inspectionOrder.getIsLeave() == 0? "☑" : "□");
        inspectionOrderExportDto.setSend0(inspectionOrder.getSend() != null && inspectionOrder.getSend() == 1? "☑" : "□");
        inspectionOrderExportDto.setSend1(inspectionOrder.getSend() != null && inspectionOrder.getSend() == 0? "☑" : "□");
        inspectionOrderExportDto.setProcessing0(inspectionOrder.getProcessing() != null && inspectionOrder.getProcessing() == 0? "☑" : "□");
        inspectionOrderExportDto.setProcessing1(inspectionOrder.getProcessing() != null && inspectionOrder.getProcessing() == 1? "☑" : "□");
        inspectionOrderExportDto.setCriterionRule0(inspectionOrder.getCriterionRule() != null && inspectionOrder.getCriterionRule() == 0? "☑" : "□");
        inspectionOrderExportDto.setCriterionRule1(inspectionOrder.getCriterionRule() != null && inspectionOrder.getCriterionRule() == 1? "☑" : "□");
        return inspectionOrderExportDto;
    }
}
