package com.anshul.celusiontestapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anshul.celusiontestapplication.R;
import com.anshul.celusiontestapplication.models.MobiOrderDetail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


/**
 * Created by anshultyagi on 6/7/2017.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    Context mContext;
    private List<MobiOrderDetail> mMobiInventoryList;

    public OrderAdapter(List<MobiOrderDetail> mobiInventoryModelList, Context context) {
        mMobiInventoryList = mobiInventoryModelList;
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_list, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        MobiOrderDetail mobiInventoryModel = mMobiInventoryList.get(position);
        if (mobiInventoryModel.getOrderID() == null) {
            holder.tvOrderId.setText("");
        } else
            holder.tvOrderId.setText(mobiInventoryModel.getOrderID());
        holder.tvDate.setText(changeDate(mobiInventoryModel.getOrderDate()));
        holder.tCountry.setText(mobiInventoryModel.getShipCountry());
        holder.tvShipAdd.setText(mobiInventoryModel.getShipAddress());
    }

    private String changeDate(String OurDate) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(OurDate);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd"); //this format is changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            OurDate = dateFormatter.format(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OurDate;
    }

    @Override
    public int getItemCount() {
        return mMobiInventoryList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvOrderId, tvDate, tCountry,
                tvShipAdd;
        public LinearLayout ll;

        public MyViewHolder(View v) {
            super(v);
            tvOrderId = (TextView) v.findViewById(R.id.tvOrderId);
            tvDate = (TextView) v.findViewById(R.id.tvDate);
            tCountry = (TextView) v.findViewById(R.id.tvShipCountry);
            tvShipAdd = (TextView) v.findViewById(R.id.tvShipAdd);
            ll = (LinearLayout) v.findViewById(R.id.ll);
        }

    }
}