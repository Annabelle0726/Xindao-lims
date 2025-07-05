package com.ruoyi.personnel.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.personnel.dto.PersonCommunicationAbilityDto;
import com.ruoyi.personnel.mapper.PersonCommunicationAbilityMapper;
import com.ruoyi.personnel.pojo.PersonCommunicationAbility;
import com.ruoyi.personnel.service.PersonCommunicationAbilityService;
import com.ruoyi.system.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 沟通能力 服务实现类
 * </p>
 *
 * @author
 * @since 2024-10-09 12:00:57
 */
@Service
public class PersonCommunicationAbilityServiceImpl extends ServiceImpl<PersonCommunicationAbilityMapper, PersonCommunicationAbility> implements PersonCommunicationAbilityService {

    @Resource
    private UserMapper userMapper;

    @Override
    public IPage<PersonCommunicationAbilityDto> personPersonCommunicationAbilityPage(Page page, Integer departLimsId, Integer userId, String userName) {
        IPage<PersonCommunicationAbilityDto> personCommunicationAbilityDtoIPage = baseMapper.personPersonCommunicationAbilityPage(page, departLimsId, userId, userName);
        List<PersonCommunicationAbilityDto> collect = personCommunicationAbilityDtoIPage.getRecords().stream().map(personCommunicationAbilityDto -> {
            if (ObjectUtils.isNotEmpty(personCommunicationAbilityDto.getUserId())) {
                List<String> account = new ArrayList<>();
                List<String> name = new ArrayList<>();
                for (String s : personCommunicationAbilityDto.getUserId().split(",")) {
                    User user = userMapper.selectById(Integer.parseInt(s));
                    account.add(user.getAccount());
                    name.add(user.getName());
                }
                personCommunicationAbilityDto.setAccount(account.stream().collect(Collectors.joining(",")));
                personCommunicationAbilityDto.setUserName(name.stream().collect(Collectors.joining(",")));
            }
            return personCommunicationAbilityDto;  // 这里可以对返回的数据进行处理，如添加一些新的属性或转换等。
        }).collect(Collectors.toList());
        personCommunicationAbilityDtoIPage.setRecords(collect);
        return personCommunicationAbilityDtoIPage;
    }

    @Override
    public void exportPersonCommunicationAbility(Integer id, HttpServletResponse response) throws Exception {
        PersonCommunicationAbility personCommunicationAbility = baseMapper.selectById(id);
        //沟通人
        String collect = " ";
        if (ObjectUtils.isNotEmpty(personCommunicationAbility.getUserId())) {
            List<String> name = new ArrayList<>();
            for (String s : personCommunicationAbility.getUserId().split(",")) {
                User user = userMapper.selectById(Integer.parseInt(s));
                name.add(user.getName());
            }
            collect = name.stream().collect(Collectors.joining(","));
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");

        //时间
        String communicationTime = "";
        if (ObjectUtils.isNotEmpty(personCommunicationAbility.getCommunicationTime())) {
            communicationTime = personCommunicationAbility.getCommunicationTime().format(formatter);
        }

        InputStream inputStream = this.getClass().getResourceAsStream("/static/communication-deal.docx");
        ConfigureBuilder builder = Configure.builder();
        builder.useSpringEL(true);
        String finalCollect = collect;
        String finalCommunicationTime = communicationTime;
        XWPFTemplate template = XWPFTemplate.compile(inputStream, builder.build()).render(
                new HashMap<String, Object>() {{
                    put("userName", finalCollect);
                    put("communicationTime", finalCommunicationTime);
                    put("communicationPlace", personCommunicationAbility.getCommunicationPlace());
                    put("communicationContent", personCommunicationAbility.getCommunicationContent());
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "沟通记录", "UTF-8");
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
