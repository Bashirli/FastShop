package com.bashirli.fastshop.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bashirli.fastshop.R;
import com.bashirli.fastshop.databinding.ActivitySplashBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {
private ActivitySplashBinding binding;
private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySplashBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        ConnectivityManager cm=(ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo()==null){
            AlertDialog.Builder alert=new AlertDialog.Builder(this);
            alert.setTitle(R.string.attention);
            alert.setMessage(R.string.InternetProblem);
            alert.setCancelable(false);
            alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();

                }
            }).create().show();
            return;
        }


        auth=FirebaseAuth.getInstance();
        if(auth.getCurrentUser()==null){
            auth.signInAnonymously();
        }


        Handler handler=new Handler();
        startAnim();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
            startActivity(new Intent(SplashActivity.this,ScreenActivity.class));
            finish();
            }
        },1400);


    }

    private void startAnim(){
        Animation animation=AnimationUtils.loadAnimation(this,R.anim.anim2);
        binding.imageView.startAnimation(animation);
    }

}

