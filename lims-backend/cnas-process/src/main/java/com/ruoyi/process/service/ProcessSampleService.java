package com.ruoyi.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.process.pojo.ProcessSample;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 样品接收 服务类
 * </p>
 *
 * @author
 * @since 2024-12-12 05:02:49
 */
public interface ProcessSampleService extends IService<ProcessSample> {

    IPage<ProcessSample> pageProcessSample(Page page, ProcessSample processSample);

    void exportProcessSample(ProcessSample processSample, HttpServletResponse response);
}
