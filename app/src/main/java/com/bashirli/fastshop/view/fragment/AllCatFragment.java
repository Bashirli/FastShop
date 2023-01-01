package com.bashirli.fastshop.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bashirli.fastshop.R;
import com.bashirli.fastshop.adapter.AllCatAdapter;
import com.bashirli.fastshop.adapter.SliderAdapter;
import com.bashirli.fastshop.databinding.FragmentAllCatBinding;
import com.bashirli.fastshop.model.Categories;
import com.bashirli.fastshop.model.RetrofitResponse;
import com.bashirli.fastshop.mvvm.AllCatMVVM;
import com.google.android.material.snackbar.Snackbar;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;

import java.util.ArrayList;
import java.util.List;


public class AllCatFragment extends Fragment {
private AllCatMVVM viewmodel;
private FragmentAllCatBinding binding;
private AllCatAdapter electronicAdapter,jeweleryAdapter,menAdapter,womenAdapter;
    private Categories categories=new Categories();
    private String[] category=categories.getCategories();
    private SliderAdapter sliderAdapter;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_cat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding=FragmentAllCatBinding.bind(view);
         viewmodel=new ViewModelProvider(this).get(AllCatMVVM.class);
        viewmodel.getData();

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewmodel.error.setValue(false);
                viewmodel.loading.setValue(true);
                viewmodel.myData.setValue(null);
                binding.screenConstraint.setVisibility(View.GONE);
                viewmodel.refreshData();
                binding.swipeRefresh.setRefreshing(false);
            }
        });

        observer();
    }

    private void observer(){
        viewmodel.loading.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    binding.screenConstraint.setVisibility(View.GONE);
                    binding.progressBar.setVisibility(View.VISIBLE);
                }else{
                    binding.screenConstraint.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.GONE);
                }
            }
        });

        viewmodel.myData.observe(getViewLifecycleOwner(), new Observer<List<RetrofitResponse>>() {
            @Override
            public void onChanged(List<RetrofitResponse> retrofitResponses) {
                if(retrofitResponses!=null){
                    binding.progressBar.setVisibility(View.GONE);
                    binding.screenConstraint.setVisibility(View.VISIBLE);
                    showData(retrofitResponses);
                }
            }
        });

        viewmodel.error.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    binding.screenConstraint.setVisibility(View.GONE);
                    binding.progressBar.setVisibility(View.GONE);
                    Snackbar.make(requireView(),R.string.error,Snackbar.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void showData(List<RetrofitResponse> retrofitResponses){
    electronicSetup(retrofitResponses);
       jewelerySetup(retrofitResponses);
    menSetup(retrofitResponses);
    womenSetup(retrofitResponses);
storeImageData(retrofitResponses);

    }

    private void storeImageData(List<RetrofitResponse> list){
        ArrayList<String> myImageList=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            myImageList.add(list.get(i).imageURL);
        }
        sliderAdapter=new SliderAdapter(myImageList);
        binding.sliderView.setSliderAdapter(sliderAdapter);
        binding.sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        binding.sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        binding.sliderView.startAutoCycle();

    }
    private void electronicSetup(List<RetrofitResponse> retrofitResponses){
        ArrayList<RetrofitResponse> myList = new ArrayList<>();

        for(int i=0;i<retrofitResponses.size();i++){
            if(retrofitResponses.get(i).category.equals(category[0])){
                myList.add(retrofitResponses.get(i));
            }
        }
        electronicAdapter=new AllCatAdapter(myList);
        binding.recyclerElectronics.setAdapter(electronicAdapter);
        binding.recyclerElectronics.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false));


    }

    private void jewelerySetup(List<RetrofitResponse> retrofitResponses){
        ArrayList<RetrofitResponse> myList = new ArrayList<>();

        for(int i=0;i<retrofitResponses.size();i++){
            if(retrofitResponses.get(i).category.equals(category[1])){
                myList.add(retrofitResponses.get(i));
            }
        }

        jeweleryAdapter=new AllCatAdapter(myList);
        binding.recyclerJewelery.setAdapter(jeweleryAdapter);
        binding.recyclerJewelery.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false));


    }
    private void menSetup(List<RetrofitResponse> retrofitResponses){
        ArrayList<RetrofitResponse> myList = new ArrayList<>();

        for(int i=0;i<retrofitResponses.size();i++){
            if(retrofitResponses.get(i).category.equals(category[2])){
                myList.add(retrofitResponses.get(i));
            }
        }

        menAdapter=new AllCatAdapter(myList);
        binding.recyclerMenCat.setAdapter(menAdapter);
        binding.recyclerMenCat.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false));


    }
    private void womenSetup(List<RetrofitResponse> retrofitResponses){
        ArrayList<RetrofitResponse> myList = new ArrayList<>();
        for(int i=0;i<retrofitResponses.size();i++){
            if(retrofitResponses.get(i).category.equals(category[3])){
                myList.add(retrofitResponses.get(i));
            }
        }

        womenAdapter=new AllCatAdapter(myList);
        binding.recyclerWomenCat.setAdapter(womenAdapter);
        binding.recyclerWomenCat.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false));


    }



}

