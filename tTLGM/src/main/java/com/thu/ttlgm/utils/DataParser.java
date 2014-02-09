package com.thu.ttlgm.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.thu.ttlgm.bean.*;
import com.thu.ttlgm.bean.Class;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.RegistryMatcher;
import org.simpleframework.xml.transform.Transform;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by SemonCat on 2014/1/13.
 */
public class DataParser {

    public static Subject SubjectParser(Context mContext,File mFile){

        try {
            String TAG = "SubjectParser";

            /**解析XM<**/
            //使simpleXML可以解析中文慣用格式
            DateFormat format = new SimpleDateFormat("yyyy/MM/dd");

            RegistryMatcher m = new RegistryMatcher();
            m.bind(Date.class, new DateFormatTransformer(format));

            //開始解析XML
            Serializer mSerializer = new Persister(m);

            Subject mSubject = mSerializer.read(Subject.class,mFile);

            //Log.d(TAG,"Subject:"+mSubject.getTitle());
            //Log.d(TAG,"Class[0]:"+mSubject.getClassList().get(0).toString());

            return  mSubject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class DateFormatTransformer implements Transform<Date>
    {
        private DateFormat dateFormat;


        public DateFormatTransformer(DateFormat dateFormat)
        {
            this.dateFormat = dateFormat;
        }



        @Override
        public Date read(String value) throws Exception
        {
            return dateFormat.parse(value);
        }


        @Override
        public String write(Date value) throws Exception
        {
            return dateFormat.format(value);
        }

    }
}
