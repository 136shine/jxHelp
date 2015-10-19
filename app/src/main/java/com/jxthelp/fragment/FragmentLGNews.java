package com.jxthelp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jxthelp.App;
import com.jxthelp.R;
import com.jxthelp.adapter.LGAdapter;
import com.jxthelp.ui.LoginActivity;
import com.jxthelp.ui.MainActivity;
import com.jxthelp.ui.WebView;
import com.jxthelp.util.ToastUtils;

import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by idisfkj on 15/10/17.
 * Email : idisfkj@qq.com.
 */
public class FragmentLGNews extends Fragment {
    @InjectView(R.id.lg_list)
    ListView lgList;
    public static int item;
    public static LGAdapter lgAdapter;
    private View lodaView;
    private int lastItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lg_news, null);
        ButterKnife.inject(this, view);
        lgAdapter=new LGAdapter();
        lodaView=getLayoutInflater(savedInstanceState).inflate(R.layout.loda_tv,null);
        lgList.addFooterView(lodaView);
        lgList.setAdapter(lgAdapter);
        lgList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(lastItem == lgAdapter.getCount() && scrollState==this.SCROLL_STATE_IDLE){
                    lodaView.setVisibility(View.VISIBLE);
                    handler.sendEmptyMessage(0);
//                    lgAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItem=firstVisibleItem+visibleItemCount-1;
            }
        });
        lgList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item = i;
                ToastUtils.showShort("" + item);
                Intent intent = new Intent(getActivity(), WebView.class);
                startActivity(intent);
            }
        });
        System.out.println("LG onCreateView");
        return view;
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    lgList.setSelection(lgAdapter.getCount());
                    lgAdapter.upDate(new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            lodaView.setVisibility(View.GONE);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            ToastUtils.showShort("网络异常加载失败");
                        }
                    });  //加载更多数据，这里可以使用异步加载


                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
