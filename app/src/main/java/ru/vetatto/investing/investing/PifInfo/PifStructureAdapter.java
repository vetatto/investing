package ru.vetatto.investing.investing.PifInfo;

import android.content.Context;
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

public class PifStructureAdapter extends RecyclerView.Adapter<PifStructureAdapter.ViewHolder>   {
    private LayoutInflater inflater;
    private List<PifStructureData> phones;
    int divider_check = 0;
    public PifStructureAdapter(Context context, ArrayList<PifStructureData> phones) {
        this.phones = phones;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public PifStructureAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.operation_spiner, parent, false);

        return new PifStructureAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PifStructureAdapter.ViewHolder holder, int position) {
        final PifStructureData phone = phones.get(position);
        Log.d("TEST","Type:"+divider_check);
        NumberFormat f = NumberFormat.getInstance();
        Log.d("ADS","position "+position+" size "+phones.size());

        holder.amountPay.setText(phone.getPifStructureEmittet());
        holder.dateOperation.setText(phone.getPifStructureProcent());
    }

    @Override
    public int getItemCount() {
        return phones.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //  final ImageView spider;
        final TextView nameView;
        final TextView amountPay, dateOperation;
        ViewHolder(View view) {
            super(view);
            nameView = (TextView) view.findViewById(R.id.typeOperation);
            amountPay=(TextView) view.findViewById(R.id.amount_pay);
            dateOperation= (TextView) view.findViewById(R.id.date_operation);
        }
    }
}
