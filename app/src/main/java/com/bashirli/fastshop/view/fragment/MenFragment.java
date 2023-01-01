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
import com.bashirli.fastshop.databinding.FragmentMenBinding;
import com.bashirli.fastshop.model.RetrofitResponse;
import com.bashirli.fastshop.mvvm.MenCatMVVM;

import java.util.List;

public class MenFragment extends Fragment {
    private FragmentMenBinding binding;
    private MenCatMVVM viewModel;
    private MenAndWomenAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_men, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel=new ViewModelProvider(requireActivity()).get(MenCatMVVM.class);
        binding=FragmentMenBinding.bind(view);
        viewModel.getData();

        binding.menRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.linearLayout.setVisibility(View.GONE);
                binding.progressBar2.setVisibility(View.VISIBLE);
                viewModel.refreshData();
                binding.menRefresh.setRefreshing(false);
            }
        });


        observeData();
    }

    private void observeData(){
        viewModel.loading.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    binding.progressBar2.setVisibility(View.VISIBLE);
                    binding.linearLayout.setVisibility(View.GONE);
                }else{
                    binding.progressBar2.setVisibility(View.GONE);
                }
            }
        });

        viewModel.error.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    binding.progressBar2.setVisibility(View.GONE);
                    binding.linearLayout.setVisibility(View.GONE);
                    Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.myData.observe(getViewLifecycleOwner(), new Observer<List<RetrofitResponse>>() {
            @Override
            public void onChanged(List<RetrofitResponse> retrofitResponses) {
                if(retrofitResponses!=null){
                    binding.progressBar2.setVisibility(View.GONE);
                    binding.linearLayout.setVisibility(View.VISIBLE);
                    setup(retrofitResponses);
                }else{
                    binding.linearLayout.setVisibility(View.GONE);
                }
            }
        });

    }

    private void setup(List<RetrofitResponse> list){
        adapter=new MenAndWomenAdapter(list);
        binding.recyclerMen.setLayoutManager(new GridLayoutManager(requireContext(),2));
        binding.recyclerMen.setAdapter(adapter);
    }


}