package com.thu.ttlgm;

import android.os.Bundle;
import android.util.Log;

import com.joanzapata.pdfview.PDFView;
import com.thu.ttlgm.utils.ConstantUtil;

import java.io.File;

/**
 * Created by SemonCat on 2014/2/3.
 */
public class PdfActivity extends BaseActivity{

    private static final String TAG = PdfActivity.class.getName();

    private PDFView mPDFView;


    public static final String PDFPathString = "pdfPath";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setupPDFView();
    }

    @Override
    protected void setupView() {
        mPDFView = (PDFView)findViewById(R.id.PdfView);

    }

    private void setupPDFView(){
        String Path = getIntent().getStringExtra(PDFPathString);
        File mFile = new File(Path);
        if (mFile != null && mFile.exists()){

            mPDFView.fromFile(mFile)
                    .defaultPage(0)
                    .load();
        }else{
            finish();
            Log.d(TAG,"File==null");
        }

    }

    @Override
    protected int setupLayout() {
        return R.layout.activity_pdf;
    }
}
