package com.ruoyi.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.manage.pojo.ManageDocumentAlter;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 文件变更 服务类
 * </p>
 *
 * @author 
 * @since 2024-11-11 11:04:01
 */
public interface ManageDocumentAlterService extends IService<ManageDocumentAlter> {

    IPage<ManageDocumentAlter> pageManageDocumentAlter(Page page, ManageDocumentAlter manageDocumentAlter);

    ManageDocumentAlter getManageDocumentAlter(Integer id);

    int addManageDocumentAlter(ManageDocumentAlter manageDocumentAlter);

    int doManageDocumentAlter(ManageDocumentAlter manageDocumentAlter);

    int checkManageDocumentAlter(ManageDocumentAlter manageDocumentAlter);

    void checkManageDocumentAlterPdf(Long id, HttpServletResponse response) throws Exception;

    void exportManageDocumentAlter(ManageDocumentAlter manageDocumentAlter, HttpServletResponse response) throws Exception;

    int delManageDocumentAlter(Integer id);
}
