package com.ruoyi.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.manage.pojo.ManageRecordTotal;

import java.util.Map;

/**
 * <p>
 * 外来文件确认记录总历史记录表 服务类
 * </p>
 *
 * @author
 * @since 2024-11-12 10:30:08
 */
public interface ManageRecordTotalService extends IService<ManageRecordTotal> {

    IPage<ManageRecordTotal> pageManageRecordTotal(Page page, ManageRecordTotal manageRecordTotal);

    int submitManageRecordTotal(Integer id);

    int ratifyManageRecordTotal(Integer id, String ratifyState);
}
