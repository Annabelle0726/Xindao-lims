package com.ruoyi.device.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.numgen.NumberGenerator;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.device.dto.DeviceInstructionDto;
import com.ruoyi.device.dto.DeviceOperationInstructionDto;
import com.ruoyi.device.mapper.InstructionMapper;
import com.ruoyi.device.pojo.DeviceInstruction;
import com.ruoyi.device.service.DeviceInstructionService;
import com.ruoyi.device.service.DeviceOperationInstructionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * <p>
 * 作业指导书添加受控文件表 服务实现类
 * </p>
 *
 * @author
 * @since 2024-12-04 10:29:18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DeviceInstructionServiceImpl extends ServiceImpl<InstructionMapper, DeviceInstruction> implements DeviceInstructionService {

    @Autowired
    private DeviceOperationInstructionService operationInstructionService;

    @Autowired
    private NumberGenerator<DeviceInstruction> numberGenerator;

    @Override
    public IPage<DeviceOperationInstructionDto> pageByPageQueryOfHomeworkInstructions(Page page, DeviceOperationInstructionDto operationInstructionDto) {
        return baseMapper.pageByPageQueryOfHomeworkInstructions(page, QueryWrappers.queryWrappers(operationInstructionDto));
    }

    @Override
    public void newHomeworkGuidebookAdded(DeviceInstructionDto instructionDto) {
        if (ObjectUtils.isEmpty(instructionDto.getApplicationNumber())) {
            String year = new SimpleDateFormat("yy", Locale.CHINESE).format(new Date());
            String month = new SimpleDateFormat("MM", Locale.CHINESE).format(new Date());
            String day = new SimpleDateFormat("dd", Locale.CHINESE).format(new Date());
            String processNumber = numberGenerator.generateNumberWithPrefix(3, "WJSK" + year + month + day, DeviceInstruction::getApplicationNumber);
            instructionDto.setApplicationNumber(processNumber);
        }
        saveOrUpdate(instructionDto);
        if (ObjectUtils.isNotEmpty(instructionDto.getFeTempHumRecordList())) {
            instructionDto.getFeTempHumRecordList().forEach(i -> {
                i.setInstructionId(instructionDto.getId());
                i.setUploader(SecurityUtils.getUserId().intValue());
                i.setUpdateTime(LocalDateTime.now());
            });
            operationInstructionService.saveOrUpdateBatch(instructionDto.getFeTempHumRecordList());
        }
    }
}
