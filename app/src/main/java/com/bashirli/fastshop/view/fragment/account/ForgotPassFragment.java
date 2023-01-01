package com.bashirli.fastshop.view.fragment.account;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.bashirli.fastshop.R;
import com.bashirli.fastshop.databinding.FragmentForgotPassBinding;
import com.bashirli.fastshop.mvvm.ForgotPassMVVM;

public class ForgotPassFragment extends Fragment {
    private FragmentForgotPassBinding binding;
    private ForgotPassMVVM viewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_pass, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding=FragmentForgotPassBinding.bind(view);

        viewModel=new ViewModelProvider(requireActivity()).get(ForgotPassMVVM.class);


        binding.goBackForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections navDirections=ForgotPassFragmentDirections.actionForgotPassFragmentToLoginFragment();
                Navigation.findNavController(requireActivity(),R.id.fragmentContainerView2).navigate(navDirections);
            }
        });

        binding.sendReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendReset(view);
            }
        });


    }


    private void sendReset(View view){
    if(binding.editTextTextEmailAddress2.getText().toString().isEmpty()){
        Toast.makeText(requireContext(), R.string.errorEmpty, Toast.LENGTH_SHORT).show();
        return;
    }



    viewModel.sendResetEmail(binding.editTextTextEmailAddress2.getText().toString());
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

        viewModel.error.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    progressDialog.cancel();
                    Toast.makeText(requireContext(), viewModel.errorMessage.getValue(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.success.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    progressDialog.cancel();
                    AlertDialog.Builder alert=new AlertDialog.Builder(requireContext());
                    alert.setTitle(R.string.attention).setMessage(R.string.resetInfo2)
                            .setCancelable(false)
                            .setNegativeButton(R.string.Ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    NavDirections navDirections=ForgotPassFragmentDirections.actionForgotPassFragmentToLoginFragment();
                                    Navigation.findNavController(requireActivity(),R.id.fragmentContainerView2).navigate(navDirections);
                                }
                            }).create().show();
                }
            }
        });

}

}