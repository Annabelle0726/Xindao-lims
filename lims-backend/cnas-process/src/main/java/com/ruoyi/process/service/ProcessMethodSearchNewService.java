package com.ruoyi.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.process.dto.ProcessMethodSearchNewArchivedDto;
import com.ruoyi.process.dto.ProcessMethodSearchNewBackupsDto;
import com.ruoyi.process.pojo.ProcessMethodSearchNew;
import com.ruoyi.process.pojo.ProcessMethodSearchNewArchived;
import com.ruoyi.process.pojo.ProcessMethodSearchNewBackups;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 标准查新
 *
 * @author zhuo
 * @since 2024-11-04
 */
public interface ProcessMethodSearchNewService extends IService<ProcessMethodSearchNew> {

    /**
     * 新增标准查新
     * @param processMethodSearchNewList
     * @return
     */
    boolean addMethodSearchNew(List<ProcessMethodSearchNew> processMethodSearchNewList);

    /**
     * 标准查新列表
     * @param processMethodSearchNew
     * @return
     */
    IPage<ProcessMethodSearchNew> pageMethodSearchNew(Page page, ProcessMethodSearchNewBackupsDto processMethodSearchNew);

    /**
     * 标准查新导出
     * @param archivedId
     * @param response
     */
    void exportMethodSearchNew(Integer archivedId, HttpServletResponse response);

    /**
     * 导入标准查新
     * @param file
     * @return
     */
    boolean importMethodSearchNew(MultipartFile file);

    /**
     * 新增标准查新审批流程
     * @param archived
     * @return
     */
    boolean addSearchNewArchived(ProcessMethodSearchNewArchived archived);

    /**
     * 查询存档
     * @param page
     * @param archived
     * @return
     */
    IPage<ProcessMethodSearchNewArchivedDto> pageSearchNewArchived(Page page, ProcessMethodSearchNewArchivedDto archived);

    /**
     * 查询存档备份列表
     * @param page
     * @param backups
     * @return
     */
    IPage<ProcessMethodSearchNewBackups> pageSearchNewBackups(Page page, ProcessMethodSearchNewBackups backups);

    /**
     * 存档批准
     * @param archived
     * @return
     */
    boolean ratifySearchNewArchivedr(ProcessMethodSearchNewArchived archived);
}

