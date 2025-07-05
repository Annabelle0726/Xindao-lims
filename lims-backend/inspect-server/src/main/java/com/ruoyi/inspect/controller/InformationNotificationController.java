package com.ruoyi.inspect.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.system.service.InformationNotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 消息通知 前端控制器
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-04-23 02:14:30
 */
@Api(tags = "消息通知")
@RestController
@RequestMapping("/informationNotification")
public class InformationNotificationController {

    @Autowired
    private InformationNotificationService informationNotificationService;

    @ApiOperation(value = "消息通知-滚动分页查询")
    @GetMapping("page")
    public Result<?> getPage(Long size, Long current, String messageType) {
        return Result.success(informationNotificationService.getPage(new Page<>(current, size), messageType));
    }

    @ApiOperation(value = "消息通知-滚动查询")
    @GetMapping("msgRoll")
    public Result<?> msgRoll(Page page) {
        return Result.success(informationNotificationService.msgRoll(page));
    }

    @ApiOperation(value = "消息通知-点击详情触发修改状态为已读")
    @PostMapping("triggerModificationStatusToRead")
    public Result<?> triggerModificationStatusToRead(@RequestBody Map<String, Integer> params) {
        Integer id = params.get("id");
        informationNotificationService.triggerModificationStatusToRead(id);
        return Result.success();
    }

    @ApiOperation(value = "消息通知-标记所有信息为已读/删除所有已读消息")
    @PostMapping("informationReadOrDelete/{isMarkAllInformationRead}")
    public Result<?> markAllInformationReadOrDeleteAllReadMessages(@PathVariable("isMarkAllInformationRead") Boolean isMarkAllInformationRead) {
        informationNotificationService.markAllInformationReadOrDeleteAllReadMessages(isMarkAllInformationRead);
        return Result.success();
    }
}
