package com.commodity.scw.model;

/**
 * Created by liyushen on 2017/4/2 17:08
 * 会员
 */
public class PersonInfo {

    /**
     * name : string
     * avatar : string
     * imToken : string
     * imId : string
     */

    private String name;
    private String avatar;
    private String imToken;
    private String imId;
    private String receivedTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getImToken() {
        return imToken;
    }

    public void setImToken(String imToken) {
        this.imToken = imToken;
    }

    public String getImId() {
        return imId;
    }

    public void setImId(String imId) {
        this.imId = imId;
    }

    public String getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(String receivedTime) {
        this.receivedTime = receivedTime;
    }
}
