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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import serializable.StudentHp;
import serializable.StudentHpMap;
import serializable.StudentInfo;
import serializable.StudentInfoMap;

/**
 * Created by SemonCat on 2014/3/5.
 */
public class SQService {

    private final static String TAG = SQService.class.getName();

    private final static String ServerIP = ConstantUtil.ServerIP;

    public interface OnAllStudentGetListener{
        void OnAllStudentGetEvent(List<Student> mStudentList);
    }

    public interface OnHpInfoGetListener{
        void OnHpInfoGetEvent(List<Student> mStudentList);
    }

    public static void startServer(){
        String URL = ServerIP+"/ServerSetting?service=clear";
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(URL,new AsyncHttpResponseHandler(){});
    }

    public static void startQuestion(String quizId,String taskId){
        String URL = ServerIP+"/CoinInterface?service=1&taskId="+taskId+"&quizId="+quizId+"&called=all";
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(URL,new AsyncHttpResponseHandler(){});
    }

    public static void AnswerQuestion(String quizId,String taskId){
        String URL = ServerIP+"/CoinInterface?service=1&taskId="+taskId+"&quizId="+quizId+"&called=all";
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(URL,new AsyncHttpResponseHandler(){});
    }

    public static void getAllStudents(final OnAllStudentGetListener mListener){
        String URL = ServerIP+"/TeacherQuery?service=3";
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(URL,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(responseBody));

                    StudentInfoMap infoMap = (StudentInfoMap) objIn.readObject();

                    List<Student> mData = transStudentInfoMap2StudentBean(infoMap);


                    if (mListener!=null)
                        mListener.OnAllStudentGetEvent(mData);

                    getAllStudentHp(mData,mListener);

                }catch (IOException mIOException){
                    mIOException.printStackTrace();
                }catch (ClassNotFoundException mClassNotFoundException){
                    mClassNotFoundException.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(TAG,"Error:"+new String(responseBody));
            }
        });

    }

    private static void getAllStudentHp(final List<Student> srcList,final OnAllStudentGetListener mListener){

        String URL = ServerIP+"/TeacherQuery?service=1";
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(URL,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(responseBody));

                    StudentHpMap hpMap = (StudentHpMap) objIn.readObject();


                    Map<String, StudentHp> map = hpMap.getHpMap();
                    for (String key:map.keySet()){
                        StudentHp hp = map.get(key);
                        for (Student mStudent:srcList){

                            if (mStudent.getID().equals(hp.getSid())){
                                mStudent.setBlood(hp.getHpPercent());
                                mStudent.setLogin(true);
                            }
                        }
                    }

                    if (mListener!=null)
                        mListener.OnAllStudentGetEvent(srcList);
                }catch (IOException mIOException){
                    mIOException.printStackTrace();
                }catch (ClassNotFoundException mClassNotFoundException){
                    mClassNotFoundException.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(TAG,"Error:"+new String(responseBody));
            }
        });
    }

    public static List<Student> transStudentInfoMap2StudentBean(StudentInfoMap infoMap){
        List<Student> mData = new ArrayList<Student>();
        Map<String, StudentInfo> map = infoMap.getStudentMap();
        for (String key:map.keySet()){
            StudentInfo mInfo = map.get(key);
            mData.add(Student.toStudent(mInfo));
        }

        return mData;
    }
}
