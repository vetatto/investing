package ru.vetatto.investing.investing;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Registration extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);
        final Context context = this;
        Button btn_registration = (Button) findViewById(R.id.button_registration);
        btn_registration.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
               /*Intent intent = new Intent(context, Registration.class);
                context.startActivity(intent);*/
                //finish();
                final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                EditText text_email = (EditText) findViewById(R.id.registration_email);
                EditText text_password = (EditText) findViewById(R.id.registration_password);
                EditText text_name = (EditText) findViewById(R.id.registration_name);
                final String input_email = text_email.getText().toString();
                final String input_password=text_password.getText().toString();
                final String input_name=text_name.getText().toString();
                if(!input_email.isEmpty() || !input_password.isEmpty() || !input_name.isEmpty()) {
                    PostRegistration example = new PostRegistration();
                    example.Post("", text_email.getText().toString(), text_password.getText().toString(), text_name.getText().toString(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                String responseStr = response.body().string();
                                Log.d("TEST", responseStr);
                                Log.d("TEST", "Сохраняем токены доступа");
                                try{
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
                                    else{
                                        Log.d("TESTE",response.message());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                Log.d("TESTE",response.message());
                            }
                        }
                    });
                }
                else{
                    Log.d("TESTE", "Логин пароль или имя не заполнены");
                }
            }
        });
        /**
         * Для регистрации POST запрос на register
         * name
         * email
         * password
         * password_confirmation
         */
    }

}
