package com.ruoyi.basic.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.basic.dto.CopyStandardProductListDto;
import com.ruoyi.basic.dto.InsSampleReceiveDto;
import com.ruoyi.basic.dto.ProductDto;
import com.ruoyi.basic.dto.ResetTreeDragDTO;
import com.ruoyi.basic.mapper.IfsInventoryQuantityMapper;
import com.ruoyi.basic.mapper.StandardProductListMapper;
import com.ruoyi.basic.mapper.StandardProductListSupplierAskMapper;
import com.ruoyi.basic.mapper.StandardTreeMapper;
import com.ruoyi.basic.pojo.StandardProductList;
import com.ruoyi.basic.pojo.StandardProductListSupplierAsk;
import com.ruoyi.basic.pojo.StandardTree;
import com.ruoyi.basic.service.StandardProductListService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description 针对表【standard_product_list(标准树下的检验项目)】的数据库操作Service实现
 * @createDate 2024-03-05 10:33:29
 */
@Service
@AllArgsConstructor
public class StandardProductListServiceImpl extends ServiceImpl<StandardProductListMapper, StandardProductList>
        implements StandardProductListService {

    private StandardProductListMapper standardProductListMapper;

    private StandardTreeMapper standardTreeMapper;

    private IfsInventoryQuantityMapper ifsInventoryQuantityMapper;

    private StandardProductListSupplierAskMapper standardProductListSupplierAskMapper;


    @Override
    public int upStandardProductList(StandardProductList list) {
        return standardProductListMapper.updateById(list);
    }


    @Override
    public List<StandardProductList> selectStandardProductList(InsSampleReceiveDto insSample) {
        // 是否没有产品
        boolean isNoSample = false;
//        String[] models = insSample.getModel().split("-(?=[^-]*$)");//拆分最后一个【-】
        String model = insSample.getModel();
        String modelNum = insSample.getModelNum();
        List<StandardProductList> list = standardProductListMapper.selectDetail(insSample.getStandardMethodListId(), 1, model, insSample.getIsCableTag());
        if (list.size() == 0) {
            if (Objects.equals(insSample.getFactory(), "") || insSample.getFactory() == null) {
                return null;
            }
            String[] split = insSample.getFactory().split(" - ");
            split[3] = split[3].replace("- ", "");
            String tree = split[0] + " - " + split[1] + " - " + split[2] + " - " + split[3] + " - null";
            list = standardProductListMapper.selectDetail2(insSample.getStandardMethodListId(), 1, tree, insSample.getIsCableTag());
            if (list.size() == 0) {
                String tree1 = split[0] + " - " + split[1] + " - " + split[2] + " - null - " + split[3];
                list = standardProductListMapper.selectDetail2(insSample.getStandardMethodListId(), 1, tree1, insSample.getIsCableTag());

                // 只有对象的一层
                if (list.size() == 0) {
                    String tree2 = split[0] + " - " + split[1] + " - " + split[2] + " - null - null";
                    list = standardProductListMapper.selectDetail2(insSample.getStandardMethodListId(), 1, tree2, insSample.getIsCableTag());
                    // 样品直接赋值样品分类
                    list.forEach(standardProductList -> standardProductList.setSample(standardProductList.getSampleType()));
                    isNoSample = true;
                }
            }
        }
        String[] split1 = insSample.getFactory().split(" - ");
        if (!isNoSample) {
            //判断长度
            if (split1.length > 4) {
                if (ObjectUtils.isNotEmpty(split1[3])) {
                    list = list.stream().filter(list1 -> Objects.nonNull(list1.getSample()) && Objects.equals(list1.getSample(), split1[3])).collect(Collectors.toList());
                } else if (split1[3].equals("")) {
                    list = list.stream().filter(list1 -> Objects.nonNull(list1.getSampleType()) && Objects.equals(list1.getSampleType(), split1[2])).collect(Collectors.toList());
                }
            }
        }
        list = list.stream().filter(a -> {
                    try {
                        if (a.getSection() != null && !Objects.equals(a.getSection(), "")) {
                            List<String> sections = JSON.parseArray(a.getSection(), String.class);// 区间
                            List<String> cores = JSON.parseArray(a.getCores(), String.class); // 芯数
                            List<String> conductorMaterials = JSON.parseArray(a.getConductorMaterial(), String.class); // 导体材质
                            List<String> conductorTypes = JSON.parseArray(a.getConductorType(), String.class); // 导体类型
                            List<String> asks = JSON.parseArray(a.getAsk(), String.class);
                            List<String> tells = JSON.parseArray(a.getTell(), String.class);
                            boolean isIf;
                            for (int i = 0; i < sections.size(); i++) {
                                if (Objects.equals(a.getBsm(), "1")) {
                                    return true;
                                } else {
                                    if (sections.get(i).contains("&")) {
                                        String[] split = sections.get(i).split("&");
                                        isIf = getIsIf(split[0], modelNum, cores.get(i), conductorMaterials.get(i), conductorTypes.get(i), insSample)
                                                && getIsIf(split[1], modelNum, cores.get(i), conductorMaterials.get(i), conductorTypes.get(i), insSample);
                                    } else {
                                        isIf = getIsIf(sections.get(i), modelNum, cores.get(i), conductorMaterials.get(i), conductorTypes.get(i), insSample);
                                    }
                                    if (isIf) {
                                        a.setSection(sections.get(i));
                                        a.setAsk(asks.get(i));
                                        a.setTell(tells.get(i));
                                        return true;
                                    }
                                }
                            }
                            return false;
                        }
                    } catch (Exception ignored) {
                        return false;
                    }
                    return true;
                }).peek(standardProductList -> {
                    //判断是否是原材下单, 需要啊把颜色绑定到试样颜色的要求值上
                    if (StringUtils.isNotBlank(insSample.getPartNo())) {
                        // 判断是否有这个字段且是文本类型
                        if (StringUtils.isNotBlank(standardProductList.getInspectionItem())
                                && standardProductList.getInspectionItem().contains("试样颜色")) {
                            Map<String, String> partColor = baseMapper.selectPartColor(insSample.getPartNo());
                            if (CollectionUtils.isNotEmpty(partColor)) {
                                if (StringUtils.isNotBlank(partColor.get("color"))) {
                                    // 判断检验值是否为空和不等于 -  和 /
                                    if (StringUtils.isBlank(standardProductList.getAsk()) || partColor.get("color").contains("本")) { // 没有ask直接复制
                                        // 查询对象绑定表和产品绑定表
                                        standardProductList.setAsk("=" + partColor.get("color")
                                                + (StringUtils.isBlank(partColor.get("color_code")) ? "" : "(" + partColor.get("color_code") + ")"));
                                        standardProductList.setTell(partColor.get("color")
                                                + (StringUtils.isBlank(partColor.get("color_code")) ? "" : "(" + partColor.get("color_code") + ")"));
                                        standardProductList.setInspectionValueType("2");
                                    } else { // 拼接到要求描述前面
                                        standardProductList.setTell(partColor.get("color")
                                                + (StringUtils.isBlank(partColor.get("color_code")) ? "" : "(" + partColor.get("color_code") + ")") + "@" + standardProductList.getTell());
                                    }
                                }
                            }
                        }
                    }
                })
                .collect(Collectors.toList());

        // 查询厂家是否有特殊要求值
        if (insSample.getIfsInventoryId() != null) {
            // 查询原材料厂家名称
            String supplierName = ifsInventoryQuantityMapper.selectById(insSample.getIfsInventoryId())
                    .getSupplierName();
            List<Long> collect = list.stream().map(StandardProductList::getId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)) {
                List<StandardProductListSupplierAsk> supplierAsks = standardProductListSupplierAskMapper.selectList(Wrappers.<StandardProductListSupplierAsk>lambdaQuery()
                        .in(StandardProductListSupplierAsk::getProductListId, collect)
                        .eq(StandardProductListSupplierAsk::getSupplierName, supplierName));

                // 判断有没有特殊值绑定
                if (CollectionUtils.isNotEmpty(supplierAsks)) {
                    for (StandardProductList standardProductList : list) {
                        for (StandardProductListSupplierAsk supplierAsk : supplierAsks) {
                            if (standardProductList.getId().equals(supplierAsk.getProductListId())) {
                                // 重新赋值要求值和要求描述
                                standardProductList.setAsk(supplierAsk.getAsk());
                                standardProductList.setTell(supplierAsk.getTell());
                            }
                        }
                    }
                }
            }
        }

        return list;
    }

    /**
     *
     * @param str                 判定公式
     * @param model               型号
     * @param standardCores       芯数
     * @param conductorMaterial   导体材质
     * @param conductorType       导体类型
     * @param insSample
     * @return
     */
    private boolean getIsIf(String str, String model, String standardCores, String conductorMaterial, String conductorType,InsSampleReceiveDto insSample) {
        Matcher matcher = Pattern.compile("\\d+(\\.\\d+)?").matcher(model);
        String model2 = "";
        while (matcher.find()) {
            model2 += matcher.group();
            break;
        }
        boolean flag = false;
        if (str.contains("≥") || str.contains(">=")) {
            String param = str.replace("≥", "").replace(">=", "");
            flag = new BigDecimal(model2).compareTo(new BigDecimal(param)) > -1;
        } else if (str.contains("≤") || str.contains("<=")) {
            String param = str.replace("≤", "").replace("<=", "");
            flag = new BigDecimal(model2).compareTo(new BigDecimal(param)) < 1;
        } else if (str.contains(">") || str.contains("＞")) {
            String param = str.replace(">", "").replace("＞", "");
            flag = new BigDecimal(model2).compareTo(new BigDecimal(param)) > 0;
        } else if (str.contains("<") || str.contains("＜")) {
            String param = str.replace("<", "").replace("＜", "");
            flag = new BigDecimal(model2).compareTo(new BigDecimal(param)) < 0;
        } else if (str.contains("=")) {
            String param = str.replace("=", "");
            flag = new BigDecimal(model2).compareTo(new BigDecimal(param)) == 0;
        }
        if (flag) {
            boolean coresMatch = true;
            boolean conductorMaterialMatch = true;
            boolean conductorTypeMatch = true;

            // 判断是否有线芯数量
            if (StringUtils.isNotBlank(standardCores)) {
                if (StringUtils.isBlank(insSample.getCores()) || !standardCores.equals(insSample.getCores())) {
                    coresMatch = false;
                }
            }

            // 判断是否有导体材质
            if (StringUtils.isNotBlank(conductorMaterial)) {
                if (StringUtils.isBlank(insSample.getConductorMaterial()) || !conductorMaterial.equals(insSample.getConductorMaterial())) {
                    conductorMaterialMatch = false;
                }
            }

            // 判断是否有导体类型
            if (StringUtils.isNotBlank(conductorType)) {
                if (StringUtils.isBlank(insSample.getConductorType()) || !conductorType.equals(insSample.getConductorType())) {
                    conductorTypeMatch = false;
                }
            }
            // 最终判断
            flag = coresMatch && conductorMaterialMatch && conductorTypeMatch;
        }

        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> selectStandardProductListByMethodId(Integer id, String tree) {
        String[] trees = tree.split(" - ");
        // 判断是否拖拽
        boolean isDrag = false;
        List<StandardProductList> list = new ArrayList<>();
        if (trees.length == 3) {
            List<StandardTree> treeList = new ArrayList<>();
            StandardTree standardTree = new StandardTree();
            standardTree.setFactory(trees[0]);
            standardTree.setLaboratory(trees[1]);
            standardTree.setSampleType(trees[2]);
            List<ProductDto> pList = standardTreeMapper.selectPList(trees[2]);
            if (pList.size() == 0 || pList.get(0) == null) {
                List<StandardTree> treeList1 = standardTreeMapper.selectList(Wrappers.<StandardTree>lambdaQuery().eq(StandardTree::getLaboratory, trees[1]).eq(StandardTree::getSampleType, trees[2]));
                if (treeList1.size() == 0) {
                    treeList.add(standardTree);
                } else {
                    treeList.addAll(treeList1);
                }
            } else {
                for (ProductDto p : pList) {
                    standardTree.setSample(p.getName());
                    List<StandardTree> treeList1 = standardTreeMapper.selectList(Wrappers.<StandardTree>lambdaQuery().eq(StandardTree::getLaboratory, trees[1]).eq(StandardTree::getSampleType, trees[2]).eq(StandardTree::getSample, p.getName()));
                    if (treeList1.size() == 0) {
                        treeList.add(JSON.parseObject(JSON.toJSONString(standardTree), StandardTree.class));
                    } else {
                        treeList.addAll(treeList1);
                    }
                }
            }
            for (StandardTree standardTree2 : treeList) {
                String tree2 = trees[0] + " - " + trees[1] + " - " + trees[2] + " - " + standardTree2.getSample() + " - " + standardTree2.getModel();
                list.addAll(standardTreeMapper.selectStandardProductListByTree("\"" + trees[2] + "\"", standardTree2.getSample(), standardTree2.getModel(), tree2, trees[1]));
                list.addAll(standardTreeMapper.selectStandardProductListByTree2("\"" + trees[2] + "\",\"" + standardTree2.getSample() + "\"", standardTree2.getSample(), standardTree2.getModel(), tree2, trees[1]));
            }
        } else if (trees.length == 4) {
            // 判断第四层是否有型号
            Long count = standardProductListMapper.selectCount(Wrappers.<StandardProductList>lambdaQuery()
                    .eq(StandardProductList::getStandardMethodListId, id)
                    .isNull(StandardProductList::getModel)
                    .like(StandardProductList::getTree, tree));
            if (count == 0) {
                isDrag = true;
            }

            isDrag = true;
            List<StandardTree> treeList = standardTreeMapper.selectList(Wrappers.<StandardTree>lambdaQuery().eq(StandardTree::getLaboratory, trees[1]).eq(StandardTree::getSampleType, trees[2]).eq(StandardTree::getSample, trees[3]));
            if (treeList.size() == 0) {
                StandardTree standardTree = new StandardTree();
                standardTree.setFactory(trees[0]);
                standardTree.setLaboratory(trees[1]);
                standardTree.setSampleType(trees[2]);
                standardTree.setSample(trees[3]);
                treeList.add(standardTree);
            }
            for (StandardTree standardTree : treeList) {
                String str = tree + " - " + standardTree.getModel();
                list.addAll(standardTreeMapper.selectStandardProductListByTree("\"" + trees[2] + "\"", standardTree.getSample(), standardTree.getModel(), str, trees[1]));
                list.addAll(standardTreeMapper.selectStandardProductListByTree2("\"" + trees[2] + "\",\"" + trees[3] + "\"", standardTree.getSample(), standardTree.getModel(), str, trees[1]));
            }
        } else {
            isDrag = true;
            list.addAll(standardTreeMapper.selectStandardProductListByTree("\"" + trees[2] + "\"", trees[3].equals("null") ? null : trees[3], trees[4], tree, trees[1]));
            list.addAll(standardTreeMapper.selectStandardProductListByTree2("\"" + trees[2] + "\",\"" + trees[3] + "\"", trees[3].equals("null") ? null : trees[3], trees[4], tree, trees[1]));
        }
        for (StandardProductList productList : list) {
            productList.setId(IdWorker.getId());
        }
        List<StandardProductList> standardProductLists;

        if (isDrag) {
            standardProductLists = standardProductListMapper.selectList(Wrappers.<StandardProductList>lambdaQuery()
                    .eq(StandardProductList::getStandardMethodListId, id)
                    .like(StandardProductList::getTree, tree)
                    .orderByAsc(StandardProductList::getSort));
            // 判断是否有没有序号的, 没有序号重置
            boolean b = standardProductLists.stream().anyMatch(standardProductList -> standardProductList.getSort() == null);
        } else {
            standardProductLists = standardProductListMapper.selectList(Wrappers.<StandardProductList>lambdaQuery()
                    .eq(StandardProductList::getStandardMethodListId, id)
                    .like(StandardProductList::getTree, tree));
        }

        for (StandardProductList sp : standardProductLists) {
            for (StandardProductList pl : list) {
                // 判断条件是否只有一个, 有一个的话默认第一个
                String radiusList = pl.getRadiusList();
                if (StringUtils.isNotBlank(radiusList) && !radiusList.equals("null") && !radiusList.equals("\"\"")) {
                    JSONArray jsonArray = JSON.parseArray(radiusList);
                    List<String> radius = jsonArray.toJavaList(String.class);
                    if (CollectionUtils.isNotEmpty(radius) && radius.size() == 1) {
                        pl.setRadius(radius.get(0));
                    }
                }
                if (Objects.equals(sp.getInspectionItem(), pl.getInspectionItem())
                        && Objects.equals((sp.getInspectionItemSubclass() == null) ? "" : sp.getInspectionItemSubclass(), pl.getInspectionItemSubclass() == null ? "" : pl.getInspectionItemSubclass())
//                        && Objects.equals(sp.getSample(), pl.getSample())
                        && Objects.equals(sp.getModel(), pl.getModel())
                        && sp.getTree().indexOf(pl.getSample() == null ? "null" : pl.getSample()) > -1
                        && Objects.equals(sp.getStructureItemParameterId(), pl.getStructureItemParameterId())) {
                    pl.setId(sp.getId());
                    // 添加排序字段
                    pl.setSort(sp.getSort());
                    if (sp.getState() != null && !sp.getState().equals("")) {
                        pl.setState(sp.getState());
                    } else {
                        pl.setState(id == 0 ? 1 : 0);
                    }
                    if (sp.getMethodS() != null && !sp.getMethodS().equals("")) {
                        pl.setMethod(sp.getMethodS());
                    }
                    if (sp.getRadius() != null && !sp.getRadius().equals("")) {
                        pl.setRadius(sp.getRadius());
                    }
                    if (sp.getRates() != null && !sp.getRates().equals("")) {
                        pl.setRates(sp.getRates());
                    }
                    if (sp.getAsk() != null && !sp.getAsk().equals("")) {
                        pl.setAsk(sp.getAsk());
                    }
                    if (sp.getTell() != null && !sp.getTell().equals("")) {
                        pl.setTell(sp.getTell());
                    }
                    if (sp.getPrice() != null && !sp.getPrice().equals("")) {
                        pl.setPrice(sp.getPrice());
                    }
                    if (sp.getManHour() != null && !sp.getManHour().equals("")) {
                        pl.setManHour(sp.getManHour());
                    }
                    if (sp.getSection() != null && !sp.getSection().equals("")) {
                        pl.setSection(sp.getSection());
                    }
                    if (sp.getCores() != null && !sp.getCores().equals("")) {
                        pl.setCores(sp.getCores());
                    }
                    if (sp.getConductorMaterial() != null && !sp.getConductorMaterial().equals("")) {
                        pl.setConductorMaterial(sp.getConductorMaterial());
                    }
                    if (sp.getConductorType() != null && !sp.getConductorType().equals("")) {
                        pl.setConductorType(sp.getConductorType());
                    }
                    if (sp.getTemplateId() != null && !sp.getTemplateId().equals("")) {
                        pl.setTemplateId(sp.getTemplateId());
                    }
                    break;
                }
            }
        }

        Integer userId = SecurityUtils.getUserId().intValue();
        if (trees.length == 5) {
            standardProductListMapper.delete(Wrappers.<StandardProductList>lambdaUpdate()
                    .eq(StandardProductList::getStandardMethodListId, id)
                    .eq(StandardProductList::getTree, tree));
        } else {
            standardProductListMapper.delete(Wrappers.<StandardProductList>lambdaUpdate()
                    .eq(StandardProductList::getStandardMethodListId, id)
                    .like(StandardProductList::getTree, tree));
        }


        List<StandardProductList> productLists = list.stream().map(a -> {
            a.setFactory(trees[0]);
            a.setLaboratory(trees[1]);
            a.setSampleType(trees[2]);
            a.setCreateUser(userId);
            a.setUpdateUser(userId);
            a.setStandardMethodListId(id);
            return a;
        }).collect(Collectors.toList());
//            this.saveBatch(productLists);
        // 批量添加标准
        if (CollectionUtils.isNotEmpty(productLists)) {
            baseMapper.saveBatchProductLists(productLists);
        }


        Collections.sort(list, (o1, o2) -> {
            String field1 = o1.getManHourGroup();
            String field2 = o2.getManHourGroup();

            boolean isEmpty1 = field1 == null || field1.isEmpty();
            boolean isEmpty2 = field2 == null || field2.isEmpty();

            if (isEmpty1 && isEmpty2) {
                return 0;
            } else if (isEmpty1) {
                return 1;
            } else if (isEmpty2) {
                return -1;
            } else {
                int num1 = extractNumber(field1);
                int num2 = extractNumber(field2);
                return Integer.compare(num1, num2);
            }
        });
          // 按照索引排序
        if (isDrag) {
            list.sort((o1, o2) -> (o1.getSort() == null ? 0 : o1.getSort())
                    - (o2.getSort() == null ? 0 : o2.getSort()));
        }

        Map<String, Object> map = new HashMap<>();
        map.put("productList", list);
        map.put("total", list.size());
        return map;
    }

    private int extractNumber(String s) {
        // 从字符串中提取数字的逻辑，这里假设字段的格式是 "text<number>"
        String number = s;
        if (!s.matches("\\d+")) {
            number = s.replaceAll("\\D", "");
        }
        return Integer.parseInt(number);
    }

    @Override
    public IPage<StandardProductList> selectStandardProductByMethodId(Integer id, String tree, Integer page, String laboratory, String item, String items) {
        IPage<StandardProductList> iPage = new Page<>();
        iPage.setSize(100);
        iPage.setCurrent(page);
        return standardProductListMapper.standardProductListIPage(id, tree, iPage, laboratory, item, items);
    }

    @Override
    public Map<String, List<?>> selectStandardProductEnumByMethodId(Integer id, String tree, String item) {
        HashMap<String, List<?>> map = new HashMap<>();
        map.put("item", standardProductListMapper.selectList(Wrappers.<StandardProductList>lambdaQuery()
                .eq(StandardProductList::getStandardMethodListId, id)
                .like(StandardProductList::getTree, tree)
                .select(StandardProductList::getInspectionItem)
                .groupBy(StandardProductList::getInspectionItem)));
        if (ObjectUtils.isNotEmpty(item)) {
            map.put("items", standardProductListMapper.selectList(Wrappers.<StandardProductList>lambdaQuery()
                    .eq(StandardProductList::getStandardMethodListId, id)
                    .eq(StandardProductList::getInspectionItem, item)
                    .like(StandardProductList::getTree, tree)
                    .select(StandardProductList::getInspectionItemSubclass)
                    .groupBy(StandardProductList::getInspectionItemSubclass)));
        } else {
            map.put("items", standardProductListMapper.selectList(Wrappers.<StandardProductList>lambdaQuery()
                    .eq(StandardProductList::getStandardMethodListId, id)
                    .like(StandardProductList::getTree, tree)
                    .select(StandardProductList::getInspectionItemSubclass)
                    .groupBy(StandardProductList::getInspectionItemSubclass)));
        }
        return map;
    }

    /**
     * 修改标准库区间
     * @param list
     * @return
     */
    @Override
    public boolean updateSection(StandardProductList list) {
        standardProductListMapper.updateSection(list);
        return true;
    }


    /**
     * 标准库拖拽
     * @param resetTreeDragDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetTreeDrag(ResetTreeDragDTO resetTreeDragDTO) {
        Integer beginIndex = Integer.parseInt(resetTreeDragDTO.getBeginIndex());
        Integer endIndex = Integer.parseInt(resetTreeDragDTO.getEndIndex());
        Integer methodId = Integer.parseInt(resetTreeDragDTO.getMethodId());
        Long productionId = Long.parseLong(resetTreeDragDTO.getProductionId());
        // 判断是从上往下拖拽还是从下往上放拖拽
        // 从上往下
        if (beginIndex < endIndex) {
            standardProductListMapper.updateSortUp(beginIndex,
                    endIndex,
                    methodId,
                    resetTreeDragDTO.getTree());

            // 从下往上
        } else if (beginIndex > endIndex){
            standardProductListMapper.updateSortDown(beginIndex,
                    endIndex,
                    methodId,
                    resetTreeDragDTO.getTree());
        } else {
            return;
        }
        // 修改标准库项顺序
        standardProductListMapper.update(null, Wrappers.<StandardProductList>lambdaUpdate()
                .eq(StandardProductList::getId, productionId)
                .set(StandardProductList::getSort, endIndex));
    }

    @Override
    public void resetTreeDragBatch(List<StandardProductList> standardProductLists) {
        this.updateBatchById(standardProductLists);
    }

    /**
     * 检验项要求值对比
     * @param copyDto
     * @return
     */
    @Override
    public List<StandardProductList> copyStandardProductList(CopyStandardProductListDto copyDto) {
        List<StandardProductList> productLists = new ArrayList<>();

        // 对比检验项一样的填充要求值要求描述
        for (StandardProductList oldProductList : copyDto.getOldStandardProductList()) {
            String oldItemName = oldProductList.getInspectionItemClassEn()
                    + oldProductList.getInspectionItem()
                    + oldProductList.getInspectionItemSubclass();
            for (StandardProductList newProductList : copyDto.getNewStandardProductList()) {
                String newItemName = newProductList.getInspectionItemClassEn()
                        + newProductList.getInspectionItem()
                        + newProductList.getInspectionItemSubclass();
                // 判断名称是否一样
                if (oldItemName.equals(newItemName)) {
                    // 区间
                    oldProductList.setSection(newProductList.getSection());
                    // 芯数
                    oldProductList.setCores(newProductList.getCores());
                    // 要求值
                    oldProductList.setAsk(newProductList.getAsk());
                    // 要求描述
                    oldProductList.setTell(newProductList.getTell());
                    // 单价
                    oldProductList.setPrice(newProductList.getPrice());
                    // 工时分组
                    oldProductList.setManHour(newProductList.getManHour());
                    // 导体材质
                    oldProductList.setConductorMaterial(newProductList.getConductorMaterial());
                    // 导体类型
                    oldProductList.setConductorType(newProductList.getConductorType());
                    productLists.add(oldProductList);
                }
            }
        }
        return productLists;
    }

    /**
     * 检验项复制对比一个
     * @param dto
     * @return
     */
    @Override
    public List<StandardProductList> copyStandardProductOne(CopyStandardProductListDto dto) {
        if (CollectionUtils.isEmpty(dto.getNewStandardProductList()) && dto.getNewStandardProductList().size() == 1 &&
                CollectionUtils.isEmpty(dto.getOldStandardProductList()) && dto.getOldStandardProductList().size() == 1) {
            throw new BaseException("需要对比的检验项请选择一个");
        }
        StandardProductList newProductList = dto.getNewStandardProductList().get(0);
        StandardProductList oldProductList = dto.getOldStandardProductList().get(0);
        // 区间
        oldProductList.setSection(newProductList.getSection());
        // 芯数
        oldProductList.setCores(newProductList.getCores());
        // 要求值
        oldProductList.setAsk(newProductList.getAsk());
        // 要求描述
        oldProductList.setTell(newProductList.getTell());
        // 单价
        oldProductList.setPrice(newProductList.getPrice());
        // 工时分组
        oldProductList.setManHour(newProductList.getManHour());
        // 导体材质
        oldProductList.setConductorMaterial(newProductList.getConductorMaterial());
        // 导体类型
        oldProductList.setConductorType(newProductList.getConductorType());
        List<StandardProductList> productLists = new ArrayList<>();
        productLists.add(oldProductList);
        return productLists;
    }

    /**
     * 检验项复制排序
     * @param copyDto
     * @return
     */
    @Override
    public boolean copyStandardProductSort(CopyStandardProductListDto copyDto) {
        List<StandardProductList> productLists = new ArrayList<>();

        // 对比检验项一样的填充要求值要求描述
        for (StandardProductList oldProductList : copyDto.getOldStandardProductList()) {
            String oldItemName = oldProductList.getInspectionItemClassEn()
                    + oldProductList.getInspectionItem()
                    + oldProductList.getInspectionItemSubclass();
            for (StandardProductList newProductList : copyDto.getNewStandardProductList()) {
                String newItemName = newProductList.getInspectionItemClassEn()
                        + newProductList.getInspectionItem()
                        + newProductList.getInspectionItemSubclass();
                // 判断名称是否一样
                if (oldItemName.equals(newItemName)) {
                    StandardProductList standardProductList = new StandardProductList();
                    standardProductList.setId(oldProductList.getId());
                    // 复制排序
                    standardProductList.setSort(newProductList.getSort());
                    productLists.add(standardProductList);
                }
            }
        }
        this.updateBatchById(productLists);
        return false;
    }
}




