package com.jxthelp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jxthelp.App;
import com.jxthelp.R;
import com.jxthelp.adapter.DrawerAdapter;

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
        list=new ArrayList<String>();
        name=getResources().getStringArray(R.array.tab_array);
        list=addList();
        Log.v("TAG", list.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.drawer_left_list,null);
        listView= (ListView) view.findViewById(R.id.drawer_list);
        drawerAdapter=new DrawerAdapter(this.getActivity().getApplication(),list);
        listView.setAdapter(drawerAdapter);
        return view;

    }
    public List<String> addList(){
        for(int i=0;i<name.length;i++){
            list.add(name[i]);
        }
        return list;
    }
}
