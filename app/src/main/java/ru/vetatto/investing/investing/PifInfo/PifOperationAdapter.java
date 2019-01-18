package ru.vetatto.investing.investing.PifInfo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

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

        View view = inflater.inflate(R.layout.operation_spiner, parent, false);

        return new PifOperationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PifOperationAdapter.ViewHolder holder, int position) {
        final PifOperationData phone = phones.get(position);
        Log.d("TEST","Type:"+divider_check);
        NumberFormat f = NumberFormat.getInstance();
        Log.d("ADS","position "+position+" size "+phones.size());
        if(phone.getPifOperationType()==0){
            holder.nameView.setText("Покупка");
        }
        else{
            holder.nameView.setText("Продажа");
        }

    }

    @Override
    public int getItemCount() {
        return phones.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //  final ImageView spider;
        final TextView nameView;
        ViewHolder(View view) {
            super(view);
            nameView = (TextView) view.findViewById(R.id.typeOperation);
        }
    }
}
