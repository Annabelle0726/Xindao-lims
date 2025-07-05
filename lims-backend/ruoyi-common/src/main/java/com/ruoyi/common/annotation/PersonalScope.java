package com.ruoyi.common.annotation;

import java.lang.annotation.*;

/**
 * 仅看我权限
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PersonalScope {

    /**
     * 权限标识名称
     * @return
     */
    String permsName();

    /**
     * 对象名称
     * @return
     */
    Class objectName();

    /**
     * 参数名称
     * @return
     */
    String paramName();

}
