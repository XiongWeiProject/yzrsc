package com.commodity.scw.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.commodity.scw.MainApplication;
import com.commodity.yzrsc.R;

/**
 * 发货
 * Created by yangxuqiang on 2017/4/9.
 */

public class SendGoodsDialog extends Dialog {
    private final Context context;
    private CommonDialog.OnClickCancelListener cancelListener;
    private CommonDialog.OnClickCancelListener submitListener;
    private EditText dialog_input_code;
    private TextView dialog_input_compin;
    private View.OnClickListener listener;

    public SendGoodsDialog(Context context) {
        super(context, R.style.CommonDialogStyle);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View inflate = View.inflate(context, R.layout.dialog_send_goods, null);
        dialog_input_code = (EditText) inflate.findViewById(R.id.dialog_input_code);
        dialog_input_compin = (TextView) inflate.findViewById(R.id.dialog_input_compin);
        Button dialog_cancle = (Button) inflate.findViewById(R.id.dialog_cancle);
        Button dialog_submit = (Button) inflate.findViewById(R.id.dialog_submit);
        dialog_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cancelListener!=null){
                    cancelListener.clickCance();
                }
            }
        });
        dialog_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(submitListener!=null){
                    submitListener.clickCance();
                }
            }
        });
        dialog_input_compin.setOnClickListener(new View.OnClickListener() {
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
    public void setCancleOnClickListener(CommonDialog.OnClickCancelListener listener){
        this.cancelListener=listener;
    }
    public void setSubmitListener(CommonDialog.OnClickCancelListener listener){
        this.submitListener=listener;
    }
    public String getInputCode(){
       return dialog_input_code.getText().toString();
    }
    public void setCText(String s){
        dialog_input_compin.setText(s);
    }
    public void setOnClick(View.OnClickListener listener){
        this.listener=listener;
    }
}
