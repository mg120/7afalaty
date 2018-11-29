package com.tasmim.a7afalaty.activities;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tasmim.a7afalaty.Home;
import com.tasmim.a7afalaty.R;
import com.tasmim.a7afalaty.adapter.MyServicesAdapter;
import com.tasmim.a7afalaty.model.MyServicesModel;
import com.tasmim.a7afalaty.network.ApiClient;
import com.tasmim.a7afalaty.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyServices extends AppCompatActivity {

    ProgressBar progressBar;
    TextView no_services_txt;
    RecyclerView services_recycler;
    MyServicesAdapter servicesAdapter;
    RecyclerView.LayoutManager layoutManager;

    TextView back;
    ApiService apiService;
    SwipeRefreshLayout swipe_layout;
    LayoutAnimationController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_services);

        services_recycler = findViewById(R.id.services_recycler_id);
        progressBar = findViewById(R.id.my_services_progress);
        no_services_txt = findViewById(R.id.no_services_txt);
        back = findViewById(R.id.myServices_back_txt_id);
        swipe_layout = (SwipeRefreshLayout) findViewById(R.id.aklat_swipeRefreshLayout);

        controller = AnimationUtils.loadLayoutAnimation(MyServices.this, R.anim.layout_down_top);
        getUserServices(Home.user_id + "");
        Log.e("user_id: ", Home.user_id + "");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_layout.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getUserServices(Home.user_id + "");
                        swipe_layout.setRefreshing(false);
                    }
                }, 1000);

            }
        });
        swipe_layout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);
    }

    private void getUserServices(String user_id) {
        progressBar.setVisibility(View.VISIBLE);
        no_services_txt.setVisibility(View.GONE);
        services_recycler.setVisibility(View.GONE);
        apiService = ApiClient.getClient().create(ApiService.class);
        Call<MyServicesModel> call = apiService.userServices(user_id);
        call.enqueue(new Callback<MyServicesModel>() {
            @Override
            public void onResponse(Call<MyServicesModel> call, Response<MyServicesModel> response) {
                progressBar.setVisibility(View.GONE);
                MyServicesModel servicesModel = response.body();
                String status = servicesModel.getStatus();
                List<MyServicesModel.Datum> services_list = servicesModel.getData();

                if (services_list.size() > 0) {
                    no_services_txt.setVisibility(View.GONE);
                    services_recycler.setVisibility(View.VISIBLE);
                    layoutManager = new GridLayoutManager(MyServices.this, 1);
                    services_recycler.setLayoutManager(layoutManager);
                    services_recycler.setHasFixedSize(true);
                    servicesAdapter = new MyServicesAdapter(MyServices.this, services_list);
                    services_recycler.setAdapter(servicesAdapter);
                    // Set Animation to Recyceler ...
                    services_recycler.setLayoutAnimation(controller);
                    services_recycler.getAdapter().notifyDataSetChanged();
                    services_recycler.scheduleLayoutAnimation();
                } else {
                    services_recycler.setVisibility(View.GONE);
                    no_services_txt.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<MyServicesModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }
}