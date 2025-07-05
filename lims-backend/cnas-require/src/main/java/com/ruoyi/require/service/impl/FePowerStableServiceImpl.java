package com.ruoyi.require.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.Pictures;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import com.ruoyi.require.dto.FePowerStableDto;
import com.ruoyi.require.dto.FePowerStableExportDto;
import com.ruoyi.require.mapper.FeMeasuredQuantityMapper;
import com.ruoyi.require.mapper.FePowerStableMapper;
import com.ruoyi.require.pojo.FeMeasuredQuantity;
import com.ruoyi.require.pojo.FePowerStable;
import com.ruoyi.require.service.FePowerStableService;
import com.ruoyi.system.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 设施和环境条件-设施和环境条件要求-电源稳定性 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-07 04:16:52
 */
@Service
public class FePowerStableServiceImpl extends ServiceImpl<FePowerStableMapper, FePowerStable> implements FePowerStableService {

    @Resource
    private FeMeasuredQuantityMapper feMeasuredQuantityMapper;
    @Resource
    private UserMapper userMapper;

    @Value("${file.path}")
    private String imgUrl;

    @Override
    public IPage<FePowerStableDto> getLaboratoryFacilityPowerStablePage(Page page) {
        return baseMapper.getLaboratoryFacilityPowerStablePage(page);
    }

    @Override
    public Map<String, Objects> getCalibrationDate(Integer deviceId) {
        return baseMapper.getCalibrationDate(deviceId);
    }

    /**
     * 导出电源稳定性
     * @param powerStableId
     * @param response
     */
    @Override
    public void exportFePowerStable(Integer powerStableId, HttpServletResponse response) {
        FePowerStableExportDto powerStable = baseMapper.selectPowerStable(powerStableId);

        // 检测人
        String testerUrl = null;
        if (powerStable.getTesterId() != null) {
            testerUrl = userMapper.selectById(powerStable.getTesterId()).getSignatureUrl();
            if (StringUtils.isBlank(testerUrl)) {
                throw new ErrorException("找不到检测人的签名");
            }
        }

        // 核查人
        String checkerUrl = null;
        if (powerStable.getCheckerId() != null) {
            checkerUrl = userMapper.selectById(powerStable.getCheckerId()).getSignatureUrl();
            if (StringUtils.isBlank(checkerUrl)) {
                throw new ErrorException("找不到核查人的签名");
            }
        }

        // 查询详情
        List<FeMeasuredQuantity> feMeasuredQuantities = feMeasuredQuantityMapper.selectList(Wrappers.<FeMeasuredQuantity>lambdaQuery()
                .eq(FeMeasuredQuantity::getPowerStableId, powerStableId));

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/power-stable.docx");
        Configure configure = Configure.builder()
                .bind("measuredQuantityList", new HackLoopTableRenderPolicy())
                .build();
        String finalTesterUrl = testerUrl;
        String finalCheckerUrl = checkerUrl;
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("stable", powerStable);
                    put("measuredQuantityList", feMeasuredQuantities);
                    put("testerUrl", StringUtils.isNotBlank(finalTesterUrl) ? Pictures.ofLocal(imgUrl + "/" + finalTesterUrl).create() : null);
                    put("checkerUrl", StringUtils.isNotBlank(finalCheckerUrl) ? Pictures.ofLocal(imgUrl + "/" + finalCheckerUrl).create() : null);
                }});
        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "电源稳定性测试导出", "UTF-8");
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
