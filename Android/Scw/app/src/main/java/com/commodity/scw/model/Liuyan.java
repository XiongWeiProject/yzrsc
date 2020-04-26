package com.commodity.scw.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yangxuqiang on 2017/6/2.
 */

public class Liuyan implements Serializable {
    private List<LiuyanEntity> data;

    public List<LiuyanEntity> getData() {
        return data;
    }

    public void setData(List<LiuyanEntity> data) {
        this.data = data;
    }
}
