package com.ruoyi.personnel.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.data.Pictures;
import com.ruoyi.common.constant.MenuJumpPathConstants;
import com.ruoyi.common.core.domain.entity.InformationNotification;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.DateImageUtil;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.WxCpUtils;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.personnel.dto.PersonPersonnelCapacityDto;
import com.ruoyi.personnel.dto.PersonPersonnelCapacityExportDto;
import com.ruoyi.personnel.pojo.PersonPersonnelCapacity;
import com.ruoyi.personnel.mapper.PersonPersonnelCapacityMapper;
import com.ruoyi.personnel.service.PersonPersonnelCapacityService;
import com.ruoyi.system.mapper.UserMapper;
import com.ruoyi.system.service.InformationNotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * <p>
 * 人员能力 服务实现类
 * </p>
 *
 * @author
 * @since 2024-10-10 11:26:18
 */
@Service
public class PersonPersonnelCapacityServiceImpl extends ServiceImpl<PersonPersonnelCapacityMapper, PersonPersonnelCapacity> implements PersonPersonnelCapacityService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private InformationNotificationService informationNotificationService;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


    @Value("${file.path}")
    private String imgUrl;

    @Override
    public IPage<PersonPersonnelCapacityDto> personPersonnelCapacityPage(Page page, Integer departLimsId, Integer userId, String userName) {
        return baseMapper.personPersonnelCapacityPage(page, departLimsId, userId, userName);
    }

    /**
     * 导出人员能力
     * @param id
     * @param response
     */
    @Override
    public void exportPersonnelCapacity(Integer id, HttpServletResponse response) {
        PersonPersonnelCapacityExportDto capacityExportDto = baseMapper.selectExportPersonnelCapacity(id);

        // 确认人
        String confirmUrl = null;
        if (capacityExportDto.getConfirmOperatingPersonnelId() != null) {
            confirmUrl = userMapper.selectById(capacityExportDto.getConfirmOperatingPersonnelId()).getSignatureUrl();
            if (StringUtils.isBlank(confirmUrl)) {
                throw new ErrorException("缺少确认人签名");
            }
        }

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/personnel-capacity.docx");
        ConfigureBuilder builder = Configure.builder();
        builder.useSpringEL(true);
        String finalConfirmUrl = confirmUrl;
        XWPFTemplate template = XWPFTemplate.compile(inputStream, builder.build()).render(
                new HashMap<String, Object>() {{
                    put("capacity", capacityExportDto);
                    put("confirmUrl", StringUtils.isNotBlank(finalConfirmUrl) ? Pictures.ofLocal(imgUrl + "/" + finalConfirmUrl).create() : null);
                    put("confirmDateUrl", capacityExportDto.getConfirmDate() != null ?
                            Pictures.ofStream(DateImageUtil.createDateImage(capacityExportDto.getConfirmDate())).create() : null);
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "人员能力", "UTF-8");
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
     * 提交确认人员能力
     * @param personPersonnelCapacity
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitConfirmPersonnelCapability(PersonPersonnelCapacity personPersonnelCapacity) {
        if (personPersonnelCapacity.getConfirmOperatingPersonnelId() == null) {
            throw new ErrorException("缺少确认人");
        }
        User formUser = userMapper.selectById(personPersonnelCapacity.getUserId());

        Integer userId = SecurityUtils.getUserId().intValue();
        User user = userMapper.selectById(userId);
        // 消息发送
        InformationNotification info = new InformationNotification();
        // 发送人
        info.setCreateUser(user.getName());
        info.setMessageType("6");
        info.setTheme("CNAS人员能力确认通知");
        info.setContent(formUser.getName() + "的人员能力待确认");
        info.setSenderId(userId);
        // 接收人
        info.setConsigneeId(personPersonnelCapacity.getConfirmOperatingPersonnelId());
        info.setJumpPath(MenuJumpPathConstants.PERSONNEL);
        informationNotificationService.addInformationNotification(info);
        this.saveOrUpdate(personPersonnelCapacity);

        // 发送企业微信通知
        threadPoolTaskExecutor.execute(() -> {
            // 查询接收人
            User personnel = userMapper.selectById(personPersonnelCapacity.getConfirmOperatingPersonnelId());

            String message = "";
            message += "CNAS人员能力确认通知";
            message += "\n请去资源管理-人员-人员能力填写";
            message += "\n" + formUser.getName() + "的人员能力待确认";
            //发送企业微信消息通知
            try {
                WxCpUtils.inform(personnel.getAccount(), message, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }

}
