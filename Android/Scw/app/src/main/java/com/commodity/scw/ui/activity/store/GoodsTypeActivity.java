package com.commodity.scw.ui.activity.store;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.model.store.TypeChildrenEntity;
import com.commodity.scw.model.store.TypeContextEntity;
import com.commodity.scw.model.store.UploadTypeEntity;
import com.commodity.scw.ui.BaseActivity;
import com.commodity.scw.ui.adapter.ItemResultAdapter;
import com.commodity.scw.ui.adapter.TypeGuideAdapter;
import com.commodity.scw.utils.GsonUtils;
import com.ta.utdid2.android.utils.StringUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 商品分类
 * Created by 328789 on 2017/4/15.
 */

public class GoodsTypeActivity extends BaseActivity {
    @Bind(R.id.head_title)
    TextView title;
    @Bind(R.id.upload_result)
    ListView upload_result_view;
    @Bind(R.id.upload_type)
    ListView upload_type_view;
    private List<TypeContextEntity> typeData=new ArrayList<>();
    private ItemResultAdapter itemResultAdapter;
    private List<TypeChildrenEntity> resultData=new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_goods_type;
    }

    @Override
    protected void initView() {
        title.setText("分类");
        sendRequest(1,"");
    }

    @Override
    protected void initListeners() {

    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        customLoadding.show();
        HttpManager httpManager = new HttpManager(tag, HttpMothed.GET, IRequestConst.RequestMethod.GetGoodsKindList,null,this);
        httpManager.request();
    }


    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        JSONObject response = (JSONObject) resultInfo.getResponse();
        if(response!=null){
            if(response.optBoolean("success")){
                JSONObject data = response.optJSONObject("data");
                String s = response.toString();
                if(!StringUtils.isEmpty(s)){
                    UploadTypeEntity uploadTypeEntity = GsonUtils.jsonToObject(s, UploadTypeEntity.class);
                    TypeContextEntity typeContextEntity = new TypeContextEntity();
                    typeContextEntity.setName("全部");
                    typeData.add(typeContextEntity);
                    typeData.addAll(uploadTypeEntity.getData());
                    if(typeData!=null&&typeData.size()>0){
                        initData();
                    }
                }
            }
        }else {
            tip("获取分类失败");
        }
    }

    private void initData() {
        final TypeGuideAdapter typeGuideAdapter = new TypeGuideAdapter(mContext, typeData, R.layout.item_type_guide);
        upload_type_view.setAdapter(typeGuideAdapter);
        upload_type_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                typeGuideAdapter.setClickIndex(position);
                refreshAdapter(position);
                if(typeData.get(position).getChildren().size()==0){
                    Intent intent = new Intent();
                    intent.putExtra("typeCode",String.valueOf(typeData.get(position).getId()));
                    intent.putExtra("typeCode_value",String.valueOf(typeData.get(position).getName()));
                    setResult(resultCode,intent);
                    finish();
                }
            }
        });

        refreshAdapter(0);
    }
    public static final int resultCode=0x121;
    /**
     *
     * @param index
     */
    private void refreshAdapter(final int index){
        resultData.clear();
        if(index==0){
            for (int i=1;i<typeData.size();i++){
                resultData.addAll(typeData.get(i).getChildren());
            }
        }else {
            resultData.addAll(typeData.get(index).getChildren());
        }

        if(resultData !=null&& resultData.size()>=0){
            if(itemResultAdapter==null){
                itemResultAdapter = new ItemResultAdapter(mContext, resultData, R.layout.item_type_result);
                upload_result_view.setAdapter(itemResultAdapter);
                upload_result_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        itemResultAdapter.setClickPosition(position, resultData);
                        //返回结果
                        Intent intent = new Intent();
//                        Bundle bundle = new Bundle();
//                        bundle.putString("typeCode",String.valueOf(resultData.get(position).getId()));
//                        intent.putExtra("bundle",bundle);
                        intent.putExtra("typeCode",String.valueOf(resultData.get(position).getId()));
                        intent.putExtra("typeCode_value",String.valueOf(resultData.get(position).getName()));
                        setResult(resultCode,intent);
                        finish();
                    }
                });
            }else {
                itemResultAdapter.setClickPosition(index, resultData);
            }
        }

    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
        tip(msg);
    }

    @OnClick({R.id.head_back})
    public void click(View v){
        switch (v.getId()){
            case R.id.head_back:
                Intent intent = new Intent();
                intent.putExtra("typeCode",typeData.get(1).getName());
                setResult(resultCode,intent);
                finish();
                break;
        }
    }
}
