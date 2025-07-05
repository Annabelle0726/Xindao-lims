package com.ruoyi.inspect.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.inspect.dto.InsReportExport;
import com.ruoyi.inspect.dto.ReportPageDto;
import com.ruoyi.inspect.pojo.InsReport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Administrator
* @description 针对表【ins_report(检验报告)】的数据库操作Mapper
* @createDate 2024-03-17 22:10:02
* @Entity com.yuanchu.mom.pojo.InsReport
*/
public interface InsReportMapper extends BaseMapper<InsReport> {

    /**
     * 查询检验任务
     * @param userId 当前登录人id
     * @param queryStatus  状态
     * @param createOrderUser  是否是只有查看自己的权限
     * @return
     */
    IPage<ReportPageDto> pageInsReport(IPage<ReportPageDto> page,
                                       @Param("ew") QueryWrapper<ReportPageDto> ew,
                                       @Param("laboratory") String laboratory,
                                       @Param("userId") Integer userId,
                                       @Param("queryStatus") String queryStatus,
                                       @Param("createOrderUser") Integer createOrderUser);

    String getLaboratoryByName(@Param("name") String name, @Param("type") String type);

    /**
     * 报告报表导出
     * @return
     */
    List<InsReportExport> reportAllExport(@Param("ew") QueryWrapper<ReportPageDto> ew,
                                          @Param("userId") Integer userId,
                                          @Param("queryStatus") String queryStatus,
                                          @Param("createOrderUser") Integer createOrderUser);
}




