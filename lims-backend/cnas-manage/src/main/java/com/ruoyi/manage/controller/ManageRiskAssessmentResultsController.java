package com.ruoyi.manage.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.read.listener.PageReadListener;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deepoove.poi.data.style.*;


import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.manage.pojo.ManageRiskAssessmentResults;
import com.ruoyi.manage.service.ManageRiskAssessmentResultsService;
import com.ruoyi.manage.vo.ManageRiskAssessmentResultsVo;
import com.deepoove.poi.data.style.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * 危险因素辨识与风险评价结果一览表 前端控制器
 * </p>
 *
 * @author
 * @since 2024-11-15 02:58:51
 */
@Api(tags = "危险因素辨识与风险评价结果一览表")
@RestController
@RequestMapping("/manageRiskAssessmentResults")
public class ManageRiskAssessmentResultsController {

    @Resource
    private ManageRiskAssessmentResultsService manageRiskAssessmentResultsService;

    @ApiOperation(value = "分页查询")
    @GetMapping("/getPageResults")
    public Result<IPage<ManageRiskAssessmentResultsVo>> getPageResults(Page page) {
        IPage<ManageRiskAssessmentResultsVo> ipage = manageRiskAssessmentResultsService.getPageResults(page);
        return Result.success(ipage);
    }

    @ApiOperation(value = "批准")
    @PostMapping("/hazardIdentificationAndRiskApproval")
    public Result<?> hazardIdentificationAndRiskApproval(@RequestBody Map<String, Integer> param) {
        Integer approve = param.get("approve");
        Integer status = param.get("status");
        manageRiskAssessmentResultsService.update(Wrappers.<ManageRiskAssessmentResults>lambdaUpdate()
                .set(ManageRiskAssessmentResults::getApprove, approve)
                .set(ManageRiskAssessmentResults::getApproveStatus, status)
                .set(ManageRiskAssessmentResults::getApproveDate, LocalDateTime.now()));
        return Result.success();
    }

    @ApiOperation(value = "审批")
    @PostMapping("/dangerousRiskApproval")
    public Result<?> dangerousRiskApproval(@RequestBody Map<String, Integer> param) {
        Integer approval = param.get("approval");
        Integer status = param.get("status");
        manageRiskAssessmentResultsService.update(Wrappers.<ManageRiskAssessmentResults>lambdaUpdate()
                .set(ManageRiskAssessmentResults::getApproval, approval)
                .set(ManageRiskAssessmentResults::getApprovalStatus, status)
                .set(ManageRiskAssessmentResults::getApprovalDate, LocalDateTime.now()));
        return Result.success();
    }

    @ApiOperation(value = "导入")
    @PostMapping("/riskAssessmentImport")
    public void riskAssessmentImport(MultipartFile file) throws IOException {
        boolean excelFile = isExcelFile(file);
        if (!excelFile) {
            throw new ErrorException("请导入excel文件！");
        }
        EasyExcel.read(file.getInputStream(), ManageRiskAssessmentResults.class, new PageReadListener<ManageRiskAssessmentResults>(dataList -> {
            Integer userId = SecurityUtils.getUserId().intValue();
            dataList.forEach(i -> {
                i.setEditor(userId);
                i.setEditorDate(LocalDateTime.now());
                i.setApproveStatus(0);
                i.setApprovalStatus(0);
            });
            manageRiskAssessmentResultsService.saveOrUpdateBatch(dataList);
        })).extraRead(CellExtraTypeEnum.MERGE).sheet().doRead();
    }

    @ApiOperation(value = "新增")
    @PostMapping("/addNewRiskFactors")
    public void addNewRiskFactors(@RequestBody ManageRiskAssessmentResults manageRiskAssessmentResults) {
        Integer userId = SecurityUtils.getUserId().intValue();
        manageRiskAssessmentResults.setEditor(userId);
        manageRiskAssessmentResults.setEditorDate(LocalDateTime.now());
        manageRiskAssessmentResultsService.saveOrUpdate(manageRiskAssessmentResults);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("/removeRiskFactors")
    public void removeRiskFactors(Integer id) {
        manageRiskAssessmentResultsService.removeById(id);
    }


    @ApiOperation(value = "导出危险因素辨识与风险评价结果一览")
    @GetMapping("/exportHazardFactorIdentification")
    public void exportPersonTraining(HttpServletResponse response){
        manageRiskAssessmentResultsService.exportPersonTraining(response);
    }

    public static boolean isExcelFile(MultipartFile file) {
        if (file.isEmpty()) {
            return false;
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return false;
        }
        String[] parts = originalFilename.split("\\.");
        if (parts.length == 0) {
            return false;
        }
        String fileExtension = parts[parts.length - 1].toLowerCase();
        return fileExtension.equals("xls") || fileExtension.equals("xlsx");
    }
}
