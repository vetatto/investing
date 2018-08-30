package ru.vetatto.investing.investing;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Get {
   /* public static final MediaType FORM
            = MediaType.parse("application/x-www-form-urlencode;");*/

    OkHttpClient client = new OkHttpClient();
    Call Get(String url, String token, Callback callback) {
        Request request = new Request.Builder()
                .header("Accept", "application/json")
                .addHeader("Authorization", "Bearer "+token)
                .url("http://hobbyhome44.ru:8080/api"+url)
                .get()

                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }
}