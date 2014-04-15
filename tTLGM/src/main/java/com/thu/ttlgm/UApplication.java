package com.thu.ttlgm;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Build;
import android.webkit.CookieSyncManager;

import com.bugsense.trace.BugSenseHandler;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LargestLimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.thu.ttlgm.utils.ConstantUtil;

/**
 * Created by SemonCat on 2014/3/5.
 */
public class UApplication extends Application {
    private Thread.UncaughtExceptionHandler defaultUEH;

    // handler listener
    private Thread.UncaughtExceptionHandler _unCaughtExceptionHandler =
            new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable ex) {

                    BugSenseHandler.sendException(new Exception(ex));


                    // re-throw critical exception further to the os (important)
                    defaultUEH.uncaughtException(thread, ex);
                }
            };


    @Override
    public void onCreate() {
        setupBugSense();
        super.onCreate();

        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(
                        new DisplayImageOptions.Builder()
                                .showImageOnLoading(R.drawable.obj_o_picloading)
                                .cacheInMemory(true)
                                .cacheOnDisc(true)
                                //.displayer(new FadeInBitmapDisplayer(300))
                                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                                .build()
                )
                .memoryCache(new WeakMemoryCache())
                .threadPoolSize(5) // default
                .build();// Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    private void setupBugSense() {
        BugSenseHandler.initAndStartSession(this, ConstantUtil.BugSenseApiKey);

        BugSenseHandler.addCrashExtraData("Device", Build.MODEL);

        defaultUEH = Thread.getDefaultUncaughtExceptionHandler();

        // setup handler for uncaught exception
        Thread.setDefaultUncaughtExceptionHandler(_unCaughtExceptionHandler);
    }


    @Override
    public void onTerminate() {
        BugSenseHandler.closeSession(this);

        super.onTerminate();
    }

}
