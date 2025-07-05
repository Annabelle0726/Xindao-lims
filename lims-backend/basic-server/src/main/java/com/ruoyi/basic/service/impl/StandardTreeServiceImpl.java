package com.ruoyi.basic.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.basic.dto.FactoryDto;
import com.ruoyi.basic.dto.LaboratoryDto;
import com.ruoyi.basic.dto.SampleTypeDto;
import com.ruoyi.basic.mapper.StandardProductListMapper;
import com.ruoyi.basic.mapper.StandardTreeMapper;
import com.ruoyi.basic.pojo.StandardProductList;
import com.ruoyi.basic.pojo.StandardTree;
import com.ruoyi.basic.pojo.StructureTestObject;
import com.ruoyi.basic.service.StandardProductListService;
import com.ruoyi.basic.service.StandardTreeService;
import com.ruoyi.basic.service.StructureTestObjectService;
import com.ruoyi.common.exception.base.BaseException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @description 针对表【standard_tree(标准树)】的数据库操作Service实现
 * @createDate 2024-03-01 15:06:44
 */
@Service
@AllArgsConstructor
public class StandardTreeServiceImpl extends ServiceImpl<StandardTreeMapper, StandardTree>
        implements StandardTreeService {


    private StandardTreeMapper standardTreeMapper;

    private StandardProductListMapper standardProductListMapper;

    private StandardProductListService standardProductListService;

    private StructureTestObjectService structureTestObjectService;


    @Override
    public List<FactoryDto> selectStandardTreeList() {
        List<FactoryDto> factoryDtos = standardTreeMapper.selectStandardTreeList();
        for (FactoryDto factoryDto : factoryDtos) {
            for (LaboratoryDto laboratoryDto : factoryDto.getChildren()) {
                laboratoryDto.getChildren().sort((o1, o2) -> (o1.getSort() == null ? 0 : o1.getSort())
                        - (o2.getSort() == null ? 0 : o2.getSort()));
                for (SampleTypeDto sampleTypeDto : laboratoryDto.getChildren()) {
//                    if (sampleTypeDto.getChildren().size() == 0) {
                    sampleTypeDto.getChildren().addAll(standardTreeMapper.getStandardTree3(sampleTypeDto.getValue()));
//                    }
                }
            }
        }
        return factoryDtos;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addStandardTree(StandardTree standardTree) {
        LambdaQueryWrapper<StandardTree> wrapper = Wrappers.<StandardTree>lambdaQuery()
                .eq(StandardTree::getFactory, standardTree.getFactory())
                .eq(StandardTree::getLaboratory, standardTree.getLaboratory())
                .eq(StandardTree::getSampleType, standardTree.getSampleType())
                .eq(StandardTree::getSample, standardTree.getSample())
                .eq(StandardTree::getModel, standardTree.getModel());
        if (StringUtils.isNotBlank(standardTree.getSample())) {
            wrapper.eq(StandardTree::getSample, standardTree.getSample());
        }

        StandardTree tree = standardTreeMapper.selectOne(wrapper);
        if (tree != null) {
            throw new BaseException("该型号已存在");
        }
        return standardTreeMapper.insert(standardTree);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delStandardTree(String tree) {
        String[] trees = tree.split(" - ");
        switch (trees.length) {
            case 5:
                if (trees[3].equals("null")) {
                    standardTreeMapper.delete(Wrappers.<StandardTree>lambdaUpdate().eq(StandardTree::getFactory, trees[0]).eq(StandardTree::getLaboratory, trees[1]).eq(StandardTree::getSampleType, trees[2]).isNull(StandardTree::getSample).eq(StandardTree::getModel, trees[4]));
                } else {
                    standardTreeMapper.delete(Wrappers.<StandardTree>lambdaUpdate().eq(StandardTree::getFactory, trees[0]).eq(StandardTree::getLaboratory, trees[1]).eq(StandardTree::getSampleType, trees[2]).eq(StandardTree::getSample, trees[3]).eq(StandardTree::getModel, trees[4]));
                }
                break;
        }
        return 1;
    }

    @Override
    public List<SampleTypeDto> getStandardTree2() {
        return standardTreeMapper.getStandardTree2();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int upStandardProducts(Map<String, Object> product) {
        List<Integer> ids = JSON.parseArray(product.get("ids") + "");
        StandardProductList productList = JSON.parseObject(JSON.toJSONString(product.get("standardProductList")), StandardProductList.class);
        if (productList.getMethodS() != null) {
            standardProductListMapper.update(productList, Wrappers.<StandardProductList>lambdaUpdate().in(StandardProductList::getId, ids).like(StandardProductList::getMethod, productList.getMethodS()));
            return 1;
        }
        if (productList.getRadiusList() != null) {
            standardProductListMapper.update(productList, Wrappers.<StandardProductList>lambdaUpdate().in(StandardProductList::getId, ids).like(StandardProductList::getRadiusList, productList.getRadius()));
            return 1;
        }
        standardProductListMapper.update(productList, Wrappers.<StandardProductList>lambdaUpdate().in(StandardProductList::getId, ids));
        return 1;
    }

    @Override
    public boolean updateTreeSort(List<FactoryDto> list) {
        List<StructureTestObject> testObjects = new ArrayList<>();
        for (FactoryDto factoryDto : list) {
            for (LaboratoryDto laboratoryDto : factoryDto.getChildren()) {
                List<SampleTypeDto> children = laboratoryDto.getChildren();
                int sort = 0;
                // 循环第三层
                for (SampleTypeDto child : children) {
                    StructureTestObject structureTestObject = new StructureTestObject();
                    structureTestObject.setSort(sort);
                    structureTestObject.setId(child.getSampleTypeId());
                    testObjects.add(structureTestObject);
                    sort++;
                }
            }
        }
        structureTestObjectService.updateBatchById(testObjects);

        return true;
    }

    /**
     * 修改标准数
     * @param standardTree
     * @return
     */
    @Override
    public int updateStandardTree(StandardTree standardTree) {
        // 修改名称匹配的标准树下的检验项目
        // 查询所有对象+名称的树

        LambdaUpdateWrapper<StandardProductList> updateWrapper = Wrappers.<StandardProductList>lambdaUpdate()
                .eq(StandardProductList::getFactory, standardTree.getFactory())
                .eq(StandardProductList::getLaboratory, standardTree.getLaboratory())
                .eq(StandardProductList::getSampleType, standardTree.getSampleType())
                .eq(StandardProductList::getModel, standardTree.getOldModel());
        if (StringUtils.isNotBlank(standardTree.getSample()) && !standardTree.getSample().equals("null")) {
            updateWrapper.eq(StandardProductList::getSample, standardTree.getSample());
        } else {
            updateWrapper.isNull(StandardProductList::getSample);
        }

        List<StandardProductList> standardProductLists = standardProductListService.list(updateWrapper);
        if (CollectionUtils.isNotEmpty(standardProductLists)) {
            for (StandardProductList standardProductList : standardProductLists) {
                // 修改样品名称
                standardProductList.setModel(standardTree.getModel());
                // 修改树名称
                // 需要截取第四级, 避免三四级名称一样修改错误
                String[] trees = standardProductList.getTree().split(" - ");
                trees[4] = standardTree.getModel();
                List<String> list = CollUtil.newArrayList(trees);
                String newName = CollUtil.join(list, " - ");
                standardProductList.setTree(newName);
            }
            standardProductListService.updateBatchById(standardProductLists);
        }

        // 修改标准数检验项目
        LambdaUpdateWrapper<StandardTree> wrapper = Wrappers.<StandardTree>lambdaUpdate()
                .eq(StandardTree::getFactory, standardTree.getFactory())
                .eq(StandardTree::getLaboratory, standardTree.getLaboratory())
                .eq(StandardTree::getSampleType, standardTree.getSampleType())
                .eq(StandardTree::getModel, standardTree.getOldModel())
                .set(StandardTree::getModel, standardTree.getModel());
        if (StringUtils.isNotBlank(standardTree.getSample()) && !standardTree.getSample().equals("null")) {
            wrapper.eq(StandardTree::getSample, standardTree.getSample());
        } else {
            wrapper.isNull(StandardTree::getSample);
        }
        return standardTreeMapper.update(null, wrapper);
    }


}




