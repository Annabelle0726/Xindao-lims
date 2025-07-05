package com.ruoyi.web.controller.chat.domain;

import com.ruoyi.common.annotation.Excel;
import java.util.Date;

/**
 * ChatAgent文件存储表
 */
public class ChatAgentFile {
    private static final long serialVersionUID = 1L;

    private String fileId;

    @Excel(name = "会话ID")
    private String sessionId;

    @Excel(name = "原始文件名")
    private String originalName;

    @Excel(name = "存储路径")
    private String filePath;

    @Excel(name = "文件类型")
    private String fileType;

    @Excel(name = "文件大小")
    private Long fileSize;

    @Excel(name = "内容摘要")
    private String contentSummary;

    @Excel(name = "上传时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date uploadTime;

    @Excel(name = "上传用户")
    private String uploadUser;

    // =============== Getters and Setters ===============
    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getContentSummary() {
        return contentSummary;
    }

    public void setContentSummary(String contentSummary) {
        this.contentSummary = contentSummary;
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
}