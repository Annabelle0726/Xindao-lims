package com.ruoyi.require.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.require.dto.ProcurementSuppliesExpendDto;
import com.ruoyi.require.pojo.ProcurementSuppliesExpends;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author
 * @since 2024-11-15 03:47:19
 */
public interface ProcurementSuppliesExpendsService extends IService<ProcurementSuppliesExpends> {
    public List<ProcurementSuppliesExpendDto> selectAll(Long procurementSuppliesListId);
    public Integer addExpends(ProcurementSuppliesExpendDto expendDto) throws ServiceException;
    public Integer deleteExpends(Long id);
}
