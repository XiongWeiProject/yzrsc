package com.commodity.yzrsc.ui.activity.friend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.commodity.scw.ui.widget.webview.AdvancedWebView;
import com.commodity.scw.utils.SharetUtil;
import com.commodity.yzrsc.R;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.view.RoundAngleImageView;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.tools.RongWebviewActivity;

public class DynamicDetailsActivity extends BaseActivity {

    @Bind(R.id.head_back)
    ImageView headBack;
    @Bind(R.id.tv_weixin)
    ImageView tvWeixin;
    @Bind(R.id.iv_qq)
    ImageView ivQq;
    @Bind(R.id.ll_head)
    RoundAngleImageView llHead;
    @Bind(R.id.dynamic_name)
    TextView dynamicName;
    @Bind(R.id.dynamic_content)
    TextView dynamicContent;
    @Bind(R.id.rcv_pic)
    RecyclerView rcvPic;
    @Bind(R.id.dynamic_video)
    ImageView dynamicVideo;
    @Bind(R.id.fl_video)
    FrameLayout flVideo;
    @Bind(R.id.dynamic_time)
    TextView dynamicTime;
    @Bind(R.id.dynamic_zan)
    ImageView dynamicZan;
    @Bind(R.id.icon_like)
    ImageView iconLike;
    @Bind(R.id.rcv_zan)
    RecyclerView rcvZan;
    @Bind(R.id.ll_zan)
    LinearLayout llZan;
    @Bind(R.id.view_line)
    View viewLine;
    @Bind(R.id.rcv_evalution)
    RecyclerView rcvEvalution;
    @Bind(R.id.tv_more)
    ImageView tvMore;
    @Bind(R.id.rl_more)
    RelativeLayout rlMore;
    @Bind(R.id.ll_evalution)
    LinearLayout llEvalution;
    @Bind(R.id.tv_android)
    ImageView tvAndroid;
    @Bind(R.id.iv_ios)
    ImageView ivIos;

    public static void startAction(Context activity, String id, String title) {
        Intent intent = new Intent(activity, DynamicDetailsActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        activity.startActivity(intent);
    }
    @Override
    protected int getContentView() {
        return R.layout.activity_dynamic_details;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.head_back, R.id.tv_weixin, R.id.iv_qq, R.id.tv_android, R.id.iv_ios})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back:
                finish();
                break;
            case R.id.tv_weixin:
                SharetUtil.shareUrl(DynamicDetailsActivity.this, SHARE_MEDIA.WEIXIN,"www.baidu.com","",null);
                break;
            case R.id.iv_qq:
                SharetUtil.shareUrl(DynamicDetailsActivity.this, SHARE_MEDIA.QQ,"www.baidu.com","",null);
                break;
            case R.id.tv_android:
                WebviewActivity.startAction(DynamicDetailsActivity.this,"www.baidu.com","安卓下载");
                break;
            case R.id.iv_ios:
                WebviewActivity.startAction(DynamicDetailsActivity.this,"www.baidu.com","IOS下载");
                break;
        }
    }
}
