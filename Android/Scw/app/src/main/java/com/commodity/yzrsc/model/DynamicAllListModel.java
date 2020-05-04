package com.commodity.yzrsc.model;

import java.util.List;

public class DynamicAllListModel  {


    /**
     * id : 5
     * memberId : 4081
     * memberAvatar :
     * memberNickname : Xxxx
     * dynamicCatalog_Id : 1
     * dynamicType : 1
     * description : 哈哈
     * isLike : false
     * pictures : ["https://yzrsc-api.83soft.cn/UpLoadFiles/Image/20200502172252834025921174500.png"]
     * videoUrl :
     * commentCount : 0
     * likeCount : 0
     * createTime : 1小时前
     * ext1 :
     */

    private int id;
    private int memberId;
    private String memberAvatar;
    private String memberNickname;
    private int dynamicCatalog_Id;
    private int dynamicType;
    private String description;
    private boolean isLike;
    private String videoUrl;
    private int commentCount;
    private int likeCount;
    private String createTime;
    private String ext1;
    private List<String> pictures;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getMemberAvatar() {
        return memberAvatar;
    }

    public void setMemberAvatar(String memberAvatar) {
        this.memberAvatar = memberAvatar;
    }

    public String getMemberNickname() {
        return memberNickname;
    }

    public void setMemberNickname(String memberNickname) {
        this.memberNickname = memberNickname;
    }

    public int getDynamicCatalog_Id() {
        return dynamicCatalog_Id;
    }

    public void setDynamicCatalog_Id(int dynamicCatalog_Id) {
        this.dynamicCatalog_Id = dynamicCatalog_Id;
    }

    public int getDynamicType() {
        return dynamicType;
    }

    public void setDynamicType(int dynamicType) {
        this.dynamicType = dynamicType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIsLike() {
        return isLike;
    }

    public void setIsLike(boolean isLike) {
        this.isLike = isLike;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }
}
