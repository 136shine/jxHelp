package com.jxthelp.request;

/**
 * 请求监听类
 */
public interface Listener {

    public void onStart();
    public void onFinish();
    public void onError();
}
