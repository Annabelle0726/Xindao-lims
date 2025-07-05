package com.ruoyi.inspect.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.basic.dto.IfsInventoryQuantitySupplierDto;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.PersonalScope;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.inspect.dto.ReportPageDto;
import com.ruoyi.inspect.service.InsReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

@RestController
@RequestMapping("/insReport")
@Api(tags = "检验报告")
public class InsReportController {

    @Resource
    private InsReportService insReportService;

    @Value("${wordUrl}")
    private String wordUrl;

    @ApiOperation(value = "查询检验报告数据")
    @GetMapping("/pageInsReport")
    @PreAuthorize("@ss.hasPermi('business:reportPreparation')")
    @PersonalScope(permsName = "business:reportPreparation", objectName = ReportPageDto.class, paramName = "createOrderUser")
    public Result pageInsReport(Page page, ReportPageDto reportPageDto){
        return Result.success(insReportService.pageInsReport(page, reportPageDto));
    }

    @ApiOperation(value = "报告上传")
    @PostMapping("/inReport")
    public Result inReport(MultipartFile file, Integer id) {
        String urlString;
        String pathName;
        try {
            String path = wordUrl;
            File realpath = new File(path);
            if (!realpath.exists()) {
                realpath.mkdirs();
            }
            pathName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss")) + "_" + file.getOriginalFilename();
            urlString = realpath + "/" + pathName;
            file.transferTo(new File(urlString));
            return Result.success(insReportService.inReport("/word/" + pathName, id));
        } catch (Exception e) {
            throw new ErrorException("文件上传失败");
        }
    }

    @ApiOperation(value = "报告还原")
    @PostMapping("/upReportUrl")
    public Result upReportUrl(@RequestBody Map<String, Object> param) {
        Integer id = (Integer) param.get("id");
        return Result.success(insReportService.upReportUrl(id));
    }

    @ApiOperation(value = "报告在线编制")
    @GetMapping("/upReportFile")
    public Result upReportFile() {
        return Result.success();
    }

    @ApiOperation(value = "提交")
    @PostMapping("/writeReport")
    public Result writeReport(@RequestBody Map<String, Object> param) {
        Integer id = (Integer) param.get("id");
        Integer userId = (Integer) param.get("userId");
        return Result.success(insReportService.writeReport(id, userId, null));
    }

    /**
     * @param userId 需要的批准人
     * @return
     */

    @ApiOperation(value = "审核")
    @PostMapping("/examineReport")
    public Result examineReport(@RequestBody Map<String, Object> param) {
        Integer id = (Integer) param.get("id");
        Integer isExamine = (Integer) param.get("isExamine");
        String examineTell = (String) param.get("examineTell");
        Integer userId = (Integer) param.get("userId");
        return Result.success(insReportService.examineReport(id, isExamine, examineTell, userId));
    }

    @ApiOperation(value = "批准")
    @PostMapping("/ratifyReport")
    public Result ratifyReport(@RequestBody Map<String, Object> param) {
        Integer id = (Integer) param.get("id");
        Integer isRatify = (Integer) param.get("isRatify");
        String ratifyTell = (String) param.get("ratifyTell");
        return Result.success(insReportService.ratifyReport(id, isRatify, ratifyTell));
    }

    @Anonymous
    @RequestMapping("/onlyOffice/save")
    public void saveFile(@RequestParam String fileName, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            // 获取传输的json数据
            Scanner scanner = new Scanner(request.getInputStream()).useDelimiter("\\A");
            String body = scanner.hasNext() ? scanner.next() : "";
            JSONObject jsonObject = JSONObject.parseObject(body);

            if (jsonObject.containsKey("url")) {
                String jsonArray = jsonObject.get("lastsave").toString(); // 更新时间
                String fileUrl = jsonObject.get("url").toString(); // 更新文件url
                HttpUtil.downloadFile(fileUrl, FileUtil.file(wordUrl + "/" + fileName));
            }
        } catch (Exception e) {
            e.printStackTrace();
            writer.write("{\"error\":-1}");
            return;
        }
        /*
         * status = 1，我们给onlyOffice的服务返回{"error":"0"}的信息。
         * 这样onlyOffice会认为回调接口是没问题的，这样就可以在线编辑文档了，否则的话会弹出窗口说明
         */
        if (Objects.nonNull(writer)) {
            writer.write("{\"error\":0}");
        }
    }

    @ApiOperation(value = "报告批量下载")
    @GetMapping("/downAll")
    public Result downAll(String ids) {
        return Result.success(insReportService.downAll(ids));
    }

    @ApiOperation(value = "报告批量上传")
    @PostMapping("/upAll")
    public Result upAll(MultipartFile file) throws IOException {
        return Result.success(insReportService.upAll(file));
    }

    /**
     * 退回到检验任务
     * @param id
     * @return
     */
    @ApiOperation(value = "退回到检验任务")
    @PostMapping("/sendBackTask")
    public Result sendBackTask(@RequestBody Map<String, Object> param){
        Integer id = (Integer) param.get("id");
        return Result.success(insReportService.sendBackTask(id));
    }

    /**
     * 报告导出
     * @param dto
     * @param response
     * @throws Exception
     */
    @ApiOperation(value = "报告导出")
    @GetMapping("/reportAllExport")
    @PreAuthorize("@ss.hasPermi('business:reportPreparation')")
    @PersonalScope(permsName = "business:reportPreparation", objectName = ReportPageDto.class, paramName = "createOrderUser")
    public void reportAllExport(ReportPageDto dto, HttpServletResponse response) throws Exception {
        insReportService.reportAllExport(dto,response);
    }
}
