package com.ebabu.ach.Beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by darshna on 2/23/2017.
 */

public class CartMedicineDetail implements Parcelable{

    private String medicine_name;
    private String sold_by;
    private String price;
    private int quantity;
    private String create_at;
    private String medicine_id;
    private String medicine_form;
    private int selectedQuantity = 1;
    private String sale_price;
    private String shipping;

    public String getSale_price() {
        return sale_price;
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    protected CartMedicineDetail(Parcel in) {
        medicine_name = in.readString();
        sold_by = in.readString();
        price = in.readString();
        quantity = in.readInt();
        create_at = in.readString();
        medicine_id = in.readString();
        medicine_form = in.readString();
        selectedQuantity = in.readInt();
    }

    public static final Creator<CartMedicineDetail> CREATOR = new Creator<CartMedicineDetail>() {
        @Override
        public CartMedicineDetail createFromParcel(Parcel in) {
            return new CartMedicineDetail(in);
        }

        @Override
        public CartMedicineDetail[] newArray(int size) {
            return new CartMedicineDetail[size];
        }
    };

    public CartMedicineDetail() {

    }

    public String getMedicine_form() {
        return medicine_form;
    }

    public void setMedicine_form(String medicine_form) {
        this.medicine_form = medicine_form;
    }

    public int getSelectedQuantity() {
        return selectedQuantity;
    }

    public void setSelectedQuantity(int selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public String getSold_by() {
        return sold_by;
    }

    public void setSold_by(String sold_by) {
        this.sold_by = sold_by;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getMedicine_id() {
        return medicine_id;
    }

    public void setMedicine_id(String medicine_id) {
        this.medicine_id = medicine_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(medicine_name);
        dest.writeString(sold_by);
        dest.writeString(price);
        dest.writeInt(quantity);
        dest.writeString(create_at);
        dest.writeString(medicine_id);
        dest.writeString(medicine_form);
        dest.writeInt(selectedQuantity);
    }
}
