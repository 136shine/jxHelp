package com.jxthelp.drawer;


import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;

/**
 * Created by idisfkj on 15-9-16 20:00.
 * Email: idisfkj@qq.com
 * 打开/关闭DrawerLayout
 */
public class DrawerActionBar extends ActionBarDrawerToggle{
    private Activity mActivity;
    public DrawerActionBar(Activity activity, DrawerLayout drawerLayout, int openDrawerContentDescRes, int closeDrawerContentDescRes){
        super(activity, drawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);
        this.mActivity=activity;
    }
    @Override
    public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        super.onDrawerSlide(drawerView, slideOffset);
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        super.onDrawerStateChanged(newState);
    }
}
