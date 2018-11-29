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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.tasmim.a7afalaty.model.AddServiceModel;
import com.tasmim.a7afalaty.model.HomeModel;
import com.tasmim.a7afalaty.model.MyServicesModel;
import com.tasmim.a7afalaty.model.UpdateServiceModel;
import com.tasmim.a7afalaty.network.ApiClient;
import com.tasmim.a7afalaty.network.ApiService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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

public class UpdateService extends AppCompatActivity implements View.OnClickListener {

    ApiService apiService;
    MyServicesModel.Datum datum;
    List<HomeModel.Datum> list;
    MultipartBody.Part body;
    NetworkTester networkTester = new NetworkTester(this);
    List<String> sections_list = new ArrayList<>();

    EditText update_service_name, update_service_details;
    Spinner update_service_section;
    Button update_service_btn;
    SpotsDialog dialog;
    ImageView service_imageView;
    TextView back, update_service_location;
    int selected_item_id;
    int selected_pos;

    double latitude, longtitude;
    String address, cityName, streetName;
    int PLACE_PICKER_REQUEST = 2000;
    private static final int INTENT_REQUEST_CODE = 12345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_service);

        if (getIntent().getExtras() != null) {
            datum = getIntent().getExtras().getParcelable("service_data");
        }
        back = findViewById(R.id.updateService_back_txt_id);
        service_imageView = findViewById(R.id.update_service_img_id);
        update_service_name = findViewById(R.id.update_service_name_ed);
        update_service_details = findViewById(R.id.update_service_details_ed);
        update_service_section = findViewById(R.id.update_section_type);
        update_service_location = findViewById(R.id.update_service_location_txt_id);
        update_service_btn = findViewById(R.id.update_service_btn_id);

        Picasso.with(UpdateService.this).load(datum.getImage()).into(service_imageView);
        update_service_name.setText(datum.getTitle());
        update_service_details.setText(datum.getDisc());
        update_service_section.setSelection(datum.getCategoryId());
        selected_item_id = datum.getCategoryId();
        latitude = Double.parseDouble(datum.getLat());
        longtitude = Double.parseDouble(datum.getLng());

        getServiceSections(datum.getCategoryName());

        // --------------------------------------------------------------------

        update_service_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(UpdateService.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        update_service_btn.setOnClickListener(this);
        service_imageView.setOnClickListener(this);
    }

    private void getServiceSections(final String categoryName) {
        apiService = ApiClient.getClient().create(ApiService.class);
        Call<HomeModel> call = apiService.homeData();
        call.enqueue(new Callback<HomeModel>() {
            @Override
            public void onResponse(Call<HomeModel> call, Response<HomeModel> response) {
                HomeModel homeModel = response.body();
                String status = homeModel.getStatus();
                list = homeModel.getData();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getName().equals(categoryName)) selected_pos = i;
                    sections_list.add(list.get(i).getName());
                }
//                Toast.makeText(UpdateService.this, "" + sections_list.size(), Toast.LENGTH_SHORT).show();
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(UpdateService.this, android.R.layout.simple_dropdown_item_1line, sections_list);
                update_service_section.setAdapter(adapter);
                update_service_section.setSelection(selected_pos);
                update_service_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String selected_item = update_service_section.getSelectedItem().toString();
                        selected_item_id = list.get(i).getId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onFailure(Call<HomeModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.update_service_btn_id:
                if (!FUtilsValidation.isEmpty(update_service_name, getString(R.string.required))
                        && !FUtilsValidation.isEmpty(update_service_details, getString(R.string.required))
                        ) {

                    if (networkTester.isNetworkAvailable()) {
                        dialog = new SpotsDialog(UpdateService.this, getString(R.string.updatting), R.style.Custom);
                        dialog.show();

                        Log.e("id: ", datum.getId() + "");
                        Log.e("title: ", update_service_name.getText().toString());
                        Log.e("user_id: ", Home.user_id + "");
                        Log.e("section_id: ", list.get(selected_pos).getId() + "");
                        Log.e("details: ", update_service_details.getText().toString());
                        Log.e("image: ", body + "");

                        RequestBody service_id_Part = RequestBody.create(MultipartBody.FORM, datum.getId() + "");
                        RequestBody userId_Part = RequestBody.create(MultipartBody.FORM, Home.user_id + "");
                        RequestBody section_id_Part = RequestBody.create(MultipartBody.FORM, selected_item_id + "");
                        RequestBody title_Part = RequestBody.create(MultipartBody.FORM, update_service_name.getText().toString().trim());
                        RequestBody details_Part = RequestBody.create(MultipartBody.FORM, update_service_details.getText().toString().trim());
                        RequestBody lat_Part = RequestBody.create(MultipartBody.FORM, latitude + "");
                        RequestBody lng_Part = RequestBody.create(MultipartBody.FORM, longtitude + "");

                        dialog = new SpotsDialog(UpdateService.this, getString(R.string.updatting), R.style.Custom);
                        dialog.show();

                        apiService = ApiClient.getClient().create(ApiService.class);
                        Call<UpdateServiceModel> call = apiService.updateService(datum.getId() + "", userId_Part, section_id_Part, title_Part, details_Part, lat_Part, lng_Part, body);
                        call.enqueue(new Callback<UpdateServiceModel>() {
                            @Override
                            public void onResponse(Call<UpdateServiceModel> call, Response<UpdateServiceModel> response) {
                                dialog.dismiss();
                                UpdateServiceModel updateServiceModel = response.body();
                                String status = updateServiceModel.getStatus();
                                if (status.equals("success")) {
                                    Toasty.success(UpdateService.this, "تم التحديث بنجاح", 1500).show();
                                    finish();
                                } else {
                                    Toasty.error(UpdateService.this, "حدث خطأ ما, حاول مرة اخرى!", 1500).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<UpdateServiceModel> call, Throwable t) {
                                dialog.dismiss();
                                t.printStackTrace();
                            }
                        });

                    }
                }
                break;
            case R.id.update_service_img_id:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                try {
                    startActivityForResult(intent, INTENT_REQUEST_CODE);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {

            if (requestCode == INTENT_REQUEST_CODE) {
                try {
                    android.net.Uri selectedImage = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    service_imageView.setImageBitmap(bitmap);
                    InputStream is = getContentResolver().openInputStream(data.getData());

                    createMainImageMultiPartFile(getBytes(is));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == PLACE_PICKER_REQUEST) {
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

    private void createMainImageMultiPartFile(byte[] imageBytes) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
        body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
    }
}