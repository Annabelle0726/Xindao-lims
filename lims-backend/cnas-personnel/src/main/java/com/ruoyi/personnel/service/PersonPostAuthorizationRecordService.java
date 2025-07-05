package com.ruoyi.personnel.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.personnel.dto.PersonPostAuthorizationRecordDto;
import com.ruoyi.personnel.pojo.PersonPostAuthorizationRecord;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 任职授权记录 服务类
 * </p>
 *
 * @author 
 * @since 2024-10-09 10:48:17
 */
public interface PersonPostAuthorizationRecordService extends IService<PersonPostAuthorizationRecord> {

    IPage<PersonPostAuthorizationRecordDto> personPostAuthorizationRecordPage(Page page,
                                                                              Integer departLimsId,
                                                                              Integer userId,
                                                                              String userName);

    void exportPersonPostAuthorizationRecord(Integer id, HttpServletResponse response);
}
