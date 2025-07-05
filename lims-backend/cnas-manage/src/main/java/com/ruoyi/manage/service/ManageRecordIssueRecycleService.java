package com.ruoyi.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.manage.pojo.ManageRecordIssueRecycle;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 所有文件(内、外部文件)的发放与回收记录 服务类
 * </p>
 *
 * @author 
 * @since 2024-11-13 09:11:05
 */
public interface ManageRecordIssueRecycleService extends IService<ManageRecordIssueRecycle> {

    IPage<ManageRecordIssueRecycle> pageManageRecordIssueRecycle(Page page, ManageRecordIssueRecycle manageRecordIssueRecycle);

    String exportOutManageRecordIssueRecycle(ManageRecordIssueRecycle manageRecordIssueRecycle, HttpServletResponse response);

    int exportInManageRecordIssueRecycle(MultipartFile file);

    int addManageRecordIssueRecycle(ManageRecordIssueRecycle manageRecordIssueRecycle);

    int doManageRecordIssueRecycle(ManageRecordIssueRecycle manageRecordIssueRecycle);
}
