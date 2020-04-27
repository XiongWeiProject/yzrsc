package com.commodity.yzrsc.model;

import java.io.Serializable;

/**
 * 转售
 * Created by yangxuqiang on 2017/3/26.
 */

public class ZhuanshouEntity implements Serializable {
//    {"data":[],"index":0,"count":0,"totalPage":0,"totalCount":0,"size":0}
    private int index;
    private int count;
    private int totalPage;
    private int totalCount;
    private int size;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }


}
