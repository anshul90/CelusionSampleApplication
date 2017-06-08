package com.anshul.celusiontestapplication.Screens;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.anshul.celusiontestapplication.R;
import com.anshul.celusiontestapplication.adapter.OrderAdapter;
import com.anshul.celusiontestapplication.database.DatabaseHandler;
import com.anshul.celusiontestapplication.models.MobiOrderDetail;
import com.anshul.celusiontestapplication.utils.AppUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anshultyagi on 6/7/2017.
 */

public class OrderDetailActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    String CustomerId;
    MobiOrderDetail mobiOrderDetail;
    DatabaseHandler databaseHandler;
    ProgressDialog pDialogVolley;
    private RecyclerView mRecyclerView;
    private OrderAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<MobiOrderDetail> mobiOrderDetailList = new ArrayList<>();
    private TextView empty_textView;
    private String mLatitudeLabel;
    private String mLongitudeLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details_activity);
        requestQueue = Volley.newRequestQueue(this);
        empty_textView = (TextView) findViewById(R.id.empty_text);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_Order);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        databaseHandler = new DatabaseHandler(OrderDetailActivity.this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            CustomerId = bundle.getString("CustomerID");
            mLatitudeLabel = bundle.getString("latitude");
            mLongitudeLabel = bundle.getString("longitude");
        }

        if (AppUtils.hasNetworkConnection(this)) {
            requestVolleyToGetOrderDetails(AppUtils.AppABaseUrl + "Order/" + CustomerId);
        } else {
            Toast.makeText(this, "There is no internet connection.", Toast.LENGTH_SHORT).show();
            showDbData();
        }
    }

    public void showDbData() {
        if (mobiOrderDetailList == null) {
            mobiOrderDetailList = new ArrayList<>();
        } else {
            mobiOrderDetailList.clear();
        }
        Cursor cursor = databaseHandler.getOrdersData("SELECT * FROM " + "orders_table"
                        + " where member_id=?",
                CustomerId);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                mobiOrderDetail = new MobiOrderDetail();
                mobiOrderDetail.setShipCountry(cursor.getString(3));
                mobiOrderDetail.setOrderID(cursor.getString(1));
                mobiOrderDetail.setOrderDate(cursor.getString(4));
                mobiOrderDetail.setShipAddress(cursor.getString(2));
                mobiOrderDetailList.add(mobiOrderDetail);
            }
            mAdapter = new OrderAdapter(mobiOrderDetailList, OrderDetailActivity.this);
            mAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(mAdapter);
            empty_textView.setVisibility(View.GONE);
        } else {
            empty_textView.setVisibility(View.VISIBLE);
        }
    }

    private void requestVolleyToGetOrderDetails(String url) {
        pDialogVolley = new ProgressDialog(this);
        if (AppUtils.hasNetworkConnection(this)) {
            pDialogVolley.setMessage("please_wait");
        } else {
            pDialogVolley.setMessage("please_wait_low_connectivity");

        }
        pDialogVolley.show();
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        pDialogVolley.hide();
                        if (response != null && response.length() > 0) {
                            // MobiInventory feedListBean = new Gson().fromJson(String.valueOf(response), MobiInventory.class);
                            Type listType = new TypeToken<List<MobiOrderDetail>>() {
                            }.getType();
                            mobiOrderDetailList = new Gson().fromJson(String.valueOf(response), listType);
                            mAdapter = new OrderAdapter(mobiOrderDetailList, OrderDetailActivity.this);
                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.setAdapter(mAdapter);
                            if (mobiOrderDetailList.size() > 0) {
                                empty_textView.setVisibility(View.GONE);
                                //databaseHandler.deleteOrders(OrderDetailActivity.this);
                                for (int i = 0; i < mobiOrderDetailList.size(); i++) {
                                    databaseHandler.insertNewOrder(CustomerId,
                                            mobiOrderDetailList.get(i).getOrderID(),
                                            mobiOrderDetailList.get(i).getOrderDate(),
                                            mobiOrderDetailList.get(i).getShipCountry(),
                                            mobiOrderDetailList.get(i).getShipAddress()
                                    );
                                }
                            } else {
                                empty_textView.setVisibility(View.VISIBLE);
                            }
                        } else {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialogVolley.hide();
                        String json = null;
                        //8888 String errorMsg = error.networkResponse.data;
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            switch (response.statusCode) {
                                case 401:
                                    json = new String(response.data);
                                    json = trimMessage(json, "errors");
                                    if (json != null) displayMessage(json);
                                    break;
                            }
                            //Additional cases
                        }
                    }

                    public String trimMessage(String json, String key) {
                        String trimmedString = null;

                        try {
                            JSONObject obj = new JSONObject(json);
                            JSONArray jsonArray = obj.getJSONArray(key);
                            JSONObject errorJsonObject = jsonArray.getJSONObject(0);
                            trimmedString = errorJsonObject.getString("errorMessage");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            return null;
                        }

                        return trimmedString;
                    }

                    //Somewhere that has access to a context
                    public void displayMessage(String toastString) {
                        Toast.makeText(OrderDetailActivity.this, toastString, Toast.LENGTH_LONG).show();
//                        Log.v("SERVER RESPONSE", toastString);

                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<String, String>();
                header.put("Content-Type", "application/json; charset=utf-8");

                String un = "admin";
                String pw = "admin";
                String credentials = un + ":" + pw;
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                header.put("lat", mLatitudeLabel);
                header.put("long", mLongitudeLabel);
                header.put("Authorization", auth);
                return header;
            }

            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }
        };
        requestQueue.add(jsObjRequest);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pDialogVolley != null && pDialogVolley.isShowing()) {
            pDialogVolley.cancel();
        }
    }
}
