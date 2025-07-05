package com.ruoyi.inspect.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.inspect.pojo.InsUnqualifiedHandlerFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
* @author 27233
* @description 针对表【ins_unqualified_handler_file(不合格处理附件记录表)】的数据库操作Service
* @createDate 2024-07-31 13:38:38
*/
public interface InsUnqualifiedHandlerFileService extends IService<InsUnqualifiedHandlerFile> {

    InsUnqualifiedHandlerFile uploadFile(Long handlerId, MultipartFile file);

    void downloadOAFile(Long handlerFileId, HttpServletResponse response);
}
