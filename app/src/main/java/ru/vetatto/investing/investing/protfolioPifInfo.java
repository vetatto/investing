package ru.vetatto.investing.investing;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ViewportChangeListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PreviewColumnChartView;
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
    //SkeletonScreen skeletonScreen;
    int day_per = 0, day_end=0, month=0, year=0;
    String  day_per_s, day_end_s, pay_per_s,pay_end_s;
    ArrayList average_marge = new ArrayList();
    float average;
    public static final String TYPE_VIEW = "VIEW";
    private ShimmerFrameLayout mShimmerViewContainer;
    private ColumnChartView Columnchart;
    private PreviewColumnChartView previewChart;
    private ColumnChartData Columndata;
    private ColumnChartData previewData;
    List<Column> columns = new ArrayList<Column>();
    List<SubcolumnValue> Columnvalues;

   /* public static void start(Context context, String type) {
        Intent intent = new Intent(context, protfolioPifInfo.class);
        intent.putExtra(PARAMS_TYPE, type);
        context.startActivity(intent);*/
   // }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protfolio_pif_info);
        mShimmerViewContainer =(ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.startShimmer(); // If auto-start is set to false

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
       actionbar.setDisplayHomeAsUpEnabled(true);
       actionbar.setHomeButtonEnabled(true);
        final CollapsingToolbarLayout mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbar.setTitle(" ");
        toolbar.setTitle(" ");
        Columnchart = (ColumnChartView) findViewById(R.id.Columnchart);
        previewChart = (PreviewColumnChartView) findViewById(R.id.chart_preview);




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
       // dialog.show();
        String api_token = getIntent().getStringExtra("token");
        int id = getIntent().getIntExtra("idPif", 0);
        PifTitle = getIntent().getStringExtra("name");
        TextView infoTitle = (TextView) findViewById(R.id.title_pif);
        infoTitle.setText(PifTitle);
        //toolbar.setTitle(PifTitle);
        String mType = getIntent().getStringExtra("view");
        final View rootView = findViewById(R.id.rootView);
        /*if (TYPE_VIEW.equals(mType)) {
            skeletonScreen = Skeleton.bind(rootView)
                    .load(R.layout.pifinfo_scelet)
                    .duration(1000)
                    .color(R.color.shimmer_color)
                    .angle(0)
                    .show();
        }*/

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
                        int z=1;
                        for (int i = 0; i < friends.length(); i++) {
                            Columnvalues = new ArrayList<SubcolumnValue>();
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
                            labels.add(date);
                            String[] subStr = date.split("-");

                            if (i == 0) {
                                day_per = Integer.valueOf(subStr[2]);
                                day_per_s=date;
                                day_end = Integer.valueOf(subStr[2]);
                                month = Integer.valueOf(subStr[1]);
                                year = Integer.valueOf(subStr[0]);
                                pay_per_s=pay;
                                pay_end_s=pay;

                             //   Log.d("TEST_DATE", "Первый день месяца " + date);
                            }
                            ///В районе одного года
                            if (year == Integer.valueOf(subStr[0])) {
                                //в районе одного месяца
                                if (month == Integer.valueOf(subStr[1])) {
                                    if (day_end <= Integer.valueOf(subStr[2])) {
                                        day_end = Integer.valueOf(subStr[2]);
                                        day_end_s=date;;
                                        pay_end_s=pay;
                                    }
                                }
                                //месяц сменился
                                if (month < Integer.valueOf(subStr[1])) {
                                    //Log.d("TEST_DATE","Первый день "+day_per_s+" стоимость "+pay_per_s);
                                   // Log.d("TEST_DATE","Последний день "+day_end_s+" стоимость "+pay_end_s);
                                    float procent = (Float.valueOf(pay_end_s)-Float.valueOf(pay_per_s))/(Float.valueOf(pay_per_s)/100);
                                    Log.d("TEST_DATE",  z+".доход за "+day_end_s+" "+String.valueOf(procent));
                                    Columnvalues.add(new SubcolumnValue(procent, ChartUtils.pickColor()));
                                    columns.add(new Column(Columnvalues));
                                    z=z+1;
                                    average_marge.add(procent);
                                    day_per_s=date;
                                    day_end_s=date;
                                    pay_per_s=pay;
                                    pay_end_s=pay;
                                    day_per = Integer.valueOf(subStr[2]);
                                    day_end = Integer.valueOf(subStr[2]);
                                    month = Integer.valueOf(subStr[1]);


                                }
                            }
                            //Год сменился
                          if(year < Integer.valueOf(subStr[0])){
                              //Log.d("TEST_DATE","Первый день "+day_per_s+" стоимость"+pay_per_s);
                              //Log.d("TEST_DATE","Последний день "+day_end_s+" стоимость"+pay_end_s);
                              float procent = (Float.valueOf(pay_end_s)-Float.valueOf(pay_per_s))/(Float.valueOf(pay_per_s)/100);
                              Log.d("TEST_DATE", z+".доход за "+day_end_s+" "+String.valueOf(procent));
                              Columnvalues.add(new SubcolumnValue(procent, ChartUtils.pickColor()));
                              columns.add(new Column(Columnvalues));
                              z=z+1;
                              average_marge.add(procent);
                              day_per = Integer.valueOf(subStr[2]);
                              day_per_s=date;
                              pay_per_s=pay;
                              pay_end_s=pay;
                              day_end = Integer.valueOf(subStr[2]);
                              month = Integer.valueOf(subStr[1]);
                              year = Integer.valueOf(subStr[0]);
                          }
                        }

                        for (int i = 0; i < average_marge.size(); i++) {
                            average = average + Float.valueOf(average_marge.get(i).toString());
                        }
                        Log.d("TEST_DATE", "Сумма доходностей "+average);
                        Log.d("TEST_DATE", "Периодов "+average_marge.size());
                        Log.d("TEST_DATE", "Средняя месячная доходность фонда за весь период "+average/average_marge.size());

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
                                //entriesPie.add(new PieEntry(pif_price*pif_amount, "Инвестиции")); //revenue1
                                //if((end_price*pif_amount-pif_price*pif_amount)>0){
                                //    entriesPie.add(new PieEntry((end_price*pif_amount-pif_price*pif_amount), "Доход")); //revenue1
                               // }
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
                            mShimmerViewContainer.stopShimmer();
                            mShimmerViewContainer.setVisibility(View.INVISIBLE);
                            AppBarLayout appbar = (AppBarLayout) findViewById(R.id.appbar2);
                            appbar.setVisibility(View.VISIBLE);
                            RelativeLayout content_pif_info = (RelativeLayout) findViewById(R.id.container_portfoli_pif_info);
                            content_pif_info.setVisibility(View.VISIBLE);
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

                            generateDefaultData();

                            Columnchart.setColumnChartData(Columndata);
                            // Disable zoom/scroll for previewed chart, visible chart ranges depends on preview chart viewport so
                            // zoom/scroll is unnecessary.
                            Columnchart.setZoomEnabled(false);
                            Columnchart.setScrollEnabled(false);

                            previewChart.setColumnChartData(previewData);
                            previewChart.setViewportChangeListener(new ViewportListener());

                            previewX(false);
                        }
                    });

                } else {
                   // dialog.dismiss();
                }
            }
        });




    }
    private void generateDefaultData() {
        int numSubcolumns = 1;
        int numColumns = 50;

        Columndata = new ColumnChartData(columns);
        Columndata.setAxisXBottom(new Axis());
        Columndata.setAxisYLeft(new Axis().setHasLines(true));

        // prepare preview data, is better to use separate deep copy for preview chart.
        // set color to grey to make preview area more visible.
        previewData = new ColumnChartData(Columndata);
        for (Column column : previewData.getColumns()) {
            for (SubcolumnValue value : column.getValues()) {
                value.setColor(ChartUtils.DEFAULT_DARKEN_COLOR);
            }
        }

    }

    private void previewY() {
        Viewport tempViewport = new Viewport(Columnchart.getMaximumViewport());
        float dy = tempViewport.height() / 4;
        tempViewport.inset(0, dy);
        previewChart.setCurrentViewportWithAnimation(tempViewport);
        previewChart.setZoomType(ZoomType.VERTICAL);
    }

    private void previewX(boolean animate) {
        Viewport tempViewport = new Viewport(Columnchart.getMaximumViewport());
        float dx = tempViewport.width() / 4;
        tempViewport.inset(dx, 0);
        if (animate) {
            previewChart.setCurrentViewportWithAnimation(tempViewport);
        } else {
            previewChart.setCurrentViewport(tempViewport);
        }
        previewChart.setZoomType(ZoomType.HORIZONTAL);
    }

    private void previewXY() {
        // Better to not modify viewport of any chart directly so create a copy.
        Viewport tempViewport = new Viewport(Columnchart.getMaximumViewport());
        // Make temp viewport smaller.
        float dx = tempViewport.width() / 4;
        float dy = tempViewport.height() / 4;
        tempViewport.inset(dx, dy);
        previewChart.setCurrentViewportWithAnimation(tempViewport);
    }
    private class ViewportListener implements ViewportChangeListener {

        @Override
        public void onViewportChanged(Viewport newViewport) {
            // don't use animation, it is unnecessary when using preview chart because usually viewport changes
            // happens to often.
            Columnchart.setCurrentViewport(newViewport);
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmer();
    }

    @Override
    protected void onPause() {
        mShimmerViewContainer.startShimmer();
        super.onPause();
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



