package com.jxthelp.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jxthelp.App;
import com.jxthelp.R;
import com.jxthelp.api.GetUrl;
import com.jxthelp.bean.CourseInfo;
import com.jxthelp.bean.XueQi;
import com.jxthelp.util.HttpUtils;
import com.jxthelp.util.ToastUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by idisfkj on 15-9-27 11:29.
 * Email: idisfkj@qq.com
 */
public class LoginActivity extends BaseActivity {
    private EditText username;
    private EditText password;
    private Button login;
    private Button test;
    private String message;

    public static String user;
    public static String pass;
    public static String xm;
    public static int i;

    private long mExitTime;
    private ProgressDialog pd;
    private int error = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initView();

        App.getHttpClient().getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
        App.getHttpClient().getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(username.getText().toString().trim(), password.getText().toString().trim());
            }
        });
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void initView() {
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login_bt);
        test = (Button) findViewById(R.id.test);
    }

    public void login(final String username, final String password) {
        message = "登录成功";
        if (username == null || username.length() <= 0) {
            ToastUtils.showShort("请输入账号");
            return;
        }
        if (password == null || password.length() <= 0) {
            ToastUtils.showShort("请输入密码");
            return;
        }

        pd = ProgressDialog.show(this, " 登录中...", "请稍后...", true);

        //要在主线程外加载
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (postSuccess()) {

                    //保存用户信息
                    if (!App.sp.getBoolean("isSaved", false)) {
                        App.editor = App.sp.edit();
                        App.editor.putString("username", username);
                        App.editor.putString("password", password);
                        App.editor.putString("xm", xm);
                        App.editor.putBoolean("isSaved", true);
                        App.editor.commit();
                    }
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    pd.cancel();
                    finish();
                } else if (error == -1) {
                    pd.cancel();
                    message = "系统异常，请稍后在试";
                } else {
                    message = "用户名或密码错误";
                    pd.cancel();
                }

                handler.sendMessage(handler.obtainMessage());
            }
        }).start(); //start


    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ToastUtils.showShort(message);
        }
    };


    //获取post __VIEWSTATE的参数值
    private String getData() {
        String __VIEWSTATE = "";
        String temp = "";

        try {
            temp = HttpUtils.getHttp(GetUrl.Url1, App.getHttpClient(), "http://www.jxust.cn/", "");

        } catch (IOException e) {
            System.out.println("eee");
            e.printStackTrace();
        }
        if (temp == "") {
            return null;

        } else {
            org.jsoup.nodes.Document doc = Jsoup.parse(temp);
            Element element = doc.select("input[name=__VIEWSTATE]").first();
            __VIEWSTATE = element.attr("value");
            System.out.println("__VIEWSTATE-----------" + __VIEWSTATE);
            return __VIEWSTATE;
        }
    }

    //判读是否登入成功
    public boolean postSuccess() {
        String info = null;
        String cookieString = null;
        CookieStore mCookieStore = null;
        user = username.getText().toString();
        pass = password.getText().toString();
        //获取Cookie
        try {
            HttpUtils.postHttp(GetUrl.Url1, App.getHttpClient(), new ArrayList<BasicNameValuePair>(), "");
            mCookieStore = App.getHttpClient().getCookieStore();
            List<Cookie> cookies = mCookieStore.getCookies();
            for (int i = 0; i < cookies.size(); i++) {
                Cookie cookie = cookies.get(i);
                String name = cookie.getName();
                String value = cookie.getValue();
                StringBuffer sb = new StringBuffer();
                sb.append(name + "=" + value + ";");
                App.editor = App.sp.edit();
                App.editor.putString("Cookie", sb.toString());
                App.editor.commit();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
        pairs.add(new BasicNameValuePair("__VIEWSTATE", getData()));
        pairs.add(new BasicNameValuePair("txtUserName", user));
        pairs.add(new BasicNameValuePair("TextBox2", pass));
        pairs.add(new BasicNameValuePair("RadioButtonList1", "教师"));
        pairs.add(new BasicNameValuePair("Button1", null));
        pairs.add(new BasicNameValuePair("lbLanguage", null));

        try {
            System.out.println("22222222");
            info = HttpUtils.postHttp(GetUrl.Url1, App.getHttpClient(), pairs, cookieString);
            System.out.println("info-------------" + info);

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (info != null) {
            org.jsoup.nodes.Document doc = Jsoup.parse(info);
            String title = doc.select("title").text().toString().substring(0, 5);
            if (title.equals("ERROR")) {
                error = -1;
            }
            String userTeacher = doc.select("span[id=xhxm]").text();
            System.out.println("userTeacher:" + userTeacher);
            int index = userTeacher.indexOf("老");
            if (index != -1) {
                xm = userTeacher.substring(0, index);
                System.out.println("xm:" + xm);
                i = 1;
            } else {
                i = 0;
            }
        }
        if (i == 1) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                ToastUtils.showShort("再按一次回到桌面");
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
