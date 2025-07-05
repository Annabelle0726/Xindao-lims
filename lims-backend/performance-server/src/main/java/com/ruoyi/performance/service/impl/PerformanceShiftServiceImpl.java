package com.ruoyi.performance.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.JackSonUtil;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.performance.dto.PerformanceShiftAddDto;
import com.ruoyi.performance.dto.PerformanceShiftMapDto;
import com.ruoyi.performance.mapper.PerformanceShiftMapper;
import com.ruoyi.performance.pojo.PerformanceShift;
import com.ruoyi.performance.service.PerformanceShiftService;
import com.ruoyi.system.mapper.UserMapper;
import com.ruoyi.system.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * <p>
 * 绩效管理-班次 服务实现类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-05-08 09:12:04
 */
@Service
public class PerformanceShiftServiceImpl extends ServiceImpl<PerformanceShiftMapper, PerformanceShift> implements PerformanceShiftService {

    @Autowired
    private ISysDictTypeService dictTypeService;

    @Autowired
    UserMapper userMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void performanceShiftAdd(PerformanceShiftAddDto performanceShiftAddDto) {
        List<PerformanceShift> list = new ArrayList<>();
        LocalDateTime startWeek = performanceShiftAddDto.getStartWeek();
        LocalDateTime endWeek = performanceShiftAddDto.getEndWeek();


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDateTime = performanceShiftAddDto.getStartWeek().format(formatter);
        String[] splitUserId = performanceShiftAddDto.getUserId().split(",");
        for (String userId : splitUserId) {
            //判断是否跨月
            boolean isMonth = startWeek.getMonthValue() != endWeek.getMonthValue();
            if (isMonth){
                //如果跨月,则两个月都判断一下看数据库是哪个月份的数据没有
                boolean exists1 = baseMapper.exists(Wrappers.<PerformanceShift>lambdaQuery()
                        .eq(PerformanceShift::getWorkTime, startWeek)
                        .eq(PerformanceShift::getUserId, userId));
                boolean exists2 = baseMapper.exists(Wrappers.<PerformanceShift>lambdaQuery()
                        .eq(PerformanceShift::getWorkTime, endWeek)
                        .eq(PerformanceShift::getUserId, userId));
                if (!exists1 && !exists2){
                    //两个月都不存在数据
                    list = saveMonth(performanceShiftAddDto.getStartWeek(), userId, list);
                    list = saveMonth(performanceShiftAddDto.getEndWeek(), userId, list);
                }else if (!exists1 && exists2){
                    //开始的月份不存在数据
                    list = saveMonth(performanceShiftAddDto.getStartWeek(), userId, list);
                }else if (exists1 && !exists2){
                    //结束的月份不存在数据
                    list = saveMonth(performanceShiftAddDto.getEndWeek(), userId, list);
                }
            }else {
                //不跨月
                boolean exists = baseMapper.exists(Wrappers.<PerformanceShift>lambdaQuery()
                        .in(PerformanceShift::getWorkTime, formattedDateTime)
                        .eq(PerformanceShift::getUserId, userId));
                // 如果不存在添加数据
                if (!exists) {
                    list = saveMonth(performanceShiftAddDto.getEndWeek(), userId, list);
                }
            }
        }
        if (!list.isEmpty()) {
            baseMapper.insertBatchSomeColumn(list);
            list.clear();
        }
        // 再次更新
        List<LocalDateTime> datesBetween = getLocalDateTimesBetween(performanceShiftAddDto.getStartWeek(), performanceShiftAddDto.getEndWeek());
        for (LocalDateTime date : datesBetween) {
            for (String s : splitUserId) {
                PerformanceShift performanceShift = new PerformanceShift();
                performanceShift.setShift(performanceShiftAddDto.getShift());
                performanceShift.setUserId(Integer.valueOf(s));
                performanceShift.setWorkTime(date);
                String formatterDateTime = date.format(formatter);
                baseMapper.update(new PerformanceShift(), Wrappers.<PerformanceShift>lambdaUpdate()
                        .set(PerformanceShift::getShift, performanceShiftAddDto.getShift())
                        .eq(PerformanceShift::getUserId, s)
                        .eq(PerformanceShift::getWorkTime, formatterDateTime));
            }
        }
    }

