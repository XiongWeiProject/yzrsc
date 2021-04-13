package com.commodity.yzrsc.model;

import java.io.Serializable;
import java.util.List;

public class DynamicAllListModel implements Serializable {


    /**
     * id : 40
     * memberId : 4082
     * memberAvatar : https://api.acb.wang/UpLoadFiles/Image/202005042154492571062125040331.jpg
     * memberNickname : Wee888
     * dynamicCatalog_Id : 5
     * dynamicType : 1
     * description : Eeeeeeee
     * isLike : false
     * pictures : ["https://api.acb.wang/UpLoadFiles/Image/202005081947084044041688072905.jpg","https://api.acb.wang/UpLoadFiles/Image/202005081947084044041688072905.jpg","https://api.acb.wang/UpLoadFiles/Image/20200508194708420100311117186.jpg","https://api.acb.wang/UpLoadFiles/Image/202005081947084356572107228942.jpg","https://api.acb.wang/UpLoadFiles/Image/20200508194708451284730273223.jpg"]
     * videoUrl :
     * commentCount : 0
     * likeCount : 0
     * createTime : 2020-05-08
     * ext1 :
     * commentList : []
     * likeList : []
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
    private List<commentList> commentList;
    private List<likeList> likeList;

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

    public List<commentList> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<commentList> commentList) {
        this.commentList = commentList;
    }

    public List<likeList> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<likeList> likeList) {
        this.likeList = likeList;
    }
    public static class likeList{
        private int memberId;
        private String memberNickname;

        public int getMemberId() {
            return memberId;
        }

        public void setMemberId(int memberId) {
            this.memberId = memberId;
        }

        public String getMemberNickname() {
            return memberNickname;
        }

        public void setMemberNickname(String memberNickname) {
            this.memberNickname = memberNickname;
        }
    }


    public static class commentList{
        private int id;
        private int memberId;
        private String memberNickname;
        private int commentType;
        private String comment;
        private String replyiedMemberId;
        private String replyiedNickname;
        private String ext1;
        private String createTime;

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

        public String getMemberNickname() {
            return memberNickname;
        }

        public void setMemberNickname(String memberNickname) {
            this.memberNickname = memberNickname;
        }

        public int getCommentType() {
            return commentType;
        }

        public void setCommentType(int commentType) {
            this.commentType = commentType;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getReplyiedMemberId() {
            return replyiedMemberId;
        }

        public void setReplyiedMemberId(String replyiedMemberId) {
            this.replyiedMemberId = replyiedMemberId;
        }

        public String getReplyiedNickname() {
            return replyiedNickname;
        }

        public void setReplyiedNickname(String replyiedNickname) {
            this.replyiedNickname = replyiedNickname;
        }

        public String getExt1() {
            return ext1;
        }

        public void setExt1(String ext1) {
            this.ext1 = ext1;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
