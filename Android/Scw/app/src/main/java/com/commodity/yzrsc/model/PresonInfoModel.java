package com.commodity.yzrsc.model;

public class PresonInfoModel {


    /**
     * memberId : 0
     * memberNickName : string
     * memberAvatar : string
     * dynamicBackGroundPic : string
     */

    private int memberId;
    private String memberNickName;
    private String memberAvatar;
    private String dynamicBackGroundPic;

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getMemberNickName() {
        return memberNickName;
    }

    public void setMemberNickName(String memberNickName) {
        this.memberNickName = memberNickName;
    }

    public String getMemberAvatar() {
        return memberAvatar;
    }

    public void setMemberAvatar(String memberAvatar) {
        this.memberAvatar = memberAvatar;
    }

    public String getDynamicBackGroundPic() {
        return dynamicBackGroundPic;
    }

    public void setDynamicBackGroundPic(String dynamicBackGroundPic) {
        this.dynamicBackGroundPic = dynamicBackGroundPic;
    }
}
