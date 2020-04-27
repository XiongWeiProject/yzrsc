package com.commodity.yzrsc.model;

import java.util.List;

/**
 * Created by liyushen on 2017/5/10 22:02
 * 商品详情
 */
public class CommodityBean {

    /**
     * kind : 观音
     * specification : 1*1*0.5毫米
     * weight : .1克
     * sellerAvatar : http://scw-api.83soft.cn/UpLoadFiles/Image/20170520233930412133.png
     * shopName : 美美滴啊
     * sellerImToken : tYG15J12gLSL1PHd7LkTKfJmAAXJ1D/+Bij+pc6Rk1l3WgEMSPG/SBrfzhnurPYq/cQJ1w05Ey6tT9QKNowGFA==
     * sellerImId : 49
     * postage : 1.0
     * favoredCount : 4
     * shopId : 49
     * isFollowed : true
     * propagationUrl : http://scw-web.83soft.cn/m/shop/index?promotionCode=4962
     * id : 256
     * code : 12
     * description : 深绿
     * suggestedPrice : 24.0
     * price : 12.0
     * video : http://scw-api.83soft.cn/UpLoadFiles/Video/20170520134844176724.mp4
     * videoSnap : http://scw-web.83soft.cn/asset/play01.png
     * images : ["http://scw-api.83soft.cn/UpLoadFiles/Image/20170520134843903709.png","http://scw-api.83soft.cn/UpLoadFiles/Image/20170520134844045717.png"]
     * updateTime : 2017-05-20 13:48:44
     * isFavored : true
     * isReselled : true
     */

    private String kind;
    private String kindId;
    private String specification;
    private String weight;
    private String sellerAvatar;
    private String shopName;
    private String sellerImToken;
    private String sellerImId;
    private double postage;
    private int favoredCount;
    private int shopId;
    private boolean isFollowed;
    private String propagationUrl;
    private String goodsSaleUrl;
    private int id;
    private String code;
    private String description;
    private double suggestedPrice;
    private double price;
    private String video;
    private String videoSnap;
    private String updateTime;
    private boolean isFavored;
    private boolean isReselled;
    private List<String> images;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getSellerAvatar() {
        return sellerAvatar;
    }

    public void setSellerAvatar(String sellerAvatar) {
        this.sellerAvatar = sellerAvatar;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getSellerImToken() {
        return sellerImToken;
    }

    public void setSellerImToken(String sellerImToken) {
        this.sellerImToken = sellerImToken;
    }

    public String getSellerImId() {
        return sellerImId;
    }

    public void setSellerImId(String sellerImId) {
        this.sellerImId = sellerImId;
    }

    public double getPostage() {
        return postage;
    }

    public void setPostage(double postage) {
        this.postage = postage;
    }

    public int getFavoredCount() {
        return favoredCount;
    }

    public void setFavoredCount(int favoredCount) {
        this.favoredCount = favoredCount;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public boolean isIsFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(boolean isFollowed) {
        this.isFollowed = isFollowed;
    }

    public String getPropagationUrl() {
        return propagationUrl;
    }

    public void setPropagationUrl(String propagationUrl) {
        this.propagationUrl = propagationUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getSuggestedPrice() {
        return suggestedPrice;
    }

    public void setSuggestedPrice(double suggestedPrice) {
        this.suggestedPrice = suggestedPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideoSnap() {
        return videoSnap;
    }

    public void setVideoSnap(String videoSnap) {
        this.videoSnap = videoSnap;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isIsFavored() {
        return isFavored;
    }

    public void setIsFavored(boolean isFavored) {
        this.isFavored = isFavored;
    }

    public boolean isIsReselled() {
        return isReselled;
    }

    public void setIsReselled(boolean isReselled) {
        this.isReselled = isReselled;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getGoodsSaleUrl() {
        return goodsSaleUrl;
    }

    public void setGoodsSaleUrl(String goodsSaleUrl) {
        this.goodsSaleUrl = goodsSaleUrl;
    }

    public String getKindId() {
        return kindId;
    }

    public void setKindId(String kindId) {
        this.kindId = kindId;
    }
}
