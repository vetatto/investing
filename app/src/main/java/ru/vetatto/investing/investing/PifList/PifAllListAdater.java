package ru.vetatto.investing.investing.PifList;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import ru.vetatto.investing.investing.R;
import ru.vetatto.investing.investing.PifInfo.protfolioPifInfo;

public class PifAllListAdater extends RecyclerView.Adapter<PifAllListAdater.ViewHolder>   {
    private static boolean infilter=false;
    private LayoutInflater inflater;
    private static List<PifAllListData> allPif;
    private static List<PifAllListData> allPifCopy;
    int divider_check = 0;
    public PifAllListAdater(Context context, List<PifAllListData> allPif) {
        this.allPif = allPif;
        this.allPifCopy = allPif;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public PifAllListAdater.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.pif_all_list_spiner, parent, false);

        return new ViewHolder(view);
    }


    public void onBindViewHolder(PifAllListAdater.ViewHolder holder, int position) {
        float procent_pif, procent_izm;

        final PifAllListData phone = allPif.get(position);
        Log.d("TEST", "Type:" + divider_check);

        NumberFormat f = NumberFormat.getInstance();
        if (phone.getTypeInstrument() == 1) {
            if (divider_check == phone.getTypeInstrument()) {
                // holder.divider.setVisibility(View.GONE);
                // holder.spider.setVisibility(View.GONE);
            } else {
                // holder.divider.setVisibility(View.VISIBLE);
                //  holder.spider.setVisibility(View.VISIBLE);
                divider_check = phone.getTypeInstrument();
                //holder.divider.setText("Паевые фонды");

            }
            holder.datePif.setText(phone.getDate());
            holder.nameView.setText(phone.getPifTitle());
            holder.ukTitle.setText(phone.getukTitle());
            holder.pay_price.setText(phone.getPifPayPrice() + " \u20BD");

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
          /*  holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                   CardView catCard = v.findViewById(R.id.card_pif_list_item);
                    CardView.LayoutParams layoutParams = (CardView.LayoutParams)
                            catCard.getLayoutParams();
                    layoutParams.setMarginStart(64);
                    catCard.setLayoutParams(layoutParams);
                    Log.d("TEST_LONG", "LONG");
                    return true;
                }
            });*/
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



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        final TextView nameView,/*change_m,*/ pay_price, ukTitle, companyView, all_procent, datePif, procent;
        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);

            pay_price  = (TextView) view.findViewById(R.id.pay_price);
            nameView = (TextView) view.findViewById(R.id.titlePortfolio);
            companyView = (TextView) view.findViewById(R.id.pay_price);
            ukTitle = (TextView) view.findViewById(R.id.textView6);
            all_procent = (TextView) view.findViewById(R.id.textView4);
            datePif = (TextView) view.findViewById(R.id.amount);
           procent= (TextView) view.findViewById(R.id.izm_day);
        }

        @Override
        public void onClick(View view) {

        }

        @Override
        public boolean onLongClick(View view) {
            Log.d("TEST_LONG","LONG");
            return true;
        }
    }
    }
