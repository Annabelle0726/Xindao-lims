package com.ruoyi.manage.controller;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deepoove.poi.data.style.*;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;
import com.ruoyi.manage.dto.InternalCheckDto;
import com.ruoyi.manage.pojo.InternalCheck;
import com.ruoyi.manage.service.InternalCheckService;
import com.deepoove.poi.data.style.*;
import com.deepoove.poi.data.style.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * 内审检查表
 *
 * @author zhuo
 * @since 2024-11-11
 */
@Api(tags = "内审检查")
@AllArgsConstructor
@RestController
@RequestMapping("/internalCheck")
public class InternalCheckController {

    private InternalCheckService internalCheckService;

    /**
     * 内审检查分页查询
     * @param
     * @return
     */

    @ApiOperation(value = "内审检查分页查询")
    @GetMapping("/pageInternalCheck")
    public Result<IPage<InternalCheckDto>> pageInternalCheck(Page page,InternalCheck internalCheck) throws Exception {
        return Result.success(internalCheckService.pageInternalCheck(page, internalCheck));
    }

    /**
     * 内审检查新增
     * @return
     */

    @ApiOperation(value = "内审检查新增")
    @PostMapping("/addInternalCheck")
    public Result addInternalCheck(@RequestBody InternalCheckDto internalCheck){
        return Result.success(internalCheckService.addInternalCheck(internalCheck));
    }

    /**
     * 内审检查修改
     * @return
     */

    @ApiOperation(value = "内审检查修改")
    @PostMapping("/updateInternalCheck")
    public Result updateInternalCheck(@RequestBody InternalCheckDto internalCheck){
        return Result.success(internalCheckService.updateInternalCheck(internalCheck));
    }

    /**
     * 内审检查删除
     * @return
     */

    @ApiOperation(value = "内审检查删除")
    @DeleteMapping("/delInternalCheck")
    public Result delInternalCheck(Integer checkId){
        return Result.success(internalCheckService.delInternalCheck(checkId));
    }

    /**
     * 内审检查查看详情
     * @return
     */

    @ApiOperation(value = "内审检查查看详情")
    @GetMapping("/getInternalCheckOne")
    public Result<InternalCheckDto> getInternalCheckOne(Integer checkId){
        return Result.success(internalCheckService.getInternalCheckOne(checkId));
    }


    /**
     * 内审检查批准
     * @return
     */
    @ApiOperation(value = "内审检查批准")
    @PostMapping("/ratifyInternalCheck")
    public Result ratifyInternalCheck(@RequestBody InternalCheckDto internalCheck){
        return Result.success(internalCheckService.ratifyInternalCheck(internalCheck));
    }

    /**
     * 导出内审检查
     * @return
     */

    @ApiOperation(value = "导出内审检查")
    @GetMapping("/exportInternalCheck")
    public void exportInternalCheck(Integer checkId, HttpServletResponse response){
        internalCheckService.exportInternalCheck(checkId, response);
    }

}

