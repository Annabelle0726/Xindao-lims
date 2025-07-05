package com.ruoyi.device.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.device.pojo.CollectBridge;

/**
 * 数字电桥采集
 *
 * @author zhuo
 * @since 2025-02-19
 */
public interface CollectBridgeService extends IService<CollectBridge> {

    /**
     * 填充采集数据
     * @param jsonObject
     */
    void addBridgeValue(JSONObject jsonObject);
}

