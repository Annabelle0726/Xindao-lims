package com.ruoyi.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.process.dto.ProcessMethodVerifyDto;
import com.ruoyi.process.pojo.ProcessMethodVerify;
import com.ruoyi.process.pojo.ProcessMethodVerifyMethodFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 标准方法验证
 *
 * @author zhuo
 * @since 2024-11-05
 */
public interface ProcessMethodVerifyService extends IService<ProcessMethodVerify> {

    /**
     * 标准方法跟新验证列表
     * @param page
     * @param methodVerifyDto
     * @return
     */
    IPage<ProcessMethodVerify> pagesMethodVerify(Page page, ProcessMethodVerifyDto methodVerifyDto);

    /**
     * 新增标准方法验证
     * @param methodVerifyDto
     * @return
     */
    boolean addMethodSearchNew(ProcessMethodVerifyDto methodVerifyDto);

    /**
     * 查询标准方法验证详情
     * @param methodVerifyId
     * @return
     */
    ProcessMethodVerifyDto getMethodVerifyOne(Integer methodVerifyId);

    /**
     * 修改标准方法验证
     * @param methodVerifyDto
     * @return
     */
    boolean updateMethodVerify(ProcessMethodVerifyDto methodVerifyDto);

    /**
     * 删除标准方法验证
     * @param methodVerifyId
     * @return
     */
    boolean delMethodVerify(Integer methodVerifyId);

    /**
     * 标准方法验证确认
     * @param methodVerifyId
     * @return
     */
    boolean methodVerifyAffirm(Integer methodVerifyId);

    /**
     * 标准方法验证新增原始记录
     * @param methodVerifyId
     * @param file
     * @return
     */
    boolean uploadVerifyMethodFile(Integer methodVerifyId, MultipartFile file);

    /**
     * 标准方法更新验证原始记录列表
     * @return
     */
    List<ProcessMethodVerifyMethodFile> getVerifyMethodFileList(Integer methodVerifyId);

    /**
     * 导出标准方法更新验证
     *
     * @param methodVerifyId 标准方法验证id
     * @param response
     */
    void exportMethodVerify(Integer methodVerifyId, HttpServletResponse response);
}

