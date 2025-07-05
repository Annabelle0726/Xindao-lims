package com.ruoyi.require.task;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.WxCpUtils;
import com.ruoyi.require.dto.FeTempHumRecordDto;
import com.ruoyi.require.mapper.CollectLaboratoryMapper;
import com.ruoyi.require.mapper.CollectTemperatureHumidityMapper;
import com.ruoyi.require.mapper.FeTempHumDateMapper;
import com.ruoyi.require.mapper.FeTempHumRecordMapper;
import com.ruoyi.require.pojo.CollectLaboratory;
import com.ruoyi.require.pojo.CollectTemperatureHumidity;
import com.ruoyi.require.pojo.FeTempHumDate;
import com.ruoyi.require.pojo.FeTempHumRecord;
import com.ruoyi.system.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Author: yuan
 * Date: 2024-12-19 星期四 8:33:56
 * Description:
 */
@Component
public class FeTempHumDateSchedule {

    @Autowired
    private CollectLaboratoryMapper collectLaboratoryMapper;
    @Autowired
    private FeTempHumDateMapper feTempHumDateMapper;
    @Autowired
    private CollectTemperatureHumidityMapper collectTemperatureHumidityMapper;
    @Autowired
    private FeTempHumRecordMapper feTempHumRecordMapper;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private UserMapper userMapper;

    // 获取当前年份和月份 如：2024-11
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
    private String formattedDate = sdf.format(Calendar.getInstance().getTime());

    /**
     * 检查实验室是否有任务
     */
    public synchronized void checkLaboratoriesSchedule() {
        // 获取当前年份和月份 如：2024-11
        formattedDate = sdf.format(Calendar.getInstance().getTime());
        // 查询所有的实验室
        List<CollectLaboratory> collectLaboratories = collectLaboratoryMapper.selectList(null);
        // 遍历实验室，判断任务列表中是否存该实验室的任务
        for (CollectLaboratory collectLaboratory : collectLaboratories) {
            // 查询数据库中是否存在该实验室的任务
            int count = feTempHumDateMapper.selectFeTempHumDateIncludeFormattedDate(collectLaboratory.getLaboratory(), formattedDate);
            // 查询结果为0，说明数据库中不存在该实验室的任务，需要新增
            if (count == 0) {
                FeTempHumDate feTempHumDate = new FeTempHumDate();
                feTempHumDate.setMonthDate(formattedDate);
                feTempHumDate.setTestAreaName(collectLaboratory.getLaboratory());
                if (collectLaboratory.getLaboratory().equals("恒温二")) {
                    feTempHumDate.setSubjoin("注: 该实验室环境条件温度波动不大于±1℃要求");
                }
                feTempHumDateMapper.insert(feTempHumDate);
            }
        }
    }

