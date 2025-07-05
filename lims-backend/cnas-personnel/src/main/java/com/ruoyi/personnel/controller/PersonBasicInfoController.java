package com.ruoyi.personnel.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.core.domain.entity.DepartmentDto;
import com.ruoyi.common.core.domain.entity.DepartmentLims;
import com.ruoyi.common.utils.FileSaveUtil;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.personnel.dto.PersonBasicInfoDetailsDto;
import com.ruoyi.personnel.dto.PersonBasicInfoDto;
import com.ruoyi.personnel.dto.UserPageDto;
import com.ruoyi.personnel.pojo.Annex;
import com.ruoyi.personnel.pojo.PersonBasicInfoFile;
import com.ruoyi.personnel.pojo.PersonBasicInfoWork;
import com.ruoyi.personnel.service.AnnexService;
import com.ruoyi.personnel.service.PersonBasicInfoFileService;
import com.ruoyi.personnel.service.PersonBasicInfoService;
import com.ruoyi.personnel.service.PersonBasicInfoWorkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-08-30 09:19:57
 */
@Api(tags = "人员-人员基本信息")
@RestController
@RequestMapping("/personBasicInfo")
public class PersonBasicInfoController {

    @Resource
    private PersonBasicInfoService personBasicInfoService;

    @Resource
    private PersonBasicInfoFileService personBasicInfoFileService;
    @Resource
    private PersonBasicInfoWorkService personBasicInfoWorkService;

    @Resource
    private AnnexService annexService;


    @ApiOperation(value = "查询CNAS人员侧边栏")
    @GetMapping("/selectCNSAPersonTree")
    public Result<List<DepartmentDto>> selectCNSAPersonTree() {
        return Result.success(personBasicInfoService.selectCNSAPersonTree());
    }

    @ApiOperation(value = "获取CNAS人员基本信息")
    @GetMapping("/getCNASPersonnelInfo")
    public Result getCNASPersonnelInfo(Integer userId) {
        return Result.success(personBasicInfoService.getCNASPersonnelInfo(userId));
    }

    @ApiOperation(value = "保存CNAS人员基本信息")
    @PostMapping("/saveCNASPersonnelInfo")
    public Result saveCNASPersonnelInfo(@RequestBody PersonBasicInfoDto personBasicInfoDto) {
        personBasicInfoService.saveCNASPersonnelInfo(personBasicInfoDto);
        return Result.success();
    }

    @ApiOperation(value = "人员明细分页查询")
    @GetMapping("basicInformationOfPersonnelSelectPage")
    public Result<IPage<Map<String, Object>>> basicInformationOfPersonnelSelectPage(Page page, String name, Integer departmentId) {
        return Result.success(personBasicInfoService.basicInformationOfPersonnelSelectPage(page, name, departmentId));
    }

    // 上传文件接口
    @ApiOperation(value = "上传文件接口")
    @PostMapping("/saveCNASFile")
    public Result saveFile(@RequestPart("file") MultipartFile file) {
        String s = FileSaveUtil.uploadWordFile(file);
        return Result.success(s, "上传成功");
    }

    @GetMapping("/getAnnexByUserId")
    public Result<List<Annex>> getAnnexByUserId(Integer userId) {
        List<Annex> list = annexService.list(new LambdaQueryWrapper<Annex>().eq(Annex::getUserId, userId));
        return Result.success(list);
    }

    // 删除文件
    @DeleteMapping("/deleteCNASFile")
    public Result saveFile(String fileName) {
        String[] split = fileName.split(",");
        for (String s : split) {
            FileSaveUtil.DeleteFile(s);
        }
        return Result.success();

    }

    /**
     * 人员基本信息附件新增
     */
    @PostMapping("/addAnnex")
    public Result addAnnex(@RequestBody Annex annex) {
        annexService.save(annex);
        return Result.success();
    }

    @GetMapping("/getAnnex")
    public Result<Annex> getAnnex(Integer id) {
        return Result.success(annexService.getById(id));
    }
    /**
     * 人员基本信息附件删除
     */
    @DeleteMapping("/deleteAnnex")
    public Result deleteAnnex(Integer id) {
        annexService.removeById(id);
        return Result.success();
    }

