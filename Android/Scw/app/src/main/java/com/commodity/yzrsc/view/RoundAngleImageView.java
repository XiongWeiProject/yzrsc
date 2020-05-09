package com.commodity.yzrsc.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.commodity.yzrsc.R;


/**
 * Time: 2020/3/19
 * Description:
 */
@SuppressLint("AppCompatCustomView")
public class RoundAngleImageView extends ImageView {
    private Paint paint;
    private int   roundWidth = 7;
    private int   roundHeight = 7;
    private boolean   bottomShow = true;
    private Paint paint2;

    public RoundAngleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public RoundAngleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundAngleImageView(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundAngleImageView);
            roundWidth = a.getDimensionPixelSize(R.styleable.RoundAngleImageView_roundWidth, roundWidth);
            roundHeight = a.getDimensionPixelSize(R.styleable.RoundAngleImageView_roundHeight, roundHeight);
            bottomShow = a.getBoolean(R.styleable.RoundAngleImageView_bottomShow, bottomShow);
        } else {
            float density = context.getResources().getDisplayMetrics().density;
            roundWidth = (int) (roundWidth * density);
            roundHeight = (int) (roundHeight * density);
        }

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        paint2 = new Paint();
        paint2.setXfermode(null);
    }

    @Override
    public void draw(Canvas canvas) {
        if (getMeasuredWidth()>0&&getMeasuredHeight()>0){
            Bitmap bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas2 = new Canvas(bitmap);
            super.draw(canvas2);
            drawLiftUp(canvas2);
            drawRightUp(canvas2);
            if (bottomShow) {
                drawLiftDown(canvas2);
                drawRightDown(canvas2);
            }
            canvas.drawBitmap(bitmap, 0, 0, paint2);
            bitmap.recycle();
        }
    }

    private void drawLiftUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, roundHeight);
        path.lineTo(0, 0);
        path.lineTo(roundWidth, 0);
        path.arcTo(new RectF(0, 0, roundWidth * 2, roundHeight * 2), -90, -90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawLiftDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, getMeasuredHeight() - roundHeight);
        path.lineTo(0, getMeasuredHeight());
        path.lineTo(roundWidth, getMeasuredHeight());
        path.arcTo(new RectF(0, getMeasuredHeight() - roundHeight * 2, roundWidth * 2, getMeasuredHeight()), 90, 90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawRightDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo(getMeasuredWidth() - roundWidth, getMeasuredHeight());
        path.lineTo(getMeasuredWidth(), getMeasuredHeight());
        path.lineTo(getMeasuredWidth(), getMeasuredHeight() - roundHeight);
        path.arcTo(new RectF(getMeasuredWidth() - roundWidth * 2, getMeasuredHeight() - roundHeight * 2, getMeasuredWidth(), getMeasuredHeight()), -0, 90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawRightUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo(getMeasuredWidth(), roundHeight);
        path.lineTo(getMeasuredWidth(), 0);
        path.lineTo(getMeasuredWidth() - roundWidth, 0);
        path.arcTo(new RectF(getMeasuredWidth() - roundWidth * 2, 0, getMeasuredWidth(), 0 + roundHeight * 2), -90, 90);
        path.close();
        canvas.drawPath(path, paint);
    }
}

