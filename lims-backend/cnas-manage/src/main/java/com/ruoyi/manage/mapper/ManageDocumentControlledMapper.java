package com.ruoyi.manage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.manage.pojo.ManageDocumentControlled;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 文件受控 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-11-08 02:54:44
 */
public interface ManageDocumentControlledMapper extends BaseMapper<ManageDocumentControlled> {

    IPage<ManageDocumentControlled> pageManageDocumentControlled(Page page, @Param("ew") QueryWrapper<ManageDocumentControlled> queryWrappers);

    ManageDocumentControlled getManageDocumentControlled(Long id);
}
