package com.ebabu.ach.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ebabu.ach.R;
import com.ebabu.ach.Utils.*;
import com.ebabu.ach.Utils.PermissionsUtils;
import com.ebabu.ach.constants.IKeyConstants;
import com.ebabu.ach.constants.IUrlConstants;
import com.ebabu.ach.customview.CustomEditText;
import com.ebabu.ach.customview.CustomTextView;
import com.ebabu.ach.customview.MyLoading;
import com.soundcloud.android.crop.Crop;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SignupSecondActivity extends AppCompatActivity {

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;
    private Context context;
    private ImageView ivCertificate;
  //public  File imageCertificate = null;
    public static File imageCertificate = null;

    private CustomEditText address, city, state, country, zipCode;
    private String fName, lName, mobileNum, email, qualification;
    private CustomEditText certificate;
    private Uri mCameraImageUri;
    private int isForCamera=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_second);
        context = this;
        initView();
    }

    private void initView() {
        Utils.setUpToolbar(context, getString(R.string.signup), false);

        address = (CustomEditText) findViewById(R.id.et_address);
        city = (CustomEditText) findViewById(R.id.et_city);
        zipCode = (CustomEditText) findViewById(R.id.et_zip_code);
        state = (CustomEditText) findViewById(R.id.et_state);
        country = (CustomEditText) findViewById(R.id.et_country);
        ivCertificate = (ImageView) findViewById(R.id.iv_certificate);
        certificate = (CustomEditText) findViewById(R.id.btn_upload_certificate);

        Intent intent = getIntent();
        fName = intent.getStringExtra(IKeyConstants.FIRSTNAME);
        lName = intent.getStringExtra(IKeyConstants.LASTNAME);
        mobileNum = intent.getStringExtra(IKeyConstants.MOBILE);
        email = intent.getStringExtra(IKeyConstants.EMAIL);
        qualification = intent.getStringExtra(IKeyConstants.QUALIFICATION);

        findViewById(R.id.btn_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (areFieldValid()) {
                    sendOTP();
                }
            }
        });
        findViewById(R.id.iv_certificate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
                final AlertDialog alertDialog;
                alertDialogBuilder.setMessage(context.getString(R.string.selectstorage));
                alertDialogBuilder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        openCamera();
                    }
                });
                alertDialogBuilder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        openGallery();
                    }
                });
                alertDialog = alertDialogBuilder.create();;
                alertDialog.show();
                alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorPrimary));
                alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }



        });
    }
    private void openGallery() {
        isForCamera=2;

        if (Build.VERSION.SDK_INT >= com.ebabu.ach.Utils.PermissionsUtils.SDK_INT_MARSHMALLOW) {
            if (!com.ebabu.ach.Utils.PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, "Write External Storage")) {
                return;
            }
        }

        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PICK_FROM_FILE);

    }

    private void openCamera() {
        isForCamera=1;
        if (Build.VERSION.SDK_INT >= com.ebabu.ach.Utils.PermissionsUtils.SDK_INT_MARSHMALLOW) {
            if (!com.ebabu.ach.Utils.PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.CAMERA, "Camera")) {
                return;
            }

            if (Build.VERSION.SDK_INT >= com.ebabu.ach.Utils.PermissionsUtils.SDK_INT_MARSHMALLOW) {
                if (!com.ebabu.ach.Utils.PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, "Write External Storage")) {
                    return;
                }
            }

        }

        if (Build.VERSION.SDK_INT <= PermissionsUtils.SDK_INT_MARSHMALLOW) {
            mCameraImageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/temp.jpg"));
        } else {
           /* mImageCaptureUri = FileProvider.getUriForFile(ProfileActivity.this,
                     "com.mateapp.provider",
                    createImageFile());*/
            mCameraImageUri = FileProvider.getUriForFile(context, getApplicationContext().getPackageName() + ".provider", createImageFile());
        }


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
        //  mCameraImageUri = Uri.fromFile(f);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraImageUri);
        //((Activity) context).startActivityForResult(intent, PICK_FROM_CAMERA);
        startActivityForResult(intent, PICK_FROM_CAMERA);

    }
    private File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        //  mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FROM_CAMERA) {
                if ( mCameraImageUri != null )
                    beginCrop(mCameraImageUri);
            } else if (requestCode == PICK_FROM_FILE) {
                mCameraImageUri = data.getData();

                if ( mCameraImageUri != null )
                    beginCrop(mCameraImageUri);

            } else if (requestCode == Crop.REQUEST_CROP) {
                new ImageCompressionTask(data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            }
        }

    }

    private void beginCrop(Uri source) {
        File f = new File(android.os.Environment.getExternalStorageDirectory(), "cropped.jpg");
        if(f.exists())
            f.delete();
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri destination = Uri.fromFile(f);
        Crop.of(source, destination).asSquare().start((Activity) context);
    }

    private class ImageCompressionTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;
        private Intent intent;
        Bitmap bitmap;
        public ImageCompressionTask(Intent intent) {
            this.intent = intent;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();

            if (intent != null) {
                Uri imageUri = Crop.getOutput(intent);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (bitmap != null) {
                ivCertificate.setImageBitmap(bitmap);
                imageCertificate = new File(Environment.getExternalStorageDirectory()+ File.separator + "certificate.jpeg");
                ByteArrayOutputStream datasecond = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100, datasecond);
                byte[] bitmapdata = datasecond.toByteArray();

                if (imageCertificate.exists())
                    imageCertificate.delete();

                try {
                    imageCertificate.createNewFile();
                    FileOutputStream fo = new FileOutputStream(imageCertificate);
                    fo.write(bitmapdata);
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

        }
    }

    private void sendOTP() {
        final MyLoading myLoading = new MyLoading(context);
        myLoading.show("Sending OTP");

        final JSONObject input = new JSONObject();
        JSONObject data = new JSONObject();
        try{
            data.put("first_name", fName);
            data.put("last_name", lName);
            data.put("email", email);
            data.put("mobile", mobileNum);
            data.put("degree", qualification);
            data.put("address", address.getText().toString().trim());
            data.put("city", city.getText().toString().trim());
            data.put("zip_code", zipCode.getText().toString().trim());
            data.put("state", state.getText().toString().trim());
            data.put("country", country.getText().toString().trim());
            input.put("doctor_info", data);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, Object> param = new HashMap<String, Object>();

        param.put("doctor_detail", input.toString());

        if (SignupFirstActivity.imageProfile != null)
            param.put("profile_pic", SignupFirstActivity.imageProfile);
         else
            param.put("profile_pic", IKeyConstants.EMPTY);

        if (imageCertificate!= null){
            param.put("file[0]", imageCertificate);
        } else {
            param.put("file[0]", IKeyConstants.EMPTY);
        }

        new AQuery(context).ajax(IUrlConstants.REGISTER_USER, param, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {

                myLoading.dismiss();

                if (json != null) {
                    try {
                        if ((IKeyConstants.SUCCESS).equalsIgnoreCase(json.getString("status"))) {
                            Toast.makeText(context, "message = " + json.getString("message"), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, OtpActivity.class);
                            intent.putExtra(IKeyConstants.USERID, json.getString("user_id"));
                            startActivity(intent);
                        } else {
                            Toast.makeText(context, "ERROR " + json.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "EMPTY JSON", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }.method(AQuery.METHOD_POST));
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionsUtils.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Log.d("Checking", "permissions: " + Arrays.asList(permissions) + ", grantResults:" + Arrays.asList(grantResults));
                if (PermissionsUtils.getInstance(context).areAllPermissionsGranted(grantResults)) {
                    if (isForCamera == 1) {
                        openCamera();
                    } else if (isForCamera == 2) {
                        openGallery();
                    } else if (isForCamera == 3) {
                        // completeProfileclick();
                    }
                } else {
                    Utility.checkIfPermissionsGranted(context);
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private boolean areFieldValid() {
        if (address.getText().toString().trim().length() <= 0) {
            address.setError(getString(R.string.empty_address));
            return false;
        }
        if (city.getText().toString().trim().length() <= 0) {
            city.setError(getString(R.string.empty_city));
            return false;
        }
        if (zipCode.getText().toString().trim().length() <= 0) {
            zipCode.setError(getString(R.string.empty_zip_code));
            return false;
        }
        if (state.getText().toString().trim().length() <= 0){
            state.setError(getString(R.string.empty_state));
            return false;
        }
        if (country.getText().toString().trim().length() <= 0){
            address.setError(getString(R.string.empty_country));
            return false;
        }

        if (mCameraImageUri==null){
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
            final AlertDialog alertDialog;
            alertDialogBuilder.setMessage(context.getString(R.string.upload_certificate));
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {

                }
            });
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorPrimary));
            return false;
        }
        if(!Utils.isNetworkConnected(context)){
            Toast.makeText(context,"Check your Network Connection",Toast.LENGTH_SHORT).show();
        }

        return true;
    }
}
