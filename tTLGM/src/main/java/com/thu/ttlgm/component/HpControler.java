package com.thu.ttlgm.component;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.thu.ttlgm.R;
import com.yvelabs.chronometer2.Chronometer;

/**
 * Created by SemonCat on 2014/3/12.
 */
public class HpControler {

    public interface OnHpControlerListener{
        void OnHpStartClick();

        void OnHpResumeClick();

        void OnHpPauseClick();

        void OnHpStopClick();
    }

    private Context mContext;

    private UChronometer Timer;

    private ToggleButton HpControl;

    private ImageView StopHp;

    private OnHpControlerListener mListener;

    private boolean IsStart;

    private View Container;

    public HpControler(Context context,SlidingDrawer slidingDrawer){
        this.mContext = context;
        this.Container = slidingDrawer.getContent();
        this.IsStart = false;

        setupView();
        setupEvent();
    }

    public HpControler(Context context,DrawerLayout mDrawerLayout){
        this.mContext = context;
        this.Container = mDrawerLayout;
        this.IsStart = false;

        setupView();
        setupEvent();
    }


    private void setupView(){

        Timer = (UChronometer) Container.findViewById(R.id.Timer);

        HpControl = (ToggleButton) Container.findViewById(R.id.HpControl);

        StopHp = (ImageView) Container.findViewById(R.id.HpStop);

        Timer.setTextSize(30);
        Timer.setPlayPauseAlphaAnimation(true);
        Timer.setTypeFace(Chronometer.getTypeface_N_GAGE(mContext));

    }

    private void setupEvent(){
        HpControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Timer.pause();

                    if (mListener!=null)
                        mListener.OnHpPauseClick();
                }else{

                    Timer.start();

                    if (IsStart){
                        if (mListener!=null)
                            mListener.OnHpResumeClick();
                    }else{
                        if (mListener!=null)
                            mListener.OnHpStartClick();
                    }


                }
            }
        });

        StopHp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsStart = false;
                Timer.reset();
                HpControl.setChecked(true);

                if (mListener!=null)
                    mListener.OnHpStopClick();
            }
        });
    }

    public void startHp(){
        IsStart = true;
        Timer.reset();
        Timer.start();

    }

    public void setListener(OnHpControlerListener mListener) {
        this.mListener = mListener;
    }
}
