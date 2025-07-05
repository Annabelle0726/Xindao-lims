package com.ruoyi.require.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import com.ruoyi.require.mapper.FeCalibrationScheduleMapper;
import com.ruoyi.require.pojo.FeCalibrationSchedule;
import com.ruoyi.require.service.FeCalibrationScheduleService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author
 * @since 2024-11-13 02:53:05
 */
@Service
public class FeCalibrationScheduleServiceImpl extends ServiceImpl<FeCalibrationScheduleMapper, FeCalibrationSchedule> implements FeCalibrationScheduleService {

    @Override
    public IPage<FeCalibrationSchedule> page(Page page, String instrumentName, String managementNumber) {
        IPage<FeCalibrationSchedule> ipage = this.baseMapper.ipage(page,instrumentName, managementNumber);
        return ipage;
    }

    @Override
    public void exportWordOfValueTraceabilityPlan(HttpServletResponse response) {
        List<FeCalibrationSchedule> feCalibrationSchedules = this.baseMapper.selectList(null);

        int size = feCalibrationSchedules.size();
        for (int i = 0; i < size; i++) {
            FeCalibrationSchedule feCalibrationSchedule = feCalibrationSchedules.get(i);
            feCalibrationSchedule.setIndex(i + 1);
        }

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/value-traceability-plan.docx");
        Configure configure = Configure.builder()
                .bind("feCalibrationSchedule", new HackLoopTableRenderPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("feCalibrationSchedule", feCalibrationSchedules);
                }}
        );

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "仪器设备量值溯源管理总体计划", "UTF-8");
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
