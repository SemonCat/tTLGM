package com.thu.ttlgm.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.thu.ttlgm.R;

import java.io.File;
import java.util.List;

/**
 * Created by SemonCat on 2014/4/16.
 */
public class PPTPreviewAdapter extends BaseAdapter{

    private Context mContext;
    private File[] mImages;
    private DisplayImageOptions displayImageOptions;

    public PPTPreviewAdapter(Context context,String Path) {
        this.mContext = context;

        displayImageOptions = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();

        File mFile = new File(Path);
        if (mFile.exists()) {

            mImages = mFile.listFiles()[0].listFiles();
        } else {
            mImages = new File[0];
        }
    }

    @Override
    public int getCount() {
        return mImages.length;
    }

    @Override
    public File getItem(int position) {
        return mImages[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = ((LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.adapter_ppt_preview,
                    parent, false);

            mViewHolder = new ViewHolder();
            mViewHolder.PPT_Preview_Image = (ImageView) convertView.findViewById(R.id.PPT_Preview_Image);

            convertView.setTag(mViewHolder);
        }else{
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        String ImagePath = getItem(position).getPath();

        ImageLoader.getInstance().displayImage("file://"+ImagePath,mViewHolder.PPT_Preview_Image,displayImageOptions);

        return convertView;
    }


    class ViewHolder{
        ImageView PPT_Preview_Image;
    }
}
