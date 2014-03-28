package com.thu.ttlgm.service;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.facebook.FacebookException;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.widget.WebDialog;
import com.thu.ttlgm.R;
import com.thu.ttlgm.utils.ConstantUtil;

import java.util.Arrays;

/**
 * Created by SemonCat on 2014/3/27.
 */
public class FacebookMgr {
    public interface FacebookMgrListener{
        void OnLoginCancel();
    }
    private static final String TAG = FacebookMgr.class.getName();

    private Activity mActivity;

    private FacebookMgrListener mListener;

    public FacebookMgr(Activity activity) {
        this.mActivity = activity;



    }

    /**
     * 登入程序
     */
    public void Login(){

        if (IsLogin()){
            return;
        }

        //準備登入Dialog
        WebDialog localWebDialog = new WebDialog
                .Builder(mActivity, mActivity.getString(R.string.app_id), "oauth", null)
                .build();


        //設定登入完成CallBack
        localWebDialog.setOnCompleteListener(new WebDialog.OnCompleteListener()
        {
            public void onComplete(Bundle bundle, FacebookException facebookException)
            {
                if (bundle!=null){
                    //登入成功
                    Session.getActiveSession();
                    AccessToken localAccessToken = AccessToken.
                            createFromExistingAccessToken(bundle.getString("access_token"), null, null, AccessTokenSource.WEB_VIEW, null);
                    //打開Session
                    Session.openActiveSessionWithAccessToken(
                            mActivity.getApplicationContext(),
                            localAccessToken,
                            new Session.StatusCallback() {
                                @Override
                                public void call(Session session, SessionState sessionState, Exception e) {

                                }
                            });
                }else{
                    Log.d(TAG,"User Cancel Dialog");
                    if (mListener!=null){
                        mListener.OnLoginCancel();
                    }
                }

            }
        });




        localWebDialog.show();
    }

    public void PostPhoto(String AlbumId,String message,byte[] photo){



        Bundle mBundle = new Bundle();
        mBundle.putString("message", message);
        mBundle.putByteArray("source", photo);


        new Request(Session.getActiveSession(),
                "/"+ AlbumId+"/photos",
                mBundle,
                HttpMethod.POST,
                new Request.OnProgressCallback() {
                    @Override
                    public void onProgress(long current, long max) {

                    }

                    @Override
                    public void onCompleted(Response response) {
                        Log.d(TAG, response.toString());
                    }
                }
        ).executeAndWait();
    }

    public void setListener(FacebookMgrListener mListener) {
        this.mListener = mListener;
    }

    public boolean IsLogin(){
        return (Session.getActiveSession()!=null && Session.getActiveSession().isOpened());
    }
}
