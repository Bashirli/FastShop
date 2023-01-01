package com.bashirli.fastshop.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bashirli.fastshop.databinding.CommentrecyclerBinding;
import com.bashirli.fastshop.model.CommentData;
import com.bashirli.fastshop.view.activity.util.Util;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    private ArrayList<CommentData> arrayList;
    private Util util=new Util();

    public CommentAdapter(ArrayList<CommentData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CommentrecyclerBinding binding=CommentrecyclerBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new CommentHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
    util.setImageURL(holder.binding.recyclerPP
            ,holder.itemView.getContext(),
            arrayList.get(position).getImageURL(),
            util.placeHolder(holder.itemView.getContext()));
    holder.binding.recyclerNickname.setText(arrayList.get(position).getEmail());
    holder.binding.recyclerPublishedComment.setText(arrayList.get(position).getComment());

        Timestamp timestamp=arrayList.get(position).getTimestamp();
        Date date=timestamp.toDate();
        String myDate=android.text.format.DateFormat.getDateFormat(holder.itemView.getContext())
                .format(date).toString();

        holder.binding.recyclerPublishedTime.setText(myDate);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder{
        private CommentrecyclerBinding binding;
        public CommentHolder(@NonNull CommentrecyclerBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }

    public void updateData(ArrayList<CommentData> newArrayList){
        arrayList.clear();
        arrayList.addAll(newArrayList);
        notifyDataSetChanged();
    }

}
