package ru.vetatto.investing.investing.Add;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.StringPrepParseException;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ru.vetatto.investing.investing.FirstFragment;
import ru.vetatto.investing.investing.HTTP.Get;
import ru.vetatto.investing.investing.HTTP.Put;
import ru.vetatto.investing.investing.MainActivity;
import ru.vetatto.investing.investing.PifList.PifAutocompleteAdapter;
import ru.vetatto.investing.investing.PifList.PifAutocompleteData;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_add_pif);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
       // ViewPager pager = (ViewPager) findViewById(R.id.addViewPager);
       // AddFragmentPagerAdapter pagerAdapter = new AddFragmentPagerAdapter(getSupportFragmentManager());
       // pager.setAdapter(pagerAdapter);
        f = NumberFormat.getInstance();
            //view = inflater.inflate(R.layout.add_pif, null);
            load = (ProgressBar) findViewById(R.id.load_add_pif);
            ll = (LinearLayout) findViewById(R.id.mainAddLayout);
            add_pif();
    }



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
      ////Обработка сохранения операции с паями
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
                    date_pif.put("comission_procent", cprocent);
                    date_pif.put("comission_sum", csum);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                example.put("/add_pay_pif", api_token, date_pif, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String responseStr = response.body().string();
                            Log.d("TESTE", responseStr);
                            try {
                                JSONObject dataJsonObj = new JSONObject(responseStr);
                                JSONObject data = dataJsonObj.getJSONObject("data");
                                String api_token = data.getString("api_token");
                                Log.d("TESTE", "API_TOKEN: " + api_token);


                                if (!api_token.isEmpty()) {
                                    Intent intent = new Intent(context, MainActivity.class);
                                    context.startActivity(intent);
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Log.d("TESTE", response.toString());
                        }
                    }
                });

            }
        };
        // присвоим обработчик кнопке OK (btnOk)
        button_save.setOnClickListener(oclBtnOk);

//URL:/add_pif_operation
    ////{"message":"add",
        //"date":"2018-12-25",
        //"id_instrument":244,
        //"amount":"86.16232",
        //"price":"58.03",
        //"sum_invest":"1000"
        //}////
        final RadioGroup radioGroup = findViewById(R.id.radio_add);
           radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("chk", "id" + checkedId);

                if (checkedId == R.id.radio_pay) {
                    money_price.setEnabled(false);
                    amount_pay.setEnabled(true);
                    amount_pay.requestFocus();
                    //some code
                } else if (checkedId == R.id.radio_summ) {
                    money_price.setEnabled(true);
                    amount_pay.setEnabled(false);
                    money_price.requestFocus();
                    //some code
                }

            }

        });

        //Обработка ввода суммы паев
        amount_pay.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                if (amount_pay.isFocused()) {
                    //Если кол-во паев меньше 0 то пишем 0
                    if(amount_pay.getText().toString().isEmpty()){
                        amounts_pay=0;
                    }
                    else{
                        amounts_pay = Float.valueOf(amount_pay.getText().toString());
                    }
                    //Если стоимость пая указана, но сумма инвестирования не указана
                    if ((!pay_price.getText().toString().isEmpty()) && (money_price.getText().toString().isEmpty())) {
                        first_pay=true; // Первым указали стоимость пая
                        float price;
                        if (amounts_pay < 0 && amount_pay.getText().toString().isEmpty()) {
                          money_price.setText("0.00");
                        }
                        else{
                            price = Float.valueOf(money_price.getText().toString());
                            money_price.setText(String.valueOf(price*amounts_pay));
                        }
                    }
                    //Если указана сумма инвестирования, но не указана стоимость пая
                    else if (!money_price.getText().toString().isEmpty() && (pay_price.getText().toString().isEmpty())) {
                        float price;
                        float amounts_pay;
                        amounts_pay = Float.valueOf(amount_pay.getText().toString());
                        if (amounts_pay < 0 && amount_pay.getText().toString().isEmpty()) {
                            pay_price.setText("0.00");
                        }
                        else{
                            price = Float.valueOf(money_price.getText().toString());
                            pay_price.setText(String.valueOf(price / amounts_pay));
                        }
                    }

                    //Если указана и стоимость пая и сумма инвестирования
                    else if(!money_price.getText().toString().isEmpty() && !pay_price.getText().toString().isEmpty()){
                       //Если первым указали стоимость пая
                        if(first_pay){
                            float price;
                            price = Float.valueOf(pay_price.getText().toString());
                            money_price.setText(String.valueOf(price*amounts_pay));
                        }
                        else{
                            float price;
                            price = Float.valueOf(money_price.getText().toString());
                            pay_price.setText(String.valueOf(price/amounts_pay));
                        }
                    }
                }
            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        namePif.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
               Toast.makeText(context,
                        "Clicked item from auto completion list "
                                + adapterPif.getItem(pos).getName().toString()+" "+adapterPif.getItem(pos).getEndDate().toString()
                        , Toast.LENGTH_SHORT).show();
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
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
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
                    Log.d("TESTE", response.body().string());
                    String responseStr = response.body().string();
                    String resp = response.message().toString();
                    if (resp.equals("Unauthorized")) {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("API_TOKEN", null);
                        editor.commit();
                       // FragmentTransaction tran = getFragmentManager().beginTransaction();
                      //  tran.replace(R.id.container, new FirstFragment()).commit();
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


}
