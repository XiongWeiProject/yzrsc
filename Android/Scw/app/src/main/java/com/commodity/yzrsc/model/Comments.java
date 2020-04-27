package com.commodity.yzrsc.model;

/**
 * Created by liyushen on 2017/4/3 22:39
 * 评论
 */
public class Comments {

    /**
     * id : 0
     * createTime : 2017-05-13T09:21:36.910Z
     * goodsSaleId : 0
     * isRead : true
     * orderCode : string
     * evaluationContent : string
     * goodsImage : string
     */

    private int id;
    private String createTime;
    private int goodsSaleId;
    private boolean isRead;
    private String orderCode;
    private String evaluationContent;
    private String goodsImage;

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

    public int getGoodsSaleId() {
        return goodsSaleId;
    }

    public void setGoodsSaleId(int goodsSaleId) {
        this.goodsSaleId = goodsSaleId;
    }

    public boolean isIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getEvaluationContent() {
        return evaluationContent;
    }

    public void setEvaluationContent(String evaluationContent) {
        this.evaluationContent = evaluationContent;
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }
}
