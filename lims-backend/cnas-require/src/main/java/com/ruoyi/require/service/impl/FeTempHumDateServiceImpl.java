package com.ruoyi.require.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.Pictures;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import com.ruoyi.require.dto.FeTempHumDateDto;
import com.ruoyi.require.dto.FeTempHumRecordDto;
import com.ruoyi.require.mapper.FeTempHumDateMapper;
import com.ruoyi.require.pojo.FeTempHumDate;
import com.ruoyi.require.pojo.FeTempHumRecord;
import com.ruoyi.require.service.FeTempHumDateService;
import com.ruoyi.require.service.FeTempHumRecordService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 设施和环境条件-设施和环境条件要求-温湿度 区域 -父 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-09 11:02:18
 */
@Service
public class FeTempHumDateServiceImpl extends ServiceImpl<FeTempHumDateMapper, FeTempHumDate> implements FeTempHumDateService {

    @Resource
    private FeTempHumRecordService feTempHumRecordService;
    @Value("${file.path}")
    private String imgUrl;

    @Override
    public IPage<FeTempHumDateDto> getFeTempHumDate(Page page, FeTempHumDateDto feTempHumDateDto) {
        return baseMapper.getFeTempHumDate(page, QueryWrappers.queryWrappers(feTempHumDateDto));
    }

    @Override
    public void exportTemperatureAndHumidityRecords(Integer dateId, HttpServletResponse response) {
        FeTempHumDate feTempHumDate = baseMapper.selectById(dateId);
        // 获取年度月度时间
        String[] month = feTempHumDate.getMonthDate().split("-");

        IPage<FeTempHumRecordDto> list = feTempHumRecordService.getFeTempHumRecordPage(new Page<>(1, -1), dateId);
        List<FeTempHumRecordDto> feTempHumRecordDtos = list.getRecords();
        // 按照天数分组
        Map<Integer, List<FeTempHumRecordDto>> groupMap = feTempHumRecordDtos.stream().peek(feTempHumRecordDto -> {
                    feTempHumRecordDto.setMonthDay(feTempHumRecordDto.getRecordDate().getDayOfMonth());
                })
                .collect(Collectors.groupingBy(FeTempHumRecordDto::getMonthDay));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        // 遍历31获取31个对象
        List<FeTempHumRecordDto> recordDtoList = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            FeTempHumRecordDto recordDto = new FeTempHumRecordDto();
            recordDto.setMonth(i);
            recordDtoList.add(recordDto);
            // 获取当前月的
            List<FeTempHumRecordDto> recordDtos = groupMap.get(i);
            if (CollectionUtils.isNotEmpty(recordDtos)) {
                // 复制对象
                BeanUtils.copyProperties(recordDtos.get(0), recordDto, "month");
                recordDto.setMorningTestTimeStr(recordDto.getMorningTestTime() == null ? null : recordDto.getMorningTestTime().format(formatter));
                recordDto.setAfternoonTimeStr(recordDto.getAfternoonTime() == null ? null : recordDto.getAfternoonTime().format(formatter));
                recordDto.setMorningRecorderUrlRender(StringUtils.isNotBlank(recordDto.getMorningRecorderUrl())
                        ? Pictures.ofLocal(imgUrl + "/" + recordDto.getMorningRecorderUrl()).create() : null);
                recordDto.setAfternoonRecorderUrlRender(StringUtils.isNotBlank(recordDto.getAfternoonRecorderUrl())
                        ? Pictures.ofLocal(imgUrl + "/" + recordDto.getAfternoonRecorderUrl()).create() : null);
            }

        }

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/temp_hum_date.docx");
        Configure configure = Configure.builder()
                .bind("recordList", new HackLoopTableRenderPolicy())
                .build();

        HashMap<String, Object> map = new HashMap<>();
        map.put("testAreaName", feTempHumDate.getTestAreaName());
        map.put("experimentalYear", month[0]);
        map.put("experimentalMonth", month[1]);
        map.put("subjoin", feTempHumDate.getSubjoin());

        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("feTempHum", map);
                    put("recordList", recordDtoList);
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "温湿度记录导出", "UTF-8");
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

    /**
     * 温湿度确认
     * @param feTempHumDate
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void affirmFeTempHumDate(FeTempHumDate feTempHumDate) {
        // 温湿度确认
        this.update(Wrappers.<FeTempHumDate>lambdaUpdate()
                .eq(FeTempHumDate::getDateId, feTempHumDate.getDateId())
                .set(FeTempHumDate::getRegistrarUserId, feTempHumDate.getRegistrarUserId()));

    }
}
