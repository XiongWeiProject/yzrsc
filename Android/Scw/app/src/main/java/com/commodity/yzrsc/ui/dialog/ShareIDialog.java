package com.commodity.yzrsc.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commodity.yzrsc.MainApplication;
import com.commodity.yzrsc.R;
import com.commodity.yzrsc.manager.ImageLoaderManager;



public class ShareIDialog extends Dialog {
    private Context context;
    private TextView number_share;
    private ImageView image_share;
    private View.OnClickListener weixinListener;
    private View.OnClickListener qqListener;
    private TextView content_weixin;
    private ImageView image_weixin;
    private TextView frind_s;
    private TextView frind_content;
    private ImageView frind_I;

    public ShareIDialog(Context context) {
        super(context, R.style.CommonDialogStyle);
        this.context = context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }
    private void init() {
        LayoutInflater inflater =LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.dialog_share_image, null);
        setContentView(view);
        TextView cancle = (TextView) view.findViewById(R.id.cancle_button);
        frind_content = (TextView) view.findViewById(R.id.frind_content);
        content_weixin = (TextView) view.findViewById(R.id.content_weixin);
        number_share = (TextView) view.findViewById(R.id.number_share);
        image_share = (ImageView) view.findViewById(R.id.image_share);
        image_weixin = (ImageView) view.findViewById(R.id.image_weixin);
        frind_I = (ImageView) view.findViewById(R.id.frind_I);
        LinearLayout shar_qq = (LinearLayout) view.findViewById(R.id.shar_qq);
        LinearLayout shar_weixin = (LinearLayout) view.findViewById(R.id.shar_weixin);

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        shar_qq.setOnClickListener(new clickListener());
        shar_weixin.setOnClickListener(new clickListener());

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (MainApplication.SCREEN_W * 0.75); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
    }



    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.shar_qq:
                    dismiss();
                    if (qqListener!=null){
                        qqListener.onClick(v);
                    }
                    break;
                case R.id.shar_weixin:
                    dismiss();
                    if (weixinListener!=null){
                        weixinListener.onClick(v);

                    }
                    break;
            }
        }
    };
    public void weixinClick(View.OnClickListener weixinListener){
        this.weixinListener=weixinListener;
    }
    public void qqClick(View.OnClickListener qqListener){
        this.qqListener=qqListener;
    }
    public void setNumber(String n){
        number_share.setText(n);
    }
    public void setImage(String url){
//        ImageLoaderManager.getInstance().displayImage(url,image_share);
        ImageLoaderManager.getInstance().displayImage(url, image_share,
                R.drawable.icons_clock);
    }
    public void setWeixinContent(){
        content_weixin.setText("分享到微信空间");
        image_weixin.setImageResource(R.drawable.umeng_socialize_wxcircle);
        frind_content.setText("分享给好友");
        frind_I.setImageResource(R.drawable.umeng_socialize_wechat);
    }
    public void setQQContent(){
        content_weixin.setText("分享到QQ空间");
        image_weixin.setImageResource(R.drawable.u_space);
        frind_content.setText("分享给好友");
        frind_I.setImageResource(R.drawable.u_qq);
    }

}