package com.ruoyi.process.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.process.dto.QualitySuperviseDetailsDto;
import com.ruoyi.process.mapper.QualitySuperviseDetailsCorrectMapper;
import com.ruoyi.process.pojo.*;
import com.ruoyi.process.service.QualitySuperviseDetailsService;
import com.ruoyi.process.service.QualitySuperviseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 质量监督主表
 *
 * @author zhuo
 * @since 2024-11-07
 */
@Api(tags = "质量监督计划")
@AllArgsConstructor
@RestController
@RequestMapping("/qualitySupervise")
public class QualitySuperviseController {

    @Resource
    private QualitySuperviseService qualitySuperviseService;
    @Resource
    private QualitySuperviseDetailsService qualitySuperviseDetailsService;
    @Resource
    private QualitySuperviseDetailsCorrectMapper qualitySuperviseDetailsCorrectMapper;

    /**
     * 导入监督计划
     * @return
     */
    @ApiOperation(value = "导入监督计划")
    @PostMapping("/importQualitySupervise")
    public Result importQualitySupervise(MultipartFile file, QualitySupervise qualitySupervise){
        return Result.success(qualitySuperviseService.importQualitySupervise(file, qualitySupervise));
    }

    /**
     * 监督计划列表
     * @return
     */
    @ApiOperation(value = "监督计划列表")
    @GetMapping("/pageQualitySupervise")
    public Result<IPage<QualitySupervise>> pageQualitySupervise(QualitySupervise qualitySupervise ,Page page) throws Exception {
        return Result.success(qualitySuperviseService.pageQualitySupervise(page, qualitySupervise));
    }

    /**
     * 监督计划批准
     * @return
     */
    @ApiOperation(value = "监督计划批准")
    @PostMapping("/ratifyQualitySupervise")
    public Result ratifyQualitySupervise(@RequestBody QualitySupervise qualitySupervise){
        return Result.success(qualitySuperviseService.ratifyQualitySupervise(qualitySupervise));
    }

    /**
     * 监督计划删除
     * @return
     */
    @ApiOperation(value = "监督计划删除")
    @DeleteMapping("/delQualitySupervise")
    public Result delQualitySupervise(Integer superviseId){
        return Result.success(qualitySuperviseService.removeById(superviseId));
    }

    /**
     * 监督计划详情列表
     * @param qualitySuperviseDetails
     * @param page
     * @return
     */
    @ApiOperation(value = "监督计划详情列表")
    @GetMapping("/pageQualitySuperviseDetail")
    public Result<IPage<QualitySuperviseDetailsDto>> pageQualitySuperviseDetail(QualitySuperviseDetailsDto qualitySuperviseDetails,Page page) throws Exception {
        return Result.success(qualitySuperviseService.pageQualitySuperviseDetail(page, qualitySuperviseDetails));
    }

    /**
     * 新增监督计划详情
     * @return
     */
    @ApiOperation(value = "新增监督计划详情")
    @PostMapping("/addQualitySuperviseDetail")
    public Result addQualitySuperviseDetail(@RequestBody QualitySuperviseDetails qualitySuperviseDetails){
        if (qualitySuperviseDetails.getSuperviseId() == null) {
            throw new ErrorException("缺少监督计划主表id");
        }
        return Result.success(qualitySuperviseDetailsService.save(qualitySuperviseDetails));
    }

    /**
     * 修改监督计划详情
     * @return
     */
    @ApiOperation(value = "修改监督计划详情")
    @PostMapping("/updateQualitySuperviseDetail")
    public Result updateQualitySuperviseDetail(@RequestBody QualitySuperviseDetails qualitySuperviseDetails){
        return Result.success(qualitySuperviseDetailsService.updateById(qualitySuperviseDetails));
    }

    /**
     * 删除监督计划详情
     * @return
     */
    @ApiOperation(value = "删除监督计划详情")
    @DeleteMapping("/delQualitySuperviseDetail")
    public Result delQualitySuperviseDetail(Integer superviseDetailsId){
        return Result.success(qualitySuperviseDetailsService.removeById(superviseDetailsId));
    }

    /**
     * 查询该计划监督员
     * @return
     */
    @ApiOperation(value = "查询该计划监督员")
    @GetMapping("/getRecordUser")
    public Result getRecordUser(Integer superviseDetailsId){
        return Result.success(qualitySuperviseService.getRecordUser(superviseDetailsId));
    }


