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
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.manage.mapper.ManageRecordIssueRecycleMapper;

import com.ruoyi.manage.pojo.ManageRecordIssueRecycle;

import com.ruoyi.manage.service.ManageRecordIssueRecycleService;

import com.ruoyi.system.mapper.UserMapper;
import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
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
import java.util.*;

/**
 * <p>
 * 所有文件(内、外部文件)的发放与回收记录 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-13 09:11:05
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ManageRecordIssueRecycleServiceImpl extends ServiceImpl<ManageRecordIssueRecycleMapper, ManageRecordIssueRecycle> implements ManageRecordIssueRecycleService {

    @Resource
    private ManageRecordIssueRecycleMapper manageRecordIssueRecycleMapper;

    @Value("${wordUrl}")
    private String wordUrl;

    @Resource
    private UserMapper userMapper;

    @Override
    public IPage<ManageRecordIssueRecycle> pageManageRecordIssueRecycle(Page page, ManageRecordIssueRecycle manageRecordIssueRecycle) {
        return manageRecordIssueRecycleMapper.pageManageRecordIssueRecycle(page, QueryWrappers.queryWrappers(manageRecordIssueRecycle));
    }

    @Override
    public String exportOutManageRecordIssueRecycle(ManageRecordIssueRecycle manageRecordIssueRecycle, HttpServletResponse response) {
        List<ManageRecordIssueRecycle> manageRecordIssueRecycleList = manageRecordIssueRecycleMapper.pageManageRecordIssueRecycle(new Page(-1, -1), QueryWrappers.queryWrappers(manageRecordIssueRecycle)).getRecords();
        //生成检验报告发放登记表
        String url;
        try {
            InputStream inputStream = this.getClass().getResourceAsStream("/static/recycle-deal.docx");
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

        List<Map<String, Object>> recycleList = new ArrayList<>();
        Integer index = 1;
        Integer index1 = 1;
        for (int c = 0; c < manageRecordIssueRecycleList.size(); c++) {
            //超过15行换页
            if (c % 15 == 0) {
                List<RowRenderData> rows = new ArrayList<>();
                //表格的行数
                for (int i = 0; i < 17; i++) {
                    RowRenderData rowRenderData = new RowRenderData();
                    RowStyle rowStyle = new RowStyle();
                    rowStyle.setHeight(40);
                    rowRenderData.setRowStyle(rowStyle);
                    List<CellRenderData> cells = new ArrayList<>();
                    //表格的列数
                    for (int j = 0; j < 12; j++) {
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
                                textRenderData.setText("序号@No.∑1");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 1) {
                                //第二列
                                textRenderData.setText("文件编号@File number∑2");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 2) {
                                //第三列
                                textRenderData.setText("文件名称@File name∑3");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 3) {
                                //第四列
                                textRenderData.setText("版号@Edition number∑4");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 4) {
                                //第五列
                                textRenderData.setText("份数@Number of copies∑5");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 5) {
                                //第六列
                                textRenderData.setText("文件类别@File category∑6");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 6) {
                                //第七列
                                textRenderData.setText("分发号@Distribution number∑7");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 7) {
                                //第八列
                                textRenderData.setText("接收部门@Receiving department∑8");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 8 || j == 9) {
                                //第九列
                                textRenderData.setText("发放 Issue∑9");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 10 || j == 11) {
                                //第八列
                                textRenderData.setText("回收 Reclaim∑10");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            }
                        }
                        else if (i == 1) {
                            //第一行
                            if (j == 0) {
                                //第一列序号
                                textRenderData.setText("序号@No.∑1");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 1) {
                                //第二列
                                textRenderData.setText("文件编号@File number∑2");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 2) {
                                //第三列
                                textRenderData.setText("文件名称@File name∑3");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 3) {
                                //第四列
                                textRenderData.setText("版号@Edition number∑4");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 4) {
                                //第五列
                                textRenderData.setText("份数@Number of copies∑5");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 5) {
                                //第六列
                                textRenderData.setText("文件类别@File category∑6");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 6) {
                                //第七列
                                textRenderData.setText("分发号@Distribution number∑7");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 7) {
                                //第八列
                                textRenderData.setText("接收部门@Receiving department∑8");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 8) {
                                //第九列
                                textRenderData.setText("接受人@Recipients");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 9) {
                                //第十列
                                textRenderData.setText("日期@Date");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 10) {
                                //第十一列
                                textRenderData.setText("签收人@Signatory");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else {
                                //第十二列
                                textRenderData.setText("日期@Date");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            }
                        }
                        else {
                            //其他行
                            if (j == 0) {
                                //第一列
                                try {
                                    String insReportCode = manageRecordIssueRecycleList.get((i - 2) + (index1 - 1) * 15).getDocumentCode();
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
                                    textRenderData.setText(manageRecordIssueRecycleList.get((i - 2) + (index1 - 1) * 15).getDocumentCode());
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
                                    textRenderData.setText(manageRecordIssueRecycleList.get((i - 2) + (index1 - 1) * 15).getDocumentName());
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
                                    textRenderData.setText(manageRecordIssueRecycleList.get((i - 2) + (index1 - 1) * 15).getDocumentVersion());
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
                                    textRenderData.setText(manageRecordIssueRecycleList.get((i - 2) + (index1 - 1) * 15).getPages());
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
                                    textRenderData.setText(manageRecordIssueRecycleList.get((i - 2) + (index1 - 1) * 15).getDocumentType());
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
                                    textRenderData.setText(manageRecordIssueRecycleList.get((i - 2) + (index1 - 1) * 15).getNumber());
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
                                    textRenderData.setText(manageRecordIssueRecycleList.get((i - 2) + (index1 - 1) * 15).getDepartLims());
                                } catch (Exception e) {
                                    textRenderData.setText("");
                                }
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 8) {
                                try {
                                    textRenderData.setText(manageRecordIssueRecycleList.get((i - 2) + (index1 - 1) * 15).getReceiveUserName());
                                } catch (Exception e) {
                                    textRenderData.setText("");
                                }
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 9) {
                                try {
                                    textRenderData.setText(manageRecordIssueRecycleList.get((i - 2) + (index1 - 1) * 15).getReceiveDate() + "");
                                } catch (Exception e) {
                                    textRenderData.setText("");
                                }
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 10) {
                                try {
                                    textRenderData.setText(manageRecordIssueRecycleList.get((i - 2) + (index1 - 1) * 15).getSignedUserName());
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
                                    textRenderData.setText(manageRecordIssueRecycleList.get((i - 2) + (index1 - 1) * 15).getSignedDate() + "");
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
                table.put("recycle", tableRenderData);
                table.put("index1", index1);
                recycleList.add(table);
                index1++;
            }
        }
        Integer finalIndex = index1;
        XWPFTemplate template = XWPFTemplate.compile(url, builder.build()).render(
                new HashMap<String, Object>() {{
                    put("size", finalIndex);
                    put("recycleList", recycleList);
                }});
        String name = UUID.randomUUID() + "_所有文件的发放与回收记录" + ".docx";
        try {
            template.writeAndClose(Files.newOutputStream(Paths.get(wordUrl + "/" + name)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String path = wordUrl + "/" + name;
        // 处理合并单元格的问题
        try {
            ZipSecureFile.setMinInflateRatio(0.0001);
            FileInputStream stream = new FileInputStream(path);
            XWPFDocument document = new XWPFDocument(stream);
            List<XWPFTable> xwpfTables = document.getTables();
            for (int i = 0; i < xwpfTables.size(); i++) {
                Set<String> set1 = new HashSet<>();
                Map<String, Map<String, Integer>> maps = new HashMap<>();
                for (int j = 0; j < xwpfTables.get(i).getRows().size(); j++) {
                    for (int k = 0; k < xwpfTables.get(i).getRows().get(j).getTableCells().size(); k++) {
                        if (xwpfTables.get(i).getRows().get(j).getTableCells().get(k).getText().indexOf("∑") > -1) {
                            String[] split = xwpfTables.get(i).getRows().get(j).getTableCells().get(k).getText().split("∑");
                            if (set1.add(split[1])) {
                                Map<String, Integer> map = new HashMap<>();
                                map.put("sr", j);
                                map.put("sc", k);
                                map.put("er", j + 0);
                                map.put("ec", k + 0);
                                maps.put(split[1], map);
                            } else {
                                Map<String, Integer> map1 = maps.get(split[1]);
                                if (j == map1.get("sr")) {
                                    map1.put("ec", map1.get("ec") + 1);
                                } else if (k == map1.get("sc")) {
                                    map1.put("er", map1.get("er") + 1);
                                }
                            }
                            String str = xwpfTables.get(i).getRows().get(j).getTableCells().get(k).getText().split("∑")[0];
                            xwpfTables.get(i).getRows().get(j).getTableCells().get(k).removeParagraph(0);
                            xwpfTables.get(i).getRows().get(j).getTableCells().get(k).setText(str);
                            xwpfTables.get(i).getRows().get(j).getTableCells().get(k).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
                            xwpfTables.get(i).getRows().get(j).getTableCells().get(k).getParagraphArray(0).setAlignment(ParagraphAlignment.CENTER);
                        }
                    }
                }
                // 单元格排序, 避免格式错乱
                List<Map.Entry<String, Map<String, Integer>>> entries = new ArrayList<>(maps.entrySet());
                entries.sort((o1, o2) -> o1.getValue().get("sc") - o2.getValue().get("sc"));
                // 按照顺序添加进集合
                List<String> list = new ArrayList<>();
                for (Map.Entry<String, Map<String, Integer>> entry : entries) {
                    list.add(entry.getKey());
                }
                for (int a = list.size() - 1; a >= 0; a--) {
                    Map<String, Integer> v = maps.get(list.get(a));
                    for (int j = 0; j < v.get("er") - v.get("sr") + 1; j++) {
                        if (v.get("ec") > v.get("sc")) {
                            try {
                                mergeCellsHorizontally(xwpfTables.get(i), v.get("sr") + j, v.get("sc"), v.get("ec"));
//                                TableTools.mergeCellsHorizonal(xwpfTables.get(i), v.get("sr") + j, v.get("sc"), v.get("ec"));
                            } catch (Exception e) {
                            }
                        }
                    }
                    if (v.get("er") > v.get("sr")) {
                        try {
                            mergeCellsVertically(xwpfTables.get(i), v.get("sc"), v.get("sr"), v.get("er"));
//                            TableTools.mergeCellsVertically(xwpfTables.get(i), v.get("sc"), v.get("sr"), v.get("er"));
                        } catch (Exception e) {
                        }
                    }
                }
            }
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            document.write(fileOutputStream);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //处理中英文换行的问题
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
    public int exportInManageRecordIssueRecycle(MultipartFile file) {
        List<ManageRecordIssueRecycle> manageRecordIssueRecycleList = new ArrayList<>();
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
                for (int i = 2; i < rows.size(); i++) { // 从第三行开始，跳过表头
                    XWPFTableRow row = rows.get(i);
                    if (row.getTableCells().size() != 12) {
                        continue;
                    }
                    if (ObjectUtils.isNotEmpty(row.getCell(1).getText())) {
                        ManageRecordIssueRecycle manageRecordIssueRecycle = new ManageRecordIssueRecycle();
                        manageRecordIssueRecycle.setDocumentCode(row.getCell(1).getText());
                        manageRecordIssueRecycle.setDocumentName(row.getCell(2).getText());
                        manageRecordIssueRecycle.setDocumentVersion(row.getCell(3).getText());
                        manageRecordIssueRecycle.setPages(row.getCell(4).getText());
                        manageRecordIssueRecycle.setDocumentType(row.getCell(5).getText());
                        manageRecordIssueRecycle.setNumber(row.getCell(6).getText());
                        manageRecordIssueRecycle.setDepartLims(row.getCell(7).getText());
                        try {
                            manageRecordIssueRecycle.setReceiveUser(userMapper.selectOne(Wrappers.<User>lambdaQuery()
                                    .eq(User::getName, row.getCell(8).getText())).getId());
                        } catch (Exception e) {
                            manageRecordIssueRecycle.setReceiveUser(null);
                        }
                        try {
                            manageRecordIssueRecycle.setSignedUser(userMapper.selectOne(Wrappers.<User>lambdaQuery()
                                    .eq(User::getName, row.getCell(10).getText())).getId());
                        } catch (Exception e) {
                            manageRecordIssueRecycle.setSignedUser(null);
                        }
                        try {
                            manageRecordIssueRecycle.setReceiveDate(LocalDate.parse(row.getCell(9).getText(), dateTimeFormatter));
                        } catch (Exception e) {
                            manageRecordIssueRecycle.setReceiveDate(null);
                        }
                        try {
                            manageRecordIssueRecycle.setSignedDate(LocalDate.parse(row.getCell(11).getText(), dateTimeFormatter));
                        } catch (Exception e) {
                            manageRecordIssueRecycle.setSignedDate(null);
                        }

                        ManageRecordIssueRecycle issueRecycle = manageRecordIssueRecycleMapper.selectOne(Wrappers.<ManageRecordIssueRecycle>lambdaQuery()
                                .eq(ManageRecordIssueRecycle::getDocumentCode, manageRecordIssueRecycle.getDocumentCode())
                                .eq(ManageRecordIssueRecycle::getDocumentName, manageRecordIssueRecycle.getDocumentName())
                                .eq(ManageRecordIssueRecycle::getDocumentVersion, manageRecordIssueRecycle.getDocumentVersion()));
                        if (ObjectUtils.isNotEmpty(issueRecycle)) {
                            manageRecordIssueRecycle.setId(issueRecycle.getId());
                        }
                        manageRecordIssueRecycleList.add(manageRecordIssueRecycle);
                    }
                }
            }
            saveOrUpdateBatch(manageRecordIssueRecycleList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int addManageRecordIssueRecycle(ManageRecordIssueRecycle manageRecordIssueRecycle) {
        return manageRecordIssueRecycleMapper.insert(manageRecordIssueRecycle);
    }

    @Override
    public int doManageRecordIssueRecycle(ManageRecordIssueRecycle manageRecordIssueRecycle) {
        return manageRecordIssueRecycleMapper.updateById(manageRecordIssueRecycle);
    }

    // 水平合并单元格
    private static void mergeCellsHorizontally(XWPFTable table, int row, int fromCol, int toCol) {
        for (int i = fromCol; i <= toCol; i++) {
            if (i == fromCol) {
                table.getRow(row).getCell(i).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
            } else {
                table.getRow(row).getCell(i).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

    // 垂直合并单元格
    private static void mergeCellsVertically(XWPFTable table, int col, int fromRow, int toRow) {
        for (int i = fromRow; i <= toRow; i++) {
            if (i == fromRow) {
                table.getRow(i).getCell(col).getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
            } else {
                table.getRow(i).getCell(col).getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
            }
        }
    }
}
