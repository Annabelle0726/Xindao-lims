package com.ruoyi.inspect.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.inspect.dto.UnqualifiedHandlerDto;
import com.ruoyi.inspect.pojo.CommonOa;
import com.ruoyi.inspect.pojo.InsUnqualifiedHandler;
import com.ruoyi.inspect.vo.UnqualifiedHandlerVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 27233
* @description 针对表【ins_unqualified_handler(不合格处理表)】的数据库操作Mapper
* @createDate 2024-07-31 13:38:14
* @Entity com.yuanchu.mom.pojo.InsUnqualifiedHandler
*/
@Mapper
public interface InsUnqualifiedHandlerMapper extends BaseMapper<InsUnqualifiedHandler> {

    IPage<UnqualifiedHandlerVO> selectPageList(IPage<UnqualifiedHandlerVO> page, @Param("ew")QueryWrapper<UnqualifiedHandlerDto> ew);

    UnqualifiedHandlerVO findById(@Param("id") Long id);

    /**
     * 查看oa流程
     * @param id
     * @return
     */
    List<CommonOa> getOaFlow(@Param("id") Integer id);
}




