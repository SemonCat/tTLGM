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


    public static final String SubjectFileName = "/ClassData.xml";
    public static final String SubjectDataPath = TLGMPath + SubjectFileName;

    /**ppt**/
    public static final String PPTDir = "/PPT";
    public static final String PPTPath = TLGMPath + PPTDir;

    /**pdf**/
    public static final String PDFDir = "/PDF";
    public static final String PDFPath = TLGMPath + PDFDir;
}
