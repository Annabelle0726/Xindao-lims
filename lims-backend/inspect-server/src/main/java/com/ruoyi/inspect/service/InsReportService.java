package com.ruoyi.inspect.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.basic.pojo.IfsInventoryQuantity;
import com.ruoyi.inspect.dto.ReportPageDto;
import com.ruoyi.inspect.pojo.InsOrder;
import com.ruoyi.inspect.pojo.InsReport;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【ins_report(检验报告)】的数据库操作Service
* @createDate 2024-03-17 22:10:02
*/
public interface InsReportService extends IService<InsReport> {

    Map<String,Object> pageInsReport(Page page, ReportPageDto reportPageDto);

    void wordToPdf(String path,String sealUrl, boolean isRawMater);

    int inReport(String url, Integer id);

    int upReportUrl(Integer id);

    //提交
    int writeReport(Integer id, Integer userId, Integer submitUserId);

    //审核
    int examineReport(Integer id, Integer isExamine, String examineTell, Integer userId);

    //批准
    int ratifyReport(Integer id, Integer isRatify, String ratifyTell);

    int wordInsertUrl(Map<String, Object> map, String url);

    String downAll(String ids);

    int upAll(MultipartFile file) throws IOException;

    void isRawMaterial(InsOrder insOrder);

    Long getUnqualifiedCount(InsOrder insOrder);

    /**
     * 移库操作
     * @param one
     * @return
     */
    String moveRawMaterial(IfsInventoryQuantity one);

    /**
     * 退回到检验任务
     * @param id
     * @return
     */
    boolean sendBackTask(Integer id);

    /**
     * 报告导出
     * @param dto
     * @param response
     */
    void reportAllExport(ReportPageDto dto, HttpServletResponse response) throws UnsupportedEncodingException;
}
