package com.thu.ttlgm.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.thu.ttlgm.R;
import com.thu.ttlgm.adapter.PPTImageAdapter;
import com.thu.ttlgm.component.HackyViewPager;
import com.thu.ttlgm.component.UViewPager;
import com.thu.ttlgm.service.SQService;
import com.thu.ttlgm.utils.SharedPreferencesUtils;

import java.io.File;

/**
 * Created by SemonCat on 2014/3/5.
 */
public class PPTFragment extends PlayFragment{
    public static final String PPTPathString = "pptPath";

    private static final String TAG = PPTFragment.class.getName();

    private HackyViewPager mViewPager;
    private PPTImageAdapter mAdapter;

    private int[] missionPosition = new int[]{3,5,7};

    private Button PlayGame;

    private int mPosition;

    private ImageView Left;
    private ImageView Right;


    @Override
    protected void setupView() {
        mViewPager = (HackyViewPager) getActivity().findViewById(R.id.view_pager);
        mAdapter = new PPTImageAdapter(getActivity(),getFiles());


        mViewPager.setAdapter(mAdapter);
        mPosition = SharedPreferencesUtils.getPPTPage(getActivity(),SharedPreferencesUtils.getWeek(getActivity()));
        mViewPager.setCurrentItem(mPosition,true);

        PlayGame = (Button) getActivity().findViewById(R.id.PlayGame);

        Left = (ImageView) getActivity().findViewById(R.id.leftButton);
        Right = (ImageView) getActivity().findViewById(R.id.rightButton);


    }

    @Override
    protected void setupEvent() {
        Left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mPosition-1,true);
            }
        });

        Right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mPosition+1,true);
            }
        });


        ViewPager.OnPageChangeListener mListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                SharedPreferencesUtils.setPPTPage(getActivity(),SharedPreferencesUtils.getWeek(getActivity()),position);


                mPosition = position;
                Log.d(TAG,"Page:"+position);

                PlayGame.setVisibility(View.GONE);

                if (checkMission(mPosition)){
                    PlayGame.setVisibility(View.VISIBLE);
                }else{
                    PlayGame.setVisibility(View.GONE);
                }

                /*
                for (int mission:missionPosition){
                    if (position==mission){

                        Log.d(TAG,"This Page has mission:"+position);
                        PlayGame.setVisibility(View.VISIBLE);


                    }
                }*/


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        mViewPager.setOnPageChangeListener(mListener);
        mListener.onPageSelected(mPosition);

        PlayGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Send Mission");



                Toast.makeText(getActivity(),"已發送任務！",Toast.LENGTH_SHORT).show();

                String MissionCode = parseMission(mPosition);

                if (MissionCode!=null){
                    Log.d(TAG,"sendMission:"+MissionCode);
                    SQService.startQuestion(MissionCode,"1");

                    if (MissionCode.equals("21")){
                        SQService.startQuestion("0","1");
                    }else if(MissionCode.equals("22")){
                        SQService.startQuestion("1","1");
                    }
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
    public void OnAlertTouch(MotionEvent event) {
        Right.dispatchTouchEvent(event);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ppt, container, false);
    }

    private boolean checkMission(int position){
        String FileName = mAdapter.getFiles()[position].getName();
        return FileName.contains("_m");
    }

    private String parseMission(int position){


        String FileName = mAdapter.getFiles()[position].getName();
        FileName = getFileName(FileName);
        boolean IsMission = FileName.contains("_m");

        if (IsMission){
            String Split[] = FileName.split("_");

            if (Split.length>=3){

               return Split[2];

            }
        }

        return null;
    }

    /**
     * 去除副檔名
     * @param s
     * @return
     */
    private String getFileName(String s){
        s  = new File(s).getName();
        int dotPos = s.lastIndexOf(".");
        return s.substring(0, dotPos);
    }
}
