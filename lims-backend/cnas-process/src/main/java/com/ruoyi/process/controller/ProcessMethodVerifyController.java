package com.ruoyi.process.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.process.dto.ProcessMethodVerifyDto;
import com.ruoyi.process.pojo.ProcessMethodVerify;
import com.ruoyi.process.pojo.ProcessMethodVerifyMethodFile;
import com.ruoyi.process.service.ProcessMethodVerifyMethodFileService;
import com.ruoyi.process.service.ProcessMethodVerifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 标准方法验证
 *
 * @author zhuo
 * @since 2024-11-05
 */
@RestController
@RequestMapping("/processMethodVerify")
@Api(tags = "标准方法验证")
@AllArgsConstructor
public class ProcessMethodVerifyController {

    @Resource
    private ProcessMethodVerifyService processMethodVerifyService;

    @Resource
    private ProcessMethodVerifyMethodFileService processMethodVerifyMethodFileService;



    /**
     * 标准查新列表
     * @return
     */
    @ApiOperation(value = "标准方法更新验证列表")
    @GetMapping("/pagesMethodVerify")
    public Result<IPage<ProcessMethodVerify>> pagesMethodVerify(ProcessMethodVerifyDto methodVerifyDto,Page page) throws Exception {
        return Result.success(processMethodVerifyService.pagesMethodVerify(page, methodVerifyDto));
    }

    /**
     * 新增标准方法验证
     * @param methodVerifyDto
     * @return
     */
    @ApiOperation(value = "新增标准方法验证")
    @PostMapping("/addMethodVerify")
    public Result addMethodVerify(@RequestBody ProcessMethodVerifyDto methodVerifyDto ) {
        return Result.success(processMethodVerifyService.addMethodSearchNew(methodVerifyDto));
    }

    /**
     * 查询标准方法验证详情
     * @param methodVerifyId
     * @return
     */
    @ApiOperation(value = "查询标准方法验证详情")
    @GetMapping("/getMethodVerifyOne")
    public Result<ProcessMethodVerifyDto> getMethodVerifyOne(Integer methodVerifyId) {
        return Result.success(processMethodVerifyService.getMethodVerifyOne(methodVerifyId));
    }


    /**
     * 修改标准方法验证
     * @param methodVerifyDto
     * @return
     */
    @ApiOperation(value = "修改标准方法验证")
    @PostMapping("/updateMethodVerify")
    public Result updateMethodVerify(@RequestBody ProcessMethodVerifyDto methodVerifyDto ) {
        return Result.success(processMethodVerifyService.updateMethodVerify(methodVerifyDto));
    }

    /**
     * 删除标准方法更新验证
     * @param methodVerifyId
     * @return
     */
    @ApiOperation(value = "删除标准方法更新验证")
    @DeleteMapping("/delMethodVerify")
    public Result delMethodVerify(Integer methodVerifyId){
        return Result.success(processMethodVerifyService.delMethodVerify(methodVerifyId));
    }

    /**
     * 导出标准方法更新验证
     * @param methodVerifyId 标准方法验证id
     */
    @ApiOperation(value = "导出标准方法更新验证")
    @GetMapping("/exportMethodVerify")
    public void exportMethodVerify(Integer methodVerifyId, HttpServletResponse response){
        processMethodVerifyService.exportMethodVerify(methodVerifyId, response);
    }

    /**
     * 验证确认
     * @param methodVerifyId
     * @return
     */
    @ApiOperation(value = "验证确认")
    @GetMapping("/methodVerifyAffirm")
    public Result methodVerifyAffirm(Integer methodVerifyId) {
        return Result.success(processMethodVerifyService.methodVerifyAffirm(methodVerifyId));
    }

    /**
     * 方法验证新增原始记录
     * @param methodVerifyId
     * @param file
     * @return
     */
    @ApiOperation(value = "方法验证新增原始记录")
    @PostMapping("/uploadVerifyMethodFile")
    public Result<?> uploadVerifyMethodFile(Integer methodVerifyId, MultipartFile file) {
        return Result.success(processMethodVerifyService.uploadVerifyMethodFile(methodVerifyId, file));
    }


    /**
     * 标准方法更新验证原始记录列表
     * @return
     */
    @ApiOperation(value = "标准方法更新验证原始记录列表")
    @GetMapping("/getVerifyMethodFileList")
    public Result<List<ProcessMethodVerifyMethodFile>> getVerifyMethodFileList(Integer methodVerifyId){
        return Result.success(processMethodVerifyService.getVerifyMethodFileList(methodVerifyId));
    }

    /**
     * 标准方法删除验证原始记录列表
     * @return
     */
    @ApiOperation(value = "标准方法删除验证原始记录列表")
    @DeleteMapping("/delVerifyMethodFileList")
    public Result delVerifyMethodFileList(Integer methodFileId){
        return Result.success(processMethodVerifyMethodFileService.removeById(methodFileId));
    }


}

