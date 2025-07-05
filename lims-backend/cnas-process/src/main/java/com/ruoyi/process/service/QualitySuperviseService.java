package com.ruoyi.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.process.dto.QualitySuperviseDetailsDto;
import com.ruoyi.process.pojo.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 质量监督主表
 *
 * @author zhuo
 * @since 2024-11-07
 */
public interface QualitySuperviseService extends IService<QualitySupervise> {

    /**
     * 导入监督计划
     * @param file
     * @return
     */
    boolean importQualitySupervise(MultipartFile file, QualitySupervise qualitySupervise);

    /**
     * 监督计划批准
     * @param qualitySupervise
     * @return
     */
    boolean ratifyQualitySupervise(QualitySupervise qualitySupervise);

    /**
     * 监督计划列表
     * @return
     */
    IPage<QualitySupervise> pageQualitySupervise(Page page, QualitySupervise qualitySupervise);

    /**
     * 监督计划详情列表
     * @return
     */
    IPage<QualitySuperviseDetailsDto> pageQualitySuperviseDetail(Page page, QualitySuperviseDetailsDto qualitySuperviseDetails);

    /**
     * 查询该计划监督员
     * @param superviseDetailsId
     * @return
     */
    List<Map<String, String>> getRecordUser(Integer superviseDetailsId);

    /**
     * 导出监督计划
     * @param superviseId
     * @param response
     */
    void exportQualitySupervise(Integer superviseId, HttpServletResponse response);

    /************************************************ 记录 ******************************************************/

    /**
     * 查询监督记录信息
     * @param superviseDetailsId
     * @return
     */
    QualitySuperviseDetailsRecord getSuperviseDetailRecord(Integer superviseDetailsId);

    /**
     * 新增监督记录信息
     * @param qualitySuperviseDetailsRecord
     * @return
     */
    boolean addSuperviseDetailRecord(QualitySuperviseDetailsRecord qualitySuperviseDetailsRecord);

    /**
     * 监督记录批准
     * @param qualitySuperviseDetailsRecord
     * @return
     */
    boolean addSuperviseRecordOpinion(QualitySuperviseDetailsRecord qualitySuperviseDetailsRecord);

    /**
     * 导出监督记录表
     * @param superviseDetailsId
     * @param response
     */
    void exportSuperviseDetailRecord(Integer superviseDetailsId, HttpServletResponse response);

    /*************************************************  不合格工作控制单 ********************************************************/

    /**
     * 新增监督记录不符合控制信息
     * @param qualitySuperviseDetailsAccording
     * @return
     */
    boolean addSuperviseDetailAccording(QualitySuperviseDetailsAccording qualitySuperviseDetailsAccording);

    /**
     * (装备流程)新增监督记录不符合控制信息
     * @param qualitySuperviseDetailsAccording
     * @return
     */
    boolean addEquipSuperviseDetailAccording(QualitySuperviseDetailsAccording qualitySuperviseDetailsAccording);

    /**
     * (装备流程)批准监督记录不符合控制信息
     * @param qualitySuperviseDetailsAccording
     * @return
     */
    boolean approverEquipSuperviseDetailAccording(QualitySuperviseDetailsAccording qualitySuperviseDetailsAccording);

    /**
     * 查询监督记录不符合控制信息
     * @param superviseDetailsId
     * @return
     */
    QualitySuperviseDetailsAccording getSuperviseDetailAccording(Integer superviseDetailsId);

    /**
     * 查询监督记录不符合控制信息列表
     * @param detailsAccording
     * @return
     */
    IPage<QualitySuperviseDetailsAccording> pageSuperviseDetailAccording(Page page, QualitySuperviseDetailsAccording detailsAccording);

    /**
     * 导出监督记录不符合控制信息
     * @param superviseDetailAccordingId
     * @param response
     */
    void superviseDetailAccordingExport(Integer superviseDetailAccordingId, HttpServletResponse response);

    /*************************************************  纠正措施处理单 ********************************************************/

    /**
     * 新增监督纠正处理信息
     * @param qualitySuperviseDetailsAccording
     * @return
     */
    boolean addSuperviseDetailCorrect(QualitySuperviseDetailsCorrect qualitySuperviseDetailsAccording);

    /**
     * (装备流程)新增监督纠正处理
     * @param qualitySuperviseDetailsCorrect
     * @return
     */
    boolean addEquipSuperviseDetailCorrect(QualitySuperviseDetailsCorrect qualitySuperviseDetailsCorrect);

    /**
     * (装备流程)批准监督纠正处理
     * @param qualitySuperviseDetailsCorrect
     * @return
     */
    boolean approveEquipSuperviseDetailCorrect(QualitySuperviseDetailsCorrect qualitySuperviseDetailsCorrect);

    /**
     * 查询监督纠正处理
     * @param superviseDetailsId
     * @return
     */
    QualitySuperviseDetailsCorrect getSuperviseDetailCorrect(Integer superviseDetailsId);

    /**
     * 查询监督纠正措施列表
     * @param page
     * @param detailsCorrect
     * @return
     */
    IPage<QualitySuperviseDetailsCorrect> pageSuperviseDetailCorrect(Page page, QualitySuperviseDetailsCorrect detailsCorrect);

    /**
     * 新增监督纠正措施附件
     * @param superviseDetailsCorrectId
     * @param file
     * @return
     */
    boolean uploadSuperviseDetailCorrectFile(Integer superviseDetailsCorrectId, MultipartFile file);

    /**
     * 查询监督纠正措施附件
     * @param superviseDetailsCorrectId
     * @return
     */
    List<QualitySuperviseDetailsCorrectFile> getSuperviseDetailCorrectFileList(Integer superviseDetailsCorrectId);

    /**
     * 导出监督纠正措施
     * @param superviseDetailsCorrectId
     * @param response
     */
    void exportSuperviseDetaillCorrect(Integer superviseDetailsCorrectId, HttpServletResponse response);



    /*************************************************  质量监督记录 ********************************************************/

}

