package com.ruoyi.basic.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.basic.pojo.Laboratory;
import com.ruoyi.basic.pojo.Seal;

import java.util.List;
import java.util.Map;

public interface SealService extends IService<Seal> {

    //新增
    int addSeal(Seal seal);

    //查询
    IPage<Seal> selectSeal(Page page, Seal seal);

    List<Laboratory>  Laboratory(Integer id);


}
