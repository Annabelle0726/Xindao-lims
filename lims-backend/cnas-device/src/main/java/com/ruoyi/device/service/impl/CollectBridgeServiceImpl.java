package com.ruoyi.device.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.device.mapper.CollectBridgeMapper;
import com.ruoyi.device.pojo.CollectBridge;
import com.ruoyi.device.service.CollectBridgeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 数字电桥采集
 *
 * @author zhuo
 * @since 2025-02-19
 */
@Service
@Slf4j
public class CollectBridgeServiceImpl extends ServiceImpl<CollectBridgeMapper, CollectBridge> implements CollectBridgeService {

    /**
     * 填充采集数据
     * @param jsonObject
     */
    @Override
    public void addBridgeValue(JSONObject jsonObject) {
        JSONArray dataArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < dataArray.size(); i++) {
            JSONObject listInfo = dataArray.getJSONObject(i);
            // 存储数据
            String dataStream = listInfo.getString("dataStream");
            if (dataStream.equals("DQCS.DQCS.SN")) {
                JSONArray dataPoints = listInfo.getJSONArray("dataPoints");
                JSONObject pointsJSONObject = dataPoints.getJSONObject(0);
                String entrustCode = pointsJSONObject.getString("value");
                // 解析时间戳
                Instant instant = Instant.ofEpochMilli(pointsJSONObject.getLong("time"));
                LocalDateTime collectDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

                // 先存储编号, 后续存储值
                CollectBridge collectBridge = new CollectBridge();
                collectBridge.setEntrustCode(entrustCode);
                collectBridge.setCollectDate(collectDate);
                baseMapper.insert(collectBridge);

            }
            // 寄存器地址等于64获取结果值
            if (dataStream.equals("DQCS.DQCS.64")) {
                JSONArray dataPoints = listInfo.getJSONArray("dataPoints");
                JSONObject pointsJSONObject = dataPoints.getJSONObject(0);
                String value = pointsJSONObject.getString("value");

                if (value.equals("64")) {
                    for (int j = 0; j < dataArray.size(); j++) {
                        JSONObject listInfo2 = dataArray.getJSONObject(j);
                        String dataStream2 = listInfo2.getString("dataStream");
                        // 寄存器地址等于64获取结果值
                        if (dataStream2.equals("DQCS.DQCS.DZZ")) {
                            JSONArray dataPoints2 = listInfo2.getJSONArray("dataPoints");
                            JSONObject pointsJSONObject2 = dataPoints2.getJSONObject(0);
                            String collectValue = pointsJSONObject2.getString("value");

                            // 解析时间戳
                            Instant instant = Instant.ofEpochMilli(pointsJSONObject2.getLong("time"));
                            LocalDateTime collectDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

                            // 查询最新一条数据
                            CollectBridge collectBridge = baseMapper.selectOne(Wrappers.<CollectBridge>lambdaQuery()
                                    .orderByDesc(CollectBridge::getCollectDate)
                                    .last("limit 1"));

                            // 判断两条数据是否相差在10分钟之内和有没有编号
                            if (isWithinTenMinutes(collectDate, collectBridge.getCollectDate()) &&
                                    StringUtils.isNotBlank(collectBridge.getEntrustCode())) {
                                // 修改检验值
                                collectBridge.setCollectValue(collectValue);
                                baseMapper.updateById(collectBridge);
                            } else {
                                // 只存储值
                                CollectBridge bridge = new CollectBridge();
                                bridge.setCollectValue(collectValue);
                                bridge.setCollectDate(collectDate);
                                baseMapper.insert(bridge);
                            }
                        }
                    }
                }
            }
        }
    }

    public static boolean isWithinTenMinutes(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        Duration duration = Duration.between(dateTime1, dateTime2);
        long minutesDifference = Math.abs(duration.toMinutes());
        return minutesDifference <= 10;
    }

}

