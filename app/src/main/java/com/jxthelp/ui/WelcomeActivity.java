package com.jxthelp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;

import com.jxthelp.App;
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
//                SharedPreferences.Editor editor=App.sp.edit();
//                editor.remove("Cookie");
//                editor.commit();
                System.out.println("isSaved:"+App.sp.getBoolean("isSaved",false));
                System.out.println("username:"+App.sp.getString("username", ""));
                System.out.println("password:"+App.sp.getString("password", ""));
                System.out.println("xm:"+App.sp.getString("xm", ""));
                System.out.println("Cookie:"+App.sp.getString("Cookie", ""));
                if(App.sp.getBoolean("isSaved", false)&&!App.sp.getString("Cookie","").isEmpty()){
                    System.out.println("==MainActivity");
                    Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    System.out.println("====LoginActivity");
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.schedule(timerTask,3000);


    }

    @Override
    public void onBackPressed() {

    }
}
