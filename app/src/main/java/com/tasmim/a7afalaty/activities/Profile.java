package com.tasmim.a7afalaty.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fourhcode.forhutils.FUtilsValidation;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.squareup.picasso.Picasso;
import com.tasmim.a7afalaty.Home;
import com.tasmim.a7afalaty.NetworkTester;
import com.tasmim.a7afalaty.R;
import com.tasmim.a7afalaty.model.LoginModel;
import com.tasmim.a7afalaty.model.ProfileModel;
import com.tasmim.a7afalaty.network.ApiClient;
import com.tasmim.a7afalaty.network.ApiService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity {

    EditText name_ed, phone_ed, email_ed, pass_ed;
    ImageView profile_user_img;
    TextView back, userName_txt, change_location;
    Button save_btn;
    int PLACE_PICKER_REQUEST = 100;
    private static final int INTENT_REQUEST_CODE = 1500;
    double latitude = Double.parseDouble(Home.user_Data.getLat());
    double longtitude = Double.parseDouble(Home.user_Data.getLat());
    String address, cityName, streetName;
    MultipartBody.Part body;

    ApiService apiService;
    SpotsDialog dialog;
    NetworkTester networkTester = new NetworkTester(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        back = findViewById(R.id.profile_back_txt_id);
        name_ed = findViewById(R.id.profile_name_ed);
        email_ed = findViewById(R.id.profile_email_ed);
        phone_ed = findViewById(R.id.profile_phone_ed);
        pass_ed = findViewById(R.id.profile_pass_ed);
        profile_user_img = findViewById(R.id.profile_user_image);
        userName_txt = findViewById(R.id.profile_userName_txt_id);
        change_location = findViewById(R.id.change_location_txt_id);
        save_btn = findViewById(R.id.profile_save_btn);

        Picasso.with(Profile.this).load(Home.user_Data.getAvatar()).into(profile_user_img);
        userName_txt.setText(Home.user_Data.getName());
        name_ed.setText(Home.user_Data.getName());
        email_ed.setText(Home.user_Data.getEmail());
        phone_ed.setText(Home.user_Data.getPhone());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        profile_user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");

                try {
                    startActivityForResult(intent, INTENT_REQUEST_CODE);

                } catch (ActivityNotFoundException e) {

                    e.printStackTrace();
                }
            }
        });
        change_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(Profile.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        profile_user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                try {
                    startActivityForResult(intent, INTENT_REQUEST_CODE);

                } catch (ActivityNotFoundException e) {

                    e.printStackTrace();
                }
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });
    }

    private void updateData() {

        if (!FUtilsValidation.isEmpty(name_ed, getString(R.string.required))
                && !FUtilsValidation.isEmpty(email_ed, getString(R.string.required))
                && FUtilsValidation.isValidEmail(email_ed, getString(R.string.enter_valid_email))
                && !FUtilsValidation.isEmpty(phone_ed, getString(R.string.required))
                ) {
            if (networkTester.isNetworkAvailable()) {
                dialog = new SpotsDialog(Profile.this, getString(R.string.updatting), R.style.Custom);
                dialog.show();
                RequestBody id_Part = RequestBody.create(MultipartBody.FORM, Home.user_Data.getId() + "");
                RequestBody NamePart = RequestBody.create(MultipartBody.FORM, name_ed.getText().toString().trim());
                RequestBody Email_Part = RequestBody.create(MultipartBody.FORM, email_ed.getText().toString().trim());
                RequestBody phone_Part = RequestBody.create(MultipartBody.FORM, phone_ed.getText().toString().trim());
                RequestBody pass_Part = RequestBody.create(MultipartBody.FORM, pass_ed.getText().toString().trim() + "");
                RequestBody lat_Part = RequestBody.create(MultipartBody.FORM, latitude + "");
                RequestBody lng_Part = RequestBody.create(MultipartBody.FORM, longtitude + "");

                apiService = ApiClient.getClient().create(ApiService.class);
                Call<ProfileModel> call = apiService.update_profile(id_Part, NamePart, Email_Part, phone_Part, pass_Part, lat_Part, lng_Part, body);
                call.enqueue(new Callback<ProfileModel>() {
                    @Override
                    public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                        dialog.dismiss();
                        ProfileModel profileModel = response.body();
                        String status = profileModel.getStatus();
                        int user_id = profileModel.getData().getId();

                        if (status.equals("success")) {
                            Toasty.success(Profile.this, getString(R.string.dataChangedSuccesfully), 1500).show();
                            Intent intent = new Intent(Profile.this, Home.class);
                            intent.putExtra("user_id", user_id);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ProfileModel> call, Throwable t) {
                        dialog.dismiss();
                        t.printStackTrace();
                    }
                });
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK && data != null) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                address = String.format("%s", place.getAddress());
                latitude = place.getLatLng().latitude;
                longtitude = place.getLatLng().longitude;

                Log.e("lat", latitude + "");
                Log.e("lan", longtitude + "");
                Log.e("address", address);
                Geocoder gcd = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = gcd.getFromLocation(latitude, longtitude, 1);
                    if (addresses.size() > 0 && addresses != null) {
                        cityName = addresses.get(0).getSubAdminArea();
                        int maxAddressLine = addresses.get(0).getMaxAddressLineIndex();
                        streetName = addresses.get(0).getFeatureName();
                        String admin_adrea = addresses.get(0).getAdminArea();

                        Log.e("cityName: ", addresses.get(0).getAddressLine(0));
                        Log.e("stret1: ", addresses.get(0).getCountryName());
                        Log.e("stret2: ", addresses.get(0).getLocality());
                        Log.e("stret3: ", addresses.get(0).getAdminArea());
                        Log.e("stret4: ", addresses.get(0).getAddressLine(maxAddressLine));
                        Log.e("stret5: ", addresses.get(0).getFeatureName());
                        Log.e("stret7: ", addresses.get(0).getSubAdminArea());

                    } else {
                        // do your stuff
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == INTENT_REQUEST_CODE) {

                if (resultCode == RESULT_OK && data != null) {

                    try {
                        profile_user_img.setImageURI(null);
                        android.net.Uri selectedImage = data.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        profile_user_img.setImageBitmap(bitmap);
                        InputStream is = getContentResolver().openInputStream(data.getData());

                        createMultiPartFile(getBytes(is));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();
        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }
        return byteBuff.toByteArray();
    }

    private void createMultiPartFile(byte[] imageBytes) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
        body = MultipartBody.Part.createFormData("avatar", "image.jpg", requestFile);
    }
}
