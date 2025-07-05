package com.ruoyi.manage.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.manage.mapper.ManageReviewProgramFileMapper;
import com.ruoyi.manage.mapper.ManageReviewProgramMapper;
import com.ruoyi.manage.pojo.ManageReviewProgram;
import com.ruoyi.manage.pojo.ManageReviewProgramFile;
import com.ruoyi.manage.service.ManageReviewProgramFileService;
import com.ruoyi.manage.vo.ReviewProgramDetailsVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-09 04:15:47
 */
@Service
public class ManageReviewProgramFileServiceImpl extends ServiceImpl<ManageReviewProgramFileMapper, ManageReviewProgramFile> implements ManageReviewProgramFileService {

    @Value("${wordUrl}")
    private String wordUrl;

    @Resource
    ManageReviewProgramMapper manageReviewProgramMapper;

    @Override
    public ReviewProgramDetailsVo selectFile(Integer id) {
        ManageReviewProgram program = manageReviewProgramMapper.selectById(id);
        LambdaQueryWrapper<ManageReviewProgramFile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ManageReviewProgramFile::getReviewId, id);
        List<ManageReviewProgramFile> files = list(queryWrapper);
        ReviewProgramDetailsVo vo = new ReviewProgramDetailsVo();
        vo.setProgram(program);
        vo.setFileList(files);
        return vo;
    }

    @Override
    public void addFile(MultipartFile file, Integer id) {
        String urlString;
        String pathName;
        String path;
        String filename = file.getOriginalFilename();
        ManageReviewProgramFile manageReviewProgramFile = new ManageReviewProgramFile();
        manageReviewProgramFile.setFileName(filename);
        manageReviewProgramFile.setReviewId(id);
        // 是文件
        path = wordUrl;
        try {
            File realpath = new File(path);
            if (!realpath.exists()) {
                realpath.mkdirs();
            }
            pathName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            urlString = realpath + "/" + pathName;
            file.transferTo(new File(urlString));
            manageReviewProgramFile.setUrl(pathName);
            this.baseMapper.insert(manageReviewProgramFile);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("附件上传错误");
        }

    }
}
