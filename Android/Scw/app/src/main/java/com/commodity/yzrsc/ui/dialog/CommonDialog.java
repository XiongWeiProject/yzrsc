package com.commodity.yzrsc.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.commodity.yzrsc.MainApplication;
import com.commodity.yzrsc.R;


/**
 * 作者：liyushen on 2016/4/26 09:42
 * 功能：仿iOS对话框单行
 */
public class CommonDialog extends Dialog {
    private Context context;
    private OnClickCancelListener clickCancelListener;
    private OnClickSubmitListener clickSubmitListener;

    private TextView tv_dialog_msg;
    private Button btn_submit;
    private Button btn_cancle;
    public CommonDialog(Context context) {
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
        View view =inflater.inflate(R.layout.view_dialog_common, null);
        setContentView(view);
        tv_dialog_msg= (TextView) view.findViewById(R.id.tv_dialog_msg);
        btn_submit= (Button) view.findViewById(R.id.btn_submit);
        btn_cancle= (Button) view.findViewById(R.id.btn_cancle);
        btn_submit.setOnClickListener(new clickListener());
        btn_cancle.setOnClickListener(new clickListener());
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (MainApplication.SCREEN_W * 0.75); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
    }
    public interface OnClickCancelListener {
        public void clickCance();
    }
    public void setClickCancelListener(OnClickCancelListener clickCancelListener) {
        this.clickCancelListener = clickCancelListener;
    }

    public interface OnClickSubmitListener {
        public void clickSubmit();
    }
    public void setClickSubmitListener(OnClickSubmitListener clickSubmitListener) {
        this.clickSubmitListener = clickSubmitListener;
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_submit:
                    dismiss();
                    if (clickSubmitListener!=null){
                        clickSubmitListener.clickSubmit();
                    }
                    break;
                case R.id.btn_cancle:
                    dismiss();
                    if (clickCancelListener!=null){
                        clickCancelListener.clickCance();
                    }
                    break;
            }
        }
    };

    public void setContext(String context) {
        tv_dialog_msg.setText(context);
    }
}