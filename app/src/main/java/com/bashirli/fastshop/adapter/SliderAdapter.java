package com.bashirli.fastshop.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bashirli.fastshop.databinding.SliderBinding;
import com.bashirli.fastshop.view.activity.util.Util;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderHolder> {
    private ArrayList<String> images;
    private Util util=new Util();

    public SliderAdapter(ArrayList<String> images) {
        this.images = images;
    }

    @Override
    public SliderHolder onCreateViewHolder(ViewGroup parent) {
       SliderBinding binding=SliderBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new SliderHolder(binding);
    }

    @Override
    public void onBindViewHolder(SliderHolder viewHolder, int position) {
        util.setImageURL(viewHolder.binding.sliderImage
                ,viewHolder.itemView.getContext()
                ,images.get(position),
                util.placeHolder(viewHolder.itemView.getContext()));
    }

    @Override
    public int getCount() {
        return images.size();
    }

    public class SliderHolder extends SliderViewAdapter.ViewHolder{
        private SliderBinding binding;
        public SliderHolder(SliderBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
