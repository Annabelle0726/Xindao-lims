package com.ruoyi.web.controller.chat.service.impl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.web.controller.chat.domain.ChatAgentFile;
import com.ruoyi.web.controller.chat.domain.FileUploadResult;
import com.ruoyi.web.controller.chat.dto.FileAnalysisDTO;
import com.ruoyi.web.controller.chat.mapper.ChatFileMapper;
import com.ruoyi.web.controller.chat.service.IChatFileService;
import org.apache.commons.io.FileUtils;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class ChatFileServiceImpl implements IChatFileService {
    private static final Logger log = LoggerFactory.getLogger(ChatFileServiceImpl.class);
    private static final int MAX_FILE_CONTENT_LENGTH = 10000; // 10KB

    @Autowired
    private ChatFileMapper chatFileMapper;

    @Value("${ruoyi.profile}")
    private String uploadPath;

    @Value("${openai.api.key}")
    private String openaiApiKey;

    @Value("${openai.api.url}")
    private String openaiApiUrl;

    @Override
    public AjaxResult uploadFile(MultipartFile file, String sessionId) {
        FileUploadResult result = new FileUploadResult();
        try {
            // 1. 文件上传
            String fileName = FileUploadUtils.upload(uploadPath, file);
            String filePath = uploadPath + "/" + fileName;

            // 2. 创建文件记录
            ChatAgentFile fileRecord = new ChatAgentFile();
            fileRecord.setFileId(UUID.randomUUID().toString());
            fileRecord.setSessionId(sessionId);
            fileRecord.setOriginalName(file.getOriginalFilename());
            fileRecord.setFilePath(filePath);
            fileRecord.setFileType(file.getContentType());
            fileRecord.setFileSize(file.getSize());
            fileRecord.setUploadTime(new Date());
            fileRecord.setUploadUser(SecurityUtils.getUsername());

            chatFileMapper.insertFile(fileRecord);

            // 3. 提取文件内容摘要
            String contentSummary = extractFileContent(file.getBytes(), MAX_FILE_CONTENT_LENGTH);
            fileRecord.setContentSummary(contentSummary);
            chatFileMapper.updateFileSummary(fileRecord);

            // 4. 构建返回结果
            result.setSuccess(true);
            result.setFileId(fileRecord.getFileId());
            result.setFileName(fileRecord.getOriginalName());
            result.setFileType(fileRecord.getFileType());
            result.setFileSize(fileRecord.getFileSize());
            result.setFilePath(fileRecord.getFilePath());
            result.setUploadTime(fileRecord.getUploadTime());
            result.setUploadUser(fileRecord.getUploadUser());
            result.setContentSummary(contentSummary);
            result.setMessage("文件上传成功");

            return AjaxResult.success("文件上传成功", result);
        } catch (Exception e) {
            log.error("文件上传失败: {}", e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("文件上传失败: " + e.getMessage());
            return AjaxResult.error(result.getMessage(), result);
        }
    }

    @Override
    public AjaxResult getFileContent(String fileId) {
        try {
            ChatAgentFile fileRecord = chatFileMapper.selectFileById(fileId);
            if (fileRecord == null) {
                return AjaxResult.error("文件不存在");
            }

            FileUploadResult result = new FileUploadResult();
            result.setFileId(fileRecord.getFileId());
            result.setFileName(fileRecord.getOriginalName());

            // 从文件系统读取内容
            String content = FileUtils.readFileToString(new File(fileRecord.getFilePath()), StandardCharsets.UTF_8);
            result.setContentSummary(content);

            return AjaxResult.success("文件内容获取成功", result);
        } catch (Exception e) {
            log.error("读取文件内容失败: {}", e.getMessage(), e);
            return AjaxResult.error("读取文件内容失败: " + e.getMessage());
        }
    }

    @Override
    public AjaxResult analyzeFileContent(FileAnalysisDTO analysisDTO) {
        try {
            ChatAgentFile fileRecord = chatFileMapper.selectFileById(analysisDTO.getFileId());
            if (fileRecord == null) {
                return AjaxResult.error("文件不存在");
            }

            FileUploadResult result = new FileUploadResult();
            result.setFileId(fileRecord.getFileId());
            result.setFileName(fileRecord.getOriginalName());

            String fileContent = FileUtils.readFileToString(new File(fileRecord.getFilePath()), StandardCharsets.UTF_8);
            String analysisResult = analyzeWithOpenAI(analysisDTO.getSessionId(), fileContent, analysisDTO.getPrompt());

            result.setContentSummary(analysisResult);
            return AjaxResult.success("文件分析完成", result);
        } catch (Exception e) {
            log.error("文件分析失败: {}", e.getMessage(), e);
            return AjaxResult.error("文件分析失败: " + e.getMessage());
        }
    }

    @Override
    public String generateChatResponse(String sessionId, String message, String context) {
        try {
            return callOpenAIChatAPI(sessionId, message, context);
        } catch (Exception e) {
            log.error("OpenAI调用失败: {}", e.getMessage(), e);
            return "请求处理失败，请稍后重试";
        }
    }

    @Override
    public AjaxResult getSessionFiles(String sessionId) {
        try {
            List<ChatAgentFile> files = chatFileMapper.selectFilesBySession(sessionId);
            return AjaxResult.success("获取会话文件成功", files);
        } catch (Exception e) {
            log.error("获取会话文件失败: {}", e.getMessage(), e);
            return AjaxResult.error("获取会话文件失败: " + e.getMessage());
        }
    }

    // =============== 私有工具方法 ===============

    private String extractFileContent(byte[] fileBytes, int maxLength) {
        try (InputStream input = new ByteArrayInputStream(fileBytes)) {
            BodyContentHandler handler = new BodyContentHandler(maxLength);
            Metadata metadata = new Metadata();
            new AutoDetectParser().parse(input, handler, metadata);

            String content = handler.toString();
            return content.substring(0, Math.min(content.length(), maxLength));
        } catch (Exception e) {
            log.warn("文件内容提取失败: {}", e.getMessage(), e);
            return "无法提取文件内容";
        }
    }

    private String analyzeWithOpenAI(String sessionId, String content, String prompt) {
        String fullPrompt = "请分析以下文件内容:\n文件内容:\n" + content + "\n\n用户要求:\n" + prompt;
        return callOpenAIChatAPI(sessionId, fullPrompt, "file-analysis");
    }

    private String callOpenAIChatAPI(String sessionId, String message, String context) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + openaiApiKey);


        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");

        List<Map<String, String>> messages = new ArrayList<>();

        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", "你是LIMS系统助手，负责分析实验室文件");
        messages.add(systemMessage);

        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", message);
        messages.add(userMessage);

        requestBody.put("messages", messages);
        requestBody.put("max_tokens", 1500);
        requestBody.put("temperature", 0.7);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                openaiApiUrl, HttpMethod.POST, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, Object> responseBody = response.getBody();
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> messageMap = (Map<String, Object>) choices.get(0).get("message");
                return (String) messageMap.get("content");
            }
        }

        log.error("OpenAI API返回错误: {}", response.getBody());
        throw new RuntimeException("OpenAI API返回错误状态: " + response.getStatusCode());
    }
}