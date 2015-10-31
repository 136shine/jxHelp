package com.jxthelp.request;

import android.os.Handler;
import android.os.Message;

import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.jxthelp.api.GetUrl;
import com.jxthelp.bean.News;
import com.jxthelp.fragment.FragmentLGNews;
import com.jxthelp.fragment.FragmentMTNews;
import com.jxthelp.fragment.FragmentXSNews;
import com.jxthelp.fragment.FragmentXXNews;
import com.jxthelp.fragment.FragmentXYNews;
import com.jxthelp.fragment.FragmentZBNews;
import com.jxthelp.fragment.FragmentZPNews;
import com.jxthelp.util.VolleyRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 新闻请求类
 */
public class NewsRequest {

    public static List<News> LGNewsList = new ArrayList<News>();
    public static List<News> XYNewsList=new ArrayList<News>();
    public static List<News> MTNewsList=new ArrayList<News>();
    public static List<News> XSNewsList=new ArrayList<News>();
    public static List<News> XXNewsList=new ArrayList<News>();
    public static List<News> ZBNewsList=new ArrayList<News>();
    public static List<News> ZPNewsList=new ArrayList<News>();
    public static boolean isOk = false;
    private static String Url;
    private static List<News> list=new ArrayList<News>();

    /**
     * 请求新闻
     *
     * @param page
     * @param listener
     */
    public static void request(final int page, final Listener listener, final int i) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                _requestNews(page,i);
                if (isOk) {
                    switch (i){
                        case 0:mHandler.sendEmptyMessage(0);break;
                        case 1:mHandler.sendEmptyMessage(1);break;
                        case 2:mHandler.sendEmptyMessage(2);break;
                        case 3:mHandler.sendEmptyMessage(3);break;
                        case 4:mHandler.sendEmptyMessage(4);break;
                        case 5:mHandler.sendEmptyMessage(5);break;
                        case 6:mHandler.sendEmptyMessage(6);break;
                    }
                    isOk = false;
                    System.out.println("finish");
                    listener.onFinish();
                } else {
                    listener.onError();
                    System.out.println("error");
                }
            }
        }).start();

    }
    static Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:FragmentLGNews.lgAdapter.notifyDataSetChanged();break;
                case 1:
                    FragmentXYNews.xyAdapter.notifyDataSetChanged();break;
                case 2:
                    FragmentMTNews.mtAdapter.notifyDataSetChanged();break;
                case 3:
                    FragmentXSNews.xsAdapter.notifyDataSetChanged();break;
                case 4:
                    FragmentXXNews.xxAdapter.notifyDataSetChanged();break;
                case 5:
                    FragmentZBNews.zbAdapter.notifyDataSetChanged();break;
                case 6:
                    FragmentZPNews.zpAdapter.notifyDataSetChanged();break;

            }
        }
    };

    private static void _requestNews(int page,int n) {

        RequestFuture<String> future = RequestFuture.newFuture();
        switch (n){
            case 0:Url=GetUrl.LGUrl;break;
            case 1:Url=GetUrl.XYUrl;break;
            case 2:Url=GetUrl.MTUrl;break;
            case 3:Url=GetUrl.XSUrl;break;
            case 4:Url=GetUrl.XXUrl;break;
            case 5:Url=GetUrl.ZBUrl;break;
            case 6:Url=GetUrl.ZPUrl;break;
        }
        StringRequest LGNewsListRequest = new StringRequest(Url + "-" + page, future, future);
        VolleyRequest.addRequest(LGNewsListRequest, "News"+n);

        // 将新数据存储在临时列表中，最终加入到对应list里面，防止出现如下异常：
        // java.lang.IllegalStateException: The content of the adapter has changed but ListView ...
        List<News> newNewsList = new ArrayList<News>();

        try {
            String LGNewsListResult = future.get(30, TimeUnit.SECONDS);
            if (LGNewsListResult.length()==12924) {
                isOk = false;
            }else {
                isOk=true;
            }

            Document doc = Jsoup.parse(LGNewsListResult);
            Elements elements = doc.select("li[class=frount1]");
            for (int i = 0; i < elements.size(); i++) {
                News news = new News();
                if(!elements.get(i).select("font").text().isEmpty()){
                    news.setTag(1);
                }else {
                    news.setTag(0);
                }
                String link = elements.get(i).select("a[href]").attr("href");
                news.setUrl(link);
                String text = elements.get(i).select("a[href]").text();
                int index = text.indexOf("]");
                int index1 = text.indexOf("[");
                String date = text.substring(index1 + 1, index);
                text = text.substring(index + 1).trim();
                news.setTitle(text);
                news.setDate(date);
                newNewsList.add(news);
                switch (n){
                    case 0:
                        list=LGNewsList;
                        break;
                    case 1:
                        list=XYNewsList;
                        break;
                    case 2:
                        list=MTNewsList;
                        break;
                    case 3:
                        list=XSNewsList;
                        break;
                    case 4:
                        list=XXNewsList;
                        break;
                    case 5:
                        list=ZBNewsList;
                        break;
                    case 6:
                        list=ZPNewsList;
                        break;
                }

            }
        } catch (InterruptedException e) {
            isOk=false;
            e.printStackTrace();
        } catch (ExecutionException e) {
            isOk=false;
            e.printStackTrace();
        } catch (TimeoutException e) {
            isOk=false;
            e.printStackTrace();
        }

        // 同步请求图片
        for (News news:newNewsList) {
            future = RequestFuture.newFuture();
            StringRequest detailRequest = new StringRequest(GetUrl.ImageUrl + news.getUrl(), future, future);
            VolleyRequest.addRequest(detailRequest, "Image"+n);

            try {
                String detailResult = future.get(30, TimeUnit.SECONDS);
                Document detailDoc = Jsoup.parse(detailResult);
                String f=detailDoc.select(".page_line").text().trim();
                int index=f.indexOf("日");
                if(index!=-1) {
                    String fb = f.substring(0, index).trim();
                    news.setFb(fb);
                }
                if (!detailDoc.select("center").text().isEmpty()) {
                    Element element = detailDoc.select("center").select("img").first();
                    String imageUrl = element.attr("src");
                    news.setImage(imageUrl);
//                    switch (n){
//                        case 0:break;
//                        case 1:mHandler.sendEmptyMessage(1);break;
//                        case 2:mHandler.sendEmptyMessage(2);break;
//                        case 3:mHandler.sendEmptyMessage(3);break;
//                    }
                    // todo parse content
                    isOk=true;
                }
            } catch (InterruptedException e) {
                isOk=false;
                e.printStackTrace();
            } catch (ExecutionException e) {
                isOk=false;
                e.printStackTrace();
            } catch (TimeoutException e) {
                isOk=false;
                e.printStackTrace();
            }

        }

        // 第一页先清空数据
        if(page==1){
            list.clear();
        }

        list.addAll(newNewsList);
    }
}
