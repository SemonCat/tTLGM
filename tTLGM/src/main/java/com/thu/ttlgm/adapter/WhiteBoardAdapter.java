package com.thu.ttlgm.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.thu.ttlgm.R;
import com.thu.ttlgm.bean.Album;
import com.thu.ttlgm.bean.WhiteBoardImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SemonCat on 2014/3/21.
 */
public class WhiteBoardAdapter extends BaseAdapter{

    private Context mContext;
    private List<WhiteBoardImage> mWhiteBoardImageList;

    private Map<String,Boolean> mIsWrongMap;

    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .build();

    public WhiteBoardAdapter(Context mContext) {
        this.mContext = mContext;
        mWhiteBoardImageList = new ArrayList<WhiteBoardImage>();
        mIsWrongMap = new HashMap<String, Boolean>();
    }

    public WhiteBoardAdapter(Context context, List<WhiteBoardImage> whiteBoardImageList) {
        this.mWhiteBoardImageList  = whiteBoardImageList;

        this.mContext = context;
        mIsWrongMap = new HashMap<String, Boolean>();
    }

    public void Refresh(List<WhiteBoardImage> whiteBoardImageList){
        this.mWhiteBoardImageList  = whiteBoardImageList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mWhiteBoardImageList.size();
    }

    @Override
    public WhiteBoardImage getItem(int position) {
        return mWhiteBoardImageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = ((LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.adapter_whiteboard,
                    parent, false);

            holder = new ViewHolder();
            holder.Image = (ImageView) convertView.findViewById(R.id.Image);
            holder.ImageTitle = (TextView) convertView.findViewById(R.id.ImageTitle);
            holder.WrongImage = (ImageView) convertView.findViewById(R.id.Wrong);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        WhiteBoardImage mWhiteBoardImage = getItem(position);

        ImageLoader.getInstance().displayImage(mWhiteBoardImage.getURL(), holder.Image,options);


        holder.ImageTitle.setText(mWhiteBoardImage.getGroupId());

        Boolean IsWrong = mIsWrongMap.get(getItem(position).getURL());

        if (IsWrong!=null && IsWrong.booleanValue()){
            holder.WrongImage.setVisibility(View.VISIBLE);
        }else{
            holder.WrongImage.setVisibility(View.GONE);
        }


        return convertView;


    }

    public View getView(int position, ViewGroup parent) {
        View convertView = null;
        ViewHolder holder;
        if (convertView == null) {
            convertView = ((LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.adapter_whiteboard,
                    parent, false);

            holder = new ViewHolder();
            holder.Image = (ImageView) convertView.findViewById(R.id.Image);
            holder.ImageTitle = (TextView) convertView.findViewById(R.id.ImageTitle);
            holder.WrongImage = (ImageView) convertView.findViewById(R.id.Wrong);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        WhiteBoardImage mWhiteBoardImage = getItem(position);

        holder.Image.setImageBitmap(ImageLoader.getInstance().loadImageSync(mWhiteBoardImage.getURL() ,options));


        holder.ImageTitle.setText(mWhiteBoardImage.getGroupId());

        Boolean IsWrong = mIsWrongMap.get(getItem(position).getURL());

        if (IsWrong!=null && IsWrong.booleanValue()){
            holder.WrongImage.setVisibility(View.VISIBLE);
        }else{
            holder.WrongImage.setVisibility(View.GONE);
        }


        return convertView;


    }

    public void setWrong(int position,boolean IsWrong){
        mIsWrongMap.put(getItem(position).getURL(),IsWrong);
        notifyDataSetChanged();
    }

    public void toggleWrong(int position){
        Boolean IsWrong = mIsWrongMap.get(getItem(position).getURL());
        if (IsWrong!=null){
            setWrong(position,!IsWrong);
        }else{
            setWrong(position,true);
        }

    }

    public void CleanArray(){
        mWhiteBoardImageList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder{
        ImageView Image;
        TextView ImageTitle;
        ImageView WrongImage;
    }

    public List<WhiteBoardImage> getData(){
        return mWhiteBoardImageList;
    }
}
