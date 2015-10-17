package com.jxthelp;

import android.app.Application;
import android.content.Context;

import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by idisfkj on 15-9-16 19:22.
 * Email: idisfkj@qq.com
 */
public class App extends Application{
    private static Context mContext;
    private static DefaultHttpClient defaultHttpClient=new DefaultHttpClient();
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=getApplicationContext();
    }
    public static DefaultHttpClient getHttpClient(){
        return defaultHttpClient;
    }
    public static Context getContext(){
        return  mContext;
    }
}
