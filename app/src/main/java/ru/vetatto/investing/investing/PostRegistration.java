package ru.vetatto.investing.investing;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class PostRegistration {
   /* public static final MediaType FORM
            = MediaType.parse("application/x-www-form-urlencode;");*/

    private OkHttpClient client = new OkHttpClient();

    Call Post(String url, String email, String password, String name,  Callback callback) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", email)
                .addFormDataPart("password", password)
                .addFormDataPart("name", name)
                .addFormDataPart("password_confirmation", password)
                .build();
        Request request = new Request.Builder()
                .header("Accept", "application/json")
                .url("http://hobbyhome44.ru:8080/api/register")
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }
}