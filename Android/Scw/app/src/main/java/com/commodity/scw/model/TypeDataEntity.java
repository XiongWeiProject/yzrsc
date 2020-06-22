package com.commodity.scw.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by yangxuqiang on 2017/5/10.
 */

public class TypeDataEntity implements Serializable {
//    "id": 0,
//            "code": "string",
//            "description": "string",
//            "suggestedPrice": 0,
//            "price": 0,
//            "video": "string",
//            "images": [
//            "string"
//            ],
//            "updateTime": "2017-05-10T13:20:03.796Z",
//            "isFavored": true
    private int id;
    private String code;
    private String description;
    private BigDecimal suggestedPrice;
    private BigDecimal price;
    private String[] images;
    private String updateTime;
    private boolean isFavored;
    private String video;
    private boolean isReselled;
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

    public BigDecimal getSuggestedPrice() {
        return suggestedPrice;
    }

    public void setSuggestedPrice(BigDecimal suggestedPrice) {
        this.suggestedPrice = suggestedPrice;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isFavored() {
        return isFavored;
    }

    public void setFavored(boolean favored) {
        isFavored = favored;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public boolean isReselled() {
        return isReselled;
    }

    public void setReselled(boolean reselled) {
        isReselled = reselled;
    }
}
