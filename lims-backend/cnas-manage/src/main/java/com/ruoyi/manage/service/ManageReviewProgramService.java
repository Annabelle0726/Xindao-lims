package com.ruoyi.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.manage.pojo.ManageReviewProgram;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2024-11-09 03:05:42
 */
public interface ManageReviewProgramService extends IService<ManageReviewProgram> {

    IPage<ManageReviewProgram> page(Page page,String startTime,String endTime, String judgingLocation);

    void exportReviewProgram(Integer id, HttpServletResponse response);

    int addReviewProgram(ManageReviewProgram manageReviewProgram);
}
