package com.ruoyi.inspect.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.inspect.mapper.InsSampleMapper;
import com.ruoyi.inspect.pojo.InsSample;
import com.ruoyi.inspect.service.InsSampleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Administrator
* @description 针对表【ins_sample(检验样品)】的数据库操作Service实现
* @createDate 2024-03-14 17:14:57
*/
@Service
public class InsSampleServiceImpl extends ServiceImpl<InsSampleMapper, InsSample>
    implements InsSampleService {

    /**
     * 根据订单id查询样品
     * @param insOrderId
     * @return
     */
    @Override
    public List<InsSample> getSampleByOrderId(Integer insOrderId) {
        return baseMapper.getSampleByOrderId(insOrderId);
    }
}




