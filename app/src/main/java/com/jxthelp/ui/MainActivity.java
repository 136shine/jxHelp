package com.jxthelp.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.jxthelp.App;
import com.jxthelp.R;
import com.jxthelp.api.GetUrl;
import com.jxthelp.bean.CourseInfo;
import com.jxthelp.bean.PersonalInfo;
import com.jxthelp.bean.XueQi;
import com.jxthelp.drawer.DrawerActionBar;
import com.jxthelp.fragment.DrawerFragment;
import com.jxthelp.fragment.FragmentKC;
import com.jxthelp.fragment.FragmentNews;
import com.jxthelp.fragment.FragmentWeather;
import com.jxthelp.util.HttpUtils;
import com.jxthelp.util.ToastUtils;
import com.jxthelp.util.VolleyRequest;

import org.apache.http.cookie.Cookie;
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
    private String[] fragmentName = {"新闻", "课程", "天气"};
    private Class fragmentArray[] = {FragmentNews.class, FragmentKC.class, FragmentWeather.class};
    private ImageView imageView;
    private TextView textView;
    //long
    private long mExitTime;

    private DisplayMetrics displayMetrics;
    public static int width;
    public static int height;
    public static Window mWindow;

    public static XueQi xueQi;
    private String kc;
    public static List<CourseInfo> listCourse = new ArrayList<CourseInfo>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWindow = getWindow();
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;

        View view = findViewById(R.id.drawer_main);
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
        getKc();
    }

    public void initTabView(View view) {
        mLayoutInflater = LayoutInflater.from(this);
        //找到tabHost
        mFragmentTabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
        //布局到Drawelayout上
        mFragmentTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        for (int i = 0; i < fragmentName.length; i++) {
            // 给每个Tab按钮设置图标和内容
            TabHost.TabSpec tabSpec = mFragmentTabHost.newTabSpec(fragmentName[i]).setIndicator(getTabView(i));
            // 将Tab按钮添加进Tab选项卡中
            mFragmentTabHost.addTab(tabSpec, fragmentArray[i], null);
        }
        //第一次进入改变默认第一个tab的颜色
        TextView tv = (TextView) mFragmentTabHost.getTabWidget().getChildAt(0).findViewById(R.id.tab_text);
        tv.setTextColor(getResources().getColor(R.color.drawer_text));

        //去除FragmentTabHost分割线
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
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

    public View getTabView(int i) {
        View view = mLayoutInflater.inflate(R.layout.tab_item, null);
        imageView = (ImageView) view.findViewById(R.id.tab_image);
        textView = (TextView) view.findViewById(R.id.tab_text);
        switch (i) {
            case 0:
                imageView.setImageResource(R.drawable.news_bg);
                break;
            case 1:
                imageView.setImageResource(R.drawable.kc_bg);
                break;
            case 2:
                imageView.setImageResource(R.drawable.weather_bg);
                break;
        }
        textView.setText(fragmentName[i]);
        return view;
    }

    //更改tab的颜色
    public void updateTab(FragmentTabHost tabHost) {
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            View view = tabHost.getTabWidget().getChildAt(i);
            TextView tv = (TextView) view.findViewById(R.id.tab_text);
            if (tabHost.getCurrentTab() == i) {
                tv.setTextColor(getResources().getColor(R.color.drawer_text));
            } else {
                tv.setTextColor(getResources().getColor(R.color.tab_text));
            }

        }

    }

    public void getKc() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    kc = HttpUtils.getHttp(GetUrl.Url2 + App.sp.getString("username", "") + "&xm=" + App.sp.getString("xm", "")
                            + "&gnmkdm=N122303", App.getHttpClient(), GetUrl.Url3 + App.sp.getString("username", ""), App.sp.getString("Cookie", ""));
                    //解析


                    Document doc = Jsoup.parse(kc);

                    //获取学期
                    Elements elementsXQ = doc.select("option[selected]");
                    if (!elementsXQ.text().toString().isEmpty()) {
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
                            }
                        }
                        for (int j = 0; j < list.size(); j++) {
                            //获取各个星期中的数据
                            Elements values = list.get(j).select("td[align=Center]");
                            for (int k = 0; k < values.size(); k++) {
                                //得到相关课的数据
                                if (!values.get(k).select("td[align=Center][rowspan=2]").text().isEmpty()) {
                                    CourseInfo data = new CourseInfo();
                                    int m = k + 1;
                                    data.setXinQi(m);
                                    //获取第一个<td></td>数据
                                    String contents = values.get(k).select("td").first().text();
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
                                    //判断是否有单双 设置结束周
                                    if (index4 != -1 || index5 != -1) {
                                        data.setEnd(Integer.parseInt(value[1].substring(index1 + 1, index2 - 1)));
                                    } else {
                                        data.setEnd(Integer.parseInt(value[1].substring(index1 + 1, index2)));
                                    }
                                    data.setClassNumber(Integer.parseInt(value[1].substring(index2 + 1, index3)));
                                    if (index4 == -1 && index5 == -1) {
                                        data.setFlag(-1);
                                    } else if (index4 != -1) {
                                        data.setFlag(1);
                                    } else {
                                        data.setFlag(0);
                                    }
                                    System.out.println("-------------");
                                    listCourse.add(data);
                                }

                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //防止按一次退出
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
        for (int i = 0; i < 4; i++) {
            VolleyRequest.cancelAll("News" + i);
        }
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
