package com.ruoyi.common.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import me.chanjar.weixin.cp.bean.message.WxCpMessageSendResult;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;

import java.io.*;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

/**
 * @Author: chenxiangfeng
 * @Date: 2021/7/20 10:35
 */
public class WxCpUtils {

	public static final String CORP_ID = "wwa423654b975441ac";
	public static final String CORP_SECRET = "snXq8qwA5tGu0YN1PlSDQqr6u9x3A0c_jQDmt8CN8Vs";
	public static final Integer AGENT_ID = 1000515;

	/**
	 * @param user      例：ZT-033268|ZT-028629，多个中间用|隔开
	 * @param content   传文本内容
	 * @param imageFile 图片
	 * @throws Exception
	 */
	public static void inform(String user, String content, File imageFile) throws Exception {
		WxCpDefaultConfigImpl config = new WxCpDefaultConfigImpl();
		// 注册的企业信息
		config.setCorpId(CORP_ID);
		config.setCorpSecret(CORP_SECRET);

		WxCpServiceImpl wxCpService = new WxCpServiceImpl();
		wxCpService.setWxCpConfigStorage(config);

		WxCpMessage message;
		WxCpMessageSendResult result;
		// 发送文本
		if (StringUtils.isNotBlank(content)) {
			message = WxCpMessage.TEXT()
					.content(content)
					// 用户id
					.toUser(user)
					// 应用id
					.agentId(AGENT_ID)
					.build();
			result = wxCpService.getMessageService().send(message);
		}
		// 发送文件
		if (imageFile != null) {
			// 获取文件id
			WxMediaUploadResult imgResult = wxCpService.getMediaService().upload("file", imageFile);
			message = WxCpMessage.IMAGE()
					.mediaId(imgResult.getMediaId())
					// 用户id
					.toUser(user)
					// 应用id
					.agentId(AGENT_ID)
					.build();
			result = wxCpService.getMessageService().send(message);
		}
	}

	/**
	 * 推送群消息
	 *
	 * @param webHookList 企业微信机器人地址
	 * @param file        图片
	 * @throws Exception
	 */
	public static void informWebHook(List<String> webHookList, File file) throws Exception {
		JSONObject json = new JSONObject();
		json.putOpt("msgtype", "image");
		JSONObject image = new JSONObject();
		image.putOpt("base64", getBase64(file));
		image.putOpt("md5", getMd5(file));
		json.putOpt("image", image);

		webHookList.stream().forEach(webHook -> {
			String result = HttpRequest.post(webHook)
					.header("Content-Type", "application/json")
					.body(json.toString())
					.execute().body();
		});
	}

	/**
	 * 推送群消息
	 *
	 * @param webHook 企业微信机器人地址
	 * @param content 数据
	 * @throws Exception
	 */
	public static String informWebHook(String webHook, String content) {
		JSONObject jsonObject = new JSONObject()
				.accumulate("msgtype", "text")
				.accumulate("text", new JSONObject()
						.accumulate("content", content));
		return HttpRequest.post(webHook).header("Content-Type", "application/json").body(jsonObject.toString()).execute().body();
	}

	public static String getMd5(File file) {
		String name = "";
		try {
			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
			byte[] bytes = new byte[1024];
			int len = 0;
			MessageDigest messagedigest = MessageDigest.getInstance("MD5");
			while ((len = inputStream.read(bytes)) > 0) {
				messagedigest.update(bytes, 0, len);
			}
			name = Md5Util.bufferToHex(messagedigest.digest());
			inputStream.close();
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		} catch (NoSuchAlgorithmException e) {
		}
		return name;

	}

	public static String getBase64(File file) {
		FileInputStream inputStream = null;
		String base64Str = "";
		try {
			inputStream = new FileInputStream(file);
			Base64.Encoder encoder = Base64.getEncoder();
			int available = inputStream.available();
			byte[] bytes = new byte[available];
			inputStream.read(bytes);
			base64Str = encoder.encodeToString(bytes);
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return base64Str;
	}

}
