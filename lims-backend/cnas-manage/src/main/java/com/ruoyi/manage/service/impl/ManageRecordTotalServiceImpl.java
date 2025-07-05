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
import com.ruoyi.manage.mapper.ManageRecordTotalMapper;
import com.ruoyi.manage.mapper.ManageRecordVerifyMapper;

import com.ruoyi.manage.pojo.ManageRecordTotal;
import com.ruoyi.manage.pojo.ManageRecordVerify;

import com.ruoyi.manage.service.ManageRecordTotalService;

import com.ruoyi.system.mapper.UserMapper;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
 * 外来文件确认记录总历史记录表 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-12 10:30:08
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ManageRecordTotalServiceImpl extends ServiceImpl<ManageRecordTotalMapper, ManageRecordTotal> implements ManageRecordTotalService {

    @Resource
    private ManageRecordTotalMapper manageRecordTotalMapper;

    @Value("${wordUrl}")
    private String wordUrl;



    @Resource
    private UserMapper userMapper;

    @Value("${file.path}")
    private String imgUrl;

    @Resource
    private ManageRecordVerifyMapper manageRecordVerifyMapper;

    @Override
    public IPage<ManageRecordTotal> pageManageRecordTotal(Page page, ManageRecordTotal manageRecordTotal) {
        return manageRecordTotalMapper.pageProcessTotaldeal(page, QueryWrappers.queryWrappers(manageRecordTotal));
    }

    @Override
    public int submitManageRecordTotal(Integer id) {
        Integer userId = SecurityUtils.getUserId().intValue();
        User user = userMapper.selectById(userId);
        if (ObjectUtils.isEmpty(user.getSignatureUrl())) throw new ErrorException("未找到填表人的电子签名,请上传自己的电子签名!");
        ManageRecordTotal manageRecordTotal = manageRecordTotalMapper.selectById(id);
        manageRecordTotal.setSubmitUser(userId);
        manageRecordTotal.setSubmitDate(LocalDate.now());
        manageRecordTotal.setSubmitUrl(user.getSignatureUrl());
        //生成样品处理申请表并将填表人的电子签名印上
        manageRecordTotal.setUrl(recordTotaldeal(id,user.getSignatureUrl()));
        return manageRecordTotalMapper.updateById(manageRecordTotal);
    }

    @Override
    public int ratifyManageRecordTotal(Integer id, String ratifyState) {
        Integer userId = SecurityUtils.getUserId().intValue();
        User user = userMapper.selectById(userId);
        if (ObjectUtils.isEmpty(user.getSignatureUrl())) throw new ErrorException("未找到批准人的电子签名,请上传自己的电子签名!");
        ManageRecordTotal manageRecordTotal = manageRecordTotalMapper.selectById(id);
        manageRecordTotal.setRatifyUser(userId);
        manageRecordTotal.setRatifyState(ratifyState);
        manageRecordTotal.setRatifyDate(LocalDate.now());
        manageRecordTotal.setRatifyUrl(user.getSignatureUrl());
        //将批准人的签名印上
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        wordInsertUrl(new HashMap<String, Object>() {{
            put("ratifyDateEn", monthNames[LocalDate.now().getMonthValue() - 1] + " " + LocalDate.now().getDayOfMonth() + ", " + LocalDate.now().getYear());
            put("ratifyDate", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")));
            put("ratifyUrl", new FilePictureRenderData(100,50,imgUrl + "/" + user.getSignatureUrl()));
        }},  wordUrl+"/"+manageRecordTotal.getUrl());
        return manageRecordTotalMapper.updateById(manageRecordTotal);
    }

    public int wordInsertUrl(Map<String, Object> map, String url) {
        XWPFTemplate template = XWPFTemplate.compile(url).render(map);
        try {
            template.writeAndClose(Files.newOutputStream(Paths.get(url)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return 1;
    }


    private String recordTotaldeal(Integer id,String signatureUrl){
        List<ManageRecordVerify> manageRecordVerifies = manageRecordVerifyMapper.selectList(Wrappers.<ManageRecordVerify>lambdaQuery().eq(ManageRecordVerify::getManageRecordTotalId,id));
        String url;
        try {
            InputStream inputStream = this.getClass().getResourceAsStream("/static/verify-deal.docx");
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
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        List<Map<String, Object>> verifyList = new ArrayList<>();
        Integer index = 1;
        Integer index1 = 1;
        for (int c = 0; c < manageRecordVerifies.size(); c++) {
            //超过21行换页
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
                    for (int j = 0; j < 8; j++) {
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
                                //第二列样品名称
                                textRenderData.setText("外来文件名称@Foreign file name");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 2) {
                                //第三列样品编号
                                textRenderData.setText("文件编号@File number");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 3) {
                                //第四列供样单位
                                textRenderData.setText("标准规范名称@Standard specification name");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 4) {
                                //第五列数量
                                textRenderData.setText("标准号@Stanard number");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 5) {
                                //第六列处理方式
                                textRenderData.setText("生效日期@Effective Date");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            } else if (j == 6) {
                                //第七列处理方式
                                textRenderData.setText("作废日期@Void date");
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            }else {
                                //第八列时间
                                textRenderData.setText("备注@Remaek");
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
                                try{
                                    String sampleName = manageRecordVerifies.get((i-1) + (index1 - 1) * 20).getDocumentName();
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
                                try{
                                    textRenderData.setText(manageRecordVerifies.get((i-1) + (index1 - 1) * 20).getDocumentName());
                                } catch (Exception e) {
                                    textRenderData.setText("");
                                }
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            }
                            else if (j == 2) {
                                try{
                                    textRenderData.setText(manageRecordVerifies.get((i-1) + (index1 - 1) * 20).getDocumentCode());
                                } catch (Exception e) {
                                    textRenderData.setText("");
                                }
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            }
                            else if (j == 3) {
                                try{
                                    textRenderData.setText(manageRecordVerifies.get((i-1) + (index1 - 1) * 20).getStandardName());
                                } catch (Exception e) {
                                    textRenderData.setText("");
                                }
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            }
                            else if (j == 4) {
                                try{
                                    textRenderData.setText(manageRecordVerifies.get((i-1) + (index1 - 1) * 20).getStandardCode());
                                } catch (Exception e) {
                                    textRenderData.setText("");
                                }
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            }
                            else if (j == 5) {
                                try{
                                    textRenderData.setText(manageRecordVerifies.get((i-1) + (index1 - 1) * 20).getEffectiveDate()+"");
                                } catch (Exception e) {
                                    textRenderData.setText("");
                                }
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            }
                            else if (j==6){
                                try{
                                    textRenderData.setText(manageRecordVerifies.get((i-1) + (index1 - 1) * 20).getCancelDate()+"");
                                } catch (Exception e) {
                                    textRenderData.setText("");
                                }
                                renderData.add(textRenderData);
                                paragraphRenderData.setContents(renderData);
                                paragraphRenderDataList.add(paragraphRenderData);
                                cellRenderData.setParagraphs(paragraphRenderDataList);
                                cells.add(cellRenderData);
                            }
                            else {
                                try{
                                    textRenderData.setText(manageRecordVerifies.get((i-1) + (index1 - 1) * 20).getNote());
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
                table.put("verify", tableRenderData);
                table.put("index1", index1);
                verifyList.add(table);
                index1++;
            }
        }
        Integer finalIndex = index1;
        XWPFTemplate template = XWPFTemplate.compile(url, builder.build()).render(
                new HashMap<String, Object>() {{
                    put("verifyList", verifyList);
                    put("size", finalIndex);
                    put("submitDate", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")));
                    put("submitDateEn", monthNames[LocalDate.now().getMonthValue() - 1] + " " + LocalDate.now().getDayOfMonth() + ", " + LocalDate.now().getYear());
                    put("ratifyDate", "{{ratifyDate}}");
                    put("ratifyDateEn","{{ratifyDateEn}}");
                    put("ratifyUrl", null);
                    put("writeUrl", new FilePictureRenderData(100,50,imgUrl + "/" + signatureUrl));
                }});
        String name = UUID.randomUUID() + "_样品处理申请表" + ".docx";
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
}
