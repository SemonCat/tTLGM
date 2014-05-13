package com.thu.ttlgm.fragment.vote;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.squareup.picasso.Picasso;
import com.thu.ttlgm.R;
import com.thu.ttlgm.bean.Student;
import com.thu.ttlgm.service.SQService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StatisticFragment extends Fragment implements View.OnClickListener {

    private static final int PREVIEW_IMAGE_NUM = 9;
    private static final int QUESTION_NUM=4;

    private Activity activity;
    private ImageButton[] qetc;
    private LinearLayout[] question;
    private int[] TicketCounter;
    private TextView[] TickerCounterView;
    private TextView TotalTicket;
    private View[] histogram;
    private int fullHistogramSize;
    private HashSet<Student>[] data;

    private Button testBtn;

    private ScheduledExecutorService mScheduledThreadPool = Executors.newScheduledThreadPool(5);

    private Handler mHandler;

    private DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
            .showImageOnFail(R.drawable.default_icon)
            .displayer(new RoundedBitmapDisplayer(360))
            .build();


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistic,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mHandler = new Handler();

        initialize();
    }

    @Override
    public void onResume() {
        super.onResume();
        mScheduledThreadPool.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {

                try{
                    getData();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, 0, 3, TimeUnit.SECONDS);

    }



    @Override
    public void onPause() {
        super.onPause();
        mScheduledThreadPool.shutdown();
    }

    private void getData(){
        SQService.getAllStudents(new SQService.OnAllStudentGetListener() {
            @Override
            public void OnAllStudentGetEvent(final List<Student> mStudentList) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        addData(mStudentList);
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view == qetc[0]) {
            showListDialog(0);
        }
        else if(view == qetc[1]) {
            showListDialog(1);
        }
        else if(view == qetc[2]) {
            showListDialog(2);
        }
        else if(view == qetc[3]) {
            showListDialog(3);
        }
        else if(view == testBtn){

            //add multiData example
            /*
                        int qid[] = new int[2];
                        qid[0]=1;
                        qid[1]=2;
                        Student students[] = new Student[2];
                        students[0] = new Student("999999", "資工系", "GID","黃金寶");
                        students[1] = new Student("999999", "資工系", "GID","黃金寶");
                        addMultiData(qid,students);
                        */

            //add data example
            /*
            addData((int)(Math.random()*4), new Student("99999"+(int)(Math.random()*4), "資工系", "GID","黃金寶"));

*/
        }
    }

    private class ListDialogFragment extends DialogFragment {

        private Activity activity;
        private GridView gridView;
        private int QID;

        public ListDialogFragment(int questionID){
            this.QID = questionID;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            this.activity = activity;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setStyle(DialogFragment.STYLE_NORMAL,android.R.style.Theme_Holo_Light_Dialog);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
            gridView = new GridView(activity);
            /*
            StatisticAdapter adapter = new StatisticAdapter(activity,R.layout.statistic_row_item,new ArrayList<Student>(data[QID]));*/

            StudentDataAdapter adapter = new StudentDataAdapter(new ArrayList<Student>(data[QID]));

            gridView.setAdapter(adapter);
            gridView.setNumColumns(4);
            if(QID==0){
                getDialog().setTitle("认知复杂");
            }
            else if(QID==1){
                getDialog().setTitle("软件复杂");
            }
            else if(QID==2){
                getDialog().setTitle("实施复杂");
            }
            else if(QID==3){
                getDialog().setTitle("管理复杂");
            }

            return gridView;
        }
    }


    private class StudentDataAdapter extends BaseAdapter{
        private List<Student> mStudentData;

        private StudentDataAdapter(List<Student> data) {
            mStudentData = data;
        }

        @Override
        public int getCount() {
            return mStudentData.size();
        }

        @Override
        public Student getItem(int position) {
            return mStudentData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null){
                convertView = ((LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.statistic_row_item,parent,false);
                holder = new ViewHolder();
                holder.name = (TextView)convertView.findViewById(R.id.name);
                holder.department = (TextView)convertView.findViewById(R.id.department);
                holder.avatar = (RoundedImageView)convertView.findViewById(R.id.avatar);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.name.setText(getItem(position).getName());
            holder.department.setText(getItem(position).getDepartment());

            String Url = getItem(position).getImageUrl();
            if (!TextUtils.isEmpty(Url) && Url.startsWith("http")){
                Picasso.with(parent.getContext().getApplicationContext()).load(getItem(position).getImageUrl())
                        .resize(50,50).placeholder(R.drawable.default_icon).into(holder.avatar);
            }



            /*
            ImageLoader.getInstance().displayImage(getItem(position).getImageUrl(), holder.avatar
                    , displayImageOptions,new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    Log.d("","onLoadingStarted");
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    if (failReason!=null){
                        failReason.getCause().printStackTrace();
                        Log.d("","FailReason:"+failReason.toString());
                    }
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    Log.d("","onLoadingComplete");
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    Log.d("","onLoadingCancelled");
                }
            });
            */


            return convertView;
        }

        private class ViewHolder{
            TextView name;
            TextView department;
            RoundedImageView avatar;
        }
    }

    private void initialize(){

        qetc = new ImageButton[QUESTION_NUM];
        TicketCounter = new int[QUESTION_NUM];
        TickerCounterView = new TextView[QUESTION_NUM];

        TickerCounterView[0] = (TextView)activity.findViewById(R.id.TicketCounter1);
        TickerCounterView[1] = (TextView)activity.findViewById(R.id.TicketCounter2);
        TickerCounterView[2] = (TextView)activity.findViewById(R.id.TicketCounter3);
        TickerCounterView[3] = (TextView)activity.findViewById(R.id.TicketCounter4);

        TotalTicket = (TextView) activity.findViewById(R.id.TotalTicket);


        qetc[0] = (ImageButton)activity.findViewById(R.id.q1etc);
        qetc[1] = (ImageButton)activity.findViewById(R.id.q2etc);
        qetc[2] = (ImageButton)activity.findViewById(R.id.q3etc);
        qetc[3] = (ImageButton)activity.findViewById(R.id.q4etc);

        qetc[0].setOnClickListener(this);
        qetc[1].setOnClickListener(this);
        qetc[2].setOnClickListener(this);
        qetc[3].setOnClickListener(this);

        question = new LinearLayout[QUESTION_NUM];

        question[0]= (LinearLayout)activity.findViewById(R.id.q1container);
        question[1] = (LinearLayout)activity.findViewById(R.id.q2container);
        question[2] = (LinearLayout)activity.findViewById(R.id.q3container);
        question[3] = (LinearLayout)activity.findViewById(R.id.q4container);

        histogram = new View[QUESTION_NUM];

        histogram[0] = activity.findViewById(R.id.histogramebar1);
        histogram[1] = activity.findViewById(R.id.histogramebar2);
        histogram[2] = activity.findViewById(R.id.histogramebar3);
        histogram[3] = activity.findViewById(R.id.histogramebar4);
        try {
            fullHistogramSize = histogram[0].getLayoutParams().width;
        }catch(NullPointerException e){
            Toast histogramErrorToast = Toast.makeText(activity,"Build histogram error!!",Toast.LENGTH_LONG);
            histogramErrorToast.show();
            fullHistogramSize=0;
        }

        data = new HashSet[QUESTION_NUM];

        for(int i=0;i<QUESTION_NUM;++i){
            data[i] = new HashSet<Student>();
        }

        testBtn = (Button)activity.findViewById(R.id.test_btn);
        testBtn.setOnClickListener(this);

   }

    private void showListDialog(int qid){
        DialogFragment listFragment = new ListDialogFragment(qid);
        listFragment.show(getFragmentManager().beginTransaction(),"listDialog");
    }

    private void refreshHistogram(){
        int data_sum[] = new int[QUESTION_NUM];

        int ticketSum = 0;

        for(int i=0;i<QUESTION_NUM;++i){
            data_sum[i] = data[i].size();

            TickerCounterView[i].setText(String.valueOf(TicketCounter[i]));
            ticketSum += TicketCounter[i];
        }

        TotalTicket.setText(String.valueOf(ticketSum));

        float sum = 0;
        for(int d :data_sum){
            sum+=d;
        }
        for(int i=0;i<QUESTION_NUM;++i){
            float to;
            if(sum == 0)
                to = 0;
            else
                to = data_sum[i] / sum;
            changeHistogram(i, to);

        }



    }

    private void changeHistogram(int number, float to) {//percent between 0~1
        to *=0.8f;
        float pixTo = (to - 1) * fullHistogramSize;
        ValueAnimator va = ObjectAnimator.ofFloat(histogram[number], "translationX", histogram[number].getTranslationX(), pixTo);
        va.start();
    }

    private void setPreviewImageDrawable(ImageView imgView, Student student){

        if (!TextUtils.isEmpty(student.getImageUrl()) && student.getImageUrl().startsWith("http")){
            Picasso.with(activity.getApplicationContext()).load(student.getImageUrl())
                    .resize(50,50).placeholder(R.drawable.default_icon).into(imgView);
        }


        /*
        ImageLoader.getInstance().displayImage(student.getImageUrl(), imgView
                , displayImageOptions);
        */

    }

    public void addData(int qid,Student student){

        //找到一樣的就不加入！
        for (HashSet<Student> StudentList : data){
            if (StudentList.contains(student)) return;
        }


        //if needed, set data for preview.
        if(question[qid].getChildCount()<PREVIEW_IMAGE_NUM){//add preview, or do nothing.
            StatisticView previewImgView = new StatisticView(activity);
            previewImgView.setCornerRadius(360);
            previewImgView.setBorderWidth(2);
            previewImgView.setBorderColor(0x333333);
            previewImgView.setRoundBackground(true);
            previewImgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            previewImgView.setMaxWidth(50);
            previewImgView.setMaxHeight(50);
            setPreviewImageDrawable(previewImgView, student);

            int size = (int) dip2px(getActivity(),50);

            question[qid].addView(previewImgView, question[qid].getChildCount(),new ViewGroup.LayoutParams(size,size));
        }
        //add data for adapter.
        data[qid].add(student);
        TicketCounter[qid]++;
        //change histogram.
        refreshHistogram();
        //play animation.
    }


    public void addData(List<Student> mStudent){
        for (Student student : mStudent){
            int Money = student.getMoney();
            if (Money > 0 && Money < 5){
                addData(student.getMoney()-1, student);
            }
        }
    }

    public void addMultiData(int[] qid,Student[] students){
        for(int i=0;i<qid.length;++i){
            if(question[qid[i]].getChildCount()<PREVIEW_IMAGE_NUM){//add preview, or do nothing.
                ImageView previewImgView = new ImageView(activity);
                setPreviewImageDrawable(previewImgView, students[i]);
                question[qid[i]].addView(previewImgView, question[qid[i]].getChildCount());
            }
        }

        for(int i=0;i<qid.length;++i){
            data[qid[i]].add(students[i]);
            TicketCounter[qid[i]]++;
        }

        refreshHistogram();

    }

    public static float dip2px(Context context,float dp){
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }
}
