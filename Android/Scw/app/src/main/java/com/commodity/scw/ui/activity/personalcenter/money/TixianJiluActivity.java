package com.commodity.scw.ui.activity.personalcenter.money;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.commodity.scw.R;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.model.mine.TixianjiluDataEntity;
import com.commodity.scw.model.mine.TixianjiluEntity;
import com.commodity.scw.ui.BaseActivity;
import com.commodity.scw.ui.adapter.TixianjiluAdapter;
import com.commodity.scw.ui.widget.xlistView.PLA_AdapterView;
import com.commodity.scw.ui.widget.xlistView.XListView;
import com.commodity.scw.utils.GsonUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;
import cn.yohoutils.StringUtil;

/**
 * 提现记录
 * Created by yangxuqiang on 2017/3/29.
 */

public class TixianJiluActivity extends BaseActivity {
    @Bind(R.id.tixianjilu_listview)
    XListView listView;
    @Bind(R.id.tixian_popup_drap)
    LinearLayout tixian_popup_drap;
    @Bind(R.id.backage_tixian)
    LinearLayout backage_tixian;
    @Bind(R.id.bg)
    View bg;

    private List<TixianjiluDataEntity> data = new ArrayList<>();
    private TextView popup_year;
    private TextView popup_moomth;
    private TextView popup_date;
    private int pageIndex=1;
    private String maxAmount=String.valueOf(Integer.MAX_VALUE);
    Calendar calendar = Calendar.getInstance();
//    private String beginDate=calendar.get(Calendar.YEAR)+"-"+(calendar.get(calendar.MONTH)+1)+"-"+calendar.get(calendar.DATE);
    private String beginDate="2017-1-1";
    private boolean isLoading;
    private TixianjiluAdapter tixianjiluAdapter;
    private String minAmount="0";


    @Override
    protected int getContentView() {
        return R.layout.activity_tixianjilu;
    }

    @Override
    protected void initView() {
        sendRequest(0,"");
        tixianjiluAdapter = new TixianjiluAdapter(mContext, data, R.layout.item_tixianjilu);
        listView.setAdapter(tixianjiluAdapter);
    }

    @Override
    protected void initListeners() {
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(false);
        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                data.clear();
                maxAmount="0";
                minAmount=maxAmount;
                beginDate="2017-1-1";
                pageIndex=1;
                sendRequest(0,"");

            }

            @Override
            public void onLoadMore() {
                sendRequest(0,"");
            }
        });
        listView.setOnItemClickListener(new PLA_AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(PLA_AdapterView<?> parent, View view, int position, long id) {
                TixianjiluDataEntity tixianjiluDataEntity = data.get(position-1);
                Bundle bundle = new Bundle();
                bundle.putSerializable("entity",tixianjiluDataEntity);
                jumpActivity(TixianDetaillActivity.class,bundle);
            }
        });
    }

    @OnClick({R.id.tixianjilu_back, R.id.tixianjilu_calecder})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tixianjilu_back://返回
                finish();
                break;
            case R.id.tixianjilu_calecder://时间
                showPopup();
                break;
        }
    }

    private void showPopup() {

        final PopupWindow popupWindow = new PopupWindow(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        View inflate = View.inflate(mContext, R.layout.popup_select, null);
        Button popup_confirm = (Button) inflate.findViewById(R.id.popup_confirm);
        final EditText popup_max = (EditText) inflate.findViewById(R.id.popup_max);
        final EditText popup_min = (EditText) inflate.findViewById(R.id.popup_min);
        popup_year = (TextView) inflate.findViewById(R.id.popup_year);
        popup_moomth = (TextView) inflate.findViewById(R.id.popup_moomth);
        popup_date = (TextView) inflate.findViewById(R.id.popup_date);

        popup_year.setText(calendar.get(Calendar.YEAR)+"年");
        popup_moomth.setText((calendar.get(calendar.MONTH)+1)+"月");
        popup_date.setText(calendar.get(calendar.DATE)+"日");

        popupWindow.setContentView(inflate);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));

        popupWindow.showAsDropDown(tixian_popup_drap);
        bg.setVisibility(View.VISIBLE);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                bg.setVisibility(View.GONE);
            }
        });

        popup_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String max = popup_max.getText().toString().trim();
                String min = popup_min.getText().toString().trim();
                if(StringUtil.isEmpty(max)||StringUtil.isEmpty(min)){
                    tip("请输入金额");
                    return;
                }else if(max.startsWith(".")||min.startsWith(".")){
                    tip("请输入正确的金额");
                    return;
                }
                maxAmount=max;
                minAmount=min;
                beginDate=popup_year.getText().toString().replace("年","")+"-"+popup_moomth.getText().toString().replace("月","")+"-"+popup_date.getText().toString().replace("日","");
                pageIndex=1;
                data.clear();
                sendRequest(0,"");
                popupWindow.dismiss();
            }
        });
        popup_year.setOnClickListener(clickListener);
        popup_moomth.setOnClickListener(clickListener);
        popup_date.setOnClickListener(clickListener);

