package com.ebabu.ach.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.AQUtility;
import com.ebabu.ach.R;
import com.ebabu.ach.Utils.AppPreference;
import com.ebabu.ach.Utils.Utils;
import com.ebabu.ach.constants.IKeyConstants;
import com.ebabu.ach.constants.IUrlConstants;
import com.ebabu.ach.customview.CustomEditText;
import com.ebabu.ach.customview.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity {
    private Context context;
    private CustomEditText tvName, tvAddress, tvCity, tvState, tvCountry, tvZipcode;
    private final static String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        context = this;
        Utils.setUpToolbar(context, "Add Address", true);
        initView();
    }

    private void initView() {
        tvName = ((CustomEditText) findViewById(R.id.et_full_name));
        tvAddress = ((CustomEditText) findViewById(R.id.et_address));
        tvCountry = ((CustomEditText) findViewById(R.id.et_country));
        tvCity = ((CustomEditText) findViewById(R.id.et_city));
        tvState = ((CustomEditText) findViewById(R.id.et_state));
        tvZipcode = ((CustomEditText) findViewById(R.id.et_zip_code));

        tvName.setText(AppPreference.getInstance(context).getFirstName() + " " + AppPreference.getInstance(context).getLastName());
        tvCountry.setText(AppPreference.getInstance(context).getCountry());

        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(areFieldValid()) {
                    addAddress();
                    Intent intent1 = new Intent(context, ShippingAddressListActivity.class);
                    startActivity(intent1);
                }
            }
        });

    }

    private void addAddress() {
        Map<String, Object> params = new HashMap<>();
        params.put(IKeyConstants.NAME, tvName.getText());
        params.put(IKeyConstants.ADDRESS, tvAddress.getText());
        params.put(IKeyConstants.CITY, tvCity.getText());
        params.put(IKeyConstants.COUNTRY, tvCountry.getText());
        params.put(IKeyConstants.STATE, tvState.getText());
        params.put(IKeyConstants.ZIPCODE, tvZipcode.getText());

        Log.d(TAG, "uniqueaddAddress(): param=" + params);
        new AQuery(context).ajax(IUrlConstants.ADD_SHIPPING_ADDRESS, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                Log.d(TAG, "uniqueaddAddress(): url= " + url + "json= " + json + "Token = " + AppPreference.getInstance(context).getSecretKey());

                if (json != null) {
                    try {
                        if (IKeyConstants.SUCCESS.equalsIgnoreCase(json.getString(IKeyConstants.STATUS))) {
                            Toast.makeText(context, "Sucessfull", Toast.LENGTH_SHORT).show();
                            JSONObject data = json.getJSONObject("data");
                            AppPreference.getInstance(context).setShippingName(tvName.getText().toString());
                            AppPreference.getInstance(context).setAddressId(data.getString(IKeyConstants.ADDRESS_ID));
                            AppPreference.getInstance(context).setShippingAddress(tvAddress.getText().toString() + " " + tvCity.getText().toString() + " " + tvState.getText().toString() + " " + tvCountry.getText().toString() + " " + tvZipcode.getText().toString());
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.header(IKeyConstants.SECRETKEY, AppPreference.getInstance(context).getSecretKey()));
    }


    private Boolean areFieldValid() {
        if (tvAddress.getText().toString().trim().length() <= 0) {
            tvAddress.setError(getString(R.string.empty_address));
            return false;
        }
        if (tvCity.getText().toString().trim().length() <= 0) {
            tvCity.setError(getString(R.string.empty_city));
            return false;
        }
        if (tvCountry.getText().toString().trim().length() <= 0) {
            tvCountry.setError(getString(R.string.empty_country));
        }

        if (tvName.getText().toString().trim().length() <= 0) {
            tvName.setError(getString(R.string.empty_name));
            return false;
        }
        if (tvState.getText().toString().trim().length() <= 0) {
            tvState.setError(getString(R.string.empty_state));
            return false;
        }
        if (tvZipcode.getText().toString().trim().length() <= 0) {
            tvZipcode.setError(getString(R.string.empty_zip_code));
            return false;
        }
        return true;
    }
}

