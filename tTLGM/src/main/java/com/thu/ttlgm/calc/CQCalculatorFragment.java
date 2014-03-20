package com.thu.ttlgm.calc;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.thu.ttlgm.BaseActivity;
import com.thu.ttlgm.R;
import com.thu.ttlgm.component.IconView;

import java.util.ArrayList;
import java.util.List;


public class CQCalculatorFragment extends Fragment {

    private IconView CalcIcon;
    private LinearLayout CalcContent;

    private long lastTouch = System.currentTimeMillis();

    private int position;
    private int groupCount=9;
    private int nowGroup=0;
    final boolean checkedCalc[]=new boolean[groupCount];
    private double groupScore[]=new double[groupCount];
    private ArrayList<Double> sortedGroupScore=new ArrayList<Double>();
    private ArrayList<String> sortedGroupName=new ArrayList<String>();

    private final String[] groupName=new String[]{"第一組", "第二組", "第三組", "第四組", "第五組", "第六組", "第七組", "第八組", "第九組",};
    public CQCalculatorFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        CalcIcon = (IconView) getActivity().findViewById(R.id.CalcIcon);
        CalcIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalcContent.setVisibility(View.VISIBLE);
                CalcIcon.setVisibility(View.GONE);
            }
        });
        CalcContent = (LinearLayout) getActivity().findViewById(R.id.CalcContent);
        final ListView mlistview = (ListView) getActivity().findViewById(R.id.listview);
        final String[] typeName = new String[] { "種類一", "種類二", "種類三", "種類四", "種類五",
                "種類六", "種類七", "種類八", "種類九", "種類十" , "種類十一" , "種類十二" , "種類十三"
                , "種類十四" , "種類十五" , "種類十六" , "種類十七" , "種類十八" , "種類十九" , "種類二十" };
        for(int i=0;i<groupCount;i++){
            checkedCalc[i]=true;
            groupScore[i]=0;
        }


        //mlistview.setSelector(R.drawable.selector);

        TextView groupTextView=(TextView) getActivity().findViewById(R.id.groupText);
        groupTextView.setText(groupName[0]);
        TextView closeTextView=(TextView) getActivity().findViewById(R.id.closeText);

        final Adapter calcAdapter = new Adapter(getActivity(),initData(),typeName);

        final ListView sortListView = (ListView) getActivity().findViewById(R.id.sortlistview);


        calcAdapter.setListener(new Adapter.OnDataChangeListener() {
            @Override
            public void beforeDataChangeEvent(int mposition) {
                position = mposition;
            }

            @Override
            public void afterDataChangeEvent() {
                mlistview.setSelection(position);
            }
        });
        View blankPlaceView=getActivity().findViewById(R.id.blankPlaceLayout);
        blankPlaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bubble();
            }
        });

        final LinearLayout calcView=(LinearLayout)getActivity().findViewById(R.id.calcList_layout);
        final LinearLayout sortView=(LinearLayout)getActivity().findViewById(R.id.sort_layout);

        View addButtonView = LayoutInflater.from(getActivity()).inflate(R.layout.addbuttonlayout, null);
        Button footButton =(Button) addButtonView.findViewById(R.id.addButton);
        footButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcAdapter.addCounter();
                calcAdapter.notifyDataSetChanged();
            }
        });

        mlistview.addFooterView(addButtonView);
        mlistview.setAdapter(calcAdapter);
        mlistview.setItemChecked(calcAdapter.getLastPosition(), true);
        groupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getNowGroup(nowGroup)!=-1){
                    nowGroup=getNowGroup(nowGroup);
                    ((TextView)view).setText(groupName[nowGroup]);
                    calcAdapter.reset();

                }else {

                }
            }
        });

        closeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (System.currentTimeMillis()-lastTouch<1000){
                    getActivity().getFragmentManager().beginTransaction().remove(CQCalculatorFragment.this).commit();
                    ((BaseActivity)getActivity()).setDrawerEnable(true);
                }else{
                    Toast.makeText(getActivity(),"若要關閉，請連續點擊X鍵。",Toast.LENGTH_SHORT).show();
                }
                lastTouch = System.currentTimeMillis();
            }
        });






        Button removeSortButton=(Button) getActivity().findViewById(R.id.removeSortButton);
        removeSortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sortView.setVisibility(View.GONE);
                LinearLayout buttonLayout=(LinearLayout)getActivity().findViewById(R.id.calcButtonLayout);
                buttonLayout.setVisibility(View.VISIBLE);
                calcView.setVisibility(View.VISIBLE);
            }
        });


        TextView calcButton=(TextView)(getActivity().findViewById(R.id.cacl));
        TextView sort=(TextView) getActivity().findViewById(R.id.sort);
        TextView button0=(TextView) getActivity().findViewById(R.id.button0);
        TextView button1=(TextView) getActivity().findViewById(R.id.button1);
        TextView button2=(TextView) getActivity().findViewById(R.id.button2);
        TextView button3=(TextView) getActivity().findViewById(R.id.button3);
        TextView button4=(TextView) getActivity().findViewById(R.id.button4);
        TextView button5=(TextView) getActivity().findViewById(R.id.button5);
        TextView button6=(TextView) getActivity().findViewById(R.id.button6);
        TextView button7=(TextView) getActivity().findViewById(R.id.button7);
        TextView button8=(TextView) getActivity().findViewById(R.id.button8);
        TextView button9=(TextView) getActivity().findViewById(R.id.button9);
        TextView buttonDel=(TextView) getActivity().findViewById(R.id.buttonDel);

        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(calcAdapter.getSum()>1){

                    LinearLayout buttonLayout=(LinearLayout)getActivity().findViewById(R.id.calcButtonLayout);
                    buttonLayout.setVisibility(View.GONE);
                    groupScore[nowGroup]=calcAdapter.getScore();
                    checkedCalc[nowGroup]=false;
                    sortedScore(groupScore);

                    sortAdapter sortAdapter=new sortAdapter(getActivity(),sortedGroupScore,sortedGroupName);
                    sortListView.setAdapter(sortAdapter);

                    calcView.setVisibility(View.GONE);
                    sortView.setVisibility(View.VISIBLE);
                }
            }
        });

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String temp=calcAdapter.getListText();
                if(calcAdapter.getListText()!=""){
                    calcAdapter.setListText(calcAdapter.getListText()+"0");
                }else {
                    //calcAdapter.setListText(calcAdapter.getListText());
                }
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp=calcAdapter.getListText()+"1";
                calcAdapter.setListText(temp);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp=calcAdapter.getListText()+"2";
                calcAdapter.setListText(temp);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp=calcAdapter.getListText()+"3";
                calcAdapter.setListText(temp);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp=calcAdapter.getListText()+"4";
                calcAdapter.setListText(temp);
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp=calcAdapter.getListText()+"5";
                calcAdapter.setListText(temp);
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp=calcAdapter.getListText()+"6";
                calcAdapter.setListText(temp);
            }
        });
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp=calcAdapter.getListText()+"7";
                calcAdapter.setListText(temp);
            }
        });
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp=calcAdapter.getListText()+"8";
                calcAdapter.setListText(temp);
            }
        });
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp=calcAdapter.getListText()+"9";
                calcAdapter.setListText(temp);
            }
        });
        buttonDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(calcAdapter.getListText()!=""){
                    int temp=Integer.valueOf(calcAdapter.getListText())/10;
                    if(temp>0){
                        calcAdapter.setListText(String.valueOf(temp));
                    }else {
                        calcAdapter.setListText("");
                    }

                }else {
                    //calcAdapter.setListText(calcAdapter.getListText());
                }

            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp=calcAdapter.getListText()+"1";
                calcAdapter.setListText(temp);
            }
        });

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LinearLayout buttonLayout=(LinearLayout)getActivity().findViewById(R.id.calcButtonLayout);
                buttonLayout.setVisibility(View.GONE);

                calcView.setVisibility(View.GONE);
                sortView.setVisibility(View.VISIBLE);
            }
        });

    }
    private void bubble(){
        CalcContent.setVisibility(View.GONE);
        CalcIcon.setVisibility(View.VISIBLE);
    }

    private void sortedScore(double[] array){
        if (sortedGroupScore.size()>0){
            sortedGroupScore.clear();
        }
        if(sortedGroupName.size()>0){
            sortedGroupName.clear();
        }

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
    private int getNowGroup(int now){
        int count=0;
        List<Integer> isVisibleList=new ArrayList<Integer>();
        isVisibleList.clear();
        for (int i=0;i<groupCount;i++){
            if(checkedCalc[i]){
                isVisibleList.add(i);
                count++;
            }else {
               // Log.d("isVisibleaaa",String.valueOf(isVisibleList.get(i)));
            }
        }
        if (isVisibleList.size()==0){
            return -1;
        }
        if(now>=isVisibleList.get(isVisibleList.size()-1)){
            for (int i=0;i<isVisibleList.size();i++){
                if(checkedCalc[isVisibleList.get(i)]){
                    return isVisibleList.get(i);
                }
            }
        }
        else {
            for (int i=0;i<isVisibleList.size();i++){
                if(checkedCalc[isVisibleList.get(i)]&&isVisibleList.get(i)>now){
                    return isVisibleList.get(i);
                }
            }
        }

        return -1;
    }


    private ArrayList<Content> initData() {
        ArrayList<Content> tests = new ArrayList<Content>();
        for (int i = 0; i < 20; i++) {
            Content content = new Content();
            content.setContent("");
            tests.add(content);
        }

        return tests;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cqcaculator, container, false);
    }
}
