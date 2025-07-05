package com.ruoyi.basic.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.basic.pojo.Certification;
import org.apache.ibatis.annotations.Param;

public interface CertificationMapper extends BaseMapper<Certification> {

    //查询资质明细列表
    IPage<Certification> getCertificationDetail(Page page, @Param("param") Certification certification);
}
