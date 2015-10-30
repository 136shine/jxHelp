package com.jxthelp.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jxthelp.App;
import com.jxthelp.R;
import com.jxthelp.bean.WeatherInfo;
import com.jxthelp.fragment.FragmentWeather;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


import java.util.List;

/**
 * Created by idisfkj on 15/10/30.
 * Email : idisfkj@qq.com.
 */
public class WeatherAdapter extends BaseAdapter {
    private List<WeatherInfo> list = FragmentWeather.list;

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
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(App.getContext()).inflate(R.layout.weather_item, null);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.weather = (TextView) convertView.findViewById(R.id.weather);
            viewHolder.wind = (TextView) convertView.findViewById(R.id.wind);
            viewHolder.dayIv = (ImageView) convertView.findViewById(R.id.day_iv);
            viewHolder.dayTv = (TextView) convertView.findViewById(R.id.day_tv);
            viewHolder.nightIv = (ImageView) convertView.findViewById(R.id.night_iv);
            viewHolder.nightTv = (TextView) convertView.findViewById(R.id.night_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(position==0) {
            viewHolder.date.setText("今天");
        }else {
            viewHolder.date.setText(list.get(position).getDate());
        }
        viewHolder.weather.setText(list.get(position).getWeather());
        viewHolder.wind.setText(list.get(position).getWind());
        DisplayImageOptions options=new DisplayImageOptions.Builder()
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(list.get(position).getDayPicture(),viewHolder.dayIv,options);
        viewHolder.dayTv.setText(list.get(position).getDayTemperature());
        ImageLoader.getInstance().displayImage(list.get(position).getNightPicture(),viewHolder.nightIv,options);
        viewHolder.nightTv.setText(list.get(position).getNightTemperature());
        return convertView;
    }

    private class ViewHolder {
        private TextView date;
        private TextView weather;
        private TextView wind;
        private ImageView dayIv;
        private TextView dayTv;
        private ImageView nightIv;
        private TextView nightTv;
    }
}
