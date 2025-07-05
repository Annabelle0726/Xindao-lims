package com.ruoyi.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.manage.pojo.ManageRecordCancel;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 作废文件销魂记录 服务类
 * </p>
 *
 * @author
 * @since 2024-11-13 01:27:22
 */
public interface ManageRecordCancelService extends IService<ManageRecordCancel> {

    IPage<ManageRecordCancel> pageManageRecordCancel(Page page, ManageRecordCancel manageRecordCancel);

    int ratifyManageRecordCancel(Integer id, String ratifyState);

    String exportOutManageRecordCancel(ManageRecordCancel manageRecordCancel, HttpServletResponse response);

    int exportInManageRecordCancel(MultipartFile file);
}
