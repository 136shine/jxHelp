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
import com.jxthelp.adapter.MTAdapter;
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
public class FragmentMTNews extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    @InjectView(R.id.lg_list)
    ListView mtList;
    @InjectView(R.id.swipe)
    SwipeRefreshLayout swipe;
    @InjectView(R.id.fab)
    FloatingActionButton fab;
    private View lodaView;
    private int lastItem;

    public static MTAdapter mtAdapter;
    private boolean isFirst=true;
    private MyDialog pd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mt_news, null);
        ButterKnife.inject(this, view);

        fab.attachToListView(mtList);
        fab.setType(FloatingActionButton.TYPE_MINI);
        fab.setColorNormal(getResources().getColor(R.color.holo_purple));
        fab.setColorPressed(getResources().getColor(R.color.holo_blue_dark));
        fab.setShadow(false);
        fab.setOnClickListener(this);
        mtAdapter=new MTAdapter();
        AnimationAdapter animationAdapter=new CardsAnimationAdapter(mtAdapter);
        animationAdapter.setAbsListView(mtList);
        lodaView=getLayoutInflater(savedInstanceState).inflate(R.layout.loda_tv,null);
        mtList.addFooterView(lodaView);
        mtList.setAdapter(animationAdapter);
        mtList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (lastItem == mtAdapter.getCount() && scrollState == this.SCROLL_STATE_IDLE) {
                    lodaView.setVisibility(View.VISIBLE);
                    handler.sendEmptyMessage(0);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItem = firstVisibleItem + visibleItemCount - 1;
                if (mtAdapter.getItemId(firstVisibleItem) > 10) {
                    fab.show();
                } else {
                    fab.hide();
                }
            }
        });
        mtList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
//        if (App.mtIsFirstLoad)
//            getData();
        System.out.println("mt:"+App.mtIsFirstLoad);
        System.out.println("mt");
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
                    mtAdapter.notifyDataSetChanged();
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
                    mtList.setSelection(mtAdapter.getCount());
                    mtAdapter.upDate(new Listener() {
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
                if (App.mtIsFirstLoad)
                pd.cancel();
                App.mtIsFirstLoad = false;
                mHandler.sendEmptyMessage(4);
                if (!isFirst)
                    mHandler.sendEmptyMessage(3);
                isFirst = false;
//                mHandler.sendEmptyMessage(2);
            }

            @Override
            public void onError() {
                if(App.mtIsFirstLoad)
                    pd.cancel();
                mHandler.sendEmptyMessage(4);
                mHandler.sendEmptyMessage(1);
            }

        }, 2);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(App.mtIsFirstLoad&&FragmentNews.current==2){
            pd=new MyDialog(getActivity());
            pd.setCanceledOnTouchOutside(false);
            pd.show();
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
        mtList.setSelection(0);
        fab.show();
    }

    @Override
    public void onRefresh() {
        App.MTPAGE = 2;
        VolleyRequest.cancelAll("Image"+2);
        NewsRequest.MTNewsList.clear();
        getData();
    }
}
