package com.ruoyi.require.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.Pictures;
import com.ruoyi.common.utils.DateImageUtil;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import com.ruoyi.require.dto.FeIlluminationDto;
import com.ruoyi.require.dto.FeIlluminationExportDto;
import com.ruoyi.require.mapper.FeIlluminationDetectionAreaMapper;
import com.ruoyi.require.mapper.FeIlluminationMapper;
import com.ruoyi.require.pojo.FeIllumination;
import com.ruoyi.require.pojo.FeIlluminationDetectionArea;
import com.ruoyi.require.service.FeIlluminationService;
import com.ruoyi.system.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 设施和环境条件-设施和环境条件要求-照度记录表 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-07 04:15:57
 */
@Service
public class FeIlluminationServiceImpl extends ServiceImpl<FeIlluminationMapper, FeIllumination> implements FeIlluminationService {

    @Resource
    private FeIlluminationDetectionAreaMapper feIlluminationDetectionAreaMapper;
    @Resource
    private UserMapper userMapper;

    @Value("${file.path}")
    private String imgUrl;


    @Override
    public IPage<FeIlluminationDto> getFeLightningProtection(Page page) {
        return baseMapper.getFeLightningProtection(page);
    }

    /**
     * 导出照度记录
     * @param intensityIlluminationId
     * @param response
     */
    @Override
    public void exportFeIllumination(Integer intensityIlluminationId, HttpServletResponse response) {
        FeIlluminationExportDto illuminationExportDto = baseMapper.selectFeIllumination(intensityIlluminationId);
        // 检测人
        String testerUrl = null;
        if (illuminationExportDto.getTesterId() != null) {
            testerUrl = userMapper.selectById(illuminationExportDto.getTesterId()).getSignatureUrl();
            if (StringUtils.isBlank(testerUrl)) {
                throw new ErrorException("找不到检测人的签名");
            }
        }

        // 核查人
        String checkerUrl = null;
        if (illuminationExportDto.getCheckerId() != null) {
            checkerUrl = userMapper.selectById(illuminationExportDto.getCheckerId()).getSignatureUrl();
            if (StringUtils.isBlank(checkerUrl)) {
                throw new ErrorException("找不到核查人的签名");
            }
        }

        // 查询详情
        List<FeIlluminationDetectionArea> feIlluminationDetectionAreas = feIlluminationDetectionAreaMapper.selectList(Wrappers.<FeIlluminationDetectionArea>lambdaQuery()
                .eq(FeIlluminationDetectionArea::getIntensityIlluminationId, intensityIlluminationId));

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/illumination.docx");
        Configure configure = Configure.builder()
                .bind("detectionAreaList", new HackLoopTableRenderPolicy())
                .build();
        String finalTesterUrl = testerUrl;
        String finalCheckerUrl = checkerUrl;

        // 判断检测日期是否为空
        LocalDateTime finalLocalDateTime = illuminationExportDto.getTestDate() == null ? null : illuminationExportDto.getTestDate().atStartOfDay();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("illumination", illuminationExportDto);
                    put("detectionAreaList", feIlluminationDetectionAreas);
                    put("testerUrl", StringUtils.isNotBlank(finalTesterUrl) ? Pictures.ofLocal(imgUrl + "/" + finalTesterUrl).create() : null);
                    put("checkerUrl", StringUtils.isNotBlank(finalCheckerUrl) ? Pictures.ofLocal(imgUrl + "/" + finalCheckerUrl).create() : null);
                    put("testDateUrl", finalLocalDateTime != null ?
                            Pictures.ofStream(DateImageUtil.createDateImage(finalLocalDateTime)).create() : null);
                }});
        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "照明记录导出", "UTF-8");
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
