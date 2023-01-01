package com.bashirli.fastshop.view.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bashirli.fastshop.R;
import com.bashirli.fastshop.adapter.CommentAdapter;
import com.bashirli.fastshop.databinding.ActivityViewItemBinding;
import com.bashirli.fastshop.model.CommentData;
import com.bashirli.fastshop.model.RetrofitResponse;
import com.bashirli.fastshop.mvvm.ViewItemMVVM;
import com.bashirli.fastshop.view.activity.util.Util;

import java.util.ArrayList;

public class ViewItemActivity extends AppCompatActivity {
    private ActivityViewItemBinding binding;
    private ViewItemMVVM viewModel;
    private RetrofitResponse retrofitResponse;
    private CommentAdapter commentAdapter;
    private Boolean isFavorite;
    private Boolean isOnCart;
    private Util util=new Util();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityViewItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel=new ViewModelProvider(ViewItemActivity.this).get(ViewItemMVVM.class);
        retrofitResponse=(RetrofitResponse) getIntent().getSerializableExtra("data");
        commentAdapter=new CommentAdapter(new ArrayList<CommentData>());
        binding.recyclerComment.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerComment.setAdapter(commentAdapter);
        binding.recyclerComment.setVisibility(View.GONE);
        binding.addCommentLayout.setVisibility(View.GONE);


        checkMyDatabase();
        loadData();

