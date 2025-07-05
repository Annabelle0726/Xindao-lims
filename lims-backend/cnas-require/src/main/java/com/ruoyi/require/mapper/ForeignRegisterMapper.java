package com.ruoyi.require.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.require.dto.ForeignRegisterDto;
import com.ruoyi.require.pojo.ForeignRegister;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 外来人员登记 Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-11-19 07:17:35
 */
public interface ForeignRegisterMapper extends BaseMapper<ForeignRegister> {

    /**
     * 外来人员登记分页查询
     * @return
     */
    IPage<ForeignRegisterDto> pageForeignRegister(Page page, @Param("ew") QueryWrapper<ForeignRegisterDto> ew,
                                                  @Param("beginDate") String beginDate,
                                                  @Param("endDate") String endDate);

    /**
     * 查询外来人员登记列表
     * @param foreignRegister
     * @return
     */
    List<ForeignRegisterDto> getForeignRegisterList(@Param("ew") QueryWrapper<ForeignRegisterDto> ew,
                                                    @Param("beginDate") String beginDate,
                                                    @Param("endDate") String endDate);
}
