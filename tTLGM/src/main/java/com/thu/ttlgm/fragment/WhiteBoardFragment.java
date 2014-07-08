package com.thu.ttlgm.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.etsy.android.grid.StaggeredGridView;
import com.thu.ttlgm.R;
import com.thu.ttlgm.adapter.WhiteBoardAdapter;
import com.thu.ttlgm.bean.WhiteBoardImage;
import com.thu.ttlgm.component.DrawView;
import com.thu.ttlgm.service.FacebookMgr;
import com.thu.ttlgm.service.SQService;
import com.thu.ttlgm.utils.ConstantUtil;
import com.thu.ttlgm.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by SemonCat on 2014/3/26.
 */
public class WhiteBoardFragment extends BaseFragment{


    private GridView mWhiteBoard;

    private WhiteBoardAdapter mWhiteBoardAdapter;

    private AmazonS3 mAmazonS3;

    private ScheduledExecutorService mScheduledThreadPool = Executors.newScheduledThreadPool(5);

    private static final String bucketName = "tlgm";

    private String prefix = "week03";

    private Handler mHandler;

    private ImageView NewProject,SaveProject;

    private ProgressBar UploadPhoto,WhiteBoardLoadingBar;

    private Button mWhiteboard_01,mWhiteboard_02,mWhiteboard_03,mWhiteboard_04;

    private DrawView mDrawView;

    private LinearLayout mWhiteBoardTitle,mWhiteBoardControl;

    private FacebookMgr mFacebookMgr;

    private TextView WhiteBoardEmptyView;



    private static final String[] Dirs = new String[]{"vote01_711","vote02_711","vote03_711","week03_04"};

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mHandler = new Handler();

        prefix = Dirs[0];

        getImage();

