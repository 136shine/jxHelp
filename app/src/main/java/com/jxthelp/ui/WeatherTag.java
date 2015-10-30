package com.jxthelp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.jxthelp.R;
import com.jxthelp.adapter.WeatherTagAdapter;
import com.jxthelp.swipeback.SwipeBackActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by idisfkj on 15/10/30.
 * Email : idisfkj@qq.com.
 */
public class WeatherTag extends SwipeBackActivity implements View.OnClickListener {
    @InjectView(R.id.weather_tag_lsit)
    ListView weatherTagLsit;
    @InjectView(R.id.weather_tag_back)
    ImageButton weatherTagBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_tag);
        ButterKnife.inject(this);
        WeatherTagAdapter weatherTagAdapter = new WeatherTagAdapter();
        weatherTagLsit.setAdapter(weatherTagAdapter);
        weatherTagBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
