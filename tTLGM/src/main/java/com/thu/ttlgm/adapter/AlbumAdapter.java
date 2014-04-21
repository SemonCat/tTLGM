package com.thu.ttlgm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpardogo.listbuddies.lib.adapters.CircularLoopAdapter;
import com.makeramen.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thu.ttlgm.R;
import com.thu.ttlgm.bean.Album;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by SemonCat on 2014/3/21.
 */
public class AlbumAdapter extends UCircularLoopAdapter {

    private Context mContext;
    private CopyOnWriteArrayList<Album> mAlbumList;

    public AlbumAdapter(Context mContext) {
        this.mContext = mContext;
        mAlbumList = new CopyOnWriteArrayList<Album>();
    }

    public AlbumAdapter(Context context, List<Album> albumList) {
        this.mAlbumList = new CopyOnWriteArrayList<Album>(albumList);
        this.mContext = context;
    }

    public synchronized void Refresh(List<Album> albumList) {

        this.mAlbumList.clear();
        this.mAlbumList.addAll(albumList);
        notifyDataSetChanged();


    }

    @Override
    protected int getCircularCount() {
        return mAlbumList.size();
    }

    @Override
    public Album getItem(int position) {

        return mAlbumList.get(getCircularPosition(position));
    }

    @Override
    public long getItemId(int position) {
        return mAlbumList.get(getCircularPosition(position)).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = ((LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.adapter_album,
                    parent, false);

            holder = new ViewHolder();
            holder.AlbumCover = (ImageView) convertView.findViewById(R.id.AlbumCover);
            holder.AlbumTitle = (TextView) convertView.findViewById(R.id.AlbumTitle);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Album mAlbum = getItem(position);


        if (mAlbum != null && mAlbum.getPhotos() != null && mAlbum.getPhotos().size() > 0) {
            String ImageUrl = mAlbum.getPhotos().get(0).getSrc_big();
            if (ImageUrl != null) {
                ImageLoader.getInstance().displayImage(ImageUrl, holder.AlbumCover);
            }
        } else {
            holder.AlbumCover.setImageResource(R.drawable.default_icon);
        }

        holder.AlbumTitle.setText(mAlbum.getName());


        return convertView;


    }

    class ViewHolder {
        ImageView AlbumCover;
        TextView AlbumTitle;
    }
}
