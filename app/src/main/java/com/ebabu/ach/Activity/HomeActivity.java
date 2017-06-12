package com.ebabu.ach.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.androidquery.AQuery;
import com.ebabu.ach.R;
import com.ebabu.ach.Utils.AppPreference;
import com.ebabu.ach.Utils.Utils;
import com.ebabu.ach.constants.IKeyConstants;
import com.ebabu.ach.customview.CustomTextView;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private AQuery aq;
    private Toolbar actionBarToolbar;
    private DrawerLayout drawerLayout;
    private String searchText;
    private MenuItem menuItemCart,menuItemNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = HomeActivity.this;
        setUpToolbar(context, "Home");
        initView();
    }

    private void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        findViewById(R.id.btn_search).setOnClickListener(this);
        findViewById(R.id.btn_logout).setOnClickListener(this);
        findViewById(R.id.btn_order_history).setOnClickListener(this);
        findViewById(R.id.btn_notifications).setOnClickListener(this);
        findViewById(R.id.btn_edit_profile).setOnClickListener(this);
      //  if(Utils.getSharedPreferences(context, IKeyConstants.USER_IMAGE).trim().length()>0)
      //  ((ImageView)findViewById(R.id.btn_profile_pic).setBackground(
        //         aq.id((ImageView)findViewById(R.id.btn_profile_pic)).image(Utils.getSharedPreferences(context,IKeyConstants.USER_IMAGE).trim(), true, true, 300, R.mipmap.no_image);

        ((CustomTextView)findViewById(R.id.tv_doctor_name)).setText(AppPreference.getInstance(context).getFirstName() + " " + AppPreference.getInstance(context).getLastName());
        ((CustomTextView)findViewById(R.id.tv_doctor_degree)).setText(AppPreference.getInstance(context).getDegree());
        menuItemCart = (MenuItem) findViewById(R.id.action_cart);

        // ((ImageView) findViewById(R.id.btn_edit_profile)).setImageBitmap(getBitmapFromURL(AppPreference.getInstance(context).getProfilePic()));
        // ((ImageView) findViewById(R.id.btn_edit_profile)).setImageBitmap(getBitmapFromURL("https://blog.sqlauthority.com/i/a/errorstop.png"));
      /*   if (!AppPreference.getInstance(context).getProfilePic().isEmpty()) {
            Picasso
                    .with(context)
                    .load(AppPreference.getInstance(context).getProfilePic())
                    .into((ImageView) findViewById(R.id.btn_edit_profile));
         } */
    }



    private Toolbar setUpToolbar(final Context context, String title) {
        final AppCompatActivity activity = (AppCompatActivity) context;
        actionBarToolbar = (Toolbar) activity.findViewById(R.id.toolbar_actionbar);
        activity.setSupportActionBar(actionBarToolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBarToolbar.setNavigationIcon(R.mipmap.menu);
        actionBarToolbar.setNavigationContentDescription(title);
        actionBarToolbar.setTitle("");
        actionBarToolbar.setSubtitle("");
        CustomTextView tvTitle = (CustomTextView) actionBarToolbar.findViewById(R.id.toolbar_title);
        tvTitle.setText(title);
        actionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });
        return actionBarToolbar;
    }

    private void openDrawer() {
        if (!drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    private void closeDrawer() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.action_cart:
                intent = new Intent(context, CartDetailActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        menuItemCart = menu.findItem(R.id.action_cart);
        menuItemCart.setIcon(Utils.getInstance(context).buildCounterDrawable(AppPreference.getInstance(context).getCounter(), R.mipmap.cart));
        return true;
    }

    @Override
    protected void onResume() {
        if(menuItemCart != null)
        menuItemCart.setIcon(Utils.getInstance(context).buildCounterDrawable(AppPreference.getInstance(context).getCounter(), R.mipmap.cart));
        ((EditText) findViewById(R.id.et_search_text)).setText("");
       // AppPreference.getInstance(context).setProfilePic(data.getString("profile_pic"))
        //aq.id().image(Utils.getSharedPreferences(context,IKeyConstants.USER_IMAGE).trxxxxxxxxxxxxxxxxxxxxim(), true, true, 300, R.mipmap.no_image);
        new AQuery(context).id((ImageView)findViewById(R.id.btn_edit_profile)).image((AppPreference.getInstance(context).getProfilePic().trim()), true, true, 300, R.mipmap.no_image);
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        closeDrawer();
        searchText = ((EditText) findViewById(R.id.et_search_text)).getText().toString().trim();
        int id = v.getId();
        Intent intent = null;
        switch (id) {
            case R.id.btn_search:
                if(searchText.length() >= 3 ) {
                    intent = new Intent(context, MedicineListActivity.class);
                    intent.putExtra(IKeyConstants.SEARCH_TEXT, searchText);
                    startActivity(intent);
                    ((EditText) findViewById(R.id.et_search_text)).setError(null);
                }
                else
                {
                    ((EditText) findViewById(R.id.et_search_text)).setError("Minimum 3 Characters are required");
                }
                break;
            case R.id.btn_logout:
                intent = new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                AppPreference.getInstance(context).clear();
                break;
            case R.id.btn_edit_profile:
                intent = new Intent(context, EditProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_order_history:
                intent = new Intent(context, OrderHistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_notifications:
                intent = new Intent(context, NotificationsActivity.class);
                startActivity(intent);
                break;
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src", src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap", "returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception", e.getMessage());
            return null;
        }
    }
}
