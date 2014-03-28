package tests;

import android.os.Bundle;
import android.test.InstrumentationTestCase;
import android.util.Log;
import android.widget.Toast;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.thu.ttlgm.bean.Album;
import com.thu.ttlgm.bean.Photo;
import com.thu.ttlgm.service.FacebookAlbumUtils;
import com.thu.ttlgm.utils.ConstantUtil;
import com.thu.ttlgm.utils.StringUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SemonCat on 2014/3/24.
 */
public class FacebookTest extends InstrumentationTestCase{

    private static final String TAG = FacebookTest.class.getName();


    Pattern redirPtn = Pattern.compile("<(?:META|meta|Meta) (?:HTTP-EQUIV|http-equiv)=\"refresh\".*URL=(.*)\">");


    public void test() throws Exception {


        DefaultHttpClient httpclient = new DefaultHttpClient();

        httpclient.getParams().setParameter(
                CoreProtocolPNames.USER_AGENT,
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:28.0) Gecko/20100101 Firefox/28.0"
        );

        String FacebookURL = "https://www.facebook.com/groups/415585308579029/";
        HttpGet httpget = new HttpGet(FacebookURL);
        httpget.addHeader("Referer","http://www.facebook.com/");
        httpget.addHeader("Host","www.facebook.com");
        httpget.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httpget.addHeader("Accept-Language","zh-tw,zh;q=0.8,en-us;q=0.5,en;q=0.3");
        httpget.addHeader("Connection","keep-alive");


        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();


        String Result = EntityUtils.toString(entity);
        if (entity != null) {
            entity.consumeContent();
        }
        Log.d(TAG,"Login:"+Result);



        Matcher mth = redirPtn.matcher(Result);
        while(mth.find()){
            String rurl = mth.group(1);

            Log.d(TAG,"轉址："+rurl);
            if (rurl.contains("/login.php")){
                Log.d(TAG,"Need Login");
            }
        }


        HttpPost httpost = new HttpPost("http://www.facebook.com/login.php");

        List <NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("email", "csthutw@gmail.com"));
        nvps.add(new BasicNameValuePair("pass", "a1a2j3c4"));
        nvps.add(new BasicNameValuePair("locale", "zh_TW"));

        httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));


        httpclient.execute(httpost);


        HttpResponse response2 = httpclient.execute(httpget);
        HttpEntity entity2 = response2.getEntity();
        String Result2 = EntityUtils.toString(entity2);
        if (entity2 != null) {
            entity2.consumeContent();
        }

        Matcher mth2 = redirPtn.matcher(Result2);
        while(mth2.find()){
            String rurl = mth2.group(1);

            if (rurl.contains("/login.php")){
                Log.d(TAG,"Second Check Need Login");
            }
        }



    }


}
