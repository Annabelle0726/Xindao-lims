package com.ruoyi.process.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.process.dto.ProcessMethodSearchNewArchivedDto;
import com.ruoyi.process.pojo.ProcessMethodSearchNewArchived;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 标准查新存档表 Mapper 接口
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-25 05:28:42
 */
public interface ProcessMethodSearchNewArchivedMapper extends BaseMapper<ProcessMethodSearchNewArchived> {

    /**
     * 查询存档
     * @param page
     * @param ew
     * @return
     */
    IPage<ProcessMethodSearchNewArchivedDto> pageSearchNewArchived(Page page, @Param("ew") QueryWrapper<ProcessMethodSearchNewArchivedDto> ew);
}
