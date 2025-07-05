package com.ruoyi.personnel.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.data.FilePictureRenderData;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.personnel.dto.PersonPostAuthorizationRecordDto;
import com.ruoyi.personnel.mapper.PersonPostAuthorizationRecordMapper;
import com.ruoyi.personnel.pojo.PersonPostAuthorizationRecord;
import com.ruoyi.personnel.service.PersonPostAuthorizationRecordService;
import com.ruoyi.system.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * <p>
 * 任职授权记录 服务实现类
 * </p>
 *
 * @author
 * @since 2024-10-09 10:48:17
 */
@Service
public class PersonPostAuthorizationRecordServiceImpl extends ServiceImpl<PersonPostAuthorizationRecordMapper, PersonPostAuthorizationRecord> implements PersonPostAuthorizationRecordService {

    @Resource
    private UserMapper userMapper;

    @Value("${file.path}")
    private String imgUrl;

    @Override
    public IPage<PersonPostAuthorizationRecordDto> personPostAuthorizationRecordPage(Page page, Integer departLimsId, Integer userId, String userName) {
        return baseMapper.personPostAuthorizationRecordPage(page, departLimsId, userId, userName);
    }

    @Override
    public void exportPersonPostAuthorizationRecord(Integer id, HttpServletResponse response) {
        PersonPostAuthorizationRecord personPostAuthorizationRecord = baseMapper.selectById(id);
        //姓名
        User user = userMapper.selectById(personPostAuthorizationRecord.getUserId());
        String name = user.getName();
        //todo: 人员任职授权记录姓名英文
//        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
//        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
//        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//        StringBuilder pinyinBuilder = new StringBuilder();
//        for (int i = 0; i < name.length(); i++) {
//            char c = name.charAt(i);
//            // 判断是否是汉字
//            if (Character.toString(c).matches("[\\u4E00-\\u9FFF]")) {
//                // 获取汉字的拼音数组
//                String[] pinyinArray = new String[0];
//                try {
//                    pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c, format);
//                } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
//
//                }
//                if (pinyinArray!= null && pinyinArray.length > 0) {
//                    pinyinBuilder.append(pinyinArray[0]);
//                }
//            } else {
//                pinyinBuilder.append(c);
//            }
//        }
//        String nameEn = pinyinBuilder.toString();
        String nameEn = user.getNameEn();
        //理论知识考试成绩
        String num1=personPostAuthorizationRecord.getNum1();
        //操作技能考试成绩
        String num2=personPostAuthorizationRecord.getNum2();
        //证书编号
        String code = personPostAuthorizationRecord.getCertificateNumber();
        //发证时间
        LocalDateTime createTime = personPostAuthorizationRecord.getCreateTime();
        String year = createTime.getYear() + "";
        String mon = createTime.getMonth().getValue() + "";
        String day = createTime.getDayOfMonth() + "";
        //个人照片
        if (ObjectUtils.isEmpty(user.getPictureUrl())) {
            throw new ErrorException(name+"的个人照片没有上传");
        }
        String pictureUrl = user.getPictureUrl();


        InputStream inputStream = this.getClass().getResourceAsStream("/static/credentials-deal.docx");
        ConfigureBuilder builder = Configure.builder();
        builder.useSpringEL(true);
        XWPFTemplate template = XWPFTemplate.compile(inputStream, builder.build()).render(
                new HashMap<String, Object>() {{
                    put("name", name);
                    put("nameEn", nameEn);
                    put("num1", num1);
                    put("num2", num2);
                    put("code", code);
                    put("year", year);
                    put("mon", mon);
                    put("day", day);
                    put("writeUrl", new FilePictureRenderData(100,50,imgUrl + "/" + pictureUrl));
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    name+"的岗位职业资格正式", "UTF-8");
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
