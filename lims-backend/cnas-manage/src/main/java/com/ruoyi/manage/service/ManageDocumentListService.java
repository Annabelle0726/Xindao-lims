package com.ruoyi.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.manage.pojo.ManageDocumentList;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文件清单
 服务类
 * </p>
 *
 * @author
 * @since 2024-11-08 11:08:11
 */
public interface ManageDocumentListService extends IService<ManageDocumentList> {

    IPage<ManageDocumentList> pageManageDocumentList(Page page, ManageDocumentList manageDocumentList);

    int uploadFile(Integer id, MultipartFile file);

    void importExcel(List<ManageDocumentList> list);

}
