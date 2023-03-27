package com.bashirli.fastshop.adapter;

import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bashirli.fastshop.R;
import com.bashirli.fastshop.databinding.RecyclercartBinding;
import com.bashirli.fastshop.model.RetrofitResponse;
import com.bashirli.fastshop.mvvm.CartMVVM;
import com.bashirli.fastshop.roomdb.RoomDAO;
import com.bashirli.fastshop.roomdb.RoomDB;
import com.bashirli.fastshop.view.activity.PopUpActivity;
import com.bashirli.fastshop.view.activity.ViewItemActivity;
import com.bashirli.fastshop.view.activity.util.Util;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {
    public ArrayList<RetrofitResponse> arrayList;
    public ArrayList<Integer> numberList;
    Util util=new Util();
    private RoomDB roomDB;
    private RoomDAO roomDAO;
    private CartMVVM requiredViewModel;


    public CartAdapter(ArrayList<RetrofitResponse> arrayList,ArrayList<Integer> numberList,CartMVVM requiredViewModel) {
         this.arrayList = arrayList;
         this.numberList=numberList;
         this.requiredViewModel=requiredViewModel;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclercartBinding binding=RecyclercartBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new CartHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {
        roomDB= Room.databaseBuilder(holder.itemView.getContext(),RoomDB.class,"db").allowMainThreadQueries().build();
        roomDAO=roomDB.getDao();
    util.setImageURL(holder.binding.cartImage
    ,holder.itemView.getContext(),arrayList.get(position).imageURL,util.placeHolder(holder.itemView.getContext()));

    holder.binding.cartTitle.setText(arrayList.get(position).title);
    holder.binding.cartPrice.setText(arrayList.get(position).price);
    holder.binding.numberOfSelectedItem.setText(String.valueOf(numberList.get(position)));

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

    changeNumber(holder.binding,position);

    }

    private void changeNumber(RecyclercartBinding binding,int position){
        Handler handler=new Handler();
        Runnable minusRunnable=new Runnable() {
            @Override
            public void run() {
                int val= Integer.parseInt(binding.numberOfSelectedItem.getText().toString());
                updateNumber(val,arrayList.get(position).id);

            }
        };
        Runnable plusRunnable=new Runnable() {
            @Override
            public void run() {
                int val= Integer.parseInt(binding.numberOfSelectedItem.getText().toString());
                updateNumber(val,arrayList.get(position).id);
            }
        };



        binding.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(binding.numberOfSelectedItem.getText().toString())==1){
                    return;
                }else{
                    int val= Integer.parseInt(binding.numberOfSelectedItem.getText().toString())-1;
                    binding.numberOfSelectedItem.setText(String.valueOf(val));
                    handler.removeCallbacks(plusRunnable);
                    handler.removeCallbacks(minusRunnable);
                    handler.postDelayed(minusRunnable,500);
                }
            }
        });
        binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int val= Integer.parseInt(binding.numberOfSelectedItem.getText().toString())+1;
                binding.numberOfSelectedItem.setText(String.valueOf(val));
                handler.removeCallbacks(plusRunnable);
                handler.removeCallbacks(minusRunnable);
                handler.postDelayed(plusRunnable,500);
            }
        });
    }

    private void updateNumber(int number,int itemId){
    roomDAO.setNewValue(number,itemId);
    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CartHolder extends RecyclerView.ViewHolder {
        private RecyclercartBinding binding;
    public CartHolder(@NonNull RecyclercartBinding binding) {
        super(binding.getRoot());
        this.binding=binding;
    }
}

    public void updateList(ArrayList<RetrofitResponse> newArrayList,ArrayList<Integer> newNumberList){
        arrayList.clear();
        numberList.clear();
        numberList.addAll(newNumberList);
        arrayList.addAll(newArrayList);
        notifyDataSetChanged();
    }

    public void updateNumberList(ArrayList<Integer> onlyNumberList){
        numberList.clear();
        numberList.addAll(onlyNumberList);
        notifyDataSetChanged();
    }


}
