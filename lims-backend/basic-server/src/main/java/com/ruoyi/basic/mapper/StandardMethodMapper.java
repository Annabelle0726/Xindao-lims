package com.ruoyi.basic.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.basic.pojo.StandardMethodList;
import com.ruoyi.framework.mybatis_config.MyBaseMapper;
import com.ruoyi.basic.pojo.StandardMethod;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Administrator
* @description 针对表【standard_method(标准方法)】的数据库操作Mapper
* @createDate 2024-03-03 19:21:41
* @Entity com.ruoyi.basic.pojo.StandardMethod
*/
public interface StandardMethodMapper extends MyBaseMapper<StandardMethod> {

    IPage<StandardMethod> selectStandardMethodList(Page page, @Param("ew") QueryWrapper<StandardMethod> ew);

    StandardMethod selectStandMethodById(@Param("id") Integer id);


    List<StandardMethodList> selectStandardMethodLists(@Param("tree") String tree);

    List<StandardMethodList> selectStandardMethodListsByNull(@Param("tree") String tree);

    List<StandardMethodList> selectStandardMethodLists3(@Param("tree") String tree);

    List<StandardMethodList> selectListEnum();

    Integer getStandardMethodId(@Param("code") String code);

}




