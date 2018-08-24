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
import java.util.ArrayList;
import java.util.List;

public class PifAllListAdater extends RecyclerView.Adapter<PifAllListAdater.ViewHolder>   {
    private static boolean infilter=false;
    private LayoutInflater inflater;
    private static List<PifAllListData> allPif;
    private static List<PifAllListData> allPifCopy;
    int divider_check = 0;
    PifAllListAdater(Context context, List<PifAllListData> allPif) {
        this.allPif = allPif;
        this.allPifCopy = allPif;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public PifAllListAdater.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.pif_all_list_spiner, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PifAllListAdater.ViewHolder holder, int position) {
        float procent_pif, procent_izm;

        final PifAllListData phone = allPif.get(position);
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
        holder.cat_name.setText(phone.getPifNameCat());
        holder.ukTitle.setText(phone.getukTitle());
        holder.pay_price.setText(phone.getPifPayPrice() + " \u20BD");
            try {
                float change_y = Float.valueOf(phone.getPifChangeY());
                if (change_y >= 0) {
                    holder.change_m.setTextColor(Color.parseColor("#FF99CC00"));
                    holder.change_m.setText(String.valueOf("+" + String.format("%.2f", change_y) + "%/год"));
                } else {
                    holder.change_m.setTextColor(Color.parseColor("#ffff4444"));
                    holder.change_m.setText(String.valueOf(String.format("%.2f", change_y) + "%/год"));
                }
            }
            catch(NumberFormatException e) {
                //holder.procent.setTextColor(Color.parseColor("#FF99CC00"));
                holder.change_m.setText("нет данных");
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
        return allPif.size();
    }
    public static void filter(String uk) {
        infilter=true;
        List<PifAllListData> allPifNew = new ArrayList<>();
        for(PifAllListData filtre_list : allPif)
        {
            if(filtre_list.getukTitle().equals("46")) {
                allPifNew.add(filtre_list);
            }
        }
        if(allPifNew.size()>0) {
            allPif = allPifNew;
        }
        else{
            allPif=allPifCopy;
        }
    }

    public static void filter_del() {
        allPif.clear();
            allPif=allPifCopy;
            infilter=false;
    }
    public boolean infilter(){
        return infilter;
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
       final ImageView spider;
        final TextView nameView,change_m, pay_price, ukTitle, companyView, all_procent, datePif, procent, cat_name, /*sr_price,*/ divider;

        ViewHolder(View view) {
            super(view);
            //Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Roboto-Light.ttf");
            spider= (ImageView) view.findViewById(R.id.spider);
            pay_price  = (TextView) view.findViewById(R.id.pay_price);
            nameView = (TextView) view.findViewById(R.id.titlePortfolio);
            //nameView.setTypeface(typeface);
            companyView = (TextView) view.findViewById(R.id.pay_price);
            ukTitle = (TextView) view.findViewById(R.id.textView6);
            all_procent = (TextView) view.findViewById(R.id.textView4);
            datePif = (TextView) view.findViewById(R.id.amount);
           procent= (TextView) view.findViewById(R.id.izm_day);
            cat_name= (TextView) view.findViewById(R.id.name_cat_pif);
            change_m=(TextView) view.findViewById(R.id.change_m);
           // sr_price=(TextView) view.findViewById(R.id.textView13);
            divider = (TextView) view.findViewById(R.id.divider);
           // companyView.setTypeface(typeface)
        }
        }
    }
