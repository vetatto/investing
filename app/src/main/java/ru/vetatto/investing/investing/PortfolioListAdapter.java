package ru.vetatto.investing.investing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class PortfolioListAdapter extends RecyclerView.Adapter<PortfolioListAdapter.ViewHolder>   {
    private LayoutInflater inflater;
    private ArrayList<PortfoliListData> PortfolioArray;
    int divider_check = 0;
    PortfolioListAdapter(Context context, ArrayList<PortfoliListData> PortfolioArray) {
        this.PortfolioArray = PortfolioArray;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public PortfolioListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.portfolio_spinner, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PortfolioListAdapter.ViewHolder holder, int position) {
        float procent_pif, procent_izm;

        final PortfoliListData PortfolioData = PortfolioArray.get(position);
        Log.d("TEST","Type:"+divider_check);
        holder.nameView.setText(PortfolioData.getPifTitle());
        holder.amount.setText(String.format("%.2f", PortfolioData.getGoalAmount()) + " \u20BD");
        holder.portfolioGoalName.setText(PortfolioData.getGoalName());
        holder.progressTextView.setValue(15); // устанавливаем нужное значение
    }

    @Override
    public int getItemCount() {
        return PortfolioArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
       // final ImageView imageView;
        final TextView nameView, amount, portfolioGoalName;
        final LineralLayoutProgressBar progressTextView;
        ViewHolder(View view) {
            super(view);
            nameView = (TextView) view.findViewById(R.id.titlePortfolio);
            amount=(TextView) view.findViewById(R.id.amount);
            portfolioGoalName=(TextView) view.findViewById(R.id.portfolioGoalName);
            progressTextView = (LineralLayoutProgressBar) view.findViewById(R.id.lineralPort);
        }
        }
    }
