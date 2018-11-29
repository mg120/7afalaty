package com.tasmim.a7afalaty.activities;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tasmim.a7afalaty.R;
import com.tasmim.a7afalaty.adapter.ServicesVPagerAdapter;
import com.tasmim.a7afalaty.adapter.ShowItemsAdapter;
import com.tasmim.a7afalaty.model.SectionModel;
import com.tasmim.a7afalaty.model.ShowItemsModel;
import com.tasmim.a7afalaty.network.ApiClient;
import com.tasmim.a7afalaty.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowItems extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    TextView back, show_items_title;

    private int item_id;
    private String item_name;
    ServicesVPagerAdapter vPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_items);

        show_items_title = findViewById(R.id.show_items_title_id);
        back = findViewById(R.id.show_items_back_txt_id);
        tabLayout = findViewById(R.id.services_tab_layout_id);
        viewPager = findViewById(R.id.services_viewpager);

        if (getIntent().getExtras() != null) {
            item_id = getIntent().getExtras().getInt("item_id");
            item_name = getIntent().getExtras().getString("item_name");
            show_items_title.setText(item_name);
        }

        tabLayout.addTab(tabLayout.newTab().setText("القائمة"));
        tabLayout.addTab(tabLayout.newTab().setText("الخريطة"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        getItemsData(item_id);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void getItemsData(int item_id) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ShowItemsModel> call = apiService.getSectionData(item_id + "");
        call.enqueue(new Callback<ShowItemsModel>() {
            @Override
            public void onResponse(Call<ShowItemsModel> call, Response<ShowItemsModel> response) {
                ShowItemsModel showItemsModel = response.body();
                String status = showItemsModel.getStatus();
                List<ShowItemsModel.Datum> services_list = showItemsModel.getData();

                Gson gson = new Gson();
                String list_obj = gson.toJson(services_list);
                Log.e("list_obj: ", list_obj);

                // Add Adapter to ViewPager .....
                vPagerAdapter = new ServicesVPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), services_list);
                viewPager.setAdapter(vPagerAdapter);
                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        viewPager.setCurrentItem(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
                // set tabs indicator color .....
                tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
                tabLayout.setTabTextColors(Color.parseColor("#A9A9A9"), Color.parseColor("#ffffff"));
                viewPager.setCurrentItem(0);
            }

            @Override
            public void onFailure(Call<ShowItemsModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
