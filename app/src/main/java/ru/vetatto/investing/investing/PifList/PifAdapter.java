package ru.vetatto.investing.investing.PifList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
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
import java.util.List;

import ru.vetatto.investing.investing.R;
import ru.vetatto.investing.investing.PifInfo.protfolioPifInfo;

import static java.lang.Math.round;

public class PifAdapter extends RecyclerView.Adapter<PifAdapter.ViewHolder>   {
    private LayoutInflater inflater;
    private List<PifData> phones;
    int divider_check = 1;
    public PifAdapter(Context context, List<PifData> phones) {
        this.phones = phones;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public PifAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.bill_spiner, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PifAdapter.ViewHolder holder, int position) {
        float procent_pif, procent_izm;

        final PifData phone = phones.get(position);
        Log.d("TEST","Type:"+divider_check);
        NumberFormat f = NumberFormat.getInstance();
        Log.d("ADS","position "+position+" size "+phones.size());
        if(position==phones.size()-1){
            AdRequest adRequest = new AdRequest.Builder().addTestDevice("95A895D25C18E95F46845472700A79EB").build();
            holder.adView.loadAd(adRequest);
        }
        else{
            holder.adView.setVisibility(View.GONE);
        }
        if (phone.getTypeInstrument() == 1){
            holder.divider.setVisibility(View.GONE);
            holder.datePif.setText(phone.getDate());
        holder.nameView.setText(phone.getPifTitle());
        procent_pif = (phone.getPifSumAmount() * phone.getPifDatePrice() - phone.getPifSrPrice() * phone.getPifSumAmount()) / (phone.getPifSrPrice() * phone.getPifSumAmount() / 100);
       //holder.sum_money.setText(String.valueOf(phone.getSum_money()));
        holder.companyView.setText(f.format(Math.round(100.00*phone.getPifSumAmount()*phone.getPifDatePrice())/100.00) + " \u20BD");
        holder.ukTitle.setText(phone.getukTitle());
        if (procent_pif >= 0) {
            holder.all_procent.setTextColor(Color.parseColor("#FF99CC00"));
            holder.all_procent.setText(String.valueOf("+" + String.format("%.2f", procent_pif) + "%"));
        } else {
            holder.all_procent.setTextColor(Color.parseColor("#ffff4444"));
            holder.all_procent.setText(String.valueOf(String.format("%.2f", procent_pif) + "%"));
        }
        procent_izm = phone.getProcent();
        if (procent_izm >= 0) {
           holder.procent.setTextColor(Color.parseColor("#FF99CC00"));
            holder.procent.setText(String.valueOf("+" + String.format("%.2f", procent_izm) + "%"));
        } else {
            holder.procent.setTextColor(Color.parseColor("#ffff4444"));
           holder.procent.setText(String.valueOf(String.format("%.2f", procent_izm) + "%"));
        }

        final Context context = holder.itemView.getContext();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, protfolioPifInfo.class);
                intent.putExtra("idPif", phone.getPifId());
                intent.putExtra("token", phone.getApiToken());
                intent.putExtra("name", phone.getukTitle() + " - " + phone.getPifTitle());
                intent.putExtra("view","VIEW");
                              context.startActivity(intent);
            }
        });
    }
    else{
            if(divider_check==0) {
              holder.divider.setVisibility(View.GONE);
              divider_check=phone.getTypeInstrument();
            }
            else{
                holder.divider.setVisibility(View.VISIBLE);
                divider_check=phone.getTypeInstrument();
            }

            holder.datePif.setText(phone.getDate());
            holder.nameView.setText(phone.getPifTitle());
            procent_pif = (phone.getPifSumAmount() * phone.getPifDatePrice() - phone.getPifSrPrice() * phone.getPifSumAmount()) / (phone.getPifSrPrice() * phone.getPifSumAmount() / 100);
            //holder.sum_money.setText(String.valueOf(phone.getSum_money()));
            holder.companyView.setText(f.format(Math.round(100.00*phone.getPifSumAmount()*phone.getPifDatePrice())/100.00) + " \u20BD");
            holder.ukTitle.setText(phone.getukTitle());
            if (procent_pif >= 0) {
                holder.all_procent.setTextColor(Color.parseColor("#FF99CC00"));
                holder.all_procent.setText(String.valueOf("+" + String.format("%.2f", procent_pif) + "%"));
            } else {
                holder.all_procent.setTextColor(Color.parseColor("#ffff4444"));
                holder.all_procent.setText(String.valueOf(String.format("%.2f", procent_pif) + "%"));
            }
            procent_izm = phone.getProcent();
            if (procent_izm >= 0) {
                holder.procent.setTextColor(Color.parseColor("#FF99CC00"));
                holder.procent.setText(String.valueOf("+" + String.format("%.2f", procent_izm) + "%"));
            } else {
                holder.procent.setTextColor(Color.parseColor("#ffff4444"));
                holder.procent.setText(String.valueOf(String.format("%.2f", procent_izm) + "%"));
            }

            final Context context = holder.itemView.getContext();
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, protfolioPifInfo.class);
                    intent.putExtra("idPif", phone.getPifId());
                    intent.putExtra("token", phone.getApiToken());
                    intent.putExtra("name", phone.getukTitle() + " - " + phone.getPifTitle());
                    intent.putExtra("view","VIEW");
                    context.startActivity(intent);
                }
            });



        }
    }

    @Override
    public int getItemCount() {
        return phones.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
     //  final ImageView spider;
     final CardView cardView;
        final TextView nameView, ukTitle, companyView, all_procent, datePif, procent,divider/*,sum_money, /*sr_price, */;
        AdView adView;
        ViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card_pif_list_item);
            nameView = (TextView) view.findViewById(R.id.typeOperation);
            companyView = (TextView) view.findViewById(R.id.pay_price);
            ukTitle = (TextView) view.findViewById(R.id.legendTitle);
            all_procent = (TextView) view.findViewById(R.id.textView4);
            datePif = (TextView) view.findViewById(R.id.amount);
            procent= (TextView) view.findViewById(R.id.izm_day);
           divider = (TextView) view.findViewById(R.id.divider);
           MobileAds.initialize(view.getContext(), "ca-app-pub-3909765981983100~5463344129");
           adView = view.findViewById(R.id.adView);

        }
        }
    }
