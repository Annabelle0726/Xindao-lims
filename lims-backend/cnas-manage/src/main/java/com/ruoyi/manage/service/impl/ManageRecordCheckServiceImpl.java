package com.ruoyi.manage.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;


import com.deepoove.poi.data.*;
import com.deepoove.poi.data.style.*;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.manage.mapper.ManageRecordCheckMapper;

import com.ruoyi.manage.pojo.ManageRecordCheck;

import com.ruoyi.manage.service.ManageRecordCheckService;

import com.ruoyi.system.mapper.UserMapper;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文件审批记录(含修订后再次审批记录) 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-12 02:31:36
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ManageRecordCheckServiceImpl extends ServiceImpl<ManageRecordCheckMapper, ManageRecordCheck> implements ManageRecordCheckService {

    @Resource
    private ManageRecordCheckMapper manageRecordCheckMapper;

    @Value("${wordUrl}")
    private String wordUrl;

    @Resource
    private UserMapper userMapper;



    @Override
    public IPage<ManageRecordCheck> pageManageRecordCheck(Page page, ManageRecordCheck manageRecordCheck) {
        return manageRecordCheckMapper.pageManageRecordCheck(page, QueryWrappers.queryWrappers(manageRecordCheck));
    }

    @Override
    public int checkManageRecordCheck(Integer id, String checkState) {
        Integer userId = SecurityUtils.getUserId().intValue();
        ManageRecordCheck manageRecordCheck = manageRecordCheckMapper.selectById(id);
        manageRecordCheck.setCheckState(checkState);
        manageRecordCheck.setCheckUser(userId);
        return manageRecordCheckMapper.updateById(manageRecordCheck);
    }

    @Override
    public int ratifyManageRecordCheck(Integer id, String ratifyState) {
        Integer userId = SecurityUtils.getUserId().intValue();
        ManageRecordCheck manageRecordCheck = manageRecordCheckMapper.selectById(id);
        manageRecordCheck.setRatifyState(ratifyState);
        manageRecordCheck.setRatifyUser(userId);
        return manageRecordCheckMapper.updateById(manageRecordCheck);
    }

    @Override
    public String exportOutManageRecordCheck(ManageRecordCheck manageRecordCheck, HttpServletResponse response) {
        List<ManageRecordCheck> manageRecordCheckList = manageRecordCheckMapper.pageManageRecordCheck(new Page(-1, -1), QueryWrappers.queryWrappers(manageRecordCheck)).getRecords();
        //生成检验报告发放登记表
        String url;
        try {
            InputStream inputStream = this.getClass().getResourceAsStream("/static/check-deal.docx");
            File file = File.createTempFile("temp", ".tmp");
            OutputStream outputStream = new FileOutputStream(file);
            IOUtils.copy(inputStream, outputStream);
            url = file.getAbsolutePath();
            inputStream.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            throw new ErrorException("找不到模板文件");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ConfigureBuilder builder = Configure.builder();
        builder.useSpringEL(true);

        List<Map<String, Object>> checkList = new ArrayList<>();
        Integer index = 1;
        Integer index1 = 1;
        for (int c = 0; c < manageRecordCheckList.size(); c++) {
            //超过20行换页
            if (c % 20 == 0) {
                List<RowRenderData> rows = new ArrayList<>();
                //表格的行数
                for (int i = 0; i < 21; i++) {
                    RowRenderData rowRenderData = new RowRenderData();
                    RowStyle rowStyle = new RowStyle();
                    rowStyle.setHeight(40);
                    rowRenderData.setRowStyle(rowStyle);
                    List<CellRenderData> cells = new ArrayList<>();
                    //表格的列数
                    for (int j = 0; j < 9; j++) {
                        CellRenderData cellRenderData = new CellRenderData();
                        CellStyle cellStyle = new CellStyle();
                        cellStyle.setVertAlign(XWPFTableCell.XWPFVertAlign.CENTER);
                        cellRenderData.setCellStyle(cellStyle);
                        List<ParagraphRenderData> paragraphRenderDataList = new ArrayList<>();
                        ParagraphRenderData paragraphRenderData = new ParagraphRenderData();
                        ParagraphStyle paragraphStyle = new ParagraphStyle();
                        paragraphStyle.setAlign(ParagraphAlignment.CENTER);
                        paragraphRenderData.setParagraphStyle(paragraphStyle);
                        List<RenderData> renderData = new ArrayList<>();
                        TextRenderData textRenderData = new TextRenderData();
                        Style style = new Style();
                        style.setFontFamily("宋体");
                        style.setColor("000000");
                        textRenderData.setStyle(style);
                        if (i == 0) {
                            //第一行
                            if (j == 0) {
                                //第一列序号
                                textRenderData.setText("序号@No.");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 1) {
                                //第二列
                                textRenderData.setText("文件名称@File name");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 2) {
                                //第三列
                                textRenderData.setText("文件编号@File number");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 3) {
                                //第四列
                                textRenderData.setText("版/次@Edition/time");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 4) {
                                //第五列
                                textRenderData.setText("编制@Editor");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 5) {
                                //第六列
                                textRenderData.setText("审核@Audit");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 6) {
                                //第七列
                                textRenderData.setText("批准@Approve");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 7) {
                                //第八列
                                textRenderData.setText("批准日期@Date of approve");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else {
                                //第九列
                                textRenderData.setText("备注@Remark");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            }
                        } else {
                            //其他行
                            if (j == 0) {
                                //第一列
                                try {
                                    String insReportCode = manageRecordCheckList.get((i - 1) + (index1 - 1) * 20).getDocumentName();
                                    textRenderData.setText(index + "");
                                } catch (Exception e) {
                                    textRenderData.setText("");
                                }
                                index++;
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 1) {
                                try {
                                    textRenderData.setText(manageRecordCheckList.get((i - 1) + (index1 - 1) * 20).getDocumentName());
                                } catch (Exception e) {
                                    textRenderData.setText("");
                                }
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 2) {
                                try {
                                    textRenderData.setText(manageRecordCheckList.get((i - 1) + (index1 - 1) * 20).getDocumentCode());
                                } catch (Exception e) {
                                    textRenderData.setText("");
                                }
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 3) {
                                try {
                                    textRenderData.setText(manageRecordCheckList.get((i - 1) + (index1 - 1) * 20).getDocumentVersion());
                                } catch (Exception e) {
                                    textRenderData.setText("");
                                }
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 4) {
                                try {
                                    textRenderData.setText(manageRecordCheckList.get((i - 1) + (index1 - 1) * 20).getWriteUserName());
                                } catch (Exception e) {
                                    textRenderData.setText("");
                                }
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 5) {
                                try {
                                    textRenderData.setText(manageRecordCheckList.get((i - 1) + (index1 - 1) * 20).getCheckUserName());
                                } catch (Exception e) {
                                    textRenderData.setText("");
                                }
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 6) {
                                try {
                                    textRenderData.setText(manageRecordCheckList.get((i - 1) + (index1 - 1) * 20).getRatifyUserName());
                                } catch (Exception e) {
                                    textRenderData.setText("");
                                }
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 7) {
                                try {
                                    textRenderData.setText(manageRecordCheckList.get((i - 1) + (index1 - 1) * 20).getRatifyDate() + "");
                                } catch (Exception e) {
                                    textRenderData.setText("");
                                }
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else {
                                try {
                                    textRenderData.setText(manageRecordCheckList.get((i - 1) + (index1 - 1) * 20).getRemark());
                                } catch (Exception e) {
                                    textRenderData.setText("");
                                }
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            }
                        }
                    }
                    rowRenderData.setCells(cells);
                    if (rowRenderData.getCells().size() != 0) {
                        rows.add(rowRenderData);
                    }
                }
                TableRenderData tableRenderData = new TableRenderData();
                tableRenderData.setRows(rows);
                int countSize = tableRenderData.getRows().get(0).getCells().size();
                for (RowRenderData row : tableRenderData.getRows()) {
                    if (row.getCells().size() != countSize) {
                        throw new ErrorException("每行单元格不相等");
                    }
                }
                TableStyle tableStyle = new TableStyle();
                tableStyle.setWidth(XWPFTable.DEFAULT_PERCENTAGE_WIDTH);
                tableStyle.setAlign(TableRowAlign.CENTER);
                BorderStyle borderStyle = new BorderStyle();
                borderStyle.setColor("000000");
                borderStyle.setType(XWPFTable.XWPFBorderType.THICK);
                borderStyle.setSize(14);
                tableStyle.setLeftBorder(borderStyle);
                tableStyle.setTopBorder(borderStyle);
                tableStyle.setRightBorder(borderStyle);
                tableStyle.setBottomBorder(borderStyle);
                tableRenderData.setTableStyle(tableStyle);
                Map<String, Object> table = new HashMap<>();
                table.put("check", tableRenderData);
                table.put("index1", index1);
                checkList.add(table);
                index1++;
            }
        }
        Integer finalIndex = index1;
        XWPFTemplate template = XWPFTemplate.compile(url, builder.build()).render(
                new HashMap<String, Object>() {{
                    put("size", finalIndex);
                    put("checkList", checkList);
                }});
        String name = UUID.randomUUID() + "_文件审批记录" + ".docx";
        try {
            template.writeAndClose(Files.newOutputStream(Paths.get(wordUrl + "/" + name)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //处理中英文换行的问题
        String path = wordUrl + "/" + name;
        try {
            FileInputStream stream1 = new FileInputStream(path);
            XWPFDocument document1 = new XWPFDocument(stream1);
            List<XWPFTable> xwpfTables1 = document1.getTables();
            for (int i = 0; i < xwpfTables1.size(); i++) {
                for (int j = 0; j < xwpfTables1.get(i).getRows().size(); j++) {
                    for (int k = 0; k < xwpfTables1.get(i).getRows().get(j).getTableCells().size(); k++) {
                        if (xwpfTables1.get(i).getRows().get(j).getTableCells().get(k).getText().contains("@")) {
                            String text = xwpfTables1.get(i).getRows().get(j).getTableCells().get(k).getText();
                            String[] split = text.split("@");
                            xwpfTables1.get(i).getRows().get(j).getTableCells().get(k).removeParagraph(0);
                            XWPFParagraph xwpfParagraph = xwpfTables1.get(i).getRows().get(j).getTableCells().get(k).addParagraph();
                            XWPFRun run = xwpfParagraph.createRun();
                            run.setText(split[0]);
                            if (ObjectUtils.isNotNull(split[1])) {
                                run.addBreak();
                                run.setText(split[1]);
                            }
                            xwpfParagraph.setAlignment(ParagraphAlignment.CENTER);
                        }
                    }
                }
            }
            FileOutputStream fileOutputStream1 = new FileOutputStream(path);
            document1.write(fileOutputStream1);
            fileOutputStream1.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return name;
    }

    @Override
    public int exportInManageRecordCheck(MultipartFile file) {
        List<ManageRecordCheck> manageRecordCheckList = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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
                    if (row.getTableCells().size() != 9) {
                        continue;
                    }
                    if (ObjectUtils.isNotEmpty(row.getCell(1).getText())) {
                        ManageRecordCheck manageRecordCheck = new ManageRecordCheck();
                        manageRecordCheck.setDocumentName(row.getCell(1).getText());
                        manageRecordCheck.setDocumentCode(row.getCell(2).getText());
                        manageRecordCheck.setDocumentVersion(row.getCell(3).getText());
                        try {
                            manageRecordCheck.setWriteUser(userMapper.selectOne(Wrappers.<User>lambdaQuery()
                                    .eq(User::getName, row.getCell(4).getText())).getId());
                        } catch (Exception e) {
                            manageRecordCheck.setWriteUser(null);
                        }
                        try {
                            manageRecordCheck.setCheckUser(userMapper.selectOne(Wrappers.<User>lambdaQuery()
                                    .eq(User::getName, row.getCell(5).getText())).getId());
                        } catch (Exception e) {
                            manageRecordCheck.setCheckUser(null);
                        }
                        try {
                            manageRecordCheck.setRatifyUser(userMapper.selectOne(Wrappers.<User>lambdaQuery()
                                    .eq(User::getName, row.getCell(6).getText())).getId());
                        } catch (Exception e) {
                            manageRecordCheck.setRatifyUser(null);
                        }
                        try {
                            manageRecordCheck.setRatifyDate(LocalDate.parse(row.getCell(7).getText(), dateTimeFormatter));
                        } catch (Exception e) {
                            manageRecordCheck.setRatifyDate(null);
                        }

                        manageRecordCheck.setRemark(row.getCell(8).getText());
                        ManageRecordCheck check = manageRecordCheckMapper.selectOne(Wrappers.<ManageRecordCheck>lambdaQuery()
                                .eq(ManageRecordCheck::getDocumentCode, manageRecordCheck.getDocumentCode())
                                .eq(ManageRecordCheck::getDocumentName, manageRecordCheck.getDocumentName())
                                .eq(ManageRecordCheck::getDocumentVersion, manageRecordCheck.getDocumentVersion()));
                        if (ObjectUtils.isNotEmpty(check)) {
                            manageRecordCheck.setId(check.getId());
                        }
                        manageRecordCheckList.add(manageRecordCheck);
                    }
                }
            }
            saveOrUpdateBatch(manageRecordCheckList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
