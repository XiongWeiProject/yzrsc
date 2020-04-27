package com.commodity.yzrsc.model.mine;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yangxuqiang on 2017/4/27.
 */

public class TixianjiluEntity implements Serializable {
    private List<TixianjiluDataEntity> data;

    public List<TixianjiluDataEntity> getData() {
        return data;
    }

    public void setData(List<TixianjiluDataEntity> data) {
        this.data = data;
    }
}
