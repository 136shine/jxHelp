package com.jxthelp;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.net.CookieStore;
import java.util.Objects;

/**
 * Created by idisfkj on 15-9-16 19:22.
 * Email: idisfkj@qq.com
 */
public class App extends Application {
    private static Context mContext;
    private static DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
    public static int page = 2;
    public static int XYPAGE = 2;
    public static int MTPAGE = 2;
    public static int XSPAGE = 2;
    public static int XXPAGE = 2;
    public static int ZBPAGE = 2;
    public static int ZPPAGE = 2;
    public static boolean lgIsFirstLoad = true;
    public static boolean xyIsFirstLoad = true;
    public static boolean mtIsFirstLoad = true;
    public static boolean xsIsFirstLoad = true;
    public static boolean xxIsFirstLoad = true;
    public static boolean zbIsFirstLoad = true;
    public static boolean zpIsFirstLoad = true;
    public static boolean TeacheIsFirst=true;
    public static boolean PersonIsFirst=true;
    public static boolean KcIsFirst=true;
    public static String CITY = "赣州";
    public static SharedPreferences.Editor editor;
    public static SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();
        sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
        mContext = getApplicationContext();
        initImageLoder(mContext);
    }

    public static DefaultHttpClient getHttpClient() {
        return defaultHttpClient;
    }

    public static Context getContext() {
        return mContext;
    }

    public static void initImageLoder(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "JXTHelp/Cache");
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024)).discCacheSize(2 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 3 * 1000))
                .discCache(new UnlimitedDiscCache(cacheDir))
                .build();
        ImageLoader.getInstance().init(configuration);
    }
}
