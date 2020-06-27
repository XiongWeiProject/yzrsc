package com.commodity.yzrsc.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.BaseHttpManager;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.http.UpLoadUtils;
import com.commodity.yzrsc.manager.ConfigManager;
import com.commodity.yzrsc.manager.ImageLoaderManager;
import com.commodity.yzrsc.model.DynamicAllListModel;
import com.commodity.yzrsc.model.Evalution;
import com.commodity.yzrsc.ottobus.BusProvider;
import com.commodity.yzrsc.ottobus.Event;
import com.commodity.yzrsc.ui.activity.friend.DynamicDetailsActivity;
import com.commodity.yzrsc.ui.activity.friend.OtherDynamicActivity;
import com.commodity.yzrsc.ui.activity.friend.PicDynamicActivity;
import com.commodity.yzrsc.ui.activity.general.BigPictureActivity;
import com.commodity.yzrsc.ui.adapter.base.BaseRecycleAdapter;
import com.commodity.yzrsc.ui.adapter.base.CommonAdapter;
import com.commodity.yzrsc.ui.adapter.base.ViewHolder;
import com.commodity.yzrsc.ui.dialog.CommonDialog;
import com.commodity.yzrsc.view.MoreEvalutionDialog;
import com.commodity.yzrsc.view.PopWinShare;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;
import fm.jiecao.jcvideoplayer_lib.JCVideoPalyActivity;
import fm.jiecao.jcvideoplayer_lib.VideoEvents;
import io.rong.imkit.utilities.RongUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class MyDynamicListAdapter extends CommonAdapter<DynamicAllListModel> {
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
    List<Evalution> evalution = new ArrayList<>();
    int position = 0;
    RecyclerView rcv_pic, rcv_zan, rcv_evalution;
    RelativeLayout rl_more;
    ShowPicAdapter showPicAdapter;

    public MyDynamicListAdapter(Context context, List<DynamicAllListModel> datas) {
        super(context, datas, R.layout.item_my_dynamic_list);
        data = datas;
    }

    @Override
    public void convert(final ViewHolder holder, final DynamicAllListModel dynamicAllListModel) {
        holder.setText(R.id.dynamic_name, dynamicAllListModel.getMemberNickname())
                .setText(R.id.dynamic_content, dynamicAllListModel.getDescription())
                .setText(R.id.dynamic_time, dynamicAllListModel.getCreateTime()).setText(R.id.like_count, dynamicAllListModel.getLikeCount() + "")
                .setText(R.id.evalution_count, dynamicAllListModel.getCommentCount() + "").setText(R.id.dynamic_dretime, dynamicAllListModel.getCreateTime());
        ImageView head = holder.getView(R.id.ll_head);
        final ImageView zan = holder.getView(R.id.dynamic_zan);
        final TextView time = holder.getView(R.id.dynamic_time);
        final TextView dynamic_delete = holder.getView(R.id.dynamic_delete);
        rcv_pic = holder.getView(R.id.rcv_pic);
        rcv_zan = holder.getView(R.id.rcv_zan);
        rcv_evalution = holder.getView(R.id.rcv_evalution);
        View view_line = holder.getView(R.id.view_line);
        rl_more = holder.getView(R.id.rl_more);
        LinearLayout ll_evalution = holder.getView(R.id.ll_evalution);
        LinearLayout ll_evalution1 = holder.getView(R.id.ll_evalution1);
        LinearLayout ll_zan = holder.getView(R.id.ll_zan);
        ImageLoaderManager.getInstance().displayImage(dynamicAllListModel.getMemberAvatar(), head,
                R.drawable.ico_pic_fail_defalt);
        position = holder.getPosition();
        if (dynamicAllListModel.getPictures().size() == 0) {
            if (TextUtils.isEmpty(dynamicAllListModel.getVideoUrl())) {
                rcv_pic.setVisibility(View.GONE);
            } else {
                rcv_pic.setVisibility(View.VISIBLE);
                List<String> list = new ArrayList<>();
                list.add(dynamicAllListModel.getExt1());
                showPicAdapter = new ShowPicAdapter(mContext, list, R.layout.item_show_pic, 1);
                rcv_pic.setLayoutManager(new GridLayoutManager(mContext, 2));
                rcv_pic.setAdapter(showPicAdapter);
            }
        } else {
            rcv_pic.setVisibility(View.VISIBLE);
            if (dynamicAllListModel.getPictures().size() == 1) {
                showPicAdapter = new ShowPicAdapter(mContext, dynamicAllListModel.getPictures(), R.layout.item_show_pic, 0);
                rcv_pic.setLayoutManager(new GridLayoutManager(mContext, 2));
            } else {
                showPicAdapter = new ShowPicAdapter(mContext, dynamicAllListModel.getPictures(), R.layout.item_shows_pic, 0);
                rcv_pic.setLayoutManager(new GridLayoutManager(mContext, 3));
            }
            rcv_pic.setAdapter(showPicAdapter);

        }
        if (dynamicAllListModel.getLikeCount() == 0 && dynamicAllListModel.getCommentCount() == 0) {
            ll_evalution1.setVisibility(View.GONE);
        }
        if (dynamicAllListModel.getLikeList()!=null&&dynamicAllListModel.getLikeCount() > 0) {
            ll_evalution1.setVisibility(View.VISIBLE);
            ll_zan.setVisibility(View.VISIBLE);
            view_line.setVisibility(View.GONE);
            rcv_zan.setLayoutManager(new GridLayoutManager(mContext, 3));
            //获取点赞列表
            ZanAdapter zanAdapter = new ZanAdapter(mContext, dynamicAllListModel.getLikeList(), R.layout.item_zan);
            rcv_zan.setAdapter(zanAdapter);
        }
        if (dynamicAllListModel.getCommentList()!=null&&dynamicAllListModel.getCommentCount() > 0) {
            ll_evalution.setVisibility(View.VISIBLE);
            rcv_evalution.setVisibility(View.VISIBLE);
            view_line.setVisibility(View.GONE);

            rcv_evalution.setLayoutManager(new LinearLayoutManager(mContext));
            EvalutionAdapter evalutionAdapter = new EvalutionAdapter(mContext, dynamicAllListModel.getCommentList(), R.layout.item_evalution);
            rcv_evalution.setAdapter(evalutionAdapter);
        }
        if (dynamicAllListModel.getCommentCount()>5){
            rl_more.setVisibility(View.VISIBLE);
            rl_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //提交成功
                    final MoreEvalutionDialog renzhengSuccessDialog = new MoreEvalutionDialog(mContext, dynamicAllListModel.getId()+"");
                    renzhengSuccessDialog.show();
                    renzhengSuccessDialog.setOnclickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            renzhengSuccessDialog.dismiss();
                        }
                    });
                    renzhengSuccessDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            switch (keyCode) {
                                case KeyEvent.KEYCODE_BACK:
                                    return true;
                                default:
                                    break;
                            }
                            return false;
                        }
                    });
                }
            });
        }else {
            rl_more.setVisibility(View.GONE);
        }
