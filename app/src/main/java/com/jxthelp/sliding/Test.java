package com.jxthelp.sliding;

/**
 * Created by idisfkj on 15/10/31.
 * Email : idisfkj@qq.com.
 */

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.jxthelp.App;
import com.jxthelp.R;
import com.jxthelp.adapter.TestAdapter;
import com.jxthelp.api.GetUrl;
import com.jxthelp.bean.Testinfo;
import com.jxthelp.dialog.MyDialog;
import com.jxthelp.ui.LoginActivity;
import com.jxthelp.util.HttpUtils;
import com.jxthelp.util.ToastUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class Test extends Activity implements View.OnClickListener {
    private String info;
    private ListView listview;
    public static List<Testinfo> listTest = new ArrayList<Testinfo>();
    private TestAdapter testAdapter;
    private MyDialog pd;
    private ImageButton testBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sliding_test);
        if (App.TeacheIsFirst) {
            pd = new MyDialog(this);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
            getHttp();
        }
        initView();
        testAdapter = new TestAdapter();
        listview.setAdapter(testAdapter);
    }

    private void initView() {
        listview = (ListView) findViewById(R.id.text_content);
        testBack = (ImageButton) findViewById(R.id.test_back);
        testBack.setOnClickListener(this);
    }

    private void getHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    info = HttpUtils.getHttp(GetUrl.TestUrl + App.sp.getString("username", "") + "&xm" + App.sp.getString("xm", "") + "&gnmkdm=N122306",
                            App.getHttpClient(),
                            GetUrl.Url3 + App.sp.getString("username", ""), App.sp.getString("Cookie", ""));
                    //解析
                    Document doc = Jsoup.parse(info);
                    //获得tr集合
                    Elements elements = doc.select(".datelist").select("tr");
//                    System.out.println(elements.toString());
                    //获得每个tr集合里的数据
                    for (int j = 1; j < elements.size(); j++) {
                        String values = elements.get(j).text();
                        String[] value = values.split(" ");
                        //过滤掉重复的数据
                        if (value[3].toString().trim().length() == 4) {
                            Testinfo testinfo = new Testinfo();
                            testinfo.setName(value[0]);
                            testinfo.setTime(value[1]);
                            testinfo.setTeacher(value[2]);
                            testinfo.setRoom(value[3]);
                            testinfo.setNumber(value[4]);
                            testinfo.setTeacher1(value[5]);
                            testinfo.setTeacher2(value[7]);
                            testinfo.setTeacher3(value[9]);
                            listTest.add(testinfo);
                        }
                    }
                    App.TeacheIsFirst = false;
                    pd.dismiss();
                    mHandler.sendEmptyMessage(0);
                } catch (Exception e) {
                    mHandler.sendEmptyMessage(1);
                    App.TeacheIsFirst = true;
                    e.printStackTrace();
                }
            }
        }).start();
    }

    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    testAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    pd.dismiss();
                    ToastUtils.showShort("网络异常");
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        finish();
    }
}

