package com.commodity.scw.model.store;

import java.io.Serializable;
import java.util.List;

/**
 * 上传分类
 * Created by 328789 on 2017/4/15.
 */

public class UploadTypeEntity implements Serializable {
    private List<TypeContextEntity> data;
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<TypeContextEntity> getData() {
        return data;
    }

    public void setData(List<TypeContextEntity> data) {
        this.data = data;
    }
}
