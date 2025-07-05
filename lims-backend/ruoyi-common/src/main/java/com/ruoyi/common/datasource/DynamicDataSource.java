//package com.ruoyi.common.datasource;
//
//import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
//import javax.sql.DataSource;
//import java.util.Map;
//
//public class DynamicDataSource extends AbstractRoutingDataSource {
//    @Override
//    protected Object determineCurrentLookupKey() {  // 修正方法名（原拼写错误）
//        return DataSourceContextHolder.getDataSourceType();
//    }
//
//    // 补充 setter 方法（需手动添加）
//    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
//        super.setTargetDataSources(targetDataSources);
//    }
//}