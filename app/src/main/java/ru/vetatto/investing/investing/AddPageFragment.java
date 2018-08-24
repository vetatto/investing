package ru.vetatto.investing.investing;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AddPageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    int pageNumber;
    int backColor;
    View view;
    Context context;
    ArrayList<PifData> phones = new ArrayList();
    ArrayList<PortfoliListData> PortfolioArray = new ArrayList();
    RecyclerView recyclerView;
    PifAdapter adapter;
    PortfolioListAdapter PortfolioListAdapter;
    float sum_money;
    float sum_invest;
    TextView money_sum;
    TextView plus;
    public String api_token;
    NumberFormat f;
    static AddPageFragment newInstance(int page) {
        AddPageFragment pageFragment = new AddPageFragment();
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

        f=NumberFormat.getInstance();
        if(pageNumber==0){
            view = inflater.inflate(R.layout.add_pif, null);
            //mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
           // mSwipeRefreshLayout.setOnRefreshListener(this);


           // mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                  //  android.R.color.holo_green_light,
                  //  android.R.color.holo_orange_light,
                  //  android.R.color.holo_red_light);
          //  portfolio_info();
        }
        else if(pageNumber==1){
            view = inflater.inflate(R.layout.list_portfolio, null);
            listPortfolio();
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


///Отображение полного списка портфеля
    private void portfolio_info(){
        context=this.getContext();
        recyclerView = view.findViewById(R.id.RV1);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        sum_invest=0;
        sum_money=0;
        TextView text = view.findViewById(R.id.textView3);
        //text.setText("Авторизация пройдена");
        Get example = new Get();
        String response = null;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        api_token = sp.getString("API_TOKEN", " ");
        phones.clear();
        adapter = new PifAdapter(context, phones);
        recyclerView.setAdapter(adapter);
        money_sum = view.findViewById(R.id.sum_money);
        plus = view.findViewById(R.id.plus);
        example.Get("/get_portfolio_instrument", api_token, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TEST",e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                   // Log.d("TEST",responseStr);
                    //Log.d("TEST","Сохраняем токены доступа");
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
                               // Log.d("TEST", "phone: " + title);
                                sum_money=sum_money+Float.valueOf(date_price)*Float.valueOf(amount);
                                sum_invest=sum_invest+Float.valueOf(sr_price)*Float.valueOf(amount);
                                phones.add(new PifData(api_token, title, Float.valueOf(date_price), Float.valueOf(amount), "33", id, Float.valueOf(sr_price),ukTitle, date,Float.valueOf(procent), 1, nameCat ));
                                // устанавливаем для списка адапте
                                //adapter.notifyDataSetChanged();
                            }
                        }
                        JSONArray currency = dataJsonObj.getJSONArray("currency");
                        for (int i = 0; i < currency.length(); i++) {
                            JSONArray currency2 = currency.getJSONArray(i);
                            for (int d = 0; d < currency2.length(); d++) {
                                JSONObject dataJsonObj_currency = currency2.getJSONObject(d);
                                String title = dataJsonObj_currency.getString("name");
                                String date = dataJsonObj_currency.getString("date");
                                String date_price = dataJsonObj_currency.getString("price");
                                String amount = dataJsonObj_currency.getString("amount");
                                int id = dataJsonObj_currency.getInt("id");
                                //Log.d("TEST", "phone: " + title);
                                sum_money=sum_money+Float.valueOf(date_price)*Float.valueOf(amount);

                                ////Рассчитать среднюю стоимость денежных вложений
                                sum_invest=sum_invest+Float.valueOf(date_price)*Float.valueOf(amount);
                                phones.add(new PifData(api_token, title, Float.valueOf(date_price), Float.valueOf(amount), "33", id, 0,title, date, 0,2, ""));
                                // устанавливаем для списка адапте
                                //adapter.notifyDataSetChanged();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //Log.d("TEST",e.toString());
                    }
                } else {
                    String responseStr = response.body().string();
                    // Log.d("TEST",responseStr);
                   // Log.d("TEST","!"+response.message().toString());
                    String resp = response.message().toString();
                    //Log.d("TEST",resp);
                    if(resp.equals("Unauthorized")) {
                       // Log.d("TEST",response.message().toString()+ "нет авторизации");
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("API_TOKEN", null);
                        editor.commit();
                        FragmentTransaction tran = getFragmentManager().beginTransaction();
                        tran.replace(R.id.container, new FirstFragment()).commit();
                    }
                }
                final Activity act = getActivity(); //only neccessary if you use fragments
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
                            adapter.notifyDataSetChanged();

                        }
                    });
            }
        });

    }


    ////СПИСОК ПОРТФЕЛЕЙ
    private void listPortfolio() {
        context = this.getContext();
        recyclerView = view.findViewById(R.id.RV1);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        TextView text = view.findViewById(R.id.textView3);
        Get example = new Get();
        String response = null;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        api_token = sp.getString("API_TOKEN", " ");
        Log.d("TEST", api_token);

        PortfolioListAdapter = new PortfolioListAdapter(context, PortfolioArray);
        recyclerView.setAdapter(PortfolioListAdapter);
        money_sum = view.findViewById(R.id.sum_money);
        plus = view.findViewById(R.id.plus);
        example.Get("/get_portfolio", api_token, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TEST", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    Log.d("TEST", responseStr);
                    //Log.d("TEST","Сохраняем токены доступа");
                    try {
                        JSONObject dataJsonObj = new JSONObject(responseStr);

                        JSONArray friends = dataJsonObj.getJSONArray("data");
                        for (int i = 0; i < friends.length(); i++) {
                            JSONArray friends2 = friends.getJSONArray(i);
                            for (int d = 0; d < friends2.length(); d++) {
                                JSONObject dataJsonObj2 = friends2.getJSONObject(d);
                                String title = dataJsonObj2.getString("name");
                                String goal_name = dataJsonObj2.getString("goal_name");
                                float goal_amount = Float.valueOf(dataJsonObj2.getString("goal_amount"));
                                int id = Integer.valueOf(dataJsonObj2.getString("id"));
                                Log.d("TEST", "phone: " + title);
                               // sum_money = sum_money + Float.valueOf(date_price) * Float.valueOf(amount);
                               // sum_invest = sum_invest + Float.valueOf(sr_price) * Float.valueOf(amount);
                                PortfolioArray.add(new PortfoliListData(api_token, id, title, goal_name, goal_amount));                           // устанавливаем для списка адапте
                                //adapter.notifyDataSetChanged();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("TEST", e.toString());
                    }
                } else {
                    String responseStr = response.body().string();
                    // Log.d("TEST",responseStr);
                    Log.d("TEST", "!" + response.message().toString());
                    String resp = response.message().toString();
                    Log.d("TEST", resp);
                    if (resp.equals("Unauthorized")) {
                        Log.d("TEST", response.message().toString() + "нет авторизации");
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("API_TOKEN", null);
                        editor.commit();
                        FragmentTransaction tran = getFragmentManager().beginTransaction();
                        tran.replace(R.id.container, new FirstFragment()).commit();
                    }
                }
                final Activity act = getActivity(); //only neccessary if you use fragments
                if (act != null)
                    act.runOnUiThread(new Runnable() {
                        public void run() {

                            money_sum.setText(String.format("%.2f", sum_money) + " \u20BD");
                            if (sum_money - sum_invest < 0) {
                                plus.setText(String.format("%.2f", sum_money - sum_invest) + " \u20BD");
                            } else if (sum_money - sum_invest > 0) {
                                plus.setText("+" + String.format("%.2f", sum_money - sum_invest) + " \u20BD");
                            } else {
                                plus.setText(String.format("%.2f", sum_money - sum_invest) + " \u20BD");
                            }
                            PortfolioListAdapter.notifyDataSetChanged();
                        }
                    });
            }
        });

    }


}