    private List<PerformanceShift>   saveMonth (LocalDateTime week,String userId,List<PerformanceShift> list){
        LocalDate firstDayOfMonth = week.toLocalDate().withDayOfMonth(1);
        LocalDate lastDayOfMonth = week.toLocalDate().with(TemporalAdjusters.lastDayOfMonth());
        List<LocalDateTime> localDateTimesBetween = getLocalDateTimesBetween(firstDayOfMonth.atStartOfDay(), lastDayOfMonth.atStartOfDay());
        localDateTimesBetween.forEach(i -> {
            PerformanceShift performanceShift = new PerformanceShift();
            performanceShift.setUserId(Integer.valueOf(userId));
            performanceShift.setWorkTime(i);
            performanceShift.setShift("");
            list.add(performanceShift);
            if (list.size() >= 1000) {
                baseMapper.insertBatchSomeColumn(list);
                list.clear();
            }
        });
        return list;
    }

    @Override
    public Map<String, Object> performanceShiftPage(Page<Object> page, String time, String userName, String laboratory) {
        //查询当前登录人员的架构
        Integer userId = SecurityUtils.getUserId().intValue();
        //判断全部,个人,组织的权限
        User user = userMapper.selectById(userId);//当前登录的人
        //获取当前人所属实验室id
        String departLimsId = user.getDepartLimsId();
        if (com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isNotEmpty(departLimsId) && !departLimsId.equals("")) {
            String[] split = departLimsId.split(",");
            //查询对应架构名称(通信实验室,电力实验室,检测办)
            String departLims = baseMapper.seldepLimsId(Integer.parseInt(split[split.length - 1]));
            if (departLims.contains("实验室")) {
                laboratory = departLims;
            }
        }
        IPage<PerformanceShiftMapDto> mapIPage = baseMapper.performanceShiftPage(page, time, userName, laboratory);

        List<SysDictData> shiftType = dictTypeService.selectDictDataByName("班次类型");
        List<Map<String, Object>> mapYearIPage = baseMapper.performanceShiftYearPage(time, userName, laboratory);
        mapIPage.getRecords().forEach(i -> {
            String[] shiftTimes = i.getShiftTime().split(";");
            double totalAttendance = 0;
            List<Map<String, Object>> map = new ArrayList<>();
            // 分割日期
            for (String shiftTime : shiftTimes) {
                Map<String, Object> hashMap = new HashMap<>();
                String[] shiftTimeAndShift = shiftTime.split("：");
                for (SysDictData enums : shiftType) {
                    if (!i.getMonthlyAttendance().containsKey(enums.getDictLabel())) {
                        i.getMonthlyAttendance().put(enums.getDictLabel(), 0);
                    }
                    if (enums.getDictValue().equals(shiftTimeAndShift[1])) {
                        BigDecimal bigDecimal = new BigDecimal(i.getMonthlyAttendance().get(enums.getDictLabel()).toString());
                        i.getMonthlyAttendance().put(enums.getDictLabel(), bigDecimal.add(new BigDecimal("1")));
                    }
                    // 半，另外半天算给早
                    if (shiftTimeAndShift[1].equals("5") && enums.getDictValue().equals("0")) {
                        BigDecimal bigDecimal = new BigDecimal(i.getMonthlyAttendance().get(enums.getDictLabel()).toString());
                        i.getMonthlyAttendance().put(enums.getDictLabel(), bigDecimal.add(new BigDecimal("0.5")));
                    }
                }
                // 早，中，夜，差
                if (shiftTimeAndShift[1].equals("1") || shiftTimeAndShift[1].equals("2") || shiftTimeAndShift[1].equals("0") || shiftTimeAndShift[1].equals("6")) {
                    i.getMonthlyAttendance().put("totalAttendance", totalAttendance += 1);
                }
                // 半
                if (shiftTimeAndShift[1].equals("5")) {
                    i.getMonthlyAttendance().put("totalAttendance", totalAttendance += 0.5);
                }
                hashMap.put("id", shiftTimeAndShift[2]);
                hashMap.put("shift", shiftTimeAndShift[1]);
                hashMap.put("time", shiftTimeAndShift[0]);
                map.add(hashMap);
            }
            double totalYearAttendance = 0;
            Map<String, Object> hashMap = new HashMap<>();
            for (Map<String, Object> record : mapYearIPage) {
                if (record.get("user_id").toString().equals(i.getUserId())) {
                    for (SysDictData enums : shiftType) {
                        if (!hashMap.containsKey(enums.getDictLabel())) {
                            hashMap.put(enums.getDictLabel(), 0);
                        }
                        if (enums.getDictValue().equals(record.get("shift"))) {
                            BigDecimal num = new BigDecimal(hashMap.get(enums.getDictLabel()).toString());
                            hashMap.put(enums.getDictLabel(), num.add(new BigDecimal("1")));
                        }
                        // 半，另外半天算给早
                        if (record.get("shift").equals("5") && enums.getDictValue().equals("0")) {
                            BigDecimal bigDecimal = new BigDecimal(hashMap.get(enums.getDictLabel()).toString());
                            hashMap.put(enums.getDictLabel(), bigDecimal.add(new BigDecimal("0.5")));
                        }
                    }
                    if (record.get("shift").equals("1") || record.get("shift").equals("2") || record.get("shift").equals("0") || record.get("shift").equals("6")) {
                        hashMap.put("totalAttendance", totalYearAttendance += 1);
                    }
                    // 半
                    if (record.get("shift").equals("5")) {
                        hashMap.put("totalAttendance", totalYearAttendance += 0.5);
                    }
                }
            }
            i.setSidebarAnnualAttendance(hashMap);
            i.setList(map);
            i.setShiftTime(null);
        });
        // 获取header时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 将字符串时间转换为 LocalDateTime 类型时间
        LocalDateTime localDateTime = LocalDateTime.parse(time, formatters);
        LocalDate firstDayOfMonth = localDateTime.toLocalDate().withDayOfMonth(1);
        LocalDate lastDayOfMonth = localDateTime.toLocalDate().with(TemporalAdjusters.lastDayOfMonth());
        List<LocalDateTime> localDateTimesBetween = getLocalDateTimesBetween(firstDayOfMonth.atStartOfDay(), lastDayOfMonth.atStartOfDay());
        List<Object> list1 = new ArrayList<>();
        for (LocalDateTime dateTime : localDateTimesBetween) {
            Map<Object, Object> hashMap = new HashMap<>();
            DateTime parse = DateUtil.parse(dateTime.format(formatter));
            hashMap.put("weekly", DateUtil.weekOfYear(DateUtil.offsetDay(parse, 1)));
            hashMap.put("headerTime", getWeek(dateTime.format(formatters)));
            list1.add(hashMap);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("page", mapIPage);
        resultMap.put("headerList", list1);
        return resultMap;
    }

    @Override
    public void performanceShiftUpdate(PerformanceShift performanceShift) {
        baseMapper.update(new PerformanceShift(), Wrappers.<PerformanceShift>lambdaUpdate()
                .eq(PerformanceShift::getId, performanceShift.getId())
                .set(PerformanceShift::getShift, performanceShift.getShift()));
    }

    @Override
    public IPage<Map<String, Object>> performanceShiftPageYear(Page<Object> page, String time, String userName, String laboratory) {
        //查询当前登录人员的架构
        Integer userId = SecurityUtils.getUserId().intValue();
        //判断全部,个人,组织的权限
        User user = userMapper.selectById(userId);//当前登录的人
        //获取当前人所属实验室id
        String departLimsId = user.getDepartLimsId();
        if (com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isNotEmpty(departLimsId) && !departLimsId.equals("")) {
            String[] split = departLimsId.split(",");
            //查询对应架构名称(通信实验室,电力实验室,检测办)
            String departLims = baseMapper.seldepLimsId(Integer.parseInt(split[split.length - 1]));
            if (departLims.contains("实验室")) {
                laboratory = departLims;
            }
        }
        IPage<Map<String, Object>> mapYearIPage = baseMapper.performanceShiftYear(page, time, userName, laboratory);
        List<SysDictData> shiftType = dictTypeService.selectDictDataByName("班次类型");
        mapYearIPage.setRecords(annualAttendanceProcessing(mapYearIPage.getRecords(), shiftType));
        return mapYearIPage;
    }

    // 年分页与导出共同使用
    public List<Map<String, Object>> annualAttendanceProcessing(List<Map<String, Object>> mapYearList, List<SysDictData> shiftType) {
        for (Map<String, Object> map : mapYearList) {
            Map<String, Object> resultMap = new LinkedHashMap<>();
            Map<String, Object> hashMapYear = new LinkedHashMap<>();
            double totalYearAttendance = 0;
            // 一年12个月
            for (int i = 1; i < 13; i++) {
                Map<String, Object> hashMapMonth = new LinkedHashMap<>();
                double totalMonthAttendance = 0;
                for (SysDictData shift : shiftType) {
                    // 初始化赋值
                    if (!hashMapYear.containsKey(shift.getDictLabel())) {
                        hashMapYear.put(shift.getDictLabel(), 0);
                    }
                    // 月
                    if (!ObjectUtils.isEmpty(map.get("month_str"))) {
                        String charArray = map.get("month_str").toString();
                        int count = countOccurrences(charArray, i + "：" + shift.getDictValue());
                        hashMapMonth.put(shift.getDictLabel(), count);
                        hashMapYear.put(shift.getDictLabel(), new BigDecimal(hashMapYear.get(shift.getDictLabel()).toString()).add(new BigDecimal(count)));
                        // 早，中，夜，差
                        if (shift.getDictValue().equals("0") || shift.getDictValue().equals("1") || shift.getDictValue().equals("2") || shift.getDictValue().equals("6")) {
                            totalMonthAttendance += count;
                            totalYearAttendance += count;
                        }
//                      半，另外半天算给早
                        if (shift.getDictValue().equals("5")) {
                            BigDecimal multiply = new BigDecimal("0.5").multiply(new BigDecimal(count)).setScale(1, BigDecimal.ROUND_CEILING);
                            hashMapMonth.put(shiftType.get(0).getDictLabel(), new BigDecimal(hashMapMonth.get(shiftType.get(0).getDictLabel()).toString()).add(multiply));
                            hashMapYear.put(shiftType.get(0).getDictLabel(), new BigDecimal(hashMapYear.get(shiftType.get(0).getDictLabel()).toString()).add(multiply));
                            totalMonthAttendance += multiply.doubleValue();
                            totalYearAttendance += multiply.doubleValue();
                        }
                    }
                    // 空数据
                    else {
                        map.put("work_time", i);
                        hashMapMonth.put(shift.getDictLabel(), 0);
                    }
                }
                hashMapMonth.put("totalMonthAttendance", totalMonthAttendance);
                hashMapYear.put("totalYearAttendance", totalYearAttendance);
                resultMap.put(i + "", hashMapMonth);
            }
            map.remove("month_str");
            map.remove("year_str");
            map.put("year", hashMapYear);
            map.put("month", resultMap);
        }
        return mapYearList;
    }

    public static int countOccurrences(String str, String target) {
        int count = 0;
        int index = 0;
        while ((index = str.indexOf(target, index)) != -1) {
            count++;
            index += target.length();
        }
        return count;
    }

    public List<List<Object>> dataRequiredForProcessingIntoExcel(List<Map<String, Object>> list, List<SysDictData> enums) throws Exception {
        List<List<Object>> data = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            List<Object> excelRowList = new ArrayList<>();
            excelRowList.add(i + 1);
            excelRowList.add(list.get(i).get("account"));
            excelRowList.add(list.get(i).get("name"));
            Map<String, Object> year = JackSonUtil.unmarshal(JackSonUtil.marshal(list.get(i).get("year")), Map.class);
            excelRowList.add(year.get("totalYearAttendance"));
            enums.forEach(j -> {
                if (!j.getDictValue().equals("5")) {
                    excelRowList.add(year.get(j.getDictLabel()));
                }
            });
            Map<String, Map<String, Object>> month = JackSonUtil.unmarshal(JackSonUtil.marshal(list.get(i).get("month")), Map.class);
            for (int j = 1; j < 13; j++) {
                Object totalMonthAttendance = month.get(j + "").get("totalMonthAttendance");
                excelRowList.add(totalMonthAttendance);
                for (SysDictData anEnum : enums) {
                    if (!anEnum.getDictValue().equals("5")) {
                        excelRowList.add(month.get(j + "").get(anEnum.getDictLabel()));
                    }
                }
            }
            data.add(excelRowList);
        }
        return data;
    }

