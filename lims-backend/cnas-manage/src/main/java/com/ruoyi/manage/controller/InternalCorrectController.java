package com.ruoyi.manage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deepoove.poi.data.style.*;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;
import com.ruoyi.manage.mapper.InternalCorrectFileMapper;
import com.ruoyi.manage.pojo.InternalCorrect;
import com.ruoyi.manage.pojo.InternalCorrectFile;
import com.ruoyi.manage.service.InternalCorrectService;
import com.deepoove.poi.data.style.*;
import com.deepoove.poi.data.style.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 内审管理纠正处理表 前端控制器
 * </p>
 *
 * @author
 * @since 2024-11-13 04:00:15
 */
@Api(tags = "内审纠正措施")
@AllArgsConstructor
@RestController
@RequestMapping("/internalCorrect")
public class InternalCorrectController {

    private InternalCorrectService internalCorrectService;
    private InternalCorrectFileMapper internalCorrectFileMapper;

    /**
     * 新增内审管理纠正处理信息
     * @return
     */

    @ApiOperation(value = "新增内审管理纠正处理")
    @PostMapping("/addInternalCorrect")
    public Result addInternalCorrect(@RequestBody InternalCorrect internalCorrect){
        return Result.success(internalCorrectService.addInternalCorrect(internalCorrect));
    }

    /**
     * 查询内审管理纠正处理
     * @return
     */

    @ApiOperation(value = "查询内审管理纠正处理")
    @GetMapping("/getInternalCorrect")
    public Result<InternalCorrect> getInternalCorrect(Integer correctId){
        return Result.success(internalCorrectService.getInternalCorrect(correctId));
    }

    /**
     * 查询内审管理纠正措施列表
     * @return
     */

    @ApiOperation(value = "查询内审管理纠正措施列表")
    @GetMapping("/pageInternalCorrect")
    public Result<IPage<InternalCorrect>> pageInternalCorrect(Page page, InternalCorrect detailsCorrect) {
        return Result.success(internalCorrectService.pageInternalCorrect(page, detailsCorrect));
    }

    /**
     * 新增内审管理纠正措施附件
     * @param correctId
     * @param file
     * @return
     */

    @ApiOperation(value = "新增内审管理纠正措施附件")
    @PostMapping("/uploadInternalCorrectFile")
    public Result<?> uploadInternalCorrectFile(Integer correctId, MultipartFile file) {
        return Result.success(internalCorrectService.uploadInternalCorrectFile(correctId, file));
    }


    /**
     * 查询内审管理纠正措施附件
     * @return
     */

    @ApiOperation(value = "查询内审管理纠正措施附件")
    @GetMapping("/getInternalCorrectFileList")
    public Result<List<InternalCorrectFile>> getInternalCorrectFileList(Integer correctId){
        return Result.success(internalCorrectService.getInternalCorrectFileList(correctId));
    }

    /**
     * 删除内审管理纠正措施附件
     * @return
     */

    @ApiOperation(value = "删除内审管理纠正措施附件")
    @DeleteMapping("/delInternalCorrectFile")
    public Result delInternalCorrectFile(Integer correctFileId){
        return Result.success(internalCorrectFileMapper.deleteById(correctFileId));
    }

    /**
     * 导出纠正措施
     * @return
     */

    @ApiOperation(value = "导出纠正措施")
    @GetMapping("/exportInternalCorrect")
    public void exportInternalCorrect(Integer correctId, HttpServletResponse response){
        internalCorrectService.exportInternalCorrect(correctId, response);
    }
}
