package com.thu.ttlgm;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.thu.ttlgm.component.HpControler;
import com.thu.ttlgm.component.SlidingDrawer;
import com.thu.ttlgm.service.PollHandler;
import com.thu.ttlgm.service.SQService;
import com.thu.ttlgm.utils.ConstantUtil;

/**
 * Created by SemonCat on 2014/1/11.
 */
public class BaseActivity extends Activity implements PollHandler.OnMessageReceive, HpControler.OnHpControlerListener {

    public interface OnGlobalTouchEvent{
        void OnTouch(MotionEvent motionEvent);
    }

    private Handler mHideHandler = new Handler();

    private SlidingDrawer mDrawer;

    private PollHandler mPollHandler;

    private static final int StartFragment = 0x123123;

    protected HpControler mHpControler;

    private OnGlobalTouchEvent mOnTouchListenerList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setupLayout());


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        setupView();
        setupEvent();
        //addSlidingDrawer();
        setupPollHandler();

    }

    protected int setupLayout() {
        return 0;
    }

    protected void setupView() {

    }

    protected void setupEvent() {

    }

    private void setupPollHandler() {
        mPollHandler = new PollHandler();
        mPollHandler.setListener(this);
        mPollHandler.start();
    }

    public void setPollHandlerListener(PollHandler.OnMessageReceive mListener) {
        if (mPollHandler != null) {
            mPollHandler.setListener(mListener);
        }
    }

    private void addSlidingDrawer() {
        ViewGroup mRootView = (ViewGroup) getWindow().
                getDecorView().findViewById(android.R.id.content);

        getLayoutInflater().inflate(R.layout.sliding_drawer,
                mRootView);
        mDrawer = (SlidingDrawer) findViewById(R.id.drawer);
        final View HandleView = mDrawer.getHandle();


        final View DownIcon = HandleView.findViewById(R.id.DownIcon);
        mDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                HandleView.setBackgroundColor(getResources().getColor(R.color.DrawerContentBackground));
                DownIcon.setVisibility(View.VISIBLE);
            }
        });


        mDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                HandleView.setBackgroundColor(Color.TRANSPARENT);
                DownIcon.setVisibility(View.GONE);
            }
        });


        setupHpControl();
    }

    protected void setupHpControl() {
        mHpControler = new HpControler(this, mDrawer);
        mHpControler.setListener(this);
    }

    public void HideDrawer() {
        if (mDrawer != null && mDrawer.isOpened()) {
            mDrawer.animateClose();
        }
    }

    public void ShowDrawer() {
        if (mDrawer != null && !mDrawer.isOpened()) {
            mDrawer.animateOpen();
        }
    }

    public void replaceFragment(final Fragment mFragment, final String TAG, boolean AddToBackStack) {

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        Fragment findFragment = getFragmentManager().findFragmentByTag(TAG);
        if (findFragment != null) {

            //return;
        }
        //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.setCustomAnimations(R.anim.slide_in_right,R.anim.zoom_out);
        transaction.replace(R.id.Fragment_Content, mFragment, TAG);


        if (AddToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commitAllowingStateLoss();


    }


    public void replaceFragment(final Fragment mFragment, final String TAG) {

        replaceFragment(mFragment, TAG, false);

    }

    public void addFragment(Fragment mFragment, String TAG) {

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        Fragment findFragment = getFragmentManager().findFragmentByTag(TAG);
        if (findFragment != null) {
            HideDrawer();
            return;
        }


        transaction.add(R.id.Fragment_Content, mFragment, TAG);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        transaction.commitAllowingStateLoss();

    }

    public void clearFragmentBackStack() {
        int counter = getFragmentManager().getBackStackEntryCount();

        for (int i = 0; i < counter; i++) {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    private void hideSystemUi() {

        getWindow().getDecorView().setSystemUiVisibility(

                View.SYSTEM_UI_FLAG_LAYOUT_STABLE

                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

                        | View.SYSTEM_UI_FLAG_FULLSCREEN

                        | View.SYSTEM_UI_FLAG_IMMERSIVE
        );

    }

    @Override
    public void OnMissionResultReceive(String quizid, String taskid, String groupid, String Answer) {
        Log.d("Mission", "ReceiveMessage,Quiz:" + quizid + ",Task:" + taskid + ",GroupId:" + groupid + ",Ans:" + Answer);
    }

    @Override
    public void OnHpLow(String sid, int blood) {

    }

    @Override
    public void getAdditional(String sid) {

    }

    @Override
    public void getWhiteBoardImage(String URL) {

    }

    long exitTime = 0;

    @Override
    public void onBackPressed() {

        if (this.isTaskRoot()) {
            if ((System.currentTimeMillis() - exitTime) < 2000) {
                super.onBackPressed();
            } else {
                Toast.makeText(getApplicationContext(), "再按一次以退出", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }

        } else {
            super.onBackPressed();
        }

    }

    public void startHpServer() {
        mHpControler.startHp();
        SQService.startHpServer(ConstantUtil.HpInterval);
    }

    public void stopHpServer() {
        SQService.pauseHpServer();
    }

    public void pauseHpServer() {
        SQService.pauseHpServer();
    }

    public void resumeHpServer() {
        SQService.resumeHpServer();
    }

    @Override
    public void OnHpStartClick() {
        Log.d("Hp", "OnHpStartClick");
        SQService.startHpServer(ConstantUtil.HpInterval);
    }

    @Override
    public void OnHpPauseClick() {
        Log.d("Hp", "OnHpPauseClick");
        pauseHpServer();
    }

    @Override
    public void OnHpStopClick() {
        Log.d("Hp", "OnHpStopClick");
        stopHpServer();
    }

    @Override
    public void OnHpResumeClick() {
        Log.d("Hp", "OnHpResumeClick");
        resumeHpServer();
    }


    public void setDrawerEnable(boolean Enable) {
        if (Enable) {
            mDrawer.setVisibility(View.VISIBLE);
        } else {
            mDrawer.setVisibility(View.GONE);
        }
    }



    public void setGlobalTouchEventListener(OnGlobalTouchEvent mListener){

        mOnTouchListenerList = mListener;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (mOnTouchListenerList!=null){
            mOnTouchListenerList.OnTouch(ev);
        }

        return super.dispatchTouchEvent(ev);
    }
}
