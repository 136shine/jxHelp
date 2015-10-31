package com.jxthelp.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jxthelp.App;
import com.jxthelp.R;
import com.jxthelp.adapter.DrawerAdapter;
import com.jxthelp.sliding.PersonalCenter;
import com.jxthelp.sliding.Test;
import com.jxthelp.ui.LoginActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by idisfkj on 15-9-16 19:39.
 * Email: idisfkj@qq.com
 */
public class DrawerFragment extends BaseFragment {

    private ListView listView;
    private List<String> list;
    private String[] name;
    private DrawerAdapter drawerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList<String>();
        name = getResources().getStringArray(R.array.tab_array);
        list = addList();
        Log.v("TAG", list.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drawer_left_list, null);
        listView = (ListView) view.findViewById(R.id.drawer_list);
        drawerAdapter = new DrawerAdapter(App.getContext(), list);
        listView.setAdapter(drawerAdapter);
        listView.setOnItemClickListener(new MyOnItemClickListener());
        return view;

    }

    public List<String> addList() {
        for (int i = 0; i < name.length; i++) {
            list.add(name[i]);
        }
        return list;
    }

    private class MyOnItemClickListener implements AdapterView.OnItemClickListener {
        Intent intent = new Intent();

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    intent = new Intent(getActivity(), PersonalCenter.class);
                    getActivity().startActivity(intent);
                    break;
                case 1:
                    intent = new Intent(getActivity(), Test.class);
                    getActivity().startActivity(intent);
                    break;
                case 2:
                    System.out.println("isSaved:"+App.sp.getBoolean("isSaved", false));
                    System.out.println("username:"+App.sp.getString("username", ""));
                    System.out.println("password:"+App.sp.getString("password", ""));
                    System.out.println("xm:" + App.sp.getString("xm", ""));
                    System.out.println("Cookie:" + App.sp.getString("Cookie", ""));
                    SharedPreferences.Editor editor = App.sp.edit();
                    editor.remove("Cookie");
                    editor.remove("isSaved");
                    editor.remove("usernmae");
                    editor.remove("password");
                    editor.remove("xm");
                    editor.commit();
                    intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                    break;
            }
        }
    }

}
