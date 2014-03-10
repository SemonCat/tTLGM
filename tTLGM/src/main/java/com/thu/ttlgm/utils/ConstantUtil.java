package com.thu.ttlgm.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by SemonCat on 2014/1/16.
 */
public class ConstantUtil {
    public static final String SDCardPath = Environment.getExternalStorageDirectory().toString();
    public static final String TLGMPath = SDCardPath + File.separator +
            "TLGM";


    public static final String ServerIP = "http://54.214.24.26:8080/StudentSquare_server";


    public static final String SubjectFileName = "/ClassData.xml";
    public static final String SubjectDataPath = TLGMPath + SubjectFileName;

    /**ppt**/
    public static final String PPTDir = "/PPT";
    public static final String PPTPath = TLGMPath + PPTDir;

    /**pdf**/
    public static final String PDFDir = "/PDF";
    public static final String PDFPath = TLGMPath + PDFDir;

    /**Facebook**/
    public static final String Facebook = "https://www.facebook.com/";

    public static final String GroupId = "415585308579029";

    public static final String WeekAlbum = "415593318578228";
}
