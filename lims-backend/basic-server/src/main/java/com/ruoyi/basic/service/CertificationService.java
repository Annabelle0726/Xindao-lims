package com.ruoyi.basic.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.basic.pojo.Certification;

public interface CertificationService extends IService<Certification> {
    //查询资质明细列表
    IPage<Certification> getCertificationDetail(Page page, Certification certification);

    //添加资质明细列表
    int addCertificationDetail(Certification certification);

    //删除资质明细列表
    int delCertificationDetail(String ids);
}
