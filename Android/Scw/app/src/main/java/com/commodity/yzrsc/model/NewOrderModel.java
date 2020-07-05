package com.commodity.yzrsc.model;

import java.io.Serializable;
import java.util.List;

/**
 * created by  xiongwei
 * on
 * role:
 */
public class NewOrderModel implements Serializable {

    /**
     * data : [2592]
     * success : true
     * msg : 下单成功
     */

    private boolean success;
    private String msg;
    private List<Integer> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }
}
