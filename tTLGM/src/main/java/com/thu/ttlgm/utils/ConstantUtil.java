package com.thu.ttlgm.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by SemonCat on 2014/1/16.
 */
public class ConstantUtil {
    public static final String BugSenseApiKey = "0b6e3c1f";

    public static final String FacebookAccessToken =
            "1417907878451151|5Hw5asKx9VIV-M3XVHkwK4GwOic";

    public static final String SDCardPath = Environment.getExternalStorageDirectory().toString();
    public static final String TLGMPath = SDCardPath + File.separator +
            "TLGM";


    public static final String ImageHost = "http://tlgm.s3.amazonaws.com/";
    public static final String AWSAccessKeyID = "AKIAJ6OP4TD5JQRPWOBA";
    public static final String AWSSecretKey = "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";


    public static final String ServerIP = "http://54.214.24.26:8080/StudentSquare_server";

    public static final int TeacherAddCoin = 1;


    public static final int HpInterval = 40;

    public static final String SubjectFileName = "/ClassData.xml";
    public static final String SubjectDataPath = TLGMPath + SubjectFileName;

    public static final String WeekPath = "/Week";

    /**ppt**/
    public static final String PPTDir = "/PPT";

    /**pdf**/
    public static final String PDFDir = "/PDF";

    /**movie**/
    public static final String MovieDir = "/MOVIE";

    /**Facebook**/
    public static final String Facebook = "https://www.facebook.com/";

    public static final String GroupId = "415585308579029";

    public static final String WeekAlbum = "415593318578228";

    public static final String WhiteBoardAlbum = "426055220865371";
}
