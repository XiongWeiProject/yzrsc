package com.commodity.scw.model;

/**
 * Created by liyushen on 2017/5/13 17:52
 */
public class Focuser {

    /**
     * id : 11
     * createTime : 2017-05-19 22:06:17
     * memberId : 69
     * isRead : false
     * memberName : 虚伪造孽徒
     * memberAvatar : http://scw-api.83soft.cn/UpLoadFiles/Image/20170518231255021287.png
     * isFollowed : false
     */

    private int id;
    private String createTime;
    private int memberId;
    private boolean isRead;
    private String memberName;
    private String memberAvatar;
    private boolean isFollowed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public boolean isIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberAvatar() {
        return memberAvatar;
    }

    public void setMemberAvatar(String memberAvatar) {
        this.memberAvatar = memberAvatar;
    }

    public boolean isIsFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(boolean isFollowed) {
        this.isFollowed = isFollowed;
    }
}