    /**
     * 每天9点执行一次 除了星期天
     * 获取温度湿度
     */
//    @Scheduled(cron = "0/5 * * * * *")
    @Scheduled(cron = "0 0 9 * * 1-6") // 每天9点执行一次 除了星期天
    public void task1() {
        checkLaboratoriesSchedule();
        // 根据时间降序，获取到最新的一条数据
        CollectTemperatureHumidity collectTemperatureHumidity = collectTemperatureHumidityMapper.selectOne(Wrappers.<CollectTemperatureHumidity>lambdaQuery()
                .orderByDesc(CollectTemperatureHumidity::getCollectTime)
                .last("limit 1"));

        // 获取所有的实验室
        List<CollectLaboratory> collectLaboratories = collectLaboratoryMapper.selectList(null);
        // 遍历获取实验室编号 获取温湿度 保存在对应的
        for (CollectLaboratory collectLaboratory : collectLaboratories) {
            // 实验室编号
            int labNumber = Integer.parseInt(collectLaboratory.getDeviceCode());
            // 根据实验室编号拼接对应的湿度和温度字段
            String temperatureField = "code" + labNumber + "Temperature";
            String humidityField = "code" + labNumber + "Humidity";
            // 获取温度
            Double temperature = getFieldValue(collectTemperatureHumidity, temperatureField) != null ? (Double) getFieldValue(collectTemperatureHumidity, temperatureField) : Double.valueOf(00.0);
            // 获取湿度
            Double humidity = getFieldValue(collectTemperatureHumidity, humidityField) != null ? (Double) getFieldValue(collectTemperatureHumidity, humidityField) : Double.valueOf(00.0);
            // 查询试验区域
            FeTempHumDate feTempHumDate = feTempHumDateMapper.selectOne(Wrappers.<FeTempHumDate>lambdaQuery()
                    .eq(FeTempHumDate::getMonthDate, formattedDate)
                    .eq(FeTempHumDate::getTestAreaName, collectLaboratory.getLaboratory())
                    .orderByDesc(FeTempHumDate::getCreateTime)
                    .last("limit 1"));
            Integer dateId = feTempHumDate.getDateId();
            // 判断今天是否有温湿度记录
            FeTempHumRecord feTempHumRecord = record(dateId);
            // 查询出来就代表今天记录过了就不记录
            if (feTempHumRecord != null) {
                // 有温度湿度记录就不记录
                continue;
            }

            // 创建今天温湿度记录对象 记录上午温度湿度
            FeTempHumRecord newFeTempHumRecord = new FeTempHumRecord();
            newFeTempHumRecord.setRecordDate(LocalDate.now());
            newFeTempHumRecord.setMorningTestTime(LocalTime.now());
            newFeTempHumRecord.setMorningTemp(temperature.toString());
            newFeTempHumRecord.setMorningHum(humidity.toString());
            newFeTempHumRecord.setDateId(dateId);
            feTempHumRecordMapper.insert(newFeTempHumRecord);

            // 判断温湿度是否超过, 超过提醒
            if (feTempHumDate.getRegistrarUserId() != null) {
                if (temperature < collectLaboratory.getTemperatureLowest() || temperature > collectLaboratory.getTemperatureHighest() ||
                        humidity< collectLaboratory.getHumidityLowest() || humidity > collectLaboratory.getHumidityHighest()) {
                    // 发送企业微信通知
                    threadPoolTaskExecutor.execute(() -> {
                        // 查询接收人
                        User personnel = userMapper.selectById(feTempHumDate.getRegistrarUserId());

                        String message = "";
                        message += "上午实验室温度或湿度超过指定区间, 请去查看";
                        message += "\n请去资源管理-设施和环境温度一温湿度记录";
                        message += StrUtil.format("\n实验室区域: {}", feTempHumDate.getTestAreaName());
                        //发送企业微信消息通知
                        try {
                            WxCpUtils.inform(personnel.getAccount(), message, null);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }

        }
    }

    /**
     * 判断今天是否有温湿度记录
     */
    private FeTempHumRecord record(Integer dateId) {
        // 获取今天的日期
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        // 获取今天的 00:00:00 和 23:59:59
        String start = today.atStartOfDay().format(dateTimeFormatter);  // 00:00:00
        String end = today.atTime(LocalTime.MAX).format(dateTimeFormatter); // 23:59:59
        // 查询今天是否有温湿度记录
        return feTempHumRecordMapper.selectOne(Wrappers.<FeTempHumRecord>lambdaQuery()
                // 哪个实验室
                .eq(FeTempHumRecord::getDateId, dateId)
                // 记录时间在今天之内
                .eq(FeTempHumRecord::getRecordDate, today)
                .between(FeTempHumRecord::getMorningTestTime, start, end)
                .orderByDesc(FeTempHumRecord::getTempHumId)
                .last("limit 1"));
    }

    /**
     * 每天14点执行一次 除了星期六和星期天
     * 获取温度湿度
     */
//    @Scheduled(cron = "0/5 * * * * *")
    @Scheduled(cron = "0 0 14 * * 1-5") // 每天14点执行一次 除了星期六和星期天
    public void task2() {

        // 检查实验室是否有任务 如果没有新增任务
        checkLaboratoriesSchedule();
        // 根据时间降序，获取到最新的一条数据
        CollectTemperatureHumidity collectTemperatureHumidity = collectTemperatureHumidityMapper.selectOne(Wrappers.<CollectTemperatureHumidity>lambdaQuery()
                .orderByDesc(CollectTemperatureHumidity::getCollectTime)
                .last("limit 1"));
        // 获取所有的实验室
        List<CollectLaboratory> collectLaboratories = collectLaboratoryMapper.selectList(null);
        // 遍历获取实验室编号 获取温湿度 保存在对应的
        for (CollectLaboratory collectLaboratory : collectLaboratories) {
            // 实验室编号
            int labNumber = Integer.parseInt(collectLaboratory.getDeviceCode());
            // 根据实验室编号拼接对应的湿度和温度字段
            String temperatureField = "code" + labNumber + "Temperature";
            String humidityField = "code" + labNumber + "Humidity";
            // 获取温度
            Double temperature = getFieldValue(collectTemperatureHumidity, temperatureField) != null ? (Double) getFieldValue(collectTemperatureHumidity, temperatureField) : Double.valueOf(00.0);
            // 获取湿度
            Double humidity = getFieldValue(collectTemperatureHumidity, humidityField) != null ? (Double) getFieldValue(collectTemperatureHumidity, humidityField) : Double.valueOf(00.0);
            // 查询试验区域
            FeTempHumDate feTempHumDate = feTempHumDateMapper.selectOne(Wrappers.<FeTempHumDate>lambdaQuery()
                    .eq(FeTempHumDate::getMonthDate, formattedDate)
                    .eq(FeTempHumDate::getTestAreaName, collectLaboratory.getLaboratory())
                    .orderByDesc(FeTempHumDate::getCreateTime)
                    .last("limit 1"));
            Integer dateId = feTempHumDate.getDateId();
            // 查询今天是否有温湿度记录
            FeTempHumRecord feTempHumRecord = record(dateId);
            // 如果没有查询结果就说明上午没有记录
            if (feTempHumRecord == null) {
                // 创建今天温湿度记录对象 记录下午温度湿度
                FeTempHumRecord newFeTempHumRecord = new FeTempHumRecord();
                newFeTempHumRecord.setAfternoonTime(LocalTime.now());
                newFeTempHumRecord.setAfternoonTemp(temperature.toString());
                newFeTempHumRecord.setAfternoonHum(humidity.toString());
                newFeTempHumRecord.setDateId(dateId);
                feTempHumRecordMapper.insert(newFeTempHumRecord);
                continue;
            }

            // 查询出来就代表今天记录过 再判断下午有记录过没有
            // 温度是否记录过
            if (feTempHumRecord.getAfternoonTemp() == null) {
                // 没有就自动记录
                feTempHumRecord.setAfternoonTemp(temperature.toString());
                // 修改记录时间
                feTempHumRecord.setAfternoonTime(LocalTime.now());
            }
            // 湿度是否记录过
            if (feTempHumRecord.getAfternoonHum() == null) {
                // 没有就自动记录
                feTempHumRecord.setAfternoonHum(humidity.toString());
                // 修改记录时间
                feTempHumRecord.setAfternoonTime(LocalTime.now());
            }
            feTempHumRecordMapper.updateById(feTempHumRecord);

            // 判断温湿度是否超过, 超过提醒
            if (feTempHumDate.getRegistrarUserId() != null) {
                if (temperature < collectLaboratory.getTemperatureLowest() || temperature > collectLaboratory.getTemperatureHighest() ||
                        humidity< collectLaboratory.getHumidityLowest() || humidity > collectLaboratory.getHumidityHighest()) {
                    // 发送企业微信通知
                    threadPoolTaskExecutor.execute(() -> {
                        // 查询接收人
                        User personnel = userMapper.selectById(feTempHumDate.getRegistrarUserId());

                        String message = "";
                        message += "下午实验室温度或湿度超过指定区间, 请去查看";
                        message += "\n请去资源管理-设施和环境温度一温湿度记录";
                        message += StrUtil.format("\n实验室区域: {}", feTempHumDate.getTestAreaName());
                        //发送企业微信消息通知
                        try {
                            WxCpUtils.inform(personnel.getAccount(), message, null);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                }

                if (feTempHumDate.getTestAreaName().equals("恒温二")) {
                    // 判断上下午温差是否超过1
                    if (temperature - Double.valueOf(feTempHumRecord.getMorningTemp()) > 1) {
                        // 发送企业微信通知
                        threadPoolTaskExecutor.execute(() -> {
                            // 查询接收人
                            User personnel = userMapper.selectById(feTempHumDate.getRegistrarUserId());

                            String message = "";
                            message += "恒温二实验室上下温差超过 1℃, 请去查看";
                            message += "\n请去资源管理-设施和环境温度一温湿度记录";
                            message += StrUtil.format("\n实验室区域: {}", feTempHumDate.getTestAreaName());
                            //发送企业微信消息通知
                            try {
                            WxCpUtils.inform(personnel.getAccount(), message, null);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                }
            }
        }
    }

    /**
     * 星期天执行添加空白数据
     * 获取温度湿度
     */
//    @Scheduled(cron = "0/5 * * * * *")
    @Scheduled(cron = "0 0 9 * * 7") //
    public void task3() {
        checkLaboratoriesSchedule();

        // 获取所有的实验室
        List<CollectLaboratory> collectLaboratories = collectLaboratoryMapper.selectList(null);
        // 遍历获取实验室编号 获取温湿度 保存在对应的
        for (CollectLaboratory collectLaboratory : collectLaboratories) {
            // 查询试验区域
            FeTempHumDate feTempHumDate = feTempHumDateMapper.selectOne(Wrappers.<FeTempHumDate>lambdaQuery()
                    .eq(FeTempHumDate::getMonthDate, formattedDate)
                    .eq(FeTempHumDate::getTestAreaName, collectLaboratory.getLaboratory())
                    .orderByDesc(FeTempHumDate::getCreateTime)
                    .last("limit 1"));
            Integer dateId = feTempHumDate.getDateId();
            // 判断今天是否有温湿度记录
            // 获取今天的日期
            LocalDate today = LocalDate.now();
            // 查询今天是否有温湿度记录
            Long count = feTempHumRecordMapper.selectCount(Wrappers.<FeTempHumRecord>lambdaQuery()
                    // 哪个实验室
                    .eq(FeTempHumRecord::getDateId, dateId)
                    // 记录时间在今天之内
                    .eq(FeTempHumRecord::getRecordDate, today));
            // 查询出来就代表今天记录过了就不记录
            if (!count.equals(0L)) {
                // 有温度湿度记录就不记录
                continue;
            }

            // 创建今天温湿度记录对象 记录上午温度湿度
            FeTempHumRecord newFeTempHumRecord = new FeTempHumRecord();
            newFeTempHumRecord.setRecordDate(LocalDate.now());
            newFeTempHumRecord.setDateId(dateId);
            feTempHumRecordMapper.insert(newFeTempHumRecord);
        }
    }

    /**
     * 扫描实验室温度记录判断是否有没有确认的温度信息
     *
     */
//    @Scheduled(cron = "0/5 * * * * *")
    @Scheduled(cron = "0 0 16 * * 1-6") // 每天15点执行一次 除了星期天
    public void affirmTempHum() {
        // 查询有日期但是未填写名字的
        List<FeTempHumRecordDto> feTempHumRecordList = feTempHumRecordMapper.selectNoaffirm(LocalDate.now());

        for (FeTempHumRecordDto feTempHumRecord : feTempHumRecordList) {

            // 发送企业微信通知
            threadPoolTaskExecutor.execute(() -> {
                // 查询接收人
                User personnel = userMapper.selectById(feTempHumRecord.getRegistrarUserId());

                String message = "";
                message += "温湿度记录确认通知";
                message += "\n请去资源管理-设施和环境温度一温湿度记录";
                message += StrUtil.format("\n实验室区域: {}", feTempHumRecord.getTestAreaName());
                //发送企业微信消息通知
                try {
                    WxCpUtils.inform(personnel.getAccount(), message, null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

        }

    }


    /**
     * 根据实验室编号获取温度湿度
     *
     * @param collectTemperatureHumidity 温度湿度数据
     * @param fieldName                  字段名
     * @return 字段值
     */
    public static Object getFieldValue(CollectTemperatureHumidity collectTemperatureHumidity, String fieldName) {
        try {
            Field field = collectTemperatureHumidity.getClass().getDeclaredField(fieldName);
            field.setAccessible(true); // 设置可以访问私有字段
            return field.get(collectTemperatureHumidity); // 获取字段的值
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
