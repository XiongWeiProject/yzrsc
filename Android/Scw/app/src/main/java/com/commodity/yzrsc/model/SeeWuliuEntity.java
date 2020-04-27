package com.commodity.yzrsc.model;

import java.io.Serializable;
import java.util.List;

/**
 * 物流
 * Created by yangxuqiang on 2017/4/4.
 */

public class SeeWuliuEntity implements Serializable {
    private List<WuliuDataEntity> data;

    public List<WuliuDataEntity> getData() {
        return data;
    }

    public void setData(List<WuliuDataEntity> data) {
        this.data = data;
    }
}
