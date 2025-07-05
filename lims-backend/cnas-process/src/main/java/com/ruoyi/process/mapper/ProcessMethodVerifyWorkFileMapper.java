package com.ruoyi.process.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.process.pojo.ProcessMethodVerifyWorkFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 标准方法验证上岗证附件表
 *
 * @author zhuo
 * @since 2024-11-12
 */
@Mapper
public interface ProcessMethodVerifyWorkFileMapper extends BaseMapper<ProcessMethodVerifyWorkFile> {

    /**
     * 根据用户id查询上岗证
     * @param userId
     * @return
     */
    String selectWorkFile(@Param("userId") Integer userId);
}

