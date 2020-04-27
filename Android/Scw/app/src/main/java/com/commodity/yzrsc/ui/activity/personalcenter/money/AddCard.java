package com.commodity.yzrsc.ui.activity.personalcenter.money;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.utils.ScreenUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.widget.WheelView;
import cn.yohoutils.StringUtil;

/**
 * 添加银行卡
 * Created by yangxuqiang on 2017/4/3.
 */

public class AddCard extends BaseActivity {
    @Bind(R.id.head_title)
    TextView title;
    @Bind(R.id.addcard_button_add)
    LinearLayout addcard_button_add;
    @Bind(R.id.addcard_image)
    ImageView addcard_image;
    @Bind(R.id.addcard_edit_number)
    EditText addcard_edit_number;
    @Bind(R.id.addcard_name)
    EditText addcard_name;
    @Bind(R.id.addcard_text_yinhang)
    TextView addcard_text_yinhang;

    private String yinhang;
    private List<String> items=new ArrayList<>();
    private String[] banks= new String[]{
        "中信银行", "中国邮政储蓄银行", "交通银行", "光大银行", "兴业银行", "农业银行",
                "工商银行", "平安银行", "广发银行", "建设银行", "招商银行", "民生银行","浦发银行"
    };

    public static String viewFlag="add";//界面标识

    @Override
    protected int getContentView() {
        return R.layout.activity_addcard;
    }

    @Override
    protected void initView() {
        title.setText("绑定银行卡");

        items.add("中信银行");
        items.add("中国邮政储蓄银行");
        items.add("交通银行");
        items.add("广大银行");
//        items.add("中信银行");
//        items.add("中信银行");
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            if(extras.containsKey("name")){
                addcard_name.setText(extras.getString("name"));
            }
            if(extras.containsKey("cardNumber")){
                addcard_edit_number.setText(extras.getString("cardNumber"));
            }
            if(extras.containsKey("brank")){
                addcard_text_yinhang.setText(extras.getString("brank"));
            }
        }

    }

    @Override
    protected void initListeners() {

    }
    @OnClick({R.id.head_back,R.id.add_card_confirmation,R.id.addcard_button_add})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.head_back:
                finish();
                break;
            case R.id.add_card_confirmation://确认
                String name = addcard_name.getText().toString().trim();
                String number = addcard_edit_number.getText().toString().trim();
                String blank = addcard_text_yinhang.getText().toString().trim();
                if (StringUtil.isEmpty(name)){
                    tip("请输入姓名");
                }else if(StringUtil.isEmpty(number)){
                    tip("请输入卡号");
                }else if(StringUtil.isEmpty(blank)){
                    tip("请选择开户银行");
                }else if(!isNumber(name)){
                    tip("姓名不能含有数字");
                }else if(isCard(blank)){
                    tip("银行卡号必须为数字");
                }else  {

                    Bundle bundle = new Bundle();
                    bundle.putString("view",viewFlag);
                    bundle.putString("name",name);
                    bundle.putString("number",number);
                    bundle.putString("blank",blank);
                    jumpActivity(ConfirmTixianActivity.class,bundle);
                    finish();
                }
                break;
            case R.id.addcard_button_add://选择银行卡
                showSingle();
                break;
        }
    }

    private void showSingle() {
//        final BankDialog bankDialog = new BankDialog(mContext, items);
//        bankDialog.show();
//        bankDialog.setItemClick(new BankDialog.OnitemClickListener() {
//            @Override
//            public void itemClick(int position) {
//
//                yinhang=items.get(position);
//                addcard_text_yinhang.setText(yinhang);
//                bankDialog.dismiss();
//            }
//        });
        OptionPicker picker = new OptionPicker(this, banks);
        picker.setTitleTextSize(ScreenUtils.sp2px(mContext,14));
        picker.setSubmitTextColor(0xFFBA0008);
        picker.setCancelTextColor(0xFF111A3B);
        picker.setCancelTextSize(14);
        picker.setSubmitTextSize(14);
        picker.setTextColor(0xFF111A3B, 0xFF999999);
        WheelView.LineConfig config = new WheelView.LineConfig();
        config.setColor(0xFFF2F2F2);
        picker.setLineConfig(config);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int i, String s) {
                addcard_text_yinhang.setText(s);
            }
        });
        picker.show();
    }
    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        if(tag==0){

        }

    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        JSONObject response = (JSONObject) resultInfo.getResponse();
        if(response.optBoolean("success")){

        }else {
            tip(response.optString("msg"));
        }
    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
        tip(msg);
    }

    public boolean isNumber(String name){
        Pattern compile = Pattern.compile("[^0-9]{0,}");
        Matcher matcher = compile.matcher(name);
        return matcher.matches();
    }

    public boolean isCard(String name){
        Pattern compile = Pattern.compile("[0-9]{0,}");
        Matcher matcher = compile.matcher(name);
        return matcher.matches();
    }

}
