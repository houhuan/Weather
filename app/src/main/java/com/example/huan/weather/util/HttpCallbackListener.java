package com.example.huan.weather.util;

/**
 * Created by huan on 2016/8/18.
 */
public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}
