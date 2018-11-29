package com.tasmim.a7afalaty.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tasmim.a7afalaty.R;
import com.tasmim.a7afalaty.activities.BookingActivity;
import com.tasmim.a7afalaty.activities.ShowItemDetails;
import com.tasmim.a7afalaty.activities.ShowItems;
import com.tasmim.a7afalaty.model.HomeModel;

import java.util.List;

/**
 * Created by Ma7MouD on 10/11/2018.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    Context context;
    List<HomeModel.Datum> list;

    public HomeAdapter(Context context, List<HomeModel.Datum> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_layout_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeAdapter.ViewHolder holder, final int position) {

        Picasso.with(context).load(list.get(position).getImage()).into(holder.item_bg_img);
        Picasso.with(context).load(list.get(position).getLogo()).into(holder.item_img);
        holder.item_txt.setText(list.get(position).getName());
        holder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowItems.class);
                intent.putExtra("item_id", list.get(position).getId());
                intent.putExtra("item_name", list.get(position).getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView item_bg_img, item_img;
        TextView item_txt;
        LinearLayout item_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            item_bg_img = itemView.findViewById(R.id.home_item_bg_img_id);
            item_layout = itemView.findViewById(R.id.home_item_layout_id);
            item_img = itemView.findViewById(R.id.home_item_icon_id);
            item_txt = itemView.findViewById(R.id.home_item_txt_id);
        }
    }
}
