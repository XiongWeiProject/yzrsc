package com.commodity.yzrsc.model;

public class Evalution {

    /**
     * id : 0
     * memberId : 0
     * memberNickname : string
     * commentType : 0
     * comment : string
     * replyiedMemberId : 0
     * replyiedNickname : string
     * ext1 : string
     * createTime : string
     */

    private int id;
    private int memberId;
    private String memberNickname;
    private int commentType;
    private String comment;
    private int replyiedMemberId;
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

    public int getReplyiedMemberId() {
        return replyiedMemberId;
    }

    public void setReplyiedMemberId(int replyiedMemberId) {
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
