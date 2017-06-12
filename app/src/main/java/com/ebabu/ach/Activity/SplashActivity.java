package com.ebabu.ach.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.ebabu.ach.R;
import com.ebabu.ach.Utils.AppPreference;
import com.ebabu.ach.Utils.Utils;

public class SplashActivity extends AppCompatActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        Thread thread = new Thread() {
            @Override
            public void run() {

                try {
                    Thread.sleep(3 * 1000);

                    if (!AppPreference.getInstance(context).isLoggedIn()) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        finish();
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, HomeActivity.class);
                        finish();
                        startActivity(intent);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}
