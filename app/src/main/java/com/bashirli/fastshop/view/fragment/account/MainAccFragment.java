package com.bashirli.fastshop.view.fragment.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.bashirli.fastshop.R;
import com.bashirli.fastshop.databinding.FragmentMainAccBinding;
import com.bashirli.fastshop.view.activity.ScreenActivity;

public class MainAccFragment extends Fragment {
    private FragmentMainAccBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_acc, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding=FragmentMainAccBinding.bind(view);
        animations();
        onclick();


    }

    private void animations(){
        Animation animation= AnimationUtils.loadAnimation(requireContext(),R.anim.anim3);
        Animation animation2= AnimationUtils.loadAnimation(requireContext(),R.anim.anim4);
        Animation animation3=AnimationUtils.loadAnimation(requireContext(),R.anim.anim1);

        binding.imageView4.setAnimation(animation3);
        binding.textView9.setAnimation(animation);
        binding.textView6.setAnimation(animation);
        binding.textView10.setAnimation(animation2);
        binding.textView11.setAnimation(animation2);
    }

    private void onclick(){
        binding.textView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireActivity(), ScreenActivity.class));
                requireActivity().finish();
            }
        });

        binding.textView10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections navDirections=MainAccFragmentDirections.actionMainAccFragmentToLoginFragment();
                Navigation.findNavController(requireActivity(),R.id.fragmentContainerView2).navigate(navDirections);

            }
        });


        binding.textView11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections navDirections=MainAccFragmentDirections.actionMainAccFragmentToRegisterFragment();
                Navigation.findNavController(requireActivity(),R.id.fragmentContainerView2).navigate(navDirections);
            }
        });
    }


}