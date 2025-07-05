package com.ruoyi.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.manage.pojo.ClientSatisfaction;
import com.ruoyi.manage.pojo.ClientSatisfactionAnalyseFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 客户满意度
 *
 * @author zhuo
 * @since 2024-11-09
 */
public interface ClientSatisfactionService extends IService<ClientSatisfaction> {

    /**
     * 客户满意度调查列表
     * @param page
     * @param clientSatisfaction
     * @return
     */
    IPage<ClientSatisfaction> pageClientSatisfaction(Page page, ClientSatisfaction clientSatisfaction);

    /**
     * 新增客户分析附件
     * @param file
     * @return
     */
    boolean uploadAnalyseFile(MultipartFile file);

    /**
     * 查询客户分析附件
     * @param page
     * @param analyseFile
     * @return
     */
    IPage<ClientSatisfactionAnalyseFile> pageAnalyseFile(Page page, ClientSatisfactionAnalyseFile analyseFile);

    /**
     * 客户满意度导出
     *
     * @param clientSatisfactionId
     * @param response
     */
    void exportWordClientSatisfaction(Integer clientSatisfactionId, HttpServletResponse response);

    /**
     * 确认客户满意度
     *
     * @param clientSatisfaction 要修改客户满意度的状态对象
     * @param userId             修改人id
     */
    void confirmClientSatisfaction(ClientSatisfaction clientSatisfaction, Integer userId);
}

