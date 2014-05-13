package com.thu.ttlgm.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.thu.ttlgm.R;
import com.thu.ttlgm.bean.Photo;
import com.thu.ttlgm.bean.WhiteBoardImage;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by SemonCat on 2014/2/5.
 */
public class UserDrawAdapter extends PagerAdapter {

    private Context mContext;
    private List<WhiteBoardImage> mImageURL;

    public UserDrawAdapter(Context mContext) {
        this.mContext = mContext;
        this.mImageURL = new ArrayList<WhiteBoardImage>();

    }

    public UserDrawAdapter(Context mContext, List<WhiteBoardImage> photoList) {
        this.mContext = mContext;
        this.mImageURL = photoList;
    }


    public void Refresh(List<WhiteBoardImage> photoList){
        this.mImageURL = photoList;
        notifyDataSetChanged();
    }

    public String getItem(int position){
        return mImageURL.get(position).getURL();
    }

    @Override
    public int getCount() {
        return mImageURL.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());



        ImageLoader.getInstance().displayImage(getItem(position),photoView);

        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return photoView;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
