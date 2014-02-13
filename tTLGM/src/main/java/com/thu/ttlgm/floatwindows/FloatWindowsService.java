package com.thu.ttlgm.floatwindows;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.thu.ttlgm.R;
import com.thu.ttlgm.component.Radial.RadialMenuItem;
import com.thu.ttlgm.component.Radial.RadialMenuWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SemonCat on 2014/1/22.
 */
public class FloatWindowsService extends Service {
    private WindowManager windowManager;
    private FloatLayout mFloatLayout;

    private ImageView mIcon;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);


        mFloatLayout = new FloatLayout(this);
        mFloatLayout.setVisibility(View.GONE);
        mIcon = new ImageView(this);
        mIcon.setImageResource(R.drawable.bubble);
        mIcon.setPadding(20,20,20,20);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = 0;

        windowManager.addView(mFloatLayout, params);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        windowManager.addView(mIcon, params);


        mIcon.setOnTouchListener(new View.OnTouchListener() {
            private WindowManager.LayoutParams paramsF = params;
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            private int lastX,lastY,right,left,top,bottom;
            private float x1,y1,x2,y2;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                int screenWidth = getResources().getDisplayMetrics().widthPixels;
                int screenHeight = getResources().getDisplayMetrics().heightPixels;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        x1 =  event.getRawX();//得到相對應屏幕左上角的坐標
                        y1 =  event.getRawY();
                        // Get current time in nano seconds.

                        initialX = paramsF.x;
                        initialY = paramsF.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getRawX();
                        y2 = event.getRawY();
                        Log.i("i", x1 + ",,," + y1 +",,,"+x2+",,,"+y2);
                        double distance = Math.sqrt(Math.abs(x1-x2)*Math.abs(x1-x2)+Math.abs(y1-y2)*Math.abs(y1-y2));//兩點之間的距離
                        Log.i("i", "x1 - x2>>>>>>"+ distance);
                        if (distance < 30)
                            mIcon.performClick();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        paramsF.x = initialX + (int) (event.getRawX() - initialTouchX);
                        paramsF.y = initialY + (int) (event.getRawY() - initialTouchY);

                        windowManager.updateViewLayout(v, paramsF);

                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;

                        left = v.getLeft() + dx;
                        top = v.getTop() + dy;
                        right = v.getRight() + dx;
                        bottom = v.getBottom() + dy;
                        if (left < 0) {
                            left = 0;
                            right = left + v.getWidth();
                        }
                        if (right > screenWidth) {
                            right = screenWidth;
                            left = right - v.getWidth();
                        }
                        if (top < 0) {
                            top = 0;
                            bottom = top + v.getHeight();
                        }
                        if (bottom > screenHeight) {
                            bottom = screenHeight;
                            top = bottom - v.getHeight();
                        }
                        Log.i("i", "值：" + left +">>>"+ top+">>>"+right+">>>"+bottom);
                        v.layout(left, top, right, bottom);
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();

                        break;
                }
                return true;
            }
        });

        mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mFloatLayout.setVisibility(View.VISIBLE);
                pieMenu.show(mFloatLayout, Gravity.CENTER, 0, 0);
                mIcon.setVisibility(View.GONE);

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
                mIcon.setVisibility(View.VISIBLE);

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

        // pieMenu.setDismissOnOutsideClick(true, menuLayout);
        pieMenu.setAnimationSpeed(300);


        pieMenu.setIconSize(30, 50);
        pieMenu.setTextSize(20);
        pieMenu.setOutlineColor(Color.BLACK, 225);
        pieMenu.setInnerRingRadius(60,180);
        pieMenu.setCenterCircleRadius(50);
        pieMenu.setOuterRingRadius(190,250);
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
        if (mIcon != null) windowManager.removeView(mIcon);
    }
}
