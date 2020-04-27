package com.commodity.yzrsc.ui.widget.citypickerview.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.model.AdressBean;
import com.commodity.yzrsc.ui.widget.citypickerview.utils.JAssetsUtils;
import com.commodity.yzrsc.ui.widget.citypickerview.widget.wheel.OnWheelChangedListener;
import com.commodity.yzrsc.ui.widget.citypickerview.widget.wheel.WheelView;
import com.commodity.yzrsc.ui.widget.citypickerview.widget.wheel.adapters.ArrayWheelAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 省市区三级选择
 * 作者：liji on 2015/12/17 10:40
 * 邮箱：lijiwork@sina.com
 */
public class CityPicker implements CanShow, OnWheelChangedListener {

    private Context context;

    private PopupWindow popwindow;

    private View popview;

    private WheelView mViewProvince;

    private WheelView mViewCity;

    private WheelView mViewDistrict;

    private RelativeLayout mRelativeTitleBg;

    private TextView mTvOK;

    private TextView mTvTitle;

    private TextView mTvCancel;

    /**
     * 所有省
     */
    protected List<AdressBean> mProvinceDatas = new ArrayList<>();

    /**
     * key - 省 value - 市
     */
    protected Map<String, List<AdressBean>> mCitisDatasMap = new HashMap<String, List<AdressBean>>();

    /**
     * key - 市 values - 区
     */
    protected Map<String, List<AdressBean>> mDistrictDatasMap = new HashMap<String, List<AdressBean>>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceId;

    /**
     * 当前市的名称
     */
    protected String mCurrentCityId;

    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictId = "";


    private OnCityItemClickListener listener;

    public interface OnCityItemClickListener {
        void onSelected(AdressBean... citySelected);

        void onCancel();
    }

