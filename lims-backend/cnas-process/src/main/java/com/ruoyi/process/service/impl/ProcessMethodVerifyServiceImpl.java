package com.ruoyi.process.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.PictureRenderData;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.device.mapper.DeviceMapper;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import com.ruoyi.inspect.util.UserUtils;
import com.ruoyi.process.dto.ProcessMethodVerifyDto;
import com.ruoyi.process.dto.ProcessMethodVerifyExportWordDto;
import com.ruoyi.process.mapper.ProcessMethodVerifyCalibrationsFileMapper;
import com.ruoyi.process.mapper.ProcessMethodVerifyMapper;
import com.ruoyi.process.mapper.ProcessMethodVerifyWorkFileMapper;
import com.ruoyi.process.pojo.*;
import com.ruoyi.process.service.*;
import com.ruoyi.system.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 标准方法验证
 *
 * @author zhuo
 * @since 2024-11-05
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProcessMethodVerifyServiceImpl extends ServiceImpl<ProcessMethodVerifyMapper, ProcessMethodVerify> implements ProcessMethodVerifyService {

    @Resource
    private ProcessMethodVerifyCalibrationsFileService processMethodVerifyCalibrationsFileService;
    @Resource
    private ProcessMethodVerifyMethodFileService processMethodVerifyMethodFileService;
    @Resource
    private ProcessMethodVerifyWorkFileService processMethodVerifyWorkFileService;
    @Resource
    private ProcessMethodVerifyMachineAttachmentService processMethodVerifyMachineAttachmentService;
    @Resource
    private ProcessMethodVerifyCalibrationsFileMapper processMethodVerifyCalibrationsFileMapper;
    @Resource
    private ProcessMethodVerifyWorkFileMapper processMethodVerifyWorkFileMapper;
    @Resource
    private DeviceMapper deviceMapper;

    @Resource
    private UserMapper userMapper;

    @Value("${file.path}")
    private String imgUrl;

    @Value("${wordUrl}")
    private String wordUrl;


    /**
     * 标准方法跟新验证修改
     *
     * @param page
     * @param methodVerifyDto
     * @return
     */
    @Override
    public IPage<ProcessMethodVerify> pagesMethodVerify(Page page, ProcessMethodVerifyDto methodVerifyDto) {
        if (methodVerifyDto.getOperationType() == null) {
            throw new ErrorException("请选择是变更还是验证");
        }
        return baseMapper.pagesMethodVerify(page, QueryWrappers.queryWrappers(methodVerifyDto));
    }

    /**
     * 新增标准方法验证
     *
     * @param methodVerifyDto
     * @return
     */
    @Override
    public boolean addMethodSearchNew(ProcessMethodVerifyDto methodVerifyDto) {
        if (methodVerifyDto.getOperationType() == null) {
            throw new ErrorException("请选择是变更还是验证");
        }
        // 新增
        baseMapper.insert(methodVerifyDto);

        // 判断确认人是否为空
        if (StringUtils.isNotBlank(methodVerifyDto.getConfirmUser())) {
            // 添加上岗证
            this.addWorkFile(methodVerifyDto);
        }

        // 新增设备信息
        if (CollectionUtils.isNotEmpty(methodVerifyDto.getMachineAttachmentList())) {
            this.addDevice(methodVerifyDto);
        }

        return true;
    }

    /**
     * 查询标准方法验证详情
     *
     * @param methodVerifyId
     * @return
     */
    @Override
    public ProcessMethodVerifyDto getMethodVerifyOne(Integer methodVerifyId) {
        ProcessMethodVerify processMethodVerify = baseMapper.selectById(methodVerifyId);
        ProcessMethodVerifyDto processMethodVerifyDto = new ProcessMethodVerifyDto();
        BeanUtil.copyProperties(processMethodVerify, processMethodVerifyDto);

        // 查询设备详情
        processMethodVerifyDto.setMachineAttachmentList(processMethodVerifyMachineAttachmentService.list(Wrappers.<ProcessMethodVerifyMachineAttachment>lambdaQuery()
                .eq(ProcessMethodVerifyMachineAttachment::getMethodVerifyId, methodVerifyId)));
        // 查询原始记录
        processMethodVerifyDto.setMethodFileList(processMethodVerifyMethodFileService.list(Wrappers.<ProcessMethodVerifyMethodFile>lambdaQuery()
                .eq(ProcessMethodVerifyMethodFile::getMethodVerifyId, methodVerifyId)));
        // 查询上岗证
        processMethodVerifyDto.setWorkFileList(processMethodVerifyWorkFileService.list(Wrappers.<ProcessMethodVerifyWorkFile>lambdaQuery()
                .eq(ProcessMethodVerifyWorkFile::getMethodVerifyId, methodVerifyId)));
        // 查询校准证书
        processMethodVerifyDto.setCalibrationsFileList(processMethodVerifyCalibrationsFileMapper.selectCalibrationsFileList(methodVerifyId));


        return processMethodVerifyDto;
    }

    /**
     * 修改标准方法验证
     *
     * @param methodVerifyDto
     * @return
     */
    @Override
    public boolean updateMethodVerify(ProcessMethodVerifyDto methodVerifyDto) {
        baseMapper.updateById(methodVerifyDto);

        // 删除上岗证
        processMethodVerifyWorkFileService.remove(Wrappers.<ProcessMethodVerifyWorkFile>lambdaQuery()
                .eq(ProcessMethodVerifyWorkFile::getMethodVerifyId, methodVerifyDto.getMethodVerifyId()));

        // 删除原本设备详情
        processMethodVerifyMachineAttachmentService.remove(Wrappers.<ProcessMethodVerifyMachineAttachment>lambdaQuery()
                .eq(ProcessMethodVerifyMachineAttachment::getMethodVerifyId, methodVerifyDto.getMethodVerifyId()));

        // 删除元校准证书
        processMethodVerifyCalibrationsFileService.remove(Wrappers.<ProcessMethodVerifyCalibrationsFile>lambdaQuery()
                .eq(ProcessMethodVerifyCalibrationsFile::getMethodVerifyId, methodVerifyDto.getMethodVerifyId()));

        // 判断确认人是否为空
        if (StringUtils.isNotBlank(methodVerifyDto.getConfirmUser())) {
            // 添加上岗证
            this.addWorkFile(methodVerifyDto);
        }

        // 新增设备信息
        if (CollectionUtils.isNotEmpty(methodVerifyDto.getMachineAttachmentList())) {
            this.addDevice(methodVerifyDto);
        }
        return true;
    }

    /**
     * 删除标准方法验证
     *
     * @param methodVerifyId
     * @return
     */
    @Override
    public boolean delMethodVerify(Integer methodVerifyId) {
        // 删除所有设备
        processMethodVerifyMachineAttachmentService.remove(Wrappers.<ProcessMethodVerifyMachineAttachment>lambdaQuery()
                .eq(ProcessMethodVerifyMachineAttachment::getMethodVerifyId, methodVerifyId));

        // 删除所有原始记录
        processMethodVerifyMethodFileService.remove(Wrappers.<ProcessMethodVerifyMethodFile>lambdaQuery()
                .eq(ProcessMethodVerifyMethodFile::getMethodVerifyId, methodVerifyId));


        baseMapper.deleteById(methodVerifyId);

        return true;
    }

    /**
     * 方法验证确认
     *
     * @param methodVerifyId
     * @return
     */
    @Override
    public boolean methodVerifyAffirm(Integer methodVerifyId) {
        baseMapper.update(null, Wrappers.<ProcessMethodVerify>lambdaUpdate()
                .set(ProcessMethodVerify::getConfirmDate, LocalDateTime.now())
                .eq(ProcessMethodVerify::getMethodVerifyId, methodVerifyId));
        return true;
    }

    /**
     * 方法验证新增原始记录
     *
     * @param methodVerifyId
     * @param file
     * @return
     */
    @Override
    public boolean uploadVerifyMethodFile(Integer methodVerifyId, MultipartFile file) {
        if (methodVerifyId == null) {
            throw new ErrorException("缺少原始记录id");
        }

        String urlString;
        String pathName;
        String path;
        String filename = file.getOriginalFilename();
        String contentType = file.getContentType();
        ProcessMethodVerifyMethodFile methodFile = new ProcessMethodVerifyMethodFile();
        methodFile.setMethodVerifyId(methodVerifyId);
        methodFile.setFileName(filename);
        if (contentType != null && contentType.startsWith("image/")) {
            // 是图片
            path = imgUrl;
            methodFile.setType(1);
        } else {
            // 是文件
            path = wordUrl;
            methodFile.setType(2);
        }
        try {
            File realpath = new File(path);
            if (!realpath.exists()) {
                realpath.mkdirs();
            }
            pathName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss")) + "_" + file.getOriginalFilename();
            urlString = realpath + "/" + pathName;
            file.transferTo(new File(urlString));
            methodFile.setFileUrl(pathName);
            return processMethodVerifyMethodFileService.save(methodFile);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("附件上传错误");
            return false;
        }
    }

    /**
     * 标准方法验证原始记录列表
     *
     * @param methodVerifyId
     * @return
     */
    @Override
    public List<ProcessMethodVerifyMethodFile> getVerifyMethodFileList(Integer methodVerifyId) {
        return processMethodVerifyMethodFileService.list(Wrappers.<ProcessMethodVerifyMethodFile>lambdaQuery()
                .eq(ProcessMethodVerifyMethodFile::getMethodVerifyId, methodVerifyId));
    }

    /**
     * 导出标准方法更新验证
     *
     * @param methodVerifyId 标准方法验证id
     * @param response
     */
    @Override
    public void exportMethodVerify(Integer methodVerifyId, HttpServletResponse response) {
        // 获取标准方法更新验证数据
        ProcessMethodVerify processMethodVerify = baseMapper.selectById(methodVerifyId);

        // 将内部字段转换成展示到word中的对象
        ProcessMethodVerifyExportWordDto exportWordTemplate = getExportWordTemplate(processMethodVerify);

        // 定义一个集合存放人员签名
        ArrayList<PictureRenderData> pictureRenderDataList = new ArrayList<>();
        // TODO:确认最多会有5个人
        String confirmUser = processMethodVerify.getConfirmUser();
        if (StringUtils.isNotBlank(confirmUser)) {
            // 对人员id字符串进行分割成数组
            String[] userIds = confirmUser.split(",");
            // 循环获取人员签名
            for (String userIdStr : userIds) {
                // 转换为int类型
                Integer userId = Integer.valueOf(userIdStr);
                // 获取人员签名对象
                PictureRenderData finalUserSignatureUrl = UserUtils.getFinalUserSignatureUrl(userId);
                // 将人员签名对象添加到集合中
                pictureRenderDataList.add(finalUserSignatureUrl);
            }
        }

        // 判断集合长度，并补null到5个
        while (pictureRenderDataList.size() < 5) {
            pictureRenderDataList.add(null);
        }

        // 获取设备信息
        List<ProcessMethodVerifyMachineAttachment> processMethodVerifyMachineAttachmentList = processMethodVerifyMachineAttachmentService.list(Wrappers.<ProcessMethodVerifyMachineAttachment>lambdaQuery()
                .eq(ProcessMethodVerifyMachineAttachment::getMethodVerifyId, methodVerifyId));

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/method-verify.docx");
        Configure configure = Configure.builder()
                .bind("processMethodVerifyMachineAttachmentList", new HackLoopTableRenderPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("processMethodVerify", exportWordTemplate);
                    put("processMethodVerifyMachineAttachmentList", processMethodVerifyMachineAttachmentList);
                    put("affirmUserUrl1", pictureRenderDataList.get(0));
                    put("affirmUserUrl2", pictureRenderDataList.get(1));
                    put("affirmUserUrl3", pictureRenderDataList.get(2));
                    put("affirmUserUrl4", pictureRenderDataList.get(3));
                    put("affirmUserUrl5", pictureRenderDataList.get(4));
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "标准（方法）确认记录", "UTF-8");
            response.setHeader("Content-disposition",
                    "attachment;filename=" + fileName + ".docx");
            OutputStream os = response.getOutputStream();
            template.write(os);
            os.flush();
            os.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("导出失败");
        }
    }

    private ProcessMethodVerifyExportWordDto getExportWordTemplate(ProcessMethodVerify processMethodVerify) {
        ProcessMethodVerifyExportWordDto processMethodVerifyExportWordDto = new ProcessMethodVerifyExportWordDto();
        BeanUtil.copyProperties(processMethodVerify, processMethodVerifyExportWordDto);
        // （人）是否满足
        if (processMethodVerify.getPersonIsSatisfied() != null) {
            processMethodVerifyExportWordDto.setPersonIsSatisfiedStr(processMethodVerify.getPersonIsSatisfied() == 1 ? "√" : "×");
        }
        // （机）是否满足
        if (processMethodVerify.getMachineIsSatisfied() != null) {
            processMethodVerifyExportWordDto.setMachineIsSatisfiedStr(processMethodVerify.getMachineIsSatisfied() == 1 ? "√" : "×");
        }
        // （料）是否满足
        if (processMethodVerify.getMaterialIsSatisfied() != null) {
            processMethodVerifyExportWordDto.setMaterialIsSatisfiedStr(processMethodVerify.getMaterialIsSatisfied() == 1 ? "√" : "×");
        }
        // （法）是否满足
        if (processMethodVerify.getMethodIsSatisfied() != null) {
            processMethodVerifyExportWordDto.setMethodIsSatisfiedStr(processMethodVerify.getMethodIsSatisfied() == 1? "√" : "×");
        }
        // （环）是否满足
        if (processMethodVerify.getEnvironmentIsSatisfied() != null) {
            processMethodVerifyExportWordDto.setEnvironmentIsSatisfiedStr(processMethodVerify.getEnvironmentIsSatisfied() == 1 ? "√" : "×");
        }
        // （测量溯源性）是否满足
        if (processMethodVerify.getTraceabilityIsSatisfied() != null) {
            processMethodVerifyExportWordDto.setTraceabilityIsSatisfiedStr(processMethodVerify.getTraceabilityIsSatisfied() == 1 ? "√" : "×");
        }
        // （样品管理需求）是否满足
        if (processMethodVerify.getManagementIsSatisfied() != null) {
            processMethodVerifyExportWordDto.setManagementIsSatisfiedStr(processMethodVerify.getManagementIsSatisfied() == 1 ? "√" : "×");
        }
        // （其他）是否满足
        if (processMethodVerify.getOtherIsSatisfied() != null) {
            processMethodVerifyExportWordDto.setOtherIsSatisfiedStr(processMethodVerify.getOtherIsSatisfied() == 1 ? "√" : "×");
        }
        // 确认日期
        processMethodVerifyExportWordDto.setConfirmDateStr(processMethodVerify.getConfirmDate() != null ? processMethodVerify.getConfirmDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")): null);
        return processMethodVerifyExportWordDto;
    }


    /**
     * 添加上岗证
     *
     * @param methodVerifyDto
     */
    private void addWorkFile(ProcessMethodVerifyDto methodVerifyDto) {
        List<String> confirmUserIds = StrUtil.split(methodVerifyDto.getConfirmUser(), ",");
        List<ProcessMethodVerifyWorkFile> workFileList = new ArrayList<>();
        for (String confirmUserId : confirmUserIds) {
            Integer userId = Integer.valueOf(confirmUserId);
            String workName = processMethodVerifyWorkFileMapper.selectWorkFile(userId);
            // 判断是否有证书
            if (StringUtils.isNotBlank(workName)) {
                User user = userMapper.selectById(userId);
                ProcessMethodVerifyWorkFile workFile = new ProcessMethodVerifyWorkFile();
                workFile.setMethodVerifyId(methodVerifyDto.getMethodVerifyId());
                workFile.setType(1);
                workFile.setFileUrl(workName);
                workFile.setFileName(workName);
                workFile.setUserId(user.getId());
                workFile.setUserName(user.getName());
                workFileList.add(workFile);
            }
        }
        if (CollectionUtils.isNotEmpty(workFileList)) {
            processMethodVerifyWorkFileService.saveBatch(workFileList);
        }
    }

    /**
     * 添加设备信息
     *
     * @param methodVerifyDto
     */
    private void addDevice(ProcessMethodVerifyDto methodVerifyDto) {
        List<ProcessMethodVerifyCalibrationsFile> calibrationsFiles = new ArrayList<>();
        for (ProcessMethodVerifyMachineAttachment machineAttachment : methodVerifyDto.getMachineAttachmentList()) {
            machineAttachment.setMethodVerifyId(methodVerifyDto.getMethodVerifyId());
            // 查询校准证书
            String calibrationsName = processMethodVerifyCalibrationsFileMapper.selectCalibrationsFile(machineAttachment.getDeviceId());
            // 判断是否有证书
            if (StringUtils.isNotBlank(calibrationsName)) {
                ProcessMethodVerifyCalibrationsFile calibrationsFile = new ProcessMethodVerifyCalibrationsFile();
                calibrationsFile.setMethodVerifyId(methodVerifyDto.getMethodVerifyId());
                calibrationsFile.setType(1);
                calibrationsFile.setFileUrl(calibrationsName);
                calibrationsFile.setFileName(calibrationsName);
                calibrationsFile.setDeviceId((machineAttachment.getDeviceId()));
                calibrationsFiles.add(calibrationsFile);
            }
        }
        // 添加校准证书
        processMethodVerifyCalibrationsFileService.saveBatch(calibrationsFiles);

        // 添加设备
        processMethodVerifyMachineAttachmentService.saveBatch(methodVerifyDto.getMachineAttachmentList());
    }

}

