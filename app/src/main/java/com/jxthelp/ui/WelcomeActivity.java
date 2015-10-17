package com.jxthelp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;

import com.jxthelp.R;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by idisfkj on 15-9-27 09:32.
 * Email: idisfkj@qq.com
 */
public class WelcomeActivity extends BaseActivity {
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        Timer timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                /*if(sp.getBoolean("isSaved",false)){
                    Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {*/
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
//                }
            }
        };
        timer.schedule(timerTask,3000);


    }

    @Override
    public void onBackPressed() {

    }
}
