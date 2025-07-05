package com.ruoyi.manage.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.Pictures;
import com.ruoyi.common.utils.DateImageUtil;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import com.ruoyi.manage.mapper.ManageRiskAssessmentResultsMapper;
import com.ruoyi.manage.pojo.ManageRiskAssessmentResults;
import com.ruoyi.manage.service.ManageRiskAssessmentResultsService;
import com.ruoyi.manage.vo.ManageRiskAssessmentResultsVo;
import com.ruoyi.system.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * <p>
 * 危险因素辨识与风险评价结果一览表 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-15 02:58:51
 */
@Service
public class ManageRiskAssessmentResultsServiceImpl extends ServiceImpl<ManageRiskAssessmentResultsMapper, ManageRiskAssessmentResults> implements ManageRiskAssessmentResultsService {

    @Autowired
    private UserMapper userMapper;

    @Value("${file.path}")
    private String imgUrl;

    @Override
    public IPage<ManageRiskAssessmentResultsVo> getPageResults(Page page) {
        return baseMapper.getPageResults(page, false);
    }

    @Override
    public void exportPersonTraining(HttpServletResponse response) {
        // 查询详情
        IPage<ManageRiskAssessmentResultsVo> detailedDtos = baseMapper.getPageResults(new Page(1, -1), true);
        if (detailedDtos.getRecords().isEmpty()) {
            throw new ErrorException("审核通过的数据为空！请审核通过后在导出");
        }
        ManageRiskAssessmentResultsVo manageRiskAssessmentResultsVo = detailedDtos.getRecords().get(0);

        //获取编制人的签名地址
        String writeUrl = userMapper.selectById(manageRiskAssessmentResultsVo.getEditor()).getSignatureUrl();
        if (ObjectUtils.isEmpty(writeUrl) || writeUrl.equals("")) {
            throw new ErrorException("找不到检验人的签名");
        }

        //获取复核人的签名地址
        String examineUrl = null;
        if (manageRiskAssessmentResultsVo.getApproval() != null) {
            examineUrl = userMapper.selectById(manageRiskAssessmentResultsVo.getApproval()).getSignatureUrl();
            if (StringUtils.isBlank(examineUrl)) {
                throw new ErrorException("找不到复核人的签名");
            }
        }

        //获取批准人的签名地址
        String ratifyUrl = null;
        if (manageRiskAssessmentResultsVo.getApproval() != null) {
            ratifyUrl = userMapper.selectById(manageRiskAssessmentResultsVo.getApproval()).getSignatureUrl();
            if (StringUtils.isBlank(ratifyUrl)) {
                throw new ErrorException("找不到复核人的签名");
            }
        }

        int index = 1;
        for (ManageRiskAssessmentResultsVo detailedDto : detailedDtos.getRecords()) {
            detailedDto.setIndex(index);
            index++;
        }
        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/risk-factor-identification-risk.docx");
        String finalExamineUrl = examineUrl;
        String finalRatifyUrl = ratifyUrl;
        Configure configure = Configure.builder()
                .bind("trainingDetailedList", new HackLoopTableRenderPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("trainingDetailedList", detailedDtos.getRecords());
                    put("writeUrl", StringUtils.isNotBlank(writeUrl) ? Pictures.ofLocal(imgUrl + "/" + writeUrl).create() : null);
                    put("examineUrl", StringUtils.isNotBlank(finalExamineUrl) ? Pictures.ofLocal(imgUrl + "/" + finalExamineUrl).create() : null);
                    put("ratifyUrl", StringUtils.isNotBlank(finalRatifyUrl) ? Pictures.ofLocal(imgUrl + "/" + finalRatifyUrl).create() : null);
                    put("writeDateUrl", manageRiskAssessmentResultsVo.getEditorDate() != null ?
                            Pictures.ofStream(DateImageUtil.createDateImage(manageRiskAssessmentResultsVo.getEditorDate())).create() : null);
                    put("examineDateUrl", manageRiskAssessmentResultsVo.getApproveDate() != null ?
                            Pictures.ofStream(DateImageUtil.createDateImage(manageRiskAssessmentResultsVo.getApproveDate())).create() : null);
                    put("ratifyDateUrl", manageRiskAssessmentResultsVo.getApproveDate() != null ?
                            Pictures.ofStream(DateImageUtil.createDateImage(manageRiskAssessmentResultsVo.getApproveDate())).create() : null);
                }});
        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "危险因素辨识与风险评价结果一览", "UTF-8");
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
