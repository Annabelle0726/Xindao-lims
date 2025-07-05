package com.ruoyi.require.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.require.dto.SupplierManagementDto;
import com.ruoyi.require.mapper.SupplierManagementMapper;
import com.ruoyi.require.pojo.SupplierManagement;
import com.ruoyi.require.service.SupplierManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-15 02:46:45
 */
@Service
public class SupplierManagementServiceImpl extends ServiceImpl<SupplierManagementMapper, SupplierManagement> implements SupplierManagementService {

    @Autowired
    private SupplierManagementMapper supplierManagementMapper;

    public SupplierManagementServiceImpl(SupplierManagementMapper supplierManagementMapper) {
        this.supplierManagementMapper = supplierManagementMapper;
    }

    @Override
    public List<SupplierManagement> selectSupplierManagement(SupplierManagement supplierManagement) {
        return this.list();
    }

    @Override
    public void exportSupplierManagement(Integer parentId, HttpServletResponse response) {
        List<SupplierManagement> data = supplierManagementMapper.selectSupplierManagementAll(parentId);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        try {
            String fileName = URLEncoder.encode("供应商管理", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            // 新建ExcelWriter
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build();
            WriteSheet writeSheet = EasyExcel.writerSheet(0, "供应商管理").head(SupplierManagementDto.class).build();
            excelWriter.write(data, writeSheet);
            // 关闭流
            excelWriter.finish();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("导出失败");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("导出失败");
        }
    }

    @Override
    public IPage<SupplierManagement> selectQualifiedSupplierManagement(Page page, SupplierManagement supplierManagement) {
        return baseMapper.selectQualifiedSupplierManagement(page, QueryWrappers.queryWrappers(supplierManagement));
    }

    @Override
    public List<SupplierManagement> selectQualifiedSupplierManagementById(Integer supplierManagementId) {
        return this.list(new QueryWrapper<SupplierManagement>().lambda().eq(SupplierManagement::getSupplierManagementId , supplierManagementId));
    }

    @Override
    public List<SupplierManagement> selectSupplierManagementByParentId(Integer parentId) {
        return supplierManagementMapper.selectSupplierManagement(parentId);
    }

    @Override
    public List<SupplierManagement> selectSupplierManagementAll() {
        return supplierManagementMapper.selectList(null);
    }
}
