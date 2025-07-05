package com.ruoyi.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.manage.pojo.ManageReviewProgram;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-11-09 03:05:42
 */
public interface ManageReviewProgramMapper extends BaseMapper<ManageReviewProgram> {


    IPage<ManageReviewProgram> page(Page page, @Param("startTime") String startTime,@Param("endTime") String endTime,@Param("judgingLocation") String judgingLocation);
}
