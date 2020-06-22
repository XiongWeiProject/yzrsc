package com.commodity.yzrsc.model;

import java.util.List;

/**
 * Created by liyushen on 2017/5/10 22:02
 * 购物车详情
 */
public class CartBean {

    /**
     {
     "id": 0,
     "goodsSaleId": 0,
     "goodsSaleName": "string",
     "goodsImage": "string",
     "quantity": 0,
     "shopId": 0,
     "goodsPrice": 0,
     "shopName": "string",
     "sellerAvatar": "string"
     }
     */

    private int id;
    private int shopId;
    private String shopName;
    private String sellerAvatar;

    private String goodsSaleId;
    private String goodsSaleName;
    private String goodsImage;
    private Integer quantity;
    private Double goodsPrice;
    private Double postage;

    public Double getPostage() {
        return postage;
    }

    public void setPostage(Double postage) {
        this.postage = postage;
    }

    public Double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }

    public String getGoodsSaleId() {
        return goodsSaleId;
    }

    public void setGoodsSaleId(String goodsSaleId) {
        this.goodsSaleId = goodsSaleId;
    }

    public String getGoodsSaleName() {
        return goodsSaleName;
    }

    public void setGoodsSaleName(String goodsSaleName) {
        this.goodsSaleName = goodsSaleName;
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

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
