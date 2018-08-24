package ru.vetatto.investing.investing;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
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
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SecondFragment extends Fragment {
    View view;
    Context context;
    ArrayList<PifData> phones = new ArrayList();
    RecyclerView recyclerView;
    PifAdapter adapter;
    float sum_money;
    float sum_invest;
    TextView money_sum;
    TextView plus;
    public String api_token;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // view = inflater.inflate(R.layout.second_first, container, false);
        view = inflater.inflate(R.layout.title_view, container, false);
        ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
        PortfolioFragmentPagerAdapter pagerAdapter = new PortfolioFragmentPagerAdapter(getFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(listener);

        return view;
    }
    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                ((MainActivity) getActivity()).showFloatingActionButton();
            }
            if (position == 1) {
                ((MainActivity) getActivity()).hideFloatingActionButton();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    public static SecondFragment newInstance() {
        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}