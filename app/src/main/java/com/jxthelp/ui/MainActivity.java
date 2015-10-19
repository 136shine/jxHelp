package com.jxthelp.ui;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jxthelp.App;
import com.jxthelp.R;
import com.jxthelp.api.GetUrl;
import com.jxthelp.drawer.DrawerActionBar;
import com.jxthelp.fragment.DrawerFragment;
import com.jxthelp.fragment.FragmentKC;
import com.jxthelp.fragment.FragmentLGNews;
import com.jxthelp.fragment.FragmentNews;
import com.jxthelp.fragment.FragmentTest;
import com.jxthelp.util.HttpUtils;
import com.jxthelp.util.ToastUtils;
import com.jxthelp.util.VolleyRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private DrawerActionBar drawerActionBar;
    private DrawerLayout drawerLayout;
    private FragmentTabHost mFragmentTabHost;
    private LayoutInflater mLayoutInflater;
    private String[] fragmentName={"新闻","课程","用户"};
    private Class fragmentArray[]={FragmentNews.class, FragmentKC.class, FragmentTest.class};
    private ImageView imageView;
    private TextView textView;
    private boolean isFirst=true;
    //long
    private long mExitTime;

    private DisplayMetrics displayMetrics;
    public static int width;
    public static int height;

    public static List<String> listLink = new ArrayList<String>();
    public static List<String> listTitle = new ArrayList<String>();
    public static List<String> listImage=new ArrayList<String>();
    public static List<String> listDate=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayMetrics =new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width=displayMetrics.widthPixels;
        height=displayMetrics.heightPixels;

        View view=findViewById(R.id.drawer_main);
        initTabView(view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerActionBar = new DrawerActionBar(this, drawerLayout, R.string.draweropen, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerActionBar);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.left_layout, new DrawerFragment()).commit();
        getData();
//        getImageData();
    }
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:FragmentLGNews.lgAdapter.notifyDataSetChanged();
                    break;

            }
        }
    };

    public void initTabView(View view){
        mLayoutInflater=LayoutInflater.from(this);
        //找到tabHost
        mFragmentTabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
        mFragmentTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        for(int i=0;i<fragmentName.length;i++){
            // 给每个Tab按钮设置图标和内容
            TabHost.TabSpec tabSpec=mFragmentTabHost.newTabSpec(fragmentName[i]).setIndicator(getTabView(i));
            // 将Tab按钮添加进Tab选项卡中
            mFragmentTabHost.addTab(tabSpec, fragmentArray[i], null);
        }
        //第一次进入改变默认第一个tab的颜色
        TextView tv= (TextView) mFragmentTabHost.getTabWidget().getChildAt(0).findViewById(R.id.tab_text);
        tv.setTextColor(getResources().getColor(R.color.drawer_text));

        //去除FragmentTabHost分割线
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.GINGERBREAD_MR1){
            mFragmentTabHost.getTabWidget().setShowDividers(0);
        }
        //tab改变时事件
        mFragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                updateTab(mFragmentTabHost);
            }
        });

    }
    public View getTabView(int i){
        View view=mLayoutInflater.inflate(R.layout.tab_item, null);
        imageView= (ImageView) view.findViewById(R.id.tab_image);
        textView = (TextView) view.findViewById(R.id.tab_text);
        switch (i){
            case 0:imageView.setImageResource(R.drawable.news_bg);break;
            case 1:imageView.setImageResource(R.drawable.kc_bg);break;
            case 2:imageView.setImageResource(R.drawable.user_bg);break;
        }
        textView.setText(fragmentName[i]);
        return view;
    }
    //更改tab的颜色
    public void updateTab(FragmentTabHost tabHost){
        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++){
            View view=tabHost.getTabWidget().getChildAt(i);
            TextView tv = (TextView) view.findViewById(R.id.tab_text);
            if(tabHost.getCurrentTab()==i) {
                tv.setTextColor(getResources().getColor(R.color.drawer_text));
            }else {
                tv.setTextColor(getResources().getColor(R.color.tab_text));
            }

        }

    }
    public void getData(){
        StringRequest mStringRequest = new StringRequest("http://www.jxust.cn/list/10", new Response.Listener<String>() {
            int m;
            @Override
            public void onResponse(String s) {
                org.jsoup.nodes.Document doc = Jsoup.parse(s);
                Elements elements = doc.select("li[class=frount1]").select("a[href]");
                for (int i = 0; i < elements.size(); i++) {
                    m = i;
                    String link = elements.get(i).attr("href");
                    listLink.add(link);
                    String text = elements.get(i).text();
                    int index = text.indexOf("]");
                    int index1 = text.indexOf("[");
                    String date = text.substring(index1 + 1, index);
                    text = text.substring(index + 1).trim();
                    listTitle.add(text);
                    listDate.add(date);

                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(int n=0;n<listLink.size();n++){
                            try {
                                String a = HttpUtils.getHttp(GetUrl.ImageUrl+listLink.get(n),App.getHttpClient(),GetUrl.ImageUrl);
                                Document doc1 = Jsoup.parse(a);
                                if (!doc1.select("center").text().isEmpty()) {
                                    Element element = doc1.select("center").select("img").first();
                                    String imageUrl = element.attr("src");
                                    System.out.println("imageUrl:" + imageUrl);
                                    listImage.add(imageUrl);
                                } else {
                                    System.out.println("aa");
                                    listImage.add("aa");
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (listImage.size() == 20) {
//                                FragmentLGNews.lgAdapter.notifyDataSetChanged();
                                mHandler.sendEmptyMessage(0);
                            }
                        }
                    }

                }).start();


                    //获取图片地址
/*                        StringRequest stringRequestIV = new StringRequest(GetUrl.ImageUrl + listLink.get(n), new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                Document doc = Jsoup.parse(s);
                                if (!doc.select("center").text().isEmpty()) {
                                    Element element = doc.select("center").select("img").first();
                                    String imageUrl = element.attr("src");
                                    System.out.println("imageUrl:" + imageUrl);
                                    listImage.add(imageUrl);
                                } else {
                                    System.out.println("aa");
                                    listImage.add("aa");
                                }
                                if (listImage.size() == 20) {
                                    FragmentLGNews.lgAdapter.notifyDataSetChanged();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                System.out.println("出错");
                            }
                        });
                        VolleyRequest.addRequest(stringRequestIV, "LGImage");*/


//                FragmentLGNews.lgAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("请求失败：" + volleyError.toString());
            }
        });
        VolleyRequest.addRequest(mStringRequest,"LGNews");
    }
    public void getImageData() {
        for (int i = 0; i < listLink.size(); i++) {

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            //防止按一次退出
            if (System.currentTimeMillis() - mExitTime >2000) {
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
        listTitle.clear();
        listLink.clear();
        listImage.clear();
        VolleyRequest.cancelAll("LGNews");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
