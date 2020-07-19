package com.commodity.yzrsc.model;

/**
 * created by  xiongwei
 * on
 * role:
 */
public class PayParemsModel {

    /**
     * app_id : 2021001164600354
     * notify_url : http://yzrsc.83soft.cn/PayNotify/alipay.aspx
     * timestamp : 2020-07-19 14:30:26
     * biz_content : {"subject":"易州人商城-购买商品","out_trade_no":"O637307658241727140","timeout_express":"1d","total_amount":"0.01","product_code":"QUICK_MSECURITY_PAY"}
     * method : alipay.trade.app.pay
     * charset : utf-8
     * sign_type : RSA2
     * version : 1.0
     * sign : akXUybXKcqO4E6AW5N/BxtSzDnLRNLTvVYiF1qD2uUgH+qDNPyjYLWIjE2yJmAOqwoUuHp+3++HBqVgrFSoU9vtm7GDMcFmSLHu1YB2aG12Fes3nuR3LIdDuW/G5xC3wtkXZ3uHQQG+AM+jUOBNo4mas2vkTGRPryJN1cfGoE17Kvi1FhNyxharqE+U/VsJOgSwIUee1chRs4aq6r8oSPkSPjA8LULdKTfi0ZlwKgTOY3WXHqTyJYYIfw1KMEE0DvXX8+BYq0WKRRYpqnAqkIq9p9fLqFW76lhgd7tealCTO5i4NOX6RmB97P0f+xZoWvfXGmPVFx8EwCba3KqycTA==
     * out_trade_no : O637307658241727140
     * payment_link : app_id=2021001164600354&notify_url=http%3a%2f%2fyzrsc.83soft.cn%2fPayNotify%2falipay.aspx&timestamp=2020-07-19+14%3a30%3a26&biz_content=%7b%22subject%22%3a%22%e6%98%93%e5%b7%9e%e4%ba%ba%e5%95%86%e5%9f%8e-%e8%b4%ad%e4%b9%b0%e5%95%86%e5%93%81%22%2c%22out_trade_no%22%3a%22O637307658241727140%22%2c%22timeout_express%22%3a%221d%22%2c%22total_amount%22%3a%220.01%22%2c%22product_code%22%3a%22QUICK_MSECURITY_PAY%22%7d&method=alipay.trade.app.pay&charset=utf-8&sign_type=RSA2&version=1.0&sign=akXUybXKcqO4E6AW5N%2fBxtSzDnLRNLTvVYiF1qD2uUgH%2bqDNPyjYLWIjE2yJmAOqwoUuHp%2b3%2b%2bHBqVgrFSoU9vtm7GDMcFmSLHu1YB2aG12Fes3nuR3LIdDuW%2fG5xC3wtkXZ3uHQQG%2bAM%2bjUOBNo4mas2vkTGRPryJN1cfGoE17Kvi1FhNyxharqE%2bU%2fVsJOgSwIUee1chRs4aq6r8oSPkSPjA8LULdKTfi0ZlwKgTOY3WXHqTyJYYIfw1KMEE0DvXX8%2bBYq0WKRRYpqnAqkIq9p9fLqFW76lhgd7tealCTO5i4NOX6RmB97P0f%2bxZoWvfXGmPVFx8EwCba3KqycTA%3d%3d
     */

    private String app_id;
    private String notify_url;
    private String timestamp;
    private String biz_content;
    private String method;
    private String charset;
    private String sign_type;
    private String version;
    private String sign;
    private String out_trade_no;
    private String payment_link;

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getBiz_content() {
        return biz_content;
    }

    public void setBiz_content(String biz_content) {
        this.biz_content = biz_content;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getPayment_link() {
        return payment_link;
    }

    public void setPayment_link(String payment_link) {
        this.payment_link = payment_link;
    }
}
