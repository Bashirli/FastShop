package com.bashirli.fastshop.view.fragment;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bashirli.fastshop.R;
import com.bashirli.fastshop.adapter.MenAndWomenAdapter;
import com.bashirli.fastshop.databinding.FragmentWomenBinding;
import com.bashirli.fastshop.model.RetrofitResponse;
import com.bashirli.fastshop.mvvm.WomenCatMVVM;

import java.util.List;

public class WomenFragment extends Fragment {
    private WomenCatMVVM viewModel;
    private FragmentWomenBinding binding;
    private MenAndWomenAdapter womenAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_women, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding=FragmentWomenBinding.bind(view);
        viewModel=new ViewModelProvider(requireActivity()).get(WomenCatMVVM.class);
        viewModel.getData();

    binding.womenRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            binding.progressBar3.setVisibility(View.VISIBLE);
            binding.linearWomen.setVisibility(View.GONE);
            viewModel.refreshData();
            binding.womenRefresh.setRefreshing(false);
        }
    });


        observeAllData();

    }

    private void observeAllData(){
        viewModel.loading.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    binding.progressBar3.setVisibility(View.VISIBLE);
                    binding.linearWomen.setVisibility(View.GONE);
                }else{
                    binding.progressBar3.setVisibility(View.GONE);
                }
            }
        });
        viewModel.error.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    binding.progressBar3.setVisibility(View.GONE);
                    binding.linearWomen.setVisibility(View.GONE);
                    Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.myData.observe(getViewLifecycleOwner(), new Observer<List<RetrofitResponse>>() {
            @Override
            public void onChanged(List<RetrofitResponse> list) {
                if(list!=null){
                    binding.progressBar3.setVisibility(View.GONE);
                    binding.linearWomen.setVisibility(View.VISIBLE);
                    setup(list);
                }
            }
        });


    }

    private void setup(List<RetrofitResponse> list){
        womenAdapter=new MenAndWomenAdapter(list);
        binding.recyclerWomen.setLayoutManager(new GridLayoutManager(requireContext(),2));
        binding.recyclerWomen.setAdapter(womenAdapter);

    }


}