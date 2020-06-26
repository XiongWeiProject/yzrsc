package com.commodity.yzrsc.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.commodity.yzrsc.MainApplication;
import com.commodity.yzrsc.R;
import com.commodity.yzrsc.model.ShareEntity;
import com.commodity.yzrsc.ui.adapter.ShareAdapter;
import com.commodity.yzrsc.utils.SharetUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;
import java.util.List;


public class ShareDialog<T> extends Dialog implements AdapterView.OnItemClickListener {
    private ShareEntity data;
    private Context context;
    private OnClickCancelListener clickCancelListener;

    public ShareDialog(Context context, ShareEntity data) {
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
//        list.add(data);
//        list.add(data);
//        list.add(data);
//        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        list.add(data);
        ShareAdapter shareAdapter = new ShareAdapter(context, list, R.layout.item_grid_proview);
        gridview.setAdapter(shareAdapter);
        gridview.setOnItemClickListener(this);
        cancle.setOnClickListener(new clickListener());

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (MainApplication.SCREEN_W * 1); // 高度设置为屏幕的0.6
        lp.height=(int)(MainApplication.SCREEN_H*0.5);
        lp.x=0;
        lp.y=(int)(MainApplication.SCREEN_H*0.5);
        dialogWindow.setAttributes(lp);
        setCanceledOnTouchOutside(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UMImage umImage = new UMImage(context, data.imageUrl);
        switch (position) {
//            case 0:
//                SharetUtil.shareMoreImage(data.images,context,data.desc);
//                break;
            case 0:
                SharetUtil.shareUrl((Activity)context, SHARE_MEDIA.WEIXIN,data.shareURL,data.desc,umImage);
                break;
            case 1:
                SharetUtil.shareUrl((Activity)context,SHARE_MEDIA.WEIXIN_CIRCLE,data.shareURL,data.desc,umImage);
                break;
            case 2:
                SharetUtil.shareUrl((Activity)context,SHARE_MEDIA.QQ,data.shareURL,data.desc,umImage);
                break;
            case 3:
                ClipboardManager clipboardManager = (ClipboardManager) MainApplication.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipurl = ClipData.newPlainText("url", data.shareURL);
                clipboardManager.setPrimaryClip(clipurl);

                Toast.makeText((Activity) context, "复制链接成功", Toast.LENGTH_LONG).show();
//                SharetUtil.shareUrl((Activity)context,SHARE_MEDIA.QZONE,data.shareURL,data.desc,umImage);
                break;
//            case 4:
//                SharetUtil.shareUrl((Activity)context,SHARE_MEDIA.SINA,data.shareURL,data.desc,umImage);
//                break;
//            case 5:
//                SharetUtil.saveImages(context,data.images);
//                break;
//            case 6:
//
//                break;
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
}