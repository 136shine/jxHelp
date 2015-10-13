package com.jxthelp.fragment;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jxthelp.App;
import com.jxthelp.R;
import com.jxthelp.ui.LoginActivity;
import com.jxthelp.ui.WebView;
import com.jxthelp.util.ToastUtils;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by idisfkj on 15-9-17 12:17.
 * Email: idisfkj@qq.com
 */
public class FragmentNews extends Fragment {
    @InjectView(R.id.new_list)
    ListView newList;
    public static int item;
    private List<String> listTitle = LoginActivity.listTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, null);
        ButterKnife.inject(this, view);
        newList.setAdapter(adapter);

        newList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item = i;
                ToastUtils.showShort(""+item);
                Intent intent = new Intent(getActivity(), WebView.class);
                startActivity(intent);
            }
        });
        return view;
    }

    BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return listTitle.size();
        }

        @Override
        public Object getItem(int i) {
            return listTitle.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = LayoutInflater.from(App.getContext()).inflate(R.layout.new_item, null);
                viewHolder.textView = (TextView) view.findViewById(R.id.new_text);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.textView.setText(listTitle.get(i));
            return view;
        }
    };

    public class ViewHolder {
        private TextView textView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
