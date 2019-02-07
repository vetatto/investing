package ru.vetatto.investing.investing.GraphicEditActiv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.vetatto.investing.investing.R;

public class GraphicLegendAdapter extends RecyclerView.Adapter<GraphicLegendAdapter.ViewHolder> implements CompoundButton.OnCheckedChangeListener  {
    private LayoutInflater inflater;
    private List<GraphicLegendData> legendData;
    List<String> hideLegend = new ArrayList<String>();
    List<String> showLegend = new ArrayList<String>();
    public GraphicLegendAdapter(Context context, ArrayList<GraphicLegendData> legendData) {
        this.legendData = legendData;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public GraphicLegendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.graphiclegendspinner, parent, false);

        return new GraphicLegendAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GraphicLegendAdapter.ViewHolder holder, int position) {
        final GraphicLegendData legend = legendData.get(position);
        holder.title.setText(legend.getGraphicLegendTitle());
        holder.title.setTextColor(legend.getGraphicLegendColor());
        if (holder.title != null) {
            holder.title.setOnCheckedChangeListener(this);
        }
    }

    @Override
    public int getItemCount() {
        return legendData.size();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        if(compoundButton.isChecked()) {
            showLegend.add(compoundButton.getText().toString());
            if(hideLegend.contains(compoundButton.getText().toString())) {
                hideLegend.remove(hideLegend.indexOf(compoundButton.getText().toString()));
            }
        }
        else{
            hideLegend.add(compoundButton.getText().toString());
            if(showLegend.contains(compoundButton.getText().toString())) {
                showLegend.remove(showLegend.indexOf(compoundButton.getText().toString()));
            }
        }
        mBluetoothClickListener.onBluetoothDeviceClicked(hideLegend, showLegend);
}
    public interface OnBluetoothDeviceClickedListener {
        void onBluetoothDeviceClicked(List<String> hideLegend,List<String> showLegends);
    }
    private OnBluetoothDeviceClickedListener mBluetoothClickListener;

    public void setOnBluetoothDeviceClickedListener(OnBluetoothDeviceClickedListener l) {
        mBluetoothClickListener = l;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        //  final ImageView spider;
        final Switch title;
        ViewHolder(View view) {
            super(view);
            title = (Switch) view.findViewById(R.id.legendTitle);
        }
    }
}
