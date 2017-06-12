package com.ebabu.ach.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ebabu.ach.Beans.TrackOrder;
import com.ebabu.ach.R;

import java.util.List;

/**
 * Created by Sahitya on 2/18/2017.
 */

public class TrackOrderAdapter extends RecyclerView.Adapter<TrackOrderAdapter.ViewHolder> {
    private Context context;
    private List<TrackOrder> medicineList;

    public TrackOrderAdapter(Context context, List<TrackOrder> medicineList) {
        this.context = context;
        this.medicineList = medicineList;

    }

    @Override
    public TrackOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_item_track_order, parent, false);
        TrackOrderAdapter.ViewHolder vh = new TrackOrderAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(TrackOrderAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}