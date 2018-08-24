package ru.vetatto.investing.investing;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Post {
   /* public static final MediaType FORM
            = MediaType.parse("application/x-www-form-urlencode;");*/

    OkHttpClient client = new OkHttpClient();

    Call post(String url, Callback callback) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", "vetatto@mail.ru")
                .addFormDataPart("password", "118823vet")
                .build();
        Request request = new Request.Builder()
                .header("Accept", "application/json")
                .url("http://hobbyhome44.ru:8080/api/login")
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }
}