package com.thu.ttlgm.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.thu.ttlgm.R;
import com.thu.ttlgm.bean.Student;
import com.thu.ttlgm.bean.StudentColor;
import com.thu.ttlgm.component.FancyCoverFlow.FancyCoverFlow;
import com.thu.ttlgm.component.FancyCoverFlow.FancyCoverFlowAdapter;
import com.thu.ttlgm.service.SQService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by SemonCat on 2014/3/7.
 */
public class StudentGroupStep2Adapter extends FancyCoverFlowAdapter{

    private static final String TAG = StudentGroupStep2Adapter.class.getName();
// =============================================================================
    // Private members
    // ================================

    private Context mContext;
    private List<Student> mData;

    private HashSet<StudentColor> mStudentColor;
    private Handler mHandler = new Handler();
    DisplayImageOptions options;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    // =============================================================================
    // Supertype overrides
    // =============================================================================


    private final static int wall_backround[] = new int[]{0,R.drawable.wallcards_green,
            R.drawable.wallcards_blue,R.drawable.wallcards_red,R.drawable.wallcards_orange};

    public StudentGroupStep2Adapter(Context context,List<Student> data) {
        this.mContext = context;
        this.mData = data;
        mStudentColor = new HashSet<StudentColor>();

        options = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();

    }

    public void setColor(final HashSet<StudentColor> mColor){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (mStudentColor){
                    mStudentColor = mColor;

                }
            }
        });

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
        synchronized (mData){
            if (mData.size()==0) {return 0;}
            return Integer.MAX_VALUE;
        }
    }

    @Override
    public Student getItem(int position) {
        synchronized (mData){
            return mData.get(position%mData.size());
        }
    }

    @Override
    public long getItemId(int position) {
        synchronized (mData){
            if (mData.size()==0) {return 0;}
            return position%mData.size();
        }
    }

    @Override
    public View getCoverFlowItem(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = ((LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.adapter_student_step2_card,
                    parent, false);

            holder = new ViewHolder();
            holder.Icon = (ImageView) convertView.findViewById(R.id.Student_Icon);
            holder.StudentID = (TextView) convertView.findViewById(R.id.Student_ID);
            holder.Name = (TextView) convertView.findViewById(R.id.Student_Name);
            holder.Department = (TextView) convertView.findViewById(R.id.Student_Department);
            holder.WallBackground = (ImageView) convertView.findViewById(R.id.WallBackground);
            holder.GroupID = (TextView) convertView.findViewById(R.id.Student_Group);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Student mStudent = this.getItem(position);
        holder.Name.setText(mStudent.getName());
        holder.StudentID.setText(mStudent.getID());
        holder.Department.setText(mStudent.getDepartment());
        holder.GroupID.setText(mStudent.getGroupID());


        if (mStudentColor!=null){
            int color = SQService.findColorBySID(mStudentColor,mStudent.getID());
            if (color>0 && color<wall_backround.length){
                int drawable = wall_backround[color];
                holder.WallBackground.setImageResource(drawable);
            }

        }


        String URL = mStudent.getImageUrl();
        if (URL.startsWith("http")){
            imageLoader.displayImage(URL, holder.Icon,options);
        }else{
            holder.Icon.setImageResource(R.drawable.default_icon);
        }



        return convertView;
    }

    class ViewHolder{
        ImageView Icon;
        ImageView WallBackground;
        TextView Name;
        TextView StudentID;
        TextView Department;
        TextView GroupID;
    }
}
