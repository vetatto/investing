package ru.vetatto.investing.investing.IndexViewPager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ru.vetatto.investing.investing.Billing.BillingAdapter;
import ru.vetatto.investing.investing.Billing.BillingData;
import ru.vetatto.investing.investing.GraphicEditActiv.GraphicLegendAdapter;
import ru.vetatto.investing.investing.GraphicEditActiv.GraphicLegendData;
import ru.vetatto.investing.investing.HTTP.Get;
import ru.vetatto.investing.investing.Login.LoginActivity;
import ru.vetatto.investing.investing.MainFragment;
import ru.vetatto.investing.investing.PifList.PifAdapter;
import ru.vetatto.investing.investing.PifList.PifAllListAdater;
import ru.vetatto.investing.investing.PifList.PifAllListData;
import ru.vetatto.investing.investing.PifList.PifData;
import ru.vetatto.investing.investing.R;

public class BillingFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    private SwipeRefreshLayout mSwipeRefreshLayout,mSwipeRefreshLayout_pif;
    int pageNumber;
    int backColor;
    View view,view2,Rootview;
    Context context;
    ArrayList<BillingData> billing = new ArrayList();
    BillingAdapter billingAdapteradapter;
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
    LinearLayout portfolio_main, billing_main;
    ProgressBar load_portfolio, billingLoad;
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
    Float all_sum,pif_sum;


   public static BillingFragment newInstance(int page) {
        BillingFragment pageFragment = new BillingFragment();
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
            Rootview = inflater.inflate(R.layout.toolbar, null);
            view = inflater.inflate(R.layout.billing_fragment, null);
            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_billing);
            mSwipeRefreshLayout.setOnRefreshListener(this);
            mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
            all_portfolio_billing();

       return view;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                all_portfolio_billing();
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
        context=this.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.RVBilling);
        billing.clear();
        billingAdapteradapter = new BillingAdapter(context, billing);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        billing_main = view.findViewById(R.id.billing_main);
        billingLoad = view.findViewById(R.id.loadBilling);
        billingLoad.setVisibility(View.GONE);
        billing_main.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(billingAdapteradapter);
        hide_billing();
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
                                JSONArray pifArray = new JSONArray(pif);
                            for (int b = 0; b < pifArray.length(); b++) {
                                JSONObject pifJsonObj2 = pifArray.getJSONObject(b);
                                String pif_amount = pifJsonObj2.getString("amount");
                                String pif_income = pifJsonObj2.getString("doxod");
                                billing.add(new BillingData(api_token,"Пифы",pif_amount,pif_income,"0"));
                                all_sum=Float.valueOf(pif_amount);
                                pif_sum=Float.valueOf(pif_amount);
                            }
                            JSONArray cashArray = new JSONArray(cash);
                            for (int b = 0; b < cashArray.length(); b++) {
                                JSONObject cashJsonObj2 = cashArray.getJSONObject(b);
                                String cash_amount = cashJsonObj2.getString("amount");
                                billing.add(new BillingData(api_token,"Деньги",cash_amount,null,"0"));
                                all_sum=all_sum+Float.valueOf(cash_amount);
                            }
                        }

                    } catch (JSONException e) {
                        Log.d("JSON_BILLING", e.getMessage());
                    }
                }
                final Activity act = getActivity();
                if (act != null)
                    act.runOnUiThread(new Runnable() {
                        public void run() {
                            TextView money_sums =act.findViewById(R.id.textView31);
                            money_sums.setText(f.format( Math.round(all_sum*100.00)/100.00)+" \u20BD");
                            billingAdapteradapter.notifyDataSetChanged();
                            show_billing();

                        }
                    }
            );}
        });

    }

    private void hide_billing() {
        billing_main.setVisibility(View.INVISIBLE);
        billingLoad.setVisibility(View.VISIBLE);
    }
    private void show_billing() {
        billing_main.setVisibility(View.VISIBLE);
        billingLoad.setVisibility(View.INVISIBLE);
    }
}