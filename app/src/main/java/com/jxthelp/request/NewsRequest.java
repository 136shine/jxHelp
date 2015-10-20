package com.jxthelp.request;

import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.jxthelp.api.GetUrl;
import com.jxthelp.bean.News;
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

    public static List<News> newsList = new ArrayList<News>();

    /**
     * 请求新闻
     * @param page
     * @param listener
     */
    public static void request(final int page, final Listener listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                _requestNews(page);
                listener.onFinish();
            }
        }).start();

    }

    private static void _requestNews(int page) {

        RequestFuture<String> future = RequestFuture.newFuture();
        StringRequest newsListRequest = new StringRequest(GetUrl.LGUrl+"-"+page,future,future);
        VolleyRequest.addRequest(newsListRequest, "LGNews");

        try{
            String newsListResult=future.get(30, TimeUnit.SECONDS);

            Document doc = Jsoup.parse(newsListResult);
            Elements elements = doc.select("li[class=frount1]").select("a[href]");
            for (int i = 0; i < elements.size(); i++) {
                News news = new News();
                String link = elements.get(i).attr("href");
                news.setUrl(link);
                String text = elements.get(i).text();
                int index = text.indexOf("]");
                int index1 = text.indexOf("[");
                String date = text.substring(index1 + 1, index);
                text = text.substring(index + 1).trim();
                news.setTitle(text);
                news.setDate(date);
                newsList.add(news);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        // 同步请求图片
        for (News news : newsList) {
            future = RequestFuture.newFuture();
            StringRequest detailRequest = new StringRequest(GetUrl.ImageUrl + news.getUrl(), future, future);
            VolleyRequest.addRequest(detailRequest, "LGImage");

            try {
                String detailResult = future.get(30, TimeUnit.SECONDS);
                Document detailDoc = Jsoup.parse(detailResult);
                if (!detailDoc.select("center").text().isEmpty()) {
                    Element element = detailDoc.select("center").select("img").first();
                    String imageUrl = element.attr("src");
                    news.setImage(imageUrl);
                    // todo parse content
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
    }
}
