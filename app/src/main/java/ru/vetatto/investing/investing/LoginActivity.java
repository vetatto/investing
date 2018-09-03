package ru.vetatto.investing.investing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences prefs;
    Context context;
    ProgressBar load;
    LinearLayout ll;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login2);
        activity=this;
        load = (ProgressBar) findViewById(R.id.load_autorize);
        ll = (LinearLayout) findViewById(R.id.autorize_main);
        final EditText text_email = (EditText) findViewById(R.id.login_email);
        final EditText text_password = (EditText) findViewById(R.id.login_password);
        text_email.setVisibility(View.VISIBLE);
        text_password.setVisibility(View.VISIBLE);
         context = this;
         prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final String email = prefs.getString("email"," ");
        final String password = prefs.getString("password"," ");
        final String GCM_id = prefs.getString("GCM_TOKEN","");
        final Post example = new Post();
        String response = null;
        TextView btn_registration = (TextView) findViewById(R.id.textView16);
        btn_registration.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(context, Registration.class);
                context.startActivity(intent);
                //finish();
            }
        });
        Button btn_autorization = (Button) findViewById(R.id.button);
        btn_autorization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
                text_email.setVisibility(View.INVISIBLE);
                text_password.setVisibility(View.INVISIBLE);
               EditText text_email = (EditText) findViewById(R.id.login_email);
               EditText text_password = (EditText) findViewById(R.id.login_password);
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
                                Log.d("TESTE", responseStr);
                                Log.d("TESTE", "Сохраняем токены доступа");
                                try{
                                    JSONObject dataJsonObj = new JSONObject(responseStr);
                                    JSONObject data = dataJsonObj.getJSONObject("data");
                                    String api_token = data.getString("api_token");
                                    Log.d("TESTE", "API_TOKEN: " + api_token);
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
                                Log.d("TESTE",response.toString());
                            }
                        }
                    });
                }
                else {
                    final Activity act = activity; //only neccessary if you use fragments
                    if (act != null)
                        act.runOnUiThread(new Runnable() {
                            public void run() {
                                show();
                            }
                        });
                    Log.d("TESTE", "Логин и пароль пустые");
                }
            }

        });


        if(email.equals(" ") || password.equals(" ")){
            text_email.setVisibility(View.VISIBLE);
            text_password.setVisibility(View.VISIBLE);
            Log.d("TESTE", "E-mail не указан");
            final Activity act = activity; //only neccessary if you use fragments
            if (act != null)
                act.runOnUiThread(new Runnable() {
                    public void run() {
                        show();
                    }
                });
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
                        Log.d("TESTE", responseStr);
                        Log.d("TESTE", "Сохраняем токены доступа");
                        try {
                            JSONObject dataJsonObj = new JSONObject(responseStr);
                            JSONObject data = dataJsonObj.getJSONObject("data");
                            String api_token = data.getString("api_token");
                            Log.d("TESTE", "API_TOKEN: " + api_token);
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
                        final Activity act = activity; //only neccessary if you use fragments
                        if (act != null)
                            act.runOnUiThread(new Runnable() {
                                public void run() {
                                    show();
                                }
                            });
                    }
                }
            });

        }
    }

    private void hide() {
        load.setVisibility(View.VISIBLE);
        ll.setVisibility(View.INVISIBLE);
    }
    private void show() {
        ll.setVisibility(View.VISIBLE);
        load.setVisibility(View.GONE);
    }

}
