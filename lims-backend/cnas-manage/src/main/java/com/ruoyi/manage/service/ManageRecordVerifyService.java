package com.ruoyi.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.manage.pojo.ManageRecordVerify;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * <p>
 * 外来文件确认记录 服务类
 * </p>
 *
 * @author
 * @since 2024-11-12 10:29:44
 */
public interface ManageRecordVerifyService extends IService<ManageRecordVerify> {

    IPage<ManageRecordVerify> pageManageRecordVerify(Page page, ManageRecordVerify manageRecordVerify);

    int addManageRecordVerify(ManageRecordVerify manageRecordVerify);

    int delManageRecordVerify(Integer id);

    int exportManageRecordVerify(MultipartFile file);
}
