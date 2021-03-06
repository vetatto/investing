package ru.vetatto.investing.investing.SMS;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ru.vetatto.investing.investing.HTTP.Get;

import ru.vetatto.investing.investing.R;

public class SMSRead extends Activity {
    ArrayList<SMSData> phones = new ArrayList();
    RecyclerView recyclerView;
    SMSAdapter adapter;
   ArrayList ukFromSMS = new ArrayList();
    Map<String, List<String>> operation_get_arrya = new HashMap<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_read);
        recyclerView = findViewById(R.id.sms_recycles);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new SMSAdapter(this, phones);
        recyclerView.setAdapter(adapter);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String api_token = prefs.getString("API_TOKEN", null);
        ukFromSMS.add("Arsagera");
        ukFromSMS.add("SberbankAM");
        ukFromSMS.add("Sberbank AM");
        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            Uri uriSMSURI = Uri.parse("content://sms/inbox");
            final Cursor cur = getContentResolver().query(uriSMSURI, null, null, null, null);
            final String[] sms = {""};
            Get example = new Get();
            example.Get("/get_operations/", api_token, new Callback() {
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
                            JSONArray friends = dataJsonObj.getJSONArray("operations");
                            for (int i = 0; i < friends.length(); i++) {
                               JSONObject dataJsonObj2 = friends.getJSONObject(i);
                                    String date = dataJsonObj2.getString("date");
                                    String amount = dataJsonObj2.getString("amount");
                                    String price = dataJsonObj2.getString("price");
                                    String id = dataJsonObj2.getString("id");
                                    String id_instrument = dataJsonObj2.getString("id_instrument");
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
                                    Date dates = sdf.parse(date);
                                    operation_get_arrya.put(id,Arrays.asList(date,amount,price,id_instrument));
                                }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        Log.d("TEST_SMS", "Ошибка");
                    }

                    while (cur.moveToNext()) {
                        for (int i = 0; i < ukFromSMS.size(); i++) {
                            if (ukFromSMS.get(i).toString().equals(cur.getString(2))) {
                                ///Парсим SMS
                                if (cur.getString(2).equals("Arsagera")) {
                                    Pattern pattern = Pattern.compile("Vydany pai fonda\\s([A-z]*-[A-z]*)\\sv\\skolichestve\\s([0-9]+[\\.]+[0-9]*)\\W\\sraschetnaya\\sstoimost\\spaya\\s([0-9]+[\\.]+[0-9]*)");
                                    Matcher matcher = pattern.matcher(cur.getString(cur.getColumnIndexOrThrow("body")));
                                    int count_matcher=0;
                                        while (matcher.find()) {
                                            count_matcher++;
                                            boolean operation_searched = false;
                                            for (List<String> value : operation_get_arrya.values()) {
                                                if (value.get(3).equals("123")) {
                                                    if (value.get(1).equals(matcher.group(2).toString()) & value.get(2).equals(matcher.group(3).toString())) {
                                                        phones.add(new SMSData("УК Арсагера", cur.getString(cur.getColumnIndexOrThrow("body")), "Обработано. Покупка " + matcher.group(2).toString() + " паев"));
                                                        operation_searched = true;
                                                        break;
                                                    }
                                                }
                                            }
                                            if (!operation_searched) {
                                                phones.add(new SMSData("УК Арсагера", cur.getString(cur.getColumnIndexOrThrow("body")), "Операция не найдена"));
                                            }
                                        }
                             if(count_matcher==0) {
                                 phones.add(new SMSData("УК Арсагера", cur.getString(cur.getColumnIndexOrThrow("body")), "Формат не распознан"));
                             }
                                    }
                                if (cur.getString(2).equals("SberbankAM") || cur.getString(2).equals("Sberbank AM")) {
                                    int count_matcher=0;
                                    Pattern pattern = Pattern.compile("в фонде\\s([А-я]*[\\s]{0,1}[А-я]*)\\sсовершена\\sоперация\\sпокупки\\s([0-9]+[\\.]+[0-9]*)\\sпаев\\sна\\sсумму\\s([0-9\\s]*[\\.]+[0-9]{2})");
                                    Matcher matcher = pattern.matcher(cur.getString(cur.getColumnIndexOrThrow("body")));
                                        while (matcher.find()) {
                                            count_matcher++;
                                            boolean operation_searched = false;
                                            for (List<String> value : operation_get_arrya.values()) {
                                                if (value.get(3).equals("1029")) {
                                                    Date date = new Date(cur.getLong(cur.getColumnIndexOrThrow("date")));
                                                    SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd");
                                                    String java_date = jdf.format(date);
                                                    if (value.get(0).equals(java_date) & matcher.group(2).toString().equals(value.get(1))) {
                                                        phones.add(new SMSData("УК Сбербанк АМ", cur.getString(cur.getColumnIndexOrThrow("body")), "Обработано. Покупка " + matcher.group(2).toString() + " паев"));
                                                        operation_searched = true;
                                                        break;
                                                    }
                                                }
                                            }
                                            if(!operation_searched){
                                                phones.add(new SMSData("УК Сбербанк АМ", cur.getString(cur.getColumnIndexOrThrow("body")), "Операцию необходимо добавить"));
                                            }
                                        }
                                    if(count_matcher<1) {
                                        phones.add(new SMSData("УК Сбербанк АМ", cur.getString(cur.getColumnIndexOrThrow("body")), "Формат не распознан"));
                                    }

                                    }
                                }
                            }
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }

            });


        }
      else {
            Log.d("SMS_TEST","Запрашиваем разрешение");
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_SMS},
                    1);
        }

    }
}