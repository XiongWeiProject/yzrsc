package com.commodity.yzrsc.model;

/**
 * Created by liyushen on 2017/5/13 21:06
 */
public class Banner {
    /**
     * id : 4
     * adverSource : 引导页
     * adverType : 链接
     * linkType : 外链
     * link : www.baidu.com
     * mediaUrl : /UpLoadFiles/Image/20170511121959130973.jpg
     */
    private int id;
    private String adverSource;
    private String adverType;
    private String linkType;
    private String link;
    private String mediaUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdverSource() {
        return adverSource;
    }

    public void setAdverSource(String adverSource) {
        this.adverSource = adverSource;
    }

    public String getAdverType() {
        return adverType;
    }

    public void setAdverType(String adverType) {
        this.adverType = adverType;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }
}
