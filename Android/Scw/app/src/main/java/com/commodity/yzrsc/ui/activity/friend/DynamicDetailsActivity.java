package com.commodity.yzrsc.ui.activity.friend;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.manager.ImageLoaderManager;
import com.commodity.yzrsc.model.DynamicAllListModel;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.adapter.EvalutionAdapter;
import com.commodity.yzrsc.ui.adapter.ShowPicAdapter;
import com.commodity.yzrsc.ui.adapter.ZanAdapter;
import com.commodity.yzrsc.utils.SharetUtil;
import com.commodity.yzrsc.view.MoreEvalutionDialog;
import com.commodity.yzrsc.view.RoundAngleImageView;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    DynamicAllListModel dynamicAllListModels;
    ShowPicAdapter showPicAdapter;
    public static void startAction(Context activity, String id, String title) {
        Intent intent = new Intent(activity, DynamicDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("title", title);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }
    @Override
    protected int getContentView() {
        return R.layout.activity_dynamic_details;
    }

    @Override
    protected void initView() {
        dynamicAllListModels = (DynamicAllListModel) WeakDataHolder.getInstance().getData("dynamicAllListModels");
        dynamicName.setText(dynamicAllListModels.getMemberNickname()+"");
        dynamicContent.setText(dynamicAllListModels.getDescription()+"");
        dynamicTime.setText(dynamicAllListModels.getCreateTime()+"");
        ImageLoaderManager.getInstance().displayImage(dynamicAllListModels.getMemberAvatar(), llHead,R.drawable.ico_pic_fail_defalt);
        if (dynamicAllListModels.getPictures().size() == 0) {
            if (TextUtils.isEmpty(dynamicAllListModels.getVideoUrl())) {
                rcvPic.setVisibility(View.GONE);
            } else {
                rcvPic.setVisibility(View.VISIBLE);
                List<String> list = new ArrayList<>();
                list.add(dynamicAllListModels.getExt1());
                showPicAdapter = new ShowPicAdapter(mContext, list, R.layout.item_show_pic, 1);
                rcvPic.setLayoutManager(new GridLayoutManager(mContext, 2));
                rcvPic.setAdapter(showPicAdapter);
            }
        } else {
            rcvPic.setVisibility(View.VISIBLE);
            if (dynamicAllListModels.getPictures().size() == 1) {
                showPicAdapter = new ShowPicAdapter(mContext, dynamicAllListModels.getPictures(), R.layout.item_show_pic, 0);
                rcvPic.setLayoutManager(new GridLayoutManager(mContext, 2));
            } else {
                showPicAdapter = new ShowPicAdapter(mContext, dynamicAllListModels.getPictures(), R.layout.item_shows_pic, 0);
                rcvPic.setLayoutManager(new GridLayoutManager(mContext, 3));
            }
            rcvPic.setAdapter(showPicAdapter);

        }
        if (dynamicAllListModels.getLikeCount() == 0 && dynamicAllListModels.getCommentCount() == 0) {
            llEvalution.setVisibility(View.GONE);
        }
        if (dynamicAllListModels.getLikeList()!=null&&dynamicAllListModels.getLikeCount() > 0) {
            llEvalution.setVisibility(View.VISIBLE);
            llZan.setVisibility(View.VISIBLE);
            viewLine.setVisibility(View.GONE);
            rcvZan.setLayoutManager(new GridLayoutManager(mContext, 3));
            //获取点赞列表
            ZanAdapter zanAdapter = new ZanAdapter(mContext, dynamicAllListModels.getLikeList(), R.layout.item_zan);
            rcvZan.setAdapter(zanAdapter);
        }
        if (dynamicAllListModels.getCommentList()!=null&&dynamicAllListModels.getCommentCount() > 0) {
            llEvalution.setVisibility(View.VISIBLE);
            rcvEvalution.setVisibility(View.VISIBLE);
            viewLine.setVisibility(View.GONE);

            rcvEvalution.setLayoutManager(new LinearLayoutManager(mContext));
            EvalutionAdapter evalutionAdapter = new EvalutionAdapter(mContext, dynamicAllListModels.getCommentList(), R.layout.item_evalution);
            rcvEvalution.setAdapter(evalutionAdapter);
        }
        if (dynamicAllListModels.getCommentCount()>5){
            rlMore.setVisibility(View.VISIBLE);
            rlMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //提交成功
                    final MoreEvalutionDialog renzhengSuccessDialog = new MoreEvalutionDialog(mContext, dynamicAllListModels.getId()+"");
                    renzhengSuccessDialog.show();
                    renzhengSuccessDialog.setOnclickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            renzhengSuccessDialog.dismiss();
                        }
                    });
                    renzhengSuccessDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            switch (keyCode) {
                                case KeyEvent.KEYCODE_BACK:
                                    return true;
                                default:
                                    break;
                            }
                            return false;
                        }
                    });
                }
            });
        }else {
            rlMore.setVisibility(View.GONE);
        }
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
                SharetUtil.shareUrl(DynamicDetailsActivity.this, SHARE_MEDIA.WEIXIN,"http://yzrsc.83soft.cn//m/UserDynamic/Detail?id="+dynamicAllListModels.getId(),dynamicAllListModels.getDescription(),null);
                break;
            case R.id.iv_qq:
                SharetUtil.shareUrl(DynamicDetailsActivity.this, SHARE_MEDIA.QQ,"http://yzrsc.83soft.cn//m/UserDynamic/Detail?id="+dynamicAllListModels.getId(),dynamicAllListModels.getDescription(),null);
                break;
            case R.id.tv_android:
                WebviewActivity.startAction(DynamicDetailsActivity.this,"https://www.baidu.com/","安卓下载");
                break;
            case R.id.iv_ios:
                WebviewActivity.startAction(DynamicDetailsActivity.this,"https://www.baidu.com/","IOS下载");
                break;
        }
    }
}
