package ru.vetatto.investing.investing;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;

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
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ru.vetatto.investing.investing.HTTP.Get;

public class protfolioPifInfo extends AppCompatActivity {
    List<Entry> entries;
    ArrayList<PieEntry> entriesPie = new ArrayList<PieEntry>();
    ArrayList<String> labels;
    String PifTitle;
    private PieChart mChart;
    float pif_price, change_m, change_3m, change_year,pif_amount,end_price;
    int day_investing;
    ProgressDialog dialog;
    Get example;
    Call call_link;
    NumberFormat f;
    ConstraintLayout pifinfo_view;
    private LineChartView chart;
    private LineChartData data;
    private int numberOfLines = 1;
    private int maxNumberOfLines = 4;
    private int numberOfPoints = 12;

    float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];

    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean hasLabels = false;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = false;
    private boolean pointsHaveDifferentColor;
    private boolean hasGradientToTransparent = false;
    List<PointValue> values = new ArrayList<PointValue>();
    List<AxisValue> axisValues = new ArrayList<AxisValue>();
    TextView my_sum_pif,procent_m;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protfolio_pif_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
        final CollapsingToolbarLayout mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbar.setTitle(" ");
        toolbar.setTitle(" ");
        f=NumberFormat.getInstance();
        my_sum_pif = (TextView) findViewById(R.id.my_sum_pif);
        procent_m = (TextView) findViewById(R.id.procent);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
        //toolbar.setTitle(PifTitle);

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
                    Log.d("TEST_PIF",responseStr);
                    entries = new ArrayList<Entry>();
                    labels = new ArrayList<String>();
                    try {

                        ///Данные графика
                        JSONObject dataJsonObj = new JSONObject(responseStr);
                        day_investing= Integer.valueOf(dataJsonObj.getString("day_investing"));
                        JSONArray friends = dataJsonObj.getJSONArray("data");
                        for (int i = 0; i < friends.length(); i++) {
                                JSONObject dataJsonObj2 = friends.getJSONObject(i);
                                String date = dataJsonObj2.getString("date");
                                String pay = dataJsonObj2.getString("pay");
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
                                Date dates = sdf.parse(date);
                                entries.add(new Entry(dates.getTime(), Float.valueOf(pay)));
                            values.add(new PointValue(dates.getTime(), Float.valueOf(pay)));
                            AxisValue axisValue = new AxisValue(dates.getTime());
                            axisValue.setLabel(date);
                            axisValues.add(axisValue);
                          /*  values.add(new PointValue(1, 4));
                            values.add(new PointValue(2, 3));
                            values.add(new PointValue(3, 4));*/

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
                            pif_amount =  Float.valueOf(pifinfo.getString("amount"));
                            end_price =  Float.valueOf(pifinfo.getString("end_price"));
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
                            chart = (LineChartView) findViewById(R.id.chart_line);
                            Line line = new Line(values).setColor(R.color.colorPlus).setCubic(true);
                            List<Line> lines = new ArrayList<Line>();
                            line.setHasPoints(false);
                            line.setStrokeWidth(2);
                            lines.add(line);
                            LineChartData data = new LineChartData();
                            data.setLines(lines);

                            Axis axisX = new Axis(axisValues);
                            Axis axisY = new Axis().setHasLines(true);
                            axisX.setName("Axis X");
                            //axisY.setName("Axis Y");
                            //axisX.setMaxLabelChars(4);
                            axisX.setHasLines(true);
                            axisX.setMaxLabelChars(10);
                            data.setAxisYLeft(axisY);
                            data.setAxisXBottom(axisX);
                            LineChartView chart = (LineChartView) findViewById(R.id.chart_line);

                            chart.setContainerScrollEnabled(true, ContainerScrollType.VERTICAL);
                            data.setBaseValue(Float.NEGATIVE_INFINITY);
                            chart.setLineChartData(data);
                            chart.setViewportCalculationEnabled(false);
                            TextView change_m_text =  (TextView) findViewById(R.id.textView24);
                            change_m_text.setText(String.format("%.2f",change_m)+" %");
                            TextView change_3m_text =  (TextView) findViewById(R.id.textView26);
                            change_3m_text.setText(String.format("%.2f",change_3m)+" %");
                            TextView change_yaer_text =  (TextView) findViewById(R.id.textView9);
                            change_yaer_text.setText(String.format("%.2f",change_year)+" %");
                             my_sum_pif.setText(f.format(Math.round(day_investing)));
                             float proc_rasch=(((pif_price*pif_amount)-(end_price*pif_amount))/(pif_price*pif_amount)*(-100));
                             procent_m.setText(String.format("%.2f",(proc_rasch)));
                            dialog.dismiss();
                            //pifinfo_view.setVisibility(View.VISIBLE);
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



