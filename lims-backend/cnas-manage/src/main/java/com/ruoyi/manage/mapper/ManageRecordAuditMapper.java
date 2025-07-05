package com.ruoyi.manage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.manage.pojo.ManageRecordAudit;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 文件修订申请审批记录 Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-11-14 10:29:18
 */
public interface ManageRecordAuditMapper extends BaseMapper<ManageRecordAudit> {

    IPage<ManageRecordAudit> pageManageRecordAudit(Page page, @Param("ew") QueryWrapper<ManageRecordAudit> queryWrappers);
}
