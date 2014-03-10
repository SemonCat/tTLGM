package com.thu.ttlgm.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.thu.ttlgm.R;
import com.thu.ttlgm.adapter.PPTImageAdapter;
import com.thu.ttlgm.component.HackyViewPager;
import com.thu.ttlgm.component.UViewPager;
import com.thu.ttlgm.service.SQService;

import java.io.File;

/**
 * Created by SemonCat on 2014/3/5.
 */
public class PPTFragment extends PlayFragment{
    public static final String PPTPathString = "pptPath";

    private static final String TAG = PPTFragment.class.getName();

    private HackyViewPager mViewPager;
    private PPTImageAdapter mAdapter;

    private int[] missionPosition = new int[]{7,18};

    private Button PlayGame;

    private int mPosition;


    @Override
    protected void setupView() {
        mViewPager = (HackyViewPager) getActivity().findViewById(R.id.view_pager);
        mAdapter = new PPTImageAdapter(getActivity(),getFiles());


        mViewPager.setAdapter(mAdapter);


        PlayGame = (Button) getActivity().findViewById(R.id.PlayGame);
    }

    @Override
    protected void setupEvent() {
        ViewPager.OnPageChangeListener mListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                Log.d(TAG,"Page:"+position);

                PlayGame.setVisibility(View.GONE);

                for (int mission:missionPosition){
                    if (position==mission){

                        Log.d(TAG,"This Page has mission:"+position);
                        PlayGame.setVisibility(View.VISIBLE);


                    }
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        mListener.onPageSelected(0);
        mViewPager.setOnPageChangeListener(mListener);

        PlayGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Send Mission");


                if (mPosition==missionPosition[0]){
                    SQService.startQuestion("0","1");
                }else if(mPosition==missionPosition[1]){
                    SQService.startQuestion("1","1");
                }


            }
        });
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