//        Calendar instance = Calendar.getInstance();
//        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
//
//            }
//        },instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DAY_OF_MONTH));
//        datePickerDialog.show();
    }
    private View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Calendar calendar = Calendar.getInstance();
            final DatePicker picker = new DatePicker(TixianJiluActivity.this);
            picker.setTopPadding(2);
            picker.setRangeStart(1900, 8, 29);
            picker.setRangeEnd(2100, 1, 11);
            picker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DATE));
            picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                @Override
                public void onDatePicked(String year, String month, String day) {
                    popup_year.setText(year+"年");
                    popup_moomth.setText(month+"月");
                    popup_date.setText(day+"日");
                }
            });
            picker.setOnWheelListener(new DatePicker.OnWheelListener() {
                @Override
                public void onYearWheeled(int index, String year) {
                    picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
                }

                @Override
                public void onMonthWheeled(int index, String month) {
                    picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
                }

                @Override
                public void onDayWheeled(int index, String day) {
                    picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
                }
            });
            picker.show();
        }
    };

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        if(tag==0){
            HashMap<String, String> map = new HashMap<>();
            map.put("sortOrder","1");
            map.put("pageSize","20");
            map.put("pageIndex",String.valueOf(pageIndex));
            map.put("maxAmount",maxAmount);
            map.put("minAmount",minAmount);
            map.put("beginDate",beginDate);
            new HttpManager(tag, HttpMothed.GET, IRequestConst.RequestMethod.GetWithdrawLog,map,this).request();
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        JSONObject response = (JSONObject) resultInfo.getResponse();
        if(response.optBoolean("success")){
            if(tag==0){

                JSONObject pageInfo = response.optJSONObject("pageInfo");
                if(0==pageInfo.optInt("totalCount")){
                    listView.setVisibility(View.INVISIBLE);
                    backage_tixian.setVisibility(View.VISIBLE);
                }else {
                    if(0==pageInfo.optInt("count")){
                        listView.setPullLoadEnable(false);
                    }else {
                        listView.setVisibility(View.VISIBLE);
                        backage_tixian.setVisibility(View.GONE);
                        TixianjiluEntity tixianjiluEntity = GsonUtils.jsonToObject(response.toString(), TixianjiluEntity.class);

                        int position = 0;
                        if(tixianjiluEntity.getData().size()>0){
                            position=data.size();
                        }else {
                            position=data.size()-1;
                        }

//                        if(!isLoading||position==-1){
//                            data.clear();
//                            position=0;
//                        }
//                        isLoading=false;
                        if(pageInfo.optInt("totalPage")==pageInfo.optInt("index")){
                            listView.setPullLoadEnable(false);
                        }else {
                            pageIndex++;
                        }
                        data.addAll(tixianjiluEntity.getData());
                        tixianjiluAdapter.notifyDataSetChanged();
//                        listView.setSelection(position);
//                        listView.setPullLoadEnable(true);
                        listView.stopLoadMore();
                        listView.stopRefresh();
                    }

                }

            }
        }else {
            tip(response.optString("msg"));
        }

    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
        tip(msg);
    }
}
