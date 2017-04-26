package com.example.perfecto.tipcalculator;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.perfecto.tipcalculator.api.model.Tip;

import java.util.List;

/**
 * Created by paulb on 4/24/17.
 */

public class TipAdapter extends RecyclerView.Adapter<TipAdapter.TipViewHolder> {
    private List<Tip> tips;
    private int rowLayout;
    private Context context;

    public TipAdapter(List<Tip> tips, int rowLayout, Context context) {
        this.tips = tips;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    public static class TipViewHolder extends RecyclerView.ViewHolder {
        TextView li_subtotal;
        TextView li_percent;
        TextView li_split;

        public TipViewHolder(View v) {
            super(v);
            li_subtotal = (TextView) v.findViewById(R.id.li_subtotal);
            li_percent = (TextView) v.findViewById(R.id.li_percent);
            li_split = (TextView) v.findViewById(R.id.li_split);
        }
    }

    @Override
    public TipAdapter.TipViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new TipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TipViewHolder holder, final int position) {
        holder.li_subtotal.setText(String.valueOf(String.format("%.2f", tips.get(position).getSubtotal())));
        holder.li_percent.setText(String.valueOf(String.format("%.0f", tips.get(position).getPercent())));
        holder.li_split.setText(String.valueOf(String.format("%s", tips.get(position).getSplit())));
    }

    @Override
    public int getItemCount() {
        return tips.size();
    }
}
