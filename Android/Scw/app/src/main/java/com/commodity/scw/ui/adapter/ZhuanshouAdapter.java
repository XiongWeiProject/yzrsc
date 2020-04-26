package com.commodity.scw.ui.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.commodity.scw.R;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.manager.ImageLoaderManager;
import com.commodity.scw.model.ShareEntity;
import com.commodity.scw.model.ZhuanshouEntity;
import com.commodity.scw.model.ZhuanshouItemEntity;
import com.commodity.scw.ui.activity.commodity.CommodityDetailActivity;
import com.commodity.scw.ui.adapter.base.CommonAdapter;
import com.commodity.scw.ui.adapter.base.ViewHolder;
import com.commodity.scw.ui.dialog.ShareDialog;
import com.commodity.scw.utils.ScreenUtils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * 转售
 * Created by yangxuqiang on 2017/3/26.
 */

public class ZhuanshouAdapter extends CommonAdapter<ZhuanshouItemEntity> {
    private Context context;
    private boolean hind;
    private boolean isCheckboxHind=true;
    private List<ZhuanshouEntity> deleteList;
    private OncheckClick oncheckClick;
    private OncheckClick onMoreClick;
    private int state;
    private int pateState;

    public ZhuanshouAdapter(Context context, List<ZhuanshouItemEntity> datas, int layoutId) {
        super(context, datas, layoutId);
        this.context=context;
    }

    @Override
    public void convert(ViewHolder holder, final ZhuanshouItemEntity zhuanshouEntity) {
        ImageView popup_button_delete = (ImageView) holder.getView(R.id.popup_button_delete);

        LinearLayout zhuanshou_item = holder.getView(R.id.zhuanshou_item);


        final int position = holder.getPosition();

        if(!isCheckboxHind){
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) zhuanshou_item.getLayoutParams();
            layoutParams.leftMargin=ScreenUtils.dp2px(mContext, 54);
            zhuanshou_item.setLayoutParams(layoutParams);
        }else {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) zhuanshou_item.getLayoutParams();
            layoutParams.leftMargin=ScreenUtils.dp2px(mContext, 0);
            zhuanshou_item.setLayoutParams(layoutParams);
        }

        if(deleteList!=null&&deleteList.contains(mDatas.get(position))){
            popup_button_delete.setBackgroundResource(R.drawable.icon_xzoff);
        }else {
            popup_button_delete.setBackgroundResource(R.drawable.icon_xzon);
        }
        //删除box
        popup_button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(oncheckClick!=null){
                    oncheckClick.click(view,position);
                }
            }
        });
        //更多
        holder.setOnClickListener(R.id.shop_more, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onMoreClick!=null){
                    onMoreClick.click(view,position);
                }
            }
        });
        //分享
        Button buttonShare = holder.getView(R.id.zhuanshou_move);
        if(hind){
            buttonShare.setVisibility(View.GONE);

        }else {
            buttonShare.setVisibility(View.VISIBLE);
            //分享
            holder.setOnClickListener(R.id.zhuanshou_move, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                            .loadImage(zhuanshouEntity.getImage(), new ImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String imageUri, View view) {

                                }

                                @Override
                                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                                }

                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                                    List<String> array = new ArrayList<>();
//                                    array.add(zhuanshouEntity.getImage());
//                                    SharetUtil.show(mContext,"http://www.soucangwang.com",loadedImage,"文巴掌商城",zhuanshouEntity.getDescription(), new ShareCallBack() {
//                                        @Override
//                                        public void onResult() {
//
//                                        }
//
//                                        @Override
//                                        public void onError() {
//
//                                        }
//
//                                        @Override
//                                        public void onCancel() {
//
//                                        }
//                                    },array);
                                    ShareEntity shareEntity = new ShareEntity();
                                    shareEntity.desc=zhuanshouEntity.getDescription();
                                    shareEntity.images=zhuanshouEntity.getImages();
                                    shareEntity.imageUrl=zhuanshouEntity.getImage();
                                    shareEntity.shareURL= zhuanshouEntity.getGoodsSaleUrl();
                                    ShareDialog shareDialog = new ShareDialog(context,shareEntity);
                                    shareDialog.show();
                                }

                                @Override
                                public void onLoadingCancelled(String imageUri, View view) {

                                }
                            });

                }
            });
        }
        //设置参数
        ImageView imgeview = holder.getView(R.id.zhuanshou_imag);
        ImageLoaderManager.getInstance().displayImage(zhuanshouEntity.getImage(),imgeview,R.drawable.ico_pic_fail_defalt);
        holder.setText(R.id.zhuanshou_content,zhuanshouEntity.getDescription());
        holder.setText(R.id.zhuan_optional,"商品ID "+zhuanshouEntity.getId());
        holder.setText(R.id.zhuan_viewCount,"浏览 "+zhuanshouEntity.getViewCount());
//        holder.setText(R.id.zhuan_soldCount,"销售 "+zhuanshouEntity.getSoldCount());
        holder.setText(R.id.zhuan_favorCount,"收藏 "+zhuanshouEntity.getFavorCount());
        holder.setText(R.id.shop_price,"¥ "+zhuanshouEntity.getPrice());
        if(state==0){
            holder.setText(R.id.shop_lir,"（利润"+zhuanshouEntity.getProfit()+"）");
        }else {
            holder.setText(R.id.shop_lir,"");
        }

        holder.setOnClickListener(R.id.item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(pateState==0){
//                    Bundle bundle = new Bundle();
//                    bundle.putString("goodsSaleId",String.valueOf(zhuanshouEntity.getId()));
//                    bundle.putString("url", IRequestConst.RequestMethod.GetParentGoodsSaleDetail);
//                    Intent intent = new Intent(mContext, CommodityDetailActivity.class);
//                    intent.putExtras(bundle);
//                    mContext.startActivity(intent);
//                }else {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("goodsSaleId",String.valueOf(zhuanshouEntity.getId()));
//                    Intent intent = new Intent(mContext, CommodityDetailActivity.class);
//                    intent.putExtras(bundle);
//                    mContext.startActivity(intent);
//                }
                Bundle bundle = new Bundle();
                bundle.putString("goodsSaleId",String.valueOf(zhuanshouEntity.getId()));
                if(pateState==0){
                    bundle.putString("proview","proview");
                }else {
                    bundle.putString("proview","p");
                }
                Intent intent = new Intent(mContext, CommodityDetailActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);

            }
        });

    }


    public void hindButton(boolean hind){
        this.hind=hind;
    }
    public void hindCheckbox(boolean isCheckboxHind){
        this.isCheckboxHind=isCheckboxHind;
        notifyDataSetChanged();
    }
    public void setDeleteList(List deleteList){
        this.deleteList=deleteList;
        notifyDataSetChanged();
    }

    public void setMoreClick(OncheckClick oncheckClick){
        this.onMoreClick=oncheckClick;
    }

    /**
     * 删除按钮
     * @param oncheckClick
     */
    public void setOncheckClick(OncheckClick oncheckClick){
        this.oncheckClick=oncheckClick;
    }

    public interface OncheckClick{
        void click(View v, int position);
    }
    public void itemAnimal(View v, int x){
        int translationx = ScreenUtils.dp2px(mContext, x);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(v, "translationX", 0, translationx);
        translationX.setDuration(400);
        translationX.setRepeatCount(0);
        translationX.start();

    }
    public void setState(int p){
        this.state=p;
    }
    public void setPageState(int state){
        this.pateState=state;
    }
}
