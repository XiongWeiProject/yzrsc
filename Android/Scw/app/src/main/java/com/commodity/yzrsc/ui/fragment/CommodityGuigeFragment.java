package com.commodity.yzrsc.ui.fragment;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.commodity.yzrsc.MainApplication;
import com.commodity.yzrsc.R;
import com.commodity.yzrsc.ui.BaseFragment;

import org.json.JSONObject;

import butterknife.Bind;

/**
 * Created by liyushen on 2017/4/8 11:29
 * 商品详细规格
 */
public class CommodityGuigeFragment extends BaseFragment {
    @Bind(R.id.ll_content)
    LinearLayout ll_content;
    @Bind(R.id.tv_huohao)
    TextView tv_huohao;
    @Bind(R.id.tv_kuanshi)
    TextView tv_kuanshi;
    @Bind(R.id.tv_chicun)
    TextView tv_chicun;
    @Bind(R.id.tv_zhongliang)
    TextView tv_zhongliang;
    JSONObject dataJson;
    @Override
    protected int getContentView() {
        return R.layout.view_commodity_xiangxiguige;
    }

    @Override
    protected void initView() {
        ll_content.setMinimumHeight(MainApplication.SCREEN_H-(MainApplication.SCREEN_W/3));
        if (dataJson!=null){
            tv_huohao.setText(dataJson.optString("code"));
            tv_kuanshi.setText(dataJson.optString("kind"));
            tv_chicun.setText(dataJson.optString("specification"));
            tv_zhongliang.setText(dataJson.optString("weight"));
        }
    }

    @Override
    protected void initListeners() {

    }

    public void setDataView(JSONObject jsonObject){
        dataJson=jsonObject;
    }
}
