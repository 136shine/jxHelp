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
import com.jxthelp.App;
import com.jxthelp.R;
import com.jxthelp.api.GetUrl;
import com.jxthelp.bean.News;
import com.jxthelp.cache.ImageCacheManager;
import com.jxthelp.fragment.FragmentLGNews;
import com.jxthelp.request.Listener;
import com.jxthelp.request.NewsRequest;
import com.jxthelp.util.DensityUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * Created by idisfkj on 15/10/18.
 * Email : idisfkj@qq.com.
 */
public class LGAdapter extends BaseAdapter {
    private int count=2;
    private Drawable drawable;
    @Override
    public int getCount() {
        return NewsRequest.newsList.size();
    }

    @Override
    public Object getItem(int i) {
        return NewsRequest.newsList.get(i);
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
        News news = NewsRequest.newsList.get(i);
        viewHolder.textView.setText(news.getTitle());
        viewHolder.date.setText("日期:"+news.getDate());
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
        viewHolder.imageContainer= ImageCacheManager.loadImage(GetUrl.ImageUrl+news.getImage(),
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
    public void upDate(final Response.Listener listener,Response.ErrorListener errorListener){
        count=count+1;
        NewsRequest.request(count, new Listener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                mHandler.sendEmptyMessage(0);

            }
        });
        System.out.println("count:-----------" + count);
    }

    public class ViewHolder {
        private TextView date;
        private TextView textView;
        private ImageView newsIv;
        private com.android.volley.toolbox.ImageLoader.ImageContainer imageContainer;
    }
}

