package ru.vetatto.investing.investing;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FirstFragment extends Fragment {
    SharedPreferences prefs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        String email = prefs.getString("email", " ");
        String password = prefs.getString("password", " ");
        Log.d("TESTE", "Class of view: " + email);
        if(email.isEmpty()|| password.isEmpty()) {
            Log.d("TESTE", "Логин и password пустые");

        } else {
            Log.d("TEST", "Логин и пароль указаны");
            Log.d("TEST", "Получаем токены доступа");
            Post example = new Post();
            String response = null;

            example.post("", email, password, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseStr = null;
                    if (response.isSuccessful()) {
                        try {
                            responseStr = response.body().string();
                        }
                        catch (NullPointerException e){
                            
                        }
                        Log.d("TEST", responseStr);
                        Log.d("TEST", "Сохраняем токены доступа");
                        try {
                            JSONObject dataJsonObj = new JSONObject(responseStr);
                            JSONObject data = dataJsonObj.getJSONObject("data");
                            String api_token = data.getString("api_token");
                            Log.d("TESTE", "API_TOKEN: " + api_token);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("API_TOKEN", api_token);
                            editor.commit();
                            if (!api_token.isEmpty()) {
                                final Activity act = getActivity();
                                if (act != null)
                                    act.runOnUiThread(new Runnable() {
                                        public void run() {
                                            FragmentTransaction tran = getFragmentManager().beginTransaction();
                                            tran.replace(R.id.container, new SecondFragment()).commit();
                                        }
                                    });



                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }else{
                        Log.d("TESTE", "Ошибка авторизации");
                    }
                }
            });
        }
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public static FirstFragment newInstance() {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}