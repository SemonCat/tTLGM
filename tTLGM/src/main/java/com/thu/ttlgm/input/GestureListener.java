package com.thu.ttlgm.input;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by SemonCat on 2014/3/30.
 */
public class GestureListener implements View.OnTouchListener {


    public interface OnGestureEvent{
        void onSwipeTop();
        void onSwipeBottom();
    }

    private OnGestureEvent mListener;

    private final GestureDetector gestureDetector;

    public GestureListener(Context mContext){
        gestureDetector = new GestureDetector(mContext, new UGestureListener());

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {


        return gestureDetector.onTouchEvent(event);
    }

    public boolean onTouch(MotionEvent event) {


        return gestureDetector.onTouchEvent(event);
    }

    private class UGestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_DISTANCE_THRESHOLD = 25;
        private static final int SWIPE_VELOCITY_THRESHOLD = 25;

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                    }
                }
            } catch (Exception exception) {
                //exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {

    }

    public void onSwipeLeft() {

    }

    public void onSwipeTop() {

        if (mListener!=null)
            mListener.onSwipeTop();
    }

    public void onSwipeBottom() {

        if (mListener!=null)
            mListener.onSwipeBottom();
    }


    public void setListener(OnGestureEvent mListener) {
        this.mListener = mListener;
    }
}


