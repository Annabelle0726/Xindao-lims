package com.ruoyi.basic.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.basic.mapper.CertificationMapper;
import com.ruoyi.basic.pojo.Certification;
import com.ruoyi.basic.service.CertificationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CertificationServiceImpl extends ServiceImpl<CertificationMapper, Certification> implements CertificationService {


    private CertificationMapper certificationMapper;


    //查询资质明细列表
    @Override
    public IPage<Certification> getCertificationDetail(Page page, Certification certification) {
        return  certificationMapper.getCertificationDetail(page, certification);
    }

    //添加资质明细列表
    @Override
    public int addCertificationDetail(Certification certification) {
        return certificationMapper.insert(certification);
    }

    //删除资质明细列表
    @Override
    public int delCertificationDetail(String ids) {
        List<Integer> list = Arrays.stream(ids.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        return certificationMapper.deleteBatchIds(list);
    }

}
