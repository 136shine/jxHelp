package com.jxthelp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jxthelp.App;
import com.jxthelp.R;
import com.jxthelp.adapter.NewsViewPagerAdapter;
import com.jxthelp.dialog.MyDialog;
import com.jxthelp.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by idisfkj on 15-9-17 12:17.
 * Email: idisfkj@qq.com
 */
public class FragmentNews extends Fragment implements View.OnClickListener {

    @InjectView(R.id.ho_news)
    LinearLayout hoNews;
    @InjectView(R.id.horizontal)
    HorizontalScrollView horizontal;
    @InjectView(R.id.news_viewpager)
    ViewPager newsViewpager;
    public static int current;
    private int avarWidth;
    private List<Fragment> list = new ArrayList<Fragment>();
    private List<TextView> listTV = new ArrayList<TextView>();
    private List<TextView> listLine = new ArrayList<TextView>();
    public static MyDialog pd;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, null);
        ButterKnife.inject(this, view);
        avarWidth = MainActivity.width / 4;
        initView();
        listTV.get(0).setTextColor(getResources().getColor(R.color.title_kc));
        listLine.get(0).setVisibility(View.VISIBLE);
        horizontal.smoothScrollTo(avarWidth * current, 0);
        list = getList();
        newsViewpager.setAdapter(new NewsViewPagerAdapter(getChildFragmentManager(), list));
        newsViewpager.addOnPageChangeListener(new PageChangeListener());
        return view;
    }

    public void initView() {
        String[] title = {"理工新闻", "校园传真", "媒体聚焦", "学术报告", "学校公告", "招标公告", "校园招聘"};
        for (int i = 0; i < title.length; i++) {
            View view = LayoutInflater.from(App.getContext()).inflate(R.layout.news_title_item, null);
            TextView tv = (TextView) view.findViewById(R.id.news_title);
            tv.setWidth(avarWidth);
            tv.setId(i);
            listTV.add(tv);
            TextView line = (TextView) view.findViewById(R.id.news_line);
            line.setWidth(avarWidth);
            listLine.add(line);
            tv.setText(title[i]);
            hoNews.addView(view);
            tv.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < listTV.size(); i++) {
            if (v.getId() == i) {
                //防止点击不加载数据
                current = i;
                newsViewpager.setCurrentItem(i);
            }
        }
    }

    public class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if ((App.lgIsFirstLoad && position == 0) || (App.xyIsFirstLoad && position == 1)
                    || (App.mtIsFirstLoad && position == 2) || (App.xsIsFirstLoad && position == 3)
                    || (App.xxIsFirstLoad && position == 4) || (App.zbIsFirstLoad && position == 5)
                    || (App.zpIsFirstLoad && position == 6)) {
                pd = new MyDialog(getActivity());
                pd.setCanceledOnTouchOutside(false);
                pd.show();
            }
            current = position;
            System.out.println("current:" + current);
            horizontal.smoothScrollTo(avarWidth / 4 * 3 * position, 0);
            for (int i = 0; i < listTV.size(); i++) {
                if (position == i) {
                    listTV.get(i).setTextColor(getResources().getColor(R.color.title_kc));
                    listLine.get(i).setVisibility(View.VISIBLE);
                } else {
                    listTV.get(i).setTextColor(getResources().getColor(R.color.title_kc1));
                    listLine.get(i).setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public List<Fragment> getList() {
        FragmentLGNews fragmentLGNews = new FragmentLGNews();
        FragmentXSNews fragmentXSNews = new FragmentXSNews();
        FragmentMTNews fragmentMTNews = new FragmentMTNews();
        FragmentXYNews fragmentXYNews = new FragmentXYNews();
        FragmentXXNews fragmentXXNews = new FragmentXXNews();
        FragmentZBNews fragmentZBNews = new FragmentZBNews();
        FragmentZPNews fragmentZPNews = new FragmentZPNews();
        list.add(fragmentLGNews);
        list.add(fragmentXYNews);
        list.add(fragmentMTNews);
        list.add(fragmentXSNews);
        list.add(fragmentXXNews);
        list.add(fragmentZBNews);
        list.add(fragmentZPNews);
        return list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        list.clear();
        listTV.clear();
        listLine.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
