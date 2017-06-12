package com.ebabu.ach.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ebabu.ach.Adapter.TrackOrderAdapter;
import com.ebabu.ach.Beans.TrackOrder;
import com.ebabu.ach.R;
import com.ebabu.ach.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class TrackOrderActivity extends AppCompatActivity {
    private Context context;
    private LinearLayoutManager medicineLayout;
    private RecyclerView rvMedicine;
    private TrackOrderAdapter medicineAdapter;
    private List<TrackOrder> medicineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);
        context = TrackOrderActivity.this;
        Utils.setUpToolbar(context, "My Order", true);
        initView();
    }

    private void initView() {
        rvMedicine = (RecyclerView) findViewById(R.id.rv_order_track);
        medicineList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            medicineList.add(new TrackOrder());
        }
        medicineAdapter = new TrackOrderAdapter(context, medicineList);
        medicineLayout = new LinearLayoutManager(context);
        medicineLayout.setOrientation(LinearLayoutManager.VERTICAL);
        rvMedicine.setLayoutManager(medicineLayout);
        rvMedicine.setAdapter(medicineAdapter);
    }
}
/**/