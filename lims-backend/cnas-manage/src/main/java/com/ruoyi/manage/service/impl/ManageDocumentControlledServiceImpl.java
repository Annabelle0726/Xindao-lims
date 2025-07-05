package com.ruoyi.manage.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.manage.mapper.ManageDocumentControlledMapper;
import com.ruoyi.manage.mapper.ManageDocumentListMapper;
import com.ruoyi.manage.mapper.ManageRecordCheckMapper;
import com.ruoyi.manage.pojo.ManageDocumentControlled;
import com.ruoyi.manage.pojo.ManageDocumentList;
import com.ruoyi.manage.pojo.ManageRecordCheck;
import com.ruoyi.manage.service.ManageDocumentControlledService;
import com.ruoyi.system.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 文件受控 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-08 02:54:44
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ManageDocumentControlledServiceImpl extends ServiceImpl<ManageDocumentControlledMapper, ManageDocumentControlled> implements ManageDocumentControlledService {

    @Resource
    private ManageDocumentControlledMapper manageDocumentControlledMapper;

    @Resource
    private ManageDocumentListMapper manageDocumentListMapper;

    @Resource
    private ManageRecordCheckMapper manageRecordCheckMapper;



    @Value("${wordUrl}")
    private String wordUrl;

    @Resource
    private UserMapper userMapper;

    @Override
    public IPage<ManageDocumentControlled> pageManageDocumentControlled(Page page, ManageDocumentControlled manageDocumentControlled) {
        return manageDocumentControlledMapper.pageManageDocumentControlled(page, QueryWrappers.queryWrappers(manageDocumentControlled));
    }

    @Override
    public int addManageDocumentControlled(ManageDocumentControlled manageDocumentControlled) {
        manageDocumentControlled.setState("待审核");
        String urlString;
        String pathName;
        String path;
        if (ObjectUtils.isNotEmpty(manageDocumentControlled.getFile())) {
            MultipartFile file = manageDocumentControlled.getFile();
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
            manageDocumentControlled.setUrl(pathName);
        }
        return manageDocumentControlledMapper.insert(manageDocumentControlled);
    }

    @Override
    public int delManageDocumentControlled(Long id) {
        ManageDocumentControlled manageDocumentControlled = manageDocumentControlledMapper.selectById(id);
        if (ObjectUtils.isNotEmpty(manageDocumentControlled.getUrl())) {
            // 删除旧文件
            File oldFile = new File(wordUrl + "/" + manageDocumentControlled.getUrl());
            oldFile.delete();
        }
        return manageDocumentControlledMapper.deleteById(id);
    }

    @Override
    public ManageDocumentControlled getManageDocumentControlled(Long id) {
        ManageDocumentControlled manageDocumentControlled = manageDocumentControlledMapper.getManageDocumentControlled(id);
        //获取部门信息
        String departmentLimsName = userMapper.selectUserDepartmentLimsName(manageDocumentControlled.getCreateUser());
        manageDocumentControlled.setCreateUserDepartLims(departmentLimsName);
        return manageDocumentControlled;
    }

    @Override
    public int doManageDocumentControlled(ManageDocumentControlled manageDocumentControlled) {
        ManageDocumentControlled manageDocumentControlled1 = manageDocumentControlledMapper.selectById(manageDocumentControlled.getId());
        if (ObjectUtils.isNotEmpty(manageDocumentControlled.getFile())) {
            if (ObjectUtils.isNotEmpty(manageDocumentControlled1.getUrl())) {
                // 删除旧文件
                File oldFile = new File(wordUrl + "/" + manageDocumentControlled1.getUrl());
                oldFile.delete();
            }
            //上传新文件
            String urlString;
            String pathName;
            String path;
            MultipartFile file = manageDocumentControlled.getFile();
            path = wordUrl;
            try {
                File realpath = new File(path);
                if (!realpath.exists()) {
                    realpath.mkdirs();
                }
                pathName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                urlString = realpath + "/" + pathName;
                file.transferTo(new File(urlString));
                manageDocumentControlled.setUrl(pathName);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("附件上传错误");
                return 0;
            }
        }
        return manageDocumentControlledMapper.updateById(manageDocumentControlled);
    }

    @Override
    public int checkManageDocumentControlled(ManageDocumentControlled manageDocumentControlled) {
        ManageDocumentControlled documentControlled = manageDocumentControlledMapper.selectById(manageDocumentControlled.getId());
        if (manageDocumentControlled.getState().equals("通过")) {
            // 删除旧文件
            File oldFile = new File(wordUrl + "/" + documentControlled.getUrl());
            oldFile.delete();
            //上传新文件
            String urlString;
            String pathName;
            String path;
            MultipartFile file = manageDocumentControlled.getFile();
            path = wordUrl;
            try {
                File realpath = new File(path);
                if (!realpath.exists()) {
                    realpath.mkdirs();
                }
                pathName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                urlString = realpath + "/" + pathName;
                file.transferTo(new File(urlString));
                manageDocumentControlled.setUrl(pathName);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("附件上传错误");
                return 0;
            }
            //将文件受控已知文件清单
            ManageDocumentList manageDocumentList = new ManageDocumentList();
            manageDocumentList.setDocumentCode(documentControlled.getDocumentCode());
            manageDocumentList.setType(documentControlled.getType());
            manageDocumentList.setName(documentControlled.getName());
            manageDocumentList.setVersion(documentControlled.getVersion());
            manageDocumentList.setWriter(documentControlled.getWriter());
            manageDocumentList.setEffectiveDate(LocalDate.now());
            manageDocumentList.setState("有效");
            manageDocumentList.setUrl(manageDocumentControlled.getUrl());
            manageDocumentListMapper.insert(manageDocumentList);
        }
        /*新增8.4的文件审批记录*/
        ManageRecordCheck manageRecordCheck = new ManageRecordCheck();
        manageRecordCheck.setDocumentCode(documentControlled.getDocumentCode());
        manageRecordCheck.setDocumentName(documentControlled.getName());
        manageRecordCheck.setDocumentVersion(documentControlled.getVersion());
        manageRecordCheck.setWriteUser(userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getName,documentControlled.getWriter())).getId());
        manageRecordCheck.setCheckUser(documentControlled.getDutyUser());
        manageRecordCheck.setCheckState(manageDocumentControlled.getState());
        manageRecordCheck.setRemark(documentControlled.getInstructions());
        manageRecordCheckMapper.insert(manageRecordCheck);
        return manageDocumentControlledMapper.updateById(manageDocumentControlled);
    }

    @Override
    public void checkManageDocumentControlledPdf(Long id, HttpServletResponse response) throws Exception {
        ManageDocumentControlled manageDocumentControlled = manageDocumentControlledMapper.selectById(id);
        File file = new File(wordUrl + "/" + manageDocumentControlled.getUrl());
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

}
