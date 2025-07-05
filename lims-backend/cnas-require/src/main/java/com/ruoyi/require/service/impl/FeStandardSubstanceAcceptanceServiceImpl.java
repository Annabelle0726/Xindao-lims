package com.ruoyi.require.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.require.dto.AcceptanceDto;
import com.ruoyi.require.mapper.FeStandardSubstanceAcceptanceInspectionMapper;
import com.ruoyi.require.mapper.FeStandardSubstanceAcceptanceMapper;
import com.ruoyi.require.mapper.FeStandardSubstanceMapper;
import com.ruoyi.require.pojo.FeStandardSubstance;
import com.ruoyi.require.pojo.FeStandardSubstanceAcceptance;
import com.ruoyi.require.pojo.FeStandardSubstanceAcceptanceInspection;
import com.ruoyi.require.service.FeStandardSubstanceAcceptanceInspectionService;
import com.ruoyi.require.service.FeStandardSubstanceAcceptanceService;
import com.ruoyi.require.vo.AcceptanceDetailsVo;
import com.ruoyi.require.vo.AcceptanceVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 * 标准物质验收 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-14 03:29:41
 */
@Service
public class FeStandardSubstanceAcceptanceServiceImpl extends ServiceImpl<FeStandardSubstanceAcceptanceMapper, FeStandardSubstanceAcceptance> implements FeStandardSubstanceAcceptanceService {

    @Resource
    private FeStandardSubstanceAcceptanceInspectionMapper feStandardSubstanceAcceptanceInspectionMapper;
    @Resource
    private FeStandardSubstanceAcceptanceInspectionService feStandardSubstanceAcceptanceInspectionService;
    @Resource
    private FeStandardSubstanceMapper feStandardSubstanceMapper;



    @Override
    public void addAcceptance(AcceptanceDto dto) {
        FeStandardSubstanceAcceptance acceptance = dto.getAcceptance();
        this.baseMapper.insert(acceptance);
        List<FeStandardSubstanceAcceptanceInspection> list = dto.getList();
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(v -> {
                v.setAcceptanceId(acceptance.getId());
            });
            feStandardSubstanceAcceptanceInspectionService.saveBatch(list);
        }
    }

    @Override
    public IPage<AcceptanceVo> getPageAcceptance(Page page, String name) {
        return this.baseMapper.getPageAcceptance(page,name);
    }

    @Override
    public AcceptanceDetailsVo getAcceptanceDetails(Integer id) {
        FeStandardSubstanceAcceptance acceptance = this.baseMapper.selectById(id);
        FeStandardSubstance substance = feStandardSubstanceMapper.selectById(acceptance.getSubstanceId());
        QueryWrapper<FeStandardSubstanceAcceptanceInspection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("acceptance_id",id);
        List<FeStandardSubstanceAcceptanceInspection> list = feStandardSubstanceAcceptanceInspectionMapper.selectList(queryWrapper);
        AcceptanceDetailsVo vo = new AcceptanceDetailsVo();
        vo.setSubstance(substance);
        vo.setAcceptance(acceptance);
        vo.setList(list);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteAcceptance(Integer id) {
        feStandardSubstanceAcceptanceInspectionMapper.delete(new QueryWrapper<FeStandardSubstanceAcceptanceInspection>()
                .lambda().eq(FeStandardSubstanceAcceptanceInspection::getAcceptanceId,id));
        return this.baseMapper.deleteById(id);
    }

    @Override
    public void exportFeStandardSubstanceAcceptance(HttpServletResponse response) {
        List<FeStandardSubstanceAcceptance> list = this.list();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        try {
            String fileName = URLEncoder.encode("标准物质验收", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            // 新建ExcelWriter
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build();
            WriteSheet writeSheet = EasyExcel.writerSheet(0, "标准物质验收").head(FeStandardSubstanceAcceptance.class).build();
            excelWriter.write(list, writeSheet);
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
