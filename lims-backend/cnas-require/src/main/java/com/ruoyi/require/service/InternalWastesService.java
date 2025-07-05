package com.ruoyi.require.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.require.dto.InternalWastesDto;
import com.ruoyi.require.pojo.InternalWastes;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 安全内务三废登记 服务类
 * </p>
 *
 * @author
 * @since 2024-11-19 06:39:27
 */
public interface InternalWastesService extends IService<InternalWastes> {

    /**
     * 安全内务三废处理分页查询
     * @param page
     * @param internalWastes
     * @return
     */
    IPage<InternalWastesDto> pageInternalWastes(Page page, InternalWastes internalWastes);

    /**
     * 安全内务三废处理新增
     * @param internalWastes
     * @return
     */
    boolean addInternalWastes(InternalWastesDto internalWastes);

    /**
     * 安全内务三废处理修改
     * @param internalWastes
     * @return
     */
    boolean updateInternalWastes(InternalWastesDto internalWastes);

    /**
     * 安全内务三废处理删除
     * @param wastesId
     * @return
     */
    boolean delInternalWastes(Integer wastesId);

    /**
     * 安全内务三废处理查看详情
     * @param wastesId
     * @return
     */
    InternalWastesDto getInternalWastesOne(Integer wastesId);

    /**
     * 导出三废处理
     * @param wastesId
     * @param response
     */
    void exportInternalWastes(Integer wastesId, HttpServletResponse response);
}
