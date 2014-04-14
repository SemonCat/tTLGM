package com.thu.ttlgm.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.thu.ttlgm.R;
import com.thu.ttlgm.utils.ViewUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by SemonCat on 2014/2/5.
 */
public class PPTImageAdapter extends PagerAdapter {

    private Context mContext;
    private File[] mImages;

    private DisplayImageOptions options;

    private boolean StartLoad;

    public PPTImageAdapter(Context mContext, File[] mImages) {
        this.mContext = mContext;
        this.mImages = mImages;

        options = new DisplayImageOptions.Builder()
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

        ViewGroup viewGroup = (ViewGroup)((LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                inflate(R.layout.adapter_ppt_image, null);

        final PhotoView photoView = (PhotoView) viewGroup.findViewById(R.id.PPT_Content);

        final ProgressBar mProgressBar = (ProgressBar) viewGroup.findViewById(R.id.PPT_LoadingBar);


        if (StartLoad){
            ImageLoader.getInstance().displayImage("file://" + mImages[position].getAbsolutePath(), photoView,options,new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    mProgressBar.setVisibility(View.GONE);
                    photoView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        }

        container.addView(viewGroup);

        /*
        PhotoView photoView = new PhotoView(container.getContext());

        if (StartLoad){
            ImageLoader.getInstance().displayImage("file://" + mImages[position].getAbsolutePath(), photoView,options);
        }

        // Now just add PhotoView to ViewPager and return it
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        */
        return viewGroup;


    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //ImageView imageView = (ImageView) object;
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    private Bitmap decodeFile(File f) throws IOException{
        int IMAGE_MAX_SIZE = 300;

        Bitmap b = null;

        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis = new FileInputStream(f);
        BitmapFactory.decodeStream(fis, null, o);
        fis.close();

        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int)Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                    (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        fis = new FileInputStream(f);
        b = BitmapFactory.decodeStream(fis, null, o2);
        fis.close();

        return b;
    }

    public void StartLoad(){
        StartLoad = true;
        notifyDataSetChanged();
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
