package com.jxthelp.util;

import android.widget.Toast;

import com.jxthelp.App;

/**
 * Created by idisfkj on 15-9-27 09:51.
 * Email: idisfkj@qq.com
 */
public class ToastUtils {
    public ToastUtils(){

    }
    public static void showShort(String msg){
         Toast.makeText(App.getContext(),msg,Toast.LENGTH_SHORT).show();
    }
    public static void showLong(String msg){
        Toast.makeText(App.getContext(),msg,Toast.LENGTH_LONG).show();
    }
}
