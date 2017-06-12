package com.ebabu.ach.Utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ebabu.ach.Activity.LoginActivity;
import com.ebabu.ach.Activity.SplashActivity;
import com.ebabu.ach.R;
import com.ebabu.ach.customview.CustomTextView;
import com.rey.material.app.DialogFragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static android.graphics.Color.rgb;

/**
 * Created by hp on 10/09/2016.
 */
public class Utils {
    public static final String SOS_MESSAGE = "Hey!!! I'm in trouble, having major health problem.\nPlease reach me ASAP";
    private static String mDob;
    private Context context;
    private static Utils utils = null;
    public static String PREFERENCE = "ACH";
    public static final SimpleDateFormat displayTimeFormat = new SimpleDateFormat("hh:mm a");
    public static final SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd MMM, yyyy");
    public static final SimpleDateFormat numericDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat dbNumericDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat completeTimestampFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP = 999;
    public static final String[] RATING_TEXT = {"Hated It", "Didn't like it", "Just OK", "Liked It", "Awesome"};

    public static Toolbar setUpToolbar(final Context context, String title, Boolean color) {
        final AppCompatActivity activity = (AppCompatActivity) context;
        Toolbar actionBarToolbar = (Toolbar) activity.findViewById(R.id.toolbar_actionbar);
        activity.setSupportActionBar(actionBarToolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        //actionBarToolbar.setBackgroundResource(backgroundResource);
        actionBarToolbar.setNavigationContentDescription(title);
        actionBarToolbar.setTitle("");
        actionBarToolbar.setSubtitle("");
        CustomTextView tvTitle = (CustomTextView) actionBarToolbar.findViewById(R.id.toolbar_title);
        tvTitle.setText(title);
        if (color == Boolean.FALSE) {
            tvTitle.setTextColor(rgb(97, 97, 97));
            actionBarToolbar.setNavigationIcon(R.mipmap.dark_back);
            actionBarToolbar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.white)));

        } else if (color == Boolean.TRUE) {
            tvTitle.setTextColor(rgb(255, 255, 255));
            actionBarToolbar.setNavigationIcon(R.mipmap.back);
            actionBarToolbar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.primaryColor)));

        }
        actionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        return actionBarToolbar;
    }

    public static int getNotificationsIcon() {
        if (Build.VERSION.SDK_INT >= 21) {
            return R.mipmap.ic_launcher;
        } else {
            return R.mipmap.ic_launcher;
        }
    }
    public static String getSharedPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        return settings.getString(name, "");
    }



    private Utils(Context context) {
        this.context = context;
    }

    public static Utils getInstance(Context context) {
        if (utils == null) {
            utils = new Utils(context);
        }
        return utils;
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = ((Activity) context).getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static void openTimerPickerDialog(Context context, final TextView tvTime) {
        try {

            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            com.rey.material.app.Dialog.Builder builder = new com.rey.material.app.TimePickerDialog.Builder(R.style.MyTimePickerTheme, hour, minute) {
                @Override
                public void onPositiveActionClicked(DialogFragment fragment) {
                    com.rey.material.app.TimePickerDialog dialog = (com.rey.material.app.TimePickerDialog) fragment.getDialog();
                    tvTime.setText(dialog.getFormattedTime(displayTimeFormat).toUpperCase());
                    fragment.dismiss();
                }

                @Override
                public void onNegativeActionClicked(DialogFragment fragment) {
                    fragment.dismiss();
                }
            };

            builder.positiveAction("OK")
                    .negativeAction("CANCEL");
            DialogFragment fragment = DialogFragment.newInstance(builder);

            fragment.show(((AppCompatActivity) context).getSupportFragmentManager(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openCalendarDialog(Context context, String dob, final TextView tvDob, boolean isDob) {
        mDob = dob;
        try {
            int mDay, mMonth, mYear, mMinDay, mMinMonth, mMinYear, mMaxDay, mMaxMonth, mMaxYear;
            Calendar cal = Calendar.getInstance();
            mDay = cal.get(Calendar.DAY_OF_MONTH);
            mMonth = cal.get(Calendar.MONTH);
            mYear = cal.get(Calendar.YEAR);

            if (isDob) {
                mMinDay = mDay;
                mMinMonth = mMonth;
                mMinYear = mYear - 80;

                mMaxDay = 31;
                mMaxMonth = 12;
                mMaxYear = mYear - 5;
                mYear = mMaxYear;
            } else {
                mMinDay = mDay;
                mMinMonth = mMonth;
                mMinYear = mYear - 1;

                mMaxDay = mDay;
                mMaxMonth = mMonth;
                mMaxYear = mYear;
            }

            if (dob != null && !dob.isEmpty()) {
                try {
                    Calendar dobCal = Calendar.getInstance();
                    try {
                        dobCal.setTime(displayDateFormat.parse(dob));
                    } catch (Exception e) {
                        dobCal.setTime(numericDateFormat.parse(dob));
                    }
                    mDay = dobCal.get(Calendar.DAY_OF_MONTH);
                    mMonth = dobCal.get(Calendar.MONTH);
                    mYear = dobCal.get(Calendar.YEAR);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            com.rey.material.app.Dialog.Builder builder = new com.rey.material.app.DatePickerDialog.Builder(mMinDay, mMinMonth, mMinYear, mMaxDay, mMaxMonth, mMaxYear, mDay, mMonth, mYear) {
                @Override
                public void onPositiveActionClicked(DialogFragment fragment) {
                    com.rey.material.app.DatePickerDialog dialog = (com.rey.material.app.DatePickerDialog) fragment.getDialog();
                    mDob = dialog.getFormattedDate(displayDateFormat);

                    tvDob.setText(dialog.getFormattedDate(displayDateFormat));
                    fragment.dismiss();
                }

                @Override
                public void onNegativeActionClicked(DialogFragment fragment) {
                    fragment.dismiss();
                }
            };

            builder.positiveAction("OK")
                    .negativeAction("CANCEL").style(R.style.MyCalendarTheme);
            DialogFragment fragment = DialogFragment.newInstance(builder);


            fragment.show(((AppCompatActivity) context).getSupportFragmentManager(), null);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Drawable buildCounterDrawable(int count, int bgResourceId) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_menu_counter, null);
        ImageView imgCounterBackground = (ImageView) view.findViewById(R.id.img_counter_background);
        imgCounterBackground.setImageResource(bgResourceId);

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.tv_count);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = (TextView) view.findViewById(R.id.tv_count);
            textView.setText("" + count);
        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(((Activity) context).getResources(), bitmap);
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    public static boolean isNameValid(String fullName) {
        String regx = "^[\\p{L} .'-]+$";

        if (fullName == null) {
            return false;
        } else {
            return Pattern.matches(regx, fullName);
        }
    }

    public static boolean isWebsiteValid(String website) {
        String regex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

        if (website == null) {
            return false;
        } else {
            return Pattern.matches(regex, website);
        }
    }

    public static byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public static boolean intToBoolean(int intValue) {
        return intValue == 0 ? false : true;
    }

    public static void logout(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        AppPreference.getInstance(context).clear();
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        return width;
    }

    public static void openPlayStoreToRateUs(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }


    public static void sendSMS(Context context, String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(context.getApplicationContext(), "Message sent to " + phoneNo,
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(context.getApplicationContext(), ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public static String getImei(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public static void generateNotification(Context context, Map<String, String> data) {
        try {
            Bitmap largeBitmap = getBitmapFromUrl(context, data.get("image"));
            long when = System.currentTimeMillis();
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context.getApplicationContext());
            Intent resultIntent;
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            resultIntent = new Intent(context.getApplicationContext(), SplashActivity.class);
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification = new NotificationCompat.Builder(context.getApplicationContext())
                    .setColor(context.getResources().getColor(R.color.primaryColor))
                    .setSmallIcon(Utils.getNotificationsIcon())
                    .setAutoCancel(true)
                    .setContentIntent(resultPendingIntent)
                    .setLargeIcon(largeBitmap)
                    .setContentTitle(data.get("title"))
                    .setContentText(data.get("message")).build();

            notification.defaults |= Notification.DEFAULT_LIGHTS;
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
            notification.priority = Notification.PRIORITY_MAX;
            notificationManager.notify((int) when, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmapFromUrl(Context context, String myUrl) {
        URL url = null;
        try {
            if (myUrl != null && !myUrl.isEmpty()) {
                url = new URL(myUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bitmap image = null;
        try {
            if (url != null) {
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (image == null) {
            image = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        }

        return image;
    }

    public static Date getReverseDate(String dob) {
        String strDate = dbNumericDateFormat.format(dob);
        try {
            Date dt = dbNumericDateFormat.parse(strDate);
            return dt;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }


    public static HashMap<String, ArrayList<String>> getServiceCategories() {
        HashMap<String, ArrayList<String>> mapServiceCategories = new HashMap<>();

        ArrayList<String> subcategories = new ArrayList<>();
        subcategories.add("CA for Small Business");
        subcategories.add("Company Registration");
        subcategories.add("Divorce Lawyer");
        subcategories.add("Income Tax Filling");
        subcategories.add("Lawyer");
        subcategories.add("PAN Card Registration");
        subcategories.add("Web Designer & Developer");
        mapServiceCategories.put("Business", subcategories);
        subcategories = new ArrayList<>();
        subcategories.add("Birthday Party Decorator");
        subcategories.add("Birthday Party Planner");
        subcategories.add("DJ for Weddings & Events");
        subcategories.add("Party Photography");
        mapServiceCategories.put("Event & Wedding", subcategories);

        return mapServiceCategories;
    }
//    public static boolean isGooglePlayServicesAvailable(Activity activity) {
//        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
//        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
//        if (status != ConnectionResult.SUCCESS) {
//            if (googleApiAvailability.isUserResolvableError(status)) {
//                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
//            }
//            return false;
//        }
//        return true;
//    }

}
