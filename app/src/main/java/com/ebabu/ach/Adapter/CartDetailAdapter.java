package com.ebabu.ach.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ebabu.ach.Activity.CartDetailActivity;
import com.ebabu.ach.Beans.CartDetail;
import com.ebabu.ach.Beans.CartMedicineDetail;
import com.ebabu.ach.R;
import com.ebabu.ach.Utils.AppPreference;
import com.ebabu.ach.constants.IKeyConstants;
import com.ebabu.ach.constants.IUrlConstants;
import com.ebabu.ach.customview.CustomTextView;
import com.ebabu.ach.customview.MyLoading;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Sahitya on 2/18/2017.
 */

public class CartDetailAdapter extends RecyclerView.Adapter<CartDetailAdapter.ViewHolder> {
    private Context context;
    private List<CartDetail> medicineList;
    private final static String TAG = "TAG";
    private MyLoading myLoading;


    public CartDetailAdapter(Context context, List<CartDetail> medicineList) {
        this.context = context;
        this.medicineList = medicineList;
        myLoading = new MyLoading(context);

    }

    @Override
    public CartDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_item_cart, parent, false);
        CartDetailAdapter.ViewHolder vh = new CartDetailAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final CartDetailAdapter.ViewHolder holder, final int position) {
        final CartDetail cartDetail = medicineList.get(position);

        if (cartDetail.getMedicine_detail() != null) {
            if (cartDetail.getMedicine_detail().getMedicine_name() != null && !cartDetail.getMedicine_detail().getMedicine_name().isEmpty()) {
                holder.mediName.setText(Html.fromHtml(cartDetail.getMedicine_detail().getMedicine_name()));
            } else {
                holder.mediName.setText(IKeyConstants.NA);
            }

            if (cartDetail.getMedicine_detail().getSale_price() != null && !cartDetail.getMedicine_detail().getSale_price().isEmpty()) {
                holder.mediSalePrice.setText("\u20b9" + Html.fromHtml(cartDetail.getMedicine_detail().getSale_price().toString()));
            } else {
                holder.mediSalePrice.setText("\u20b9" + IKeyConstants.NA);
            }
           /* if (cartDetail.getMedicine_detail().getPrice() != null && !cartDetail.getMedicine_detail().getPrice().isEmpty()) {
                holder.mediOriginalPrice.setText("\u20b9" + Html.fromHtml(cartDetail.getMedicine_detail().getPrice().toString()));
            } else {
                holder.mediOriginalPrice.setText("\u20b9" + IKeyConstants.NA);
            }
*/
            if (cartDetail.getMedicine_detail().getPrice().equalsIgnoreCase(cartDetail.getMedicine_detail().getSale_price())) {
                holder.mediDiscount.setVisibility(View.GONE);
                holder.mediOriginalPrice.setVisibility(View.GONE);
            } else {
                float discount = getDiscout(Float.valueOf(cartDetail.getMedicine_detail().getPrice()), Float.valueOf(cartDetail.getMedicine_detail().getSale_price()));
                holder.mediDiscount.setText(String.valueOf(discount) + "%");
                holder.mediOriginalPrice.setText("\u20b9" + Html.fromHtml(cartDetail.getMedicine_detail().getPrice()));
            }

            if (cartDetail.getMedicine_detail().getQuantity() <= 0) {
                holder.mediQuantity.setText(IKeyConstants.NA);

            } else {
                if (cartDetail.getMedicine_detail().getMedicine_form().equalsIgnoreCase(IKeyConstants.SOLID_MEDICINE)) {
                    holder.mediQuantity.setText("Strip of " + Html.fromHtml(cartDetail.getMedicine_detail().getQuantity() + "\u00A0Tablets"));
                } else if (cartDetail.getMedicine_detail().getMedicine_form().toString().equalsIgnoreCase(IKeyConstants.LIQUID_MEDICINE)) {
                    holder.mediQuantity.setText("Bottle of " + Html.fromHtml(cartDetail.getMedicine_detail().getQuantity() + " ml Syrup"));
                } else {
                    holder.mediQuantity.setText(IKeyConstants.NA);
                }
            }
        }
        holder.tvQuantity.setText(String.valueOf(cartDetail.getMedicine_quantity()));

        holder.decQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartDetail.getMedicine_quantity() > 1) {
                    myLoading.show("Processing");
                    changeQuantity(cartDetail.getMedicine_quantity() - 1, cartDetail.getMedicine_detail().getMedicine_id(), position, 100);
                } else {
                    Toast.makeText(context, "Quantity can't be zero", Toast.LENGTH_SHORT).show();
                }
                ((CartDetailActivity) context).showCharge();

            }
        });

        holder.incQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myLoading.show("Processing");
                changeQuantity(cartDetail.getMedicine_quantity() + 1, cartDetail.getMedicine_detail().getMedicine_id(), position, 100);
                ((CartDetailActivity) context).showCharge();
            }
        });

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myLoading.show("Processing");
                Map<String, Object> param = new HashMap<String, Object>();
                param.put(IKeyConstants.MEDICINE_ID, cartDetail.getMedicine_detail().getMedicine_id());
                Log.d(TAG, "RemoveItem(): param= " + param);
                new AQuery(context).ajax(IUrlConstants.REMOVE_ITEM_FROM_CART, param, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {
                        Log.d(TAG, "RemoveItem(): json1= " + json + "position = " + position);
                        if (json != null) {
                            try {
                                if (json.getString(IKeyConstants.STATUS).equalsIgnoreCase(IKeyConstants.SUCCESS)) {
                                    medicineList.remove(position);
                                    myLoading.dismiss();
                                    notifyDataSetChanged();
                                    if(AppPreference.getInstance(context).getCounter() > 0)
                                    AppPreference.getInstance(context).setCounter(AppPreference.getInstance(context).getCounter()-1);
                                    ((CartDetailActivity) context).showCharge();

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

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    private void changeQuantity(final int medicineQuantity, final String medicineID, final int position, int price) {
        final JSONObject input = new JSONObject();
        try {
            input.put("quantity", medicineQuantity);
            input.put("medicine_id", medicineID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new AQuery(context).post(IUrlConstants.ADD_TO_CART, input, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                try {
                    Log.d(TAG, "changequantity(): json=" + json);
                    if (json != null) {
                        if (json.getString(IKeyConstants.STATUS).equalsIgnoreCase(IKeyConstants.SUCCESS)) {
                            medicineList.get(position).setMedicine_quantity(medicineQuantity);
                            notifyItemChanged(position);
                            ((CartDetailActivity) context).showCharge();
                            myLoading.dismiss();
                        }
                    }
                } catch (Exception e) {

                }
            }
        }.header(IKeyConstants.SECRETKEY, AppPreference.getInstance(context).getSecretKey()));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView mediName, mediSalePrice, mediDiscount, mediQuantity, tvQuantity, incQuantity, decQuantity, mediOriginalPrice, btnRemove;

        public ViewHolder(View itemView) {
            super(itemView);
            mediName = (CustomTextView) itemView.findViewById(R.id.medicine_name);
            mediSalePrice = (CustomTextView) itemView.findViewById(R.id.saleprice);
            mediDiscount = (CustomTextView) itemView.findViewById(R.id.txt_discount);
            mediOriginalPrice = (CustomTextView) itemView.findViewById(R.id.original_price);
            mediQuantity = (CustomTextView) itemView.findViewById(R.id.medicine_quantity);
            tvQuantity = (CustomTextView) itemView.findViewById(R.id.txt_quantity);
            incQuantity = (CustomTextView) itemView.findViewById(R.id.btn_increase);
            decQuantity = (CustomTextView) itemView.findViewById(R.id.btn_decrease);
            btnRemove = (CustomTextView) itemView.findViewById(R.id.btn_remove);
        }
    }

    public int getDiscout(float salePrice, float originalPrice) {
        float discount = 0;
        discount = ((-originalPrice + salePrice) / originalPrice) * 100;
        return (int) discount;
    }
}

