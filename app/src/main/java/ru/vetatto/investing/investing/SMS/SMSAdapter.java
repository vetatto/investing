package ru.vetatto.investing.investing.SMS;

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
import java.util.List;

import ru.vetatto.investing.investing.PifList.PifAdapter;
import ru.vetatto.investing.investing.PifList.PifData;
import ru.vetatto.investing.investing.R;
import ru.vetatto.investing.investing.protfolioPifInfo;

    public class SMSAdapter extends RecyclerView.Adapter<SMSAdapter.ViewHolder>   {
        private LayoutInflater inflater;
        private List<SMSData> phones;
        int divider_check = 0;
        public SMSAdapter(Context context, List<SMSData> phones) {
            this.phones = phones;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = inflater.inflate(R.layout.sms_recycle, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            float procent_pif, procent_izm;

            final SMSData phone = phones.get(position);
                holder.fromSMS.setText(phone.getSMSFrom());
                holder.titleSMS.setText(phone.getSMSTitle());

            }


        @Override
        public int getItemCount() {
            return phones.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            //  final ImageView spider;
            TextView fromSMS,titleSMS;

            ViewHolder(View view) {
                super(view);

                titleSMS = (TextView) view.findViewById(R.id.titleSMS);
                fromSMS = (TextView) view.findViewById(R.id.fromSMS);

            }
        }
    }
