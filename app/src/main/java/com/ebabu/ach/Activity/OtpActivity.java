package com.ebabu.ach.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ebabu.ach.R;
import com.ebabu.ach.Utils.AppPreference;
import com.ebabu.ach.Utils.Utils;
import com.ebabu.ach.constants.IKeyConstants;
import com.ebabu.ach.constants.IUrlConstants;
import com.ebabu.ach.customview.CustomEditText;
import com.ebabu.ach.customview.MyLoading;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

public class OtpActivity extends AppCompatActivity {
    private Context context;
    private CustomEditText etOtp;
    private String userId, deviceToken, deviceId, deviceType = "Android";
    private String TAG = "TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        context = OtpActivity.this;
        Utils.setUpToolbar(context, getString(R.string.signup), true);
        initView();
    }

    private void initView() {
        // firebaseInstanceId = new MyFirebaseInstanceIDService();

        deviceToken = FirebaseInstanceId.getInstance().getToken();
        deviceId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d(TAG, "OTPDetails() : token =" + deviceToken + " id = " + deviceId);
        Intent intent = getIntent();
        userId = intent.getStringExtra(IKeyConstants.USERID);
        etOtp = (CustomEditText) findViewById(R.id.et_otp);
        etOtp.addTextChangedListener(inputTextWatcher);
        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOTP(etOtp.getText().toString().trim());
            }
        });

        etOtp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Log.d("OTP", "OTP length: " + etOtp.getText().toString().trim().length());
                    if (etOtp.getText().toString().trim().length() == 0) {
                        Toast.makeText(context, "Please enter OTP", Toast.LENGTH_LONG).show();
                    } else if (etOtp.getText().toString().trim().length() < 4) {
                        Toast.makeText(context, "Invalid OTP", Toast.LENGTH_LONG).show();
                    } else {
                        verifyOTP(etOtp.getText().toString());
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void verifyOTP(String otp) {
        final MyLoading myLoading = new MyLoading(context);
        myLoading.show("Verifying OTP");
        final JSONObject input = new JSONObject();
        try {
            input.put("otp", otp);
            input.put("user_id", userId);
            input.put("device_id", deviceId);
            input.put("device_type", deviceType);
            input.put("device_token", deviceToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new AQuery(context).post(IUrlConstants.VERIFY_OTP, input, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                Log.d(TAG, "verifyOTP:input=" + input + " Json =  " + json + "url=" + url);
                if (json != null) {
                    try {
                        Intent intent;
                        if ((IKeyConstants.SUCCESS).equalsIgnoreCase(json.getString("status"))) {
                            JSONObject data = json.getJSONObject("data");
                            JSONObject ship_address = data.getJSONObject("ship_address");
                            AppPreference.getInstance(context).setSecretKey(data.getString("token"));
                            AppPreference.getInstance(context).setFirstName(data.getString("firstname"));
                            AppPreference.getInstance(context).setLastName(data.getString("lastname"));
                            AppPreference.getInstance(context).setEmail(data.getString("email"));
                            AppPreference.getInstance(context).setMobile(data.getString("mobile"));
                            AppPreference.getInstance(context).setDegree(data.getString("degree"));
                            AppPreference.getInstance(context).setCity(data.getString("city"));
                            AppPreference.getInstance(context).setZipCode(data.getString("zip_code"));
                            AppPreference.getInstance(context).setState(data.getString("state"));
                            AppPreference.getInstance(context).setAddress(data.getString("address"));
                            AppPreference.getInstance(context).setCountry(data.getString("country"));
                            AppPreference.getInstance(context).setCounter(data.getInt("cart_count"));
                            AppPreference.getInstance(context).setProfilePic(data.getString("profile_pic"));
                            AppPreference.getInstance(context).setCertificate(data.getJSONArray("certificate_file").toString());

                            if(ship_address.length() != 0) {
                                String shippingAdd = ship_address.getString(IKeyConstants.ADDRESS) + " " + ship_address.getString(IKeyConstants.CITY) + " " + ship_address.getString(IKeyConstants.STATE) + " " + ship_address.getString(IKeyConstants.COUNTRY) + " " + data.getString("zip_code");
                                AppPreference.getInstance(context).setAddressId(ship_address.getString(IKeyConstants.ADDRESS_ID));
                                AppPreference.getInstance(context).setShippingAddress(shippingAdd);
                                AppPreference.getInstance(context).setShippingName(ship_address.getString(IKeyConstants.NAME));
                            }
                            setDialog("OK", "","Your Credentials are being Varified",context);
                            /*intent = new Intent(context, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);*/
                        } else if ((IKeyConstants.FAILED).equalsIgnoreCase(json.getString("status"))) {
                            Toast.makeText(context, "OTP not Matched", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "ERROR " + json.getString("message") + "\n Status = " + json.getString("status"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "EMPTY JSON", Toast.LENGTH_SHORT).show();
                }
                myLoading.dismiss();
            }
        });

        /*  new AQuery(context).ajax(IUrlConstants.VERIFY_OTP, param, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                if (json != null) {
                    try {
                        String key = json.getString("status").toString();
                        if (key == "success") {
                            message = json.getString("message");
                            secretKey = json.getString("secret_key");
                            Toast.makeText(context, "message = " + message + "\n secret key = " + secretKey, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(OtpActivity.this, "ERROR " + json.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(OtpActivity.this, "EMPTY JSON", Toast.LENGTH_SHORT).show();
                    }
                }
                myLoading.dismiss();
            }
        }.method(AQuery.METHOD_POST));*/


    }

    private final TextWatcher inputTextWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            try {
                if (s.length() == 4) {
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    verifyOTP(etOtp.getText().toString().trim());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    //method for generating Dialogue
    public void setDialog(String posButton,String negButton ,String msg,final Context ctx){
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(ctx);
        final AlertDialog alertDialog;
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton(posButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(ctx, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        alertDialog = alertDialogBuilder.create();;
        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorPrimary));
        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorPrimary));
    }
}
