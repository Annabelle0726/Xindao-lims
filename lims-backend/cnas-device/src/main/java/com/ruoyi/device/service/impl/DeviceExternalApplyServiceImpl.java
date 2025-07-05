package com.ruoyi.device.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.device.mapper.DeviceExternalApplyMapper;
import com.ruoyi.device.pojo.DeviceExternalApply;
import com.ruoyi.device.service.DeviceExternalApplyService;
import com.ruoyi.inspect.util.UserUtils;
import com.ruoyi.system.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.HashMap;

/**
 * <p>
 * 利用外部设备申请表 服务实现类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-17 10:28:43
 */
@Service
public class DeviceExternalApplyServiceImpl extends ServiceImpl<DeviceExternalApplyMapper, DeviceExternalApply> implements DeviceExternalApplyService {

    @Resource
    private UserMapper userMapper;

    /**
     * 利用外部设备申请列表
     * @param page
     * @param deviceExternalApply
     * @return
     */
    @Override
    public IPage<DeviceExternalApply> pageDeviceExternalApply(Page page, DeviceExternalApply deviceExternalApply) {
        return baseMapper.pageDeviceExternalApply(page, QueryWrappers.queryWrappers(deviceExternalApply));
    }

    /**
     * 新增利用外部设备申请
     * @param deviceExternalApply
     * @return
     */
    @Override
    public boolean addDeviceExternalApply(DeviceExternalApply deviceExternalApply) {
        DeviceExternalApply apply = new DeviceExternalApply();
        // 当前登录用户信息和部门
        User user = userMapper.selectById(SecurityUtils.getUserId().intValue());
        switch (deviceExternalApply.getFlowType()) {
            case 0:
                BeanUtils.copyProperties(deviceExternalApply, apply);
                // 申请
                apply.setUseReason(deviceExternalApply.getUseReason());
                apply.setApplicantUserId(user.getId());
                apply.setApplicantUser(user.getName());
                apply.setApplicantDate(LocalDate.now());

                // 处理人信息
                User departmentHeadUser = userMapper.selectById(deviceExternalApply.getDepartmentHeadUserId());
                apply.setApplicantUserId(departmentHeadUser.getId());
                apply.setApplicantUser(departmentHeadUser.getName());

                baseMapper.insert(apply);
                break;
            case 1:
                apply.setExternalApplyId(deviceExternalApply.getExternalApplyId());
                // 申请部门负责人意见
                apply.setDepartmentHeadOpinion(deviceExternalApply.getDepartmentHeadOpinion());
                apply.setDepartmentHeadDate(LocalDate.now());

                // 计量室信息
                User meteringRoomUser = userMapper.selectById(deviceExternalApply.getMeteringRoomUserId());
                apply.setMeteringRoomUserId(meteringRoomUser.getId());
                apply.setMeteringRoomUser(meteringRoomUser.getName());

                baseMapper.updateById(apply);
                break;
            case 2:
                apply.setExternalApplyId(deviceExternalApply.getExternalApplyId());
                // 计量室意见
                apply.setMeteringRoomOpinion(deviceExternalApply.getMeteringRoomOpinion());
                apply.setMeteringRoomDate(LocalDate.now());

                // 批准人信息
                User approverUser = userMapper.selectById(deviceExternalApply.getApproverUserId());
                apply.setApproverUserId(approverUser.getId());
                apply.setApproverUser(approverUser.getName());

                baseMapper.updateById(apply);
                break;
            case 3:
                apply.setExternalApplyId(deviceExternalApply.getExternalApplyId());
                //批准人
                apply.setApproverOpinion(deviceExternalApply.getApproverOpinion());
                apply.setApproverDate(LocalDate.now());

                apply.setIsFinish(1);
                baseMapper.updateById(apply);
                break;
        }

        return true;
    }

    /**
     * 导出利用外部设备申请
     *
     * @param externalApplyId 外部设备申请id
     * @param response
     */
    @Override
    public void exportDeviceExternalApply(Integer externalApplyId, HttpServletResponse response) {
        // 查询外部设备申请
        DeviceExternalApply deviceAccidentReport = baseMapper.selectDeviceExternalById(externalApplyId);

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/word/device-external-apply.docx");
        Configure configure = Configure.builder()
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("deviceAccidentReport", deviceAccidentReport);
                    // 申请人签名
                    put("applicantUserUrl", UserUtils.getFinalUserSignatureUrl(deviceAccidentReport.getApplicantUserId()));
                    // 部门负责人签名
                    put("departmentHeadUserUrl", UserUtils.getFinalUserSignatureUrl(deviceAccidentReport.getDepartmentHeadUserId()));
                    // 计量室人签名
                    put("meteringRoomUserUrl", UserUtils.getFinalUserSignatureUrl(deviceAccidentReport.getMeteringRoomUserId()));
                    // 批准人签名
                    put("approverUserUrl", UserUtils.getFinalUserSignatureUrl(deviceAccidentReport.getApproverUserId()));
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "利用外部设备申请", "UTF-8");
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
