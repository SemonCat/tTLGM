package com.thu.ttlgm.fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.thu.ttlgm.MainActivity;
import com.thu.ttlgm.R;
import com.thu.ttlgm.adapter.UnitIndexPagerAdapter;
import com.thu.ttlgm.bean.*;
import com.thu.ttlgm.bean.Class;
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
    private UViewPager mUViewPager;
    private UnitIndexPagerAdapter mAdapter;
    private int mCurrentItemPosition;

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
        mUViewPager = (UViewPager) getActivity().findViewById(R.id.ViewPagerClass);
        StartClass = (ImageView) getActivity().findViewById(R.id.StartClass);
    }

    private Handler mHandler = new Handler();
    @Override
    protected void setupAdapter() {

        mAdapter = new UnitIndexPagerAdapter(getActivity(),getClassData());
        mUViewPager.setAdapter(mAdapter);
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
    }


    @Override
    protected void setupEvent(){
        UViewPager.OnPageChangeListener mListener = new UViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                ((MainActivity)getActivity()).setCurrentClass(mAdapter.getItem(position));
                SharedPreferencesUtils.setWeek(getActivity(),position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        mUViewPager.setOnPageChangeListener(mListener);

        mListener.onPageSelected(SharedPreferencesUtils.getWeek(getActivity()));


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
