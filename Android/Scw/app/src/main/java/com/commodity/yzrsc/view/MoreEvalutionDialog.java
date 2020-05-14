package com.commodity.yzrsc.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.commodity.yzrsc.MainApplication;
import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.BaseHttpManager;
import com.commodity.yzrsc.http.HttpManageNew;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.manager.ConfigManager;
import com.commodity.yzrsc.model.CommodityBean;
import com.commodity.yzrsc.model.DynamicAllListModel;
import com.commodity.yzrsc.model.Evalution;
import com.commodity.yzrsc.model.TypeModel;
import com.commodity.yzrsc.ui.adapter.Evalution2Adapter;
import com.commodity.yzrsc.ui.adapter.EvalutionAdapter;
import com.commodity.yzrsc.utils.GsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoreEvalutionDialog extends Dialog {
    private Context context;
    private View.OnClickListener listener;
    String ids ;
    RecyclerView rcv_evalution;
    List<Evalution> evalutions = new ArrayList<>();
    public MoreEvalutionDialog(Context context , String id ) {
        super(context, R.style.CommonDialogStyle);
        this.context = context;
        ids = id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        View inflate = LayoutInflater.from(context).inflate(R.layout.layout_more_evalution, null);
        Button confirm = (Button) inflate.findViewById(R.id.btn_close);
        rcv_evalution = (RecyclerView) inflate.findViewById(R.id.rcv_more_evalution);
        favorGoods(ids);
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

    private void favorGoods(String goodsSaleId){
        Map<String, String> parmMap = new HashMap<String, String>();
        JSONArray array=new JSONArray();
        array.put(goodsSaleId);
        parmMap.put("id",goodsSaleId);
        parmMap.put("minId", 0+"");
        parmMap.put("pageSize", 20+"");
        HttpManager httpManager = new HttpManager(0, HttpMothed.GET,
                IRequestConst.RequestMethod.PostDynamEvalutionList, parmMap, new BaseHttpManager.IRequestListener() {
            @Override
            public void onPreExecute(int Tag) {

            }

            @Override
            public void onSuccess(int Tag, ServiceInfo result) {
                JSONObject resultJson = (JSONObject) result.getResponse();
                if (resultJson != null && resultJson.optBoolean("success")) {
                    if (resultJson.optJSONArray("data").toString()!=null){
                        try {
                            evalutions = JSON.parseArray(resultJson.getString("data"), Evalution.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        rcv_evalution.setLayoutManager(new LinearLayoutManager(context));
                        Evalution2Adapter zanAdapter = new Evalution2Adapter(context, evalutions, R.layout.item_evalution);
                        rcv_evalution.setAdapter(zanAdapter);
                    }

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
        });
        httpManager.request();
    }
}
