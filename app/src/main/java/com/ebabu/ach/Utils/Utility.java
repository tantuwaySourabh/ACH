package com.ebabu.ach.Utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


import com.ebabu.ach.Activity.LoginActivity;
import com.ebabu.ach.R;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utility {

    public static Context appContext;
    private static String LIRA_TAXI_PREFERENCE = "LIRA_TAXI_PREFERENCE";
    private static int MAX_IMAGE_DIMENSION = 720;


    // for username string preferences
    public static void setSharedPreference(Context context, String name, String value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(LIRA_TAXI_PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        // editor.clear();
        editor.putString(name, value);
        editor.commit();
    }


    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {

        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        context.startService(new Intent(context, serviceClass));
        return false;
    }


    public static boolean setLocale(Context appContext, String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = appContext.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        //Utility.setSharedPreference (appContext, Constant.SET_LANGUAGE, lang);
        return true;
    }


    // for username string preferences
    public static void setIntegerSharedPreference(Context context, String name, int value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(LIRA_TAXI_PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        // editor.clear();
        editor.putInt(name, value);
        editor.commit();
    }

    //Drawable
    public static void setDrawableSharedPreference(Context context, String name, int value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(LIRA_TAXI_PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        // editor.clear();
        editor.putInt(name, value);
        editor.commit();
    }

    public static String getSharedPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(LIRA_TAXI_PREFERENCE, 0);
        return settings.getString(name, "");
    }

    public static int getIngerSharedPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(LIRA_TAXI_PREFERENCE, 0);
        return settings.getInt(name, 0);
    }

    public static void setSharedPreferenceBoolean(Context context, String name, boolean value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(LIRA_TAXI_PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(name, value);
        editor.commit();
    }

    public static boolean getSharedPreferencesBoolean(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(LIRA_TAXI_PREFERENCE, 0);
        return settings.getBoolean(name, true);
    }

    public static Bitmap decodeSampledBitmapFromResource(String path, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

/*	public static Drawable getImageFromURL(String photoDomain) {
        Drawable drawable = null;
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet request = new HttpGet(photoDomain.trim());
			HttpResponse response = httpClient.execute(request);
			InputStream is = response.getEntity().getContent();
			drawable = Drawable.createFromStream (is, "src");
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return drawable;
	}*/

/*	public static String postParamsAndfindJSON(String url,
                                               ArrayList<NameValuePair> params) {
		String result = "";

		System.out.println("URL comes in jsonparser class is:  " + url+params);
		try {
			int TIMEOUT_MILLISEC = 100000; // = 10 seconds
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout (httpParams, TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout (httpParams, TIMEOUT_MILLISEC);

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			// httpGet.setURI(new URI(url));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			InputStream is = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader (new InputStreamReader (is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder ();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			is.close();
			result = sb.toString();

		} catch (Exception e) {
			System.out.println("exception in jsonparser class ........");
			e.printStackTrace();
			result="";
			return result;
		}
		return result;
	}*/

	/*public static String multiPart(String url,MultipartEntity entity) {
        String result="";
		HttpClient httpclient;

		try {
			httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);

			httppost.setEntity(entity);
			System.out.println("cvsc" + httppost);
			HttpResponse response = httpclient.execute(httppost);
			result = EntityUtils.toString(response.getEntity());
			System.out.println("Given Result to photo  " + result);

		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;

	}*/


    public static Bitmap DownloadImageDirect(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        // String imageEncoded = Base64Coder.encodeTobase64(image);

        // Log.d("LOOK", imageEncoded);
        return imageEncoded;
    }

    public static void alertBoxShow(Context context, int msg) {
        // set dialog for user's current location set for searching theaters.
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Alert");
        dialog.setMessage(msg);
        dialog.setPositiveButton(" Ok ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();

            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void ShowStringAlertWithMessage(Context context, int alerttitle,
                                                  int locationvalidation) {
        // Assign the alert builder , this can not be assign in Click events
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage(locationvalidation);
        builder.setTitle(alerttitle);
        // Set behavior of negative button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancel the dialog
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void ShowStringAlert2WithMessage(final Context context, int alerttitle,
                                                   int locationvalidation) {
        // Assign the alert builder , this can not be assign in Click events
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage(locationvalidation);
        builder.setTitle(alerttitle);
        // Set behavior of negative button
        builder.setPositiveButton("GET STARTED", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancel the dialog
                dialog.cancel();
                //((HomeActivity) context).moveToNextActivity("GET STARTED");
                //Intent intent=new Intent(context,GetStartedActivity.class);
            }
        });
        builder.setNegativeButton("FAQ", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancel the dialog
                dialog.cancel();
                //((HomeActivity) context).moveToNextActivity("FAQ");
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


    public static boolean MobileValidator(String mobile) {
        Pattern pattern;
        Matcher matcher;
        final String MOBILE_PATTERN = "^[0-9]*$";
        pattern = Pattern.compile(MOBILE_PATTERN);
        matcher = pattern.matcher(mobile);
        return matcher.matches();
    }

	/*public static String findJSONFromUrl(String url) {
        String result = "";

		System.out.println("URL comes in jsonparser class is:  " + url);
		try {
			int TIMEOUT_MILLISEC = 100000; // = 10 seconds
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout (httpParams,
					TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout (httpParams, TIMEOUT_MILLISEC);
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			// httpGet.setURI(new URI(url));

			HttpResponse httpResponse = httpClient.execute(httpGet);
			InputStream is = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader (new InputStreamReader (is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder ();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			is.close();
			result = sb.toString();
			System.out.println("result  in jsonparser class ........" + result);

		} catch (Exception e) {
			System.out.println("exception in jsonparser class ........");
			result=null;
		}
		return result;
	}*/

    public static Bitmap getBitmap(String url) {
        Bitmap imageBitmap = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            try {
                imageBitmap = BitmapFactory.decodeStream(new FlushedInputStream(is));
            } catch (OutOfMemoryError error) {
                error.printStackTrace();
                System.out.println("exception in get bitma putility");
            }

            bis.close();
            is.close();
            final int IMAGE_MAX_SIZE = 50;
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) > IMAGE_MAX_SIZE) {
                scale++;
            }
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                // b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = imageBitmap.getHeight();
                int width = imageBitmap.getWidth();

                double y = Math.sqrt(IMAGE_MAX_SIZE / (((double) width) / height));
                double x = (y / height) * width;
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(imageBitmap, (int) x, (int) y, true);
                imageBitmap.recycle();
                imageBitmap = scaledBitmap;

                System.gc();
            } else {
                // b = BitmapFactory.decodeStream(in);
            }

        } catch (OutOfMemoryError error) {
            error.printStackTrace();
            System.out.println("exception in get bitma putility");
        } catch (Exception e) {
            System.out.println("exception in get bitma putility");
            e.printStackTrace();
        }
        return imageBitmap;
    }

    static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }
    }

    public static byte[] scaleImage(Context context, Uri photoUri)
            throws IOException {
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int rotatedWidth, rotatedHeight;
        int orientation = 0;// getOrientation(context, photoUri);

        if (orientation == 90 || orientation == 270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }

        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > MAX_IMAGE_DIMENSION
                || rotatedHeight > MAX_IMAGE_DIMENSION) {
            float widthRatio = ((float) rotatedWidth)
                    / ((float) MAX_IMAGE_DIMENSION);
            float heightRatio = ((float) rotatedHeight)
                    / ((float) MAX_IMAGE_DIMENSION);
            float maxRatio = Math.max(widthRatio, heightRatio);

            // Create the bitmap from file
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
        } else {
            srcBitmap = BitmapFactory.decodeStream(is);
        }
        is.close();

		/*
         * if the orientation is not 0 (or -1, which means we don't know), we
		 * have to do a rotation.
		 */
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0,
                    srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        /*
         * if (type.equals("image/png")) {
		 * srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); } else if
		 * (type.equals("image/jpg") || type.equals("image/jpeg")) {
		 * srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); }
		 */
        byte[] bMapArray = baos.toByteArray();
        baos.close();
        return bMapArray;
    }

    static int mMaxWidth, mMaxHeight;

    @SuppressWarnings("deprecation")
    public static Bitmap loadResizedImage(Context mContext, final File imageFile) {
        WindowManager windowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        mMaxWidth = display.getWidth();
        mMaxHeight = display.getHeight();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
        int scale = calculateInSampleSize(options, mMaxWidth, mMaxHeight);
        while (options.outWidth / scale > mMaxWidth
                || options.outHeight / scale > mMaxHeight) {
            scale++;
        }
        Bitmap bitmap = null;
        Bitmap scaledBitmap = null;
        if (scale > 1) {
            try {
                scale--;
                options = new BitmapFactory.Options();
                options.inSampleSize = scale;
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                options.inPurgeable = true;
                options.inTempStorage = new byte[16 * 100];
                bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(),
                        options);
                if (bitmap == null) {
                    return null;
                }

                // resize to desired dimensions
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                double newWidth;
                double newHeight;
                if ((double) width / mMaxWidth < (double) height / mMaxHeight) {
                    newHeight = mMaxHeight;
                    newWidth = (newHeight / height) * width;
                } else {
                    newWidth = mMaxWidth;
                    newHeight = (newWidth / width) * height;
                }

                scaledBitmap = Bitmap.createScaledBitmap(bitmap,
                        Math.round((float) newWidth),
                        Math.round((float) newHeight), true);
                bitmap.recycle();
                bitmap = scaledBitmap;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            }
            System.gc();
        } else {
            bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        }

        return rotateImage(bitmap, imageFile);
    }

    public static Bitmap rotateImage(final Bitmap bitmap,
                                     final File fileWithExifInfo) {
        if (bitmap == null) {
            return null;
        }
        Bitmap rotatedBitmap = bitmap;
        int orientation = 0;
        try {
            orientation = getImageOrientation(fileWithExifInfo
                    .getAbsolutePath());
            if (orientation != 0) {
                Matrix matrix = new Matrix();
                matrix.postRotate(orientation, (float) bitmap.getWidth() / 2,
                        (float) bitmap.getHeight() / 2);
                rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.recycle();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotatedBitmap;
    }

    public static int getImageOrientation(final String file) throws IOException {
        ExifInterface exif = new ExifInterface(file);
        int orientation = exif
                .getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return 0;
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
            default:
                return 0;
        }
    }

    public static Typeface Appttf(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "AE100132.TTF");
    }

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }


    // remove for preferences

    public static void removepreference(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(LIRA_TAXI_PREFERENCE, 0);
        settings.edit().remove(name).commit();
    }

    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public static boolean validateLastName(String lastName) {
        return lastName.matches("[a-zA-z]+([ '-][a-zA-Z]+)*");
    }

    /* public static void ShowAlertDialog(String msg)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
            builder.setTitle("SwipeMe");
            builder.setMessage(msg);
            builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });

    // Create the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();

        }
    */
    public static boolean validateFirstName(String firstName) {
        return firstName.matches("[A-Z][a-zA-Z]*");
    } // end

    /**
     * Function to show settings alert dialog
     */
    public static void showSettingsAlert(final Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public static String getMonth(String month) {

        switch (month) {
            case "1":
                return "January";
            case "2":
                return "February";
            case "3":
                return "March";
            case "4":
                return "April";
            case "5":
                return "May";
            case "6":
                return "June";
            case "7":
                return "July";
            case "8":
                return "August";
            case "9":
                return "September";
            case "10":
                return "October";
            case "11":
                return "November";
            case "12":
                return "December";
            default:
                return "January";
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


  /*  public static void resetPreferences(Context appcontext) {
        appcontext.getSharedPreferences(Utility.LIRA_TAXI_PREFERENCE, Context.MODE_PRIVATE).edit().clear().commit();
        myPrefrence = new MyPrefrence(appcontext);
        //	myDetail = myPrefrence.getDetail();
        myPrefrence.resetLoginPreference();
        Intent logout = new Intent(appcontext, RegisterActivity.class);
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        logout.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        appcontext.startActivity(logout);
    }*/

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

  /*  public static Drawable buildCounterDrawable(Context appContext, int count, int bgResourceId) {
        LayoutInflater inflater = LayoutInflater.from(appContext);
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

        return new BitmapDrawable(((Activity) appContext).getResources(), bitmap);
    }*/


    public static void checkIfPermissionsGranted(final Context appContext) {
        android.support.v7.app.AlertDialog alertDialog;
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(appContext);
        alertDialogBuilder.setMessage(appContext.getString(R.string.permission));
        alertDialogBuilder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                goToSettings(appContext);
            }
        });

        alertDialogBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(appContext.getResources().getColor(R.color.colorPrimary));
        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(appContext.getResources().getColor(R.color.colorPrimary));
    }

    private static void goToSettings(Context appContext) {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + appContext.getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        appContext.startActivity(myAppSettings);
    }

    public static void setUpToolbar_(Context appContext, String title) {
        final AppCompatActivity activity = (AppCompatActivity) appContext;
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar_actionbar);
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
     //  ((TextView) activity.findViewById(R.id.headet_text)).setText(title);
        //  actionBar.setTitle(title);
        actionBar.setHomeAsUpIndicator(R.mipmap.back);
    }


    public static void setUpToolbar(Context appContext, String title) {
        final AppCompatActivity activity = (AppCompatActivity) appContext;
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar_actionbar);
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
       // ((TextView) activity.findViewById(R.id.headet_text)).setText(title);

        //  actionBar.setTitle(title);
        actionBar.setHomeAsUpIndicator(R.mipmap.back);
    }




    public static void resetPreferences(Context appcontext) {
        // appcontext.getSharedPreferences(Utility.LIRA_TAXI_PREFERENCE, Context.MODE_PRIVATE).edit().clear().commit();
        Intent logout = new Intent(appcontext, LoginActivity.class);
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        logout.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        appcontext.startActivity(logout);
    }


    public static String convertNumber(String numberString, String string) {
        if (numberString.equalsIgnoreCase("")) {
            return "0";
        }

        if (Integer.parseInt(numberString) <= 999) {
            return numberString;
        }
        BigInteger number = new BigInteger(numberString);
        //  System.out.println(number+" is "+createString(number)+" should be "+string);
        return createString(number);
    }


    private static final String NAMES[] = new String[]{
            "k",
            "M",
            "G",
            "T",
            "P",
            "E",
            "Z",
            "Y",
           /* "Octillion",
            "Nonillion",
            "Decillion",
            "Undecillion",
            "Duodecillion",
            "Tredecillion",
            "Quattuordecillion",
            "Quindecillion",
            "Sexdecillion",
            "Septendecillion",
            "Octodecillion",
            "Novemdecillion",
            "Vigintillion",*/
    };
    private static final BigInteger THOUSAND = BigInteger.valueOf(1000);
    private static final NavigableMap<BigInteger, String> MAP;

    static {
        MAP = new TreeMap<BigInteger, String>();
        for (int i = 0; i < NAMES.length; i++) {
            MAP.put(THOUSAND.pow(i + 1), NAMES[i]);
        }
    }

    public static String createString(BigInteger number) {
        Map.Entry<BigInteger, String> entry = MAP.floorEntry(number);
        if (entry == null) {
            return "Nearly nothing";
        }
        BigInteger key = entry.getKey();
        BigInteger d = key.divide(THOUSAND);
        BigInteger m = number.divide(d);
        float f = m.floatValue() / 1000.0f;
        float rounded = ((int) (f * 100.0)) / 100.0f;
        if (rounded % 1 == 0) {
            return ((int) rounded) + " " + entry.getValue();
        }
        return rounded + " " + entry.getValue();
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Kartavya", null);
        return Uri.parse(path);
    }

    public static void ShareImage(Bitmap bm, Context context, String description) {
      //  String newString = description + "\n\n\nDownload Now:\nhttp://base3.engineerbabu.com:8282/BR_App/index/share.php/" + Utility.getSharedPreferences(appContext, Constant.REFERRAL_CODE);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "kartavya");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "");
        shareIntent.putExtra(Intent.EXTRA_STREAM, getImageUri(context, bm));
        ((Activity) context).startActivityForResult(Intent.createChooser(shareIntent, "Share with friends"), 123);
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = ((Activity) context).getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }


    public static Drawable buildCounterDrawable(Context appContext, int count, int bgResourceId) {
        LayoutInflater inflater = LayoutInflater.from(appContext);
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

        return new BitmapDrawable(((Activity) appContext).getResources(), bitmap);
    }
}

