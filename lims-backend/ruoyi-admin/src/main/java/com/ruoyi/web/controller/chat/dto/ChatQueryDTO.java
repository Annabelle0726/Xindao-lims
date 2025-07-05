package com.ruoyi.web.controller.chat.dto;

import java.io.Serializable;

/**
 * 聊天查询数据传输对象
 */
public class ChatQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String sessionId;
    private String message;
    private String context; // JSON格式的上下文

    // Getters and Setters
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "ChatQueryDTO{" +
                "sessionId='" + sessionId + '\'' +
                ", message='" + message + '\'' +
                ", context='" + context + '\'' +
                '}';
    }
}