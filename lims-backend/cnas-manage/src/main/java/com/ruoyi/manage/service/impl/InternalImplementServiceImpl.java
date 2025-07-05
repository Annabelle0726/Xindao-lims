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
import com.ruoyi.common.utils.DateImageUtil;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import com.ruoyi.manage.dto.InternalImplementDto;

import com.ruoyi.manage.mapper.InternalImplementMapper;

import com.ruoyi.manage.pojo.InternalImplement;
import com.ruoyi.manage.pojo.InternalImplementDetail;

import com.ruoyi.manage.service.InternalImplementDetailService;
import com.ruoyi.manage.service.InternalImplementService;



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
 * 内审实施计划
 *
 * @author zhuo
 * @since 2024-11-11
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class InternalImplementServiceImpl extends ServiceImpl<InternalImplementMapper, InternalImplement> implements InternalImplementService {

    @Resource
    private InternalImplementDetailService internalImplementDetailService;

    @Resource
    private UserMapper userMapper;
    @Value("${file.path}")
    private String imgUrl;

    /**
     * 内审实施计划分页查询
     * @param page
     * @param internalImplement
     * @return
     */
    @Override
    public IPage<InternalImplementDto> pageInternalImplement(Page page, InternalImplement internalImplement) {
        return baseMapper.pageInternalImplement(page, QueryWrappers.queryWrappers(internalImplement));
    }

    /**
     * 内审实施计划新增
     * @param internalImplement
     * @return
     */
    @Override
    public boolean addInternalImplement(InternalImplementDto internalImplement) {
        Integer userId = SecurityUtils.getUserId().intValue();
        // 添加编制人
        User user = userMapper.selectById(userId);
        internalImplement.setWriteTime(LocalDateTime.now());
        internalImplement.setWriteUserId(user.getId());
        internalImplement.setWriteUserName(user.getName());

        baseMapper.insert(internalImplement);
        // 新增详情
        for (InternalImplementDetail internalImplementDetail : internalImplement.getImplementDetailList()) {
            internalImplementDetail.setImplementId(internalImplement.getImplementId());
        }
        internalImplementDetailService.saveBatch(internalImplement.getImplementDetailList());
        return true;
    }

    /**
     * 内审实施计划修改
     * @param internalImplement
     * @return
     */
    @Override
    public boolean updateInternalImplement(InternalImplementDto internalImplement) {
        baseMapper.updateById(internalImplement);

        // 删除之前的详情
        internalImplementDetailService.remove(Wrappers.<InternalImplementDetail>lambdaQuery()
                .eq(InternalImplementDetail::getImplementId, internalImplement.getImplementId()));

        // 新增详情
        for (InternalImplementDetail internalImplementDetail : internalImplement.getImplementDetailList()) {
            internalImplementDetail.setImplementId(internalImplement.getImplementId());
        }
        internalImplementDetailService.saveBatch(internalImplement.getImplementDetailList());

        return true;
    }

    /**
     * 内审实施计划删除
     * @param implementId
     * @return
     */
    @Override
    public boolean delInternalImplement(Integer implementId) {
        internalImplementDetailService.remove(Wrappers.<InternalImplementDetail>lambdaQuery()
                .eq(InternalImplementDetail::getImplementId, implementId));
        baseMapper.deleteById(implementId);
        return true;
    }

    /**
     * 内审实施计划查看详情
     * @param implementId
     * @return
     */
    @Override
    public InternalImplementDto getInternalImplementOne(Integer implementId) {
        InternalImplement internalImplement = baseMapper.selectById(implementId);
        InternalImplementDto internalImplementDto = new InternalImplementDto();
        BeanUtils.copyProperties(internalImplement, internalImplementDto);

        // 查询详细信息
        internalImplementDto.setImplementDetailList(internalImplementDetailService.list(Wrappers.<InternalImplementDetail>lambdaQuery()
                .eq(InternalImplementDetail::getImplementId, implementId)));
        return internalImplementDto;
    }

    /**
     * 内审实施计划批准
     * @param internalImplement
     * @return
     */
    @Override
    public boolean ratifyInternalImplement(InternalImplementDto internalImplement) {
        Integer userId = SecurityUtils.getUserId().intValue();
        User user = userMapper.selectById(userId);
        baseMapper.update(null, Wrappers.<InternalImplement>lambdaUpdate()
                .eq(InternalImplement::getImplementId, internalImplement.getImplementId())
                .set(InternalImplement::getRatifyUserId, userId)
                .set(InternalImplement::getRatifyUserName, user.getName())
                .set(InternalImplement::getRatifyRemark, internalImplement.getRatifyRemark())
                .set(InternalImplement::getRatifyStatus, internalImplement.getRatifyStatus())
                .set(InternalImplement::getRatifyTime, LocalDateTime.now())
        );
        return true;
    }

    /**
     * 导出内审实施计划
     * @param implementId
     * @param response
     */
    @Override
    public void exportInternalImplement(Integer implementId, HttpServletResponse response) {
        InternalImplement internalImplement = baseMapper.selectById(implementId);

        //获取提交人的签名地址
        String writeUrl = userMapper.selectById(internalImplement.getWriteUserId()).getSignatureUrl();
        if (StringUtils.isBlank(writeUrl)) {
            throw new ErrorException("找不到检验人的签名");
        }

        //获取批准人的签名地址
        String ratifyUrl = null;
        if (internalImplement.getRatifyUserId() != null) {
            ratifyUrl = userMapper.selectById(internalImplement.getRatifyUserId()).getSignatureUrl();
            if (StringUtils.isBlank(ratifyUrl)) {
                throw new ErrorException("找不到批准人的签名");
            }
        }

        // 查询详情
        List<InternalImplementDetail> detailList = internalImplementDetailService.list(Wrappers.<InternalImplementDetail>lambdaQuery()
                .eq(InternalImplementDetail::getImplementId, implementId));

        int index = 1;
        for (InternalImplementDetail detail : detailList) {
            detail.setIndex(index);
            index++;
        }

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/internal-implement.docx");
        String finalRatifyUrl = ratifyUrl;
        Configure configure = Configure.builder()
                .bind("implementDetailList", new HackLoopTableRenderPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("implement", internalImplement);
                    put("implementDetailList", detailList);
                    put("writeUrl", StringUtils.isNotBlank(writeUrl) ? Pictures.ofLocal(imgUrl + "/" + writeUrl).create() : null);
                    put("ratifyUrl", StringUtils.isNotBlank(finalRatifyUrl) ? Pictures.ofLocal(imgUrl + "/" + finalRatifyUrl).create() : null);
                    put("writeDateUrl", internalImplement.getWriteTime() != null ?
                            Pictures.ofStream(DateImageUtil.createDateImage(internalImplement.getWriteTime())).create() : null);
                    put("ratifyDateUrl", internalImplement.getRatifyTime() != null ?
                            Pictures.ofStream(DateImageUtil.createDateImage(internalImplement.getRatifyTime())).create() : null);
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    internalImplement.getPurposes(), "UTF-8");
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

