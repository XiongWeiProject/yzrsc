package com.commodity.scw.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liyushen on 2017/4/29 13:34
 * 使用帮助实体类
 */
public class UseHelp implements Serializable {
    /**
     * id : 1
     * name : 交易介绍
     * sorter : 3
     * guides : [{"id":1,"title":"交易流程","description":"<p>测试交易流程<\/p><p><img alt=\"\" data-cke-saved-src=\"https://pic.36krcnd.com/avatar/201704/26120045/wp9mg3snxlv9yn2r.jpg!feature\" src=\"https://pic.36krcnd.com/avatar/201704/26120045/wp9mg3snxlv9yn2r.jpg!feature\" style=\"width: 200px; height: 125px;\">\u200b\u200b\u200b\u200b\u200b\u200b\u200b<br><\/p>","guideTypeId":1,"guideTypeName":"交易介绍","sorter":2},{"id":2,"title":"如何购买商品","description":"<p>测试如何购买商品内容<br><\/p>","guideTypeId":1,"guideTypeName":"交易介绍","sorter":1}]
     */
    private static final long serialVersionUID = 5231134212346077681L;
    private int id;
    private String name;
    private int sorter;
    private List<GuidesBean> guides;

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

    public int getSorter() {
        return sorter;
    }

    public void setSorter(int sorter) {
        this.sorter = sorter;
    }

    public List<GuidesBean> getGuides() {
        return guides;
    }

    public void setGuides(List<GuidesBean> guides) {
        this.guides = guides;
    }

    public  class GuidesBean {
        /**
         * id : 1
         * title : 交易流程
         * description : <p>测试交易流程</p><p><img alt="" data-cke-saved-src="https://pic.36krcnd.com/avatar/201704/26120045/wp9mg3snxlv9yn2r.jpg!feature" src="https://pic.36krcnd.com/avatar/201704/26120045/wp9mg3snxlv9yn2r.jpg!feature" style="width: 200px; height: 125px;">​​​​​​​<br></p>
         * guideTypeId : 1
         * guideTypeName : 交易介绍
         * sorter : 2
         */

        private int id;
        private String title;
        private String description;
        private int guideTypeId;
        private String guideTypeName;
        private int sorter;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getGuideTypeId() {
            return guideTypeId;
        }

        public void setGuideTypeId(int guideTypeId) {
            this.guideTypeId = guideTypeId;
        }

        public String getGuideTypeName() {
            return guideTypeName;
        }

        public void setGuideTypeName(String guideTypeName) {
            this.guideTypeName = guideTypeName;
        }

        public int getSorter() {
            return sorter;
        }

        public void setSorter(int sorter) {
            this.sorter = sorter;
        }
    }
}
