package com.anshul.celusionapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anshul.celusionapplication.R;
import com.anshul.celusionapplication.models.MobiOrderDetail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    public OrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_list, parent, false);
        OrderAdapter.MyViewHolder vh = new OrderAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(OrderAdapter.MyViewHolder holder, final int position) {

        MobiOrderDetail mobiInventoryModel = mMobiInventoryList.get(position);
        if (mobiInventoryModel.getOrderID() == null) {
            holder.tvOrderId.setText("");
        } else
            holder.tvOrderId.setText(mobiInventoryModel.getOrderID());
        //holder.tvDate.setText(mobiInventoryModel.getOrderDate());
        holder.tvDate.setText(getDate(mobiInventoryModel.getOrderDate()));
        holder.tCountry.setText(mobiInventoryModel.getShipCountry());
        holder.tvShipAdd.setText(mobiInventoryModel.getShipAddress());
    }

    public String getDate(String inputString) {
        Date date = null;
        String outputString = "";
        try {
            // Convert input string into a date
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            date = inputFormat.parse(inputString);
        } catch (Exception e) {
            e.printStackTrace();
        }
// Format date into output format
        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        if (date != null) {
            outputString = outputFormat.format(date);
        }
        return outputString;
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