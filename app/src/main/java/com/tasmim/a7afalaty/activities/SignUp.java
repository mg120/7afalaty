package com.tasmim.a7afalaty.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fourhcode.forhutils.FUtilsValidation;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tasmim.a7afalaty.Home;
import com.tasmim.a7afalaty.NetworkTester;
import com.tasmim.a7afalaty.R;
import com.tasmim.a7afalaty.model.SendCodeModel;
import com.tasmim.a7afalaty.model.SignModel;
import com.tasmim.a7afalaty.network.ApiClient;
import com.tasmim.a7afalaty.network.ApiService;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    EditText name_ed, email_ed, phone_ed, pass_ed;
    TextView getLocation_txt, login_txt;
    Button sign_btn;
    int PLACE_PICKER_REQUEST = 10;
    double latitude, longtitude;
    String address, cityName, streetName;
    int sign_type;

    ApiService apiService;
    SpotsDialog dialog;
    NetworkTester networkTester = new NetworkTester(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name_ed = findViewById(R.id.sign_name_ed);
        email_ed = findViewById(R.id.sign_email_ed);
        phone_ed = findViewById(R.id.sign_phone_ed);
        pass_ed = findViewById(R.id.sign_pass_ed);
        getLocation_txt = findViewById(R.id.get_location_txt_id);
        sign_btn = findViewById(R.id.sign_btn_id);
        login_txt = findViewById(R.id.login_txt_id);

        login_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, Login.class));
            }
        });

        getLocation_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(SignUp.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        sign_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(final View view) {
        String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        // get token from Firebase
        String token = FirebaseInstanceId.getInstance().getToken();

        if (!FUtilsValidation.isEmpty(name_ed, getString(R.string.required))
                && !FUtilsValidation.isEmpty(email_ed, getString(R.string.required))
                && FUtilsValidation.isValidEmail(email_ed, getString(R.string.enter_valid_email))
                && !FUtilsValidation.isEmpty(phone_ed, getString(R.string.required))
                && !FUtilsValidation.isEmpty(pass_ed, getString(R.string.required))
                ) {
            if (networkTester.isNetworkAvailable()) {
                dialog = new SpotsDialog(SignUp.this, getString(R.string.signup), R.style.Custom);
                dialog.show();
                apiService = ApiClient.getClient().create(ApiService.class);
                Call<SignModel> call = apiService.user_sgnUp(name_ed.getText().toString().trim(), phone_ed.getText().toString().trim(), email_ed.getText().toString().trim(), pass_ed.getText().toString().trim(), sign_type + "", latitude + "", longtitude + "", token);
                call.enqueue(new Callback<SignModel>() {
                    @Override
                    public void onResponse(Call<SignModel> call, Response<SignModel> response) {
                        dialog.dismiss();
                        SignModel signModel = response.body();
                        String status = signModel.getStatus();
                        if (status.equals("success")) {
                            SignModel.Data data = signModel.getData();
                            final int id = data.getId();
                            final int text_msg = data.getCode();
//                            String phone_num = data.getPhone();
                            Toasty.success(SignUp.this, getString(R.string.register_success), 1500).show();
//                            startActivity(new Intent(SignUp.this, Login.class));
//                            try {
//                                SmsManager smsManager = SmsManager.getDefault();
//                                smsManager.sendTextMessage(phone_num, null, text_msg + "", null, null);
//                            } catch (Exception ex) {
//                                ex.printStackTrace();
//                            }
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignUp.this);
                            alertDialog.setTitle("الكود");
                            alertDialog.setMessage("ادخل الكود المرسل اليك");

                            final EditText input = new EditText(SignUp.this);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT);
                            input.setLayoutParams(lp);
                            alertDialog.setView(input);
                            alertDialog.setIcon(R.drawable.password);
                            alertDialog.setCancelable(false);
                            alertDialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, int which) {
                                    String password = input.getText().toString();
                                    apiService = ApiClient.getClient().create(ApiService.class);
                                    Call<SendCodeModel> call = apiService.send_code(id + "", text_msg + "");
                                    call.enqueue(new Callback<SendCodeModel>() {
                                        @Override
                                        public void onResponse(Call<SendCodeModel> call, Response<SendCodeModel> response) {
                                            dialog.cancel();
                                            SendCodeModel sendCodeModel = response.body();
                                            String status1 = sendCodeModel.getStatus();
                                            String code_data = sendCodeModel.getData();
                                            if (status1.equals("success") && sign_type == 1) {
                                                Toasty.success(SignUp.this, "تم التحقق من الكود", 1500).show();
                                                startActivity(new Intent(SignUp.this, Login.class));
                                            } else {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                                                builder.setTitle("حفلاتى")
                                                        .setMessage("تم التحقق من الكود الخاص بك, برجاء الانتظار حتى يتم الموافقة على طلبك")
                                                        .setCancelable(false)
                                                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                dialogInterface.dismiss();
                                                                startActivity(new Intent(SignUp.this, Login.class));
                                                            }
                                                        });
                                                builder.setNegativeButton(getString(R.string.No), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();
                                                    }
                                                }).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<SendCodeModel> call, Throwable t) {
                                            dialog.cancel();
                                            t.printStackTrace();
                                        }
                                    });
                                }
                            });
//                            alertDialog.setNegativeButton(getString(R.string.No),
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            dialog.cancel();
//                                        }
//                                    });
                            alertDialog.show();
                        } else {
                            Toasty.error(SignUp.this, getString(R.string.email_already_exist), 1500).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SignModel> call, Throwable t) {
                        dialog.dismiss();
                        t.printStackTrace();
                    }
                });
            }
        } else {
            Toasty.error(SignUp.this, getString(R.string.error_connection), 1500).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
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

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.user_btn:
                if (checked) {
                    // Pirates are the best
                    sign_type = 1;
                }
                break;
            case R.id.developer_btn:
                if (checked) {
                    // Ninjas rule
                    sign_type = 2;
                }
                break;
        }
    }
}