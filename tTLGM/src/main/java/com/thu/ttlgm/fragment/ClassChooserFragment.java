package com.thu.ttlgm.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thu.ttlgm.MainActivity;
import com.thu.ttlgm.R;
import com.thu.ttlgm.adapter.ClassPagerAdapter;
import com.thu.ttlgm.bean.*;
import com.thu.ttlgm.bean.Class;
import com.thu.ttlgm.component.UViewPager;
import com.thu.ttlgm.utils.ConstantUtil;
import com.thu.ttlgm.utils.DataParser;

import java.io.File;

/**
 * Created by SemonCat on 2014/1/11.
 */
public class ClassChooserFragment extends BaseFragment{

    private static final String TAG = ClassChooserFragment.class.getName();


    //ViewPager
    private UViewPager mUViewPager;
    private ClassPagerAdapter mAdapter;
    private int mCurrentItemPosition;


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
        mUViewPager = (UViewPager) getActivity().findViewById(R.id.ViewPagerClass);
        Log.d(TAG,"setupView");
    }

    private Handler mHandler = new Handler();
    @Override
    protected void setupAdapter() {

        mAdapter = new ClassPagerAdapter(getActivity(),getClassData());
        mUViewPager.setAdapter(mAdapter);
        mUViewPager.enableCenterLockOfChilds();

        Class mData = ((MainActivity)getActivity()).getCurrentClass();
        if (mData!=null){

            mCurrentItemPosition =
                mAdapter.getItemPosition(mData);
        }

        mUViewPager.setCurrentItemInCenter(0);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mUViewPager.setCurrentItem(mCurrentItemPosition,true);
            }
        });
    }


    @Override
    protected void setupEvent(){
        mUViewPager.setOnPageChangeListener(new UViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((MainActivity)getActivity()).setCurrentClass(mAdapter.getItem(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_classchooser, container, false);
    }

}
