package com.tasmim.a7afalaty.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fourhcode.forhutils.FUtilsValidation;
import com.tasmim.a7afalaty.Home;
import com.tasmim.a7afalaty.NetworkTester;
import com.tasmim.a7afalaty.R;
import com.tasmim.a7afalaty.model.LoginModel;
import com.tasmim.a7afalaty.network.ApiClient;
import com.tasmim.a7afalaty.network.ApiService;

import java.util.List;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPass extends AppCompatActivity {

    EditText new_pass_ed, confirm_new_pass_ed;
    Button send_new_pass_btn;

    ApiService apiService;
    int user_id;
    SpotsDialog dialog;
    NetworkTester networkTester = new NetworkTester(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pass);
        if (getIntent().getExtras() != null) {
            user_id = getIntent().getExtras().getInt("user_id");
        }
        new_pass_ed = findViewById(R.id.new_pass_ed);
        confirm_new_pass_ed = findViewById(R.id.con_new_pass_ed);
        send_new_pass_btn = findViewById(R.id.send_new_pass_btn);

        send_new_pass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_pass = new_pass_ed.getText().toString().trim();
                String con_new_pass = confirm_new_pass_ed.getText().toString().trim();

                if (!FUtilsValidation.isEmpty(new_pass_ed, getString(R.string.required))
                        && !FUtilsValidation.isEmpty(confirm_new_pass_ed, getString(R.string.required))
                        ) {
                    if (networkTester.isNetworkAvailable()) {
                        dialog = new SpotsDialog(NewPass.this, getString(R.string.changing), R.style.Custom);
                        dialog.show();
                        apiService = ApiClient.getClient().create(ApiService.class);
                        Call<LoginModel> call = apiService.reset_pass(user_id + "", new_pass);
                        call.enqueue(new Callback<LoginModel>() {
                            @Override
                            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                                dialog.dismiss();
                                LoginModel loginModel = response.body();
                                String status = loginModel.getStatus();
                                if (status.equals("success")) {
                                    Toasty.success(NewPass.this, "تم تغيير كلمة المرور بنجاح", 1500).show();
//                                    LoginModel.Data data = loginModel.getData();
                                    List<LoginModel.Data> list = loginModel.getData();
                                    LoginModel.Data data = list.get(0);
                                    Intent intent = new Intent(NewPass.this, Home.class);
                                    intent.putExtra("user_data", data);
                                    startActivity(intent);
                                    finish();
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
                    Toasty.error(NewPass.this, getString(R.string.error_connection), 1500).show();
                }
            }
        });
    }
}
