package com.ruoyi.manage.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.manage.pojo.ManageDocumentList;
import com.ruoyi.manage.service.ManageDocumentListService;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Data
public class ManageDocumentListListener extends AnalysisEventListener<ManageDocumentList> {
    private static final int BATCH_COUNT = 1000;
    List<ManageDocumentList> list = new ArrayList<>();

    private ManageDocumentListService manageDocumentListService;

    public ManageDocumentListListener(ManageDocumentListService manageDocumentListService) {
        this.manageDocumentListService = manageDocumentListService;
    }

    @Override
    public void invoke(ManageDocumentList data, AnalysisContext analysisContext) {
        list.add(data);
        if (list.size() >= BATCH_COUNT) {
            save();
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        int size = list.size();
        if(size == 0){
            throw new BaseException("导入数据不得为空");
        }else {
            save();
        }
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        // 获取表头信息
        if(CollectionUtils.isEmpty(headMap) || headMap.size() != 8){
            throw new RuntimeException("导入模板有误");
        }
        List<String> colums = Arrays.asList(
                "序号", "文件编号", "类别", "名称",
                "文件版本", "作者", "生效日期", "文件状态"
        );
        for (int i = 0; i < colums.size(); i++) {
            if(!headMap.get(i).equals(colums.get(i))){
                throw new BaseException("导入模板有误");
            }
        }
    }

    private void save() {
        manageDocumentListService.importExcel(list);
    }
}
