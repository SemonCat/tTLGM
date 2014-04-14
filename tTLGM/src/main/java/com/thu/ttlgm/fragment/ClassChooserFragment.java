package com.thu.ttlgm.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.thu.ttlgm.MainActivity;
import com.thu.ttlgm.R;
import com.thu.ttlgm.adapter.ClassPagerAdapter;
import com.thu.ttlgm.adapter.ResourceAdapter;
import com.thu.ttlgm.bean.*;
import com.thu.ttlgm.bean.Class;
import com.thu.ttlgm.component.JazzyViewPager.JazzyVerticalViewPager;
import com.thu.ttlgm.component.JazzyViewPager.JazzyViewPager;
import com.thu.ttlgm.component.UViewPager;
import com.thu.ttlgm.utils.ConstantUtil;
import com.thu.ttlgm.utils.DataParser;
import com.thu.ttlgm.utils.SharedPreferencesUtils;

import java.io.File;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

/**
 * Created by SemonCat on 2014/1/11.
 */
public class ClassChooserFragment extends BaseFragment{

    private static final String TAG = ClassChooserFragment.class.getName();


    //ViewPager
    private JazzyVerticalViewPager mVerticalViewPager;
    private ClassPagerAdapter mAdapter;


    private ViewPager.OnPageChangeListener mOnPageChangeListener;


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
        mVerticalViewPager = (JazzyVerticalViewPager) getActivity().findViewById(R.id.ViewPagerClass);
        mVerticalViewPager.setOffscreenPageLimit(3);
    }

    @Override
    protected void setupAdapter() {

        mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                ((MainActivity) getActivity()).setCurrentClass(mAdapter.getItem(position));
                SharedPreferencesUtils.setWeek(getActivity(), position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        mAdapter = new ClassPagerAdapter(mVerticalViewPager,getActivity(),getChildFragmentManager(),getClassData());
        mVerticalViewPager.setFadeEnabled(!mVerticalViewPager.getFadeEnabled());
        mVerticalViewPager.setAdapter(mAdapter);

        mVerticalViewPager.setCurrentItem(SharedPreferencesUtils.getWeek(getActivity()));
        mOnPageChangeListener.onPageSelected(mVerticalViewPager.getCurrentItem());
    }


    @Override
    protected void setupEvent(){
        mVerticalViewPager.setOnPageChangeListener(mOnPageChangeListener);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_classchooser, container, false);
    }

}
