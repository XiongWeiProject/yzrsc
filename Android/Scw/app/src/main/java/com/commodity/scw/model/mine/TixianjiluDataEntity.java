package com.commodity.scw.model.mine;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by yangxuqiang on 2017/4/27.
 */

public class TixianjiluDataEntity implements Serializable {
//    {
//        "id": 0,
//            "amount": 0,
//            "status": "string",
//            "createTime": "2017-04-27T10:52:36.576Z"
//    transactionType
//    }
    private int id;
    private BigDecimal amount;
    private String status;
    private String createTime;
    private String transactionType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}
