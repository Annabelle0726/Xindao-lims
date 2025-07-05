package com.ruoyi.basic.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.basic.pojo.Certification;
import com.ruoyi.basic.service.CertificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "资质说明")
@AllArgsConstructor
@RestController
@RequestMapping("/certification")

public class CertificationController {

    private CertificationService certificationService;


    @ApiOperation(value = "查询资质明细列表")
    @GetMapping("/getCertificationDetail")
    public Result getCertificationDetail(Page page,Certification certification) {
        return Result.success(certificationService.getCertificationDetail(page, certification));
    }

    @ApiOperation(value = "添加资质明细列表")
    @PostMapping("/addCertificationDetail")
    public Result addCertificationDetail(@RequestBody Certification certification) {
        return Result.success(certificationService.addCertificationDetail(certification));
    }
    @ApiOperation(value = "删除资质明细列表")
    @DeleteMapping("/delCertificationDetail")
    public Result<?> delCertificationDetail(String ids) {
        return Result.success(certificationService.delCertificationDetail(ids));
    }
}
