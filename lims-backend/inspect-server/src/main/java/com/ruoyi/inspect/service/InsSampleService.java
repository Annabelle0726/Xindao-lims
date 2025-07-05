package com.ruoyi.inspect.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.inspect.pojo.InsSample;

import java.util.List;

/**
* @author Administrator
* @description 针对表【ins_sample(检验样品)】的数据库操作Service
* @createDate 2024-03-14 17:14:57
*/
public interface InsSampleService extends IService<InsSample> {

    /**
     * 根据订单id查询样品
     * @param insOrderId
     * @return
     */
    List<InsSample> getSampleByOrderId(Integer insOrderId);
}
