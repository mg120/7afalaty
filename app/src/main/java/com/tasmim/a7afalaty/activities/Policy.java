package com.tasmim.a7afalaty.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.api.Api;
import com.tasmim.a7afalaty.R;
import com.tasmim.a7afalaty.model.PolicyModel;
import com.tasmim.a7afalaty.network.ApiClient;
import com.tasmim.a7afalaty.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Policy extends AppCompatActivity {

    TextView policy_txt, back;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);

        back = findViewById(R.id.policy_back_txt_id);
        policy_txt = findViewById(R.id.app_policy_txt);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        apiService = ApiClient.getClient().create(ApiService.class);
        Call<PolicyModel> call = apiService.policyData();
        call.enqueue(new Callback<PolicyModel>() {
            @Override
            public void onResponse(Call<PolicyModel> call, Response<PolicyModel> response) {
                PolicyModel policyModel = response.body();
                String status = policyModel.getStatus();
                String data = policyModel.getData();
                policy_txt.setText(data);
            }

            @Override
            public void onFailure(Call<PolicyModel> call, Throwable t) {

            }
        });
    }
}
