package com.commodity.scw.model.store;

import java.io.Serializable;
import java.util.List;

/**
 * 分类
 * Created by 328789 on 2017/4/15.
 */

public class TypeContextEntity implements Serializable {
//    "id": 0,
//            "name": "string",
//            "image": "string",
//            "parentId": 0,
//            "children": [
//    {}
//    ]
    private int id;
    private String name;
    private String image;
    private int parentId;
    private List<TypeChildrenEntity> children;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<TypeChildrenEntity> getChildren() {
        return children;
    }

    public void setChildren(List<TypeChildrenEntity> children) {
        this.children = children;
    }
}
