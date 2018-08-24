package ru.vetatto.investing.investing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

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
        final String email = prefs.getString("email"," ");
        final String password = prefs.getString("password"," ");
        final Post example = new Post();
        String response = null;
        Button btn_autorization = (Button) findViewById(R.id.button);
        btn_autorization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               EditText text_email = (EditText) findViewById(R.id.editText6);
               EditText text_password = (EditText) findViewById(R.id.editText7);
               final String input_email = text_email.getText().toString();
               final String input_password=text_password.getText().toString();
                if(!input_email.isEmpty() || !input_password.isEmpty()) {
                    example.post("", text_email.getText().toString(), text_password.getText().toString(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                String responseStr = response.body().string();
                                Log.d("TEST", responseStr);
                                Log.d("TEST", "Сохраняем токены доступа");
                                try {

                                    JSONObject dataJsonObj = new JSONObject(responseStr);
                                    JSONObject data = dataJsonObj.getJSONObject("data");
                                    String api_token = data.getString("api_token");
                                    Log.d("TEST", "API_TOKEN: " + api_token);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putString("API_TOKEN", api_token);
                                    editor.putString("email",input_email);
                                    editor.putString("password",input_password);
                                    editor.commit();
                                    if (!api_token.isEmpty()) {
                                        Intent intent = new Intent(context, MainActivity.class);
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
                else {
                    Log.d("INVESTING", "Логин и пароль пустые");
                }
            }

        });


        if(email.equals(" ") || password.equals(" ")){
            Log.d("INVESTING", "E-mail не указан");
        }
        else {
            example.post("", email, password, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseStr = response.body().string();
                        Log.d("TEST", responseStr);
                        Log.d("TEST", "Сохраняем токены доступа");
                        try {
                            JSONObject dataJsonObj = new JSONObject(responseStr);
                            JSONObject data = dataJsonObj.getJSONObject("data");
                            String api_token = data.getString("api_token");
                            Log.d("TEST", "API_TOKEN: " + api_token);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("API_TOKEN", api_token);
                            editor.commit();
                            if (!api_token.isEmpty()) {
                                Intent intent = new Intent(context, MainActivity.class);
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
}
