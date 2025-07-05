package com.ruoyi;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Collections;
import java.util.Scanner;

// 演示例子，执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
public class CodeGenerator {

    public static String database_url = "jdbc:mysql://localhost:3306/center-lims";
    public static String database_username = "root";
    public static String database_password= "123456";
    public static String author = "";
    public static String model_name = "/cnas-process"; // 如果为分布式填子模块名称，如果不是分布式为空即可
    public static String setParent = "com.ruoyi.process"; // 包路径
    public static String tablePrefix = "cnas_"; // 设置过滤表前缀
    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir");
        FastAutoGenerator.create(database_url, database_username, database_password)
                // 全局配置
                .globalConfig(builder -> {
                    builder.author(author) // 设置作者
                            .commentDate("yyyy-MM-dd hh:mm:ss")   //注释日期
                            .outputDir(projectPath + model_name + "/src/main/java") // 指定输出目录
                            .disableOpenDir() //禁止打开输出目录，默认打开
                            .enableSwagger() // 开启swagger

                    ;
                })
                // 包配置
                .packageConfig(builder -> {
                    builder.entity("pojo");
                    builder.parent(setParent) // 设置父包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, projectPath + model_name +  "/src/main/resources/mapper")); // 设置mapperXml生成路径
                })
                // 策略配置
                .strategyConfig(builder -> {
                    builder.addInclude(scanner("表名，多个空格分割").split(" ")) // 设置需要生成的表名
                            .addTablePrefix(tablePrefix) // 设置过滤表前缀
                            // Entity 策略配置
                            .entityBuilder()
                            .enableLombok() //开启 Lombok
                            .naming(NamingStrategy.underline_to_camel)  //数据库表映射到实体的命名策略：下划线转驼峰命
                            .columnNaming(NamingStrategy.underline_to_camel)    //数据库表字段映射到实体的命名策略：下划线转驼峰命
                            .enableFileOverride() // 覆盖已经生成的Entity文件
                            .logicDeleteColumnName("state")
                            .addTableFills(
                                    new Column("create_time", FieldFill.INSERT),
                                    new Column("update_time", FieldFill.INSERT_UPDATE),
                                    new Column("create_user", FieldFill.INSERT),
                                    new Column("update_user", FieldFill.INSERT_UPDATE)
                            )
                            .idType(IdType.AUTO) // 自增主键

                            // Mapper 策略配置
                            .mapperBuilder()
                            .enableFileOverride() // 覆盖已生成Mapper文件
                            .enableBaseResultMap() // 自动生成resultMap
                            // Service 策略配置
                            .serviceBuilder()
                            .formatServiceFileName("%sService") //格式化 service 接口文件名称，%s进行匹配表名，如 UserService
                            .formatServiceImplFileName("%sServiceImpl") //格式化 service 实现类文件名称，%s进行匹配表名，如 UserServiceImpl
                            .enableFileOverride() // 覆盖已生成文件

                            // Controller 策略配置
                            .controllerBuilder()
                            .enableFileOverride() // 覆盖已生成文件
                            .enableRestStyle() // 开启生成@RestController 控制器
                    ;
                })
                //5、模板引擎
                .templateEngine(new FreemarkerTemplateEngine())	//默认
                .execute();
    }

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();

            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }
}
