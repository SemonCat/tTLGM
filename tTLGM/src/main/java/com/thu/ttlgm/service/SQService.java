package com.thu.ttlgm.service;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.thu.ttlgm.bean.Student;
import com.thu.ttlgm.bean.StudentColor;
import com.thu.ttlgm.utils.ConstantUtil;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
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

    private static List<Student> cacheStudnetList = new ArrayList<Student>();

    public interface OnAllStudentGetListener{
        void OnAllStudentGetEvent(List<Student> mStudentList);
    }

    public interface OnHpInfoGetListener{
        void OnHpInfoGetEvent(List<Student> mStudentList);
    }

    public interface OnStudentGetListener{
        void OnStudentGetEvent(List<Student> mStudentList);
    }

    public interface OnStudentColorGetListener{
        void OnStudentColorGetEvent(HashSet<StudentColor> mStudentList);
    }

    public static void startServer(){
        String URL = ServerIP+"/ServerSetting?service=reload";
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(URL,new AsyncHttpResponseHandler(){});
    }

    public static void startHpServer(int hpMinTxt){
        String URL = ServerIP+"/HpInterface?service=1&mins="
                + hpMinTxt;
        AsyncHttpClient client = new AsyncHttpClient();

        resumeHpServer();
        client.get(URL,new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (responseBody!=null){
                    String message = new String(responseBody);
                    Log.d(TAG,"Success:"+message);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (responseBody!=null){
                    String message = new String(responseBody);
                    Log.d(TAG,"Failure:"+message);
                }
            }
        });
    }

    public static void pauseHpServer(){
        String URL = ServerIP+"/HpInterface?service=6";
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(URL,new AsyncHttpResponseHandler(){});
    }

    public static void resumeHpServer(){
        String URL = ServerIP+"/HpInterface?service=7";
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(URL,new AsyncHttpResponseHandler(){});
    }

    public static void AddStudentCoin(String Sid,int coin){
        String URL = ServerIP+"/AddStudentCoin?sid="+Sid+"&coin="+coin;
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

    private static AsyncHttpClient StudentHttpClient;
    public static void getAllStudents(final OnAllStudentGetListener mListener){
        String URL = ServerIP+"/TeacherQuery?service=3";
        if (StudentHttpClient==null){
            StudentHttpClient = new AsyncHttpClient();
        }

        StudentHttpClient.get(URL,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(responseBody));

                    StudentInfoMap infoMap = (StudentInfoMap) objIn.readObject();

                    List<Student> mData = transStudentInfoMap2StudentBean(infoMap);

                    if (cacheStudnetList.size()!=0){
                        cacheStudnetList.clear();
                    }
                    cacheStudnetList = mData;

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
                //Log.d(TAG,"Error:"+new String(responseBody));
            }
        });

    }

    private static void getAllStudentHp(final List<Student> srcList,final OnAllStudentGetListener mListener){

        String URL = ServerIP+"/TeacherQuery?service=1";
        if (StudentHttpClient==null){
            StudentHttpClient = new AsyncHttpClient();
        }

        StudentHttpClient.get(URL,new AsyncHttpResponseHandler(){
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
                //Log.d(TAG,"Error:"+new String(responseBody));
            }
        });
    }

    public static void getCacheStudentList(final OnStudentGetListener mListener){
        if (cacheStudnetList.size()==0 || cacheStudnetList.isEmpty()){
            getAllStudents(new OnAllStudentGetListener() {
                @Override
                public void OnAllStudentGetEvent(List<Student> mStudentList) {
                    if (mListener!=null)
                        mListener.OnStudentGetEvent(new ArrayList<Student>(mStudentList));
                }
            });
        }else{

            if (mListener!=null)
                mListener.OnStudentGetEvent(new ArrayList<Student>(cacheStudnetList));
        }
    }

    public static void findAllColor(List<Student> mStudentList,final OnStudentColorGetListener mListener){

        AsyncHttpClient client = new AsyncHttpClient();

        final HashSet<StudentColor> mData = new HashSet<StudentColor>();

        for (final Student mStudent:mStudentList){
            String URL = ServerIP+"/FindStudentColor?sid="+mStudent.getID();

            client.get(URL,new AsyncHttpResponseHandler(){

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    String message[] = new String(responseBody).split(",");

                    if (message.length==2){
                        StudentColor mStudentColor = new StudentColor(mStudent);
                        mStudentColor.setColor(Integer.valueOf(message[1]));
                        mData.add(mStudentColor);
                    }

                    if (mListener!=null)
                        mListener.OnStudentColorGetEvent(mData);
                }
            });

        }
    }

    public static Student findStudentBySID(List<Student> mStudentList,String SID){
        for (Student mStudent:mStudentList){
            if (mStudent.getID().equals(SID)){
                return mStudent;
            }
        }

        return null;
    }

    public static int findColorBySID(HashSet<StudentColor> mStudentList,String SID){
        for (StudentColor mStudent:mStudentList){
            if (mStudent.getID().equals(SID)){
                return mStudent.getColor();
            }
        }

        return -1;
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
