package com.tasmim.a7afalaty.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tasmim.a7afalaty.Home;
import com.tasmim.a7afalaty.R;
import com.tasmim.a7afalaty.activities.UpdateBooking;
import com.tasmim.a7afalaty.model.DeleteBookingModel;
import com.tasmim.a7afalaty.model.MyBookingModel;
import com.tasmim.a7afalaty.network.ApiClient;
import com.tasmim.a7afalaty.network.ApiService;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ma7MouD on 10/25/2018.
 */

public class MyBookingsAdapter extends RecyclerView.Adapter<MyBookingsAdapter.ViewHolder> {

    Context context;
    List<MyBookingModel.Datum> list;
    ApiService apiService ;

    public MyBookingsAdapter(Context context, List<MyBookingModel.Datum> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyBookingsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_booking_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyBookingsAdapter.ViewHolder holder, final int position) {

        holder.booking_item_name.setText(list.get(position).getName());
        Picasso.with(context).load(list.get(position).getServiceImage()).into(holder.imageView);
        holder.booking_item_desc.setText(list.get(position).getDisc());
        holder.booking_item_time.setText(list.get(position).getCreatedAt());
        holder.update_booking_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateBooking.class);
                intent.putExtra("booking_data", list.get(position));
                context.startActivity(intent);
            }
        });
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiService = ApiClient.getClient().create(ApiService.class);
                Call<DeleteBookingModel> call = apiService.deleteBooking(list.get(position).getId()+"");
                call.enqueue(new Callback<DeleteBookingModel>() {
                    @Override
                    public void onResponse(Call<DeleteBookingModel> call, Response<DeleteBookingModel> response) {
                        DeleteBookingModel deleteBookingModel = response.body();
                        String status = deleteBookingModel.getStatus();
                        if (status.equals("success")){
                            Toasty.success(context, "تم مسح الحجز" , 1500).show();
                            list.remove(position);
                            MyBookingsAdapter.this.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<DeleteBookingModel> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView booking_item_name, booking_item_state, booking_item_time, booking_item_desc;
        Button update_booking_btn, delete_btn;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.booking_item_img_id);
            booking_item_name = itemView.findViewById(R.id.booking_item_name_txt_id);
            booking_item_state = itemView.findViewById(R.id.booking_item_state_txt_id);
            booking_item_time = itemView.findViewById(R.id.booking_item_time_txt_id);
            booking_item_desc = itemView.findViewById(R.id.booking_item_desc_id);
            update_booking_btn = itemView.findViewById(R.id.update_booking_btn_id);
            delete_btn = itemView.findViewById(R.id.delete_booking_btn_id);
        }
    }
}
