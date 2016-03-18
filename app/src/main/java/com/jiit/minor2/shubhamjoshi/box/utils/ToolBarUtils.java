package com.jiit.minor2.shubhamjoshi.box.utils;

import android.content.Context;
import android.content.res.TypedArray;

import com.jiit.minor2.shubhamjoshi.box.R;

public class ToolBarUtils {

    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }

}