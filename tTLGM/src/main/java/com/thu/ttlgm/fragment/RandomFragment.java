package com.thu.ttlgm.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.makeramen.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.thu.ttlgm.MainActivity;
import com.thu.ttlgm.R;
import com.thu.ttlgm.bean.Student;
import com.thu.ttlgm.component.IconView;
import com.thu.ttlgm.service.SQService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by SemonCat on 2014/3/12.
 */

public class RandomFragment extends BaseFragment{

    private static final String TAG = RandomFragment.class.getName();

    private ImageView Close,Refresh,Add,Sub,RandomBackground;

    private IconView TargetIcon;

    private TextView Student_Name,Student_ID,Student_Department,AddCoinTip;

    private LinearLayout RandomArea;

    private ProgressBar LoadBar;

    private List<Bitmap> mIconList;

    private Handler mHandler = new Handler();

    private RoundedImageView RandomIcon;

    private Student mTarget;

    private List<Student> mStudentList;

    private long StartTime = 50;

    private Animation animation;

    private Animation rotateAnim;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupAnim();
        LoadImage();
    }

    private void setupAnim(){
        animation = AnimationUtils.loadAnimation(getActivity(),R.anim.fade_out_push_top);

        rotateAnim = AnimationUtils.loadAnimation(getActivity(),R.anim.rotate_around_center_point);
    }


    @Override
    protected void setupView() {
        Refresh = (ImageView) getActivity().findViewById(R.id.RefreshCoin);
        Close = (ImageView) getActivity().findViewById(R.id.Close);
        Add = (ImageView) getActivity().findViewById(R.id.AddCoin);
        Sub = (ImageView) getActivity().findViewById(R.id.SubCoin);
        RandomBackground = (ImageView) getActivity().findViewById(R.id.RandomBackground);

        Student_Name = (TextView) getActivity().findViewById(R.id.Student_Name);
        Student_ID = (TextView) getActivity().findViewById(R.id.Student_ID);
        Student_Department = (TextView) getActivity().findViewById(R.id.Student_Department);

        AddCoinTip = (TextView) getActivity().findViewById(R.id.CoinTip);

        LoadBar = (ProgressBar) getActivity().findViewById(R.id.LoadBar);

        RandomIcon = (RoundedImageView) getActivity().findViewById(R.id.RandomIcon);

        RandomArea = (LinearLayout) getActivity().findViewById(R.id.RandomArea);

        TargetIcon = (IconView) getActivity().findViewById(R.id.TargetIcon);

        setAddSubEnable(false);
    }

    @Override
    protected void setupEvent() {
        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                ((MainActivity)getActivity()).setDrawerEnable(true);
            }
        });

        Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRandom();
            }
        });


        TargetIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowRandomArea();
            }
        });

        RandomBackground.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {



                if (!(event.getX() > RandomArea.getLeft() && event.getX() < RandomArea.getRight()) &&
                        (event.getY() > RandomArea.getTop() && event.getY() < RandomArea.getBottom())){
                    HideRandomArea();
                }

                return false;
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add.setEnabled(false);
                SQService.AddStudentCoin(mTarget.getID(),1);

                AddCoinTip.setVisibility(View.VISIBLE);
                AddCoinTip.startAnimation(animation);

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        finish();
                        ((MainActivity)getActivity()).setDrawerEnable(true);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });


            }
        });
    }

    private void LoadImage(){
        mIconList = new ArrayList<Bitmap>();

        SQService.getCacheStudentList(new SQService.OnStudentGetListener() {
            @Override
            public void OnStudentGetEvent(List<Student> studentList) {
                mStudentList = studentList;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mIconList.clear();

                        for (Student mStudent:mStudentList) {
                            ImageSize targetSize = new ImageSize(50, 50); // result Bitmap will be fit to this size

                            String URL = mStudent.getImageUrl();
                            if (URL.startsWith("http")){

                                Bitmap bmp = ImageLoader.getInstance().
                                        loadImageSync(mStudent.getImageUrl(), targetSize);

                                mIconList.add(bmp);
                            }
                        }

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                LoadBar.setVisibility(View.GONE);
                            }
                        });

                        startRandom();

                    }
                }).start();
            }
        });
    }



    public static void LoadCache(){
        SQService.getCacheStudentList(new SQService.OnStudentGetListener() {
            @Override
            public void OnStudentGetEvent(final List<Student> mStudentList) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        for (Student mStudent:mStudentList) {
                            ImageSize targetSize = new ImageSize(50, 50); // result Bitmap will be fit to this size

                            String URL = mStudent.getImageUrl();
                            if (URL.startsWith("http")){
                                //Log.d(TAG,"LoadImage");

                                ImageLoader.getInstance().
                                        loadImageSync(mStudent.getImageUrl(), targetSize);

                            }
                        }

                    }
                }).start();
            }
        });
    }

    private long time;
    private void startRandom(){


        time = StartTime;


        mHandler.post(new Runnable() {
            @Override
            public void run() {
                RandomBackground.clearAnimation();

                if (RandomBackground.getVisibility()==View.VISIBLE){
                    RandomBackground.startAnimation(rotateAnim);
                }
                setAddSubEnable(false);
                resetData();
            }
        });

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                int targetIndex = new Random().nextInt(mStudentList.size() - 1);

                try{
                    RandomIcon.setImageBitmap(mIconList.get(targetIndex));
                }catch (IndexOutOfBoundsException mIndexOutOfBoundsException){
                    targetIndex = new Random().nextInt(mIconList.size() - 1);
                    RandomIcon.setImageBitmap(mIconList.get(targetIndex));
                }

                if (time!=0){
                    mHandler.postDelayed(this,time--);
                }else {
                    mTarget = mStudentList.get(targetIndex);
                    ImageLoader.getInstance().displayImage(mTarget.getImageUrl(),RandomIcon);
                    ImageLoader.getInstance().displayImage(mTarget.getImageUrl(),TargetIcon);

                    Student_Name.setText(mTarget.getName());
                    Student_Department.setText(mTarget.getDepartment());
                    Student_ID.setText(mTarget.getID());

                    RandomBackground.clearAnimation();
                    setAddSubEnable(true);
                }
            }
        },time);
    }

    private void resetData(){
        Student_Name.setText("");
        Student_ID.setText("");
        Student_Department.setText("");
    }

    private void setAddSubEnable(boolean enable){
        Refresh.setEnabled(enable);
        Add.setEnabled(enable);
        Sub.setEnabled(enable);
    }

    private void HideRandomArea(){
        RandomBackground.setVisibility(View.GONE);
        RandomArea.setVisibility(View.GONE);

        TargetIcon.setVisibility(View.VISIBLE);
    }

    private void ShowRandomArea(){
        RandomBackground.setVisibility(View.VISIBLE);
        RandomArea.setVisibility(View.VISIBLE);

        TargetIcon.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_random, container, false);
    }
}
