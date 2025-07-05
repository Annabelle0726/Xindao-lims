package com.ruoyi.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.manage.dto.ManageMeetingDto;
import com.ruoyi.manage.pojo.ManageMeeting;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-11-11 09:33:47
 */
public interface ManageMeetingMapper extends BaseMapper<ManageMeeting> {


    IPage<ManageMeetingDto> page(Page page, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("place") String place);
}
