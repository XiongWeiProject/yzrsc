package com.commodity.yzrsc.model;

import java.util.List;

public class DynamicAllListModel  {

    /**
     * id : 1
     * memberId : 4080
     * memberAvatar :
     * memberNickname :
     * isLike : false
     * pictures : []
     * videoUrl :
     * commentCount : 0
     * likeCount : 0
     * createTime : 2020-04-21
     * ext1 :
     * dynamicCatalog_Id : 1
     * dynamicType : 0
     * description : string
     */

    private int id;
    private int memberId;
    private String memberAvatar;
    private String memberNickname;
    private boolean isLike;
    private String videoUrl;
    private int commentCount;
    private int likeCount;
    private String createTime;
    private String ext1;
    private int dynamicCatalog_Id;
    private int dynamicType;
    private String description;
    private List<?> pictures;

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

    public List<?> getPictures() {
        return pictures;
    }

    public void setPictures(List<?> pictures) {
        this.pictures = pictures;
    }
}
