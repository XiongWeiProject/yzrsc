package com.commodity.scw.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.yohoutils.StringUtil;

/**
 * Created by liyushen on 2017/6/3 17:04
 */
public class AlbumInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String albumID = null;//ID
    private String albumName = null;
    private List<String> photos = null;//前几天列表
    private int total;//总数
    private String filePath;//文件夹路径
    private boolean isSelect = false;//是否被选中

    public String getAlbumID() {
        return albumID;
    }

    public void setAlbumID(String albumID) {
        this.albumID = albumID;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public AlbumInfo() {
    }

    public AlbumInfo(JSONObject jo) {
        if (jo == null) {
            return;
        }
        albumID = jo.optString("id");
        albumName = jo.optString("title");
        total = StringUtil.valueOfInt(jo.optString("photo_num"), 0);
        JSONArray jaPhotos = jo.optJSONArray("items");
        if (jaPhotos != null && jaPhotos.length() > 0) {
            photos = new ArrayList<String>();
            for (int i = 0; i < jaPhotos.length(); i++) {
                JSONObject joItem = jaPhotos.optJSONObject(i);
                if (joItem != null) {
                    String url = joItem.optString("image");
                    if (!StringUtil.isEmpty(url)) {
                        photos.add(url);
                    }
                }
            }
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((albumID == null) ? 0 : albumID.hashCode());
        result = prime * result
                + ((albumName == null) ? 0 : albumName.hashCode());
        result = prime * result
                + ((filePath == null) ? 0 : filePath.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AlbumInfo other = (AlbumInfo) obj;
        if (albumID == null) {
            if (other.albumID != null)
                return false;
        } else if (!albumID.equals(other.albumID))
            return false;
        if (albumName == null) {
            if (other.albumName != null)
                return false;
        } else if (!albumName.equals(other.albumName))
            return false;
        if (filePath == null) {
            if (other.filePath != null)
                return false;
        } else if (!filePath.equals(other.filePath))
            return false;
        return true;
    }
}