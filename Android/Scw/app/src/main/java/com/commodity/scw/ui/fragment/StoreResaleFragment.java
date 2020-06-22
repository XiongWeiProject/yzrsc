package com.commodity.scw.ui.fragment;

import android.widget.ListView;

import com.commodity.yzrsc.R;
import com.commodity.scw.model.ZhuanshouEntity;
import com.commodity.scw.model.ZhuanshouItemEntity;
import com.commodity.scw.ui.BaseFragment;
import com.commodity.scw.ui.adapter.ZhuanshouAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 我的转售
 * Created by yangxuqiang on 2017/3/25.
 */

public class StoreResaleFragment extends BaseFragment {
    @Bind(R.id.zhuanshou_listview)
    ListView zhuanshou_listview;

    private List<ZhuanshouItemEntity> data=new ArrayList();
    @Override
    protected int getContentView() {
        return R.layout.framgnet_store_resale;
    }

    @Override
    protected void initView() {
        for (int i = 0; i < 10; i++) {
            data.add(new ZhuanshouItemEntity());
        }
        ZhuanshouAdapter zhuanshouAdapter = new ZhuanshouAdapter(mContext, data, R.layout.item_zhuanshou);
        zhuanshou_listview.setAdapter(zhuanshouAdapter);
    }

    @Override
    protected void initListeners() {

    }
}
