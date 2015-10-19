package com.jxthelp.util;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.jxthelp.App;

/**
 * Created by idisfkj on 15/10/18.
 * Email : idisfkj@qq.com.
 */
public class VolleyRequest {
    public static RequestQueue mRequestQueue = Volley.newRequestQueue(App.getContext());
    public static void addRequest(Request<?> request, Object object) {
        if (object != null) {
            request.setTag(object);
        }
        mRequestQueue.add(request);
    }
    public static void cancelAll(Object tag){
        mRequestQueue.cancelAll(tag);
    }

}
