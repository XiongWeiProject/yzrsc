package com.commodity.yzrsc.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 328789 on 2017/5/25.
 */

public class LiuyanEntity implements Serializable {
//    "orderId": 0,
//            "reason": "string",
//            "description": "string"

//    "createTime": "2017-06-02T12:12:57.851Z",
//            "isSeller": true,
//            "message": "string",
//            "images": [
//            "string"
//            ]
    private String message;
    private List<String> images;
    private boolean isSeller;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public boolean isSeller() {
        return isSeller;
    }

    public void setSeller(boolean seller) {
        isSeller = seller;
    }
}
