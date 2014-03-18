package com.thu.ttlgm.calc;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.app.Fragment;
import android.widget.Button;
import android.widget.ListView;


import com.thu.ttlgm.R;

import java.util.ArrayList;

public class sortFragment extends Fragment {

    private double groupScore[]=new double[]{};
    private String[] groupName=new String[]{};
    private ArrayList<Double> sortedGroupScore=new ArrayList<Double>();
    private ArrayList<String> sortedGroupName=new ArrayList<String>();

    public sortFragment(double groupScore[],String[] groupName) {
        this.groupScore=groupScore;
        this.groupName=groupName;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sortedScore(groupScore);
        ListView mlistview = (ListView) getActivity().findViewById(R.id.sortlistview);
        sortAdapter sortAdapter=new sortAdapter(getActivity(),sortedGroupScore,sortedGroupName);
        mlistview.setAdapter(sortAdapter);

        Button removeSortButton=(Button) getActivity().findViewById(R.id.removeSortButton);
        removeSortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getFragmentManager().beginTransaction().remove(sortFragment.this).commit();
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sort, container, false);
    }


    private void sortedScore(double[] array){
        for(int i=0;i<array.length;i++){
            if(array[i]>0){
                sortedGroupScore.add(array[i]);
                sortedGroupName.add(groupName[i]);
            }
        }
        if(sortedGroupScore.size()>1){
            for (int i=0; i<sortedGroupScore.size();i++){
                for (int j = i; j < sortedGroupScore.size(); j++)
                    if (sortedGroupScore.get(j) > sortedGroupScore.get(i))
                    {
                        double temp=sortedGroupScore.get(j);
                        sortedGroupScore.set(j,sortedGroupScore.get(i));
                        sortedGroupScore.set(i,temp);

                        String temp2=sortedGroupName.get(j);
                        sortedGroupName.set(j,sortedGroupName.get(i));
                        sortedGroupName.set(i,temp2);
                    }
            }
        }

    }
}
