package com.ruoyi.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.process.dto.QualityMonitorDetailsDto;
import com.ruoyi.process.dto.QualityMonitorDto;
import com.ruoyi.process.pojo.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 质量监控计划主表
 *
 * @author zhuo
 * @since 2024-11-06
 */
public interface QualityMonitorService extends IService<QualityMonitor> {

    /**
     * 导入监控计划
     * @param file
     * @return
     */
    boolean importQualityMonitor(MultipartFile file, QualityMonitor qualityMonitor);


    /**
     * 监控计划审核
     * @param qualityMonitor
     * @return
     */
    boolean examineQualityMonitor(QualityMonitor qualityMonitor);

    /**
     * 监控计划批准
     * @param qualityMonitor
     * @return
     */
    boolean ratifyQualityMonitor(QualityMonitor qualityMonitor);

    /**
     * 监控计划列表
     * @param page
     * @param qualityMonitor
     * @return
     */
    IPage<QualityMonitorDto> pageQualityMonitor(Page page, QualityMonitor qualityMonitor);

    /**
     * 监控计划详情列表
     * @param page
     * @param qualityMonitorDetails
     * @return
     */
    IPage<QualityMonitorDetailsDto> pageQualityMonitorDetail(Page page, QualityMonitorDetails qualityMonitorDetails);


    /**
     * 导出监控计划
     *
     * @param qualityMonitorId
     * @param response
     */
    void exportQualityMonitorDetail(Integer qualityMonitorId, HttpServletResponse response);


    /************************************************************  实施  *******************************************************************/

    /**
     * 查询监控计划详情实施信息
     * @param qualityMonitorDetailsId
     * @return
     */
    QualityMonitorDetailsRatify getQualityMonitorRatify(Integer qualityMonitorDetailsId);

    /**
     * 新增监控详情实施
     * @param qualityMonitorDetailsRatify
     * @return
     */
    boolean addQualityMonitorRatify(QualityMonitorDetailsRatify qualityMonitorDetailsRatify);

    /**
     * 监控计划详情实施意见
     * @param qualityMonitorDetailsRatify
     * @return
     */
    boolean addQualityMonitorRatifyOpinion(QualityMonitorDetailsRatify qualityMonitorDetailsRatify);

    /**
     * 导出监控计划详情实施信息
     *
     * @param detailsRatifyId 监控计划详情实施id
     * @param response
     */
    void exportQualityMonitorRatify(Integer detailsRatifyId, HttpServletResponse response);


    /************************************************************  评价  *******************************************************************/

    /**
     *
     * @param qualityMonitorDetailsId
     * @return
     */
    QualityMonitorDetailsEvaluate getQualityMonitorEvaluate(Integer qualityMonitorDetailsId);

    /**
     * 新增监控评价
     * @param qualityMonitorDetailsEvaluate
     * @return
     */
    boolean addQualityMonitorEvaluate(QualityMonitorDetailsEvaluate qualityMonitorDetailsEvaluate);

    /**
     * 监控评价审批意见
     * @param qualityMonitorDetailsEvaluate
     * @return
     */
    boolean addMonitorEvaluateOpinion(QualityMonitorDetailsEvaluate qualityMonitorDetailsEvaluate);

    /**
     * 新增监控评价附件表
     * @param qualityMonitorDetailsId
     * @param file
     * @return
     */
    boolean uploadEvaluateFile(Integer qualityMonitorDetailsId, MultipartFile file);

    /**
     * 查询监控评价附件列表
     * @return
     */
    List<QualityMonitorDetailsEvaluateFile> getEvaluateFileList(Integer qualityMonitorDetailsId);


    /**
     * 导出监控评价
     * @param detailsEvaluateId 监控评价id
     */
    void exportQualityMonitorEvaluate(Integer detailsEvaluateId, HttpServletResponse response);

    /**
     * 上传监控完成报告
     * @param file
     * @param qualityMonitorDetailsId
     * @return
     */
    boolean uploadFinishReport(MultipartFile file, Integer qualityMonitorDetailsId);

    /**
     * 批准完成报告
     * @param qualityMonitorDetails
     * @return
     */
    boolean ratifyFinishReport(QualityMonitorDetails qualityMonitorDetails);
}

