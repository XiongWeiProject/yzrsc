package com.commodity.yzrsc.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.BaseHttpManager;
import com.commodity.yzrsc.http.HttpManageNew;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.http.UpLoadUtils;
import com.commodity.yzrsc.manager.ConfigManager;
import com.commodity.yzrsc.manager.Constanct;
import com.commodity.yzrsc.manager.ImageLoaderManager;
import com.commodity.yzrsc.manager.SPKeyManager;
import com.commodity.yzrsc.model.DynamicAllListModel;
import com.commodity.yzrsc.rongyun.ui.adapter.BaseAdapter;
import com.commodity.yzrsc.ui.activity.friend.PicDynamicActivity;
import com.commodity.yzrsc.ui.activity.friend.VideoDynamicActivity;
import com.commodity.yzrsc.ui.adapter.base.CommonAdapter;
import com.commodity.yzrsc.ui.adapter.base.ViewHolder;
import com.commodity.yzrsc.ui.fragment.HomeFriendFragment;
import com.commodity.yzrsc.ui.widget.customview.GoodsLinearLayout;
import com.commodity.yzrsc.view.PopWinShare;
import com.tencent.mm.opensdk.modelpay.PayReq;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.rong.imageloader.core.ImageLoader;
import io.rong.imkit.utilities.RongUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DynamicListAdapter extends CommonAdapter<DynamicAllListModel> {
    private PopupWindow popupWindow;
    private View popupView = null;
    private EditText inputComment;
    private String nInputContentText;
    private TextView btn_submit;
    private RelativeLayout rl_input_container;
    private InputMethodManager mInputManager;
    PopWinShare popWinShare;
    private boolean isLike = false;
    List<DynamicAllListModel> data;
    int position = 0;

    public DynamicListAdapter(Context context, List<DynamicAllListModel> datas) {
        super(context, datas, R.layout.item_dynamic_list);
        data = datas;
    }

    @Override
    public void convert(ViewHolder holder, final DynamicAllListModel dynamicAllListModel) {
        holder.setText(R.id.dynamic_name, dynamicAllListModel.getMemberNickname())
                .setText(R.id.dynamic_content, dynamicAllListModel.getDescription())
                .setText(R.id.dynamic_time, dynamicAllListModel.getCreateTime());
        ImageView head = holder.getView(R.id.ll_head);
        final ImageView zan = holder.getView(R.id.dynamic_zan);
        final TextView time = holder.getView(R.id.dynamic_time);
        ImageLoaderManager.getInstance().displayImage(dynamicAllListModel.getMemberAvatar(), head,
                R.drawable.ico_pic_fail_defalt);
        position = holder.getPosition();
        zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popWinShare == null) {
                    //自定义的单击事件
                    OnClickLintener paramOnClickListener = new OnClickLintener();
                    popWinShare = new PopWinShare((Activity) mContext, paramOnClickListener, RongUtils.dip2px(163), RongUtils.dip2px(34), 2);
                    TextView isIsLike = (TextView) popWinShare.getContentView().findViewById(R.id.tv_like);
                    if (dynamicAllListModel.isIsLike()) {
                        isLike = true;
                        isIsLike.setText("取消");
                    } else {
                        isLike = false;
                        isIsLike.setText("赞");
                    }
                    //监听窗口的焦点事件，点击窗口外面则取消显示
                    popWinShare.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {

                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                popWinShare.dismiss();
                            }
                        }
                    });


                }
//设置默认获取焦点
                popWinShare.setFocusable(true);
//以某个控件的x和y的偏移量位置开始显示窗口
                popWinShare.showAsDropDown(time, 175, -75);
//如果窗口存在，则更新
                popWinShare.update();
            }
        });
    }

    class OnClickLintener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            popWinShare.dismiss();
            switch (v.getId()) {
                case R.id.tv_like:
                    if (isLike) {
                        isLikes("0");
                    } else {
                        isLikes("1");
                    }
                    break;
                case R.id.tv_evalution:
                    showPopupcomment();
                    break;

                default:
                    break;
            }

        }

    }

    private void isLikes(String goodsSaleId) {
        FormBody requestBody = new FormBody.Builder().add("flag", goodsSaleId).add("id", data.get(position).getId() + "").build();
        UpLoadUtils.instance().requesDynamic(IRequestConst.RequestMethod.PostDynamicLike, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("failure:", e.getMessage());
                tips("e.getMessage()");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.e("onResponse:", string);
                JSONObject jsob;
                if (string != null) {
                    try {
                        jsob = new JSONObject(string);
                        if (jsob != null && jsob.optBoolean("success")) {
                            //提交成功
                            if (isLike) {
                                tips("取消成功");
                            } else {
                                tips("点赞成功");

                            }

                        } else {
                            //提交失败
                            tips("提交失败");

                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                        tips("json解析异常");
                    }
                }

            }
        });
    }


    @SuppressLint("WrongConstant")
    private void showPopupcomment() {
        if (popupView == null) {
            //加载评论框的资源文件
            popupView = LayoutInflater.from(mContext).inflate(R.layout.comment_popupwindow, null);
        }
        inputComment = (EditText) popupView.findViewById(R.id.et_discuss);
        btn_submit = (Button) popupView.findViewById(R.id.btn_confirm);
        rl_input_container = (RelativeLayout) popupView.findViewById(R.id.rl_input_container);
        //利用Timer这个Api设置延迟显示软键盘，这里时间为200毫秒
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                mInputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                mInputManager.showSoftInput(inputComment, 0);
            }

        }, 200);
        if (popupWindow == null) {
            popupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT, false);

        }
        //popupWindow的常规设置，设置点击外部事件，背景色
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE)
                    popupWindow.dismiss();
                return false;

            }
        });

        // 设置弹出窗体需要软键盘，放在setSoftInputMode之前
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        // 再设置模式，和Activity的一样，覆盖，调整大小。
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //设置popupwindow的显示位置，这里应该是显示在底部，即Bottom
        popupWindow.showAtLocation(popupView, Gravity.BOTTOM, 0, 0);

        popupWindow.update();

        //设置监听
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            // 在dismiss中恢复透明度
            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            public void onDismiss() {

                mInputManager.hideSoftInputFromWindow(inputComment.getWindowToken(), 0); //强制隐藏键盘


            }
        });
        //外部点击事件
        rl_input_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputManager.hideSoftInputFromWindow(inputComment.getWindowToken(), 0); //强制隐藏键盘
                popupWindow.dismiss();

            }
        });
        //评论框内的发送按钮设置点击事件
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                nInputContentText = inputComment.getText().toString().trim();

                if (nInputContentText == null || "".equals(nInputContentText)) {
                    tip("请输入评论内容");
                    return;
                }
                mInputManager.hideSoftInputFromWindow(inputComment.getWindowToken(), 0);
                popupWindow.dismiss();
                subEvalution(nInputContentText);

            }
        });
    }

    private void subEvalution(String nInputContentText) {
        FormBody requestBody = new FormBody.Builder().add("memberId", String.valueOf(0)).add("commentType", String.valueOf(0))
                .add("comment", nInputContentText).add("dynamicId", data.get(position).getId() + "").build();
        UpLoadUtils.instance().requesDynamic(IRequestConst.RequestMethod.PostDynamicEvaluate, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                tips(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.e("onResponse:", string);
                JSONObject jsob;
                if (string != null) {
                    try {
                        jsob = new JSONObject(string);
                        if (jsob != null && jsob.optBoolean("success")) {
                            //提交成功
                            tips("评论成功");

                        } else {
                            //提交失败
                            tips("评论失败");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        tips("json解析异常");
                    }
                }

            }
        });
    }

}
