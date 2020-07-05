package com.commodity.yzrsc.model;

import java.util.List;

/**
 * created by  xiongwei
 * on
 * role:
 */
public class PayModel {

    /**
     * orderIds : [2588,2589]
     * payment : alipay
     */

    private String payment;
    private List<Integer> orderIds;

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public List<Integer> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<Integer> orderIds) {
        this.orderIds = orderIds;
    }
}
