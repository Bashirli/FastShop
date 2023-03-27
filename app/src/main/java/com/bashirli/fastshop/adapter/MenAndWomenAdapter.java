package com.bashirli.fastshop.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bashirli.fastshop.R;
import com.bashirli.fastshop.databinding.RecyclermenwomenBinding;
import com.bashirli.fastshop.model.RetrofitResponse;
import com.bashirli.fastshop.view.activity.PopUpActivity;
import com.bashirli.fastshop.view.activity.ViewItemActivity;
import com.bashirli.fastshop.view.activity.util.Util;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MenAndWomenAdapter extends RecyclerView.Adapter<MenAndWomenAdapter.MyHolder> {
    private List<RetrofitResponse> arrayList;
    private Util util=new Util();
    private FirebaseAuth auth;


    public MenAndWomenAdapter(List<RetrofitResponse> arrayList) {

        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclermenwomenBinding binding=RecyclermenwomenBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        util.setImageURL(holder.binding.imageView6
                ,holder.itemView.getContext()
                ,arrayList.get(position).imageURL
                ,util.placeHolder(holder.itemView.getContext()));

        holder.binding.textView8.setText(arrayList.get(position).title);
        holder.binding.textView7.setText(arrayList.get(position).price);
        auth=FirebaseAuth.getInstance();
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent=new Intent(holder.itemView.getContext(), PopUpActivity.class);
                intent.putExtra("data",arrayList.get(position));
                holder.itemView.getContext().startActivity(intent);

                return false;
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(auth.getCurrentUser().isAnonymous()){
                    Toast.makeText(holder.itemView.getContext(), R.string.perItem,Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(holder.itemView.getContext(), ViewItemActivity.class);
                    intent.putExtra("data",arrayList.get(position));
                    holder.itemView.getContext().startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        private RecyclermenwomenBinding binding;
        public MyHolder(@NonNull RecyclermenwomenBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
    public void updateList(List<RetrofitResponse> list){
        arrayList.clear();
        arrayList.addAll(list);
        notifyDataSetChanged();
    }
}
