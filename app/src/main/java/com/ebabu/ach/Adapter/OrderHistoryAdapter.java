package com.ebabu.ach.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ebabu.ach.Activity.TrackOrderActivity;
import com.ebabu.ach.Beans.OrderHistory;
import com.ebabu.ach.R;
import com.ebabu.ach.Utils.AppPreference;
import com.ebabu.ach.constants.IKeyConstants;
import com.ebabu.ach.constants.IUrlConstants;
import com.ebabu.ach.customview.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.id.input;
import static android.content.ContentValues.TAG;

/**
 * Created by HacKy on 3/2/2017.
 */

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    private Context context;
    private List<OrderHistory> orderHistoryList;

    public OrderHistoryAdapter(Context context, List<OrderHistory> orderHistoryList) {
        this.orderHistoryList = orderHistoryList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_item_order_history, parent, false);
        OrderHistoryAdapter.ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final OrderHistory orderHistory = orderHistoryList.get(position);

        if (orderHistory.getName() != null && !orderHistory.getName().isEmpty()) {
            holder.mediName.setText(Html.fromHtml(orderHistory.getName()));
        } else {
            holder.mediName.setText(IKeyConstants.NA);
        }
        if (orderHistory.getQuantity() != null && !orderHistory.getQuantity().isEmpty()) {
            holder.mediQuantity.setText(Html.fromHtml(orderHistory.getQuantity()) + " Strip(s) / Bottle(s)");
        } else {
            holder.mediQuantity.setText(IKeyConstants.NA);
        }
        if (orderHistory.getGrand_total() != null && !orderHistory.getGrand_total().isEmpty()) {
            holder.mediPrice.setText("₹ "+Html.fromHtml(orderHistory.getGrand_total()));
        } else {
            holder.mediPrice.setText(IKeyConstants.NA);
        }
        if (orderHistory.getDelivery_status() != null && !orderHistory.getDelivery_status().isEmpty()) {
            if (orderHistory.getDelivery_status().equalsIgnoreCase(IKeyConstants.PENDING)) {
                holder.mediStatus.setText(Html.fromHtml(orderHistory.getDelivery_status().toUpperCase()));
                holder.mediStatus.setTextColor(ContextCompat.getColor(context, R.color.yellow));
                holder.btnCancelOrder.setVisibility(View.VISIBLE);
                holder.itemView.findViewById(R.id.divider).setVisibility(View.VISIBLE);
            } else if (orderHistory.getDelivery_status().equalsIgnoreCase(IKeyConstants.CANCEL)) {
                holder.mediStatus.setText(Html.fromHtml(orderHistory.getDelivery_status().toUpperCase()));
                holder.mediStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
                holder.btnCancelOrder.setVisibility(View.GONE);
                holder.itemView.findViewById(R.id.divider).setVisibility(View.GONE);
            } else {
                holder.mediStatus.setText(Html.fromHtml(orderHistory.getDelivery_status().toUpperCase()));
                holder.mediStatus.setTextColor(ContextCompat.getColor(context, R.color.green));
                holder.btnCancelOrder.setVisibility(View.GONE);
                holder.itemView.findViewById(R.id.divider).setVisibility(View.GONE);
            }
        } else {
            holder.mediStatus.setText(IKeyConstants.NA);
        }
        if (orderHistory.getInvoice_no() != null && !orderHistory.getInvoice_no().isEmpty()) {
            holder.orderNumber.setText("( " + Html.fromHtml(orderHistory.getInvoice_no()) + " )");
        } else {
            holder.orderNumber.setText(IKeyConstants.NA);
        }
        if (orderHistory.getSale_datetime() != null && !orderHistory.getSale_datetime().isEmpty()) {
            holder.orderDate.setText(getDate(Long.parseLong(orderHistory.getSale_datetime()), "dd/mm/yyyy"));
        } else {
            holder.orderDate.setText(IKeyConstants.NA);
        }
        if (orderHistory.getImage() != null && orderHistory.getImage().startsWith("http")) {
            new AQuery(context).id(holder.ivMediImage).image(orderHistory.getImage());
        } else {
            holder.ivMediImage.setImageResource(R.mipmap.no_image);
        }
/*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, TrackOrderActivity.class);
                context.startActivity(intent );
            }
        });*/

        holder.btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> params = new HashMap<>();
                params.put(IKeyConstants.ORDER_ID, orderHistory.getOrder_id());
                new AQuery(context).ajax(IUrlConstants.CANCEL_ORDER, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {
                        Log.d(TAG, " canceorder(): Url=" + url + "input = " + params + "json=" + json);
                        Log.d(TAG, " canceorder(): SECRET Key = " + AppPreference.getInstance(context).getSecretKey());
                        if (json != null) {
                            try {
                                if (json.getString(IKeyConstants.STATUS).equalsIgnoreCase(IKeyConstants.SUCCESS)) {
                                    holder.btnCancelOrder.setVisibility(View.GONE);
                                    holder.mediStatus.setText(IKeyConstants.CANCEL);
                                    holder.mediStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
                                    holder.itemView.findViewById(R.id.divider).setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.header(IKeyConstants.SECRETKEY, AppPreference.getInstance(context).getSecretKey()));
            }
        });
    }



    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    @Override
    public int getItemCount() {
        return orderHistoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView btnCancelOrder, mediName, mediPrice, mediStatus, mediQuantity, orderDate, orderNumber;
        ImageView ivMediImage;

        public ViewHolder(View itemView) {
            super(itemView);
            btnCancelOrder = (CustomTextView) itemView.findViewById(R.id.btn_cancel_order);
            mediName = (CustomTextView) itemView.findViewById(R.id.txt_medicine_name);
            mediPrice = (CustomTextView) itemView.findViewById(R.id.medicine_price);
            mediQuantity = (CustomTextView) itemView.findViewById(R.id.medicine_quantity);
            mediStatus = (CustomTextView) itemView.findViewById(R.id.txt_order_status);
            orderDate = (CustomTextView) itemView.findViewById(R.id.txt_order_date);
            orderNumber = (CustomTextView) itemView.findViewById(R.id.txt_order_number);
            ivMediImage = (ImageView) itemView.findViewById(R.id.medicine_image);
        }
    }
}
