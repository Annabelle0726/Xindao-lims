package com.ruoyi.framework.model;

import lombok.Data;

/**
 * zhuo
 */
@Data
public class SsoOauthTokenModel {

    private String access_token;

    private String expires_in;

    private String id_token;

    private String scope;

    private String token_type;
}
