package com.ruoyi.common.utils;

import com.alibaba.excel.util.IoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * 保存文件工具
 */
@Slf4j
@Component
public class FileSaveUtil {

    // 取yml中的路径 + /
    private static String FILE_PATH;

    private static String WORD_URL_PATH;

    private static String[] ALLOWED;

    @Value("${file.path}")
    private String file;

    @Value("${wordUrl}")
    private String wordUrl;

    @Value("${file.allowed}")
    private String[] allowed;

    @PostConstruct
    public void getFile() {
        FILE_PATH = this.file;
    }

    @PostConstruct
    public void getWordUrl(){
        WORD_URL_PATH = this.wordUrl;
    }

    @PostConstruct
    public void getAllowed(){
        ALLOWED = this.allowed;
    }

    /**
     * 存储文件主函数
     * @param file 文件二进制流
     * @return 返回文件名称用于存储数据库
     */
    public static String StoreFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        // 生成随机名称：时间_随机6位数字
        String FileName = System.currentTimeMillis() + "_" + getNumber(6);
        String suffix = null;
        if (originalFilename != null) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 如果后缀名不通过抛出异常
            if (!isFileAllowed(suffix)){
                throw new RuntimeException(suffix);
            }
        }
        // 名称拼接
        String fileName = FileName + suffix;
        // 进行存储
        try {
            storeFileWithFileName(file.getBytes(), fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileName;
    }

    public static Boolean DeleteFile(String fileName) {
        if (ObjectUtils.isEmpty(fileName)) {
            return false;
        }
        return FileSystemUtils.deleteRecursively(new File(FILE_PATH + "/" + fileName));
    }
    /**
     * 存储文件函数
     * @param content 文件二进制流
     * @param fileName 文件名称
     */
    private static void storeFileWithFileName(byte[] content, String fileName) {
        // 存储路径
        String path = FILE_PATH + File.separatorChar;
        // 目录不存在则创建
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        // 开始存储
        try (FileOutputStream os = new FileOutputStream(path + fileName);
             ByteArrayInputStream is = new ByteArrayInputStream(content)) {
             IoUtils.copy(is, os);
        } catch (IOException e) {
            throw new RuntimeException("文件存储格式异常");
        }
    }

    /**
     * 判断文件是否被允许上传
     *
     * @param fileName 文件名
     * @return 允许true, 否则false
     */
    private static boolean isFileAllowed(String fileName) {
        // 获取后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        for (String allow : ALLOWED) {
            if (allow.equals(suffixName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 保存文件到word文件夹里
     * @param file
     * @return
     */
    public static String uploadWordFile(MultipartFile file) {
        String urlString;
        String pathName;
        String path;
        try {
            String contentType = file.getContentType();
            if (contentType != null && contentType.startsWith("image/")) {
                // 是图片
                path = FILE_PATH;
            } else {
                // 是文件
                path = WORD_URL_PATH;
            }
            File realpath = new File(path);
            if (!realpath.exists()) {
                realpath.mkdirs();
            }
            pathName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss")) + "-" + file.getOriginalFilename();
            urlString = realpath + "/" + pathName;
            file.transferTo(new File(urlString));
            return pathName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    /**
     * 获取随机数字
     * @param n 位数
     * @return 返回随机值
     */
    public static String getNumber(int n) {
        char[] chars = "1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            char c = chars[new Random().nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }
}
