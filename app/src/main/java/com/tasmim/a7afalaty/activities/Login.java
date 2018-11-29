package com.tasmim.a7afalaty.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fourhcode.forhutils.FUtilsValidation;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tasmim.a7afalaty.Home;
import com.tasmim.a7afalaty.NetworkTester;
import com.tasmim.a7afalaty.R;
import com.tasmim.a7afalaty.model.LoginModel;
import com.tasmim.a7afalaty.network.ApiClient;
import com.tasmim.a7afalaty.network.ApiService;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity implements View.OnClickListener {

    EditText email_ed, pass_ed;
    Button login_btn;
    TextView signUp_txt, back, forget_pass_txt;

    ApiService apiService;
    SpotsDialog dialog;
    NetworkTester networkTester = new NetworkTester(this);

    String user_email, user_pass;

    // Shared Preferences
    public static final String MY_PREFS_NAME = "all_user_data";
    SharedPreferences.Editor user_data_edito;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        back = findViewById(R.id.login_back_txt_id);
        email_ed = findViewById(R.id.login_phone_ed);
        pass_ed = findViewById(R.id.login_pass_ed);
        login_btn = findViewById(R.id.login_btn_id);
        forget_pass_txt = findViewById(R.id.forget_pass_txt_id);
        signUp_txt = findViewById(R.id.sign_txt_id);

        // Get Data from Shared Preferences ...
        sharedPreferences = getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        email_ed.setText(sharedPreferences.getString("email", null));
        pass_ed.setText(sharedPreferences.getString("password", null));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        signUp_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, SignUp.class));
            }
        });
        forget_pass_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, ForgetPass.class));
            }
        });

        login_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        // get token from Firebase
        String token = FirebaseInstanceId.getInstance().getToken();

        if (!FUtilsValidation.isEmpty(email_ed, getString(R.string.required))
                && !FUtilsValidation.isEmpty(pass_ed, getString(R.string.required))
                ) {
            if (networkTester.isNetworkAvailable()) {
                dialog = new SpotsDialog(Login.this, getString(R.string.signing), R.style.Custom);
                dialog.show();
                user_email = email_ed.getText().toString().trim();
                user_pass = pass_ed.getText().toString().trim();
                apiService = ApiClient.getClient().create(ApiService.class);
                Call<LoginModel> call = apiService.user_login(user_email, user_pass, device_id, "1", token);
                call.enqueue(new Callback<LoginModel>() {
                    @Override
                    public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                        dialog.dismiss();
                        LoginModel login_data = response.body();
                        int code = login_data.getCode();
                        if (code == 200) {
//                            int code = login_data.getCode();
                            LoginModel.Data user_data = login_data.getData().get(0);
                            String name = user_data.getName();
                            // save data to Shared Preferences ...
                            user_data_edito = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            user_data_edito.putString("name", name);
                            user_data_edito.putInt("user_id", user_data.getId());
                            user_data_edito.commit();
                            user_data_edito.apply();
//
                            Toasty.success(Login.this, getString(R.string.login_success), 1500).show();
                            Intent intent = new Intent(Login.this, Home.class);
                            intent.putExtra("user_id", user_data.getId());
                            startActivity(intent);
                        } else {
                            Toasty.error(Login.this, getString(R.string.incorrect_email_or_password), 1500).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginModel> call, Throwable t) {
                        dialog.dismiss();
                        t.printStackTrace();
                    }
                });
            }
        } else {
            Toasty.error(Login.this, getString(R.string.error_connection), 1500).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        editor = sharedPreferences.edit();
        editor.putString("email", user_email);
        editor.putString("password", user_pass);
        editor.commit();
        editor.apply();
    }
}