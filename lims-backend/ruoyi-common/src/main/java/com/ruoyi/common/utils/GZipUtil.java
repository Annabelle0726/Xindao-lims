package com.ruoyi.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 压缩解压工具类
 */
public class GZipUtil {


    /**
     * 压缩
     * @param str
     * @return
     */
    public static String compress(String str) {
        if (str == null || str.trim().length() == 0) {
            return null;
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             GZIPOutputStream gzip = new GZIPOutputStream(out)) {
            gzip.write(str.getBytes("utf-8"));
            gzip.close();

            return new String(out.toByteArray(), "iso-8859-1");
        } catch (Exception e) {
            throw new RuntimeException("数据压缩失败");
        }

    }

    /**
     * 解压
     * @param str
     * @return
     */
    public static String uncompress(String str) {
        if (str == null || str.trim().length() == 0) {
            return null;
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes("iso-8859-1"))){
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[1024];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }

            return new String(out.toByteArray(), "utf-8");
        } catch (Exception e) {
            throw new RuntimeException("数据解压失败");
        }

    }
}
