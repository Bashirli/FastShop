package com.bashirli.fastshop.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bashirli.fastshop.R;
import com.bashirli.fastshop.databinding.RecyclerfavoritesBinding;
import com.bashirli.fastshop.model.RetrofitResponse;
import com.bashirli.fastshop.view.activity.PopUpActivity;
import com.bashirli.fastshop.view.activity.ViewItemActivity;
import com.bashirli.fastshop.view.activity.util.Util;

import java.util.ArrayList;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesHolder> {
    public ArrayList<RetrofitResponse> arrayList;
    private Util util=new Util();

    public FavoritesAdapter(ArrayList<RetrofitResponse> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public FavoritesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       RecyclerfavoritesBinding binding=RecyclerfavoritesBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new FavoritesHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesHolder holder, int position) {
    util.setImageURL(holder.binding.recyclerImage,holder.itemView.getContext()
    ,arrayList.get(position).imageURL,util.placeHolder(holder.itemView.getContext()));

    holder.binding.recyclerTitle.setText(arrayList.get(position).title);
        holder.binding.recyclerAbout.setText(arrayList.get(position).description);
        holder.binding.recyclerPrice.setText(arrayList.get(position).price);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent=new Intent(holder.itemView.getContext(), PopUpActivity.class);
                intent.putExtra("data",arrayList.get(position));
                holder.itemView.getContext().startActivity(intent);

                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent=new Intent(holder.itemView.getContext(), ViewItemActivity.class);
                    intent.putExtra("data",arrayList.get(position));
                    holder.itemView.getContext().startActivity(intent);
                }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void updateList(List<RetrofitResponse> newArrayList){
        arrayList.clear();
        arrayList.addAll(newArrayList);
        notifyDataSetChanged();
    }

    public class FavoritesHolder extends RecyclerView.ViewHolder {
        private RecyclerfavoritesBinding binding;
        public FavoritesHolder(@NonNull RecyclerfavoritesBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
