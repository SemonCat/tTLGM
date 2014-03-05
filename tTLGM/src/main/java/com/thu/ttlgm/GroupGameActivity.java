package com.thu.ttlgm;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.thu.ttlgm.adapter.StudentGroupAdapter;
import com.thu.ttlgm.bean.Student;
import com.thu.ttlgm.component.FancyCoverFlow.FancyCoverFlow;
import com.thu.ttlgm.service.SQService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by SemonCat on 2014/2/9.
 */
public class GroupGameActivity extends BaseActivity{

    private static final String TAG = GroupGameActivity.class.getName();

    private FancyCoverFlow mFancyCoverFlow;
    private FancyCoverFlow mFancyCoverFlow2;

    private StudentGroupAdapter mStudentGroupAdapter;
    private StudentGroupAdapter mStudentGroupAdapter2;

    private ScheduledExecutorService scheduledExecutorService;

    //切換間隔，單位秒
    private final static int updateinterval = 1;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mFancyCoverFlow.ScrollToRight();
            mFancyCoverFlow2.ScrollToLeft();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void setupView() {
        mFancyCoverFlow = (FancyCoverFlow) findViewById(R.id.fancyCoverFlow);
        mStudentGroupAdapter = new StudentGroupAdapter(this,new ArrayList<Student>());
        mFancyCoverFlow.setAdapter(mStudentGroupAdapter);
        mFancyCoverFlow.setSelection(Integer.MAX_VALUE / 2);
        getStudentDataFromServer();
        /*
        mFancyCoverFlow2  = (FancyCoverFlow) findViewById(R.id.fancyCoverFlow2);
        mStudentGroupAdapter2 = new StudentGroupAdapter();
        mFancyCoverFlow2.setAdapter(mStudentGroupAdapter2);
        mFancyCoverFlow2.setSelection(Integer.MAX_VALUE / 2);
        */
    }

    @Override
    protected void setupEvent() {

    }


    @Override
    public void onResume() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 當Activity顯示出來後，每N秒鐘切換一次圖片顯示
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), updateinterval, updateinterval, TimeUnit.SECONDS);

        super.onResume();
    }

    @Override
    public void onPause() {
        scheduledExecutorService.shutdown();
        super.onPause();
    }

    private class ScrollTask implements Runnable {

        public void run() {
            synchronized (mFancyCoverFlow) {

                /*
                Message mMessage = mHandler.obtainMessage();

                mHandler.sendMessage(mMessage);
                */
                getStudentDataFromServer();
            }
        }

    }


    public void Finish(View mView){

    }

    @Override
    protected int setupLayout() {
        return R.layout.activity_groupgame;
    }

    private void getStudentDataFromServer(){
        SQService.getAllStudents(new SQService.OnAllStudentGetListener() {
            @Override
            public void OnAllStudentGetEvent(List<Student> mStudentList) {
                mStudentGroupAdapter.refreshOnUiThread(mStudentList);
            }
        });
    }
}