    /**
     * 导出监督计划
     * @return
     */
    @ApiOperation(value = "导出监督计划")
    @GetMapping("/exportQualitySupervise")
    public void exportQualitySupervise(Integer superviseId, HttpServletResponse response){
        qualitySuperviseService.exportQualitySupervise(superviseId, response);
    }


    /************************************************* 记录 *********************************************************/

    /**
     * 查询监督记录信息
     * @return
     */
    @ApiOperation(value = "查询监督记录信息")
    @GetMapping("/getSuperviseDetailRecord")
    public Result<QualitySuperviseDetailsRecord> getSuperviseDetailRecord(Integer superviseDetailsId){
        return Result.success(qualitySuperviseService.getSuperviseDetailRecord(superviseDetailsId));
    }

    /**
     * 新增监督记录信息
     * @return
     */
    @ApiOperation(value = "新增监督记录信息")
    @PostMapping("/addSuperviseDetailRecord")
    public Result addSuperviseDetailRecord(@RequestBody QualitySuperviseDetailsRecord qualitySuperviseDetailsRecord){
        return Result.success(qualitySuperviseService.addSuperviseDetailRecord(qualitySuperviseDetailsRecord));
    }

    /**
     * 监督记录批准
     * @return
     */
    @ApiOperation(value = "监督记录批准")
    @PostMapping("/addSuperviseRecordOpinion")
    public Result addSuperviseRecordOpinion(@RequestBody QualitySuperviseDetailsRecord qualitySuperviseDetailsRecord){
        return Result.success(qualitySuperviseService.addSuperviseRecordOpinion(qualitySuperviseDetailsRecord));
    }

    /**
     * 导出质量监督详情记录表
     * @return
     */
    @ApiOperation(value = "导出监督记录表")
    @GetMapping("/exportSuperviseDetailRecord")
    public void exportSuperviseDetailRecord(Integer superviseDetailsId, HttpServletResponse response){
        qualitySuperviseService.exportSuperviseDetailRecord(superviseDetailsId, response);
    }

    /*************************************************  不合格工作控制单 ********************************************************/


    /**
     * 新增监督记录不符合控制信息
     * @return
     */
    @ApiOperation(value = "新增监督记录不符合控制信息")
    @PostMapping("/addSuperviseDetailAccording")
    public Result addSuperviseDetailAccording(@RequestBody QualitySuperviseDetailsAccording qualitySuperviseDetailsAccording){
        return Result.success(qualitySuperviseService.addSuperviseDetailAccording(qualitySuperviseDetailsAccording));
    }

    /**
     * 新增监督记录不符合控制信息(装备流程)
     * @return
     */
    @ApiOperation(value = "(装备流程)新增监督记录不符合控制信息")
    @PostMapping("/addEquipSuperviseDetailAccording")
    public Result addEquipSuperviseDetailAccording(@RequestBody QualitySuperviseDetailsAccording qualitySuperviseDetailsAccording){
        return Result.success(qualitySuperviseService.addEquipSuperviseDetailAccording(qualitySuperviseDetailsAccording));
    }

    /**
     * (装备流程)批准监督记录不符合控制信息
     * @return
     */
    @ApiOperation(value = "(装备流程)批准监督记录不符合控制信息")
    @PostMapping("/approverEquipSuperviseDetailAccording")
    public Result approverEquipSuperviseDetailAccording(@RequestBody QualitySuperviseDetailsAccording qualitySuperviseDetailsAccording){
        return Result.success(qualitySuperviseService.approverEquipSuperviseDetailAccording(qualitySuperviseDetailsAccording));
    }

    /**
     * 查询监督记录不符合控制信息
     * @return
     */
    @ApiOperation(value = "查询监督记录不符合控制信息")
    @GetMapping("/getSuperviseDetailAccording")
    public Result<QualitySuperviseDetailsAccording> getSuperviseDetailAccording(Integer superviseDetailsId){
        return Result.success(qualitySuperviseService.getSuperviseDetailAccording(superviseDetailsId));
    }

    /**
     * 查询监督记录不符合控制信息列表
     * @return
     */
    @ApiOperation(value = "查询监督记录不符合控制信息列表")
    @GetMapping("/pageSuperviseDetailAccording")
    public Result<IPage<QualitySuperviseDetailsAccording>> pageSuperviseDetailAccording(QualitySuperviseDetailsAccording detailsAccording,Page page) throws Exception {
        return Result.success(qualitySuperviseService.pageSuperviseDetailAccording(page, detailsAccording));
    }

