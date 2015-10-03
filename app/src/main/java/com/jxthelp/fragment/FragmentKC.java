package com.jxthelp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jxthelp.R;
import com.jxthelp.ui.LoginActivity;
import com.jxthelp.util.HttpUtils;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import java.io.IOException;

/**
 * Created by idisfkj on 15-9-17 12:19.
 * Email: idisfkj@qq.com
 */
public class FragmentKC extends Fragment {
    private String kc;
    private String url3="jw.jxust.cn";
    private String url1="http://jw.jxust.cn/jstjkbcx.aspx?zgh=";
    private String url2="http://jw.jxust.cn/js_main.aspx?xh=";

    private DefaultHttpClient defaultHttpClient=new DefaultHttpClient();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        defaultHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
        defaultHttpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);

        System.out.println("user" + LoginActivity.user);
        System.out.println("xm" + LoginActivity.xm);

        getData();



        return  inflater.inflate(R.layout.fragment_kc,null);
    }
    public void getData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    kc= HttpUtils.getHttp(url1+LoginActivity.user+"&xm="
                            +LoginActivity.xm+"&gnmkdm=N122303",defaultHttpClient,url2+LoginActivity.user);
                    System.out.println("kc-----------:"+kc);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

}
