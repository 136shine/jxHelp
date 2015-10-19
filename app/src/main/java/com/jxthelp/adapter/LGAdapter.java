package com.jxthelp.adapter;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.jxthelp.App;
import com.jxthelp.R;
import com.jxthelp.api.GetUrl;
import com.jxthelp.cache.BitmapCache;
import com.jxthelp.cache.ImageCacheManager;
import com.jxthelp.fragment.FragmentLGNews;
import com.jxthelp.ui.MainActivity;
import com.jxthelp.util.DensityUtils;
import com.jxthelp.util.HttpUtils;
import com.jxthelp.util.VolleyRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by idisfkj on 15/10/18.
 * Email : idisfkj@qq.com.
 */
public class LGAdapter extends BaseAdapter {
    private List<String> listTitle = MainActivity.listTitle;
    private List<String> listLink =MainActivity.listLink;
    private List<String> listImage=MainActivity.listImage;
    private List<String> listDate=MainActivity.listDate;
    private int count=2;
    private Drawable drawable;
    @Override
    public int getCount() {
        return listTitle.size();
    }

    @Override
    public Object getItem(int i) {
        return listTitle.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(App.getContext()).inflate(R.layout.lg_item, null);
            viewHolder.textView = (TextView) view.findViewById(R.id.lg_text);
            viewHolder.newsIv= (ImageView) view.findViewById(R.id.news_iv);
            viewHolder.date= (TextView) view.findViewById(R.id.date);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(listTitle.get(i));
        viewHolder.date.setText("日期:"+listDate.get(i));
        DisplayImageOptions options=new DisplayImageOptions.Builder()
                .cacheOnDisc(false)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)//是否考虑JPEG图像EXIF参数（旋转，翻转）
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
//                .showImageOnLoading(mDrawable)// 设置图片在下载期间显示的图片 可能会造成刷新闪烁
                .showImageOnFail(R.drawable.welcome)// 设置图片加载/解码过程中错误时候显示的图片
                .showImageForEmptyUri(R.drawable.welcome)// 设置图片Uri为空或是错误的时候显示的图片
                .build();
//        System.out.println("ImageUrl:======" + listImage.size());
//       ImageLoader imageLoader= com.nostra13.universalimageloader.core.ImageLoader.getInstance();
//        imageLoader.init(ImageLoaderConfiguration.createDefault(App.getContext()));
        /*DisplayImageOptions options=new DisplayImageOptions.Builder()
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();*/
//               ImageLoader.getInstance().displayImage(GetUrl.ImageUrl + listImage.get(i), viewHolder.newsIv, options);
        drawable=new ColorDrawable(App.getContext().getResources().getColor(R.color.title_kc));
        viewHolder.imageContainer= ImageCacheManager.loadImage(GetUrl.ImageUrl+listImage.get(i),
                ImageCacheManager.getImageListener(viewHolder.newsIv,drawable,drawable),
                DensityUtils.dip2px(App.getContext(),80), DensityUtils.dip2px(App.getContext(), 80));
        return view;
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
    public void upDate(Response.Listener listener,Response.ErrorListener errorListener){
        volleyRequest(listener,errorListener);
        count=count+1;
        System.out.println("count:-----------" + count);
    }
    public void volleyRequest(Response.Listener listener,Response.ErrorListener errorListener){
        StringRequest stringRequest=new StringRequest(GetUrl.LGUrl+"-"+count, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                org.jsoup.nodes.Document doc = Jsoup.parse(s);
                Elements elements = doc.select("li[class=frount1]").select("a[href]");
                for (int i = 0; i < elements.size(); i++) {
//                    System.out.println(i + ":  " + elements.get(i));
//                    System.out.println(i + ": " + elements.get(i).attr("href"));
                    String link=elements.get(i).attr("href");
                    listLink.add(link);

                    String text=elements.get(i).text();
                    int index=text.indexOf("]");
                    int index1=text.indexOf("[");
                    String date=text.substring(index1+1,index);
                    text=text.substring(index+1).trim();
                    listTitle.add(text);
                    listDate.add(date);
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(int n=20*(count-2);n<listLink.size();n++){
                            try {
                                String a = HttpUtils.getHttp(GetUrl.ImageUrl + listLink.get(n), App.getHttpClient(),GetUrl.ImageUrl);
                                Document doc1 = Jsoup.parse(a);
                                if (!doc1.select("center").text().isEmpty()) {
                                    Element element = doc1.select("center").select("img").first();
                                    String imageUrl = element.attr("src");
//                                    System.out.println("imageUrl:" + imageUrl);
                                    listImage.add(imageUrl);
                                } else {
//                                    System.out.println("aa");
                                    listImage.add("aa");
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (listImage.size() == 20*(count-1)) {
//                                FragmentLGNews.lgAdapter.notifyDataSetChanged();
                                mHandler.sendEmptyMessage(0);
                            }
                        }
                    }

                }).start();
//                FragmentLGNews.lgAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        VolleyRequest.addRequest(stringRequest, "LGNews");
    }

    public class ViewHolder {
        private TextView date;
        private TextView textView;
        private ImageView newsIv;
        private com.android.volley.toolbox.ImageLoader.ImageContainer imageContainer;
    }
}

