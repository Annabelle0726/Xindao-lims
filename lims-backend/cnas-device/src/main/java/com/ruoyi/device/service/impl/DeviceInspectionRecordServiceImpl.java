package com.ruoyi.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.device.dto.DeviceInspectionRecordDto;
import com.ruoyi.device.mapper.DeviceInspectionRecordMapper;
import com.ruoyi.device.mapper.DeviceMapper;
import com.ruoyi.device.pojo.Device;
import com.ruoyi.device.pojo.DeviceInspectionRecord;
import com.ruoyi.device.pojo.DeviceInspectionRecordDetails;
import com.ruoyi.device.service.DeviceInspectionRecordDetailsService;
import com.ruoyi.device.service.DeviceInspectionRecordService;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import com.ruoyi.inspect.util.UserUtils;
import com.ruoyi.system.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 设备点检记录表 服务实现类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 04:25:14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DeviceInspectionRecordServiceImpl extends ServiceImpl<DeviceInspectionRecordMapper, DeviceInspectionRecord> implements DeviceInspectionRecordService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private DeviceInspectionRecordDetailsService deviceInspectionRecordDetailsService;
    @Resource
    private DeviceInspectionRecordMapper deviceInspectionRecordMapper;

    /**
     * 分页查询设备点检记录
     *
     * @param page 当前页码
     */
    @Override
    public Result<IPage<DeviceInspectionRecord>> getDeviceInspectionRecordByPage(Page page, DeviceInspectionRecordDto deviceInspectionRecordDto) {
        IPage<DeviceInspectionRecord> iPage = deviceInspectionRecordMapper.selectDeviceParameterPage(page, QueryWrappers.queryWrappers(deviceInspectionRecordDto));
        return Result.success(iPage);
    }


    /**
     * 查询点检详情
     * @param inspectionRecordId
     * @return
     */
    @Override
    public Result getDeviceInspectionRecord(Integer inspectionRecordId) {
        DeviceInspectionRecord deviceInspectionRecord = baseMapper.selectById(inspectionRecordId);
        DeviceInspectionRecordDto dto = new DeviceInspectionRecordDto();
        BeanUtils.copyProperties(deviceInspectionRecord, dto);
        List<DeviceInspectionRecordDetails> list = deviceInspectionRecordDetailsService.list(Wrappers.<DeviceInspectionRecordDetails>lambdaQuery().eq(DeviceInspectionRecordDetails::getInspectionRecordId, inspectionRecordId));
        dto.setDetails(list);
        return Result.success(dto);
    }

    /**
     * 新增设备点检记录
     *
     * @param deviceInspectionRecord 设备点检记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addDeviceInspectionRecord(DeviceInspectionRecordDto deviceInspectionRecord) {

        Integer userId = SecurityUtils.getUserId().intValue();
        User user = userMapper.selectById(userId);
        deviceInspectionRecord.setRecorderId(userId);
        deviceInspectionRecord.setRecorder(user.getName());

        // 查询复核人id
        if (deviceInspectionRecord.getReviewerId() != null) {
            User reviewUser = userMapper.selectById(deviceInspectionRecord.getReviewerId());
            deviceInspectionRecord.setReviewer(reviewUser.getName());
        }
        this.saveOrUpdate(deviceInspectionRecord);
        if (CollectionUtils.isNotEmpty(deviceInspectionRecord.getDetails())) {
            for (DeviceInspectionRecordDetails detail : deviceInspectionRecord.getDetails()) {
                detail.setInspectionRecordId(deviceInspectionRecord.getInspectionRecordId());
            }
            deviceInspectionRecordDetailsService.saveBatch( deviceInspectionRecord.getDetails());
        }

        return Result.success();
    }


    /**
     * 修改设备点检记录
     *
     * @param deviceInspectionRecord 设备点检记录
     */
    @Override
    public Result updateInspectionRecordAndDetails(DeviceInspectionRecordDto deviceInspectionRecord) {
        // 查询复核人id
        if (deviceInspectionRecord.getReviewerId() != null) {
            User reviewUser = userMapper.selectById(deviceInspectionRecord.getReviewerId());
            deviceInspectionRecord.setReviewer(reviewUser.getName());
        }
        this.saveOrUpdate(deviceInspectionRecord);
        deviceInspectionRecordDetailsService.remove(Wrappers.<DeviceInspectionRecordDetails>lambdaQuery().eq(DeviceInspectionRecordDetails::getInspectionRecordId, deviceInspectionRecord.getInspectionRecordId()));
        if (CollectionUtils.isNotEmpty(deviceInspectionRecord.getDetails())) {
            for (DeviceInspectionRecordDetails detail : deviceInspectionRecord.getDetails()) {
                detail.setInspectionRecordId(deviceInspectionRecord.getInspectionRecordId());
            }
            deviceInspectionRecordDetailsService.saveBatch( deviceInspectionRecord.getDetails());
        }
        return Result.success();
    }

    /**
     * 删除设备点检记录
     *
     * @param deviceInspectionRecord 设备点检记录
     */
    @Override
    public Result deleteDeviceInspectionRecordOrDetails(DeviceInspectionRecordDto deviceInspectionRecord) {
        this.removeById(deviceInspectionRecord);
        deviceInspectionRecordDetailsService.remove(Wrappers.<DeviceInspectionRecordDetails>lambdaQuery().eq(DeviceInspectionRecordDetails::getInspectionRecordId, deviceInspectionRecord.getInspectionRecordId()));
        return Result.success();
    }

    /**
     * 复核点检记录
     * @param dto
     * @return
     */
    @Override
    public Result reviewDeviceInspectionRecord(DeviceInspectionRecordDto dto) {
        LambdaUpdateWrapper<DeviceInspectionRecord> wrapper = Wrappers.<DeviceInspectionRecord>lambdaUpdate()
                .eq(DeviceInspectionRecord::getInspectionRecordId, dto.getInspectionRecordId())
                .set(DeviceInspectionRecord::getStatus, dto.getStatus())
                .set(DeviceInspectionRecord::getReviewerRemark, dto.getReviewerRemark());

        // 为0清除审核人
        if (dto.getStatus().equals(0)) {
            wrapper.set(DeviceInspectionRecord::getReviewerId, null)
                    .set(DeviceInspectionRecord::getReviewer, null);
        }
        this.update(wrapper);

        return Result.success();
    }

    /**
     * 导出设备点检记录
     *
     * @param deviceInspectionRecordId 设备点检记录id
     * @param response                 响应
     */
    @Override
    public Result exportDeviceInspectionRecord(Integer deviceInspectionRecordId, HttpServletResponse response) {
        DeviceInspectionRecord deviceInspectionRecord = baseMapper.selectById(deviceInspectionRecordId);
        DeviceInspectionRecordDto deviceInspectionRecordDto = new DeviceInspectionRecordDto();
        BeanUtils.copyProperties(deviceInspectionRecord, deviceInspectionRecordDto);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        deviceInspectionRecordDto.setTestDateString(deviceInspectionRecord.getTestDate() == null ? null : deviceInspectionRecord.getTestDate().format(dateFormatter));

        List<DeviceInspectionRecordDetails> deviceInspectionRecordDetailsList = deviceInspectionRecordDetailsService.list(Wrappers.<DeviceInspectionRecordDetails>lambdaQuery().eq(DeviceInspectionRecordDetails::getInspectionRecordId, deviceInspectionRecordId));

        Integer deviceId = deviceInspectionRecord.getDeviceId();

        Device device = deviceMapper.selectById(deviceId);

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/word/device-inspection-record.docx");
        Configure configure = Configure.builder()
                .bind("deviceInspectionRecordDetailsList", new HackLoopTableRenderPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("deviceInspectionRecord", deviceInspectionRecordDto);
                    put("deviceInspectionRecordDetailsList", deviceInspectionRecordDetailsList);
                    put("device", device);
                    put("recorderUrl", UserUtils.getFinalUserSignatureUrl(deviceInspectionRecordDto.getRecorderId()));
                    put("reviewerUrl", UserUtils.getFinalUserSignatureUrl(deviceInspectionRecordDto.getReviewerId()));
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    device.getDeviceName() + "点检记录", "UTF-8");
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
        return Result.success();
    }
}
