package com.thu.ttlgm.adapter;

import android.content.Context;
import android.graphics.Path;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thu.ttlgm.R;

import org.w3c.dom.Text;

import java.io.File;

/**
 * Created by SemonCat on 2014/2/2.
 */
public class ResourceAdapter extends BaseAdapter{

    private String mPath;
    private File mDir;
    private Context mContext;

    public ResourceAdapter(Context context,String path) {
        this.mContext = context;
        mPath = path;
        this.mDir = new File(mPath);
    }

    @Override
    public int getCount() {
        if (mDir!=null)
            return mDir.list().length;
        return 0;

    }

    @Override
    public File getItem(int position) {
        return mDir.listFiles()[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        File mFile = getItem(position);

        ViewHolder holder;

        if (convertView == null) {
            convertView = ((LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.adapter_resource,
                    parent, false);

            holder = new ViewHolder();
            holder.FileName = (TextView) convertView.findViewById(R.id.FileName);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.FileName.setText(mFile.getName());


        return convertView;
    }

    class ViewHolder{
        TextView FileName;
    }
}
