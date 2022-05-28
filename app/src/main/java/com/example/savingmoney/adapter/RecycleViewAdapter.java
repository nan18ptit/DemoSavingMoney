package com.example.savingmoney.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.ClipData;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.savingmoney.R;
import com.example.savingmoney.UpdateDeleteActivity;
import com.example.savingmoney.model.item;

import java.util.ArrayList;
import java.util.List;


public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.HomeViewHolder>{
    private List<item> list;
    private ItemListener itemListener;
    public RecycleViewAdapter() {
        list = new ArrayList<>();
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public void setList(List<item> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public item getItem(int position){
        return list.get(position);
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        item item = list.get(position);
        holder.title.setText(item.getTitle());
        holder.category.setText(item.getCategory());
        holder.price.setText(item.getPrice());
        holder.date.setText(item.getDate());
        View itemView = holder.itemView;
        itemView.setOnClickListener(view -> {
            Log.d("TAG", "onItemClick: Hế lô");
            Intent intent = new Intent();
            intent.setClass(itemView.getContext(),UpdateDeleteActivity.class);
            intent.putExtra("item", item);
            startActivity(itemView.getContext(),intent,null);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView title, category, price, date;
        public HomeViewHolder(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.tvTitle);
            category = view.findViewById(R.id.tvCategory);
            price = view.findViewById(R.id.tvPrice);
            date = view.findViewById(R.id.tvDate);
        }

        @Override
        public void onClick(View view) {
            if (itemListener != null){
                itemListener.onItemClick(view, getAdapterPosition());
            }
        }
    }
    public abstract interface ItemListener{
        void onItemClick(View view, int position);
    }
}
