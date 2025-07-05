package com.ruoyi.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.manage.pojo.InternalCorrect;
import com.ruoyi.manage.pojo.InternalCorrectFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 内审管理纠正处理表 服务类
 * </p>
 *
 * @author 
 * @since 2024-11-13 04:00:15
 */
public interface InternalCorrectService extends IService<InternalCorrect> {

    /**
     * 新增内审管理纠正处理信息
     * @param InternalAccording
     * @return
     */
    boolean addInternalCorrect(InternalCorrect InternalAccording);

    /**
     * 查询内审管理纠正处理
     * @param correctId
     * @return
     */
    InternalCorrect getInternalCorrect(Integer correctId);

    /**
     * 查询内审管理纠正措施列表
     * @param page
     * @param detailsCorrect
     * @return
     */
    IPage<InternalCorrect> pageInternalCorrect(Page page, InternalCorrect detailsCorrect);

    /**
     * 新增内审管理纠正措施附件
     * @param correctId
     * @param file
     * @return
     */
    boolean uploadInternalCorrectFile(Integer correctId, MultipartFile file);

    /**
     * 查询内审管理纠正措施附件
     * @param correctId
     * @return
     */
    List<InternalCorrectFile> getInternalCorrectFileList(Integer correctId);

    /**
     * 导出纠正措施
     * @param correctId
     * @param response
     */
    void exportInternalCorrect(Integer correctId, HttpServletResponse response);
}
