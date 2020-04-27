package com.commodity.yzrsc.model;

/**
 * Created by liyushen on 2017/3/19 15:09
 * 商品是实体类
 */
public class MarketGoods {
    private String id;
    private String describe;
    private ImgType[] imgTypes;//多少张图片
    private String suggestedPrice;
    private String price;
    private String video;
    private String dateTime;
    private boolean isFavorite;
    private boolean isReselled;
    private boolean isOnsale;
    private String videoSnap;
    private String shopId;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public ImgType[] getImgTypes() {
        return imgTypes;
    }

    public void setImgTypes(ImgType[] imgTypes) {
        this.imgTypes = imgTypes;
    }

    public String getSuggestedPrice() {
        return suggestedPrice;
    }

    public void setSuggestedPrice(String suggestedPrice) {
        this.suggestedPrice = suggestedPrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isReselled() {
        return isReselled;
    }

    public void setReselled(boolean reselled) {
        isReselled = reselled;
    }

    public String getVideoSnap() {
        return videoSnap;
    }

    public void setVideoSnap(String videoSnap) {
        this.videoSnap = videoSnap;
    }

    public boolean isOnsale() {
        return isOnsale;
    }

    public void setOnsale(boolean onsale) {
        isOnsale = onsale;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}
