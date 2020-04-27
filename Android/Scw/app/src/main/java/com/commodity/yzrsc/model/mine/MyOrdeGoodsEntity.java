package com.commodity.yzrsc.model.mine;

import java.io.Serializable;

/**
 * Created by yangxuqiang on 2017/4/21.
 */

public class MyOrdeGoodsEntity implements Serializable {
//    {
//            "id": 0,
//                "code": "string",
//                "image": "string",
//                "descritpion": "string"description
//        }
    private int id;
    private String  code;
    private String image;
    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
