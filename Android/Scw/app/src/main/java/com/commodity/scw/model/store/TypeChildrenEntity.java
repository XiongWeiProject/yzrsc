package com.commodity.scw.model.store;

import java.io.Serializable;

/**
 * 分类子选项
 * Created by 328789 on 2017/4/15.
 */

public class TypeChildrenEntity implements Serializable {
    private int id;
    private String name;
    private String image;
    private int parentId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
