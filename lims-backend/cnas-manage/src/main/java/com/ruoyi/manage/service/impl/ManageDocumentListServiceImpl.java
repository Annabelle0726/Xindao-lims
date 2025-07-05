package com.ruoyi.manage.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.UUID;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.manage.mapper.ManageDocumentListMapper;
import com.ruoyi.manage.pojo.ManageDocumentList;
import com.ruoyi.manage.service.ManageDocumentListService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 文件清单
 * 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-08 11:08:11
 */
@Service
public class ManageDocumentListServiceImpl extends ServiceImpl<ManageDocumentListMapper, ManageDocumentList> implements ManageDocumentListService {

    @Resource
    private ManageDocumentListMapper manageDocumentListMapper;



    @Value("${wordUrl}")
    private String wordUrl;

    @Override
    public IPage<ManageDocumentList> pageManageDocumentList(Page page, ManageDocumentList manageDocumentList) {
        return manageDocumentListMapper.pageManageDocumentList(page, QueryWrappers.queryWrappers(manageDocumentList));
    }


    @Override
    public int uploadFile(Integer id, MultipartFile file) {
        String urlString;
        String pathName;
        String path;
        ManageDocumentList manageDocumentList = manageDocumentListMapper.selectById(id);
        if (ObjectUtils.isNotEmpty(manageDocumentList.getUrl())){
            // 删除旧文件
            File oldFile = new File(wordUrl + "/" + manageDocumentList.getUrl());
            oldFile.delete();
        }
        //上传新文件
        path = wordUrl;
        try {
            File realpath = new File(path);
            if (!realpath.exists()) {
                realpath.mkdirs();
            }
            pathName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            urlString = realpath + "/" + pathName;
            file.transferTo(new File(urlString));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("附件上传错误");
            return 0;
        }
        manageDocumentList.setUrl(pathName);
        return manageDocumentListMapper.updateById(manageDocumentList);
    }

    @Override
    public void importExcel(List<ManageDocumentList> list) {
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        list = list.stream().filter(manageDocumentList -> ObjectUtils.isNotEmpty(manageDocumentList.getName())).collect(Collectors.toList());
        saveBatch(list);
    }

}
