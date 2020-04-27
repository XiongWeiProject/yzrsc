package com.commodity.yzrsc.model;

/**
 * Created by liyushen on 2017/4/3 22:22
 * 消息通知
 */
public class Notice {
    /**
     * id : 0
     * createTime : 2017-05-13T09:21:36.903Z
     * content : string
     * isRead : true
     */

    private int id;
    private String createTime;
    private String content;
    private boolean isRead;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
}
