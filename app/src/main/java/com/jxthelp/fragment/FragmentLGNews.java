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
import com.jxthelp.adapter.LGAdapter;
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
 * Created by idisfkj on 15/10/17.
 * Email : idisfkj@qq.com.
 */
public class FragmentLGNews extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    @InjectView(R.id.lg_list)
    ListView lgList;
    public static LGAdapter lgAdapter;
    @InjectView(R.id.swipe)
    SwipeRefreshLayout swipe;
    @InjectView(R.id.fab)
    FloatingActionButton fab;
    private View lodaView;
    private int lastItem;
    private boolean isFirst = true;

    private MyDialog pd;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lg_news, null);
        ButterKnife.inject(this, view);
        fab.attachToListView(lgList);
        fab.setType(FloatingActionButton.TYPE_MINI);
        fab.setColorNormal(getResources().getColor(R.color.holo_purple));
        fab.setColorPressed(getResources().getColor(R.color.holo_blue_dark));
        fab.setShadow(false);
        fab.setOnClickListener(this);
        lgAdapter = new LGAdapter();
        //设置动画
        AnimationAdapter animationAdapter=new CardsAnimationAdapter(lgAdapter);
        animationAdapter.setAbsListView(lgList);
        lodaView = getLayoutInflater(savedInstanceState).inflate(R.layout.loda_tv, null);
        lgList.addFooterView(lodaView);
        lgList.setAdapter(animationAdapter);
        lgList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (lastItem == lgAdapter.getCount() && scrollState == this.SCROLL_STATE_IDLE) {
                    lodaView.setVisibility(View.VISIBLE);
                    handler.sendEmptyMessage(0);
//                    lgAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItem = firstVisibleItem + visibleItemCount - 1;
                if (lgAdapter.getItemId(firstVisibleItem) > 10) {
                    fab.show();
                } else {
                    fab.hide();
                }
            }
        });
        lgList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), WebView.class);
                intent.putExtra("Item", i);
                intent.putExtra("Current", FragmentNews.current);
                startActivity(intent);
            }
        });
        System.out.println("LG onCreateView");
        swipe.setColorSchemeResources(R.color.holo_blue_dark, R.color.holo_green_dark
                , R.color.holo_orange_light, R.color.holo_purple, R.color.holo_red_dark);
        swipe.setOnRefreshListener(this);
        System.out.println("lg:"+App.lgIsFirstLoad);
        if (App.lgIsFirstLoad){
            pd=new MyDialog(getActivity());
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }
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
                    lgAdapter.notifyDataSetChanged();
                    break;
                case 3:
                    ToastUtils.showShort("刷新成功");
                    break;
                case 4:
                    swipe.setRefreshing(false);
                    break;
                case 5:pd.dismiss();break;
            }
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    lgList.setSelection(lgAdapter.getCount());
                    lgAdapter.upDate(new Listener() {
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
                if(App.lgIsFirstLoad)
                mHandler.sendEmptyMessage(5);
                App.lgIsFirstLoad = false;
                mHandler.sendEmptyMessage(4);
                if (!isFirst)
                    mHandler.sendEmptyMessage(3);
                isFirst = false;
                mHandler.sendEmptyMessage(2);
            }

            @Override
            public void onError() {
                if(App.lgIsFirstLoad)
                    mHandler.sendEmptyMessage(5);
                mHandler.sendEmptyMessage(4);
                mHandler.sendEmptyMessage(1);
            }

        },0);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser&&App.lgIsFirstLoad){
            getData();
            System.out.println("getData()");
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onRefresh() {
        App.page = 2;
        VolleyRequest.cancelAll("Image"+0);
        NewsRequest.LGNewsList.clear();
        getData();
    }

    @Override
    public void onClick(View v) {
        lgList.setSelection(0);
        fab.show();
    }
}
