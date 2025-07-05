//package com.ruoyi.common.datasource;
//
//public class DataSourceContextHolder {
//    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();
//
//    public static void setDataSourceType(String dsType) {
//        CONTEXT_HOLDER.set(dsType);
//    }
//
//    public static String getDataSourceType() {
//        return CONTEXT_HOLDER.get();
//    }
//
//    public static void clearDataSourceType() {
//        CONTEXT_HOLDER.remove();
//    }
//}