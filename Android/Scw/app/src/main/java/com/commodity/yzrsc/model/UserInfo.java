package com.commodity.yzrsc.model;

import java.io.Serializable;

/**
 * 用户实体类
 *
 * @author liyushen
 */
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id : 0
     * nickname : string
     * mobile : string
     * promotionCode : string
     * inviter : string
     * avatar : string
     * deviceToken : string
     * expireTime : 2017-05-07T05:50:18.488Z
     * isPermanet : true
     * imToken : string
     */
//    private String id;//ID ,
//    private String nickname;//昵称 ,
//    private String mobile;//账户(手机) ,
//    private String promotionCode; //推广码 ,
//    private String inviter;//邀请人 ,
//    private String avatar;//头像 ,
//    private String deviceToken; //令牌 ,
//    private String expireTime;//失效时间 ,
//    private boolean isPermanet; //是否永久
//    private String imToken; //融云Token
    private String id;
    private String nickname;
    private String mobile;
    private String promotionCode;
    private String inviter;
    private String avatar;
    private String deviceToken;
    private String expireTime;
    private boolean isPermanet;
    private String imToken;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public String getInviter() {
        return inviter;
    }

    public void setInviter(String inviter) {
        this.inviter = inviter;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public boolean isIsPermanet() {
        return isPermanet;
    }

    public void setIsPermanet(boolean isPermanet) {
        this.isPermanet = isPermanet;
    }

    public String getImToken() {
        return imToken;
    }

    public void setImToken(String imToken) {
        this.imToken = imToken;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", mobile='" + mobile + '\'' +
                ", promotionCode='" + promotionCode + '\'' +
                ", inviter='" + inviter + '\'' +
                ", avatar='" + avatar + '\'' +
                ", deviceToken='" + deviceToken + '\'' +
                ", expireTime='" + expireTime + '\'' +
                ", isPermanet=" + isPermanet +
                ", imToken='" + imToken + '\'' +
                '}';
    }
}
