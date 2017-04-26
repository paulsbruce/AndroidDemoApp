package com.example.perfecto.tipcalculator;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.perfecto.tipcalculator.TipCalcFragment.OnListFragmentInteractionListener;
import com.example.perfecto.tipcalculator.api.model.Tip;

import java.util.List;

public class MyTipCalcRecyclerViewAdapter extends RecyclerView.Adapter<MyTipCalcRecyclerViewAdapter.TipViewHolder> {

    private final List<Tip> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyTipCalcRecyclerViewAdapter(List<Tip> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public TipViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tipcalc, parent, false);
        return new TipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TipViewHolder holder, int position) {
        holder.tip = mValues.get(position);
        holder.li_subtotal.setText(mValues.get(position).getSubtotal().toString());
        holder.li_percent.setText(mValues.get(position).getPercent().toString());
        holder.li_split.setText(mValues.get(position).getSplit());

        holder.li_tip_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.tip);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class TipViewHolder extends RecyclerView.ViewHolder {

        Tip tip;

        GridLayout li_tip_container;
        TextView li_subtotal;
        TextView li_percent;
        TextView li_split;

        public TipViewHolder(View v) {
            super(v);
            li_tip_container = (GridLayout) v.findViewById(R.id.li_tip_container);
            li_subtotal = (TextView) v.findViewById(R.id.li_subtotal);
            li_percent = (TextView) v.findViewById(R.id.li_percent);
            li_split = (TextView) v.findViewById(R.id.li_split);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + li_subtotal.getText() + "'";
        }
    }
}
