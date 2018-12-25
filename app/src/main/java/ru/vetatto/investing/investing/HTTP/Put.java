package ru.vetatto.investing.investing.HTTP;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Put {

    private OkHttpClient client = new OkHttpClient();

    public Call put(String url, String token, JSONObject json, Callback callback) {
        String jsonString = json.toString();
        RequestBody body = RequestBody.create(MediaType.get("JSON"),jsonString);
        Request request = new Request.Builder()
                .header("Accept", "application/json")
                .addHeader("Content-Type","application/json")
                .addHeader("Authorization", "Bearer "+token)
                .url("http://hobbyhome44.ru:8080/api"+url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }
}
