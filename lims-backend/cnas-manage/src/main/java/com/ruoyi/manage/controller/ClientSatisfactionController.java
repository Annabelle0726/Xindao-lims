package com.ruoyi.manage.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;
import com.ruoyi.manage.mapper.ClientSatisfactionAnalyseFileMapper;
import com.ruoyi.manage.pojo.ClientSatisfaction;
import com.ruoyi.manage.pojo.ClientSatisfactionAnalyseFile;
import com.ruoyi.manage.service.ClientSatisfactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * 客户满意度
 *
 * @author zhuo
 * @since 2024-11-09
 */
@Api(tags = "客户满意度调查")
@AllArgsConstructor
@RestController
@RequestMapping("/clientSatisfaction")
public class ClientSatisfactionController {

    private ClientSatisfactionService clientSatisfactionService;
    private ClientSatisfactionAnalyseFileMapper clientSatisfactionAnalyseFileMapper;

    /**
     * 客户满意度调查列表
     * @param
     * @return
     */
    @ApiOperation(value = "客户满意度调查列表")
    @GetMapping("/pageClientSatisfaction")
    public Result<IPage<ClientSatisfaction>> pageClientSatisfaction(Page page,ClientSatisfaction clientSatisfaction) throws Exception {
        return Result.success(clientSatisfactionService.pageClientSatisfaction(page, clientSatisfaction));
    }

    /**
     * 客户满意度调查新增
     * @return
     */
    @ApiOperation(value = "客户满意度调查新增")
    @PostMapping("/addClientSatisfaction")
    public Result addClientSatisfaction(@RequestBody ClientSatisfaction clientSatisfaction){
        return Result.success(clientSatisfactionService.save(clientSatisfaction));
    }

    /**
     * 客户满意度调查修改
     * @return
     */
    @ApiOperation(value = "客户满意度调查新增")
    @PostMapping("/updateClientSatisfaction")
    public Result updateClientSatisfaction(@RequestBody ClientSatisfaction clientSatisfaction){
        return Result.success(clientSatisfactionService.updateById(clientSatisfaction));
    }

    /**
     * 客户满意度调查修改
     * @return
     */

    @ApiOperation(value = "客户满意度调查新增")
    @DeleteMapping("/delClientSatisfaction")
    public Result updateClientSatisfaction(Integer clientSatisfactionId){
        return Result.success(clientSatisfactionService.removeById(clientSatisfactionId));
    }

    /**
     * 客户满意度导出
     * @param clientSatisfactionId
     * @param response
     * @return
     */

    @ApiOperation(value = "客户满意导出")
    @GetMapping("/exportWordClientSatisfaction")
    public Result exportWordClientSatisfaction(Integer clientSatisfactionId, HttpServletResponse response){
        clientSatisfactionService.exportWordClientSatisfaction(clientSatisfactionId, response);
        return Result.success();
    }

    /**
     * 确认客户满意度
     * @param clientSatisfaction 要修改客户满意度的状态对象
     * @param userId 修改人id
     */

    @ApiOperation(value = "确认客户满意度")
    @PostMapping("/confirmClientSatisfaction")
    public void confirmClientSatisfaction(@RequestBody ClientSatisfaction clientSatisfaction, Integer userId){
        clientSatisfactionService.confirmClientSatisfaction(clientSatisfaction, userId);
    }

    /**
     * 新增客户分析附件
     * @param file
     * @return
     */

    @ApiOperation(value = "新增户分析附件")
    @PostMapping("/uploadAnalyseFile")
    public Result<?> uploadAnalyseFile(MultipartFile file) {
        return Result.success(clientSatisfactionService.uploadAnalyseFile(file));
    }


    /**
     * 查询户分析附件
     * @return
     */

    @ApiOperation(value = "查询户分析附件")
    @GetMapping("/pageAnalyseFile")
    public Result<IPage<ClientSatisfactionAnalyseFile>> pageAnalyseFile(Page page, ClientSatisfactionAnalyseFile analyseFile) {
        return Result.success(clientSatisfactionService.pageAnalyseFile(page, analyseFile));
    }

    /**
     * 删除户分析附件
     * @return
     */

    @ApiOperation(value = "删除户分析附件")
    @DeleteMapping("/delAnalyseFile")
    public Result delAnalyseFile(Integer analyseFileId){
        return Result.success(clientSatisfactionAnalyseFileMapper.deleteById(analyseFileId));
    }
}

