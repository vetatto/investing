package ru.vetatto.investing.investing.UK;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ru.vetatto.investing.investing.HTTP.Get;
import ru.vetatto.investing.investing.R;
import ru.vetatto.investing.investing.SMS.SMSAdapter;
import ru.vetatto.investing.investing.SMS.SMSData;

public class UKlist extends AppCompatActivity {
    ArrayList<UKData> ukData = new ArrayList();
    UKAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uklist);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
        actionbar.setTitle("Список Управляющих");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        RecyclerView recyclerView = findViewById(R.id.UKlist);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new UKAdapter(this, ukData);
        recyclerView.setAdapter(adapter);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String api_token = prefs.getString("API_TOKEN", null);


        Get ukdate_get = new Get();
        ukdate_get.Get("/uk_list/", api_token, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("TEST", e.getMessage());
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                Call call_link = call;
                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    try {
                        ///Данные об операциях
                        JSONObject dataJsonObj = new JSONObject(responseStr);
                        JSONArray friends = dataJsonObj.getJSONArray("uklist");
                        for (int i = 0; i < friends.length(); i++) {
                            JSONObject dataJsonObj2 = friends.getJSONObject(i);
                            String title = dataJsonObj2.getString("title");
                            URL image = new URL(dataJsonObj2.getString("logo"));
                          ukData.add(new UKData(title,image));
                        }

                    }  catch (JSONException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    Log.d("TEST_SMS", "Ошибка");
                }


            }
        });
        }
}
