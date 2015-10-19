package com.jxthelp.cache;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.LruCache;


/**
 * Created by idisfkj on 15/10/18.
 * Email : idisfkj@qq.com.
 */
public class BitmapCache extends LruCache<String,Bitmap> implements com.android.volley.toolbox.ImageLoader.ImageCache{
    public BitmapCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(String key, Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}
