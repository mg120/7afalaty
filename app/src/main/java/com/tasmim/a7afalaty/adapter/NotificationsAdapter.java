package com.tasmim.a7afalaty.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tasmim.a7afalaty.Home;
import com.tasmim.a7afalaty.NetworkTester;
import com.tasmim.a7afalaty.R;
import com.tasmim.a7afalaty.model.AcceptBookingModel;
import com.tasmim.a7afalaty.model.CancelBookingModel;
import com.tasmim.a7afalaty.model.NotificationsModel;
import com.tasmim.a7afalaty.network.ApiClient;
import com.tasmim.a7afalaty.network.ApiService;

import java.util.List;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ma7MouD on 10/30/2018.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    Context context;
    List<NotificationsModel.Datum> list;
    ApiService apiService;
    SpotsDialog dialog;

    public NotificationsAdapter(Context context, List<NotificationsModel.Datum> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public NotificationsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationsAdapter.ViewHolder holder, final int position) {
        if (list.get(position).getType() == 1) {
            holder.notification_developer_layout.setVisibility(View.GONE);
        }
        if (list.get(position).getType() == 3 && Home.user_Data.getType() == 1) {
            holder.notification_developer_layout.setVisibility(View.GONE);
            holder.notification_desc.setTextColor(Color.GREEN);
        } else if (list.get(position).getType() == 4 && Home.user_Data.getType() == 1) {
            holder.notification_developer_layout.setVisibility(View.GONE);
            holder.notification_desc.setTextColor(Color.RED);
        }
        if (Home.user_Data.getType() == 2 && list.get(position).getType() == 2) {
            holder.notification_developer_layout.setVisibility(View.VISIBLE);
        }
        if (list.get(position).getType() == 3 && Home.user_Data.getType() == 1) {
            holder.notification_developer_layout.setVisibility(View.GONE);
            holder.notification_desc.setTextColor(Color.GREEN);
        } else if (list.get(position).getType() == 4 && Home.user_Data.getType() == 1) {
            holder.notification_developer_layout.setVisibility(View.GONE);
            holder.notification_desc.setTextColor(Color.RED);
        }

        holder.notification_desc.setText(list.get(position).getMessage());
        holder.accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("owner_id: ", Home.user_id + "");
                Log.e("book_id: ", list.get(position).getId() + "");
                dialog = new SpotsDialog(context, "جارى التحديث", R.style.Custom);
                dialog.show();
                apiService = ApiClient.getClient().create(ApiService.class);
                Call<AcceptBookingModel> call = apiService.accept_Booking(Home.user_id + "", list.get(position).getBook_id() + "");
                call.enqueue(new Callback<AcceptBookingModel>() {
                    @Override
                    public void onResponse(Call<AcceptBookingModel> call, Response<AcceptBookingModel> response) {
                        dialog.dismiss();
                        AcceptBookingModel acceptBookingModel = response.body();
                        String status = acceptBookingModel.getStatus();
                        int code = acceptBookingModel.getCode();
                        if (code == 200) {
                            Toasty.success(context, "تم قبول الحجز", 1500).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AcceptBookingModel> call, Throwable t) {
                        dialog.dismiss();
                        t.printStackTrace();
                    }
                });
            }
        });
        holder.reject_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new SpotsDialog(context, "جارى التحديث", R.style.Custom);
                dialog.show();
                apiService = ApiClient.getClient().create(ApiService.class);
                Call<CancelBookingModel> call = apiService.cancel_Booking(Home.user_id + "", list.get(position).getId() + "");
                call.enqueue(new Callback<CancelBookingModel>() {
                    @Override
                    public void onResponse(Call<CancelBookingModel> call, Response<CancelBookingModel> response) {
                        dialog.dismiss();
                        CancelBookingModel cancelBookingModel = response.body();
                        String status = cancelBookingModel.getStatus();
                        int code = cancelBookingModel.getCode();
                        Toasty.success(context, "تم رفض الحجز", 1500).show();
                    }

                    @Override
                    public void onFailure(Call<CancelBookingModel> call, Throwable t) {
                        dialog.dismiss();
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
        TextView notification_userName, notification_desc;
        LinearLayout notification_developer_layout;
        Button accept_btn, reject_btn;

        public ViewHolder(View itemView) {
            super(itemView);
            notification_userName = itemView.findViewById(R.id.notification_userName_txt_id);
            notification_desc = itemView.findViewById(R.id.notification_item_desc_id);
            notification_developer_layout = itemView.findViewById(R.id.notification_owner_layout_id);
            accept_btn = itemView.findViewById(R.id.accept_booking_btn_id);
            reject_btn = itemView.findViewById(R.id.reject_booking_btn_id);
        }
    }
}
