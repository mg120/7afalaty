package com.tasmim.a7afalaty.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tasmim.a7afalaty.Home;
import com.tasmim.a7afalaty.NetworkTester;
import com.tasmim.a7afalaty.R;
import com.tasmim.a7afalaty.adapter.NotificationsAdapter;
import com.tasmim.a7afalaty.model.NotificationsModel;
import com.tasmim.a7afalaty.network.ApiClient;
import com.tasmim.a7afalaty.network.ApiService;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyNotifications extends AppCompatActivity {

    ProgressBar progressBar;
    TextView no_notifications_txt, back;
    RecyclerView notifications_recycler;
    NotificationsAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    NetworkTester networkTester = new NetworkTester(this);
    ApiService apiService;

    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notifications);

        if (getIntent().getExtras() != null) {
            type = Integer.parseInt(getIntent().getExtras().getString("type"));
        }
        back = findViewById(R.id.notifications_back_txt_id);
        progressBar = findViewById(R.id.notifications_progress);
        no_notifications_txt = findViewById(R.id.no_notifications_txt);
        notifications_recycler = findViewById(R.id.notifications_recycler);

        if (networkTester.isNetworkAvailable()) {
            gettMyNotifications(Home.user_id + "");
        } else {
            Toasty.error(MyNotifications.this, getString(R.string.error_connection), 1500).show();
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void gettMyNotifications(String id) {
        progressBar.setVisibility(View.GONE);
        apiService = ApiClient.getClient().create(ApiService.class);
        Call<NotificationsModel> call = apiService.my_notifies(id);
        call.enqueue(new Callback<NotificationsModel>() {
            @Override
            public void onResponse(Call<NotificationsModel> call, Response<NotificationsModel> response) {
                NotificationsModel notificationsModel = response.body();
                String status = notificationsModel.getStatus();
                List<NotificationsModel.Datum> list = notificationsModel.getData();

                if (list.size() > 0) {
                    no_notifications_txt.setVisibility(View.GONE);
                    layoutManager = new GridLayoutManager(MyNotifications.this, 1);
                    notifications_recycler.setLayoutManager(layoutManager);
                    notifications_recycler.setHasFixedSize(true);
                    adapter = new NotificationsAdapter(MyNotifications.this, list);
                    notifications_recycler.setAdapter(adapter);
                } else {
                    notifications_recycler.setVisibility(View.GONE);
                    no_notifications_txt.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<NotificationsModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }
}
