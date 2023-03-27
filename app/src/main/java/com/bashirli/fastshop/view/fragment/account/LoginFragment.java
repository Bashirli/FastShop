package com.bashirli.fastshop.view.fragment.account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.bashirli.fastshop.R;
import com.bashirli.fastshop.databinding.FragmentLoginBinding;
import com.bashirli.fastshop.mvvm.LoginMVVM;
import com.bashirli.fastshop.view.activity.ScreenActivity;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private LoginMVVM viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding=FragmentLoginBinding.bind(view);
        startAnim();
        viewModel=new ViewModelProvider(requireActivity()).get(LoginMVVM.class);
        onClicks(view);
    }

    private void onClicks(View view){
        binding.goBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections navDirections=LoginFragmentDirections.actionLoginFragmentToMainAccFragment();
                Navigation.findNavController(requireActivity(),R.id.fragmentContainerView2).navigate(navDirections);
            }
        });

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(view);
            }
        });
        binding.forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections navDirections=LoginFragmentDirections.actionLoginFragmentToForgotPassFragment();
                Navigation.findNavController(requireActivity(),R.id.fragmentContainerView2).navigate(navDirections);
            }
        });


    }

    private boolean error(){
        if(binding.editTextTextPersonName2.getText().toString().isEmpty()||
        binding.editTextTextPassword2.getText().toString().isEmpty()){
            Toast.makeText(requireContext(), R.string.errorEmpty, Toast.LENGTH_SHORT).show();
        return true;
        }
        if(binding.editTextTextPassword2.getText().toString().length()<6){
            Toast.makeText(requireContext(), R.string.smallpass, Toast.LENGTH_SHORT).show();
        return true;
        }


        return  false;
    }
    private void login(View view){
        if(error()){
            return;
        }
        viewModel.login(binding.editTextTextPersonName2.getText().toString(),binding.editTextTextPassword2.getText().toString());
        observeData();



    }

    private void observeData(){
        ProgressDialog progressDialog=new ProgressDialog(requireContext());
        progressDialog.setTitle(R.string.attention);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        viewModel.loading.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    progressDialog.show();
                }
            }
        });

        viewModel.success.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
               if(aBoolean){
                   progressDialog.cancel();
                   Toast.makeText(requireContext(),R.string.welcome,Toast.LENGTH_SHORT).show();
                   startActivity(new Intent(requireActivity(), ScreenActivity.class));
                   requireActivity().finish();
               }
            }
        });

        viewModel.error.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    progressDialog.cancel();
                    Toast.makeText(requireContext(), viewModel.errorMessage.getValue(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void startAnim(){
        binding.loginHello.setTranslationY(1000);
        binding.loginHello.animate().setDuration(800).translationY(0).start();
        Animation animation= AnimationUtils.loadAnimation(requireContext(), R.anim.anim3);
        Animation animation2= AnimationUtils.loadAnimation(requireContext(),R.anim.anim4);
        Animation animation3=AnimationUtils.loadAnimation(requireContext(),R.anim.anim1);

        binding.loginButton.setAnimation(animation3);
        binding.editTextTextPassword2.setAnimation(animation3);
        binding.editTextTextPersonName2.setAnimation(animation3);
        binding.goBackLogin.setAnimation(animation);
    }

}
