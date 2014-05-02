package com.thu.ttlgm.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.thu.ttlgm.bean.Photo;
import com.thu.ttlgm.utils.ViewUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by SemonCat on 2014/2/5.
 */
public class PhotoViewAdapter extends PagerAdapter {

    private Context mContext;
    private List<Photo> mPhotoList;

    enum Rotation {
        UP(0), DOWN(360),RIGHT(180),LEFT(90);    //    调用构造函数来构造枚举项

        private int value = 0;

        private Rotation(int value) {    //    必须是private的，否则编译错误
            this.value = value;
        }

        public static Rotation valueOf(int value) {    //    手写的从int到enum的转换函数
            switch (value) {
                case 0:
                    return UP;
                case 360:
                    return DOWN;
                case 180:
                    return RIGHT;
                case 90:
                    return LEFT;
                default:
                    return UP;
            }
        }

        public int value() {
            return this.value;
        }
    }

    private SparseArray<Rotation> mRotationSparseArray;


    private PhotoViewAttacher.OnPhotoTapListener mListener;

    public PhotoViewAdapter(Context mContext) {
        this.mContext = mContext;
        this.mPhotoList = new ArrayList<Photo>();
        this.mRotationSparseArray = new SparseArray<Rotation>();

    }

    public PhotoViewAdapter(Context mContext, List<Photo> photoList) {
        this.mContext = mContext;
        this.mPhotoList = photoList;
        this.mRotationSparseArray = new SparseArray<Rotation>();
    }


    public void Refresh(List<Photo> photoList){
        this.mPhotoList = photoList;
        notifyDataSetChanged();
    }

    public Photo getItem(int position){
        return mPhotoList.get(position);
    }

    @Override
    public int getCount() {
        return mPhotoList.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());

        Rotation mRotation = mRotationSparseArray.get(position);

        if (mRotation!=null){
            photoView.setRotation(mRotation.value());
        }

        if (mListener!=null){
            photoView.setOnPhotoTapListener(mListener);
        }


        ImageLoader.getInstance().displayImage(mPhotoList.get(position).getSrc_big(),photoView);


        // Now just add PhotoView to ViewPager and return it
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return photoView;

    }

    public void setListener(PhotoViewAttacher.OnPhotoTapListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ImageView imageView = (ImageView) object;
        container.removeView(imageView);
    }

    public void setImageRotation(int position,boolean turnRight){
            Rotation mRotation = getImageRotation(position);
            mRotation = mRotation!=null ? mRotation : Rotation.UP;
            Rotation newRotation = Rotation.valueOf(turnRight ? mRotation.value()+90 : mRotation.value()-90);
            setImageRotation(position,newRotation);
    }

    public void setImageRotation(int position,Rotation mRotation){
        mRotationSparseArray.put(position,mRotation);
        notifyDataSetChanged();
    }

    public Rotation getImageRotation(int position){
        return mRotationSparseArray.get(position);
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
