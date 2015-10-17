package com.jxthelp.ui;

import android.app.ProgressDialog;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jxthelp.App;
import com.jxthelp.R;
import com.jxthelp.bean.CourseInfo;
import com.jxthelp.bean.XueQi;
import com.jxthelp.util.HttpUtils;
import com.jxthelp.util.ToastUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.jsoup.Jsoup;
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
    private String url1 = "http://jw.jxust.cn/default2.aspx";
    private String url2 = "http://jw.jxust.cn/jstjkbcx.aspx?zgh=";
    private String url3 = "http://jw.jxust.cn/js_main.aspx?xh=";
    private String kc;
    private String message;

    private List<CourseInfo> courseInfos = new ArrayList<CourseInfo>();

    public static List<String> listLink = new ArrayList<String>();
    public static List<String> listTitle = new ArrayList<String>();
    public static List<CourseInfo> listCourse = new ArrayList<CourseInfo>();

    public static String user;
    public static String pass;
    private String xh;
    public static String xm;
    public static int i;

    public static XueQi xueQi;

    private int t;
    private long mExitTime;
    private ProgressDialog pd;
    private SharedPreferences sp;
    private boolean isSaved=false;

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
                if (listTitle.size() < 1) {
                    RequestQueue mRequestQueue = Volley.newRequestQueue(App.getContext());
                    StringRequest mStringRequest = new StringRequest("http://www.jxust.cn/", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            System.out.println("请求成功");
                            org.jsoup.nodes.Document doc = Jsoup.parse(s);
                            Elements elements = doc.select("ul[id=tc0]").select("a[href]");
                            for (int i = 0; i < elements.size(); i++) {
                                System.out.println(i + ":  " + elements.get(i));
                                System.out.println(i + ": " + elements.get(i).attr("href"));
                                listLink.add(elements.get(i).attr("href"));
                                System.out.println(i + ": " + elements.get(i).text());
                                listTitle.add(elements.get(i).text());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("请求失败：" + volleyError.toString());
                        }
                    });
                    mRequestQueue.add(mStringRequest);
                }
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
                    try {

                        kc = HttpUtils.getHttp(url2 + user + "&xm=" + xm + "&gnmkdm=N122303", App.getHttpClient(), url3 + user);
                        System.out.println("kc----------:" + kc);
                        //解析


                        org.jsoup.nodes.Document doc = Jsoup.parse(kc);

                        //获取学期
                        Elements elementsXQ = doc.select("option[selected]");
                        xueQi = new XueQi();
                        String s = elementsXQ.get(0).text();
                        int b = s.indexOf("-");
                        xueQi.setXnUp(s.substring(0, b));
                        xueQi.setXnDown(s.substring(b + 1));
                        xueQi.setXq(elementsXQ.get(1).text());

                        //获取课表
                        //获取每个<tr></tr>数据
                        Elements elements = doc.select(".formbox").select("tr");
                        List<Element> list = new ArrayList<Element>();
                        for (int i = 0; i < elements.size(); i++) {
                            //筛选出有课的<tr></tr>数据
                            if (!elements.get(i).select("td[align=Center][rowspan=2]").text().isEmpty()) {
                                list.add(elements.get(i));
                                System.out.println(i + ":" + elements.get(i));
                            }
                        }
                        for (int j = 0; j < list.size(); j++) {
                            //获取各个星期中的数据
                            Elements values = list.get(j).select("td[align=Center]");
                            System.out.println("values:     " + values);
                            for (int k = 0; k < values.size(); k++) {
                                //得到相关课的数据
                                if (!values.get(k).select("td[align=Center][rowspan=2]").text().isEmpty()) {
                                    CourseInfo data = new CourseInfo();
                                    int m = k + 1;
                                    System.out.println("星期：" + m);
                                    data.setXinQi(m);
                                    //获取第一个font数据
                                    String contents = values.get(k).select("font").first().text();
                                    String[] value = contents.split(" ");
                                    data.setCourseName(value[0]);
                                    data.setCourseRoom(value[3]);
                                    data.setCourseClass(value[4]);
                                    int index1 = value[1].indexOf("-");
                                    int index2 = value[1].indexOf("(");
                                    int index3 = value[1].indexOf(",");
                                    int index4 = value[1].indexOf("单");
                                    int index5 = value[1].indexOf("双");
                                    data.setStart(Integer.parseInt(value[1].substring(0, index1)));
                                    data.setEnd(Integer.parseInt(value[1].substring(index1 + 1, index2)));
                                    data.setClassNumber(Integer.parseInt(value[1].substring(index2 + 1, index3)));
                                    if (index4 == -1 && index5 == -1) {
                                        data.setFlag(-1);
                                    } else if (index4 != -1) {
                                        data.setFlag(1);
                                    } else {
                                        data.setFlag(0);
                                    }
                                    System.out.println("flag:" + data.getFlag());


                                    for (int n = 0; n < value.length; n++) {
                                        System.out.println("value" + n + ":" + value[n]);
                                    }
                                    System.out.println("-------------");
                                    listCourse.add(data);
                                }

                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (kc == null) {
                        pd.cancel();
                        message = "网络异常";
                    }
                    /*//保存用户信息
                    if(!sp.getBoolean("isSaved",false)) {
                        isSaved = true;
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("username", username);
                        editor.putString("password", password);
                        editor.putString("xm", xm);
                        editor.putBoolean("isSaved", true);
                        editor.commit();
                    }*/
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        pd.cancel();
                        finish();
                } else {
                    pd.cancel();
                    message = "登录失败";
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
        /*RequestQueue mRequestQueue=Volley.newRequestQueue(this);
        StringRequest mStringRequest=new StringRequest(url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                org.jsoup.nodes.Document doc=Jsoup.parse(s);
                Element element=doc.select("input[name=__VIEWSTATE]").first();
                System.out.println("sadfsadf:"+element);
                __VIEWSTATE=element.attr("value");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mRequestQueue.add(mStringRequest);*/

        try {
            temp = HttpUtils.getHttp(url1, App.getHttpClient(), "http://www.jxust.cn/");
//            System.out.println("temp:----------" + temp);

        } catch (IOException e) {
            System.out.println("eee");
            e.printStackTrace();
        }
        if (temp == "") {
            return null;

        }
        org.jsoup.nodes.Document doc = Jsoup.parse(temp);
        Element element = doc.select("input[name=__VIEWSTATE]").first();
        __VIEWSTATE = element.attr("value");
        System.out.println("__VIEWSTATE-----------" + __VIEWSTATE);
        return __VIEWSTATE;
    }

    //判读是否登入成功
    public boolean postSuccess() {
        String info = null;
        String cookieString = null;
        user = username.getText().toString();
        pass = password.getText().toString();
        //获取Cookie
        try {
            HttpUtils.postHttp(url1, App.getHttpClient(), new ArrayList<BasicNameValuePair>(),"");
            org.apache.http.client.CookieStore mCookieStore=App.getHttpClient().getCookieStore();
            List<Cookie> cookies=mCookieStore.getCookies();
            if(cookies.isEmpty()){
                System.out.println("Cookies为空");
            }else {
                for(int i=0;i<cookies.size();i++){
                    Cookie cookie=cookies.get(i);
                    Log.d("Cookie", cookies.get(i).getName() + "=" + cookies.get(i).getValue());
                    cookieString= cookie.getName() + "=" + cookie.getValue() + "; domain=" + cookie.getDomain();
                    /*SharedPreferences.Editor editor=sp.edit();
                    editor.putString("cookie",cookieString);
                    editor.commit();*/
                    System.out.println("Cookie:"+cookieString);
                }
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
            info = HttpUtils.postHttp(url1, App.getHttpClient(), pairs,cookieString);
            System.out.println("info-------------" + info);

        } catch (IOException e) {
            e.printStackTrace();
        }
        /*RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                System.out.println("s------------------------------:" + s.toString());
                org.jsoup.nodes.Document doc = Jsoup.parse(s);
                String userTeacher = doc.select("span[id=xhxm]").text();
                System.out.println("userTeacher:"+userTeacher);
                    int index = userTeacher.indexOf("老");
                if(index!=-1) {
                    xm = userTeacher.substring(0, index);
                    System.out.println("xm:" + xm);
                    i = 1;
                }else {
                    i = 0;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("请求出错！");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("__VIEWSTATE", getData());
                params.put("txtUserName", user);
                params.put("TextBox2", pass);
                params.put("RadioButtonList1", "%BD%CC%CA%A6");//%BD%CC%CA%A6&
                params.put("Button1","");
                params.put("lbLanguage","");
                params.put("hidPdrs","");
                params.put("hidsc","");
                return params;
            }
        };
        mRequestQueue.add(mStringRequest);*/
        if (info != null) {
            org.jsoup.nodes.Document doc = Jsoup.parse(info);
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
        /*tokenizer=new StringTokenizer(info);
        while(tokenizer.hasMoreTokens()){
            String values=tokenizer.nextToken();
            if(StringUtils.isValue(values,"defer")){
                System.out.println("用户名、密码、验证码出错");
                return false;
//                    t=-1;
            }
            if (StringUtils.isValue(values,"<title>ERROR")){
                System.out.println("系统出错");
                return false;
//                    t=0;
            }
            if(StringUtils.isValue(values,"id=\"xhxm")){
                xh=StringUtils.getValue(values,"id=\"xhxm\">","<",10);
                xm=StringUtils.getValue(xh,"","老",0);
//                    t=1;
                System.out.println("xh: "+xh);
                System.out.println("xm: "+xm);
                return true;
            }*/
    /*    }
        return true;
    }else {
//            t=2;
        System.out.println("空");
        return false;
    }*/
//        return true;
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
