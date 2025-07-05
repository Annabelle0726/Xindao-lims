package com.ruoyi.inspect.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.data.Pictures;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.DateImageUtil;
import com.ruoyi.common.utils.api.IfsApiUtils;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.inspect.dto.IfsStockQueryDTO;
import com.ruoyi.inspect.dto.SpotCheckQuarterDto;
import com.ruoyi.inspect.dto.SpotCheckYearDto;
import com.ruoyi.inspect.mapper.SpotCheckQuarterMapper;
import com.ruoyi.inspect.mapper.SpotCheckYearMapper;
import com.ruoyi.inspect.pojo.SpotCheckQuarter;
import com.ruoyi.inspect.pojo.SpotCheckQuarterItem;
import com.ruoyi.inspect.pojo.SpotCheckYear;
import com.ruoyi.inspect.pojo.SpotCheckYearItem;
import com.ruoyi.inspect.service.FinishProductSpotCheckService;
import com.ruoyi.inspect.service.SpotCheckQuarterItemService;
import com.ruoyi.inspect.service.SpotCheckYearItemService;
import com.ruoyi.system.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 成品抽样
 *
 * @Author zhuo
 * @Date 2024/9/29
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FinishProductSpotCheckServiceImpl implements FinishProductSpotCheckService {

    @Resource
    private IfsApiUtils ifsApiUtils;
    @Resource
    private SpotCheckQuarterMapper spotCheckQuarterMapper;
    @Resource
    private SpotCheckQuarterItemService spotCheckQuarterItemService;
    @Resource
    private SpotCheckYearMapper spotCheckYearMapper;
    @Resource
    private SpotCheckYearItemService spotCheckYearItemService;
    @Value("${wordUrl}")
    private String wordUrl;
    @Value("${file.path}")
    private String imgUrl;
    @Resource
    private UserMapper userMapper;


    public Result getIfsStockReport(IfsStockQueryDTO ifsStockQueryDTO) {
        JSONObject jresult = new JSONObject();
        jresult.put("total", 0);
        jresult.put("count", 0);
        jresult.put("data", new JSONArray());
        JSONObject jsonObject = new JSONObject();
        generateIfsStockQueryParams(jsonObject, ifsStockQueryDTO, ifsStockQueryDTO.getPage(), ifsStockQueryDTO.getLimit());
        Result result = ifsApiUtils.getIfsStock(jsonObject.toJSONString());
        if (result.getCode() == 200) {
            JSONObject data = (JSONObject) JSON.toJSON(result.getData());
            JSONArray array = new JSONArray();
            for (int i = 0; i < data.getJSONArray("LIST_INFO").size(); i++) {
                JSONObject listInfo = data.getJSONArray("LIST_INFO").getJSONObject(i);
                array.add(listInfo);
            }
            //获取总数量
            int count = data.getIntValue("TOTAL_RECORD");
            //获取页数
            int total = (int) Math.ceil((double) count / (double) ifsStockQueryDTO.getLimit());
            jresult.put("total", total);
            jresult.put("count", count);
            jresult.put("data", array);
        }
        return Result.success(jresult);
    }

    public void generateIfsStockQueryParams(JSONObject result, IfsStockQueryDTO ifsStockQueryDTO, Long pageNo, Long pageSize) {
        result.put("PAGE", pageNo);
        result.put("LIMIT", pageSize);
        result.put("WAREHOUSE", "成品仓库");
        result.put("QUANTITY_FLAG", ">0");
        //表里面的OTC行号
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getCotcOrderNo())) {
            result.put("C_OTC_ORDER_NO", ifsStockQueryDTO.getCotcOrderNo());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getCotcLineNo())) {
            result.put("C_OTC_LINE_NO", ifsStockQueryDTO.getCotcLineNo());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getPartNo())) {
            result.put("PART_NO", ifsStockQueryDTO.getPartNo());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getPartDescription())) {
            result.put("PART_DESC", "%" + ifsStockQueryDTO.getPartDescription() + "%");
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getLocNo())) {
            result.put("LOCATION_NO", ifsStockQueryDTO.getLocNo());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getLocDesc())) {
            result.put("LOCATION_DESC", ifsStockQueryDTO.getLocDesc());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getLotBatchNo())) {
            result.put("LOT_BATCH_NO", ifsStockQueryDTO.getLotBatchNo());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getWdrNo())) {
            result.put("WDR_NO", ifsStockQueryDTO.getWdrNo());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getWarehouse())) {
            result.put("WAREHOUSE", ifsStockQueryDTO.getWarehouse());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getLocationGroup())) {
            result.put("LOCATION_GROUP", ifsStockQueryDTO.getLocationGroup());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getLocationGroupDesc())) {
            result.put("LOCATION_GROUP_DESC", ifsStockQueryDTO.getLocationGroupDesc());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getReelNumber())) {
            result.put("ATTR1", ifsStockQueryDTO.getReelNumber());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getStartMeter())) {
            result.put("ATTR2", ifsStockQueryDTO.getStartMeter());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getEndMeter())) {
            result.put("ATTR3", ifsStockQueryDTO.getEndMeter());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getOuterColor())) {
            result.put("ATTR4", ifsStockQueryDTO.getOuterColor());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getInsulationColor())) {
            result.put("ATTR5", ifsStockQueryDTO.getInsulationColor());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getOtcOrderNo())) {
            result.put("ATTR6", ifsStockQueryDTO.getOtcOrderNo());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getMpsNo())) {
            result.put("ATTR7", ifsStockQueryDTO.getMpsNo());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getLetteringInfo())) {
            result.put("ATTR8", ifsStockQueryDTO.getLetteringInfo());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getSStockQuantity())) {
            result.put("ATTR9", ifsStockQueryDTO.getSStockQuantity());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getSAvailableStockQuantity())) {
            result.put("ATTR10", ifsStockQueryDTO.getSAvailableStockQuantity());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getSunit())) {
            result.put("ATTR11", ifsStockQueryDTO.getSunit());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getStockSource())) {
            result.put("ATTR12", ifsStockQueryDTO.getStockSource());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getInspInstorageTime())) {
            result.put("ATTR13", ifsStockQueryDTO.getInspInstorageTime());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getTestRemark())) {
            result.put("ATTR14", ifsStockQueryDTO.getTestRemark());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getGrossWeight())) {
            result.put("ATTR15", ifsStockQueryDTO.getGrossWeight());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getNetWeight())) {
            result.put("ATTR16", ifsStockQueryDTO.getNetWeight());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getPackingManner())) {
            result.put("ATTR17", ifsStockQueryDTO.getPackingManner());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getCylinderNumber())) {
            result.put("ATTR18", ifsStockQueryDTO.getCylinderNumber());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getRemark())) {
            result.put("ATTR19", ifsStockQueryDTO.getRemark());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getLengthRequirement())) {
            result.put("ATTR20", ifsStockQueryDTO.getLengthRequirement());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getCustomerName())) {
            result.put("ATTR21", ifsStockQueryDTO.getCustomerName());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getSystemNo())) {
            result.put("ATTR22", ifsStockQueryDTO.getSystemNo());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getInSource())) {
            result.put("ATTR23", ifsStockQueryDTO.getInSource());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getSplitQuality())) {
            result.put("ATTR24", ifsStockQueryDTO.getSplitQuality());
        }
        if (StrUtil.isNotBlank(ifsStockQueryDTO.getReserveQuantity())) {
            result.put("QTY_RESERVED", ifsStockQueryDTO.getReserveQuantity());
        }
    }

    /************************************************* 季度抽样 ***********************************************************/

    /**
     * 新增季度抽检
     * @param spotCheckQuarterDto
     * @return
     */
    @Override
    public boolean addQuarter(SpotCheckQuarterDto spotCheckQuarterDto) {
        // 根据编号查询之前的
        SpotCheckQuarter spotCheckQuarter = spotCheckQuarterMapper.selectOne(Wrappers.<SpotCheckQuarter>lambdaQuery()
                .eq(SpotCheckQuarter::getQuarterNo, spotCheckQuarterDto.getQuarterNo())
                .orderByDesc(SpotCheckQuarter::getCreateTime)
                .last("limit 1"));
        if (spotCheckQuarter == null) {
            spotCheckQuarterMapper.insert(spotCheckQuarterDto);
            // 添加详情
            for (SpotCheckQuarterItem quarterItem : spotCheckQuarterDto.getQuarterItems()) {
                quarterItem.setQuarterId(spotCheckQuarterDto.getQuarterId());
            }
            spotCheckQuarterItemService.saveBatch(spotCheckQuarterDto.getQuarterItems());
        } else {
            // 累加详情
            for (SpotCheckQuarterItem quarterItem : spotCheckQuarterDto.getQuarterItems()) {
                quarterItem.setQuarterId(spotCheckQuarter.getQuarterId());
            }
            spotCheckQuarterItemService.saveBatch(spotCheckQuarterDto.getQuarterItems());
        }

        return true;
    }

    /**
     * 查询季度抽样详情
     * @param quarterId
     * @return
     */
    @Override
    public SpotCheckQuarterDto getQuarter(Integer quarterId) {
        SpotCheckQuarter spotCheckQuarter = spotCheckQuarterMapper.selectById(quarterId);

        // 查询详情
        List<SpotCheckQuarterItem> list = spotCheckQuarterItemService.list(Wrappers.<SpotCheckQuarterItem>lambdaQuery()
                .eq(SpotCheckQuarterItem::getQuarterId, spotCheckQuarter.getQuarterId()));
        SpotCheckQuarterDto spotCheckQuarterDto = new SpotCheckQuarterDto();
        BeanUtil.copyProperties(spotCheckQuarter, spotCheckQuarterDto);
        spotCheckQuarterDto.setQuarterItems(list);

        return spotCheckQuarterDto;
    }

    /**
     * 季度抽样分页查询
     * @param page
     * @param spotCheckQuarter
     * @return
     */
    @Override
    public IPage<SpotCheckQuarterDto> getQuarterPage(Page page, SpotCheckQuarterDto spotCheckQuarter) {
        return spotCheckQuarterMapper.getQuarterPage(page, QueryWrappers.queryWrappers(spotCheckQuarter));
    }

    /**
     * 删除季度抽样
     * @param quarterId
     * @return
     */
    @Override
    public boolean deleteQuarter(Integer quarterId) {
        spotCheckQuarterMapper.deleteById(quarterId);
        spotCheckQuarterItemService.remove(Wrappers.<SpotCheckQuarterItem>lambdaQuery()
                .eq(SpotCheckQuarterItem::getQuarterId, quarterId));
        return true;
    }

    /**
     * 成品下单界面查询季度信息
     * @return
     */
    @Override
    public List<Map<String, Object>> getQuarterOnOrder() {
        List<Map<String, Object>> quarterOnOrder = spotCheckQuarterMapper.getQuarterOnOrder();
        for (Map<String, Object> stringMap : quarterOnOrder) {
            List<SpotCheckQuarterItem> list = spotCheckQuarterItemService.list(Wrappers.<SpotCheckQuarterItem>lambdaQuery()
                    .eq(SpotCheckQuarterItem::getQuarterId, (Integer) stringMap.get("value")));
            List<Map<String, Object>> itemMaps = list.stream().map(spotCheckQuarterItem -> {
                Map<String, Object> map = new HashMap<>();
                map.put("value", spotCheckQuarterItem.getQuarterItemId());
                map.put("label", spotCheckQuarterItem.getProductModel() + " "
                        + spotCheckQuarterItem.getProductType() + " "
                        + spotCheckQuarterItem.getSpotCheckNumber());
                return map;
            }).collect(Collectors.toList());
            stringMap.put("children", itemMaps);

        }

        return quarterOnOrder;
    }

    /**
     * 修改季度检验
     * @param spotCheckQuarterDto
     * @return
     */
    @Override
    public boolean updateQuarterOnOrder(SpotCheckQuarterDto spotCheckQuarterDto) {
        if (CollectionUtils.isNotEmpty(spotCheckQuarterDto.getQuarterItems())) {
            // 清除之前详情
            spotCheckQuarterItemService.remove(Wrappers.<SpotCheckQuarterItem>lambdaQuery()
                    .eq(SpotCheckQuarterItem::getQuarterId, spotCheckQuarterDto.getQuarterId()));
            // 添加详情
            for (SpotCheckQuarterItem quarterItem : spotCheckQuarterDto.getQuarterItems()) {
                quarterItem.setQuarterId(spotCheckQuarterDto.getQuarterId());
            }
            spotCheckQuarterItemService.saveBatch(spotCheckQuarterDto.getQuarterItems());
        }

        spotCheckQuarterMapper.updateById(spotCheckQuarterDto);
        return true;
    }

    /**
     * 生成最终报告
     * @param quarterId
     * @return
     */
    @Override
    public boolean finalReportQuarter(Integer quarterId, HttpServletResponse response) {
        // 添加报告信息
        SpotCheckQuarter spotCheckQuarter = spotCheckQuarterMapper.selectById(quarterId);

        // 查询详情
        List<SpotCheckQuarterItem> itemList = spotCheckQuarterItemService.list(Wrappers.<SpotCheckQuarterItem>lambdaQuery()
                .eq(SpotCheckQuarterItem::getQuarterId, spotCheckQuarter.getQuarterId()));

        for (SpotCheckQuarterItem spotCheckQuarterItem : itemList) {
            // 拼接型号和数量
            spotCheckQuarterItem.setSpotCheckNumber((StringUtils.isBlank(spotCheckQuarterItem.getProductModel()) ? "" : spotCheckQuarterItem.getProductModel()) +
                    (StringUtils.isBlank(spotCheckQuarterItem.getSpotCheckNumber()) ? "" : spotCheckQuarterItem.getSpotCheckNumber()));
        }

        //编制人
        String writeUrl = null;
        if (spotCheckQuarter.getWriteUser() != null) {
            try {
                writeUrl = userMapper.selectById(spotCheckQuarter.getWriteUser()).getSignatureUrl();
            } catch (Exception e) {
                throw new ErrorException("找不到编制人的签名");
            }
            if (StrUtil.isBlank(writeUrl)) {
                throw new ErrorException("找不到编制人的签名");
            }
        }

        // 初始化所有的 countersignUrl 变量
        String[] countersignUrls = new String[7];

        // 如果 spotCheckQuarter.getCountersignUser() 不是空或空白字符串
        if (StrUtil.isNotBlank(spotCheckQuarter.getCountersignUser())) {
            String[] split = spotCheckQuarter.getCountersignUser().split(",");

            // 遍历 split 数组，并将元素依次赋值给 countersignUrls 数组
            for (int i = 0; i < Math.min(split.length, countersignUrls.length); i++) {

                String countersignUrl = null;
                try {
                    countersignUrl = userMapper.selectById(Integer.parseInt(split[i])).getSignatureUrl();
                } catch (Exception e) {
                    throw new ErrorException("找不到编制人的签名");
                }
                if (StrUtil.isBlank(countersignUrl)) {
                    throw new ErrorException("找不到编制人的签名");
                }

                countersignUrls[i] = countersignUrl;
            }
        }

        // 将 countersignUrls 数组中的值依次赋值给 countersignUrl1 到 countersignUrl7
        String countersignUrl1 = countersignUrls[0];
        String countersignUrl2 = countersignUrls[1];
        String countersignUrl3 = countersignUrls[2];
        String countersignUrl4 = countersignUrls[3];
        String countersignUrl5 = countersignUrls[4];
        String countersignUrl6 = countersignUrls[5];
        String countersignUrl7 = countersignUrls[6];

        //审核人
        String examineUrl = null;
        if (spotCheckQuarter.getExamineUser() != null) {
            try {
                examineUrl = userMapper.selectById(spotCheckQuarter.getExamineUser()).getSignatureUrl();
            } catch (Exception e) {
                throw new ErrorException("找不到审核人的签名");
            }
            if (StrUtil.isBlank(examineUrl)) {
                throw new ErrorException("找不到审核人的签名");
            }
        }

        //批准人
        String ratifyUrl = null;
        if (spotCheckQuarter.getRatifyUser() != null) {
            try {
                ratifyUrl = userMapper.selectById(spotCheckQuarter.getRatifyUser()).getSignatureUrl();
            } catch (Exception e) {
                throw new ErrorException("找不到批准人的签名");
            }
            if (StrUtil.isBlank(ratifyUrl)) {
                throw new ErrorException("找不到批准人的签名");
            }
        }

        String finalWriteUrl = writeUrl;
        String finalExamineUrl = examineUrl;
        String finalRatifyUrl = ratifyUrl;

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/spot_check_quarter.docx");
        ConfigureBuilder builder = Configure.builder();
        builder.useSpringEL(true);

        XWPFTemplate template = XWPFTemplate.compile(inputStream, builder.build()).render(
                new HashMap<String, Object>() {{
                    put("quarter", spotCheckQuarter);
                    put("quarterItems", itemList);
                    put("writeUrl", StrUtil.isBlank(finalWriteUrl) ? null :
                            Pictures.ofLocal(imgUrl + "/" + finalWriteUrl).create());
                    put("countersignUrl1", StrUtil.isBlank(countersignUrl1) ? null :
                            Pictures.ofLocal(imgUrl + "/" + countersignUrl1).create());
                    put("countersignUrl2", StrUtil.isBlank(countersignUrl2) ? null :
                            Pictures.ofLocal(imgUrl + "/" + countersignUrl2).create());
                    put("countersignUrl3", StrUtil.isBlank(countersignUrl3) ? null :
                            Pictures.ofLocal(imgUrl + "/" + countersignUrl3).create());
                    put("countersignUrl4", StrUtil.isBlank(countersignUrl4) ? null :
                            Pictures.ofLocal(imgUrl + "/" + countersignUrl4).create());
                    put("countersignUrl5", StrUtil.isBlank(countersignUrl5) ? null :
                            Pictures.ofLocal(imgUrl + "/" + countersignUrl5).create());
                    put("countersignUrl6", StrUtil.isBlank(countersignUrl6) ? null :
                            Pictures.ofLocal(imgUrl + "/" + countersignUrl6).create());
                    put("countersignUrl7", StrUtil.isBlank(countersignUrl7) ? null :
                            Pictures.ofLocal(imgUrl + "/" + countersignUrl7).create());
                    put("examineUrl", StrUtil.isBlank(finalExamineUrl) ? null :
                            Pictures.ofLocal(imgUrl + "/" + finalExamineUrl).create());
                    put("ratifyUrl", StrUtil.isBlank(finalRatifyUrl) ? null :
                            Pictures.ofLocal(imgUrl + "/" + finalRatifyUrl).create());
                    put("createDateUrl", Pictures.ofStream(DateImageUtil.createDateImage(spotCheckQuarter.getCreateTime())).create());
                }});
        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    spotCheckQuarter.getQuarterNo() + "抽样计划", "UTF-8");
            response.setHeader("Content-disposition",
                    "attachment;filename=" + fileName + ".docx");
            OutputStream os = response.getOutputStream();
            template.write(os);
            os.flush();
            os.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("导出失败");
        }
        return true;
    }


    /******************************************************* 年度抽样  *************************************************************/

    @Override
    public boolean addSpotCheckYear(SpotCheckYearDto spotCheckYearDto) {
        spotCheckYearMapper.insert(spotCheckYearDto);
        // 详情
        for (SpotCheckYearItem yearItem : spotCheckYearDto.getYearItems()) {
            yearItem.setYearId(spotCheckYearDto.getYearId());
        }
        spotCheckYearItemService.saveBatch(spotCheckYearDto.getYearItems());
        return true;
    }

    @Override
    public SpotCheckYearDto getSpotCheckYear(Integer yearId) {
        SpotCheckYear spotCheckYear = spotCheckYearMapper.selectById(yearId);

        // 查询详情
        List<SpotCheckYearItem> list = spotCheckYearItemService.list(Wrappers.<SpotCheckYearItem>lambdaQuery()
                .eq(SpotCheckYearItem::getYearId, spotCheckYear.getYearId()));
        SpotCheckYearDto spotCheckYearDto = new SpotCheckYearDto();
        BeanUtil.copyProperties(spotCheckYear, spotCheckYearDto);
        spotCheckYearDto.setYearItems(list);

        return spotCheckYearDto;
    }

    @Override
    public IPage<SpotCheckYearDto> getSpotCheckYearPage(Page page, SpotCheckYearDto spotCheckYear) {
        return spotCheckYearMapper.getSpotCheckYearPage(page, QueryWrappers.queryWrappers(spotCheckYear));
    }

    @Override
    public boolean deleteSpotCheckYear(Integer yearId) {
        spotCheckYearMapper.deleteById(yearId);
        spotCheckYearItemService.remove(Wrappers.<SpotCheckYearItem>lambdaQuery()
                .eq(SpotCheckYearItem::getYearId, yearId));
        return true;
    }

    /**
     * 修改年度检验
     * @param spotCheckYearDto
     * @return
     */
    @Override
    public boolean updateSpotCheckYear(SpotCheckYearDto spotCheckYearDto) {
        spotCheckYearMapper.updateById(spotCheckYearDto);

        // 删除详情
        spotCheckYearItemService.remove(Wrappers.<SpotCheckYearItem>lambdaQuery()
                .eq(SpotCheckYearItem::getYearId, spotCheckYearDto.getYearId()));

        // 重新新增
        for (SpotCheckYearItem yearItem : spotCheckYearDto.getYearItems()) {
            yearItem.setYearId(spotCheckYearDto.getYearId());
        }
        spotCheckYearItemService.saveBatch(spotCheckYearDto.getYearItems());
        return false;
    }

    @Override
    public boolean finalReportSpotCheckYear(Integer yearId, HttpServletResponse response) {
        SpotCheckYear spotCheckYear = spotCheckYearMapper.selectById(yearId);

        // 查询详情
        List<SpotCheckYearItem> itemList = spotCheckYearItemService.list(Wrappers.<SpotCheckYearItem>lambdaQuery()
                .eq(SpotCheckYearItem::getYearId, spotCheckYear.getYearId()));

        //编制人
        String writeUrl = null;
        if (spotCheckYear.getWriteUser() != null) {
            try {
                writeUrl = userMapper.selectById(spotCheckYear.getWriteUser()).getSignatureUrl();
            } catch (Exception e) {
                throw new ErrorException("找不到编制人的签名");
            }
            if (StrUtil.isBlank(writeUrl)) {
                throw new ErrorException("找不到编制人的签名");
            }
        }

        // 初始化所有的 countersignUrl 变量
        String[] countersignUrls = new String[7];

        // 如果不是空或空白字符串
        if (StrUtil.isNotBlank(spotCheckYear.getCountersignUser())) {
            String[] split = spotCheckYear.getCountersignUser().split(",");

            // 遍历 split 数组，并将元素依次赋值给 countersignUrls 数组
            for (int i = 0; i < Math.min(split.length, countersignUrls.length); i++) {

                String countersignUrl = null;
                try {
                    countersignUrl = userMapper.selectById(Integer.parseInt(split[i])).getSignatureUrl();
                } catch (Exception e) {
                    throw new ErrorException("找不到编制人的签名");
                }
                if (StrUtil.isBlank(countersignUrl)) {
                    throw new ErrorException("找不到编制人的签名");
                }

                countersignUrls[i] = countersignUrl;
            }
        }

        // 将 countersignUrls 数组中的值依次赋值给 countersignUrl1 到 countersignUrl7
        String countersignUrl1 = countersignUrls[0];
        String countersignUrl2 = countersignUrls[1];
        String countersignUrl3 = countersignUrls[2];
        String countersignUrl4 = countersignUrls[3];
        String countersignUrl5 = countersignUrls[4];
        String countersignUrl6 = countersignUrls[5];
        String countersignUrl7 = countersignUrls[6];

        //审核人
        String examineUrl = null;
        if (spotCheckYear.getExamineUser() != null) {
            try {
                examineUrl = userMapper.selectById(spotCheckYear.getExamineUser()).getSignatureUrl();
            } catch (Exception e) {
                throw new ErrorException("找不到审核人的签名");
            }
            if (StrUtil.isBlank(examineUrl)) {
                throw new ErrorException("找不到审核人的签名");
            }
        }

        //批准人
        String ratifyUrl = null;
        if (spotCheckYear.getRatifyUser() != null) {
            try {
                ratifyUrl = userMapper.selectById(spotCheckYear.getRatifyUser()).getSignatureUrl();
            } catch (Exception e) {
                throw new ErrorException("找不到批准人的签名");
            }
            if (StrUtil.isBlank(ratifyUrl)) {
                throw new ErrorException("找不到批准人的签名");
            }
        }

        String finalWriteUrl = writeUrl;
        String finalExamineUrl = examineUrl;
        String finalRatifyUrl = ratifyUrl;

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/spot_check_year.docx");
        ConfigureBuilder builder = Configure.builder();
        builder.useSpringEL(true);
        XWPFTemplate template = XWPFTemplate.compile(inputStream, builder.build()).render(
                new HashMap<String, Object>() {{
                    put("year", spotCheckYear);
                    put("yearItems", itemList);
                    put("writeUrl", StrUtil.isBlank(finalWriteUrl) ? null :
                            Pictures.ofLocal(imgUrl + "/" + finalWriteUrl).create());
                    put("countersignUrl1", StrUtil.isBlank(countersignUrl1) ? null :
                            Pictures.ofLocal(imgUrl + "/" + countersignUrl1).create());
                    put("countersignUrl2", StrUtil.isBlank(countersignUrl2) ? null :
                            Pictures.ofLocal(imgUrl + "/" + countersignUrl2).create());
                    put("countersignUrl3", StrUtil.isBlank(countersignUrl3) ? null :
                            Pictures.ofLocal(imgUrl + "/" + countersignUrl3).create());
                    put("countersignUrl4", StrUtil.isBlank(countersignUrl4) ? null :
                            Pictures.ofLocal(imgUrl + "/" + countersignUrl4).create());
                    put("countersignUrl5", StrUtil.isBlank(countersignUrl5) ? null :
                            Pictures.ofLocal(imgUrl + "/" + countersignUrl5).create());
                    put("countersignUrl6", StrUtil.isBlank(countersignUrl6) ? null :
                            Pictures.ofLocal(imgUrl + "/" + countersignUrl6).create());
                    put("countersignUrl7", StrUtil.isBlank(countersignUrl7) ? null :
                            Pictures.ofLocal(imgUrl + "/" + countersignUrl7).create());
                    put("examineUrl", StrUtil.isBlank(finalExamineUrl) ? null :
                            Pictures.ofLocal(imgUrl + "/" + finalExamineUrl).create());
                    put("ratifyUrl", StrUtil.isBlank(finalRatifyUrl) ? null :
                            Pictures.ofLocal(imgUrl + "/" + finalRatifyUrl).create());
                    put("createDateUrl", Pictures.ofStream(DateImageUtil.createDateImage(spotCheckYear.getCreateTime())).create());
                }});
        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    spotCheckYear.getYearHead(), "UTF-8");
            response.setHeader("Content-disposition",
                    "attachment;filename=" + fileName + ".docx");
            OutputStream os = response.getOutputStream();
            template.write(os);
            os.flush();
            os.close();
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }


}
