package com.ebabu.ach.Beans;

/**
 * Created by Sahitya on 2/13/2017.
 */

public class OrderMedicine {
    private String medicine_name;
    private String medicine_form;
    private String sale_price;
    private String shipping;

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public String getSale_price() {
        return sale_price;
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
    }

    private int quantity;
    private int selectedQuantity = 1;

    public String getMedicine_image() {
        return medicine_image;
    }

    public void setMedicine_image(String medicine_image) {
        this.medicine_image = medicine_image;
    }

    private String medicine_id;

    public String getMedicine_id() {
        return medicine_id;
    }

    public void setMedicine_id(String medicine_id) {
        this.medicine_id = medicine_id;
    }


    private String medicine_image;
    private String sold_by;
    private String price;
    private String create_at;

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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
}
