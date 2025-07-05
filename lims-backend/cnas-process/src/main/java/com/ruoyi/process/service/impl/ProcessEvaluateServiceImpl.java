package com.ruoyi.process.service.impl;

import cn.hutool.core.lang.UUID;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.process.mapper.ProcessEvaluateMapper;
import com.ruoyi.process.pojo.ProcessEvaluate;
import com.ruoyi.process.service.ProcessEvaluateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 测量不确定度的评价 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-02 01:10:43
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProcessEvaluateServiceImpl extends ServiceImpl<ProcessEvaluateMapper, ProcessEvaluate> implements ProcessEvaluateService {

    @Resource
    private ProcessEvaluateMapper processEvaluateMapper;

    @Value("${wordUrl}")
    private String wordUrl;

    @Override
    public IPage<ProcessEvaluate> pageProcessEvaluate(Page page, ProcessEvaluate processEvaluate) {
        return processEvaluateMapper.pageProcessEvaluate(page, QueryWrappers.queryWrappers(processEvaluate));
    }

    @Override
    public int addProcessEvaluate(MultipartFile file) {
        String urlString;
        String pathName;
        String path=wordUrl;
        ProcessEvaluate processEvaluate = new ProcessEvaluate();
        processEvaluate.setReportName(file.getOriginalFilename());
        try {
            File realpath = new File(path);
            if (!realpath.exists()) {
                realpath.mkdirs();
            }
            pathName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            urlString = realpath + "/" + pathName;
            file.transferTo(new File(urlString));
            processEvaluate.setReportUrl(pathName);
            return processEvaluateMapper.insert(processEvaluate);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("附件上传错误");
            return 0;
        }
    }

    @Override
    public int doProcessEvaluate(ProcessEvaluate processEvaluate) {
        return processEvaluateMapper.updateById(processEvaluate);
    }

    @Override
    public void exportProcessEvaluate(ProcessEvaluate processEvaluate, HttpServletResponse response) throws Exception{
        List<ProcessEvaluate> data = processEvaluateMapper.pageProcessEvaluate(new Page(-1, -1), QueryWrappers.queryWrappers(processEvaluate)).getRecords();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        String fileName = URLEncoder.encode("测量不确定度的评定导出", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        try {
            // 新建ExcelWriter
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build();
            WriteSheet mainSheet = EasyExcel.writerSheet(0, "测量不确定度的评定导出").head(ProcessEvaluate.class).build();
            excelWriter.write(data, mainSheet);
            // 关闭流
            excelWriter.finish();
        } catch (IOException e) {
            throw new RuntimeException("导出失败");
        }
    }
}
