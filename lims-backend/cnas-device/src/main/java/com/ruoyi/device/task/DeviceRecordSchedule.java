package com.ruoyi.device.task;

import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.WxCpUtils;
import com.ruoyi.device.dto.DeviceRecordDto;
import com.ruoyi.device.mapper.DeviceMapper;
import com.ruoyi.device.mapper.DeviceRecordMapper;
import com.ruoyi.device.pojo.Device;
import com.ruoyi.device.pojo.DeviceRecord;
import com.ruoyi.system.mapper.UserMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 设备使用记录提醒
 */
@Component
public class DeviceRecordSchedule {

    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private DeviceRecordMapper deviceRecordMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 提醒填写设备使用记录
     */
//    @Scheduled(cron = "0/5 * * * * *")
    @Scheduled(cron = "0 0 9 * * 6") // 每周六执行一次
    public void task1() {
        // 查询未填写的设备使用记录
        List<DeviceRecordDto> deviceRecords = deviceRecordMapper.selectNotFilled();
        Map<Integer, List<DeviceRecordDto>> userPersonIdMap = deviceRecords.stream().collect(Collectors.groupingBy(DeviceRecord::getUsePersonId));
        userPersonIdMap.forEach((userId, recordList) -> {
            threadPoolTaskExecutor.execute(() -> {
                // 企业微信通知填写设备使用记录
                User user = userMapper.selectById(userId);
                String message = "";
                message += "设备使用记录未填写提醒通知";
                for (DeviceRecordDto deviceRecord : recordList) {
                    message += "\n设备名称编号: " + deviceRecord.getDeviceName() + "-" + deviceRecord.getManagementNumber();
                    message += "\n委托编号: " + deviceRecord.getSampleCode();
                    message += "\n";
                }
                message += "\n请去填写设备使用记录";

                //发送企业微信消息通知
                try {
                    WxCpUtils.inform(user.getAccount(), message, null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            });
        });
    }


    /**
     * 每天9点执行一次 除了星期天
     * 判断是否有设备到达校准有效期
     */
//    @Scheduled(cron = "0/10 * * * * *")
    @Scheduled(cron = "0 0 9 * * 1-6") // 每天9点执行一次 除了星期天
    public void task2() {
        // 查询到达校准有效期的设备-提前5天
        List<Device> deviceList = deviceMapper.selectOverdueDevice();
        Map<Integer, List<Device>> userPersonIdMap = deviceList.stream().collect(Collectors.groupingBy(Device::getEquipmentManager));
        userPersonIdMap.forEach((userId, recordList) -> {
            threadPoolTaskExecutor.execute(() -> {
                // 企业微信通知填写设备使用记录
                User user = userMapper.selectById(userId);
                String message = "";
                message += "设备到达校准有效期";
                for (Device deviceRecord : recordList) {
                    message += "\n设备名称: " + deviceRecord.getDeviceName();
                    message += "\n设备编号: " + deviceRecord.getManagementNumber();
                    message += "\n校准有效期: " + deviceRecord.getActivationDate();
                    message += "\n";
                }
                message += "\n请去进行设备校准";

                //发送企业微信消息通知
                try {
                    WxCpUtils.inform(user.getAccount(), message, null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            });
        });
    }

}
