package com.ruoyi.common.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IndustryChainUtils {
    private static final String CLOUD_LOGIN = "https://gym.ztt.cn:1443/openApi/v1/user/login";
    private static final String CLOUD_REQUEST = "https://gym.ztt.cn:1443/openApi/codeGenerate/getCodeDetailByOrder";
    //请求表单账号
    private static final String USERNAME = "ZTKJ-N002096";
    //请求表单密码
    private static final String PASSWORD = "Abc12345@";
    //Base64格式编码
    private static final String PWDBASE64 = Base64.encode(PASSWORD);

    //获取token
    public static String getToken() {
        HttpRequest request = HttpRequest.post(CLOUD_LOGIN)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .form("username", USERNAME)
                .form("password", PWDBASE64);
        HttpResponse response = request.execute();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(response.body());
            //通过返回数据获取token
            String accessToken = jsonNode.get("data").get("token").asText();
            return accessToken;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    //输出所有数据
    public static String getIndustryChainAll(String orderNo, String lineNum, String releaseNum) {
        String token = getToken();
        HttpRequest request = HttpRequest.get(CLOUD_REQUEST)
                .header("Authorization", "Bearer " + token)
                .form("orderNo", orderNo)
                .form("lineNum", lineNum)
                .form("releaseNum", releaseNum);
        HttpResponse response = request.execute();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(response.body());
            return jsonNode.toString();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     *
     * @param orderNo  订单号
     * @param lineNum  行号
     * @param releaseNum  下达号
     * @return
     */
    public static String getIndustryChainAttrFields(String orderNo, String lineNum, String releaseNum) {
        String token = getToken();
        HttpRequest request = HttpRequest.get(CLOUD_REQUEST)
                .header("Authorization", "Bearer " + token)
                .form("orderNo", orderNo)
                .form("lineNum", lineNum)
                .form("releaseNum", releaseNum);
        HttpResponse response = request.execute();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(response.body());
            // 判断是否通过
            if (jsonNode.get("code").asInt() != 200) {
                return null;
            }
            JsonNode data = jsonNode.get("data");
            for (JsonNode dataNode : data) {
                //获取attrFields数据
                String attrFields = dataNode.get("attrFields").toPrettyString();
                return attrFields;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


}

