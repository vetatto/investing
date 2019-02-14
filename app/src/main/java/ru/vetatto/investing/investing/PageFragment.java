package ru.vetatto.investing.investing;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
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
import android.widget.Switch;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ru.vetatto.investing.investing.GraphicEditActiv.GraphicLegendAdapter;
import ru.vetatto.investing.investing.GraphicEditActiv.GraphicLegendData;
import ru.vetatto.investing.investing.HTTP.Get;
import ru.vetatto.investing.investing.Login.LoginActivity;
import ru.vetatto.investing.investing.PifInfo.PifOperationAdapter;
import ru.vetatto.investing.investing.PifInfo.PifOperationData;
import ru.vetatto.investing.investing.PifList.PifAdapter;
import ru.vetatto.investing.investing.PifList.PifAllListAdater;
import ru.vetatto.investing.investing.PifList.PifAllListData;
import ru.vetatto.investing.investing.PifList.PifData;

public class PageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    int pageNumber;
    int backColor;
    View view,Rootview;
    Context context;
    ArrayList<PifData> phones = new ArrayList();
    ArrayList<PifAllListData> allPif = new ArrayList();
    RecyclerView recyclerView;
    PifAdapter adapter;
    static PifAllListAdater allPifAdapter;
    float sum_money;
    float sum_invest;
    TextView money_sum, procent_dohod;
    TextView plus;
    private static MenuItem menuItem, menuItem2;
    public String api_token;
    LinearLayout portfolio_main;
    ProgressBar load_portfolio;
    NumberFormat f;
    ArrayList<Entry> entries = new ArrayList<Entry>();
    ArrayList<String> labels = new ArrayList<String>();
    List<PointValue> values = new ArrayList<PointValue>();
    List<AxisValue> axisValues = new ArrayList<AxisValue>();
    List<Line> lines = new ArrayList<Line>();
    private LineChartData data_char;
    ArrayList<GraphicLegendData> legendData = new ArrayList();
    GraphicLegendAdapter legendAdapter;
    String legendTitle;
    List<String> hideLegend = new ArrayList<String>();
    List<String> showLegend = new ArrayList<String>();
    String responseStr;
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
           // getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            Rootview = inflater.inflate(R.layout.toolbar, null);
            view = inflater.inflate(R.layout.second_first, null);
            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
            mSwipeRefreshLayout.setOnRefreshListener(this);
            mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
            portfolio_info();
        }
        else if(pageNumber==1){
            Rootview = inflater.inflate(R.layout.toolbar, null);
            view = inflater.inflate(R.layout.second_first, null);
            all_portfolio_billing();
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


////*****************************************************
////Функция отображения состояния счетов по категориям
// В этой функции в актионбаре отображается сумма по всем инструментам и доход по всем инструментам
// Деньги, Пифы
////*****************************************************
    private void all_portfolio_billing(){
        Log.d("JSON_BILLING", "TEST :");
        context=this.getContext();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        api_token = sp.getString("API_TOKEN", " ");
        Get example = new Get();
        example.Get("/get_index/", api_token, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("JSON_BILLING", "ERROR_FALURE:"+e.getMessage());
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    Log.d("JSON_BILLING", responseStr);
                    //Обрабатываем категорию Деньги
                    try {
                        JSONObject dataJsonObj = new JSONObject(responseStr);
                        JSONArray friends = dataJsonObj.getJSONArray("billing");
                        for (int i = 0; i < friends.length(); i++) {
                                JSONObject dataJsonObj2 = friends.getJSONObject(i);
                                String pif = dataJsonObj2.getString("pif");
                                String cash = dataJsonObj2.getString("cash");
                                Log.d("JSON_BILLING","Пифы "+pif);
                                Log.d("JSON_BILLING","Деньги "+cash);
                                //phones.add(new PifData(api_token, " ", Float.valueOf(date_price), Float.valueOf(amount), "33", id, Float.valueOf(sr_price), ukTitle, date, Float.valueOf(procent), 1, nameCat, sum_money));
                            }

                    } catch (JSONException e) {
                        Log.d("JSON_BILLING", e.getMessage());
                    }
                }
                final Activity act = getActivity();
                if (act != null)
                    act.runOnUiThread(new Runnable() {
                        public void run() {

                        }
                    }
            );}
        });

    }

 ///******************************************
