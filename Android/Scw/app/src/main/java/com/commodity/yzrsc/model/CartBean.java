package com.commodity.yzrsc.model;

import java.util.List;

/**
 * Created by liyushen on 2017/5/10 22:02
 * 购物车详情
 */
public class CartBean {


    /**
     * shopId : 5087
     * shopName : 店铺5087
     * sellerAvatar : https://api.acb.wang/UpLoadFiles/Image/202005162347557413881811190036.jpg
     * shoppingCartGoods : [{"shoppingCartId":27,"goodsSaleId":4238,"goodsSaleName":"铁轨，小鸟，人，自然好看","goodsImage":"https://api.acb.wang/UpLoadFiles/Image/202006152303485913891029941652.jpeg","goodsPrice":0.01,"quantity":1,"postage":0.01},{"shoppingCartId":43,"goodsSaleId":4236,"goodsSaleName":"啤酒好看也好喝,来吧来吧","goodsImage":"https://api.acb.wang/UpLoadFiles/Image/202006152301294351522107228942.jpeg","goodsPrice":0.01,"quantity":5,"postage":0.01},{"shoppingCartId":44,"goodsSaleId":4236,"goodsSaleName":"啤酒好看也好喝,来吧来吧","goodsImage":"https://api.acb.wang/UpLoadFiles/Image/202006152301294351522107228942.jpeg","goodsPrice":0.01,"quantity":1,"postage":0.01}]
     */

    private int shopId;
    private String shopName;
    private String sellerAvatar;


    private List<ShoppingCartGoodsBean> shoppingCartGoods;

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getSellerAvatar() {
        return sellerAvatar;
    }

    public void setSellerAvatar(String sellerAvatar) {
        this.sellerAvatar = sellerAvatar;
    }

    public List<ShoppingCartGoodsBean> getShoppingCartGoods() {
        return shoppingCartGoods;
    }

    public void setShoppingCartGoods(List<ShoppingCartGoodsBean> shoppingCartGoods) {
        this.shoppingCartGoods = shoppingCartGoods;
    }

    public static class ShoppingCartGoodsBean {
        /**
         * shoppingCartId : 27
         * goodsSaleId : 4238
         * goodsSaleName : 铁轨，小鸟，人，自然好看
         * goodsImage : https://api.acb.wang/UpLoadFiles/Image/202006152303485913891029941652.jpeg
         * goodsPrice : 0.01
         * quantity : 1
         * postage : 0.01
         */

        private int shoppingCartId;
        private int goodsSaleId;
        private String goodsSaleName;
        private String goodsImage;
        private double goodsPrice;
        private int quantity;
        private double postage;

        public int getShoppingCartId() {
            return shoppingCartId;
        }

        public void setShoppingCartId(int shoppingCartId) {
            this.shoppingCartId = shoppingCartId;
        }

        public int getGoodsSaleId() {
            return goodsSaleId;
        }

        public void setGoodsSaleId(int goodsSaleId) {
            this.goodsSaleId = goodsSaleId;
        }

        public String getGoodsSaleName() {
            return goodsSaleName;
        }

        public void setGoodsSaleName(String goodsSaleName) {
            this.goodsSaleName = goodsSaleName;
        }

        public String getGoodsImage() {
            return goodsImage;
        }

        public void setGoodsImage(String goodsImage) {
            this.goodsImage = goodsImage;
        }

        public double getGoodsPrice() {
            return goodsPrice;
        }

        public void setGoodsPrice(double goodsPrice) {
            this.goodsPrice = goodsPrice;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getPostage() {
            return postage;
        }

        public void setPostage(double postage) {
            this.postage = postage;
        }
    }
}
