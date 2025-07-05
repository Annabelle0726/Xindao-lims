package com.ruoyi.process.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.process.pojo.ProcessMethodSearchNewBackups;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 标准查新备份表 Mapper 接口
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-25 05:29:02
 */
public interface ProcessMethodSearchNewBackupsMapper extends BaseMapper<ProcessMethodSearchNewBackups> {

    /**
     * 查询存档备份列表
     * @param page
     * @param ew
     * @return
     */
    IPage<ProcessMethodSearchNewBackups> pageSearchNewBackups(Page page, @Param("ew") QueryWrapper<ProcessMethodSearchNewBackups> ew);
}
