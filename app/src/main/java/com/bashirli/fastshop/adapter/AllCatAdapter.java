package com.bashirli.fastshop.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bashirli.fastshop.R;
import com.bashirli.fastshop.databinding.RecyclerallBinding;
import com.bashirli.fastshop.model.RetrofitResponse;
import com.bashirli.fastshop.view.activity.util.Util;
import com.bashirli.fastshop.view.activity.PopUpActivity;
import com.bashirli.fastshop.view.activity.ViewItemActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class AllCatAdapter extends RecyclerView.Adapter<AllCatAdapter.MyHolder> {
    private List<RetrofitResponse> arrayList;
    private Util util=new Util();
    private FirebaseAuth auth;


    public AllCatAdapter(List<RetrofitResponse> arrayList) {

        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerallBinding binding=RecyclerallBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        auth=FirebaseAuth.getInstance();
        util.setImageURL(holder.binding.imageView2
                ,holder.itemView.getContext()
                ,arrayList.get(position).imageURL
                ,util.placeHolder(holder.itemView.getContext()));
        
        holder.binding.textView3.setText(arrayList.get(position).title);
        holder.binding.textView4.setText(arrayList.get(position).price);

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
    private RecyclerallBinding binding;
        public MyHolder(@NonNull RecyclerallBinding binding) {
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
