package com.thu.ttlgm.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.thu.ttlgm.R;
import com.thu.ttlgm.bean.*;
import com.thu.ttlgm.bean.Class;
import com.thu.ttlgm.component.JazzyViewPager.JazzyVerticalViewPager;
import com.thu.ttlgm.component.JazzyViewPager.JazzyViewPager;
import com.thu.ttlgm.component.UPagerAdapter;
import com.thu.ttlgm.utils.ConstantUtil;

import java.text.SimpleDateFormat;
import java.util.Random;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by SemonCat on 2014/1/13.
 */
public class ClassPagerAdapter extends PagerAdapter{
    private Activity mActivity;

    private FragmentManager mFragmentManager;

    private int mPageCount;

    private Subject mData;

    private JazzyVerticalViewPager mJazzyViewPager;

    private int weekInfoBackground[] = new int[]{R.drawable.obj_t_nubg01,
            R.drawable.obj_t_nubg02,R.drawable.obj_t_nubg03,R.drawable.obj_t_nubg04,R.drawable.obj_t_nubg05,
            R.drawable.obj_t_nubg06,R.drawable.obj_t_nubg07,R.drawable.obj_t_nubg08,R.drawable.obj_t_nubg09,
            R.drawable.obj_t_nubg10,R.drawable.obj_t_nubg11,R.drawable.obj_t_nubg12,R.drawable.obj_t_nubg13,
            R.drawable.obj_t_nubg14,R.drawable.obj_t_nubg15,R.drawable.obj_t_nubg16,R.drawable.obj_t_nubg17,
            R.drawable.obj_t_nubg18,R.drawable.obj_t_nubg19};

    public ClassPagerAdapter(Activity activity,Subject data) {
        mActivity = activity;
        mData = data;
    }

    public ClassPagerAdapter(JazzyVerticalViewPager jazzyViewPager,Activity activity,FragmentManager fragmentManager,Subject data) {

        mActivity = activity;
        mData = data;
        this.mJazzyViewPager = jazzyViewPager;
        this.mFragmentManager = fragmentManager;
    }

    @Override
    public int getCount() {
        if (mData!=null && mData.getClassList()!=null)
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

        return view == obj;
    }

    @Override
    public Object instantiateItem(ViewGroup container,final int position) {

        Class mClass = mData.getClassList().get(position);


        final JazzyViewPager jazzyViewPager = new JazzyViewPager(mActivity);
        jazzyViewPager.setId(position+1);
        ClassInfoPagerAdapter mClassInfoPagerAdapter =
                new ClassInfoPagerAdapter(mActivity,mClass,mFragmentManager);

        jazzyViewPager.setTransitionEffect(JazzyViewPager.TransitionEffect.ZoomIn);

        jazzyViewPager.setAdapter(mClassInfoPagerAdapter);
        jazzyViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position!=0){
                    mJazzyViewPager.setPagingEnabled(false);
                    jazzyViewPager.requestDisallowInterceptTouchEvent(true);
                }else{
                    mJazzyViewPager.setPagingEnabled(true);
                    jazzyViewPager.requestDisallowInterceptTouchEvent(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        container.addView(jazzyViewPager);

        if (mJazzyViewPager!=null){
            mJazzyViewPager.setObjectForPosition(jazzyViewPager,position);
        }

        return jazzyViewPager;


        /*
        ViewGroup viewGroup = (ViewGroup)((LayoutInflater) mActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                inflate(R.layout.adapter_class, null);

        TextView Week = (TextView)viewGroup.findViewById(R.id.WeekInfo);
        TextView ClassTitle = (TextView) viewGroup.findViewById(R.id.ClassTitle);
        TextView ClassDate = (TextView) viewGroup.findViewById(R.id.ClassDate);
        ImageView WeekInfoBackground = (ImageView) viewGroup.findViewById(R.id.WeekInfoBackground);
        ListView ResourceList = (ListView) viewGroup.findViewById(R.id.Resource);

        final ResourceAdapter mMovieAdapter = new ResourceAdapter(mActivity, ConstantUtil.TLGMPath+ConstantUtil.MovieDir+ConstantUtil.WeekPath+mClass.getWeek());

        ResourceList.setAdapter(mMovieAdapter);

        ResourceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OpenMovie(mMovieAdapter.getItem(position).getPath());
            }
        });

        Week.setText(String.valueOf(mClass.getWeek()));
        WeekInfoBackground.setImageResource(weekInfoBackground[new Random().nextInt(weekInfoBackground.length)]);

        ClassTitle.setText(mClass.getTitle());

        ClassDate.setText(new SimpleDateFormat("yyyy/MM/dd").format(mClass.getDate()));

        container.addView(viewGroup);

        if (mJazzyViewPager!=null){
            mJazzyViewPager.setObjectForPosition(viewGroup,position);
        }


        return viewGroup;
        */
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        //must be overridden else throws exception as not overridden.
        //Log.d("Tag", collection.getChildCount()+"");
        collection.removeView((View) view);
    }


    private void OpenMovie(String Path){
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Path));
            intent.setDataAndType(Uri.parse(Path), "video/mp4");
            mActivity.startActivity(intent);
        }catch (ActivityNotFoundException mActivityNotFoundException){
            Toast.makeText(mActivity,"找不到播放器",Toast.LENGTH_SHORT).show();
        }
    }


}
