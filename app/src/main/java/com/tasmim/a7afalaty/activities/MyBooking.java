package com.tasmim.a7afalaty.activities;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tasmim.a7afalaty.Home;
import com.tasmim.a7afalaty.R;
import com.tasmim.a7afalaty.adapter.MyBookingsAdapter;
import com.tasmim.a7afalaty.model.MyBookingModel;
import com.tasmim.a7afalaty.network.ApiClient;
import com.tasmim.a7afalaty.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBooking extends AppCompatActivity {

    ProgressBar progressBar;
    TextView no_bookings_txt, back;
    RecyclerView recyclerView;
    MyBookingsAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    ApiService apiService;
    SwipeRefreshLayout swipe_layout;
    LayoutAnimationController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_booking);

        back = findViewById(R.id.my_booking_back_txt_id);
        progressBar = findViewById(R.id.my_bookings_progress);
        no_bookings_txt = findViewById(R.id.no_bookings_txt);
        recyclerView = findViewById(R.id.booking_recycler_id);
        swipe_layout = (SwipeRefreshLayout) findViewById(R.id.booking_swipeRefreshLayout);

        layoutManager = new GridLayoutManager(MyBooking.this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        controller = AnimationUtils.loadLayoutAnimation(MyBooking.this, R.anim.layout_down_top);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        userBookingData(Home.user_id + "");

        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_layout.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        userBookingData(Home.user_id + "");
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

    private void userBookingData(String userId) {
        apiService = ApiClient.getClient().create(ApiService.class);
        Call<MyBookingModel> call = apiService.allUserBooking(userId);
        call.enqueue(new Callback<MyBookingModel>() {
            @Override
            public void onResponse(Call<MyBookingModel> call, Response<MyBookingModel> response) {
                progressBar.setVisibility(View.GONE);
                MyBookingModel myBookingModel = response.body();
                String status = myBookingModel.getStatus();
                List<MyBookingModel.Datum> list = myBookingModel.getData();

                if (list.size() > 0) {
                    no_bookings_txt.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter = new MyBookingsAdapter(MyBooking.this, list);
                    recyclerView.setAdapter(adapter);
                    // Set Animation to Recyceler ...
                    recyclerView.setLayoutAnimation(controller);
                    recyclerView.getAdapter().notifyDataSetChanged();
                    recyclerView.scheduleLayoutAnimation();
                } else {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    no_bookings_txt.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<MyBookingModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }
}
