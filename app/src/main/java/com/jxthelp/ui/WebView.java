package com.jxthelp.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jxthelp.R;
import com.jxthelp.fragment.FragmentLGNews;
import com.jxthelp.fragment.FragmentNews;

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
public class WebView extends BaseActivity {
    @InjectView(R.id.webView)
    android.webkit.WebView webView;
    private String mUrl = LoginActivity.listLink.get(FragmentLGNews.item);
    private static Elements elements1;
    private static Elements elements2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        ButterKnife.inject(this);
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest("http://www.jxust.cn" + mUrl, new Response.Listener<String>() {
            //            String text;
            @Override
            public void onResponse(String s) {
                Document doc = Jsoup.parse(s);
                elements1 = doc.select("center");
                elements2 = doc.select("p[style]");
                webView.loadDataWithBaseURL("http://www.jxust.cn/", "<div style=\"font-size:20px\">" + elements1.toString() + elements2.toString() + "</div>",
                        "text/html", "utf-8", null);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mRequestQueue.add(mStringRequest);
        WebSettings settings = webView.getSettings();
        //设置字体大小
        settings.setTextSize(WebSettings.TextSize.LARGEST);
//        settings.setTextZoom(30);
        //支持javaScript
        settings.setJavaScriptEnabled(true);
        //支持缩放
        settings.setSupportZoom(true);
        //出现缩放工具
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);//扩大比例的缩放
        //设置加载进来的页面自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
//        webView.loadUrl("http://www.jxust.cn" + mUrl);
//        webView.setWebViewClient(new WebViewClient() {
//            // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
//            @Override
//            public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//        });
    }
}