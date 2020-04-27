package com.commodity.yzrsc.model.mine;

import java.io.Serializable;

/**
 * Created by yangxuqiang on 2017/4/26.
 */

public class BlankDataEntity implements Serializable {
//    "id": 0,
//            "bankName": "string",
//            "cardNumber": "string",
//            "cardHolder": "string"
    private int id;
    private String bankName;
    private String cardNumber;
    private String cardHolder;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }
}
