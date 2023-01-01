package com.bashirli.fastshop.view.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bashirli.fastshop.R;
import com.bashirli.fastshop.adapter.AllCatAdapter;
import com.bashirli.fastshop.databinding.FragmentSearchBinding;
import com.bashirli.fastshop.model.RetrofitResponse;
import com.bashirli.fastshop.mvvm.SearchMVVM;

import java.util.ArrayList;


public class SearchFragment extends Fragment {
private FragmentSearchBinding binding;
private SearchMVVM viewModel;
private   AllCatAdapter allCatAdapter=new AllCatAdapter(new ArrayList<RetrofitResponse>());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding=FragmentSearchBinding.bind(view);
    viewModel=new ViewModelProvider(requireActivity()).get(SearchMVVM.class);

    viewModel.loadData();
    observeLoad();
    binding.recyclerView.setAdapter(allCatAdapter);
    binding.recyclerView.setLayoutManager(new GridLayoutManager(requireContext(),2));
    textChangeListener();

    binding.textView16.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            NavDirections navDirections=SearchFragmentDirections.actionSearchFragmentToScreenFragment();
            Navigation.findNavController(requireActivity(),R.id.fragmentContainerView).navigate(navDirections);
        }
    });
    }

    private void textChangeListener(){
        binding.editText.addTextChangedListener(new TextWatcher() {
            Handler handler=new Handler();


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Runnable runnable=new Runnable() {
                    @Override
                    public void run() {
                        if(!editable.toString().isEmpty()) {
                            viewModel.search(editable.toString(), allCatAdapter);
                            searchObserve();
                        }
                    }
                };
                binding.progressBar7.setVisibility(View.VISIBLE);
               if(!editable.toString().isEmpty()){
                   handler.removeCallbacks(runnable);
                   handler.postDelayed(runnable,1000);
                   binding.progressBar5.setVisibility(View.INVISIBLE);
               }else{
                   handler.removeCallbacks(runnable);
                   binding.recyclerView.setVisibility(View.INVISIBLE);
                   binding.textView15.setVisibility(View.VISIBLE);
                   binding.progressBar7.setVisibility(View.GONE);
                   binding.textView15.setText(R.string.NothingToShow);
                   binding.editText.setVisibility(View.VISIBLE);
               }
            }
        });

    }
    private void searchObserve(){
        viewModel.getLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    binding.recyclerView.setVisibility(View.INVISIBLE);
                 binding.progressBar7.setVisibility(View.VISIBLE);
                 binding.textView15.setVisibility(View.GONE);
                }
            }
        });
        viewModel.getSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    binding.recyclerView.setVisibility(View.VISIBLE);
                        binding.progressBar7.setVisibility(View.GONE);
                        binding.textView15.setVisibility(View.GONE);
                }
            }
        });
        viewModel.getError().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    binding.recyclerView.setVisibility(View.INVISIBLE);
                    binding.progressBar7.setVisibility(View.GONE);
                    binding.textView15.setVisibility(View.VISIBLE);
                    binding.editText.setVisibility(View.VISIBLE);
                    binding.textView15.setText(viewModel.getErrorMessage());

                }
            }
        });


    }


    private void observeLoad(){
        viewModel.getLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    binding.progressBar5.setVisibility(View.VISIBLE);
                    binding.recyclerView.setVisibility(View.INVISIBLE);
                    binding.editText.setVisibility(View.INVISIBLE);
                    binding.textView15.setVisibility(View.INVISIBLE);
                }
            }
        });
        viewModel.getSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    binding.progressBar5.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    binding.editText.setVisibility(View.VISIBLE);
                    binding.textView15.setVisibility(View.VISIBLE);
                }
            }
        });
        viewModel.getError().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
            if(aBoolean){
                binding.progressBar5.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.GONE);
                binding.editText.setVisibility(View.GONE);
                binding.textView15.setText(R.string.error);
            }
            }
        });


    }

}