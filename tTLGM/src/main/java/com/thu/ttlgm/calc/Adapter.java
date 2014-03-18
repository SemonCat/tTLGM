package com.thu.ttlgm.calc;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;


import com.thu.ttlgm.R;

import java.util.ArrayList;

public class Adapter extends BaseAdapter {

    public interface OnDataChangeListener{
        void beforeDataChangeEvent(int position);
        void afterDataChangeEvent();
    }

	private ArrayList<Content> contentArrayList;

	private TextView typeView;
	private EditText contentEditText;
    private int counter=1;
    public int lastPosition=0;
    private Activity mActivity;
    private String typeName[];


    private OnDataChangeListener mListener;

	public Adapter(Activity activity, ArrayList<Content> contentArrayList,String typeName[]) {
        this.mActivity = activity;
		this.contentArrayList = contentArrayList;
        this.typeName=typeName;

	}

    public void reset(){
        for (Content mContent:contentArrayList){
            mContent.setContent("");
        }
        lastPosition=0;
        counter = 1;
        notifyDataSetChanged();
    }

	@Override
	public int getCount() {
		return counter;
	}
    public void addCounter(){
        if (mListener!=null){
            mListener.beforeDataChangeEvent(lastPosition);
        }
        counter++;
        notifyDataSetChanged();
        if (mListener!=null){
            mListener.afterDataChangeEvent();
        }

    }

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Content content = contentArrayList.get(position);
        convertView = LayoutInflater.from(mActivity).inflate(R.layout.adapter, null);
		typeView = (TextView) convertView.findViewById(R.id.typeText);
        contentEditText = (EditText) convertView.findViewById(R.id.inputEditText);

        contentEditText.setText(content.getContent());
        contentEditText.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                lastPosition = position;
                //textView1.setSelecti

               // int inType = contentEditText.getInputType(); // backup the input type
                //contentEditText.setInputType(InputType.TYPE_NULL);
                //closeSoftType();
               //contentEditText.requestFocus();// disable soft input
               // contentEditText.onTouchEvent(event); // call native handler
               // contentEditText.setInputType(inType); // restore input type

                return true; // consume touch even
            }
        });

        if (position==lastPosition){
           // contentEditText.requestFocus();
        }

		//typeView.setText("種類"+(position+1) + "");
        typeView.setText(typeName[position]);

        contentEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                lastPosition = position;
                //textView1.setSelecti
                /*
                if ((position == counter - 1 )&& counter<20) {
                    addCounter();
                }*/
                content.setContent(s.toString());
            }
        });
        /*
        if (lastPosition==position){
            //editText1.onRestoreInstanceState(stateScoll);
            contentEditText.requestFocus();

        }*/

       // Log.d("position", String.valueOf(position));
       // Log.d("counter",String.valueOf(getCount()));



		return convertView;
	}
    static double log(float x, int base)
    {
        return  (Math.log(x) / Math.log(base));
    }

    public String getListText(){
        //Log.d("getList",contentArrayList.get(lastPosition).getContent());
        //contentEditText.requestFocus();
        return contentArrayList.get(lastPosition).getContent();
    }
    public void setListText(String setString){
        contentArrayList.get(lastPosition).setContent(setString);

        //Log.d("getList",contentArrayList.get(lastPosition).getContent());
        if ((lastPosition == counter - 1 )&& counter<20) {
            addCounter();

        }
        notifyDataSetChanged();
    }
    public int getLastPosition(){
        return lastPosition;
    }
    public float getSum(){


        float sum=1;
        if(counter-1>0){
            for(int i=0;i<counter-1;i++){
                try {
                    sum=sum*(Float.valueOf(contentArrayList.get(i).getContent())+1);
                }catch (NumberFormatException mNumberFormatException){
                    continue;
                }
            }
        }else {
            sum=0;
        }
       // Log.d("SUM",String.valueOf(sum));
        //Log.d("LOG1024",String.valueOf(log(1024,2)));
        return sum;
    }
    public double getScore(){
        float sum=1;
        if(counter-1>0){
            for(int i=0;i<counter-1;i++){
                try {
                    sum=sum*(Float.valueOf(contentArrayList.get(i).getContent())+1);
                }catch (NumberFormatException mNumberFormatException){
                    continue;
                }

            }
            return log(sum,2);
        }else {
            sum=0;
        }
        return 0;
    }

    public void setListener(OnDataChangeListener mListener) {
        this.mListener = mListener;
    }

    private void closeSoftType(){
        InputMethodManager inputMethodManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
