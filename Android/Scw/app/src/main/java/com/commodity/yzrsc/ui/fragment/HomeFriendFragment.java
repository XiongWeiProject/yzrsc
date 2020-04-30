package com.commodity.yzrsc.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.model.TypeModel;
import com.commodity.yzrsc.rongyun.server.utils.NToast;
import com.commodity.yzrsc.ui.BaseFragment;
import com.commodity.yzrsc.ui.activity.commodity.CommodityOrderActivity;
import com.commodity.yzrsc.ui.activity.friend.PicDynamicActivity;
import com.commodity.yzrsc.ui.adapter.FragmentViewPagerAdapter;
import com.commodity.yzrsc.ui.widget.imageview.CircleImageView;
import com.commodity.yzrsc.view.PopWinShare;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.utilities.RongUtils;

public class HomeFriendFragment extends BaseFragment {


    @Bind(R.id.my_image_head)
    CircleImageView myImageHead;
    @Bind(R.id.iv_release_dynamic)
    AppCompatImageView ivReleaseDynamic;
    @Bind(R.id.tl)
    TabLayout tl;
    @Bind(R.id.vp)
    ViewPager vp;

    PopWinShare popWinShare;
    List<TypeModel> typeModel = new ArrayList<>();
    @Bind(R.id.title)
    TextView title;

    String userDynamicCatalog_Id;


    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titles = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.fragment_home_friend;
    }

    @Override
    protected void initView() {
        sendRequest(1);


    }

    @Override
    protected void initListeners() {

    }

    private void setTab() {
        titles.clear();
        fragmentList.clear();
        for (int i = 0; i < typeModel.size(); i++) {
            titles.add(typeModel.get(i).getName());
            DynamicFragment dynamicFragment = DynamicFragment.newInstance(typeModel.get(i).getId(), "");
            fragmentList.add(dynamicFragment);
        }
        FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(getChildFragmentManager(), fragmentList, titles);
        vp.setAdapter(adapter);
        tl.setupWithViewPager(vp);
        vp.setCurrentItem(1);
        tl.getTabAt(0).select();
        vp.setOffscreenPageLimit(0);
    }

    @Override
    public void sendRequest(int tag) {
        super.sendRequest(tag);
        if (tag == 1) {
            customLoadding.show();
            Map<String, String> parmMap = new HashMap<String, String>();
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.GetDynamicCatalog, parmMap, this);
            httpManager.request();
        }
    }

    @Override
    public void onSuccess(int tag, ServiceInfo result) {
        super.onSuccess(tag, result);

        JSONObject response = (JSONObject) result.getResponse();
        if (response.optBoolean("success")) {
            try {
                typeModel = JSON.parseArray(response.getString("data"), TypeModel.class);
                setTab();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            tip(response.optString("msg"));
        }
    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
        tip(msg);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.my_image_head, R.id.iv_release_dynamic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.my_image_head:
                NToast.longToast(getActivity(), "点击了头衔进入个人动态");
                break;
            case R.id.iv_release_dynamic:
                if (popWinShare == null) {
                    //自定义的单击事件
                    OnClickLintener paramOnClickListener = new OnClickLintener();
                    popWinShare = new PopWinShare(getActivity(), paramOnClickListener, RongUtils.dip2px(110), RongUtils.dip2px(84));
                    //监听窗口的焦点事件，点击窗口外面则取消显示
                    popWinShare.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {

                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                popWinShare.dismiss();
                            }
                        }
                    });
                }
//设置默认获取焦点
                popWinShare.setFocusable(true);
//以某个控件的x和y的偏移量位置开始显示窗口
                popWinShare.showAsDropDown(myImageHead, -75, 0);
//如果窗口存在，则更新
                popWinShare.update();
                break;
        }
    }

    class OnClickLintener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pic:
//                    Bundle bundle=new Bundle();
//                    bundle.putString("userDynamicCatalog_Id",typeModel.get(i).getId());
                    popWinShare.dismiss();
                    jumpActivity(PicDynamicActivity.class);
                    break;
                case R.id.video:

                    break;

                default:
                    break;
            }

        }

    }
}
