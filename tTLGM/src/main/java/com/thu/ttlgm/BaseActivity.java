package com.thu.ttlgm;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.thu.ttlgm.component.SlidingDrawer;
import com.thu.ttlgm.fragment.ClassChooserFragment;
import com.thu.ttlgm.fragment.GameFragment;
import com.thu.ttlgm.fragment.GroupGameFragment;
import com.thu.ttlgm.fragment.ResourcePickerFragment;
import com.thu.ttlgm.fragment.StudentsFragment;
import com.thu.ttlgm.service.PollHandler;

/**
 * Created by SemonCat on 2014/1/11.
 */
public class BaseActivity extends Activity implements PollHandler.OnMessageReceive {

    private Handler mHideHandler = new Handler();

    private SlidingDrawer mDrawer;

    private PollHandler mPollHandler;

    private static final int StartFragment = 0x123123;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setupLayout());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setupView();
        setupEvent();
        addSlidingDrawer();
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
    }

    public void HideDrawer() {
        if (mDrawer != null) {
            mDrawer.animateClose();
        }
    }

    public void ShowDrawer() {
        if (mDrawer != null) {
            mDrawer.animateOpen();
        }
    }

    public void toHome(View mView) {
        replaceFragment(new ClassChooserFragment(), ClassChooserFragment.class.getName());
        HideDrawer();
    }

    public void toStudents(View mView) {
        replaceFragment(new StudentsFragment(), StudentsFragment.class.getName());
        HideDrawer();
    }

    public void toWorks(View mView) {
        HideDrawer();
    }

    public void toFiles(View mView) {
        replaceFragment(new ResourcePickerFragment(), ResourcePickerFragment.class.getName());
        HideDrawer();
    }

    public void toGroupGame(View mView) {
        replaceFragment(new GroupGameFragment(), GroupGameFragment.class.getName());


        /*
        Intent mIntent = new Intent(this, GroupGameActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(mIntent);
        */

        HideDrawer();
    }

    public void replaceFragment(final Fragment mFragment,final String TAG) {

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment findFragment = getFragmentManager().findFragmentByTag(TAG);
        if (findFragment != null) {
            return;
        }
        transaction.replace(R.id.Fragment_Content, mFragment, TAG);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        transaction.commit();


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

                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

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

        }else{
            super.onBackPressed();
        }
    }
}
