package com.tasmim.a7afalaty.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.api.Api;
import com.tasmim.a7afalaty.R;
import com.tasmim.a7afalaty.model.ContactModel;
import com.tasmim.a7afalaty.network.ApiClient;
import com.tasmim.a7afalaty.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUs extends AppCompatActivity {

    ApiService apiService;
    TextView back, contact_us_email, contact_us_phone, contact_us_fax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        back = findViewById(R.id.contact_back_txt_id);
        contact_us_email = findViewById(R.id.contact_us_email);
        contact_us_phone = findViewById(R.id.contact_us_phone);
        contact_us_fax = findViewById(R.id.contact_us_fax);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        apiService = ApiClient.getClient().create(ApiService.class);
        Call<ContactModel> call = apiService.contactData();
        call.enqueue(new Callback<ContactModel>() {
            @Override
            public void onResponse(Call<ContactModel> call, Response<ContactModel> response) {
                ContactModel contactModel = response.body();
                String status = contactModel.getStatus();
                ContactModel.Data data = contactModel.getData();
                contact_us_email.setText(data.getSiteEmail());
                contact_us_phone.setText(data.getSitePhone());
                contact_us_fax.setText(data.getSiteFax());
            }

            @Override
            public void onFailure(Call<ContactModel> call, Throwable t) {

            }
        });

    }
}
