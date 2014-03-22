package com.thu.ttlgm.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.readystatesoftware.viewbadger.BadgeView;
import com.thu.ttlgm.R;
import com.thu.ttlgm.bean.Student;
import com.thu.ttlgm.bean.Subject;
import com.thu.ttlgm.component.AvatarView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Created by SemonCat on 2014/1/16.
 */
public class StudentAdapter extends BaseAdapter {

    private static final String TAG = StudentAdapter.class.getName();

    private Context mContext;
    private List<Student> mData;
    private List<Student> mTmpData;

    private Random mRandom = new Random();

    private Handler mHandler = new Handler();

    private boolean IsShowBlood = false;

    DisplayImageOptions options;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private int[] bloods = {35,68,41,68,10,25};
    private int[] moneys = {205,631,211,642,148,621};

    public enum SortType{
        ID_ASC,
        ID_DESC,
        Blood_ASC,
        Blood_DESC,
        Money_ASC,
        Money_DESC
    }

    public StudentAdapter(Context context,List<Student> data) {
        this.mContext = context;
        this.mData = data;

        /*
        int i = 0;
        for (Student mStudent : data){
            mStudent.setMoney(moneys[i]);
            mStudent.setBlood(bloods[i]);
            i++;
        }*/

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();

    }

    public StudentAdapter(Context context,Subject data) {
        this(context,data.getStudents());
    }

    public StudentAdapter(Context context){
        this(context,new ArrayList<Student>());
    }

    public void refresh(List<Student> data){
        this.mData = data;
        notifyDataSetChanged();
    }

    public void refreshOnUiThread(final List<Student> data){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mData = data;
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Student getItem(int position) {
        return mData.get(position);
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
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.adapter_student,
                    parent, false);

            holder = new ViewHolder();
            holder.Icon = (AvatarView) convertView.findViewById(R.id.Icon);
            holder.StudentID = (TextView) convertView.findViewById(R.id.student_id);
            holder.Name = (TextView) convertView.findViewById(R.id.name);
            holder.Department = (TextView) convertView.findViewById(R.id.department);
            holder.Coin = new BadgeView(mContext, holder.Icon);
            holder.Coin.setBadgeBackgroundColor(Color.YELLOW);
            holder.Coin.setTextColor(Color.BLACK);
            holder.Coin.setBadgePosition(BadgeView.POSITION_TOP_LEFT);
            holder.Coin.show();

            holder.Blood = new BadgeView(mContext, holder.Icon);
            holder.Blood.show();

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Student mStudent = this.getItem(position);
        holder.Name.setText(mStudent.getName());
        holder.StudentID.setText(mStudent.getID());
        holder.Department.setText(mStudent.getDepartment());

        int blood = mStudent.getBlood();
        int money = mStudent.getMoney();

        holder.Icon.setNumberTextEnable(IsShowBlood);
        holder.Icon.setProgress(blood);

        String URL = mStudent.getImageUrl();
        if (URL.startsWith("http")){
            imageLoader.displayImage(URL,holder.Icon,options);
        }else{
            holder.Icon.setImageResource(R.drawable.default_icon);
        }
        holder.Coin.setText(String.valueOf(money));



        holder.Blood.setText(String.valueOf(blood));



        return convertView;
    }

    public void Sort(SortType mType){
        if (mData!=null && mData.size()!=0){

            switch(mType){
                case ID_ASC:

                    Collections.sort(mData,new Comparator<Student>() {
                        @Override
                        public int compare(Student lhs, Student rhs) {
                            return Integer.valueOf(rhs.getID())-Integer.valueOf(lhs.getID());
                        }
                    });


                    break;
                case ID_DESC:

                    Collections.sort(mData,new Comparator<Student>() {
                        @Override
                        public int compare(Student lhs, Student rhs) {
                            return Integer.valueOf(lhs.getID())-Integer.valueOf(rhs.getID());
                        }
                    });

                    break;

                case Blood_ASC:

                    Collections.sort(mData,new Comparator<Student>() {
                        @Override
                        public int compare(Student lhs, Student rhs) {
                            return Integer.valueOf(rhs.getBlood())-Integer.valueOf(lhs.getBlood());
                        }
                    });

                    break;

                case Blood_DESC:

                    Collections.sort(mData,new Comparator<Student>() {
                        @Override
                        public int compare(Student lhs, Student rhs) {
                            return Integer.valueOf(lhs.getBlood())-Integer.valueOf(rhs.getBlood());
                        }
                    });

                    break;

                case Money_ASC:

                    Collections.sort(mData,new Comparator<Student>() {
                        @Override
                        public int compare(Student lhs, Student rhs) {
                            return Integer.valueOf(rhs.getMoney())-Integer.valueOf(lhs.getMoney());
                        }
                    });

                    break;

                case Money_DESC:

                    Collections.sort(mData,new Comparator<Student>() {
                        @Override
                        public int compare(Student lhs, Student rhs) {
                            return Integer.valueOf(lhs.getMoney())-Integer.valueOf(rhs.getMoney());
                        }
                    });

                    break;
            }

            notifyDataSetChanged();

        }
    }

    public void OnlyShowLogin(boolean onlyShowLogin){
        if (onlyShowLogin){
            mTmpData = new ArrayList<Student>(mData);
            mData.clear();
            for (Student mStudent:mData){
                if (mStudent.IsLogin()){
                    mData.add(mStudent);
                }
            }

            refresh(mData);
        }else{
            refresh(mTmpData);
        }

    }

    public void setShowBlood(boolean Enable){
        IsShowBlood = Enable;

        notifyDataSetInvalidated();
    }

    class ViewHolder{
        AvatarView Icon;
        TextView Name;
        TextView StudentID;
        TextView Department;
        BadgeView Coin;
        BadgeView Blood;
    }
}
