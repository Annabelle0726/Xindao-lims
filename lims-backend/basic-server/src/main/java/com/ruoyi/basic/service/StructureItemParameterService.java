package com.ruoyi.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.basic.pojo.StructureItemParameter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StructureItemParameterService extends IService<StructureItemParameter> {

    /**
     * 导入检验项目
     * @param file
     */
    void importEquipData(MultipartFile file) throws IOException;
}
