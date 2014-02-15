package com.thu.ttlgm.floatwindows;

import android.content.Context;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.SpringUtil;

/**
 * Created by SemonCat on 2014/2/14.
 */
public class FloatImageView extends FrameLayout{

    private static final String TAG = FloatImageView.class.getName();

    private WindowManager windowManager;
    private WindowManager.LayoutParams paramsF;
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;

    private int screenWidth;
    private int screenHeight;

    private final int mClickEventDistance = dip2px(10);
    private ImageView mImageView;

    private BaseSpringSystem mBaseSpringSystem;
    private Spring mScaleSpring;
    private Spring mTranslationLeftSpring;
    private Spring mTranslationRightSpring;


    public FloatImageView(Context context,WindowManager windowManager,WindowManager.LayoutParams paramsF) {
        super(context);
        this.paramsF = paramsF;
        this.windowManager = windowManager;

        initScreenValue();

        setupView();
        setupRebound();

    }

    private void initScreenValue(){
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;
    }

    private void setupView(){
        mImageView = new ImageView(getContext());

        addView(mImageView);
    }

    private void setupRebound(){
        mBaseSpringSystem = SpringSystem.create();
        mScaleSpring = mBaseSpringSystem.createSpring()
                .addListener(new SimpleSpringListener() {
                    @Override
                    public void onSpringUpdate(Spring spring) {
                        float scale = (float) SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, 1, 0.7);
                        mImageView.setScaleX(scale);
                        mImageView.setScaleY(scale);

                    }
                });

        mTranslationLeftSpring = mBaseSpringSystem.createSpring().
                addListener(new SimpleSpringListener(){
                    @Override
                    public void onSpringUpdate(Spring spring) {
                        float translationX = (float) SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, paramsF.x, 0);
                        updateWindowLayout((int)translationX,paramsF.y);

                    }

                    @Override
                    public void onSpringAtRest(Spring spring) {
                        mTranslationLeftSpring.setEndValue(0);
                    }
                });

        mTranslationRightSpring = mBaseSpringSystem.createSpring().
                addListener(new SimpleSpringListener(){
                    @Override
                    public void onSpringUpdate(Spring spring) {
                        float translationX = (float) SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, paramsF.x, screenWidth-getWidth());
                        updateWindowLayout((int)translationX,paramsF.y);

                    }

                    @Override
                    public void onSpringAtRest(Spring spring) {
                        mTranslationRightSpring.setEndValue(0);
                    }
                });
    }

    public void setImageResource(int id){
        mImageView.setImageResource(id);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        initScreenValue();
        super.onLayout(changed, left, top, right, bottom);
    }

    private int lastX,lastY,right,left,top,bottom;
    private float x1,y1,x2,y2;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //開始縮放動畫
                mScaleSpring.setEndValue(1);
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                x1 =  event.getRawX();//得到相對應屏幕左上角的坐標
                y1 =  event.getRawY();
                // Get current time in nano seconds.

                initialX = paramsF.x;
                initialY = paramsF.y;
                initialTouchX = event.getRawX();
                initialTouchY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getRawX();
                y2 = event.getRawY();

                //結束縮放動畫
                mScaleSpring.setEndValue(0);

                //判斷彈回動畫
                if (paramsF.x<screenWidth/2)
                    mTranslationLeftSpring.setEndValue(1);
                else{
                    mTranslationRightSpring.setEndValue(1);
                }

                //Log.i("i", x1 + ",,," + y1 + ",,," + x2 + ",,," + y2);
                double distance = Math.sqrt(Math.abs(x1-x2)*Math.abs(x1-x2)+Math.abs(y1-y2)*Math.abs(y1-y2));//兩點之間的距離
                //Log.i("i", "x1 - x2>>>>>>"+ distance);
                if (distance < mClickEventDistance)
                    performClick();
                break;
            case MotionEvent.ACTION_MOVE:

                int moveX = initialX + (int) (event.getRawX() - initialTouchX);
                int moveY = initialY + (int) (event.getRawY() - initialTouchY);
                updateWindowLayout(moveX,moveY);

                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;

                left = getLeft() + dx;
                top = getTop() + dy;
                right = getRight() + dx;
                bottom = getBottom() + dy;
                if (left < 0) {
                    left = 0;
                    right = left + getWidth();
                }
                if (right > screenWidth) {
                    right = screenWidth;
                    left = right - getWidth();
                }
                if (top < 0) {
                    top = 0;
                    bottom = top + getHeight();
                }
                if (bottom > screenHeight) {
                    bottom = screenHeight;
                    top = bottom - getHeight();
                }
                //Log.i("i", "值：" + left +">>>"+ top+">>>"+right+">>>"+bottom);
                layout(left, top, right, bottom);
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();

                break;

            case MotionEvent.ACTION_CANCEL:
                mScaleSpring.setEndValue(0);
                break;
        }
        //super.onTouchEvent(event);
        return true;
    }

    private void updateWindowLayout(int x,int y){
        paramsF.x = x;
        paramsF.y = y;
        windowManager.updateViewLayout(this, paramsF);
    }

    private int dip2px(float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
