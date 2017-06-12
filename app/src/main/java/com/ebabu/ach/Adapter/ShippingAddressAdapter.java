package com.ebabu.ach.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ebabu.ach.Activity.ShippingAddressListActivity;
import com.ebabu.ach.Beans.ShippingAddress;
import com.ebabu.ach.R;
import com.ebabu.ach.Utils.AppPreference;
import com.ebabu.ach.constants.IKeyConstants;
import com.ebabu.ach.constants.IUrlConstants;
import com.ebabu.ach.customview.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HacKy on 2/25/2017.
 */

public class ShippingAddressAdapter extends RecyclerView.Adapter<ShippingAddressAdapter.ViewHolder> {
    private Context context;
    private List<ShippingAddress> addressList;
    private static final String TAG = "TAG";

    public ShippingAddressAdapter(Context context, List<ShippingAddress> addressList) {
        this.context = context;
        this.addressList = addressList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_item_address_list, parent, false);
        ShippingAddressAdapter.ViewHolder vh = new ShippingAddressAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ShippingAddress shippingAddress = addressList.get(position);
        final String Address = shippingAddress.getAddress() + " " + shippingAddress.getCity() + " " + shippingAddress.getState() + " " + shippingAddress.getCountry() + " " + shippingAddress.getZip();
        holder.tvName.setText(shippingAddress.getName());
        holder.tvAddress.setText(Address);
        holder.itemView.findViewById(R.id.item_addresslist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPreference.getInstance(context).setShippingAddress(Address);
                AppPreference.getInstance(context).setAddressId(shippingAddress.getAddress_id());
                AppPreference.getInstance(context).setShippingName(shippingAddress.getName());
                ((ShippingAddressListActivity) context).finish();
            }
        });

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAddress(shippingAddress.getAddress_id(), position);
            }
        });


    }

    public void removeAddress(String addressId, final int position) {
        Map<String, Object> params = new HashMap<>();
        params.put(IKeyConstants.ADDRESS_ID, addressId);
        new AQuery(context).ajax(IUrlConstants.DELETE_SHIPPING_ADDRESS, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                Log.d(TAG, "removeAddress(): json= " + json + " params=" + params + " token = " + AppPreference.getInstance(context).getSecretKey());
                if (json != null) {
                    try {
                        if (json.getString(IKeyConstants.STATUS).equalsIgnoreCase(IKeyConstants.SUCCESS)) {
                            addressList.remove(position);
                            notifyItemRemoved(position);
                            ((ShippingAddressListActivity) context).hideButton();
                            if (addressList.size() == 0) {
                                AppPreference.getInstance(context).setAddressId(IKeyConstants.EMPTY);
                                AppPreference.getInstance(context).setShippingName(IKeyConstants.EMPTY);
                                AppPreference.getInstance(context).setShippingAddress(IKeyConstants.EMPTY);
                                ((ShippingAddressListActivity) context).finish();
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.header(IKeyConstants.SECRETKEY, AppPreference.getInstance(context).getSecretKey()));
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView tvName, tvAddress;
        ImageView btnRemove;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (CustomTextView) itemView.findViewById(R.id.txt_name);
            tvAddress = (CustomTextView) itemView.findViewById(R.id.txt_address);
            btnRemove = (ImageView) itemView.findViewById(R.id.btn_delete_address);
        }
    }
}
