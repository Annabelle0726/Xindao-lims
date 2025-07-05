package com.ruoyi.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.manage.pojo.ManageRecordIntervals;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 文件定期审查记录 服务类
 * </p>
 *
 * @author
 * @since 2024-11-13 10:54:31
 */
public interface ManageRecordIntervalsService extends IService<ManageRecordIntervals> {

    IPage<ManageRecordIntervals> pageManageRecordIntervals(Page page, ManageRecordIntervals manageRecordIntervals);

    String exportOutManageRecordIntervals(ManageRecordIntervals manageRecordIntervals, HttpServletResponse response);

    int exportInManageRecordIntervals(MultipartFile file);

    int addManageRecordIntervals(ManageRecordIntervals manageRecordIntervals);

    int delManageRecordIntervals(Integer id);
}
