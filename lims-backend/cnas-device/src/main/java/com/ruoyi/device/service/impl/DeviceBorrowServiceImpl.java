package com.ruoyi.device.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.numgen.NumberGenerator;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.device.mapper.DeviceBorrowMapper;
import com.ruoyi.device.mapper.DeviceLogMapper;
import com.ruoyi.device.pojo.DeviceBorrow;
import com.ruoyi.device.pojo.DeviceLog;
import com.ruoyi.device.service.DeviceBorrowService;
import com.ruoyi.system.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-09-21 10:53:51
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DeviceBorrowServiceImpl extends ServiceImpl<DeviceBorrowMapper, DeviceBorrow> implements DeviceBorrowService {

    @Resource
    private DeviceBorrowMapper deviceBorrowMapper;

    @Resource
    private DeviceLogMapper deviceLogMapper;

    @Resource
    private UserMapper userMapper;

    @Autowired
    private NumberGenerator<DeviceBorrow> numberGenerator;

    @Override
    public IPage<DeviceBorrow> deviceBorrowPage(Page page, DeviceBorrow deviceBorrow) {
        return deviceBorrowMapper.deviceBorrowPage(page, QueryWrappers.queryWrappers(deviceBorrow));
    }

    @Override
    public int saveDeviceBorrow(DeviceBorrow deviceBorrow) {
        Integer userId = SecurityUtils.getUserId().intValue();
        User user = userMapper.selectById(userId);
        //新增的时候添加新建流程
        if (ObjectUtils.isEmpty(deviceBorrow.getId())) {
            deviceBorrow.setSubmitUser(user.getName());
            String year = new SimpleDateFormat("yy", Locale.CHINESE).format(new Date());
            String month = new SimpleDateFormat("MM", Locale.CHINESE).format(new Date());
            String processNumber = numberGenerator.generateNumberWithPrefix(3, "DG-TC-23FM " + month + "-" + year + month, DeviceBorrow::getProcessNumber);
            deviceBorrow.setProcessNumber(processNumber);
            deviceBorrowMapper.insert(deviceBorrow);
            DeviceLog deviceLog = new DeviceLog();
            deviceLog.setOperator(user.getName());
            deviceLog.setOperationTime(LocalDateTime.now());
            deviceLog.setOperationType("新建");
            deviceLog.setOperationContent("新建流程");
            deviceLog.setRelevanceForm("device_borrow");
            deviceLog.setRelevanceId(deviceBorrow.getId());
            deviceLogMapper.insert(deviceLog);
        } else {
            DeviceBorrow borrow = deviceBorrowMapper.selectById(deviceBorrow.getId());
            deviceBorrowMapper.updateById(deviceBorrow);
            //通过或者驳回增加流程跟踪
            if (deviceBorrow.getNowState().equals("关闭")) {
                DeviceLog deviceLog = new DeviceLog();
                deviceLog.setOperator(user.getName());
                deviceLog.setOperationTime(LocalDateTime.now());
                deviceLog.setOperationType("接收通过");
                deviceLog.setRelevanceForm("device_borrow");
                deviceLog.setRelevanceId(deviceBorrow.getId());
                deviceLogMapper.insert(deviceLog);
            }
            else if (deviceBorrow.getNowState().equals("提交") && borrow.getNowState().equals("接收")) {
                DeviceLog deviceLog = new DeviceLog();
                deviceLog.setOperator(user.getName());
                deviceLog.setOperationTime(LocalDateTime.now());
                deviceLog.setOperationType("接收驳回");
                deviceLog.setRelevanceForm("device_borrow");
                deviceLog.setRelevanceId(deviceBorrow.getId());
                deviceLogMapper.insert(deviceLog);
            }
        }
        return 0;
    }

    @Override
    public DeviceBorrow getDeviceBorrow(Integer id) {
        List<DeviceLog> deviceLogs = deviceLogMapper.selectList(Wrappers.<DeviceLog>lambdaQuery()
                .eq(DeviceLog::getRelevanceForm, "device_borrow")
                .eq(DeviceLog::getRelevanceId, id));
        DeviceBorrow deviceBorrow = deviceBorrowMapper.selectById(id);
        deviceBorrow.setDeviceLogs(deviceLogs);
        return deviceBorrow;
    }

    @Override
    public List<DeviceBorrow> getDeviceBorrowBydeviceId(Integer deviceId) {
        return deviceBorrowMapper.getDeviceBorrowBydeviceId(deviceId);
    }
}
