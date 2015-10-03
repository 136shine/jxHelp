package com.jxthelp.util;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by idisfkj on 15-9-28 08:54.
 * Email: idisfkj@qq.com
 */
public class HttpUtils {
    public HttpUtils() {
    }

    public static String getHttp(String url, DefaultHttpClient defaultHttpClient, String setHeader) throws IOException {
        HttpGet request = new HttpGet(url);
        request.setHeader("Referer", setHeader);
        HttpResponse response = defaultHttpClient.execute(request);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            return EntityUtils.toString(response.getEntity());
        }
        return "";
    }

    public static String postHttp(String url, DefaultHttpClient defaultHttpClient, List<BasicNameValuePair> pair, String setHeader) throws IOException {
        HttpPost request = new HttpPost(url);
        request.setEntity(new UrlEncodedFormEntity(pair, "gb2312"));//此处一定要用gb2312
        request.setHeader("Referer", setHeader);
        defaultHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
        defaultHttpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
        HttpResponse response = defaultHttpClient.execute(request);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            return EntityUtils.toString(response.getEntity());
        }
        return "";
    }
}
