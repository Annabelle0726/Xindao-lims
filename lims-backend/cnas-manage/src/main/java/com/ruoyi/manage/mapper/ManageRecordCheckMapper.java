package com.ruoyi.manage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.manage.pojo.ManageRecordCheck;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 文件审批记录(含修订后再次审批记录) Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-11-12 02:31:36
 */
public interface ManageRecordCheckMapper extends BaseMapper<ManageRecordCheck> {

    IPage<ManageRecordCheck> pageManageRecordCheck(Page page, @Param("ew") QueryWrapper<ManageRecordCheck> queryWrappers);
}
