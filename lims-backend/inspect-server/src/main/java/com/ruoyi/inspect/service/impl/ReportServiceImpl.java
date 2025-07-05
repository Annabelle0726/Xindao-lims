package com.ruoyi.inspect.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.inspect.mapper.*;
import com.ruoyi.inspect.pojo.InsOrder;
import com.ruoyi.inspect.pojo.InsSample;
import com.ruoyi.inspect.pojo.InsSampleUser;
import com.ruoyi.inspect.service.ReportService;
import com.ruoyi.performance.dto.AuxiliaryAllDto;
import com.ruoyi.performance.dto.AuxiliaryOriginalHoursLookDto;
import com.ruoyi.performance.service.AuxiliaryOriginalHoursService;
import com.ruoyi.system.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {

    @Autowired
    private InsOrderMapper insOrderMapper;

    @Autowired
    private InsSampleMapper insSampleMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private InsSampleUserMapper insSampleUserMapper;

    @Autowired
    private AuxiliaryOriginalHoursService auxiliaryOriginalHoursService;

    //首页-->日历任务图
    @Override
    public Map<String, Object> calendarWorkByWeek() {
        Integer userId = SecurityUtils.getUserId().intValue();
        //
        List<SysRole> roleList = SecurityUtils.getLoginUser().getUser().getRoles();

//        String name = roleMapper.selectById(userMapper.selectById(userId).getRoleId()).getName();
        String name = roleList.stream().map(SysRole::getRoleName).collect(Collectors.joining(","));
        Map<String, Object> map = new HashMap<>();
        List<Integer> insState = new ArrayList<>();
        insState.add(0);
        insState.add(1);
        /*获取后一周日期*/
        LocalDate currentDate = LocalDate.now();
        List<LocalDate> weekDays = new ArrayList<>();
        for (int i = 6, j = 0; i >= 0; i--, j++) {
            weekDays.add(currentDate.minusDays(i));
            //查询当天需要检测的委托订单
            List<InsOrder> insOrders = insOrderMapper.selectList(Wrappers.<InsOrder>lambdaQuery()
                    .eq(InsOrder::getState, 1)
                    .in(InsOrder::getInsState, insState)
                    .apply("DATE(create_time) = CURDATE() - INTERVAL " + j + " DAY"));
            //如果当前登录人是测试工程师或者是检测组长,需要过滤出检验人是他们的订单或者是还没检验的订单
            if (name.contains("测试工程师") || name.contains("检测组长")) {
                insOrders = insOrders.stream().filter(insOrder -> {
                    List<InsSample> insSamples = insSampleMapper.selectList(Wrappers.<InsSample>lambdaQuery().eq(InsSample::getInsOrderId, insOrder.getId()));
                    List<Integer> sampleId = insSamples.stream().map(InsSample::getId).collect(Collectors.toList());
                    List<InsSampleUser> insSampleUsers = insSampleUserMapper.selectList(Wrappers.<InsSampleUser>lambdaQuery()
                            .in(InsSampleUser::getInsSampleId, sampleId)
                            .eq(InsSampleUser::getState, 0)  //检验人
                    );
                    return insSampleUsers.size() == 0 || insSampleUsers.stream().map(InsSampleUser::getUserId).collect(Collectors.toList()).contains(userId);
                }).collect(Collectors.toList());
            }
            //如果当前登录人是送样员,需过滤出单子的送样员是当前人的订单
            else if (name.contains("送样员")) {
                insOrders = insOrders.stream().filter(insOrder ->
                        ObjectUtils.isNotEmpty(insOrder.getIssueUser()) && insOrder.getIssueUser().equals(userId)
                ).collect(Collectors.toList());
            }
            List<Map<String, Object>> works = insOrders.stream().map(insOrder -> {
                List<InsSample> insSamples = insSampleMapper.selectList(Wrappers.<InsSample>lambdaQuery().eq(InsSample::getInsOrderId, insOrder.getId()));
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("text", insOrder.getEntrustCode());
                hashMap.put("sample", insSamples.stream().map(InsSample::getSample).collect(Collectors.joining(",")));
                hashMap.put("type", insOrder.getType());
                hashMap.put("state", insOrder.getState());
                User user = userMapper.selectById(insOrder.getCreateUser());
                hashMap.put("name", user.getName());
                return hashMap;
            }).collect(Collectors.toList());
            map.put("work" + i, works);
        }
        map.put("weekDays", weekDays);
        return map;
    }

    @Override
    public AuxiliaryAllDto currentUserWorkHourCount(AuxiliaryOriginalHoursLookDto dto) {
        List<AuxiliaryAllDto> auxiliaryAllDtos = auxiliaryOriginalHoursService.selectAuxiliaryAllByMonth(dto);
        int userId = SecurityUtils.getUserId().intValue();
        // 过滤掉非当前用户数据
        AuxiliaryAllDto auxiliaryAllDto = null;
        for (AuxiliaryAllDto allDto : auxiliaryAllDtos) {
            if(userId == allDto.getUserId()){
                auxiliaryAllDto = allDto;
                break;
            }
        }
        if(ObjectUtils.isEmpty(auxiliaryAllDto)){
            auxiliaryAllDto = new AuxiliaryAllDto(
                new BigDecimal(0),
                new BigDecimal(0),
                new BigDecimal(0),
                userId,SecurityUtils.getUsername(),
                dto.getMonth()
            );
        }
        return auxiliaryAllDto;
    }
}
