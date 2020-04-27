package com.commodity.yzrsc.model;

import java.util.List;

/**
 * Created by liyushen on 2017/5/29 14:28
 * 转售管理实体类
 */
public class SellerZhuanshouEntity {

    /**
     * originalAmount : 0.02
     * totalProfit : 0.98
     * id : 233
     * code : O636315896703335900
     * createTime : 2017-05-28 17:34:30
     * total : 1.0
     * state : 订单已提交
     * orderGoods : [{"id":249,"code":"1508","image":"http://scw-api.83soft.cn/UpLoadFiles/Image/20170522231012378174.png","description":"富贵竹","price":0}]
     */

    private double originalAmount;
    private double totalProfit;
    private int id;
    private String code;
    private String createTime;
    private double total;
    private String state;
    private List<OrderGoodsBean> orderGoods;

    public double getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(double originalAmount) {
        this.originalAmount = originalAmount;
    }

    public double getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(double totalProfit) {
        this.totalProfit = totalProfit;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<OrderGoodsBean> getOrderGoods() {
        return orderGoods;
    }

    public void setOrderGoods(List<OrderGoodsBean> orderGoods) {
        this.orderGoods = orderGoods;
    }

    public static class OrderGoodsBean {
        /**
         * id : 249
         * code : 1508
         * image : http://scw-api.83soft.cn/UpLoadFiles/Image/20170522231012378174.png
         * description : 富贵竹
         * price : 0.0
         */

        private int id;
        private String code;
        private String image;
        private String description;
        private double price;

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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }
}
