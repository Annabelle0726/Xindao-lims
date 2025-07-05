package com.ruoyi.manage.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.system.mapper.UserMapper;


import com.ruoyi.manage.mapper.ManageDocumentCancelMapper;
import com.ruoyi.manage.mapper.ManageDocumentListMapper;
import com.ruoyi.manage.mapper.ManageRecordAuditMapper;
import com.ruoyi.manage.mapper.ManageRecordCheckMapper;
import com.ruoyi.manage.pojo.ManageDocumentCancel;
import com.ruoyi.manage.pojo.ManageDocumentList;
import com.ruoyi.manage.pojo.ManageRecordAudit;
import com.ruoyi.manage.pojo.ManageRecordCheck;
import com.ruoyi.manage.service.ManageDocumentCancelService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文件作废 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-09 02:37:35
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ManageDocumentCancelServiceImpl extends ServiceImpl<ManageDocumentCancelMapper, ManageDocumentCancel> implements ManageDocumentCancelService {

    @Resource
    private ManageDocumentCancelMapper manageDocumentCancelMapper;

    @Resource
    private ManageRecordCheckMapper manageRecordCheckMapper;

    @Resource
    private ManageRecordAuditMapper manageRecordAuditMapper;



    @Resource
    private ManageDocumentListMapper manageDocumentListMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public IPage<ManageDocumentCancel> pageManageDocumentCancel(Page page, ManageDocumentCancel manageDocumentCancel) {
        return manageDocumentCancelMapper.pageManageDocumentCancel(page, QueryWrappers.queryWrappers(manageDocumentCancel));
    }

    @Override
    public int addManageDocumentCancel(ManageDocumentCancel manageDocumentCancel) {
        manageDocumentCancel.setState("待审核");
        /*新增8.4的文件修订申请审批记录*/
        ManageRecordAudit manageRecordAudit = new ManageRecordAudit();
        manageRecordAudit.setDocumentCode(manageDocumentCancel.getDocumentCode());
        manageRecordAudit.setDocumentName(manageDocumentCancel.getName());
        manageRecordAudit.setAlterThing("作废");
        manageRecordAudit.setMethod("作废");
        manageRecordAuditMapper.insert(manageRecordAudit);
        return manageDocumentCancelMapper.insert(manageDocumentCancel);
    }

    @Override
    public int checkManageDocumentCancel(Integer id, String state) {
        ManageDocumentCancel manageDocumentCancel = manageDocumentCancelMapper.selectById(id);
        if (state.equals("通过")){
            //删除文件清单对应数据
            manageDocumentListMapper.delete(Wrappers.<ManageDocumentList>lambdaQuery().eq(ManageDocumentList::getDocumentCode,manageDocumentCancel.getDocumentCode()));
        }
        manageDocumentCancel.setState(state);
        /*新增8.4的文件审批记录*/
        ManageRecordCheck manageRecordCheck = new ManageRecordCheck();
        manageRecordCheck.setDocumentCode(manageDocumentCancel.getDocumentCode());
        manageRecordCheck.setDocumentName(manageDocumentCancel.getName());
        manageRecordCheck.setDocumentVersion(manageDocumentCancel.getVersion());
        manageRecordCheck.setWriteUser(manageDocumentCancel.getCreateUser());
        manageRecordCheck.setCheckUser(manageDocumentCancel.getCheckUser());
        manageRecordCheck.setCheckState(manageDocumentCancel.getState());
        manageRecordCheck.setRemark(manageDocumentCancel.getCancelNote());
        manageRecordCheckMapper.insert(manageRecordCheck);
        return manageDocumentCancelMapper.updateById(manageDocumentCancel);
    }

    @Override
    public ManageDocumentCancel getManageDocumentCancel(Integer id) {
        ManageDocumentCancel manageDocumentCancel = manageDocumentCancelMapper.getManageDocumentCancel(id);
        String limsName = userMapper.selectUserDepartmentLimsName(manageDocumentCancel.getCreateUser());
        manageDocumentCancel.setCreateUserDepartLims(limsName);
        return manageDocumentCancel;
    }

    @Override
    public void exportManageDocumentCancel(ManageDocumentCancel manageDocumentCancel, HttpServletResponse response) throws Exception{
        List<ManageDocumentCancel> data = manageDocumentCancelMapper.pageManageDocumentCancel(new Page(-1, -1), QueryWrappers.queryWrappers(manageDocumentCancel)).getRecords();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        String fileName = URLEncoder.encode("文件作废列表导出", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        try {
            // 新建ExcelWriter
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build();
            WriteSheet mainSheet = EasyExcel.writerSheet(0, "文件作废列表导出").head(ManageDocumentCancel.class).build();
            excelWriter.write(data, mainSheet);
            // 关闭流
            excelWriter.finish();
        } catch (IOException e) {
            throw new RuntimeException("导出失败");
        }
    }

    @Override
    public int delManageDocumentCancel(Integer id) {
        ManageDocumentCancel manageDocumentCancel = manageDocumentCancelMapper.selectById(id);
        /*删除8.4的文件修订申请审批记录*/
        manageRecordAuditMapper.delete(Wrappers.<ManageRecordAudit>lambdaQuery()
        .eq(ManageRecordAudit::getDocumentCode,manageDocumentCancel.getDocumentCode())
        .eq(ManageRecordAudit::getDocumentName,manageDocumentCancel.getName())
        .eq(ManageRecordAudit::getMethod,"作废"));
        return manageDocumentCancelMapper.deleteById(id);
    }

    @Override
    public int doManageDocumentCancel(ManageDocumentCancel manageDocumentCancel) {
        return manageDocumentCancelMapper.updateById(manageDocumentCancel);
    }
}
