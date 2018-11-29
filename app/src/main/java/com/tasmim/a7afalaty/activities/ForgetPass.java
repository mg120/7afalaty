package com.tasmim.a7afalaty.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tasmim.a7afalaty.NetworkTester;
import com.tasmim.a7afalaty.R;
import com.tasmim.a7afalaty.model.ForgetPassModel;
import com.tasmim.a7afalaty.network.ApiClient;
import com.tasmim.a7afalaty.network.ApiService;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPass extends AppCompatActivity {

    TextView back;
    Button send_btn;
    EditText phone_ed, email_ed;
    SpotsDialog dialog ;

    ApiService apiService;
    NetworkTester networkTester = new NetworkTester(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);

        back = findViewById(R.id.restore_pass_back_txt_id);
        send_btn = findViewById(R.id.send_btn_id);
        phone_ed = findViewById(R.id.pass_phone_ed);
        email_ed = findViewById(R.id.pass_email_ed);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        phone_ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone_ed.setBackgroundColor(Color.parseColor("#00E676"));
                email_ed.setBackgroundResource(R.drawable.show_item_bg);
            }
        });
        email_ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email_ed.setBackgroundColor(Color.parseColor("#00E676"));
                phone_ed.setBackgroundResource(R.drawable.show_item_bg);
            }
        });

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (networkTester.isNetworkAvailable()) {
                    dialog = new SpotsDialog(ForgetPass.this, getString(R.string.sending), R.style.Custom);
                    dialog.show();
                    apiService = ApiClient.getClient().create(ApiService.class);
                    Call<ForgetPassModel> call = apiService.forget_pass(email_ed.getText().toString());
                    call.enqueue(new Callback<ForgetPassModel>() {
                        @Override
                        public void onResponse(Call<ForgetPassModel> call, Response<ForgetPassModel> response) {
                            dialog.dismiss();
                            ForgetPassModel forgetPassModel = response.body();
                            String status = forgetPassModel.getStatus();
                            Toast.makeText(ForgetPass.this, "" + status, Toast.LENGTH_SHORT).show();
                            if (status.equals("success")) {
                                Toasty.success(ForgetPass.this, "تم ارسال الكود الى بريدك الالكترونى", 1500).show();
                                ForgetPassModel.Data data = forgetPassModel.getData();
                                int user_id = data.getId();
                                int code = data.getCode();

                                Intent intent = new Intent(ForgetPass.this, Code.class);
                                intent.putExtra("user_id", user_id);
                                intent.putExtra("code", code);
                                startActivity(intent);
//                            startActivity(new Intent(ForgetPass.this, Code.class));
                            } else {
                                Toasty.error(ForgetPass.this, "البريد الالكترونى الذى ادخلتة غير صحيح !", 1500).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ForgetPassModel> call, Throwable t) {
                            dialog.dismiss();
                            t.printStackTrace();
                        }

                    });
                } else {
                    Toasty.error(ForgetPass.this, getString(R.string.error_connection), 1500).show();
                }
            }
        });
    }
}
