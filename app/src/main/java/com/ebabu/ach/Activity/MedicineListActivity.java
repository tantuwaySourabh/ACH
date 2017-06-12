package com.ebabu.ach.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ebabu.ach.Adapter.MedicineListAdapter;
import com.ebabu.ach.Beans.OrderMedicine;
import com.ebabu.ach.R;
import com.ebabu.ach.Utils.AppPreference;
import com.ebabu.ach.Utils.Utils;
import com.ebabu.ach.constants.IKeyConstants;
import com.ebabu.ach.constants.IUrlConstants;
import com.ebabu.ach.customview.CustomEditText;
import com.ebabu.ach.customview.MyLoading;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MedicineListActivity extends AppCompatActivity {
    private final static String TAG = "TAG";
    private Context context;
    private LinearLayoutManager medicineLayout;
    private RecyclerView rvMedicine;
    private MedicineListAdapter medicineAdapter;
    private List<OrderMedicine> medicineList;
    private String searchText;
    private MyLoading myLoading;
    private MenuItem actionCart;
    private boolean loading = true;
    private boolean areMoreMedicine = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_medicine);
        context = MedicineListActivity.this;
        Utils.setUpToolbar(context, "Medicines", true);
        initView();
        addPaginationInRecyclerView();
    }

    private int previousDataSize = 0;


    private String created_at = "0";

    @Override
    protected void onResume() {
        if(actionCart != null)
            actionCart.setIcon(Utils.getInstance(context).buildCounterDrawable(AppPreference.getInstance(context).getCounter(), R.mipmap.cart));
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_order_medicine, menu);
        actionCart = menu.findItem(R.id.action_cart);
        actionCart.setIcon(Utils.getInstance(context).buildCounterDrawable(AppPreference.getInstance(context).getCounter(), R.mipmap.cart));
        return true;
    }

    private void initView() {
        Log.d(TAG, "mobiletoken=" + AppPreference.getInstance(context).getSecretKey());
        myLoading = new MyLoading(context);
        Intent intent = getIntent();
        searchText = intent.getStringExtra(IKeyConstants.SEARCH_TEXT);
        rvMedicine = (RecyclerView) findViewById(R.id.rv_medicine);
        actionCart = (MenuItem) findViewById(R.id.action_cart);
        medicineList = new ArrayList<>();
        if (medicineList.size() <= 0) {
            myLoading.show("Fetching medicines");
        }
        ((CustomEditText) findViewById(R.id.et_search_text_medicine)).setText(searchText);
        medicineAdapter = new MedicineListAdapter(context, medicineList);
        medicineLayout = new LinearLayoutManager(context);
        medicineLayout.setOrientation(LinearLayoutManager.VERTICAL);
        rvMedicine.setLayoutManager(medicineLayout);
        rvMedicine.setAdapter(medicineAdapter);
        fetchMedicineList();
        ((CustomEditText) findViewById(R.id.et_search_text_medicine)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                medicineList.clear();
                previousDataSize = 0;
                created_at = "0";
                if(s.length() >= 3) {
                    searchText = s.toString();
                    fetchMedicineList();
                    Log.d("on change", searchText);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void setCartIcon(){
        actionCart.setIcon(Utils.getInstance(context).buildCounterDrawable(AppPreference.getInstance(context).getCounter(), R.mipmap.cart));

    }
    private void fetchMedicineList() {
        Log.d("fetch", "true");
        JSONObject input = new JSONObject();
        try {
            input.put("create_at", created_at);
            input.put("search_key", searchText);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        loading = true;
        new AQuery(context).post(IUrlConstants.SEARCH_MEDICINE, input, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                Log.d(TAG, "fetchMedicineList(): url=" + url + ", json=" + ", secret key =" + AppPreference.getInstance(context).getSecretKey());
                try {
                    if (json != null) {
                        if (IKeyConstants.SUCCESS.equalsIgnoreCase(json.getString(IKeyConstants.STATUS))) {
                            Type type = new TypeToken<List<OrderMedicine>>() {
                            }.getType();
                            JSONArray jsonArray = null;
                            jsonArray = json.getJSONArray("data");
                            List<OrderMedicine> tempForumList = new Gson().fromJson(jsonArray.toString(), type);
                            if (tempForumList.size() > 0) {
                                medicineList.addAll(previousDataSize, tempForumList);
                                previousDataSize = medicineList.size();
                                medicineAdapter.notifyDataSetChanged();
                                created_at = tempForumList.get(tempForumList.size() - 1).getCreate_at();
                                //areMoreMedicine = true;
                            }
                        } else {
                            //  areMoreMedicine = false;
                            Toast.makeText(context, json.getString(IKeyConstants.MESSAGE), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        medicineAdapter.notifyDataSetChanged();
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
    }

    private void addPaginationInRecyclerView() {

        rvMedicine.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (dy > 0) //check for scroll down
                {

                    visibleItemCount = medicineLayout.getChildCount();
                    totalItemCount = medicineLayout.getItemCount();
                    pastVisiblesItems = medicineLayout.findFirstVisibleItemPosition();
/**/
                    if (!loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            Log.d("TAG", "visibleItemCount: " + visibleItemCount + ", totalItemCount: " + totalItemCount + ", pastVisiblesItems: " + pastVisiblesItems);

                            Log.v("...", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                            fetchMedicineList();
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.action_cart:
                intent = new Intent(context, CartDetailActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}