    public void setOnCityItemClickListener(OnCityItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Default text color
     */
    public static final int DEFAULT_TEXT_COLOR = 0xFF585858;

    /**
     * Default text size
     */
    public static final int DEFAULT_TEXT_SIZE = 18;

    // Text settings
    private int textColor = DEFAULT_TEXT_COLOR;

    private int textSize = DEFAULT_TEXT_SIZE;

    /**
     * 滚轮显示的item个数
     */
    private static final int DEF_VISIBLE_ITEMS = 5;

    // Count of visible items
    private int visibleItems = DEF_VISIBLE_ITEMS;

    /**
     * 省滚轮是否循环滚动
     */
    private boolean isProvinceCyclic = true;

    /**
     * 市滚轮是否循环滚动
     */
    private boolean isCityCyclic = true;

    /**
     * 区滚轮是否循环滚动
     */
    private boolean isDistrictCyclic = true;

    /**
     * item间距
     */
    private int padding = 5;


    /**
     * Color.BLACK
     */
    private String cancelTextColorStr = "#000000";


    /**
     * Color.BLUE
     */
    private String confirmTextColorStr = "#0000FF";

    /**
     * 标题背景颜色
     */
    private String titleBackgroundColorStr = "#E9E9E9";
    /**
     * 标题颜色
     */
    private String titleTextColorStr = "#E9E9E9";

    /**
     * 第一次默认的显示省份，一般配合定位，使用
     */
    private String defaultProvinceId = "江苏";

    /**
     * 第一次默认得显示城市，一般配合定位，使用
     */
    private String defaultCityId = "常州";

    /**
     * 第一次默认得显示，一般配合定位，使用
     */
    private String defaultDistrictId = "新北区";

    /**
     * 两级联动
     */
    private boolean showProvinceAndCity = false;

    /**
     * 标题
     */
    private String mTitle = "选择地区";

    /**
     * 设置popwindow的背景
     */
    private int backgroundPop = 0xa0000000;

    private CityPicker(Builder builder) {
        this.textColor = builder.textColor;
        this.textSize = builder.textSize;
        this.visibleItems = builder.visibleItems;
        this.isProvinceCyclic = builder.isProvinceCyclic;
        this.isDistrictCyclic = builder.isDistrictCyclic;
        this.isCityCyclic = builder.isCityCyclic;
        this.context = builder.mContext;
        this.padding = builder.padding;
        this.mTitle = builder.mTitle;
        this.titleBackgroundColorStr = builder.titleBackgroundColorStr;
        this.confirmTextColorStr = builder.confirmTextColorStr;
        this.cancelTextColorStr = builder.cancelTextColorStr;

        this.defaultDistrictId = builder.defaultDistrict;
        this.defaultCityId = builder.defaultCityName;
        this.defaultProvinceId = builder.defaultProvinceName;

        this.showProvinceAndCity = builder.showProvinceAndCity;
        this.backgroundPop = builder.backgroundPop;
        this.titleTextColorStr = builder.titleTextColorStr;

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        popview = layoutInflater.inflate(R.layout.pop_citypicker, null);

        mViewProvince = (WheelView) popview.findViewById(R.id.id_province);
        mViewCity = (WheelView) popview.findViewById(R.id.id_city);
        mViewDistrict = (WheelView) popview.findViewById(R.id.id_district);
        mRelativeTitleBg = (RelativeLayout) popview.findViewById(R.id.rl_title);
        mTvOK = (TextView) popview.findViewById(R.id.tv_confirm);
        mTvTitle = (TextView) popview.findViewById(R.id.tv_title);
        mTvCancel = (TextView) popview.findViewById(R.id.tv_cancel);


        popwindow = new PopupWindow(popview, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        popwindow.setBackgroundDrawable(new ColorDrawable(backgroundPop));
        popwindow.setAnimationStyle(R.style.AnimBottom);
        popwindow.setTouchable(true);
        popwindow.setOutsideTouchable(false);
        popwindow.setFocusable(true);


        /**
         * 设置标题背景颜色
         */
        if (!TextUtils.isEmpty(this.titleBackgroundColorStr)) {
            mRelativeTitleBg.setBackgroundColor(Color.parseColor(this.titleBackgroundColorStr));
        }

        /**
         * 设置标题
         */
        if (!TextUtils.isEmpty(this.mTitle)) {
            mTvTitle.setText(this.mTitle);
        }


        //设置确认按钮文字颜色
        if (!TextUtils.isEmpty(this.titleTextColorStr)) {
            mTvTitle.setTextColor(Color.parseColor(this.titleTextColorStr));
        }


        //设置确认按钮文字颜色
        if (!TextUtils.isEmpty(this.confirmTextColorStr)) {
            mTvOK.setTextColor(Color.parseColor(this.confirmTextColorStr));
        }

        //设置取消按钮文字颜色
        if (!TextUtils.isEmpty(this.cancelTextColorStr)) {
            mTvCancel.setTextColor(Color.parseColor(this.cancelTextColorStr));
        }


        //只显示省市两级联动
        if (this.showProvinceAndCity) {
            mViewDistrict.setVisibility(View.GONE);
        } else {
            mViewDistrict.setVisibility(View.VISIBLE);
        }

        //初始化城市数据
        initProvinceDatas(context);

        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
        // 添加onclick事件
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancel();
                hide();
            }
        });
        mTvOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showProvinceAndCity) {
                    listener.onSelected(getAdressBean(mCurrentProviceId, mProvinceDatas), getAdressBean(mCurrentCityId, mCitisDatasMap.get(mCurrentProviceId)), null);
                } else {
                    listener.onSelected(getAdressBean(mCurrentProviceId, mProvinceDatas), getAdressBean(mCurrentCityId, mCitisDatasMap.get(mCurrentProviceId)),
                            getAdressBean(mCurrentDistrictId, mDistrictDatasMap.get(mCurrentCityId)));
                }
                hide();
            }
        });

    }

    public static class Builder {
        /**
         * Default text color
         */
        public static final int DEFAULT_TEXT_COLOR = 0xFF585858;

        /**
         * Default text size
         */
        public static final int DEFAULT_TEXT_SIZE = 18;

        // Text settings
        private int textColor = DEFAULT_TEXT_COLOR;

        private int textSize = DEFAULT_TEXT_SIZE;

        /**
         * 滚轮显示的item个数
         */
        private static final int DEF_VISIBLE_ITEMS = 5;

        // Count of visible items
        private int visibleItems = DEF_VISIBLE_ITEMS;

        /**
         * 省滚轮是否循环滚动
         */
        private boolean isProvinceCyclic = true;

        /**
         * 市滚轮是否循环滚动
         */
        private boolean isCityCyclic = true;

        /**
         * 区滚轮是否循环滚动
         */
        private boolean isDistrictCyclic = true;

        private Context mContext;

        /**
         * item间距
         */
        private int padding = 5;


        /**
         * Color.BLACK
         */
        private String cancelTextColorStr = "#000000";


        /**
         * Color.BLUE
         */
        private String confirmTextColorStr = "#0000FF";

        /**
         * 标题背景颜色
         */
        private String titleBackgroundColorStr = "#E9E9E9";

        /**
         * 标题颜色
         */
        private String titleTextColorStr = "#E9E9E9";


        /**
         * 第一次默认的显示省份，一般配合定位，使用
         */
        private String defaultProvinceName = "江苏省";

        /**
         * 第一次默认得显示城市，一般配合定位，使用
         */
        private String defaultCityName = "常州市";

        /**
         * 第一次默认得显示，一般配合定位，使用
         */
        private String defaultDistrict = "新北区";

        /**
         * 标题
         */
        private String mTitle = "选择地区";

        /**
         * 两级联动
         */
        private boolean showProvinceAndCity = false;

        /**
         * 设置popwindow的背景
         */
        private int backgroundPop = 0xa0000000;

        public Builder(Context context) {
            this.mContext = context;
        }

        /**
         * 设置popwindow的背景
         *
         * @param backgroundPopColor
         * @return
         */
        public Builder backgroundPop(int backgroundPopColor) {
            this.backgroundPop = backgroundPopColor;
            return this;
        }

        /**
         * 设置标题背景颜色
         *
         * @param colorBg
         * @return
         */
        public Builder titleBackgroundColor(String colorBg) {
            this.titleBackgroundColorStr = colorBg;
            return this;
        }

        /**
         * 设置标题背景颜色
         *
         * @param titleTextColorStr
         * @return
         */
        public Builder titleTextColor(String titleTextColorStr) {
            this.titleTextColorStr = titleTextColorStr;
            return this;
        }


        /**
         * 设置标题
         *
         * @param mtitle
         * @return
         */
        public Builder title(String mtitle) {
            this.mTitle = mtitle;
            return this;
        }

        /**
         * 是否只显示省市两级联动
         *
         * @param flag
         * @return
         */
        public Builder onlyShowProvinceAndCity(boolean flag) {
            this.showProvinceAndCity = flag;
            return this;
        }

        /**
         * 第一次默认的显示省份，一般配合定位，使用
         *
         * @param defaultProvinceName
         * @return
         */
        public Builder province(String defaultProvinceName) {
            this.defaultProvinceName = defaultProvinceName;
            return this;
        }

        /**
         * 第一次默认得显示城市，一般配合定位，使用
         *
         * @param defaultCityName
         * @return
         */
        public Builder city(String defaultCityName) {
            this.defaultCityName = defaultCityName;
            return this;
        }

        /**
         * 第一次默认地区显示，一般配合定位，使用
         *
         * @param defaultDistrict
         * @return
         */
        public Builder district(String defaultDistrict) {
            this.defaultDistrict = defaultDistrict;
            return this;
        }

        //        /**
        //         * 确认按钮文字颜色
        //         * @param color
        //         * @return
        //         */
        //        public Builder confirTextColor(int color) {
        //            this.confirmTextColor = color;
        //            return this;
        //        }

        /**
         * 确认按钮文字颜色
         *
         * @param color
         * @return
         */
        public Builder confirTextColor(String color) {
            this.confirmTextColorStr = color;
            return this;
        }

        //        /**
        //         * 取消按钮文字颜色
        //         * @param color
        //         * @return
        //         */
        //        public Builder cancelTextColor(int color) {
        //            this.cancelTextColor = color;
        //            return this;
        //        }

        /**
         * 取消按钮文字颜色
         *
         * @param color
         * @return
         */
        public Builder cancelTextColor(String color) {
            this.cancelTextColorStr = color;
            return this;
        }

        /**
         * item文字颜色
         *
         * @param textColor
         * @return
         */
        public Builder textColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        /**
         * item文字大小
         *
         * @param textSize
         * @return
         */
        public Builder textSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        /**
         * 滚轮显示的item个数
         *
         * @param visibleItems
         * @return
         */
        public Builder visibleItemsCount(int visibleItems) {
            this.visibleItems = visibleItems;
            return this;
        }

        /**
         * 省滚轮是否循环滚动
         *
         * @param isProvinceCyclic
         * @return
         */
        public Builder provinceCyclic(boolean isProvinceCyclic) {
            this.isProvinceCyclic = isProvinceCyclic;
            return this;
        }

        /**
         * 市滚轮是否循环滚动
         *
         * @param isCityCyclic
         * @return
         */
        public Builder cityCyclic(boolean isCityCyclic) {
            this.isCityCyclic = isCityCyclic;
            return this;
        }

        /**
         * 区滚轮是否循环滚动
         *
         * @param isDistrictCyclic
         * @return
         */
        public Builder districtCyclic(boolean isDistrictCyclic) {
            this.isDistrictCyclic = isDistrictCyclic;
            return this;
        }

        /**
         * item间距
         *
         * @param itemPadding
         * @return
         */
        public Builder itemPadding(int itemPadding) {
            this.padding = itemPadding;
            return this;
        }

        public CityPicker build() {
            CityPicker cityPicker = new CityPicker(this);
            return cityPicker;
        }

    }

    private void setUpData() {
        int provinceDefault = -1;
        if (!TextUtils.isEmpty(defaultProvinceId) && mProvinceDatas.size() > 0) {
            for (int i = 0; i < mProvinceDatas.size(); i++) {
                if (mProvinceDatas.get(i).getId().contains(defaultProvinceId)) {
                    provinceDefault = i;
                    break;
                }
            }
        }
        ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter<AdressBean>(context, mProvinceDatas);
        mViewProvince.setViewAdapter(arrayWheelAdapter);
        //获取所设置的省的位置，直接定位到该位置
        if (-1 != provinceDefault) {
            mViewProvince.setCurrentItem(provinceDefault);
        }
        // 设置可见条目数量
        mViewProvince.setVisibleItems(visibleItems);
        mViewCity.setVisibleItems(visibleItems);
        mViewDistrict.setVisibleItems(visibleItems);
        mViewProvince.setCyclic(isProvinceCyclic);
        mViewCity.setCyclic(isCityCyclic);
        mViewDistrict.setCyclic(isDistrictCyclic);
        arrayWheelAdapter.setPadding(padding);
        arrayWheelAdapter.setTextColor(textColor);
        arrayWheelAdapter.setTextSize(textSize);

        updateCities();
        updateAreas();
    }

    /**
     * 解析省市区的数据
     */

    protected void initProvinceDatas(Context context) {
        //省数据
        String provinceStr = JAssetsUtils.getJsonDataFromAssets(context, "province.json");
        try {
            JSONObject jsonObject1 = new JSONObject(provinceStr);
            JSONObject provincejsonObject = jsonObject1.optJSONObject("data");
            Iterator keys = provincejsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next().toString();
                AdressBean adressBean = new AdressBean();
                adressBean.setId(key);
                adressBean.setName(provincejsonObject.optString(key));
                adressBean.setParent("0");
                mProvinceDatas.add(adressBean);
                List<AdressBean> cityList = new ArrayList<>();
                mCitisDatasMap.put(adressBean.getId(), cityList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //市数据
        String cityStr = JAssetsUtils.getJsonDataFromAssets(context, "city.json");
        List<AdressBean> allCityList = new ArrayList<>();
        try {
            JSONObject jsonObject1 = new JSONObject(cityStr);
            JSONArray cityjsonObject = jsonObject1.optJSONArray("data");
            for (int i = 0; i < cityjsonObject.length(); i++) {
                AdressBean adressBean = new AdressBean();
                adressBean.setId(cityjsonObject.getJSONObject(i).optString("id"));
                adressBean.setName(cityjsonObject.getJSONObject(i).optString("name"));
                adressBean.setParent(cityjsonObject.getJSONObject(i).optString("parent"));
                allCityList.add(adressBean);
                List<AdressBean> districtList = new ArrayList<>();
                mDistrictDatasMap.put(adressBean.getId(), districtList);
            }
            for (String adressBeanid : mCitisDatasMap.keySet()) {
                for (int i = 0; i < allCityList.size(); i++) {
                    if (adressBeanid.equals(allCityList.get(i).getParent())) {
                        mCitisDatasMap.get(adressBeanid).add(allCityList.get(i));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //县数据
        String districtStr = JAssetsUtils.getJsonDataFromAssets(context, "district.json");
        List<AdressBean> allDistrictStrList = new ArrayList<>();
        try {
            JSONObject jsonObject1 = new JSONObject(districtStr);
            JSONArray districtjsonObject = jsonObject1.optJSONArray("data");
            for (int i = 0; i < districtjsonObject.length(); i++) {
                AdressBean adressBean = new AdressBean();
                adressBean.setId(districtjsonObject.getJSONObject(i).optString("id"));
                adressBean.setName(districtjsonObject.getJSONObject(i).optString("name"));
                adressBean.setParent(districtjsonObject.getJSONObject(i).optString("parent"));
                allDistrictStrList.add(adressBean);
            }
            for (String adressBeanid : mDistrictDatasMap.keySet()) {
                for (int i = 0; i < allDistrictStrList.size(); i++) {
                    if (adressBeanid.equals(allDistrictStrList.get(i).getParent())) {
                        mDistrictDatasMap.get(adressBeanid).add(allDistrictStrList.get(i));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mCurrentProviceId = mProvinceDatas.get(11).getId();
        mCurrentCityId = mCitisDatasMap.get(mCurrentProviceId).get(0).getId();
        mCurrentDistrictId = mDistrictDatasMap.get(mCurrentCityId).get(0).getId();
        Log.e("initProvinceDatas: ", mProvinceDatas.size() + "==" + mCitisDatasMap.size() + "==" + mDistrictDatasMap.size());
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityId = mCitisDatasMap.get(mCurrentProviceId).get(pCurrent).getId();
        List<AdressBean> areas = mDistrictDatasMap.get(mCurrentCityId);

        if (areas == null) {
            areas = new ArrayList<>();
        }

        int districtDefault = -1;
        if (!TextUtils.isEmpty(defaultDistrictId) && areas.size() > 0) {
            for (int i = 0; i < areas.size(); i++) {
                if (areas.get(i).getId().contains(defaultDistrictId)) {
                    districtDefault = i;
                    break;
                }
            }
        }

        ArrayWheelAdapter districtWheel = new ArrayWheelAdapter<AdressBean>(context, areas);
        // 设置可见条目数量
        districtWheel.setTextColor(textColor);
        districtWheel.setTextSize(textSize);
        mViewDistrict.setViewAdapter(districtWheel);
        if (-1 != districtDefault) {
            mViewDistrict.setCurrentItem(districtDefault);
            //获取默认设置的区
            mCurrentDistrictId = defaultDistrictId;
        } else {
            mViewDistrict.setCurrentItem(0);
            //获取第一个区名称
            mCurrentDistrictId = mDistrictDatasMap.get(mCurrentCityId).get(0).getId();

        }
        districtWheel.setPadding(padding);
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceId = mProvinceDatas.get(pCurrent).getId();
        List<AdressBean> cities = mCitisDatasMap.get(mCurrentProviceId);
        if (cities == null) {
            cities = new ArrayList<>();
        }

        int cityDefault = -1;
        if (!TextUtils.isEmpty(defaultCityId) && cities.size() > 0) {
            for (int i = 0; i < cities.size(); i++) {
                if (cities.get(i).getId().contains(defaultCityId)) {
                    cityDefault = i;
                    break;
                }
            }
        }

        ArrayWheelAdapter cityWheel = new ArrayWheelAdapter<AdressBean>(context, cities);
        // 设置可见条目数量
        cityWheel.setTextColor(textColor);
        cityWheel.setTextSize(textSize);
        mViewCity.setViewAdapter(cityWheel);
        if (-1 != cityDefault) {
            mViewCity.setCurrentItem(cityDefault);
        } else {
            mViewCity.setCurrentItem(0);
        }

        cityWheel.setPadding(padding);
        updateAreas();
    }

    @Override
    public void setType(int type) {
    }

    @Override
    public void show() {
        if (!isShow()) {
            setUpData();
            popwindow.showAtLocation(popview, Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    public void hide() {
        if (isShow()) {
            popwindow.dismiss();
        }
    }

    @Override
    public boolean isShow() {
        return popwindow.isShowing();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewProvince) {

            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictId = mDistrictDatasMap.get(mCurrentCityId).get(newValue).getId();
        }
    }

    private AdressBean getAdressBean(String tag, List<AdressBean> adressBeens) {
        for (int i = 0; i < adressBeens.size(); i++) {
            if (tag.equals(adressBeens.get(i).getId())) {
                return adressBeens.get(i);
            }
        }
        return null;
    }

    public void setmProvincesCurrentItem(String str){
        int provinceDefault = -1;
        if (!TextUtils.isEmpty(str) && mProvinceDatas.size() > 0) {
            for (int i = 0; i < mProvinceDatas.size(); i++) {
                if (mProvinceDatas.get(i).getName().contains(str)) {
                    provinceDefault = i;
                    mCurrentProviceId=mProvinceDatas.get(i).getId();
                    break;
                }
            }
        }
        //获取所设置的省的位置，直接定位到该位置
        if (-1 != provinceDefault) {
            mViewProvince.setCurrentItem(provinceDefault);
        }
    }

    public void setmCitisCurrentItem(String str){
        int provinceDefault = -1;
        List<AdressBean> list= mCitisDatasMap.containsKey(mCurrentProviceId)?mCitisDatasMap.get(mCurrentProviceId):null;
        if (list!=null){
            if (!TextUtils.isEmpty(str) && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getName().contains(str)) {
                        provinceDefault = i;
                        mCurrentCityId=list.get(i).getId();
                        break;
                    }
                }
            }
        }
        //获取所设置的省的位置，直接定位到该位置
        if (-1 != provinceDefault) {
            mViewCity.setCurrentItem(provinceDefault);
        }
    }

    public void setmDistrictCurrentItem(String str){
        int provinceDefault = -1;
        List<AdressBean> list= mDistrictDatasMap.containsKey(mCurrentCityId)?mDistrictDatasMap.get(mCurrentCityId):null;
        if (list!=null){
            if (!TextUtils.isEmpty(str) && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getName().contains(str)) {
                        provinceDefault = i;
                        mCurrentDistrictId=list.get(i).getId();
                        break;
                    }
                }
            }
        }
        //获取所设置的省的位置，直接定位到该位置
        if (-1 != provinceDefault) {
            mViewDistrict.setCurrentItem(provinceDefault);
        }
    }
}
