package com.jxthelp.sliding;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jxthelp.App;
import com.jxthelp.R;
import com.jxthelp.api.GetUrl;
import com.jxthelp.bean.PersonalInfo;
import com.jxthelp.dialog.MyDialog;
import com.jxthelp.swipeback.SwipeBackActivity;
import com.jxthelp.util.HttpUtils;
import com.jxthelp.util.ToastUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by Administrator on 2015/10/19.
 */
public class PersonalCenter extends SwipeBackActivity implements View.OnClickListener {
    private TextView number, name, sex, identity, birthday, zhengzhimianmao, school, xueli, degree, zhicheng;
    private String info;
    private PersonalInfo personalinfo = new PersonalInfo();
    private MyDialog pd;
    private ImageButton personBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sliding_personalcenter);
//        if(App.PersonIsFirst) {
            pd = new MyDialog(this);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
            getPersonalCenter();
//        }
        initView();
    }

    private void initView() {
        number = (TextView) findViewById(R.id.number);
        name = (TextView) findViewById(R.id.name);
        sex = (TextView) findViewById(R.id.sex);
        identity = (TextView) findViewById(R.id.identity);
        birthday = (TextView) findViewById(R.id.birthday);
        zhengzhimianmao = (TextView) findViewById(R.id.ZzMm);
        school = (TextView) findViewById(R.id.school);
        xueli = (TextView) findViewById(R.id.xueli);
        degree = (TextView) findViewById(R.id.degree);
        zhicheng = (TextView) findViewById(R.id.ZhiCheng);
        personBack= (ImageButton) findViewById(R.id.person_back);
        personBack.setOnClickListener(this);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    getdata();
                    break;
                case 1:
                    ToastUtils.showShort("网络异常");
                    break;
            }
        }
    };

    public void getdata() {
        number.setText(personalinfo.getNumber());
        name.setText(personalinfo.getName());
        sex.setText(personalinfo.getSex());
        identity.setText(personalinfo.getIdentity());
        birthday.setText(personalinfo.getBirthday());
        zhengzhimianmao.setText(personalinfo.getZhengzhimianmao());
        school.setText(personalinfo.getSchool());
        xueli.setText(personalinfo.getXueli());
        degree.setText(personalinfo.getDegree());
        zhicheng.setText(personalinfo.getZhicheng());
    }

    private void getPersonalCenter() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    info = HttpUtils.getHttp(GetUrl.PersonUrl + App.sp.getString("username", "") + "&xm=" + App.sp.getString("xm", "")
                                    + "&gnmkdm=N122501", App.getHttpClient(),
                            GetUrl.Url3 + App.sp.getString("username", ""), App.sp.getString("Cookie", ""));
                    //解析
                    Document doc = Jsoup.parse(info);
                    //获得每个tr集合
                    Elements elements = doc.select(".formlist").select("tr");
                    //获得第一个tr里的数据得到职工号和姓名
                    Elements elements1 = elements.get(0).select("td[width=200]");
                    //获得第二个tr里的数据得到性别和民族
                    Elements elements3 = elements.get(1).select("option[selected][value=男]");
                    Elements elements4 = elements.get(1).select("option[selected][value=1]");
                    //获得第三个tr里的数据得到出生日期和政治面貌
                    String elements5 = elements.get(2).select("input").attr("value");
                    Elements elements6 = elements.get(2).select("option[selected][value=1]");
                    //获得第四个tr里的数据得到学院
                    Elements elements7 = elements.get(3).select("option[selected][value=05]");
                    //获得第七个tr里的数据得到学历和学位
                    Elements elements8 = elements.get(6).select("option[selected][value=硕士研究生]");
                    Elements elements9 = elements.get(6).select("option[selected][value=硕士]");
                    //获得第八个tr里的数据得到职称
                    Elements elements10 = elements.get(7).select("option[selected][value=副教授]");
                    personalinfo.setNumber(elements1.text().toString());
                    personalinfo.setName(App.sp.getString("xm", ""));
                    personalinfo.setSex(elements3.text().toString());
                    personalinfo.setIdentity(elements4.text().toString());
                    personalinfo.setBirthday(elements5.toString());
                    personalinfo.setZhengzhimianmao(elements6.text().toString());
                    personalinfo.setSchool(elements7.text().toString());
                    personalinfo.setXueli(elements8.text().toString());
                    personalinfo.setDegree(elements9.text().toString());
                    personalinfo.setZhicheng(elements10.text().toString());
//                    App.PersonIsFirst=false;
                    mHandler.sendEmptyMessage(0);
                    pd.dismiss();
                } catch (Exception e) {
//                    App.PersonIsFirst=true;
                    mHandler.sendEmptyMessage(1);
                    pd.dismiss();
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}

