package com.ruoyi.manage.service.impl;

import cn.hutool.core.lang.UUID;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.manage.dto.ManageDocumentIssueRecycleDto;
import com.ruoyi.manage.mapper.ManageDocumentIssueRecycleMapper;
import com.ruoyi.manage.mapper.ManageRecordCheckMapper;
import com.ruoyi.manage.mapper.ManageRecordIssueRecycleMapper;
import com.ruoyi.manage.pojo.ManageDocumentIssueRecycle;
import com.ruoyi.manage.pojo.ManageRecordCheck;
import com.ruoyi.manage.pojo.ManageRecordIssueRecycle;
import com.ruoyi.manage.service.ManageDocumentIssueRecycleService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文件发放回收 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-09 09:18:24
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ManageDocumentIssueRecycleServiceImpl extends ServiceImpl<ManageDocumentIssueRecycleMapper, ManageDocumentIssueRecycle> implements ManageDocumentIssueRecycleService {

    @Resource
    private ManageDocumentIssueRecycleMapper manageDocumentIssueRecycleMapper;



    @Value("${wordUrl}")
    private String wordUrl;

    @Resource
    private ManageRecordCheckMapper manageRecordCheckMapper;

    @Resource
    private ManageRecordIssueRecycleMapper manageRecordIssueRecycleMapper;

    @Override
    public IPage<ManageDocumentIssueRecycleDto> pageManageDocumentIssueRecycle(Page page, ManageDocumentIssueRecycleDto manageDocumentIssueRecycleDto) {
        return manageDocumentIssueRecycleMapper.pageManageDocumentIssueRecycle(page, QueryWrappers.queryWrappers(manageDocumentIssueRecycleDto));
    }

    @Override
    public ManageDocumentIssueRecycleDto getManageDocumentIssueRecycle(Long id) {
        return manageDocumentIssueRecycleMapper.getManageDocumentIssueRecycle(id);
    }

    @Override
    public void exportManageDocumentIssueRecycle(ManageDocumentIssueRecycleDto manageDocumentIssueRecycleDto, HttpServletResponse response)throws Exception {
        List<ManageDocumentIssueRecycleDto> data = manageDocumentIssueRecycleMapper.pageManageDocumentIssueRecycle(new Page(-1, -1), QueryWrappers.queryWrappers(manageDocumentIssueRecycleDto)).getRecords();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        String fileName = URLEncoder.encode("文件发放回收导出", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        try {
            // 新建ExcelWriter
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build();
            WriteSheet mainSheet = EasyExcel.writerSheet(0, "文件发放回收导出").head(ManageDocumentIssueRecycleDto.class).build();
            excelWriter.write(data, mainSheet);
            // 关闭流
            excelWriter.finish();
        } catch (IOException e) {
            throw new RuntimeException("导出失败");
        }
    }

    @Override
    public int checkManageDocumentIssueRecycle(Integer id, String documentState) {
        ManageDocumentIssueRecycle manageDocumentIssueRecycle = manageDocumentIssueRecycleMapper.selectById(id);
        manageDocumentIssueRecycle.setDocumentState(documentState);
        /*新增8.4的文件审批记录*/
        ManageRecordCheck manageRecordCheck = new ManageRecordCheck();
        manageRecordCheck.setDocumentCode(manageDocumentIssueRecycle.getDocumentCode());
        manageRecordCheck.setDocumentName(manageDocumentIssueRecycle.getName());
        manageRecordCheck.setDocumentVersion(manageDocumentIssueRecycle.getVersion());
        manageRecordCheck.setCheckUser(manageDocumentIssueRecycle.getReceiveUser());
        manageRecordCheck.setCheckState(documentState);
        manageRecordCheckMapper.insert(manageRecordCheck);
        return manageDocumentIssueRecycleMapper.updateById(manageDocumentIssueRecycle);
    }

    @Override
    public int addManageDocumentIssueRecycle(ManageDocumentIssueRecycle manageDocumentIssueRecycle) {
        manageDocumentIssueRecycle.setDocumentState("待审核");
        if (ObjectUtils.isNotEmpty(manageDocumentIssueRecycle.getFile())){
            String urlString;
            String pathName;
            String path;
            MultipartFile file = manageDocumentIssueRecycle.getFile();
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
            manageDocumentIssueRecycle.setUrl(pathName);
        }
        /*新增8.3的发放与回收记录*/
        ManageRecordIssueRecycle manageRecordIssueRecycle = new ManageRecordIssueRecycle();
        manageRecordIssueRecycle.setDocumentCode(manageDocumentIssueRecycle.getDocumentCode());
        manageRecordIssueRecycle.setDocumentName(manageDocumentIssueRecycle.getName());
        manageRecordIssueRecycle.setDocumentVersion(manageDocumentIssueRecycle.getVersion());
        manageRecordIssueRecycle.setReceiveUser(manageDocumentIssueRecycle.getIssueUser());
        manageRecordIssueRecycle.setReceiveDate(manageDocumentIssueRecycle.getIssueDate());
        manageRecordIssueRecycleMapper.insert(manageRecordIssueRecycle);
        return manageDocumentIssueRecycleMapper.insert(manageDocumentIssueRecycle);
    }

    @Override
    public int doManageDocumentIssueRecycle(ManageDocumentIssueRecycle manageDocumentIssueRecycle) {
        ManageDocumentIssueRecycle documentIssueRecycle = manageDocumentIssueRecycleMapper.selectById(manageDocumentIssueRecycle.getId());
        //判读是否是回收
        if (ObjectUtils.isNotEmpty(manageDocumentIssueRecycle.getRecycleUser())){
            /*新增8.3的发放与回收记录*/
            ManageRecordIssueRecycle manageRecordIssueRecycle = manageRecordIssueRecycleMapper.selectOne(Wrappers.<ManageRecordIssueRecycle>lambdaQuery()
                    .eq(ManageRecordIssueRecycle::getDocumentCode, documentIssueRecycle.getDocumentCode())
                    .eq(ManageRecordIssueRecycle::getDocumentName, documentIssueRecycle.getName())
                    .eq(ManageRecordIssueRecycle::getDocumentVersion, documentIssueRecycle.getVersion())
                    .eq(ManageRecordIssueRecycle::getReceiveUser, documentIssueRecycle.getIssueUser())
                    .eq(ManageRecordIssueRecycle::getReceiveDate, documentIssueRecycle.getIssueDate()));
            manageRecordIssueRecycle.setSignedUser(manageDocumentIssueRecycle.getRecycleUser());
            manageRecordIssueRecycle.setSignedDate(manageDocumentIssueRecycle.getRecycleDate());
            manageRecordIssueRecycleMapper.updateById(manageRecordIssueRecycle);
        }
        if (ObjectUtils.isNotEmpty(manageDocumentIssueRecycle.getFile())) {
            if (ObjectUtils.isNotEmpty(documentIssueRecycle.getUrl())) {
                // 删除旧文件
                File oldFile = new File(wordUrl + "/" + documentIssueRecycle.getUrl());
                oldFile.delete();
            }
            //上传新文件
            String urlString;
            String pathName;
            String path;
            MultipartFile file = manageDocumentIssueRecycle.getFile();
            path = wordUrl;
            try {
                File realpath = new File(path);
                if (!realpath.exists()) {
                    realpath.mkdirs();
                }
                pathName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                urlString = realpath + "/" + pathName;
                file.transferTo(new File(urlString));
                manageDocumentIssueRecycle.setUrl(pathName);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("附件上传错误");
                return 0;
            }
        }
        return manageDocumentIssueRecycleMapper.updateById(manageDocumentIssueRecycle);
    }

    @Override
    public int delManageDocumentIssueRecycle(Long id) {
        ManageDocumentIssueRecycle documentIssueRecycle = manageDocumentIssueRecycleMapper.selectById(id);
        manageRecordIssueRecycleMapper.delete(Wrappers.<ManageRecordIssueRecycle>lambdaQuery()
                .eq(ManageRecordIssueRecycle::getDocumentCode, documentIssueRecycle.getDocumentCode())
                .eq(ManageRecordIssueRecycle::getDocumentName, documentIssueRecycle.getName())
                .eq(ManageRecordIssueRecycle::getDocumentVersion, documentIssueRecycle.getVersion())
                .eq(ManageRecordIssueRecycle::getReceiveUser, documentIssueRecycle.getIssueUser())
                .eq(ManageRecordIssueRecycle::getReceiveDate, documentIssueRecycle.getIssueDate()));
        return manageDocumentIssueRecycleMapper.deleteById(id);
    }
}
