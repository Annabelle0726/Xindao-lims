package com.ruoyi.inspect.util;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;

import java.util.*;

/**
 * @Author zhuo
 * @Date 2024/11/16
 */
public class XWPFDocumentUtils {

    public static void updateMergeByDocument(XWPFDocument document) {
        // 处理合并单元格的问题
        List<XWPFTable> xwpfTables = document.getTables();
        for (int i = 0; i < xwpfTables.size(); i++) {
            Set<String> set1 = new HashSet<>();
            Map<String, Map<String, Integer>> maps = new HashMap<>();
            for (int j = 0; j < xwpfTables.get(i).getRows().size(); j++) {
                for (int k = 0; k < xwpfTables.get(i).getRows().get(j).getTableCells().size(); k++) {
                    if (xwpfTables.get(i).getRows().get(j).getTableCells().get(k).getText().contains("∑")) {
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
                        } catch (Exception e) {
                        }
                    }
                }
                if (v.get("er") > v.get("sr")) {
                    try {
                        mergeCellsVertically(xwpfTables.get(i), v.get("sc"), v.get("sr"), v.get("er"));
                    } catch (Exception e) {
                    }
                }
            }
        }
        //处理中英文换行的问题
        List<XWPFTable> xwpfTables1 = document.getTables();
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

