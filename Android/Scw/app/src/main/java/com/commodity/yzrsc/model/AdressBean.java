package com.commodity.yzrsc.model;

/**
 * Created by liyushen on 2017/4/23 10:46
 * 地点实体类
 */
public class AdressBean {
    private String id;
    private String name;
    private String parent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return name;
    }
}
