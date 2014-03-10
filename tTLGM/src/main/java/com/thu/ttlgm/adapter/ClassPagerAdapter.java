package com.thu.ttlgm.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

    private int weekInfoBackground[] = new int[]{R.drawable.nubg1,
            R.drawable.nubg2,R.drawable.nubg3,R.drawable.nubg4,R.drawable.nubg5,
            R.drawable.nubg6,R.drawable.nubg7,R.drawable.nubg8,R.drawable.nubg9,
            R.drawable.nubg10,R.drawable.nubg11,R.drawable.nubg12,R.drawable.nubg13,
            R.drawable.nubg14,R.drawable.nubg15,R.drawable.nubg16,R.drawable.nubg17,
            R.drawable.nubg18,R.drawable.nubg19,R.drawable.nubg20};

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
    public int getItemPosition(Object object) {
        return mData.getClassList().indexOf(object);
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
        ImageView WeekInfoBackground = (ImageView) viewGroup.findViewById(R.id.WeekInfoBackground);

        Week.setText(String.valueOf(mClass.getWeek()));
        WeekInfoBackground.setImageResource(weekInfoBackground[new Random().nextInt(weekInfoBackground.length)]);

        ClassTitle.setText(mClass.getTitle());

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
