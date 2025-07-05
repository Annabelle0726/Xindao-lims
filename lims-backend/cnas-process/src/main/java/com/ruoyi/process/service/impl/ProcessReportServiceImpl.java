package com.ruoyi.process.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
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
import com.ruoyi.common.utils.SignatureImageUtil;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import com.ruoyi.process.dto.ProcessReportDto;
import com.ruoyi.process.mapper.ProcessReportMapper;
import com.ruoyi.process.pojo.ProcessReport;
import com.ruoyi.process.service.ProcessReportService;
import com.ruoyi.system.mapper.UserMapper;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 检验报告发放登记表 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-05 08:58:39
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProcessReportServiceImpl extends ServiceImpl<ProcessReportMapper, ProcessReport> implements ProcessReportService {

    @Resource
    private ProcessReportMapper processReportMapper;
    @Value("${file.path}")
    private String imgUrl;

    @Resource
    private UserMapper userMapper;

    @Override
    public IPage<ProcessReportDto> pageProcessReport(Page page, ProcessReportDto processReport) {
        IPage<ProcessReportDto> processReportIPage = processReportMapper.pageProcessReport(page, QueryWrappers.queryWrappers(processReport));

        return processReportIPage;
    }

    @Override
    public void exportProcessReport(ProcessReportDto dto, HttpServletResponse response) {
        List<ProcessReportDto> processReports = processReportMapper.exportProcessReport();
        for (ProcessReportDto processReport : processReports) {
            // 发送人生成签名图片
            processReport.setSendUserRender(StringUtils.isNotBlank(processReport.getSendUserUrl())
                    ? Pictures.ofLocal(imgUrl + "/" + processReport.getSendUserUrl()).create() : null);

            // 签收人生成图片
            processReport.setSignatoryRender(StringUtils.isNotBlank(processReport.getSignatory())
                    ? Pictures.ofStream(SignatureImageUtil.saveImageToFile(processReport.getSignatory())).create() : null);
        }

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/report-deal.docx");
        Configure configure = Configure.builder()
                .bind("reportList", new HackLoopTableRenderPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("reportList", processReports);
                }});
        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "检验报告发放登记表", "UTF-8");
            response.setHeader("Content-disposition",
                    "attachment;filename=" + fileName + ".docx");
            OutputStream os = response.getOutputStream();
            template.write(os);
            os.flush();
            os.close();
            inputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("导出失败");
        }
    }
}
