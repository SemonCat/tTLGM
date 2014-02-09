package com.thu.ttlgm.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.TabWidget;

import com.makeramen.RoundedImageView;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

/**
 * Created by SemonCat on 2014/1/15.
 */
public class AvatarView extends RoundedImageView {

    private int mProgress;
    private Paint mPaint;
    private int mMaskColor = 0x81000000;


    public AvatarView(Context context) {
        this(context, null);
    }

    public AvatarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvatarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setProgress(50);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPaint==null)
            mPaint = new Paint();
        else
            mPaint.reset();

        /**畫血量遮罩**/
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mMaskColor);

        RectF oval=new RectF();

        oval.set(0, 0, this.getWidth(), this.getHeight());

        float startAngle = Number2Angle(getProgress());

        canvas.drawArc(oval,startAngle,2*(90-startAngle),false,mPaint);

        /**畫血量趴數**/
        drawPercent(canvas,mPaint);
    }

    private void drawPercent(Canvas canvas,Paint mPaint) {
        mPaint.reset();


        if (getProgress()>50)
            mPaint.setColor(Color.WHITE);

        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize((this.getHeight() / 10) * 4);

        String percentString = new DecimalFormat("###").format(getProgress());

        percentString = percentString + "%";


        canvas.drawText(
                percentString,
                this.getWidth() / 2,
                (int) ((this.getHeight() / 2) - ((mPaint.descent() + mPaint
                        .ascent()) / 2)), mPaint);
    }

    /**
     * 轉換數字到對應的角度
     */
    private float Number2Angle(int number){
        return (float)((number+50)*1.8);
    }


    public int getProgress() {
        return mProgress;
    }

    /**
     * 設定進度。
     * @param Progress 0-100
     */
    public void setProgress(int Progress) {
        if (Progress>100) Progress = 100;
        else if (Progress<0) Progress = 0;
        this.mProgress = Progress;
        invalidate();

    }

}
