package com.commodity.scw.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.commodity.scw.MainApplication;
import com.commodity.scw.R;
import com.commodity.scw.model.MarketGoods;
import com.commodity.scw.utils.CustomToast;
import com.commodity.scw.utils.KeyBoardUtils;

import static com.commodity.scw.R.id.tv_text1;

/**
 * Created by liyushen on 2017/5/8 20:22
 * 转售弹出框
 */
public class ResellGoodsDialog extends Dialog {
    private Context context;
    private OnClickCancelListener clickCancelListener;
    private OnClickSubmitListener clickSubmitListener;

    private Button btn_submit;
    private Button btn_cancle;
    private EditText et_price;
    private TextView tv_info;
    private EditText et_desc;

    private MarketGoods curMarketGoods;
    public ResellGoodsDialog(Context context) {
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
        View view =inflater.inflate(R.layout.view_dialog_resellgood, null);
        setContentView(view);
        et_price= (EditText) view.findViewById(R.id.et_price);
        tv_info= (TextView) view.findViewById(R.id.tv_info);
        et_desc= (EditText) view.findViewById(R.id.et_desc);
        btn_submit= (Button) view.findViewById(R.id.btn_submit);
        btn_cancle= (Button) view.findViewById(R.id.btn_cancle);
        btn_submit.setOnClickListener(new clickListener());
        btn_cancle.setOnClickListener(new clickListener());
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (MainApplication.SCREEN_W * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);

        et_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (curMarketGoods!=null){
                    if (et_price.getText().toString().isEmpty()){
                        String dd=String.format("建议零售价：¥%s\n预计利润：%s - ¥%s=%s",curMarketGoods.getSuggestedPrice(),"未知",curMarketGoods.getPrice(),"差值");
                        tv_info.setText(dd);
                    }else {
                        double chajia= Double.parseDouble(et_price.getText().toString())-Double.parseDouble(curMarketGoods.getPrice());
                        String dd=String.format("建议零售价：¥%s\n预计利润：%s- ¥%s=%s",curMarketGoods.getSuggestedPrice(),"¥"+et_price.getText().toString(),curMarketGoods.getPrice(),String.format("%.2f", chajia));
                        tv_info.setText(dd);
                    }

                }
            }
        });
    }
    public interface OnClickCancelListener {
        public void clickCance();
    }
    public void setClickCancelListener(OnClickCancelListener clickCancelListener) {
        this.clickCancelListener = clickCancelListener;
    }

    public interface OnClickSubmitListener {
        public void clickSubmit(String goodsSaleId,String description,String resellPrice);
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
                        KeyBoardUtils.hindKeyBord(et_price);
                        if (et_price.getText().toString().isEmpty()){
                            CustomToast.showToast("价格不能为空");
                            return;
                        }
                        if (Double.parseDouble(et_price.getText().toString())==0.0){
                            CustomToast.showToast("价格不能为0");
                            return;
                        }

                        if (et_desc.getText().toString().isEmpty()){
                            CustomToast.showToast("描述不能为空");
                            return;
                        }
                        if (et_desc.getText().toString().length()<8){
                            CustomToast.showToast("描述不能少于8个字");
                            return;
                        }
                        if (curMarketGoods!=null){
                            clickSubmitListener.clickSubmit(curMarketGoods.getId(),et_desc.getText().toString(),et_price.getText().toString());
                        }

                    }
                    dismiss();
                    break;
                case R.id.btn_cancle:
                    KeyBoardUtils.hindKeyBord(et_price);
                    dismiss();
                    if (clickCancelListener!=null){
                        clickCancelListener.clickCance();
                    }
                    break;
            }
        }
    }

    public void setDataView(MarketGoods marketGoods){
        curMarketGoods=marketGoods;
        if (isShowing()){
            et_price.setText("");
            if (et_price.getText().toString().isEmpty()){
                String dd=String.format("建议零售价：¥%s\n预计利润：%s - ¥%s=%s",curMarketGoods.getSuggestedPrice(),"未知",curMarketGoods.getPrice(),"差值");
                tv_info.setText(dd);
            }else {
                double chajia= Double.parseDouble(et_price.getText().toString())-Double.parseDouble(curMarketGoods.getPrice());
                String dd=String.format("建议零售价：¥%s\n预计利润：%s- ¥%s=%s",curMarketGoods.getSuggestedPrice(),"¥"+et_price.getText().toString(),curMarketGoods.getPrice(),chajia);
                tv_info.setText(dd);
            }
            et_desc.setText(curMarketGoods.getDescribe());
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
