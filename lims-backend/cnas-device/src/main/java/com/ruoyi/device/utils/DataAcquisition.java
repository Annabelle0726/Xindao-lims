package com.ruoyi.device.utils;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ruoyi.device.pojo.DataConfig;
import com.ruoyi.device.pojo.Device;
import com.ruoyi.framework.exception.ErrorException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DataAcquisition {

    private static final String HTTP = "http://";

    private static final String GETFILE = ":9527/lims/getFile"; // 获取文件接口

    private static final String MOVEFILE = ":9527/lims/moveFile"; // 文件移动地址

    private static final String splitIdentifier = "@-@"; // 自定义唯一标识分割符

    public static final String frequency = "frequency";

    /**
     * 数采入口
     *
     * @param dataConfig
     * @param device
     * @return
     */
    public static Map<String, Object> dataAcquisitionEntrance(List<DataConfig> dataConfig, Device device, String entrustCode, String sampleCode, String ip, String cableTag) {
        // 判断是否是影像测量仪
        if (device.getManagementNumber().equals("JCZX-ZB-OP07001")) {
            if (device.getFileType().equals(".xlsx")) {
                String url = device.getCollectUrl() + "\\" + sampleCode.replace("/", "");
                if (StringUtils.isNotBlank(dataConfig.get(0).getAnotherName())) {
                    url += dataConfig.get(0).getAnotherName() ;
                }
                url += ".xlsx";
                device.setCollectUrl(url);
            }
        }
        // 判断是否是电缆燃烧烟密度测量系统
        if (device.getManagementNumber().equals("JCZX-ZB-ZT03002")) {
            if (device.getFileType().equals(".txt")) {
                device.setCollectUrl(device.getCollectUrl() + "\\-" + sampleCode.replace("/", ""));
            }
        }
        /**
         * filePath 文件采集路径
         * fileExtension 文件后缀
         * entrustCode 委托编号
         * sampleCode 样品编号
         * mdbEntrustCode mdb文件需要：委托编号字段 为什么没有去这个mdb前缀呢？因为已经给客户的部分电脑上安装了采集器，而用户不接受重新安装采集器，所以就没有去除
         * mdbSampleCode mdb文件需要：样品编号字段
         */
        String http = HTTP + ip + GETFILE +
                "?filePath=" + device.getCollectUrl() +
                "&fileExtension=" + device.getFileType() +
                "&entrustCode=" + entrustCode +
                "&sampleCode=" + sampleCode +
                "&mdbEntrustCode=" + device.getEntrustCode() +
                "&mdbSampleCode=" + device.getSampleCode() +
                "&dbFileName=" + device.getDbFileName();
        String result = null;
        try {
            result = HttpUtil.get(http);
        } catch (IORuntimeException e) {
            e.printStackTrace();
            throw new ErrorException("所在电脑未安装或未启动：LIMS文件采集器！");
        }
        JSONObject jsonObject = JSON.parseObject(result);
        if (Objects.equals(jsonObject.get("code"), 1)) {
            if (ObjectUtils.isEmpty(jsonObject.get("msg"))) {
                throw new ErrorException("未查询到文件！可能该路径（" + device.getCollectUrl() + "）下并没有所需(" + device.getFileType() +")文件！");
            } else {
                throw new ErrorException(jsonObject.get("msg") + "");
            }
        } else {
            String data = jsonObject.get("data") + "";
            // 考虑到一个检测项可能会存在多个数采配置，所以需要进行分组
            Map<String, List<DataConfig>> userMap = dataConfig.stream()
                    .peek(i -> {
                        String itemName = i.getInspectionItem();
                        if (StringUtils.isNotBlank(i.getInspectionItemClass())) {
                            itemName += "@" + i.getInspectionItemClass();
                        }
                        String name = i.getInspectionItem().equals(i.getInspectionItemSubclass()) ? itemName + "," : itemName + "," + i.getInspectionItemSubclass();

                        // 添加检验项名称
                        i.setInsProductItem(name);
                    })
                    .collect(Collectors.groupingBy(DataConfig::getInsProductItem));
            Map<String, Object> map;
            switch (device.getFileType()) {
                case ".docx":
                    map = analysisString(data, userMap, device, entrustCode, sampleCode);
                    break;
                case ".xlsx":
                    map = analysisList(data, userMap, device, entrustCode, sampleCode);
                    break;
                case ".txt":
                    map = analysisTxt(data, userMap, device, entrustCode, sampleCode);
                    break;
                case ".csv":
                    map = analysisList(data, userMap, device, entrustCode, sampleCode);
                    break;
                case ".mdb":
                    // 判断是否是拉力机
                    if (device.getManagementNumber().equals("JCZX-ZB-FF01014")) {
                        map = analysisMdbByPull(data, userMap, device, cableTag);
                    } else {
                        map = analysisMdb(data, userMap, device);
                    }
                    break;
                case ".db":
                    map = analysisDb(data, userMap, device);
                    break;
                case ".png":
                    map = readPngString(data, userMap, device);
                    break;
                default:
                    map = null;
                    break;
            }
            // 如果存在存储地址，则移动地址
            if (ObjectUtils.isNotEmpty(device.getStorageUrl())) {
                String s = HTTP + ip + MOVEFILE + "?startFilePath=" + device.getCollectUrl() + "&endFilePath=" + device.getStorageUrl() + "&fileType=" + device.getFileType();
                HttpUtil.get(s);
            }
            return map;
        }
    }

    public static Map<String, Object> createFrequency(String entrustCode, String sampleCode, Map<String, Object> map) {
        Set<String> set = new LinkedHashSet<>();
        map.forEach((key, value) -> {
            String[] split = key.split(",");
            String inspectionItem = split[0];
            // 只要有一个不为空就set进去
            if (ObjectUtils.isNotEmpty(value)) {
                set.add(inspectionItem);
            }
        });
        Map<String, Object> result = new HashMap<>();
        for (String inspectionItemKey : set) {
            Map<String, Object> hashMap = new HashMap<>();
            map.forEach((key, value) -> {
                String[] split = key.split(",");
                String inspectionItem = split[0];
                if (inspectionItemKey.equals(inspectionItem)) {
                    if (split.length > 1) {
                        hashMap.put(split[1], value);
                    } else {
                        hashMap.put("", value);
                    }
                }
            });
            String frequency = createKey(entrustCode, sampleCode, inspectionItemKey);
            hashMap.put("frequency", frequency);
            result.put(inspectionItemKey, hashMap);
        }
        return result;
    }

    public static String createKey(String entrustCode, String sampleCode, String inspectionItemKey) {

        return "1";
    }

    /**
     * 需要通过X,Y轴定位
     *
     * @param data
     * @param dataConfig
     * @return
     */
    private static Map<String, Object> analysisDb(String data, Map<String, List<DataConfig>> dataConfig, Device device) {
        JSONObject jsonObject = JSON.parseObject(data);
        Map<String, Object> map = new HashMap<>();
        if (jsonObject.isEmpty()) {
            return map;
        }
        JSONArray dataList = JSONArray.parseArray(jsonObject.get("data").toString());
        dataConfig.forEach((k, v) -> {
            AtomicInteger numberOfDataEntries = new AtomicInteger();
            List<Object> list = new ArrayList<>();
            for (int config = 0; config < v.size(); config++) {
                String refery = getRefer(v.get(config).getRefery());
                for (int i = 0; i < dataList.size(); i++) {
                    JSONObject jsonObject1 = JSON.parseObject(dataList.get(i).toString());
                    Object o = jsonObject1.get(refery);
                    if (ObjectUtils.isNotEmpty(o)) {
                        numberOfDataEntries.addAndGet(1);
                        list.add(o);
                    }
                }
            }
            // 拼接数采配置
            List<Object> result = new ArrayList<>();
            for (int i = 0; i < numberOfDataEntries.get(); i++) {
                String aggregate = "";
                for (int j = 0; j < v.size(); j++) {
                    int index;
                    if (j == 0) {
                        index = i;
                    } else {
                        index = numberOfDataEntries.get() + i;
                    }
                    aggregate += list.get(index).toString() + ",";
                }
                int lastIndex = aggregate.lastIndexOf(",");
                String substring = aggregate.substring(0, lastIndex);
                result.add(substring);
            }
            // 进行公式计算
            Object resultValue = calculationFormula(result, v.get(0), k, device);
            map.put(k, resultValue);
        });
        return map;
    }

    /**
     * @param data
     * @param dataConfig
     * @return
     */
    private static Map<String, Object> analysisMdb(String data, Map<String, List<DataConfig>> dataConfig, Device device) {
        JSONObject jsonObject = JSON.parseObject(data);
        Map<String, Object> map = new HashMap<>();
        if (jsonObject.isEmpty()) {
            return map;
        }
        JSONArray dataList = JSONArray.parseArray(jsonObject.get("data").toString());
        dataConfig.forEach((k, v) -> {
            DataConfig configVo = v.get(0);
            AtomicInteger numberOfDataEntries = new AtomicInteger();
            List<Object> list = new ArrayList<>();
            for (int config = 0; config < v.size(); config++) {
                String refery = getRefer(v.get(config).getRefery());
                for (int i = 0; i < dataList.size(); i++) {
                    JSONObject jsonObject1 = JSON.parseObject(dataList.get(i).toString());
                    Object o = jsonObject1.get(refery);
                    if (ObjectUtils.isNotEmpty(o)) {
                        numberOfDataEntries.addAndGet(1);
                        list.add(o);
                    }
                }
            }
            // 拼接数采配置
            List<Object> result = new ArrayList<>();
            for (int i = 0; i < numberOfDataEntries.get(); i++) {
                String aggregate = "";
                for (int j = 0; j < v.size(); j++) {
                    int index;
                    if (j == 0) {
                        index = i;
                    } else {
                        index = numberOfDataEntries.get() + i;
                    }
                    aggregate += list.get(index).toString() + ",";
                }
                int lastIndex = aggregate.lastIndexOf(",");
                String substring = aggregate.substring(0, lastIndex);
                result.add(substring);

            }
            // 进行公式计算
            Object resultValue = calculationFormula(result, v.get(0), k, device);
            map.put(k, resultValue);
        });
        return map;
    }


    /**
     * 拉力机数采
     *
     * @param data
     * @param dataConfig
     * @return
     */
    private static Map<String, Object> analysisMdbByPull(String data, Map<String, List<DataConfig>> dataConfig, Device device, String cableTag) {
        JSONObject jsonObject = JSON.parseObject(data);
        Map<String, Object> map = new HashMap<>();
        if (jsonObject.isEmpty()) {
            return map;
        }
        JSONArray dataList = JSONArray.parseArray(jsonObject.get("data").toString());

        dataConfig.forEach((k, v) -> {
            List<Map<String, String>> resultValue = new ArrayList<>();

            DataConfig configVo = v.get(0);
            for (int i = 0; i < dataList.size(); i++) {
                JSONObject jsonObject1 = JSON.parseObject(dataList.get(i).toString());
                // 获取时间
                String dDate = jsonObject1.getString("dDate");
                String dTime = jsonObject1.getString("dTime");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

                // 解析第一个日期时间字符串
                LocalDateTime dateTime1 = LocalDateTime.parse(dDate, formatter);
                // 获取年月日
                String yearMonthDay = dateTime1.toLocalDate().toString();

                // 解析第二个日期时间字符串
                LocalDateTime dateTime2 = LocalDateTime.parse(dTime, formatter);
                // 获取时分
                String hourMinute = dateTime2.toLocalTime().toString();
                // 拼接年月日和时分
                String mergedDateTime = yearMonthDay + " " + hourMinute;

                // 获取厚度
                String report = jsonObject1.getString("Report");
                String thickness = extractValue(report, "厚度:\\s*(\\S*?)~");

                // 获取拉伸强度伸长率
                String result = "";
                if (StringUtils.isNotBlank(configVo.getMatchingName()) && configVo.getMatchingName().contains("拉伸强度")) {
                    result = extractValue(report, "拉伸强度:\\s*(\\S*?)~");

                }
                //判断检验子项是否是拉伸强度
                if (StringUtils.isNotBlank(configVo.getMatchingName()) && configVo.getMatchingName().contains("伸长率")) {
                    result = extractValue(report, "伸长率:\\s*(\\S*?)~");
                }
                Map<String, String> reportMap = new HashMap<>();
                reportMap.put("mergedDateTime", mergedDateTime);
                reportMap.put("thickness", thickness);
                reportMap.put("result", result);
                resultValue.add(reportMap);
            }
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("equipName", device.getDeviceName());
            hashMap.put("equipValue", device.getManagementNumber());
            hashMap.put("result", resultValue);
            map.put(k, hashMap);
        });
        return map;
    }

    private static String extractValue(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.find() ? matcher.group(1) : null;
    }

    private static Pattern SPATTERN = Pattern.compile("([-+])?\\d+(\\.\\d+)?");

    /**
     * 只需X轴
     *
     * @param data       采集到的文件字符串
     * @param dataConfig 用户配置好的x,y轴定位数据与参照物
     * @return
     */
    private static Map<String, Object> readPngString(String data, Map<String, List<DataConfig>> dataConfig, Device device) {
        Map<String, Object> map = new HashMap<>();
        dataConfig.forEach((k, v) -> {
            List<Object> list = new ArrayList<>();
            for (int config = 0; config < v.size(); config++) {
                String referx = getRefer(v.get(config).getReferx());
                String result = null;
                // 通过\n将字符串分割为行
                String[] aColumnY = data.split("\n");
                List<String> list1 = new ArrayList<>();
                // 该循环得出用户配置的y轴
                for (int i = 0; i < aColumnY.length; i++) {
                    String addDataWithSpaces = referx.replaceAll("", " ");
                    int x = getXOrY(v.get(config).getX(), k, "X");
                    if (aColumnY[i].contains(addDataWithSpaces)) {
                        Matcher matcher = SPATTERN.matcher(aColumnY[i]);
                        while (matcher.find()) {
                            String group = matcher.group();
                            list1.add(group);
                        }
                    }
                    if (ObjectUtils.isNotEmpty(list1)) {
                        result = list1.get(x);
                    }
                }
                if (ObjectUtils.isNotEmpty(result)) {
                    list.add(result);
                }
            }
            // 进行公式计算
            Object resultValue = calculationFormula(list, v.get(0), k, device);
            map.put(k, resultValue);
        });
        return map;
    }

    /**
     * 从文件中提取出来的文字，如果有公式，进行公式计算，否则取列表第一个值
     *
     * @param list       提取出的数字
     * @param dataConfig 存储公式的对象
     * @return
     */
    private static Object calculationFormula(List<Object> list, DataConfig dataConfig, String insProductItem, Device device) {
        if (list.size() == 0) {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("equipName", device.getDeviceName());
            hashMap.put("equipValue", device.getManagementNumber());
            hashMap.put("result", null);
            return hashMap;
        }
        ArrayList<Object> listResult = new ArrayList<>();
        Map<String, Object> hashMap = new HashMap<>();
        // 如果不为空，进行公式计算
        if (ObjectUtils.isNotEmpty(dataConfig.getFormula())) {
            // 否则：没有公式代表不需要计算，直接提取List里面的数据
            if (ObjectUtils.isEmpty(device.getEntrustCode()) && ObjectUtils.isEmpty(device.getSampleCode())) {
                String s = calculationFormulaList(list, dataConfig.getFormula());
                listResult.add(s);
            } else {
                list.forEach(i -> {
                    List<Object> strings = Arrays.asList(i.toString().split(","));
                    String s = calculationFormulaList(strings, dataConfig.getFormula());
                    listResult.add(s);
                });
            }
        } else {
            listResult.addAll(list);
        }
        // 为了给前端做数据区分
        if (listResult.size() > 1) {
            hashMap.put("result", listResult);
        } else {
            hashMap.put("result", listResult.get(0).toString());
        }
        hashMap.put("equipName", device.getDeviceName());
        hashMap.put("equipValue", device.getManagementNumber());
        return hashMap;
    }

    /**
     * 解析String数据
     *
     * @param data       采集到的文件字符串
     * @param dataConfig 用户配置好的x,y轴定位数据与参照物
     * @return
     */
    private static Map<String, Object> analysisTxt(String data, Map<String, List<DataConfig>> dataConfig,
                                                   Device device, String entrustCode, String sampleCode) {
        Map<String, Object> map = new HashMap<>();
        dataConfig.forEach((k, v) -> {
            List<Object> list = new ArrayList<>();
            // 委托编号与样品编号不存在，定：1、Y定范围，X定横坐标；2、只存在Y；3、只存在X
            if (ObjectUtils.isEmpty(device.getEntrustCode()) && ObjectUtils.isEmpty(device.getSampleCode())) {
                // 判断是否是烟密度
                if (device.getManagementNumber().equals("JCZX-ZB-ZT03002")) {
                    // 按行分割数据
                    String[] lines = data.split("\n");

                    // 提取最后一行的第一个数字
                    String lastLine = lines[lines.length - 1];
                    String firstNumber = lastLine.split("\t")[0];
                    list.add(firstNumber);
                } else {
                    list = analyzeData(data, v, k, ",");
                }
                // 委托编号与样品编号存在
            } else if (ObjectUtils.isNotEmpty(device.getEntrustCode()) && ObjectUtils.isNotEmpty(device.getSampleCode())) {
                list = analyzeDataEntrustCodAndSampleCode(data, v, k, ",", device, entrustCode, sampleCode);
            }
            // 进行公式计算
            Object resultValue = calculationFormula(list, v.get(0), k, device);
            map.put(k, resultValue);
        });
        return map;
    }

    /**
     * @param data       采集到的文件字符串
     * @param dataConfig 用户配置好的x,y轴定位数据与参照物
     * @return
     */
    private static Map<String, Object> analysisString(String data, Map<String, List<DataConfig>> dataConfig, Device device,
                                                      String entrustCode, String sampleCode) {
        String processingDataAfterSpaces = data
                .replaceAll("  +", splitIdentifier)
                .replaceAll("\r", "")
                .replaceAll(" ", "");
        Map<String, Object> map = new HashMap<>();
        dataConfig.forEach((k, v) -> {
            List<Object> list = new ArrayList<>();
            // 委托编号与样品编号不存在，定：1、Y定范围，X定横坐标；2、只存在Y；3、只存在X
            if (ObjectUtils.isEmpty(device.getEntrustCode()) && ObjectUtils.isEmpty(device.getSampleCode())) {
                list = analyzeData(processingDataAfterSpaces, v, k, splitIdentifier);
                // 委托编号与样品编号存在
            } else if (ObjectUtils.isNotEmpty(device.getEntrustCode()) && ObjectUtils.isNotEmpty(device.getSampleCode())) {
                list = analyzeDataEntrustCodAndSampleCode(processingDataAfterSpaces, v, k, splitIdentifier, device, entrustCode, sampleCode);
            }
            // 进行公式计算
            Object resultValue = calculationFormula(list, v.get(0), k, device);
            map.put(k, resultValue);
        });
        return map;
    }

    /**
     * 取X，Y两个定位
     *
     * @param data       采集到的文件字符串
     * @param dataConfig 用户配置好的x,y轴定位数据与参照物
     * @return
     */
    public static Map<String, Object> analysisList(String data, Map<String, List<DataConfig>> dataConfig,
                                                   Device device, String entrustCode, String sampleCode) {
        Map<String, Object> map = new HashMap<>();
        dataConfig.forEach((k, v) -> {
            List<Object> list = new ArrayList<>();
            // 委托编号与样品编号不存在，定：1、Y定范围，X定横坐标；2、只存在Y；3、只存在X
            if (ObjectUtils.isEmpty(device.getEntrustCode()) && ObjectUtils.isEmpty(device.getSampleCode())) {
                list = analyzeData(data, v, k, splitIdentifier);
                // 委托编号与样品编号存在
            } else if (ObjectUtils.isNotEmpty(device.getEntrustCode()) && ObjectUtils.isNotEmpty(device.getSampleCode())) {
                list = analyzeDataEntrustCodAndSampleCode(data, v, k, splitIdentifier, device, entrustCode, sampleCode);
            }
            // 进行公式计算
            Object resultValue = calculationFormula(list, v.get(0), k, device);
            map.put(k, resultValue);
        });
        return map;
    }

    private static List<Object> analyzeDataEntrustCodAndSampleCode(String data, List<DataConfig> v, String k, String splitIdentifier,
                                                                   Device device, String entrustCodeValue, String sampleCodeValue) {
        entrustCodeValue = entrustCodeValue.replaceAll(" ", "");
        sampleCodeValue = sampleCodeValue.replaceAll(" ", "");
        // 最终结果
        List<Object> list = new ArrayList<>();
        int numberOfDataEntries = 0;
        // 取entrustCode与sampleCode所在位
        for (int config = 0; config < v.size(); config++) {
            numberOfDataEntries = 0;
            Integer entrustCodeY = null;
            Integer sampleCodeY = null;
            Integer referYCoordinate = null;
            String refery = getRefer(v.get(config).getRefery());
            String entrustCode = getRefer(device.getEntrustCode()); // 委托编号字段
            String sampleCode = getRefer(device.getSampleCode()); // 样品编号字段
            if (ObjectUtils.isEmpty(refery)) {
                continue;
            }
            // 去除所有的空格，通过\n将字符串分割为行
            String[] aColumnY = data.replaceAll(" ", "").split("\n");
            for (int i = 0; i < aColumnY.length; i++) {
                // 如果通过判断，定位到Y轴
                if (aColumnY[i].contains(entrustCode) && aColumnY[i].contains(sampleCode)) {
                    String[] aLine = aColumnY[i].split(splitIdentifier);
                    for (int j = 0; j < aLine.length; j++) {
                        if (aLine[j].contains(entrustCode)) {
                            entrustCodeY = j;
                        }
                        if (aLine[j].contains(sampleCode) ) {
                            sampleCodeY = j;
                        }
                        if (aLine[j].contains(refery)) {
                            referYCoordinate = j;
                        }
                    }
                }
                if (ObjectUtils.isNotEmpty(entrustCodeY) && ObjectUtils.isNotEmpty(sampleCodeY) && ObjectUtils.isNotEmpty(referYCoordinate)) {
                    String[] aLine = aColumnY[i].split(splitIdentifier);
                    try {
                        if (aLine[entrustCodeY].contains(entrustCodeValue) && aLine[sampleCodeY].contains(sampleCodeValue)) {
                            String result = aLine[referYCoordinate];
                            // 防止计算公式的时候出现：[null] 这种数据
                            if (ObjectUtils.isNotEmpty(result)) {
                                numberOfDataEntries += 1;
                                list.add(result);
                            }
                        }
                    } catch (Exception e) {}
                }
            }
        }
        // 拼接数采配置
        List<Object> result = new ArrayList<>();
        for (int i = 0; i < numberOfDataEntries; i++) {
            String aggregate = "";
            for (int j = 0; j < v.size(); j++) {
                int index;
                if (j == 0) {
                    index = i;
                } else {
                    index = numberOfDataEntries + i;
                }
                aggregate += list.get(index).toString() + ",";
            }
            int lastIndex = aggregate.lastIndexOf(",");
            String substring = aggregate.substring(0, lastIndex);
            result.add(substring);
        }
        return result;
    }

    // 由于在方法中会大量的判断，所以做一个方法
    private static int getXOrY(String value, String k, String tips) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ErrorException(k + "：未配置" + tips + "坐标轴的值！");
        }
    }

    // 防止参照物为空报错，进行判断如果为空赋值空字符
    private static String getRefer(String refer) {
        return ObjectUtils.isNotEmpty(refer) ? refer.replaceAll(" ", "") : "";
    }

    /**
     * 委托编号与样品编号都为空执行
     *
     * @param data
     * @param v
     * @param k
     * @param split
     * @return
     */
    public static List<Object> analyzeData(String data, List<DataConfig> v, String k, String split) {
        List<Object> list = new ArrayList<>();
        for (int config = 0; config < v.size(); config++) {
            // 取两个用户配置的参照物
            String referx = getRefer(v.get(config).getReferx());
            String refery = getRefer(v.get(config).getRefery());
            if (ObjectUtils.isEmpty(refery) && ObjectUtils.isEmpty(referx)) {
                continue;
            }
            // 最终结果
            List<Object> result = new ArrayList<>();
            // 通过\n将字符串分割为行
            String[] aColumnY = data.replaceAll(" ", "").split("\n");
            Integer end = null;
            // 采集数据：Y轴
            for (int i = 0; i < aColumnY.length; i++) {
                // 如果Y参照不为空与X参照为空则执行，同时该行包含Y参照
                if (ObjectUtils.isNotEmpty(refery) && ObjectUtils.isEmpty(referx) && aColumnY[i].contains(refery)) {
                    // 取Y坐标值
                    int y = getXOrY(v.get(config).getY(), k, "Y");
                    String[] aLineX = aColumnY[i].split(split);
                    for (int j = 0; j < aLineX.length; j++) {
                        if (aLineX[j].contains(refery)) {
                            String[] split1 = new String[0];
                            try {
                                split1 = aColumnY[i + y].split(split);
                            } catch (Exception e) {
                                throw new ErrorException(k + "：Y轴定位超出！");
                            }
                            try {
                                result.add(split1[j]);
                            } catch (Exception e) {
                                throw new ErrorException(k + "：X轴定位超出！");
                            }
                        }
                    }
                    // 如果Y参照不为空与X参照不为空则执行,此处Y定区域
                } else if (ObjectUtils.isNotEmpty(refery) && ObjectUtils.isNotEmpty(referx)) {
                    // 取x的值，防止报错
                    int x = getXOrY(v.get(config).getX(), k, "X");
                    // 取Y坐标值
                    int y = getXOrY(v.get(config).getY(), k, "Y");
                    // 缓存Y的结束值
                    if (ObjectUtils.isEmpty(end) && aColumnY[i].contains(refery)) {
                        end = i + y;
                    }
                    // 判断是否在参照物为起到，Y坐标值为最终范围
                    if (ObjectUtils.isNotEmpty(end) && i <= end) {
                        String[] aLineX = aColumnY[i].split(split);
                        for (int j = 0; j < aLineX.length; j++) {
                            if (aLineX[j].contains(referx)) {
                                try {
                                    result.add(aLineX[j + x]);
                                } catch (Exception e) {
                                    throw new ErrorException(k + "：X轴定位超出！");
                                }
                                break;
                            }
                        }
                    }
                    // 如果X参照不为空同时该行包含X参照，则执行下面的代码
                } else if (aColumnY[i].contains(referx) && ObjectUtils.isEmpty(refery)) {
                    String[] aLineX = aColumnY[i].split(split);
                    // 取x的值，防止报错
                    int x = getXOrY(v.get(config).getX(), k, "X");
                    for (int j = 0; j < aLineX.length; j++) {
                        if (aLineX[j].contains(referx)) {
                            try {
                                result.add(aLineX[j + x]);
                            } catch (Exception e) {
                                throw new ErrorException(k + "：X轴定位超出！");
                            }
                        }
                    }
                }
            }
            // 防止计算公式的时候出现：[null] 这种数据
            if (ObjectUtils.isNotEmpty(result)) {
//                String formatProcessing = getFormatProcessing(result);
                list.addAll(result);
            }
        }
        return list;
    }

    public static String getFormatProcessing(String value) {
        value = value.replaceAll("%", "");
        if (value.contains("=")) {
            String[] split = value.split("=");
            return split[split.length - 1];
        } else if (value.contains(":")) {
            String[] split = value.split(":");
            return split[split.length - 1];
        } else {
            return value;
        }
    }

    public static String getIp(HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        // 防止回环地址变为IPv6
        return ipAddress.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ipAddress;
    }

    public static String calculationFormulaList(List<Object> list, String formula) {
        //首先将list转换为bigdecmic
        List<BigDecimal> bigDecimalList = list.stream()
                .map(obj -> {
                    return new BigDecimal((obj).toString());
                }).collect(Collectors.toList());

        //将中文的(转换英文的())
        formula = formula.replace("（", "(")
                .replace("）", ")")
                .replace("，", ",");
        //然后提取公式
        String strs = formula.substring(0, formula.indexOf("("));
        String upperStr = strs.toUpperCase();
        if (upperStr.matches(".*\\d.*")) {
            upperStr = "";
        }
        //然后获取最外面括号里面的值,再根据","分割
        int start = formula.indexOf("(");
        int end = -1;
        int a = 0;
        for (int i = start; i < formula.length(); i++) {
            char c = formula.charAt(i);
            if (c == '(') {
                a++;
            } else if (c == ')') {
                a--;
                if (a == 0) {
                    end = i;
                }
            }
        }
        if (start == -1 || end == -1) {
            throw new ErrorException("公式括号不匹配: " + formula);
        }

        String argumentsStr = formula.substring(start + 1, end);
        List<String> arguments = new ArrayList<>();
        int bracketCount = 0;
        StringBuilder currentArgument = new StringBuilder();
        for (char c : argumentsStr.toCharArray()) {
            if (c == ',' && bracketCount == 0) {
                arguments.add(currentArgument.toString());
                currentArgument.setLength(0);
            } else {
                if (c == '(') bracketCount++;
                if (c == ')') bracketCount--;
                currentArgument.append(c);
            }
        }
        arguments.add(currentArgument.toString());
        String[] bracketStrs = arguments.toArray(new String[0]);
        List<BigDecimal> results = new ArrayList<>();
        for (String expr : bracketStrs) {
            Pattern pattern = Pattern.compile("([A-Z])(\\d+)");
            Matcher matcher = pattern.matcher(expr);
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                String letter = matcher.group(1);
                int index = Integer.parseInt(matcher.group(2)) - 1; // 将1-based转为0-based
                if (index < bigDecimalList.size()) {
                    matcher.appendReplacement(sb, bigDecimalList.get(index).toString());
                } else {
                    throw new RuntimeException("公式中的下标 " + index + " 超出范围");
                }
            }
            matcher.appendTail(sb);

            // 计算表达式
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
            try {
                Object result = engine.eval(sb.toString());
                results.add(new BigDecimal(result.toString()));
            } catch (Exception e) {
                throw new IllegalArgumentException("无法计算公式: " + sb, e);
            }
        }
        // 根据函数名称进行相应计算
        BigDecimal finalResult;
        if (upperStr.equals("") || upperStr == null) {
            finalResult = results.get(0);
        } else {
            switch (upperStr) {
                case "MAX":
                    finalResult = results.stream().max(BigDecimal::compareTo)
                            .orElseThrow(() -> new IllegalArgumentException("无法计算MAX值"));
                    break;
                case "MIN":
                    finalResult = results.stream().min(BigDecimal::compareTo)
                            .orElseThrow(() -> new IllegalArgumentException("无法计算MIN值"));
                    break;
                case "SUM":
                    finalResult = results.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                    break;
                case "ABS":
                    finalResult = results.stream().map(BigDecimal::abs).reduce(BigDecimal.ZERO, BigDecimal::add);
                    break;
                case "AVERAGE":
                    finalResult = results.stream().reduce(BigDecimal.ZERO, BigDecimal::divide)
                            .divide(BigDecimal.valueOf(results.size()), 2, BigDecimal.ROUND_HALF_UP);
                    break;
                case "MEDIAN":
                    int size = results.size();
                    if (size % 2 == 1) {
                        finalResult = results.get(size / 2);
                    } else {
                        BigDecimal sum = results.get(size / 2 - 1).add(results.get(size / 2));
                        finalResult = sum.divide(BigDecimal.valueOf(2), 2, BigDecimal.ROUND_HALF_UP);
                    }
                    break;
                default:
                    throw new UnsupportedOperationException("暂不支持函数: " + upperStr);
            }
        }

        return finalResult.toString();
        // 否则：没有公式代表不需要计算，直接提取List里面的数据
    }

}
