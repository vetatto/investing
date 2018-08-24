package ru.vetatto.investing.investing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences prefs;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login2);
         context = this;
         prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String email = prefs.getString("email"," ");
        Post example = new Post();
        String response = null;

        example.post("", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    Log.d("TEST",responseStr);
                    Log.d("TEST","Сохраняем токены доступа");
                    try {
                        JSONObject dataJsonObj = new JSONObject(responseStr);
                        JSONObject data = dataJsonObj.getJSONObject("data");
                        String api_token = data.getString("api_token");
                        Log.d("TEST","API_TOKEN: "+api_token);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("API_TOKEN", api_token);
                        editor.commit();
                        if(!api_token.isEmpty()){
                            Intent intent = new Intent(context,MainActivity.class);
                            context.startActivity(intent);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    // Request not successful
                }
            }
        });


    }
}
