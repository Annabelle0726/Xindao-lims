package com.ruoyi.common.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/*
 * 李林
 * 生成SQL语句*/

@Component
@AllArgsConstructor
public class QueryWrappers<T> {

    public static <T> QueryWrapper<T> queryWrappers(T entity) {
        if (ObjectUtil.isEmpty(entity)) return null;
        Class<?> aClass = entity.getClass();
        QueryWrapper<T> wrapper = Wrappers.<T>query();
        List<Field> fieldList = new ArrayList<>();
        while (aClass != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(aClass.getDeclaredFields())));
            aClass = aClass.getSuperclass();
        }
        for (Field field : fieldList) {
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(entity);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException("查询条件生成错误");
            }
            if(value == null || value.equals("")){
                continue;
            }
            /*boolean bool = field.isAnnotationPresent(TableField.class);
            if (bool){
                if(field.getAnnotation(TableField.class).exist()==false)continue;
            }*/
            if (!field.getName().equals("orderBy")) {
                if(value.getClass()== LocalDateTime.class){
                    wrapper.like(StrUtil.toUnderlineCase(field.getName()), ((LocalDateTime) value).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                }else{
                    wrapper.like(StrUtil.toUnderlineCase(field.getName()), value);
                }
            } else {
                Map<String, String> map = (Map<String, String>) value;
                if(map.get("order")!=null){
                    wrapper.orderBy(true, map.get("order").equals("asc"), StrUtil.toUnderlineCase(map.get("field")));
                }
            }
        }
        return wrapper;
    }
}
