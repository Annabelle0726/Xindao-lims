package com.ruoyi.manage.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.data.Pictures;


import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.manage.mapper.InternalReportMapper;

import com.ruoyi.manage.pojo.InternalReport;

import com.ruoyi.manage.service.InternalReportService;

import com.ruoyi.system.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * 内审报告表
 *
 * @author zhuo
 * @since 2024-11-11
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class InternalReportServiceImpl extends ServiceImpl<InternalReportMapper, InternalReport> implements InternalReportService {


    @Resource
    private UserMapper userMapper;
    @Value("${file.path}")
    private String imgUrl;

    /**
     * 内审报告分页查询
     * @param page
     * @param internalReport
     * @return
     */
    @Override
    public IPage<InternalReport> pageInternalReport(Page page, InternalReport internalReport) {
        return baseMapper.pageInternalReport(page, QueryWrappers.queryWrappers(internalReport));
    }

    /**
     * 内审报告审核
     * @param internalReport
     * @return
     */
    @Override
    public boolean ratifyInternalCheck(InternalReport internalReport) {
        Integer userId = SecurityUtils.getUserId().intValue();
        User user = userMapper.selectById(userId);
        baseMapper.update(null, Wrappers.<InternalReport>lambdaUpdate()
                .eq(InternalReport::getReportId, internalReport.getReportId())
                .set(InternalReport::getExamineUserId, userId)
                .set(InternalReport::getExamineUserName, user.getName())
                .set(InternalReport::getExamineRemark, internalReport.getExamineRemark())
                .set(InternalReport::getExamineStatus, internalReport.getExamineStatus())
                .set(InternalReport::getExamineTime, LocalDateTime.now())
        );
        return true;
    }

    /**
     * 内审报告质量负责人填写
     * @param internalReport
     * @return
     */
    @Override
    public boolean qualityInternalReport(InternalReport internalReport) {
        Integer userId = SecurityUtils.getUserId().intValue();
        User user = userMapper.selectById(userId);
        baseMapper.update(null, Wrappers.<InternalReport>lambdaUpdate()
                .eq(InternalReport::getReportId, internalReport.getReportId())
                .set(InternalReport::getQualityUserId, userId)
                .set(InternalReport::getQualityUserName, user.getName())
                .set(InternalReport::getQualityRemark, internalReport.getQualityRemark())
                .set(InternalReport::getQualityStatus, internalReport.getQualityStatus())
                .set(InternalReport::getQualityTime, LocalDateTime.now())
        );
        return true;
    }

    /**
     * 导出内审报告
     * @param reportId
     * @param response
     */
    @Override
    public void exportInternalReport(Integer reportId, HttpServletResponse response) {
        InternalReport internalReport = baseMapper.selectById(reportId);

        //获取审核人的签名地址
        String examineUrl = null;
        if (internalReport.getExamineUserId() != null) {
            examineUrl = userMapper.selectById(internalReport.getExamineUserId()).getSignatureUrl();
            if (StringUtils.isBlank(examineUrl)) {
                throw new ErrorException("找不到审核人的签名");
            }
        }

        //获取质量负责人的签名地址
        String qualityUrl = null;
        if (internalReport.getQualityUserId() != null) {
            qualityUrl = userMapper.selectById(internalReport.getQualityUserId()).getSignatureUrl();
            if (StringUtils.isBlank(qualityUrl)) {
                throw new ErrorException("找不到质量负责人的签名");
            }
        }

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/internal-report.docx");
        ConfigureBuilder builder = Configure.builder();
        builder.useSpringEL(true);
        String finalExamineUrl = examineUrl;
        String finalQualityUrl = qualityUrl;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        XWPFTemplate template = XWPFTemplate.compile(inputStream, builder.build()).render(
                new HashMap<String, Object>() {{
                    put("report", internalReport);
                    put("examineUrl", StringUtils.isNotBlank(finalExamineUrl) ? Pictures.ofLocal(imgUrl + "/" + finalQualityUrl).create() : null);
                    put("qualityUrl", StringUtils.isNotBlank(finalQualityUrl) ? Pictures.ofLocal(imgUrl + "/" + finalQualityUrl).create() : null);
                    put("examineTime", internalReport.getExamineTime() != null ? internalReport.getExamineTime().format(formatter) : null);
                    put("qualityTime", internalReport.getQualityTime() != null ? internalReport.getQualityTime().format(formatter) : null);
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "内审报告", "UTF-8");
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

