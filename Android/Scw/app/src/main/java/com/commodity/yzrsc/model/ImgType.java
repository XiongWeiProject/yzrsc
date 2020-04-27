package com.commodity.yzrsc.model;

/**
 * Created by liyushen on 2017/3/19 15:11
 * 图片相关的信息及来源的实体类
 */
public class ImgType {
    private String imgpath;
    private String describe;
    private boolean isVideo;
    private String videopath;
    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public String getVideopath() {
        return videopath;
    }

    public void setVideopath(String videopath) {
        this.videopath = videopath;
    }
}
