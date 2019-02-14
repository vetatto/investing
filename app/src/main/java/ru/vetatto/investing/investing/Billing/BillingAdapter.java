package ru.vetatto.investing.investing.Billing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.List;

import ru.vetatto.investing.investing.PifInfo.protfolioPifInfo;
import ru.vetatto.investing.investing.PifList.PifData;
import ru.vetatto.investing.investing.R;
import ru.vetatto.investing.investing.Sell.Sell;

public class BillingAdapter extends RecyclerView.Adapter<BillingAdapter.ViewHolder>   {
    private LayoutInflater inflater;
    private List<BillingData> phones;
    int divider_check = 1;
    JSONObject date_pif = new JSONObject();

    public BillingAdapter(Context context, List<BillingData> phones) {
        this.phones = phones;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public BillingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.pif_all_list_spiner, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BillingAdapter.ViewHolder holder, int position) {
        float procent_pif, procent_izm;

        final BillingData phone = phones.get(position);
        Log.d("TEST","Type:"+divider_check);
        NumberFormat f = NumberFormat.getInstance();
        Log.d("ADS","position "+position+" size "+phones.size());

            holder.title.setText(phone.getBillTitle());
              holder.amount.setText(phone.getBillAmount());



    }

    @Override
    public int getItemCount() {
        return phones.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
     //  final ImageView spider;

        final TextView title, amount;

        ViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.titleBilling);
            amount = (TextView) view.findViewById(R.id.amountBilling);
           // ukTitle = (TextView) view.findViewById(R.id.legendTitle);



        }
        }
    }
