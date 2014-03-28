package com.thu.ttlgm.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.nhaarman.listviewanimations.itemmanipulation.AnimateAdditionAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.AnimateDismissAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.thu.ttlgm.BaseActivity;
import com.thu.ttlgm.R;
import com.thu.ttlgm.adapter.AlertAdapter;
import com.thu.ttlgm.bean.Student;
import com.thu.ttlgm.service.PollHandler;
import com.thu.ttlgm.service.SQService;

import java.util.List;

/**
 * Created by SemonCat on 2014/3/5.
 */
public class PlayFragment extends BaseFragment implements PollHandler.OnMessageReceive{

    private static final String TAG = PlayFragment.class.getName();


    private RelativeLayout container;

    private ListView mAlertListView;
    private Handler mHandler;

    private AlertAdapter mAdapter;
    private AnimateAdditionAdapter animateAdditionAdapter;
    private AnimateDismissAdapter animateDismissAdapter;



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mHandler = new Handler();
        setupAlert();
        setupPollHandler();
    }

    private void setupAlert(){
        container = (RelativeLayout) getActivity().findViewById(R.id.container);
        LayoutInflater  inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mAlertListView = (ListView) inflater.inflate(R.layout.layout_alert, null);

        mAlertListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                OnAlertTouch(event);
                return false;
            }
        });

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(dip2px(60), ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        container.addView(mAlertListView,layoutParams);



        //SetupAdapter

        mAdapter = new AlertAdapter(getActivity());

        mAdapter.setListener(new AlertAdapter.OnDeleteListener() {
            @Override
            public void OnDeleteEvent() {
                animateDismissAdapter.animateDismiss(mAdapter.getCount()-1);
            }
        });


        animateDismissAdapter = new AnimateDismissAdapter(mAdapter,new OnDismissCallback() {
            @Override
            public void onDismiss(AbsListView listView, int[] reverseSortedPositions) {
                mAdapter.removeTop();
            }
        });

        animateAdditionAdapter = new AnimateAdditionAdapter(animateDismissAdapter);

        animateAdditionAdapter.setListView(mAlertListView);

        mAlertListView.setAdapter(animateAdditionAdapter);


    }

    private void setupPollHandler(){
        ((BaseActivity)getActivity()).setPollHandlerListener(this);
    }


    @Override
    public void OnMissionResultReceive(String quizid, String taskid, String groupid, String Answer) {

    }

    @Override
    public void OnHpLow(String sid, int blood) {

    }

    @Override
    public void getAdditional(final String sid) {
        SQService.getCacheStudentList(new SQService.OnStudentGetListener() {
            @Override
            public void OnStudentGetEvent(List<Student> mStudentList) {
                final Student student = SQService.findStudentBySID(mStudentList,sid);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (student != null) {
                            Log.d(TAG, "StudentURL:" + student.getImageUrl());

                            try{
                                animateAdditionAdapter.insert(0,student);
                            }catch (NullPointerException NPE){
                                mAdapter.addStudent(student);
                            }
                        }
                    }
                });


            }
        });
    }

    @Override
    public void getWhiteBoardImage(String URL) {

    }

    public void OnAlertTouch(MotionEvent event){

    }

    public int dip2px(float dipValue) {
        final float scale = getActivity().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
