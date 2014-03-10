package com.thu.ttlgm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.thu.ttlgm.R;
import com.thu.ttlgm.adapter.GameAdapter;

/**
 * Created by SemonCat on 2014/2/9.
 */
public class GameFragment extends BaseFragment implements AdapterView.OnItemClickListener{

    private static final String TAG = GameFragment.class.getName();

    private ListView mListViewGames;
    private GameAdapter mGameAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    protected void setupView() {
        mListViewGames = (ListView) getActivity().findViewById(R.id.ListViewGames);
    }

    @Override
    protected void setupAdapter() {
        mGameAdapter = new GameAdapter(getActivity());
        mListViewGames.setAdapter(mGameAdapter);
        mListViewGames.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                startActivity(new Intent(getActivity(), GameFragment.class));
                break;
            case 1:
                break;
        }
    }

    @Override
    protected void setupEvent() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game, container, false);
    }
}
