package com.thu.ttlgm.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by SemonCat on 2014/3/12.
 */
public class SharedPreferencesUtils {

    private static final String WEEK = "Week";


    private static final String PPT_PAGE = "PPT_Page";

    private static final String PPT_PAGE_WEEK = "PPT_Page_WEEK_";

    public static void setWeek(Context mContext,int week){
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mSharedPreferences.edit().putInt(WEEK,week).commit();

    }

    public static int getWeek(Context mContext){
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        return mSharedPreferences.getInt(WEEK,1);
    }

    public static void setPPTPage(Context mContext,int page){
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mSharedPreferences.edit().putInt(PPT_PAGE,page).commit();

    }

    public static int getPPTPage(Context mContext){
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        return mSharedPreferences.getInt(PPT_PAGE,0);
    }

    public static void setPPTPage(Context mContext,int week,int page){
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        /*
        List<String> mPPTPage = new ArrayList<String>();
        mPPTPage.set(week,String.valueOf(page));

        mSharedPreferences.edit().putStringSet(PPT_PAGE,new HashSet<String>(mPPTPage));\
        */
        mSharedPreferences.edit().putInt(PPT_PAGE_WEEK+week,page).commit();

    }

    public static int getPPTPage(Context mContext,int week){
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        /*
        List<String> mPPTPage = new ArrayList<String>(mSharedPreferences.getStringSet(PPT_PAGE,new HashSet<String>()));

        try{
           return Integer.valueOf(mPPTPage.get(week));
        }catch (Exception e){

        }
        */
        return mSharedPreferences.getInt(PPT_PAGE_WEEK+week,0);

    }

}
