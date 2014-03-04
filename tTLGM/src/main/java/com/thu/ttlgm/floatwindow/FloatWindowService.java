package com.thu.ttlgm.floatwindow;

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

import aidl.IFloatWindowService;

/**
 * Created by SemonCat on 2014/1/22.
 */
public class FloatWindowService extends Service {
    private WindowManager windowManager;
    private FloatLayout mFloatLayout;

    private FloatImageView mFloatImageView;

    private int mRingMargin = 5;
    private int mCenterRingSize = 35;
    private int mInnerRingSize = 75;
    private int mOuterRingSize = 75;


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);


        mFloatLayout = new FloatLayout(this);
        mFloatLayout.setVisibility(View.GONE);


        final WindowManager.LayoutParams mFloatLayoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);


        windowManager.addView(mFloatLayout, mFloatLayoutParams);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);


        params.y = 20;
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

    public RadialMenuItem mFirstMenuScreen, menuCloseItem, mFirstMenuElse;
    public RadialMenuItem mSecondMenuScreenToggle, mSecondMenuScreenSwitch;
    public RadialMenuItem mSecondMenuElseBlood, mSecondMenuElseCamera, mSecondMenuElseWhiteboard;
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
        mFirstMenuScreen = new RadialMenuItem("投影",
                "投影");

        mSecondMenuScreenToggle = new RadialMenuItem("投影畫面","投影畫面");
        mSecondMenuScreenToggle.setDisplayIcon(R.drawable.screen);
        mSecondMenuScreenToggle.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
            @Override
            public void execute() {
                pieMenu.dismiss();
            }
        });

        mSecondMenuScreenSwitch = new RadialMenuItem("畫面切換","畫面切換");
        mSecondMenuScreenSwitch.setDisplayIcon(R.drawable.switch_screen);
        mSecondMenuScreenSwitch.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
            @Override
            public void execute() {
                pieMenu.dismiss();
            }
        });

        ArrayList<RadialMenuItem> mFirstMenuScreenChilds = new ArrayList<RadialMenuItem>();
        mFirstMenuScreenChilds.add(mSecondMenuScreenToggle);
        mFirstMenuScreenChilds.add(mSecondMenuScreenSwitch);

        mFirstMenuScreen.setMenuChildren(mFirstMenuScreenChilds);

        mSecondMenuElseBlood = new RadialMenuItem("補血",
                "補血");
        mSecondMenuElseBlood.setDisplayIcon(R.drawable.blood);
        mSecondMenuElseBlood
                .setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
                    @Override
                    public void execute() {

                        pieMenu.dismiss();
                    }
                });

        mSecondMenuElseCamera = new RadialMenuItem("拍照", "拍照");
        mSecondMenuElseCamera.setDisplayIcon(R.drawable.camera);
        mSecondMenuElseCamera
                .setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
                    @Override
                    public void execute() {

                        pieMenu.dismiss();
                    }
                });

        mSecondMenuElseWhiteboard = new RadialMenuItem("白板", "白板");
        mSecondMenuElseWhiteboard.setDisplayIcon(R.drawable.whiteboard);

        mSecondMenuElseWhiteboard
                .setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
                    @Override
                    public void execute() {

                        pieMenu.dismiss();
                    }
                });

        mFirstMenuElse = new RadialMenuItem("其他", "其他");
        children.add(mSecondMenuElseBlood);
        children.add(mSecondMenuElseCamera);
        children.add(mSecondMenuElseWhiteboard);
        mFirstMenuElse.setMenuChildren(children);

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

        pieMenu.setInnerRingColor(getResources().getColor(R.color.PieFirstMenuScreen), 180);
        pieMenu.setOuterRingColor(getResources().getColor(R.color.PieSecondMenuElseBlood), 180);
        pieMenu.setCenterCircle(menuCloseItem);

        pieMenu.addMenuEntry(new ArrayList<RadialMenuItem>() {
            {
                add(mFirstMenuScreen);
                add(mFirstMenuElse);
            }
        });


    } 


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatLayout != null) windowManager.removeView(mFloatLayout);
        if (mFloatImageView != null) windowManager.removeView(mFloatImageView);
    }

    private IFloatWindowService.Stub mBinder = new IFloatWindowService.Stub() {
        @Override
        public void setVisible(boolean visible) throws RemoteException {

            if (visible)
                mFloatImageView.setVisibility(View.VISIBLE);
            else
                mFloatImageView.setVisibility(View.GONE);
        }
    };
}
