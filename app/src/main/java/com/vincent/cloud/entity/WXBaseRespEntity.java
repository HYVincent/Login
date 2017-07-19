package com.vincent.cloud.entity;

import java.io.Serializable;

/**
 * description ：
 * project name：CCloud
 * author : Vincent
 * creation date: 2017/6/10 9:39
 *
 * @version 1.0
 */

public class WXBaseRespEntity implements Serializable {
    /**
     * code : 0811Tw9Z0n1fEZ1yKdaZ06tO9Z01Tw94
     * country : CN
     * errCode : 0
     * lang : zh_CN
     * state : wechat_sdk_微信登录
     * type : 1
     * url : wxb363a9ff53731258://oauth?code=0811Tw9Z0n1fEZ1yKdaZ06tO9Z01Tw94&state=wechat_sdk_%E5%BE%AE%E4%BF%A1%E7%99%BB%E5%BD%95
     */

    private String code;
    private String country;
    private int errCode;
    private String lang;
    private String state;
    private int type;
    private String url;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
