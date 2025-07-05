package com.ruoyi.process.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.basic.pojo.StandardTemplate;
import com.ruoyi.common.numgen.NumberGenerator;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.inspect.mapper.InsReportMapper;
import com.ruoyi.inspect.mapper.InsSampleMapper;
import com.ruoyi.inspect.pojo.InsReport;
import com.ruoyi.inspect.pojo.InsSample;
import com.ruoyi.process.dto.ProcessComplainDto;
import com.ruoyi.process.mapper.ProcessComplainMapper;
import com.ruoyi.process.pojo.ProcessComplain;
import com.ruoyi.process.service.ProcessComplainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 投诉 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-02 09:29:11
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProcessComplainServiceImpl extends ServiceImpl<ProcessComplainMapper, ProcessComplain> implements ProcessComplainService {

    @Resource
    private ProcessComplainMapper processComplainMapper;

    @Resource
    private InsReportMapper insReportMapper;

    @Resource
    private InsSampleMapper insSampleMapper;

    @Resource
    private NumberGenerator<ProcessComplain> numberGenerator;


    @Override
    public IPage<ProcessComplain> pageProcessComplain(Page page, ProcessComplain processComplain) {
        return processComplainMapper.pageProcessComplain(page, QueryWrappers.queryWrappers(processComplain));
    }

    @Override
    public int addProcessComplain(ProcessComplain processComplain) {
        //判断报告编号和样品编号是否存在
        InsReport insReport = insReportMapper.selectOne(Wrappers.<InsReport>lambdaQuery().eq(InsReport::getCode, processComplain.getCode()));
        if (ObjectUtils.isEmpty(insReport)) {
            throw new ErrorException("报告编号输入有误");
        }
        processComplain.setInsReportId(insReport.getId());
        InsSample insSample = insSampleMapper.selectOne(Wrappers.<InsSample>lambdaQuery().eq(InsSample::getSampleCode, processComplain.getSampleCode()));
        if (ObjectUtils.isEmpty(insSample)) {
            throw new ErrorException("样品编号输入有误");
        }
        //投诉编号生成
        String giveCode = numberGenerator.generateNumberWithPrefix(3,
                "JCZX-" + DateUtil.format(new Date(), "yyMMdd"),
                ProcessComplain::getComplainNo);
        processComplain.setComplainNo(giveCode);
        return processComplainMapper.insert(processComplain);
    }

    @Override
    public ProcessComplainDto getProcessComplain(Long id) {
        return processComplainMapper.getProcessComplain(id);
    }

    @Override
    public int doProcessComplain(ProcessComplain processComplain) {
        return processComplainMapper.updateById(processComplain);
    }

    @Override
    public void exportProcessComplain(ProcessComplain processComplain, HttpServletResponse response) throws Exception {
        List<ProcessComplain> data = processComplainMapper.pageProcessComplain(new Page(-1, -1), QueryWrappers.queryWrappers(processComplain)).getRecords();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        String fileName = URLEncoder.encode("投诉列表导出", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        try {
            // 新建ExcelWriter
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build();
            WriteSheet mainSheet = EasyExcel.writerSheet(0, "投诉列表导出").head(ProcessComplain.class).build();
            excelWriter.write(data, mainSheet);
            // 关闭流
            excelWriter.finish();
        } catch (IOException e) {
            throw new RuntimeException("导出失败");
        }
    }
}
