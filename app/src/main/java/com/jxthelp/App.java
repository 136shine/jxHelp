package com.jxthelp;

import android.app.Application;
import android.content.Context;

/**
 * Created by idisfkj on 15-9-16 19:22.
 * Email: idisfkj@qq.com
 */
public class App extends Application{
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=getApplicationContext();
    }
    public static Context getContext(){
        return  mContext;
    }
}
