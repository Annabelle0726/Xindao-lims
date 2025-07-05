package com.ruoyi.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.manage.pojo.ManageDocumentCancel;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 文件作废 服务类
 * </p>
 *
 * @author 
 * @since 2024-11-09 02:37:35
 */
public interface ManageDocumentCancelService extends IService<ManageDocumentCancel> {

    IPage<ManageDocumentCancel> pageManageDocumentCancel(Page page, ManageDocumentCancel manageDocumentCancel);

    int addManageDocumentCancel(ManageDocumentCancel manageDocumentCancel);

    int checkManageDocumentCancel(Integer id, String state);

    ManageDocumentCancel getManageDocumentCancel(Integer id);

    void exportManageDocumentCancel(ManageDocumentCancel manageDocumentCancel, HttpServletResponse response) throws Exception;

    int delManageDocumentCancel(Integer id);

    int doManageDocumentCancel(ManageDocumentCancel manageDocumentCancel);
}
