package com.thu.ttlgm.calc;

import android.content.Context;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;


import com.thu.ttlgm.R;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class sortAdapter extends BaseAdapter {


    private ArrayList<Double> scoreList=new ArrayList<Double>();
    private ArrayList<String> nameList=new ArrayList<String>();

    private TextView scoreTextView;
    private TextView sortTextView;
    private TextView groupTextView;
    private Context mContext;

    public sortAdapter(Context context, ArrayList<Double> scoreList,ArrayList<String> nameList) {
        this.mContext = context;
        this.scoreList = scoreList;
        this.nameList=nameList;

    }

    public void reset(){

    }

    @Override
    public int getCount() {
        return nameList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.sortadapter, null);
        sortTextView = (TextView) convertView.findViewById(R.id.sortedText);
        sortTextView.setText((position+1) + "");
        groupTextView=(TextView) convertView.findViewById(R.id.sortGroupText);
        groupTextView.setText(String.valueOf(nameList.get(position)));

        scoreTextView=(TextView) convertView.findViewById(R.id.sortScoreText);
        scoreTextView.setText(String.format("%.1f",scoreList.get(position)));

        return convertView;
    }


}
