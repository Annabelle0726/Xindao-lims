package com.ruoyi.inspect.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ruoyi.basic.dto.IfsInventoryQuantitySupplierDto;
import com.ruoyi.common.constant.InsOrderTypeConstants;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.inspect.dto.DataAnalysisDto;
import com.ruoyi.inspect.dto.SampleProductRawAnalysisDto;
import com.ruoyi.inspect.mapper.DataAnalysisMapper;
import com.ruoyi.inspect.mapper.InsProductMapper;
import com.ruoyi.inspect.service.DataAnalysisService;
import com.ruoyi.inspect.vo.DeviationAnalyzeVo;
import com.ruoyi.inspect.vo.RawMaterialSupplierVo;
import com.ruoyi.inspect.vo.RawProductAnalysisVo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据分析
 *
 * @Author zhuo
 * @Date 2024/10/16
 */
@Service
@AllArgsConstructor
public class DataAnalysisServiceImpl implements DataAnalysisService {

    private DataAnalysisMapper dataAnalysisMapper;

    private InsProductMapper insProductMapper;

    /**
     * 查询原材料柱状统计
     * @return searchTime  时间
     * @return passRate  合格率
     * @return sum  总数
     */
    @Override
    public List<Map<String, Object>> getRawPassRateByBarChart(DataAnalysisDto dataAnalysisDto) {
        // 格式化字段, 避免报错
        this.formatDataAnalysisDto(dataAnalysisDto);
        // 获取当前日期
        DateTime now = DateUtil.date();
        if (StrUtil.isNotBlank(dataAnalysisDto.getBeginDate()) && StrUtil.isNotBlank(dataAnalysisDto.getEndDate())) {
            return dataAnalysisMapper.getRawPassRateByBarChartByDay(dataAnalysisDto);
        } else if (dataAnalysisDto.getDateType().equals("1")) {
            // 获取本周的开始时间（周一 00:00:00）
            DateTime beginOfWeek = DateUtil.beginOfWeek(now);
            // 获取本周的结束时间（周日 23:59:59）
            // 获取本周的结束时间（周日 23:59:59）
            DateTime endOfWeek = DateUtil.endOfWeek(now);
            dataAnalysisDto.setBeginDate(DateUtil.format(beginOfWeek, "yyyy-MM-dd HH:mm:ss"));
            dataAnalysisDto.setEndDate(DateUtil.format(endOfWeek, "yyyy-MM-dd HH:mm:ss"));
            return dataAnalysisMapper.getRawPassRateByBarChartByWeek(dataAnalysisDto);
        } else if (dataAnalysisDto.getDateType().equals("2")) {
            // 获取当前月的开始时间（每月1号 00:00:00）
            DateTime beginOfMonth = DateUtil.beginOfMonth(now);
            // 获取当前月的结束时间（本月最后一天 23:59:59）
            DateTime endOfMonth = DateUtil.endOfMonth(now);
            dataAnalysisDto.setBeginDate(DateUtil.format(beginOfMonth, "yyyy-MM-dd HH:mm:ss"));
            dataAnalysisDto.setEndDate(DateUtil.format(endOfMonth, "yyyy-MM-dd HH:mm:ss"));
            return dataAnalysisMapper.getRawPassRateByBarChartByDay(dataAnalysisDto);
        } else if (dataAnalysisDto.getDateType().equals("3")) {
            // 获取当前年的开始时间（每年1月1日 00:00:00）
            DateTime beginOfYear = DateUtil.beginOfYear(now);
            // 获取当前年的结束时间（每年12月31日 23:59:59）
            DateTime endOfYear = DateUtil.endOfYear(now);
            dataAnalysisDto.setBeginDate(DateUtil.format(beginOfYear, "yyyy-MM-dd HH:mm:ss"));
            dataAnalysisDto.setEndDate(DateUtil.format(endOfYear, "yyyy-MM-dd HH:mm:ss"));
            return dataAnalysisMapper.getRawPassRateByBarChartByYear(dataAnalysisDto);
        }

        return null;
    }

