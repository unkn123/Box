package com.jiit.minor2.shubhamjoshi.box.Behaviour;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jiit.minor2.shubhamjoshi.box.utils.ToolBarUtils;

public class LLayoutBehaviour extends CoordinatorLayout.Behavior<RelativeLayout> {
    private int toolbarHeight;

    public LLayoutBehaviour(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.toolbarHeight = ToolBarUtils.getToolbarHeight(context);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, RelativeLayout relativeLayout, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, RelativeLayout relativeLayout, View dependency) {
        if (dependency instanceof AppBarLayout) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) relativeLayout.getLayoutParams();
            int margin = lp.bottomMargin;
            int distanceToScroll = relativeLayout.getHeight() + margin;
            float ratio = (float)dependency.getY()/(float)toolbarHeight;
            relativeLayout.setTranslationY(-distanceToScroll * ratio);
        }
        return true;
    }
}