package com.thu.ttlgm.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
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

import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * Created by SemonCat on 2014/4/14.
 */
public class UnitIndexPagerAdapter extends PagerAdapter {
    private Context mContext;
    private int mPageCount;

    private Subject mData;

    private int weekInfoBackground[] = new int[]{R.drawable.obj_t_nubg01,
            R.drawable.obj_t_nubg02,R.drawable.obj_t_nubg03,R.drawable.obj_t_nubg04,R.drawable.obj_t_nubg05,
            R.drawable.obj_t_nubg06,R.drawable.obj_t_nubg07,R.drawable.obj_t_nubg08,R.drawable.obj_t_nubg09,
            R.drawable.obj_t_nubg10,R.drawable.obj_t_nubg11,R.drawable.obj_t_nubg12,R.drawable.obj_t_nubg13,
            R.drawable.obj_t_nubg14,R.drawable.obj_t_nubg15,R.drawable.obj_t_nubg16,R.drawable.obj_t_nubg17,
            R.drawable.obj_t_nubg18,R.drawable.obj_t_nubg19};

    public UnitIndexPagerAdapter(Context context, Subject data) {
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
        if (mData!=null && mData.getClassList()!=null){
            return mData.getClassList().get(position);
        }else{
            return null;
        }

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
                inflate(R.layout.adapter_unit_index, null);

        TextView Week = (TextView)viewGroup.findViewById(R.id.WeekInfo);
        TextView ClassTitle = (TextView) viewGroup.findViewById(R.id.ClassTitle);
        TextView ClassDate = (TextView) viewGroup.findViewById(R.id.ClassDate);
        ImageView WeekInfoBackground = (ImageView) viewGroup.findViewById(R.id.WeekInfoBackground);

        Week.setText(String.valueOf(mClass.getWeek()));
        WeekInfoBackground.setImageResource(weekInfoBackground[new Random().nextInt(weekInfoBackground.length)]);

        ClassTitle.setText(mClass.getTitle());

        ClassDate.setText(new SimpleDateFormat("yyyy/MM/dd").format(mClass.getDate()));

        container.addView(viewGroup,0);

        return viewGroup;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        //must be overridden else throws exception as not overridden.
        //Log.d("Tag", collection.getChildCount()+"");
        collection.removeView((View) view);
    }
}
