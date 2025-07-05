package com.ruoyi.inspect.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.oa.OAProcess;
import com.ruoyi.inspect.dto.PushOADto;
import com.ruoyi.inspect.dto.UnqualifiedHandlerDto;
import com.ruoyi.inspect.pojo.InsUnqualifiedHandlerFile;
import com.ruoyi.inspect.service.InsUnqualifiedHandlerFileService;
import com.ruoyi.inspect.service.InsUnqualifiedHandlerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@Api("不合格处理管理")
@RequestMapping("/unqualifiedHandler")
@AllArgsConstructor
@Slf4j
public class InsUnqualifiedHandlerController {

    private final InsUnqualifiedHandlerService unqualifiedHandlerService;
    private final InsUnqualifiedHandlerFileService unqualifiedHandlerFileService;
    private static final String REQUESTID = "requestId";
    private static final String CHECKRESULT = "CHECKRESULT";




    @ApiOperation(value = "提交OA")
    @PostMapping("/pushOA")
    public Result pushOA(@RequestBody PushOADto pushOADto){
        return unqualifiedHandlerService.pushOA(pushOADto);
    }


    @ApiOperation(value = "查询")
    @GetMapping("/page")
    public Result pageByUnqualified(Page page, UnqualifiedHandlerDto unqualifiedHandlerDto){
        return Result.success(unqualifiedHandlerService.pageList(page, unqualifiedHandlerDto));
    }



    @ApiOperation(value = "附件上传")
    @PostMapping("/uploadFileByUnqualified")
    public Result<?> uploadFileByUnqualified(Long handlerId, MultipartFile file) {
        return Result.success(unqualifiedHandlerFileService.uploadFile(handlerId, file));
    }


    @Anonymous
    @ApiOperation(value = "不合格处理OA回调")
    @RequestMapping(value = "/callback", produces = "text/plain")
    public String unqualifiedHandlerOACallback(String data){
        String msg = OAProcess.oaReturnMsg(0, "success");
        JSONObject json = JSONObject.parseObject(data);
        log.info("oa回调参数========>" + json);
        try {
            Long oaWorkId = json.getLong(REQUESTID);
            String checkResult = json.getString(CHECKRESULT);
            Object o = json.get("workflowRequestLogs");
            JSONArray objects = JSONArray.parseArray(JSONObject.toJSONString(o));
            unqualifiedHandlerService.unqualifiedHandlerOACallback(oaWorkId, checkResult,objects);
        } catch (Exception e) {
            log.error("oa回调失败: " + e.getMessage());
            msg = OAProcess.oaReturnMsg(1, "oa回调失败: " + e.getMessage());
        }
        log.info("oa回调返回结果========>" + msg);
        return msg;
    }

    /**
     * 下载oa附件
     * @param handlerFileId
     * @param response
     */
    @GetMapping("/downloadOAFile/{handlerFileId}")
    public void downloadOAFile(@PathVariable("handlerFileId") Long handlerFileId, HttpServletResponse response){
        unqualifiedHandlerFileService.downloadOAFile(handlerFileId,response);
    }


    @ApiOperation(value = "新增不合格处理")
    @PostMapping("/addUnqualifiedHandler")
    public Result addUnqualifiedHandler(@RequestBody UnqualifiedHandlerDto unqualifiedHandlerDto){
        return Result.success(unqualifiedHandlerService.addUnqualifiedHandler(unqualifiedHandlerDto));
    }


    @ApiOperation(value = "编辑不合格处理")
    @PostMapping("/updateUnqualifiedHandler")
    public Result updateUnqualifiedHandler(@RequestBody UnqualifiedHandlerDto unqualifiedHandlerDto){
        return Result.success(unqualifiedHandlerService.updateUnqualifiedHandler(unqualifiedHandlerDto));
    }


    @ApiOperation(value = "查看oa流程")
    @GetMapping("/getOaFlow")
    public Result getOaFlow(Integer id){
        return Result.success(unqualifiedHandlerService.getOaFlow(id));
    }



    @ApiOperation(value = "查看不合格处理界面")
    @GetMapping("/getUnqualifiedHandler")
    public Result getUnqualifiedHandler(Integer id){
        return Result.success(unqualifiedHandlerService.getUnqualifiedHandler(id));
    }


    @ApiOperation(value = "删除不合格处理")
    @DeleteMapping("/deleteUnqualifiedHandler")
    public Result deleteUnqualifiedHandler(Integer id) {
        return Result.success(unqualifiedHandlerService.deleteUnqualifiedHandler(id));
    }


    @ApiOperation(value = "下载附件")
    @GetMapping("/downFile")
    public Result<?> oaDownFile(Integer id) {
        InsUnqualifiedHandlerFile insUnqualifiedHandlerFile = unqualifiedHandlerFileService.getById(id);
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", insUnqualifiedHandlerFile.getType());
        map.put("fileUrl", insUnqualifiedHandlerFile.getFileUrl());
        return Result.success(map);
    }

}
