package com.thu.ttlgm.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.thu.ttlgm.fragment.ClassInfoFragment;
import com.thu.ttlgm.fragment.PPTFragment;
import com.thu.ttlgm.bean.Class;
import com.thu.ttlgm.utils.ConstantUtil;
import com.thu.ttlgm.utils.SharedPreferencesUtils;

import java.io.File;

/**
 * Created by SemonCat on 2014/4/9.
 */
public class ClassInfoPagerAdapter extends FragmentStatePagerAdapter{


    private Class mClass;

    private int size;

    private String mPPT;

    private Context mContext;

    public ClassInfoPagerAdapter(Context context,Class mClass,FragmentManager fm) {
        super(fm);
        this.mContext = context;
        this.mClass = mClass;
        this.size = 1;

        //PPT Files
        File mFile = new File(ConstantUtil.TLGMPath+ConstantUtil.PPTDir+ConstantUtil.WeekPath+mClass.getWeek());


        if (mFile.exists() && mFile.list().length>0){
            mPPT = mFile.listFiles()[0].getPath();

            if (ClassInfoFragment.hasPPT(mClass.getWeek())){
                this.size = 2;
            }
        }
    }

    @Override
    public Fragment getItem(int position) {

        if (position==0){


            return new ClassInfoFragment(mClass);

        }else{
            PPTFragment pptFragment = new PPTFragment();
            Bundle mBundle = new Bundle();
            mBundle.putString(PPTFragment.PPTPathString,mPPT);
            mBundle.putInt(PPTFragment.WEEK,mClass.getWeek());
            mBundle.putInt(PPTFragment.WEEK_PAGER, SharedPreferencesUtils.getPPTPage(mContext, mClass.getWeek()));

            pptFragment.setArguments(mBundle);

            return pptFragment;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return size;
    }
}
