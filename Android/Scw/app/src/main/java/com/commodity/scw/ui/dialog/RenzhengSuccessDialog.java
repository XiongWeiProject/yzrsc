package com.commodity.scw.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.commodity.scw.MainApplication;
import com.commodity.yzrsc.R;

/**
 * Created by 328789 on 2017/5/9.
 */

public class RenzhengSuccessDialog extends Dialog {
    private Context context;
    private View.OnClickListener listener;

    public RenzhengSuccessDialog(Context context) {
        super(context, R.style.CommonDialogStyle);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_renzheng, null);
        Button confirm = (Button) inflate.findViewById(R.id.ren_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onClick(v);
                }
            }
        });
        setContentView(inflate);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (MainApplication.SCREEN_W * 0.75); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
    }
    public void setOnclickListener(View.OnClickListener listener){
        this.listener=listener;
    }
}
