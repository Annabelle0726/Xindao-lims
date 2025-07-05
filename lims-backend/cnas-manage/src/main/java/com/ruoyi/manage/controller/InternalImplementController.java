package com.ruoyi.manage.controller;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deepoove.poi.data.style.*;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;

import com.ruoyi.manage.dto.InternalImplementDto;
import com.ruoyi.manage.pojo.InternalImplement;
import com.ruoyi.manage.service.InternalImplementService;
import com.deepoove.poi.data.style.*;
import com.deepoove.poi.data.style.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * 内审实施计划
 *
 * @author zhuo
 * @since 2024-11-11
 */
@Api(tags = "内审实施计划")
@AllArgsConstructor
@RestController
@RequestMapping("/internalImplement")
public class InternalImplementController {

    private InternalImplementService internalImplementService;

    /**
     * 内审实施计划分页查询
     * @param
     * @return
     */

    @ApiOperation(value = "内审实施计划分页查询")
    @GetMapping("/pageInternalImplement")
    public Result<IPage<InternalImplementDto>> pageInternalImplement(Page page,InternalImplement internalImplement) throws Exception {
        return Result.success(internalImplementService.pageInternalImplement(page, internalImplement));
    }

    /**
     * 内审实施计划新增
     * @return
     */

    @ApiOperation(value = "内审实施计划新增")
    @PostMapping("/addInternalImplement")
    public Result addInternalImplement(@RequestBody InternalImplementDto internalImplement){
        return Result.success(internalImplementService.addInternalImplement(internalImplement));
    }

    /**
     * 内审实施计划修改
     * @return
     */

    @ApiOperation(value = "内审实施计划修改")
    @PostMapping("/updateInternalImplement")
    public Result updateInternalImplement(@RequestBody InternalImplementDto internalImplement){
        return Result.success(internalImplementService.updateInternalImplement(internalImplement));
    }

    /**
     * 内审实施计划删除
     * @return
     */

    @ApiOperation(value = "内审实施计划删除")
    @DeleteMapping("/delInternalImplement")
    public Result delInternalImplement(Integer implementId){
        return Result.success(internalImplementService.delInternalImplement(implementId));
    }

    /**
     * 内审实施计划查看详情
     * @return
     */

    @ApiOperation(value = "内审实施计划查看详情")
    @GetMapping("/getInternalImplementOne")
    public Result<InternalImplementDto> getInternalImplementOne(Integer implementId){
        return Result.success(internalImplementService.getInternalImplementOne(implementId));
    }


    /**
     * 内审实施计划批准
     * @return
     */
    @ApiOperation(value = "内审实施计划批准")
    @PostMapping("/ratifyInternalImplement")
    public Result ratifyInternalImplement(@RequestBody InternalImplementDto internalImplement){
        return Result.success(internalImplementService.ratifyInternalImplement(internalImplement));
    }

    /**
     * 导出内审实施计划
     * @return
     */

    @ApiOperation(value = "导出内审实施计划")
    @GetMapping("/exportInternalImplement")
    public void exportInternalImplement(Integer implementId, HttpServletResponse response){
        internalImplementService.exportInternalImplement(implementId, response);
    }

}

