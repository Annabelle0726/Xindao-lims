package com.ruoyi.require.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;
import com.ruoyi.require.dto.InternalWastesDto;
import com.ruoyi.require.pojo.InternalWastes;
import com.ruoyi.require.service.InternalWastesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 安全内务三废登记 前端控制器
 * </p>
 *
 * @author
 * @since 2024-11-19 06:39:27
 */
@Api(tags = "安全内务三废登记")
@AllArgsConstructor
@RestController
@RequestMapping("/internalWastes")
public class InternalWastesController {

    private InternalWastesService internalWastesService;

    /**
     * 安全内务三废处理分页查询
     * @param page
     * @param internalWastes
     * @return
     */
    @ApiOperation(value = "安全内务三废处理分页查询")
    @GetMapping("/pageInternalWastes")
    public Result<IPage<InternalWastesDto>> pageInternalWastes(Page page,InternalWastes internalWastes) throws Exception {
        return Result.success(internalWastesService.pageInternalWastes(page, internalWastes));
    }

    /**
     * 安全内务三废处理新增
     * @return
     */
    @ApiOperation(value = "安全内务三废处理新增")
    @PostMapping("/addInternalWastes")
    public Result addInternalWastes(@RequestBody InternalWastesDto internalWastes){
        return Result.success(internalWastesService.addInternalWastes(internalWastes));
    }

    /**
     * 安全内务三废处理修改
     * @return
     */
    @ApiOperation(value = "安全内务三废处理修改")
    @PostMapping("/updateInternalWastes")
    public Result updateInternalWastes(@RequestBody InternalWastesDto internalWastes){
        return Result.success(internalWastesService.updateInternalWastes(internalWastes));
    }

    /**
     * 安全内务三废处理删除
     * @return
     */
    @ApiOperation(value = "安全内务三废处理删除")
    @DeleteMapping("/delInternalWastes")
    public Result delInternalWastes(Integer wastesId){
        return Result.success(internalWastesService.delInternalWastes(wastesId));
    }

    /**
     * 安全内务三废处理查看详情
     * @return
     */
    @ApiOperation(value = "安全内务三废处理查看详情")
    @GetMapping("/getInternalWastesOne")
    public Result<InternalWastesDto> getInternalWastesOne(Integer wastesId){
        return Result.success(internalWastesService.getInternalWastesOne(wastesId));
    }

    /**
     * 导出三废处理
     * @return
     */
    @ApiOperation(value = "导出三废处理")
    @GetMapping("/exportInternalWastes")
    public void exportInternalWastes(Integer wastesId, HttpServletResponse response){
        internalWastesService.exportInternalWastes(wastesId, response);
    }

}
