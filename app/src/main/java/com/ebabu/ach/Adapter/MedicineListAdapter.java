package com.ebabu.ach.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ebabu.ach.Activity.MedicineListActivity;
import com.ebabu.ach.Beans.OrderMedicine;
import com.ebabu.ach.R;
import com.ebabu.ach.Utils.AppPreference;
import com.ebabu.ach.constants.IKeyConstants;
import com.ebabu.ach.constants.IUrlConstants;
import com.ebabu.ach.customview.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Sahitya on 2/13/2017.
 */

public class MedicineListAdapter extends RecyclerView.Adapter<MedicineListAdapter.ViewHolder> {
    private Context context;
    private List<OrderMedicine> medicineList;
    private final static String TAG = "TAG";

    public MedicineListAdapter(Context context, List<OrderMedicine> medicineList) {
        this.context = context;
        this.medicineList = medicineList;
        Log.d("Adapter","true");

    }

    @Override
    public MedicineListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_item_medicine_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MedicineListAdapter.ViewHolder holder, final int position) {
        final OrderMedicine orderMedicine = medicineList.get(position);
        if (orderMedicine.getMedicine_name().length() <= 0) {
            holder.mediName.setText("N/A");
        } else {
            holder.mediName.setText(Html.fromHtml(orderMedicine.getMedicine_name()));
        }
        if (orderMedicine.getSold_by().length() <= 0) {
            holder.mediCompany.setText("N/A");
        } else {
            holder.mediCompany.setText(Html.fromHtml(orderMedicine.getSold_by()));
        }
        if (orderMedicine.getSale_price().length() <= 0) {
            holder.mediSalePrice.setText("N/A");
        } else {
            holder.mediSalePrice.setText("\u20b9" + Html.fromHtml(orderMedicine.getSale_price()));
        }

        holder.tvQuantity.setText(String.valueOf(orderMedicine.getSelectedQuantity()));

        if (orderMedicine.getQuantity() <= 0) {
            holder.mediQuantity.setText("N/A");

        } else {
            if (orderMedicine.getMedicine_form().toString().equalsIgnoreCase("1")) {
                holder.mediQuantity.setText("Strip of " + Html.fromHtml(orderMedicine.getQuantity() + "\u00A0Tablets"));
            } else if (orderMedicine.getMedicine_form().toString().equalsIgnoreCase("2")) {
                holder.mediQuantity.setText("Bottle of " + Html.fromHtml(orderMedicine.getQuantity() + " ml Syrup"));
            } else {
                holder.mediQuantity.setText("N/A this");
            }
        }
        if (orderMedicine.getMedicine_image() != null && orderMedicine.getMedicine_image().startsWith("http")) {
            new AQuery(context).id(holder.mediImage).image(orderMedicine.getMedicine_image());
        } else {
            holder.mediImage.setImageResource(R.mipmap.medicine);
        }

        Log.d(TAG, "get price = " + orderMedicine.getPrice() + " sale_price = " + orderMedicine.getSale_price());
        if (orderMedicine.getPrice().equalsIgnoreCase(orderMedicine.getSale_price())) {
            holder.mediDiscount.setVisibility(View.GONE);
            holder.mediOriginalPrice.setVisibility(View.GONE);
        } else {
            float discount = getDiscout(Float.valueOf(orderMedicine.getPrice()), Float.valueOf(orderMedicine.getSale_price()));
            holder.mediDiscount.setText(String.valueOf(discount) + "%");
            holder.mediOriginalPrice.setText("\u20b9" + Html.fromHtml(orderMedicine.getPrice()));
        }

        holder.decQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderMedicine.getSelectedQuantity() > 1) {
                    orderMedicine.setSelectedQuantity(orderMedicine.getSelectedQuantity() - 1);
                    notifyItemChanged(position);
                } else {
                    Toast.makeText(context, "Quantity can't be zero", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.incQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderMedicine.setSelectedQuantity(orderMedicine.getSelectedQuantity() + 1);
                notifyItemChanged(position);
            }
        });

        holder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final JSONObject input = new JSONObject();
                try {
                    input.put("quantity", orderMedicine.getSelectedQuantity());
                    input.put("medicine_id", orderMedicine.getMedicine_id().toString().trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new AQuery(context).post(IUrlConstants.ADD_TO_CART, input, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {
                        Log.d(TAG, " addtocart(): Url=" + url + "input = " + input + "json=" + json);
                        Log.d(TAG, " addtocart(): SECRET Key = " + AppPreference.getInstance(context).getSecretKey());
                        try {
                            if (json != null) {
                                if (json.getString(IKeyConstants.STATUS).equalsIgnoreCase(IKeyConstants.SUCCESS)) {
                                    AppPreference.getInstance(context).setCounter(AppPreference.getInstance(context).getCounter() + 1);
                                    holder.btnAddToCart.setVisibility(View.GONE);
                                    holder.txtAddToCart.setVisibility(View.VISIBLE);
                                    holder.btnQuantity.setVisibility(View.INVISIBLE);
                                    ((MedicineListActivity)context).setCartIcon();
                                } else {

                                    Toast.makeText(context, "ERROR = " + json.getString(IKeyConstants.MESSAGE), Toast.LENGTH_SHORT).show();

                                }
                            }
                        } catch (Exception e) {

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView mediName, mediSalePrice, mediOriginalPrice, mediQuantity, mediDiscount, mediCompany, tvQuantity, incQuantity, decQuantity, btnAddToCart, txtAddToCart;
        ImageView mediImage;
        LinearLayout btnQuantity;

        public ViewHolder(View itemView) {
            super(itemView);
            btnQuantity = (LinearLayout) itemView.findViewById(R.id.btn_quantity);
            mediName = (CustomTextView) itemView.findViewById(R.id.medicine_name);
            mediSalePrice = (CustomTextView) itemView.findViewById(R.id.saleprice);
            mediOriginalPrice = (CustomTextView) itemView.findViewById(R.id.original_price);
            mediCompany = (CustomTextView) itemView.findViewById(R.id.medicine_company);
            mediQuantity = (CustomTextView) itemView.findViewById(R.id.medicine_quantity);
            mediDiscount = (CustomTextView) itemView.findViewById(R.id.txt_discount);
            mediImage = (ImageView) itemView.findViewById(R.id.medicine_image);
            tvQuantity = (CustomTextView) itemView.findViewById(R.id.txt_quantity);
            incQuantity = (CustomTextView) itemView.findViewById(R.id.btn_increase);
            decQuantity = (CustomTextView) itemView.findViewById(R.id.btn_decrease);
            btnAddToCart = (CustomTextView) itemView.findViewById(R.id.btn_add_to_cart);
            txtAddToCart = (CustomTextView) itemView.findViewById(R.id.txt_add_to_cart);
        }
    }

    public int getDiscout(float salePrice, float originalPrice) {
        float discount = 0;
        discount = ((-originalPrice + salePrice) / originalPrice) * 100;
        return (int) discount;
    }
}
