package com.thu.ttlgm.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.thu.ttlgm.R;
import com.thu.ttlgm.adapter.UserDrawAdapter;
import com.thu.ttlgm.bean.WhiteBoardImage;
import com.thu.ttlgm.component.HackyViewPager;

import java.util.List;

/**
 * Created by SemonCat on 2014/5/6.
 */
public class WhiteBoardViewPagerFragment extends BaseFragment{


    private HackyViewPager mViewPager;
    private List<WhiteBoardImage> mData;

    private TextView Name;

    private Button CloseFragment;

    private int mPosition;

    public WhiteBoardViewPagerFragment(int position,List<WhiteBoardImage> data){
        mData = data;
        this.mPosition = position;

    }

    @Override
    protected void setupView() {
        Name.setText(mData.get(mPosition).getGroupId());
    }

    @Override
    protected void setupEvent() {
        CloseFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager()!=null){
                    getFragmentManager().beginTransaction().remove(WhiteBoardViewPagerFragment.this).commit();
                }
            }
        });
    }

    @Override
    protected void setupAdapter() {
        mViewPager.setAdapter(new UserDrawAdapter(getActivity(),mData));
        mViewPager.setCurrentItem(mPosition);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Name.setText(mData.get(position).getGroupId());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_whiteboard_viewpager, container, false);

        mViewPager = (HackyViewPager) rootView.findViewById(R.id.UserDrawViewPager);

        CloseFragment = (Button) rootView.findViewById(R.id.CloseFragment);

        Name = (TextView) rootView.findViewById(R.id.Name);

        return rootView;
    }

}
