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
import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.performance.dto.AuxiliaryWorkingHoursDayDto;
import com.ruoyi.performance.dto.HoursDay;
import com.ruoyi.performance.mapper.AuxiliaryWorkingHoursDayMapper;
import com.ruoyi.performance.mapper.AuxiliaryWorkingHoursMapper;
import com.ruoyi.performance.mapper.PerformanceShiftMapper;
import com.ruoyi.performance.pojo.AuxiliaryWorkingHours;
import com.ruoyi.performance.pojo.AuxiliaryWorkingHoursDay;
import com.ruoyi.performance.pojo.PerformanceShift;
import com.ruoyi.performance.service.AuxiliaryWorkingHoursDayService;
import com.ruoyi.system.mapper.UserMapper;
import org.apache.commons.math3.analysis.function.Power;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 日工时管理的辅助工时 服务实现类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-05-28 02:22:19
 */
@Service
public class AuxiliaryWorkingHoursDayServiceImpl extends ServiceImpl<AuxiliaryWorkingHoursDayMapper, AuxiliaryWorkingHoursDay> implements AuxiliaryWorkingHoursDayService {

    @Resource
    AuxiliaryWorkingHoursDayMapper auxiliaryWorkingHoursDayMapper;

    @Resource
    AuxiliaryWorkingHoursMapper auxiliaryWorkingHoursMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    PerformanceShiftMapper performanceShiftMapper;

