package com.ruoyi.web.controller.api;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.log.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.config.PersonnelProperties;
import com.ruoyi.common.core.domain.entity.Custom;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.mapper.UserMapper;
import com.ruoyi.system.service.CustomService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 获取人事系统
 */
@Component
public class PersonnelHeaderApi {

    @Resource
    private PersonnelProperties personnelProperties;
    @Resource
    private CustomService customService;
    @Resource
    private UserMapper userMapper;

    public String fetchNewAccessToken() {
        HttpRequest request = HttpRequest.post(personnelProperties.getCode())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .form("grant_type", "client_credentials")
                .form("client_id", personnelProperties.getAppId())
                .form("client_secret", personnelProperties.getAppSecret());
        HttpResponse response = request.execute();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(response.body());
            String accessToken = jsonNode.get("access_token").asText();
            return accessToken;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    //判断是否存在
    public String getAccessToken() {
        String accessToken;
        accessToken = fetchNewAccessToken();
        return accessToken;
    }

    //调用
    public List<Company> companyUrl() {
        String accessToken = getAccessToken();
        HttpRequest request = HttpRequest.get(personnelProperties.getCompanies())
                .header("Authorization", "Bearer " + accessToken);
        List<Company> companies;
        try {
            companies = JSON.parseArray(request.execute().body(), Company.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return companies.stream().filter(ob -> {
            if (Objects.equals(ob.getStatus(), "enabled")) return true;
            return false;
        }).collect(Collectors.toList());
    }

    public List<Person> userUrl(String companyId) {
        String accessToken = getAccessToken();
        HttpRequest request = HttpRequest.get(personnelProperties.getSimple() + companyId)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/form-data");
        List<Person> person;
        try {
            person = JSON.parseArray(request.execute().body(), Person.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        List<JSONObject> department = getDepartment(companyId);
        return person.stream().filter(ob -> {
            if (Objects.equals(ob.getStatus(), "enabled")) {
                ob.setIsLive(userMapper.selectCount(Wrappers.<User>lambdaQuery().eq(User::getAccount, ob.getEmployeeID())));
                ob.setDepartment(getDepartmentStr(department, ob.getDepartmentCode()).replaceFirst("/", ""));
                return true;
            }
            return false;
        }).collect(Collectors.toList());
    }

    public String getDepartmentStr(List<JSONObject> department, String code) {
        String str = "";
        Optional<JSONObject> depart = department.stream().filter(a -> code.equals(a.get("departmentCode"))).findFirst();
        str = "/" + depart.get().get("departmentName") + str;
        if (depart.get().get("parentDepartmentCode") != null) {
            str = getDepartmentStr(department, depart.get().get("parentDepartmentCode").toString()) + str;
        }
        return str;
    }

    public List<JSONObject> getDepartment(String companyId) {
        String accessToken = getAccessToken();
        HttpRequest request = HttpRequest.get(personnelProperties.getDepartment().replace("companyId", companyId))
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/form-data");
        List<JSONObject> list;
        try {
            list = JSON.parseArray(request.execute().body());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return list.stream().filter(ob -> {
            if (Objects.equals(ob.get("status"), "enabled")) return true;
            return false;
        }).collect(Collectors.toList());
    }

    public Person selectPersonUser(String code) {
        String accessToken = getAccessToken();
        HttpRequest request = HttpRequest.get(personnelProperties.getPerson() + code)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/form-data");
        Person person;
        try {
            person = JSON.parseObject(request.execute().body(), Person.class);
            if (BeanUtil.isEmpty(person)) return null;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        List<JSONObject> department = getDepartment(person.getCompanyId());
        person.setDepartment(getDepartmentStr(department, person.getDepartmentCode()).replaceFirst("/", ""));
        person.setIsLive(userMapper.selectCount(Wrappers.<User>lambdaQuery().eq(User::getAccount, person.getEmployeeID())));
        return person;
    }

    /**
     * 将人事系统勾选的内容转移到本系统
     * @param personDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Object addPersonUser(PersonDto personDto) {
        personDto.getPerson().forEach(person -> {
            User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getAccount, person.getEmployeeID()));
            List<Company> company = personDto.getCompany();
            String companyName = "";
            Custom custom = null;
            // 填充工厂信息
            companyName = company.stream().filter(a -> a.getCompanyId().equals(person.getCompanyId())).findFirst().get().getCompanyName();
            custom = customService.getCustomId(companyName);

            if (BeanUtil.isEmpty(user)) {
                user = new User();
                user.setName(person.getName());
                user.setNameEn("not write");
                user.setAccount(person.getEmployeeID());
                user.setPhone(person.getPhoneNumber());
                user.setEmail(person.getCompanyEmail());
                user.setIsCustom(0);
                user.setPassword(SecurityUtils.encryptPassword("zttZTT123!"));
                user.setCompany(BeanUtil.isNotEmpty(custom) ? (custom.getId() + "") : companyName);
                userMapper.insert(user);
            } else {
                user.setName(person.getName());
                user.setPhone(person.getPhoneNumber());
                user.setEmail(person.getCompanyEmail());
                user.setIsCustom(0);
                user.setCompany(BeanUtil.isNotEmpty(custom) ? (custom.getId() + "") : companyName);
                userMapper.updateById(user);
            }
        });
        return 1;
    }
}
