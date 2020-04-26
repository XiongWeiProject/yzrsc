package com.commodity.scw.model;

/**
 * Created by liyushen on 2017/4/30 00:53
 */
public class WXAccessTokenInfo {

    /**
     * access_token : WD4u53PP40t2RLlBDAFh849Bkd9wemmVnAihL8P1mpL3BsVxmcZcrYqS4adu9xX0-UIa0mp5T85eORThmu75IRVCDFr-YZSRXID73u7Hx9g
     * expires_in : 7200
     * refresh_token : u2oEe1MzIwiWLvabQeas0C6k2Z-u47jcCGrVedOeIuJh0Mr3Xltixi1xIbrZEtikub_Ji23R5A02CmxjcJmH-jGBH5ND8RD9AjKzL9Uc450
     * openid : oadnS0gg37E8GWtnsxWNYlvQEhyE
     * scope : snsapi_userinfo
     * unionid : o1EBrxOc3xM8gATLg7pJeujAUOMk
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