    /**
     * 查询原材料饼状图
     * @param dataAnalysisDto
     * @return
     * sum          : 总数
     * unQualified  : 不合格数量
     * qualified  : 合格数量
     * passRate  : 合格率
     */
    @Override
    public Map<String, Object> getRawPassRateByCake(DataAnalysisDto dataAnalysisDto) {
        // 格式化字段, 避免报错
        this.formatDataAnalysisDto(dataAnalysisDto);
        // 获取当前日期
        DateTime now = DateUtil.date();
        if (StrUtil.isNotBlank(dataAnalysisDto.getBeginDate()) && StrUtil.isNotBlank(dataAnalysisDto.getEndDate())) {

        } else if (dataAnalysisDto.getDateType().equals("1")) {
            // 获取本周的开始时间（周一 00:00:00）
            DateTime beginOfWeek = DateUtil.beginOfWeek(now);
            // 获取本周的结束时间（周日 23:59:59）
            DateTime endOfWeek = DateUtil.endOfWeek(now);
            dataAnalysisDto.setBeginDate(DateUtil.format(beginOfWeek, "yyyy-MM-dd HH:mm:ss"));
            dataAnalysisDto.setEndDate(DateUtil.format(endOfWeek, "yyyy-MM-dd HH:mm:ss"));
        } else if (dataAnalysisDto.getDateType().equals("2")) {
            // 获取当前月的开始时间（每月1号 00:00:00）
            DateTime beginOfMonth = DateUtil.beginOfMonth(now);
            // 获取当前月的结束时间（本月最后一天 23:59:59）
            DateTime endOfMonth = DateUtil.endOfMonth(now);
            dataAnalysisDto.setBeginDate(DateUtil.format(beginOfMonth, "yyyy-MM-dd HH:mm:ss"));
            dataAnalysisDto.setEndDate(DateUtil.format(endOfMonth, "yyyy-MM-dd HH:mm:ss"));
        } else if (dataAnalysisDto.getDateType().equals("3")) {
            // 获取当前年的开始时间（每年1月1日 00:00:00）
            DateTime beginOfYear = DateUtil.beginOfYear(now);
            // 获取当前年的结束时间（每年12月31日 23:59:59）
            DateTime endOfYear = DateUtil.endOfYear(now);
            dataAnalysisDto.setBeginDate(DateUtil.format(beginOfYear, "yyyy-MM-dd HH:mm:ss"));
            dataAnalysisDto.setEndDate(DateUtil.format(endOfYear, "yyyy-MM-dd HH:mm:ss"));
        }
        return dataAnalysisMapper.getRawPassRateByCake(dataAnalysisDto);
    }


    /**
     * 查询检验项名称
     * @param dataAnalysisDto
     * @return
     */
    @Override
    public List<String> getRawItemNames(DataAnalysisDto dataAnalysisDto) {
        // 格式化字段, 避免报错
        this.formatDataAnalysisDto(dataAnalysisDto);
        List<IfsInventoryQuantitySupplierDto> analysisList = dataAnalysisMapper.getRawProductAnalysisAllSample(dataAnalysisDto);
        // 根据样品id查询检测参数
        if (CollectionUtils.isEmpty(analysisList)) {
            return null;
        }
        List<Integer> sampleIds = analysisList.stream().map(IfsInventoryQuantitySupplierDto::getSampleId).collect(Collectors.toList());
        // 查询检验项目

        return insProductMapper.selectItemNameBySampleIds(sampleIds);
    }

