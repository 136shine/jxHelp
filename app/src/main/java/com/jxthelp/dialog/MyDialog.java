package com.jxthelp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.jxthelp.R;

/**
 * Created by idisfkj on 15/10/22.
 * Email : idisfkj@qq.com.
 */
public class MyDialog extends Dialog {
    public MyDialog(Context context) {
        super(context, R.style.loadingDialogStyle);
    }

    public MyDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
    }
}
