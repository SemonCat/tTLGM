package com.thu.ttlgm.service;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.thu.ttlgm.bean.Student;
import com.thu.ttlgm.utils.ConstantUtil;

import org.apache.http.Header;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.Key;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import serializable.GroupMsg;

/**
 * Created by SemonCat on 2014/3/5.
 */
public class PollHandler {
    public interface OnMessageReceive{
        /**
         * 接收到學生回答
         * @param quizid
         * @param taskid
         * @param groupid
         */
        void OnMissionResultReceive(String quizid,String taskid,String groupid,String Answer);

        /**
         * 血量警示
         * @param blood 血量數據
         */
        void OnHpLow(String sid,int blood);

        /**
         * 獲得金創藥
         */
        void getAdditional(String sid);

        void getWhiteBoardImage(String URL);

    }
    private static final String TAG = PollHandler.class.getName();
    private static final int time = 3000;

    TimerTask mTimerTask;
    Timer mPollTimer;

    private static final String ServerIP = ConstantUtil.ServerIP;

    private static AsyncHttpClient client = new AsyncHttpClient();

    private List<OnMessageReceive> mListener;

    //常數
    private static final String RECEIVE_ANSWER = "$2";

    private static final String HP_INFO = "~4";

    private static final String ADDITIONAL = "additional";

    private static final String AmazonURL = "http://tlgm.s3.amazonaws.com/";

    private static final String WHITEBOARD = "#white";

    public PollHandler() {


        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                RunTask();
            }
        };

        mPollTimer = new Timer();


    }

    private void RunTask(){
        //Log.d(TAG,"Run");
        String URL = ServerIP+"/TeacherMessageBox";
        client.get(URL,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(responseBody));
                    GroupMsg gMsg = (GroupMsg) objIn.readObject();

                    // print
                    Map<String, ArrayList<String>> msgMap = gMsg.getMsg();
                    for (String key : msgMap.keySet()) {
                        ArrayList<String> myMsg = msgMap.get(key);

                        if (myMsg.size() == 0) {
                            continue;
                        }

                        String tmp = key + ":";
                        for (int i = 0; i < myMsg.size(); i++) {
                            tmp += myMsg.get(i) + " ";
                        }

                        Log.d(TAG,"Message:"+tmp);
                        //Log.d(TAG,"Key:"+key);

                    }

                    for (String key : msgMap.keySet()) {
                        ArrayList<String> myMsg = msgMap.get(key);

                        if (myMsg.size() == 0) {
                            continue;
                        }

                        String spiltMessage[] = myMsg.get(0).split(",");


                        //Log.d(TAG,"spiltMessage[1]:"+spiltMessage[1]);

                        if (spiltMessage[0].equals(HP_INFO)){
                            if (spiltMessage[1].equals(ADDITIONAL)){
                                //Log.d(TAG,"GetHpInfo,SID:"+ key+",getAdditional");
                                if (mListener!=null)
                                    for (OnMessageReceive listener:mListener)
                                        listener.getAdditional(key);
                            }else{
                                //Log.d(TAG,"GetHpInfo,SID:"+ key+",Blood:"+spiltMessage[1]);
                                if (mListener!=null)
                                    for (OnMessageReceive listener:mListener)
                                        listener.OnHpLow(key,Integer.valueOf(spiltMessage[1].replace("%","")));
                            }

                        }else if (spiltMessage[0].equals(RECEIVE_ANSWER)){

                            if (mListener!=null)
                                for (OnMessageReceive listener:mListener)
                                    listener.OnMissionResultReceive(spiltMessage[1],spiltMessage[2],key,spiltMessage[3]);

                        }else if (spiltMessage[0].equals(WHITEBOARD)){
                            if (mListener!=null)
                                for (OnMessageReceive listener:mListener)
                                    listener.getWhiteBoardImage(spiltMessage[1]);
                        }




                    }


                }catch (IOException mIOException){
                    Log.d(TAG, "Error:" + mIOException.getMessage());
                }catch (ClassNotFoundException mClassNotFoundException){
                    mClassNotFoundException.printStackTrace();
                    Log.d(TAG, "Error:" + mClassNotFoundException.getMessage());
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                Log.d(TAG, "Error Result:" + error.toString());

            }
        });
    }

    public void start(){
        mPollTimer.scheduleAtFixedRate(mTimerTask,time,time);
    }

    public void cancel(){
        mPollTimer.cancel();
    }


    public void setListener(OnMessageReceive listener) {
        if (mListener==null){
            mListener = new ArrayList<OnMessageReceive>();
        }
        mListener.add(listener);
    }
}
