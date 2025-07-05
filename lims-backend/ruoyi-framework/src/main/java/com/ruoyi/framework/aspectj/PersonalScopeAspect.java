package com.ruoyi.framework.aspectj;

import com.ruoyi.common.annotation.PersonalScope;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.vo.SysRoleVo;
import com.ruoyi.system.mapper.SysRoleMenuMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 仅看我权限
 *
 * @Author zhuo
 * @Date 2025/2/20
 */
@Aspect
@Component
public class PersonalScopeAspect {

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Pointcut("@annotation(com.ruoyi.common.annotation.PersonalScope)")
    public void dataScopePointCut() {
    }

    @Before("dataScopePointCut()")
    public void doBefore(JoinPoint point) throws Throwable {
        Long userId = SecurityUtils.getUserId();

        // 获取目标方法的签名
        MethodSignature signature = (MethodSignature) point.getSignature();
        // 获取目标方法上的注解
        PersonalScope annotation = signature.getMethod().getAnnotation(PersonalScope.class);

        // 获取权限判断是否是仅看我
        SysRoleVo sysRoleVo = sysRoleMenuMapper.selectRoleMenu(annotation.permsName(), userId);

        //没有仅看我权限跳过
        if (sysRoleVo == null || sysRoleVo.getIsRersonal() == null || !sysRoleVo.getIsRersonal().equals(1)) {
            return;
        }

        // 获取方面上所有的对象
        Object[] args = point.getArgs();
        for (Object arg : args) {
            // 循环查找匹配的对象
            if (arg.getClass().equals(annotation.objectName())) {
                Class<?> argClass = arg.getClass();
                // 查找需要填充的字段
                Field declaredField = getField(argClass, annotation.paramName());
                declaredField.setAccessible(true);
                // 添加用户Id
                declaredField.set(arg, userId.intValue());
            }
        }
    }

    // 递归查找字段
    private Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        // 从当前类开始查找字段
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            // 如果当前类没有找到，检查父类
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                return getField(superClass, fieldName);
            } else {
                throw e;
            }
        }
    }
}
