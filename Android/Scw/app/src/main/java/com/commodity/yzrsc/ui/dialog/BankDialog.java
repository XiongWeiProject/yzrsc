package com.commodity.yzrsc.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.commodity.yzrsc.MainApplication;
import com.commodity.yzrsc.R;
import com.commodity.yzrsc.ui.adapter.SingleViewAdapter;

import java.util.List;

/**
 * 选择银行
 * Created by yangxuqiang on 2017/4/3.
 */

public class BankDialog extends Dialog {
    private List<String> items;
    private Context context;
    private OnitemClickListener onitemClickListener;

    public BankDialog(Context context, List<String> items) {
        super(context, R.style.CommonDialogStyle);
        this.context=context;
        this.items=items;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }
    private void init(){
        View inflate = View.inflate(context, R.layout.single_select, null);
        setContentView(inflate);
        LinearLayout cancel = (LinearLayout) inflate.findViewById(R.id.single_cancel);
        ListView single_listview = (ListView) inflate.findViewById(R.id.single_listview);
        final SingleViewAdapter singleViewAdapter = new SingleViewAdapter(context, items, R.layout.item_single);
        single_listview.setAdapter(singleViewAdapter);
        single_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                singleViewAdapter.setClickPosition(i);
                if(onitemClickListener!=null){
                    onitemClickListener.itemClick(i);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (MainApplication.SCREEN_W * 0.75); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);

    }
    public void setItemClick(OnitemClickListener onitemClickListener){
        this.onitemClickListener=onitemClickListener;
    }

    public interface OnitemClickListener{
        void itemClick(int position);
    }

}
