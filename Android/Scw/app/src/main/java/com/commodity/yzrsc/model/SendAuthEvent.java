package com.commodity.yzrsc.model;

public class SendAuthEvent {

    private int errorCode;

    private String code;

    public SendAuthEvent(int errorCode, String code) {
        this.errorCode = errorCode;
        this.code = code;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