    @Override
    public Map<Object, Object> exportToYearExcel(String time, String userName, String laboratory) throws Exception {
        Map<Object, Object> map = new HashMap<>();
        List<SysDictData> shiftType = dictTypeService.selectDictDataByName("班次类型");
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 将字符串时间转换为 LocalDateTime 类型时间
        LocalDateTime localDateTime = LocalDateTime.parse(time, formatters);
        map.put("header", getYearHeader(localDateTime.getYear() + " 年", shiftType));
        List<Map<String, Object>> mapYearList = baseMapper.performanceShiftYearList(time, userName, laboratory);
        annualAttendanceProcessing(mapYearList, shiftType);
        List<List<Object>> lists = dataRequiredForProcessingIntoExcel(mapYearList, shiftType);
        map.put("data", lists);
        return map;
    }

    @Override
    public Map<Object, Object> exportToMonthExcel(String time, String userName, String laboratory) {
        List<SysDictData> shiftType = dictTypeService.selectDictDataByName("班次类型");
        List<PerformanceShiftMapDto> mapIPage = baseMapper.performanceShiftList(time, userName, laboratory);
        mapIPage.forEach(i -> {
            String[] shiftTimes = i.getShiftTime().split(";");
            double totalAttendance = 0;
            List<Map<String, Object>> map = new ArrayList<>();
            // 分割日期
            for (String shiftTime : shiftTimes) {
                Map<String, Object> hashMap = new HashMap<>();
                String[] shiftTimeAndShift = shiftTime.split("：");
                for (SysDictData enums : shiftType) {
                    if (!i.getMonthlyAttendance().containsKey(enums.getDictLabel())) {
                        i.getMonthlyAttendance().put(enums.getDictLabel(), 0);
                    }
                    if (enums.getDictValue().equals(shiftTimeAndShift[1])) {
                        BigDecimal bigDecimal = new BigDecimal(i.getMonthlyAttendance().get(enums.getDictLabel()).toString());
                        i.getMonthlyAttendance().put(enums.getDictLabel(), bigDecimal.add(new BigDecimal("1")));
                    }
                    // 半，另外半天算给早
                    if (shiftTimeAndShift[1].equals("5") && enums.getDictValue().equals("0")) {
                        BigDecimal bigDecimal = new BigDecimal(i.getMonthlyAttendance().get(enums.getDictLabel()).toString());
                        i.getMonthlyAttendance().put(enums.getDictLabel(), bigDecimal.add(new BigDecimal("0.5")));
                    }
                }
                // 早，中，夜，差
                if (shiftTimeAndShift[1].equals("1") || shiftTimeAndShift[1].equals("2") || shiftTimeAndShift[1].equals("0") || shiftTimeAndShift[1].equals("6")) {
                    i.getMonthlyAttendance().put("totalAttendance", totalAttendance += 1);
                }
                // 半
                if (shiftTimeAndShift[1].equals("5")) {
                    i.getMonthlyAttendance().put("totalAttendance", totalAttendance += 0.5);
                }
                hashMap.put("id", shiftTimeAndShift[2]);
                hashMap.put("shift", shiftTimeAndShift[1]);
                hashMap.put("time", shiftTimeAndShift[0]);
                map.add(hashMap);
            }
            i.setList(map);
            i.setShiftTime(null);
        });
        Map<Object, Object> map = new HashMap<>();
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 将字符串时间转换为 LocalDateTime 类型时间
        LocalDateTime localDateTime = LocalDateTime.parse(time, formatters);
        map.put("header", getMonthHeader(localDateTime, shiftType));
        List<List<Object>> lists = dataRequiredForProcessingIntoExcelMonth(mapIPage, shiftType);
        map.put("data", lists);
        return map;
    }

