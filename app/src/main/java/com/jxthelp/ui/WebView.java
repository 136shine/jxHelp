package com.jxthelp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jxthelp.R;
import com.jxthelp.dialog.MyDialog;
import com.jxthelp.request.NewsRequest;
import com.jxthelp.swipeback.SwipeBackActivity;
import com.jxthelp.util.ToastUtils;
import com.jxthelp.util.VolleyRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by idisfkj on 15/10/8.
 * Email : idisfkj@qq.com.
 */
public class WebView extends SwipeBackActivity implements View.OnClickListener {
    @InjectView(R.id.webView)
    android.webkit.WebView webView;
    @InjectView(R.id.web_back)
    ImageButton webBack;
    @InjectView(R.id.web_title)
    TextView webTitle;
    @InjectView(R.id.news_title)
    TextView newsTitle;
    private String mUrl;
    private static Elements elements1;
    private static Element element;
    private MyDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        ButterKnife.inject(this);
        dialog=new MyDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Intent intent = getIntent();
        final int i = (int) intent.getExtras().get("Item");
        int current = (int) intent.getExtras().get("Current");
        switch (current) {
            case 0:
                mUrl = NewsRequest.LGNewsList.get(i).getUrl();
                webTitle.setText("理工新闻");
                newsTitle.setText(NewsRequest.LGNewsList.get(i).getTitle());
                break;
            case 1:
                mUrl = NewsRequest.XYNewsList.get(i).getUrl();
                webTitle.setText("校园传真");
                newsTitle.setText(NewsRequest.XYNewsList.get(i).getTitle());
                break;
            case 2:
                mUrl = NewsRequest.MTNewsList.get(i).getUrl();
                webTitle.setText("媒体聚焦");
                newsTitle.setText(NewsRequest.MTNewsList.get(i).getTitle());
                break;
            case 3:
                mUrl = NewsRequest.XSNewsList.get(i).getUrl();
                webTitle.setText("学术公告");
                newsTitle.setText(NewsRequest.XSNewsList.get(i).getTitle());
                break;

        }
        webBack.setOnClickListener(this);
        StringRequest mStringRequest = new StringRequest("http://www.jxust.cn" + mUrl, new Response.Listener<String>() {
            //            String text;
            @Override
            public void onResponse(String s) {
                Document doc = Jsoup.parse(s);
                elements1 = doc.select("center");
                element = doc.select("p[style]").first();

                webView.loadDataWithBaseURL("http://www.jxust.cn/", "<h3 style=\"color:#800000; font-size:30px\">" + NewsRequest.LGNewsList.get(i).getTitle()+"</h3>"+
                        "<div style=\"word-break:break-all;font-size:25px\"><center>" + elements1.toString() +"</center>" +
                                "<<p style=\"text-indent: 2em;\">"+element.toString() + "</p><ul style=\"margin-left: 50px;\">" +
                                "</ul></div>",
                        "text/html", "utf-8", null);
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialog.dismiss();
                ToastUtils.showShort("网络异常");
            }
        });
        VolleyRequest.addRequest(mStringRequest, "LGLink");
        WebSettings settings = webView.getSettings();
        //设置字体大小
        settings.setTextSize(WebSettings.TextSize.LARGEST);
//        settings.setTextZoom(30);
        //支持javaScript
        settings.setJavaScriptEnabled(true);
        //支持缩放
        settings.setSupportZoom(true);
        //出现缩放工具
        settings.setDisplayZoomControls(false);
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);//扩大比例的缩放
        //设置加载进来的页面自适应屏幕
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
