package com.ruoyi.common.utils.api;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.config.IfsProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author zhuo
 * @Date 2024/9/29
 */
@Slf4j
@AllArgsConstructor
@Component
public class IfsApiUtils {

    private IfsProperties ifsProperties;


    /**
     * 查询采购订单未检验
     */
    public List<Map<String, Object>> getInventory(String inAttr) {
        String procedureName = "QUERY_POL_RECEIPT_STD";
        JSONObject stockMap = getJsonObject(procedureName, inAttr);
        String body = HttpRequest.post(ifsProperties.getCustorderPort()).form(stockMap).execute().body();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map map = objectMapper.readValue(body, Map.class);
            List<Map<String, Object>> maps = objectMapper.readValue(JSONUtil.toJsonStr(map.get("LIST_INFO")), new TypeReference<List<Map<String, Object>>>() {
            });
            log.info("获取采购订单-->>" + maps);
            return maps;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }



    /**
     * 登记采购检验结果STD
     */
    public Result getProcurementResults(String inAttr) {
        return getResult("REGIST_INSPECTION_RESULTS_STD", inAttr, ifsProperties.getCustorder(), "登记采购检验结果-->");
    }


    /**
     * 原材料检验-检验后移库
     *
     * @param inAttr
     * @return
     */
    public Result moveReceipt(String inAttr) {
        return getResult("MOVE_RECEIPT_STD", inAttr, ifsProperties.getCustorder(), "检验后移库-->");
    }


    /**
     * 原材料检验-采购接收更改批号
     *
     * @param inAttr
     * @return
     */
    public Result updateMoveReceiptLot(String inAttr) {
        return getResult("MODIFY_PURCH_RECEIPT_LOT_STD", inAttr, ifsProperties.getCustorder(), "修改采购订单批次号-->");
    }



    /**
     * 查询库存数据
     *
     * @param inAttr
     * @return
     */
    public Result getIfsStock(String inAttr) {
        return getResult("QUERY_INVENTORY_INFO_STD", inAttr, ifsProperties.getCustorderPort(), "查询库存信息-->");
    }


    /**
     * 通用接口
     * @param procedureName 方法名
     * @param inAttr  传参
     * @param url 地址
     * @param image 消息
     * @return
     */
    private Result<?> getResult(String procedureName, String inAttr, String url, String image) {
        JSONObject stockMap = new JSONObject();
        stockMap.put("procedureName", procedureName);
        stockMap.put("contract", ifsProperties.getContract());
        stockMap.put("contractKey", ifsProperties.getContractKeyPost());
        stockMap.put("userId", "lims_user");
        stockMap.put("inAttr", inAttr);
        String body = HttpRequest.post(url).form(stockMap).execute().body();
        log.info(image + body);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map map = objectMapper.readValue(body, Map.class);
            String successFlag = map.get("SuccessFlag").toString();
            if (Integer.valueOf(successFlag) == 1) {
                return Result.success(map);
            } else {
                return Result.fail(map.get("ErrorMsg").toString());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 获取添加请求信息
     * @param procedureName      结构名称
     * @param inAttr             请求参数
     * @return
     */
    private JSONObject getJsonObject(String procedureName, String inAttr) {
        JSONObject stockMap = new JSONObject();
        stockMap.put("procedureName", procedureName);
        stockMap.put("contract", ifsProperties.getContract());
        stockMap.put("contractKey", ifsProperties.getContractKeyPost());
        stockMap.put("userId", "lims_user");
        stockMap.put("inAttr", inAttr);
        return stockMap;
    }
}
