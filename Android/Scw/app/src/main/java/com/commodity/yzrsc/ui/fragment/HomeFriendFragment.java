package com.commodity.yzrsc.ui.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;

import com.alibaba.fastjson.JSON;
import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.model.TypeModel;
import com.commodity.yzrsc.ui.BaseFragment;
import com.commodity.yzrsc.ui.adapter.FragmentViewPagerAdapter;
import com.commodity.yzrsc.ui.widget.imageview.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

public class HomeFriendFragment extends BaseFragment {


    @Bind(R.id.my_image_head)
    CircleImageView myImageHead;
    @Bind(R.id.iv_release_dynamic)
    AppCompatImageView ivReleaseDynamic;
    @Bind(R.id.tl)
    TabLayout tl;
    @Bind(R.id.vp)
    ViewPager vp;


    List<TypeModel> typeModel = new ArrayList<>();


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


}
