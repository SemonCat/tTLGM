package com.thu.ttlgm.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thu.ttlgm.R;
import com.thu.ttlgm.adapter.ClassInfoPagerAdapter;
import com.thu.ttlgm.component.JazzyViewPager.JazzyViewPager;
import com.thu.ttlgm.bean.Class;

/**
 * Created by SemonCat on 2014/4/14.
 */
public class UnitFragment extends BaseFragment{

    private Class mClass;

    private JazzyViewPager UnitViewPager;

    private ClassInfoPagerAdapter mAdapter;


    public UnitFragment(Class mclass){
        this.mClass = mclass;
    }

    @Override
    protected void setupAdapter() {
        mAdapter = new ClassInfoPagerAdapter(getActivity(),mClass,getChildFragmentManager());

        UnitViewPager.setAdapter(mAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_unit, container, false);

        UnitViewPager = (JazzyViewPager) mView.findViewById(R.id.UnitViewPager);

        return mView;
    }
}
