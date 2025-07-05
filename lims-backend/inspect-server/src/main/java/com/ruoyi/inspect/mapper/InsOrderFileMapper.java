package com.ruoyi.inspect.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.inspect.pojo.InsOrderFile;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 检验单下的附件列表 Mapper 接口
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-06-06 10:08:21
 */
public interface InsOrderFileMapper extends BaseMapper<InsOrderFile> {

    IPage<InsOrderFile> getFileList(@Param("page") Page page, @Param("ew") QueryWrapper<InsOrderFile> ew, @Param("insOrderId") Integer insOrderId);
}
