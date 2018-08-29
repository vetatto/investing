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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

public class AddPageFragment extends Fragment  {

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
            add_pif();
        }
        return view;
    }



///Отображение полного списка портфеля
    private void add_pif(){
        context=this.getContext();
        //recyclerView = view.findViewById(R.id.RV1);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(mLayoutManager);
        sum_invest=0;
        sum_money=0;
        final ArrayList<String> responseList = new ArrayList<String>();
        final TextView text = view.findViewById(R.id.textView3);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, responseList);
        AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);
        textView.setAdapter(adapter);
        //text.setText("Авторизация пройдена");
        Get example = new Get();
        String response = null;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        api_token = sp.getString("API_TOKEN", " ");

       // adapter = new PifAdapter(context, phones);
//        recyclerView.setAdapter(adapter);
        money_sum = view.findViewById(R.id.sum_money);
        plus = view.findViewById(R.id.plus);

        example.Get("/get_uk", api_token, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TEST",e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.d("TESTE",response.toString());
                if (response.isSuccessful()) {

                    String responseStr = response.body().string();
                    Log.d("TESTE",responseStr);
                    try {
                        JSONObject dataJsonObj = new JSONObject(responseStr);
                        JSONArray friends = dataJsonObj.getJSONArray("data");
                        for (int i = 0; i < friends.length(); i++) {
                                JSONObject dataJsonObj2 = friends.getJSONObject(i);
                                String title = dataJsonObj2.getString("minTitle");
                                responseList.add(title);
                            }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("TESTE",response.body().string());
                    String responseStr = response.body().string();
                    String resp = response.message().toString();
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
                            adapter.notifyDataSetChanged();
                        }
                    });
            }
        });
    }
}