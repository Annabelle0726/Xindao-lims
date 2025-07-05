package com.ruoyi.basic.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.ruoyi.basic.service.ProductService;

import java.util.ArrayList;
import java.util.List;

public class StructureTestObjectListener extends AnalysisEventListener<StructureTestObjectData> {
    private static final int BATCH_COUNT = 1000;
    List<StructureTestObjectData> list = new ArrayList<>();

    private ProductService productService;

    public StructureTestObjectListener(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void invoke(StructureTestObjectData data, AnalysisContext analysisContext) {
        list.add(data);
        if (list.size() >= BATCH_COUNT) {
            save();
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        save();
    }


    private void save() {
        productService.importPartExcel(list);
    }
}
