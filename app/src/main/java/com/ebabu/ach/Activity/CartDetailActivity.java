package com.ebabu.ach.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ebabu.ach.Adapter.CartDetailAdapter;
import com.ebabu.ach.Beans.CartDetail;
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

public class CartDetailActivity extends AppCompatActivity {
    private Context context;
    private LinearLayoutManager medicineLayout;
    private RecyclerView rvMedicine;
    private CartDetailAdapter medicineAdapter;
    private List<CartDetail> medicineList;
    private final static String TAG = "TAG";
    private MyLoading myLoading;
    private float subTotalDiscountedPrice = 0, shippingCharge = 0, Total, totalMRP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_detail);
        context = CartDetailActivity.this;
        Utils.setUpToolbar(context, "Cart", true);
        initView();
    }

    private void initView() {
        myLoading = new MyLoading(context);
        findViewById(R.id.btn_checkout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CheckoutActivity.class);
                intent.putExtra(IKeyConstants.SUBTOTALDISCOUNTEDPRICE, String.valueOf(subTotalDiscountedPrice));
                intent.putExtra(IKeyConstants.TOTAL, String.valueOf(Total));
                intent.putExtra(IKeyConstants.TOTALDISCOUNT, String.valueOf(totalMRP - subTotalDiscountedPrice));
                intent.putExtra(IKeyConstants.TOTALMRP, String.valueOf(totalMRP));
                intent.putExtra(IKeyConstants.SHIPPING_CHARGE, String.valueOf(shippingCharge));
                startActivity(intent);
            }
        });
        rvMedicine = (RecyclerView) findViewById(R.id.rv_cart);
        medicineList = new ArrayList<>();
        if (medicineList.size() <= 0) {
            myLoading.show("Fetching medicines");
        }
        medicineAdapter = new CartDetailAdapter(context, medicineList);
        medicineLayout = new LinearLayoutManager(context);
        medicineLayout.setOrientation(LinearLayoutManager.VERTICAL);
        rvMedicine.setLayoutManager(medicineLayout);
        rvMedicine.setAdapter(medicineAdapter);
        CartDetailList();
    }

    public void showCharge() {
        shippingCharge = 0;
        totalMRP = 0;
        subTotalDiscountedPrice = 0;
        Total = 0;
        if (medicineList.size() > 0) {
            findViewById(R.id.card_view).setVisibility(View.VISIBLE);
            findViewById(R.id.btn_checkout).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.btn_checkout).setVisibility(View.INVISIBLE);
            findViewById(R.id.card_view).setVisibility(View.INVISIBLE);
            Toast.makeText(context, "No item in cart", Toast.LENGTH_SHORT).show();
            ((CustomTextView)findViewById(R.id.empty_cart_image_view)).setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < medicineList.size(); i++) {
            totalMRP = totalMRP + Float.parseFloat(medicineList.get(i).getMedicine_detail().getPrice()) * medicineList.get(i).getMedicine_quantity();
            subTotalDiscountedPrice = subTotalDiscountedPrice + Float.parseFloat(medicineList.get(i).getMedicine_detail().getSale_price()) * medicineList.get(i).getMedicine_quantity();

        }
        for (int i = 0; i < medicineList.size(); i++) {
            shippingCharge = shippingCharge + Float.valueOf(medicineList.get(i).getMedicine_detail().getShipping());
        }
        Total = subTotalDiscountedPrice + this.shippingCharge;
        ((CustomTextView) findViewById(R.id.txt_total_mrp)).setText("₹ " + String.valueOf(totalMRP));
        ((CustomTextView) findViewById(R.id.txt_total_discount)).setText("( - ) ₹ " + String.valueOf(totalMRP - subTotalDiscountedPrice));
        //((CustomTextView) findViewById(R.id.txt_sub_total)).setText("₹ " + String.valueOf(subTotalDiscountedPrice));
        ((CustomTextView) findViewById(R.id.txt_shipping_charge)).setText("( + ) ₹ " + String.valueOf(shippingCharge));
        ((CustomTextView) findViewById(R.id.txt_total)).setText("₹ " + String.valueOf(Total));

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void CartDetailList() {
        JSONObject input = new JSONObject();
        try {
            input.put(IKeyConstants.EMPTY, IKeyConstants.EMPTY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new AQuery(context).post(IUrlConstants.CART_DETAIL, input, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                Log.d(TAG, "cartDetailList(): url= " + url + "json= " + json + "Token = " + AppPreference.getInstance(context).getSecretKey());
                try {
                    if (json != null) {
                        if (IKeyConstants.SUCCESS.equalsIgnoreCase(json.getString(IKeyConstants.STATUS))) {
                            Type type = new TypeToken<List<CartDetail>>() {
                            }.getType();
                            JSONArray jsonArray = null;
                            jsonArray = json.getJSONArray("data");
                            List<CartDetail> tempForumList = new Gson().fromJson(jsonArray.toString(), type);
                            AppPreference.getInstance(context).setCounter(json.getInt(IKeyConstants.CARTCOUNT));
                            medicineList.addAll(tempForumList);
                            medicineAdapter.notifyDataSetChanged();

                            showCharge();

                            ((CustomTextView)findViewById(R.id.empty_cart_image_view)).setVisibility(View.GONE);
                        }
                        else
                        {
                            if (IKeyConstants.INVALID_TOKEN.equalsIgnoreCase(json.getString(IKeyConstants.MESSAGE)))
                            {

                            }
                            if (medicineList.size() == 0)
                            {
                                Toast.makeText(context,"Empty Cart",Toast.LENGTH_SHORT).show();
                                AppPreference.getInstance(context).setCounter(0);
                                ((CustomTextView)findViewById(R.id.empty_cart_image_view)).setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.d(TAG, "cartDetailList(): Try Nahi chala");
                    Toast.makeText(context,"Loging Out",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    AppPreference.getInstance(context).clear();
                    e.printStackTrace();
                }
                myLoading.dismiss();
            }
        }.header(IKeyConstants.SECRETKEY, AppPreference.getInstance(context).getSecretKey()));
    }
}
