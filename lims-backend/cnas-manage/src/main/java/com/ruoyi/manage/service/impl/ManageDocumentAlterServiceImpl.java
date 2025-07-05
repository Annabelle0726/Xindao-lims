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
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.UserMapper;


import com.ruoyi.manage.mapper.*;
import com.ruoyi.manage.pojo.*;
import com.ruoyi.manage.service.ManageDocumentAlterService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文件变更 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-11 11:04:01
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ManageDocumentAlterServiceImpl extends ServiceImpl<ManageDocumentAlterMapper, ManageDocumentAlter> implements ManageDocumentAlterService {

    @Resource
    private ManageDocumentAlterMapper manageDocumentAlterMapper;

    @Resource
    private ManageRecordCheckMapper manageRecordCheckMapper;



    @Value("${wordUrl}")
    private String wordUrl;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ManageDocumentListMapper manageDocumentListMapper;

    @Resource
    private ManageDocumentCancelMapper manageDocumentCancelMapper;

    @Resource
    private ManageRecordAuditMapper manageRecordAuditMapper;


    @Override
    public IPage<ManageDocumentAlter> pageManageDocumentAlter(Page page, ManageDocumentAlter manageDocumentAlter) {
        return manageDocumentAlterMapper.pageManageDocumentAlter(page, QueryWrappers.queryWrappers(manageDocumentAlter));
    }

    @Override
    public ManageDocumentAlter getManageDocumentAlter(Integer id) {
        ManageDocumentAlter manageDocumentAlter = manageDocumentAlterMapper.getManageDocumentAlter(id);
        String limsName = userMapper.selectUserDepartmentLimsName(manageDocumentAlter.getCreateUser());
        manageDocumentAlter.setCreateUserDepartLims(limsName);
        return manageDocumentAlter;
    }

    @Override
    public int addManageDocumentAlter(ManageDocumentAlter manageDocumentAlter) {
        /*新增8.4的文件修订申请审批记录*/
        ManageRecordAudit manageRecordAudit = new ManageRecordAudit();
        manageRecordAudit.setDocumentCode(manageDocumentAlter.getAlterBeforeCode());
        manageRecordAudit.setDocumentName(manageDocumentAlter.getAlterBeforeName());
        manageRecordAudit.setBeforeVersion(manageDocumentAlter.getAlterBeforeVersion());
        manageRecordAudit.setAfterVersion(manageDocumentAlter.getAlterAfterVersion());
        manageRecordAudit.setReason(manageDocumentAlter.getAlterNote());
        manageRecordAudit.setAlterUser(SecurityUtils.getUserId().intValue());
        manageRecordAudit.setMethod("修订");
        if (ObjectUtils.isNotEmpty(manageDocumentAlter.getFile())) {
            String urlString;
            String pathName;
            String path;
            MultipartFile file = manageDocumentAlter.getFile();
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
            manageDocumentAlter.setAlterAfterUrl(pathName);
            manageRecordAudit.setAfterUrl(pathName);
        }
        manageRecordAuditMapper.insert(manageRecordAudit);
        return manageDocumentAlterMapper.insert(manageDocumentAlter);
    }

    @Override
    public int doManageDocumentAlter(ManageDocumentAlter manageDocumentAlter) {
        ManageDocumentAlter manageDocumentAlter1= manageDocumentAlterMapper.selectById(manageDocumentAlter.getId());
        if (ObjectUtils.isNotEmpty(manageDocumentAlter.getFile())) {
            if (ObjectUtils.isNotEmpty(manageDocumentAlter1.getAlterAfterUrl())) {
                // 删除旧文件
                File oldFile = new File(wordUrl + "/" + manageDocumentAlter1.getAlterAfterUrl());
                oldFile.delete();
            }
            //上传新文件
            String urlString;
            String pathName;
            String path;
            MultipartFile file = manageDocumentAlter.getFile();
            path = wordUrl;
            try {
                File realpath = new File(path);
                if (!realpath.exists()) {
                    realpath.mkdirs();
                }
                pathName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                urlString = realpath + "/" + pathName;
                file.transferTo(new File(urlString));
                manageDocumentAlter.setAlterAfterUrl(pathName);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("附件上传错误");
                return 0;
            }
        }
        return manageDocumentAlterMapper.updateById(manageDocumentAlter);
    }

    @Override
    public int checkManageDocumentAlter(ManageDocumentAlter manageDocumentAlter) {
        ManageDocumentAlter oldManageDocumentAlter = manageDocumentAlterMapper.selectById(manageDocumentAlter.getId());
        if (manageDocumentAlter.getState().equals("通过")) {
            /*将文件盖章*/
            // 删除旧文件
            File oldFile = new File(wordUrl + "/" + oldManageDocumentAlter.getAlterAfterUrl());
            oldFile.delete();
            //上传新文件
            String urlString;
            String pathName;
            String path;
            MultipartFile file = manageDocumentAlter.getFile();
            path = wordUrl;
            try {
                File realpath = new File(path);
                if (!realpath.exists()) {
                    realpath.mkdirs();
                }
                pathName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                urlString = realpath + "/" + pathName;
                file.transferTo(new File(urlString));
                manageDocumentAlter.setAlterAfterUrl(pathName);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("附件上传错误");
                return 0;
            }
            if(StringUtils.isNotEmpty(oldManageDocumentAlter.getMethod())){

            }
            /*其余相关处理*/
            if ("作废".equals(oldManageDocumentAlter.getMethod())){
                //新增到作废申请里面
                ManageDocumentCancel manageDocumentCancel = new ManageDocumentCancel();
                manageDocumentCancel.setDocumentCode(oldManageDocumentAlter.getAlterBeforeCode());
                manageDocumentCancel.setName(oldManageDocumentAlter.getAlterBeforeName());
                manageDocumentCancel.setVersion(oldManageDocumentAlter.getAlterBeforeVersion());
                manageDocumentCancel.setCancelNote("文件变更,变更的文件编号是"+oldManageDocumentAlter.getAlterAfterCode());
                manageDocumentCancelMapper.insert(manageDocumentCancel);
                //删除文件清单
                manageDocumentListMapper.delete(Wrappers.<ManageDocumentList>lambdaQuery()
                        .eq(ManageDocumentList::getDocumentCode,oldManageDocumentAlter.getAlterBeforeCode()));
                //新增文件清单
                ManageDocumentList manageDocumentList = new ManageDocumentList();
                manageDocumentList.setDocumentCode(oldManageDocumentAlter.getAlterAfterCode());
                manageDocumentList.setName(oldManageDocumentAlter.getAlterAfterName());
                manageDocumentList.setVersion(oldManageDocumentAlter.getAlterAfterVersion());
                manageDocumentList.setState("有效");
                manageDocumentList.setEffectiveDate(LocalDate.now());
                manageDocumentList.setUrl(pathName);
                manageDocumentListMapper.insert(manageDocumentList);
                /*新增8.4的文件修订申请审批记录*/
                ManageRecordAudit manageRecordAudit = new ManageRecordAudit();
                manageRecordAudit.setDocumentCode(manageDocumentCancel.getDocumentCode());
                manageRecordAudit.setDocumentName(manageDocumentCancel.getName());
                manageRecordAudit.setAlterThing("作废");
                manageRecordAudit.setMethod("作废");
                manageRecordAuditMapper.insert(manageRecordAudit);
            }else {
                //如果是存档不可用,那就将文件清单的状态改为无效
                manageDocumentListMapper.update(null,Wrappers.<ManageDocumentList>lambdaUpdate()
                        .eq(ManageDocumentList::getDocumentCode,oldManageDocumentAlter.getAlterBeforeCode())
                        .set(ManageDocumentList::getState,"无效"));
            }
        }
        /*新增8.4的文件审批记录*/
        ManageRecordCheck manageRecordCheck = new ManageRecordCheck();
        manageRecordCheck.setDocumentCode(oldManageDocumentAlter.getAlterAfterCode());
        manageRecordCheck.setDocumentName(oldManageDocumentAlter.getAlterAfterName());
        manageRecordCheck.setDocumentVersion(oldManageDocumentAlter.getAlterAfterVersion());
        manageRecordCheck.setWriteUser(oldManageDocumentAlter.getCreateUser());
        manageRecordCheck.setCheckUser(oldManageDocumentAlter.getCheckUser());
        manageRecordCheck.setCheckState(manageDocumentAlter.getState());
        manageRecordCheck.setRemark(manageDocumentAlter.getAlterNote());
        manageRecordCheckMapper.insert(manageRecordCheck);
        return manageDocumentAlterMapper.updateById(manageDocumentAlter);
    }

    @Override
    public void checkManageDocumentAlterPdf(Long id, HttpServletResponse response) throws Exception {
        ManageDocumentAlter manageDocumentAlter = manageDocumentAlterMapper.selectById(id);
        File file = new File(wordUrl + "/" + manageDocumentAlter.getAlterAfterUrl());
        FileInputStream fileInputStream = new FileInputStream(file);
        response.setContentType("application/pdf");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-disposition", "inline;filename=" + file.getName());
        response.setContentLength((int)file.length());
        OutputStream os = response.getOutputStream();
        // 将文件内容写入输出流
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.flush();
        os.close();
    }

    @Override
    public void exportManageDocumentAlter(ManageDocumentAlter manageDocumentAlter, HttpServletResponse response) throws Exception {
        List<ManageDocumentAlter> data = manageDocumentAlterMapper.pageManageDocumentAlter(new Page(-1, -1), QueryWrappers.queryWrappers(manageDocumentAlter)).getRecords();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        String fileName = URLEncoder.encode("文件变更列表导出", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        try {
            // 新建ExcelWriter
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build();
            WriteSheet mainSheet = EasyExcel.writerSheet(0, "文件变更列表导出").head(ManageDocumentAlter.class).build();
            excelWriter.write(data, mainSheet);
            // 关闭流
            excelWriter.finish();
        } catch (IOException e) {
            throw new RuntimeException("导出失败");
        }
    }

    @Override
    public int delManageDocumentAlter(Integer id) {
        ManageDocumentAlter manageDocumentAlter = manageDocumentAlterMapper.selectById(id);
        /*删除8.4的文件修订申请审批记录*/
        manageRecordAuditMapper.delete(Wrappers.<ManageRecordAudit>lambdaQuery()
        .eq(ManageRecordAudit::getDocumentCode,manageDocumentAlter.getAlterBeforeCode())
        .eq(ManageRecordAudit::getDocumentName,manageDocumentAlter.getAlterBeforeName())
        .eq(ManageRecordAudit::getBeforeVersion,manageDocumentAlter.getAlterBeforeVersion())
        .eq(ManageRecordAudit::getAfterVersion,manageDocumentAlter.getAlterAfterVersion())
        .eq(ManageRecordAudit::getReason,manageDocumentAlter.getAlterNote())
        .eq(ManageRecordAudit::getAlterUser,manageDocumentAlter.getCreateUser())
        .eq(ManageRecordAudit::getMethod,"修订"));
        return manageDocumentAlterMapper.deleteById(id);
    }
}