    /**
     * 人员基本信息附件修改
     *
     */
    @PostMapping("/updateAnnex")
    public Result updateAnnex(@RequestBody Annex annex) {
        annexService.updateById(annex);
        return Result.success();
    }

    @ApiOperation(value = "导出人员基本信息")
    @GetMapping("/exportPersonBasicInfo")
    public void exportPersonBasicInfo(UserPageDto userPageDto, HttpServletResponse response) throws Exception {
        personBasicInfoService.exportPersonBasicInfo(userPageDto,response);
    }

    @ApiOperation(value = "下载人员档案卡")
    @GetMapping("/exportPersonBasicInfoById")
    public Result exportPersonBasicInfoById(Integer id, HttpServletResponse response) {
        return Result.success(personBasicInfoService.exportPersonBasicInfoById(id,response));
    }

    /**
     * 人员培训基本信息附件新增
     * @param userId
     * @param file
     * @return
     */
    @ApiOperation(value = "人员培训基本信息附件新增")
    @PostMapping("/uploadBasicInfoFile")
    public Result<?> uploadBasicInfoFile(Integer userId, MultipartFile file) {
        return Result.success(personBasicInfoService.uploadBasicInfoFile(userId, file));
    }


    /**
     * 人员培训基本信息附件列表
     * @return
     */
    @ApiOperation(value = "人员培训基本信息附件列表")
    @GetMapping("/getBasicInfoFileList")
    public Result<List<PersonBasicInfoFile>> getBasicInfoFileList(Integer userId){
        return Result.success(personBasicInfoFileService.list(Wrappers.<PersonBasicInfoFile>lambdaQuery()
                .eq(PersonBasicInfoFile::getUserId, userId)));
    }

    /**
     * 人员培训基本信息附件删除
     * @return
     */
    @ApiOperation(value = "人员培训基本信息附件删除")
    @DeleteMapping("/delBasicInfoFileList")
    public Result delBasicInfoFileList(Integer basicInfoFileId){
        return Result.success(personBasicInfoFileService.removeById(basicInfoFileId));
    }

    /**
     * 人员培训基本信息工作经历新增
     * @return
     */
    @ApiOperation(value = "人员培训基本信息工作经历新增")
    @PostMapping("/addBasicInfoWork")
    public Result<?> addBasicInfoWork(@RequestBody PersonBasicInfoWork basicInfoWork) {
        if (basicInfoWork.getUserId() == null) {
            throw new ErrorException("缺少人员id");
        }
        basicInfoWork.setUserId(basicInfoWork.getUserId());
        return Result.success(personBasicInfoWorkService.save(basicInfoWork));
    }


    /**
     * 人员工作经历列表
     * @return
     */
    @ApiOperation(value = "人员工作经历列表")
    @GetMapping("/getBasicInfoWorkList")
    public Result<List<PersonBasicInfoWork>> getBasicInfoWorkList(Integer userId){
        return Result.success(personBasicInfoWorkService.list(Wrappers.<PersonBasicInfoWork>lambdaQuery()
                .eq(PersonBasicInfoWork::getUserId, userId)));
    }

    /**
     * 人员工作经历删除
     * @return
     */
    @ApiOperation(value = "人员工作经历删除")
    @DeleteMapping("/delBasicInfoWorkList")
    public Result delBasicInfoWorkList(Integer basicInfoWorkId){
        return Result.success(personBasicInfoWorkService.removeById(basicInfoWorkId));
    }

    /**
     * 人员基本信息附件删除
     * @return
     */
    @ApiOperation(value = "人员工作经历修改")
    @PostMapping("/updateBasicInfoWorkList")
    public Result updateBasicInfoWorkList(@RequestBody PersonBasicInfoWork basicInfoWork){
        return Result.success(personBasicInfoWorkService.updateById(basicInfoWork));
    }


    @ApiOperation(value = "添加组织架构")
    @PostMapping("/addDepartmentLims")
    public Result addDepartmentLims(@RequestBody DepartmentLims department) {
        return Result.success(personBasicInfoService.addDepartment(department));
    }

    @ApiOperation(value = "删除组织架构")
    @DeleteMapping("/delDepartmentLims")
    public Result delDepartmentLims(Integer id) {
        return Result.success(personBasicInfoService.delDepartment(id));
    }
}