    /**
     * 导出监督记录不符合控制信息
     * @return
     */
    @ApiOperation(value = "导出监督记录不符合控制信息")
    @GetMapping("/superviseDetailAccordingExport")
    public void superviseDetailAccordingExport(@RequestParam("superviseDetailsId") Integer id, HttpServletResponse response){
        qualitySuperviseService.superviseDetailAccordingExport(id, response);
    }



    /*************************************************  纠正措施处理单 ********************************************************/


    /**
     * 新增监督纠正处理信息
     * @return
     */
    @ApiOperation(value = "新增监督纠正处理")
    @PostMapping("/addSuperviseDetailCorrect")
    public Result addSuperviseDetailCorrect(@RequestBody QualitySuperviseDetailsCorrect qualitySuperviseDetailsCorrect){
        return Result.success(qualitySuperviseService.addSuperviseDetailCorrect(qualitySuperviseDetailsCorrect));
    }

    /**
     * (装备流程)新增监督纠正处理信息
     * @return
     */
    @ApiOperation(value = "(装备流程)新增监督纠正处理")
    @PostMapping("/addEquipSuperviseDetailCorrect")
    public Result addEquipSuperviseDetailCorrect(@RequestBody QualitySuperviseDetailsCorrect qualitySuperviseDetailsCorrect){
        return Result.success(qualitySuperviseService.addEquipSuperviseDetailCorrect(qualitySuperviseDetailsCorrect));
    }

    /**
     * (装备流程)批准监督纠正处理
     * @return
     */
    @ApiOperation(value = "(装备流程)批准监督纠正处理")
    @PostMapping("/approveEquipSuperviseDetailCorrect")
    public Result approveEquipSuperviseDetailCorrect(@RequestBody QualitySuperviseDetailsCorrect qualitySuperviseDetailsCorrect){
        return Result.success(qualitySuperviseService.approveEquipSuperviseDetailCorrect(qualitySuperviseDetailsCorrect));
    }

    /**
     * 查询监督纠正处理
     * @return
     */
    @ApiOperation(value = "查询监督纠正处理")
    @GetMapping("/getSuperviseDetailCorrect")
    public Result<QualitySuperviseDetailsCorrect> getSuperviseDetailCorrect(Integer superviseDetailsId){
        return Result.success(qualitySuperviseService.getSuperviseDetailCorrect(superviseDetailsId));
    }

    /**
     * 查询监督纠正措施列表
     * @return
     */
    @ApiOperation(value = "查询监督纠正措施列表")
    @GetMapping("/pageSuperviseDetailCorrect")
    public Result<IPage<QualitySuperviseDetailsCorrect>> pageSuperviseDetailCorrect(QualitySuperviseDetailsCorrect detailsCorrect,Page page) throws Exception {
        return Result.success(qualitySuperviseService.pageSuperviseDetailCorrect(page, detailsCorrect));
    }

    /**
     * 新增监督纠正措施附件
     * @param superviseDetailsCorrectId
     * @param file
     * @return
     */
    @ApiOperation(value = "新增监督纠正措施附件")
    @PostMapping("/uploadSuperviseDetailCorrectFile")
    public Result<?> uploadSuperviseDetailCorrectFile(Integer superviseDetailsCorrectId, MultipartFile file) {
        return Result.success(qualitySuperviseService.uploadSuperviseDetailCorrectFile(superviseDetailsCorrectId, file));
    }


    /**
     * 查询监督纠正措施附件
     * @return
     */
    @ApiOperation(value = "查询监督纠正措施附件")
    @GetMapping("/getSuperviseDetailCorrectFileList")
    public Result<List<QualitySuperviseDetailsCorrectFile>> getSuperviseDetailCorrectFileList(Integer superviseDetailsCorrectId){
        return Result.success(qualitySuperviseService.getSuperviseDetailCorrectFileList(superviseDetailsCorrectId));
    }

    /**
     * 删除监督纠正措施附件
     * @return
     */
    @ApiOperation(value = "删除监督纠正措施附件")
    @DeleteMapping("/delSuperviseDetailCorrectFile")
    public Result delSuperviseDetailCorrectFile(Integer superviseDetailsCorrectFileId){
        return Result.success(qualitySuperviseDetailsCorrectMapper.deleteById(superviseDetailsCorrectFileId));
    }

    /**
     * 导出监督纠正措施
     * @return
     */
    @ApiOperation(value = "导出监督纠正措施")
    @GetMapping("/exportSuperviseDetaillCorrect")
    public void exportSuperviseDetaillCorrect(Integer superviseDetailsCorrectId, HttpServletResponse response){
        qualitySuperviseService.exportSuperviseDetaillCorrect(superviseDetailsCorrectId, response);
    }
}

