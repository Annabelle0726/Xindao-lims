package com.ruoyi.manage.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.alibaba.excel.read.listener.PageReadListener;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.manage.pojo.ManageControlPlanList;
import com.ruoyi.manage.service.ManageControlPlanListService;
import com.ruoyi.manage.vo.ManageControlPlanListVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
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
 * 重大风险因素分析及控制计划清单 前端控制器
 * </p>
 *
 * @author
 * @since 2024-11-15 02:58:30
 */
@Api(tags = "重大风险因素分析及控制计划清单")
@RestController
@RequestMapping("/manageControlPlanList")
public class ManageControlPlanListController {


    @Resource
    private ManageControlPlanListService manageControlPlanListService;



    @ApiOperation(value = "分页查询")
    @GetMapping("/getPageList")
    public Result<IPage<ManageControlPlanListVo>> getPageList(Page page) {
        return Result.success(manageControlPlanListService.getPageList(page));
    }

    @ApiOperation(value = "批准")
    @PostMapping("/approvalOfControlPlanChecklist")
    public Result<?> approvalOfControlPlanChecklist(@RequestBody Map<String, Integer> param){
        Integer approve = param.get("approve");
        Integer status= param.get("status");
        manageControlPlanListService.update(Wrappers.<ManageControlPlanList>lambdaUpdate()
                .set(ManageControlPlanList::getApprove, approve)
                .set(ManageControlPlanList::getApproveStatus, status)
                .set(ManageControlPlanList::getApproveDate, LocalDateTime.now()));
        return Result.success();
    }

    @ApiOperation(value = "审批")
    @PostMapping("/riskAnalysisApprovalOfControlPlanChecklist")
    public Result<?> riskAnalysisApprovalOfControlPlanChecklist(@RequestBody Map<String, Integer> param){
        Integer approval = param.get("approval");
        Integer status= param.get("status");
        manageControlPlanListService.update(Wrappers.<ManageControlPlanList>lambdaUpdate()
                .set(ManageControlPlanList::getApproval, approval)
                .set(ManageControlPlanList::getApprovalStatus, status)
                .set(ManageControlPlanList::getApprovalDate, LocalDateTime.now()));
        return Result.success();
    }

    @PostMapping("/importControlPlanList")
    public void importControlPlanList(MultipartFile file) throws IOException {
        boolean excelFile = isExcelFile(file);
        if (!excelFile) {
            throw new ErrorException("请导入excel文件！");
        }
        EasyExcel.read(file.getInputStream(), ManageControlPlanList.class, new PageReadListener<ManageControlPlanList>(dataList -> {
            Integer userId = SecurityUtils.getUserId().intValue();
            dataList.forEach(i -> {
                i.setEditor(userId);
                i.setEditorDate(LocalDateTime.now());
                i.setApproveStatus(0);
                i.setApprovalStatus(0);
            });
            manageControlPlanListService.saveOrUpdateBatch(dataList);
        })).sheet().doRead();
    }

    @ApiOperation(value = "新增")
    @PostMapping("/analysisOfMajorRiskFactorsAdded")
    public void analysisOfMajorRiskFactorsAdded(@RequestBody ManageControlPlanList manageControlPlanList) {
        Integer userId = SecurityUtils.getUserId().intValue();
        manageControlPlanList.setEditor(userId);
        manageControlPlanList.setEditorDate(LocalDateTime.now());
        manageControlPlanListService.saveOrUpdate(manageControlPlanList);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("/deleteSignificantRiskFactorAnalysis")
    public void deleteSignificantRiskFactorAnalysis(Integer id) {
        manageControlPlanListService.removeById(id);
    }

    /**
     *
     * @return
     */

    @ApiOperation(value = "重大风险因素分析及控制计划清单")
    @GetMapping("/exportSignificantRiskFactors")
    public void exportSignificantRiskFactors(HttpServletResponse response){
        manageControlPlanListService.exportPersonTraining(response);
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
