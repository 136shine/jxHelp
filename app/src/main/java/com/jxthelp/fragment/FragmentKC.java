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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getData();
        return  inflater.inflate(R.layout.fragment_kc,null);
    }
    public void getData() {

    }


}
