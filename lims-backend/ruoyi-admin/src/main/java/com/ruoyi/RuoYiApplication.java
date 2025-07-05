package com.ruoyi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 启动程序
 *
 * @author ruoyi
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableTransactionManagement
@EnableScheduling
public class RuoYiApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(RuoYiApplication.class, args);
        System.out.println("<=====================>LIMS系统启动成功<=====================>");
    }
}
