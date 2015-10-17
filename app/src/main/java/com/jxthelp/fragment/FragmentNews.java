package com.jxthelp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jxthelp.R;
import com.jxthelp.adapter.NewsViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by idisfkj on 15-9-17 12:17.
 * Email: idisfkj@qq.com
 */
public class FragmentNews extends Fragment implements View.OnClickListener {


    @InjectView(R.id.tv_lg)
    TextView tvLg;
    @InjectView(R.id.tv_xy)
    TextView tvXy;
    @InjectView(R.id.tv_mt)
    TextView tvMt;
    @InjectView(R.id.tv_xs)
    TextView tvXs;
    @InjectView(R.id.news_viewpager)
    ViewPager newsViewpager;
    @InjectView(R.id.lg_line)
    TextView lgLine;
    @InjectView(R.id.xy_line)
    TextView xyLine;
    @InjectView(R.id.mt_line)
    TextView mtLine;
    @InjectView(R.id.xs_line)
    TextView xsLine;
    private List<Fragment> list = new ArrayList<Fragment>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, null);
        ButterKnife.inject(this, view);
        tvLg.setTextColor(getResources().getColor(R.color.title_kc));
        lgLine.setVisibility(View.VISIBLE);
        list = getList();
        setTVListener();
        newsViewpager.setAdapter(new NewsViewPagerAdapter(getChildFragmentManager(), list));
        newsViewpager.addOnPageChangeListener(new PageChangeListener());
        System.out.println("onCreateView:" + newsViewpager.getCurrentItem());
        return view;
    }

    public void setTVListener() {
        tvLg.setOnClickListener(this);
        tvXy.setOnClickListener(this);
        tvMt.setOnClickListener(this);
        tvXs.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_lg:
                newsViewpager.setCurrentItem(0);
                break;
            case R.id.tv_xy:
                newsViewpager.setCurrentItem(1);
                break;
            case R.id.tv_mt:
                newsViewpager.setCurrentItem(2);
                break;
            case R.id.tv_xs:
                newsViewpager.setCurrentItem(3);
                break;
        }
    }

    public class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    tvLg.setTextColor(getResources().getColor(R.color.title_kc));
                    tvXy.setTextColor(getResources().getColor(R.color.title_kc1));
                    tvMt.setTextColor(getResources().getColor(R.color.title_kc1));
                    tvXs.setTextColor(getResources().getColor(R.color.title_kc1));
                    lgLine.setVisibility(View.VISIBLE);
                    xyLine.setVisibility(View.GONE);
                    mtLine.setVisibility(View.GONE);
                    xsLine.setVisibility(View.GONE);
                    break;
                case 1:
                    tvLg.setTextColor(getResources().getColor(R.color.title_kc1));
                    tvXy.setTextColor(getResources().getColor(R.color.title_kc));
                    tvMt.setTextColor(getResources().getColor(R.color.title_kc1));
                    tvXs.setTextColor(getResources().getColor(R.color.title_kc1));
                    lgLine.setVisibility(View.GONE);
                    xyLine.setVisibility(View.VISIBLE);
                    mtLine.setVisibility(View.GONE);
                    xsLine.setVisibility(View.GONE);
                    break;
                case 2:
                    tvLg.setTextColor(getResources().getColor(R.color.title_kc1));
                    tvXy.setTextColor(getResources().getColor(R.color.title_kc1));
                    tvMt.setTextColor(getResources().getColor(R.color.title_kc));
                    tvXs.setTextColor(getResources().getColor(R.color.title_kc1));
                    lgLine.setVisibility(View.GONE);
                    xyLine.setVisibility(View.GONE);
                    mtLine.setVisibility(View.VISIBLE);
                    xsLine.setVisibility(View.GONE);
                    break;
                case 3:
                    tvLg.setTextColor(getResources().getColor(R.color.title_kc1));
                    tvXy.setTextColor(getResources().getColor(R.color.title_kc1));
                    tvMt.setTextColor(getResources().getColor(R.color.title_kc1));
                    tvXs.setTextColor(getResources().getColor(R.color.title_kc));
                    lgLine.setVisibility(View.GONE);
                    xyLine.setVisibility(View.GONE);
                    mtLine.setVisibility(View.GONE);
                    xsLine.setVisibility(View.VISIBLE);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            System.out.println("state:" + state);
        }
    }

    public List<Fragment> getList() {
        FragmentLGNews fragmentLGNews = new FragmentLGNews();
        FragmentXSNews fragmentXSNews = new FragmentXSNews();
        FragmentMTNews fragmentMTNews = new FragmentMTNews();
        FragmentXYNews fragmentXYNews = new FragmentXYNews();
        list.add(fragmentLGNews);
        list.add(fragmentXYNews);
        list.add(fragmentMTNews);
        list.add(fragmentXSNews);
        return list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        list.clear();
        System.out.println("onDestroyView");
    }

    @Override
    public void onDestroy() {
        System.out.println("onDestroy");
        super.onDestroy();
    }
}
