package com.thu.ttlgm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etsy.android.grid.StaggeredGridView;
import com.thu.ttlgm.R;

/**
 * Created by SemonCat on 2014/3/21.
 */
public class AlbumFragment extends BaseFragment{

    private StaggeredGridView Album;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    protected void setupView() {
        Album = (StaggeredGridView) getActivity().findViewById(R.id.Album);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_album, container, false);
    }
}
