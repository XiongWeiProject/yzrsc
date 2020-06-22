package com.commodity.scw.model.mine;

import java.io.Serializable;
import java.util.List;

/**
 * 我的订单
 * Created by yangxuqiang on 2017/4/21.
 */

public class MyOrdeEntity implements Serializable {
    private List<DetailMyOrdeEntity> data;

    public List<DetailMyOrdeEntity> getData() {
        return data;
    }

    public void setData(List<DetailMyOrdeEntity> data) {
        this.data = data;
    }
}
