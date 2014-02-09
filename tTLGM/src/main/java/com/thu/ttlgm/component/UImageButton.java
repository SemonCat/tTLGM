package com.thu.ttlgm.component;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.thu.ttlgm.R;
import com.thu.ttlgm.utils.ViewUtil;

import java.util.List;

public class UImageButton extends ImageButton {
    private final static String TAG = UImageButton.class.getName();

    private ScaleAnimation mScaleAnimation;

    public UImageButton(Context context) {
        this(context, null);
    }

    public UImageButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.imageButtonStyle);
        //this.setBackgroundResource(R.drawable.round_button_green);
        //this.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    }

    public UImageButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        /*
        if (!isInEditMode())
            setSeleteStyle();
            */
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int MeasureSpec = widthMeasureSpec;

        if (heightMeasureSpec<MeasureSpec)
            MeasureSpec = heightMeasureSpec;
        super.onMeasure(MeasureSpec, MeasureSpec);
    }

    private void setSeleteStyle() {
        StateListDrawable buttonStateListDrawable = new StateListDrawable();

        Drawable mDrawable = getDrawable();
        mDrawable.setColorFilter(new PorterDuffColorFilter(0xFFFFFF,
                Mode.LIGHTEN));

        GradientDrawable selectedDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0x82C0C0C0, 0x82C0C0C0});
        selectedDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        selectedDrawable.setCornerRadius(250);


        Drawable[] layers = new Drawable[2];
        layers[0] = mDrawable;
        layers[1] = selectedDrawable;
        LayerDrawable layerDrawable = new LayerDrawable(layers);


        buttonStateListDrawable.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_window_focused}, layerDrawable);

        buttonStateListDrawable.addState(new int[]{android.R.attr.state_enabled}, getDrawable());

        this.setImageDrawable(buttonStateListDrawable);
    }

    List<View> mOtherButtons;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!isFocusable()) return false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                mOtherButtons = ViewUtil.findViewWithTypeRecursively(ViewUtil.getParent(this), UImageButton.class);

                for (View mOtherButton : mOtherButtons) {
                    mOtherButton.setFocusable(false);
                }

                this.setFocusable(true);

                break;

            case MotionEvent.ACTION_UP:

                if (mOtherButtons!=null){
                    for (View mOtherButton : mOtherButtons)
                        mOtherButton.setFocusable(true);
                }

                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }
}

