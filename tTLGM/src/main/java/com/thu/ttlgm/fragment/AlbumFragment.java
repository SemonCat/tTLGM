package com.thu.ttlgm.fragment;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.etsy.android.grid.StaggeredGridView;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphObject;
import com.thu.ttlgm.R;
import com.thu.ttlgm.adapter.AlbumAdapter;
import com.thu.ttlgm.bean.Album;
import com.thu.ttlgm.bean.Photo;
import com.thu.ttlgm.service.FacebookAlbumUtils;
import com.thu.ttlgm.service.FacebookMgr;
import com.thu.ttlgm.utils.ConstantUtil;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by SemonCat on 2014/3/21.
 */
public class AlbumFragment extends BaseFragment{

    private static final String TAG = AlbumFragment.class.getName();

    private StaggeredGridView Album;

    private AlbumAdapter mAlbumAdapter;

    private Handler mHandler;

    private List<Album> mAlbumList;

    private ProgressBar mLoadingBar;


    private ScheduledExecutorService mScheduledThreadPool = Executors.newScheduledThreadPool(5);

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        mHandler = new Handler();

        super.onActivityCreated(savedInstanceState);

        getAlbum();


    }




    @Override
    protected void setupView() {
        Album = (StaggeredGridView) getActivity().findViewById(R.id.Album);
        mLoadingBar = (ProgressBar) getActivity().findViewById(R.id.LoadingBar);
    }

    @Override
    protected void setupEvent() {
        Album.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle mBundle = new Bundle();
                List<Photo> mPhotoList = mAlbumAdapter.getItem(position).getPhotos();
                if (mAlbumList!=null && mPhotoList!=null){
                    mBundle.putParcelableArrayList(PhotoFragment.PHOTO,new ArrayList<Photo>(mPhotoList));

                    mBundle.putParcelable(PhotoFragment.ALBUM,mAlbumAdapter.getItem(position));
                }

                PhotoFragment mPhotoFragment = PhotoFragment.getInstance(mBundle);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.add(R.id.Fragment_Content,mPhotoFragment);
                fragmentTransaction.commit();

            }
        });
    }

    @Override
    protected void setupAdapter() {
        mAlbumAdapter = new AlbumAdapter(getActivity());
        Album.setAdapter(mAlbumAdapter);

    }

    private void getAlbum(){
        mScheduledThreadPool.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {

                try{
                    mAlbumList = FacebookAlbumUtils.getAllGroupAlbum();

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mAlbumAdapter.Refresh(mAlbumList);
                            mLoadingBar.setVisibility(View.GONE);
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, 0, 5, TimeUnit.SECONDS);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_album, container, false);
    }
}
