package com.thu.ttlgm.fragment;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.thu.ttlgm.adapter.PPTPreviewAdapter;
import com.thu.ttlgm.bean.Class;

import com.thu.ttlgm.R;
import com.thu.ttlgm.adapter.ResourceAdapter;
import com.thu.ttlgm.utils.ConstantUtil;
import com.thu.ttlgm.utils.SharedPreferencesUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * Created by SemonCat on 2014/4/9.
 */
public class ClassInfoFragment extends BaseFragment {

    private TextView Week;
    private TextView ClassTitle;
    private TextView ClassDate;
    private ImageView WeekInfoBackground;
    private ListView ResourceList;
    private ListView PPT_Preview;

    private ImageView PPT_Tip;

    private ResourceAdapter mMovieAdapter;
    private PPTPreviewAdapter mPPTPreviewAdapter;

    private Class mClass;

    private AdapterView.OnItemClickListener mListener;

    private int weekInfoBackground[] = new int[]{R.drawable.obj_t_nubg01,
            R.drawable.obj_t_nubg02, R.drawable.obj_t_nubg03, R.drawable.obj_t_nubg04, R.drawable.obj_t_nubg05,
            R.drawable.obj_t_nubg06, R.drawable.obj_t_nubg07, R.drawable.obj_t_nubg08, R.drawable.obj_t_nubg09,
            R.drawable.obj_t_nubg10, R.drawable.obj_t_nubg11, R.drawable.obj_t_nubg12, R.drawable.obj_t_nubg13,
            R.drawable.obj_t_nubg14, R.drawable.obj_t_nubg15, R.drawable.obj_t_nubg16, R.drawable.obj_t_nubg17,
            R.drawable.obj_t_nubg18, R.drawable.obj_t_nubg19};

    public ClassInfoFragment(Class mClass) {
        this.mClass = mClass;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupAnim();
    }

    @Override
    protected void setupView() {


        Week.setText(String.valueOf(mClass.getWeek()));
        WeekInfoBackground.setImageResource(weekInfoBackground[new Random().nextInt(weekInfoBackground.length)]);

        ClassTitle.setText(mClass.getTitle());

        ClassDate.setText(new SimpleDateFormat("yyyy/MM/dd").format(mClass.getDate()));

    }


    @Override
    protected void setupAdapter() {
        mMovieAdapter = new ResourceAdapter(getActivity(),
                ConstantUtil.TLGMPath + ConstantUtil.MovieDir + ConstantUtil.WeekPath + mClass.getWeek());

        ResourceList.setAdapter(mMovieAdapter);

        mPPTPreviewAdapter = new PPTPreviewAdapter(getActivity(), ConstantUtil.TLGMPath + ConstantUtil.PPTDir + ConstantUtil.WeekPath + mClass.getWeek());

        PPT_Preview.setAdapter(mPPTPreviewAdapter);
    }

    @Override
    protected void setupEvent() {


        ResourceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OpenMovie(mMovieAdapter.getItem(position).getPath());
            }
        });


        PPT_Preview.setOnItemClickListener(mListener);

    }

    private void setupAnim() {


        AnimatorSet mAnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(),
                R.anim.translate_right2left);

        mAnimatorSet.setTarget(PPT_Tip);

        if (hasPPT(mClass.getWeek())) {
            PPT_Tip.setVisibility(View.VISIBLE);
            mAnimatorSet.start();
        }

    }


    private void OpenMovie(String Path) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Path));
            intent.setDataAndType(Uri.parse(Path), "video/mp4");
            getActivity().startActivity(intent);
        } catch (ActivityNotFoundException mActivityNotFoundException) {
            Toast.makeText(getActivity(), "找不到播放器", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.adapter_class, container, false);

        Week = (TextView) mView.findViewById(R.id.WeekInfo);
        ClassTitle = (TextView) mView.findViewById(R.id.ClassTitle);
        ClassDate = (TextView) mView.findViewById(R.id.ClassDate);
        WeekInfoBackground = (ImageView) mView.findViewById(R.id.WeekInfoBackground);
        ResourceList = (ListView) mView.findViewById(R.id.Resource);
        PPT_Preview = (ListView) mView.findViewById(R.id.PPT_Preview);

        PPT_Tip = (ImageView) mView.findViewById(R.id.PPT_Tip);

        if (savedInstanceState != null) {

        }

        return mView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }

    public void setOnPPTPreViewItemClick(AdapterView.OnItemClickListener mListener) {
        if (mListener != null) {
            this.mListener = mListener;
        }
    }

    public static boolean hasPPT(int week) {
        File mFile = new File(ConstantUtil.TLGMPath + ConstantUtil.PPTDir + ConstantUtil.WeekPath + week);


        if (mFile.exists() && mFile.isDirectory() && mFile.list().length > 0) {

            for (File insideFile : mFile.listFiles()) {
                if (insideFile.isDirectory() && insideFile.list().length > 0) {
                    return true;
                }
            }

        }


        return false;
    }
}
