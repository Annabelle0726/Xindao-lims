package com.ruoyi.performance.excel;


import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.ruoyi.performance.dto.AuxiliaryCorrectionHoursDto;
import com.ruoyi.performance.service.AuxiliaryCorrectionHoursService;

import java.util.ArrayList;
import java.util.List;

public class AuxiliaryCorrectionHoursListener extends AnalysisEventListener<AuxiliaryCorrectionHoursDto> {

    private static final int BATCH_COUNT = 1000;
    List<AuxiliaryCorrectionHoursDto> list = new ArrayList<>();

    private AuxiliaryCorrectionHoursService auxiliaryCorrectionHoursService;

    public AuxiliaryCorrectionHoursListener(AuxiliaryCorrectionHoursService auxiliaryCorrectionHoursService) {
        this.auxiliaryCorrectionHoursService = auxiliaryCorrectionHoursService;
    }

    @Override
    public void invoke(AuxiliaryCorrectionHoursDto data, AnalysisContext analysisContext) {
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
        auxiliaryCorrectionHoursService.importExcel(list);
    }
}
