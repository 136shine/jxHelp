package com.jxthelp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jxthelp.App;
import com.jxthelp.R;
import com.jxthelp.adapter.CardsAnimationAdapter;
import com.jxthelp.adapter.ZPAdapter;
import com.jxthelp.dialog.MyDialog;
import com.jxthelp.request.Listener;
import com.jxthelp.request.NewsRequest;
import com.jxthelp.ui.WebView;
import com.jxthelp.util.ToastUtils;
import com.jxthelp.util.VolleyRequest;
import com.melnykov.fab.FloatingActionButton;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by idisfkj on 15/10/29.
 * Email : idisfkj@qq.com.
 */
public class FragmentZPNews extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    @InjectView(R.id.lg_list)
    ListView zpList;
    @InjectView(R.id.swipe)
    SwipeRefreshLayout swipe;
    @InjectView(R.id.fab)
    FloatingActionButton fab;
    
    public static ZPAdapter zpAdapter;
    private int lastItem;
    private boolean isFirst=true;
    private View lodaView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zp_news, null);
        ButterKnife.inject(this, view);
        fab.attachToListView(zpList);
        fab.setType(FloatingActionButton.TYPE_MINI);
        fab.setColorNormal(getResources().getColor(R.color.holo_purple));
        fab.setColorPressed(getResources().getColor(R.color.holo_blue_dark));
        fab.setShadow(false);
        fab.setOnClickListener(this);
        zpAdapter = new ZPAdapter();
        AnimationAdapter animationAdapter = new CardsAnimationAdapter(zpAdapter);
        animationAdapter.setAbsListView(zpList);
        lodaView = getLayoutInflater(savedInstanceState).inflate(R.layout.loda_tv, null);
        zpList.addFooterView(lodaView);
        zpList.setAdapter(animationAdapter);
        zpList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (lastItem == zpAdapter.getCount() && scrollState == this.SCROLL_STATE_IDLE) {
                    lodaView.setVisibility(View.VISIBLE);
                    handler.sendEmptyMessage(0);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItem = firstVisibleItem + visibleItemCount - 1;
                if (zpAdapter.getItemId(firstVisibleItem) > 10) {
                    fab.show();
                } else {
                    fab.hide();
                }
            }
        });
        zpList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), WebView.class);
                intent.putExtra("Item", position);
                intent.putExtra("Current", FragmentNews.current);
                startActivity(intent);
            }
        });
        swipe.setColorSchemeResources(R.color.holo_blue_dark, R.color.holo_green_dark
                , R.color.holo_orange_light, R.color.holo_purple, R.color.holo_red_dark);
        swipe.setOnRefreshListener(this);
        return view;
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    lodaView.setVisibility(View.GONE);
                    break;
                case 1:
                    ToastUtils.showShort("网络异常");
                    break;
                case 2:
                    zpAdapter.notifyDataSetChanged();
                    break;
                case 3:
                    ToastUtils.showShort("刷新成功");
                    break;
                case 4:
                    swipe.setRefreshing(false);
                    break;
            }
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    zpList.setSelection(zpAdapter.getCount());
                    zpAdapter.upDate(new Listener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onFinish() {
                            mHandler.sendEmptyMessage(0);
                        }

                        @Override
                        public void onError() {
                            mHandler.sendEmptyMessage(0);
                            mHandler.sendEmptyMessage(1);
                        }
                    });


                    break;
                default:
                    break;
            }
        }
    };

    public void getData() {
        NewsRequest.request(1, new Listener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                    FragmentNews.pd.cancel();
                App.zpIsFirstLoad = false;
                mHandler.sendEmptyMessage(4);
                if (!isFirst)
                    mHandler.sendEmptyMessage(3);
                isFirst = false;
//                mHandler.sendEmptyMessage(2);
            }

            @Override
            public void onError() {
                if(App.zpIsFirstLoad)
                    FragmentNews.pd.cancel();
                mHandler.sendEmptyMessage(4);
                mHandler.sendEmptyMessage(1);
            }

        }, 6);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (App.zpIsFirstLoad && FragmentNews.current == 6) {
            FragmentNews.current=-1;
            getData();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onClick(View v) {
        zpList.setSelection(0);
        fab.show();
    }

    @Override
    public void onRefresh() {
        App.ZPPAGE = 2;
        VolleyRequest.cancelAll("Image" + 6);
        getData();
    }
}
