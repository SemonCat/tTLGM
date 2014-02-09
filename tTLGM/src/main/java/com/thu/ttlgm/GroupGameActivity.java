package com.thu.ttlgm;

import android.os.Bundle;

import com.thu.ttlgm.adapter.StudentGroupAdapter;
import com.thu.ttlgm.component.FancyCoverFlow.FancyCoverFlow;

/**
 * Created by SemonCat on 2014/2/9.
 */
public class GroupGameActivity extends BaseActivity{

    private static final String TAG = GroupGameActivity.class.getName();

    private FancyCoverFlow mFancyCoverFlow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void setupView() {
        mFancyCoverFlow = (FancyCoverFlow) findViewById(R.id.fancyCoverFlow);
        mFancyCoverFlow.setAdapter(new StudentGroupAdapter());
        mFancyCoverFlow.setSelection(Integer.MAX_VALUE / 2);
    }

    @Override
    protected void setupEvent() {

    }

    @Override
    protected int setupLayout() {
        return R.layout.activity_groupgame;
    }
}
