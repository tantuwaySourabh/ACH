package com.ebabu.ach.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.view.WindowManager;
import android.widget.ImageView;
import com.ebabu.ach.Utils.PermissionsUtils;

import com.ebabu.ach.R;
import com.ebabu.ach.Utils.*;
import com.ebabu.ach.constants.IKeyConstants;
import com.ebabu.ach.customview.CustomEditText;
import com.soundcloud.android.crop.Crop;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupFirstActivity extends AppCompatActivity {
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;
    private Context context;
    private CustomEditText fName, lName, mobilebNum, email, qualification;
    public static File imageProfile = null;
    private Uri mCameraImageUri;
    private int isForCamera=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_first);
        context = this;
        initView();


    }

    private void initView() {
        Utils.setUpToolbar(context, getString(R.string.signup), false);
        fName = (CustomEditText) findViewById(R.id.et_first_name);
        lName = (CustomEditText) findViewById(R.id.et_last_name);
        mobilebNum = (CustomEditText) findViewById(R.id.et_mobile_num);
        email = (CustomEditText) findViewById(R.id.et_email);
        qualification = (CustomEditText) findViewById(R.id.et_qualification);

        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (areFieldValid()) {
                    Intent intent = new Intent(context, SignupSecondActivity.class);
                    intent.putExtra(IKeyConstants.FIRSTNAME, fName.getText().toString().trim());
                    intent.putExtra(IKeyConstants.LASTNAME, lName.getText().toString().trim());
                    intent.putExtra(IKeyConstants.EMAIL, email.getText().toString().trim());
                    intent.putExtra(IKeyConstants.MOBILE, mobilebNum.getText().toString().trim());
                    intent.putExtra(IKeyConstants.QUALIFICATION, qualification.getText().toString().trim());
                    startActivity(intent);
                }
            }
        });

        ((CircleImageView)findViewById(R.id.btn_profile_pic)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /*AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = ((Activity)context).getLayoutInflater().inflate(R.layout.layout_select_image, null);
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                view.findViewById(R.id.imvCamera).setOnClickListener(new View.OnClickListener( ) {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        openCamera();
                    }
                });
                view.findViewById(R.id.imvGallery).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        alertDialog.dismiss();
                        openGallery();
                    }
                });*/
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

        });}

    private void openGallery() {
        isForCamera=2;

        if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW) {
            if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, "Write External Storage")) {
                return;
            }
        }

        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PICK_FROM_FILE);

    }

    private void openCamera() {
        isForCamera=1;
        if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW) {
            if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.CAMERA, "Camera")) {
                return;
            }

            if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW) {
                if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, "Write External Storage")) {
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
        Log.d("level", "onActivityResult: ");
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

    private boolean areFieldValid() {
        if (fName.getText().toString().trim().length() <= 0) {
            fName.setError(getString(R.string.empty_firstname));
            return false;
        }
        if (!Utils.isNameValid(fName.getText().toString())) {
            fName.setError("Enter Valid First Name");
            return false;
        }
        if (lName.getText().toString().trim().length() <= 0) {
            lName.setError(getString(R.string.empty_lastname));
            return false;
        }
        if (!Utils.isNameValid(lName.getText().toString())) {
            lName.setError("Enter Valid Last Name");
            return false;
        }
        if (email.getText().toString().trim().length() <= 0) {
            email.setError("Enter Email Address");
            return false;
        }
        if (!Utils.isValidEmail(email.getText().toString())) {
            email.setError("Enter Valid Email");
            return false;
        }
        if (mobilebNum.getText().toString().trim().length() <= 0) {
            mobilebNum.setError(getString(R.string.empty_mobilenum));
            return false;
        }
        if (mobilebNum.getText().toString().trim().length() != 10) {
            mobilebNum.setError(getString(R.string.invalid_mobilenum));
            return false;
        }
        if (qualification.getText().toString().trim().length() <= 0) {
            qualification.setError(getString(R.string.empty_qualification));
        }

        return true;
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

            if (intent != null){
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
                ((ImageView)findViewById(R.id.btn_profile_pic)).setImageBitmap(bitmap);
                imageProfile = new File(Environment.getExternalStorageDirectory()+ File.separator + "final.jpeg");
                ByteArrayOutputStream datasecond = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,80, datasecond);
                byte[] bitmapdata = datasecond.toByteArray();

                if (imageProfile.exists())
                    imageProfile.delete();
                try {
                    imageProfile.createNewFile();
                    FileOutputStream fo = new FileOutputStream(imageProfile);
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

}