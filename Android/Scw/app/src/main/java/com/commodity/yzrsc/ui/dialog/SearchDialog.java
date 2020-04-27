package com.commodity.yzrsc.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.manager.SPManager;
import com.commodity.yzrsc.ui.adapter.SearchHistoryAdapter;
import com.commodity.yzrsc.ui.adapter.base.CommonAdapter;
import com.commodity.yzrsc.ui.widget.textview.ClearSearchEditText;
import com.commodity.yzrsc.ui.widget.xlistView.XListView;
import com.commodity.yzrsc.utils.KeyBoardUtils;
import com.commodity.yzrsc.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 作者：liyushen on 2016/5/16
 * 功能：搜索对话框
 */
public class SearchDialog<T> extends Dialog {
    private Window window;
    String SEARCHKET;
    Context context;
    View view;
    TextView  tv_cancel, tv_nodata;
    XListView lv_result;
    ListView lv_history;
    public ClearSearchEditText ed_search;
    List<T> resultlist;
    List<String> historylist = new ArrayList<>();//历史记录列表
    CommonAdapter<T> resultAdapter;
    SearchHistoryAdapter historyAdapter;
    OnSearchListener listener;
    int typeselectposition = 0;
    int page = 1;
    int totalPage = 0;
    public View v_background;
    private View.OnClickListener cancelListener;

    public interface OnSearchListener {
        /**
         * 搜索结果回调
         *
         * @param search     搜索内容
         */
        void doSearch(String search);
    }

    /**
     * 搜索监听
     *
     * @param listener
     */
    public void setOnSearchListener(OnSearchListener listener) {
        this.listener = listener;
    }

    public SearchDialog(Context context,   List<T> resultlist, CommonAdapter<T> resultAdapter) {
        super(context);
        this.context = context;
        this.resultlist = resultlist;
        this.resultAdapter = resultAdapter;
        SEARCHKET = context.getClass().getSimpleName();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = LayoutInflater.from(context).inflate(R.layout.activity_search, null);
        initView();
        initListener();
        showHistroyList();
        window = null;
    }


    public SearchDialog(Context context,   List<T> resultlist, CommonAdapter<T> resultAdapter,int layId) {
        super(context);
        this.context = context;
        this.resultlist = resultlist;
        this.resultAdapter = resultAdapter;
        SEARCHKET = context.getClass().getSimpleName();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = LayoutInflater.from(context).inflate(layId, null);
        initView();
        initListener();
        showHistroyList();
        window = null;
    }

    public void initView() {
        v_background =  view.findViewById(R.id.v_background);
        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_nodata = (TextView) view.findViewById(R.id.tv_nodata);
        lv_result = (XListView) view.findViewById(R.id.lv_result);
        lv_history = (ListView) view.findViewById(R.id.lv_history);
        ed_search = (ClearSearchEditText) view.findViewById(R.id.ed_search);
        lv_history.addHeaderView(LayoutInflater.from(context).inflate(R.layout.layout_header, null));
        historyAdapter = new SearchHistoryAdapter(context, historylist, R.layout.item_history);
        lv_history.setAdapter(historyAdapter);
        lv_result.setAdapter(resultAdapter);
        lv_result.setPullLoadEnable(true);
    }

