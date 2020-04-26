package com.commodity.scw.ui.activity.commodity;

import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.commodity.scw.R;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.manager.ConfigManager;
import com.commodity.scw.model.AdressBean;
import com.commodity.scw.model.AdressDetail;
import com.commodity.scw.ui.BaseActivity;
import com.commodity.scw.ui.widget.citypickerview.widget.CityPicker;
import com.commodity.scw.utils.KeyBoardUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by liyushen on 2017/3/30 21:46
 * 添加收货地址
 */
public class AddUserAdressActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.tv_rightbtn)
    TextView tv_rightbtn;
    @Bind(R.id.et_shouhuoren)
    EditText et_shouhuoren;
    @Bind(R.id.et_lianxifangshi)
    EditText et_lianxifangshi;
    @Bind(R.id.tv_shengshiqu)
    TextView tv_shengshiqu;
    @Bind(R.id.et_xiangxidizhi)
    EditText et_xiangxidizhi;

    private CityPicker cityPicker;
    private AdressBean[] adressBeens;
    private AdressDetail adressDetail;

    @Override
    protected int getContentView() {
        return R.layout.activity_add_user_adress;
    }

    @Override
    protected void initView() {
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("AdressDetail")) {
            adressDetail = (AdressDetail) getIntent().getExtras().getSerializable("AdressDetail");
        }
        if (adressDetail != null) {
            tv_title.setText("编辑收货地址");
            et_shouhuoren.setText(adressDetail.getReceiver());
            et_lianxifangshi.setText(adressDetail.getMobile());
            if (adressBeens == null) {
                adressBeens = new AdressBean[3];
                AdressBean adressBean1 = new AdressBean();
                adressBean1.setId(adressDetail.getProvinceId());
                adressBean1.setName(adressDetail.getProvince());
                adressBeens[0] = adressBean1;
                AdressBean adressBean2 = new AdressBean();
                adressBean2.setId(adressDetail.getCityId());
                adressBean2.setName(adressDetail.getCity());
                adressBeens[1] = adressBean2;
                AdressBean adressBean3 = new AdressBean();
                adressBean3.setId(adressDetail.getDistrictId());
                adressBean3.setName(adressDetail.getDistrict());
                adressBeens[2] = adressBean3;
            }
            tv_shengshiqu.setText(adressBeens[0].getName() + "/" + adressBeens[1].getName() + "/" + adressBeens[2].getName());
            et_xiangxidizhi.setText(adressDetail.getAddressDetail());

        }
        customLoadding.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initCitypicker();
            }
        },1000);

    }

    @Override
    protected void initListeners() {
        tv_shengshiqu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyBoardUtils.hindKeyBord(et_lianxifangshi);
                if (cityPicker==null){
                    initCitypicker();
                }
                cityPicker.show();
                if (adressBeens != null) {
                    setDealutCityPickerData(adressBeens[0].getName(), adressBeens[1].getName(), adressBeens[2].getName());
                }
            }
        });
        tv_rightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_shouhuoren.getText().toString().isEmpty()) {
                    tip("收货人不能为空!");
                    return;
                }
                if (et_lianxifangshi.getText().toString().isEmpty()) {
                    tip("联系方式不能为空!");
                    return;
                }
                if (tv_shengshiqu.getText().toString().isEmpty()) {
                    tip("省市区不能为空!");
                    return;
                }
                if (et_xiangxidizhi.getText().toString().isEmpty()) {
                    tip("详细地址不能为空!");
                    return;
                }
                sendRequest(1, "");
            }
        });
    }

    private void initCitypicker(){
        cityPicker = new CityPicker.Builder(AddUserAdressActivity.this).textSize(18)
                .titleTextColor("#000000")
                .backgroundPop(0x44000000)
                .province("32")
                .city("3201")
                .district("320104")
                .textColor(Color.parseColor("#333333"))
                .provinceCyclic(true)//是否循环
                .cityCyclic(true)
                .districtCyclic(true)
                .visibleItemsCount(7)
                .itemPadding(10)
                .build();
        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(AdressBean... citySelected) {
                adressBeens = citySelected;
                tv_shengshiqu.setText(citySelected[0].getName() + "/" + citySelected[1].getName() + "/" + citySelected[2].getName());
            }

            @Override
            public void onCancel() {
                // Toast.makeText(AddUserAdressActivity.this, "已取消", Toast.LENGTH_LONG).show();
            }
        });
        customLoadding.dismiss();
    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        customLoadding.show();
        if (tag == 1) {//添加或编辑用户收货地址(Id=0则为添加)
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("receiver", et_shouhuoren.getText().toString());
            parmMap.put("mobile", et_lianxifangshi.getText().toString());
            parmMap.put("addressDetail", et_xiangxidizhi.getText().toString());
            parmMap.put("province", adressBeens[0].getId());
            parmMap.put("city", adressBeens[1].getId());
            parmMap.put("district", adressBeens[2].getId());
            parmMap.put("member", ConfigManager.instance().getUser().getId());
            if (adressDetail != null) {
                parmMap.put("id", adressDetail.getId());
            } else {
                parmMap.put("id", "0");
            }
            HttpManager httpManager = new HttpManager(tag, HttpMothed.POST,
                    IRequestConst.RequestMethod.AddAddressInfo, parmMap, this);
            httpManager.request();
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        if (tag == 1) {
            JSONObject resultJson = (JSONObject) resultInfo.getResponse();
            if (resultJson != null && resultJson.optBoolean("success")) {
                if (adressDetail != null) {
                    tip("修改地址成功");
                } else {
                    tip("添加地址成功");
                }
                setResult(RESULT_OK);
                finish();
            } else {
                if (resultJson != null && !resultJson.optBoolean("success")) {
                    tip(resultJson.optString("msg"));
                }
            }
        }
    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
    }

    //设置默认显示
    private void setDealutCityPickerData(String str1, String str2, String str3) {
        cityPicker.setmProvincesCurrentItem(str1);
        cityPicker.setmCitisCurrentItem(str2);
        cityPicker.setmDistrictCurrentItem(str3);
    }
}
