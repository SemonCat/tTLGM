package com.thu.ttlgm.adapter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.readystatesoftware.viewbadger.BadgeView;
import com.squareup.picasso.Picasso;
import com.thu.ttlgm.R;
import com.thu.ttlgm.bean.Student;
import com.thu.ttlgm.component.AvatarView;
import com.thu.ttlgm.component.FancyCoverFlow.FancyCoverFlow;
import com.thu.ttlgm.component.FancyCoverFlow.FancyCoverFlowAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by SemonCat on 2014/2/9.
 */
public class StudentGroupAdapter extends FancyCoverFlowAdapter {

    private static final String TAG = StudentGroupAdapter.class.getName();
// =============================================================================
    // Private members
    // =============================================================================

    private int[] images = {
            R.drawable.default_icon,
            R.drawable.default_icon,
            R.drawable.default_icon,
            R.drawable.default_icon,
            R.drawable.default_icon,
            R.drawable.default_icon,};

    private Context mContext;
    private List<Student> mData;
    private Handler mHandler = new Handler();
    DisplayImageOptions options;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    // =============================================================================
    // Supertype overrides
    // =============================================================================


    public StudentGroupAdapter(Context context,List<Student> data) {
        this.mContext = context;
        this.mData = data;


        options = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();

    }

    public void refresh(List<Student> data){
        this.mData = data;
        notifyDataSetChanged();
    }

    public void refreshOnUiThread(final List<Student> data){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (mData){

                    mData = new CopyOnWriteArrayList<Student>(data);
                    notifyDataSetChanged();
                }
            }
        });

    }

    /**
     * 第一次資料載入完成以後，設定中間顯示
     * @param data
     * @param mView
     */
    public void refreshOnUiThreadAndSetInCenter(final List<Student> data, final FancyCoverFlow mView){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (mData){
                    mData = new CopyOnWriteArrayList<Student>(data);
                    notifyDataSetChanged();
                    if (mView!=null){
                        mView.setSelection(Integer.MAX_VALUE/2);
                    }
                }
            }
        });

    }

    @Override
    public int getCount() {
        if (mData.size()==0) {return 0;}
        return Integer.MAX_VALUE;
    }

    @Override
    public Student getItem(int position) {
        return mData.get(position%mData.size());
    }

    @Override
    public long getItemId(int position) {
        if (mData.size()==0) {return 0;}
        return position%mData.size();
    }

    @Override
    public View getCoverFlowItem(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = ((LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.adapter_student_card,
                    parent, false);

            holder = new ViewHolder();
            holder.Icon = (ImageView) convertView.findViewById(R.id.Student_Icon);
            holder.StudentID = (TextView) convertView.findViewById(R.id.Student_ID);
            holder.Name = (TextView) convertView.findViewById(R.id.Student_Name);
            holder.Department = (TextView) convertView.findViewById(R.id.Student_Department);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Student mStudent = this.getItem(position);
        holder.Name.setText(mStudent.getName());
        holder.StudentID.setText(mStudent.getID());
        holder.Department.setText(mStudent.getDepartment());

        String URL = mStudent.getImageUrl();
        if (URL.startsWith("http")){
            imageLoader.displayImage(URL, holder.Icon,options);

            //Picasso.with(convertView.getContext()).load(URL).resize(100,100).into(holder.Icon);

        }else{
            holder.Icon.setImageResource(R.drawable.default_icon);
        }


        return convertView;
    }

    class ViewHolder{
        ImageView Icon;
        TextView Name;
        TextView StudentID;
        TextView Department;
    }
}