//        else {
//            ll_evalution.setVisibility(View.VISIBLE);
//            ll_zan.setVisibility(View.VISIBLE);
//            ll_evalution1.setVisibility(View.VISIBLE);
//            view_line.setVisibility(View.VISIBLE);
//        }
        dynamic_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CommonDialog commonDialog = new CommonDialog(mContext);
                commonDialog.show();
                commonDialog.setContext("是否删除此动态？");
                commonDialog.setClickSubmitListener(new CommonDialog.OnClickSubmitListener() {
                    @Override
                    public void clickSubmit() {
                        commonDialog.dismiss();
                        deleteDynamic(dynamicAllListModel.getId());
                        BusProvider.getInstance().post(new Event.NotifyChangedView("MyDynamicActivity"));
                    }
                });
            }
        });;

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
        showPicAdapter.setOnItemClickListener(new ShowPicAdapter.ItemClickListener() {

            @Override
            public void itemClick(View v, int position, int type) {
                if (dynamicAllListModel.getDynamicType() == 1) {
                    Intent intent = new Intent(mContext, BigPictureActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("urls", (Serializable) data.get(holder.getPosition()).getPictures());
                    bundle.putInt("index", position);
                    bundle.putInt("type", 0);
                    intent.putExtras(bundle);
                    ((Activity) mContext).startActivity(intent);
                } else {
                    JCVideoPalyActivity.toActivity(mContext, data.get(holder.getPosition()).getVideoUrl(), data.get(holder.getPosition()).getExt1(), data.get(holder.getPosition()).getDescription());
                    VideoEvents videoEvents = new VideoEvents().setType(VideoEvents.POINT_CLICK_BLANK);
                    EventBus.getDefault().post(videoEvents);
                }
            }
        });
    }

    private void deleteDynamic(int id) {
        FormBody requestBody = new FormBody.Builder().add("id", String.valueOf(id))
                .add("scw-token",ConfigManager.instance().getUser().getDeviceToken()).build();
        UpLoadUtils.instance().requesDynamic(IRequestConst.RequestMethod.PostDynamDelete, requestBody, new Callback() {
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
//                            tips("删除成功");

                        } else {
                            //提交失败
//                            tips("删除失败");

                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
//                        tips("json解析异常");
                }
                }

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
                case R.id.tv_forwarding:
                    DynamicDetailsActivity.startAction(mContext,data.get(position).getId() +"","动态详情",data.get(position));
                    break;
                default:
                    break;
            }

        }

    }

    private void isLikes(String goodsSaleId) {
        FormBody requestBody = new FormBody.Builder().add("flag", goodsSaleId).add("id", data.get(position).getId() + "")
                .add("scw-token",ConfigManager.instance().getUser().getDeviceToken()).build();
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
//                            //提交成功
//                            if (isLike) {
//                                tips("取消成功");
//                            } else {
//                                tips("点赞成功");
//
//                            }

                        } else {
                            //提交失败
//                            tips("提交失败");

                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
//                        tips("json解析异常");
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
                .add("comment", nInputContentText).add("dynamicId", data.get(position).getId() + "")
                .add("scw-token",ConfigManager.instance().getUser().getDeviceToken()).build();
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
//                            tips("评论成功");

                        } else {
                            //提交失败
//                            tips("评论失败");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
//                        tips("json解析异常");
                    }
                }

            }
        });
    }

}
