package com.thu.ttlgm.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.thu.ttlgm.R;
import com.thu.ttlgm.adapter.StudentAdapter;
import com.thu.ttlgm.bean.Student;
import com.thu.ttlgm.bean.Subject;
import com.thu.ttlgm.service.SQService;
import com.thu.ttlgm.utils.ConstantUtil;
import com.thu.ttlgm.utils.DataParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by SemonCat on 2014/1/15.
 */
public class StudentsFragment extends BaseFragment implements View.OnClickListener{
    private static final String TAG =StudentsFragment.class.getName();

    private TextView SortByID;
    private TextView SortByBlood;
    private TextView SortByMoney;

    private CheckBox ShowUnLogin;
    private CheckBox ShowBlood;
    private PullToRefreshGridView mPullRefreshGridView;
    private GridView mStudentList;
    private StudentAdapter mAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    protected void setupView() {
        mPullRefreshGridView = (PullToRefreshGridView) getActivity().findViewById(R.id.studentList);
        mStudentList = mPullRefreshGridView.getRefreshableView();
        // Set a listener to be invoked when the list should be refreshed.
        mPullRefreshGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                getStudentDataFromServer();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {

            }


        });

        PauseOnScrollListener mPauseOnScrollListener = new PauseOnScrollListener(ImageLoader.getInstance(), true,true);
        mStudentList.setOnScrollListener(mPauseOnScrollListener);

        mStudentList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String Sid = mAdapter.getItem(position).getID();
                SQService.AddStudentCoin(Sid,ConstantUtil.TeacherAddCoin);
                Toast.makeText(getActivity(),"加錢成功！",Toast.LENGTH_SHORT).show();


                SQService.getAllStudents(
                        new SQService.OnAllStudentGetListener() {
                    @Override
                    public void OnAllStudentGetEvent
                            (List<Student> mStudentList) {

                        mAdapter.refreshOnUiThread(mStudentList);
                    }
                });
                return false;
            }
        });


        SortByID = (TextView) getActivity().findViewById(R.id.SortByID);
        SortByBlood = (TextView) getActivity().findViewById(R.id.SortByBlood);
        SortByMoney = (TextView) getActivity().findViewById(R.id.SortByMoney);
        ShowUnLogin = (CheckBox) getActivity().findViewById(R.id.ShowUnLogin);

        ShowBlood = (CheckBox) getActivity().findViewById(R.id.ShowBlood);
    }

    @Override
    protected void setupAdapter() {
        Subject mSubject = getClassData();
        List<Student> mStudents = new ArrayList<Student>();
        if (mSubject!=null)
            mStudents = mSubject.getStudents();
        mAdapter = new StudentAdapter(getActivity());
        mAdapter.Sort(StudentAdapter.SortType.ID_DESC);

        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(mAdapter);
        swingBottomInAnimationAdapter.setAbsListView(mStudentList);
        swingBottomInAnimationAdapter.setInitialDelayMillis(300);

        mStudentList.setAdapter(swingBottomInAnimationAdapter);
        getStudentDataFromServer();
    }

    @Override
    protected void setupEvent() {
        SortByID.setOnClickListener(this);
        SortByBlood.setOnClickListener(this);
        SortByMoney.setOnClickListener(this);
        ShowUnLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mAdapter.OnlyShowLogin(!isChecked);

            }
        });

        ShowBlood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mAdapter.setShowBlood(isChecked);
            }
        });
    }

    private boolean ID_DESC = true;
    private boolean BLOOD_DESC = true;
    private boolean Money_DESC = true;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.SortByID:
                if (ID_DESC)
                    mAdapter.Sort(StudentAdapter.SortType.ID_DESC);
                else
                    mAdapter.Sort(StudentAdapter.SortType.ID_ASC);

                ID_DESC = !ID_DESC;
                break;

            case R.id.SortByBlood:
                if (BLOOD_DESC)
                    mAdapter.Sort(StudentAdapter.SortType.Blood_DESC);
                else
                    mAdapter.Sort(StudentAdapter.SortType.Blood_ASC);

                BLOOD_DESC = !BLOOD_DESC;
                break;

            case R.id.SortByMoney:
                if (Money_DESC)
                    mAdapter.Sort(StudentAdapter.SortType.Money_DESC);
                else
                    mAdapter.Sort(StudentAdapter.SortType.Money_ASC);

                Money_DESC = !Money_DESC;
                break;
        }
    }

    private Subject getClassData(){

        File File = new File(ConstantUtil.SubjectDataPath);

        return DataParser.SubjectParser(getActivity(),File);
    }

    private void getStudentDataFromServer(){
        SQService.getAllStudents(new SQService.OnAllStudentGetListener() {
            @Override
            public void OnAllStudentGetEvent(List<Student> mStudentList) {
                mPullRefreshGridView.onRefreshComplete();
                mAdapter.refreshOnUiThread(mStudentList);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_studnets, container, false);
    }

}
