package com.ruoyi.require.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.require.dto.ForeignRegisterDto;
import com.ruoyi.require.pojo.ForeignRegister;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 外来人员登记 服务类
 * </p>
 *
 * @author
 * @since 2024-11-19 07:17:35
 */
public interface ForeignRegisterService extends IService<ForeignRegister> {

    /**
     * 外来人员登记分页查询
     * @param page
     * @param foreignRegister
     * @return
     */
    IPage<ForeignRegisterDto> pageForeignRegister(Page page, ForeignRegisterDto foreignRegister);

    /**
     * 导出外来人员登记
     * @param foreignRegister
     */
    void exportForeignRegister(ForeignRegisterDto foreignRegister, HttpServletResponse response);
}
