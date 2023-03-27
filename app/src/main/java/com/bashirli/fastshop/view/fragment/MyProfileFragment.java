package com.bashirli.fastshop.view.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.bashirli.fastshop.databinding.FragmentMyProfileBinding;
import com.bashirli.fastshop.model.UserData;
import com.bashirli.fastshop.mvvm.MyProfileMVVM;
import com.bashirli.fastshop.view.activity.util.Util;
import com.bashirli.fastshop.view.activity.ScreenActivity;

public class MyProfileFragment extends Fragment {
    private FragmentMyProfileBinding binding;
    private MyProfileMVVM viewModel;
    private  Util util=new Util();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding=FragmentMyProfileBinding.bind(view);
        viewModel=new ViewModelProvider(requireActivity()).get(MyProfileMVVM.class);
        viewModel.getUserData();
        observeData();

        onClicks();
    }

    private void onClicks(){
        binding.goBackMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections navDirections=MyProfileFragmentDirections.actionMyProfileFragmentToScreenFragment();
                Navigation.findNavController(requireActivity(),R.id.fragmentContainerView).navigate(navDirections);
            }
        });
        binding.editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            NavDirections navDirections=MyProfileFragmentDirections.actionMyProfileFragmentToEditProfileFragment();
            Navigation.findNavController(requireActivity(),R.id.fragmentContainerView).navigate(navDirections);
            }
        });
        binding.support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireContext(), R.string.asap, Toast.LENGTH_SHORT).show();
            }
        });
        binding.restorePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        binding.logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert=new AlertDialog.Builder(requireContext());
                alert.setTitle(R.string.attention).setMessage(R.string.logout)
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        }).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                viewModel.signOutUser();
                                observeSignOut();


                            }
                        }).create().show();
            }
        });
        binding.exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            requireActivity().finish();
            }
        });
    }

    private void observeData(){
        viewModel.getLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    binding.progressBar4.setVisibility(View.VISIBLE);
                    binding.myProfileImageView.setVisibility(View.INVISIBLE);
                    binding.myProfileNickname.setVisibility(View.INVISIBLE);
                    binding.linearSettings.setVisibility(View.INVISIBLE);

                }
                }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    binding.progressBar4.setVisibility(View.GONE);
                    binding.myProfileImageView.setVisibility(View.INVISIBLE);
                    binding.linearSettings.setVisibility(View.VISIBLE);
                    binding.myProfileNickname.setVisibility(View.INVISIBLE);
                    Toast.makeText(requireContext(),viewModel.getErrorMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewModel.getSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
               if(aBoolean){
                   binding.progressBar4.setVisibility(View.GONE);
                   binding.myProfileImageView.setVisibility(View.VISIBLE);
                   binding.myProfileNickname.setVisibility(View.VISIBLE);
                   binding.linearSettings.setVisibility(View.VISIBLE);
               }
            }
        });

        viewModel.getUser().observe(getViewLifecycleOwner(), new Observer<UserData>() {
            @Override
            public void onChanged(UserData userData) {
                if(userData!=null){
                   util.setImageURL(binding.myProfileImageView,requireContext(),userData.getImageURL(),util.placeHolder(requireContext()));
                binding.myProfileNickname.setText(userData.getNickname());
                }
            }
        });

    }


    private void observeSignOut(){
        ProgressDialog progressDialog=new ProgressDialog(requireContext());
        progressDialog.setTitle(R.string.attention);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        viewModel.getLoading2().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    progressDialog.show();
                }
            }
        });

        viewModel.getErrorData2().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
          if(aBoolean){
              progressDialog.cancel();
              Toast.makeText(requireContext(),viewModel.getErrorMessage(),Toast.LENGTH_SHORT).show();
          }
            }
        });
        viewModel.getSuccess2().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
      if(aBoolean){
          progressDialog.cancel();
          Toast.makeText(requireContext(), R.string.suc, Toast.LENGTH_SHORT).show();
          startActivity(new Intent(requireActivity(), ScreenActivity.class));
          requireActivity().finish();
      }
            }
        });

    }


}