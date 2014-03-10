package com.thu.ttlgm.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thu.ttlgm.R;
import com.thu.ttlgm.adapter.StudentGroupAdapter;
import com.thu.ttlgm.adapter.StudentGroupStep2Adapter;
import com.thu.ttlgm.bean.Student;
import com.thu.ttlgm.bean.StudentColor;
import com.thu.ttlgm.component.FancyCoverFlow.FancyCoverFlow;
import com.thu.ttlgm.service.SQService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by SemonCat on 2014/3/7.
 */
public class GroupGameStep2Fragment extends BaseFragment{

    private static final String TAG = GroupGameStep2Fragment.class.getName();

    private FancyCoverFlow mFancyCoverFlow;
    private FancyCoverFlow mFancyCoverFlow2;

    private StudentGroupStep2Adapter mStudentGroupAdapter;
    private StudentGroupStep2Adapter mStudentGroupAdapter2;

    private ScheduledExecutorService scheduledExecutorService;

    //切換間隔，單位秒
    private final static int updateinterval = 5;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mFancyCoverFlow.ScrollToRight();
            mFancyCoverFlow2.ScrollToLeft();
        }
    };


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        SQService.getCacheStudentList(new SQService.OnStudentGetListener() {
            @Override
            public void OnStudentGetEvent(final List<Student> mStudentList) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SQService.findAllColor(mStudentList,new SQService.OnStudentColorGetListener() {
                            @Override
                            public void OnStudentColorGetEvent(HashSet<StudentColor> mStudentList) {
                                mStudentGroupAdapter.setColor(mStudentList);
                                mStudentGroupAdapter2.setColor(mStudentList);
                            }
                        });
                    }
                }).start();
            }
        });



    }

    @Override
    protected void setupView() {
        mFancyCoverFlow = (FancyCoverFlow) getActivity().findViewById(R.id.fancyCoverFlowTop);
        mStudentGroupAdapter = new StudentGroupStep2Adapter(getActivity(), new ArrayList<Student>());
        mFancyCoverFlow.setAdapter(mStudentGroupAdapter);
        mFancyCoverFlow.setSelection(Integer.MAX_VALUE / 2);


        mFancyCoverFlow2 = (FancyCoverFlow) getActivity().findViewById(R.id.fancyCoverFlowDown);
        mStudentGroupAdapter2 = new StudentGroupStep2Adapter(getActivity(), new ArrayList<Student>());
        mFancyCoverFlow2.setAdapter(mStudentGroupAdapter2);
        mFancyCoverFlow2.setSelection(Integer.MAX_VALUE / 2);

        getStudentDataFromServer();

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


                Message mMessage = mHandler.obtainMessage();

                mHandler.sendMessage(mMessage);

                getStudentDataFromServer();
            }
        }

    }


    public void Finish(View mView) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_groupgame_step2, container, false);
    }

    private boolean IsFirstLoad = true;

    private void getStudentDataFromServer() {
        SQService.getAllStudents(new SQService.OnAllStudentGetListener() {
            @Override
            public void OnAllStudentGetEvent(List<Student> mStudentList) {
                synchronized (mFancyCoverFlow) {

                    try {
                        List<Student> list1 = mStudentList.subList(0, mStudentList.size() / 2);
                        List<Student> list2 = mStudentList.subList((mStudentList.size() / 2) + 1, mStudentList.size());

                        if (IsFirstLoad) {
                            IsFirstLoad = false;
                            mStudentGroupAdapter.refreshOnUiThreadAndSetInCenter(list1, mFancyCoverFlow);
                            mStudentGroupAdapter2.refreshOnUiThreadAndSetInCenter(list2, mFancyCoverFlow2);
                            return;
                        }

                        mStudentGroupAdapter.refreshOnUiThread(list1);
                        mStudentGroupAdapter2.refreshOnUiThread(list2);
                    } catch (IllegalArgumentException mIllegalArgumentException) {

                    }
                }
            }
        });
    }

}
