package com.ruoyi.web.controller.chat.dto;

import java.io.Serializable;

/**
 * 文件分析请求参数
 */
public class FileAnalysisDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fileId;
    private String sessionId;
    private String prompt; // 用户自定义分析提示

    // Getters and Setters
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

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    @Override
    public String toString() {
        return "FileAnalysisDTO{" +
                "fileId='" + fileId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", prompt='" + prompt + '\'' +
                '}';
    }
}