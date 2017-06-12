package com.ebabu.ach.constants;


public interface IUrlConstants {
    String BASE_URL = "http://base3.engineerbabu.com:8282/ach/api_II/";
    String LOGIN = BASE_URL + "login";
    String VERIFY_OTP = BASE_URL + "verify";
    String REGISTER_USER = BASE_URL + "register";
    String SEARCH_MEDICINE= BASE_URL + "Search_medicine";
    String ADD_TO_CART = BASE_URL + "AddToCart";
    String CART_DETAIL = BASE_URL + "Cart_detail";
    String REMOVE_ITEM_FROM_CART = BASE_URL + "Item_remove_from_cart";
    String ADD_SHIPPING_ADDRESS = BASE_URL + "Add_shipping_address";
    String SHIPPING_ADDRESS_LIST = BASE_URL + "Shipping_address_list";
    String DELETE_SHIPPING_ADDRESS = BASE_URL + "Delete_shipping_address";
    String CART_CHECKOUT = BASE_URL + "Cart_checkout";
    String ORDER_HISTORY = BASE_URL + "Order_history";
    String CANCEL_ORDER = BASE_URL + "cancel_order";
    String UPDATE_PROFILE = BASE_URL + "update_profile";
    String NOTIFICATIONS = BASE_URL + "Notification";
}
