package com.bashirli.fastshop.view.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bashirli.fastshop.databinding.ActivityPopUpBinding;
import com.bashirli.fastshop.model.RetrofitResponse;
import com.bashirli.fastshop.view.activity.util.Util;

public class PopUpActivity extends AppCompatActivity {
    @Override
    protected void onStart() {
        super.onStart();
        setVisible(true);
    }
    private ActivityPopUpBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPopUpBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
       RetrofitResponse retrofitResponse= (RetrofitResponse) getIntent().getSerializableExtra("data");

        Util util=new Util();
        util.setImageURL(binding.imageView3,this,retrofitResponse.imageURL,util.placeHolder(this));
        binding.textView2.setText(retrofitResponse.title);
        binding.textView5.setText(retrofitResponse.price);

    }
}