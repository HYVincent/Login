package com.vincent.cloud.entity;

import java.io.Serializable;

/**
 * description ：
 * project name：CCloud
 * author : Vincent
 * creation date: 2017/6/10 11:20
 *
 * @version 1.0
 */

public class WXAccessTokenEntity implements Serializable {

    /**
     * access_token : hSTsG7nq8e0yEFhOZFT-wAdTsjT9jC0AYvGRiqGwwR6Hko99o_mmYR8KO18kMxeDOz33d9tnBhMzu_NLsIha2HqvTm1OGPL1weBdvXZVFFc
     * expires_in : 7200
     * refresh_token : AeN69M27vttqCedxoIOSeY6cxvbt1N584HjEOclUXtNWxRaZWgmtfvn2jWIDX4tq5t-7Btlc1UkEyyFhV7HVIMXe-V6RPjoZdF525vLzev8
     * openid : olmt4wfxS21G4VeeVX16_zUhZezY
     * scope : snsapi_userinfo
     * unionid : o5aWQwAa7niCIXhAIRBOwglIJ7UQ
     */

    private String access_token;
    private int expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    private String unionid;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}
