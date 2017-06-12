package com.ebabu.ach.Beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sahitya on 2/18/2017.
 */

public class CartDetail implements Parcelable {
    private int medicine_quantity;


    private int cart_count;
    private String cart_id;
    private CartMedicineDetail medicine_detail;


    protected CartDetail(Parcel in) {
        medicine_quantity = in.readInt();
        cart_count = in.readInt();
        cart_id = in.readString();
        medicine_detail = in.readParcelable(CartMedicineDetail.class.getClassLoader());
    }

    public static final Creator<CartDetail> CREATOR = new Creator<CartDetail>() {
        @Override
        public CartDetail createFromParcel(Parcel in) {
            return new CartDetail(in);
        }

        @Override
        public CartDetail[] newArray(int size) {
            return new CartDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(medicine_quantity);
        dest.writeInt(cart_count);
        dest.writeString(cart_id);
        dest.writeParcelable(medicine_detail, flags);
    }

    public int getMedicine_quantity() {
        return medicine_quantity;
    }

    public void setMedicine_quantity(int medicine_quantity) {
        this.medicine_quantity = medicine_quantity;
    }

    public int getCart_count() {
        return cart_count;
    }

    public void setCart_count(int cart_count) {
        this.cart_count = cart_count;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public CartMedicineDetail getMedicine_detail() {
        return medicine_detail;
    }

    public void setMedicine_detail(CartMedicineDetail medicine_detail) {
        this.medicine_detail = medicine_detail;
    }
}
