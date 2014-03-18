package com.thu.ttlgm.fragment;

import android.animation.ValueAnimator;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.thu.ttlgm.BaseActivity;
import com.thu.ttlgm.R;
import com.thu.ttlgm.adapter.ResourceAdapter;
import com.thu.ttlgm.utils.ConstantUtil;
import com.thu.ttlgm.utils.SharedPreferencesUtils;

import java.util.List;

/**
 * Created by SemonCat on 2014/1/14.
 */
public class ResourcePickerFragment extends BaseFragment implements View.OnClickListener{

    private static final String TAG = ResourcePickerFragment.class.getName();

    private ListView mListViewPDF;
    private ListView mListViewPPT;
    private ListView mListViewMovie;
    private ResourceAdapter mPDFAdapter;
    private ResourceAdapter mPPTAdapter;
    private ResourceAdapter mMovieAdapter;

    private LinearLayout mPPTArea;
    private LinearLayout mVideoArea;
    private LinearLayout mWordArea;
    private LinearLayout mPdfArea;
    private LinearLayout mImageArea;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    protected void setupView() {
        mListViewPDF = (ListView) getActivity().findViewById(R.id.ListViewResourcePDF);
        mListViewPPT = (ListView) getActivity().findViewById(R.id.ListViewResourcePPT);
        mListViewMovie = (ListView) getActivity().findViewById(R.id.ListViewResourceMovie);

        mPPTArea = (LinearLayout) getActivity().findViewById(R.id.PPTArea);
        mVideoArea = (LinearLayout) getActivity().findViewById(R.id.VideoArea);
        mWordArea = (LinearLayout) getActivity().findViewById(R.id.WordArea);
        mPdfArea = (LinearLayout) getActivity().findViewById(R.id.PdfArea);
        mImageArea = (LinearLayout) getActivity().findViewById(R.id.ImageArea);
    }

    @Override
    protected void setupAdapter() {
        int week = (SharedPreferencesUtils.getWeek(getActivity()));

        mPDFAdapter = new ResourceAdapter(getActivity(), ConstantUtil.TLGMPath+ConstantUtil.PDFDir+ConstantUtil.WeekPath+week);
        mListViewPDF.setAdapter(mPDFAdapter);

        mPPTAdapter = new ResourceAdapter(getActivity(), ConstantUtil.TLGMPath+ConstantUtil.PPTDir+ConstantUtil.WeekPath+week);
        mListViewPPT.setAdapter(mPPTAdapter);

        mMovieAdapter = new ResourceAdapter(getActivity(),ConstantUtil.TLGMPath+ConstantUtil.MovieDir+ConstantUtil.WeekPath+week);
        mListViewMovie.setAdapter(mMovieAdapter);


    }

    @Override
    protected void setupEvent() {
        mListViewPDF.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OpenPDF(mPDFAdapter.getItem(position).getPath());
            }
        });

        mListViewPPT.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OpenPPT(mPPTAdapter.getItem(position).getPath());
            }
        });

        mListViewMovie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OpenMovie(mMovieAdapter.getItem(position).getPath());
            }
        });

        mPPTArea.setOnClickListener(this);
        mVideoArea.setOnClickListener(this);
        mWordArea.setOnClickListener(this);
        mPdfArea.setOnClickListener(this);
        mImageArea.setOnClickListener(this);
    }

    private void OpenPDF(String Path){
        /*
        Intent mIntent = new Intent(getActivity(), PdfActivity.class);
        mIntent.putExtra(PdfActivity.PDFPathString,Path);
        startActivity(mIntent);
        */

        Bundle args = new Bundle();
        args.putString(PdfFragment.PDFPathString,Path);
        PdfFragment pdfFragment = new PdfFragment();
        pdfFragment.setArguments(args);
        ((BaseActivity)getActivity()).replaceFragment(pdfFragment,PdfFragment.class.getName());
    }

    private void OpenPPT(String Path){
        /*
        Intent mIntent = new Intent(getActivity(), PPTActivity.class);
        mIntent.putExtra(PPTActivity.PPTPathString,Path);
        startActivity(mIntent);
        */

        Bundle args = new Bundle();
        args.putString(PPTFragment.PPTPathString,Path);
        PPTFragment pptFragment = new PPTFragment();
        pptFragment.setArguments(args);
        ((BaseActivity)getActivity()).replaceFragment(pptFragment,PPTFragment.class.getName());
    }


    private void OpenMovie(String Path){
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Path));
            intent.setDataAndType(Uri.parse(Path), "video/mp4");
            startActivity(intent);
        }catch (ActivityNotFoundException mActivityNotFoundException){
            Toast.makeText(getActivity(),"找不到播放器",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_resourcepicker, container, false);
    }


    @Override
    public void onClick(View v) {

        if (v.getTag()==null){
            v.setTag(Boolean.valueOf(true));
        }

        resetLayout();

        setAllAreaTag(true);
        if (((Boolean)v.getTag()).booleanValue()==true){
            slideAnimator(1,5,v).start();


            v.setTag(Boolean.valueOf(false));

        }else{
            slideAnimator(5,1,v).start();

            v.setTag(Boolean.valueOf(true));

        }

    }

    private void resetLayout(){
        setAllAreaLayoutWeight(1);
    }

    private void setAllAreaTag(boolean IsHide){
        mPPTArea.setTag(Boolean.valueOf(IsHide));
        mVideoArea.setTag(Boolean.valueOf(IsHide));
        mWordArea.setTag(Boolean.valueOf(IsHide));
        mPdfArea.setTag(Boolean.valueOf(IsHide));
        mImageArea.setTag(Boolean.valueOf(IsHide));
    }

    private void setAllAreaLayoutWeight(int weight){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT,weight);

        mPPTArea.setLayoutParams(layoutParams);
        mVideoArea.setLayoutParams(layoutParams);
        mWordArea.setLayoutParams(layoutParams);
        mPdfArea.setLayoutParams(layoutParams);
        mImageArea.setLayoutParams(layoutParams);
    }

    private ValueAnimator slideAnimator(float start, float end, final View summary) {

        ValueAnimator animator = ValueAnimator.ofFloat(start, end);
        animator.setDuration(300);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                float value = (Float) valueAnimator.getAnimatedValue();

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.MATCH_PARENT,value);

                summary.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }
}
