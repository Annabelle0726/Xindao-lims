package com.ruoyi.framework.web.service;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.config.SsoBean;
import com.ruoyi.framework.model.SsoOauthTokenModel;
import com.ruoyi.framework.model.SsoUserInfoModel;
import com.ruoyi.framework.web.ssoAuth.SsoCodeAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.exception.user.BlackListException;
import com.ruoyi.common.exception.user.CaptchaException;
import com.ruoyi.common.exception.user.CaptchaExpireException;
import com.ruoyi.common.exception.user.UserNotExistsException;
import com.ruoyi.common.exception.user.UserPasswordNotMatchException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.security.context.AuthenticationContextHolder;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 登录校验方法
 *
 * @author ruoyi
 */
@Component
@Slf4j
public class SysLoginService
{
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private SsoBean ssoBean;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid)
    {
//        // 验证码校验
//        validateCaptcha(username, code, uuid);
        // 登录前置校验
        loginPreCheck(username, password);
        // 用户验证
        Authentication authentication = null;
        try
        {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        }
        catch (Exception e)
        {
            if (e instanceof BadCredentialsException)
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }
        finally
        {
            AuthenticationContextHolder.clearContext();
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUserId());
        // 生成token
        return tokenService.createToken(loginUser);
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid)
    {
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled)
        {
            String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
            String captcha = redisCache.getCacheObject(verifyKey);
            if (captcha == null)
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
                throw new CaptchaExpireException();
            }
            redisCache.deleteObject(verifyKey);
            if (!code.equalsIgnoreCase(captcha))
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
                throw new CaptchaException();
            }
        }
    }

    /**
     * 登录前置校验
     * @param username 用户名
     * @param password 用户密码
     */
    public void loginPreCheck(String username, String password)
    {
        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            throw new UserNotExistsException();
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // IP黑名单校验
        String blackStr = configService.selectConfigByKey("sys.login.blackIPList");
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr()))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("login.blocked")));
            throw new BlackListException();
        }
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId)
    {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr());
        sysUser.setLoginDate(DateUtils.getNowDate());
        sysUserService.updateUserProfile(sysUser);
    }

    /**
     * 单点登录
     * @param code
     * @return
     */
    public String loginBySSO(String code) {

        // 获取单点登录token
        SsoOauthTokenModel tokenModel = this.getSsoAccessToken(code);
        if (tokenModel == null) {
            throw new RuntimeException("单点登录验证失败");
        }

        // 获取单点登录用户信息
        SsoUserInfoModel userInfo = this.getSsoUserInfo(tokenModel.getAccess_token());
        if (userInfo == null) {
            throw new RuntimeException("单点登录验证失败");
        }

        // 查询本地用户信息
        LoginUser loginUser = this.getSsoLoginToken(userInfo.getEmployeeId());
        recordLoginInfo(loginUser.getUserId());

        // 创建登录信息
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(userInfo.getEmployeeId(), Constants.LOGIN_SUCCESS, MessageUtils.message("user.sso.login.success")));
        redisTemplate.opsForValue().set("ssoOauthToken:idToken:" + userInfo.getEmployeeId(), tokenModel.getId_token(), 3, TimeUnit.DAYS);
        // 生成token
        return tokenService.createToken(loginUser);
    }

    /**
     * ***** 获取本地用户信息 *****
     * @param username
     * @return
     */
    public LoginUser getSsoLoginToken(String username) {
        // 用户验证
        Authentication authentication = null;
        SysUser sysUser = sysUserService.selectUserByUserName(username);
        if (sysUser == null) {
            throw new ServiceException("当前系统没有该用户");
        }
        try
        {
            // 无需密码获取用户信息
            authentication = authenticationManager.authenticate(new SsoCodeAuthenticationToken(username));
        }
        catch (Exception e)
        {
            if (e instanceof BadCredentialsException)
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.sso.not.match")));
                throw new UserPasswordNotMatchException();
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }
        finally
        {
            AuthenticationContextHolder.clearContext();
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.sso.login.success")));
        return (LoginUser) authentication.getPrincipal();

    }



    /**
     * **** 获取单点登录token ****
     * @param code
     * @return
     */
    public SsoOauthTokenModel getSsoAccessToken(String code) {
        List<NameValuePair> list = new LinkedList<>();
        list.add(new BasicNameValuePair("grant_type", "authorization_code"));
        list.add(new BasicNameValuePair("code", code));
        list.add(new BasicNameValuePair("client_id", ssoBean.getClientId()));
        list.add(new BasicNameValuePair("client_secret", ssoBean.getClientSecret()));
        list.add(new BasicNameValuePair("redirect_uri", ssoBean.getCallbackUrl()));

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Accept", "application/json");
        String result = doPost(ssoBean.getUrl() + "/oauth2/token", headers, list);
        if (org.apache.commons.lang3.StringUtils.isBlank(result)) {
            return null;
        }
        return JSON.parseObject(result, SsoOauthTokenModel.class);
    }

    /**
     * ***获取单点登录用户信息***
     * @param accessToken
     * @return
     */
    public SsoUserInfoModel getSsoUserInfo(String accessToken) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);
        String result = doPost(ssoBean.getUrl() + "/userinfo", headers, null);
        if (org.apache.commons.lang3.StringUtils.isBlank(result)) {
            return null;
        }
        return JSON.parseObject(result, SsoUserInfoModel.class);
    }

    public static String doPost(String url, Map<String, String> headers, List<NameValuePair> params) {
        CloseableHttpClient client = createSSLClientDefault();
        CloseableHttpResponse response = null;
        try {
            HttpPost method = new HttpPost(url);
            headers.forEach(method::setHeader);
            if (params != null) {
                method.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            }

            response = client.execute(method);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            log.error("统一登录请求出现异常", e.getMessage());
        } finally {
            try {
                if (client != null) {
                    client.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                log.error("统一登录请求出现异常", e.getMessage());
            }
        }
        return null;
    }


    private static CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                //信任所有
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }
}
