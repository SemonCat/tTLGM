package com.thu.ttlgm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.WindowManager.LayoutParams;

import com.thu.ttlgm.utils.PackageUtils;

import java.io.File;

/**
 * Created by SemonCat on 2014/4/21.
 */
public class UpdateActivity extends Activity {

    public static final String DOWNLOAD_FOLDER_NAME = "TLGM_APK";
    public static final String DOWNLOAD_FILE_NAME = "update.apk";

    public static final String APK_URL = "https://dl.dropboxusercontent.com/s/g7o1uojd1x7eo58/tTLGM.apk";
    private DownloadManager downloadManager;

    private long downloadId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        setFinishOnTouchOutside(false);

        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        registerReceiver(new CompleteReceiver(), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        downloadApk();

    }

    private void downloadApk() {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(APK_URL));
        request.setDestinationInExternalPublicDir(DOWNLOAD_FOLDER_NAME, DOWNLOAD_FILE_NAME);
        // request.allowScanningByMediaScanner();
        // request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        downloadId = downloadManager.enqueue(request);
    }

    class CompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            /**
             * get the id of download which have download success, if the id is my id and it's status is successful,
             * then install it
             **/
            long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (completeDownloadId == downloadId) {

                if (getStatusById(downloadId) == DownloadManager.STATUS_SUCCESSFUL) {
                    String apkFilePath =
                            new StringBuilder(Environment
                                    .getExternalStorageDirectory()
                                    .getAbsolutePath()).append(File.separator)
                                    .append(DOWNLOAD_FOLDER_NAME)
                                    .append(File.separator)
                                    .append(DOWNLOAD_FILE_NAME)
                                    .toString();

                    PackageUtils.install(UpdateActivity.this, apkFilePath);
                }else{
                    finish();
                }
            }
        }
    }

    public int getStatusById(long downloadId) {
        return getInt(downloadId, DownloadManager.COLUMN_STATUS);
    }


    private int getInt(long downloadId, String columnName) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        int result = -1;
        Cursor c = null;
        try {
            c = downloadManager.query(query);
            if (c != null && c.moveToFirst()) {
                result = c.getInt(c.getColumnIndex(columnName));
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return result;
    }

}
