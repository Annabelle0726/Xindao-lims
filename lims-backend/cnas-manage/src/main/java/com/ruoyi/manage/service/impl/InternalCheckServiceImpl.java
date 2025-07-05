package com.ruoyi.manage.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
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
import com.ruoyi.manage.dto.InternalCheckDto;
import com.ruoyi.manage.mapper.InternalCheckMapper;
import com.ruoyi.manage.pojo.InternalCheck;
import com.ruoyi.manage.pojo.InternalCheckDetail;
import com.ruoyi.manage.service.InternalCheckDetailService;
import com.ruoyi.manage.service.InternalCheckService;
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
 * 内审检查表
 *
 * @author zhuo
 * @since 2024-11-11
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class InternalCheckServiceImpl extends ServiceImpl<InternalCheckMapper, InternalCheck> implements InternalCheckService {

    @Resource
    private InternalCheckDetailService internalCheckDetailService;
    @Resource
    private UserMapper userMapper;
    @Value("${file.path}")
    private String imgUrl;

    /**
     * 内审检查分页查询
     * @param page
     * @param internalCheck
     * @return
     */
    @Override
    public IPage<InternalCheckDto> pageInternalCheck(Page page, InternalCheck internalCheck) {
        return baseMapper.pageInternalCheck(page, QueryWrappers.queryWrappers(internalCheck));
    }

    /**
     * 内审检查新增
     * @param internalCheck
     * @return
     */
    @Override
    public boolean addInternalCheck(InternalCheckDto internalCheck) {
        Integer userId = SecurityUtils.getUserId().intValue();
        // 添加编制人
        User user = userMapper.selectById(userId);
        internalCheck.setWriteTime(LocalDateTime.now());
        internalCheck.setWriteUserId(user.getId());
        internalCheck.setWriteUserName(user.getName());

        baseMapper.insert(internalCheck);
        // 新增详情
        for (InternalCheckDetail internalCheckDetail : internalCheck.getCheckDetailList()) {
            internalCheckDetail.setCheckId(internalCheck.getCheckId());
        }
        internalCheckDetailService.saveBatch(internalCheck.getCheckDetailList());
        return true;
    }

    /**
     * 内审检查修改
     * @param internalCheck
     * @return
     */
    @Override
    public boolean updateInternalCheck(InternalCheckDto internalCheck) {
        baseMapper.updateById(internalCheck);

        // 删除之前的详情
        internalCheckDetailService.remove(Wrappers.<InternalCheckDetail>lambdaQuery()
                .eq(InternalCheckDetail::getCheckId, internalCheck.getCheckId()));

        // 新增详情
        for (InternalCheckDetail internalCheckDetail : internalCheck.getCheckDetailList()) {
            internalCheckDetail.setCheckId(internalCheck.getCheckId());
        }
        internalCheckDetailService.saveBatch(internalCheck.getCheckDetailList());

        return true;
    }

    /**
     * 内审检查删除
     * @param CheckId
     * @return
     */
    @Override
    public boolean delInternalCheck(Integer CheckId) {
        internalCheckDetailService.remove(Wrappers.<InternalCheckDetail>lambdaQuery()
                .eq(InternalCheckDetail::getCheckId, CheckId));
        baseMapper.deleteById(CheckId);
        return true;
    }

    /**
     * 内审检查查看详情
     * @param CheckId
     * @return
     */
    @Override
    public InternalCheckDto getInternalCheckOne(Integer CheckId) {
        InternalCheck internalCheck = baseMapper.selectById(CheckId);
        InternalCheckDto internalCheckDto = new InternalCheckDto();
        BeanUtils.copyProperties(internalCheck, internalCheckDto);

        // 查询详细信息
        internalCheckDto.setCheckDetailList(internalCheckDetailService.list(Wrappers.<InternalCheckDetail>lambdaQuery()
                .eq(InternalCheckDetail::getCheckId, CheckId)));
        return internalCheckDto;
    }

    /**
     * 内审检查批准
     * @param internalCheck
     * @return
     */
    @Override
    public boolean ratifyInternalCheck(InternalCheckDto internalCheck) {
        Integer userId = SecurityUtils.getUserId().intValue();
        User user = userMapper.selectById(userId);
        baseMapper.update(null, Wrappers.<InternalCheck>lambdaUpdate()
                .eq(InternalCheck::getCheckId, internalCheck.getCheckId())
                .set(InternalCheck::getRatifyUserId, userId)
                .set(InternalCheck::getRatifyUserName, user.getName())
                .set(InternalCheck::getRatifyRemark, internalCheck.getRatifyRemark())
                .set(InternalCheck::getRatifyStatus, internalCheck.getRatifyStatus())
                .set(InternalCheck::getRatifyTime, LocalDateTime.now())
        );
        return true;
    }

    /**
     * 导出内审检查
     * @param checkId
     * @param response
     */
    @Override
    public void exportInternalCheck(Integer checkId, HttpServletResponse response) {
        InternalCheck internalCheck = baseMapper.selectById(checkId);

        //获取提交人的签名地址
        String writeUrl = userMapper.selectById(internalCheck.getWriteUserId()).getSignatureUrl();
        if (ObjectUtils.isEmpty(writeUrl) || writeUrl.equals("")) {
            throw new ErrorException("找不到检验人的签名");
        }

        //获取批准人的签名地址
        String ratifyUrl = null;
        if (internalCheck.getRatifyUserId() != null) {
            ratifyUrl = userMapper.selectById(internalCheck.getRatifyUserId()).getSignatureUrl();
            if (StringUtils.isBlank(ratifyUrl)) {
                throw new ErrorException("找不到复核人的签名");
            }
        }

        // 查询详情
        List<InternalCheckDetail> internalCheckDetails = internalCheckDetailService.list(Wrappers.<InternalCheckDetail>lambdaQuery()
                .eq(InternalCheckDetail::getCheckId, checkId));

        int index = 1;
        for (InternalCheckDetail detail : internalCheckDetails) {
            detail.setIndex(index);
            index++;
        }

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/internal-check.docx");
        String finalRatifyUrl = ratifyUrl;
        Configure configure = Configure.builder()
                .bind("checkDetailList", new HackLoopTableRenderPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("check", internalCheck);
                    put("checkDetailList", internalCheckDetails);
                    put("writeUrl", StringUtils.isNotBlank(writeUrl) ? Pictures.ofLocal(imgUrl + "/" + writeUrl).create() : null);
                    put("ratifyUrl", StringUtils.isNotBlank(finalRatifyUrl) ? Pictures.ofLocal(imgUrl + "/" + finalRatifyUrl).create() : null);
                    put("writeDateUrl", internalCheck.getWriteTime() != null ?
                            Pictures.ofStream(DateImageUtil.createDateImage(internalCheck.getWriteTime())).create() : null);
                    put("ratifyDateUrl", internalCheck.getRatifyTime() != null ?
                            Pictures.ofStream(DateImageUtil.createDateImage(internalCheck.getRatifyTime())).create() : null);
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "内审检查", "UTF-8");
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

