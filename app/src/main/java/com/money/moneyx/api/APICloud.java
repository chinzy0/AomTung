package com.money.moneyx.api;

import org.jetbrains.annotations.NotNull;
import okhttp3.Request;
import okhttp3.RequestBody;

public class APICloud extends Thread {

    private Request createRequest(String url, RequestBody body, Boolean method){
        Request.Builder builder =  new Request.Builder().url(url)
                .header("Content-Type", "application/json");

        if(method) builder.get(); else builder.post(body);
        return builder.build();
    }

    public Request GetExample(@NotNull RequestBody body) {
        String url = "https://api.example.com/";
        return createRequest(url, body, false);
    }

    public Request GetService(@NotNull RequestBody body) {
        String url = "https://jsonplaceholder.typicode.com/todos/1";
        return createRequest(url, body, true);
    }

    public Request GetService2(@NotNull RequestBody body) {
        String url = "https://jsonplaceholder.typicode.com/todos/1";
        return createRequest(url, body, true);
    }

    public Request GetOtp(@NotNull RequestBody body) {
        String url = "https://jsonplaceholder.typicode.com/todos/1";
        return createRequest(url, body, true);
    }
}
