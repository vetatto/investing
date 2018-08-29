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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

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
    PortfolioListAdapter PortfolioListAdapter;
    float sum_money;
    float sum_invest;
    TextView money_sum;
    TextView plus;
    public String api_token;
    NumberFormat f;
    UkAutocompleteAdapter adapter;
    ArrayList<UkAutocompleteData> ukList = new ArrayList<UkAutocompleteData>();

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


    private void add_pif(){
        context=this.getContext();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        sum_invest=0;
        sum_money=0;
        //final ArrayList<String> responseList = new ArrayList<String>();
        final TextView text = view.findViewById(R.id.textView3);
        adapter = new UkAutocompleteAdapter(context, R.layout.uk_autocomplete, R.id.UkNameLabel, ukList);
        AutoCompleteTextView nameTV = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);
        nameTV.setAdapter(adapter);
        nameTV.setOnItemClickListener(onItemClickListener);
        Get example = new Get();
        String response = null;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        api_token = sp.getString("API_TOKEN", " ");
        money_sum = view.findViewById(R.id.sum_money);
        plus = view.findViewById(R.id.plus);

        example.Get("/get_uk", api_token, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TESTE",e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    String responseStr = response.body().string();
                    Log.d("TESTE",responseStr);
                    try {
                        JSONObject dataJsonObj = new JSONObject(responseStr);
                        JSONArray friends = dataJsonObj.getJSONArray("data");
                        for (int i = 0; i < friends.length(); i++) {
                                JSONObject dataJsonObj2 = friends.getJSONObject(i);
                                String title = dataJsonObj2.getString("minTitle");
                                int id = Integer.valueOf(dataJsonObj2.getString("id"));
                                ukList.add(new UkAutocompleteData(title,"",id));
                            }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("TESTE",response.body().string());
                    String responseStr = response.body().string();
                    String resp = response.message().toString();
                    if(resp.equals("Unauthorized")) {
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
    private AdapterView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Toast.makeText(context,
                            "Clicked item from auto completion list "
                                    + adapter.getItem(i).getName().toString()
                            , Toast.LENGTH_SHORT).show();
                }
            };

}