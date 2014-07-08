package com.thu.ttlgm;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.thu.ttlgm.bean.Class;
import com.thu.ttlgm.component.HackyDrawerLayout;
import com.thu.ttlgm.component.HpControler;
import com.thu.ttlgm.floatwindow.FloatWindowService;
import com.thu.ttlgm.fragment.AlbumFragment;
import com.thu.ttlgm.fragment.ClassChooserFragment;
import com.thu.ttlgm.fragment.ClassInfoFragment;
import com.thu.ttlgm.fragment.GameFragment;
import com.thu.ttlgm.fragment.GroupGameFragment;
import com.thu.ttlgm.fragment.RandomFragment;
import com.thu.ttlgm.fragment.ResourcePickerFragment;
import com.thu.ttlgm.fragment.StudentsFragment;
import com.thu.ttlgm.fragment.UnitFragment;
import com.thu.ttlgm.fragment.UnitIndexFragment;
import com.thu.ttlgm.fragment.WhiteBoardFragment;
import com.thu.ttlgm.fragment.vote.StatisticFragment;
import com.thu.ttlgm.input.GestureListener;
import com.thu.ttlgm.service.FacebookAlbumUtils;
import com.thu.ttlgm.service.SQService;
import com.thu.ttlgm.utils.SharedPreferencesUtils;

import aidl.IFloatWindowService;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getName();

    private Class mCurrentClass;

    private HackyDrawerLayout mDrawerLayout;

    private ServiceConnection mConnection;
    private IFloatWindowService mIFloatWindowService;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupDrawer();

        //SetupDefault
        replaceFragment(new UnitIndexFragment(), UnitIndexFragment.class.getName());

        setupService();

        SQService.startServer();

        //快取圖片
        RandomFragment.LoadCache();

        setupMerlin();
    }

    private void setupMerlin(){
/*

        merlin = new Merlin.Builder()
                .withDisconnectableCallbacks()
                .withConnectableCallbacks()
                .build(context);
        merlin.registerConnectable(new Connectable() {
            @Override
            public void onConnect() {
                Log.d(TAG,"連線回復");
                ShowToast("連線回復。");
            }
        });

        merlin.registerDisconnectable(new Disconnectable() {
            @Override
            public void onDisconnect() {
                Log.d(TAG,"連線中斷");
                ShowToast("連線中斷。");
            }
        });
*/
    }

    private void setupDrawer() {
        mDrawerLayout = (HackyDrawerLayout) findViewById(R.id.Navi_Drawer);
        setupHpControl();
    }

    @Override
    protected void setupHpControl(){
        mHpControler = new HpControler(this,mDrawerLayout);
        mHpControler.setListener(this);
    }


    @Override
    protected int setupLayout() {
        return R.layout.activity_main;
    }

    private void setupService() {
        /*
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mIFloatWindowService = IFloatWindowService.Stub.asInterface(iBinder);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };

        //bindService(new Intent(this, FloatWindowService.class), mConnection,BIND_AUTO_CREATE);
        //StartFloat
        //startService(new Intent(this, FloatWindowService.class));
        */
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mIFloatWindowService != null) {

            try {
                mIFloatWindowService.setVisible(false);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        FacebookAlbumUtils.DestroyAll();

        //merlin.unbind();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIFloatWindowService != null) {
            try {
                mIFloatWindowService.setVisible(true);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        //快取相簿
        FacebookAlbumUtils.LoadAlbumCache();

        //merlin.bind();
    }


    public void toHome(View mView) {
        clearFragmentBackStack();
        replaceFragment(new UnitIndexFragment(), UnitIndexFragment.class.getName());
        mDrawerLayout.closeDrawers();
        HideDrawer();
    }

    public void toUnit(View mView) {
        clearFragmentBackStack();
        replaceFragment(new UnitFragment(mCurrentClass), ClassInfoFragment.class.getName());
        mDrawerLayout.closeDrawers();
        HideDrawer();
    }

    public void toStudents(View mView) {
        replaceFragment(new StudentsFragment(), StudentsFragment.class.getName());
        mDrawerLayout.closeDrawers();
        HideDrawer();
    }

    public void toWorks(View mView) {
        replaceFragment(new AlbumFragment(), AlbumFragment.class.getName());
        mDrawerLayout.closeDrawers();
        HideDrawer();
    }

    public void toFiles(View mView) {
        replaceFragment(new ResourcePickerFragment(), ResourcePickerFragment.class.getName());
        mDrawerLayout.closeDrawers();
        HideDrawer();
    }

    public void toGroupGame(View mView) {
        replaceFragment(new GroupGameFragment(), GroupGameFragment.class.getName());
        mDrawerLayout.closeDrawers();
        HideDrawer();
    }

    public void toRandom(View mView) {
        addFragment(new RandomFragment(), RandomFragment.class.getName());
        mDrawerLayout.closeDrawers();
        HideDrawer();

    }

    public void toWhiteBoard(View mView) {
        replaceFragment(new WhiteBoardFragment(), WhiteBoardFragment.class.getName());
        mDrawerLayout.closeDrawers();
        HideDrawer();
    }


    public void toVote(View mView) {
        replaceFragment(new StatisticFragment("对贵公司而言，您认为哪项最复杂？",
                new String[]{"认知复杂",
                        "软件复杂",
                        "实施复杂",
                        "管理复杂"}), StatisticFragment.class.getName());
        mDrawerLayout.closeDrawers();
        HideDrawer();
    }

    public void toVote2(View mView) {
        replaceFragment(new StatisticFragment("你认为ABC哪个对你目前最重要？",
                new String[]{"A",
                        "B",
                        "C"
                }), StatisticFragment.class.getName()+"2");
        mDrawerLayout.closeDrawers();
        HideDrawer();
    }


    public Class getCurrentClass() {
        return mCurrentClass;
    }

    public void setCurrentClass(Class mCurrentClass) {
        this.mCurrentClass = mCurrentClass;
    }

    @Override
    protected void onDestroy() {
        //FacebookAlbumUtils.DestroyAll();
        super.onDestroy();

    }
}
