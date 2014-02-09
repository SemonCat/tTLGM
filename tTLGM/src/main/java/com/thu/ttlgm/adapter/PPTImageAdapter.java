package com.thu.ttlgm.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.thu.ttlgm.R;

import java.io.File;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by SemonCat on 2014/2/5.
 */
public class PPTImageAdapter extends PagerAdapter {

    private Context mContext;
    private File[] mImages;

    public PPTImageAdapter(Context mContext,File[] mImages) {
        this.mContext = mContext;
        this.mImages = mImages;

    }

    @Override
    public int getCount() {
        return mImages.length;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        if(mImages[position].exists()){

            Bitmap mBitmap = BitmapFactory.decodeFile(mImages[position].getAbsolutePath());

            PhotoView photoView = new PhotoView(container.getContext());
            photoView.setImageBitmap(mBitmap);

            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return photoView;
        }else {
            return container;
        }

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
