package com.ruoyi.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.process.pojo.ProcessEvaluate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 测量不确定度的评价 服务类
 * </p>
 *
 * @author
 * @since 2024-11-02 01:10:43
 */
public interface ProcessEvaluateService extends IService<ProcessEvaluate> {

    IPage<ProcessEvaluate> pageProcessEvaluate(Page page, ProcessEvaluate processEvaluate);

    int addProcessEvaluate(MultipartFile file);

    int doProcessEvaluate(ProcessEvaluate processEvaluate);

    void exportProcessEvaluate(ProcessEvaluate processEvaluate, HttpServletResponse response) throws Exception;
}
