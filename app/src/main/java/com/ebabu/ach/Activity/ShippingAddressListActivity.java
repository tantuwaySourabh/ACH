package com.ebabu.ach.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ebabu.ach.Adapter.CartDetailAdapter;
import com.ebabu.ach.Adapter.ShippingAddressAdapter;
import com.ebabu.ach.Beans.CartDetail;
import com.ebabu.ach.Beans.ShippingAddress;
import com.ebabu.ach.R;
import com.ebabu.ach.Utils.AppPreference;
import com.ebabu.ach.Utils.Utils;
import com.ebabu.ach.constants.IKeyConstants;
import com.ebabu.ach.constants.IUrlConstants;
import com.ebabu.ach.customview.CustomTextView;
import com.ebabu.ach.customview.MyLoading;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ShippingAddressListActivity extends AppCompatActivity {
    private Context context;
    private LinearLayoutManager medicineLayout;
    private RecyclerView rvMedicine;
    private ShippingAddressAdapter medicineAdapter;
    private List<ShippingAddress> medicineList;
    private final static String TAG = "TAG";
    private MyLoading myLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address_list);
        context = this;
        Utils.setUpToolbar(context, "Cart", true);
        initView();
    }

    private void initView() {
        findViewById(R.id.btn_add_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddAddressActivity.class);
                startActivity(intent);
            }
        });
        myLoading = new MyLoading(context);
        rvMedicine = (RecyclerView) findViewById(R.id.rv_address_list);
        medicineList = new ArrayList<>();
        if (medicineList.size() <= 0) {
            myLoading.show("Fetching medicines");
        }
        medicineAdapter = new ShippingAddressAdapter(context, medicineList);
        medicineLayout = new LinearLayoutManager(context);
        medicineLayout.setOrientation(LinearLayoutManager.VERTICAL);
        rvMedicine.setLayoutManager(medicineLayout);
        rvMedicine.setAdapter(medicineAdapter);
        showAddress();

    }

    @Override
    protected void onResume() {
        medicineAdapter.notifyDataSetChanged();
        hideButton();
        super.onResume();
    }

    public void hideButton() {
        if (medicineList.size() == 4) {
            ((CustomTextView) findViewById(R.id.btn_add_address)).setVisibility(View.GONE);
        } else {
            ((CustomTextView) findViewById(R.id.btn_add_address)).setVisibility(View.VISIBLE);

        }
    }

    public void showAddress() {
        JSONObject input = new JSONObject();
        try {
            input.put(IKeyConstants.EMPTY, IKeyConstants.EMPTY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new AQuery(context).post(IUrlConstants.SHIPPING_ADDRESS_LIST, input, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                Log.d(TAG, "showAddress(): url= " + url + "json= " + json + "Token = " + AppPreference.getInstance(context).getSecretKey());
                try {
                    if (json != null) {
                        if (IKeyConstants.SUCCESS.equalsIgnoreCase(json.getString(IKeyConstants.STATUS))) {
                            Type type = new TypeToken<List<ShippingAddress>>() {
                            }.getType();
                            JSONArray jsonArray = null;
                            jsonArray = json.getJSONArray("data");
                            List<ShippingAddress> tempForumList = new Gson().fromJson(jsonArray.toString(), type);
                            medicineList.addAll(tempForumList);
                            hideButton();
                            medicineAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(context,"No Address found \n please Add Address",Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Log.d(TAG, "cartDetailList(): Try Nahi chala");
                    e.printStackTrace();
                }
                myLoading.dismiss();
            }
        }.header(IKeyConstants.SECRETKEY, AppPreference.getInstance(context).getSecretKey()));
    }
}