    /**
     * 查询原材料项检分析
     * @param dataAnalysisDto
     * @return
     */
    @Override
    public RawProductAnalysisVo getRawProductAnalysis(DataAnalysisDto dataAnalysisDto) {
        // 格式化字段, 避免报错
        this.formatDataAnalysisDto(dataAnalysisDto);
        List<IfsInventoryQuantitySupplierDto> analysisList = dataAnalysisMapper.getRawProductAnalysisAllSample(dataAnalysisDto);
        // 根据样品id查询检测参数
        if (CollectionUtils.isEmpty(analysisList)) {
            return null;
        }
        List<Integer> sampleIds = analysisList.stream().map(IfsInventoryQuantitySupplierDto::getSampleId).collect(Collectors.toList());
        // 查询检验项目
        List<SampleProductRawAnalysisDto> sampleProductList = insProductMapper.selectListBySampleIds(sampleIds);

        List<String> itemNames = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dataAnalysisDto.getItemNames())) {
            itemNames = dataAnalysisDto.getItemNames();
        } else {
            itemNames = insProductMapper.selectItemNameBySampleIds(sampleIds);
        }

        // 根据样品分组
        List<Map<String, Object>> productList = new ArrayList<>();

        Map<Integer, List<SampleProductRawAnalysisDto>> collect = sampleProductList.stream().collect(Collectors.groupingBy(SampleProductRawAnalysisDto::getInsSampleId));
        List<String> finalItemNames = itemNames;
        collect.forEach((integer, rawAnalysisDtos) -> {
            // 检验项目名称转换成map
            Map<String, Object> itemMap = new HashMap<>();
            for (String itemName : finalItemNames) {
                itemMap.put(itemName, null);
            }
            // 遍历检验项
            for (SampleProductRawAnalysisDto rawAnalysisDto : rawAnalysisDtos) {
                itemMap.put(rawAnalysisDto.getInspectionItem(), rawAnalysisDto.getLastValue());
            }
            itemMap.put("product", rawAnalysisDtos.get(0).getSampleCode());
            productList.add(itemMap);
        });

        RawProductAnalysisVo rawProductAnalysisVo = new RawProductAnalysisVo();
        rawProductAnalysisVo.setItemNames(itemNames);
        rawProductAnalysisVo.setProductList(productList);

        return rawProductAnalysisVo;
    }

    /**
     * 查询检测项分析列表
     * @param dataAnalysisDto
     * @return
     */
    @Override
    public List<IfsInventoryQuantitySupplierDto> getRawProductAnalysisAllList(DataAnalysisDto dataAnalysisDto) {
        // 格式化字段, 避免报错
        this.formatDataAnalysisDto(dataAnalysisDto);
        return dataAnalysisMapper.getRawProductAnalysisList(dataAnalysisDto);
    }

    /**
     * 查询项检分析合格率
     * @param dataAnalysisDto
     * @return
     */
    @Override
    public RawProductAnalysisVo getRawProductAnalysisRawPass(DataAnalysisDto dataAnalysisDto) {
        // 格式化字段, 避免报错
        this.formatDataAnalysisDto(dataAnalysisDto);
        List<IfsInventoryQuantitySupplierDto> analysisList = dataAnalysisMapper.getRawProductAnalysisAllSample(dataAnalysisDto);
        // 根据样品id查询检测参数
        if (CollectionUtils.isEmpty(analysisList)) {
            return null;
        }
        List<Integer> sampleIds = analysisList.stream().map(IfsInventoryQuantitySupplierDto::getSampleId).collect(Collectors.toList());
        // 查询检验项目
        List<SampleProductRawAnalysisDto> sampleProductList = insProductMapper.selectListBySampleIds(sampleIds);
        List<String> itemNames = new ArrayList<>();
        List<String> finalItemNames = itemNames;
        if (CollectionUtils.isNotEmpty(dataAnalysisDto.getItemNames())) {
            itemNames = dataAnalysisDto.getItemNames();
        } else {
            itemNames = insProductMapper.selectItemNameBySampleIds(sampleIds);
        }
        // 分组
        List<Map<String, Object>> productList = new ArrayList<>();

        Map<String, List<SampleProductRawAnalysisDto>> groupList = new HashMap<>();

        switch (dataAnalysisDto.getGroupType()) {
            case "0":  // 默认样品区分
                groupList = sampleProductList.stream()
                        .collect(Collectors.groupingBy(SampleProductRawAnalysisDto::getSample));

                break;
            case "1":  // 批次号区分
                groupList = sampleProductList.stream()
                        .collect(Collectors.groupingBy(SampleProductRawAnalysisDto::getUpdateBatchNo));

                break;
            case "2":  // 厂家区分
                groupList = sampleProductList.stream()
                        .collect(Collectors.groupingBy(SampleProductRawAnalysisDto::getSupplierName));

                break;
        }

        groupList.forEach((groupName, rawAnalysisDtos) -> {
            // 按照检验项区分
            // 检验项目名称转换成map
            Map<String, Object> itemMap = new HashMap<>();

            for (String itemName : finalItemNames) {
                itemMap.put(itemName, null);
            }

            Map<String, List<SampleProductRawAnalysisDto>> groupItemList = rawAnalysisDtos.stream().collect(Collectors.groupingBy(SampleProductRawAnalysisDto::getInspectionItem));


            groupItemList.forEach((itemName, dtos) -> {
                // 统计合格总数
                long qualifiedCount = dtos.stream()
                        .filter(dto -> dto.getInsResult() != null && dto.getInsResult().equals(1))
                        .count();
                long totalCount = dtos.size();
                BigDecimal passRate = new BigDecimal(qualifiedCount).divide(new BigDecimal(totalCount), 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal(100))
                        .setScale(2, RoundingMode.HALF_UP);
                itemMap.put(itemName, passRate);
            });

            itemMap.put("product", groupName);
            productList.add(itemMap);
        });


        RawProductAnalysisVo rawProductAnalysisVo = new RawProductAnalysisVo();
        rawProductAnalysisVo.setItemNames(itemNames);
        rawProductAnalysisVo.setProductList(productList);

        return rawProductAnalysisVo;
    }

    @Override
    public DeviationAnalyzeVo getRawSupplierCompare(DataAnalysisDto dataAnalysisDto) {
        // 判断订单id是否有五个
        if (CollectionUtils.isEmpty(dataAnalysisDto.getOrderIds()) || dataAnalysisDto.getOrderIds().size() != 5) {
            throw new ErrorException("请选择五条数据");
        }
        if (CollectionUtils.isEmpty(dataAnalysisDto.getItemNames()) || dataAnalysisDto.getItemNames().size() != 1) {
            throw new ErrorException("请选择一个检验项检验项");
        }
        if (CollectionUtils.isNotEmpty(dataAnalysisDto.getSupplierDataList())) {
            if (dataAnalysisDto.getSupplierDataList().size() != 5) {
                throw new ErrorException("厂家数据请输入完整5条");
            }
        }

        List<RawMaterialSupplierVo> rawMaterialSupplierVoList = dataAnalysisMapper.getItemValueByOrderIds(dataAnalysisDto.getOrderIds(), dataAnalysisDto.getItemNames().get(0));
        // 判断厂家材料名称规格型号是否是一样的
        RawMaterialSupplierVo rawMaterialSupplierVo = rawMaterialSupplierVoList.get(0);
        for (RawMaterialSupplierVo materialSupplierVo : rawMaterialSupplierVoList) {
            if (!materialSupplierVo.getSupplierName().equals(rawMaterialSupplierVo.getSupplierName()) ||
                    !materialSupplierVo.getSample().equals(rawMaterialSupplierVo.getSample()) ||
                    !materialSupplierVo.getModel().equals(rawMaterialSupplierVo.getModel())) {
                throw new ErrorException("选择的检验信息不匹配");
            }
        }

        List<String> lastValues = rawMaterialSupplierVoList.stream().map(RawMaterialSupplierVo::getLastValue).collect(Collectors.toList());

        // 计算本地数据
        Map<String, List<Object>> localData = dataCompute(lastValues);


        DeviationAnalyzeVo deviationAnalyzeVo = new DeviationAnalyzeVo();
        deviationAnalyzeVo.setLocalData(localData.get("data"));
        deviationAnalyzeVo.setLocalULC(localData.get("uclData"));
        deviationAnalyzeVo.setLocalLCL(localData.get("lclData"));
        deviationAnalyzeVo.setLocalAverage(localData.get("averageData"));
        deviationAnalyzeVo.setLocalRange(localData.get("rangeData"));


        // 判断是否有厂家数据
        if (CollectionUtils.isNotEmpty(dataAnalysisDto.getSupplierDataList())) {
            List<String> supplierValues = dataAnalysisDto.getSupplierDataList();
            List<Object> absoluteDeviation = new ArrayList<>();
            List<Object> averageList = new ArrayList<>();

            for (int i = 0; i < lastValues.size(); i++) {
                // 判断有值是否为空
                if (StringUtils.isNotBlank(lastValues.get(i)) ||
                        StringUtils.isNotBlank(supplierValues.get(i))) {
                    BigDecimal laseValue = new BigDecimal(lastValues.get(i));
                    BigDecimal supplierValue = new BigDecimal(supplierValues.get(i));

                    // 计算 (B3 - B2)
                    BigDecimal result = laseValue.subtract(supplierValue)
                            .divide(laseValue, 10, RoundingMode.HALF_UP)
                            .abs()
                            .setScale(2, RoundingMode.HALF_UP);

                    absoluteDeviation.add(result);
                } else {
                    absoluteDeviation.add(null);
                }
            }
            // 计算平均值
            List<String> stringList = absoluteDeviation.stream()
                    .map(obj -> obj == null ? "null" : obj.toString())
                    .collect(Collectors.toList());
            BigDecimal average = computeAverage(stringList);
            absoluteDeviation.add(average);
            for (int i = 0; i < 5; i++) {
                averageList.add(average);
            }

            // 计算厂家数据
            Map<String, List<Object>> supplierData = dataCompute(supplierValues);
            deviationAnalyzeVo.setSupplierData(supplierData.get("data"));
            deviationAnalyzeVo.setSupplierULC(supplierData.get("uclData"));
            deviationAnalyzeVo.setSupplierLCL(supplierData.get("lclData"));
            deviationAnalyzeVo.setSupplierAverage(supplierData.get("averageData"));
            deviationAnalyzeVo.setSupplierRange(supplierData.get("rangeData"));
            deviationAnalyzeVo.setAbsoluteDeviation(absoluteDeviation);
            deviationAnalyzeVo.setAverage(averageList);
        }


        return deviationAnalyzeVo;
    }

    /**
     * 查询本月与上月合格率对比
     * @param dataAnalysisDto
     * @return
     */
    @Override
    public List<Map<String, Object>> getRawUpMonth(DataAnalysisDto dataAnalysisDto) {
        return dataAnalysisMapper.getRawUpMonth();
    }

    /**
     * 查询检验项类型饼图
     * @param dataAnalysisDto
     * @return
     */
    @Override
    public Map<String, Object> getOrderTypeCookie(DataAnalysisDto dataAnalysisDto) {
        return dataAnalysisMapper.getOrderTypeCookie();
    }

    /**
     * 计算返回数据
     * @param lastValues
     * @return
     */
    private static Map<String, List<Object>> dataCompute(List<String> lastValues) {
        // 平均值
        BigDecimal average = computeAverage(lastValues);

        // 标准偏差
        BigDecimal standardDeviation = computeStandardDeviation(lastValues);

        // 相对偏差
        BigDecimal relativeDeviation = standardDeviation.divide(average, 2, RoundingMode.HALF_UP);

        // 平均相对偏差
        BigDecimal sqrt5 = BigDecimal.valueOf(Math.sqrt(5));
        BigDecimal averageRelativeDeviation = standardDeviation.divide(sqrt5, 2, RoundingMode.HALF_UP);

        // ucl
        BigDecimal ucl = average.add(new BigDecimal("3").multiply(standardDeviation)).setScale(2, RoundingMode.HALF_UP);

        // lcl
        BigDecimal lcl = average.subtract(new BigDecimal("3").multiply(standardDeviation)).setScale(2, RoundingMode.HALF_UP);

        // 极差
        BigDecimal range = computeRange(lastValues);

        // 数据拼接
        // 检测数据
        List<Object> data = new ArrayList<>();
        for (String lastValue : lastValues) {
            data.add(lastValue);
        }
        data.add(average);
        data.add(standardDeviation);
        data.add(relativeDeviation);
        data.add(averageRelativeDeviation);

        //ucl
        List<Object> uclData = new ArrayList<>();

        //lcl
        List<Object> lclData = new ArrayList<>();

        //平均值
        List<Object> averageData = new ArrayList<>();

        //极差
        List<Object> rangeData = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            uclData.add(ucl);
            lclData.add(lcl);
            averageData.add(average);
            rangeData.add(range);
        }
        Map<String, List<Object>> map = new HashMap<>();
        map.put("data", data);
        map.put("uclData", uclData);
        map.put("lclData", lclData);
        map.put("averageData", averageData);
        map.put("rangeData", rangeData);

        return map;
    }


    /**
     * 计算极差
     * @param lastValues
     * @return
     */
    public static BigDecimal computeRange(List<String> lastValues) {
        int count = 0;

        BigDecimal min = null;
        BigDecimal max = null;

        for (String value : lastValues) {
            if (StrUtil.isNotBlank(value)) {
                BigDecimal bigDecimalValue = new BigDecimal(value);

                if (min == null || bigDecimalValue.compareTo(min) < 0) {
                    min = bigDecimalValue;
                }

                if (max == null || bigDecimalValue.compareTo(max) > 0) {
                    max = bigDecimalValue;
                }
                count++;
            }

        }

        if (count == 0 || min == null || max == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal range = max.subtract(min);

        return range.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 计算平均值
     * @param values
     * @return
     */
    private static BigDecimal computeAverage(List<String> values) {
        BigDecimal sum = BigDecimal.ZERO;
        int count = 0;

        for (String value : values) {
            if (StrUtil.isNotBlank(value)) {

                BigDecimal number = new BigDecimal(value);
                sum = sum.add(number);
                count++;

            }
        }

        return count == 0 ? BigDecimal.ZERO : sum.divide(BigDecimal.valueOf(count), 2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 计算标准偏差
     * @return
     */
    private static BigDecimal computeStandardDeviation(List<String> lastValues) {
        int count = 0;

        // 将字符串转换为 BigDecimal 列表
        List<BigDecimal> values = new ArrayList<>();
        for (String value : lastValues) {
            if (StrUtil.isNotBlank(value)) {
                values.add(new BigDecimal(value));
                count++;
            }
        }

        if (count == 0) {
            return BigDecimal.ZERO;
        }

        // 计算平均值
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal value : values) {
            sum = sum.add(value);
        }
        BigDecimal mean = sum.divide(new BigDecimal(values.size()), MathContext.DECIMAL128);

        // 计算每个数值与平均值的差的平方
        BigDecimal squaredDifferenceSum = BigDecimal.ZERO;
        for (BigDecimal value : values) {
            BigDecimal difference = value.subtract(mean);
            BigDecimal squaredDifference = difference.multiply(difference);
            squaredDifferenceSum = squaredDifferenceSum.add(squaredDifference);
        }

        // 计算标准差，注意这里使用的是样本标准差计算公式
        BigDecimal variance = squaredDifferenceSum.divide(new BigDecimal(values.size() - 1), MathContext.DECIMAL128);
        BigDecimal stddev = sqrt(variance);

        // 保留两位小数
        return stddev.setScale(2, RoundingMode.HALF_UP);
    }

    private static BigDecimal sqrt(BigDecimal value) {
        // 使用 Heron's method 计算平方根
        BigDecimal x = value;
        BigDecimal tolerance = new BigDecimal("1E-10");
        BigDecimal guess = value.divide(BigDecimal.valueOf(2), MathContext.DECIMAL128);

        while (x.subtract(guess).abs().compareTo(tolerance) > 0) {
            x = guess;
            guess = x.add(value.divide(x, MathContext.DECIMAL128)).divide(new BigDecimal("2"), MathContext.DECIMAL128);
        }

        return guess;
    }

    /**
     * *****格式化字段****
     * @param dataAnalysisDto
     */
    private void formatDataAnalysisDto(DataAnalysisDto dataAnalysisDto) {
        // 格式话字段, 避免报错
        if (StrUtil.isBlank(dataAnalysisDto.getOrderType())) {
            dataAnalysisDto.setOrderType("1");
        }
        if (dataAnalysisDto.getOrderType().equals("2")) {
            // 季度检验
            dataAnalysisDto.setOrderType(InsOrderTypeConstants.QUARTERLY_TEST);
        } else {
            // 进厂检验
            dataAnalysisDto.setOrderType(InsOrderTypeConstants.ENTER_THE_FACTORY);
        }
        if (StrUtil.isBlank(dataAnalysisDto.getDateType())) {
            dataAnalysisDto.setDateType("1");
        }
        if (StrUtil.isBlank(dataAnalysisDto.getGroupType())) {
            dataAnalysisDto.setGroupType("0");
        }
    }

}
