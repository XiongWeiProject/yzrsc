package com.commodity.scw.model;

/**
 * Created by liyushen on 2017/5/30 23:19
 */
public class PicInfo {
    private String img = "";//图片地址

    private String desc = "";//简介

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public PicInfo(String img){
        this.img=img;
    }
    public PicInfo(){
    }
}
