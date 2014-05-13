package com.thu.ttlgm.fragment.vote;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.makeramen.RoundedImageView;

/**
 * Created by SemonCat on 2014/5/7.
 */
public class StatisticView extends RoundedImageView{

    public StatisticView(Context context) {
        super(context);
    }

    public StatisticView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StatisticView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ScaleAnimation anim = new ScaleAnimation(0,1,0,1);
        anim.setDuration(1000);
        anim.setFillAfter(true);
        this.startAnimation(anim);
    }
}
