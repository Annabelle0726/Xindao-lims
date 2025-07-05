package com.ruoyi.process.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;

import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import com.ruoyi.process.dto.InconsistentDistributionDto;
import com.ruoyi.process.dto.InconsistentDistributionProportionDto;
import com.ruoyi.process.mapper.InconsistentDistributionMapper;
import com.ruoyi.process.pojo.InconsistentDistribution;
import com.ruoyi.process.pojo.InconsistentDistributionDetail;
import com.ruoyi.process.service.InconsistentDistributionDetailService;
import com.ruoyi.process.service.InconsistentDistributionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 不符合项的分布 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-15 09:53:20
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class InconsistentDistributionServiceImpl extends ServiceImpl<InconsistentDistributionMapper, InconsistentDistribution> implements InconsistentDistributionService {

    private InconsistentDistributionDetailService inconsistentDistributionDetailService;


    /**
     * 不符合项分布查询
     * @param page
     * @param inconsistentDistributionDto
     * @return
     */
    @Override
    public IPage<InconsistentDistributionDto> pageInconsistentDistribution(Page page, InconsistentDistribution inconsistentDistributionDto) {
        return baseMapper.pageInconsistentDistribution(page, QueryWrappers.queryWrappers(inconsistentDistributionDto));
    }

    /**
     * 不符合项分布新增
     * @param inconsistentDistributionDto
     * @return
     */
    @Override
    public boolean addInconsistentDistribution(InconsistentDistributionDto inconsistentDistributionDto) {
        baseMapper.insert(inconsistentDistributionDto);
        // 新增详情
        for (InconsistentDistributionDetail distributionDetail : inconsistentDistributionDto.getDistributionDetailList()) {
            distributionDetail.setDistributionId(inconsistentDistributionDto.getDistributionId());
        }
        inconsistentDistributionDetailService.saveBatch(inconsistentDistributionDto.getDistributionDetailList());
        return true;
    }

    /**
     * 不符合项分布修改
     * @param inconsistentDistributionDto
     * @return
     */
    @Override
    public boolean updateInconsistentDistribution(InconsistentDistributionDto inconsistentDistributionDto) {
        inconsistentDistributionDetailService.updateBatchById(inconsistentDistributionDto.getDistributionDetailList());
        return true;
    }

    /**
     * 不符合项分布删除
     * @param distributionId
     * @return
     */
    @Override
    public boolean delInconsistentDistribution(Integer distributionId) {
        inconsistentDistributionDetailService.remove(Wrappers.<InconsistentDistributionDetail>lambdaQuery()
                .eq(InconsistentDistributionDetail::getDistributionId, distributionId));
        baseMapper.deleteById(distributionId);
        return true;
    }

    /**
     * 不符合项分布查看详情
     * @param distributionId
     * @return
     */
    @Override
    public InconsistentDistributionDto getInconsistentDistributionOne(Integer distributionId) {
        InconsistentDistributionDto distributionDto = new InconsistentDistributionDto();
        InconsistentDistribution inconsistentDistribution = baseMapper.selectById(distributionId);
        BeanUtils.copyProperties(distributionDto, inconsistentDistribution);
        // 查询详细
        List<InconsistentDistributionDetail> distributionDetailList = inconsistentDistributionDetailService.list(Wrappers.<InconsistentDistributionDetail>lambdaQuery()
                .eq(InconsistentDistributionDetail::getDistributionId, distributionId));

        // 主任汇总
        int directorSum = distributionDetailList.stream()
                .filter(detail -> detail.getDirector() != null)
                .mapToInt(InconsistentDistributionDetail::getDirector)
                .sum();

        // 技术负责人汇总
        int technologySum = distributionDetailList.stream()
                .filter(detail -> detail.getTechnology() != null)
                .mapToInt(InconsistentDistributionDetail::getTechnology)
                .sum();

        // 质量负责人汇总
        int qualitySum = distributionDetailList.stream()
                .filter(detail -> detail.getQuality() != null)
                .mapToInt(InconsistentDistributionDetail::getQuality)
                .sum();

        // 综合室汇总
        int comprehensiveSum = distributionDetailList.stream()
                .filter(detail -> detail.getComprehensive() != null)
                .mapToInt(InconsistentDistributionDetail::getComprehensive)
                .sum();

        // 试验室汇总
        int testingSum = distributionDetailList.stream()
                .filter(detail -> detail.getTesting() != null)
                .mapToInt(InconsistentDistributionDetail::getTesting)
                .sum();

        // 总数汇总
        int sum = directorSum + technologySum + qualitySum + comprehensiveSum + testingSum;

        //计算合计
        for (InconsistentDistributionDetail distributionDetail : distributionDetailList) {
            // 总数
            Integer total = (distributionDetail.getDirector() != null ? distributionDetail.getDirector() : 0)
                    + (distributionDetail.getTechnology() != null ? distributionDetail.getTechnology() : 0)
                    + (distributionDetail.getQuality() != null ? distributionDetail.getQuality() : 0)
                    + (distributionDetail.getComprehensive() != null ? distributionDetail.getComprehensive() : 0)
                    + (distributionDetail.getTesting() != null ? distributionDetail.getTesting() : 0);
            distributionDetail.setTotal(total);
            // 占比
            distributionDetail.setProportion(calculatePercentage(total, sum));
        }
        // 添加最后一行占比对象
        InconsistentDistributionProportionDto proportionDto = new InconsistentDistributionProportionDto();
        proportionDto.setEssentials("占比 %");
        proportionDto.setDirector(calculatePercentage(directorSum, sum));
        proportionDto.setTechnology(calculatePercentage(technologySum, sum));
        proportionDto.setQuality(calculatePercentage(qualitySum, sum));
        proportionDto.setComprehensive(calculatePercentage(comprehensiveSum, sum));
        proportionDto.setTesting(calculatePercentage(testingSum, sum));
        proportionDto.setTotal(sum);

        distributionDto.setDistributionDetailList(distributionDetailList);
        distributionDto.setDistributionProportion(proportionDto);

        return distributionDto;
    }

    /**
     * 计算占比
     * @return
     */
    public BigDecimal calculatePercentage(Integer numeratorNum, Integer denominatorNum) {
        BigDecimal numerator = new BigDecimal(numeratorNum);
        BigDecimal denominator = new BigDecimal(denominatorNum);

        // 检查除数是否为0
        if (denominator.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal ratio = numerator.divide(denominator, 4, RoundingMode.HALF_UP); // 保留4位小数
        BigDecimal percentage = ratio.multiply(new BigDecimal("100"));
        BigDecimal percentageRounded = percentage.setScale(2, RoundingMode.HALF_UP);
        return percentageRounded;
    }


    /**
     * 导出不符合的分布
     * @param distributionId
     * @param response
     */
    @Override
    public void exportInconsistentDistribution(Integer distributionId, HttpServletResponse response) {
        InconsistentDistributionDto inconsistentDistributionOne = getInconsistentDistributionOne(distributionId);

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/inconsistent-distribution.docx");
        Configure configure = Configure.builder()
                .bind("distributionDetailList", new HackLoopTableRenderPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("distributionDetailList", inconsistentDistributionOne.getDistributionDetailList());
                    put("proport", inconsistentDistributionOne.getDistributionProportion());
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "不符合项的分布", "UTF-8");
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
