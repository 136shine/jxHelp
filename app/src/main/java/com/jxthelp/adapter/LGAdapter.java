package com.jxthelp.adapter;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.toolbox.ImageLoader;
import com.jxthelp.App;
import com.jxthelp.R;
import com.jxthelp.api.GetUrl;
import com.jxthelp.bean.News;
import com.jxthelp.cache.ImageCacheManager;
import com.jxthelp.request.Listener;
import com.jxthelp.request.NewsRequest;
import com.jxthelp.util.DensityUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

/**
 * Created by idisfkj on 15/10/18.
 * Email : idisfkj@qq.com.
 */
public class LGAdapter extends BaseAdapter {
    private Drawable drawable;

    @Override
    public int getCount() {
        return NewsRequest.LGNewsList.size();
    }

    @Override
    public Object getItem(int i) {
        return NewsRequest.LGNewsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        News news = NewsRequest.LGNewsList.get(i);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(App.getContext()).inflate(R.layout.news_item, null);
            viewHolder.ll= (LinearLayout) view.findViewById(R.id.ll);
            viewHolder.textView = (TextView) view.findViewById(R.id.news_text);
            viewHolder.newsIv = (ImageView) view.findViewById(R.id.news_iv);
            viewHolder.date = (TextView) view.findViewById(R.id.date);
            viewHolder.fb= (TextView) view.findViewById(R.id.fb);
            viewHolder.tag= (ShimmerTextView) view.findViewById(R.id.top_tag);
            new Shimmer().start(viewHolder.tag);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(news.getTitle());
        viewHolder.fb.setText(news.getFb());
        viewHolder.date.setText(news.getDate());
        viewHolder.newsIv.setTag(news.getImage());
        if(news.getTag()==1){
            viewHolder.tag.setVisibility(View.VISIBLE);
        }else {
            viewHolder.tag.setVisibility(View.GONE);
        }
        if(news.getImage()==null){
            viewHolder.textView.setLines(2);
            LinearLayout.LayoutParams l=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            viewHolder.ll.setLayoutParams(l);
            viewHolder.newsIv.setVisibility(View.GONE);
        }else {
            if(news.getImage().equals(viewHolder.newsIv.getTag())) {
                viewHolder.textView.setLines(3);
                LinearLayout.LayoutParams l=new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                l.weight=2;
                viewHolder.ll.setLayoutParams(l);
                viewHolder.newsIv.setVisibility(View.VISIBLE);
                drawable = new ColorDrawable(App.getContext().getResources().getColor(R.color.title_kc));
                viewHolder.imageContainer = ImageCacheManager.loadImage(GetUrl.ImageUrl + news.getImage(),
                        ImageCacheManager.getImageListener(viewHolder.newsIv, drawable, drawable),
                        DensityUtils.dip2px(App.getContext(), 180), DensityUtils.dip2px(App.getContext(), 180));
            }
        }
        return view;
    }


    public void upDate(final Listener listener) {
        NewsRequest.request(App.page, new Listener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                App.page = App.page + 1;
                listener.onFinish();
            }

            @Override
            public void onError() {
                listener.onError();
            }

        }, 0);
        System.out.println("count:-----------" + App.page);
    }

    public class ViewHolder {
        private LinearLayout ll;
        private TextView date;
        private TextView textView;
        private ImageView newsIv;
        private TextView fb;
        private ShimmerTextView tag;
        private com.android.volley.toolbox.ImageLoader.ImageContainer imageContainer;
    }
}

