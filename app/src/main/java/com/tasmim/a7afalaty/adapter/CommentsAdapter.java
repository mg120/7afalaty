package com.tasmim.a7afalaty.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tasmim.a7afalaty.R;
import com.tasmim.a7afalaty.model.ItemDetailsModel;

import java.util.List;

/**
 * Created by Ma7MouD on 9/29/2018.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    Context context;
    List<ItemDetailsModel.Comment> list;
    String item_id;

    public CommentsAdapter(Context context, List<ItemDetailsModel.Comment> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentsAdapter.ViewHolder holder, int position) {
        holder.comment_userName.setText(list.get(position).getUserName());
        holder.comment_time_txt.setText(list.get(position).getDate());
        holder.comment_txt.setText(list.get(position).getComment());
        holder.comment_card_item.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView comment_userName, comment_time_txt, comment_txt;
        CardView comment_card_item;

        public ViewHolder(View itemView) {
            super(itemView);

            comment_userName = itemView.findViewById(R.id.user_comment_txt_id);
            comment_time_txt = itemView.findViewById(R.id.comment_time_id);
            comment_txt = itemView.findViewById(R.id.comment_txt);
            comment_card_item = itemView.findViewById(R.id.comment_card_item_id);
            comment_card_item.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            int pos = (int) view.getTag();
////            Toast.makeText(context, "pos : " + pos, Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(context, ItemComments.class);
//            intent.putExtra("item_id", item_id);
//            context.startActivity(intent);
        }
    }
}