package com.ruoyi.common.oa;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author ZTT
 */
@Configuration
@ConfigurationProperties(prefix = "oa")
@Data
public class OAProperty {

	/**
	 * oa地址
	 */
	private String oaHost;

	/**
	 * 文件查看ip地址
	 */
	private String prodIp;

	/**
	 * 装备电缆：不合格审批oa的id
	 */
	private String unqualifiedProcessId;

	// ✅ 正确地把 test() 放在类体内
	public void test() {
		System.out.println(this.getOaHost()); // 尝试调用getter方法
	}
}
