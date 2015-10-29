package com.jxthelp.adapter;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jxthelp.App;
import com.jxthelp.R;
import com.jxthelp.api.GetUrl;
import com.jxthelp.bean.News;
import com.jxthelp.cache.ImageCacheManager;
import com.jxthelp.request.Listener;
import com.jxthelp.request.NewsRequest;
import com.jxthelp.util.DensityUtils;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

/**
 * Created by idisfkj on 15/10/21.
 * Email : idisfkj@qq.com.
 */
public class XYAdapter extends BaseAdapter {
    private Drawable drawable;
    @Override
    public int getCount() {
        return NewsRequest.XYNewsList.size();
    }

    @Override
    public Object getItem(int position) {
        return NewsRequest.XYNewsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null) {
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(App.getContext()).inflate(R.layout.news_item,null);
            viewHolder.textView= (TextView) convertView.findViewById(R.id.news_text);
            viewHolder.date= (TextView) convertView.findViewById(R.id.date);
            viewHolder.newsIv= (ImageView) convertView.findViewById(R.id.news_iv);
            viewHolder.ll= (LinearLayout) convertView.findViewById(R.id.ll);
            viewHolder.fb= (TextView) convertView.findViewById(R.id.fb);
            viewHolder.tag= (ShimmerTextView) convertView.findViewById(R.id.top_tag);
            new Shimmer().start(viewHolder.tag);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        News news = NewsRequest.XYNewsList.get(position);
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
        return convertView;
    }
    public class ViewHolder{
        private TextView date;
        private TextView textView;
        private ImageView newsIv;
        private LinearLayout ll;
        private TextView fb;
        private ShimmerTextView tag;
        private com.android.volley.toolbox.ImageLoader.ImageContainer imageContainer;
    }
    public void upDate(final Listener listener) {
        NewsRequest.request(App.XYPAGE, new Listener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                App.XYPAGE = App.XYPAGE + 1;
                listener.onFinish();
            }

            @Override
            public void onError() {
                listener.onError();
            }

        },1);
        System.out.println("count:-----------" + App.XYPAGE);
    }
}
