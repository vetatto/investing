package ru.vetatto.investing.investing.PifInfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import ru.vetatto.investing.investing.PifInfo.PifOperationAdapter;
import ru.vetatto.investing.investing.PifInfo.PifOperationData;
import ru.vetatto.investing.investing.R;

import static java.lang.Math.round;

public class PifOperationAdapter extends RecyclerView.Adapter<PifOperationAdapter.ViewHolder>   {
    private LayoutInflater inflater;
    private List<PifOperationData> phones;
    int divider_check = 0;
    public PifOperationAdapter(Context context, ArrayList<PifOperationData> phones) {
        this.phones = phones;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public PifOperationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.bill_spiner, parent, false);

        return new PifOperationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PifOperationAdapter.ViewHolder holder, int position) {
        final PifOperationData phone = phones.get(position);
        Log.d("TEST","Type:"+divider_check);
        NumberFormat f = NumberFormat.getInstance();
        Log.d("ADS","position "+position+" size "+phones.size());
        holder.ukTitle.setText("TEST");

    }

    @Override
    public int getItemCount() {
        return phones.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //  final ImageView spider;
        final TextView nameView, ukTitle, companyView, all_procent, datePif2, procent/*,sum_money, /*sr_price, divider*/;
        AdView adView;
        ViewHolder(View view) {
            super(view);
            // spider= (ImageView) view.findViewById(R.id.spider);
            nameView = (TextView) view.findViewById(R.id.titlePortfolio);
            companyView = (TextView) view.findViewById(R.id.pay_price);
            ukTitle = (TextView) view.findViewById(R.id.textView6);
            all_procent = (TextView) view.findViewById(R.id.textView4);
            datePif2 = (TextView) view.findViewById(R.id.amount);
            procent= (TextView) view.findViewById(R.id.izm_day);
            // sum_money= (TextView) view.findViewById(R.id.textView31);
            // //divider = (TextView) view.findViewById(R.id.divider);

            MobileAds.initialize(view.getContext(), "ca-app-pub-3909765981983100~5463344129");
            adView = view.findViewById(R.id.adView);

        }
    }
}
