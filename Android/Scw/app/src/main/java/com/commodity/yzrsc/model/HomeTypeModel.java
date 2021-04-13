package com.commodity.yzrsc.model;

import java.util.List;

public class HomeTypeModel  {

    /**
     * id : 42
     * name : 逛超市
     * image : https://api.acb.wang/UploadFiles/Image/20200516233751001609534011718.jpg
     * parentId : 0
     * priority : 11
     * children : [{"id":43,"name":"新鲜水果","image":"https://api.acb.wang/UploadFiles/Image/20200822105111826820535910536.png","parentId":42,"priority":1,"children":[]},{"id":44,"name":"粮油副食","image":"https://api.acb.wang/UploadFiles/Image/20200822110149499573894373360.png","parentId":42,"priority":0,"children":[]},{"id":50,"name":"日用百货","image":"https://api.acb.wang/UploadFiles/Image/202008221103433276431201132722.png","parentId":42,"priority":0,"children":[]},{"id":70,"name":"冷藏冷冻","image":"https://api.acb.wang/UploadFiles/Image/202008221103535151271664901288.png","parentId":42,"priority":0,"children":[]},{"id":71,"name":"时令蔬菜","image":"https://api.acb.wang/UploadFiles/Image/20200822111002795796116754499.png","parentId":42,"priority":0,"children":[]},{"id":72,"name":"饮料酒水","image":"https://api.acb.wang/UploadFiles/Image/202008221109504364191081645114.png","parentId":42,"priority":0,"children":[]},{"id":73,"name":"母婴呵护","image":"https://api.acb.wang/UploadFiles/Image/202008221110253270341201132722.png","parentId":42,"priority":0,"children":[]},{"id":74,"name":"文娱用品","image":"https://api.acb.wang/UploadFiles/Image/202008221111004988721919957188.png","parentId":42,"priority":0,"children":[]},{"id":75,"name":"好吃零食","image":"https://api.acb.wang/UploadFiles/Image/202008221111139676191957478784.png","parentId":42,"priority":0,"children":[]},{"id":76,"name":"女性护理","image":"https://api.acb.wang/UploadFiles/Image/202008221111205301081313529397.png","parentId":42,"priority":0,"children":[]},{"id":77,"name":"肉蛋家禽","image":"https://api.acb.wang/UploadFiles/Image/202008221111275926144357824.png","parentId":42,"priority":0,"children":[]},{"id":78,"name":"家居清洁","image":"https://api.acb.wang/UploadFiles/Image/202008221111360925991695163838.png","parentId":42,"priority":0,"children":[]},{"id":79,"name":"计生用品","image":"https://api.acb.wang/UploadFiles/Image/202008221111412019541575676230.png","parentId":42,"priority":0,"children":[]},{"id":80,"name":"家用家电","image":"https://api.acb.wang/UploadFiles/Image/20200916142334577933355729715.jpg","parentId":42,"priority":0,"children":[]}]
     */

    private int id;
    private String name;
    private String image;
    private int parentId;
    private int priority;
    private List<ChildrenBean> children;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public List<ChildrenBean> getChildren() {
        return children;
    }

    public void setChildren(List<ChildrenBean> children) {
        this.children = children;
    }

    public static class ChildrenBean {
        /**
         * id : 43
         * name : 新鲜水果
         * image : https://api.acb.wang/UploadFiles/Image/20200822105111826820535910536.png
         * parentId : 42
         * priority : 1
         * children : []
         */

        private int id;
        private String name;
        private String image;
        private int parentId;
        private int priority;
        private List<?> children;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public List<?> getChildren() {
            return children;
        }

        public void setChildren(List<?> children) {
            this.children = children;
        }
    }
}
