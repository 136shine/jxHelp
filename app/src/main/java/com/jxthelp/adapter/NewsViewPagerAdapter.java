package com.jxthelp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.jxthelp.fragment.FragmentLGNews;
import com.jxthelp.fragment.FragmentMTNews;
import com.jxthelp.fragment.FragmentXSNews;
import com.jxthelp.fragment.FragmentXYNews;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by idisfkj on 15/10/17.
 * Email : idisfkj@qq.com.
 */
public class NewsViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> list;
    public NewsViewPagerAdapter(FragmentManager fm ,List<Fragment> list) {
        super(fm);
        this.list=list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

}
