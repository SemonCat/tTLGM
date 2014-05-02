package com.thu.ttlgm.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.thu.ttlgm.R;
import com.thu.ttlgm.adapter.PhotoViewAdapter;
import com.thu.ttlgm.bean.Photo;
import com.thu.ttlgm.component.HackyViewPager;
import com.thu.ttlgm.component.UViewPager;

import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by SemonCat on 2014/3/25.
 */
public class PhotoViewFragment extends BaseFragment{


    private static final String TAG = PhotoViewFragment.class.getName();

    public static final String SELECT_PHOTO = "select_photo";

    private HackyViewPager mPhotoView;

    private FrameLayout mPhotoViewContainer;

    private PhotoViewAdapter mPhotoViewAdapter;

    private TextView PhotoTitle;
    private TextView PhotoLikeCount;
    private TextView PhotoCommentCount;

    private Button Rotation;

    private ViewPager.OnPageChangeListener mListener;

    private View HideUi;

    public static PhotoViewFragment getInstance(Bundle bundle) {
        PhotoViewFragment mPhotoViewFragment = new PhotoViewFragment();
        mPhotoViewFragment.setArguments(bundle);
        return mPhotoViewFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getPhoto();
    }

    @Override
    protected void setupView() {
        mPhotoView = (HackyViewPager) getActivity().findViewById(R.id.PhotoView);
        mPhotoViewContainer = (FrameLayout) getActivity().findViewById(R.id.PhotoViewContainer);

        PhotoTitle = (TextView) getActivity().findViewById(R.id.PhotoTitle);
        PhotoLikeCount = (TextView) getActivity().findViewById(R.id.PhotoLikeCount);
        PhotoCommentCount = (TextView) getActivity().findViewById(R.id.PhotoCommentCount);

        Rotation = (Button) getActivity().findViewById(R.id.RotationPhotoView);
    }

    @Override
    protected void setupAdapter() {
        mPhotoViewAdapter = new PhotoViewAdapter(getActivity());
        mPhotoView.setAdapter(mPhotoViewAdapter);
    }

    @Override
    protected void setupEvent() {
        getActivity().findViewById(R.id.ClosePhotoView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(PhotoViewFragment.this).commit();
            }
        });

        Rotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoViewAdapter.setImageRotation(mPhotoView.getCurrentItem(),true);
            }
        });

        PhotoViewAttacher.OnPhotoTapListener mOnPhotoTapListener = new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if (view.getTag() == null) {
                    view.setTag(new Boolean(false));
                }

                if (((Boolean) view.getTag()).booleanValue()) {
                    ShowUI();
                    view.setTag(new Boolean(false));
                } else {
                    HideUI();
                    view.setTag(new Boolean(true));
                }
            }
        };

        mPhotoViewAdapter.setListener(mOnPhotoTapListener);

        mListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Photo mPhoto = mPhotoViewAdapter.getItem(position);
                PhotoTitle.setText(String.valueOf(mPhoto.getCaption()));

                PhotoLikeCount.setText(mPhoto.getLike_count()+" 個讚");
                PhotoCommentCount.setText(mPhoto.getComment_count()+" 則留言");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        mPhotoView.setOnPageChangeListener(mListener);


    }

    private void getPhoto(){

        Bundle mBundle = getArguments();
        if (mBundle!=null){
            List<Photo> mPhotoList = mBundle.getParcelableArrayList(PhotoFragment.PHOTO);
            if (mPhotoList!=null){
                mPhotoViewAdapter.Refresh(mPhotoList);
                mListener.onPageSelected(0);

                int select_page = getSelect();
                if (select_page!=-1 && select_page>=0 && select_page < mPhotoList.size()){
                    mPhotoView.setCurrentItem(select_page);
                }

            }
        }

    }

    private int getSelect(){
        Bundle mBundle = getArguments();
        if (mBundle!=null){
            return mBundle.getInt(SELECT_PHOTO);

        }

        return -1;
    }

    private void HideUI(){
        for (int i = 0;i<mPhotoViewContainer.getChildCount();i++){
            View mView = mPhotoViewContainer.getChildAt(i);

            if (mView!=null){
                mView.setVisibility(View.GONE);
            }

        }

        mPhotoView.setVisibility(View.VISIBLE);
    }

    private void ShowUI(){
        for (int i = 0;i<mPhotoViewContainer.getChildCount();i++){
            View mView = mPhotoViewContainer.getChildAt(i);

            if (mView!=null){
                mView.setVisibility(View.VISIBLE);
            }

        }

        mPhotoView.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_photo_view, container, false);
        return contentView;
    }

}
