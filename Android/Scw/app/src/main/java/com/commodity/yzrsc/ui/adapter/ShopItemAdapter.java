package com.commodity.yzrsc.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.UpLoadUtils;
import com.commodity.yzrsc.manager.ConfigManager;
import com.commodity.yzrsc.manager.SPKeyManager;
import com.commodity.yzrsc.model.CartBean;
import com.commodity.yzrsc.model.ShopCardModel;
import com.commodity.yzrsc.model.UserInfo;
import com.commodity.yzrsc.ui.activity.user.MyCartActivity;
import com.commodity.yzrsc.ui.adapter.base.CommonAdapter;
import com.commodity.yzrsc.ui.adapter.base.ViewHolder;
import com.commodity.yzrsc.utils.LogUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.MainActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * created by  xiongwei
 * on
 * role:
 */
public class ShopItemAdapter extends CommonAdapter<CartBean.ShoppingCartGoodsBean> {
    List<CartBean.ShoppingCartGoodsBean> data;
    double totals =0;//总价
    double oneprice = 0;
    double Yuntotals =0;//总运费
    double oneYune = 0;
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                ((MyCartActivity) mContext).deleteCart();
            }

        }
    };
    public ShopItemAdapter(Context context, List<CartBean.ShoppingCartGoodsBean> datas, int layoutId) {
        super(context, datas, layoutId);
        data = datas;
    }

    @Override
    public void convert(final ViewHolder holder, final CartBean.ShoppingCartGoodsBean shoppingCartGoodsBean) {
        ImageView imageView = holder.getView(R.id.iv_goodsImage);
        final TextView textView =holder.getView(R.id.tv_goodsNum);
        holder.setText(R.id.tv_goodsName, shoppingCartGoodsBean.getGoodsSaleName())
                .setText(R.id.tv_goodsPrice, "￥" + shoppingCartGoodsBean.getGoodsPrice())
                .setOnClickListener(R.id.iv_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteShopcar(holder.getPosition());
                    }
                }).setOnClickListener(R.id.iv_minus, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText((shoppingCartGoodsBean.getQuantity()-1)+"");
                if (shoppingCartGoodsBean.getQuantity() > 0) {
                    reduceShopcar(holder.getPosition(),shoppingCartGoodsBean.getQuantity()-1);
                    totals = totals -  shoppingCartGoodsBean.getGoodsPrice()+shoppingCartGoodsBean.getPostage();
                    ((MyCartActivity) mContext).ShowTotal(totals,0);
                }else {
                    handler.sendEmptyMessage(1);
                }
            }
        }).setOnClickListener(R.id.iv_goodsAdd, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reduceShopcar(holder.getPosition(),shoppingCartGoodsBean.getQuantity()+1);
                totals = totals+  shoppingCartGoodsBean.getGoodsPrice();
                ((MyCartActivity) mContext).ShowTotal(totals,shoppingCartGoodsBean.getPostage());
            }
        });
        oneprice = shoppingCartGoodsBean.getGoodsPrice()*shoppingCartGoodsBean.getQuantity();
        totals = totals +oneprice ;
        oneYune = shoppingCartGoodsBean.getPostage();
        Yuntotals = oneYune + shoppingCartGoodsBean.getPostage();

        if (holder.getPosition() == data.size()-1){
            ((MyCartActivity) mContext).ShowTotal(totals,Yuntotals);
        }
        textView.setText(shoppingCartGoodsBean.getQuantity()+"");
        Glide.with(mContext).load(shoppingCartGoodsBean.getGoodsImage()).error(R.drawable.rc_image_error).into(imageView);
    }
    private void reduceShopcar(int position ,int num) {
        Map<String, Object> parmMap = new HashMap<String, Object>();
        parmMap.put("id", data.get(position).getShoppingCartId());
        parmMap.put("memberId",  ConfigManager.instance().getUser().getId());
        parmMap.put("goodsSaleId" ,data.get(position).getGoodsSaleId());
        parmMap.put("quantity",num);
        JSONObject jsonObject = new JSONObject(parmMap);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),jsonObject.toString());
        LogUtil.instance().i(MyCartActivity.class, "我"+parmMap.toString());
        UpLoadUtils.instance().jsonRequest(IRequestConst.RequestMethod.POSTREDUCESHOP, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(mContext, "请求失败", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                handler.sendEmptyMessage(1);
            }
        });
    }
    private void deleteShopcar(int position) {
        Gson gson = new Gson();
        List<Integer> list = new ArrayList<>();
        list.add(data.get(position).getShoppingCartId());
        ShopCardModel shopCardModel = new ShopCardModel();
        shopCardModel.setIds(list);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(shopCardModel));
        UpLoadUtils.instance().jsonRequest(IRequestConst.RequestMethod.POSTDELETESHOP, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(mContext, "删除失败", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Looper.prepare();
                Toast.makeText(mContext, "删除成功", Toast.LENGTH_LONG).show();
                handler.sendEmptyMessage(1);
                Looper.loop();
            }
        });
    }

}
