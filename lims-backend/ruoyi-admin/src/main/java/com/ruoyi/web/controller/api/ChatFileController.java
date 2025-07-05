package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.controller.chat.dto.ChatQueryDTO;
import com.ruoyi.web.controller.chat.dto.FileAnalysisDTO;
import com.ruoyi.web.controller.chat.service.IChatFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/chat/api")
public class ChatFileController extends BaseController {

    @Autowired
    private IChatFileService chatFileService;

    /**
     * 文件上传接口
     */
    @PostMapping("/upload")
    public AjaxResult uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam String sessionId) {
        return chatFileService.uploadFile(file, sessionId);
    }

    /**
     * 获取文件内容
     */
    @GetMapping("/content/{fileId}")
    public AjaxResult getFileContent(@PathVariable String fileId) {
        return chatFileService.getFileContent(fileId);
    }

    /**
     * 分析文件内容
     */
    @PostMapping("/analyze")
    public AjaxResult analyzeFileContent(@RequestBody FileAnalysisDTO analysisDTO) {
        return chatFileService.analyzeFileContent(analysisDTO);
    }

    /**
     * 获取会话文件列表
     */
    @GetMapping("/files/{sessionId}")
    public AjaxResult getSessionFiles(@PathVariable String sessionId) {
        return chatFileService.getSessionFiles(sessionId);
    }

    /**
     * 生成聊天响应
     */
    @PostMapping("/generate")
    public AjaxResult generateResponse(@RequestBody ChatQueryDTO query) {
        String response = chatFileService.generateChatResponse(
                query.getSessionId(),
                query.getMessage(),
                query.getContext()
        );
        return AjaxResult.success("响应生成成功", response);
    }
}