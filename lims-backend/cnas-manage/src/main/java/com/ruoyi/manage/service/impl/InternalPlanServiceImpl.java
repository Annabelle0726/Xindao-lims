package com.ruoyi.manage.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.Pictures;

import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import com.ruoyi.manage.dto.InternalPlanDto;

import com.ruoyi.manage.mapper.InternalPlanMapper;

import com.ruoyi.manage.pojo.InternalPlan;
import com.ruoyi.manage.pojo.InternalPlanDetail;
import com.ruoyi.manage.service.InternalPlanDetailService;
import com.ruoyi.manage.service.InternalPlanService;


import com.ruoyi.system.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 内审年度计划 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-13 03:27:47
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class InternalPlanServiceImpl extends ServiceImpl<InternalPlanMapper, InternalPlan> implements InternalPlanService {

    @Resource
    private InternalPlanDetailService internalPlanDetailService;

    @Resource
    private UserMapper userMapper;
    @Value("${file.path}")
    private String imgUrl;


    /**
     * 内审年度计划分页查询
     * @param page
     * @param internalPlan
     * @return
     */
    @Override
    public IPage<InternalPlanDto> pageInternalPlan(Page page, InternalPlan internalPlan) {
        return baseMapper.pageInternalPlan(page, QueryWrappers.queryWrappers(internalPlan));
    }

    /**
     * 内审年度计划新增
     * @param internalPlan
     * @return
     */
    @Override
    public boolean addInternalPlan(InternalPlanDto internalPlan) {
        Integer userId = SecurityUtils.getUserId().intValue();
        // 添加编制人
        User user = userMapper.selectById(userId);
        internalPlan.setWriteTime(LocalDateTime.now());
        internalPlan.setWriteUserId(user.getId());
        internalPlan.setWriteUserName(user.getName());

        baseMapper.insert(internalPlan);
        // 新增详情
        for (InternalPlanDetail internalPlanDetail : internalPlan.getPlanDetailList()) {
            internalPlanDetail.setPlanId(internalPlan.getPlanId());
        }
        internalPlanDetailService.saveBatch(internalPlan.getPlanDetailList());
        return true;
    }

    /**
     * 内审年度计划修改
     * @param internalPlan
     * @return
     */
    @Override
    public boolean updateInternalPlan(InternalPlanDto internalPlan) {
        baseMapper.updateById(internalPlan);

        // 删除之前的详情
        internalPlanDetailService.remove(Wrappers.<InternalPlanDetail>lambdaQuery()
                .eq(InternalPlanDetail::getPlanId, internalPlan.getPlanId()));

        // 新增详情
        for (InternalPlanDetail internalPlanDetail : internalPlan.getPlanDetailList()) {
            internalPlanDetail.setPlanId(internalPlan.getPlanId());
        }
        internalPlanDetailService.saveBatch(internalPlan.getPlanDetailList());

        return true;
    }

    /**
     * 内审年度计划删除
     * @param planId
     * @return
     */
    @Override
    public boolean delInternalPlan(Integer planId) {
        internalPlanDetailService.remove(Wrappers.<InternalPlanDetail>lambdaQuery()
                .eq(InternalPlanDetail::getPlanId, planId));
        baseMapper.deleteById(planId);
        return true;
    }

    /**
     * 内审年度计划查看详情
     * @param planId
     * @return
     */
    @Override
    public InternalPlanDto getInternalPlanOne(Integer planId) {
        InternalPlan internalPlan = baseMapper.selectById(planId);
        InternalPlanDto internalPlanDto = new InternalPlanDto();
        BeanUtils.copyProperties(internalPlan, internalPlanDto);

        // 查询详细信息
        internalPlanDto.setPlanDetailList(internalPlanDetailService.list(Wrappers.<InternalPlanDetail>lambdaQuery()
                .eq(InternalPlanDetail::getPlanId, planId)));
        return internalPlanDto;
    }

    /**
     * 内审年度计划审核
     * @param internalPlanDto
     * @return
     */
    @Override
    public boolean examineInternalPlan(InternalPlanDto internalPlanDto) {
        Integer userId = SecurityUtils.getUserId().intValue();
        User user = userMapper.selectById(userId);
        baseMapper.update(null, Wrappers.<InternalPlan>lambdaUpdate()
                .eq(InternalPlan::getPlanId, internalPlanDto.getPlanId())
                .set(InternalPlan::getExamineUserId, userId)
                .set(InternalPlan::getExamineUserName, user.getName())
                .set(InternalPlan::getExamineRemark, internalPlanDto.getExamineRemark())
                .set(InternalPlan::getExamineStatus, internalPlanDto.getExamineStatus())
                .set(InternalPlan::getExamineTime, LocalDateTime.now())
        );
        return true;
    }

    /**
     * 内审年度计划批准
     * @param internalPlanDto
     * @return
     */
    @Override
    public boolean ratifyInternalPlan(InternalPlanDto internalPlanDto) {
        Integer userId = SecurityUtils.getUserId().intValue();
        User user = userMapper.selectById(userId);
        baseMapper.update(null, Wrappers.<InternalPlan>lambdaUpdate()
                .eq(InternalPlan::getPlanId, internalPlanDto.getPlanId())
                .set(InternalPlan::getRatifyUserId, userId)
                .set(InternalPlan::getRatifyUserName, user.getName())
                .set(InternalPlan::getRatifyRemark, internalPlanDto.getRatifyRemark())
                .set(InternalPlan::getRatifyStatus, internalPlanDto.getRatifyStatus())
                .set(InternalPlan::getRatifyTime, LocalDateTime.now())
        );
        return true;
    }

    /**
     * 导出内审年度计划
     * @param planId
     * @param response
     */
    @Override
    public void exportInternalImplement(Integer planId, HttpServletResponse response) {
        InternalPlan internalPlan = baseMapper.selectById(planId);
        //获取提交人的签名地址
        String writeUrl = userMapper.selectById(internalPlan.getWriteUserId()).getSignatureUrl();
        if (StringUtils.isBlank(writeUrl)) {
            throw new ErrorException("找不到检验人的签名");
        }

        //获取审核人的签名地址
        String examineUrl = null;
        if (internalPlan.getExamineUserId() != null) {
            examineUrl = userMapper.selectById(internalPlan.getExamineUserId()).getSignatureUrl();
            if (StringUtils.isBlank(examineUrl)) {
                throw new ErrorException("找不到审核人的签名");
            }
        }

        //获取批准人的签名地址
        String ratifyUrl = null;
        if (internalPlan.getRatifyUserId() != null) {
            ratifyUrl = userMapper.selectById(internalPlan.getRatifyUserId()).getSignatureUrl();
            if (StringUtils.isBlank(ratifyUrl)) {
                throw new ErrorException("找不到批准人的签名");
            }
        }

        // 查询详情
        List<InternalPlanDetail> planDetails = internalPlanDetailService.list(Wrappers.<InternalPlanDetail>lambdaQuery()
                .eq(InternalPlanDetail::getPlanId, planId));

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/internal-plan.docx");
        String finalExamineUrl = examineUrl;
        String finalRatifyUrl = ratifyUrl;
        Configure configure = Configure.builder()
                .bind("planDetailList", new HackLoopTableRenderPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("plan", internalPlan);
                    put("planDetailList", planDetails);
                    put("writeUrl", StringUtils.isNotBlank(writeUrl) ? Pictures.ofLocal(imgUrl + "/" + writeUrl).create() : null);
                    put("examineUrl", StringUtils.isNotBlank(finalExamineUrl) ? Pictures.ofLocal(imgUrl + "/" + finalExamineUrl).create() : null);
                    put("ratifyUrl", StringUtils.isNotBlank(finalRatifyUrl) ? Pictures.ofLocal(imgUrl + "/" + finalRatifyUrl).create() : null);
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "内审年度计划", "UTF-8");
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

    }

}
