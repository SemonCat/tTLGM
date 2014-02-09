package com.thu.ttlgm.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import java.util.ArrayList;
import java.util.List;


public class ViewUtil {

    /**
     * Get all the views which matches the given Tag recursively
     *
     * @param root parent view. for e.g. Layouts
     * @param tag  tag to look for
     * @return List of views
     */
    public static List<View> findViewWithTagRecursively(ViewGroup root,
                                                        Object tag) {
        List<View> allViews = new ArrayList<View>();

        final int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View childView = root.getChildAt(i);

            if (childView instanceof ViewGroup) {
                allViews.addAll(findViewWithTagRecursively(
                        (ViewGroup) childView, tag));
            } else {
                final Object tagView = childView.getTag();
                if (tagView != null && tagView.equals(tag))
                    allViews.add(childView);
            }
        }

        return allViews;
    }

    /**
     * Get all the views which matches the given Class recursively
     *
     * @param root parent view. for e.g. Layouts
     * @param mClass  class to look for
     * @return List of views
     */
    public static List<View> findViewWithTypeRecursively(ViewGroup root,
                                                        Class<?> mClass) {
        List<View> allViews = new ArrayList<View>();

        final int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View childView = root.getChildAt(i);

            if (childView instanceof ViewGroup) {
                allViews.addAll(findViewWithTagRecursively(
                        (ViewGroup) childView, mClass));
            } else {
                if (childView != null && mClass.isInstance(childView))
                    allViews.add(childView);
            }
        }

        return allViews;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static ViewGroup getParent(View view) {
        return (ViewGroup) view.getParent();
    }

    public static void removeView(View view) {
        ViewGroup parent = getParent(view);
        if (parent != null) {
            parent.removeView(view);
        }
    }

    public static void replaceView(View currentView, View newView) {
        ViewGroup parent = getParent(currentView);
        if (parent == null) {
            return;
        }
        final int index = parent.indexOfChild(currentView);
        removeView(currentView);
        removeView(newView);
        parent.addView(newView, index);
    }
}
