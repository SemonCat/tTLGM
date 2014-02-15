package com.thu.ttlgm;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;

import com.thu.ttlgm.bean.Class;
import com.thu.ttlgm.floatwindow.FloatWindowService;
import com.thu.ttlgm.fragment.ClassChooserFragment;
import com.thu.ttlgm.fragment.GameFragment;
import com.thu.ttlgm.fragment.ResourcePickerFragment;
import com.thu.ttlgm.fragment.StudentsFragment;

import aidl.IFloatWindowService;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getName();

    private Class mCurrentClass;

    private ServiceConnection mConnection;
    private IFloatWindowService mIFloatWindowService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //SetupDefault
        replaceFragment(new ClassChooserFragment(),ClassChooserFragment.class.getName());

        setupService();
    }

    @Override
    protected int setupLayout() {
        return R.layout.activity_main;
    }

    private void setupService(){
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mIFloatWindowService = IFloatWindowService.Stub.asInterface(iBinder);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        bindService(new Intent(this, FloatWindowService.class), mConnection,BIND_AUTO_CREATE);
        //StartFloat
        startService(new Intent(this, FloatWindowService.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mIFloatWindowService!=null){

            try {
                mIFloatWindowService.setVisible(false);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIFloatWindowService!=null){
            try {
                mIFloatWindowService.setVisible(true);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void toHome(View mView){
        replaceFragment(new ClassChooserFragment(), ClassChooserFragment.class.getName());
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
