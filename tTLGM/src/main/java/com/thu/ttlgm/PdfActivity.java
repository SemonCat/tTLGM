package com.thu.ttlgm;

import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.joanzapata.pdfview.PDFView;
import com.thu.ttlgm.utils.ConstantUtil;

import java.io.File;

/**
 * Created by SemonCat on 2014/2/3.
 */
public class PdfActivity extends BasePlayActivity{

    private static final String TAG = PdfActivity.class.getName();

    private PDFView mPDFView;

    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

    public static final String PDFPathString = "pdfPath";

    private GestureDetector mGestureDetector;

    private GestureDetector.OnGestureListener mListener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setupGesture();
        setupPDFView();
    }

    private int ScreenWidth;
    private void setupGesture(){
        mGestureDetector = new GestureDetector(this,mListener);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        ScreenWidth = metrics.widthPixels;

    }

    @Override
    protected void setupView() {
        mPDFView = (PDFView)findViewById(R.id.PdfView);

    }

    private void setupPDFView(){
        String Path = getIntent().getStringExtra(PDFPathString);
        File mFile = new File(Path);
        if (mFile.exists()){

            mPDFView.fromFile(mFile)
                    .defaultPage(0)
                    .enableSwipe(true)
                    .load();

            /*
            mPDFView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return mGestureDetector.onTouchEvent(event);
                }
            });
            */
        }else{
            finish();
            Log.d(TAG,"File==null");
        }

    }

    @Override
    protected int setupLayout() {
        return R.layout.activity_pdf;
    }

    public void onSwipeRight() {
        Log.d(TAG,"onSwipeRight");
    }

    public void onSwipeLeft() {
        Log.d(TAG,"onSwipeLeft");

    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }

    /*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }
    */
}
