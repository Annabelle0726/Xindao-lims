package com.ruoyi.manage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.manage.pojo.ManageRecordCancel;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 作废文件销魂记录 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-11-13 01:27:22
 */
public interface ManageRecordCancelMapper extends BaseMapper<ManageRecordCancel> {

    IPage<ManageRecordCancel> pageManageRecordCancel(Page page, @Param("ew") QueryWrapper<ManageRecordCancel> queryWrappers);
}
