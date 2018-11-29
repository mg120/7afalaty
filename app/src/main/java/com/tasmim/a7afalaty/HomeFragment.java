package com.tasmim.a7afalaty;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tasmim.a7afalaty.activities.MyServices;
import com.tasmim.a7afalaty.activities.Splash;
import com.tasmim.a7afalaty.adapter.HomeAdapter;
import com.tasmim.a7afalaty.model.HomeModel;
import com.tasmim.a7afalaty.network.ApiClient;
import com.tasmim.a7afalaty.network.ApiService;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    ProgressBar progressBar;
    RecyclerView home_recycler;
    HomeAdapter homeAdapter;
    RecyclerView.LayoutManager layoutManager;
//    NetworkTester networkTester = new NetworkTester(getActivity());

    public Home activity;
    ApiService apiService;
    LayoutAnimationController controller;
    SwipeRefreshLayout swipe_layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(getString(R.string.home));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        progressBar = getActivity().findViewById(R.id.home_progress);
        home_recycler = getActivity().findViewById(R.id.home_sections_recycler);
        swipe_layout = (SwipeRefreshLayout) getActivity().findViewById(R.id.home_swipeRefreshLayout);

        controller = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_down_top);

        if (isNetworkAvailable()) {
            getHomeData();
        } else {
            Toasty.error(getActivity(), getString(R.string.error_connection), 1500).show();
        }

        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_layout.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isNetworkAvailable()) {
                            getHomeData();
                        } else {
                            Toasty.error(getActivity(), getString(R.string.error_connection), 1500).show();
                        }
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

    private void getHomeData() {
        apiService = ApiClient.getClient().create(ApiService.class);
        Call<HomeModel> call = apiService.homeData();
        call.enqueue(new Callback<HomeModel>() {
            @Override
            public void onResponse(Call<HomeModel> call, Response<HomeModel> response) {
                HomeModel homeModel = response.body();
                String status = homeModel.getStatus();
                List<HomeModel.Datum> list = homeModel.getData();

                if (list.size() > 0) {
                    progressBar.setVisibility(View.GONE);
                    home_recycler.setVisibility(View.VISIBLE);
                    layoutManager = new GridLayoutManager(getActivity(), 2);
                    home_recycler.setLayoutManager(layoutManager);
                    home_recycler.setHasFixedSize(true);
                    homeAdapter = new HomeAdapter(activity, list);
                    home_recycler.setAdapter(homeAdapter);
                    // Set Animation to Recyceler ...
                    home_recycler.setLayoutAnimation(controller);
                    home_recycler.getAdapter().notifyDataSetChanged();
                    home_recycler.scheduleLayoutAnimation();
                }
            }

            @Override
            public void onFailure(Call<HomeModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (Home) activity;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityMgr.getActiveNetworkInfo();
        /// if no network is available networkInfo will be null
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
}
