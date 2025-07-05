package com.ruoyi.personnel.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.personnel.dto.PersonJobResponsibilitiesDto;
import com.ruoyi.personnel.mapper.PersonJobResponsibilitiesMapper;
import com.ruoyi.personnel.pojo.PersonJobResponsibilities;
import com.ruoyi.personnel.service.PersonJobResponsibilitiesService;
import com.ruoyi.system.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * <p>
 * 岗位职责 服务实现类
 * </p>
 *
 * @author
 * @since 2024-10-09 02:07:49
 */
@Service
@AllArgsConstructor
public class PersonJobResponsibilitiesServiceImpl extends ServiceImpl<PersonJobResponsibilitiesMapper, PersonJobResponsibilities> implements PersonJobResponsibilitiesService {

    @Resource
    private UserMapper userMapper;


    @Override
    public IPage<PersonJobResponsibilitiesDto> personJobResponsibilitiesSelect(Page page, String userId, String departmentId, String userName) {
        return baseMapper.personJobResponsibilitiesSelect(page, userId, departmentId, userName);
    }

    @Override
    public void exportPersonJobResponsibilities(Integer id, HttpServletResponse response) {
        PersonJobResponsibilities personJobResponsibilities = baseMapper.selectById(id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        //岗位名称
        String name = personJobResponsibilities.getPostName();
        //所属部门
        String departLims = userMapper.selectUserDepartmentLimsName(Integer.parseInt(personJobResponsibilities.getIncumbentId()));
        //工作目标
        String objective = personJobResponsibilities.getJobObjective();
        //岗位职责
        String responsibilities = personJobResponsibilities.getJobResponsibilities();
        //任职人
        User user = userMapper.selectById(Integer.parseInt(personJobResponsibilities.getIncumbentId()));
        String incumbent = user.getName();
        //任职时间
        String incumbentDate = "";
        if (ObjectUtils.isNotEmpty(personJobResponsibilities.getIncumbentDate())) {
            incumbentDate = personJobResponsibilities.getIncumbentDate().format(formatter);
        }
        //主管
        String supervisor = "";
        if (ObjectUtils.isNotEmpty(personJobResponsibilities.getSupervisorId())) {
            supervisor = userMapper.selectById(personJobResponsibilities.getSupervisorId()).getName();
        }
        //主管时间
        String supervisorDate = "";
        if (ObjectUtils.isNotEmpty(personJobResponsibilities.getSupervisorDate())) {
            supervisorDate = personJobResponsibilities.getSupervisorDate().format(formatter);
        }

        InputStream inputStream = this.getClass().getResourceAsStream("/static/explain-deal.docx");
        ConfigureBuilder builder = Configure.builder();
        builder.useSpringEL(true);
        String finalIncumbentDate = incumbentDate;
        String finalSupervisor = supervisor;
        String finalSupervisorDate = supervisorDate;
        XWPFTemplate template = XWPFTemplate.compile(inputStream, builder.build()).render(
                new HashMap<String, Object>() {{
                    put("name", name);
                    put("account", user.getAccount());
                    put("departLims", departLims);
                    put("objective", objective);
                    put("responsibilities", responsibilities);
                    put("incumbent", incumbent);
                    put("incumbentDate", finalIncumbentDate);
                    put("supervisor", finalSupervisor);
                    put("supervisorDate", finalSupervisorDate);
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    name + "的任职岗位说明书", "UTF-8");
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
