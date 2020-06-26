package com.commodity.yzrsc.model;

import java.util.List;

/**
 * created by  xiongwei
 * on
 * role:
 */
public class OrderModel {

    /**
     * ids : [0]
     * addressInfoId : 0
     */

    private String addressInfoId;
    private List<Integer> ids;

    public String getAddressInfoId() {
        return addressInfoId;
    }

    public void setAddressInfoId(String addressInfoId) {
        this.addressInfoId = addressInfoId;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}
