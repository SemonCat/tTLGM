package com.thu.ttlgm.adapter;

import com.thu.ttlgm.R;
import com.thu.ttlgm.component.FancyCoverFlow.FancyCoverFlow;
import com.thu.ttlgm.component.FancyCoverFlow.FancyCoverFlowAdapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
/**
 * Created by SemonCat on 2014/2/9.
 */
public class StudentGroupAdapter extends FancyCoverFlowAdapter {
// =============================================================================
    // Private members
    // =============================================================================

    private int[] images = {
            R.drawable.default_icon,
            R.drawable.default_icon,
            R.drawable.default_icon,
            R.drawable.default_icon,
            R.drawable.default_icon,
            R.drawable.default_icon,};

    // =============================================================================
    // Supertype overrides
    // =============================================================================

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Integer getItem(int position) {
        return images[position%images.length];
    }

    @Override
    public long getItemId(int position) {
        return position%images.length;
    }

    @Override
    public View getCoverFlowItem(int position, View reuseableView, ViewGroup viewGroup) {
        ImageView imageView = null;

        if (reuseableView != null) {
            imageView = (ImageView) reuseableView;
        } else {
            imageView = new ImageView(viewGroup.getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setLayoutParams(new FancyCoverFlow.LayoutParams(300, 400));

        }

        imageView.setImageResource(this.getItem(position));
        return imageView;
    }
}