        onClick();
    }

    private void onClick(){
        binding.textView20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnCart){
                    viewModel.deleteFromCart(retrofitResponse.id);
                    isOnCart=false;
                    return;
                }else{
                    viewModel.addToCart(retrofitResponse.id);
                    isOnCart=true;
                    return;
                }
            }
        });


        binding.commentsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.recyclerComment.getVisibility()==View.GONE){
                    binding.recyclerComment.setVisibility(View.VISIBLE);
                    binding.addCommentLayout.setVisibility(View.VISIBLE);
                   loadComments();
                }else{
                    binding.recyclerComment.setVisibility(View.GONE);
                    binding.addCommentLayout.setVisibility(View.GONE);
                    viewModel.stopDatabase();
                }
            }
        });
        binding.goBackViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFavorite){
                    viewModel.getIsFavorite().setValue(false);
                    binding.favoriteButton.setImageResource(android.R.drawable.btn_star_big_off);
                    isFavorite=false;
                }else{
                    viewModel.getIsFavorite().setValue(true);
                    binding.favoriteButton.setImageResource(android.R.drawable.btn_star_big_on);
                    isFavorite=true;
                }

                viewModel.addOrDeleteFromFavorite(retrofitResponse.id);
                observeAddOrDelete();

            }
        });
        binding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishCom(v);
            }
        });


    }

    private void publishCom(View view){
        if(binding.editTextTextPersonName5.getText().toString().isEmpty()){
            Toast.makeText(this, R.string.errorEmpty, Toast.LENGTH_SHORT).show();
        return;
        }
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setTitle(R.string.attention).setMessage(R.string.publishMsg)
                .setCancelable(false)
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.stopDatabase();
                        viewModel.publishComment(retrofitResponse.id,binding.editTextTextPersonName5.getText().toString());

                        observeComment();
                    }
                }).create().show();


    }

    private void loadComments(){
        viewModel.loadComments(retrofitResponse.id,
                commentAdapter);
        viewModel.getLoading2().observe(ViewItemActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    binding.progressBar8.setVisibility(View.VISIBLE);
                    binding.recyclerComment.setVisibility(View.GONE);
                }
            }
        });
        viewModel.getErrorData2().observe(ViewItemActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    binding.progressBar8.setVisibility(View.GONE);
                    Toast.makeText(ViewItemActivity.this,viewModel.getErrorMessage()
                            ,Toast.LENGTH_SHORT).show();
                    binding.recyclerComment.setVisibility(View.GONE);

                }
            }
        });
        viewModel.getSuccess2().observe(ViewItemActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    binding.progressBar8.setVisibility(View.GONE);
                    binding.recyclerComment.setVisibility(View.VISIBLE);

                }
            }
        });


    }


    private void observeComment(){
        ProgressDialog  progressDialog=new ProgressDialog(this);
        progressDialog.setTitle(R.string.attention);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);

        viewModel.getLoading3().observe(ViewItemActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
               if(aBoolean){
                   progressDialog.show();
               }
            }
        });
        viewModel.getSuccess3().observe(ViewItemActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
        if(aBoolean){
            progressDialog.cancel();
            Toast.makeText(ViewItemActivity.this, R.string.sucCom, Toast.LENGTH_SHORT).show();
            loadComments();
        }
            }
        });
        viewModel.getErrorData3().observe(ViewItemActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
               if(aBoolean){
                   progressDialog.cancel();
                   Toast.makeText(ViewItemActivity.this, viewModel.getErrorMessage(), Toast.LENGTH_SHORT).show();

               }
            }
        });
    }


    private void observeAddOrDelete(){
        viewModel.getSuccess().observe(ViewItemActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    isFavorite=viewModel.getIsFavorite().getValue();
                    if(viewModel.getIsFavorite().getValue()){
                        binding.favoriteButton.setImageResource(android.R.drawable.btn_star_big_on);
                    }else{
                        binding.favoriteButton.setImageResource(android.R.drawable.btn_star_big_off);
                    }
                    binding.progressBar9.setVisibility(View.GONE);
                    binding.mainConsItemView.setVisibility(View.VISIBLE);
                    viewModel.stopDatabase();
                }
            }
        });
        viewModel.getLoading().observe(ViewItemActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    binding.progressBar9.setVisibility(View.VISIBLE);
                    binding.mainConsItemView.setVisibility(View.GONE);
                }
            }
        });
        viewModel.getErrorData().observe(ViewItemActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    binding.progressBar9.setVisibility(View.GONE);
                    binding.mainConsItemView.setVisibility(View.GONE);
                    viewModel.stopDatabase();
                    Toast.makeText(ViewItemActivity.this, viewModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void loadData(){
        util.setImageURL(binding.imageView9,
                ViewItemActivity.this,
                retrofitResponse.imageURL,
                util.placeHolder(ViewItemActivity.this));

        binding.ratingBar.setRating(Float.parseFloat(retrofitResponse.ratings.rating));
        binding.itemViewCount.setText("Count : "+retrofitResponse.ratings.count);
        binding.itemTitle.setText(retrofitResponse.title);
        binding.itemPrice.setText(retrofitResponse.price);
        binding.itemAbout.setText(retrofitResponse.description);


    }


    private void checkMyDatabase(){
      viewModel.checkDatabase(retrofitResponse.id);
    viewModel.getSuccess4().observe(ViewItemActivity.this, new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean aBoolean) {
            if (aBoolean){
                isFavorite=viewModel.getIsFavorite().getValue();
                if(viewModel.getIsFavorite().getValue()){
                    binding.favoriteButton.setImageResource(android.R.drawable.btn_star_big_on);
                }else{
                    binding.favoriteButton.setImageResource(android.R.drawable.btn_star_big_off);
                }
                binding.progressBar9.setVisibility(View.GONE);
                binding.mainConsItemView.setVisibility(View.VISIBLE);
                viewModel.stopDatabase();
                checkCart();

            }
        }
    });
        viewModel.getLoading4().observe(ViewItemActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                binding.progressBar9.setVisibility(View.VISIBLE);
                binding.mainConsItemView.setVisibility(View.GONE);
                }
            }
        });
        viewModel.getErrorData4().observe(ViewItemActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    binding.progressBar9.setVisibility(View.GONE);
                    binding.mainConsItemView.setVisibility(View.GONE);
                    viewModel.stopDatabase();
                    Toast.makeText(ViewItemActivity.this, viewModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void checkCart(){
        viewModel.checkIsOnCart(ViewItemActivity.this, retrofitResponse.id);
        viewModel.getLoadingCart().observe(ViewItemActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    binding.progressBar9.setVisibility(View.VISIBLE);
                    binding.mainConsItemView.setVisibility(View.GONE);
                }
            }
        });
        viewModel.getSuccessCart().observe(ViewItemActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                      isOnCart=viewModel.getOnCart();
                    binding.progressBar9.setVisibility(View.GONE);
                    binding.mainConsItemView.setVisibility(View.VISIBLE);
                }
            }
        });
        viewModel.getErrorCart().observe(ViewItemActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    binding.progressBar9.setVisibility(View.GONE);
                    binding.mainConsItemView.setVisibility(View.GONE);

                    Toast.makeText(ViewItemActivity.this, viewModel.getCartErrorMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.stopDatabase();
        finish();
    }
}