package com.thu.ttlgm.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.thu.ttlgm.BaseActivity;

/**
 * Created by SemonCat on 2014/1/13.
 */
public class BaseFragment extends Fragment{

    private View.OnTouchListener mListener;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupView();
        setupAdapter();
        setupEvent();


        mListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (getView()!=null){
                    getView().dispatchTouchEvent(event);
                }

                return false;
            }
        };

        ((BaseActivity)getActivity()).getListenerList().add(mListener);

    }



    protected void setupView(){

    }

    protected void setupAdapter(){

    }

    protected void setupEvent(){

    }

    protected void finish(){

        ((BaseActivity)getActivity()).getListenerList().remove(mListener);
        getFragmentManager().beginTransaction().remove(this).commit();
    }

}
