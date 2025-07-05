package com.ruoyi.process.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.data.Pictures;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.process.dto.QualitySuperviseDetailsCorrectDto;
import com.ruoyi.process.pojo.QualitySuperviseDetailsCorrect;
import com.ruoyi.process.pojo.QualitySuperviseManagementReview;
import com.ruoyi.process.mapper.QualitySuperviseManagementReviewMapper;
import com.ruoyi.process.service.QualitySuperviseManagementReviewService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * <p>
 * 质量监督管理评审输入材料 服务实现类
 * </p>
 *
 * @author
 * @since 2025-05-07 10:46:11
 */
@Service
public class QualitySuperviseManagementReviewServiceImpl extends ServiceImpl<QualitySuperviseManagementReviewMapper, QualitySuperviseManagementReview> implements QualitySuperviseManagementReviewService {

    /**
     * 分页查询
     * @param page
     * @param managementReview
     * @return
     */
    @Override
    public IPage<QualitySuperviseManagementReview> pageManagementReview(Page page, QualitySuperviseManagementReview managementReview) {
        return baseMapper.pageManagementReview(page, QueryWrappers.queryWrappers(managementReview));
    }

    /**
     * 导出
     * @param managementReviewId
     * @param response
     */
    @Override
    public void exportManagementReview(Integer managementReviewId, HttpServletResponse response) {
        QualitySuperviseManagementReview managementReview = baseMapper.selectById(managementReviewId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/supervise-management-eview.docx");
        ConfigureBuilder builder = Configure.builder();
        builder.useSpringEL(true);
        XWPFTemplate template = XWPFTemplate.compile(inputStream, builder.build()).render(
                new HashMap<String, Object>() {{
                    put("managementReview", managementReview);
                    put("superviseDateStr", managementReview.getSuperviseDate() == null ? null : managementReview.getSuperviseDate().format(formatter));
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    managementReview.getFileName(), "UTF-8");
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
