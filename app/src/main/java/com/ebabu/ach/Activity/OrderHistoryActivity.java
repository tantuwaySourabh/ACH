package com.ebabu.ach.Activity;

import android.content.Context;
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
import com.ebabu.ach.Adapter.OrderHistoryAdapter;
import com.ebabu.ach.Beans.OrderHistory;
import com.ebabu.ach.Beans.OrderMedicine;
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
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderHistoryActivity extends AppCompatActivity {
    private Context context;
    private RecyclerView rvOrderHistory;
    private RecyclerView.LayoutManager orderLayoutManager;
    private OrderHistoryAdapter orderHistoryAdapter;
    private List<OrderHistory> orderHistoryList;
    private MyLoading myLoading;
    private String create_at = "0";
    private static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        context = this;
        Utils.setUpToolbar(context, "Order", true);
        initView();
    }

    public void initView() {
        myLoading = new MyLoading(context);
        rvOrderHistory = (RecyclerView) findViewById(R.id.rv_order_history);
        orderHistoryList = new ArrayList<>();
       /* for (int i = 0; i < 6; i++) {
            orderHistoryList.add(new OrderHistory());
        }*/
        orderHistoryAdapter = new OrderHistoryAdapter(context, orderHistoryList);
        if (orderHistoryList.size() <= 0) {
            myLoading.show("Proccessing");
        }
        orderLayoutManager = new LinearLayoutManager(context);
        rvOrderHistory.setLayoutManager(orderLayoutManager);
        rvOrderHistory.setAdapter(orderHistoryAdapter);
        getOrderList();
    }

    private void getOrderList() {
        Map<String, Object> param = new HashMap<>();
        param.put("create_at", create_at);
        new AQuery(context).ajax(IUrlConstants.ORDER_HISTORY, param, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                Log.d(TAG, "getOrderList(): url=" + url + ", json=" + json + ", status=" + status.getCode());
                try {
                    if (json != null) {
                        if (IKeyConstants.SUCCESS.equalsIgnoreCase(json.getString(IKeyConstants.STATUS))) {
                            Type type = new TypeToken<List<OrderHistory>>() {
                            }.getType();
                            JSONArray jsonArray = json.getJSONArray("data");
                            List<OrderHistory> tempForumList = new Gson().fromJson(jsonArray.toString(), type);
                            orderHistoryList.addAll(tempForumList);
                            myLoading.dismiss();
                            orderHistoryAdapter.notifyDataSetChanged();
                            ((CustomTextView)findViewById(R.id.no_order_image_view)).setVisibility(View.GONE);
                        }
                        else {
                            Toast.makeText(context,"No previous Order",Toast.LENGTH_SHORT).show();

                            ((CustomTextView)findViewById(R.id.no_order_image_view)).setVisibility(View.VISIBLE);
                            myLoading.dismiss();
                        }
                    }
                } catch (Exception e) {
                }
            }
        }.header(IKeyConstants.SECRETKEY, AppPreference.getInstance(context).getSecretKey()));
    }
}
