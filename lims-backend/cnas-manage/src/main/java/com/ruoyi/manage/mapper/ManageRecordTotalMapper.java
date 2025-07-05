package com.ruoyi.manage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.manage.pojo.ManageRecordTotal;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 外来文件确认记录总历史记录表 Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-11-12 10:30:08
 */
public interface ManageRecordTotalMapper extends BaseMapper<ManageRecordTotal> {

    IPage<ManageRecordTotal> pageProcessTotaldeal(Page page, @Param("ew") QueryWrapper<ManageRecordTotal> queryWrappers);
}