///Отображение полного списка портфеля
///**********************************************
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
                    Log.d("JSON_E",responseStr);
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
                                phones.add(new PifData(api_token, title, Float.valueOf(date_price), Float.valueOf(amount), "33", id, Float.valueOf(sr_price),ukTitle, date,Float.valueOf(procent), 1, nameCat, sum_money));
                            }
                        }

                        try {
                            JSONArray pif_archive = dataJsonObj.getJSONArray("pif_archive");
                            Log.d("JSON_E",pif_archive.toString());
                            for (int i = 0; i < pif_archive.length(); i++) {
                                JSONArray pif_archive2 = pif_archive.getJSONArray(i);
                                for (int d = 0; d < pif_archive2.length(); d++) {
                                    JSONObject data_pif_archive2 = pif_archive2.getJSONObject(d);
                                    String title = data_pif_archive2.getString("Title");
                                    String sr_price = data_pif_archive2.getString("sr_price");
                                    String date_price = data_pif_archive2.getString("date_price");
                                    String amount = data_pif_archive2.getString("sum_amount");
                                    String ukTitle = data_pif_archive2.getString("ukTitle");
                                    String date = data_pif_archive2.getString("date");
                                    int id = data_pif_archive2.getInt("id");
                                    String procent = data_pif_archive2.getString("sum_izm");
                                    String nameCat = data_pif_archive2.getString("name_cat");
                                    phones.add(new PifData(api_token, title, Float.valueOf(date_price), Float.valueOf(amount), "33", id, Float.valueOf(sr_price), ukTitle, date, Float.valueOf(procent), 0, "",0));
                                }
                            }
                        }catch (JSONException e){
                            Log.d("JSON_E",e.getMessage());
                        }


                        JSONArray dat =dataJsonObj.getJSONArray("dat");
                        Log.d("TEST_JSON","Массив dat "+dat.length());
                        for (int i = 0; i < dat.length(); i++) {
                            Log.d("TEST_JSON",dat.toString());
                            JSONArray dat2 =dat.getJSONArray(i);
                            Log.d("TEST_JSON","Массив dat2 "+dat2.length());
                            for (int d = 0; d < dat2.length(); d++) {
                                JSONObject dataJsonObj3 = dat2.getJSONObject(d);
                                Log.d("TEST_JSON", dat2.toString());
                                String date2 = dataJsonObj3.getString("date");
                                String pay2 = dataJsonObj3.getString("pif");
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
                                Date dates = sdf.parse(date2);
                                entries.add(new Entry(dates.getTime(), Float.valueOf(pay2)));
                                values.add(new PointValue(dates.getTime(), Float.valueOf(pay2)));
                                AxisValue axisValue = new AxisValue(dates.getTime());
                                axisValue.setLabel(date2);
                                axisValues.add(axisValue);
                                Log.d("TEST", date2);
                          /*  values.add(new PointValue(1, 4));
                            values.add(new PointValue(2, 3));
                            values.add(new PointValue(3, 4));*/

                                labels.add(date2);
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
                        } catch (ParseException e) {
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


                            LineChartView chart = (LineChartView) act.findViewById(R.id.chart_line2);

                            //In most cased you can call data model methods in builder-pattern-like manner.
                            Line line = new Line(values).setColor(Color.BLUE).setCubic(true);
                            List<Line> lines = new ArrayList<Line>();
                            line.setHasPoints(false);
                            line.setStrokeWidth(1);
                            lines.add(line);
                            LineChartData data = new LineChartData();
                            data.setLines(lines);

                            Axis axisX = new Axis(axisValues);
                            Axis axisY = new Axis().setHasLines(true);
                            //axisX.setName("Axis X");
                            //axisY.setName("Axis Y");
                            //axisX.setMaxLabelChars(4);
                            axisX.setHasLines(true);
                            axisX.setMaxLabelChars(10);
                            //data.setAxisYLeft(axisY);
                           // data.setAxisXBottom(axisX);
                            chart.setContainerScrollEnabled(true, ContainerScrollType.VERTICAL);
                            data.setBaseValue(Float.NEGATIVE_INFINITY);
                            chart.setLineChartData(data);
                            chart.setViewportCalculationEnabled(false);



                           TextView money_sums =act.findViewById(R.id.textView31);
                            TextView procent =act.findViewById(R.id.textView38);
                            final CollapsingToolbarLayout mCollapsingToolbar = (CollapsingToolbarLayout) act.findViewById(R.id.collapsing_toolbar);

                            money_sums.setText(f.format( Math.round(sum_money*100.00)/100.00)+" \u20BD");
                            AppBarLayout appBarLayout = (AppBarLayout) act.findViewById(R.id.appbar);
                            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                                boolean isShow = true;
                                int scrollRange = -1;

                                @Override
                                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                                    if (scrollRange == -1) {
                                        scrollRange = appBarLayout.getTotalScrollRange();
                                    }
                                    if (scrollRange + verticalOffset == 0) {

                                        ///Анимация исчезновения данных заезжающих на toolbar
                                        TextView mvr = act.findViewById(R.id.textView40);
                                        mvr.animate()
                                                .setDuration(500)
                                                .alpha(0)
                                                .setListener(new AnimatorListenerAdapter() {
                                                    @Override
                                                    public void onAnimationEnd(Animator animation) {

                                                    }
                                                });
                                        TextView mvr2 = act.findViewById(R.id.textView39);
                                        mvr2.animate()
                                                .setDuration(500)
                                                .alpha(0)
                                                .setListener(new AnimatorListenerAdapter() {
                                                    @Override
                                                    public void onAnimationEnd(Animator animation) {

                                                    }
                                                });
                                        mCollapsingToolbar.setTitle(f.format( Math.round(sum_money*100.00)/100.00)+" \u20BD");
                                        /////////
                                        isShow = true;
                                    } else if(isShow) {
                                        mCollapsingToolbar.setTitle(" ");
                                        //carefull there should a space between double quote otherwise it wont work
                                        TextView mvr = act.findViewById(R.id.textView40);
                                        mvr.animate()
                                                .setDuration(500)
                                                .alpha(1)
                                                .setListener(new AnimatorListenerAdapter() {
                                                    @Override
                                                    public void onAnimationEnd(Animator animation) {

                                                    }
                                                });
                                        TextView mvr2 = act.findViewById(R.id.textView39);
                                        mvr2.animate()
                                                .setDuration(500)
                                                .alpha(1)
                                                .setListener(new AnimatorListenerAdapter() {
                                                    @Override
                                                    public void onAnimationEnd(Animator animation) {

                                                    }
                                                });
                                        isShow = false;
                                    }
                                }
                            });
                            if(sum_money-sum_invest<0) {

                                procent.setText(""+f.format( Math.round((sum_money - sum_invest)*100.00)/100.00)+" \u20BD ("+f.format(Math.round(((sum_money - sum_invest) / sum_invest * 100)*100.00)/100.00)+"%)");
                                //procent.setTextColor(Color.parseColor("#B71C1C"));
                            }
                            else if(sum_money-sum_invest>0){
                                procent.setText("+"+f.format( Math.round((sum_money - sum_invest)*100.00)/100.00)+" \u20BD ("+f.format(Math.round(((sum_money - sum_invest) / sum_invest * 100)*100.00)/100.00)+"%)");
                                //procent.setTextColor(Color.parseColor("#00C853"));
                            }
                            else{
                                procent.setText(f.format( Math.round((sum_money - sum_invest)*100.00)/100.00)+" \u20BD ("+f.format(Math.round(((sum_money - sum_invest) / sum_invest * 100)*100.00)/100.00)+"%)");
                            }
                            show();
                            adapter.notifyDataSetChanged();

                        }
                    });
            }
        });
    }
///*****************************************


    ///******************************************
    ///Отображение всех пифов в системе
    ///********************************************
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
///********************************************************

    private void hide() {
        portfolio_main.setVisibility(View.INVISIBLE);
        load_portfolio.setVisibility(View.VISIBLE);
    }
    private void show() {
        portfolio_main.setVisibility(View.VISIBLE);
        load_portfolio.setVisibility(View.INVISIBLE);
    }
}