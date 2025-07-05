package com.ruoyi.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.manage.pojo.ManageRecordIntervalsTotal;

import java.util.Map;

/**
 * <p>
 * 文件定期审查记录总历史记录表 服务类
 * </p>
 *
 * @author
 * @since 2024-11-15 01:12:11
 */
public interface ManageRecordIntervalsTotalService extends IService<ManageRecordIntervalsTotal> {

    IPage<ManageRecordIntervalsTotal> pageManageRecordIntervalsTotal(Page page, ManageRecordIntervalsTotal manageRecordIntervalsTotal);

    int submitManageRecordIntervalsTotal(Integer id);

    int ratifyManageRecordIntervalsTotal(Integer id, String ratifyState);
}
