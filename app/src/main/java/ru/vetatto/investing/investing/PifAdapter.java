package ru.vetatto.investing.investing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

public class PifAdapter extends RecyclerView.Adapter<PifAdapter.ViewHolder>   {
    private LayoutInflater inflater;
    private List<PifData> phones;
    int divider_check = 0;
    PifAdapter(Context context, List<PifData> phones) {
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
        if (phone.getTypeInstrument() == 1){
            if(divider_check==phone.getTypeInstrument()) {
                holder.divider.setVisibility(View.GONE);
                holder.spider.setVisibility(View.GONE);
            }
            else{
                holder.divider.setVisibility(View.VISIBLE);
                holder.spider.setVisibility(View.VISIBLE);
                divider_check=phone.getTypeInstrument();
                holder.divider.setText("Паевые фонды");
            }
            holder.datePif.setText(phone.getDate());
        holder.nameView.setText(phone.getPifTitle());
        procent_pif = (phone.getPifSumAmount() * phone.getPifDatePrice() - phone.getPifSrPrice() * phone.getPifSumAmount()) / (phone.getPifSrPrice() * phone.getPifSumAmount() / 100);
        holder.cat_name.setText(phone.getPifNameCat());
        holder.companyView.setText(f.format(phone.getPifSumAmount() * phone.getPifDatePrice()) + " \u20BD");
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
                context.startActivity(intent);
            }
        });
    }
    else if(phone.getTypeInstrument() == 2){
            if(divider_check==phone.getTypeInstrument()) {
                holder.divider.setVisibility(View.GONE);
                holder.spider.setVisibility(View.GONE);
                divider_check=phone.getTypeInstrument();
            }
            else{
                holder.divider.setVisibility(View.VISIBLE);
                holder.spider.setVisibility(View.VISIBLE);
                holder.divider.setText("Денежные средства");
                divider_check=phone.getTypeInstrument();

            }
        }
    }

    @Override
    public int getItemCount() {
        return phones.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
       final ImageView spider;
        final TextView nameView,  ukTitle, companyView, all_procent, datePif, procent, cat_name, /*sr_price,*/ divider;

        ViewHolder(View view) {
            super(view);
            spider= (ImageView) view.findViewById(R.id.spider);
            nameView = (TextView) view.findViewById(R.id.titlePortfolio);
            companyView = (TextView) view.findViewById(R.id.pay_price);
            ukTitle = (TextView) view.findViewById(R.id.textView6);
            all_procent = (TextView) view.findViewById(R.id.textView4);
            datePif = (TextView) view.findViewById(R.id.amount);
            procent= (TextView) view.findViewById(R.id.izm_day);
            cat_name= (TextView) view.findViewById(R.id.name_cat_pif);
            divider = (TextView) view.findViewById(R.id.divider);
        }
        }
    }
