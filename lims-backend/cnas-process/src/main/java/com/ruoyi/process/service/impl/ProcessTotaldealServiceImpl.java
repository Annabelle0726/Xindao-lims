package com.ruoyi.process.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.data.*;
import com.deepoove.poi.data.style.*;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import com.ruoyi.inspect.util.UserUtils;
import com.ruoyi.process.dto.ProcessDealDto;
import com.ruoyi.process.mapper.InspectionOrderMapper;
import com.ruoyi.process.mapper.ProcessDealMapper;
import com.ruoyi.process.mapper.ProcessSampleMapper;
import com.ruoyi.process.mapper.ProcessTotaldealMapper;
import com.ruoyi.process.pojo.InspectionOrder;
import com.ruoyi.process.pojo.ProcessDeal;
import com.ruoyi.process.pojo.ProcessSample;
import com.ruoyi.process.pojo.ProcessTotaldeal;
import com.ruoyi.process.service.ProcessDealService;
import com.ruoyi.process.service.ProcessTotaldealService;
import com.ruoyi.system.mapper.UserMapper;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.SAAJMetaFactory;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 检测或校准物品的处置总表(历史) 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-02 03:59:09
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProcessTotaldealServiceImpl extends ServiceImpl<ProcessTotaldealMapper, ProcessTotaldeal> implements ProcessTotaldealService {

    @Resource
    private ProcessTotaldealMapper processTotaldealMapper;

    @Resource
    private ProcessSampleMapper processSampleMapper;

    @Resource
    private ProcessDealService processDealService;

    @Resource
    private InspectionOrderMapper inspectionOrderMapper;

   @Resource
   private ProcessDealMapper processDealMapper;

    @Value("${file.path}")
    private String imgUrl;


    @Resource
    private UserMapper userMapper;

    @Override
    public IPage<ProcessTotaldeal> pageProcessTotaldeal(Page page, ProcessTotaldeal processTotaldeal) {
        return processTotaldealMapper.pageProcessTotaldeal(page, QueryWrappers.queryWrappers(processTotaldeal));

    }

    @Override
    public int submitProcessTotaldeal(Integer id) {
        Integer userId = SecurityUtils.getUserId().intValue();
        User user = userMapper.selectById(userId);
        if (ObjectUtils.isEmpty(user.getSignatureUrl())) {
            throw new ErrorException("未找到填表人的电子签名,请上传自己的电子签名!");
        }
        ProcessTotaldeal processTotaldeal = processTotaldealMapper.selectById(id);
        processTotaldeal.setSubmitUser(userId);
        processTotaldeal.setSubmitState("已提交");
        return processTotaldealMapper.updateById(processTotaldeal);
    }

    @Override
    public int checkProcessTotaldeal(Integer id, String state) {
        Integer userId = SecurityUtils.getUserId().intValue();
        User user = userMapper.selectById(userId);
        if (ObjectUtils.isEmpty(user.getSignatureUrl())) {
            throw new ErrorException("未找到审核人的电子签名,请上传自己的电子签名!");
        }
        ProcessTotaldeal processTotaldeal = processTotaldealMapper.selectById(id);
        processTotaldeal.setExamineUser(userId);
        processTotaldeal.setExamineState(state);
        if (state.equals("不通过")) {
            processTotaldeal.setSubmitState("待提交");
        }
        return processTotaldealMapper.updateById(processTotaldeal);
    }

    @Override
    public int ratifyProcessTotaldeal(Integer id, String state) {
        Integer userId = SecurityUtils.getUserId().intValue();
        User user = userMapper.selectById(userId);
        if (ObjectUtils.isEmpty(user.getSignatureUrl())) {
            throw new ErrorException("未找到批准人的电子签名,请上传自己的电子签名!");
        }
        ProcessTotaldeal processTotaldeal = processTotaldealMapper.selectById(id);
        processTotaldeal.setRatifyUser(userId);
        processTotaldeal.setRatifyState(state);
        if (state.equals("不通过")) {
            processTotaldeal.setExamineState(state);
            processTotaldeal.setSubmitState("待提交");
        }
        return processTotaldealMapper.updateById(processTotaldeal);
    }

    @Override
    public int addProcessTotaldeal(String month) {
        //新增总表
        ProcessTotaldeal processTotaldeal = new ProcessTotaldeal();
        List<ProcessTotaldeal> processTotaldeals = processTotaldealMapper.selectList(Wrappers.<ProcessTotaldeal>lambdaQuery()
                .eq(ProcessTotaldeal::getMonth, month));
        if (processTotaldeals.size()<=0) {
            processTotaldeal.setMonth(month);//月份
            processTotaldeal.setSubmitState("待提交");
            processTotaldealMapper.insert(processTotaldeal);
        }else {
            processTotaldeal=processTotaldeals.get(0);
        }
        //新增详情表
        //1.获取该月份的第一天和最后一天
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth yearMonth = YearMonth.parse(month, formatter);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        //2.查询在这个时间范围内的所有样品登记的数据
        List<ProcessSample> processSamples = processSampleMapper.selectList(Wrappers.<ProcessSample>lambdaQuery()
                .between(ProcessSample::getLeaveDate, firstDay, lastDay));
        if (processSamples.size()>0){
            //获取处理方式
            InspectionOrder inspectionOrder = inspectionOrderMapper.selectById(processSamples.get(0).getInspectionOrderId());
            List<ProcessDeal> processDeals = new ArrayList<>();
            for (ProcessSample processSample : processSamples) {
                long count = processDealService.count(Wrappers.<ProcessDeal>lambdaQuery().eq(ProcessDeal::getProcessSampleId, processSample.getId()));
                if (count>0){
                    continue;
                }
                ProcessDeal processDeal = new ProcessDeal();
                processDeal.setSampleName(processSample.getSampleName());//样品名称
                processDeal.setSampleCode(processSample.getSampleCode());//样品编号
                processDeal.setSampleSupplier(processSample.getSampleSupplier());//供样单位
                processDeal.setNum(processSample.getNum());//样品数量
                processDeal.setDealMethod(inspectionOrder.getProcessing()==1?"实验室处理":"委托单位取回");//处理方式
                processDeal.setDealTime(LocalDate.now());//时间=当前时间
                processDeal.setTotaldealId(processTotaldeal.getId());//总表id
                processDeal.setProcessSampleId(processSample.getId());//样品接收id
                processDeals.add(processDeal);
            }
            processDealService.saveBatch(processDeals);
        }
        return 0;
    }

    @Override
    public void exportProcessTotaldeal(Integer id, HttpServletResponse response) {
        List<ProcessDealDto> sampleDealDtos = processDealMapper.selectDeal(id);
        ProcessTotaldeal processTotaldeal = processTotaldealMapper.selectById(id);

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/sample-deal.docx");
        Configure configure = Configure.builder()
                .bind("sampleList", new HackLoopTableRenderPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("sampleList", sampleDealDtos);
                    put("writeUrl",UserUtils.getFinalUserSignatureUrl(processTotaldeal.getSubmitUser()));//提交人
                    put("examineUrl", UserUtils.getFinalUserSignatureUrl(processTotaldeal.getExamineUser()));//审核人
                    put("ratifyUrl", UserUtils.getFinalUserSignatureUrl(processTotaldeal.getRatifyUser()));//批准人
                }});
        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "样品处理申请表", "UTF-8");
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
}
