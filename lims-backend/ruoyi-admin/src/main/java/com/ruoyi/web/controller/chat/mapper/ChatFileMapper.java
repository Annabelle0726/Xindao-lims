package com.ruoyi.web.controller.chat.mapper;

import com.ruoyi.web.controller.chat.domain.ChatAgentFile;
import java.util.List;

public interface ChatFileMapper {
    int insertFile(ChatAgentFile file);
    int deleteFileById(String fileId);
    ChatAgentFile selectFileById(String fileId);
    List<ChatAgentFile> selectFilesBySession(String sessionId);
    int updateFileSummary(ChatAgentFile file);
}