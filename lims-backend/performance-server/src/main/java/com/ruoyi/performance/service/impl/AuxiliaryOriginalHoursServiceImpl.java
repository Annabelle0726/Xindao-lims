package com.ruoyi.performance.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.performance.dto.AuxiliaryAllDto;
import com.ruoyi.performance.dto.AuxiliaryCorrectionHoursDto;
import com.ruoyi.performance.dto.AuxiliaryOriginalHoursDto;
import com.ruoyi.performance.dto.AuxiliaryOriginalHoursLookDto;
import com.ruoyi.performance.mapper.AuxiliaryCorrectionHoursMapper;
import com.ruoyi.performance.mapper.AuxiliaryOriginalHoursMapper;
import com.ruoyi.performance.mapper.AuxiliaryOutputWorkingHoursMapper;
import com.ruoyi.performance.mapper.AuxiliaryWorkingHoursDayMapper;
import com.ruoyi.performance.pojo.AuxiliaryCorrectionHours;
import com.ruoyi.performance.service.AuxiliaryOriginalHoursService;
import com.ruoyi.system.mapper.UserMapper;
import org.apache.commons.math3.analysis.function.Power;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuxiliaryOriginalHoursServiceImpl implements AuxiliaryOriginalHoursService {

    @Resource
    UserMapper userMapper;

    @Resource
    AuxiliaryOriginalHoursMapper auxiliaryOriginalHoursMapper;

    @Resource
    AuxiliaryOutputWorkingHoursMapper auxiliaryOutputWorkingHoursMapper;

    @Resource
    AuxiliaryWorkingHoursDayMapper auxiliaryWorkingHoursDayMapper;

    @Resource
    AuxiliaryCorrectionHoursMapper auxiliaryCorrectionHoursMapper;

    @Override
    public IPage<AuxiliaryOriginalHoursDto> selectAuxiliaryOriginalHours(Page page, AuxiliaryOriginalHoursLookDto auxiliaryOriginalHoursLookDto) {
        List<Integer> ids = new ArrayList<>();
        String departLims = auxiliaryOriginalHoursLookDto.getDepartLims();
        auxiliaryOriginalHoursLookDto.setDepartLims(null);
        String name = auxiliaryOriginalHoursLookDto.getName();
        auxiliaryOriginalHoursLookDto.setName(null);
        if (ObjectUtils.isNotEmpty(departLims)) {
            //先模糊查询出来id
            List<Integer> ides = auxiliaryCorrectionHoursMapper.selDepartLimsByName(departLims);
            for (Integer ide : ides) {
                List<User> users = userMapper.selectList(Wrappers.<User>lambdaQuery()
                        .like(User::getDepartLimsId, ide));
                if (ObjectUtils.isNotEmpty(users)) {
                    ides.clear();
                    ids.addAll(users.stream().map(User::getId).distinct().collect(Collectors.toList()));
                }
            }
        }
        if (ObjectUtils.isNotEmpty(name)) {
            ids.clear();
            ids.add(userMapper.selectOne(Wrappers.<User>lambdaQuery().like(User::getName, name)).getId());
        }
        if (ids.size() == 0) {
            ids = null;
        }
        IPage<AuxiliaryOriginalHoursDto> originalHoursDtoIPage = new Page<>();
        originalHoursDtoIPage.setSize(page.getSize());
        originalHoursDtoIPage.setCurrent(page.getCurrent());
        List<AuxiliaryOriginalHoursDto> auxiliaryOriginalHoursDtos = new ArrayList<>();
        Map<String, AuxiliaryOriginalHoursDto> data1 = new HashMap<>();
        Map<String, AuxiliaryOriginalHoursDto> data2 = new HashMap<>();
        Map<String, AuxiliaryOriginalHoursDto> data3 = new HashMap<>();
        //产量工时
        List<Map<String, Object>> maps = auxiliaryOutputWorkingHoursMapper.totalHours(auxiliaryOriginalHoursLookDto.getMonth(), ids, "产量工时");
        if (ObjectUtils.isNotEmpty(maps)) {
            data1 = getData(maps, "产量工时");
            auxiliaryOriginalHoursDtos.addAll(data1.values());
        }
        //辅助工时
        List<Map<String, Object>> maps1 = auxiliaryWorkingHoursDayMapper.totalHours(auxiliaryOriginalHoursLookDto.getMonth(), ids);
        if (ObjectUtils.isNotEmpty(maps1)) {
            data2 = getData(maps1, "辅助工时");
            auxiliaryOriginalHoursDtos.addAll(data2.values());
        }
        //加班工时
        List<Map<String, Object>> maps2 = auxiliaryOutputWorkingHoursMapper.totalHours(auxiliaryOriginalHoursLookDto.getMonth(), ids, "加班工时");
        if (ObjectUtils.isNotEmpty(maps2)) {
            data3 = getData(maps2, "加班工时");
            auxiliaryOriginalHoursDtos.addAll(data3.values());
        }
        //总工时=产量工时+辅助工时+加班工时
        Map<String, AuxiliaryOriginalHoursDto> data4 = new HashMap<String, AuxiliaryOriginalHoursDto>();
        if (data1.size() > 0) {
            Map<String, AuxiliaryOriginalHoursDto> data5 = data1;
            if (data2.size() > 0) {
                merge(data5, data4, data2);
            }
            if (data3.size() > 0) {
//                merge(data5,data4,data3);
            }
            if (data2.size() == 0 && data3.size() == 0) {
                for (Map.Entry<String, AuxiliaryOriginalHoursDto> entry : data5.entrySet()) {
                    AuxiliaryOriginalHoursDto dto = entry.getValue();
                    AuxiliaryOriginalHoursDto originalHoursDto = new AuxiliaryOriginalHoursDto();
                    BeanUtils.copyProperties(dto, originalHoursDto);
                    originalHoursDto.setType("总工时");
                    data4.put(entry.getKey(), originalHoursDto);
                }
            }
            auxiliaryOriginalHoursDtos.addAll(data4.values());
        }

        auxiliaryOriginalHoursDtos = auxiliaryOriginalHoursDtos.stream().sorted(Comparator.comparing(AuxiliaryOriginalHoursDto::getName)).collect(Collectors.toList());
        // 检查每个人的工时类型，补全缺少的工时
        Map<String, Set<String>> workHoursMap = new HashMap<>();
        String[] requiredHours = {"产量工时", "辅助工时", "加班工时", "总工时"};
        for (AuxiliaryOriginalHoursDto dto : auxiliaryOriginalHoursDtos) {
            workHoursMap.computeIfAbsent(dto.getName(), k -> new HashSet<>()).add(dto.getType());
        }
        List<AuxiliaryOriginalHoursDto> result = auxiliaryOriginalHoursDtos;
        for (String name1 : workHoursMap.keySet()) {
            Set<String> workTypes = workHoursMap.get(name1);
            for (String requiredHour : requiredHours) {
                if (!workTypes.contains(requiredHour)) {
                    AuxiliaryOriginalHoursDto auxiliaryOriginalHoursDto = new AuxiliaryOriginalHoursDto();
                    auxiliaryOriginalHoursDto.setName(name1);
                    auxiliaryOriginalHoursDto.setAuxiliaryCorrectionHours(null);
                    auxiliaryOriginalHoursDto.setType(requiredHour);
                    auxiliaryOriginalHoursDto.setMonth(auxiliaryOriginalHoursLookDto.getMonth());
                    result.add(auxiliaryOriginalHoursDto);
                }
            }
        }
        List<String> order = Arrays.asList("产量工时", "辅助工时", "加班工时", "总工时");
        Comparator<AuxiliaryOriginalHoursDto> comparator = Comparator.comparingInt(dto -> {
            String type = dto.getType();
            return order.indexOf(type) == -1 ? order.size() : order.indexOf(type);
        });
        result = result.stream().sorted(Comparator.comparing(AuxiliaryOriginalHoursDto::getName).thenComparing(comparator)).collect(Collectors.toList());
        originalHoursDtoIPage.setRecords(result);
        originalHoursDtoIPage.setTotal(result.size());
        return originalHoursDtoIPage;
    }


    //导出原始工时
    @Override
    public void exportWorkingHours(String month, String name, String departLims, HttpServletResponse response) throws IOException {
        List<AuxiliaryOriginalHoursDto> auxiliaryOriginalHoursDtos = new ArrayList<AuxiliaryOriginalHoursDto>();
        //查询原始工时(使用分页查询)
        AuxiliaryOriginalHoursLookDto auxiliaryOriginalHoursLookDto = new AuxiliaryOriginalHoursLookDto();
        if (ObjectUtils.isNotEmpty(month)) {
            auxiliaryOriginalHoursLookDto.setMonth(month);
        }
        if (ObjectUtils.isNotEmpty(name)) {
            auxiliaryOriginalHoursLookDto.setName(name);
        }
        if (ObjectUtils.isNotEmpty(departLims)) {
            auxiliaryOriginalHoursLookDto.setDepartLims(departLims);
        }
        IPage<AuxiliaryOriginalHoursDto> body = selectAuxiliaryOriginalHours(new Page(-1, -1), auxiliaryOriginalHoursLookDto);
        auxiliaryOriginalHoursDtos = body.getRecords();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("工时统计导出", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        try {
            // 新建ExcelWriter
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build();
            WriteSheet mainSheet = EasyExcel.writerSheet(0, "原始工时导出").head(AuxiliaryOriginalHoursDto.class).build();
            excelWriter.write(auxiliaryOriginalHoursDtos, mainSheet);
            // 关闭流
            excelWriter.finish();
        } catch (IOException e) {
            throw new RuntimeException("导出失败");
        }

    }

    /**
     * 查询月份全部工时
     *
     * @return
     */
    @Override
    public List<AuxiliaryAllDto> selectAuxiliaryAllByMonth(AuxiliaryOriginalHoursLookDto dto) {
        if (StringUtils.isBlank(dto.getMonth())) {
            throw new BaseException("缺少月份");
        }
        List<Integer> userIds = new ArrayList<>();

        String name = dto.getName();
        if (ObjectUtils.isNotEmpty(name)) {
            userIds.addAll(userMapper.selectList(Wrappers.<User>lambdaQuery().like(User::getName, name)).stream().map(User::getId).collect(Collectors.toList()));
        }
        // 解析输入的时间字符串
        LocalDate date = LocalDate.parse(dto.getMonth() + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // 获取前一个月的26号
        LocalDate previousMonth26th = date.minusMonths(1).withDayOfMonth(26);

        // 获取当前月的25号
        LocalDate currentMonth25th = date.withDayOfMonth(25);

        // 格式化日期为 yyyy-MM-dd HH:mm:ss
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String beginDate = previousMonth26th.atStartOfDay().format(outputFormatter);
        String endDate = currentMonth25th.atStartOfDay().format(outputFormatter);
        dto.setBeginDate(beginDate + " 00:00:00");
        dto.setEndDate(endDate + " 23:59:59");
        dto.setAssistBeginDate(beginDate);
        dto.setAssistEndDate(endDate);


        // 查询产量工时
        List<AuxiliaryAllDto> auxiliaryAllDtos = auxiliaryOutputWorkingHoursMapper.selectAuxiliaryAllByMonth(dto, userIds);

        // 查询辅助工时
        List<AuxiliaryAllDto> auxiliarySubsidiary = auxiliaryOutputWorkingHoursMapper.selectSubsidiaryAllByMonth(dto, userIds);

        //产量工时进行人员分组, 把辅助工时添加
        Map<Integer, List<AuxiliaryAllDto>> groupIds = auxiliaryAllDtos.stream().collect(Collectors.groupingBy(AuxiliaryAllDto::getUserId));

        for (AuxiliaryAllDto auxiliaryAllDto : auxiliarySubsidiary) {
            List<AuxiliaryAllDto> allDtos = groupIds.get(auxiliaryAllDto.getUserId());
            // 判断是否为空
            if (CollectionUtils.isNotEmpty(allDtos)) {
                // 添加辅助工时
                allDtos.get(0).setSubsidiaryHour(auxiliaryAllDto.getSubsidiaryHour());

            } else {
                // 没有改人员添加一行
                auxiliaryAllDtos.add(auxiliaryAllDto);
            }
        }
        for (AuxiliaryAllDto auxiliaryAllDto : auxiliaryAllDtos) {
            BigDecimal total = (auxiliaryAllDto.getYieldHour() != null ? auxiliaryAllDto.getYieldHour() : BigDecimal.ZERO)
                    .add(auxiliaryAllDto.getSubsidiaryHour() != null ? auxiliaryAllDto.getSubsidiaryHour() : BigDecimal.ZERO);
            auxiliaryAllDto.setTotalHour(total);
        }

        return auxiliaryAllDtos;
    }

    private Map<String, AuxiliaryOriginalHoursDto> getData(List<Map<String, Object>> objectMaps, String type) {
        Map<String, AuxiliaryOriginalHoursDto> dtoMap = new HashMap<>();
        for (Map<String, Object> objectMap : objectMaps) {
            String name = objectMap.get("name").toString();
            String month = objectMap.get("month").toString().substring(0, 7);
            AuxiliaryOriginalHoursDto auxiliaryOriginalHoursDto = dtoMap.get(name);
            if (auxiliaryOriginalHoursDto == null) {
                auxiliaryOriginalHoursDto = new AuxiliaryOriginalHoursDto();
                auxiliaryOriginalHoursDto.setName(name);
                auxiliaryOriginalHoursDto.setType(type);
                auxiliaryOriginalHoursDto.setMonth(month);
                // 查询这个人这个月是否有修正的产量工时
                AuxiliaryCorrectionHours auxiliaryCorrectionHours = auxiliaryCorrectionHoursMapper.selectOne(Wrappers.<AuxiliaryCorrectionHours>lambdaQuery()
                        .eq(AuxiliaryCorrectionHours::getNameUser, userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getName, auxiliaryOriginalHoursDto.getName())).getId())
                        .eq(AuxiliaryCorrectionHours::getType, auxiliaryOriginalHoursDto.getType())
                        .eq(AuxiliaryCorrectionHours::getMonth, auxiliaryOriginalHoursDto.getMonth()));
                auxiliaryOriginalHoursDto.setAuxiliaryCorrectionHours(auxiliaryCorrectionHours);
            }
            AuxiliaryCorrectionHours auxiliaryCorrectionHours = auxiliaryOriginalHoursDto.getAuxiliaryCorrectionHours();
            switch (objectMap.get("month").toString().substring(8, 10)) {
                case "01":
                    auxiliaryOriginalHoursDto.setOneHours(new BigDecimal(objectMap.get("manHours").toString()));
                    if (ObjectUtils.isNotEmpty(auxiliaryCorrectionHours)) {
                        if (auxiliaryCorrectionHours.getOneHours().compareTo(auxiliaryOriginalHoursDto.getOneHours()) != 0) {
                            auxiliaryOriginalHoursDto.setOne(1);
                        }
                    }
                    break;
                case "02":
                    auxiliaryOriginalHoursDto.setTwoHours(new BigDecimal(objectMap.get("manHours").toString()));
                    if (ObjectUtils.isNotEmpty(auxiliaryCorrectionHours)) {
                        if (auxiliaryCorrectionHours.getTwoHours().compareTo(auxiliaryOriginalHoursDto.getTwoHours()) != 0) {
                            auxiliaryOriginalHoursDto.setTwo(1);
                        }
                    }
                    break;
                case "03":
                    auxiliaryOriginalHoursDto.setThreeHours(new BigDecimal(objectMap.get("manHours").toString()));
                    if (ObjectUtils.isNotEmpty(auxiliaryCorrectionHours)) {
                        if (auxiliaryCorrectionHours.getThreeHours().compareTo(auxiliaryOriginalHoursDto.getThreeHours()) != 0) {
                            auxiliaryOriginalHoursDto.setThree(1);
                        }
                    }
                    break;
                case "04":
                    auxiliaryOriginalHoursDto.setFourHours(new BigDecimal(objectMap.get("manHours").toString()));
                    if (ObjectUtils.isNotEmpty(auxiliaryCorrectionHours)) {
                        if (auxiliaryCorrectionHours.getFourHours().compareTo(auxiliaryOriginalHoursDto.getFourHours()) != 0) {
                            auxiliaryOriginalHoursDto.setFour(1);
                        }
                    }
                    break;
                case "05":
                    auxiliaryOriginalHoursDto.setFiveHours(new BigDecimal(objectMap.get("manHours").toString()));
                    if (ObjectUtils.isNotEmpty(auxiliaryCorrectionHours)) {
                        if (auxiliaryCorrectionHours.getFiveHours().compareTo(auxiliaryOriginalHoursDto.getFiveHours()) != 0) {
                            auxiliaryOriginalHoursDto.setFive(1);
                        }
                    }
                    break;
                case "06":
                    auxiliaryOriginalHoursDto.setSixHours(new BigDecimal(objectMap.get("manHours").toString()));
                    if (ObjectUtils.isNotEmpty(auxiliaryCorrectionHours)) {
                        if (auxiliaryCorrectionHours.getSixHours().compareTo(auxiliaryOriginalHoursDto.getSixHours()) != 0) {
                            auxiliaryOriginalHoursDto.setSix(1);
                        }
                    }
                    break;
                case "07":
                    auxiliaryOriginalHoursDto.setSevenHours(new BigDecimal(objectMap.get("manHours").toString()));
                    if (ObjectUtils.isNotEmpty(auxiliaryCorrectionHours)) {
                        if (auxiliaryCorrectionHours.getSevenHours().compareTo(auxiliaryOriginalHoursDto.getSevenHours()) != 0) {
                            auxiliaryOriginalHoursDto.setSeven(1);
                        }
                    }
                    break;
                case "08":
                    auxiliaryOriginalHoursDto.setEightHours(new BigDecimal(objectMap.get("manHours").toString()));
                    if (ObjectUtils.isNotEmpty(auxiliaryCorrectionHours)) {
                        if (auxiliaryCorrectionHours.getEightHours().compareTo(auxiliaryOriginalHoursDto.getEightHours()) != 0) {
                            auxiliaryOriginalHoursDto.setEight(1);
                        }
                    }
                    break;
                case "09":
                    auxiliaryOriginalHoursDto.setNineHours(new BigDecimal(objectMap.get("manHours").toString()));
                    if (ObjectUtils.isNotEmpty(auxiliaryCorrectionHours)) {
                        if (auxiliaryCorrectionHours.getNineHours().compareTo(auxiliaryOriginalHoursDto.getNineHours()) != 0) {
                            auxiliaryOriginalHoursDto.setNine(1);
                        }
                    }
                    break;
                case "10":
                    auxiliaryOriginalHoursDto.setTenHours(new BigDecimal(objectMap.get("manHours").toString()));
                    if (ObjectUtils.isNotEmpty(auxiliaryCorrectionHours)) {
                        if (auxiliaryCorrectionHours.getTenHours().compareTo(auxiliaryOriginalHoursDto.getTenHours()) != 0) {
                            auxiliaryOriginalHoursDto.setTen(1);
                        }
                    }
                    break;
                case "11":
                    auxiliaryOriginalHoursDto.setElevenHours(new BigDecimal(objectMap.get("manHours").toString()));
                    if (ObjectUtils.isNotEmpty(auxiliaryCorrectionHours)) {
                        if (auxiliaryCorrectionHours.getElevenHours().compareTo(auxiliaryOriginalHoursDto.getElevenHours()) != 0) {
                            auxiliaryOriginalHoursDto.setEleven(1);
                        }
                    }
                    break;
                case "12":
                    auxiliaryOriginalHoursDto.setTwelveHours(new BigDecimal(objectMap.get("manHours").toString()));
                    if (ObjectUtils.isNotEmpty(auxiliaryCorrectionHours)) {
                        if (auxiliaryCorrectionHours.getTwelveHours().compareTo(auxiliaryOriginalHoursDto.getTwelveHours()) != 0) {
                            auxiliaryOriginalHoursDto.setTwelve(1);
                        }
                    }
                    break;
                case "13":
                    auxiliaryOriginalHoursDto.setThirteenHours(new BigDecimal(objectMap.get("manHours").toString()));
                    if (ObjectUtils.isNotEmpty(auxiliaryCorrectionHours)) {
                        if (auxiliaryCorrectionHours.getThirteenHours().compareTo(auxiliaryOriginalHoursDto.getThirteenHours()) != 0) {
                            auxiliaryOriginalHoursDto.setThirteen(1);
                        }
                    }
                    break;
                case "14":
                    auxiliaryOriginalHoursDto.setFourteenHours(new BigDecimal(objectMap.get("manHours").toString()));
                    if (ObjectUtils.isNotEmpty(auxiliaryCorrectionHours)) {
                        if (auxiliaryCorrectionHours.getFourteenHours().compareTo(auxiliaryOriginalHoursDto.getFourteenHours()) != 0) {
                            auxiliaryOriginalHoursDto.setFourteen(1);
                        }
                    }
                    break;
                case "15":
                    auxiliaryOriginalHoursDto.setFifteenHours(new BigDecimal(objectMap.get("manHours").toString()));
                    if (ObjectUtils.isNotEmpty(auxiliaryCorrectionHours)) {
                        if (auxiliaryCorrectionHours.getFifteenHours().compareTo(auxiliaryOriginalHoursDto.getFifteenHours()) != 0) {
                            auxiliaryOriginalHoursDto.setFifteen(1);
                        }
                    }
                    break;
                case "16":
                    auxiliaryOriginalHoursDto.setSixteenHours(new BigDecimal(objectMap.get("manHours").toString()));
                    if (ObjectUtils.isNotEmpty(auxiliaryCorrectionHours)) {
                        if (auxiliaryCorrectionHours.getSixteenHours().compareTo(auxiliaryOriginalHoursDto.getSixteenHours()) != 0) {
                            auxiliaryOriginalHoursDto.setSixteen(1);
                        }
                    }
                    break;
                case "17":
                    auxiliaryOriginalHoursDto.setSeventeenHours(new BigDecimal(objectMap.get("manHours").toString()));
                    if (ObjectUtils.isNotEmpty(auxiliaryCorrectionHours)) {
                        if (auxiliaryCorrectionHours.getSeventeenHours().compareTo(auxiliaryOriginalHoursDto.getSeventeenHours()) != 0) {
                            auxiliaryOriginalHoursDto.setSeventeen(1);
                        }
                    }
                    break;
                case "18":
                    auxiliaryOriginalHoursDto.setEighteenHours(new BigDecimal(objectMap.get("manHours").toString()));
                    if (ObjectUtils.isNotEmpty(auxiliaryCorrectionHours)) {
                        if (auxiliaryCorrectionHours.getEighteenHours().compareTo(auxiliaryOriginalHoursDto.getEighteenHours()) != 0) {
                            auxiliaryOriginalHoursDto.setEighteen(1);
                        }
                    }
                    break;
                case "19":
                    auxiliaryOriginalHoursDto.setNineteenHours(new BigDecimal(objectMap.get("manHours").toString()));
                    if (ObjectUtils.isNotEmpty(auxiliaryCorrectionHours)) {
                        if (auxiliaryCorrectionHours.getNineteenHours().compareTo(auxiliaryOriginalHoursDto.getNineteenHours()) != 0) {
                            auxiliaryOriginalHoursDto.setNineteen(1);
                        }
                    }
                    break;
                case "20":
                    auxiliaryOriginalHoursDto.setTwentyHours(new BigDecimal(objectMap.get("manHours").toString()));
                    if (ObjectUtils.isNotEmpty(auxiliaryCorrectionHours)) {
                        if (auxiliaryCorrectionHours.getTwentyHours().compareTo(auxiliaryOriginalHoursDto.getTwentyHours()) != 0) {
                            auxiliaryOriginalHoursDto.setTwenty(1);
                        }
                    }
                    break;
                case "21":
                    auxiliaryOriginalHoursDto.setTwentyOneHours(new BigDecimal(objectMap.get("manHours").toString()));
                    if (ObjectUtils.isNotEmpty(auxiliaryCorrectionHours)) {
                        if (auxiliaryCorrectionHours.getTwentyOneHours().compareTo(auxiliaryOriginalHoursDto.getTwentyOneHours()) != 0) {
                            auxiliaryOriginalHoursDto.setTwentyOne(1);
                        }
                    }
                    break;
                case "22":
                    auxiliaryOriginalHoursDto.setTwentyTwoHours(new BigDecimal(objectMap.get("manHours").toString()));
                    if (ObjectUtils.isNotEmpty(auxiliaryCorrectionHours)) {
                        if (auxiliaryCorrectionHours.getTwentyTwoHours().compareTo(auxiliaryOriginalHoursDto.getTwentyTwoHours()) != 0) {
                            auxiliaryOriginalHoursDto.setTwentyTwo(1);
                        }
                    }
                    break;
                case "23":
                    auxiliaryOriginalHoursDto.setTwentyThreeHours(new BigDecimal(objectMap.get("manHours").toString()));
                    if (ObjectUtils.isNotEmpty(auxiliaryCorrectionHours)) {
                        if (auxiliaryCorrectionHours.getTwentyThreeHours().compareTo(auxiliaryOriginalHoursDto.getTwentyThreeHours()) != 0) {
                            auxiliaryOriginalHoursDto.setTwentyThree(1);
                        }
                    }
                    break;
                case "24":
                    auxiliaryOriginalHoursDto.setTwentyFourHours(new BigDecimal(objectMap.get("manHours").toString()));
                    if (ObjectUtils.isNotEmpty(auxiliaryCorrectionHours)) {
                        if (auxiliaryCorrectionHours.getTwentyFourHours().compareTo(auxiliaryOriginalHoursDto.getTwentyFourHours()) != 0) {
                            auxiliaryOriginalHoursDto.setTwentyFour(1);
                        }
                    }
                    break;
                case "25":
                    auxiliaryOriginalHoursDto.setTwentyFiveHours(new BigDecimal(objectMap.get("manHours").toString()));
                    if (ObjectUtils.isNotEmpty(auxiliaryCorrectionHours)) {
                        if (auxiliaryCorrectionHours.getTwentyFiveHours().compareTo(auxiliaryOriginalHoursDto.getTwentyFiveHours()) != 0) {
                            auxiliaryOriginalHoursDto.setTwentyFive(1);
                        }
                    }
                    break;
                case "26":
                    auxiliaryOriginalHoursDto.setTwentySixHours(new BigDecimal(objectMap.get("manHours").toString()));
                    if (ObjectUtils.isNotEmpty(auxiliaryCorrectionHours)) {
                        if (auxiliaryCorrectionHours.getTwentySixHours().compareTo(auxiliaryOriginalHoursDto.getTwentySixHours()) != 0) {
                            auxiliaryOriginalHoursDto.setTwentySix(1);
                        }
                    }
                    break;
                case "27":
                    auxiliaryOriginalHoursDto.setTwentySevenHours(new BigDecimal(objectMap.get("manHours").toString()));
                    if (ObjectUtils.isNotEmpty(auxiliaryCorrectionHours)) {
                        if (auxiliaryCorrectionHours.getTwentySevenHours().compareTo(auxiliaryOriginalHoursDto.getTwentySevenHours()) != 0) {
                            auxiliaryOriginalHoursDto.setTwentySeven(1);
                        }
                    }
                    break;
                case "28":
                    auxiliaryOriginalHoursDto.setTwentyEightHours(new BigDecimal(objectMap.get("manHours").toString()));
                    if (ObjectUtils.isNotEmpty(auxiliaryCorrectionHours)) {
                        if (auxiliaryCorrectionHours.getTwentyEightHours().compareTo(auxiliaryOriginalHoursDto.getTwentyEightHours()) != 0) {
                            auxiliaryOriginalHoursDto.setTwentyEight(1);
                        }
                    }
                    break;
                case "29":
                    auxiliaryOriginalHoursDto.setTwentyNineHours(new BigDecimal(objectMap.get("manHours").toString()));
                    if (ObjectUtils.isNotEmpty(auxiliaryCorrectionHours)) {
                        if (auxiliaryCorrectionHours.getTwentyNineHours().compareTo(auxiliaryOriginalHoursDto.getTwentyNineHours()) != 0) {
                            auxiliaryOriginalHoursDto.setTwentyNine(1);
                        }
                    }
                    break;
                case "30":
                    auxiliaryOriginalHoursDto.setThirtyHours(new BigDecimal(objectMap.get("manHours").toString()));
                    if (ObjectUtils.isNotEmpty(auxiliaryCorrectionHours)) {
                        if (auxiliaryCorrectionHours.getThirtyHours().compareTo(auxiliaryOriginalHoursDto.getThirtyHours()) != 0) {
                            auxiliaryOriginalHoursDto.setThirty(1);
                        }
                    }
                    break;
                case "31":
                    auxiliaryOriginalHoursDto.setThirtyOneHours(new BigDecimal(objectMap.get("manHours").toString()));
                    if (ObjectUtils.isNotEmpty(auxiliaryCorrectionHours)) {
                        if (auxiliaryCorrectionHours.getThirtyOneHours().compareTo(auxiliaryOriginalHoursDto.getThirtyOneHours()) != 0) {
                            auxiliaryOriginalHoursDto.setThirtyOne(1);
                        }
                    }
                    break;
            }
            auxiliaryOriginalHoursDto.setTotal((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getOneHours()) ? auxiliaryOriginalHoursDto.getOneHours() : BigDecimal.ZERO)
                    .add(ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getThreeHours()) ? auxiliaryOriginalHoursDto.getThreeHours() : BigDecimal.ZERO)
                    .add(ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getFourHours()) ? auxiliaryOriginalHoursDto.getFourHours() : BigDecimal.ZERO)
                    .add(ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getFiveHours()) ? auxiliaryOriginalHoursDto.getFiveHours() : BigDecimal.ZERO)
                    .add(ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getSixHours()) ? auxiliaryOriginalHoursDto.getSixHours() : BigDecimal.ZERO)
                    .add(ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getSevenHours()) ? auxiliaryOriginalHoursDto.getSevenHours() : BigDecimal.ZERO)
                    .add(ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getEightHours()) ? auxiliaryOriginalHoursDto.getEightHours() : BigDecimal.ZERO)
                    .add(ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getNineHours()) ? auxiliaryOriginalHoursDto.getNineHours() : BigDecimal.ZERO)
                    .add(ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getTenHours()) ? auxiliaryOriginalHoursDto.getTenHours() : BigDecimal.ZERO)
                    .add(ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getElevenHours()) ? auxiliaryOriginalHoursDto.getElevenHours() : BigDecimal.ZERO)
                    .add(ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getTwelveHours()) ? auxiliaryOriginalHoursDto.getTwelveHours() : BigDecimal.ZERO)
                    .add(ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getThirteenHours()) ? auxiliaryOriginalHoursDto.getThirteenHours() : BigDecimal.ZERO)
                    .add(ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getFourteenHours()) ? auxiliaryOriginalHoursDto.getFourteenHours() : BigDecimal.ZERO)
                    .add(ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getFifteenHours()) ? auxiliaryOriginalHoursDto.getFifteenHours() : BigDecimal.ZERO)
                    .add(ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getSixteenHours()) ? auxiliaryOriginalHoursDto.getSixteenHours() : BigDecimal.ZERO)
                    .add(ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getSeventeenHours()) ? auxiliaryOriginalHoursDto.getSeventeenHours() : BigDecimal.ZERO)
                    .add(ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getEighteenHours()) ? auxiliaryOriginalHoursDto.getEighteenHours() : BigDecimal.ZERO)
                    .add(ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getNineteenHours()) ? auxiliaryOriginalHoursDto.getNineteenHours() : BigDecimal.ZERO)
                    .add(ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getTwentyHours()) ? auxiliaryOriginalHoursDto.getTwentyHours() : BigDecimal.ZERO)
                    .add(ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getTwentyOneHours()) ? auxiliaryOriginalHoursDto.getTwentyOneHours() : BigDecimal.ZERO)
                    .add(ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getTwentyTwoHours()) ? auxiliaryOriginalHoursDto.getTwentyTwoHours() : BigDecimal.ZERO)
                    .add(ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getTwentyThreeHours()) ? auxiliaryOriginalHoursDto.getTwentyThreeHours() : BigDecimal.ZERO)
                    .add(ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getTwentyFourHours()) ? auxiliaryOriginalHoursDto.getTwentyFourHours() : BigDecimal.ZERO)
                    .add(ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getTwentyFiveHours()) ? auxiliaryOriginalHoursDto.getTwentyFiveHours() : BigDecimal.ZERO)
                    .add(ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getTwentySixHours()) ? auxiliaryOriginalHoursDto.getTwentySixHours() : BigDecimal.ZERO)
                    .add(ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getTwentySevenHours()) ? auxiliaryOriginalHoursDto.getTwentySevenHours() : BigDecimal.ZERO)
                    .add(ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getTwentyEightHours()) ? auxiliaryOriginalHoursDto.getTwentyEightHours() : BigDecimal.ZERO)
                    .add(ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getTwentyNineHours()) ? auxiliaryOriginalHoursDto.getTwentyNineHours() : BigDecimal.ZERO)
                    .add(ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getThirtyHours()) ? auxiliaryOriginalHoursDto.getThirtyHours() : BigDecimal.ZERO)
                    .add(ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getThirtyOneHours()) ? auxiliaryOriginalHoursDto.getThirtyOneHours() : BigDecimal.ZERO)
            );
            auxiliaryOriginalHoursDto.setTotal(auxiliaryOriginalHoursDto.getTotal().setScale(4, RoundingMode.HALF_UP));

            dtoMap.put(name, auxiliaryOriginalHoursDto);
        }
        return dtoMap;
    }

    //合并
    private void merge(Map<String, AuxiliaryOriginalHoursDto> data5, Map<String, AuxiliaryOriginalHoursDto> data4, Map<String, AuxiliaryOriginalHoursDto> data2) {
        for (Map.Entry<String, AuxiliaryOriginalHoursDto> entry : data2.entrySet()) {
            String key = entry.getKey();
            AuxiliaryOriginalHoursDto value = entry.getValue();
            if (data5.containsKey(key)) {
                for (Map.Entry<String, AuxiliaryOriginalHoursDto> dtoEntry : data5.entrySet()) {
                    if (dtoEntry.getKey().equals(key)) {
                        AuxiliaryOriginalHoursDto auxiliaryOriginalHoursDto = new AuxiliaryOriginalHoursDto();
                        AuxiliaryOriginalHoursDto hoursDto = data5.get(key);
                        BeanUtils.copyProperties(hoursDto, auxiliaryOriginalHoursDto);
                        auxiliaryOriginalHoursDto.setType("总工时");
                        auxiliaryOriginalHoursDto.setOneHours((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getOneHours()) ? auxiliaryOriginalHoursDto.getOneHours() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(value.getOneHours()) ? value.getOneHours() : BigDecimal.ZERO));
                        auxiliaryOriginalHoursDto.setTwoHours((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getTwoHours()) ? auxiliaryOriginalHoursDto.getTwoHours() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(value.getTwoHours()) ? value.getTwoHours() : BigDecimal.ZERO));
                        auxiliaryOriginalHoursDto.setThreeHours((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getThreeHours()) ? auxiliaryOriginalHoursDto.getThreeHours() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(value.getThreeHours()) ? value.getThreeHours() : BigDecimal.ZERO));
                        auxiliaryOriginalHoursDto.setFourHours((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getFourHours()) ? auxiliaryOriginalHoursDto.getFourHours() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(value.getFourHours()) ? value.getFourHours() : BigDecimal.ZERO));
                        auxiliaryOriginalHoursDto.setFiveHours((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getFiveHours()) ? auxiliaryOriginalHoursDto.getFiveHours() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(value.getFiveHours()) ? value.getFiveHours() : BigDecimal.ZERO));
                        auxiliaryOriginalHoursDto.setSixHours((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getSixHours()) ? auxiliaryOriginalHoursDto.getSixHours() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(value.getSixHours()) ? value.getSixHours() : BigDecimal.ZERO));
                        auxiliaryOriginalHoursDto.setSevenHours((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getSevenHours()) ? auxiliaryOriginalHoursDto.getSevenHours() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(value.getSevenHours()) ? value.getSevenHours() : BigDecimal.ZERO));
                        auxiliaryOriginalHoursDto.setEightHours((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getEightHours()) ? auxiliaryOriginalHoursDto.getEightHours() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(value.getEightHours()) ? value.getEightHours() : BigDecimal.ZERO));
                        auxiliaryOriginalHoursDto.setNineHours((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getNineHours()) ? auxiliaryOriginalHoursDto.getNineHours() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(value.getNineHours()) ? value.getNineHours() : BigDecimal.ZERO));
                        auxiliaryOriginalHoursDto.setTenHours((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getTenHours()) ? auxiliaryOriginalHoursDto.getTenHours() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(value.getTenHours()) ? value.getTenHours() : BigDecimal.ZERO));
                        auxiliaryOriginalHoursDto.setElevenHours((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getElevenHours()) ? auxiliaryOriginalHoursDto.getElevenHours() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(value.getElevenHours()) ? value.getElevenHours() : BigDecimal.ZERO));
                        auxiliaryOriginalHoursDto.setTwelveHours((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getTwelveHours()) ? auxiliaryOriginalHoursDto.getTwelveHours() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(value.getTwelveHours()) ? value.getTwelveHours() : BigDecimal.ZERO));
                        auxiliaryOriginalHoursDto.setThirteenHours((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getThirteenHours()) ? auxiliaryOriginalHoursDto.getThirteenHours() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(value.getThirteenHours()) ? value.getThirteenHours() : BigDecimal.ZERO));
                        auxiliaryOriginalHoursDto.setFourteenHours((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getFourteenHours()) ? auxiliaryOriginalHoursDto.getFourteenHours() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(value.getFourteenHours()) ? value.getFourteenHours() : BigDecimal.ZERO));
                        auxiliaryOriginalHoursDto.setFifteenHours((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getFifteenHours()) ? auxiliaryOriginalHoursDto.getFifteenHours() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(value.getFifteenHours()) ? value.getFifteenHours() : BigDecimal.ZERO));
                        auxiliaryOriginalHoursDto.setSixteenHours((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getSixteenHours()) ? auxiliaryOriginalHoursDto.getSixteenHours() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(value.getSixteenHours()) ? value.getSixteenHours() : BigDecimal.ZERO));
                        auxiliaryOriginalHoursDto.setSeventeenHours((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getSeventeenHours()) ? auxiliaryOriginalHoursDto.getSeventeenHours() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(value.getSeventeenHours()) ? value.getSeventeenHours() : BigDecimal.ZERO));
                        auxiliaryOriginalHoursDto.setEighteenHours((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getEighteenHours()) ? auxiliaryOriginalHoursDto.getEighteenHours() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(value.getEighteenHours()) ? value.getEighteenHours() : BigDecimal.ZERO));
                        auxiliaryOriginalHoursDto.setNineteenHours((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getNineteenHours()) ? auxiliaryOriginalHoursDto.getNineteenHours() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(value.getNineteenHours()) ? value.getNineteenHours() : BigDecimal.ZERO));
                        auxiliaryOriginalHoursDto.setTwelveHours((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getTwelveHours()) ? auxiliaryOriginalHoursDto.getTwelveHours() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(value.getTwelveHours()) ? value.getTwelveHours() : BigDecimal.ZERO));
                        auxiliaryOriginalHoursDto.setTwentyOneHours((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getTwentyOneHours()) ? auxiliaryOriginalHoursDto.getTwentyOneHours() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(value.getTwentyOneHours()) ? value.getTwentyOneHours() : BigDecimal.ZERO));
                        auxiliaryOriginalHoursDto.setTwentyTwoHours((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getTwentyTwoHours()) ? auxiliaryOriginalHoursDto.getTwentyTwoHours() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(value.getTwentyTwoHours()) ? value.getTwentyTwoHours() : BigDecimal.ZERO));
                        auxiliaryOriginalHoursDto.setTwentyThreeHours((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getTwentyThreeHours()) ? auxiliaryOriginalHoursDto.getTwentyThreeHours() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(value.getTwentyThreeHours()) ? value.getTwentyThreeHours() : BigDecimal.ZERO));
                        BigDecimal v = ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getTwentyFourHours()) ? auxiliaryOriginalHoursDto.getTwentyFourHours() : BigDecimal.ZERO;
                        BigDecimal v1 = ObjectUtils.isNotEmpty(value.getTwentyFourHours()) ? value.getTwentyFourHours() : BigDecimal.ZERO;
                        BigDecimal v2 = v.add(v1);
                        auxiliaryOriginalHoursDto.setTwentyFourHours((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getTwentyFourHours()) ? auxiliaryOriginalHoursDto.getTwentyFourHours() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(value.getTwentyFourHours()) ? value.getTwentyFourHours() : BigDecimal.ZERO));
                        auxiliaryOriginalHoursDto.setTwentyFiveHours((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getTwentyFiveHours()) ? auxiliaryOriginalHoursDto.getTwentyFiveHours() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(value.getTwentyFiveHours()) ? value.getTwentyFiveHours() : BigDecimal.ZERO));
                        auxiliaryOriginalHoursDto.setTwentySixHours((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getTwentySixHours()) ? auxiliaryOriginalHoursDto.getTwentySixHours() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(value.getTwentySixHours()) ? value.getTwentySixHours() : BigDecimal.ZERO));
                        auxiliaryOriginalHoursDto.setTwentySevenHours((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getTwentySevenHours()) ? auxiliaryOriginalHoursDto.getTwentySevenHours() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(value.getTwentySevenHours()) ? value.getTwentySevenHours() : BigDecimal.ZERO));
                        auxiliaryOriginalHoursDto.setTwentyEightHours((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getTwentyEightHours()) ? auxiliaryOriginalHoursDto.getTwentyEightHours() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(value.getTwentyEightHours()) ? value.getTwentyEightHours() : BigDecimal.ZERO));
                        auxiliaryOriginalHoursDto.setTwentyNineHours((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getTwentyNineHours()) ? auxiliaryOriginalHoursDto.getTwentyNineHours() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(value.getTwentyNineHours()) ? value.getTwentyNineHours() : BigDecimal.ZERO));
                        auxiliaryOriginalHoursDto.setThirtyHours((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getThirtyHours()) ? auxiliaryOriginalHoursDto.getThirtyHours() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(value.getThirtyHours()) ? value.getThirtyHours() : BigDecimal.ZERO));
                        auxiliaryOriginalHoursDto.setThirtyOneHours((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getThirtyOneHours()) ? auxiliaryOriginalHoursDto.getThirtyOneHours() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(value.getThirtyOneHours()) ? value.getThirtyOneHours() : BigDecimal.ZERO));
                        auxiliaryOriginalHoursDto.setTotal((ObjectUtils.isNotEmpty(auxiliaryOriginalHoursDto.getTotal()) ? auxiliaryOriginalHoursDto.getTotal() : BigDecimal.ZERO).add(ObjectUtils.isNotEmpty(value.getTotal()) ? value.getTotal() : BigDecimal.ZERO));
                        data4.put(key, auxiliaryOriginalHoursDto);
                    } /*else {
                        AuxiliaryOriginalHoursDto hoursDto = data5.get(dtoEntry.getKey());
                        AuxiliaryOriginalHoursDto dto = new AuxiliaryOriginalHoursDto();
                        BeanUtils.copyProperties(hoursDto, dto);
                        dto.setType("总工时");
                        data4.put(dtoEntry.getKey(), dto);
                    }*/
                }
            } else {
                value.setType("总工时");
                data4.put(key, value);
            }
        }

    }
}
