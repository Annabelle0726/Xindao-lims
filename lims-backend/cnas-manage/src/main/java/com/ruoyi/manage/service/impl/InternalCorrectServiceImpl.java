package com.ruoyi.manage.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.data.Pictures;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.manage.dto.InternalCorrectDto;
import com.ruoyi.manage.mapper.InternalCorrectFileMapper;
import com.ruoyi.manage.mapper.InternalCorrectMapper;
import com.ruoyi.manage.pojo.InternalCorrect;
import com.ruoyi.manage.pojo.InternalCorrectFile;
import com.ruoyi.manage.service.InternalCorrectService;
import com.ruoyi.system.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 内审管理纠正处理表 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-13 04:00:15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class InternalCorrectServiceImpl extends ServiceImpl<InternalCorrectMapper, InternalCorrect> implements InternalCorrectService {

    @Resource
    private InternalCorrectFileMapper internalCorrectFileMapper;
    @Resource
    private UserMapper userMapper;
    @Value("${file.path}")
    private String imgUrl;

    @Value("${wordUrl}")
    private String wordUrl;


    @Override
    public boolean addInternalCorrect(InternalCorrect detailsCorrect) {
        InternalCorrect correct = new InternalCorrect();
        // 当前登录用户信息和部门
        User user = userMapper.selectById(SecurityUtils.getUserId().intValue());
        String departmentLimsName = userMapper.selectUserDepartmentLimsName(user.getId());

        switch (detailsCorrect.getFlowType()) {
            // 不合格提出
            case 0:
                correct.setRaiseResult(detailsCorrect.getRaiseResult());//不合格表述
                correct.setVdeRaiseResult(detailsCorrect.getVdeRaiseResult());//vde专家发现
                correct.setRaiseDepartment(departmentLimsName);//提出部门
                correct.setRaiseUserId(user.getId());//提出人id
                correct.setRaiseUserName(user.getName());// 提出人
                correct.setRaiseTime(LocalDate.now());// 提出时间

                // 原因分析人信息
                User causeUser = userMapper.selectById(detailsCorrect.getCauseUserId());
                String causeDepartmentLims = userMapper.selectUserDepartmentLimsName(causeUser.getId());

                correct.setCauseDepartment(causeDepartmentLims);//原因分析责任部门
                correct.setCauseUserId(causeUser.getId());//1原因分析人id
                correct.setCauseUserName(causeUser.getName());// 1原因分析人
                baseMapper.insert(correct);
                break;

            // 原因分析
            case 1:
                correct.setCorrectId(detailsCorrect.getCorrectId());
                correct.setCauseResult(detailsCorrect.getCauseResult());//原因分析
                correct.setCauseTime(LocalDate.now());// 1原因分析时间

                // 纠正人信息
                User correctUser = userMapper.selectById(detailsCorrect.getCorrectUserId());
                String correctUserDepartmentLims = userMapper.selectUserDepartmentLimsName(correctUser.getId());

                correct.setCorrectDepartment(correctUserDepartmentLims);//2纠正责任部门
                correct.setCorrectUserId(correctUser.getId());//2纠正人id
                correct.setCorrectUserName(correctUser.getName());// 2纠正人
                baseMapper.updateById(correct);
                break;

            // 纠正措施
            case 2:
                correct.setCorrectId(detailsCorrect.getCorrectId());
                correct.setCorrectResult(detailsCorrect.getCorrectResult());//2纠正措施
                correct.setRaiseDepartmentAffirm(detailsCorrect.getRaiseDepartmentAffirm());//2提出部门确认
                correct.setCorrectTime(LocalDate.now());// 2纠正时间

                // 验证人信息
                User validationUser = userMapper.selectById(detailsCorrect.getValidationUserId());
                String validationUserDepartmentLims = userMapper.selectUserDepartmentLimsName(validationUser.getId());

                correct.setValidationDepartment(validationUserDepartmentLims);//3验证部门
                correct.setValidationUserId(validationUser.getId());//3验证人id
                correct.setValidationUserName(validationUser.getName());// 3验证人
                baseMapper.updateById(correct);
                break;

            // 验证结果
            case 3:
                correct.setCorrectId(detailsCorrect.getCorrectId());
                correct.setValidationResult(detailsCorrect.getValidationResult());//3验证结果
                correct.setValidationTime(LocalDate.now());// 3验证时间
                correct.setIsFinish(1);
                baseMapper.updateById(correct);
                break;
        }

        return true;
    }

    /**
     * 查询内审管理纠正处理
     * @param correctId
     * @return
     */
    @Override
    public InternalCorrect getInternalCorrect(Integer correctId) {
        InternalCorrect detailsCorrect;

        detailsCorrect = baseMapper.selectOne(Wrappers.<InternalCorrect>lambdaQuery()
                .eq(InternalCorrect::getCorrectId, correctId));

        if (detailsCorrect == null) {
            detailsCorrect = new InternalCorrect();
        }
        return detailsCorrect;
    }

    /**
     * 查询内审管理纠正措施列表
     * @param page
     * @param detailsCorrect
     * @return
     */
    @Override
    public IPage<InternalCorrect> pageInternalCorrect(Page page, InternalCorrect detailsCorrect) {
        return baseMapper.pageInternalAccording(page, QueryWrappers.queryWrappers(detailsCorrect));
    }

    /**
     * 新增内审管理纠正措施附件
     * @param InternalCorrectId
     * @param file
     * @return
     */
    @Override
    public boolean uploadInternalCorrectFile(Integer InternalCorrectId, MultipartFile file) {
        if (InternalCorrectId == null) {
            throw new ErrorException("缺少纠正措施id");
        }

        String urlString;
        String pathName;
        String path;
        String filename = file.getOriginalFilename();
        String contentType = file.getContentType();
        InternalCorrectFile InternalCorrectFile = new InternalCorrectFile();
        InternalCorrectFile.setCorrectId(InternalCorrectId);
        InternalCorrectFile.setFileName(filename);
        if (contentType != null && contentType.startsWith("image/")) {
            // 是图片
            path = imgUrl;
            InternalCorrectFile.setType(1);
        } else {
            // 是文件
            path = wordUrl;
            InternalCorrectFile.setType(2);
        }
        try {
            File realpath = new File(path);
            if (!realpath.exists()) {
                realpath.mkdirs();
            }
            pathName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss")) + "_" + file.getOriginalFilename();
            urlString = realpath + "/" + pathName;
            file.transferTo(new File(urlString));
            InternalCorrectFile.setFileUrl(pathName);
            internalCorrectFileMapper.insert(InternalCorrectFile);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorException(e.getMessage());
        }
    }

    /**
     * 查询内审管理纠正措施附件
     * @param correctId
     * @return
     */
    @Override
    public List<InternalCorrectFile> getInternalCorrectFileList(Integer correctId) {
        return internalCorrectFileMapper.selectList(Wrappers.<InternalCorrectFile>lambdaQuery()
                .eq(InternalCorrectFile::getCorrectId, correctId));
    }

    /**
     * 导出纠正措施
     * @param correctId
     * @param response
     */
    @Override
    public void exportInternalCorrect(Integer correctId, HttpServletResponse response) {
        InternalCorrect internalCorrect = baseMapper.selectById(correctId);
        InternalCorrectDto internalCorrectDto = new InternalCorrectDto();
        BeanUtils.copyProperties(internalCorrect, internalCorrectDto);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 提出时间
        internalCorrectDto.setRaiseTimeString(internalCorrect.getRaiseTime() != null
                ? internalCorrect.getRaiseTime().format(formatter) : null);
        // 原因分析时间
        internalCorrectDto.setCauseTimeString(internalCorrect.getCauseTime() != null
                ? internalCorrect.getCauseTime().format(formatter) : null);

        // 纠正时间
        internalCorrectDto.setCorrectTimeString(internalCorrect.getCorrectTime() != null
                ? internalCorrect.getCorrectTime().format(formatter) : null);

        // 验证时间
        internalCorrectDto.setValidationTimeString(internalCorrect.getValidationTime() != null
                ? internalCorrect.getValidationTime().format(formatter) : null);


        // 提出人签名
        String raiseUrl = null;
        if (internalCorrect.getRaiseUserId() != null) {
            raiseUrl = userMapper.selectById(internalCorrect.getRaiseUserId()).getSignatureUrl();
            if (StringUtils.isBlank(raiseUrl)) {
                throw new ErrorException("找不到提出人的签名");
            }
        }

        // 原因分析人
        String causeUrl = null;
        if (internalCorrect.getCauseUserId() != null) {
            causeUrl = userMapper.selectById(internalCorrect.getCauseUserId()).getSignatureUrl();
            if (StringUtils.isBlank(causeUrl)) {
                throw new ErrorException("找不到原因分析人的签名");
            }
        }

        // 纠正人
        String correctUrl = null;
        if (internalCorrect.getCorrectUserId() != null) {
            correctUrl = userMapper.selectById(internalCorrect.getCorrectUserId()).getSignatureUrl();
            if (StringUtils.isBlank(correctUrl)) {
                throw new ErrorException("找不到纠正人的签名");
            }
        }

        // 验证人
        String validationUrl = null;
        if (internalCorrect.getValidationUserId() != null) {
            validationUrl = userMapper.selectById(internalCorrect.getValidationUserId()).getSignatureUrl();
            if (StringUtils.isBlank(validationUrl)) {
                throw new ErrorException("找不到验证人的签名");
            }
        }

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/internal-correct.docx");
        ConfigureBuilder builder = Configure.builder();
        builder.useSpringEL(true);
        String finalRaiseUrl = raiseUrl;
        String finalCauseUrl = causeUrl;
        String finalCorrectUrl = correctUrl;
        String finalValidationUrl = validationUrl;
        XWPFTemplate template = XWPFTemplate.compile(inputStream, builder.build()).render(
                new HashMap<String, Object>() {{
                    put("correct", internalCorrectDto);
                    put("raiseUrl", StringUtils.isNotBlank(finalRaiseUrl) ? Pictures.ofLocal(imgUrl + "/" + finalRaiseUrl).create() : null);
                    put("causeUrl", StringUtils.isNotBlank(finalCauseUrl) ? Pictures.ofLocal(imgUrl + "/" + finalCauseUrl).create() : null);
                    put("correctUrl", StringUtils.isNotBlank(finalCorrectUrl) ? Pictures.ofLocal(imgUrl + "/" + finalCorrectUrl).create() : null);
                    put("validationUrl", StringUtils.isNotBlank(finalValidationUrl) ? Pictures.ofLocal(imgUrl + "/" + finalValidationUrl).create() : null);
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "内审纠正措施", "UTF-8");
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
}
