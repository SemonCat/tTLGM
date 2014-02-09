package com.thu.ttlgm.floatwindows;

import android.content.ClipData;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.thu.ttlgm.MainActivity;
import com.thu.ttlgm.R;
import com.thu.ttlgm.arc.ArcMenu;

/**
 * Created by SemonCat on 2014/1/22.
 */
public class FloatLayout extends FrameLayout{

    private static final String TAG = FloatLayout.class.getName();

    public FloatLayout(Context context){
        this(context, null);
    }

    public FloatLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_floatwindows, this);

        setupView();

        setupEvent();


    }


    private void setupView(){
        ArcMenu menu = (ArcMenu) findViewById(R.id.arc_menu);

        final int itemCount = 4;
        for (int i = 0; i < itemCount; i++) {
            ImageView item = new ImageView(getContext());
            item.setImageResource(R.drawable.settings);

            final int position = i;
            menu.addItem(item, new OnClickListener() {

                @Override
                public void onClick(View v) {

                }
            });// Add a menu item
        }

    }

    private void setupEvent(){
        
    }

    private void setupRadia(){

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

       super.onTouchEvent(ev);
       return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onFinishInflate() {

    }
}
