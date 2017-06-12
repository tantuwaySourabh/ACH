package com.ebabu.ach.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by darshna on 2/22/2017.
 */
public class AppPreference {
    private static AppPreference appPreference;
    private Context context;
    private SharedPreferences sharedPreferences;

    private final static String PREFERENCE_NAME = "AppPreference";
    private final static String USER_TYPE = "USER_TYPE";
    private final static String SECRET_KEY = "SECRET_KEY";
    private final static String USER_ID = "USER_ID";
    private final static String ADDRESS = "ADDRESS";
    private final static String CITY = "CITY";
    private final static String ZIP_CODE = "ZIP";
    private final static String DEGREE = "DEGREE";
    private final static String STATE = "STATE";
    private final static String COUNTRY = "COUNTRY";
    private final static String MOBILE = "MOBILE";
    private final static String EMAIL = "EMAIL";
    private final static String FIRSTNAME = "FIRSTNAME";
    private final static String LASTNAME = "LASTNAME";
    private final static String PROFILE_PIC = "PROFILE_PIC";
    private final static String CERTIFICATE = "CERTIFICATE";
    private final static String SHIPPING_NAME = "SHIPPING_NAME";
    private final static String SHIPPING_ADDRESS = "SHIPPING_ADDRESS";
    private final static String ADDRESS_ID = "ADDRESS_ID";
    private final static String CART_COUNTER = "CART_COUNTER";



    private AppPreference(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static AppPreference getInstance(Context context) {
        return new AppPreference(context);
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public String getUserType() {
        String userType = sharedPreferences.getString(USER_TYPE, null);
        return userType;
    }

    public String getAddressId() {
        String userType = sharedPreferences.getString(ADDRESS_ID, null);
        return userType;
    }

    public String getShippingName() {
        String userType = sharedPreferences.getString(SHIPPING_NAME, null);
        return userType;
    }

    public String getShippingAddress() {
        String userType = sharedPreferences.getString(SHIPPING_ADDRESS, null);
        return userType;
    }

    public void setUserType(String userType) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_TYPE, userType);
        editor.commit();
    }

    public void setShippingName(String userType) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHIPPING_NAME, userType);
        editor.commit();
    }

    public void setShippingAddress(String userType) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHIPPING_ADDRESS, userType);
        editor.commit();
    }


    public int getCounter() {
        return sharedPreferences.getInt(CART_COUNTER, 0);
    }


    public void setCounter(int counter) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(CART_COUNTER, counter);
        editor.commit();
    }

    public String getSecretKey() {
        String userType = sharedPreferences.getString(SECRET_KEY, null);
        return userType;
    }

    public void setSecretKey(String secretKey) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SECRET_KEY, secretKey);
        editor.commit();
    }

    public void setAddressId(String secretKey) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ADDRESS_ID, secretKey);

        editor.commit();
    }


    public boolean isLoggedIn() {
        if (sharedPreferences.contains(SECRET_KEY)) {
            return true;
        }

        return false;
    }

    public void setFirstName(String firstName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FIRSTNAME, firstName);
        editor.commit();
    }

    public void setLastName(String lastName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LASTNAME, lastName);
        editor.commit();
    }

    public void setEmail(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL, email);
        editor.commit();
    }

    public void setMobile(String mobile) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MOBILE, mobile);
        editor.commit();
    }

    public void setDegree(String degree) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DEGREE, degree);
        editor.commit();
    }

    public void setCity(String city) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CITY, city);
        editor.commit();
    }

    public void setZipCode(String zip_code) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ZIP_CODE, zip_code);
        editor.commit();
    }

    public void setState(String state) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(STATE, state);
        editor.commit();
    }

    public void setAddress(String address) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ADDRESS, address);
        editor.commit();
    }

    public void setCountry(String country) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(COUNTRY, country);
        editor.commit();
    }

    /*---------------------------------------------------------------------------------------*/
    public String getFirstName() {
        String userType = sharedPreferences.getString(FIRSTNAME, null);
        return userType;
    }

    public String getLastName() {
        String userType = sharedPreferences.getString(LASTNAME, null);
        return userType;
    }

    public String getEmail() {
        String userType = sharedPreferences.getString(EMAIL, null);
        return userType;
    }

    public String getMobile() {
        String userType = sharedPreferences.getString(MOBILE, null);
        return userType;
    }

    public String getDegree() {
        String userType = sharedPreferences.getString(DEGREE, null);
        return userType;
    }

    public String getAddress() {
        String userType = sharedPreferences.getString(ADDRESS, null);
        return userType;
    }

    public String getCity() {
        String userType = sharedPreferences.getString(CITY, null);
        return userType;
    }

    public String getZipCode() {
        String userType = sharedPreferences.getString(ZIP_CODE, null);
        return userType;
    }

    public String getState() {
        String userType = sharedPreferences.getString(STATE, null);
        return userType;
    }

    public String getCountry() {
        String userType = sharedPreferences.getString(COUNTRY, null);
        return userType;
    }

    /*--------------------------------------------------------------*/
    public void setProfilePic(String profilePic) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PROFILE_PIC, profilePic);
        editor.commit();
    }


 public String getProfilePic() {
   return  sharedPreferences.getString(PROFILE_PIC, "");
   //     editor.putString(PROFILE_PIC, profilePic);
    //    editor.commit();
    }

    public   String getCertificate() {
        return sharedPreferences.getString(CERTIFICATE,"");
    }

    public void setCertificate(String certificate) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CERTIFICATE, certificate);
        editor.commit();
    }
}
