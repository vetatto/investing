package ru.vetatto.investing.investing;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    int pageNumber;
    int backColor;
    View view;
    Context context;
    ArrayList<PifData> phones = new ArrayList();
    ArrayList<PifAllListData> allPif = new ArrayList();
    RecyclerView recyclerView;
    PifAdapter adapter;
    static PifAllListAdater allPifAdapter;
    float sum_money;
    float sum_invest;
    TextView money_sum;
    TextView plus;
    private static MenuItem menuItem, menuItem2;
    public String api_token;
    LinearLayout portfolio_main;
    ProgressBar load_portfolio;
    NumberFormat f;

    static PageFragment newInstance(int page) {
        PageFragment pageFragment = new PageFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        f=NumberFormat.getInstance();

        if(pageNumber==0){

            view = inflater.inflate(R.layout.second_first, null);
            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
            mSwipeRefreshLayout.setOnRefreshListener(this);
            mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);

            portfolio_info();
           // ((MainActivity) getActivity()).showFloatingActionButton();
        }
        else if(pageNumber==1){
            view = inflater.inflate(R.layout.allpiffragment, null);

                get_all_pif_list("");
        }
        return view;
    }

    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Отменяем анимацию обновления
                mSwipeRefreshLayout.setRefreshing(false);
                portfolio_info();
            }
        }, 4000);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menuItem = menu.findItem(R.id.menu_filter_pif);
        menuItem2 = menu.findItem(R.id.menu_filter_pif_del);
        if(pageNumber==0) {
            menuItem.setVisible(false);
            menuItem2.setVisible(false);
        }
        if(pageNumber==1){
            if(allPifAdapter.infilter()) {
                menuItem.setVisible(false);
                menuItem2.setVisible(true);
            }
            else{
                menuItem.setVisible(true);
                menuItem2.setVisible(false);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_filter_pif) {
            Intent intent = new Intent(context,filterpif.class);
            super.startActivityForResult(intent,1);
        }
        if (id == R.id.menu_filter_pif_del) {
            if(pageNumber==0) {
                menuItem.setVisible(false);
                menuItem2.setVisible(false);
            }
            if(pageNumber==1){
                allPifAdapter.filter_del();
                allPifAdapter.notifyDataSetChanged();
                if(allPifAdapter.infilter()==true) {
                    menuItem.setVisible(false);
                    menuItem2.setVisible(true);
                }
                else{
                    menuItem.setVisible(true);
                    menuItem2.setVisible(false);
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }


    public static void OnResult(int requestCode, int resultCode, Intent data) {
        Log.d("TEST", data.getStringExtra("TEST"));
        PifAllListAdater.filter("");
        allPifAdapter.notifyDataSetChanged();
        if(allPifAdapter.infilter()==true) {
            menuItem.setVisible(false);
            menuItem2.setVisible(true);
        }
        else{
            menuItem.setVisible(true);
            menuItem2.setVisible(false);
        }
    }

    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menuItem = menu.findItem(R.id.menu_filter_pif);
        MenuItem menuItem2 = menu.findItem(R.id.menu_filter_pif_del);
        if(pageNumber==0) {
            menuItem.setVisible(false);
            menuItem2.setVisible(false);
        }
        if(pageNumber==1) {
            if (allPifAdapter.infilter()) {
                menuItem.setVisible(false);
                menuItem2.setVisible(true);
            } else {
                menuItem.setVisible(true);
                menuItem2.setVisible(false);
            }
        }
    }
   // }

///Отображение полного списка портфеля
    private void portfolio_info(){
        context=this.getContext();
        recyclerView = view.findViewById(R.id.RV1);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        portfolio_main = view.findViewById(R.id.portfolio_main);
        load_portfolio = view.findViewById(R.id.load_portfolio);
        load_portfolio.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(mLayoutManager);
        sum_invest=0;
        sum_money=0;
        Get example = new Get();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        api_token = sp.getString("API_TOKEN", " ");
        phones.clear();
        adapter = new PifAdapter(context, phones);
        recyclerView.setAdapter(adapter);
        money_sum = view.findViewById(R.id.sum_money);
        plus = view.findViewById(R.id.plus);
        hide();
        example.Get("/get_portfolio_instrument", api_token, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                final Activity act = getActivity();
                if (act != null)
                    act.runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog.Builder builder;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                builder = new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog_Alert);
                            } else {
                                builder = new AlertDialog.Builder(context);
                            }
                            builder.setTitle("Сервер не отвечает")
                                    .setMessage("Нам не удалось установить связь с сервером. Проверьте подключение к интернету или попробуйте позже!")
                                    .setPositiveButton("Повторить", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            portfolio_info();
                                        }
                                    })
                                    .setNegativeButton("Выход", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            getActivity().finish();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    try {
                        JSONObject dataJsonObj = new JSONObject(responseStr);
                        JSONArray friends = dataJsonObj.getJSONArray("pif");
                        for (int i = 0; i < friends.length(); i++) {
                            JSONArray friends2 = friends.getJSONArray(i);
                            for (int d = 0; d < friends2.length(); d++) {
                                JSONObject dataJsonObj2 = friends2.getJSONObject(d);
                                String title = dataJsonObj2.getString("Title");
                                String sr_price = dataJsonObj2.getString("sr_price");
                                String date_price = dataJsonObj2.getString("date_price");
                                String amount = dataJsonObj2.getString("sum_amount");
                                String ukTitle = dataJsonObj2.getString("ukTitle");
                                String date = dataJsonObj2.getString("date");
                                int id =dataJsonObj2.getInt("id");
                                String procent = dataJsonObj2.getString("sum_izm");
                                String nameCat = dataJsonObj2.getString("name_cat");
                                sum_money=sum_money+Float.valueOf(date_price)*Float.valueOf(amount);
                                sum_invest=sum_invest+Float.valueOf(sr_price)*Float.valueOf(amount);
                                phones.add(new PifData(api_token, title, Float.valueOf(date_price), Float.valueOf(amount), "33", id, Float.valueOf(sr_price),ukTitle, date,Float.valueOf(procent), 1, nameCat));
                            }
                        }
                       /* JSONArray currency = dataJsonObj.getJSONArray("currency");
                        for (int i = 0; i < currency.length(); i++) {
                            JSONArray currency2 = currency.getJSONArray(i);
                            for (int d = 0; d < currency2.length(); d++) {
                                JSONObject dataJsonObj_currency = currency2.getJSONObject(d);
                                String title = dataJsonObj_currency.getString("name");
                                String date = dataJsonObj_currency.getString("date");
                                String date_price = dataJsonObj_currency.getString("price");
                                String amount = dataJsonObj_currency.getString("amount");
                                int id = dataJsonObj_currency.getInt("id");
                                sum_money=sum_money+Float.valueOf(date_price)*Float.valueOf(amount);
                                ////Рассчитать среднюю стоимость денежных вложений
                                sum_invest=sum_invest+Float.valueOf(date_price)*Float.valueOf(amount);
                                phones.add(new PifData(api_token, title, Float.valueOf(date_price), Float.valueOf(amount), "33", id, 0,title, date, 0,2, ""));
                            }
                        }*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    String resp = response.message().toString();
                    if(resp.equals("Unauthorized")) {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("API_TOKEN", null);
                        editor.commit();
                        Intent intent = new Intent(context,LoginActivity.class);
                        context.startActivity(intent);
                    }
                }
                final Activity act = getActivity();
                if (act != null)
                    act.runOnUiThread(new Runnable() {
                        public void run() {
                            money_sum.setText(f.format(sum_money)+" \u20BD");
                            if(sum_money-sum_invest<0) {
                                plus.setText(f.format(sum_money - sum_invest)+" \u20BD");
                            }
                            else if(sum_money-sum_invest>0){
                                plus.setText("+"+f.format(sum_money - sum_invest)+" \u20BD");
                            }
                            else{
                                plus.setText(f.format(sum_money - sum_invest)+" \u20BD");
                            }
                            show();
                            adapter.notifyDataSetChanged();

                        }
                    });
            }
        });
    }

    private  void get_all_pif_list(String sort) {
        Log.d("TEST","ОТображаем весь список");
        context=this.getContext();
        recyclerView = view.findViewById(R.id.RV1);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        sum_invest=0;
        sum_money=0;
        TextView text = view.findViewById(R.id.textView3);
        Get example = new Get();
        String response = null;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        api_token = sp.getString("API_TOKEN", " ");
        allPif.clear();
        allPifAdapter = new PifAllListAdater(context, allPif);
        recyclerView.setAdapter(allPifAdapter);
        money_sum = view.findViewById(R.id.sum_money);
        plus = view.findViewById(R.id.plus);
        example.Get("/get_all_pif_list"+sort, api_token, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                final Activity act = getActivity();
                if (act != null)
                    act.runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog.Builder builder;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                builder = new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog_Alert);
                            } else {
                                builder = new AlertDialog.Builder(context);
                            }
                            builder.setTitle("Сервер не отвечает")
                                    .setMessage("Нам не удалось установить связь с сервером. Проверьте подключение к интернету или попробуйте позже!")
                                    .setPositiveButton("Повторить", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                           portfolio_info();
                                        }
                                    })
                                    .setNegativeButton("Выход", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                           getActivity().finish();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    try {
                        Log.d("TEST",responseStr);
                        JSONObject dataJsonObj = new JSONObject(responseStr);
                        JSONArray friends = dataJsonObj.getJSONArray("pif");
                        for (int i = 0; i < friends.length(); i++) {
                                JSONObject dataJsonObj2 = friends.getJSONObject(i);
                                String title = dataJsonObj2.getString("pifTitle");
                                String sr_price = dataJsonObj2.getString("end_pay");
                                String change_y = dataJsonObj2.getString("change_y");
                                String ukTitle = dataJsonObj2.getString("ukTitle");
                                String pif_cat = dataJsonObj2.getString("pif_cat");
                                int id =dataJsonObj2.getInt("pif_id");
                                allPif.add(new PifAllListData(api_token, title, Float.valueOf(sr_price),  change_y, "33", id, 0,ukTitle, "2018-01-12",0, 1, pif_cat));
                            }
                        }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    String resp = response.message().toString();
                    if(resp.equals("Unauthorized")) {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("API_TOKEN", null);
                        editor.commit();
                        Intent intent = new Intent(context,LoginActivity.class);
                        context.startActivity(intent);
                    }
                }
                final Activity act = getActivity();
                if (act != null)
                    act.runOnUiThread(new Runnable() {
                        public void run() {
                            allPifAdapter.notifyDataSetChanged();

                        }
                    });
            }
        });

    }
    private void hide() {
        portfolio_main.setVisibility(View.INVISIBLE);
        load_portfolio.setVisibility(View.VISIBLE);
    }
    private void show() {
        portfolio_main.setVisibility(View.VISIBLE);
        load_portfolio.setVisibility(View.INVISIBLE);
    }
}