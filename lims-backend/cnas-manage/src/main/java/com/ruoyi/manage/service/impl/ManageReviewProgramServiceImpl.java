package com.ruoyi.manage.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.data.FilePictureRenderData;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.manage.mapper.ManageReviewProgramMapper;
import com.ruoyi.manage.pojo.ManageReviewProgram;
import com.ruoyi.manage.service.ManageReviewProgramService;
import com.ruoyi.system.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-09 03:05:42
 */
@Service
public class ManageReviewProgramServiceImpl extends ServiceImpl<ManageReviewProgramMapper, ManageReviewProgram> implements ManageReviewProgramService {


    @Resource
    private UserMapper userMapper;

    @Value("${file.path}")
    private String imgUrl;





    @Override
    public IPage<ManageReviewProgram> page(Page page, String startTime,String endTime, String judgingLocation) {
        IPage<ManageReviewProgram> iPage = this.baseMapper.page(page,startTime,endTime,judgingLocation);
        return iPage;
    }

    @Override
    public void exportReviewProgram(Integer id, HttpServletResponse response) {
        ManageReviewProgram reviewProgram = baseMapper.selectById(id);
        List<String> name = new ArrayList<>();
        for (String s : reviewProgram.getParticipants().split(",")) {
            User user = userMapper.selectById(Integer.parseInt(s));
            name.add(user.getName());
        }
        String participantsName = name.stream().collect(Collectors.joining(","));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        //编制人签名
        User user1 = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getName, reviewProgram.getEditor()));
        if (ObjectUtils.isEmpty(user1.getSignatureUrl())){
            throw new ErrorException(user1.getName()+"没有上传个人签名,请上传!");
        }
        String signatureUrl1 = user1.getSignatureUrl();
        //批准人签名
        if (ObjectUtils.isEmpty(reviewProgram.getApprove())){
            throw new ErrorException("没有进行批准");
        }
        User user2 = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getName, reviewProgram.getApprove()));
        if (ObjectUtils.isEmpty(user2.getSignatureUrl())){
            throw new ErrorException(user2.getName()+"没有上传个人签名,请上传!");
        }
        String signatureUrl2 = user2.getSignatureUrl();

        InputStream inputStream = this.getClass().getResourceAsStream("/static/review-program.docx");
        ConfigureBuilder builder = Configure.builder();
        builder.useSpringEL(true);
        XWPFTemplate template = XWPFTemplate.compile(inputStream, builder.build()).render(
                new HashMap<String, Object>() {{
                    put("reviewProgram", reviewProgram);
                    put("writeUrl", new FilePictureRenderData(100,50,imgUrl + "/" + signatureUrl1));
                    put("ratifyUrl", new FilePictureRenderData(100,50,imgUrl + "/" + signatureUrl2));
                    put("editorDate", reviewProgram.getEditorDate().format(formatter));
                    put("approveDate", reviewProgram.getApproveDate().format(formatter));
                    put("participantsName",participantsName);
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "管理评审计划", "UTF-8");
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

    @Override
    public int addReviewProgram(ManageReviewProgram manageReviewProgram) {
        Integer userId = SecurityUtils.getUserId().intValue();
        String name = userMapper.selectById(userId).getName();
        manageReviewProgram.setEditor(name);
        manageReviewProgram.setEditorDate(LocalDateTime.now());
        return baseMapper.insert(manageReviewProgram);
    }
}
