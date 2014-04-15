package com.thu.ttlgm.fragment;

import android.animation.ObjectAnimator;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.thu.ttlgm.BaseActivity;
import com.thu.ttlgm.R;
import com.thu.ttlgm.adapter.PPTImageAdapter;
import com.thu.ttlgm.component.HackyViewPager;
import com.thu.ttlgm.input.GestureListener;
import com.thu.ttlgm.service.SQService;
import com.thu.ttlgm.utils.SharedPreferencesUtils;

import java.io.File;

/**
 * Created by SemonCat on 2014/3/5.
 */
public class PPTFragment extends PlayFragment {
    public static final String PPTPathString = "pptPath";

    public static final String WEEK_PAGER = "week_pager";

    public static final String WEEK = "week";

    private static final String TAG = PPTFragment.class.getName();

    private HackyViewPager mViewPager;
    private PPTImageAdapter mAdapter;

    private Button PlayGame;

    private int mPosition;

    private ImageView Left;
    private ImageView Right;


    private String mPPTFilePath;

    private int week;

    private GestureListener mGestureListener;


    private ResourcePickerFragment mResourcePickerFragment;

    private boolean IsVisible;

    private BaseActivity.OnGlobalTouchEvent mOnGlobalTouchEvent;

    private FrameLayout ResourceParent;

    public PPTFragment() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        week = getArguments().getInt(WEEK, SharedPreferencesUtils.getWeek(getActivity()));
        mPosition = getArguments().getInt(WEEK_PAGER, SharedPreferencesUtils.getPPTPage(getActivity(), week));

        super.onActivityCreated(savedInstanceState);


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mPPTFilePath = savedInstanceState.getString(PPTPathString, mPPTFilePath);
            mPosition = savedInstanceState.getInt(WEEK_PAGER);

            setupView();
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(PPTPathString, mPPTFilePath);
        outState.putInt(WEEK_PAGER, mPosition);
    }

    @Override
    protected void setupView() {

        mAdapter = new PPTImageAdapter(getActivity(), getFiles());


        mViewPager.setAdapter(mAdapter);

        mViewPager.setCurrentItem(mPosition);
        mViewPager.setOffscreenPageLimit(3);


        mResourcePickerFragment = new ResourcePickerFragment();



    }

    private void setupGesture(){
        mGestureListener = new GestureListener(getActivity());
        mGestureListener.setListener(new GestureListener.OnGestureEvent() {
            @Override
            public void onSwipeTop() {
                if (getFragmentManager()!=null && isVisible() && IsVisible){
                    FragmentTransaction mFragmentTransaction= getFragmentManager().beginTransaction();
                    mFragmentTransaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                    mFragmentTransaction.replace(R.id.ResourceContainer,
                            mResourcePickerFragment);

                    mFragmentTransaction.commit();

                }

            }

            @Override
            public void onSwipeBottom() {

            }
        });

        BaseActivity mBaseActivity = ((BaseActivity) getActivity());

        if (mBaseActivity!=null){
            ResourceParent.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mResourcePickerFragment!=null){
                        mResourcePickerFragment.finish();
                    }
                    return false;
                }
            });


            mOnGlobalTouchEvent = new BaseActivity.OnGlobalTouchEvent() {
                @Override
                public void OnTouch(MotionEvent motionEvent) {
                    if (mGestureListener != null) {
                        mGestureListener.onTouch(motionEvent);
                    }
                }
            };

            mBaseActivity.setGlobalTouchEventListener(mOnGlobalTouchEvent);


        }

    }

    @Override
    protected void setupEvent() {
        Left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mPosition - 1, true);
            }
        });

        Left.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                mViewPager.setCurrentItem(0);
                return true;
            }
        });

        Right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mPosition + 1, true);
            }
        });

        Right.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mViewPager.setCurrentItem(mAdapter.getCount() - 1);
                return true;
            }
        });


        ViewPager.OnPageChangeListener mListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                SharedPreferencesUtils.setPPTPage(getActivity(), week, position);


                if (position==0){
                    Left.setVisibility(View.GONE);
                }else{
                    Left.setVisibility(View.VISIBLE);
                }

                if (position==mAdapter.getCount() - 1){
                    Right.setVisibility(View.GONE);
                }else{
                    Right.setVisibility(View.VISIBLE);
                }

                mPosition = position;

                PlayGame.setVisibility(View.GONE);

                if (checkMission(mPosition)) {
                    PlayGame.setVisibility(View.VISIBLE);
                } else {
                    PlayGame.setVisibility(View.GONE);
                }


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
                Log.d(TAG, "Send Mission");


                Toast.makeText(getActivity(), "已發送任務！", Toast.LENGTH_SHORT).show();

                String MissionCode = parseMission(mPosition);

                if (MissionCode != null) {
                    Log.d(TAG, "sendMission:" + MissionCode);
                    SQService.startQuestion(MissionCode, "1");

                }


            }
        });
    }

    private File[] getFiles() {
        //if (mPPTFile!=null && mPPTFile.exists()) return mPPTFile.listFiles();

        if (mPPTFilePath == null) {
            mPPTFilePath = getArguments().getString(PPTPathString);
        }

        String Path = mPPTFilePath;
        File mFile = new File(Path);
        if (mFile.exists()) {

            return mFile.listFiles();
        } else {
            finish();
            Log.d(TAG, "File==null");
            return null;
        }
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);

        IsVisible = visible;

        if (visible) {
            mAdapter.StartLoad();

            if (getActivity()!=null){
                setupGesture();
            }
        }
    }

    @Override
    public void OnAlertTouch(MotionEvent event) {
        Right.dispatchTouchEvent(event);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_ppt, container, false);
        mViewPager = (HackyViewPager) mView.findViewById(R.id.view_pager);
        PlayGame = (Button) mView.findViewById(R.id.PlayGame);

        Left = (ImageView) mView.findViewById(R.id.leftButton);
        Right = (ImageView) mView.findViewById(R.id.rightButton);


        ResourceParent = (FrameLayout) mView.findViewById(R.id.ResourceParent);


        return mView;
    }

    private boolean checkMission(int position) {
        try {
            String FileName = mAdapter.getFiles()[position].getName();
            return FileName.contains("_m");
        } catch (ArrayIndexOutOfBoundsException mArrayIndexOutOfBoundsException) {
            mArrayIndexOutOfBoundsException.printStackTrace();
            return false;
        }
    }

    private String parseMission(int position) {


        String FileName = mAdapter.getFiles()[position].getName();
        FileName = getFileName(FileName);
        boolean IsMission = FileName.contains("_m");

        if (IsMission) {
            String Split[] = FileName.split("_");

            if (Split.length >= 3) {

                return Split[2];

            }
        }

        return null;
    }

    /**
     * 去除副檔名
     *
     * @param s
     * @return
     */
    private String getFileName(String s) {
        s = new File(s).getName();
        int dotPos = s.lastIndexOf(".");
        return s.substring(0, dotPos);
    }

    @Override
    public void onDestroy() {
        BaseActivity mBaseActivity = ((BaseActivity) getActivity());

        if (mBaseActivity!=null){
            mBaseActivity.setGlobalTouchEventListener(null);
        }
        super.onDestroy();
    }
}
