package com.thu.ttlgm;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

/**
 * Created by SemonCat on 2014/1/11.
 */
public class BaseActivity extends Activity {

    private Handler mHideHandler = new Handler();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setupLayout());

        // hide the navigation bar

        hideSystemUi();


        // register a listener for when the navigation bar re-appears
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(

                new View.OnSystemUiVisibilityChangeListener() {

                    @Override

                    public void onSystemUiVisibilityChange(int visibility) {

                        if (visibility == 0) {

                            // the navigation bar re-appears, let’s hide it
                            // after 2 seconds
                            mHideHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hideSystemUi();
                                }
                            }, 2000);

                        }

                    }

                });

        setupView();
        setupEvent();
    }

    protected int setupLayout() {
        return 0;
    }

    protected void setupView() {

    }

    protected void setupEvent() {

    }

    private void hideSystemUi() {

        getWindow().getDecorView().setSystemUiVisibility(

                View.SYSTEM_UI_FLAG_LAYOUT_STABLE

                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

                        | View.SYSTEM_UI_FLAG_FULLSCREEN

                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

    }

}
