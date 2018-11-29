package com.tasmim.a7afalaty.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tasmim.a7afalaty.R;
import com.tasmim.a7afalaty.adapter.ShowItemsAdapter;
import com.tasmim.a7afalaty.model.ShowItemsModel;

import java.util.List;


public class ServicesFrag extends Fragment {

    ProgressBar progressBar;
    RecyclerView items_recycler;
    TextView back, show_items_title, no_availabe_services;

    private int item_id;
    private String item_name;
    List<ShowItemsModel.Datum> services_list;
    ShowItemsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            services_list = getArguments().getParcelableArrayList("data_list");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_services, container, false);
        no_availabe_services = rootView.findViewById(R.id.no_services_txt_id);
        progressBar = rootView.findViewById(R.id.services_progress);
        show_items_title = rootView.findViewById(R.id.show_items_title_id);
        back = rootView.findViewById(R.id.show_items_back_txt_id);
        items_recycler = rootView.findViewById(R.id.services_recycler);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar.setVisibility(View.VISIBLE);

        if (services_list.size() > 0) {
            no_availabe_services.setVisibility(View.GONE);
            items_recycler.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            items_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            items_recycler.setHasFixedSize(true);
            adapter = new ShowItemsAdapter(getActivity(), services_list);
            items_recycler.setAdapter(adapter);

        } else {
            progressBar.setVisibility(View.GONE);
            items_recycler.setVisibility(View.GONE);
            no_availabe_services.setVisibility(View.VISIBLE);
        }

    }
}
