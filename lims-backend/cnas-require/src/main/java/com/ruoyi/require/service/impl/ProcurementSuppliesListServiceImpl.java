package com.ruoyi.require.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.inspect.dto.ProcurementSuppliesListEDto;
import com.ruoyi.require.dto.ProcurementSuppliesListDto;
import com.ruoyi.require.mapper.ProcurementSuppliesListMapper;
import com.ruoyi.require.mapper.SupplierManagementMapper;
import com.ruoyi.require.pojo.ProcurementSuppliesList;
import com.ruoyi.require.pojo.SupplierManagement;
import com.ruoyi.require.service.ProcurementSuppliesListService;
import com.ruoyi.system.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务与供应商 耗材列表 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-15 04:04:32
 */
@Service
public class ProcurementSuppliesListServiceImpl extends ServiceImpl<ProcurementSuppliesListMapper, ProcurementSuppliesList> implements ProcurementSuppliesListService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SupplierManagementMapper supplierManagementMapper;

    @Autowired
    private ProcurementSuppliesListMapper procurementSuppliesListMapper;

    @Override
    public IPage<ProcurementSuppliesListDto> selectList(Page page, ProcurementSuppliesListDto list) {
        IPage<ProcurementSuppliesList> iPage = baseMapper.selectProcurementSuppliesList(page, list);
        IPage<ProcurementSuppliesListDto> result = new Page<>();
        List<ProcurementSuppliesListDto> dtos = new ArrayList<>();
        BeanUtils.copyProperties(iPage, result);
        for (int i = 0; i < iPage.getRecords().size(); i++) {
            ProcurementSuppliesList record = iPage.getRecords().get(i);
            ProcurementSuppliesListDto dto = new ProcurementSuppliesListDto();
            BeanUtils.copyProperties(record, dto);
            if (record.getPersonInCharge() != 0) {
                User user = userMapper.selectById(record.getPersonInCharge());
                dto.setPersonInChargeName(user.getName());
            }
            if (record.getUpdateUser() != 0) {
                User updateUser = userMapper.selectById(record.getUpdateUser());
                dto.setUpdateUserName(updateUser.getName());
            }

            if (record.getSupplier() != 0) {
                SupplierManagement supplierManagement = supplierManagementMapper.selectById(record.getSupplier());
                dto.setSupplierName(supplierManagement.getSupplierName());
            }

            dtos.add(dto);
        }
        result.setRecords(dtos);

        return result;
    }

    @Override
    public Integer addProcurementSuppliesList(ProcurementSuppliesListDto dto) {
        ProcurementSuppliesList list = new ProcurementSuppliesList();
        BeanUtils.copyProperties(dto, list);
        return baseMapper.insert(list);
    }

    @Override
    public Integer updateProcurementSuppliesList(ProcurementSuppliesListDto dto) {
        ProcurementSuppliesList list = new ProcurementSuppliesList();
        BeanUtils.copyProperties(dto, list);
        return baseMapper.updateById(list);
    }

    @Override
    public void exportProcurementSuppliesList(Integer contentsId,HttpServletResponse response) {
//        List<ProcurementSuppliesList> data = this.list(new QueryWrapper<ProcurementSuppliesList>().lambda()
//                .select(ProcurementSuppliesList::getId
//                        , ProcurementSuppliesList::getUnit
//                        , ProcurementSuppliesList::getLowerLimit
//                        , ProcurementSuppliesList::getConsumablesType
//                        , ProcurementSuppliesList::getItemNumber
//                        , ProcurementSuppliesList::getConsumablesName
//                        , ProcurementSuppliesList::getSpecifications
//                        , ProcurementSuppliesList::getCurrentAmount
//                        , ProcurementSuppliesList::getRemark).eq(ProcurementSuppliesList::getContentsId, contentsId));
        List<ProcurementSuppliesList> data = procurementSuppliesListMapper.selectProcurementSuppliesListByContentsId(contentsId);
        if (data.size()==0) {
            throw new RuntimeException("无数据");
        }
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        try {
            String fileName = URLEncoder.encode("耗材列表", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            // 新建ExcelWriter
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build();
            WriteSheet writeSheet = EasyExcel.writerSheet(0, "耗材列表").head(ProcurementSuppliesListEDto.class).build();
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
}
