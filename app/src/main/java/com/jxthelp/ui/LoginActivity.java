package com.jxthelp.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jxthelp.R;
import com.jxthelp.util.HttpUtils;
import com.jxthelp.util.StringUtils;
import com.jxthelp.util.ToastUtils;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by idisfkj on 15-9-27 11:29.
 * Email: idisfkj@qq.com
 */
public class LoginActivity extends BaseActivity {
    private EditText username;
    private EditText password;
    private Button login;
    private String url1="http://jw.jxust.cn/default2.aspx";
    private String url2="http://jw.jxust.cn/jstjkbcx.aspx?zgh=";
    private String url3="http://jw.jxust.cn/js_main.aspx?xh=";
    private String kc;

    public static String user;
    public static String pass;
    private String xh;
    public static String xm;

    private int t;

    private DefaultHttpClient defaultHttpClient=new DefaultHttpClient();

    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initView();
        defaultHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
        defaultHttpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(username.getText().toString().trim(),password.getText().toString().trim());
            }
        });

    }
    public void initView(){
        username= (EditText) findViewById(R.id.username);
        password= (EditText) findViewById(R.id.password);
        login= (Button) findViewById(R.id.login_bt);
    }
    public void login(final String username,String password){
        if(username==null||username.length()<=0){
            ToastUtils.showShort("用户名不能为空!");
            return;
        }
        if(password==null|| password.length()<=0 ){
            ToastUtils.showShort("密码不能为空!");
            return;
        }
        pd=ProgressDialog.show(this,"登入中...","请稍后",true);

        //要在主线程外加载
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (postSuccess()){
                    try {
                        kc=HttpUtils.getHttp(url2+user+"&xm="+xm+"&gnmkdm=N122303",defaultHttpClient,url3+user);
                        System.out.println("kc----------:"+kc);
                        //解析

                        org.jsoup.nodes.Document doc= Jsoup.parse(kc);
                        //获取每个<tr></tr>数据
                        Elements elements=doc.select(".formbox").select("tr");
                        List<Element> list=new ArrayList<Element>();
                        for(int i=0;i<elements.size();i++){
                            //筛选出有课的<tr></tr>数据
                            if(!elements.get(i).select("td[align=Center][rowspan=2]").text().isEmpty()){
                                list.add(elements.get(i));
                                System.out.println(i+":"+elements.get(i));
                            }
                        }
                        for(int j=0;j<list.size();j++){
                            //获取各个星期中的数据
                            Elements values=list.get(j).select("td[align=Center]");
                            System.out.println("values:     "+values);
                            for(int k=0;k<values.size();k++){
                                //得到相关课的数据
                                if(!values.get(k).select("td[align=Center][rowspan=2]").text().isEmpty()){
                                    System.out.println("星期："+(k+1));
                                    String contents=values.get(k).select("font").first().text();
                                    String[] value=contents.split(" ");
                                    for (int n=0;n<value.length;n++){
                                        System.out.println("value"+n+":"+value[n]);
                                    }
                                    System.out.println("-------------");
                                }
                            }
                        }


                        /*Elements elements=doc.select(".formbox").select("td[align=Center][rowspan=2]");
                        System.out.println("elements------------:"+elements);
                        for(int i=0;i<elements.size();i++){
                            String values=elements.get(i).select("font").first().text();
                            String[] value=values.split(" ");
                            System.out.println("values:"+values);
                            for (int j=0;j<value.length;j++){
                                System.out.println("value"+j+":"+value[j]);
                            }
                            System.out.println("---------");
                        }*/


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    pd.cancel();
                    finish();
                }else {

                }


            }
        }).start(); //start


    }


    //获取post __VIEWSTATE的参数值
    private String getData(){
        String __VIEWSTATE="";
        String  temp="";
        StringTokenizer tokenizer=null;

        try {
            temp= HttpUtils.getHttp(url1,defaultHttpClient,"");

            System.out.println("temp:----------"+temp);

        } catch (IOException e) {
            System.out.println("eee");
            e.printStackTrace();
        }
        if(temp == ""){
            return null;

        }
        //默认分割字符串
        tokenizer=new StringTokenizer(temp);

        System.out.println("tokenizer----------:"+tokenizer);

        while(tokenizer.hasMoreElements()){
            String values=tokenizer.nextToken();
            if(StringUtils.isValue(values,"value")){
                if(StringUtils.getValue(values,"value","\"",7).length()==48){
                    __VIEWSTATE=StringUtils.getValue(values,"value","\"",7);
                }
            }
        }
        System.out.println("__VIEWSTATE------------" + __VIEWSTATE);

        return __VIEWSTATE;
    }

    //判读是否登入成功
    public boolean postSuccess(){
        List<Cookie> cookies=defaultHttpClient.getCookieStore().getCookies();
        Cookie cookie;
        if(cookies.isEmpty()){
            Log.d("TAG","cookie为空");
        }else {
            for(int i=0;i<cookies.size();i++){
//                cookie=cookies.get(i);
                Log.d("TAG",cookies.get(i).getName()+"="+cookies.get(i).getValue());
            }
        }
        StringTokenizer tokenizer;
        user=username.getText().toString();
        pass=password.getText().toString();

        List<BasicNameValuePair> pairs=new ArrayList<BasicNameValuePair>();
        pairs.add(new BasicNameValuePair("__VIEWSTATE",getData()));
        pairs.add(new BasicNameValuePair("txtUserName", user));
        pairs.add(new BasicNameValuePair("TextBox2", pass));
        pairs.add(new BasicNameValuePair("RadioButtonList1","教师"));
        pairs.add(new BasicNameValuePair("Button1", null));
        pairs.add(new BasicNameValuePair("lbLanguage", null));
        String info=null;
        try {
           info= HttpUtils.postHttp(url1,defaultHttpClient,pairs,"");
            System.out.println("info-------------"+info);

        } catch (IOException e) {
            e.printStackTrace();
        }
        /*RequestQueue mRequestQueue= Volley.newRequestQueue(this);
        StringRequest mStringRequest=new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                System.out.println("s------------------------------:"+s.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<String,String>();
                params.put("__VIEWSTATE",getData());
                params.put("txtUserName", user);
                params.put("TextBox2", pass);
                params.put("RadioButtonList1", "%BD%CC%CA%A6&");
                return params;
            }
        };
        mRequestQueue.add(mStringRequest);*/
        if(info !=null){
            tokenizer=new StringTokenizer(info);
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
                }
            }
            return true;
        }else {
//            t=2;
            System.out.println("空");
            return false;
        }
//        return true;
    }
}
