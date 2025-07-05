package com.ruoyi.manage.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.manage.mapper.ManageRecordTotalMapper;
import com.ruoyi.manage.mapper.ManageRecordVerifyMapper;
import com.ruoyi.manage.pojo.ManageRecordTotal;
import com.ruoyi.manage.pojo.ManageRecordVerify;
import com.ruoyi.manage.service.ManageRecordVerifyService;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 外来文件确认记录 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-12 10:29:44
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ManageRecordVerifyServiceImpl extends ServiceImpl<ManageRecordVerifyMapper, ManageRecordVerify> implements ManageRecordVerifyService {

    @Resource
    private ManageRecordVerifyMapper manageRecordVerifyMapper;

    @Resource
    private ManageRecordTotalMapper manageRecordTotalMapper;

    @Override
    public IPage<ManageRecordVerify> pageManageRecordVerify(Page page, ManageRecordVerify manageRecordVerify) {
        if (ObjectUtils.isEmpty(manageRecordVerify.getManageRecordTotalId())) {
            //获取当前年份
            LocalDate currentDate = LocalDate.now();
            // 定义日期格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
            // 格式化当前日期
            String currentMonth = currentDate.format(formatter);
            //查询历史
            ManageRecordTotal manageRecordTotal = manageRecordTotalMapper.selectOne(Wrappers.<ManageRecordTotal>lambdaQuery().eq(ManageRecordTotal::getYear, currentMonth));
            manageRecordVerify.setManageRecordTotalId(manageRecordTotal.getId());
        }
        return manageRecordVerifyMapper.pageManageRecordVerify(page, QueryWrappers.queryWrappers(manageRecordVerify));
    }

    @Override
    public int addManageRecordVerify(ManageRecordVerify manageRecordVerify) {
        //获取当前年份
        LocalDate currentDate = LocalDate.now();
        // 定义日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        // 格式化当前日期
        String currentMonth = currentDate.format(formatter);
        ManageRecordTotal manageRecordTotal = manageRecordTotalMapper.selectOne(Wrappers.<ManageRecordTotal>lambdaQuery().eq(ManageRecordTotal::getYear, currentMonth));
        manageRecordVerify.setManageRecordTotalId(manageRecordTotal.getId());
        manageRecordVerifyMapper.insert(manageRecordVerify);
        manageRecordTotal.setTotalNum(1 + manageRecordTotal.getTotalNum());
        return manageRecordTotalMapper.updateById(manageRecordTotal);
    }

    @Override
    public int delManageRecordVerify(Integer id) {
        ManageRecordVerify manageRecordVerify = manageRecordVerifyMapper.selectById(id);
        manageRecordVerifyMapper.deleteById(id);
        ManageRecordTotal manageRecordTotal = manageRecordTotalMapper.selectById(manageRecordVerify.getManageRecordTotalId());
        manageRecordTotal.setTotalNum(manageRecordTotal.getTotalNum() - 1);
        return manageRecordTotalMapper.updateById(manageRecordTotal);
    }

    @Override
    public int exportManageRecordVerify(MultipartFile file) {
        List<ManageRecordVerify> manageRecordVerifyList = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //获取当前年份
        LocalDate currentDate = LocalDate.now();
        // 定义日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        // 格式化当前日期
        String currentMonth = currentDate.format(formatter);
        ManageRecordTotal manageRecordTotal = manageRecordTotalMapper.selectOne(Wrappers.<ManageRecordTotal>lambdaQuery().eq(ManageRecordTotal::getYear, currentMonth));
        try {
            InputStream inputStream = file.getInputStream();
            XWPFDocument document = new XWPFDocument(inputStream);
            List<XWPFTable> tables = document.getTables();
            if (tables.isEmpty()) {
                throw new ErrorException("文档中没有表格");
            }

            for (XWPFTable table : tables) {
                List<XWPFTableRow> rows = table.getRows();
                if (rows.size() <= 1) {
                    throw new ErrorException("表格没有数据行");
                }
                for (int i = 1; i < rows.size(); i++) { // 从第二行开始，跳过表头
                    XWPFTableRow row = rows.get(i);
                    if (row.getTableCells().size() != 8) {
                        continue;
                    }
                    if (ObjectUtils.isNotEmpty(row.getCell(1).getText())) {
                        ManageRecordVerify manageRecordVerify = new ManageRecordVerify();
                        manageRecordVerify.setDocumentName(row.getCell(1).getText());
                        manageRecordVerify.setDocumentCode(row.getCell(2).getText());
                        manageRecordVerify.setStandardName(row.getCell(3).getText());
                        manageRecordVerify.setStandardCode(row.getCell(4).getText());
                        try {
                            manageRecordVerify.setEffectiveDate(LocalDate.parse(row.getCell(5).getText(), dateTimeFormatter));
                        } catch (Exception e) {
                            manageRecordVerify.setEffectiveDate(null);
                        }
                        try {
                            manageRecordVerify.setCancelDate(LocalDate.parse(row.getCell(6).getText(), dateTimeFormatter));
                        } catch (Exception e) {
                            manageRecordVerify.setCancelDate(null);
                        }
                        manageRecordVerify.setNote(row.getCell(7).getText());
                        manageRecordVerify.setManageRecordTotalId(manageRecordTotal.getId());
                        if (manageRecordVerifyMapper.selectCount(Wrappers.<ManageRecordVerify>lambdaQuery()
                                .eq(ManageRecordVerify::getDocumentCode, manageRecordVerify.getDocumentCode())
                                .eq(ManageRecordVerify::getDocumentName, manageRecordVerify.getDocumentName())
                                .eq(ManageRecordVerify::getStandardName, manageRecordVerify.getStandardName())
                                .eq(ManageRecordVerify::getStandardCode, manageRecordVerify.getStandardCode())
                                .eq(ManageRecordVerify::getManageRecordTotalId, manageRecordVerify.getManageRecordTotalId())) <= 0) {
                            manageRecordVerifyList.add(manageRecordVerify);
                        }
                    }
                }
            }
            saveBatch(manageRecordVerifyList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
