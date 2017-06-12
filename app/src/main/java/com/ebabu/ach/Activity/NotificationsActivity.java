
package com.ebabu.ach.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ebabu.ach.Adapter.MedicineListAdapter;
import com.ebabu.ach.Adapter.NotificationsAdapter;
import com.ebabu.ach.Beans.Notifications;
import com.ebabu.ach.Beans.OrderMedicine;
import com.ebabu.ach.R;
import com.ebabu.ach.Utils.AppPreference;
import com.ebabu.ach.Utils.Utils;
import com.ebabu.ach.constants.IKeyConstants;
import com.ebabu.ach.constants.IUrlConstants;
import com.ebabu.ach.customview.MyLoading;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {
    private final static String TAG = "TAG";
    private Context context;
    private LinearLayoutManager medicineLayout;
    private RecyclerView rvMedicine;
    private NotificationsAdapter medicineAdapter;
    private List<Notifications> medicineList;
    private MyLoading myLoading;
    private boolean loading = true;
    private boolean areMoreMedicine = true;
    private int previousDataSize = 0;
    private String created_at = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        context = this;
        Utils.setUpToolbar(context,"Notifications",true);
        initView();
    }

    private void initView() {
        myLoading = new MyLoading(context);
        rvMedicine = (RecyclerView) findViewById(R.id.rv_notifications);
        medicineList = new ArrayList<>();
        if (medicineList.size() <= 0) {
            myLoading.show("Fetching medicines");
        }
        medicineAdapter = new NotificationsAdapter(context, medicineList);
        medicineLayout = new LinearLayoutManager(context);
        medicineLayout.setOrientation(LinearLayoutManager.VERTICAL);
        rvMedicine.setLayoutManager(medicineLayout);
        rvMedicine.setAdapter(medicineAdapter);
        fetchNotifications();
    }

    private void fetchNotifications() {
        if (areMoreMedicine) {
            JSONObject input = new JSONObject();
            try {
                input.put("create_at", created_at);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            new AQuery(context).post(IUrlConstants.NOTIFICATIONS, input, JSONObject.class, new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    Log.d(TAG, "fetchNotifications(): url=" + url + ", json=" + ", secret key =" + AppPreference.getInstance(context).getSecretKey());
                    try {
                        if (json != null) {
                            if (IKeyConstants.SUCCESS.equalsIgnoreCase(json.getString(IKeyConstants.STATUS))) {
                                Type type = new TypeToken<List<Notifications>>() {
                                }.getType();
                                JSONArray jsonArray = json.getJSONArray("data");
                                List<Notifications> tempForumList = new Gson().fromJson(jsonArray.toString(), type);
                                if (tempForumList.size() > 0) {
                                    medicineList.addAll(previousDataSize, tempForumList);
                                    previousDataSize = medicineList.size();
                                    medicineAdapter.notifyDataSetChanged();
                                    created_at = tempForumList.get(tempForumList.size() - 1).getCreate_at();
                                    areMoreMedicine = true;
                                }
                            } else {
                                areMoreMedicine = false;
                                Toast.makeText(context, json.getString(IKeyConstants.MESSAGE), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(context, getString(R.string.unexpected_error), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, getString(R.string.unexpected_error), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    loading = false;
                    myLoading.dismiss();
                }
            }.header(IKeyConstants.SECRETKEY, AppPreference.getInstance(context).getSecretKey()));
        } else {
            myLoading.dismiss();
        }
    }
}
