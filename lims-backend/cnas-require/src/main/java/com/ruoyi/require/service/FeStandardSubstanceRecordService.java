package com.ruoyi.require.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.require.pojo.FeStandardSubstanceRecord;
import com.ruoyi.require.vo.SubstanceRecordVo;

import java.util.List;

/**
 * <p>
 * 标准物质清单借用归还记录表 服务类
 * </p>
 *
 * @author
 * @since 2024-11-14 01:49:11
 */
public interface FeStandardSubstanceRecordService extends IService<FeStandardSubstanceRecord> {


    void borrowSubstance(FeStandardSubstanceRecord feStandardSubstanceRecord);

    void returnSubstance(FeStandardSubstanceRecord feStandardSubstanceRecord);

    List<FeStandardSubstanceRecord> getSubstanceRecord(Integer id);

    IPage<SubstanceRecordVo> getPage(Page page, SubstanceRecordVo vo);

}
