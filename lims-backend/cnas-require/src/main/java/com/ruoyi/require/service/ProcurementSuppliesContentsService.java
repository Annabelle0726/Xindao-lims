package com.ruoyi.require.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.require.pojo.ProcurementSuppliesContents;

import java.util.List;
import java.util.Map;

public interface ProcurementSuppliesContentsService extends IService<ProcurementSuppliesContents> {

    List<ProcurementSuppliesContents> directoryListing();

    List<Map<String,Object>> getNodeNames();

    List<Map<String,Object>> getUserList();
}
