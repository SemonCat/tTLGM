package com.thu.ttlgm.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.thu.ttlgm.R;
import com.thu.ttlgm.bean.*;
import com.thu.ttlgm.bean.Class;
import com.thu.ttlgm.component.UPagerAdapter;

import java.util.Random;

/**
 * Created by SemonCat on 2014/1/13.
 */
public class ClassPagerAdapter extends UPagerAdapter{
    private Context mContext;
    private int mPageCount;

    private Subject mData;

    public ClassPagerAdapter(Context context,Subject data) {
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        if (mData!=null)
            return mData.getClassList().size();
        else return 0;
    }

    public Class getItem(int position){
        return mData.getClassList().get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return (view ==(View)obj);
    }

    @Override
    public Object instantiateItem(ViewGroup container,final int position) {
        Class mClass = mData.getClassList().get(position);

        ViewGroup viewGroup = (ViewGroup)((LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                inflate(R.layout.adapter_class, null);

        TextView Week = (TextView)viewGroup.findViewById(R.id.WeekInfo);
        TextView ClassTitle = (TextView) viewGroup.findViewById(R.id.ClassTitle);
        TextView ClassContent = (TextView) viewGroup.findViewById(R.id.ClassContent);

        Week.setText(String.valueOf(mClass.getWeek()));
        ClassTitle.setText(mClass.getTitle());
        ClassContent.setText(mClass.getContent());

        container.addView(viewGroup);

        return viewGroup;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        //must be overridden else throws exception as not overridden.
        //Log.d("Tag", collection.getChildCount()+"");
        collection.removeView((View) view);
    }

    @Override
    public float getPageWidth(int position) {
        return 0.5f;
    }

}
