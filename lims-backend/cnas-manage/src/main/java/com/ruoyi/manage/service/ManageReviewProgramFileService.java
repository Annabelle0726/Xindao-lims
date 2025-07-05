package com.ruoyi.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.manage.pojo.ManageReviewProgramFile;
import com.ruoyi.manage.vo.ReviewProgramDetailsVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author
 * @since 2024-11-09 04:15:47
 */
public interface ManageReviewProgramFileService extends IService<ManageReviewProgramFile> {

    ReviewProgramDetailsVo selectFile(Integer id);


    void addFile(MultipartFile file,Integer id);
}
