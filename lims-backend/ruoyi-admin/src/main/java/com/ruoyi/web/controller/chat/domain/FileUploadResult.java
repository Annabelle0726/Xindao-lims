package com.ruoyi.web.controller.chat.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件上传结果对象
 */
public class FileUploadResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean success;
    private String fileId;
    private String fileName;
    private String fileType;
    private long fileSize;
    private String filePath;
    private Date uploadTime;
    private String uploadUser;
    private String message;
    private String contentSummary;

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getUploadUser() {
        return uploadUser;
    }

    public void setUploadUser(String uploadUser) {
        this.uploadUser = uploadUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContentSummary() {
        return contentSummary;
    }

    public void setContentSummary(String contentSummary) {
        this.contentSummary = contentSummary;
    }

    @Override
    public String toString() {
        return "FileUploadResult{" +
                "success=" + success +
                ", fileId='" + fileId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileSize=" + fileSize +
                ", filePath='" + filePath + '\'' +
                ", uploadTime=" + uploadTime +
                ", uploadUser='" + uploadUser + '\'' +
                ", message='" + message + '\'' +
                ", contentSummary='" + contentSummary + '\'' +
                '}';
    }
}