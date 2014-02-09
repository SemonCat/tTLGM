package com.thu.ttlgm.adapter;

import android.content.Context;
import android.location.Address;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

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
public class StudentAdapter extends BaseAdapter{

    private static final String TAG = StudentAdapter.class.getName();

    private Context mContext;
    private List<Student> mData;

    private Random mRandom = new Random();

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

        int i = 0;
        for (Student mStudent : data){
            mStudent.setMoney(moneys[i]);
            mStudent.setBlood(bloods[i]);
            i++;
        }
    }

    public StudentAdapter(Context context,Subject data) {
        this(context,data.getStudents());
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
        return Integer.valueOf(mData.get(position).getID());
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
            holder.mBadgeView = new BadgeView(mContext, holder.Icon);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Student mStudent = this.getItem(position);
        holder.Name.setText(mStudent.getName());
        holder.StudentID.setText(mStudent.getID());
        holder.Department.setText(mStudent.getDepartment());

        /*
        int blood = mRandom.nextInt(99)+1;
        int money = mRandom.nextInt(400)+100;
        */
        int blood = mStudent.getBlood();
        int money = mStudent.getMoney();

        holder.Icon.setProgress(blood);

        holder.mBadgeView.setText(String.valueOf(money));
        holder.mBadgeView.show();


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

    class ViewHolder{
        AvatarView Icon;
        TextView Name;
        TextView StudentID;
        TextView Department;
        BadgeView mBadgeView;
    }
}
