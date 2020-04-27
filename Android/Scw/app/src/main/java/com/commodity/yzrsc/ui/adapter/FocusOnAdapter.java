package com.commodity.yzrsc.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.BaseHttpManager;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.manager.ImageLoaderManager;
import com.commodity.yzrsc.model.Focuser;
import com.commodity.yzrsc.ui.adapter.base.CommonAdapter;
import com.commodity.yzrsc.ui.adapter.base.ViewHolder;
import com.commodity.yzrsc.ui.dialog.CommonDialog;
import com.commodity.yzrsc.ui.dialog.CustomLoadding;
import com.commodity.yzrsc.utils.CustomToast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liyushen on 2017/4/3 22:34
 * 关注 adapter
 */
public class FocusOnAdapter  extends CommonAdapter<Focuser> {
    private Context context;
    private List<Focuser> dataList;
    public FocusOnAdapter(Context context, List<Focuser> datas, int layoutId) {
        super(context, datas, layoutId);
        this.context=context;
        this.dataList=datas;
    }

    @Override
    public void convert(final ViewHolder holder, final Focuser data) {
        holder.setText(R.id.tv_text1,data.getCreateTime());
        holder.setText(R.id.tv_text2,data.getMemberName());
        ImageLoaderManager.getInstance().displayImage(data.getMemberAvatar(), (ImageView) holder.getView(R.id.view_ico1),R.drawable.ico_defalut_header);
        if (data.isIsFollowed()){
            holder.setText(R.id.btn_huxiangguanzhu,"取消关注");
        }else {
            holder.setText(R.id.btn_huxiangguanzhu,"互相关注");
        }
        holder.setOnClickListener(R.id.btn_huxiangguanzhu, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fouce(data.getMemberId()+"",holder.getPosition());
            }
        });
    }


    private void fouce(final String id, final int postion){
        CommonDialog commonDialog=new CommonDialog(mContext);
        commonDialog.show();
        commonDialog.setContext(dataList.get(postion).isIsFollowed()?"确定取消关注？":"确定关注？");
        commonDialog.setClickSubmitListener(new CommonDialog.OnClickSubmitListener() {
            @Override
            public void clickSubmit() {
                String url = IRequestConst.RequestMethod.AddContact;
                if (dataList.get(postion).isIsFollowed()){
                    url = IRequestConst.RequestMethod.DeleteContact;
                }
                final CustomLoadding customLoadding=new CustomLoadding(context);
                customLoadding.show();
                Map<String, String> parmMap = new HashMap<String, String>();
                parmMap.put("contactId",id);
                HttpManager httpManager = new HttpManager(0, HttpMothed.POST,
                        url, parmMap, new BaseHttpManager.IRequestListener() {
                    @Override
                    public void onPreExecute(int Tag) {
                    }
                    @Override
                    public void onSuccess(int Tag, ServiceInfo result) {
                        JSONObject resultJson= (JSONObject) result.getResponse();
                        if (resultJson!=null&&resultJson.optBoolean("success")){
                            if (dataList.get(postion).isIsFollowed()){
                                CustomToast.showToast("取消关注成功");
                                dataList.get(postion).setIsFollowed(false);
                            }else {
                                CustomToast.showToast("关注成功");
                                dataList.get(postion).setIsFollowed(true);
                            }
                            notifyDataSetChanged();
                        }else {
                            if (resultJson!=null&&!resultJson.optBoolean("success")){
                                CustomToast.showToast(resultJson.optString("msg"));
                            }
                        }
                        if (customLoadding.isShowing()){
                            customLoadding.dismiss();
                        }
                    }

                    @Override
                    public void onError(int Tag, String code, String msg) {
                        if (customLoadding.isShowing()){
                            customLoadding.dismiss();
                        }
                    }

                    @Override
                    public void OnTimeOut(int Tag, boolean isShowTip) {
                        if (customLoadding.isShowing()){
                            customLoadding.dismiss();
                        }
                    }

                    @Override
                    public void OnNetError(int Tag, boolean isShowTip) {

                    }
                });
                httpManager.request();
            }
        });
    }
}