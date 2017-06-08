package com.anshul.celusiontestapplication.Screens;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
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
import com.anshul.celusiontestapplication.adapter.CustomerAdapter;
import com.anshul.celusiontestapplication.database.DatabaseHandler;
import com.anshul.celusiontestapplication.models.MobiInventory;
import com.anshul.celusiontestapplication.utils.AppUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final String TAG = CustomerActivity.class.getSimpleName();
    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;
    RequestQueue requestQueue;
    DatabaseHandler databaseHandler;
    MobiInventory mobiInventory;
    double longitude;
    double latitude;
    ProgressDialog pDialogVolley;
    /**
     * Provides the entry point to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;
    private String mLatitudeLabel;
    private String mLongitudeLabel;
    private RecyclerView mRecyclerView;
    private CustomerAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<MobiInventory> customerModelList = new ArrayList<>();
    private TextView empty_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        requestQueue = Volley.newRequestQueue(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_Customer);
        empty_textView = (TextView) findViewById(R.id.empty_text);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        databaseHandler = new DatabaseHandler(CustomerActivity.this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getLastLocation();
        }
    }

    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();
                            mLatitudeLabel = String.valueOf(mLastLocation.getLatitude());
                            mLongitudeLabel = String.valueOf(mLastLocation.getLongitude());
                            if (AppUtils.hasNetworkConnection(CustomerActivity.this)) {
                                if (customerModelList.size() == 0)
                                    requestVolleyToGetCustomerDetails(AppUtils.AppABaseUrl + "customer");
                            } else {
                                Toast.makeText(CustomerActivity.this, "There is no internet connection.", Toast.LENGTH_SHORT).show();
                                showDbData();
                            }

                        } else {
                            Log.w("TAG", "getLastLocation:exception", task.getException());
                        }
                    }
                });
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(CustomerActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i("TAG", "Displaying permission rationale to provide additional context.");
            // Request permission
            startLocationPermissionRequest();
        } else {
            Log.i("TAG", "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest();
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i("TAG", "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i("TAG", "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation();
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.

            }
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
                mobiInventory = new MobiInventory();
                mobiInventory.setCompanyName(cursor.getString(3));
                mobiInventory.setCustomerID(cursor.getString(1));
                mobiInventory.setContactName(cursor.getString(2));
                mobiInventory.setCountry(cursor.getString(4));
                mobiInventory.setPhone(cursor.getString(5));
                mobiInventory.setPostalCode(cursor.getString(6));
                customerModelList.add(mobiInventory);
            }
            mAdapter = new CustomerAdapter(customerModelList, CustomerActivity.this,
                    mLatitudeLabel, mLongitudeLabel);
            mAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(mAdapter);
            empty_textView.setVisibility(View.GONE);
        } else {
            empty_textView.setVisibility(View.VISIBLE);
        }
    }

    private void requestVolleyToGetCustomerDetails(String url) {
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
                            Type listType = new TypeToken<List<MobiInventory>>() {
                            }.getType();
                            customerModelList = new Gson().fromJson(String.valueOf(response), listType);
                            mAdapter = new CustomerAdapter(customerModelList, CustomerActivity.this,
                                    mLatitudeLabel, mLongitudeLabel);
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
                        Toast.makeText(CustomerActivity.this, toastString, Toast.LENGTH_LONG).show();
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
