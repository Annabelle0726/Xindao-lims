package com.ruoyi.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.manage.pojo.ManageDocumentControlled;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 文件受控 服务类
 * </p>
 *
 * @author
 * @since 2024-11-08 02:54:44
 */
public interface ManageDocumentControlledService extends IService<ManageDocumentControlled> {

    IPage<ManageDocumentControlled> pageManageDocumentControlled(Page page, ManageDocumentControlled manageDocumentControlled);

    int addManageDocumentControlled(ManageDocumentControlled manageDocumentControlled);

    int delManageDocumentControlled(Long id);

    ManageDocumentControlled getManageDocumentControlled(Long id);

    int doManageDocumentControlled(ManageDocumentControlled manageDocumentControlled);

    int checkManageDocumentControlled(ManageDocumentControlled manageDocumentControlled);

    void checkManageDocumentControlledPdf(Long id, HttpServletResponse response) throws Exception ;
}
