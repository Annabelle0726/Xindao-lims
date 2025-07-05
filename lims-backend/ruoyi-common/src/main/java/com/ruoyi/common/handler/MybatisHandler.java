package com.ruoyi.common.handler;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MybatisHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Integer userId = null;
        try {
            userId = SecurityUtils.getUserId().intValue();
        } catch (Exception ignored) {
        }
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime",  LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "createUser", Integer.class, userId);
        this.strictInsertFill(metaObject, "updateUser", Integer.class, userId);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Integer userId = null;
        try {
            userId = SecurityUtils.getUserId().intValue();
        } catch (Exception ignored) {
        }
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now()); // 起始版本 3.3.0(推荐)
        this.strictUpdateFill(metaObject, "updateUser", Integer.class, userId);
    }
}
