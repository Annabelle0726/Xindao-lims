package com.ruoyi.performance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.performance.pojo.ShiftTime;

import java.util.List;

/**
 * <p>
 * 班次对应的时间 服务类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-07-24 11:22:17
 */
public interface ShiftTimeService extends IService<ShiftTime> {

    void shiftTimeAdd(ShiftTime shiftTime);

    List<ShiftTime> shiftTimeList();

}
