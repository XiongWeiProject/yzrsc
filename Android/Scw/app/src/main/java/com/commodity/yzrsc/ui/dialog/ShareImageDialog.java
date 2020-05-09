package com.commodity.yzrsc.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.commodity.yzrsc.MainApplication;
import com.commodity.yzrsc.R;
import com.commodity.yzrsc.model.ShareEntity;
import com.commodity.yzrsc.ui.adapter.ShareImageAdapter;
import com.commodity.yzrsc.utils.SharetUtil;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;
import java.util.List;



public class ShareImageDialog extends Dialog implements AdapterView.OnItemClickListener {
    private ShareEntity data;
    private Context context;
    private OnClickCancelListener clickCancelListener;
    private ShareIDialog shareIDialog;
    private Tencent mTencent;

    public ShareImageDialog(Context context, ShareEntity data) {
        super(context, R.style.CommonDialogStyle);
        this.context = context;
        this.data = data;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }
    private void init() {
        LayoutInflater inflater =LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.dialog_share, null);
        setContentView(view);

        Button cancle = (Button) view.findViewById(R.id.share_cancle);
        GridView gridview = (GridView) view.findViewById(R.id.share_gridview);
        List<Object> list=new ArrayList<>();
        list.add(data);
        list.add(data);
//        list.add(data);
//        list.add(data);
        ShareImageAdapter shareAdapter = new ShareImageAdapter(context, list, R.layout.item_grid_proview);
        gridview.setAdapter(shareAdapter);
        gridview.setOnItemClickListener(this);
        cancle.setOnClickListener(new clickListener());

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (MainApplication.SCREEN_W * 1); // 高度设置为屏幕的0.6
        lp.height=(int)(MainApplication.SCREEN_H*0.3);
        lp.x=0;
        lp.y=(int)(MainApplication.SCREEN_H*0.5);
        dialogWindow.setAttributes(lp);
        setCanceledOnTouchOutside(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UMImage umImage = new UMImage(context, data.imageUrl);
        if(shareIDialog==null){
            shareIDialog = new ShareIDialog(context);
        }
        shareIDialog.show();
        shareIDialog.setImage(data.imageUrl);
        shareIDialog.setNumber("共"+data.images.size()+"张");
        switch (position) {
            case 0:
                if(mTencent==null){
                    mTencent = Tencent.createInstance("tencent100424468", MainApplication.getContext());
                }

//                SharetUtil.shareMoreImage(data.images, context);
                shareIDialog.setQQContent();
                shareIDialog.qqClick(new View.OnClickListener() {//好友
                    @Override
                    public void onClick(View v) {
                        Bundle params = new Bundle();
                        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, data.imageUrl);
                        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "易州人商城");
                        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
                        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.
                                SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
                        mTencent.shareToQQ((Activity) context, params, new BaseUiListener());

                    }
                });
                shareIDialog.weixinClick(new View.OnClickListener() {//空间
                    @Override
                    public void onClick(View v) {
                        final Bundle params = new Bundle();
                        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
                        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "标题");//必填
                        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, data.desc);//选填
                        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, data.imageUrl);//必填
                        ArrayList<String> strings = new ArrayList<>();
                        strings.addAll(data.images);
                        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, strings);
                        mTencent.shareToQzone((Activity)context, params, new BaseUiListener());
                    }
                });
                break;
            case 1:
//                SharetUtil.share((Activity) context, SHARE_MEDIA.WEIXIN, data.desc, umImage, data.shareURL);
                shareIDialog.setWeixinContent();
                shareIDialog.qqClick(new View.OnClickListener() {//好友
                    @Override
                    public void onClick(View v) {

                    }
                });
                shareIDialog.weixinClick(new View.OnClickListener() {//朋友圈
                    @Override
                    public void onClick(View v) {
                        SharetUtil.shareMoreImage(data.images,context,data.desc);
                    }
                });
                break;
            case 2:
                SharetUtil.shareUrl((Activity) context, SHARE_MEDIA.WEIXIN_CIRCLE, data.shareURL, data.desc, umImage);
                break;
            case 3:
                SharetUtil.share((Activity) context, SHARE_MEDIA.QQ, "", umImage, data.shareURL);
                break;
            case 4:
                SharetUtil.share((Activity) context, SHARE_MEDIA.QZONE, "", umImage, data.shareURL);
                break;
        }

        dismiss();
    }

    public interface OnClickCancelListener {
        public void clickCance();
    }
    public void setClickCancelListener(OnClickCancelListener clickCancelListener) {
        this.clickCancelListener = clickCancelListener;
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.share_cancle:
                    dismiss();
                    if (clickCancelListener!=null){
                        clickCancelListener.clickCance();
                    }
                    break;
            }
        }
    };

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {

        }

        @Override
        public void onError(UiError e) {

        }
        @Override
        public void onCancel() {

        }
    }
}