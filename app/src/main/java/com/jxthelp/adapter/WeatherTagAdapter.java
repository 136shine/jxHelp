package com.jxthelp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jxthelp.App;
import com.jxthelp.R;
import com.jxthelp.bean.WeatherIndex;
import com.jxthelp.fragment.FragmentWeather;

import java.util.List;

/**
 * Created by idisfkj on 15/10/30.
 * Email : idisfkj@qq.com.
 */
public class WeatherTagAdapter extends BaseAdapter {
    private List<WeatherIndex> list= FragmentWeather.listIndex;
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(App.getContext()).inflate(R.layout.weather_tag_item,null);
            viewHolder.title= (TextView) convertView.findViewById(R.id.title);
            viewHolder.zs= (TextView) convertView.findViewById(R.id.zs);
            viewHolder.tipt= (TextView) convertView.findViewById(R.id.tipt);
            viewHolder.des= (TextView) convertView.findViewById(R.id.des);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(list.get(position).getTitle());
        viewHolder.zs.setText(list.get(position).getZs());
        viewHolder.tipt.setText(list.get(position).getTipt());
        viewHolder.des.setText(list.get(position).getDes());
        return convertView;
    }
    private class ViewHolder{
        private TextView title;
        private TextView zs;
        private TextView tipt;
        private TextView des;
    }
}
