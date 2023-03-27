package com.bashirli.fastshop.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.bashirli.fastshop.R;
import com.bashirli.fastshop.databinding.ActivityScreenBinding;
import com.bashirli.fastshop.view.fragment.CartFragmentDirections;
import com.bashirli.fastshop.view.fragment.EditProfileFragmentDirections;
import com.bashirli.fastshop.view.fragment.FavoritesFragmentDirections;
import com.bashirli.fastshop.view.fragment.MyProfileFragmentDirections;
import com.bashirli.fastshop.view.fragment.ScreenFragmentDirections;
import com.bashirli.fastshop.view.fragment.SearchFragmentDirections;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class ScreenActivity extends AppCompatActivity {
private ActivityScreenBinding binding;
private FirebaseAuth auth;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityScreenBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        auth=FirebaseAuth.getInstance();
        clicks(view);
    }

    private void clicks(View v){
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(auth.getCurrentUser().isAnonymous()){
                    Snackbar.make(v, R.string.reqacc,Snackbar.LENGTH_SHORT).show();
                    return;
                }else{
                    turnToMain();

                    NavDirections navDirections= ScreenFragmentDirections.actionScreenFragmentToCartFragment();
                    Navigation.findNavController(ScreenActivity.this,R.id.fragmentContainerView).navigate(navDirections);

                }

            }
        });



    binding.searchBar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            turnToMain();
            NavDirections navDirections= ScreenFragmentDirections.actionScreenFragmentToSearchFragment();
            Navigation.findNavController(ScreenActivity.this,R.id.fragmentContainerView).navigate(navDirections);

        }
    });

    binding.myProfile.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(auth.getCurrentUser().isAnonymous()){
            startActivity(new Intent(ScreenActivity.this, AccountActivity.class));
            finish();
            }else{
                turnToMain();
                NavDirections navDirections= ScreenFragmentDirections.actionScreenFragmentToMyProfileFragment();
                Navigation.findNavController(ScreenActivity.this,R.id.fragmentContainerView).navigate(navDirections);
            }
        }
    });

    binding.mainScreen.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            turnToMain();
        }
    });
    binding.myFavorites.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(auth.getCurrentUser().isAnonymous()){
                Toast.makeText(ScreenActivity.this, R.string.reqacc, Toast.LENGTH_SHORT).show();
            }else{
                turnToMain();
                NavDirections navDirections= ScreenFragmentDirections.actionScreenFragmentToFavoritesFragment();
                Navigation.findNavController(ScreenActivity.this,R.id.fragmentContainerView).navigate(navDirections);
            }
            }
    });

    }


    private void turnToMain(){
    try {
        NavDirections navDirections= SearchFragmentDirections.actionSearchFragmentToScreenFragment();
        Navigation.findNavController(ScreenActivity.this,R.id.fragmentContainerView).navigate(navDirections);
    }catch (Exception e){}
        try {
            NavDirections navDirections= FavoritesFragmentDirections.actionFavoritesFragmentToScreenFragment();
            Navigation.findNavController(ScreenActivity.this,R.id.fragmentContainerView).navigate(navDirections);
        }catch (Exception e){}

        try {
            NavDirections navDirections= MyProfileFragmentDirections.actionMyProfileFragmentToScreenFragment();
            Navigation.findNavController(ScreenActivity.this,R.id.fragmentContainerView).navigate(navDirections);
        }catch (Exception e){}

        try {
            NavDirections navDirections= EditProfileFragmentDirections.actionEditProfileFragmentToScreenFragment();
            Navigation.findNavController(ScreenActivity.this,R.id.fragmentContainerView).navigate(navDirections);
        }catch (Exception e){}
        try {
            NavDirections navDirections= CartFragmentDirections.actionCartFragmentToScreenFragment();
            Navigation.findNavController(ScreenActivity.this,R.id.fragmentContainerView).navigate(navDirections);
        }catch (Exception e){}


    }


}