package com.commodity.scw.ui.activity.personalcenter.money;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.scw.http.BaseHttpManager;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.model.MoneyDetailEntity;
import com.commodity.scw.model.mine.TixianjiluDataEntity;
import com.commodity.scw.model.mine.TixianjiluEntity;
import com.commodity.scw.ui.BaseActivity;
import com.commodity.scw.ui.adapter.MoneyDetailAdapter;
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
import cn.qqtheme.framework.picker.DatePicker;
import cn.yohoutils.StringUtil;

/**
 * 钱包明细
 * Created by yangxuqiang on 2017/3/29.
 */

public class MoneyDetailActivity extends BaseActivity {
    @Bind(R.id.head_back)
    LinearLayout back;
    @Bind(R.id.head_text_right)
    TextView right;
    @Bind(R.id.head_title)
    TextView title;
    @Bind(R.id.moneydetail_listview)
    XListView listView;
    @Bind(R.id.backage_detial)
    LinearLayout backage_detial;
    private List<TixianjiluDataEntity> data=new ArrayList<>();
    Calendar calendar = Calendar.getInstance();
    private int pageIndex=1;
    private MoneyDetailAdapter moneyDetailAdapter;

    private TextView popup_year;
    private TextView popup_moomth;
    private TextView popup_date;

    private String maxAmount=String.valueOf(Integer.MAX_VALUE);
    private String beginDate="2017-1-1";
    private boolean isLoading;
    private TixianjiluAdapter tixianjiluAdapter;
    private String minAmount="0";
    @Override
    protected int getContentView() {

        return R.layout.activity_moneydetail;
    }

    @Override
    protected void initView() {
        title.setText("钱包明细");
        right.setText("日期");
        moneyDetailAdapter = new MoneyDetailAdapter(mContext, data, R.layout.item_moneydetail);
        listView.setAdapter(moneyDetailAdapter);
        sendRequest(0,"");
    }

    @Override
    protected void initListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(false);
        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {

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
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });
    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        customLoadding.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("sortOrder","1");
        map.put("pageSize","20");
        map.put("pageIndex",String.valueOf(pageIndex));
        map.put("maxAmount",String.valueOf(Integer.MAX_VALUE));
        map.put("minAmount","0");
//        map.put("beginDate",calendar.get(Calendar.YEAR)+"-"+(calendar.get(calendar.MONTH)+1)+"-"+calendar.get(calendar.DATE));
        map.put("beginDate","2017-1-1");
        new HttpManager(tag, HttpMothed.GET, IRequestConst.RequestMethod.GetTransactionLog,map,this).request();
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        JSONObject response = (JSONObject) resultInfo.getResponse();
        if(response.optBoolean("success")){
            listView.stopLoadMore();
            if(0==response.optJSONObject("pageInfo").optInt("totalCount")){
                //无数据
                backage_detial.setVisibility(View.VISIBLE);
                listView.setVisibility(View.INVISIBLE);
            }else {
                backage_detial.setVisibility(View.INVISIBLE);
                if (0 == response.optJSONObject("pageInfo").optInt("count")) {
                    listView.setPullLoadEnable(false);
                } else {
                    TixianjiluEntity tixianjiluEntity = GsonUtils.jsonToObject(response.toString(), TixianjiluEntity.class);
                    int position = data.size();
                    data.addAll(tixianjiluEntity.getData());
                    moneyDetailAdapter.notifyDataSetChanged();
                    listView.setSelection(position);
                    pageIndex++;
//                    listView.setPullLoadEnable(true);
                    if (response.optJSONObject("pageInfo").optInt("totalPage") == response.optJSONObject("pageInfo").optInt("index")) {
                        listView.setPullLoadEnable(false);
                    }
                    listView.stopLoadMore();
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

        popupWindow.showAsDropDown(back);
//        bg.setVisibility(View.VISIBLE);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

//                bg.setVisibility(View.GONE);
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
                HashMap<String, String> map = new HashMap<>();
                map.put("sortOrder","1");
                map.put("pageSize","20");
                map.put("pageIndex",String.valueOf(pageIndex));
                map.put("maxAmount",maxAmount);
                map.put("minAmount",minAmount);
//        map.put("beginDate",calendar.get(Calendar.YEAR)+"-"+(calendar.get(calendar.MONTH)+1)+"-"+calendar.get(calendar.DATE));
                map.put("beginDate",beginDate);
                new HttpManager(0, HttpMothed.GET, IRequestConst.RequestMethod.GetTransactionLog, map, new BaseHttpManager.IRequestListener() {
                    @Override
                    public void onPreExecute(int Tag) {

                    }

                    @Override
                    public void onSuccess(int Tag, ServiceInfo result) {
                        JSONObject response = (JSONObject) result.getResponse();
                        if(response.optBoolean("success")){
                            listView.stopLoadMore();
                            if(0==response.optJSONObject("pageInfo").optInt("totalCount")){
                                //无数据
                                backage_detial.setVisibility(View.VISIBLE);
                                listView.setVisibility(View.INVISIBLE);
                            }else {
                                backage_detial.setVisibility(View.INVISIBLE);
                                if (0 == response.optJSONObject("pageInfo").optInt("count")) {
                                    listView.setPullLoadEnable(false);
                                } else {
                                    TixianjiluEntity tixianjiluEntity = GsonUtils.jsonToObject(response.toString(), TixianjiluEntity.class);
                                    int position = data.size();
                                    data.addAll(tixianjiluEntity.getData());
                                    moneyDetailAdapter.notifyDataSetChanged();
                                    listView.setSelection(position);
                                    pageIndex++;
//                    listView.setPullLoadEnable(true);
                                    if (response.optJSONObject("pageInfo").optInt("totalPage") == response.optJSONObject("pageInfo").optInt("index")) {
                                        listView.setPullLoadEnable(false);
                                    }
                                    listView.stopLoadMore();
                                }
                            }

                        }else {
                            tip(response.optString("msg"));
                        }
                    }

                    @Override
                    public void onError(int Tag, String code, String msg) {

                    }

                    @Override
                    public void OnTimeOut(int Tag, boolean isShowTip) {

                    }

                    @Override
                    public void OnNetError(int Tag, boolean isShowTip) {

                    }
                }).request();

                popupWindow.dismiss();
            }
        });
        popup_year.setOnClickListener(clickListener);
        popup_moomth.setOnClickListener(clickListener);
        popup_date.setOnClickListener(clickListener);

    }

    private View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Calendar calendar = Calendar.getInstance();
            final DatePicker picker = new DatePicker(MoneyDetailActivity.this);
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
}
