package com.ruoyi.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.process.dto.ProcessComplainDto;
import com.ruoyi.process.pojo.ProcessComplain;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 投诉 服务类
 * </p>
 *
 * @author
 * @since 2024-11-02 09:29:11
 */
public interface ProcessComplainService extends IService<ProcessComplain> {

    IPage<ProcessComplain> pageProcessComplain(Page page, ProcessComplain processComplain);

    int addProcessComplain(ProcessComplain processComplain);

    ProcessComplainDto getProcessComplain(Long id);

    int doProcessComplain(ProcessComplain processComplain);

    void exportProcessComplain(ProcessComplain processComplain, HttpServletResponse response) throws Exception;
}
