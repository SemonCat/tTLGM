package com.thu.ttlgm.fragment;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.thu.ttlgm.MainActivity;
import com.thu.ttlgm.R;
import com.thu.ttlgm.adapter.UnitIndexPagerAdapter;
import com.thu.ttlgm.bean.*;
import com.thu.ttlgm.bean.Class;
import com.thu.ttlgm.component.JazzyViewPager.JazzyViewPager;
import com.thu.ttlgm.component.UViewPager;
import com.thu.ttlgm.utils.ConstantUtil;
import com.thu.ttlgm.utils.DataParser;
import com.thu.ttlgm.utils.SharedPreferencesUtils;

import java.io.File;
/**
 * Created by SemonCat on 2014/4/14.
 */
public class UnitIndexFragment extends BaseFragment{
    private static final String TAG = ClassChooserFragment.class.getName();


    //ViewPager
    private JazzyViewPager mUViewPager;
    private UnitIndexPagerAdapter mAdapter;
    private int mCurrentItemPosition;

    private FrameLayout ViewPagerContainer;

    private ImageView StartClass;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    private Subject getClassData(){

        File File = new File(ConstantUtil.SubjectDataPath);

        return DataParser.SubjectParser(getActivity(),File);
    }

    @Override
    protected void setupView() {
        mUViewPager = (JazzyViewPager) getActivity().findViewById(R.id.ViewPagerClass);
        ViewPagerContainer = (FrameLayout) getActivity().findViewById(R.id.ViewPagerContainer);

        StartClass = (ImageView) getActivity().findViewById(R.id.StartClass);
    }

    @Override
    protected void setupAdapter() {

        Subject mSubjectData = getClassData();

        if (mSubjectData==null){
            new AlertDialog.Builder(getActivity())
                    .setTitle("找不到課堂資料耶：（")
                    .setMessage("很抱歉！\n使用本軟體需要先匯入課堂資料！")
                    .setNegativeButton("關閉",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().finish();
                        }
                    })
                    .setPositiveButton("重試",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getActivity(),MainActivity.class));

                            getActivity().finish();
                        }
                    })
                    .setCancelable(false)
                    .create()
                    .show();
        }

        mAdapter = new UnitIndexPagerAdapter(getActivity(),mSubjectData);
        mUViewPager.setPageMargin(30);
        mUViewPager.setOffscreenPageLimit(3);
        mUViewPager.setAdapter(mAdapter);


        /*
        mUViewPager.enableCenterLockOfChilds();

        Class mData = ((MainActivity)getActivity()).getCurrentClass();
        if (mData!=null){

            mCurrentItemPosition =
                    mAdapter.getItemPosition(mData);
        }else{
            mCurrentItemPosition = 1;
        }

        mUViewPager.setCurrentItemInCenter(1);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mUViewPager.setCurrentItemInCenter(SharedPreferencesUtils.getWeek(getActivity()));
            }
        });
        */

        Class mData = ((MainActivity)getActivity()).getCurrentClass();
        int prefWeek = SharedPreferencesUtils.getWeek(getActivity());
        if (mData!=null){

            mCurrentItemPosition =
                    mAdapter.getItemPosition(mData);
        }else if (prefWeek < mAdapter.getCount()){
            mCurrentItemPosition = SharedPreferencesUtils.getWeek(getActivity());
        }else{
            mCurrentItemPosition = 0;
        }

        mUViewPager.setCurrentItem(mCurrentItemPosition);
    }


    @Override
    protected void setupEvent(){

        ViewPagerContainer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // dispatch the events to the ViewPager, to solve the problem that we can swipe only the middle view.
                return mUViewPager.dispatchTouchEvent(event);
            }
        });

        ViewPager.OnPageChangeListener mListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                Class mClass = mAdapter.getItem(position);
                MainActivity mainActivity = ((MainActivity)getActivity());
                if (mClass!=null && mainActivity!=null){
                    mainActivity.setCurrentClass(mClass);
                }
                SharedPreferencesUtils.setWeek(getActivity(),position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        mUViewPager.setOnPageChangeListener(mListener);

        mListener.onPageSelected(mCurrentItemPosition);


        StartClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).toUnit(v);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_unit_index, container, false);
    }
}
