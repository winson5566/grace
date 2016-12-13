package com.awinson.utils;

import com.google.gson.Gson;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by winson on 2016/12/6.
 */
@Service
public class HttpUtils {

    final static MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static OkHttpClient client = new OkHttpClient();

    /**
     * 不带参数的GET请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String doGet(String url) throws IOException {
        Map<String, Object> map = new HashMap();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = null;
        response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String doPost(String url, Map<String, String> params) throws IOException {
        FormBody.Builder formBody = new FormBody.Builder();
        for (Map.Entry<String, String> map : params.entrySet()) {
            String key = map.getKey().toString();
            String value = "";
            if (map.getValue() != null) {
                value = map.getValue();
            }
            formBody.add(key, value);
        }
        RequestBody requestBody = formBody.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


}
