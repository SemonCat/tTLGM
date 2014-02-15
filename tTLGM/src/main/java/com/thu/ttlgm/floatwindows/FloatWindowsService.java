package com.thu.ttlgm.floatwindows;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.thu.ttlgm.R;
import com.thu.ttlgm.component.Radial.RadialMenuItem;
import com.thu.ttlgm.component.Radial.RadialMenuWidget;

import java.util.ArrayList;
import java.util.List;

import aidl.IFloatWindowsService;

/**
 * Created by SemonCat on 2014/1/22.
 */
public class FloatWindowsService extends Service {
    private WindowManager windowManager;
    private FloatLayout mFloatLayout;

    private FloatImageView mFloatImageView;

    private int mRingMargin = 5;
    private int mCenterRingSize = 25;
    private int mInnerRingSize = 50;
    private int mOuterRingSize = 50;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);


        mFloatLayout = new FloatLayout(this);
        mFloatLayout.setVisibility(View.GONE);


        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);



        params.gravity = Gravity.CENTER;
        params.y = 20;

        windowManager.addView(mFloatLayout, params);
        params.gravity = Gravity.TOP | Gravity.LEFT;

        mFloatImageView = new FloatImageView(this,windowManager,params);
        mFloatImageView.setImageResource(R.drawable.bubble);


        windowManager.addView(mFloatImageView, params);


        mFloatImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mFloatLayout.setVisibility(View.VISIBLE);
                pieMenu.show(mFloatLayout, Gravity.CENTER, 0, 0);
                mFloatImageView.setVisibility(View.GONE);

            }
        });




        setupRadiaMenu();
    }

    private RadialMenuWidget pieMenu;

    public RadialMenuItem menuItem, menuCloseItem, menuExpandItem;
    public RadialMenuItem firstChildItem, secondChildItem, thirdChildItem;
    private List<RadialMenuItem> children = new ArrayList<RadialMenuItem>();

    private void setupRadiaMenu() {
        pieMenu = new RadialMenuWidget(this);

        pieMenu.setEventListener(new RadialMenuWidget.RadialEventListener() {
            @Override
            public void OnDismiss() {
                mFloatImageView.setVisibility(View.VISIBLE);
                mFloatLayout.setVisibility(View.GONE);
            }
        });

        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);

        pieMenu.setCenterLocation(metrics.heightPixels / 2, metrics.heightPixels / 2);
        pieMenu.setSourceLocation(0, 0);
        menuCloseItem = new RadialMenuItem("close", null);
        menuCloseItem
                .setDisplayIcon(android.R.drawable.ic_menu_close_clear_cancel);
        menuItem = new RadialMenuItem("test",
                "test");
        menuItem.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
            @Override
            public void execute() {
                pieMenu.dismiss();
            }
        });

        firstChildItem = new RadialMenuItem("test",
                "test");
        firstChildItem
                .setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
                    @Override
                    public void execute() {

                        pieMenu.dismiss();
                    }
                });

        secondChildItem = new RadialMenuItem("test", "test");
        secondChildItem.setDisplayIcon(R.drawable.ic_launcher);
        secondChildItem
                .setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
                    @Override
                    public void execute() {

                        pieMenu.dismiss();
                    }
                });

        thirdChildItem = new RadialMenuItem("test", "test");
        thirdChildItem.setDisplayIcon(R.drawable.ic_launcher);
        thirdChildItem
                .setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
                    @Override
                    public void execute() {

                        pieMenu.dismiss();
                    }
                });

        menuExpandItem = new RadialMenuItem("test", "test");
        children.add(firstChildItem);
        children.add(secondChildItem);
        children.add(thirdChildItem);
        menuExpandItem.setMenuChildren(children);

        menuCloseItem
                .setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
                    @Override
                    public void execute() {
                        // menuLayout.removeAllViews();
                        pieMenu.dismiss();
                    }
                });

        pieMenu.setAnimationSpeed(300);


        pieMenu.setIconSize(30, 50);
        pieMenu.setTextSize(20);
        pieMenu.setOutlineColor(Color.BLACK, 225);
        pieMenu.setCenterCircleRadius(mCenterRingSize);
        pieMenu.setInnerRingRadius(mCenterRingSize+mRingMargin, mCenterRingSize+mRingMargin+mInnerRingSize);
        pieMenu.setOuterRingRadius(mCenterRingSize+mRingMargin+mInnerRingSize+mRingMargin,mCenterRingSize+mRingMargin+mInnerRingSize+mRingMargin+ mOuterRingSize);

        //pieMenu.setInnerRingColor(0xAA66CC, 180);
        //pieMenu.setOuterRingColor(0x0099CC, 180);
        pieMenu.setCenterCircle(menuCloseItem);

        pieMenu.addMenuEntry(new ArrayList<RadialMenuItem>() {
            {
                add(menuItem);
                add(menuExpandItem);
            }
        });


    } 


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatLayout != null) windowManager.removeView(mFloatLayout);
        if (mFloatImageView != null) windowManager.removeView(mFloatImageView);
    }

    private IFloatWindowsService.Stub mBinder = new IFloatWindowsService.Stub() {
        @Override
        public void setVisible(boolean visible) throws RemoteException {

            if (visible)
                mFloatImageView.setVisibility(View.VISIBLE);
            else
                mFloatImageView.setVisibility(View.GONE);
        }
    };
}
