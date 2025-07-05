package com.ruoyi.personnel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.personnel.dto.PersonPostAuthorizationRecordDto;
import com.ruoyi.personnel.pojo.PersonPostAuthorizationRecord;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 任职授权记录 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-10-09 10:48:17
 */
public interface PersonPostAuthorizationRecordMapper extends BaseMapper<PersonPostAuthorizationRecord> {

    IPage<PersonPostAuthorizationRecordDto> personPostAuthorizationRecordPage(Page page, @Param("departLimsId") Integer departLimsId, @Param("userId") Integer userId, @Param("userName") String userName);
}
