package com.thu.ttlgm.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thu.ttlgm.R;

import java.io.File;
import java.util.List;

/**
 * Created by SemonCat on 2014/2/9.
 */
public class GameAdapter extends BaseAdapter{

    private static final String TAG = GameAdapter.class.getName();

    private String[] Games = new String[]{"分組遊戲","大富翁"};
    private int[] BackgroundColor = new int[]{Color.RED,Color.YELLOW};

    private Context mContext;


    public GameAdapter(Context Context) {
        this.mContext = Context;
    }

    @Override
    public int getCount() {
        return Games.length;
    }

    @Override
    public String getItem(int position) {
        return Games[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String mName = getItem(position);

        ViewHolder holder;

        if (convertView == null) {
            convertView = ((LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.adapter_games,
                    parent, false);

            holder = new ViewHolder();
            holder.GameName = (TextView) convertView.findViewById(R.id.GameName);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.GameName.setText(mName);


        convertView.setBackgroundColor(BackgroundColor[position]);
        return convertView;
    }

    class ViewHolder{
        TextView GameName;
    }
}