    public void initListener() {
        tv_nodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"".equals(ed_search.getText().toString())) {
                    page = 1;
                    if (listener != null) {
                        listener.doSearch(ed_search.getText().toString());
                    }
                }
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
                if(cancelListener!=null){
                    cancelListener.onClick(v);
                }
            }
        });

        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ("".equals(s.toString()) || "".equals(ed_search.getText().toString())) {
                    showHistroyList();
                } else {
                    if (listener != null)
                        page = 1;
                    listener.doSearch(s.toString() );
                }
            }
        });

        historyAdapter.setOnHistorySearchListener(new SearchHistoryAdapter.OnHistorySearchListener() {
            @Override
            public void OnClearBt() {
                SPManager.instance().setString(SEARCHKET, "");
                getHistorySearchList();
                historyAdapter.notifyDataSetChanged();
                showNodata("暂无历史搜索记录");
            }

            @Override
            public void OnItemClick(String search) {
                ed_search.setText(search);
                ed_search.setSelection(search.length());
                if (listener != null)
                    listener.doSearch(search );
            }
        });
        lv_result.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                page = 1;
                if (listener != null) {
                    listener.doSearch(ed_search.getText().toString() );
                }
            }

            @Override
            public void onLoadMore() {
                page++;
                if (page > totalPage) {
                    lv_result.setPullLoadEnable(false);
                    return;
                }
                if (listener != null) {
                    listener.doSearch(ed_search.getText().toString());
                }
            }
        });

        lv_result.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                         KeyBoardUtils.closeKeybord(ed_search, context);
                        break;
                }
                return false;
            }
        });
    }

    public void showNodata(String str) {
        lv_history.setVisibility(View.GONE);
        lv_result.setVisibility(View.GONE);
        tv_nodata.setVisibility(View.VISIBLE);
        tv_nodata.setText(str);
        resultAdapter.notifyDataSetChanged();
    }

    public void showHistroyList() {
        getHistorySearchList();
        lv_history.setVisibility(View.VISIBLE);//历史搜索显示
        lv_result.setVisibility(View.GONE);//搜索结果隐藏
        tv_nodata.setVisibility(View.GONE);
        if (historylist.size() == 0) {
            showNodata("暂无历史搜索记录");
        } else {
            if (historyAdapter != null) {
                historyAdapter.notifyDataSetChanged();
            }
        }
    }

    public void showResultList() {
        lv_history.setVisibility(View.GONE);//历史搜索隐藏
        lv_result.setVisibility(View.VISIBLE);//搜索结果显示
        tv_nodata.setVisibility(View.GONE);
        if (resultlist.size() == 0) {
            showNodata("暂无搜索结果");
        } else {
            resultAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void show() {
        setContentView(view);
        windowDeploy();
        // 设置触摸对话框意外的地方取消对话框
        showKeyBoardByTimer();
        setCanceledOnTouchOutside(false);
        ed_search.setText("");
        super.show();
    }

    /**
     * 设置窗口显示
     */
    public void windowDeploy() {
        window = getWindow(); // 得到对话框
        window.setWindowAnimations(R.style.AnimationAcivity); // 设置窗口弹出动画
        window.setBackgroundDrawableResource(R.color.trans); // 设置对话框背景为透明
        android.view.WindowManager.LayoutParams p = getWindow()
                .getAttributes(); // 获取对话框当前的参数值
        p.height = ScreenUtils.getScreenHeight(context); // 高度设置为屏幕的高度
        p.width = ScreenUtils.getScreenWidth(context); // 宽度设置为屏幕的宽度
        p.gravity = Gravity.CENTER; // 设置重力
        window.setAttributes(p);
    }

    /*
    * 写缓存
    */
    public void writeInSP(String editString) {
        String sp = SPManager.instance().getString(SEARCHKET);
        if (sp == null || sp.equals("")) {
            SPManager.instance().setString(SEARCHKET, editString);
        } else {
            //使用分割追加方式，分隔符为"/"
            SPManager.instance().setString(SEARCHKET, clearRepeat(sp, editString));
        }
        //TODO 刷新列表
    }

    /*
     * 清除旧的重复缓存数据，显示新的，节约缓存
     * 逻辑：当输入的字符在缓存中有相同，清除缓存中的字符，将新的字符添加在缓存里
     */
    public String clearRepeat(String sp, String editString) {
        if (sp.equals(editString)) {//两次搜索记录，但是搜索内容一样
            return editString;
        }
        if (!sp.contains("/")) {//只有一次搜索记录
            return sp + "/" + editString;
        }
        String[] arr = sp.split("/");
        List<String> list = new ArrayList<>();
        Collections.addAll(list, arr);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(editString)) {
                list.remove(i);
                i--;
            }
        }
        String newsp = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            newsp = newsp + "/" + list.get(i);
        }
        return newsp + "/" + editString;
    }

    /*
   * 获取缓存历史记录
   */
    private void getHistorySearchList() {
        String sp = SPManager.instance().getString(SEARCHKET);
        Log.e("sp", sp);
        if (sp == null || sp.equals("")) {
            historylist.clear();

        } else {
            if (!sp.contains("/")) {
                historylist.clear();
                historylist.add(sp);
                return;
            }
            String[] arr = sp.split("/");
            historylist.clear();
            for (int i = arr.length - 1; i >= 0; i--) {
                historylist.add(arr[i]);//将缓存中的历史记录倒叙加载
            }
        }
    }
    /**
     * 用一个定时器控制当打开这个Activity的时候就出现软键盘
     * <p/>
     * 默认500毫秒
     */
    public void showKeyBoardByTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ed_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 500);
    }

    public XListView getListView() {
        return lv_result;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }


    public String getSearchString() {
        return ed_search.getText().toString();
    }

    public void setHint(String hint){
        this.ed_search.setHint(hint);
    }
    public void setCancelListener(View.OnClickListener cancelListener){
        this.cancelListener=cancelListener;
    }
}
