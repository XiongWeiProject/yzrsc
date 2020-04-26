package com.commodity.scw.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yangxuqiang on 2017/4/25.
 */

public class ZhuansouDataEntity implements Serializable {
    private List<ZhuanshouItemEntity> data;
    private ZhuanshouEntity pageInfo;

    public List<ZhuanshouItemEntity> getData() {
        return data;
    }

    public void setData(List<ZhuanshouItemEntity> data) {
        this.data = data;
    }

    public ZhuanshouEntity getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(ZhuanshouEntity pageInfo) {
        this.pageInfo = pageInfo;
    }
}
