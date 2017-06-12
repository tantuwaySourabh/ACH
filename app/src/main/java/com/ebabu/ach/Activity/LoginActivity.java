package com.ebabu.ach.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import com.ebabu.ach.customview.CustomTextView;
import com.ebabu.ach.customview.MyLoading;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private Context context;
    private Intent intent = null;
    private CustomEditText etMobileNum;
    private String mobileNum, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = LoginActivity.this;
        initView();
    }

    private void initView() {

            etMobileNum = (CustomEditText) findViewById(R.id.et_login_mobile);
            findViewById(R.id.btn_signup).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(context, SignupFirstActivity.class);
                    startActivity(intent);
                }
            });
            findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (areFieldsValid()) {
                        verifyMobileNum();
                    }
                }
            });

    }


    private void verifyMobileNum() {
        final MyLoading myLoading = new MyLoading(context);
        myLoading.show("Verifying Mobile Number");

        final JSONObject input = new JSONObject();
        try {
            input.put("mobile", mobileNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new AQuery(context).post(IUrlConstants.LOGIN, input, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                if (json != null) {
                    try {
                        if ((IKeyConstants.SUCCESS).equalsIgnoreCase(json.getString("status"))) {
                            Intent intent;
                            userId = json.getString("user_id");
                            Toast.makeText(context, "Message = " + json.getString("message"), Toast.LENGTH_SHORT).show();
                            intent = new Intent(context, OtpActivity.class);
                            intent.putExtra(IKeyConstants.USERID, userId);
                            intent.putExtra(IKeyConstants.MOBILE, mobileNum);
                            startActivity(intent);
                        } else {
                            Toast.makeText(context, "ERROR " + json.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "EMPTY JSON", Toast.LENGTH_SHORT).show();
                    }
                }
                myLoading.dismiss();
            }
        });
    }

    private boolean areFieldsValid() {
        mobileNum = etMobileNum.getText().toString();

        if (mobileNum.isEmpty()) {
            etMobileNum.setError("Mobile number cannot be blank");
            return false;
        }

        if (mobileNum.length() != 10) {
            etMobileNum.setError("Mobile number must be of 10 digits");
            return false;
        }

        int firstNumber = mobileNum.charAt(0);
        if (firstNumber < 7) {
            etMobileNum.setError("Mobile number is not valid");
            return false;
        }

        if (!Utils.isNetworkConnected(context)) {
            Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
