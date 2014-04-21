package com.thu.ttlgm.adapter;

import com.jpardogo.listbuddies.lib.adapters.CircularLoopAdapter;

/**
 * Created by SemonCat on 2014/4/20.
 */
public abstract class UCircularLoopAdapter extends CircularLoopAdapter{
    @Override
    public int getCount() {
        if (getCircularCount()==0) return 0;
        return Integer.MAX_VALUE;
    }


    @Override
    public int getCircularPosition(int position) {
        if (getCircularCount()==0) return 0;
        return position % getCircularCount();
    }

}
