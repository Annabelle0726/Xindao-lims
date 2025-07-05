package com.ruoyi.process.controller;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.process.dto.ProcessMethodSearchNewArchivedDto;
import com.ruoyi.process.dto.ProcessMethodSearchNewBackupsDto;
import com.ruoyi.process.pojo.ProcessMethodSearchNew;
import com.ruoyi.process.pojo.ProcessMethodSearchNewArchived;
import com.ruoyi.process.pojo.ProcessMethodSearchNewBackups;
import com.ruoyi.process.service.ProcessMethodSearchNewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 标准查新
 *
 * @author zhuo
 * @since 2024-11-04
 */
@Api(tags = "标准查新")
@AllArgsConstructor
@RestController
@RequestMapping("/processMethodSearchNew")
public class ProcessMethodSearchNewController {

    private ProcessMethodSearchNewService processMethodSearchNewService;

    /**
     * 新增标准查新
     * @param processMethodSearchNewList
     * @return
     */
    @ApiOperation(value = "新增标准查新")
    @PostMapping("/addMethodSearchNew")
    public Result addMethodSearchNew(@RequestBody List<ProcessMethodSearchNew> processMethodSearchNewList ) {
        return Result.success(processMethodSearchNewService.addMethodSearchNew(processMethodSearchNewList));
    }

    /**
     * 修改标准查新
     * @param processMethodSearchNew
     * @return
     */
    @ApiOperation(value = "修改标准查新")
    @PostMapping("/updateMethodSearchNew")
    public Result updateMethodSearchNew(@RequestBody ProcessMethodSearchNew processMethodSearchNew ) {
        return Result.success(processMethodSearchNewService.updateById(processMethodSearchNew));
    }

    /**
     * 标准查新列表
     */
    @ApiOperation(value = "标准查新列表")
    @GetMapping("/pageMethodSearchNew")
    public Result<IPage<ProcessMethodSearchNew>> pageMethodSearchNew(ProcessMethodSearchNewBackupsDto processMethodSearchNew,Page page) throws Exception {
        return Result.success(processMethodSearchNewService.pageMethodSearchNew(page, processMethodSearchNew));
    }

    @ApiOperation(value = "标准查新导出")
    @GetMapping("/exportMethodSearchNew")
    public void exportMethodSearchNew(Integer archivedId, HttpServletResponse response) throws Exception {
        processMethodSearchNewService.exportMethodSearchNew(archivedId, response);
    }

    /**
     * 导入标准查新
     * @return
     */
    @ApiOperation(value = "导入标准查新")
    @PostMapping("/importMethodSearchNew")
    public Result importMethodSearchNew(MultipartFile file){
        return Result.success(processMethodSearchNewService.importMethodSearchNew(file));
    }

    /**
     * 新增存档
     * @param archived
     * @return
     */
    @ApiOperation(value = "新增存档")
    @PostMapping("/addSearchNewArchived")
    public Result addSearchNewApprovalProcess(@RequestBody ProcessMethodSearchNewArchived archived) {
        return Result.success(processMethodSearchNewService.addSearchNewArchived(archived));
    }

    /**
     * 新增存档
     * @return
     */
    @ApiOperation(value = "查询存档")
    @GetMapping("/pageSearchNewArchived")
    public Result pageSearchNewArchived(ProcessMethodSearchNewArchivedDto archived,Page page) throws Exception {
        return Result.success(processMethodSearchNewService.pageSearchNewArchived(page, archived));
    }

    /**
     * 标准查新列表
     * @return
     */
    @ApiOperation(value = "查询存档备份列表")
    @GetMapping("/pageSearchNewBackups")
    public Result<IPage<ProcessMethodSearchNewBackups>> pageSearchNewBackups(ProcessMethodSearchNewBackups backups,Page page) throws Exception {
        return Result.success(processMethodSearchNewService.pageSearchNewBackups(page, backups));
    }


    /**
     * 存档批准
     * @return
     */
    @ApiOperation(value = "存档批准")
    @PostMapping("/ratifySearchNewArchivedr")
    public Result ratifySearchNewArchivedr(@RequestBody ProcessMethodSearchNewArchived archived) {
        return Result.success(processMethodSearchNewService.ratifySearchNewArchivedr(archived));
    }
}