    // 获取两个localDateTime的每一天
    public static List<LocalDateTime> getLocalDateTimesBetween(LocalDateTime start, LocalDateTime end) {
        List<LocalDateTime> localDateTimes = new ArrayList<>();
        LocalDate currentDate = start.toLocalDate();
        LocalDateTime currentLocalDateTime = start;
        while (!currentDate.isAfter(end.toLocalDate())) {
            localDateTimes.add(currentLocalDateTime);
            currentLocalDateTime = currentLocalDateTime.plusDays(1);
            currentDate = currentDate.plusDays(1);
        }
        return localDateTimes;
    }

    public static String getWeek(String dayStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(dayStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            return day + " " + getWeekDay(dayOfWeek);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getWeekDay(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                return "周一";
            case Calendar.TUESDAY:
                return "周二";
            case Calendar.WEDNESDAY:
                return "周三";
            case Calendar.THURSDAY:
                return "周四";
            case Calendar.FRIDAY:
                return "周五";
            case Calendar.SATURDAY:
                return "周六";
            case Calendar.SUNDAY:
                return "周日";
            default:
                return "未知";
        }
    }

    /**
     * 返回表头
     * <p>
     * 外层List代表行内层 List代表列  相同的列数据会被主动合并
     * 构造双列表头
     *
     * @return List<List < String>>
     */
    private static List<List<String>> getYearHeader(String year, List<SysDictData> enums) {
        List<List<String>> line = new ArrayList<>();
        line.add(Arrays.asList("考勤汇总", "序号", "序号"));
        line.add(Arrays.asList("考勤汇总", "工号", "工号"));
        line.add(Arrays.asList("考勤汇总", "姓名", "姓名"));
        line.add(Arrays.asList("出勤详情", year, "出勤"));
        // 年 header
        for (SysDictData anEnum : enums) {
            if (!anEnum.getDictValue().equals("5")) {
                line.add(Arrays.asList("考勤汇总", year, anEnum.getDictLabel()));
            }
        }
        // 月header
        for (int i = 1; i < 13; i++) {
            line.add(Arrays.asList("出勤详情", i + " 月", "出勤"));
            for (SysDictData anEnum : enums) {
                if (!anEnum.getDictValue().equals("5")) {
                    line.add(Arrays.asList("出勤详情", i + " 月", anEnum.getDictLabel()));
                }
            }
        }
        return line;
    }

    private static List<List<String>> getMonthHeader(LocalDateTime localDateTimeYear, List<SysDictData> enums) {
        String year = localDateTimeYear.getYear() + " 年人员班次";
        List<List<String>> line = new ArrayList<>();
        line.add(Arrays.asList(year, "序号", "序号", "序号"));
        line.add(Arrays.asList(year, "姓名", "姓名", "姓名"));
        line.add(Arrays.asList(year, "实验室", "实验室", "实验室"));
        line.add(Arrays.asList(year, localDateTimeYear.getYear() + "", localDateTimeYear.getYear() + "", "出勤"));
        line.add(Arrays.asList(year, localDateTimeYear.getYear() + "", localDateTimeYear.getYear() + "", enums.get(3).getDictLabel()));
        line.add(Arrays.asList(year, "年", "年", enums.get(4).getDictLabel()));
        line.add(Arrays.asList(year, localDateTimeYear.getMonthValue() + "", localDateTimeYear.getMonthValue() + "", enums.get(0).getDictLabel()));
        line.add(Arrays.asList(year, "月", "月", enums.get(1).getDictLabel()));
        line.add(Arrays.asList(year, "", "", enums.get(2).getDictLabel()));
        line.add(Arrays.asList(year, "周次", "星期", "出差"));
        LocalDate firstDayOfMonth = localDateTimeYear.toLocalDate().withDayOfMonth(1);
        LocalDate lastDayOfMonth = localDateTimeYear.toLocalDate().with(TemporalAdjusters.lastDayOfMonth());
        List<LocalDateTime> timeList = getLocalDateTimesBetween(firstDayOfMonth.atStartOfDay(), lastDayOfMonth.atStartOfDay());
        timeList.forEach(i -> {
            int dayOfYear = i.getDayOfMonth();
            Date from = Date.from(i.atZone(ZoneId.systemDefault()).toInstant());
            String weekDay = getWeekDay(i.getDayOfWeek().getValue());
            line.add(Arrays.asList(year, DateUtil.weekOfYear(DateUtil.offsetDay(from, 1)) + "", weekDay, dayOfYear + ""));
        });
        return line;
    }

    public List<List<Object>> dataRequiredForProcessingIntoExcelMonth(List<PerformanceShiftMapDto> list, List<SysDictData> enums) {
        List<List<Object>> data = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            List<Object> excelRowList = new ArrayList<>();
            excelRowList.add(i + 1);
            excelRowList.add(list.get(i).getName());
            excelRowList.add(list.get(i).getDepartment());
            excelRowList.add(list.get(i).getMonthlyAttendance().get("totalAttendance"));
            excelRowList.add(list.get(i).getMonthlyAttendance().get(enums.get(3).getDictLabel())); // 休
            excelRowList.add(list.get(i).getMonthlyAttendance().get(enums.get(4).getDictLabel())); // 假
            excelRowList.add(list.get(i).getMonthlyAttendance().get(enums.get(0).getDictLabel())); // 早
            excelRowList.add(list.get(i).getMonthlyAttendance().get(enums.get(1).getDictLabel())); // 中
            excelRowList.add(list.get(i).getMonthlyAttendance().get(enums.get(2).getDictLabel())); // 夜
            excelRowList.add(list.get(i).getMonthlyAttendance().get(enums.get(6).getDictLabel())); // 差
            for (Map<String, Object> o : list.get(i).getList()) {
                String enumLabel = "";
                for (SysDictData anEnum : enums) {
                    if (anEnum.getDictValue().equals(o.get("shift"))) {
                        enumLabel = anEnum.getDictLabel();
                    }
                }
                excelRowList.add(ObjectUtils.isEmpty(enumLabel) ? "-" : enumLabel);
            }
            data.add(excelRowList);
        }
        return data;
    }
}
