package com.ruoyi.basic.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.basic.pojo.StructureTestObjectPart;
import com.ruoyi.basic.service.StructureTestObjectPartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 检验对象零件表
 *
 * @author zhuo
 * @since 2024-08-07
 */
@RestController
@RequestMapping("/structureTestObjectPart")
@AllArgsConstructor
@Api(tags = "检验对象零件表")
public class StructureTestObjectPartController {

    private StructureTestObjectPartService structureTestObjectPartService;

    @ApiOperation(value = "根据检验对象id查询零件")
    @GetMapping("/selectByTestObjectId")
    public Result selectByTestObjectId(Page page,StructureTestObjectPart structureTestObjectPart){
        return Result.success(structureTestObjectPartService.selectByTestObjectId(page,structureTestObjectPart));
    }

    @ApiOperation(value = "新增检验对象零件")
    @PostMapping("/addTestObjectPart")
    public Result addTestObjectPart(@RequestBody StructureTestObjectPart structureTestObjectPart) {
        structureTestObjectPartService.addTestObjectPart(structureTestObjectPart);
        return Result.success();
    }

    @ApiOperation(value = "更新检验对象零件")
    @PostMapping("/updateTestObjectPart")
    public Result updateTestObjectPart(@RequestBody StructureTestObjectPart structureTestObjectPart) {
        structureTestObjectPartService.updateTestObjectPart(structureTestObjectPart);
        return Result.success();
    }

    @ApiOperation(value = "删除检验对象零件")
    @DeleteMapping("/deleteTestObjectPart")
    public Result deleteTestObjectPart(Integer id) {
        structureTestObjectPartService.removeById(id);
        return Result.success();
    }

}

