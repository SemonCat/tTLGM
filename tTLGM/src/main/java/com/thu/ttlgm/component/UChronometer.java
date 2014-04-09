package com.thu.ttlgm.component;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yvelabs.chronometer2.utils.AnimationUtils;
import com.yvelabs.chronometer2.utils.DateUtils;
import com.yvelabs.chronometer2.utils.FontUtils;
/**
 * Created by SemonCat on 2014/4/10.
 */
public class UChronometer extends LinearLayout {

    public static final String START = "START";
    public static final String PAUSE = "PAUSE";
    public static final String STOP = "STOP";
    private static final int TICK_WHAT = 2;

    private Context context;
    private OnChronometerTickListener mOnChronometerTickListener;

    private TextView minutesTV;
    private TextView secondsTV;
    private TextView colonTV2;

    private long startTime;
    private long stopTime;
    private boolean isPlayPauseAlphaAnimation = false; //暂停时, 是否播放动画
    private String state = this.STOP;
    private boolean mStarted;
    private long pauseDuringTime = 0;

    public UChronometer(Context context) {
        super(context);
        this.context = context;
        init();
    }
    public UChronometer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }
    public UChronometer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    //A callback that notifies when the chronometer has incremented on its own.
    public interface OnChronometerTickListener {
        void onChronometerTick(UChronometer chronometer);
    }
    void dispatchChronometerTick() {
        if (mOnChronometerTickListener != null) {
            mOnChronometerTickListener.onChronometerTick(this);
        }
    }
    public void setOnChronometerTickListener(OnChronometerTickListener listener) {
        mOnChronometerTickListener = listener;
    }
    public OnChronometerTickListener getOnChronometerTickListener() {
        return mOnChronometerTickListener;
    }

    /**
     * initialization
     */
    private void init () {
        setOrientation(HORIZONTAL);

        minutesTV = new TextView(context);
        secondsTV = new TextView(context);
        colonTV2 = new TextView(context);

        addView(minutesTV);
        addView(colonTV2);
        addView(secondsTV);

        minutesTV.setText("00");
        secondsTV.setText("00");
        colonTV2.setText(":");

        minutesTV.setSingleLine();
        secondsTV.setSingleLine();
    }

    public void setTextSize (float size) {
        minutesTV.setTextSize(size);
        colonTV2.setTextSize(size);
        secondsTV.setTextSize(size);
    }

    public void setTypeFace (Typeface typeFace) {
        minutesTV.setTypeface(typeFace);
        colonTV2.setTypeface(typeFace);
        secondsTV.setTypeface(typeFace);
    }

    public void setTextColor (int color) {
        minutesTV.setTextColor(color);
        colonTV2.setTextColor(color);
        secondsTV.setTextColor(color);
    }

    public void setTextColor (ColorStateList color) {
        minutesTV.setTextColor(color);
        colonTV2.setTextColor(color);
        secondsTV.setTextColor(color);
    }

    public void setTextBold (boolean flag) {
        minutesTV.getPaint().setFakeBoldText(flag);
        colonTV2.getPaint().setFakeBoldText(flag);
        secondsTV.getPaint().setFakeBoldText(flag);
    }

    public String getState () {
        return state;
    }

    public boolean isPlayPauseAlphaAnimation() {
        return isPlayPauseAlphaAnimation;
    }
    public void setPlayPauseAlphaAnimation(boolean isPlayPauseAlphaAnimation) {
        this.isPlayPauseAlphaAnimation = isPlayPauseAlphaAnimation;
    }

    public void setStartingTime (long time) {
        pauseDuringTime = time;
    }

    private String hoursValidate (long hours) {
        if (hours <= 0)
            return "00";

        if (hours < 10) {
            return "0" + hours;
        }

        return String.valueOf(hours);
    }

    private String minutesValidate (long minutes) {
        if (minutes <= 0) {
            return "00";
        }

        if (minutes < 10) {
            return "0" + minutes;
        }

        return String.valueOf(minutes);
    }

    private String secondsValidate (long seconds) {
        if (seconds <= 0) {
            return "00";
        }

        if (seconds < 10) {
            return "0" + seconds;
        }

        return String.valueOf(seconds);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        stop();
    }

    public static Typeface getTypeface(Context context) {
        return FontUtils.getTypeface(context, FontUtils.N_GAGE);
    }

    public static Typeface getTypeface_FONT_BREAKDOWN (Context context) {
        return FontUtils.getTypeface(context, FontUtils.FONT_BREAKDOWN);
    }

    public static Typeface getTypeface_FONT_DUPLEX (Context context) {
        return FontUtils.getTypeface(context, FontUtils.FONT_DUPLEX);
    }

    public static Typeface getTypeface_SQUID_REGULAR (Context context) {
        return FontUtils.getTypeface(context, FontUtils.SQUID_REGULAR);
    }

    public static Typeface getTypeface_SQUID_SMALL_CAPS (Context context) {
        return FontUtils.getTypeface(context, FontUtils.SQUID_SMALL_CAPS);
    }

    public static Typeface getTypeface_VTKS_UNTITLED (Context context) {
        return FontUtils.getTypeface(context, FontUtils.VTKS_UNTITLED);
    }

    public static Typeface getTypeface_N_GAGE (Context context) {
        return FontUtils.getTypeface(context, FontUtils.N_GAGE);
    }

    public long duringTime() {
        if (startTime == 0 && stopTime == 0)
            return 0;

        if (startTime > 0 && stopTime == 0)
            return System.currentTimeMillis() - startTime;

        if (startTime > 0 && stopTime > 0)
            return stopTime - startTime;

        return 0;
    }

    private void playPauseAnimation (boolean flag) {
        if (flag == true) {
            new AnimationUtils().getPauseAlpha(minutesTV);
            new AnimationUtils().getPauseAlpha(secondsTV);
        }
    }

    private void clearPauseAnimation(boolean flag) {
        if (flag == true) {
            minutesTV.clearAnimation();
            secondsTV.clearAnimation();
        }
    }


    public void reset() {
        startTime = 0;
        pauseDuringTime = 0;
        stopTime = 0;
        mStarted = false;

// 显示归零
        mHandler.removeMessages(TICK_WHAT);
        updateText(startTime);

// 停止动画
        clearPauseAnimation(isPlayPauseAlphaAnimation);

        state = this.STOP;
    }

    public void start () {
        if (mStarted == false) {
            startTime = System.currentTimeMillis() - pauseDuringTime;
            pauseDuringTime = 0;
            stopTime = 0;
            mStarted = true;

            updateText(System.currentTimeMillis());
            dispatchChronometerTick();
            mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK_WHAT), 1000);

//停止动画
            clearPauseAnimation(isPlayPauseAlphaAnimation);

            state = this.START;
        }
    }

    public void pause() {
        if (mStarted == true) {
            stop();
            pauseDuringTime = duringTime();

// 开始动画
            playPauseAnimation(isPlayPauseAlphaAnimation);

            state = this.PAUSE;
        }
    }

    public void stop() {
        pauseDuringTime = 0;

// 停止动画
        clearPauseAnimation(isPlayPauseAlphaAnimation);

        if (mStarted == true) {
            stopTime = System.currentTimeMillis();
            mStarted = false;
            mHandler.removeMessages(TICK_WHAT);
            dispatchChronometerTick();
            updateText(stopTime);
            state = this.STOP;
        }
    }


    private synchronized void updateText(long now) {
        long currentTime = now - startTime;

        minutesTV.setText(minutesValidate(DateUtils.getMinutes(currentTime)));
        secondsTV.setText(secondsValidate(DateUtils.getSeconds(currentTime)));
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
            if (mStarted) {
                updateText(System.currentTimeMillis());
                dispatchChronometerTick();
                mHandler.sendMessageDelayed(Message.obtain(this, TICK_WHAT), 1000);
            }
        }
    };



}
