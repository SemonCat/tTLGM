package com.thu.ttlgm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.thu.ttlgm.PPTActivity;
import com.thu.ttlgm.PdfActivity;
import com.thu.ttlgm.R;
import com.thu.ttlgm.adapter.ResourceAdapter;
import com.thu.ttlgm.utils.ConstantUtil;

import java.util.List;

/**
 * Created by SemonCat on 2014/1/14.
 */
public class ResourcePickerFragment extends BaseFragment{


    private ListView mListViewPDF;
    private ListView mListViewPPT;
    private ResourceAdapter mPDFAdapter;
    private ResourceAdapter mPPTAdapter;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    protected void setupView() {
        mListViewPDF = (ListView) getActivity().findViewById(R.id.ListViewResourcePDF);
        mListViewPPT = (ListView) getActivity().findViewById(R.id.ListViewResourcePPT);
    }

    @Override
    protected void setupAdapter() {
        mPDFAdapter = new ResourceAdapter(getActivity(), ConstantUtil.PDFPath);
        mListViewPDF.setAdapter(mPDFAdapter);

        mPPTAdapter = new ResourceAdapter(getActivity(), ConstantUtil.PPTPath);
        mListViewPPT.setAdapter(mPPTAdapter);
    }

    @Override
    protected void setupEvent() {
        mListViewPDF.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OpenPDF(mPDFAdapter.getItem(position).getPath());
            }
        });

        mListViewPPT.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OpenPPT(mPPTAdapter.getItem(position).getPath());
            }
        });
    }

    private void OpenPDF(String Path){
        Intent mIntent = new Intent(getActivity(), PdfActivity.class);
        mIntent.putExtra(PdfActivity.PDFPathString,Path);
        startActivity(mIntent);
    }

    private void OpenPPT(String Path){
        Intent mIntent = new Intent(getActivity(), PPTActivity.class);
        mIntent.putExtra(PPTActivity.PPTPathString,Path);
        startActivity(mIntent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_resourcepicker, container, false);
    }
}
