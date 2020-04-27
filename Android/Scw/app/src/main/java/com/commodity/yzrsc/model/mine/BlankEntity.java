package com.commodity.yzrsc.model.mine;

import java.io.Serializable;
import java.util.List;

/**
 * 银行卡
 * Created by yangxuqiang on 2017/4/26.
 */

public class BlankEntity implements Serializable {
    private List<BlankDataEntity> data;

    public List<BlankDataEntity> getData() {
        return data;
    }

    public void setData(List<BlankDataEntity> data) {
        this.data = data;
    }
}
