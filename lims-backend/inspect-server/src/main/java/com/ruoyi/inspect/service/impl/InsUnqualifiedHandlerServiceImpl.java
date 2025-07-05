package com.ruoyi.inspect.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.oa.OAProcess;
import com.ruoyi.common.oa.OAProperty;
import com.ruoyi.common.oa.OAResult;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.inspect.dto.PushOADto;
import com.ruoyi.inspect.dto.UnqualifiedHandlerDto;
import com.ruoyi.inspect.mapper.InsOrderMapper;
import com.ruoyi.inspect.mapper.InsUnqualifiedHandlerMapper;
import com.ruoyi.inspect.pojo.CommonOa;
import com.ruoyi.inspect.pojo.InsOrder;
import com.ruoyi.inspect.pojo.InsUnqualifiedHandler;
import com.ruoyi.inspect.pojo.InsUnqualifiedHandlerFile;
import com.ruoyi.inspect.service.CommonOaService;
import com.ruoyi.inspect.service.InsUnqualifiedHandlerFileService;
import com.ruoyi.inspect.service.InsUnqualifiedHandlerService;
import com.ruoyi.inspect.vo.UnqualifiedHandlerVO;
import com.ruoyi.system.mapper.UserMapper;
import com.ruoyi.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 27233
 * @description 针对表【ins_unqualified_handler(不合格处理表)】的数据库操作Service实现
 * @createDate 2024-07-31 13:38:14
 */
