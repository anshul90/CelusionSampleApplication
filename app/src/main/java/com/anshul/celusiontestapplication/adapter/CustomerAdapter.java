package com.anshul.celusiontestapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anshul.celusiontestapplication.R;
import com.anshul.celusiontestapplication.Screens.OrderDetailActivity;
import com.anshul.celusiontestapplication.models.MobiInventory;

import java.util.List;

/**
 * Created by anshultyagi on 6/7/2017.
 */

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.MyViewHolder> {
    Context mContext;
    String mLatitudeLabel, mLongitudeLabel;
    private List<MobiInventory> mMobiInventoryList;

    public CustomerAdapter(List<MobiInventory> mobiInventoryModelList, Context context,
                           String mLatitudeLabel, String mLongitudeLabel) {
        mMobiInventoryList = mobiInventoryModelList;
        mContext = context;
        this.mLatitudeLabel = mLatitudeLabel;
        this.mLongitudeLabel = mLongitudeLabel;
    }

    @Override
    public CustomerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_list, parent, false);
        CustomerAdapter.MyViewHolder vh = new CustomerAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CustomerAdapter.MyViewHolder holder, final int position) {

        MobiInventory mobiInventoryModel = mMobiInventoryList.get(position);

        holder.tCompanyName.setText(mobiInventoryModel.getCompanyName());
        holder.tContactName.setText(mobiInventoryModel.getContactName());
        String sCountryName = mobiInventoryModel.getCountry();
        String sPostal = mobiInventoryModel.getPostalCode();
        holder.tCountry.setText(sCountryName + " - " + sPostal);
        //holder.tPostal.setText(mobiInventoryModel.getPostalCode());
        holder.tPhone.setText(mobiInventoryModel.getPhone());

        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(v.getContext(), OrderDetailActivity.class);
                in.putExtra("CustomerID", mMobiInventoryList.get(position).getCustomerID());
                in.putExtra("latitude", mLatitudeLabel);
                in.putExtra("longitude", mLongitudeLabel);
                v.getContext().startActivity(in);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mMobiInventoryList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tContactName, tCompanyName, tCountry,/*tPostal,*/
                tPhone;
        public LinearLayout ll;

        public MyViewHolder(View v) {
            super(v);
            tContactName = (TextView) v.findViewById(R.id.tvContactName);
            tCompanyName = (TextView) v.findViewById(R.id.tvCompanyName);
            tCountry = (TextView) v.findViewById(R.id.tvCountry);
            tPhone = (TextView) v.findViewById(R.id.tvPhone);
            ll = (LinearLayout) v.findViewById(R.id.ll);


        }


    }
}