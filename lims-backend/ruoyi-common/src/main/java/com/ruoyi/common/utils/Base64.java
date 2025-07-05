package com.ruoyi.common.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;

public class Base64 {
    /**
     * base64 编码（方法一）
     * @explain DatatypeConverter.java实现
     * @param str 待编码字符串
     * @return 编码字符串
     */
    public static String encode(String str) {
        // base64字符串
        String base64Str = "";
        // 非字符串才进行编码
        if (StringUtils.isNotBlank(str)) {
            // String 转 byte[]
            byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
            // 编码
            base64Str = DatatypeConverter.printBase64Binary(bytes);
        }
        return base64Str;
    }
}
