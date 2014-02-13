package com.thu.ttlgm.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thu.ttlgm.MainActivity;
import com.thu.ttlgm.R;
import com.thu.ttlgm.adapter.ClassPagerAdapter;
import com.thu.ttlgm.bean.Subject;
import com.thu.ttlgm.component.UViewPager;
import com.thu.ttlgm.utils.ConstantUtil;
import com.thu.ttlgm.utils.DataParser;

import java.io.File;

/**
 * Created by SemonCat on 2014/1/11.
 */
public class ClassChooserFragment extends BaseFragment{

    private static final String TAG = ClassChooserFragment.class.getName();

    private Context mContext;

    //ViewPager
    private UViewPager mUViewPager;
    private ClassPagerAdapter mAdapter;
    private static final String CURRENTITEMPOSITION = "CurrentItemPosition";
    private int mCurrentItemPosition;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContext = getActivity().getApplicationContext();

    }

    private Subject getClassData(){

        File File = new File(ConstantUtil.SubjectDataPath);

        return DataParser.SubjectParser(mContext,File);
    }

    @Override
    protected void setupView() {
        mUViewPager = (UViewPager) getActivity().findViewById(R.id.ViewPagerClass);

    }

    @Override
    protected void setupAdapter() {
        mAdapter = new ClassPagerAdapter(getActivity(),getClassData());
        mUViewPager.setAdapter(mAdapter);
        mUViewPager.enableCenterLockOfChilds();
        mCurrentItemPosition =
                mAdapter.getItemPosition(((MainActivity)getActivity()).getCurrentClass());

        Log.d(TAG,"Currect:"+mCurrentItemPosition);

        mUViewPager.setCurrentItemInCenter(mCurrentItemPosition);

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
