package com.money.moneyx.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import okhttp3.Request;
import okhttp3.RequestBody;

public class APICloud extends Thread {

    private Request createRequest(String url, RequestBody body, Boolean method){
        Request.Builder builder =  new Request.Builder().url(url)
                .header("Content-Type", "application/json");

        if(method) builder.get();
        else builder.post(body);
        return builder.build();
    }
    public Request GetService(@NotNull RequestBody body) {
        String url = "https://jsonplaceholder.typicode.com/todos/1";
        return createRequest(url, body, true);
    }

    public Request GenerateOTP(@NotNull RequestBody body) {
        String url = "http://zaserzafear.thddns.net:9973/api/OTP/GenerateOTP";
        return createRequest(url, body, false);
    }
}
