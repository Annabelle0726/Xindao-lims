package com.ruoyi.require.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;
import com.ruoyi.require.dto.ForeignRegisterDto;
import com.ruoyi.require.pojo.ForeignRegister;
import com.ruoyi.require.service.ForeignRegisterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 外来人员登记 前端控制器
 * </p>
 *
 * @author
 * @since 2024-11-19 07:17:35
 */
@Api(tags = "外来人员登记")
@AllArgsConstructor
@RestController
@RequestMapping("/foreignRegister")
public class ForeignRegisterController {

    private ForeignRegisterService foreignRegisterService;

    /**
     * 外来人员登记分页查询
     * @param page
     * @param foreignRegister
     * @return
     */
    @ApiOperation(value = "外来人员登记分页查询")
    @GetMapping("/pageForeignRegister")
    public Result<IPage<ForeignRegisterDto>> pageForeignRegister(Page page,ForeignRegisterDto foreignRegister) throws Exception {
        return Result.success(foreignRegisterService.pageForeignRegister(page, foreignRegister));
    }

    /**
     * 外来人员登记新增
     * @return
     */
    @ApiOperation(value = "外来人员登记新增")
    @PostMapping("/addForeignRegister")
    public Result addForeignRegister(@RequestBody ForeignRegister foreignRegister){
        return Result.success(foreignRegisterService.save(foreignRegister));
    }

    /**
     * 外来人员登记修改
     * @return
     */
    @ApiOperation(value = "外来人员登记修改")
    @PostMapping("/updateForeignRegister")
    public Result updateForeignRegister(@RequestBody ForeignRegister foreignRegister){
        return Result.success(foreignRegisterService.updateById(foreignRegister));
    }

    /**
     * 外来人员登记删除
     * @return
     */
    @ApiOperation(value = "外来人员登记删除")
    @DeleteMapping("/delForeignRegister")
    public Result delForeignRegister(Integer registerId){
        return Result.success(foreignRegisterService.removeById(registerId));
    }

    /**
     * 外来人员登记查看详情
     * @return
     */
    @ApiOperation(value = "外来人员登记查看详情")
    @GetMapping("/getForeignRegisterOne")
    public Result<ForeignRegister> getForeignRegisterOne(Integer registerId){
        return Result.success(foreignRegisterService.getById(registerId));
    }

    /**
     * 导出外来人员登记
     * @return
     */
    @ApiOperation(value = "导出外来人员登记")
    @GetMapping("/exportForeignRegister")
    public void exportForeignRegister(ForeignRegisterDto foreignRegister, HttpServletResponse response){
        foreignRegisterService.exportForeignRegister(foreignRegister, response);
    }

}
