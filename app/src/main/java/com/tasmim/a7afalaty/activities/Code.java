package com.tasmim.a7afalaty.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fourhcode.forhutils.FUtilsValidation;
import com.tasmim.a7afalaty.NetworkTester;
import com.tasmim.a7afalaty.R;
import com.tasmim.a7afalaty.model.ResendCodeModel;
import com.tasmim.a7afalaty.network.ApiClient;
import com.tasmim.a7afalaty.network.ApiService;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Code extends AppCompatActivity {

    Button send_code_btn;
    EditText code_ed;
    SpotsDialog dialog;

    int user_id, code;
    NetworkTester networkTester = new NetworkTester(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);

        if (getIntent().getExtras() != null) {
            user_id = getIntent().getExtras().getInt("user_id");
            code = getIntent().getExtras().getInt("code");
        }
        send_code_btn = findViewById(R.id.send_btn_id);
        code_ed = findViewById(R.id.code_ed);

        send_code_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!FUtilsValidation.isEmpty(code_ed, getString(R.string.required))) {
                    if (networkTester.isNetworkAvailable()) {
//                startActivity(new Intent(Login.this, Home.class));
                        dialog = new SpotsDialog(Code.this, getString(R.string.sending), R.style.Custom);
                        dialog.show();
                        ApiService apiService = ApiClient.getClient().create(ApiService.class);
                        Call<ResendCodeModel> call = apiService.resend_Code(user_id + "", code_ed.getText().toString().trim());
                        call.enqueue(new Callback<ResendCodeModel>() {
                            @Override
                            public void onResponse(Call<ResendCodeModel> call, Response<ResendCodeModel> response) {
                                dialog.dismiss();
                                ResendCodeModel resendCodeModel = response.body();
                                String status = resendCodeModel.getStatus();
                                if (status.equals("success")) {
                                    Intent intent = new Intent(Code.this, NewPass.class);
                                    intent.putExtra("user_id", user_id);
                                    startActivity(intent);
//                                startActivity(new Intent(Code.this, NewPass.class));
                                } else {
                                    Toasty.error(Code.this, "الكود الذى ادخلتة غير صحيح", 1500).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResendCodeModel> call, Throwable t) {
                                dialog.dismiss();
                                t.printStackTrace();
                            }
                        });
                    }
                } else {
                    Toasty.error(Code.this, getString(R.string.error_connection), 1500).show();
                }
            }
        });
    }
}
