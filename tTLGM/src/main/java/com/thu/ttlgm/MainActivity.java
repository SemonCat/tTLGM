package com.thu.ttlgm;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import com.thu.ttlgm.bean.*;
import com.thu.ttlgm.bean.Class;
import com.thu.ttlgm.component.UImageButton;
import com.thu.ttlgm.floatwindows.FloatWindowsService;
import com.thu.ttlgm.fragment.ClassChooserFragment;
import com.thu.ttlgm.fragment.GameFragment;
import com.thu.ttlgm.fragment.ResourcePickerFragment;
import com.thu.ttlgm.fragment.StudentsFragment;
import com.thu.ttlgm.utils.DataParser;
import com.thu.ttlgm.utils.ViewUtil;
import com.thu.ttlgm.websocket.WebSocketClient;

import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getName();

    private Class mCurrentClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //SetupDefault
        replaceFragment(new ClassChooserFragment(),ClassChooserFragment.class.getName());

        //StartFloat
        startService(new Intent(this, FloatWindowsService.class));
    }

    @Override
    protected int setupLayout() {
        return R.layout.activity_main;
    }

    public void toHome(View mView){
        replaceFragment(new ClassChooserFragment(),ClassChooserFragment.class.getName());
    }

    public void toStudents(View mView){
        replaceFragment(new StudentsFragment(),StudentsFragment.class.getName());

    }

    public void toWorks(View mView){

    }

    public void toFiles(View mView){
        replaceFragment(new ResourcePickerFragment(),ResourcePickerFragment.class.getName());
    }

    public void toGames(View mView){
        replaceFragment(new GameFragment(),GameFragment.class.getName());
    }

    private void replaceFragment(Fragment mFragment,String TAG){


        FragmentTransaction transaction =getFragmentManager().beginTransaction();
        Fragment findFragment = getFragmentManager().findFragmentByTag(TAG);
        if (findFragment!=null){
            return;
        }
        transaction.replace(R.id.Fragment_Content, mFragment,TAG);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        transaction.commit();

    }


    public Class getCurrentClass() {
        return mCurrentClass;
    }

    public void setCurrentClass(Class mCurrentClass) {
        this.mCurrentClass = mCurrentClass;
    }

}
