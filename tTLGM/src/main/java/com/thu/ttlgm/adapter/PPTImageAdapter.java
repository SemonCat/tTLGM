package com.thu.ttlgm.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.thu.ttlgm.R;

import java.io.File;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by SemonCat on 2014/2/5.
 */
public class PPTImageAdapter extends PagerAdapter {

    private Context mContext;
    private File[] mImages;

    private DisplayImageOptions options;;

    public PPTImageAdapter(Context mContext, File[] mImages) {
        this.mContext = mContext;
        this.mImages = mImages;

        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisc(true)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();


    }

    public File[] getFiles() {
        return mImages;
    }


    @Override
    public int getCount() {
        return mImages.length;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {

        PhotoView photoView = new PhotoView(container.getContext());

        ImageLoader.getInstance().displayImage("file://" + mImages[position].getAbsolutePath(), photoView,options);

        // Now just add PhotoView to ViewPager and return it
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return photoView;


    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
