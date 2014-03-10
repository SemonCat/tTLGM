package com.thu.ttlgm.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.thu.ttlgm.R;
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
 * Created by SemonCat on 2014/3/5.
 */
public class GroupGameFragment extends BaseFragment{

    private static final String TAG = GroupGameFragment.class.getName();

    private FancyCoverFlow mFancyCoverFlow;
    private FancyCoverFlow mFancyCoverFlow2;

    private StudentGroupAdapter mStudentGroupAdapter;
    private StudentGroupAdapter mStudentGroupAdapter2;

    private ScheduledExecutorService scheduledExecutorService;

    //切換間隔，單位秒
    private final static int updateinterval = 3;

    private boolean IsFirstLoad = true;

    private ImageView Finish;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mFancyCoverFlow.ScrollToRight();
            mFancyCoverFlow2.ScrollToLeft();
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void setupView() {
        mFancyCoverFlow = (FancyCoverFlow) getActivity().findViewById(R.id.fancyCoverFlow);
        mStudentGroupAdapter = new StudentGroupAdapter(getActivity(),new ArrayList<Student>());
        mFancyCoverFlow.setAdapter(mStudentGroupAdapter);
        mFancyCoverFlow.setSelection(Integer.MAX_VALUE / 2);

        getStudentDataFromServer();
        /*
        mFancyCoverFlow2  = (FancyCoverFlow) findViewById(R.id.fancyCoverFlow2);
        mStudentGroupAdapter2 = new StudentGroupAdapter();
        mFancyCoverFlow2.setAdapter(mStudentGroupAdapter2);
        mFancyCoverFlow2.setSelection(Integer.MAX_VALUE / 2);
        */

        Finish = (ImageView) getActivity().findViewById(R.id.Finish);
        Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getFragmentManager().beginTransaction().replace(R.id.Fragment_Content,new GroupGameStep2Fragment()).commit();
                getActivity().getFragmentManager().beginTransaction().remove(GroupGameFragment.this).commit();
            }
        });
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_groupgame, container, false);
    }

    private void getStudentDataFromServer(){
        SQService.getAllStudents(new SQService.OnAllStudentGetListener() {
            @Override
            public void OnAllStudentGetEvent(List<Student> mStudentList) {
                synchronized (mFancyCoverFlow) {
                    try{
                        if (IsFirstLoad){
                            IsFirstLoad = false;
                            mStudentGroupAdapter.refreshOnUiThreadAndSetInCenter(mStudentList, mFancyCoverFlow);
                            return;
                        }

                        mStudentGroupAdapter.refreshOnUiThread(mStudentList);
                    }catch (IllegalArgumentException mIllegalArgumentException){

                    }
                }


            }
        });
    }
}
