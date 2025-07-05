package com.ruoyi.personnel.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.FileSaveUtil;
import com.ruoyi.personnel.dto.PersonPostAuthorizationRecordDto;
import com.ruoyi.personnel.pojo.PersonPostAuthorizationRecord;
import com.ruoyi.personnel.service.PersonPostAuthorizationRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 任职授权记录 前端控制器
 * </p>
 *
 * @author
 * @since 2024-10-09 10:48:17
 */
@Api(tags = "人员 - 任职授权记录")
@RestController
@RequestMapping("/personPostAuthorizationRecord")
public class PersonPostAuthorizationRecordController {

    @Autowired
    private PersonPostAuthorizationRecordService personPostAuthorizationRecordService;

    @ApiOperation(value = "新增/更新 任职授权记录")
    @PostMapping("/addOrUpdatePersonPostAuthorizationRecord")
    public Result<?> addOrUpdatePersonPostAuthorizationRecord(@RequestBody PersonPostAuthorizationRecord personRewardPunishmentRecord) {
        personPostAuthorizationRecordService.saveOrUpdate(personRewardPunishmentRecord);
        return Result.success();
    }

    @ApiOperation(value = "删除 任职授权记录")
    @DeleteMapping("/deletePersonPostAuthorizationRecord")
    public Result<?> deletePersonPostAuthorizationRecord(@RequestParam("id") Integer id) {
        // 删除文件
        PersonPostAuthorizationRecord postAuthorizationRecord = personPostAuthorizationRecordService.getById(id);
        FileSaveUtil.DeleteFile(postAuthorizationRecord.getSystemFileName());
        // 删除数据
        personPostAuthorizationRecordService.removeById(id);
        return Result.success();
    }


    @ApiOperation(value = "查询 任职授权记录")
    @GetMapping("/PersonPostAuthorizationRecordPage")
    public Result<IPage<PersonPostAuthorizationRecordDto>> PersonPostAuthorizationRecordPage(Page page,
                                                                                             Integer departLimsId,
                                                                                             Integer userId,
                                                                                             String userName) {
        return Result.success(personPostAuthorizationRecordService.personPostAuthorizationRecordPage(page, departLimsId, userId, userName));
    }

    @ApiOperation(value = "导出岗位职业资格证书")
    @GetMapping("/exportPersonPostAuthorizationRecord")
    public void exportPersonPostAuthorizationRecord(Integer id, HttpServletResponse response){
        personPostAuthorizationRecordService.exportPersonPostAuthorizationRecord(id,response);
    }
}
