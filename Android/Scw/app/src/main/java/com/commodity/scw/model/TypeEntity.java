package com.commodity.scw.model;

import java.io.Serializable;
import java.util.List;

/**
 * 分类
 * Created by yangxuqiang on 2017/5/10.
 */

public class TypeEntity implements Serializable {
    private List<TypeDataEntity> data;

    public List<TypeDataEntity> getData() {
        return data;
    }

    public void setData(List<TypeDataEntity> data) {
        this.data = data;
    }
}
