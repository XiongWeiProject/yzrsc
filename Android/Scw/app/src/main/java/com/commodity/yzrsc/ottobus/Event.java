package com.commodity.yzrsc.ottobus;


import com.commodity.yzrsc.model.CommodityBean;

/**
 * 作者：liyushen on 2016/12/7 14:22
 * 功能：简化应用组件间的通信的实现方法
 */
public class Event {
    //得到微信授权登录返回信息
    public static class GetWeChatInfo {
        public GetWeChatInfo() {
        }

        public GetWeChatInfo(String str) {
            this.string = str;
        }

        private String string;

        public String getStr() {
            return string;
        }
    }
    //发送商品详情实体类
    public static class SendCommodInfo {
        public SendCommodInfo() {
        }

        public SendCommodInfo(CommodityBean commodityBean) {
            this.commodityBean = commodityBean;
        }

        private CommodityBean commodityBean;

        public CommodityBean getCommodityBean() {
            return commodityBean;
        }
    }

    //收到融云的消息了
    public static class ReceiverRongyunMessage {
        public ReceiverRongyunMessage() {
        }

        public ReceiverRongyunMessage(String string) {
            this.userid = string;
        }

        private String userid;

        public String getUserid() {
            return userid;
        }
    }

    //通知要改变一些操作
    public static class NotifyChangedView {
        public NotifyChangedView() {
        }

        public NotifyChangedView(Object data) {
            this.data = data;
        }

        private Object data;

        public Object getDataObject() {
            return data;
        }
    }
}
