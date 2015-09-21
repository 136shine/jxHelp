package com.jxthelp.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Created by idisfkj on 15-9-16 19:27.
 * Email: idisfkj@qq.com
 */
public abstract class BaseActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
