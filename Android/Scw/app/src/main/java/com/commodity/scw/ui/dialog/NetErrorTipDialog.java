package com.commodity.scw.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.commodity.scw.MainApplication;
import com.commodity.yzrsc.R;

/**
 * Created by liyushen on 2017/8/12 22:32
 * 网络异常提示dialog
 */
public class NetErrorTipDialog extends Dialog {
    private Context context;
    private OnClickSubmitListener clickSubmitListener;

    private TextView tv_dialog_msg;
    private Button btn_submit;
    public NetErrorTipDialog(Context context) {
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
        View view =inflater.inflate(R.layout.view_dialog_neterror, null);
        setContentView(view);
        tv_dialog_msg= (TextView) view.findViewById(R.id.tv_dialog_msg);
        btn_submit= (Button) view.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new clickListener());
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (MainApplication.SCREEN_W * 0.75); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
    }
    public interface OnClickCancelListener {
        public void clickCance();
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
            }
        }
    };

    public void setContext(String context) {
        tv_dialog_msg.setText(context);
    }
}