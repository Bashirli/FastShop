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

import com.bashirli.fastshop.databinding.FragmentRegisterBinding;
import com.bashirli.fastshop.R;
import com.bashirli.fastshop.mvvm.RegisterMVVM;
import com.bashirli.fastshop.view.activity.ScreenActivity;

import java.util.HashMap;

public class RegisterFragment extends Fragment {
    private  FragmentRegisterBinding binding;
    private RegisterMVVM viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding=FragmentRegisterBinding.bind(view);
         startAnim();
        viewModel=new ViewModelProvider(requireActivity()).get(RegisterMVVM.class);
        binding.goBackReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections navDirections= RegisterFragmentDirections.actionRegisterFragmentToMainAccFragment();
                Navigation.findNavController(requireActivity(),R.id.fragmentContainerView2).navigate(navDirections);
            }
        });
        binding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndRegister();
            }
        });
      }

      private boolean error(){
        if(binding.editTextTextEmailAddress.getText().toString().isEmpty()||
            binding.editTextTextPassword.getText().toString().isEmpty()||
            binding.editTextTextPersonName.getText().toString().isEmpty()){
            Toast.makeText(requireContext(), R.string.errorEmpty, Toast.LENGTH_SHORT).show();
            return true;
        }
        if(binding.editTextTextPassword.getText().toString().length()<6){
            Toast.makeText(requireContext(), R.string.smallpass, Toast.LENGTH_SHORT).show();
        return true;
        }
        if(binding.editTextTextPersonName.getText().toString().length()>35){
            Toast.makeText(requireContext(), R.string.longname, Toast.LENGTH_SHORT).show();
       return true;
        }
        return false;
      }

      private void checkAndRegister(){
        if (error()){
            return;
        }

          HashMap<String,Object> userData=new HashMap<>();
        userData.put("nickname",binding.editTextTextPersonName.getText().toString());
          userData.put("email",binding.editTextTextEmailAddress.getText().toString());
          userData.put("profilePicture","null");
          userData.put("number","null");

          viewModel.register(
                  binding.editTextTextEmailAddress.getText().toString()
                  ,binding.editTextTextPassword.getText().toString()
                  ,userData);
        observeData();

      }

      private void observeData(){
          ProgressDialog progressDialog=new ProgressDialog(requireContext());
          progressDialog.setCancelable(false);
          progressDialog.setMessage("Please wait");
          progressDialog.setTitle(R.string.attention);



        viewModel.success.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
        if (aBoolean){
            progressDialog.cancel();
            Toast.makeText(requireContext(), R.string.accSuccess, Toast.LENGTH_SHORT).show();
            requireActivity().finish();
            startActivity(new Intent(requireActivity(), ScreenActivity.class));
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

        viewModel.loading.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    progressDialog.show();
                }
            }
        });

      }


    private void startAnim(){
        binding.textView12.setTranslationY(1000);
        binding.textView12.animate().setDuration(800).translationY(0).start();
        Animation animation= AnimationUtils.loadAnimation(requireContext(),R.anim.anim3);
        Animation animation2= AnimationUtils.loadAnimation(requireContext(),R.anim.anim4);
        Animation animation3=AnimationUtils.loadAnimation(requireContext(),R.anim.anim1);

        binding.buttonRegister.setAnimation(animation3);
        binding.editTextTextEmailAddress.setAnimation(animation3);
        binding.editTextTextPassword.setAnimation(animation3);
        binding.editTextTextPersonName.setAnimation(animation3);
        binding.goBackReg.setAnimation(animation);
    }

}