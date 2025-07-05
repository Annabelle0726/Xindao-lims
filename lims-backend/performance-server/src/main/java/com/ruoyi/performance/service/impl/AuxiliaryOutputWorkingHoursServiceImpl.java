package com.ruoyi.performance.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.performance.dto.AuxiliaryOutputWorkingHoursDto;
import com.ruoyi.performance.dto.AuxiliaryWorkingHoursDayDto;
import com.ruoyi.performance.mapper.AuxiliaryOutputWorkingHoursMapper;
import com.ruoyi.performance.mapper.AuxiliaryWorkingHoursDayMapper;
import com.ruoyi.performance.pojo.AuxiliaryOutputWorkingHours;
import com.ruoyi.performance.pojo.AuxiliaryWorkingHoursDay;
import com.ruoyi.performance.service.AuxiliaryOutputWorkingHoursService;
import com.ruoyi.system.mapper.UserMapper;
import org.apache.commons.math3.analysis.function.Power;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 日工时管理的产量工时 服务实现类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-05-28 03:48:48
 */
@Service
public class AuxiliaryOutputWorkingHoursServiceImpl extends ServiceImpl<AuxiliaryOutputWorkingHoursMapper, AuxiliaryOutputWorkingHours> implements AuxiliaryOutputWorkingHoursService {

    @Resource
    AuxiliaryOutputWorkingHoursMapper auxiliaryOutputWorkingHoursMapper;

    @Resource
    AuxiliaryWorkingHoursDayMapper auxiliaryWorkingHoursDayMapper;

    @Resource
    UserMapper userMapper;

