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
import com.commodity.scw.R;
import com.commodity.scw.model.MarketGoods;

/**
 * Created by liyushen on 2017/6/4 17:15
 * 转售成功弹出diaolog
 */
public class ResellSuccessDialog extends Dialog {
    private Context context;
    private OnClickCancelListener clickCancelListener;
    private OnClickSubmitListener clickSubmitListener;

    private Button btn_submit;
    private Button btn_cancle;

    public ResellSuccessDialog(Context context) {
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
        View view =inflater.inflate(R.layout.view_dialog_resellsuccess, null);
        setContentView(view);
        btn_submit= (Button) view.findViewById(R.id.btn_submit);
        btn_cancle= (Button) view.findViewById(R.id.btn_cancle);
        btn_submit.setOnClickListener(new clickListener());
        btn_cancle.setOnClickListener(new clickListener());
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (MainApplication.SCREEN_W * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
    }
    public interface OnClickCancelListener {
        public void clickCance();
    }
    public void setClickCancelListener(OnClickCancelListener clickCancelListener) {
        this.clickCancelListener = clickCancelListener;
    }

    public interface OnClickSubmitListener {
        public void clickSubmit( );
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
                    if (clickSubmitListener!=null){
                        clickSubmitListener.clickSubmit();
                    }
                    dismiss();
                    break;
                case R.id.btn_cancle:
                    dismiss();
                    if (clickCancelListener!=null){
                        clickCancelListener.clickCance();
                    }
                    break;
            }
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}

