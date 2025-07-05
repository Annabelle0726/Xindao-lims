package com.ruoyi.personnel.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.ruoyi.personnel.service.PersonTrainingDetailedService;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PersonTrainingDetailedListener extends AnalysisEventListener<PersonTrainingDetailedUpload> {

    private Integer planId;

    private static final int BATCH_COUNT = 1000;
    List<PersonTrainingDetailedUpload> list = new ArrayList<>();

    private PersonTrainingDetailedService personTrainingDetailedService;

    public PersonTrainingDetailedListener(PersonTrainingDetailedService personTrainingDetailedService) {
        this.personTrainingDetailedService = personTrainingDetailedService;
    }

    @Override
    public void invoke(PersonTrainingDetailedUpload data, AnalysisContext context) {
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
        personTrainingDetailedService.importExcel(list, this.planId);
    }
}
