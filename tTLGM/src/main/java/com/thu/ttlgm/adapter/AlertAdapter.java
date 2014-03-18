package com.thu.ttlgm.adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.makeramen.RoundedImageView;
import com.nhaarman.listviewanimations.itemmanipulation.AnimateAdditionAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thu.ttlgm.R;
import com.thu.ttlgm.bean.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SemonCat on 2014/3/5.
 */
public class AlertAdapter extends BaseAdapter implements AnimateAdditionAdapter.Insertable{

    public interface OnDeleteListener{
        void OnDeleteEvent();
    }

    private List<Student> mStudentList = new ArrayList<Student>();
    private Context mContext;
    private Handler mHandler;

    DisplayImageOptions options;

    private static final int timeout = 10*1000;

    private OnDeleteListener mListener;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    public AlertAdapter(Context mContext) {
        this.mContext = mContext;
        this.mHandler = new Handler();
        this.mStudentList = new ArrayList<Student>();

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();

    }

    public void refresh(List<Student> data){
        this.mStudentList = data;
        notifyDataSetChanged();
    }

    public void refreshOnUiThread(final List<Student> data){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mStudentList = data;
                notifyDataSetChanged();
            }
        });

    }

    private void setupAutoDelete(){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mListener!=null)
                    mListener.OnDeleteEvent();
            }
        },timeout);
    }

    public List<Student> getData(){
        return mStudentList;
    }

    public void addStudent(Student mStudent){
        if (mStudentList.contains(mStudent)) {return;}

        setupAutoDelete();
        mStudentList.add(mStudent);
        notifyDataSetChanged();

    }


    public void remove(Student mStudent){
        mStudentList.remove(mStudent);
        notifyDataSetChanged();
    }

    public void remove(int position){
        if (position>getCount()-1 || position<0) return;

        mStudentList.remove(position);
        notifyDataSetChanged();
    }

    public void removeTop(){

        mStudentList.remove(getCount()-1);
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return mStudentList.size();
    }

    @Override
    public Student getItem(int position) {
        return mStudentList.get(position);
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
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.adapter_alert,
                    parent, false);

            holder = new ViewHolder();
            holder.Icon = (RoundedImageView) convertView.findViewById(R.id.Icon);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Student mStudent = this.getItem(position);

        String URL = mStudent.getImageUrl();
        if (URL.startsWith("http")){
            imageLoader.displayImage(URL, holder.Icon, options);
        }


        return convertView;
    }

    @Override
    public void add(int index, Object item) {

        addStudent((Student)item);
    }

    class ViewHolder{
        RoundedImageView Icon;
    }

    public void setListener(OnDeleteListener listener){
        this.mListener = listener;
    }

}
