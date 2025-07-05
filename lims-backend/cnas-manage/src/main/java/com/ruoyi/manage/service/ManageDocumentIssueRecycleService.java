package com.ruoyi.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.manage.dto.ManageDocumentIssueRecycleDto;
import com.ruoyi.manage.pojo.ManageDocumentIssueRecycle;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 文件发放回收 服务类
 * </p>
 *
 * @author 
 * @since 2024-11-09 09:18:24
 */
public interface ManageDocumentIssueRecycleService extends IService<ManageDocumentIssueRecycle> {

    IPage<ManageDocumentIssueRecycleDto> pageManageDocumentIssueRecycle(Page page, ManageDocumentIssueRecycleDto manageDocumentIssueRecycleDto);

    ManageDocumentIssueRecycleDto getManageDocumentIssueRecycle(Long id);

    void exportManageDocumentIssueRecycle(ManageDocumentIssueRecycleDto manageDocumentIssueRecycleDto, HttpServletResponse response) throws Exception;

    int checkManageDocumentIssueRecycle(Integer id, String documentState);

    int addManageDocumentIssueRecycle(ManageDocumentIssueRecycle manageDocumentIssueRecycle);

    int doManageDocumentIssueRecycle(ManageDocumentIssueRecycle manageDocumentIssueRecycle);

    int delManageDocumentIssueRecycle(Long id);
}
