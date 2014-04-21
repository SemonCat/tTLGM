package com.thu.ttlgm.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.etsy.android.grid.StaggeredGridView;
import com.jpardogo.listbuddies.lib.views.ListBuddiesLayout;
import com.thu.ttlgm.R;
import com.thu.ttlgm.adapter.AlbumAdapter;
import com.thu.ttlgm.adapter.PhotoAdapter;
import com.thu.ttlgm.bean.Album;
import com.thu.ttlgm.bean.Photo;
import com.thu.ttlgm.service.FacebookAlbumUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SemonCat on 2014/3/21.
 */
public class PhotoFragment extends BaseFragment {

    private static final String TAG = PhotoFragment.class.getName();

    public static final String ALBUM = "album";
    public static final String PHOTO = "photo";

    private ListBuddiesLayout Photo;

    private PhotoAdapter mPhotoAdapter1,mPhotoAdapter2;

    private Handler mHandler;

    private Album mAlbum;
    private List<Photo> mPhotoList;

    public static PhotoFragment getInstance(Bundle bundle) {
        PhotoFragment mPhotoFragment = new PhotoFragment();
        mPhotoFragment.setArguments(bundle);
        return mPhotoFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        mHandler = new Handler();

        super.onActivityCreated(savedInstanceState);

        getPhoto();


    }


    @Override
    protected void setupView() {
        Photo = (ListBuddiesLayout) getActivity().findViewById(R.id.PhotoGridView);
    }

    @Override
    protected void setupEvent() {
        getActivity().findViewById(R.id.ClosePhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(PhotoFragment.this).commit();
            }
        });

        /*
        Photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle mBundle = new Bundle();
                if (mPhotoList!=null){
                    mBundle.putParcelableArrayList(PhotoFragment.PHOTO,new ArrayList<Photo>(
                            mPhotoList));
                }

                mBundle.putInt(PhotoViewFragment.SELECT_PHOTO,position);

                PhotoViewFragment mPhotoViewFragment = PhotoViewFragment.getInstance(mBundle);

                getFragmentManager().beginTransaction().add(R.id.Fragment_Content,mPhotoViewFragment).commit();
            }
        });

        */
        Photo.setOnItemClickListener(new ListBuddiesLayout.OnBuddyItemClickListener() {
            @Override
            public void onBuddyItemClicked(AdapterView<?> parent, View view, int buddy, int position, long id) {
                Bundle mBundle = new Bundle();
                if (mPhotoList != null) {
                    mBundle.putParcelableArrayList(PhotoFragment.PHOTO, new ArrayList<Photo>(
                            mPhotoList));
                }

                mBundle.putInt(PhotoViewFragment.SELECT_PHOTO, position);

                PhotoViewFragment mPhotoViewFragment = PhotoViewFragment.getInstance(mBundle);

                getFragmentManager().beginTransaction().add(R.id.Fragment_Content, mPhotoViewFragment).commit();
            }
        });
    }

    @Override
    protected void setupAdapter() {
        mPhotoAdapter1 = new PhotoAdapter(getActivity());
        mPhotoAdapter2 = new PhotoAdapter(getActivity());

        Photo.setAdapters(mPhotoAdapter1,mPhotoAdapter2);

    }

    private void getPhoto() {

        Bundle mBundle = getArguments();
        if (mBundle != null) {

            /*
            mPhotoList = mBundle.getParcelableArrayList(PHOTO);
            if (mPhotoList!=null){
                mPhotoAdapter.Refresh(mPhotoList);
            }
            */

            mAlbum = mBundle.getParcelable(ALBUM);
            if (mAlbum != null) {
                mPhotoList = FacebookAlbumUtils.getPhotoListByAlbumId(mAlbum.getId());

                List<Photo> mList1 = mPhotoList.subList(0,mPhotoList.size()/2);
                List<Photo> mList2 = mPhotoList.subList(mPhotoList.size()/2,mPhotoList.size());

                mPhotoAdapter1.Refresh(mList1);
                mPhotoAdapter2.Refresh(mList2);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_photo, container, false);
        return contentView;
    }
}
