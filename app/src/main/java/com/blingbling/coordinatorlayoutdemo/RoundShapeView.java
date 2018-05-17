package com.blingbling.coordinatorlayoutdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by BlingBling on 2018/5/18.
 */
public class RoundShapeView extends View {

    private static final int SHADOW_DY = 3;
    private Paint mPaint;
    private float mShadowDy;
    private float mRadius;

    public RoundShapeView(Context context) {
        this(context, null);
    }

    public RoundShapeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        final float density = context.getResources().getDisplayMetrics().density;
        mShadowDy = SHADOW_DY * density;
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        //绘制阴影，param1：模糊半径；param2：x轴大小：param3：y轴大小；param4：阴影颜色
        mPaint.setShadowLayer(mShadowDy, 0, mShadowDy, Color.parseColor("#22000000"));
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rect = new RectF(mRadius, 0, getMeasuredWidth() - mRadius, getMeasuredHeight() - mShadowDy * 2);
        canvas.drawRoundRect(rect, mRadius, mRadius, mPaint);
    }

    public void setRoundRadius(float radius) {
        mRadius = radius;
        postInvalidate();
    }
}
