package com.commodity.scw.ui.widget.layout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.commodity.scw.utils.ScreenUtils;

/**
 * 物流
 * Created by yangxuqiang on 2017/4/4.
 */

public class CircularView extends View {

    private Paint linePaint;
    private Paint circularPaint;
    private int radius;
    private int width;

    public CircularView(Context context) {
        super(context);
        init(context);
    }

    public CircularView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CircularView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        linePaint = new Paint();
        linePaint.setColor(Color.parseColor("#EFEFEF"));
        circularPaint = new Paint();
        circularPaint.setColor(Color.parseColor("#111A38"));

        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(ScreenUtils.dp2px(context,1));
        circularPaint.setAntiAlias(true);
        circularPaint.setStrokeWidth(ScreenUtils.dp2px(context,1));

        radius = ScreenUtils.dp2px(context,5);
        width = ScreenUtils.dp2px(context,31);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if(widthMode==MeasureSpec.UNSPECIFIED){
            widthSize= ScreenUtils.dp2px(getContext(),20);
        }
        if(heightMode==MeasureSpec.UNSPECIFIED){
            heightSize= ScreenUtils.dp2px(getContext(),200);
        }
        setMeasuredDimension(widthSize,heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(width,0,width,getMeasuredHeight(),linePaint);
        canvas.drawCircle(width,radius,radius,circularPaint);

    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
