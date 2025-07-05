package com.ruoyi.common.utils.file;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ruoyi.common.core.domain.MinioResult;
import com.ruoyi.common.exception.UtilException;
import com.ruoyi.common.exception.file.InvalidExtensionException;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class MinioUtils {
    @Resource
    private MinioClient minioClient;

    @Value("${minio.preview-expiry}")
    private Integer previewExpiry;

    /**
     * 判断存储桶是否存在，不存在则创建
     *
     * @param bucketName 存储桶名称
     */
    public void existBucket(String bucketName) {
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建存储桶
     *
     * @param bucketName 存储桶名称
     * @return 是否创建成功
     */
    public Boolean makeBucket(String bucketName) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 删除存储桶
     *
     * @param bucketName 存储桶名称
     * @return 是否删除成功
     */
    public Boolean removeBucket(String bucketName) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断对象是否存在
     *
     * @param bucketName 存储桶名称
     * @param originalFileName MinIO中存储对象全路径
     * @return 对象是否存在
     */
    public boolean existObject(String bucketName, String originalFileName) {
        try {
            minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(originalFileName).build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 文件上传
     *
     * @param bucketName 存储桶名称
     * @param file       文件
     * @return 桶中位置
     */
    public MinioResult upload(String bucketName, MultipartFile file, Boolean isPreviewExpiry) throws InvalidExtensionException {
        MultipartFile[] fileArr = {file};
        List<MinioResult> fileNames = upload(bucketName, fileArr, isPreviewExpiry);
        return fileNames.isEmpty() ? null : fileNames.get(0);
    }

    /**
     * 上传文件
     *
     * @param bucketName 存储桶名称
     * @param fileList   文件列表
     * @return 桶中位置列表
     */
    public List<MinioResult> upload(String bucketName, List<MultipartFile> fileList, Boolean isPreviewExpiry) throws InvalidExtensionException {
        MultipartFile[] fileArr = fileList.toArray(new MultipartFile[0]);
        return upload(bucketName, fileArr, isPreviewExpiry);
    }

    /**
     * description: 上传文件
     *
     * @param bucketName 存储桶名称
     * @param fileArr    文件列表
     * @return 桶中位置列表
     */
    public List<MinioResult> upload(String bucketName, MultipartFile[] fileArr, Boolean isPreviewExpiry) throws InvalidExtensionException {
        for (MultipartFile file : fileArr) {
            FileUploadUtils.assertAllowed(file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        }
        // 保证桶一定存在
        existBucket(bucketName);
        // 执行正常操作
        List<MinioResult> bucketFileNames = new ArrayList<>(fileArr.length);
        for (MultipartFile file : fileArr) {
            // 获取原始文件名称
            String originalFileName = file.getOriginalFilename();
            // 获取当前日期，格式例如：2020-11
            String datePath = new SimpleDateFormat("yyyy-MM").format(new Date());
            // 文件名称
            String uuid = IdWorker.get32UUID();
            // 获取文件后缀
            String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
            String bucketFilePath = datePath + "/" + uuid + suffix;

            // 推送文件到MinIO
            try (InputStream in = file.getInputStream()) {
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(bucketFilePath)
                        .stream(in, in.available(), -1)
                        .contentType(file.getContentType())
                        .build()
                );
            } catch (Exception e) {
                throw new UtilException("MinioUtils：上传文件工具类异常");
            }
            MinioResult minioResult = new MinioResult();
            minioResult.setBucketFileName(bucketFilePath);
            // 返回永久预览地址
            if (isPreviewExpiry) {
                String previewUrl = getPreviewUrl(bucketFilePath, bucketName, isPreviewExpiry);
                minioResult.setPreviewExpiry(previewUrl);
            }
            minioResult.setOriginalName(originalFileName);
            bucketFileNames.add(minioResult);
        }
        return bucketFileNames;
    }

    /**
     * 文件下载
     *
     * @param bucketName       存储桶名称
     * @param bucketFileName   桶中文件名称
     * @param originalFileName 原始文件名称
     * @param response         response对象
     */
    public void download(String bucketName, String bucketFileName, String originalFileName, HttpServletResponse response) {
        GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(bucketName).object(bucketFileName).build();
        try (GetObjectResponse objResponse = minioClient.getObject(objectArgs)) {
            byte[] buf = new byte[1024];
            int len;
            try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
                while ((len = objResponse.read(buf)) != -1) {
                    os.write(buf, 0, len);
                }
                os.flush();
                byte[] bytes = os.toByteArray();
                response.setCharacterEncoding("utf-8");
                //设置强制下载不打开
                response.setContentType("application/force-download");
                // 设置附件名称编码
                originalFileName = new String(originalFileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
                // 设置附件名称
                response.addHeader("Content-Disposition", "attachment;fileName=" + originalFileName);
                // 写入文件
                try (ServletOutputStream stream = response.getOutputStream()) {
                    stream.write(bytes);
                    stream.flush();
                }
            }
        } catch (Exception e) {
            throw new UtilException("MinioUtils：上传文件工具类异常");
        }
    }

    /**
     * 获取已上传对象的文件流（后端因为业务需要获取文件流可以调用该方法）
     *
     * @param bucketName     存储桶名称
     * @param bucketFileName 桶中文件名称
     * @return 文件流
     */
    public InputStream getFileStream(String bucketName, String bucketFileName) throws Exception {
        GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(bucketName).object(bucketFileName).build();
        return minioClient.getObject(objectArgs);
    }

    /**
     * 批量删除文件对象结果
     *
     * @param bucketName      存储桶名称
     * @param bucketFileName 桶中文件名称
     * @return 删除结果
     */
    public DeleteError removeObjectsResult(String bucketName, String bucketFileName) {
        List<DeleteError> results = removeObjectsResult(bucketName, Collections.singletonList(bucketFileName));
        return !results.isEmpty() ? results.get(0) : null;
    }

    /**
     * 批量删除文件对象结果
     *
     * @param bucketName      存储桶名称
     * @param bucketFileNames 桶中文件名称集合
     * @return 删除结果
     */
    public List<DeleteError> removeObjectsResult(String bucketName, List<String> bucketFileNames) {
        Iterable<Result<DeleteError>> results = removeObjects(bucketName, bucketFileNames);
        List<DeleteError> res = new ArrayList<>();
        for (Result<DeleteError> result : results) {
            try {
                res.add(result.get());
            } catch (Exception e) {
                throw new UtilException("MinioUtils：上传文件工具类异常");
            }
        }
        return res;
    }

    /**
     * 批量删除文件对象
     *
     * @param bucketName      存储桶名称
     * @param bucketFileNames 桶中文件名称集合
     */
    private Iterable<Result<DeleteError>> removeObjects(String bucketName, List<String> bucketFileNames) {
        List<DeleteObject> dos = bucketFileNames.stream().map(DeleteObject::new).collect(Collectors.toList());
        return minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(bucketName).objects(dos).build());
    }

    /**
     * 查询预览url
     * @param bucketFileName minio文件名称
     * @param bucketName 存储桶名称
     * @param isPreviewExpiry 是否需要过期时间 默认24小时
     * @return
     */
    public String getPreviewUrl(String bucketFileName, String bucketName, Boolean isPreviewExpiry) {
        if (StringUtils.isNotBlank(bucketFileName)) {
            try {
                minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(bucketFileName).build());
                // 为false只生成24小时有效时长的url链接，可以访问该文件
                if (isPreviewExpiry){
                    return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(bucketName).object(bucketFileName).build());
                }else {
                    return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(bucketName).object(bucketFileName).expiry(previewExpiry, TimeUnit.HOURS).build());
                }
            } catch (Exception e) {
                throw new UtilException("MinioUtils：上传文件工具类异常");
            }
        }
        return null;
    }
}
