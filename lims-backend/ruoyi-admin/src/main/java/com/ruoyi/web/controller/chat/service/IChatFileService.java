package com.ruoyi.web.controller.chat.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.controller.chat.dto.FileAnalysisDTO;
import org.springframework.web.multipart.MultipartFile;

public interface IChatFileService {
    AjaxResult uploadFile(MultipartFile file, String sessionId);
    AjaxResult getFileContent(String fileId);
    AjaxResult analyzeFileContent(FileAnalysisDTO analysisDTO);
    AjaxResult getSessionFiles(String sessionId);
    String generateChatResponse(String sessionId, String message, String context);
}