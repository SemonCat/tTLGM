package com.thu.ttlgm.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.thu.ttlgm.BaseActivity;
import com.thu.ttlgm.R;

import java.lang.reflect.Field;

/**
 * Created by SemonCat on 2014/1/13.
 */
public class BaseFragment extends Fragment {


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupView();
        setupAdapter();
        setupEvent();

    }


    protected void setupView() {

    }

    protected void setupAdapter() {

    }

    protected void setupEvent() {

    }

    protected void finish() {
        if (getFragmentManager()!=null){
            FragmentTransaction mFragmentTransaction= getFragmentManager().beginTransaction();
            mFragmentTransaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
            mFragmentTransaction.remove(this);


            mFragmentTransaction.commit();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        ImageLoader.getInstance().pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        ImageLoader.getInstance().resume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
