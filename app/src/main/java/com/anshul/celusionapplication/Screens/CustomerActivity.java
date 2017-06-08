package com.anshul.celusionapplication.Screens;

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
import com.anshul.celusionapplication.R;
import com.anshul.celusionapplication.adapter.CustomerAdapter;
import com.anshul.celusionapplication.database.DatabaseHandler;
import com.anshul.celusionapplication.models.MobiInventory;
import com.anshul.celusionapplication.utils.AppUtils;
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

public class CustomerActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    DatabaseHandler databaseHandler;
    MobiInventory MobiInventory;
    private RecyclerView mRecyclerView;
    private CustomerAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<MobiInventory> customerModelList = new ArrayList<>();
    private TextView empty_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        requestQueue = Volley.newRequestQueue(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_Customer);
        empty_textView = (TextView) findViewById(R.id.empty_text);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        databaseHandler = new DatabaseHandler(CustomerActivity.this);

        if (AppUtils.hasNetworkConnection(this)) {
            requestVolleyToGetCustomerDetails(AppUtils.AppABaseUrl + "customer");
        } else {
            Toast.makeText(this, "There is no internet connection.", Toast.LENGTH_SHORT).show();
            showDbData();
        }
    }

    public void showDbData() {
        if (customerModelList == null) {
            customerModelList = new ArrayList<>();
        } else {
            customerModelList.clear();
        }
        Cursor cursor = databaseHandler.getMembersData("SELECT * FROM " + "customers_table");
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                MobiInventory = new MobiInventory();
                MobiInventory.setCompanyName(cursor.getString(3));
                MobiInventory.setCustomerID(cursor.getString(1));
                MobiInventory.setContactName(cursor.getString(2));
                MobiInventory.setPhone(cursor.getString(4));
                MobiInventory.setPostalCode(cursor.getString(5));
                customerModelList.add(MobiInventory);
            }
            mAdapter = new CustomerAdapter(customerModelList, CustomerActivity.this);
            mAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(mAdapter);
            empty_textView.setVisibility(View.GONE);
        } else {
            empty_textView.setVisibility(View.VISIBLE);
        }
    }

    private void requestVolleyToGetCustomerDetails(String url) {

        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if (response != null && response.length() > 0) {
                            // MobiInventory feedListBean = new Gson().fromJson(String.valueOf(response), MobiInventory.class);
                            Type listType = new TypeToken<List<MobiInventory>>() {
                            }.getType();
                            customerModelList = new Gson().fromJson(String.valueOf(response), listType);
                            mAdapter = new CustomerAdapter(customerModelList, CustomerActivity.this);
                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.setAdapter(mAdapter);
                            if (customerModelList.size() > 0) {
                                empty_textView.setVisibility(View.GONE);
                                databaseHandler.deleteMembers(CustomerActivity.this);
                                for (int i = 0; i < customerModelList.size(); i++) {
                                    databaseHandler.insertNewCustomer(
                                            customerModelList.get(i).getCustomerID(),
                                            customerModelList.get(i).getContactName(),
                                            customerModelList.get(i).getCompanyName(),
                                            customerModelList.get(i).getCountry(),
                                            customerModelList.get(i).getPhone(),
                                            customerModelList.get(i).getPostalCode()
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
                        Toast.makeText(CustomerActivity.this, toastString, Toast.LENGTH_LONG).show();
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
                header.put("lat", "12.971599");
                header.put("long", "77.594563");
                header.put("Authorization", auth);
                return header;
            }

            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                //mStatusCode = response.statusCode;
//                Log.i("statusCode", String.valueOf(mStatusCode));
                return super.parseNetworkResponse(response);
            }
        };
        requestQueue.add(jsObjRequest);

    }
}
