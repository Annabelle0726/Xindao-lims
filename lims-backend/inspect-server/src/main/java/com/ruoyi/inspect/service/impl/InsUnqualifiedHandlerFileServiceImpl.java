package com.ruoyi.inspect.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.inspect.mapper.InsUnqualifiedHandlerFileMapper;
import com.ruoyi.inspect.pojo.InsUnqualifiedHandlerFile;
import com.ruoyi.inspect.service.InsUnqualifiedHandlerFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
* @author 27233
* @description 针对表【ins_unqualified_handler_file(不合格处理附件记录表)】的数据库操作Service实现
* @createDate 2024-07-31 13:38:38
*/
@Service
@Slf4j
public class InsUnqualifiedHandlerFileServiceImpl extends ServiceImpl<InsUnqualifiedHandlerFileMapper, InsUnqualifiedHandlerFile>
    implements InsUnqualifiedHandlerFileService {

    @Value("${wordUrl}")
    private String wordUrl;

    @Value("${file.path}")
    private String imgUrl;

    @Override
    public InsUnqualifiedHandlerFile uploadFile(Long handlerId, MultipartFile file) {
        String urlString;
        String pathName;
        String path;
        String prefix;
        String filename = file.getOriginalFilename();
        String contentType = file.getContentType();
        InsUnqualifiedHandlerFile unqualifiedHandlerFile = new InsUnqualifiedHandlerFile();
//        unqualifiedHandlerFile.setUnqualifiedId(handlerId);
        unqualifiedHandlerFile.setFileName(filename);
        if (contentType != null && contentType.startsWith("image/")) {
            // 是图片
            path = imgUrl;
            prefix = "/image/";
            unqualifiedHandlerFile.setType(1);
        } else {
            // 是文件
            path = wordUrl;
            prefix = "/word/";
            unqualifiedHandlerFile.setType(2);
        }
        try {
            File realpath = new File(path);
            if (!realpath.exists()) {
                realpath.mkdirs();
            }
            pathName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            urlString = realpath + "/" + pathName;
            file.transferTo(new File(urlString));
            unqualifiedHandlerFile.setFileUrl(pathName);
            baseMapper.insert(unqualifiedHandlerFile);
            return unqualifiedHandlerFile;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("附件上传错误");
            throw new ErrorException(e.getMessage());
        }
    }

    @Override
    public void downloadOAFile(Long handlerFileId, HttpServletResponse response) {
        response.reset();
        String fileName;
        try {
            //查询上传附件记录
            InsUnqualifiedHandlerFile file = baseMapper.selectById(handlerFileId);
            if (file != null){
                fileName = file.getFileName();
                fileName =  URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Cache-Control", "no-cache");
                response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
                response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
                //获取文件夹路径
                String path = file.getType()==1?imgUrl:wordUrl;
                //文件完整路径
                String fullPath = path + file.getFileUrl();
                //判断文件是否存在
                File fullFile = new File(fullPath);
                if(fullFile.exists()){
                    IoUtil.copy(Files.newInputStream(fullFile.toPath()),response.getOutputStream());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}




