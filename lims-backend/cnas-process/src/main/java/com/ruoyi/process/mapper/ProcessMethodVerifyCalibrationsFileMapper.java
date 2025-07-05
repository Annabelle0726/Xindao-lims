package com.ruoyi.process.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.process.dto.ProcessMethodVerifyCalibrationsFileDto;
import com.ruoyi.process.pojo.ProcessMethodVerifyCalibrationsFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 标准方法验证设备校准附件表
 *
 * @author zhuo
 * @since 2024-11-12
 */
@Mapper
public interface ProcessMethodVerifyCalibrationsFileMapper extends BaseMapper<ProcessMethodVerifyCalibrationsFile> {

    /**
     * 查询校准证书附件表
     * @param methodVerifyId
     * @return
     */
    List<ProcessMethodVerifyCalibrationsFileDto> selectCalibrationsFileList(@Param("methodVerifyId") Integer methodVerifyId);

    /**
     * 设备id查询校准证书
     * @param deviceId
     * @return
     */
    String selectCalibrationsFile(@Param("deviceId") Integer deviceId);
}

