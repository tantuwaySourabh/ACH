
package com.ebabu.ach.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ebabu.ach.R;
import com.ebabu.ach.Utils.AppPreference;
import com.ebabu.ach.Utils.Utils;
import com.ebabu.ach.constants.IKeyConstants;
import com.ebabu.ach.constants.IUrlConstants;
import com.ebabu.ach.customview.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    private Context context;
    private String totalAmount, subTotalDiscountedPrice, totalMRP, totalDiscount, shippingCharge = "0";
    private CustomTextView btnAddAddress, btnChangeAddress, tvAddress, tvName;
    private CardView cardView;
    private static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        context = this;
        Utils.setUpToolbar(context, "Checkout", true);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        totalAmount = intent.getStringExtra(IKeyConstants.TOTAL);
        subTotalDiscountedPrice = intent.getStringExtra(IKeyConstants.SUBTOTALDISCOUNTEDPRICE);
        shippingCharge = intent.getStringExtra(IKeyConstants.SHIPPING_CHARGE);
        totalMRP = intent.getStringExtra(IKeyConstants.TOTALMRP);
        totalDiscount = intent.getStringExtra(IKeyConstants.TOTALDISCOUNT);
        ((CustomTextView) findViewById(R.id.txt_shipping_charge)).setText("( + ) " + "\u20b9" + shippingCharge);
        ((CustomTextView) findViewById(R.id.txt_total_discount)).setText("( - ) " + "\u20b9" + totalDiscount);
        ((CustomTextView) findViewById(R.id.txt_total_mrp)).setText("\u20b9" + totalMRP);
        ((CustomTextView) findViewById(R.id.txt_total)).setText("\u20b9" + totalAmount);
        //  ((CustomTextView) findViewById(R.id.txt_sub_total)).setText("\u20b9" + subTotalDiscountedPrice);
        ((CustomTextView) findViewById(R.id.txt_mobile)).setText(AppPreference.getInstance(context).getMobile());

        ((CustomTextView) findViewById(R.id.btn_add_address)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context, AddAddressActivity.class);
                startActivity(intent1);
            }
        });
        ((CustomTextView) findViewById(R.id.btn_change_address)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context, ShippingAddressListActivity.class);
                startActivity(intent1);
            }
        });
        ((CustomTextView) findViewById(R.id.btn_place_order)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidAddres()) {
                    placeOrder();
                }
            }
        });
        btnAddAddress = (CustomTextView) findViewById(R.id.btn_add_address);
        btnChangeAddress = (CustomTextView) findViewById(R.id.btn_change_address);
        cardView = (CardView) findViewById(R.id.card_shipping_address);
        tvName = (CustomTextView) findViewById(R.id.txt_name);
        tvAddress = (CustomTextView) findViewById(R.id.txt_address);
        checkAddress();
    }

    private void checkAddress() {
        if (AppPreference.getInstance(context).getAddressId() != null && !AppPreference.getInstance(context).getAddressId().isEmpty()) {
            tvAddress.setText(AppPreference.getInstance(context).getShippingAddress());
            tvName.setText(AppPreference.getInstance(context).getShippingName());
            btnAddAddress.setVisibility(View.GONE);
            btnChangeAddress.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.VISIBLE);
        } else {
            cardView.setVisibility(View.GONE);
            btnAddAddress.setVisibility(View.VISIBLE);
            btnChangeAddress.setVisibility(View.GONE);
        }
    }

    private Boolean isValidAddres() {

        if (cardView.getVisibility() == View.INVISIBLE || btnAddAddress.getVisibility() == View.VISIBLE) {
            Toast.makeText(context, "Address not selected", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void placeOrder() {
        String paymentMode = null, transactionId = null;

        if (((RadioButton) findViewById(R.id.payment_cod)).isChecked()) {
            paymentMode = "cash_on_delivery";
            transactionId = "0";
        } else {
            paymentMode = "online";
            transactionId = "1231";
        }
        Map<String, Object> params = new HashMap<>();
        params.put(IKeyConstants.ADDRESS_ID, AppPreference.getInstance(context).getAddressId());
        params.put(IKeyConstants.TOTAL_AMOUNT, totalMRP);
        params.put(IKeyConstants.PAY_AMOUNT, subTotalDiscountedPrice);
        params.put(IKeyConstants.SHIPPING_AMOUNT, shippingCharge);
        params.put(IKeyConstants.PAYMENT_MODE, paymentMode);
        params.put(IKeyConstants.TRANSACTION_ID, transactionId);

        new AQuery(context).ajax(IUrlConstants.CART_CHECKOUT, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                Log.d(TAG, "placeOrder(): params= " + params + " json= " + json);
                if (json != null) {
                    try {
                        if (IKeyConstants.SUCCESS.equalsIgnoreCase(json.getString(IKeyConstants.STATUS))) {
                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            AppPreference.getInstance(context).setCounter(0);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.header(IKeyConstants.SECRETKEY, AppPreference.getInstance(context).getSecretKey()));
    }

    @Override
    protected void onResume() {
        checkAddress();
        super.onResume();
    }
}
