package com.commodity.yzrsc.model.mine;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 我的订单
 *
 * Created by yangxuqiang on 2017/4/21.
 */

public class DetailMyOrdeEntity implements Serializable {
//    "data": [
//    {
//        "id": 0,
//            "code": "string",
//            "createTime": "2017-04-21T06:10:25.063Z",
//            "total": 0,
//            "state": "string",
//            "orderGoods": [
//        {
//            "id": 0,
//                "code": "string",
//                "image": "string",
//                "descritpion": "string"
//originalAmount (number, optional): 原价 ,
//    totalProfit (number, optional): 纯利润(去除服务费) ,
//        }
//        ]
//    }
//    ],
    private int id;
    private String code;
    private String createTime;
    private BigDecimal total;
    private BigDecimal originalAmount;
    private BigDecimal totalProfit;
    private String state;
    private List<MyOrdeGoodsEntity> orderGoods;

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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<MyOrdeGoodsEntity> getOrderGoods() {
        return orderGoods;
    }

    public void setOrderGoods(List<MyOrdeGoodsEntity> orderGoods) {
        this.orderGoods = orderGoods;
    }

    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }
}