    @Override
    public IPage<AuxiliaryOutputWorkingHoursDto> selectAuxiliaryOutputWorkingHours(Page page, AuxiliaryOutputWorkingHoursDto auxiliaryOutputWorkingHoursDto) {
        String dates = auxiliaryOutputWorkingHoursDto.getDateTime();
        String week = auxiliaryOutputWorkingHoursDto.getWeek();
        auxiliaryOutputWorkingHoursDto.setDateTime(null);
        auxiliaryOutputWorkingHoursDto.setWeek(null);
        List<Long> ids = new ArrayList<>();
        if (ids.size() == 0) {
            ids = null;
        }
        if (ObjectUtils.isNotEmpty(dates) && ObjectUtils.isNotEmpty(week)) {
            String[] split = dates.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\"", "").split(",");
            String[] weeks = week.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\"", "").split(",");
            IPage<AuxiliaryOutputWorkingHoursDto> auxiliaryOutputWorkingHoursDtoIPage = auxiliaryOutputWorkingHoursMapper.selectAuxiliaryOutputWorkingHours(page,
                    QueryWrappers.queryWrappers(auxiliaryOutputWorkingHoursDto)
                            .ge("date_time", split[0]).le("date_time", split[1] + " 23:59:59")
                            .ge("week", weeks[0]).le("week", weeks[1]), ids);
            for (AuxiliaryOutputWorkingHoursDto record : auxiliaryOutputWorkingHoursDtoIPage.getRecords()) {
                record.setDateTime(record.getDateTime().substring(0,10));
            }
            return auxiliaryOutputWorkingHoursDtoIPage ;
        } else if (ObjectUtils.isNotEmpty(dates) && ObjectUtils.isEmpty(week)) {
            String[] split = dates.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\"", "").split(",");
            IPage<AuxiliaryOutputWorkingHoursDto> auxiliaryOutputWorkingHoursDtoIPage = auxiliaryOutputWorkingHoursMapper.selectAuxiliaryOutputWorkingHours(page,
                    QueryWrappers.queryWrappers(auxiliaryOutputWorkingHoursDto)
                            .ge("date_time", split[0]).le("date_time", split[1] + " 23:59:59"), ids);
            for (AuxiliaryOutputWorkingHoursDto record : auxiliaryOutputWorkingHoursDtoIPage.getRecords()) {
                record.setDateTime(record.getDateTime().substring(0,10));
            }
            return auxiliaryOutputWorkingHoursDtoIPage ;
        } else if (ObjectUtils.isEmpty(dates) && ObjectUtils.isNotEmpty(week)) {
            String[] weeks = week.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\"", "").split(",");
            IPage<AuxiliaryOutputWorkingHoursDto> auxiliaryOutputWorkingHoursDtoIPage = auxiliaryOutputWorkingHoursMapper.selectAuxiliaryOutputWorkingHours(page,
                    QueryWrappers.queryWrappers(auxiliaryOutputWorkingHoursDto)
                            .ge("week", weeks[0]).le("week", weeks[1]), ids);
            for (AuxiliaryOutputWorkingHoursDto record : auxiliaryOutputWorkingHoursDtoIPage.getRecords()) {
                record.setDateTime(record.getDateTime().substring(0,10));
            }
            return auxiliaryOutputWorkingHoursDtoIPage ;
        } else {
            IPage<AuxiliaryOutputWorkingHoursDto> auxiliaryOutputWorkingHoursDtoIPage = auxiliaryOutputWorkingHoursMapper.selectAuxiliaryOutputWorkingHours(page, QueryWrappers.queryWrappers(auxiliaryOutputWorkingHoursDto), ids);
            for (AuxiliaryOutputWorkingHoursDto record : auxiliaryOutputWorkingHoursDtoIPage.getRecords()) {
                record.setDateTime(record.getDateTime().substring(0,10));
            }
            return auxiliaryOutputWorkingHoursDtoIPage ;
        }
    }

    //统计产量工时汇总和辅助工时汇总
    @Override
    public Map<String, Object> collectWorkingHours(AuxiliaryOutputWorkingHoursDto auxiliaryOutputWorkingHoursDto) {
        AuxiliaryOutputWorkingHours outputWorkingHours = new AuxiliaryOutputWorkingHours();
        AuxiliaryWorkingHoursDay workingHoursDay = new AuxiliaryWorkingHoursDay();
        List<Integer> ids = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(auxiliaryOutputWorkingHoursDto.getWeekDay())){
            outputWorkingHours.setWeekDay(auxiliaryOutputWorkingHoursDto.getWeekDay());
            workingHoursDay.setWeekDay(auxiliaryOutputWorkingHoursDto.getWeekDay());
        }
        if (ObjectUtils.isNotEmpty(auxiliaryOutputWorkingHoursDto.getName())){
            List<User> user = userMapper.selectList(Wrappers.<User>lambdaQuery().like(User::getName, auxiliaryOutputWorkingHoursDto.getName()));
            ids.addAll(user.stream().map(User::getId).collect(Collectors.toList()));
        }
        String dates = auxiliaryOutputWorkingHoursDto.getDateTime();
        auxiliaryOutputWorkingHoursDto.setDateTime(null);
        Map<String, Object> map = new HashMap<>();
        BigDecimal sumOutputWorkTime = BigDecimal.ZERO;
        BigDecimal sumApprovedWorkingHour = BigDecimal.ZERO;

        //判断是组长还是组员还是管理员
        List<AuxiliaryOutputWorkingHours> auxiliaryOutputWorkingHours = new ArrayList<>();
        List<AuxiliaryWorkingHoursDay> auxiliaryWorkingHoursDays = new ArrayList<>();
        if (ids.size() == 0) {
            ids=null;
        }
        if (ObjectUtils.isNotEmpty(dates)) {
            String[] split = dates.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\"", "").split(",");
            //统计当前用户的产品工时
            auxiliaryOutputWorkingHours = auxiliaryOutputWorkingHoursMapper.selectLists(QueryWrappers.queryWrappers(outputWorkingHours)
                    .ge("date_time", split[0])
                    .le("date_time", split[1] + " 23:59:59"),ids);
            //统计当前用户的辅助工时
            auxiliaryWorkingHoursDays = auxiliaryWorkingHoursDayMapper.selectLists(QueryWrappers.queryWrappers(workingHoursDay)
                    .eq("state","已批准")
                    .ge("date_time", split[0])
                    .le("date_time", split[1] + " 23:59:59"),ids);
        }else if (ObjectUtils.isNotEmpty(auxiliaryOutputWorkingHoursDto.getWeekDay())){
            //统计当前用户的产品工时
            auxiliaryOutputWorkingHours = auxiliaryOutputWorkingHoursMapper.selectLists(QueryWrappers.queryWrappers(outputWorkingHours),ids);
            //统计当前用户的辅助工时
            auxiliaryWorkingHoursDays = auxiliaryWorkingHoursDayMapper.selectLists(QueryWrappers.queryWrappers(workingHoursDay).eq("state","已批准"),ids);
        }
        else {
            auxiliaryOutputWorkingHours = auxiliaryOutputWorkingHoursMapper.selectListByIds(ids);
            auxiliaryWorkingHoursDays = auxiliaryWorkingHoursDayMapper.selectListByIds(ids);
        }
        if (ObjectUtils.isNotEmpty(auxiliaryOutputWorkingHours)) {
            Map<String, BigDecimal> sumMap = new HashMap<>();
            for (AuxiliaryOutputWorkingHours auxiliaryOutputWorkingHour : auxiliaryOutputWorkingHours) {
                if (!sumMap.containsKey(auxiliaryOutputWorkingHour.getManHourGroup()+auxiliaryOutputWorkingHour.getOrderNo()+auxiliaryOutputWorkingHour.getSample())) {
                    sumMap.put(auxiliaryOutputWorkingHour.getManHourGroup()+auxiliaryOutputWorkingHour.getOrderNo()+auxiliaryOutputWorkingHour.getSample(), auxiliaryOutputWorkingHour.getOutputWorkTime());
                }
            }
            sumOutputWorkTime = sumMap.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        map.put("产量工时汇总", sumOutputWorkTime);
        if (ObjectUtils.isNotEmpty(auxiliaryWorkingHoursDays)) {
            for (AuxiliaryWorkingHoursDay auxiliaryWorkingHoursDay : auxiliaryWorkingHoursDays) {
                sumApprovedWorkingHour = sumApprovedWorkingHour.add(auxiliaryWorkingHoursDay.getReviewerNonproductiveTime());//复核工时
            }
        }
        map.put("辅助工时汇总", sumApprovedWorkingHour);
        return map;
    }

    //导出
    @Override
    public void exportWorkingHours(HttpServletResponse response) throws IOException {
        List<AuxiliaryOutputWorkingHoursDto> auxiliaryOutputWorkingHoursDtos = new ArrayList<>();
        List<AuxiliaryWorkingHoursDayDto> auxiliaryWorkingHoursDayDtos = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        //查询辅助工时
        auxiliaryWorkingHoursDayDtos = auxiliaryWorkingHoursDayMapper.selectDataByUser(ids);
        //查询统计工时
        auxiliaryOutputWorkingHoursDtos = auxiliaryOutputWorkingHoursMapper.selectDataByUser(ids);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("日工时管理导出", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        try {
            // 新建ExcelWriter
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build();

            WriteSheet mainSheet = EasyExcel.writerSheet(0, "辅助工时导出").head(AuxiliaryWorkingHoursDayDto.class).build();
            excelWriter.write(auxiliaryWorkingHoursDayDtos, mainSheet);

            WriteSheet mainSheet1 = EasyExcel.writerSheet(1, "产量工时导出").head(AuxiliaryOutputWorkingHoursDto.class).build();
            excelWriter.write(auxiliaryOutputWorkingHoursDtos, mainSheet1);
            // 关闭流
            excelWriter.finish();
        } catch (IOException e) {
            throw new RuntimeException("导出失败");
        }
    }

    /**
     * 导出产量工时
     * @param response
     */
    @Override
    public void exportOutputHours(AuxiliaryOutputWorkingHoursDto auxiliaryOutputWorkingHoursDto, HttpServletResponse response) {
        //查询导出的费用统计数据
        String dates = auxiliaryOutputWorkingHoursDto.getDateTime();
        String week = auxiliaryOutputWorkingHoursDto.getWeek();
        auxiliaryOutputWorkingHoursDto.setDateTime(null);
        auxiliaryOutputWorkingHoursDto.setWeek(null);

        List<Integer> ids = new ArrayList<>();
        String name = auxiliaryOutputWorkingHoursDto.getName();
        if (ObjectUtils.isNotEmpty(name)) {
            ids.addAll(userMapper.selectList(Wrappers.<User>lambdaQuery().like(User::getName, name)).stream().map(User::getId).collect(Collectors.toList()));
        }

        List<AuxiliaryOutputWorkingHoursDto> auxiliaryOutputWorkingHoursDtoIPage = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(dates) && ObjectUtils.isNotEmpty(week)) {
            String[] split = dates.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\"", "").split(",");
            String[] weeks = week.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\"", "").split(",");
            auxiliaryOutputWorkingHoursDtoIPage = auxiliaryOutputWorkingHoursMapper.selectAuxiliaryOutputWorkingHoursList(QueryWrappers.queryWrappers(auxiliaryOutputWorkingHoursDto)
                            .ge("date_time", split[0]).le("date_time", split[1] + " 23:59:59")
                            .ge("week", weeks[0]).le("week", weeks[1]), ids);

        } else if (ObjectUtils.isNotEmpty(dates) && ObjectUtils.isEmpty(week)) {
            String[] split = dates.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\"", "").split(",");
            auxiliaryOutputWorkingHoursDtoIPage = auxiliaryOutputWorkingHoursMapper.selectAuxiliaryOutputWorkingHoursList(QueryWrappers.queryWrappers(auxiliaryOutputWorkingHoursDto)
                            .ge("date_time", split[0]).le("date_time", split[1] + " 23:59:59"), ids);

        } else if (ObjectUtils.isEmpty(dates) && ObjectUtils.isNotEmpty(week)) {
            String[] weeks = week.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\"", "").split(",");
            auxiliaryOutputWorkingHoursDtoIPage = auxiliaryOutputWorkingHoursMapper.selectAuxiliaryOutputWorkingHoursList(QueryWrappers.queryWrappers(auxiliaryOutputWorkingHoursDto)
                            .ge("week", weeks[0]).le("week", weeks[1]), ids);

        } else {
            auxiliaryOutputWorkingHoursDtoIPage = auxiliaryOutputWorkingHoursMapper.selectAuxiliaryOutputWorkingHoursList(QueryWrappers.queryWrappers(auxiliaryOutputWorkingHoursDto), ids);
        }

        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("产量工时信息导出", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            //新建ExcelWriter
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build();
            //获取sheet0对象
            WriteSheet mainSheet = EasyExcel.writerSheet(0, "产量工时信息导出").head(AuxiliaryOutputWorkingHoursDto.class).build();

            //向sheet0写入数据 传入空list这样只导出表头
            excelWriter.write(auxiliaryOutputWorkingHoursDtoIPage, mainSheet);
            //关闭流
            excelWriter.finish();
        } catch (IOException e) {
            throw new RuntimeException("导出失败");
        }
    }
}
