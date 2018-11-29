package com.tasmim.a7afalaty.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tasmim.a7afalaty.R;
import com.tasmim.a7afalaty.activities.ShowItemDetails;
import com.tasmim.a7afalaty.model.ShowItemsModel;

import java.util.List;

/**
 * Created by Ma7MouD on 10/11/2018.
 */

public class ShowItemsAdapter extends RecyclerView.Adapter<ShowItemsAdapter.ViewHolder> {

    Context context;
    List<ShowItemsModel.Datum> list;

    public ShowItemsAdapter(Context context, List<ShowItemsModel.Datum> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ShowItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShowItemsAdapter.ViewHolder holder, int position) {
        Picasso.with(context).load("http://my-parties.atls.sa/public/images/services/" +list.get(position).getImage()).into(holder.imageView);
        holder.item_Name.setText(list.get(position).getTitle());
        holder.item_price.setText(list.get(position).getDetails());
        holder.item_ratingBar.setRating(list.get(position).getStars());
        holder.item_details_btn.setTag(position);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView item_Name, item_price;
        RatingBar item_ratingBar;
        Button item_details_btn;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image_id);
            item_Name = itemView.findViewById(R.id.item_Name_id);
            item_price = itemView.findViewById(R.id.item_price_id);
            item_ratingBar = itemView.findViewById(R.id.item_ratingBar_id);
            item_details_btn = itemView.findViewById(R.id.item_details_btn_id);
            item_details_btn.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = (int) view.getTag();
            Intent intent = new Intent(context, ShowItemDetails.class);
            intent.putExtra("item_id", list.get(pos).getId());
            context.startActivity(intent);
        }
    }
}
