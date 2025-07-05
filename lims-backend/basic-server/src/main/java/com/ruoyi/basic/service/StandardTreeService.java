package com.ruoyi.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.basic.dto.FactoryDto;
import com.ruoyi.basic.dto.SampleTypeDto;
import com.ruoyi.basic.pojo.StandardTree;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【standard_tree(标准树)】的数据库操作Service
* @createDate 2024-03-01 15:06:44
*/
public interface StandardTreeService extends IService<StandardTree> {

    List<FactoryDto> selectStandardTreeList();


    int addStandardTree(StandardTree standardTree);

    int delStandardTree(String tree);

    List<SampleTypeDto> getStandardTree2();

    int upStandardProducts(Map<String, Object> product);

    /**
     * 标准数排序
     * @param list
     * @return
     */
    boolean updateTreeSort(List<FactoryDto> list);

    int updateStandardTree(StandardTree standardTree);
}
