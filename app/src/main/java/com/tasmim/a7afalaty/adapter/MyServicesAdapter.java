package com.tasmim.a7afalaty.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tasmim.a7afalaty.R;
import com.tasmim.a7afalaty.activities.ShowItemDetails;
import com.tasmim.a7afalaty.activities.UpdateService;
import com.tasmim.a7afalaty.model.DeleteServiceModel;
import com.tasmim.a7afalaty.model.MyServicesModel;
import com.tasmim.a7afalaty.network.ApiClient;
import com.tasmim.a7afalaty.network.ApiService;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ma7MouD on 10/26/2018.
 */

public class MyServicesAdapter extends RecyclerView.Adapter<MyServicesAdapter.ViewHolder> {

    Context context;
    List<MyServicesModel.Datum> list;
    ApiService apiService;

    public MyServicesAdapter(Context context, List<MyServicesModel.Datum> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyServicesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_service_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyServicesAdapter.ViewHolder holder, final int position) {
        Picasso.with(context).load(list.get(position).getImage()).into(holder.imageView);
        holder.service_item_name.setText(list.get(position).getTitle());
        holder.service_item_details.setText(list.get(position).getDisc());
        holder.service_rating.setRating(list.get(position).getStars());
        holder.service_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("id: ", list.get(position).getId() + "");
                Log.e("section: ", list.get(position).getCategoryName());
                Log.e("section_id: ", list.get(position).getCategoryId() + "");
                Log.e("name: ", list.get(position).getTitle());
                Log.e("desc: ", list.get(position).getDisc());
                Intent intent = new Intent(context, UpdateService.class);
                intent.putExtra("service_data", list.get(position));
                context.startActivity(intent);

            }
        });
        holder.service_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("service_id: ", list.get(position).getId() + "");
                apiService = ApiClient.getClient().create(ApiService.class);
                Call<DeleteServiceModel> call = apiService.delete_service(list.get(position).getId() + "");
                call.enqueue(new Callback<DeleteServiceModel>() {
                    @Override
                    public void onResponse(Call<DeleteServiceModel> call, Response<DeleteServiceModel> response) {
                        DeleteServiceModel deleteServiceModel = response.body();
                        String status = deleteServiceModel.getStatus();
                        if (status.equals("success")) {
                            list.remove(position);
                            MyServicesAdapter.this.notifyDataSetChanged();
                            Toasty.success(context, "تم مسح الخدمة", 1500).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DeleteServiceModel> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowItemDetails.class);
                intent.putExtra("item_id", list.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView service_item_name, service_item_details;
        RatingBar service_rating;
        Button service_update, service_delete;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.service_item_img_id);
            service_item_name = itemView.findViewById(R.id.service_item_name_txt_id);
            service_item_details = itemView.findViewById(R.id.service_item_desc_id);
            service_rating = itemView.findViewById(R.id.service_item_rating);
            service_update = itemView.findViewById(R.id.update_service_btn_id);
            service_delete = itemView.findViewById(R.id.delete_service_btn_id);
        }
    }
}
