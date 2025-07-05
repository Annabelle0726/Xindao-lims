package com.ruoyi.basic.service.impl;

import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.basic.pojo.StandardMethodList;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.basic.mapper.StandardMethodMapper;
import com.ruoyi.basic.mapper.StandardProductListMapper;
import com.ruoyi.basic.mapper.StructureItemParameterMapper;
import com.ruoyi.basic.pojo.StandardMethod;
import com.ruoyi.basic.pojo.StandardProductList;
import com.ruoyi.basic.pojo.StructureItemParameter;
import com.ruoyi.basic.service.StandardMethodService;
import com.ruoyi.basic.service.StandardProductListService;
import com.ruoyi.basic.service.StructureItemParameterService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author Administrator
 * @description 针对表【standard_method(标准方法)】的数据库操作Service实现
 * @createDate 2024-03-03 19:21:41
 */
@Service
@AllArgsConstructor
public class StandardMethodServiceImpl extends ServiceImpl<StandardMethodMapper, StandardMethod>
        implements StandardMethodService {


    private StandardMethodMapper standardMethodMapper;

    private StandardProductListMapper standardProductListMapper;
    private StandardProductListService standardProductListService;

    private StructureItemParameterMapper structureItemParameterMapper;
    private StructureItemParameterService structureItemParameterService;

    @Override
    public IPage<StandardMethod> selectStandardMethodList(Page page, StandardMethod standardMethod) {
        return standardMethodMapper.selectStandardMethodList(page, QueryWrappers.queryWrappers(standardMethod));
    }

    @Override
    public List<StandardMethod> selectStandardMethods() {
        return standardMethodMapper.selectList(Wrappers.<StandardMethod>lambdaQuery().select(StandardMethod::getId, StandardMethod::getCode, StandardMethod::getName).ne(StandardMethod::getId, 0));
    }

    @Override
    public int addStandardMethod(StandardMethod standardMethod) {
        int insert = standardMethodMapper.insert(standardMethod);
        return insert;
    }

    @Override
    public int delStandardMethod(Integer id) {
        int i = standardMethodMapper.deleteById(id);
        return i;
    }

    @Override
    public int upStandardMethod(StandardMethod standardMethod) {
        StandardMethod oldStandardMethod = standardMethodMapper.selectById(standardMethod.getId());
        if (!oldStandardMethod.getCode().equals(standardMethod.getCode())) {
            CompletableFuture.supplyAsync(() -> replaceMethod(oldStandardMethod.getCode(), standardMethod.getCode()));
        }
        int i = standardMethodMapper.updateById(standardMethod);
        return i;
    }

    //编辑method后全部替换
    public String replaceMethod(String oldCode, String code) {
        //查询StandardProductList中所有Method如果包含之前的则替换
        List<StandardProductList> standardProductLists = standardProductListMapper.selectList(null);
        for (StandardProductList standardProductList : standardProductLists) {
            if (standardProductList.getMethod().contains(oldCode)) {
                String[] split = standardProductList.getMethod().split(",");
                String a = null;
                for (int i = 0; i < split.length; i++) {
                    String methodName = split[i].substring(1, split[i].length() - 1);
                    if (i == 0) {
                        methodName = split[i].substring(2, split[i].length() - 1);
                    } else if (i == split.length - 1) {
                        methodName = split[i].substring(1, split[i].length() - 2);
                    }
                    if (methodName.equals(oldCode)) {
                        methodName = code;
                    }
                    a += "\"" + methodName + "\",";
                }
                String method = "[\"" + a.substring(0, a.length() - 1) + "\"]";
                standardProductList.setMethod(method);
            }
        }
        standardProductListService.updateBatchById(standardProductLists);
        //查询StructureItemParameter中所有Method如果包含之前的则替换
        List<StructureItemParameter> structureItemParameters = structureItemParameterMapper.selectList(null);
        for (StructureItemParameter structureItemParameter : structureItemParameters) {
            if (structureItemParameter.getMethod().contains(oldCode)) {
                String[] split = structureItemParameter.getMethod().split(",");
                String a = null;
                for (int i = 0; i < split.length; i++) {
                    String methodName = split[i].substring(1, split[i].length() - 1);
                    if (i == 0) {
                        methodName = split[i].substring(2, split[i].length() - 1);
                    } else if (i == split.length - 1) {
                        methodName = split[i].substring(1, split[i].length() - 2);
                    }
                    if (methodName.equals(oldCode)) {
                        methodName = code;
                    }
                    a += "\"" + methodName + "\",";
                }
                String method = "[\"" + a.substring(0, a.length() - 1) + "\"]";
                structureItemParameter.setMethod(method);
            }
        }
        structureItemParameterService.updateBatchById(structureItemParameters);
        return "替换完毕!";
    }


    // 格式化数据
    public StandardMethod formatData(List<Object> list) {
        StandardMethod standardMethod = new StandardMethod();
        standardMethod.setField(list.get(1).toString());
        // 造格式
        List<List<Object>> structureTestObjectId = new ArrayList<>();
        if (ObjectUtils.isEmpty(list.get(3))) {
            structureTestObjectId.add(Arrays.asList(list.get(2)));
        } else {
            structureTestObjectId.add(Arrays.asList(list.get(2), list.get(3)));
        }
        standardMethod.setStructureTestObjectId(JSONUtil.toJsonStr(structureTestObjectId));
        standardMethod.setCode(list.get(4).toString());
        standardMethod.setName(list.get(5).toString());
        standardMethod.setNameEn(list.get(6).toString());
        if (!Objects.equals(list.get(7), null)) {
            standardMethod.setRemark(list.get(7).toString());
        }
        standardMethod.setQualificationId(list.get(8).toString());
        if (ObjectUtils.isNotEmpty(list.get(9))) {
            if (list.get(9).equals("是")) {
                standardMethod.setIsProduct(1);
            } else if (list.get(9).equals("否")) {
                standardMethod.setIsProduct(0);
            }
        }
        if (ObjectUtils.isNotEmpty(list.get(10))) {
            if (list.get(10).equals("是")) {
                standardMethod.setIsUse(1);
            } else if (list.get(9).equals("否")) {
                standardMethod.setIsUse(0);
            }
        }
        return standardMethod;
    }

    @Override
    public Map<String, List<?>> selectsStandardMethodByFLSSM(String tree) {
        String[] trees = tree.split(" - ");
        Map<String, List<?>> map = new HashMap<>();
        String str = "";
        List<StandardMethodList> standardMethodLists = new ArrayList<>();
        switch (trees.length){
            case 5:
                str += "\"" + trees[2] + "\",\"" + trees[3] + "\",\"" + trees[4] + "\"";
                standardMethodLists.addAll(standardMethodMapper.selectStandardMethodLists(str));
                standardMethodLists.addAll(standardMethodMapper.selectStandardMethodLists("\"" + trees[2] + "\",\"" + trees[3] + "\""));
                standardMethodLists.addAll(standardMethodMapper.selectStandardMethodLists("\"" + trees[2] + "\""));
                break;
            case 4:
                str += "\"" + trees[2] + "\",\"" + trees[3] + "\"";
                standardMethodLists.addAll(standardMethodMapper.selectStandardMethodLists(str));
                standardMethodLists.addAll(standardMethodMapper.selectStandardMethodLists("\"" + trees[2] + "\""));
                break;
            case 3:
                str += "\"" + trees[2] + "\"";
                standardMethodLists.addAll(standardMethodMapper.selectStandardMethodLists3(str));
                break;
            default:
                map.put("standardMethodList", null);
                return map;
        }
        standardMethodLists.addAll(standardMethodMapper.selectStandardMethodListsByNull(str));
        map.put("standardMethodList", standardMethodLists);
        return map;
    }

    @Override
    public List<StandardMethodList> selectStandardMethodEnum() {
        return standardMethodMapper.selectListEnum();
    }

}




