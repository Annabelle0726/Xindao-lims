package com.ruoyi.manage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.manage.pojo.ManageRecordVerify;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 外来文件确认记录 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-11-12 10:29:44
 */
public interface ManageRecordVerifyMapper extends BaseMapper<ManageRecordVerify> {

    IPage<ManageRecordVerify> pageManageRecordVerify(Page page, @Param("ew") QueryWrapper<ManageRecordVerify> queryWrappers);

}
