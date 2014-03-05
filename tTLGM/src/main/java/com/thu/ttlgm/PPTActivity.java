package com.thu.ttlgm;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.thu.ttlgm.adapter.PPTImageAdapter;
import com.thu.ttlgm.component.HackyViewPager;

import java.io.File;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by SemonCat on 2014/2/5.
 */
public class PPTActivity extends BasePlayActivity {

    public static final String PPTPathString = "pptPath";

    private static final String TAG = PPTActivity.class.getName();

    private HackyViewPager mViewPager;
    private PPTImageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    protected void setupView() {
        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
        mAdapter = new PPTImageAdapter(this,getFiles());

        mViewPager.setAdapter(mAdapter);
    }

    @Override
    protected void setupEvent() {

    }

    private File[] getFiles(){
        String Path = getIntent().getStringExtra(PPTPathString);
        File mFile = new File(Path);
        if (mFile != null && mFile.exists()){

            return mFile.listFiles();
        }else{

            finish();
            Log.d(TAG, "File==null");
            return null;
        }
    }

    @Override
    protected int setupLayout() {
        return R.layout.activity_ppt;
    }

}
