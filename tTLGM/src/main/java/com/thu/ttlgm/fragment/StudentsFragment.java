package com.thu.ttlgm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.thu.ttlgm.R;
import com.thu.ttlgm.adapter.StudentAdapter;
import com.thu.ttlgm.bean.Student;
import com.thu.ttlgm.bean.Subject;
import com.thu.ttlgm.utils.ConstantUtil;
import com.thu.ttlgm.utils.DataParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SemonCat on 2014/1/15.
 */
public class StudentsFragment extends BaseFragment implements View.OnClickListener{
    private static final String TAG =StudentsFragment.class.getName();

    private TextView SortByID;
    private TextView SortByBlood;
    private TextView SortByMoney;

    private GridView mStudentList;
    private StudentAdapter mAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    protected void setupView() {
        mStudentList = (GridView) getActivity().findViewById(R.id.studentList);
        SortByID = (TextView) getActivity().findViewById(R.id.SortByID);
        SortByBlood = (TextView) getActivity().findViewById(R.id.SortByBlood);
        SortByMoney = (TextView) getActivity().findViewById(R.id.SortByMoney);
    }

    @Override
    protected void setupAdapter() {
        Subject mSubject = getClassData();
        List<Student> mStudents = new ArrayList<Student>();
        if (mSubject!=null)
            mStudents = mSubject.getStudents();
        mAdapter = new StudentAdapter(getActivity(),mStudents);
        mAdapter.Sort(StudentAdapter.SortType.ID_DESC);
        mStudentList.setAdapter(mAdapter);
    }

    @Override
    protected void setupEvent() {
        SortByID.setOnClickListener(this);
        SortByBlood.setOnClickListener(this);
        SortByMoney.setOnClickListener(this);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_studnets, container, false);
    }
}
