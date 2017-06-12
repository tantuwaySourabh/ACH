package com.ebabu.ach.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.ebabu.ach.Utils.AppPreference;
import com.ebabu.ach.Utils.Utils;
import com.ebabu.ach.constants.IKeyConstants;
import com.ebabu.ach.constants.IUrlConstants;
import com.ebabu.ach.customview.CustomEditText;
import com.ebabu.ach.customview.CustomTextView;
import com.ebabu.ach.customview.MyLoading;
import com.soundcloud.android.crop.Crop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private static final String TAG = "TAG";
    private CustomTextView mobilebNum;
    private CustomEditText fName, lName, email, qualification, address, city, zipCode, state, country;
    private ImageView ivProfile, ivCertificate;
    private int type;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;
    public static File imageProfile = null, imageDoc = null;
    private Uri mCameraImageUri;
    private AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        context = this;
        initView();
    }

    public void initView() {
        aq = new AQuery(context);
        Utils.setUpToolbar(context, "Edit Profile", true);

        fName = (CustomEditText) findViewById(R.id.et_first_name);
        lName = (CustomEditText) findViewById(R.id.et_last_name);
        mobilebNum = (CustomTextView) findViewById(R.id.et_mobile_num);
        email = (CustomEditText) findViewById(R.id.et_email);
        qualification = (CustomEditText) findViewById(R.id.et_qualification);
        ivProfile = (ImageView) findViewById(R.id.profile_pic);
        ivCertificate = (ImageView) findViewById(R.id.certificate);

        address = (CustomEditText) findViewById(R.id.et_address);
        city = (CustomEditText) findViewById(R.id.et_city);
        zipCode = (CustomEditText) findViewById(R.id.et_zip_code);
        state = (CustomEditText) findViewById(R.id.et_state);
        country = (CustomEditText) findViewById(R.id.et_country);

        fName.setText(AppPreference.getInstance(context).getFirstName());
        lName.setText(AppPreference.getInstance(context).getLastName());
        email.setText(AppPreference.getInstance(context).getEmail());
        mobilebNum.setText(AppPreference.getInstance(context).getMobile());
        qualification.setText(AppPreference.getInstance(context).getDegree());
        address.setText(AppPreference.getInstance(context).getAddress());
        city.setText(AppPreference.getInstance(context).getCity());
        zipCode.setText(AppPreference.getInstance(context).getZipCode());
        state.setText(AppPreference.getInstance(context).getState());
        country.setText(AppPreference.getInstance(context).getCountry());

        aq.id(ivProfile).image((AppPreference.getInstance(context).getProfilePic().trim()), true, true, 300, R.mipmap.no_image);
        try {
         //   JSONArray ImageArray = new JSONArray(AppPreference.getInstance(context).getCertificate().trim());
         //   aq.id(ivCertificate).image(ImageArray.get(0).toString(), true, true, 300, R.mipmap.no_image);
            aq.id(ivCertificate).image(AppPreference.getInstance(context).getCertificate(), true, true, 300, R.mipmap.no_image);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ivProfile.setOnClickListener(this);
        findViewById(R.id.btn_upload_certificate).setOnClickListener(this);
        findViewById(R.id.btn_update).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.profile_pic) {
            type = 1;
            getImage();
        } else if (v.getId() == R.id.btn_upload_certificate) {
            type = 2;
            getImage();
        } else if (v.getId() == R.id.btn_update)
            if (areFieldValid())
                updateProfile();

    }

    public void getImage() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.layout_select_image, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.imvCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                mCameraImageUri = Uri.fromFile(f);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraImageUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);

            }
        });
        view.findViewById(R.id.imvGallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, PICK_FROM_FILE);

            }
        });
        alertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FROM_CAMERA) {
                if (mCameraImageUri != null)
                    beginCrop(mCameraImageUri);

            } else if (requestCode == PICK_FROM_FILE) {

                mCameraImageUri = data.getData();

                if (mCameraImageUri != null)
                    beginCrop(mCameraImageUri);


            } else if (requestCode == Crop.REQUEST_CROP) {
                new ImageCompressionTask(data).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    }

    private void beginCrop(Uri source) {
        File f = new File(android.os.Environment.getExternalStorageDirectory(), "cropped.jpg");
        if (f.exists())
            f.delete();
        try {
            f.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Uri destination = Uri.fromFile(f);
        Crop.of(source, destination).asSquare().start((Activity) context);
    }

    private class ImageCompressionTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;
        private Intent intent;
        Bitmap bitmap = null;

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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (bitmap != null) {

                File getImage = new File(Environment.getExternalStorageDirectory() + File.separator + "final.jpeg");
                ByteArrayOutputStream datasecond = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, datasecond);
                byte[] bitmapdata = datasecond.toByteArray();

                if (getImage.exists())
                    getImage.delete();

                try {
                    getImage.createNewFile();
                    FileOutputStream fo = new FileOutputStream(getImage);
                    fo.write(bitmapdata);
                    fo.close();

                    if (type == 1) {
                        ivProfile.setImageBitmap(bitmap);
                        imageProfile = getImage;
                    } else {
                        ivCertificate.setImageBitmap(bitmap);
                        imageDoc = getImage;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }


  /*  aq.id(ivProfile).image((AppPreference.getInstance(context).getProfilePic().trim()), true, true, 300, R.mipmap.no_image);
    try {
        JSONArray ImageArray=new JSONArray(AppPreference.getInstance(context).getCertificate().trim());
        aq.id(ivCertificate).image(ImageArray.get(0).toString(), true, true, 300, R.mipmap.no_image);
    } catch (Exception e) {
        e.printStackTrace();
    } */


    private void updateProfile() {
        final MyLoading myLoading = new MyLoading(context);
        myLoading.show("Updating");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("first_name", fName.getText().toString().trim());
        params.put("last_name", lName.getText().toString().trim());
        params.put("email", email.getText().toString().trim());
        params.put("degree", qualification.getText().toString().trim());
        params.put("address", address.getText().toString().trim());
        params.put("city", city.getText().toString().trim());
        params.put("zip_code", zipCode.getText().toString().trim());
        params.put("state", state.getText().toString().trim());
        params.put("country", country.getText().toString().trim());

        if (imageProfile != null)
            params.put("profile_pic", imageProfile);
        else
            params.put("profile_pic", IKeyConstants.EMPTY);

        if (imageDoc != null) {
            params.put("file[0]", imageDoc);
        } else {
            params.put("file[0]", IKeyConstants.EMPTY);
        }

        new AQuery(context).ajax(IUrlConstants.UPDATE_PROFILE, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {

                if (json != null) {
                    try {
                        if ((IKeyConstants.SUCCESS).equalsIgnoreCase(json.getString("status"))) {
                            JSONObject data = null;
                            try {
                                data = json.getJSONObject("data");
                                AppPreference.getInstance(context).setFirstName(data.getString("firstname"));
                                AppPreference.getInstance(context).setLastName(data.getString("lastname"));
                                AppPreference.getInstance(context).setEmail(data.getString("email"));
                               // AppPreference.getInstance(context).setMobile(data.getString("mobile"));
                                AppPreference.getInstance(context).setDegree(data.getString("degree"));
                                AppPreference.getInstance(context).setCity(data.getString("city"));
                                AppPreference.getInstance(context).setZipCode(data.getString("zip_code"));
                                AppPreference.getInstance(context).setState(data.getString("state"));
                                AppPreference.getInstance(context).setAddress(data.getString("address"));
                                AppPreference.getInstance(context).setCountry(data.getString("country"));
                                AppPreference.getInstance(context).setProfilePic(data.getString("profile_pic"));
                                if (data.getJSONArray("certificate_file").length() > 0) {
                                    AppPreference.getInstance(context).setCertificate(data.getJSONArray("certificate_file").getString(0));
                                Log.d("getCertificate ",AppPreference.getInstance(context).getCertificate());
                                }

                                myLoading.dismiss();
                                Toast.makeText(context, "Updating Done", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(context, "message =" + json.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                }
                myLoading.dismiss();
            }
        }.method(AQuery.METHOD_POST).header("secret_key", AppPreference.getInstance(context).getSecretKey()));
    }

    private Boolean areFieldValid() {
        if (fName.getText().toString().trim().length() <= 0) {
            fName.setError(getString(R.string.empty_firstname));
            return false;
        }
        if (lName.getText().toString().trim().length() <= 0) {
            lName.setError(getString(R.string.empty_lastname));
            return false;
        }

        if (qualification.getText().toString().trim().length() <= 0) {
            qualification.setError(getString(R.string.empty_qualification));
        }


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
        if (state.getText().toString().trim().length() <= 0) {
            state.setError(getString(R.string.empty_state));
            return false;
        }
        if (country.getText().toString().trim().length() <= 0) {
            address.setError(getString(R.string.empty_country));
            return false;
        }
        return true;
    }

}