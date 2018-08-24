package ru.vetatto.investing.investing;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class protfolioPifInfo extends AppCompatActivity {
    List<Entry> entries;
    ArrayList<PieEntry> entriesPie = new ArrayList<PieEntry>();
    ArrayList<String> labels;
    String PifTitle;
    private PieChart mChart;
    float pif_price, change_m, change_3m, change_year;
    ProgressDialog dialog;
    Get example;
    Call call_link;
    ConstraintLayout pifinfo_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protfolio_pif_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pifinfo_view = (ConstraintLayout) findViewById(R.id.pif_info_view);
        pifinfo_view.setVisibility(View.INVISIBLE);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Загружаем данные...");
        dialog.setCancelable(false);
        dialog.setIndeterminate(true);
        dialog.show();
        String api_token = getIntent().getStringExtra("token");
        int id = getIntent().getIntExtra("idPif", 0);
        PifTitle = getIntent().getStringExtra("name");
        //TextView infoTitle = (TextView) findViewById(R.id.textView22);
        //infoTitle.setText("Информация "+PifTitle);
        toolbar.setTitle(PifTitle);
        example = new Get();//Делаем запрос к серверу
       //Log.d("TEST", "/get_portfolio_instrument/"+id);
        example.Get("/get_portfolio_instrument/"+id, api_token, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("TEST",e.getMessage());
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                call_link=call;
                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    Log.d("TEST",responseStr);
                    entries = new ArrayList<Entry>();
                    labels = new ArrayList<String>();
                    try {

                        ///Данные графика
                        JSONObject dataJsonObj = new JSONObject(responseStr);
                        JSONArray friends = dataJsonObj.getJSONArray("data");
                        for (int i = 0; i < friends.length(); i++) {
                                JSONObject dataJsonObj2 = friends.getJSONObject(i);
                                String date = dataJsonObj2.getString("date");
                                String pay = dataJsonObj2.getString("pay");
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
                                Date dates = sdf.parse(date);
                                entries.add(new Entry(dates.getTime(), Float.valueOf(pay)));
                                labels.add(date);
                            }
                        //// ДАнные пифа
                        JSONObject pifinfo = dataJsonObj.getJSONObject("operation");
                       // for (int i = 0; i < pifinfo.length(); i++) {
                          //  JSONArray pifinfo2 = pifinfo.getJSONArray(i);
                            pif_price = Float.valueOf(pifinfo.getString("pif_price"));


                           try {
                                   change_m = Float.valueOf(pifinfo.getString("change_m"));
                                   change_3m = Float.valueOf(pifinfo.getString("change_3m"));
                                   change_year = Float.valueOf(pifinfo.getString("change_year"));
                           }
                           //Отлавливаем сообщение об ошибке
                           catch(NumberFormatException e) {
                               Log.d("TEST",e.getMessage());
                        }
                            float pif_amount =  Float.valueOf(pifinfo.getString("amount"));
                             float end_price =  Float.valueOf(pifinfo.getString("end_price"));
                           /* for (int d = 0; d < pifinfo2.length(); d++) {
                                JSONObject dataJsonObj3 = pifinfo2.getJSONObject(d);
                                float pif_price = Float.valueOf(dataJsonObj3.getString("pif_price"));
                                float pif_amount =  Float.valueOf(dataJsonObj3.getString("amount"));*/
                                entriesPie.add(new PieEntry(pif_price*pif_amount, "Инвестиции")); //revenue1
                                if((end_price*pif_amount-pif_price*pif_amount)>0){
                                    entriesPie.add(new PieEntry((end_price*pif_amount-pif_price*pif_amount), "Доход")); //revenue1
                                }
                                Log.d("TEST",String.valueOf(pifinfo));
                           // }


                } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("TEST",e.toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            LineChart lineChart = (LineChart) findViewById(R.id.chart);
                            lineChart.setDrawGridBackground(false);
                            lineChart.setBackgroundColor(Color.WHITE);
                            YAxis left = lineChart.getAxisLeft();
                            left.setDrawLabels(true); // no axis labels
                            //left.setDrawAxisLine(false); // no axis line
                            left.setDrawGridLines(false); // no grid lines
                            //left.setDrawZeroLine(true); // draw a zero line
                            lineChart.getAxisRight().setEnabled(false); // no right axis
                            XAxis xAxis = lineChart.getXAxis();
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                            xAxis.setTextSize(10f);
                            xAxis.setTextColor(Color.BLACK);
                            //xAxis.setDrawAxisLine(false);
                            xAxis.setDrawLabels(true);
                            xAxis.setDrawGridLines(false);
                            //xAxis.setCenterAxisLabels(true);
                            //xAxis.setGranularity(1f); // one hour
                            xAxis.setValueFormatter(new IAxisValueFormatter() {
                                private SimpleDateFormat mFormat = new SimpleDateFormat("dd.M.yyyy");
                                @Override
                                public String getFormattedValue(float value, AxisBase axis) {
                                    return mFormat.format(value);
                                }
                            });
                                LineDataSet dataset = new LineDataSet(entries, PifTitle);
                                dataset.setColors(ColorTemplate.getHoloBlue());
                                dataset.setAxisDependency(YAxis.AxisDependency.LEFT);
                                dataset.setDrawCircles(false);
                                LineData lineData = new LineData(dataset);
                                lineChart.setData(lineData);
                                lineChart.invalidate();
                            mChart = findViewById(R.id.chart1);
                            mChart.setUsePercentValues(true);
                            mChart.getDescription().setEnabled(false);
                            mChart.setExtraOffsets(0, 0, 0, 0);
                            mChart.setDrawCenterText(false);
                            mChart.setDrawEntryLabels(true);
                            mChart.setUsePercentValues(false);
                            mChart.getDescription().setEnabled(false);
                            mChart.setHoleRadius(60);
                            mChart.setEntryLabelColor(R.color.colorPrimary);
                           // entriesPie.add(new PieEntry(100f, "Доход")); //expense1
                            List<Integer> colors = new ArrayList<Integer>();
                            colors.add(R.color.colorPrimary);
                            PieDataSet dataset2 = new PieDataSet(entriesPie, "PifTitle");


                            //ab.setSubtitle("sub-title");
                            dataset2.setColors(ColorTemplate.MATERIAL_COLORS);
                            dataset2.setSliceSpace(2);
                            PieData data = new PieData(dataset2);
                            mChart.setData(data);
                            mChart.getLegend().setEnabled(false);
                            mChart.invalidate();
                            TextView change_m_text =  (TextView) findViewById(R.id.textView24);
                            change_m_text.setText(String.format("%.2f",change_m)+" %");
                            TextView change_3m_text =  (TextView) findViewById(R.id.textView26);
                            change_3m_text.setText(String.format("%.2f",change_3m)+" %");
                            TextView change_yaer_text =  (TextView) findViewById(R.id.textView9);
                            change_yaer_text.setText(String.format("%.2f",change_year)+" %");
                            dialog.dismiss();
                            pifinfo_view.setVisibility(View.VISIBLE);

                        }
                    });

                } else {
                    dialog.dismiss();
                }
            }
        });




    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("TEST", "onDestroy");
        if(dialog.isShowing()) {
            dialog.dismiss();

        }
        if(call_link !=null){
            call_link.cancel();
        }
    }
}