        /*
        mFacebookMgr = new FacebookMgr(getActivity());
        mFacebookMgr.Login();
        mFacebookMgr.setListener(new FacebookMgr.FacebookMgrListener() {
            @Override
            public void OnLoginCancel() {

            }
        });
        */
    }

    @Override
    protected void setupView() {
        WhiteBoardEmptyView = (TextView) getActivity().findViewById(R.id.WhiteBoardEmptyView);

        mWhiteBoard = (GridView) getActivity().findViewById(R.id.WhiteBoard);
        mWhiteBoard.setEmptyView(WhiteBoardEmptyView);

        NewProject = (ImageView) getActivity().findViewById(R.id.NewProject);
        SaveProject = (ImageView) getActivity().findViewById(R.id.SaveProject);
        mDrawView = (DrawView) getActivity().findViewById(R.id.DrawView);
        mDrawView.setDrawingCacheEnabled(true);

        mWhiteBoardTitle = (LinearLayout) getActivity().findViewById(R.id.WhiteBoardTitle);
        mWhiteBoardControl = (LinearLayout) getActivity().findViewById(R.id.WhiteBoardControl);

        mWhiteboard_01 = (Button) getActivity().findViewById(R.id.whiteboard_01);
        mWhiteboard_02 = (Button) getActivity().findViewById(R.id.whiteboard_02);
        mWhiteboard_03 = (Button) getActivity().findViewById(R.id.whiteboard_03);
        mWhiteboard_04 = (Button) getActivity().findViewById(R.id.whiteboard_04);

        UploadPhoto = (ProgressBar) getActivity().findViewById(R.id.UploadPhoto);

        WhiteBoardLoadingBar  = (ProgressBar) getActivity().findViewById(R.id.WhiteBoardLoadingBar);
    }

    private void ShowGridProgress(){
        WhiteBoardLoadingBar.setVisibility(View.VISIBLE);
        mWhiteBoard.setVisibility(View.GONE);
        WhiteBoardEmptyView.setVisibility(View.GONE);
    }

    private void HideGridProgress(){
        WhiteBoardLoadingBar.setVisibility(View.GONE);
        mWhiteBoard.setVisibility(View.VISIBLE);

    }

    @Override
    protected void setupAdapter() {
        mWhiteBoardAdapter = new WhiteBoardAdapter(getActivity());
        mWhiteBoard.setAdapter(mWhiteBoardAdapter);

    }

    @Override
    protected void setupEvent() {

        mWhiteBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //mWhiteBoardAdapter.toggleWrong(position);
                WhiteBoardViewPagerFragment whiteBoardViewPagerFragment =
                        new WhiteBoardViewPagerFragment(position,mWhiteBoardAdapter.getData());

                if (getFragmentManager()!=null) {
                    getFragmentManager().beginTransaction().add(R.id.Fragment_Content, whiteBoardViewPagerFragment).commit();
                }
            }
        });

        NewProject.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mDrawView.Clean();
                return true;
            }
        });

        NewProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWhiteBoardTitle.setVisibility(View.GONE);
                mWhiteBoardControl.setVisibility(View.VISIBLE);
            }
        });

        SaveProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProject();
            }
        });

        mWhiteboard_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefix = Dirs[0];
                startWhiteBoard(prefix);
                mDrawView.Clean();
                mWhiteBoardTitle.setVisibility(View.VISIBLE);
                //mWhiteBoardControl.setVisibility(View.GONE);
                mWhiteBoardAdapter.CleanArray();
                ShowGridProgress();

            }
        });

        mWhiteboard_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefix = Dirs[1];
                startWhiteBoard(prefix);
                mDrawView.Clean();
                mWhiteBoardTitle.setVisibility(View.VISIBLE);
                //mWhiteBoardControl.setVisibility(View.GONE);
                mWhiteBoardAdapter.CleanArray();
                ShowGridProgress();
            }
        });

        mWhiteboard_03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefix = Dirs[2];
                startWhiteBoard(prefix);
                mDrawView.Clean();
                mWhiteBoardTitle.setVisibility(View.VISIBLE);
                //mWhiteBoardControl.setVisibility(View.GONE);
                mWhiteBoardAdapter.CleanArray();
                ShowGridProgress();
            }
        });

        mWhiteboard_04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefix = Dirs[3];
                startWhiteBoard(prefix);
                mDrawView.Clean();
                mWhiteBoardTitle.setVisibility(View.VISIBLE);
                //mWhiteBoardControl.setVisibility(View.GONE);
                mWhiteBoardAdapter.CleanArray();
                ShowGridProgress();
            }
        });
    }

    private void getImage(){
        ShowGridProgress();

        setupAWS();

        mScheduledThreadPool.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {

                try{
                    getImageFromAWS();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, 0, 3, TimeUnit.SECONDS);

    }

    private void setupAWS(){
        AWSCredentials credentials;
        credentials = new BasicAWSCredentials(ConstantUtil.AWSAccessKeyID, ConstantUtil.AWSSecretKey);
        mAmazonS3 = new AmazonS3Client(credentials);
    }

    private void getImageFromAWS(){


        final List<WhiteBoardImage> mWhiteBoardImageList = new ArrayList<WhiteBoardImage>();

        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName(bucketName)
                .withPrefix(prefix+"/")
                .withDelimiter("/");


        ObjectListing objects = mAmazonS3.listObjects(listObjectsRequest);
        do
        {
            for (S3ObjectSummary objectSummary : objects.getObjectSummaries())
            {
                String objKey = objectSummary.getKey();

                String groupid = objKey.replace(prefix+"/","");
                groupid = groupid.replace(".png","");
                groupid = groupid.split("_")[0];

                WhiteBoardImage mWhiteBoardImage = new WhiteBoardImage(ConstantUtil.ImageHost+objKey,
                        groupid,objectSummary.getLastModified().getTime());

                mWhiteBoardImageList.add(mWhiteBoardImage);
            }
            objects = mAmazonS3.listNextBatchOfObjects(objects);
        } while (objects.isTruncated());


        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mWhiteBoardAdapter.Refresh(mWhiteBoardImageList);
                HideGridProgress();
            }
        });

    }


    private void saveProject(){
        if (!mFacebookMgr.IsLogin()){
            mFacebookMgr.Login();
            return;
        }

        ShowProgress();


        final Bitmap mBitmap =  ViewUtil.getWholeGridViewItemsToBitmap(mDrawView,mWhiteBoard);
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (mBitmap!=null){
                    //ViewUtil.savePic(mBitmap, Environment.getExternalStorageDirectory()+"/Test.png");
                    byte[] mPhoto = ViewUtil.save2byte(mBitmap);
                    mFacebookMgr.PostPhoto(ConstantUtil.WhiteBoardAlbum,"",mPhoto);
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"儲存完畢！",Toast.LENGTH_SHORT).show();
                        HideProgress();
                    }
                });
            }
        }).start();

    }

    private void ShowProgress(){
        UploadPhoto.setVisibility(View.VISIBLE);
        SaveProject.setVisibility(View.INVISIBLE);
    }

    private void HideProgress(){
        UploadPhoto.setVisibility(View.INVISIBLE);
        SaveProject.setVisibility(View.VISIBLE);
    }

    private void startWhiteBoard(String Dir){
        //SQService.startQuestion(Dir,"WhiteBoard");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_whiteboard, container, false);
    }
}
