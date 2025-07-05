package com.ruoyi.manage.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.data.FilePictureRenderData;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.manage.mapper.ManageReviewReportMapper;
import com.ruoyi.manage.pojo.ManageReviewReport;
import com.ruoyi.manage.service.ManageReviewReportService;
import com.ruoyi.system.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 管理评审报告 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-12 04:44:39
 */
@Service
public class ManageReviewReportServiceImpl extends ServiceImpl<ManageReviewReportMapper, ManageReviewReport> implements ManageReviewReportService {

    @Resource
    UserMapper userMapper;

    @Value("${file.path}")
    private String imgUrl;

    @Override
    public IPage<ManageReviewReport> page(Page page, String startTime, String endTime, String place) {
        IPage<ManageReviewReport> iPage = this.baseMapper.page(page, startTime, endTime, place);
        return iPage;
    }

    @Override
    public void exportReviewReport(Integer id, HttpServletResponse response) {
        ManageReviewReport manageReviewReport = baseMapper.selectById(id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        //完成时间
        String completionDate = "";
        if (ObjectUtils.isNotEmpty(manageReviewReport.getCompletionDate())) {
            completionDate = manageReviewReport.getCompletionDate().format(formatter);
        }
        //出席人员
        String attendessName=null;
        if (ObjectUtils.isNotEmpty(manageReviewReport.getAttendess())) {
            List<String> name = new ArrayList<>();
            for (String s : manageReviewReport.getAttendess().split(",")) {
                User user = userMapper.selectById(Integer.parseInt(s));
                name.add(user.getName());
            }
            attendessName = name.stream().collect(Collectors.joining(","));
        }
        //日期
        String createTime = manageReviewReport.getCreateTime().format(formatter);
        //审核人
        String signatureUrl1 = null;
        if (ObjectUtils.isNotEmpty(manageReviewReport.getAudit())) {
            User user1 = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getName, manageReviewReport.getAudit()));
            if (ObjectUtils.isEmpty(user1.getSignatureUrl())) {
                throw new ErrorException(user1.getName() + "的个人签名没有上传");
            }
            signatureUrl1 = user1.getSignatureUrl();
        }
        //批准人
        String signatureUrl2 = null;
        if (ObjectUtils.isNotEmpty(manageReviewReport.getApproval())) {
            User user2 = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getName, manageReviewReport.getApproval()));
            if (ObjectUtils.isEmpty(user2.getSignatureUrl())) {
                throw new ErrorException(user2.getName() + "的个人签名没有上传");
            }
            signatureUrl2 = user2.getSignatureUrl();
        }


        InputStream inputStream = this.getClass().getResourceAsStream("/static/review-report.docx");
        ConfigureBuilder builder = Configure.builder();
        builder.useSpringEL(true);
        String finalCompletionDate = completionDate;
        FilePictureRenderData picture1=null;
        FilePictureRenderData picture2=null;
        if (signatureUrl1!=null){
             picture1 = new FilePictureRenderData(100, 50, imgUrl + "/" + signatureUrl1);
        }
        if (signatureUrl2!=null) {
             picture2 = new FilePictureRenderData(100, 50, imgUrl + "/" + signatureUrl2);
        }
        FilePictureRenderData finalPicture1 = picture1;
        FilePictureRenderData finalPicture2 = picture2;
        String finalAttendessName = attendessName;
        XWPFTemplate template = XWPFTemplate.compile(inputStream, builder.build()).render(
                new HashMap<String, Object>() {{
                    put("report", manageReviewReport);
                    put("completionDate", finalCompletionDate);
                    put("date", manageReviewReport.getDate().format(formatter));
                    put("createTime", createTime);
                    put("examineUrl", finalPicture1);
                    put("ratifyUrl", finalPicture2);
                    put("attendessName", finalAttendessName);
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "评审报告", "UTF-8");
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
