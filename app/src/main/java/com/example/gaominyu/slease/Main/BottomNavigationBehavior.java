package com.example.gaominyu.slease.Main;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class BottomNavigationBehavior extends CoordinatorLayout.Behavior<BottomNavigationView> {

    private Context context;

    public BottomNavigationBehavior(Context context) {
        super();
        this.context = context;
    }

    public BottomNavigationBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, BottomNavigationView child, View dependency) {
        boolean dependsOn = dependency instanceof FrameLayout;
        return dependsOn;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, BottomNavigationView child, View directTargetChild, View target, int nestedScrollAxes, int type) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, final BottomNavigationView child, View target, int dx, int dy, int[] consumed, int type) {
        if (dy < 0) {
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showBottomNavigationView(child); // Run this on view instead to improve performance
                }
            });
        } else if (dy > 0) {
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideBottomNavigationView(child); // Run this on view instead to improve performance
                }
            });
        }
    }

    private void hideBottomNavigationView(BottomNavigationView view) {
        view.animate().translationY(view.getHeight());
    }

    private void showBottomNavigationView(BottomNavigationView view) {
        view.animate().translationY(0);
    }
}