package com.commodity.scw.ui.widget.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.commodity.scw.R;
import com.commodity.scw.manager.ImageLoaderManager;
import com.commodity.scw.model.ImgType;
import com.commodity.scw.ui.widget.imageview.XCRoundRectImageView;
import com.commodity.scw.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyushen on 2017/3/19 15:41
 */
public class GoodsLinearLayout extends LinearLayout{
    private Context context;
    int sc_w= 80;
    LayoutParams layoutParams=new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    LayoutParams layoutParamsImg ;
    List<ImageView> imageViewList =new ArrayList<>();
    private OnItemCickListen onItemCickListen;
    public GoodsLinearLayout(Context context) {
        super(context);
        this.context=context;
        init();
    }
    public GoodsLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init();

    }

    public GoodsLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        init();

    }
    private void init(){
        setOrientation(HORIZONTAL);
        setLayoutParams(layoutParams);
        sc_w =  ScreenUtils.dp2px(context,80);
        layoutParamsImg =new LayoutParams(sc_w, sc_w);
        layoutParamsImg.setMargins(16,0,0,0);
        for (int i = 0; i < 9 ; i++) {
            imageViewList.add(getImageView());
        }
        for (int i = 0; i < imageViewList.size() ; i++) {
          addView(imageViewList.get(i));
        }
        
    }

    public void setImgs(final ImgType[] imgTypes){

        if (imgTypes!=null&&imgTypes.length>0){
            for (int i = 0; i < imageViewList.size(); i++) {
                if (i<imgTypes.length){
                    imageViewList.get(i).setVisibility(VISIBLE);
                    final int finalI = i;
                    if (imgTypes[i].isVideo()){
                        if (imgTypes[i].getImgpath()!=null){
                            ImageLoaderManager.getInstance().displayImage(imgTypes[i].getImgpath(),imageViewList.get(i),R.drawable.icon_video);
                        }else {
                            imageViewList.get(i).setImageDrawable(context.getResources().getDrawable(R.drawable.icon_video));
                        }
                    }else {
                        if(imgTypes[i].getImgpath()!=null){
                            ImageLoaderManager.getInstance().displayImage(imgTypes[i].getImgpath(),imageViewList.get(i),R.drawable.ico_pic_fail_defalt);
                        }else {
                            imageViewList.get(i).setImageDrawable(context.getResources().getDrawable(R.drawable.ico_pic_fail_defalt));
                        }
                    }
                    imageViewList.get(i).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (onItemCickListen!=null){
                                onItemCickListen.onClick(imgTypes[finalI], finalI);
                            }
                        }
                    });
                }else {
                    imageViewList.get(i).setVisibility(GONE);
                }
            }
        }else {
        }
    }

    private ImageView getImageView(){
        XCRoundRectImageView imageView=new XCRoundRectImageView(context);
        imageView.setLayoutParams(layoutParamsImg);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    public interface OnItemCickListen{
        void onClick(ImgType imgType,int postion);
    }

    public void setOnItemCickListen(OnItemCickListen onItemCickListen){
        this.onItemCickListen=onItemCickListen;
    }
}
