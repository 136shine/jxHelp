package com.jxthelp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.jxthelp.App;
import com.jxthelp.R;
import com.jxthelp.swipeback.SwipeBackActivity;
import com.jxthelp.util.ToastUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by idisfkj on 15/10/30.
 * Email : idisfkj@qq.com.
 */
public class WeatherSearch extends SwipeBackActivity implements View.OnClickListener {
    @InjectView(R.id.weather_setting_back)
    ImageButton weatherSettingBack;
    @InjectView(R.id.setting_et)
    EditText settingEt;
    @InjectView(R.id.setting_bt)
    Button settingBt;
    public static int tag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_search);
        ButterKnife.inject(this);
        weatherSettingBack.setOnClickListener(this);
        settingBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(settingEt.getText().toString().trim().isEmpty()){
                    ToastUtils.showShort("城市不能为空");
                }else {
                    App.CITY=settingEt.getText().toString().trim();
                    tag=1;
                    finish();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
