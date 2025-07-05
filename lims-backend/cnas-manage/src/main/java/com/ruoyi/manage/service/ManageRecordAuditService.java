package com.ruoyi.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.manage.pojo.ManageRecordAudit;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 文件修订申请审批记录 服务类
 * </p>
 *
 * @author
 * @since 2024-11-14 10:29:18
 */
public interface ManageRecordAuditService extends IService<ManageRecordAudit> {

    IPage<ManageRecordAudit> pageManageRecordAudit(Page page, ManageRecordAudit manageRecordAudit);

    int addManageRecordAudit(ManageRecordAudit manageRecordAudit);

    int doManageRecordAudit(ManageRecordAudit manageRecordAudit);

    int ratifyManageRecordAudit(Integer id);

    String exportOutManageRecordAudit(ManageRecordAudit manageRecordAudit, HttpServletResponse response);
}
