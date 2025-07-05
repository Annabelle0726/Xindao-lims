package com.ruoyi.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.manage.pojo.ManageRecordCheck;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 文件审批记录(含修订后再次审批记录) 服务类
 * </p>
 *
 * @author 
 * @since 2024-11-12 02:31:36
 */
public interface ManageRecordCheckService extends IService<ManageRecordCheck> {

    IPage<ManageRecordCheck> pageManageRecordCheck(Page page, ManageRecordCheck manageRecordCheck);

    int checkManageRecordCheck(Integer id, String checkState);

    int ratifyManageRecordCheck(Integer id, String ratifyState);

    String exportOutManageRecordCheck(ManageRecordCheck manageRecordCheck, HttpServletResponse response);

    int exportInManageRecordCheck(MultipartFile file);

}