@Service
@Slf4j
public class InsUnqualifiedHandlerServiceImpl extends ServiceImpl<InsUnqualifiedHandlerMapper, InsUnqualifiedHandler>
        implements InsUnqualifiedHandlerService {

    @Resource
    private CommonOaService commonOaService;
    @Resource
    private OAProperty oaProperty;
    @Resource
    private InsUnqualifiedHandlerFileService insUnqualifiedHandlerFileService;
    @Resource
    private InsOrderMapper insOrderMapper;


    @Override
    public IPage<UnqualifiedHandlerVO> pageList(Page page, UnqualifiedHandlerDto unqualifiedHandlerDto) {
        return baseMapper.selectPageList(page, QueryWrappers.queryWrappers(unqualifiedHandlerDto));
    }

    @Override
    public Result pushOA(PushOADto pushOADto) {
        //获取不合格处理记录
        UnqualifiedHandlerVO vo = baseMapper.findById(pushOADto.getHandlerId());

        if (vo.getRequestId() != null) {
            throw new ErrorException("该不合格处理已提交过OA");
        }

        //提交oa相关字段赋值
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Map<String, String> mainFields = new HashMap<>();
        mainFields.put("bh", vo.getNo());//编号
        mainFields.put("gysmc", vo.getSupplierName());//供应商名称
        mainFields.put("wlmc", vo.getMaterialName());//物料名称
        mainFields.put("scpc", vo.getProductionBatch());//生产批次
        mainFields.put("dhsl", vo.getCargoQuantity());//到货数量
        mainFields.put("ggxh", vo.getSpecsModels());//规格型号
        mainFields.put("bjrq", vo.getInspectTime().format(formatter));//报检日期
        mainFields.put("fkr", SecurityUtils.getUsername());//反馈人
        mainFields.put("fkrq", vo.getFeedbackTime().format(formatter));//反馈日期
        mainFields.put("fl", vo.getClassification());//分类
        mainFields.put("bhggs", vo.getOffGradeAscription());//不合格归属
        mainFields.put("bhgqkms", vo.getUnqualifiedDesc());//不合格情况描述

        //查询附件
        List<InsUnqualifiedHandlerFile> handlerFiles = insUnqualifiedHandlerFileService.list(Wrappers.<InsUnqualifiedHandlerFile>lambdaQuery()
                .eq(InsUnqualifiedHandlerFile::getUnqualifiedId, vo.getHandlerId()));
        if (CollectionUtils.isNotEmpty(handlerFiles)) {
            StringBuilder fileUrl = new StringBuilder();
            for (int i = 0; i < handlerFiles.size(); i++) {
                String path = handlerFiles.get(i).getType().equals(1) ? "/lims/img/" : "/lims/word/";
                if (i == handlerFiles.size() - 1) {
                    fileUrl.append("<a href='" + oaProperty.getProdIp()).append(path + handlerFiles.get(i).getFileUrl()
                            + "'target='_blank'>" + handlerFiles.get(i).getFileName() + "</a>");
                } else {
                    fileUrl.append("<a href='" + oaProperty.getProdIp()).append(path + handlerFiles.get(i).getFileUrl()
                            + "'target='_blank'>" + handlerFiles.get(i).getFileName() + "</a>").append("<br/>");
                }
            }
            mainFields.put("xlimsfj", fileUrl.toString());
        }
        //流程标题
        String requestName = vo.getHeadline();
        //发起OA
        boolean oa = false;
        try {
            log.info("发起不合格处理OA审核流程");
            String unqualifiedProcessId = oaProperty.getUnqualifiedProcessId();
            OAResult oaResult = OAProcess.start(mainFields, requestName, unqualifiedProcessId,SecurityUtils.getUsername());
            log.info("不合格处理OA审核流程结束，返回结果->{}" + oaResult);
            oa = oaResult.success();
            if (oa) {
                String addWorkflowResult = oaResult.getAddWorkflowResult();
                baseMapper.update(null, new LambdaUpdateWrapper<InsUnqualifiedHandler>()
                        .set(InsUnqualifiedHandler::getRequestId, addWorkflowResult)
                        .set(InsUnqualifiedHandler::getOaState, 2)
                        .eq(InsUnqualifiedHandler::getId, pushOADto.getHandlerId()));
            } else {
                return Result.fail("提交oa失败: " + oaResult.getErrorMsg());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unqualifiedHandlerOACallback(Long oaWorkId, String checkResult, JSONArray objects) {
        // 没有这个就结束
        Long count1 = baseMapper.selectCount(Wrappers.<InsUnqualifiedHandler>lambdaQuery()
                .eq(InsUnqualifiedHandler::getRequestId, Integer.valueOf(String.valueOf(oaWorkId))));
        if (count1 == 0) {
            return;
        }

        //oa状态：3通过 4驳回
        Integer oaState = "1".equals(checkResult) ? 3 : 4;
        baseMapper.update(null, new LambdaUpdateWrapper<InsUnqualifiedHandler>()
                .set(InsUnqualifiedHandler::getOaState, oaState)
                .ge(InsUnqualifiedHandler::getRequestId, Integer.valueOf(String.valueOf(oaWorkId))));
        //保存oa审批流程记录
        List<CommonOa> commonOaList = new ArrayList<>();
        objects.forEach(l -> {
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(l));
            CommonOa oa = new CommonOa();
            oa.setApprover(jsonObject.getString("operatorName"));
            oa.setWorkflowId(oaWorkId);
            oa.setOperation(jsonObject.getString("operateType"));
            oa.setApprovalDate(jsonObject.getString("operateDate"));
            oa.setApprovalOpinion(jsonObject.getString("remark"));
            oa.setNodeName(jsonObject.getString("nodeName"));
            oa.setApprovalTime(jsonObject.getString("operateTime"));

            //查询判断之前是否添加过
            long count = commonOaService.count(Wrappers.<CommonOa>lambdaQuery()
                    .eq(CommonOa::getApprover, oa.getApprover())
                    .eq(CommonOa::getWorkflowId, oa.getWorkflowId())
                    .eq(CommonOa::getOperation, oa.getOperation())
                    .eq(CommonOa::getApprovalDate, oa.getApprovalDate())
                    .eq(CommonOa::getApprovalOpinion, oa.getApprovalOpinion())
                    .eq(CommonOa::getNodeName, oa.getNodeName())
                    .eq(CommonOa::getApprovalTime, oa.getApprovalTime())
            );
            // 没有重复添加
            if (count == 0) {
                commonOaList.add(oa);
            }
        });
        commonOaService.saveBatch(commonOaList);

    }

    /**
     * 查看oa流程
     * @param id
     * @return
     */
    @Override
    public List<CommonOa> getOaFlow(Integer id) {
        return baseMapper.getOaFlow(id);
    }

    /**
     * 新增不合格处理
     * @param unqualifiedHandlerDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addUnqualifiedHandler(UnqualifiedHandlerDto unqualifiedHandlerDto) {
        if (unqualifiedHandlerDto.getInventoryQuantityId() == null) {
            throw new ErrorException("缺少原材料id");
        }
        long count = this.count(Wrappers.<InsUnqualifiedHandler>lambdaQuery()
                .eq(InsUnqualifiedHandler::getInsOrderId, unqualifiedHandlerDto.getInsOrderId()));
        if (count > 0) {
            throw new ErrorException("当前检验单已提交过不合格");
        }
        // 原材料编号切割
        InsOrder order = insOrderMapper.selectOne(Wrappers.<InsOrder>lambdaQuery()
                .eq(InsOrder::getId, unqualifiedHandlerDto.getInsOrderId())
                .select(InsOrder::getEntrustCode));
        unqualifiedHandlerDto.setNo(order.getEntrustCode());

        this.save(unqualifiedHandlerDto);
        if (CollectionUtils.isNotEmpty(unqualifiedHandlerDto.getUnqualifiedHandlerFiles())) {
            for (InsUnqualifiedHandlerFile unqualifiedHandlerFile : unqualifiedHandlerDto.getUnqualifiedHandlerFiles()) {
                unqualifiedHandlerFile.setUnqualifiedId(unqualifiedHandlerDto.getId());
            }
            insUnqualifiedHandlerFileService.updateBatchById(unqualifiedHandlerDto.getUnqualifiedHandlerFiles());
        }

        return true;
    }

    /**
     * 编辑不合格处理
     * @param unqualifiedHandlerDto
     * @return
     */
    @Override
    public boolean updateUnqualifiedHandler(UnqualifiedHandlerDto unqualifiedHandlerDto) {
        // 删除附件, 重新添加
        insUnqualifiedHandlerFileService.remove(Wrappers.<InsUnqualifiedHandlerFile>lambdaQuery()
                .eq(InsUnqualifiedHandlerFile::getUnqualifiedId, unqualifiedHandlerDto.getId()));
        if (CollectionUtils.isNotEmpty(unqualifiedHandlerDto.getUnqualifiedHandlerFiles())) {
            for (InsUnqualifiedHandlerFile unqualifiedHandlerFile : unqualifiedHandlerDto.getUnqualifiedHandlerFiles()) {
                unqualifiedHandlerFile.setUnqualifiedId(unqualifiedHandlerDto.getId());
            }
            insUnqualifiedHandlerFileService.updateBatchById(unqualifiedHandlerDto.getUnqualifiedHandlerFiles());
        }

        return this.updateById(unqualifiedHandlerDto);
    }

    /**
     * 查看不合格处理界面
     * @param id
     * @return
     */
    @Override
    public UnqualifiedHandlerDto getUnqualifiedHandler(Integer id) {
        InsUnqualifiedHandler insUnqualifiedHandler = this.getById(id);
        UnqualifiedHandlerDto unqualifiedHandlerDto = new UnqualifiedHandlerDto();
        BeanUtil.copyProperties(insUnqualifiedHandler, unqualifiedHandlerDto);
        // 查看附件
        List<InsUnqualifiedHandlerFile> list = insUnqualifiedHandlerFileService.list(Wrappers.<InsUnqualifiedHandlerFile>lambdaQuery()
                .eq(InsUnqualifiedHandlerFile::getUnqualifiedId, unqualifiedHandlerDto.getId()));
        unqualifiedHandlerDto.setUnqualifiedHandlerFiles(list);
        return unqualifiedHandlerDto;
    }

    /**
     * 删除不合格
     * @param id
     * @return
     */
    @Override
    public boolean deleteUnqualifiedHandler(Integer id) {
        insUnqualifiedHandlerFileService.remove(Wrappers.<InsUnqualifiedHandlerFile>lambdaQuery()
                .eq(InsUnqualifiedHandlerFile::getUnqualifiedId, id));
        this.removeById(id);
        return true;
    }
}




