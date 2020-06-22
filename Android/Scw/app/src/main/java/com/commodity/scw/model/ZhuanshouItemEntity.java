package com.commodity.scw.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 328789 on 2017/4/18.
 */

public class ZhuanshouItemEntity implements Serializable {
//    {
//        "profit": 0.00,
//    "id": 35,
//            "code": "",
//            "description": "1",
//            "price": 1.00,
//            "image": "http://scw-api.83soft.cn/UpLoadFiles/Image/20170423211831224941.jpg",
//            "viewCount": 0,
//            "soldCount": 0,
//            "favoredCount": 0
//    }
    private int id;
    private String code;
    private String description;
    private BigDecimal price;
    private BigDecimal profit;
    private String image;
    private int viewCount;
    private int soldCount;
    private int favorCount;
    private List<String> images;
    private String goodsSaleUrl;

    public String getGoodsSaleUrl() {
        return goodsSaleUrl;
    }

    public void setGoodsSaleUrl(String goodsSaleUrl) {
        this.goodsSaleUrl = goodsSaleUrl;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getSoldCount() {
        return soldCount;
    }

    public void setSoldCount(int soldCount) {
        this.soldCount = soldCount;
    }

    public int getFavorCount() {
        return favorCount;
    }

    public void setFavorCount(int favorCount) {
        this.favorCount = favorCount;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
