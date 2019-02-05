package ru.vetatto.investing.investing.UK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.List;

import ru.vetatto.investing.investing.PifInfo.protfolioPifInfo;
import ru.vetatto.investing.investing.PifList.PifData;
import ru.vetatto.investing.investing.R;

public class UKAdapter extends RecyclerView.Adapter<UKAdapter.ViewHolder>   {
    private LayoutInflater inflater;
    private List<UKData> ukdata;
    int divider_check = 1;
    Bitmap mIcon_val;
    public UKAdapter(Context context, List<UKData> ukdata) {
        this.ukdata = ukdata;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public UKAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.uk_spiner, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UKAdapter.ViewHolder holder, int position) {
        float procent_pif, procent_izm;

        final UKData UK = ukdata.get(position);
        Log.d("TEST","Type:"+divider_check);
        NumberFormat f = NumberFormat.getInstance();
        Log.d("ADS","position "+position+" size "+ukdata.size());
        /*if(position==ukdata.size()-1){
            AdRequest adRequest = new AdRequest.Builder().addTestDevice("95A895D25C18E95F46845472700A79EB").build();
            holder.adView.loadAd(adRequest);
        }
        else{
            holder.adView.setVisibility(View.GONE);
        }*/


            holder.UKTitle.setText(UK.getUKTitle());


        //final Context context = holder.itemView.getContext();

                URL newurl = UK.getUKImage();
                Glide.with(holder.itemView.getContext())
                        .load(newurl)
                        .into(holder.logo);
                /*mIcon_val= BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
                holder.logo.setImageBitmap(mIcon_val);*/



        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, protfolioPifInfo.class);
                intent.putExtra("idPif", phone.getPifId());
                intent.putExtra("token", phone.getApiToken());
                intent.putExtra("name", phone.getukTitle() + " - " + phone.getPifTitle());
                intent.putExtra("view","VIEW");
                              context.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return ukdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
     //  final ImageView spider;
        final TextView UKTitle;
        final ImageView logo;
        AdView adView;
        ViewHolder(View view) {
            super(view);
            UKTitle= (TextView) view.findViewById(R.id.UKTitle);
            logo = (ImageView) view.findViewById(R.id.UKLogo);
           MobileAds.initialize(view.getContext(), "ca-app-pub-3909765981983100~5463344129");
           adView = view.findViewById(R.id.adView);

        }
        }
    }