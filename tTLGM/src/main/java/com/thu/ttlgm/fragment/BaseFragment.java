package com.thu.ttlgm.fragment;

import android.app.Fragment;
import android.os.Bundle;

import com.thu.ttlgm.BaseActivity;

/**
 * Created by SemonCat on 2014/1/13.
 */
public class BaseFragment extends Fragment{

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupView();
        setupAdapter();
        setupEvent();

    }

    protected void setupView(){

    }

    protected void setupAdapter(){

    }

    protected void setupEvent(){

    }

    protected void finish(){

        getFragmentManager().beginTransaction().remove(this).commit();
    }

}
