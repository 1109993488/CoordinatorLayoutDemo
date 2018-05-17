package com.blingbling.coordinatorlayoutdemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by BlingBling on 2018/5/17.
 */
public class MineTopView extends FrameLayout implements CoordinatorLayout.AttachedBehavior {

    protected ImageView mHeadView;
    protected TextView mNameView;
    protected ViewGroup mBottomView;
    protected RoundShapeView mShapeView;

    private Behavior mBehavior;

    public MineTopView(@NonNull Context context) {
        super(context);
    }

    public MineTopView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_mine_top, this);

        mHeadView = findViewById(R.id.head);
        mNameView = findViewById(R.id.name);
        mBottomView = findViewById(R.id.bottom);
        mShapeView = findViewById(R.id.round_shape);
        mHeadView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "mHead", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public CoordinatorLayout.Behavior getBehavior() {
        if (mBehavior == null) {
            mBehavior = new Behavior();
        }
        return mBehavior;
    }

    public static class Behavior extends CoordinatorLayout.Behavior<MineTopView> {

        private final int VALUE_TOOLBAR_HEIGHT = 48;
        private final int VALUE_BOTTOM_MARGIN = 16;

        private float mHeadRangeX;
        private float mHeadRangeY;
        private float mNameRangeX;
        private float mNameRangeY;
        private float mBottomRangeY;
        private float mBottomScaleMin;
        private float mShapeRadiusMax;

        public Behavior() {
        }

        public Behavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean layoutDependsOn(CoordinatorLayout parent, MineTopView child, View dependency) {
            if (dependency instanceof AppBarLayout) {
                final float density = child.getResources().getDisplayMetrics().density;

                mHeadRangeX = child.mHeadView.getMeasuredHeight() / 4.0f;
                mHeadRangeY = child.mHeadView.getTop() + child.mHeadView.getMeasuredHeight() / 2.0f - VALUE_TOOLBAR_HEIGHT / 2.0f * density;

                mNameRangeX = child.mHeadView.getMeasuredHeight() * 0.5f;
                mNameRangeY = child.mNameView.getTop() + child.mNameView.getMeasuredHeight() / 2.0f - VALUE_TOOLBAR_HEIGHT / 2.0f * density;

                mBottomRangeY = child.mBottomView.getTop() - VALUE_TOOLBAR_HEIGHT * density;
                mBottomScaleMin = (VALUE_BOTTOM_MARGIN * 2 * density) / dependency.getMeasuredWidth();
                mShapeRadiusMax = density * 5;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean onDependentViewChanged(CoordinatorLayout parent, MineTopView child, View dependency) {
            int totalScrollRange = ((AppBarLayout) dependency).getTotalScrollRange();
            final float scale = Math.abs(dependency.getY() / totalScrollRange);

            float sizeScale = 1 - 0.5f * scale;
            child.mHeadView.setScaleX(sizeScale);
            child.mHeadView.setScaleY(sizeScale);

            float bottomScale = 1 - mBottomScaleMin * (1 - scale);
            child.mBottomView.setScaleX(bottomScale);
            child.mBottomView.setScaleY(bottomScale);
            child.mShapeView.setRoundRadius(mShapeRadiusMax * (1 - scale));

            child.mHeadView.setTranslationX(-scale * mHeadRangeX);
            child.mHeadView.setTranslationY(-scale * mHeadRangeY);
            child.mNameView.setTranslationX(-scale * mNameRangeX);
            child.mNameView.setTranslationY(-scale * mNameRangeY);
            child.mBottomView.setTranslationY(-scale * mBottomRangeY);

            return super.onDependentViewChanged(parent, child, dependency);
        }
    }
}
