package com.thu.ttlgm.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thu.ttlgm.R;
import com.thu.ttlgm.adapter.PPTImageAdapter;
import com.thu.ttlgm.component.HackyViewPager;

import java.io.File;

/**
 * Created by SemonCat on 2014/3/5.
 */
public class PPTFragment extends PlayFragment{
    public static final String PPTPathString = "pptPath";

    private static final String TAG = PPTFragment.class.getName();

    private HackyViewPager mViewPager;
    private PPTImageAdapter mAdapter;

    @Override
    protected void setupView() {
        mViewPager = (HackyViewPager) getActivity().findViewById(R.id.view_pager);
        mAdapter = new PPTImageAdapter(getActivity(),getFiles());

        mViewPager.setAdapter(mAdapter);
    }

    @Override
    protected void setupEvent() {

    }

    private File[] getFiles(){
        /*
        String Path = getActivity().getIntent().getStringExtra(PPTPathString);
        */
        String Path = getArguments().getString(PPTPathString);
        File mFile = new File(Path);
        if (mFile.exists()){

            return mFile.listFiles();
        }else{
            finish();
            Log.d(TAG, "File==null");
            return null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ppt, container, false);
    }
}
