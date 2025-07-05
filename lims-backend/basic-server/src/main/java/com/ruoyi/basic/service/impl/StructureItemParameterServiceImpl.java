package com.ruoyi.basic.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.basic.mapper.StructureItemParameterMapper;
import com.ruoyi.basic.pojo.StandardTemplate;
import com.ruoyi.basic.pojo.StructureItemParameter;
import com.ruoyi.basic.service.StandardTemplateService;
import com.ruoyi.basic.service.StructureItemParameterService;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.system.service.ISysDictTypeService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StructureItemParameterServiceImpl extends ServiceImpl<StructureItemParameterMapper, StructureItemParameter> implements StructureItemParameterService {

    @Resource
    private StructureItemParameterMapper structureItemParameterMapper;
    @Resource
    private ISysDictTypeService dictTypeService;
    @Resource
    private StandardTemplateService standardTemplateService;

    /**
     *
     * @param file
     */
    @Override
    public void importEquipData(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        List<StructureItemParameter> lists = new ArrayList<>();
        AtomicReference<String> sample = new AtomicReference<>();
        ExcelUtil.readBySax(inputStream, -1, (i, l, list1) -> {
            if (l == 1) {
                sample.set(list1.get(1) + "");
            }
            if (l >= 1) {
                StructureItemParameter str = new StructureItemParameter();
                // 测试对象
                if (list1.get(1) == null) {
                    str.setSample(null);
                } else {
                    String brand = (String) list1.get(1);
                    StringBuilder builder = new StringBuilder();
                    builder.append("[");
                    // 产品
                    if (ObjectUtil.isNotEmpty(list1.get(2))) {
                        String production = (String) list1.get(2);
                        if (!production.contains("；")) {
                            builder.append(String.format("[\"%s\",\"%s\"]", brand, production));
                        } else {
                            Arrays.stream(production.split("；")).forEach(item -> {
                                builder.append(String.format("[\"%s\",\"%s\"],", brand, item));
                            });
                            builder.deleteCharAt(builder.length() - 1);
                        }
                    } else {
                        builder.append("[");
                        builder.append(String.format("\"%s\"", brand));
                        builder.append("]");
                    }
                    builder.append("]");
                    str.setSample(builder.toString());
                }
                // 检验项
                str.setInspectionItem(list1.get(4).toString().trim());
                // 检验项英文
                if (list1.get(5) != null) {
                    str.setInspectionItemEn(list1.get(5).toString());
                }
                // 检验子项
                if (list1.get(6) == null) {
                    str.setInspectionItemSubclass(null);
                } else {
                    str.setInspectionItemSubclass(list1.get(6).toString().trim());
                }
                // 检验子项英文
                if (list1.get(7) == null) {
                    str.setInspectionItemSubclassEn(null);
                } else {
                    str.setInspectionItemSubclassEn(String.valueOf(list1.get(7).toString()));
                }
                // 检验项分类
                if (list1.get(22) != null && list1.get(22) != "") {
                    str.setInspectionItemClass(list1.get(22).toString().trim());
                } else {
                    str.setInspectionItemClass(null);
                }
                // 检验项分类英文
                if (list1.get(23) != null && list1.get(23) != "") {
                    str.setInspectionItemClassEn(list1.get(23) + "");
                } else {
                    str.setInspectionItemClassEn(null);
                }

                LambdaQueryWrapper<StructureItemParameter> wrapper = Wrappers.lambdaQuery(StructureItemParameter.class)
                        .eq(StructureItemParameter::getInspectionItem, str.getInspectionItem())
                        .eq(StructureItemParameter::getSample, str.getSample())

                        .last("limit 1");
                // 判断是否有检验项类型
                if (ObjectUtils.isNotEmpty(str.getInspectionItemClass())) {
                    wrapper.eq(StructureItemParameter::getInspectionItemClass, str.getInspectionItemClass());
                }

                // 判断是否有检验子项
                if (ObjectUtils.isNotEmpty(str.getInspectionItemSubclass())) {
                    wrapper.eq(StructureItemParameter::getInspectionItemSubclass, str.getInspectionItemSubclass());
                }
                StructureItemParameter db_str = this.getOne(wrapper);
                if (ObjectUtils.isNotEmpty(db_str)) {
                    str.setId(db_str.getId());
                }
                // 方法名称
                if (list1.get(8) == null) {
                    str.setMethod(null);
                } else {
                    StringBuffer buffer = new StringBuffer();
                    String input = list1.get(8).toString();
                    buffer.append("[");
                    String[] values = input.split("；");
                    for (String value : values) {
                        buffer.append("\"").append(value.trim()).append("\",");
                    }
                    buffer.deleteCharAt(buffer.length() - 1);
                    buffer.append("]");
                    str.setMethod(buffer.toString());
                }
                // 试验室
                if (list1.get(9) == null) {
                    str.setSonLaboratory(null);
                } else {
                    str.setSonLaboratory(list1.get(9).toString());
                }
                // 计量单位
                if (list1.get(10) == null) {
                    str.setUnit(null);
                } else {
                    str.setUnit(list1.get(10).toString());
                }
                // 要求值
                if (list1.get(11) == null) {
                    str.setAskTell(null);
                } else {
                    str.setAskTell(list1.get(11).toString());
                }
                // 要求描述
                if (list1.get(12) == null) {
                    str.setAsk(null);
                } else {
                    str.setAsk(list1.get(12).toString());
                }
                // 单价
                if (list1.get(13) == null) {
                    str.setPrice(null);
                } else {
                    str.setPrice(list1.get(13) + "");
                }
                // 工时系数
                if (list1.get(14) == null) {
                    str.setManHour(null);
                } else {
                    str.setManHour(Double.valueOf(list1.get(14).toString()));
                }
                // 工时分组
                if (list1.get(15) == null) {
                    str.setManHourGroup(null);
                } else {
                    str.setManHourGroup(list1.get(15).toString());
                }
                // 预计完成时间
                if (list1.get(16) == null) {
                    str.setManDay(null);
                } else {
                    str.setManDay(Integer.valueOf(list1.get(16).toString()));
                }
                // 数据类型
                String jy;
                if (list1.get(17).toString().equals("非采集类型")) {
                    jy = "0";
                } else {
                    jy = "1";
                    // 绑定设备
                    if (list1.get(28) == null) {
                        str.setRates(null);
                    } else {
                        // 查询设备信息
                        List<String> managementNumberList = StrUtil.split(list1.get(28).toString(), '；');
                        if (CollectionUtils.isNotEmpty(managementNumberList)) {
                            List<Integer> deviceIds = structureItemParameterMapper.selectDeviceIdsByNumber(managementNumberList);
                            if (CollectionUtils.isNotEmpty(deviceIds)) {
                                str.setDeviceIds(CollUtil.join(deviceIds, ","));
                            }
                        }
                    }

                }
                str.setInspectionItemType(jy);
                // 检验项类型
                String validateValueType = list1.get(18).toString();
                if (ObjectUtils.isNotEmpty(validateValueType)) {
                    List<SysDictData> enums = dictTypeService.selectDictDataByName("检验值类型")
                            .stream().filter(sysDictData -> sysDictData.getDictLabel().equals(validateValueType)).collect(Collectors.toList());
                    str.setInspectionValueType(enums.get(0).getDictValue());
                }
                int bsm;
                //特殊标识
                if (list1.get(19).toString().equals("否")) {
                    bsm = 0;
                } else {
                    bsm = 1;
                }
                str.setBsm(bsm + "");
                // 数字字典
                if (list1.get(20) != null) {
                    str.setDic(list1.get(20) + "");
                } else {
                    str.setDic(null);
                }
                // 原始记录模板
                StandardTemplate standTempIdByName = standardTemplateService.getStandTempIdByName(String.valueOf(list1.get(21)));
                if (standTempIdByName != null) {
                    str.setTemplateId(standTempIdByName.getId());
                } else {
                    str.setTemplateId(null);
                }
                try {
                    if (list1.get(24) != null) {
                        str.setLaboratory(list1.get(24) + "");
                    }
                } catch (Exception e) {
                }

                // 条件
                if (list1.get(25) == null) {
                    str.setRadiusList(null);
                } else {
                    StringBuffer buffer = new StringBuffer();
                    String input = list1.get(25).toString();
                    buffer.append("[");
                    String[] values = input.split("；");
                    for (String value : values) {
                        buffer.append("\"").append(value.trim()).append("\",");
                    }
                    buffer.deleteCharAt(buffer.length() - 1);
                    buffer.append("]");
                    str.setRadiusList(buffer.toString());
                }
                //收费标准
                if (list1.get(26) == null) {
                    str.setRates(null);
                } else {
                    str.setRates(list1.get(26) + "");
                }
                // 抽样类型
                if (list1.get(27) != null){
                    String spotCheckType = list1.get(27).toString();
                    if (ObjectUtils.isNotEmpty(spotCheckType)) {
                        List<SysDictData> enums = dictTypeService.selectDictDataByName("抽检类型")
                                .stream().filter(sysDictData -> sysDictData.getDictLabel().equals(spotCheckType)).collect(Collectors.toList());
                        str.setSpotCheckType(enums.get(0).getDictValue());
                    }
                }else {
                    str.setSpotCheckType(null);
                }
                lists.add(str);
            }
        });
        try {
            this.saveOrUpdateBatch(lists);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("服务端报错");
        }
    }
}