    @Override
    public IPage<AuxiliaryWorkingHoursDayDto> selectAuxiliaryWorkingHoursDay(Page page, AuxiliaryWorkingHoursDayDto auxiliaryWorkingHoursDayDto) {
        String dates = auxiliaryWorkingHoursDayDto.getDateTime();
        String week = auxiliaryWorkingHoursDayDto.getWeek();
        auxiliaryWorkingHoursDayDto.setDateTime(null);
        auxiliaryWorkingHoursDayDto.setWeek(null);
        Map<String, Object> map = new HashMap<>();
        List<Integer> ids = new ArrayList<>();
        if (ids.size() == 0) {
            ids = null;
        }
        if (ObjectUtils.isNotEmpty(dates) && ObjectUtils.isNotEmpty(week)) {
            String[] split = dates.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\"", "").split(",");
            String[] weeks = week.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\"", "").split(",");
            IPage<AuxiliaryWorkingHoursDayDto> auxiliaryWorkingHoursDayDtoIPage = auxiliaryWorkingHoursDayMapper.selectAuxiliaryWorkingHoursDay(page,
                    QueryWrappers.queryWrappers(auxiliaryWorkingHoursDayDto)
                            .ge("date_time", split[0]).le("date_time", split[1] + " 23:59:59")
                            .ge("week", weeks[0]).le("week", weeks[1]), ids);
            for (AuxiliaryWorkingHoursDayDto record : auxiliaryWorkingHoursDayDtoIPage.getRecords()) {
                record.setDateTime(record.getDateTime().substring(0, 10));
            }
            return auxiliaryWorkingHoursDayDtoIPage;
        } else if (ObjectUtils.isNotEmpty(dates) && ObjectUtils.isEmpty(week)) {
            String[] split = dates.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\"", "").split(",");
            IPage<AuxiliaryWorkingHoursDayDto> auxiliaryWorkingHoursDayDtoIPage = auxiliaryWorkingHoursDayMapper.selectAuxiliaryWorkingHoursDay(page,
                    QueryWrappers.queryWrappers(auxiliaryWorkingHoursDayDto)
                            .ge("date_time", split[0]).le("date_time", split[1] + " 23:59:59"), ids);
            for (AuxiliaryWorkingHoursDayDto record : auxiliaryWorkingHoursDayDtoIPage.getRecords()) {
                record.setDateTime(record.getDateTime().substring(0, 10));
            }
            return auxiliaryWorkingHoursDayDtoIPage;
        } else if (ObjectUtils.isEmpty(dates) && ObjectUtils.isNotEmpty(week)) {
            String[] weeks = week.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\"", "").split(",");
            IPage<AuxiliaryWorkingHoursDayDto> auxiliaryWorkingHoursDayDtoIPage = auxiliaryWorkingHoursDayMapper.selectAuxiliaryWorkingHoursDay(page,
                    QueryWrappers.queryWrappers(auxiliaryWorkingHoursDayDto)
                            .ge("week", weeks[0]).le("week", weeks[1]), ids);
            for (AuxiliaryWorkingHoursDayDto record : auxiliaryWorkingHoursDayDtoIPage.getRecords()) {
                record.setDateTime(record.getDateTime().substring(0, 10));
            }
            return auxiliaryWorkingHoursDayDtoIPage;
        } else {
            IPage<AuxiliaryWorkingHoursDayDto> auxiliaryWorkingHoursDayDtoIPage = auxiliaryWorkingHoursDayMapper.selectAuxiliaryWorkingHoursDay(page, QueryWrappers.queryWrappers(auxiliaryWorkingHoursDayDto), ids);
            for (AuxiliaryWorkingHoursDayDto record : auxiliaryWorkingHoursDayDtoIPage.getRecords()) {
                record.setDateTime(ObjectUtils.isEmpty(record.getDateTime())? null : record.getDateTime().substring(0, 10));
            }
            return auxiliaryWorkingHoursDayDtoIPage;
        }

    }

    //根据编号查询辅助工时配置信息
    @Override
    public AuxiliaryWorkingHoursDay selectAuxiliaryWorkingHoursByNumber(String number) {
        //根据填写的编号查询辅助工时配置
        AuxiliaryWorkingHours auxiliaryWorkingHours = auxiliaryWorkingHoursMapper.selectOne(Wrappers.<AuxiliaryWorkingHours>lambdaQuery().eq(AuxiliaryWorkingHours::getNumber, number));
        if (ObjectUtils.isEmpty(auxiliaryWorkingHours)) {
            throw new BaseException("该编号没有对应的辅助工时配置");
        }
        AuxiliaryWorkingHoursDay auxiliaryWorkingHoursDay = new AuxiliaryWorkingHoursDay();
        BeanUtils.copyProperties(auxiliaryWorkingHours, auxiliaryWorkingHoursDay);
        return auxiliaryWorkingHoursDay;
    }

    //录入数据(新增)
    @Override
    public int insertAuxiliaryWorkingHoursDay(AuxiliaryWorkingHoursDay auxiliaryWorkingHoursDay) {
        Integer userId = SecurityUtils.getUserId().intValue();
        auxiliaryWorkingHoursDay.setNameUser(userId);//姓名id
        auxiliaryWorkingHoursDay.setState("已提交");
        return auxiliaryWorkingHoursDayMapper.insert(auxiliaryWorkingHoursDay);
    }

    //审核/批准
    @Override
    public boolean checkOrApprove(HoursDay hoursDay) {
        List<AuxiliaryWorkingHoursDay> auxiliaryWorkingHoursDays = hoursDay.getAuxiliaryWorkingHoursDays();
        for (AuxiliaryWorkingHoursDay auxiliaryWorkingHoursDay : auxiliaryWorkingHoursDays) {
            Integer userId = SecurityUtils.getUserId().intValue();
            auxiliaryWorkingHoursDay.setReviewer(userMapper.selectById(userId).getName());
            if (auxiliaryWorkingHoursDay.getReviewerNumber() == null) {
                auxiliaryWorkingHoursDay.setReviewerNumber(auxiliaryWorkingHoursDay.getAmount());//复核数量
                auxiliaryWorkingHoursDay.setReviewerNonproductiveTime(auxiliaryWorkingHoursDay.getNonproductiveTime());//复核工时
            }
        }
        return updateBatchById(auxiliaryWorkingHoursDays);
    }

    //编辑
    @Override
    public int updateAuxiliaryWorkingHoursDay(AuxiliaryWorkingHoursDay auxiliaryWorkingHoursDay) {
        Integer userId = SecurityUtils.getUserId().intValue();
        auxiliaryWorkingHoursDay.setNameUser(userId);//姓名id
        return auxiliaryWorkingHoursDayMapper.updateById(auxiliaryWorkingHoursDay);
    }

    //删除
    @Override
    public int deleteAuxiliaryWorkingHoursDay(Integer id) {
        return auxiliaryWorkingHoursDayMapper.deleteById(id);
    }

    //根据编号当前用户信息查询所在班次
    @Override
    public String selectshiftByUser(LocalDateTime dateTime) {
        Integer userId = SecurityUtils.getUserId().intValue();
        PerformanceShift performanceShift = performanceShiftMapper.selectOne(Wrappers.<PerformanceShift>lambdaQuery().eq(PerformanceShift::getUserId, userId).eq(PerformanceShift::getWorkTime, dateTime));
        if (ObjectUtils.isEmpty(performanceShift)) {
            return null;
        }
        return performanceShift.getShift();
    }

    /**
     * 导出辅助工时
     * @param
     * @param response
     */
    @Override
    public void exportWorkingHours(AuxiliaryWorkingHoursDayDto auxiliaryWorkingHoursDayDto, HttpServletResponse response) {
        String dates = auxiliaryWorkingHoursDayDto.getDateTime();
        String week = auxiliaryWorkingHoursDayDto.getWeek();
        auxiliaryWorkingHoursDayDto.setDateTime(null);
        auxiliaryWorkingHoursDayDto.setWeek(null);
        List<Integer> ids = new ArrayList<>();
        String name = auxiliaryWorkingHoursDayDto.getName();
        if (ObjectUtils.isNotEmpty(name)) {
            ids.addAll(userMapper.selectList(Wrappers.<User>lambdaQuery().like(User::getName, name)).stream().map(User::getId).collect(Collectors.toList()));
        }
        List<AuxiliaryWorkingHoursDayDto> auxiliaryWorkingHoursDayDtoIPage = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(dates) && ObjectUtils.isNotEmpty(week)) {
            String[] split = dates.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\"", "").split(",");
            String[] weeks = week.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\"", "").split(",");
            auxiliaryWorkingHoursDayDtoIPage = auxiliaryWorkingHoursDayMapper.selectAuxiliaryWorkingHoursDayList(QueryWrappers.queryWrappers(auxiliaryWorkingHoursDayDto)
                            .ge("date_time", split[0]).le("date_time", split[1] + " 23:59:59")
                            .ge("week", weeks[0]).le("week", weeks[1]), ids);
        } else if (ObjectUtils.isNotEmpty(dates) && ObjectUtils.isEmpty(week)) {
            String[] split = dates.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\"", "").split(",");
            auxiliaryWorkingHoursDayDtoIPage = auxiliaryWorkingHoursDayMapper.selectAuxiliaryWorkingHoursDayList(QueryWrappers.queryWrappers(auxiliaryWorkingHoursDayDto)
                            .ge("date_time", split[0]).le("date_time", split[1] + " 23:59:59"), ids);
        } else if (ObjectUtils.isEmpty(dates) && ObjectUtils.isNotEmpty(week)) {
            String[] weeks = week.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\"", "").split(",");
            auxiliaryWorkingHoursDayDtoIPage = auxiliaryWorkingHoursDayMapper.selectAuxiliaryWorkingHoursDayList(QueryWrappers.queryWrappers(auxiliaryWorkingHoursDayDto)
                            .ge("week", weeks[0]).le("week", weeks[1]), ids);
        } else {
            auxiliaryWorkingHoursDayDtoIPage = auxiliaryWorkingHoursDayMapper.selectAuxiliaryWorkingHoursDayList( QueryWrappers.queryWrappers(auxiliaryWorkingHoursDayDto), ids);
        }

        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("辅助工时信息导出", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            //新建ExcelWriter
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build();
            //获取sheet0对象
            WriteSheet mainSheet = EasyExcel.writerSheet(0, "辅助工时信息导出").head(AuxiliaryWorkingHoursDayDto.class).build();

            //向sheet0写入数据 传入空list这样只导出表头
            excelWriter.write(auxiliaryWorkingHoursDayDtoIPage, mainSheet);
            //关闭流
            excelWriter.finish();
        } catch (IOException e) {
            throw new RuntimeException("导出失败");
        }
    }


}
