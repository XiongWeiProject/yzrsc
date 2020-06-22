package com.commodity.scw.model.store;

import java.io.Serializable;

/**
 * Created by yangxuqiang on 2017/5/8.
 */

public class ShardGridEntity implements Serializable {
    private int id;
    private String title;

    public ShardGridEntity(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
