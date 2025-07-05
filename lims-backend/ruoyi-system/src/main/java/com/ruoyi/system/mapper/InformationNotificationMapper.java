package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.entity.InformationNotification;
import com.ruoyi.common.core.domain.entity.InformationNotificationDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 消息通知 Mapper 接口
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-04-23 02:14:30
 */
public interface InformationNotificationMapper extends BaseMapper<InformationNotification> {

    IPage<InformationNotificationDto> getPage(Page page, String messageType, Integer userId);

    /**
     * 消息通知-滚动查询
     *
     * @param userId
     * @return
     */
    IPage<InformationNotificationDto> msgRoll(Page page,@Param("userId") Integer userId);
}
