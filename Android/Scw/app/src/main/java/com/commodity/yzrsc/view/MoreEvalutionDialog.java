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

import com.commodity.yzrsc.MainApplication;
import com.commodity.yzrsc.R;
import com.commodity.yzrsc.model.Evalution;
import com.commodity.yzrsc.ui.adapter.EvalutionAdapter;

import java.util.List;

public class MoreEvalutionDialog extends Dialog {
    private Context context;
    private View.OnClickListener listener;
    List<Evalution>datas ;
    public MoreEvalutionDialog(Context context , List<Evalution>data) {
        super(context, R.style.CommonDialogStyle);
        this.context = context;
        datas = data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        View inflate = LayoutInflater.from(context).inflate(R.layout.layout_more_evalution, null);
        Button confirm = (Button) inflate.findViewById(R.id.btn_close);
        RecyclerView rcv_evalution = (RecyclerView) inflate.findViewById(R.id.rcv_more_evalution);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onClick(v);
                }
            }
        });
        rcv_evalution.setLayoutManager(new LinearLayoutManager(context));
        EvalutionAdapter zanAdapter = new EvalutionAdapter(context, datas, R.layout.item_evalution);
        rcv_evalution.setAdapter(zanAdapter);
        setContentView(inflate);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (MainApplication.SCREEN_W * 0.75); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
    }
    public void setOnclickListener(View.OnClickListener listener){
        this.listener=listener;
    }
}
