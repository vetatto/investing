/////********************************************
////ru.vetato.investing.investing.Add.Add.java
////класс обработки покупки паев фондов
////*********************************************

package ru.vetatto.investing.investing.Add;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ru.vetatto.investing.investing.HTTP.Get;
import ru.vetatto.investing.investing.HTTP.Put;
import ru.vetatto.investing.investing.R;

public class Add extends AppCompatActivity {

    View view;
    TextView money_sum, plus, date_pay;
    Spinner namePif;
    ImageView hr_pif;
    LinearLayout ll;
    AutoCompleteTextView nameTV;
    ProgressBar load;
    EditText pay_price, money_price,amount_pay,edit_procent_comission,edit_sum_comission;

    int pageNumber, pif_id;
    Context context;
    float sum_money;
    float sum_invest;
    public String api_token;
    NumberFormat f;
    UkAutocompleteAdapter adapter;
    PifAutocompleteAdapter adapterPif;
    ArrayList<UkAutocompleteData> ukList = new ArrayList<UkAutocompleteData>();
    ArrayList<PifAutocompleteData> PifList = new ArrayList<PifAutocompleteData>();
    boolean sumPayInput = false;
    float amounts_pay,price_pay;
    JSONArray pif;
    JSONObject date_pif = new JSONObject();
    boolean first_pay,first_money;
    int DIALOG_DATE = 1;
    int myYear = 2019;
    String myMonth;
    int myDay = 01;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_add_pif);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
       // ViewPager pager = (ViewPager) findViewById(R.id.addViewPager);
       // AddFragmentPagerAdapter pagerAdapter = new AddFragmentPagerAdapter(getSupportFragmentManager());
       // pager.setAdapter(pagerAdapter);
        f = NumberFormat.getInstance();
            //view = inflater.inflate(R.layout.add_pif, null);
            load = (ProgressBar) findViewById(R.id.load_add_pif);
            ll = (LinearLayout) findViewById(R.id.mainAddLayout);
            Button button_save = findViewById(R.id.button2);
            button_save.setEnabled(false);
            button_save.setBackgroundColor(Color.parseColor("#EEEEEE"));

            add_pif();
    }
    /////***********************************************
    ///// Проверка ввода всех данных и активации кнопки
    /////***********************************************
    private void button_save_activate() {
        try {
            if (pif_id > 0 && //Если выбран фонд
                    (pay_price.getText().toString() != "" & Float.valueOf(pay_price.getText().toString()) > 0) & // Если указана стоимость Пая
                     ((money_price.getText().length() > 0 & Float.valueOf(money_price.getText().toString()) > 0) | (amount_pay.getText().length() > 0 & Float.valueOf(amount_pay.getText().toString()) > 0)) &
                    (date_pay.getText().length() > 0 & !date_pay.getText().toString().equals("0000-00-00"))) {
                Button button_save = findViewById(R.id.button2);
                button_save.setEnabled(true);
                button_save.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            } else {
                Button button_save = findViewById(R.id.button2);
                button_save.setEnabled(false);
                button_save.setBackgroundColor(Color.parseColor("#EEEEEE"));
            }
        } catch (NumberFormatException e) {
            ///Обработка ошибок с NumberFormat
        }
    }
    /////*********************************************
    private void add_pif() {
        context = this;
        sum_invest = 0;
        sum_money = 0;
        pay_price = findViewById(R.id.pay_price);
        date_pay = findViewById(R.id.date_pay);
        hr_pif = findViewById(R.id.hr_pif);
        adapter = new UkAutocompleteAdapter(this, R.layout.uk_autocomplete, R.id.UkNameLabel, ukList);
        adapterPif = new PifAutocompleteAdapter(this, R.layout.uk_autocomplete, R.id.UkNameLabel, PifList);
        nameTV = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        nameTV.setAdapter(adapter);
        nameTV.setOnItemClickListener(onItemClickListener);
        namePif = (Spinner) findViewById(R.id.spinner);
        namePif.setAdapter(adapterPif);
        money_price=(EditText) findViewById(R.id.money_price);
        amount_pay=(EditText) findViewById(R.id.amount_pay);
        edit_procent_comission=(EditText) findViewById(R.id.edit_procent_comission);
        edit_sum_comission=(EditText) findViewById(R.id.edit_sum_comission);

        ////*************************************************
        //// Обработка нажатия на дату
        ////************************************************
        ////Если дата еще не выбиралась
        date_pay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b==true) {
                    showDialog(DIALOG_DATE);
                }
            }
        });
        /////Если фокус на поле с датой
        date_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    showDialog(DIALOG_DATE);
            }
        });
        /////*************************************************

        /////***************************************
        ///// Обработчик нажатия кнопки сохранить
       /////****************************************
        Button button_save = findViewById(R.id.button2);
        // создаем обработчик нажатия
        View.OnClickListener oclBtnOk = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Put example = new Put();
                try {
                    String pprice=pay_price.getText().toString();
                    String pamount=amount_pay.getText().toString();
                    String sinvest=money_price.getText().toString();
                    String cprocent=edit_procent_comission.getText().toString();
                    String csum = edit_sum_comission.getText().toString();
                    String doperation=date_pay.getText().toString();
                    date_pif.put("message", "add");
                    date_pif.put("idPif", pif_id);
                    date_pif.put("payPrice", pprice);
                    date_pif.put("payAmount", pamount);
                    date_pif.put("sumInvest", sinvest);
                    date_pif.put("date", doperation);
                    date_pif.put("comissionProcent", cprocent);
                    date_pif.put("comission_sum", csum);
                    Button button_save = findViewById(R.id.button2);
                    button_save.setEnabled(false);
                    button_save.setBackgroundColor(Color.parseColor("#EEEEEE"));

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                example.put("/add_pay_pif", api_token, date_pif, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Button button_save = findViewById(R.id.button2);
                        button_save.setEnabled(true);
                        button_save.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                           String responseStr = response.body().string();
                            //Log.d("TESTE", responseStr);
                            try {
                                JSONObject dataJsonObj = new JSONObject(responseStr);
                                String message =dataJsonObj.getString("message");
                              //  Log.d("TESTE", "API_TOKEN: " + api_token);
                                if(message.equals("OK")){
                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            Toast.makeText(context,
                                                    "Операция успешно добавлена", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    finish();
                                }
                                else{
                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            Toast.makeText(context,
                                                    "Ошибка", Toast.LENGTH_SHORT).show();
                                            Button button_save = findViewById(R.id.button2);
                                            button_save.setEnabled(true);
                                            button_save.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Button button_save = findViewById(R.id.button2);
                            button_save.setEnabled(true);
                            button_save.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                            Log.d("TESTE", call.request().toString());
                        }

                    }
                });

            }
        };
        button_save.setOnClickListener(oclBtnOk);
        ////***************************************

        ////***************************************
        // Обработка RadioButton
        ///****************************************
        final RadioGroup radioGroup = findViewById(R.id.radio_add);
           radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_pay) {
                    money_price.setEnabled(false);
                    amount_pay.setEnabled(true);
                    amount_pay.requestFocus();
                } else if (checkedId == R.id.radio_summ) {
                    money_price.setEnabled(true);
                    amount_pay.setEnabled(false);
                    money_price.requestFocus();
                }
            }
        });
           ///*************************************

         ///***************************************
           ///Активируем или нет кнопку по событиям
        ///****************************************
        money_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                button_save_activate();
            }
        });
        date_pay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                button_save_activate();
            }
        });
        amount_pay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                button_save_activate();
            }

        });
        ////***************************************

        namePif.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                nameTV.clearFocus();
                namePif.setMinimumHeight(40);
                hr_pif.setVisibility(View.VISIBLE);
                pay_price.setText(adapterPif.getItem(pos).getPay().toString());
                date_pay.setText(adapterPif.getItem(pos).getEndDate().toString());
                TextView textView18 = findViewById(R.id.textView18);
                radioGroup.setVisibility(View.VISIBLE);
                RadioButton radio_button_sum = findViewById(R.id.radio_summ);
                radio_button_sum.setChecked(true);
                textView18.setVisibility(View.VISIBLE);
                CardView investinfo = findViewById(R.id.invest_info);
                investinfo.setVisibility(View.VISIBLE);
                pif_id=adapterPif.getItem(pos).getId();
                button_save_activate();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        ///***********ПРОРАБОТАТЬ ПЕРЕАВТОРИЗАЦИЮ********/////////
        Get example = new Get();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        api_token = sp.getString("API_TOKEN", " ");
        money_sum = findViewById(R.id.sum_money);
        plus = findViewById(R.id.plus);
        hide();
        example.Get("/get_uk", api_token, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TESTE", e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d("TESTE", "Загружено");
                    String responseStr = response.body().string();
                    Log.d("TESTE", responseStr);
                    try {
                        JSONObject dataJsonObj = new JSONObject(responseStr);
                        JSONArray friends = dataJsonObj.getJSONArray("data");
                        for (int i = 0; i < friends.length(); i++) {
                            JSONObject dataJsonObj2 = friends.getJSONObject(i);
                            String title = dataJsonObj2.getString("minTitle");
                            int id = Integer.valueOf(dataJsonObj2.getString("id"));
                            ukList.add(new UkAutocompleteData(title,id));
                        }
                        JSONObject dataJsonObj2 = new JSONObject(responseStr);
                        pif = dataJsonObj2.getJSONArray("pif");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            show();
                            adapter.notifyDataSetChanged();

                        }
                    });
                } else {
                    Log.d("TESTE", call.request().body().toString());

                    String resp = response.message().toString();
                    if (resp.equals("Unauthorized")) {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("API_TOKEN", null);
                        editor.commit();
                       // FragmentTransaction tran = getFragmentManager().beginTransaction();
                      //  tran.replace(R.id.container, new LoginGragment()).commit();
                    }
                }
            }
        });
    }

    private AdapterView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    int clickid = adapter.getItem(i).getId();
                    PifList.clear();
                    for (int ii = 0; ii < pif.length(); ii++) {
                        try {
                            Log.d("TESTE",pif.getJSONObject(ii).toString());
                            JSONObject dataJsonObj3 = pif.getJSONObject(ii);
                            String title = dataJsonObj3.getString("minTitle");
                            int id = Integer.valueOf(dataJsonObj3.getString("id"));
                            int ukid = Integer.valueOf(dataJsonObj3.getString("ukId"));
                            String end_pay = dataJsonObj3.getString("end_pay");
                            String end_date = dataJsonObj3.getString("update_time");
                            if(ukid==clickid) {
                                PifList.add(new PifAutocompleteData(title, id, end_pay,end_date));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                   adapterPif.notifyDataSetChanged();
                    namePif.performClick();
                }
            };

    private void hide() {
        load.setVisibility(View.VISIBLE);
        ll.setVisibility(View.INVISIBLE);
    }
    private void show() {
        ll.setVisibility(View.VISIBLE);
        load.setVisibility(View.INVISIBLE);
    }


    ///////******************************
    //////Диалог выбора даты  !!!!!!!!!ПЕРЕДЕЛАТЬ НА DIALOG FRAGMENT
    //////*******************************
    DatePickerDialog.OnDateSetListener DatePickerCallBack = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
            myYear = year;
            int myMonth2 = monthOfYear;
            myDay = dayOfMonth;
            switch(myMonth2) {
                case 0:myMonth="01";
                    break;
                case 1:myMonth="02";
                    break;
                case 2:myMonth="03";
                    break;
                case 3:myMonth="04";
                    break;
                case 4:myMonth="05";
                    break;
                case 5:myMonth="06";
                    break;
                case 6:myMonth="07";
                    break;
                case 7:myMonth="08";
                    break;
                case 8:myMonth="09";
                    break;
                case 9:myMonth="10";
                    break;
                case 10:myMonth="11";
                    break;
                case 11:myMonth="12";
                    break;
            }
            date_pay.setText(myDay + "." + myMonth + "." + myYear);
        }
    };
    protected Dialog onCreateDialog(int id) {
        Calendar newDate = Calendar.getInstance();
        DatePickerDialog tpd = new DatePickerDialog(this, DatePickerCallBack, newDate.get(Calendar.YEAR),newDate.get(Calendar.MONTH), newDate.get(Calendar.DAY_OF_MONTH));
        return tpd;
    }
    ///////******************************
}
