package com.ruoyi.manage.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.data.Pictures;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.manage.dto.ClientSatisfactionDto;
import com.ruoyi.manage.mapper.ClientSatisfactionAnalyseFileMapper;
import com.ruoyi.manage.mapper.ClientSatisfactionMapper;
import com.ruoyi.manage.pojo.ClientSatisfaction;
import com.ruoyi.manage.pojo.ClientSatisfactionAnalyseFile;
import com.ruoyi.manage.service.ClientSatisfactionService;
import com.ruoyi.system.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;


/**
 * 客户满意度
 *
 * @author zhuo
 * @since 2024-11-09
 */
@Service
public class ClientSatisfactionServiceImpl extends ServiceImpl<ClientSatisfactionMapper, ClientSatisfaction> implements ClientSatisfactionService {

    @Resource
    private ClientSatisfactionAnalyseFileMapper clientSatisfactionAnalyseFileMapper;

    @Resource
    private UserMapper userMapper;

    @Value("${file.path}")
    private String imgUrl;

    @Value("${wordUrl}")
    private String wordUrl;

    /**
     * 客户满意度调查列表
     *
     * @param page
     * @param clientSatisfaction
     * @return
     */
    @Override
    public IPage<ClientSatisfaction> pageClientSatisfaction(Page page, ClientSatisfaction clientSatisfaction) {
        return baseMapper.pageClientSatisfaction(page, QueryWrappers.queryWrappers(clientSatisfaction));
    }

    /**
     * 新增客户分析附件
     *
     * @param file
     * @return
     */
    @Override
    public boolean uploadAnalyseFile(MultipartFile file) {

        String urlString;
        String pathName;
        String path;
        String filename = file.getOriginalFilename();
        String contentType = file.getContentType();
        ClientSatisfactionAnalyseFile analyseFile = new ClientSatisfactionAnalyseFile();
        analyseFile.setFileName(filename);
        if (contentType != null && contentType.startsWith("image/")) {
            // 是图片
            path = imgUrl;
            analyseFile.setType(1);
        } else {
            // 是文件
            path = wordUrl;
            analyseFile.setType(2);
        }
        try {
            File realpath = new File(path);
            if (!realpath.exists()) {
                realpath.mkdirs();
            }
            pathName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss")) + "_" + file.getOriginalFilename();
            urlString = realpath + "/" + pathName;
            file.transferTo(new File(urlString));
            analyseFile.setFileUrl(pathName);
            clientSatisfactionAnalyseFileMapper.insert(analyseFile);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorException(e.getMessage());
        }
    }

    /**
     * 查询客户分析附件
     *
     * @param page
     * @param analyseFile
     * @return
     */
    @Override
    public IPage<ClientSatisfactionAnalyseFile> pageAnalyseFile(Page page, ClientSatisfactionAnalyseFile analyseFile) {
        return clientSatisfactionAnalyseFileMapper.pageAnalyseFile(page, QueryWrappers.queryWrappers(analyseFile));
    }

    /**
     * 客户满意度导出
     *
     * @param clientSatisfactionId
     * @param response
     */
    @Override
    public void exportWordClientSatisfaction(Integer clientSatisfactionId, HttpServletResponse response) {
        // 查询客户满意度调查
        ClientSatisfactionDto clientSatisfaction = baseMapper.exportWordClientSatisfaction(clientSatisfactionId);
        if (clientSatisfaction == null) {
            throw new RuntimeException("客户满意度调查不存在");
        }
        // 查询确认人的签名
        String confirmPersonUrl = null;
        // 状态为确认且确认人不为空
        if (clientSatisfaction.getConfirmStatus() != null && clientSatisfaction.getConfirmStatus() != 0 && clientSatisfaction.getConfirmPersonId() != null) {
            confirmPersonUrl = userMapper.selectById(clientSatisfaction.getConfirmPersonId()).getSignatureUrl();
            if (StringUtils.isBlank(confirmPersonUrl)) {
                throw new ErrorException("找不到验证人的签名");
            }
        }
        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/customer-satisfaction-questionnaire.docx");
        ConfigureBuilder builder = Configure.builder();
        String finalConfirmPersonUrl = confirmPersonUrl;
        XWPFTemplate template = XWPFTemplate.compile(inputStream, builder.build()).render(
                new HashMap<String, Object>() {{
                    put("clientSatisfaction", clientSatisfaction);
                    put("finalConfirmPersonUrl", StringUtils.isNotBlank(finalConfirmPersonUrl) ? Pictures.ofLocal(imgUrl + "/" + finalConfirmPersonUrl).create() : null);
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "客户满意度", "UTF-8");
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

    /**
     * 确认客户满意度
     *
     * @param clientSatisfaction 要修改客户满意度的状态对象
     * @param userId             修改人id
     */
    @Override
    public void confirmClientSatisfaction(ClientSatisfaction clientSatisfaction, Integer userId) {
        User user = userMapper.selectById(userId);
        if (user != null) {
            clientSatisfaction.setConfirmPerson(user.getName());
            clientSatisfaction.setConfirmPersonId(user.getId());
        }
        clientSatisfaction.setConfirmStatus(1);
        baseMapper.updateById(clientSatisfaction);
    }
}

