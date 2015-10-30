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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jxthelp.App;
import com.jxthelp.R;
import com.jxthelp.adapter.WeatherAdapter;
import com.jxthelp.api.GetUrl;
import com.jxthelp.bean.WeatherIndex;
import com.jxthelp.bean.WeatherInfo;
import com.jxthelp.dialog.MyDialog;
import com.jxthelp.ui.WeatherSearch;
import com.jxthelp.ui.WeatherTag;
import com.jxthelp.util.ToastUtils;
import com.jxthelp.util.VolleyRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by idisfkj on 15-9-17 12:20.
 * Email: idisfkj@qq.com
 */
public class FragmentWeather extends Fragment implements View.OnClickListener {

    @InjectView(R.id.temperature)
    TextView temperature;
    @InjectView(R.id.weather)
    TextView weather;
    @InjectView(R.id.wind)
    TextView wind;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.zs)
    TextView zs;
    @InjectView(R.id.tipt)
    TextView tipt;
    @InjectView(R.id.des)
    TextView des;
    @InjectView(R.id.tag)
    RelativeLayout tag;
    public static List<WeatherInfo> list = new ArrayList<WeatherInfo>();
    public static List<WeatherIndex> listIndex = new ArrayList<WeatherIndex>();
    @InjectView(R.id.list_view)
    ListView listView;
    @InjectView(R.id.pm)
    TextView pm;
    @InjectView(R.id.setting)
    ImageButton setting;
    @InjectView(R.id.weather_title)
    TextView weatherTitle;
    private WeatherAdapter weatherAdapter;
    private String pm25;
    private MyDialog pd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, null);
        ButterKnife.inject(this, view);
        getData();
        weatherAdapter = new WeatherAdapter();
        listView.setAdapter(weatherAdapter);
        listView.setEnabled(false);
        pd = new MyDialog(getActivity());
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        System.out.println("Test On");
        tag.setOnClickListener(this);
        setting.setOnClickListener(this);
        return view;
    }
    public void getData() {
        final String city= App.CITY;
        String CITY = null;
        try {
            CITY=URLEncoder.encode(city.trim(),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringRequest mStringRequest = new StringRequest(GetUrl.WEATHERUrl1+CITY+GetUrl.WEATHERUrl2, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                System.out.println(s.toString());
                Document doc = Jsoup.parse(s);
                String error=doc.select("error").text().toString();
                if(Integer.parseInt(error)==-3){
                    ToastUtils.showShort("不支持该城市,请重新设置");
                    pd.dismiss();
                    return;
                }
                list.clear();
                listIndex.clear();
                String location = doc.select("currentCITY").text().toString();
                Elements elements = doc.select("weather_data").select("date");
                for (int i = 0; i < elements.size(); i++) {
                    WeatherInfo weatherInfo = new WeatherInfo();
                    String date = elements.get(i).text().toString().trim();
                    if (i == 0) {
                        weatherInfo.setLocation(location);
                        int index = date.indexOf("(");
                        int index1 = date.indexOf("：");
                        String date1 = date.substring(0, 2);
                        String riQi = date.substring(2, index);
                        String shiShi = date.substring(index1 + 1, date.length() - 1);
                        weatherInfo.setRiQi(riQi);
                        weatherInfo.setShiShi(shiShi);
                        weatherInfo.setDate(date1);
                        System.out.println("date:" + date);
                        System.out.println("shiShi:" + shiShi);
                    } else {
                        weatherInfo.setDate(date);
                    }
                    String dayPicture = doc.select("dayPictureUrl").get(i).text().toString();
                    String nightPicture = doc.select("nightPictureUrl").get(i).text().toString();
                    String weather = doc.select("weather").get(i).text().toString();
                    String wind = doc.select("wind").get(i).text().toString();
                    String temperature = doc.select("temperature").get(i).text().toString();
                    int index = temperature.indexOf("~");
                    String dayTemperature = temperature.substring(0, index).trim();
                    String nightTemperature = temperature.substring(index + 1).trim();
                    weatherInfo.setDayPicture(dayPicture);
                    weatherInfo.setNightPicture(nightPicture);
                    weatherInfo.setWeather(weather);
                    weatherInfo.setWind(wind);
                    weatherInfo.setDayTemperature(dayTemperature + "℃");
                    weatherInfo.setNightTemperature(nightTemperature);
                    list.add(weatherInfo);
                }
                Element index = doc.select("index").first();
                Elements Title = index.select("title");
                for (int j = 0; j < Title.size(); j++) {
                    WeatherIndex weatherIndex = new WeatherIndex();
                    String title = Title.get(j).text().toString().trim();
                    String zs = index.select("zs").get(j).text().toString().trim();
                    String tipt = index.select("tipt").get(j).text().toString().trim();
                    String des = index.select("des").get(j).text().toString().trim();
                    weatherIndex.setTitle(title);
                    weatherIndex.setZs(zs);
                    weatherIndex.setTipt(tipt);
                    weatherIndex.setDes(des);
                    listIndex.add(weatherIndex);
                }
                pm25 = doc.select("pm25").text().toString();
                pd.dismiss();
                weatherTitle.setText(city);
                mHandle.sendEmptyMessage(0);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                ToastUtils.showShort("网络异常");
            }
        });
        VolleyRequest.addRequest(mStringRequest, "WEATHER");
    }

    Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    weatherAdapter.notifyDataSetChanged();
                    temperature.setText(list.get(0).getShiShi());
                    weather.setText(list.get(0).getWeather());
                    wind.setText(list.get(0).getWind());
                    title.setText(listIndex.get(0).getTitle());
                    zs.setText(listIndex.get(0).getZs());
                    tipt.setText(listIndex.get(0).getTipt());
                    des.setText(listIndex.get(0).getDes());
                    pm.setText(pm25);
                    break;
            }
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        System.out.println("set");
        System.out.println("aa:"+isVisibleToUser);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(WeatherSearch.tag==1) {
            pd = new MyDialog(getActivity());
            pd.setCanceledOnTouchOutside(false);
            pd.show();
            getData();
            WeatherSearch.tag=0;
        }else {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tag:
                Intent intent = new Intent(getActivity(), WeatherTag.class);
                startActivity(intent);
                break;
            case R.id.setting:
                Intent intent1=new Intent(getActivity(), WeatherSearch.class);
                startActivity(intent1);
                break;

        }

    }
}
